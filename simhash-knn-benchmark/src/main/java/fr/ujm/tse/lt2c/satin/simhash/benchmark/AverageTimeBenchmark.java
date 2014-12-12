package fr.ujm.tse.lt2c.satin.simhash.benchmark;

import java.util.Random;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.storage.HashStore;
import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.topk.TopK;
import fr.ujm.tse.lt2c.satin.simhash.knn.db.freshness.FreshnessMode;
import fr.ujm.tse.lt2c.satin.simhash.knn.db.hashes.SimpleInMemoryHashDB;

/**
 * @author Julien
 * 
 */

@State(Scope.Thread)
@Fork(1)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5)
public class AverageTimeBenchmark {

	@Param({ "2" })
	public int hashsize;
	@Param({ "5", "10", "50", "100", "200", "300", "400", "500" })
	public int k;
	@Param({ "1000", "10000", "25000", "50000", "100000", "200000", "300000",
			"400000", "500000", "600000", "700000", "800000", "900000",
			"1000000" })
	public int items;

	public int queries = 10;

	SimpleInMemoryHashDB dbArray;

	SimpleInMemoryHashDB dbGuava;

	@Setup
	public void setup() {
		dbArray = new SimpleInMemoryHashDB(hashsize, HashStore.PRIMITIVE,
				TopK.ARRAYLIST, FreshnessMode.OLDERFIRST);
		dbGuava = new SimpleInMemoryHashDB(hashsize, HashStore.PRIMITIVE,
				TopK.GUAVA_MIN_MAX_QUEUE, FreshnessMode.OLDERFIRST);
		// Store values
		for (int i = 0; i < items; i++) {
			final long[] hash = new long[hashsize];
			hash[0] = i;
			dbArray.putDocument(hash, "" + i);
			dbGuava.putDocument(hash, "" + i);
		}
	}

	@Benchmark
	public void benchArrayList() {
		final Random random = new Random();
		for (int i = 0; i < queries; i++) {
			final long[] query = new long[hashsize];
			query[0] = random.nextLong();
			dbArray.kNNUnSorted(query, k);
		}
	}

	@Benchmark
	public void benchGuava() {
		final Random random = new Random();
		for (int i = 0; i < queries; i++) {
			final long[] query = new long[hashsize];
			query[0] = random.nextLong();
			dbGuava.kNNUnSorted(query, k);
		}
	}
}
