package fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.storage;

import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.LongDistanceDocumentIterator;
import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.MyLongArrayList;

/**
 * Stores hashes into a single contiguous list. Should overcome the issue of
 *
 * @author Julien
 *
 */
public class PrimitiveArrayListStorage extends HashStorage {
	/**
	 * Size of hash, number of longs
	 */
	private final int longPerHash;
	/**
	 * Backing arraylist
	 */
	private final MyLongArrayList arraylist;

	public PrimitiveArrayListStorage(final int longPerHash) {
		this(longPerHash, 2 << 20);
	}

	public PrimitiveArrayListStorage(final int longPerHash,
			final int initialCapacity) {
		super();
		this.longPerHash = longPerHash;
		arraylist = new MyLongArrayList(initialCapacity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.HashStorage#addHash(long
	 * [])
	 */
	@Override
	public void addHash(final long[] hash) {
		for (final long part : hash) {
			arraylist.add(part);
		}

	}

	@Override
	public LongDistanceDocumentIterator getDistanceIterator(final long[] query) {

		return new MyLongDistanceDocumentIterator(query, 0, arraylist.size());
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
		long[] query;

		int counter;
		/**
		 * Where to stop iterating
		 */
		int end;

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
			for (int i = 0; i < longPerHash; i++) {
				distance += Long.bitCount(arraylist.getQuick(counter + i)
						^ query[i]);
			}

			// Store the document ID into the long
			// distance = distance << 32;
			// distance += counter;
			// Go on
			counter += longPerHash;
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
			return end / longPerHash;
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
		return arraylist.size() / longPerHash;
	}

}
