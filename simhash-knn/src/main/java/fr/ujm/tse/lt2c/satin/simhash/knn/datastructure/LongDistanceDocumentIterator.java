/**
 *
 */
package fr.ujm.tse.lt2c.satin.simhash.knn.datastructure;

/**
 * Homemade quick'n'dirty primitive iterator to save useless boxing operations
 *
 * @author Julien
 *
 */
public interface LongDistanceDocumentIterator {

	/**
	 *
	 * @return a {@link Long} containing the distance on its 32 most significant
	 *         digits and the document id on its 32 last. <br>
	 *         -1 when no more data
	 */
	public long getNextLong();

	/**
	 * Useful hint for sampling
	 *
	 * @return the total number of elements
	 */
	public int elements();

	/**
	 *
	 * @return the index of the first document in the global DB
	 */
	public int getStartIndex();
}
