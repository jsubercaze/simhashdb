/**
 * 
 */
package fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.counting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMultiset;

/**
 * @author Julien Subercaze
 * 
 */
public class SimpleCounting implements WordCountingI {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.counting.CountingI
	 * #count(java.util.List)
	 */
	@Override
	public Map<String, Double> count(final List<List<String>> sentences) {
		if (sentences == null || sentences.size() == 0) {
			return Collections.emptyMap();
		}
		final List<String> words = new ArrayList<>();
		for (final List<String> sentence : sentences) {
			words.addAll(sentence);
		}
		final ImmutableMultiset<String> multiset = ImmutableMultiset
				.copyOf(words);
		final Map<String, Double> result = new HashMap<>();
		for (final String word : multiset.elementSet()) {
			result.put(word, (double) multiset.count(word));
		}
		return result;
	}

}
