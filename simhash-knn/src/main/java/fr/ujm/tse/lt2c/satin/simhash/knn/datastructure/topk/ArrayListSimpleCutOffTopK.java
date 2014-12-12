package fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.topk;

import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.MinMaxPriorityQueue;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Longs;

import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.LongDistanceDocumentIterator;
import fr.ujm.tse.lt2c.satin.simhash.knn.db.freshness.FreshnessMode;

/**
 * Simple {@link ArrayList} based structure to store top-k values
 *
 * Not very satisfying theoretical bounds. Very easy to implement though.
 *
 * For <code>n</code> being the number of entries in the DB, this class exhibits
 * following complexity :
 * <ul>
 * <li>Worst case, memory: O(n), time: O(n log n)</li>
 * <li>Best case, memory O(k), time: O(k log k)</li>
 * </ul>
 *
 * In the practice it usually beats Guava {@link MinMaxPriorityQueue}. I guess
 * this is mostly due to the boxing operations required by Guava. We deal here
 * with plain primitives.
 *
 *
 *
 * @author Julien
 *
 */
public class ArrayListSimpleCutOffTopK extends AbstractTopK {

	public ArrayListSimpleCutOffTopK(final int k,
			final LongDistanceDocumentIterator iterator,
			final FreshnessMode mode) {
		super(k, iterator, mode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.topk.TopKI#getTopK(fr
	 * .ujm.
	 * tse.lt2c.satin.simhash.knn.datastructure.LongDistanceDocumentIterator,
	 * int)
	 */
	@Override
	public long[] getTopKUnSorted() {
		final LongArrayList distances = new LongArrayList();
		long d = 0;
		int i = start;
		int added = 0;
		long max = 0;
		while ((d = iterator.getNextLong()) != -1) {
			if (added > k) {
				if (d > max) {
					i++;
					continue;
				}
				distances.add(setDistance(d, i));
			} else {
				added++;
				if (d > max) {
					max = d;
				}
				distances.add(setDistance(d, i));
			}
			i++;
		}
		final List<Long> r = Ordering.natural().reverse()
				.greatestOf(distances, k);
		return Longs.toArray(r);
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
		return getTopKUnSorted();
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
		// Create a new array list and do the same
		final int k = topks[0].length;
		final Set<Long> distances = new HashSet<>(k * topks.length);
		for (final long[] topk : topks) {

			for (final long l : topk) {
				distances.add(l);
			}
		}
		final List<Long> r = Ordering.natural().reverse()
				.greatestOf(distances, k);
		return Longs.toArray(r);
	}

}
