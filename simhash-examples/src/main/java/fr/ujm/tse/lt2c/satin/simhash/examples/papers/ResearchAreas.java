/**
 * 
 */
package fr.ujm.tse.lt2c.satin.simhash.examples.papers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.google.common.collect.ImmutableMultiset;
import com.google.common.hash.Hashing;

import fr.ujm.tse.lt2c.satin.simhash.knn.InMemorySimHashDB;

/**
 * In this example we compute distance between different research papers :
 * <ul>
 * <li>3 papers on SimHash</li>
 * <li>2 on astrophysics (taken from new in Arxiv)</li>
 * <li>2 on quantitative finance (same process)</li>
 * <li>2 on multi agent systems (again)</li>
 * </ul>
 * 
 * All papers are stored in the pdfs/ folder
 * 
 * 
 * We demonstrate the use of the kNN database to retrieve closest article
 * 
 * @author Julien Subercaze
 * 
 */
public class ResearchAreas {
	/**
	 * Minimum occurrence to be kept in the map
	 */
	private static final int MINIMAL_OCCURENCE = 3;
	/**
	 * Words must long enough
	 */
	private static final int MINIMAL_LENGTH = 4;
	/**
	 * Numbers of neighbors for querying
	 */
	private static final int K = 3;

	public static void main(final String[] args) {
		// Init DB
		final InMemorySimHashDB knnDB = new InMemorySimHashDB(Hashing.murmur3_128());
		// Iterate over pdf
		final File dir = new File("pdfs/");
		final File[] files = dir.listFiles();
		final List<File> pdfs = new ArrayList<>();
		for (final File file : files) {
			if (file.getAbsolutePath().endsWith(".pdf")) {
				System.out.print("Adding " + file.getName());
				pdfs.add(file);
				final String text = textFromPDF(file);
				final Map<String, Double> document = simpleCountingMethod(text);
				System.out.println(" with ID : " + knnDB.putDocument(document));
			}

		}
		// Finished adding
		System.out.println("Finished adding PDFS into the Simhash DB");
		System.out.println("Now contains " + knnDB.size() + " documents");
		System.out.println();
		System.out.println("Starting query to the DB");
		for (int i = 0; i < knnDB.size(); i++) {
			System.out.println("Querying " + K + " neighbors for "
					+ pdfs.get(i).getName());
			final int[] ids = knnDB.kNearestNeighbors(i, K);
			System.out.println(Arrays.toString(ids));
			for (int j = 0; j < ids.length; j++) {
				System.out.println(j + 1 + "-" + pdfs.get(ids[j]));
			}
		}
	}

	private static String textFromPDF(final File f) {
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(f));
			final PDDocument pdf = PDDocument.load(is, true);
			final PDFTextStripper stripper = new PDFTextStripper();
			final String text = stripper.getText(pdf);
			pdf.close();
			return text;
		} catch (final Exception e) {

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (final IOException e) {

				}
			}
		}
		return "";
	}

	/**
	 * Naive counting method
	 * 
	 * @param text
	 * @return a map<word,occurrence> for the given text
	 */
	private static Map<String, Double> simpleCountingMethod(final String text) {
		if (text == null || text.length() == 0) {
			return Collections.emptyMap();
		}
		final List<String> words = new ArrayList<>();
		// Break into words
		final BreakIterator boundary = BreakIterator
				.getWordInstance(Locale.ENGLISH);
		boundary.setText(text);
		int start = boundary.first();
		for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary
				.next()) {
			if (end - start >= MINIMAL_LENGTH) {
				words.add(text.substring(start, end));
			}
		}
		// Into cardinality map
		final ImmutableMultiset<String> multiset = ImmutableMultiset
				.copyOf(words);
		final Map<String, Double> result = new HashMap<>();
		for (final String word : multiset.elementSet()) {
			if (multiset.count(word) > MINIMAL_OCCURENCE) {
				result.put(word, (double) multiset.count(word));
			}
		}
		return result;
	}
}
