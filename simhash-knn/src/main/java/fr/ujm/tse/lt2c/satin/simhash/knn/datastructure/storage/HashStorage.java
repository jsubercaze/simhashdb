package fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.storage;

import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.LongDistanceDocumentIterator;

/**
 * Storage interface for hashes
 *
 * @author Julien
 *
 */
public abstract class HashStorage {

	/**
	 * Add the given hash to the storage
	 *
	 * @param hash
	 */
	public abstract void addHash(long[] hash);

	/**
	 *
	 * @param query
	 * @return an iterator returning distances for the given query on the full
	 *         database
	 */
	public abstract LongDistanceDocumentIterator getDistanceIterator(
			long[] query);

	/**
	 *
	 * @param query
	 * @param start
	 *            inclusive
	 * @param end
	 *            exclusive
	 * @return an iterator returning distances for the given query on a fixed
	 *         range of the DB
	 */
	public abstract LongDistanceDocumentIterator getDistanceIterator(
			long[] query, int start, int end);

	/**
	 *
	 * @return the number of hashes stored
	 */
	public abstract int size();
}
