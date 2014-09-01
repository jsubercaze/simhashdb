/**
 * 
 */
package fr.ujm.tse.lt2c.satin.text.simhash.text.test;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.counting.CoOccurrenceThreshold;
import fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.nlp.StandardFrenchStanfordNLPPipeline;

/**
 * @author Julien
 * 
 */
public class CoOccurrenceCountingTest {

	@Test
	public void test() throws Exception {
		final String text = FileUtils.readFileToString(new File(
				"docs/sarko2.txt"));
		// Preprocess french NLP
		final StandardFrenchStanfordNLPPipeline pipe = new StandardFrenchStanfordNLPPipeline(
				"c:/libs/posmodels/french.tagger");
		final List<List<String>> sentences = pipe.process(text);
		// Count with threshold
		final CoOccurrenceThreshold counting = new CoOccurrenceThreshold(3);
		final Map<String, Double> res = counting.count(sentences);
		System.out.println("Size " + res);

	}
}
