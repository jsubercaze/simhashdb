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
public abstract class AbstractTopK implements TopKI {
	/**
	 * Number of results of the query
	 */
	final int k;
	/**
	 * Pseudo-Iterator returning distances
	 */
	final LongDistanceDocumentIterator iterator;
	/**
	 * Younger or older elements have priority in case of distance equality
	 */
	final FreshnessMode freshness;
	/**
	 * First value of the counter. May be overriden for multi-threading purpose
	 */
	protected int start;

	public AbstractTopK(final int k,
			final LongDistanceDocumentIterator iterator,
			final FreshnessMode freshness) {

		this.k = k;
		this.iterator = iterator;
		this.freshness = freshness;

	}

	/**
	 * Put distance and index in a single long. Shift distance left by 32 then
	 * add the index depending on the freshness strategy. If older first, add as
	 * it is. If younger first add two's complement.
	 *
	 * @param d
	 * @param index
	 * @return
	 */
	protected final long setDistance(long d, final int index) {
		// move distance to MSD
		d = d << 32;
		// adds docid to LSD
		final int temp = freshness.offset ^ index;
		d += temp;
		return d;
	}

	/**
	 * @param start
	 *            the start to set
	 */

	@Override
	public final void setStart(final int start) {
		this.start = start;
	}

}
