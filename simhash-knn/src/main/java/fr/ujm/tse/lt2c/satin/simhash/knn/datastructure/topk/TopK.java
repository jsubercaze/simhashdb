/**
 *
 */
package fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.topk;

import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.LongDistanceDocumentIterator;
import fr.ujm.tse.lt2c.satin.simhash.knn.db.freshness.FreshnessMode;

/**
 * @author Julien
 *
 */
public enum TopK {
	GUAVA_MIN_MAX_QUEUE, ARRAYLIST, ARRAYLIST_MAX_CUTOFF;

	public static TopKI newTopK(final TopK topk,
			final LongDistanceDocumentIterator iterator, final int k,
			final FreshnessMode mode) {
		switch (topk) {
		case ARRAYLIST:
			return new ArrayListTopK(k, iterator, mode);
		case GUAVA_MIN_MAX_QUEUE:
			return new GuavaMinMaxTopK(k, iterator, mode);
		case ARRAYLIST_MAX_CUTOFF:
			return new ArrayListSimpleCutOffTopK(k, iterator, mode);
		default:
			throw new UnsupportedOperationException();
		}

	}
}
