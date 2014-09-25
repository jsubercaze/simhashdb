package fr.ujm.tse.lt2c.satin.simhash.knn;

import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Ordering;
import com.google.common.hash.HashFunction;

import fr.ujm.tse.lt2c.satin.simhash.core.hashing.SimHash;

/**
 * @author Julien Subercaze
 *
 *
 *         API to store and retrieves nearest neighbor for a given Hash, this
 *         class uses an in-memory backed by an array list
 */
public class InMemorySimHashDB {

	/**
	 * Default size of the internal {@link ArrayList}
	 */
	protected static final int DEFAULT_SIZE = 2 >> 12;

	/**
	 * Internal Storage
	 */
	ArrayList<long[]> hashes;
	/**
	 * SimHasher
	 */
	protected final SimHash simhash;

	/**
	 * Charset for String encoding
	 */
	protected final Charset charset;
	/**
	 * Description of the DB; optional
	 */
	protected String description = "Simhash DB";

	protected InMemorySimHashDB() {
		hashes = new ArrayList<>(DEFAULT_SIZE);
		simhash = null;
		charset = null;
	}

	protected InMemorySimHashDB(final SimHash simHash) {
		this(simHash, Charset.defaultCharset());

	}

	protected InMemorySimHashDB(final HashFunction hashFunction) {
		this(new SimHash(hashFunction), Charset.defaultCharset());
	}

	protected InMemorySimHashDB(final HashFunction hashFunction,
			final Charset charset) {
		this(new SimHash(hashFunction), charset);
	}

	/**
	 *
	 * @param simHash
	 *            Instance of {@link SimHash}, used to hash document
	 * @param charset
	 *            String encoding {@link Charset}
	 * */
	public InMemorySimHashDB(final SimHash simHash, final Charset charset) {
		this(simHash, charset, DEFAULT_SIZE);
	}

	/**
	 *
	 * @param simHash
	 *            Instance of {@link SimHash}, used to hash document
	 * @param charset
	 *            String encoding {@link Charset}
	 * @param expected
	 *            Expected number of documents in this DB
	 * */
	public InMemorySimHashDB(final SimHash simHash, final Charset charset,
			final int expected) {
		simhash = simHash;
		this.charset = charset;
		hashes = new ArrayList<>(expected);
	}

	/**
	 * Load a {@link InMemorySimHashDB} from hashes stored on the disk
	 *
	 * @param hashes
	 * @param function
	 * @return
	 */
	public static InMemorySimHashDB loadFromHashes(final File hashes,
			final HashFunction function) {
		final InMemorySimHashDB db = new InMemorySimHashDB(function);
		final long size = hashes.length() / (function.bits() << 3);
		final int arraySize = function.bits() >> 6;
		// Read into a list of long arrays
		final ArrayList<long[]> hashes_ = new ArrayList<>((int) size);
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile(hashes, "r");

			final FileChannel fc = file.getChannel();
			final ByteBuffer buf = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					hashes.length());
			// Read the data
			long[] tab = null;
			int tabIndex = 0;
			for (int i = 0; i < hashes.length() / 8; i++) {
				if (i % arraySize == 0) {
					if (tab != null) {
						hashes_.add(tab);
					}

					tab = new long[arraySize];
					tabIndex = 0;
				}
				tab[tabIndex++] = buf.getLong();
			}
			if (tab != null) {
				hashes_.add(tab);
			}
			file.close();
			db.hashes = hashes_;
			return db;
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (final IOException e) {

				}
			}
		}

	}

	/**
	 * Serialize the DB to disk
	 *
	 * @param stream
	 */
	public boolean saveHashes(final File f) {
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile(f, "rw");

			final FileChannel fc = file.getChannel();
			final ByteBuffer buf = fc.map(FileChannel.MapMode.READ_WRITE, 0, 8
					* hashes.size() * hashes.get(0).length);
			for (final long[] ls : hashes) {
				for (final long l : ls) {
					buf.putLong(l);
				}
			}
			file.close();
		} catch (final IOException e) {
			return false;
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (final IOException e) {

				}
			}
		}
		return true;

	}

	/**
	 * Add a document to the database, do not take care of duplicates
	 *
	 * @param document
	 *
	 * @return the document ID in the database
	 */
	protected int putDocument(final Map<String, Double> document) {
		if (document == null || document.size() == 0) {
			return -1;
		}
		hashes.add(simhash.hash(document, charset).toLongArray());
		return hashes.size() - 1;
	}

	// /**
	// * This method is only allowed when no hashing method has been set to
	// avoid
	// * at maximum inconsistent hashes in the DB. User has still the
	// possibility
	// * to use various hash functions prior to this call, however this process
	// * should be more intentional than mixed use of this method and other
	// * putDocument methods.
	// *
	// * @param hash
	// * add directly a hash into the DB.
	// * @return
	// */
	// public int putDocument(final long[] hash) {
	// if (hash == null || hash.length == 0) {
	// return -1;
	// }
	// hashes.add(hash);
	// return hashes.size() - 1;
	// }

	/**
	 *
	 * @return the size of the document collection added to this DB
	 */
	public int size() {
		return hashes.size();
	}

	/**
	 * The number of document is not known in advance, to retrieve a bounded
	 * number, use the standard knn search :
	 *
	 * @param document
	 * @param radius
	 * @return all the document IDs that are within the hamming ball of
	 *         <code>radius</code> of the given <code>document</code>
	 */
	protected Collection<Integer> hammingBallNeighbors(
			final Map<String, Double> document, final int radius) {
		return hammingBallNeighbors(simhash.hash(document, charset)
				.toLongArray(), radius);
	}

	protected Collection<Integer> hammingBallNeighbors(final long[] hash,
			final int radius) {
		// FIXME
		return null;
	}

	protected int[] kNearestNeighbors(final Map<String, Double> document,
			final int k) {
		final Long[] res = mainkNearestNeighbors(simhash
				.hash(document, charset).toLongArray(), k);
		final int[] result = new int[k];
		int j = 0;
		for (final Long l : res) {
			result[j++] = (int) (l & 0xFFFFFFFF);
		}
		return result;
	}

	/**
	 * Returns
	 *
	 * @param longArray
	 * @param k
	 *            number of nearest neighbors
	 * @return list of nearest neighbors ID
	 */
	private Long[] mainkNearestNeighbors(final long[] query, final int k) {
		// Primitive iterable storage to use with Guava Ordering, avoid Object
		// overhead of Long
		final LongArrayList distances = new LongArrayList(hashes.size());
		int i = 0;
		for (final long[] hash : hashes) {
			long d = distance(query, hash);
			// System.out.println("Distance with " + i + " " + d);
			d = d << 32;
			d += i;
			distances.add(d);
			i++;
		}

		final List<Long> r = Ordering.natural().reverse()
				.greatestOf(distances, k);
		return r.toArray(new Long[r.size()]);
		// final int[] result = new int[k];
		// int j = 0;
		// for (final Long l : r) {
		// result[j++] = (int) (l & 0xFFFFFFFF);
		// }
		// return result;
	}

	protected long[] getHash(final int documentID) {
		if (documentID >= hashes.size()) {
			return null;
		}
		return hashes.get(documentID);
	}

	/**
	 * Detailed results of the execution query
	 *
	 * @param query
	 * @param k
	 * @return
	 */
	protected KnnQueryResult kNearestNeighbors(final long[] query, final int k) {
		final long t1 = System.nanoTime();
		final Long[] res = mainkNearestNeighbors(query, k);
		final long time = System.nanoTime() - t1;
		final int[] distances = new int[k];
		final int[] documents = new int[k];
		final int j = 0;
		for (final Long l : res) {
			documents[j] = (int) (l & 0xFFFFFFFF);
			distances[j] = (int) (l & 0xFFFFFFFF << 32);
		}
		return new KnnQueryResult(documents, distances, time);
	}

	/**
	 *
	 * @param documentID
	 *            the id of the document in the database
	 * @param k
	 *            number of nearest neighbors (do not include self)
	 * @return list of nearest neighbors ID
	 */
	protected int[] kNearestNeighbors(final int documentID, final int k) {
		if (documentID > hashes.size() || documentID < 0) {
			throw new RuntimeException("Invalid Document ID: " + documentID
					+ " either <0 or larger than the DB size:" + size());
		}
		// Remove the first one, since it is the same document
		final Long[] res = mainkNearestNeighbors(hashes.get(documentID), k + 1);
		final int[] result = new int[k + 1];
		int j = 0;
		for (final Long l : res) {
			result[j++] = (int) (l & 0xFFFFFFFF);
		}
		final int[] restrim = new int[k];
		System.arraycopy(result, 1, restrim, 0, k);
		return restrim;
	}

	public int distance(final long[] hash1, final long[] hash2) {
		int distance = 0;
		for (int i = 0; i < hash1.length; i++) {
			distance += Long.bitCount(hash1[i] ^ hash2[i]);
		}
		return distance;
	}

	/**
	 * @return the description
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * @return the description
	 */
	public final void setDescription(final String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SimHashKNNDB [hashes=" + hashes.size() + ", simhash=" + simhash
				+ ", charset=" + charset + ", description=" + description + "]";
	}

	/**
	 *
	 * @return a {@link String} representing the hashes as binary strings
	 */
	public String viewDB() {
		final StringBuilder sb = new StringBuilder();
		sb.append("[\n");
		for (final long[] ls : hashes) {
			for (final long l : ls) {
				sb.append(Long.toBinaryString(l));
			}
			sb.append("\n");
		}
		sb.append("]");
		return sb.toString();
	}
}
