/**
 *
 */
package fr.ujm.tse.lt2c.satin.simhash.test.knn.hashes;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.storage.HashStore;
import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.topk.TopK;
import fr.ujm.tse.lt2c.satin.simhash.knn.db.hashes.SimpleInMemoryHashDB;

/**
 * @author Julien
 *
 */
public class HashDBTestGuavaTopKTest3Long {
	SimpleInMemoryHashDB db;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// One long => 64 bits hash
		db = new SimpleInMemoryHashDB(3, HashStore.PRIMITIVE,
				TopK.GUAVA_MIN_MAX_QUEUE);
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
			final long[] val = new long[3];
			val[0] = i;
			val[1] = i;
			val[2] = i;
			db.putDocument(val, "" + i);
		}
		// Get top-5 from 0
		final long[] val = new long[3];
		val[0] = 0;
		val[1] = 0;
		val[2] = 0;
		final String[] ids = db.kNNSorted(val, 5);
		final String[] expected = { "0", "1", "2", "4", "8" };
		System.out.println(Arrays.toString(ids));
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
