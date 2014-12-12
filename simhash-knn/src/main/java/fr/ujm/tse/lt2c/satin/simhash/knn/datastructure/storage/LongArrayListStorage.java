/**
 *
 */
package fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.storage;

import java.util.ArrayList;

import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.LongDistanceDocumentIterator;

/**
 * Store hashes as a single long array. Favors sequential access, reduce
 * overhead of objects.
 *
 * @author Julien
 *
 */
public class LongArrayListStorage extends HashStorage {

	ArrayList<long[]> arrayList;

	public LongArrayListStorage() {
		arrayList = new ArrayList<>(2 << 20);
	}

	/**
	 * @param initialCapacity
	 */
	public LongArrayListStorage(final int initialCapacity) {
		arrayList = new ArrayList<>(initialCapacity);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.storage.HashStorage#addHash
	 * (long[])
	 */
	@Override
	public void addHash(final long[] hash) {
		arrayList.add(hash);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.storage.HashStorage#
	 * getDistanceIterator(long[])
	 */
	@Override
	public LongDistanceDocumentIterator getDistanceIterator(final long[] query) {
		return new MyLongDistanceDocumentIterator(query, 0, arrayList.size());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.storage.HashStorage#
	 * getDistanceIterator(long[], int, int)
	 */
	@Override
	public LongDistanceDocumentIterator getDistanceIterator(final long[] query,
			final int start, final int end) {
		return new MyLongDistanceDocumentIterator(query, start, end - 1);
	}

	class MyLongDistanceDocumentIterator implements
	LongDistanceDocumentIterator {
		/**
		 * Hash to compare distance with
		 */
		final long[] query;

		int counter;
		/**
		 * Where to stop iterating
		 */
		final int end;

		final int startIndex;

		public MyLongDistanceDocumentIterator(final long[] query,
				final int counter, final int end) {
			super();
			this.query = query;
			this.counter = counter;
			this.end = end;
			startIndex = counter;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.
		 * LongDistanceDocumentIterator#getNextLong()
		 */
		@Override
		public long getNextLong() {
			if (counter == end) {
				return -1;
			}
			// Compute the distance
			int distance = 0;
			final long[] hash = arrayList.get(counter);
			int i = 0;
			for (final long element : query) {
				distance += Long.bitCount(hash[i] ^ element);
				i++;
			}
			// Go on
			counter++;
			return distance;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.
		 * LongDistanceDocumentIterator#elements()
		 */
		@Override
		public int elements() {
			return end;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.
		 * LongDistanceDocumentIterator#getStartIndex()
		 */
		@Override
		public int getStartIndex() {
			return startIndex;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.storage.HashStorage#size
	 * ()
	 */
	@Override
	public int size() {
		return arrayList.size();
	}
}
