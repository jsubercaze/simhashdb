package fr.ujm.tse.lt2c.satin.simhash.knn;

import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Ordering;
import com.google.common.hash.HashFunction;

import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;
import fr.ujm.tse.lt2c.satin.simhash.core.hashing.SimHash;

/**
 * @author Julien Subercaze
 * 
 * 
 *         API to store and retrieves nearest neighbor for a given Hash, this
 *         class uses an in-memory backed by an array list
 */
public class SimHashKNNDB {
	/**
	 * Default size of the internal {@link ArrayList}
	 */
	private static final int DEFAULT_SIZE = 2 >> 12;

	/**
	 * Internal Storage
	 */
	ArrayList<long[]> hashes;
	/**
	 * SimHasher
	 */
	private final SimHash simhash;

	/**
	 * Charset for String encoding
	 */
	private final Charset charset;
	/**
	 * Convert document
	 */
	private ConversionMethod textToDocument;

	public SimHashKNNDB() {
		hashes = new ArrayList<>(50);
		simhash = null;
		charset = null;
	}

	public SimHashKNNDB(final SimHash simHash) {
		this(simHash, Charset.defaultCharset());

	}

	public SimHashKNNDB(final SimHash simHash, final ConversionMethod method) {
		this(simHash, Charset.defaultCharset());
		textToDocument = method;

	}

	public SimHashKNNDB(final HashFunction hashFunction) {
		this(new SimHash(hashFunction), Charset.defaultCharset());
	}

	public SimHashKNNDB(final HashFunction hashFunction,
			final ConversionMethod method) {
		this(new SimHash(hashFunction), Charset.defaultCharset());
		textToDocument = method;
	}

	public SimHashKNNDB(final HashFunction hashFunction, final Charset charset) {
		this(new SimHash(hashFunction), charset);
	}

	public SimHashKNNDB(final HashFunction hashFunction, final Charset charset,
			final ConversionMethod method) {
		this(new SimHash(hashFunction), charset);
		textToDocument = method;
	}

	/**
	 * 
	 * @param simHash
	 *            Instance of {@link SimHash}, used to hash document
	 * @param charset
	 *            String encoding {@link Charset}
	 * */
	public SimHashKNNDB(final SimHash simHash, final Charset charset) {
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
	public SimHashKNNDB(final SimHash simHash, final Charset charset,
			final int expected) {
		simhash = simHash;
		this.charset = charset;
		hashes = new ArrayList<>(expected);
	}

	/**
	 * Add a document to the database, do not take care of duplicates
	 * 
	 * @param document
	 * 
	 * @return the document ID in the database
	 */
	public int putDocument(final Map<String, Double> document) {
		if (document == null || document.size() == 0) {
			return -1;
		}
		hashes.add(simhash.hash(document, charset).toLongArray());
		return hashes.size() - 1;
	}

	public int putDocument(final String text) {
		if (text == null || text.length() == 0) {
			return -1;
		}
		final Map<String, Double> document = textToDocument.convert(text);
		return putDocument(document);
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
	public Collection<Integer> hammingBallNeighbors(
			final Map<String, Double> document, final int radius) {
		return hammingBallNeighbors(simhash.hash(document, charset)
				.toLongArray(), radius);
	}

	public Collection<Integer> hammingBallNeighbors(final long[] hash,
			final int radius) {

		return null;
	}

	public int[] kNearestNeighbors(final Map<String, Double> document,
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

	public long[] getHash(final int documentID) {
		if (documentID >= hashes.size()) {
			return null;
		}
		return hashes.get(documentID);
	}

	public SimQueryResult kNearestNeighbors(final long[] query, final int k) {
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
		return new SimQueryResult(documents, distances, time);
	}

	/**
	 * 
	 * @param documentID
	 *            the id of the document in the database
	 * @param k
	 *            number of nearest neighbors (do not include self)
	 * @return list of nearest neighbors ID
	 */
	public int[] kNearestNeighbors(final int documentID, final int k) {
		if (documentID > hashes.size()) {
			return null;
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
	 * Serialize the DB to disk
	 * 
	 * @param stream
	 */
	public boolean save(final OutputStream stream) {
		final FSTObjectOutput out = new FSTObjectOutput(stream);
		try {
			out.writeObject(this);
		} catch (final IOException e) {
			return false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (final IOException e) {

					return false;
				}
			}
		}
		return true;

	}

	public static synchronized SimHashKNNDB buildFromStream(
			final InputStream stream) {
		FSTObjectInput in = null;
		try {
			in = new FSTObjectInput(stream);
			final SimHashKNNDB result = (SimHashKNNDB) in.readObject();
			return result;
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException e) {

				}
			}
		}

	}
}
