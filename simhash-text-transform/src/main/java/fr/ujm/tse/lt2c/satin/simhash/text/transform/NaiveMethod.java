/**
 * 
 */
package fr.ujm.tse.lt2c.satin.simhash.text.transform;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.common.collect.ImmutableMultiset;

import fr.ujm.tse.lt2c.satin.simhash.knn.ConversionMethod;

/**
 * 
 * 
 * @author Julien Subercaze
 * 
 */
class NaiveMethod implements ConversionMethod {

	/**
	 * Minimum occurrence to be kept in the map
	 */
	private int minimalOccurrence = 3;
	/**
	 * Words must long enough
	 */
	private int minimalLength = 4;

	public NaiveMethod() {
		super();
	}

	public NaiveMethod(final int minimalOccurrence, final int minimalLength) {
		super();
		this.minimalOccurrence = minimalOccurrence;
		this.minimalLength = minimalLength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ujm.tse.lt2c.satin.simhash.text.transform.ConversionMethod#convert()
	 */
	@Override
	public Map<String, Double> convert(final String text) {
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
			if (end - start >= minimalLength) {
				words.add(text.substring(start, end));
			}
		}
		// Into cardinality map
		final ImmutableMultiset<String> multiset = ImmutableMultiset
				.copyOf(words);
		final Map<String, Double> result = new HashMap<>();
		for (final String word : multiset.elementSet()) {
			if (multiset.count(word) > minimalOccurrence) {
				result.put(word, (double) multiset.count(word));
			}
		}
		return result;
	}
}
