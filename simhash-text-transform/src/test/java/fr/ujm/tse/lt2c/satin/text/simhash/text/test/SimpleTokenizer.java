/**
 *
 */
package fr.ujm.tse.lt2c.satin.text.simhash.text.test;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.nlp.NLPPipelineI;

/**
 * Transform text into sentences, uses {@link BreakIterator} with the default
 * locale
 *
 *
 * @author Julien
 *
 */
public class SimpleTokenizer implements NLPPipelineI {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.nlp.NLPPipelineI
	 * #process(java.lang.String)
	 */
	@Override
	public List<List<String>> process(final String text) {
		final BreakIterator iter = BreakIterator.getSentenceInstance(Locale
				.getDefault());
		iter.setText(text);
		final List<List<String>> result = new ArrayList<List<String>>();
		for (int start = iter.first(), end = iter.next(); end != BreakIterator.DONE; start = end, end = iter
				.next()) {
			// Break into words
			final String sub = text.substring(start, end);
			final BreakIterator iter2 = BreakIterator.getWordInstance(Locale
					.getDefault());
			iter2.setText(sub);
			final List<String> sentence = new ArrayList<String>();
			for (int start2 = iter2.first(), end2 = iter2.next(); end2 != BreakIterator.DONE; start2 = end2, end2 = iter2
					.next()) {
				sentence.add(sub.substring(start2, end2));
			}
			result.add(sentence);

		}
		return result;
	}
}
