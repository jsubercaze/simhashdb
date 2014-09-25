/**
 *
 */
package fr.ujm.tse.lt2c.satin.simhash.knn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import com.google.common.hash.HashFunction;

/**
 * Same as {@link InMemorySimHashDB} but allows to store / retrieve the document
 * ID as String.
 *
 *
 * @author Julien
 *
 */
public class InMemorySimHashDBwithDocIDs extends InMemorySimHashDB {

	ArrayList<String> documentIDs = new ArrayList<String>();

	/**
	 * @param hashFunction
	 */
	public InMemorySimHashDBwithDocIDs(final HashFunction hashFunction) {
		super(hashFunction);
	}

	public void putDocument(final Map<String, Double> document,
			final String documentID) {
		super.putDocument(document);
		documentIDs.add(documentID);
	}

	public String[] getKNNFromDocument(final Map<String, Double> document,
			final int k) {
		final int[] ids = super.kNearestNeighbors(document, k);
		final String[] docIDs = new String[ids.length];
		int i = 0;
		for (final int id : ids) {
			docIDs[i] = documentIDs.get(ids[i]);
			i++;
		}
		return docIDs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ujm.tse.lt2c.satin.simhash.knn.InMemorySimHashDB#saveHashes(java.io
	 * .File)
	 */
	@Override
	public boolean saveHashes(final File f) {
		super.saveHashes(f);
		// Save the list of docids to disk
		PrintWriter out2 = null;
		try {
			out2 = new PrintWriter(new FileWriter(f.getAbsolutePath() + "docs"));
			for (final String text : documentIDs) {
				out2.write(text + "\r\n");
			}
		} catch (final IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());

		} finally {
			if (out2 != null) {
				out2.close();
			}
		}
		return true;
	}

	public static InMemorySimHashDBwithDocIDs loadFromFileSystem(
			final File hashes, final HashFunction function) {
		final InMemorySimHashDB db = InMemorySimHashDB.loadFromHashes(hashes,
				function);
		final InMemorySimHashDBwithDocIDs dbdoc = new InMemorySimHashDBwithDocIDs(
				function);
		dbdoc.hashes = db.hashes;
		dbdoc.documentIDs = new ArrayList<String>();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(hashes.getAbsolutePath()
					+ "docs"));
			String line;
			while ((line = in.readLine()) != null) {
				dbdoc.documentIDs.add(line);
			}
			return dbdoc;
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				in.close();
			} catch (final IOException e) {

			}
		}

	}

	@Override
	public String viewDB() {
		final StringBuilder sb = new StringBuilder();
		sb.append("[\n");
		int i = 0;
		for (final long[] ls : hashes) {
			sb.append(documentIDs.get(i) + " : ");
			for (final long l : ls) {
				sb.append(Long.toBinaryString(l));
			}
			sb.append("\n");
			i++;
		}
		sb.append("]");
		return sb.toString();
	}
}
