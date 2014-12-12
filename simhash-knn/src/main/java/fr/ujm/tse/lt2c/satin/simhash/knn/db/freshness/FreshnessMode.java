/**
 *
 */
package fr.ujm.tse.lt2c.satin.simhash.knn.db.freshness;

/**
 * @author Julien
 *
 */
public enum FreshnessMode {
	OLDERFIRST(0), YOUNGERFIRST(0xFFFFFFFF);

	public final int offset;

	private FreshnessMode(final int offset) {
		this.offset = offset;
	}

}
