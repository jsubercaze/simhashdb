/**
 *
 */
package fr.ujm.tse.lt2c.satin.simhash.test.knn.hashes.performance;

import java.io.IOException;
import java.util.Random;

import fr.ujm.tse.lt2c.satin.simhash.knn.db.hashes.MultiThreadedInMemoryHashDB;

/**
 * @author Julien
 * 
 */
public class TopKPerformanceMulti {

	private static final int CHUNK = 1_000_000;

	public static void main(final String[] args) throws IOException {
		final MultiThreadedInMemoryHashDB db = new MultiThreadedInMemoryHashDB(
				TopKPerformance.HASHSIZE, TopKPerformance.STORE,
				TopKPerformance.TOPK, TopKPerformance.MODE);
		db.setMin_chunk_size(CHUNK);
		// Store values
		for (int i = 0; i < TopKPerformance.HASHES; i++) {
			final long[] hash = new long[TopKPerformance.HASHSIZE];
			hash[0] = i;
			db.putDocument(hash, "" + i);
		}

		// System.out.println(db.size());
		// System.out.println("VisualVM");
		// System.in.read();
		final Random random = new Random();
		long time = 0;
		for (int i = 0; i < TopKPerformance.ITERATIONS; i++) {
			final long[] query = new long[TopKPerformance.HASHSIZE];
			query[0] = random.nextLong();
			final long t1 = System.nanoTime();
			db.kNNUnSorted(query, TopKPerformance.K);
			time += System.nanoTime() - t1;
		}
		time /= TopKPerformance.ITERATIONS;
		System.out.println(time / 1000000 + " ms per query");

	}
}
