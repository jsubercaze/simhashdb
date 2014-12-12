/**
 *
 */
package fr.ujm.tse.lt2c.satin.simhash.test.knn.hashes.multithread;

import static org.junit.Assert.assertArrayEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.storage.HashStore;
import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.topk.TopK;
import fr.ujm.tse.lt2c.satin.simhash.knn.db.freshness.FreshnessMode;
import fr.ujm.tse.lt2c.satin.simhash.knn.db.hashes.MultiThreadedInMemoryHashDB;

/**
 * @author Julien
 *
 */
public class HashDBMultiThreadedTest {
	MultiThreadedInMemoryHashDB db;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// One long => 64 bits hash
		db = new MultiThreadedInMemoryHashDB(1, HashStore.PRIMITIVE,
				TopK.ARRAYLIST, FreshnessMode.OLDERFIRST);
		db.setMin_chunk_size(10);
		db.setMultithread_threshold(20);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link fr.ujm.tse.lt2c.satin.simhash.knn.db.hashes.SimpleInMemoryHashDB#kNNSorted(long[], int)}
	 * .
	 */
	@Test
	public void testKNN() {
		// Adds all integers from 0 to 10
		for (int i = 0; i <= 128; i++) {
			final long[] val = new long[1];
			val[0] = i;
			db.putDocument(val, "" + i);
		}
		// Get top-5 from 0
		final long[] val = new long[1];
		val[0] = 0;
		final String[] ids = db.kNNSorted(val, 5);
		final String[] expected = { "0", "1", "2", "4", "8" };
		assertArrayEquals(expected, ids);
		// Get top-5 from 0
		// val = new long[1];
		// val[0] = 0;
		// ids = db.kNN(val, 9);
		//
		// final String[] expected2 = { "0", "1", "2", "4", "8", "16", "32",
		// "64",
		// "128" };
		// assertArrayEquals(expected2, ids);
	}
}
