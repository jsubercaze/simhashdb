/**
 *
 */
package fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.storage;

/**
 * @author Julien
 *
 */
public enum HashStore {
	PRIMITIVE, LONGARRAYS;

	public static HashStorage createStorage(final HashStore store,
			final int initialCapacity, final int hashsize) {
		switch (store) {
		case LONGARRAYS:
			return new LongArrayListStorage(initialCapacity);

		case PRIMITIVE:
			return new PrimitiveArrayListStorage(hashsize, initialCapacity);
		default:
			throw new UnsupportedOperationException("Unknown Storage");
		}

	}

	public static HashStorage createStorage(final HashStore store,
			final int hashsize) {
		switch (store) {
		case LONGARRAYS:
			return new LongArrayListStorage();
		case PRIMITIVE:
			return new PrimitiveArrayListStorage(hashsize);
		default:
			throw new UnsupportedOperationException("Unknown Storage");
		}

	}
}
