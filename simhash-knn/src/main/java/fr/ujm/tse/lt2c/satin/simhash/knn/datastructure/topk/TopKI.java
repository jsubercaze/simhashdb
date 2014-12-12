package fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.topk;

import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.LongDistanceDocumentIterator;

/**
 * Takes an iterator of distances and returns the top-k value
 *
 *
 * @author Julien
 *
 */
public interface TopKI {
	/**
	 *
	 * @return the top-k distances (with document ID on 32 LSD), k was passed by
	 *         the constructor, and {@link LongDistanceDocumentIterator} as well
	 */
	public long[] getTopKUnSorted();

	/**
	 *
	 * @return the top-k distances (with document ID on 32 LSD), k was passed by
	 *         the constructor, and {@link LongDistanceDocumentIterator} as well
	 */
	public long[] getTopKSorted();

	/**
	 * For map/reduce or fork-join purpose, join multiple result
	 *
	 * @param topks
	 * @return result of the joins that is a join of the parameters
	 */
	public long[] joinTopKs(final long[]... topks);

	/**
	 * Set the index of the docID where the topk starts to compute distances
	 *
	 * @param startIndex
	 */
	public void setStart(int startIndex);
}
