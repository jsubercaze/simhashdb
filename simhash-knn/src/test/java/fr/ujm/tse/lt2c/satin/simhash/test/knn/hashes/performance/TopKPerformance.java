/**
 *
 */
package fr.ujm.tse.lt2c.satin.simhash.test.knn.hashes.performance;

import java.io.IOException;
import java.util.Random;

import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.storage.HashStore;
import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.topk.TopK;
import fr.ujm.tse.lt2c.satin.simhash.knn.db.freshness.FreshnessMode;
import fr.ujm.tse.lt2c.satin.simhash.knn.db.hashes.SimpleInMemoryHashDB;

/**
 * @author Julien
 * 
 */
public class TopKPerformance {
	// times 64 bits
	public final static int HASHSIZE = 2;
	//
	public final static int HASHES = 1_000_000;
	// Size of K
	public final static int K = 100;
	// Iterations of query
	public static final int ITERATIONS = 3;

	public final static HashStore STORE = HashStore.PRIMITIVE;
	public final static TopK TOPK = TopK.GUAVA_MIN_MAX_QUEUE;
	public final static FreshnessMode MODE = FreshnessMode.OLDERFIRST;

	public static void main(final String[] args) throws IOException {
		final SimpleInMemoryHashDB db = new SimpleInMemoryHashDB(HASHSIZE,
				STORE, TOPK, MODE);
		// Store values
		for (int i = 0; i < HASHES; i++) {
			final long[] hash = new long[HASHSIZE];
			hash[0] = i;
			db.putDocument(hash, "" + i);
		}

		System.out.println(db.size());
		System.out.println("VisualVM");
		System.in.read();
		final Random random = new Random();
		long time = 0;
		for (int i = 0; i < ITERATIONS; i++) {
			final long[] query = new long[HASHSIZE];
			query[0] = random.nextLong();
			final long t1 = System.nanoTime();
			db.kNNUnSorted(query, K);
			time += System.nanoTime() - t1;
		}
		time /= ITERATIONS;
		System.out.println(time / 1000000 + " ms per query");

	}
}
