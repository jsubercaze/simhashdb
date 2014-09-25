/**
 * 
 */
package fr.ujm.tse.lt2c.satin.simhash.knn;

/**
 * Result of a query
 * 
 * 
 * @author Julien Subercaze
 * 
 */
public class knnQueryResult {
	/**
	 * K nearest documents
	 */
	int[] documents;
	/**
	 * Distances for this document
	 */
	int[] distances;
	/**
	 * Query execution time in nanoseconds
	 */
	long time;

	public knnQueryResult(final int[] documents, final int[] distances,
			final long time) {
		super();
		this.documents = documents;
		this.distances = distances;
		this.time = time;
	}

	/**
	 * @return the documents
	 */
	public final int[] getDocuments() {
		return documents;
	}

	/**
	 * @return the distances
	 */
	public final int[] getDistances() {
		return distances;
	}

	/**
	 * @return the time
	 */
	public final long getTime() {
		return time;
	}

	public int ithDocument(final int i) {
		return documents[i];
	}

	public int ithDistance(final int i) {
		return distances[i];
	}

}
