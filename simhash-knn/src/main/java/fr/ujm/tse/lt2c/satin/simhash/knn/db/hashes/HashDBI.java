package fr.ujm.tse.lt2c.satin.simhash.knn.db.hashes;

/**
 * Support hash retrieval only, no simhash layer, us instead.
 *
 *
 * @author Julien
 *
 */
public interface HashDBI {
	/**
	 * Put a new document in the database
	 *
	 * @param hash
	 *            of the document
	 * @param documentID
	 *            its ID
	 */
	public void putDocument(long[] hash, String documentID);

	/**
	 * Returns the k nearest neighbour for the given query. In case of equality,
	 * document added first have priority over recent ones. Values are sorted.
	 *
	 * @param query
	 * @param k
	 * @return
	 */
	public String[] kNNSorted(long[] query, int k);

	/**
	 * Returns the k nearest neighbour for the given query. In case of equality,
	 * document added first have priority over recent ones. Values are unsorted.
	 *
	 * @param query
	 * @param k
	 * @return
	 */
	public String[] kNNUnSorted(long[] query, int k);

	/**
	 * WARNING : may return huge quantities of document if epsilon is too large.
	 *
	 * @param query
	 *            hash
	 * @param epsilon
	 *            hamming ball radius size
	 * @return all the documentID within the Hamming ball of radius epsilon
	 */
	public String[] epsilonNN(long[] query, int epsilon);

	/**
	 *
	 * @return the number of elements in the DB
	 */
	public int size();

}
