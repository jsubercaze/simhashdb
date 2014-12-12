/**
 *
 */
package fr.ujm.tse.lt2c.satin.simhash.knn.db.hashes;

import java.util.ArrayList;

import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.LongDistanceDocumentIterator;
import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.storage.HashStorage;
import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.storage.HashStore;
import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.topk.TopK;
import fr.ujm.tse.lt2c.satin.simhash.knn.db.freshness.FreshnessMode;

/**
 * Performance lie in two parameters : storage and top-k
 *
 *
 * @author Julien
 *
 */
public class SimpleInMemoryHashDB implements HashDBI {
	private static final int INITIAL_CAPACITY = 4096;
	/**
	 * Keeps documentIDS
	 */
	final ArrayList<String> documentIDs;
	/**
	 * Store hashes, index corresponds with documentIDs
	 */
	final HashStorage storage;
	/**
	 * Datastructure used to compute top-k queries.
	 */
	TopK topk;
	/**
	 * Younger or older elements have priority
	 */
	FreshnessMode mode = FreshnessMode.OLDERFIRST;

	public SimpleInMemoryHashDB(final int hashsize) {
		documentIDs = new ArrayList<>(INITIAL_CAPACITY);
		// // Default topk is guava's
		topk = TopK.GUAVA_MIN_MAX_QUEUE;
		// Default storage is primitive, way faster
		storage = HashStore.createStorage(HashStore.PRIMITIVE,
				INITIAL_CAPACITY, hashsize);
	}

	public SimpleInMemoryHashDB(final int hashsize, final HashStore store) {
		documentIDs = new ArrayList<>(INITIAL_CAPACITY);
		// Default topk is guava's
		topk = TopK.GUAVA_MIN_MAX_QUEUE;
		// Default storage is primitive, way faster
		storage = HashStore.createStorage(store, INITIAL_CAPACITY, hashsize);

	}

	public SimpleInMemoryHashDB(final int hashsize, final HashStore store,
			final TopK topk) {
		this(hashsize, store);
		this.topk = topk;
	}

	public SimpleInMemoryHashDB(final int hashsize, final HashStore store,
			final TopK topk, final FreshnessMode mode) {
		this(hashsize, store, topk);
		this.mode = mode;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.ujm.tse.lt2c.satin.simhash.knn.db.hashes.HashDBI#putDocument(long[],
	 * java.lang.String)
	 */
	@Override
	public void putDocument(final long[] hash, final String documentID) {
		storage.addHash(hash);
		documentIDs.add(documentID);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.ujm.tse.lt2c.satin.simhash.knn.db.hashes.HashDBI#kNN(long[], int)
	 */
	@Override
	public String[] kNNSorted(final long[] query, final int k) {
		final LongDistanceDocumentIterator iterator = storage
				.getDistanceIterator(query);
		final long[] distAndDocs = TopK.newTopK(topk, iterator, k, mode)
				.getTopKSorted();
		return convertDistAndDocsToDocIDS(distAndDocs);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.ujm.tse.lt2c.satin.simhash.knn.db.hashes.HashDBI#kNNUnSorted(long[],
	 * int)
	 */
	@Override
	public String[] kNNUnSorted(final long[] query, final int k) {
		final LongDistanceDocumentIterator iterator = storage
				.getDistanceIterator(query);
		final long[] distAndDocs = TopK.newTopK(topk, iterator, k, mode)
				.getTopKUnSorted();
		return convertDistAndDocsToDocIDS(distAndDocs);
	}

	/**
	 * @param distAndDocs
	 * @return
	 */
	protected String[] convertDistAndDocsToDocIDS(final long[] distAndDocs) {
		final String[] ids = new String[distAndDocs.length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = documentIDs.get((int) (distAndDocs[i] ^ mode.offset));
		}
		return ids;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.ujm.tse.lt2c.satin.simhash.knn.db.hashes.HashDBI#epsilonNN(long[],
	 * int)
	 */
	@Override
	public String[] epsilonNN(final long[] query, final int epsilon) {
		throw new UnsupportedOperationException("soon");
	}

	/**
	 * @return the topk
	 */
	public TopK getTopk() {
		return topk;
	}

	/**
	 * @param topk
	 *            the topk to set
	 */
	public void setTopk(final TopK topk) {
		this.topk = topk;
	}

	/**
	 * @return the mode
	 */
	public FreshnessMode getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(final FreshnessMode mode) {
		this.mode = mode;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.ujm.tse.lt2c.satin.simhash.knn.db.hashes.HashDBI#size()
	 */
	@Override
	public int size() {
		return storage.size();
	}

}
