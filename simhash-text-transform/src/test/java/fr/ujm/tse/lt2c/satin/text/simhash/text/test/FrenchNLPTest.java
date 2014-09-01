/**
 * 
 */
package fr.ujm.tse.lt2c.satin.text.simhash.text.test;

import java.util.List;

import org.junit.Test;

import fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.nlp.StandardFrenchStanfordNLPPipeline;

/**
 * @author Julien Subercaze
 * 
 */
public class FrenchNLPTest {

	@Test
	public void test() {

		final StandardFrenchStanfordNLPPipeline pipe = new StandardFrenchStanfordNLPPipeline(
				"c:/libs/posmodels/french.tagger");
		// pipe
		// .process("John threw his hat away as France scored. The blue line would be bigger than the red line");
		final List<List<String>> res = pipe
				.process("les chiens jouent dans la cour bleue avec leur maitre. Michel mange des bouts de bouillie molle ");
		System.out.println(res);

	}
}
