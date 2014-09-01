/**
 * 
 */
package fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.nlp;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

/**
 * Filter adjective, verbs and nouns into the final list before counting.
 * 
 * 
 * @author Julien Subercaze
 * 
 */
public class StandardFrenchStanfordNLPPipeline implements NLPPipelineI {

	String posModel;
	protected StanfordCoreNLP pipeline;

	public StandardFrenchStanfordNLPPipeline(final String posModel) {
		Properties props;
		props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos,lemma");
		props.put("pos.model", posModel);
		// StanfordCoreNLP loads a lot of models, so you probably
		// only want to do this once per execution
		pipeline = new StanfordCoreNLP(props);
	}

	@Override
	public List<List<String>> process(final String text) {
		final Annotation document = new Annotation(text);
		pipeline.annotate(document);
		final List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		final List<List<String>> result = new ArrayList<>();
		for (final CoreMap sentence : sentences) {
			final List<String> filtered = new ArrayList<>();
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (final CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// this is the text of the token
				// final String word = token.get(TextAnnotation.class);
				// this is the Lemma tag of the token
				final String lemm = token.get(LemmaAnnotation.class);
				final String pos = token.get(PartOfSpeechAnnotation.class);
				final char first = pos.charAt(0);
				if (first == 'V' || first == 'N' || first == 'A') {
					filtered.add(lemm.toLowerCase());
				}
				// System.out.println(word + " :" + lemm + " : " + pos);
			}
			result.add(filtered);
		}

		return result;

	}
}
