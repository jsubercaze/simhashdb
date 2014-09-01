/**
 * 
 */
package fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.nlp;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;

/**
 * @author Julien Subercaze
 * 
 */
public class SentenceSplitting implements PreprocessingStep {

	public List<List<HasWord>> splitIntoSentences(final String text) {
		final DocumentPreprocessor dp = new DocumentPreprocessor(text);
		final List<List<HasWord>> sentences = new ArrayList<>();
		for (final List<HasWord> sentence : dp) {
			sentences.add(sentence);
		}
		return sentences;
	}
}
