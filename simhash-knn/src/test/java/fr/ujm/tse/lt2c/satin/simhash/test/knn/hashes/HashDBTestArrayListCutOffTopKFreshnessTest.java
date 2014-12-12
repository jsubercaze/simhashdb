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
import fr.ujm.tse.lt2c.satin.simhash.knn.db.freshness.FreshnessMode;
import fr.ujm.tse.lt2c.satin.simhash.knn.db.hashes.SimpleInMemoryHashDB;

/**
 * @author Julien
 *
 */
public class HashDBTestArrayListCutOffTopKFreshnessTest {
	SimpleInMemoryHashDB db;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// One long => 64 bits hash
		db = new SimpleInMemoryHashDB(1, HashStore.PRIMITIVE,
				TopK.ARRAYLIST_MAX_CUTOFF, FreshnessMode.YOUNGERFIRST);
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
		for (int i = 0; i <= 32; i++) {
			final long[] val = new long[1];
			val[0] = i;
			db.putDocument(val, "" + i);
		}
		// Get top-5 from 0
		final long[] val = new long[1];
		val[0] = 0;
		final String[] ids = db.kNNSorted(val, 5);
		// 0 has distance 0
		// Others have distance 1, last added first
		final String[] expected = { "0", "32", "16", "8", "4" };
		System.out.println(Arrays.toString(ids));
		assertArrayEquals(expected, ids);
		// Get top-5 from 0

	}
}
