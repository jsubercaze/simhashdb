/**
 *
 */
package fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.topk;

import java.util.Arrays;

import com.google.common.collect.MinMaxPriorityQueue;
import com.google.common.primitives.Longs;

import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.LongDistanceDocumentIterator;
import fr.ujm.tse.lt2c.satin.simhash.knn.db.freshness.FreshnessMode;

/**
 * Uses the min-max heap from Guava to manage top-k results
 *
 *
 * @author Julien
 *
 */
public class GuavaMinMaxTopK extends AbstractTopK {

	public GuavaMinMaxTopK(final int k,
			final LongDistanceDocumentIterator iterator,
			final FreshnessMode mode) {
		super(k, iterator, mode);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.topk.TopKI#getTopK()
	 */
	@Override
	public long[] getTopKUnSorted() {
		final MinMaxPriorityQueue<Long> queue = MinMaxPriorityQueue
				.maximumSize(k).create();
		long d = 0;
		int i = 0;
		while ((d = iterator.getNextLong()) != -1) {
			queue.add(setDistance(d, i));
			i++;
		}
		return Longs.toArray(queue);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.topk.TopKI#getTopKSorted
	 * ()
	 */
	@Override
	public long[] getTopKSorted() {
		final long[] array = getTopKUnSorted();
		Arrays.sort(array);
		return array;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.topk.TopKI#joinTopKs(
	 * fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.topk.TopKI[])
	 */
	@Override
	public long[] joinTopKs(final long[]... topks) {
		final MinMaxPriorityQueue<Long> queue = MinMaxPriorityQueue
				.maximumSize(k).create();
		for (final long[] ls : topks) {
			for (final long dist : ls) {
				queue.add(dist);
			}
		}
		return Longs.toArray(queue);
	}

}
