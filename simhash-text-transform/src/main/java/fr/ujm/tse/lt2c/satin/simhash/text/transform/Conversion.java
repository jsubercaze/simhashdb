/**
 * 
 */
package fr.ujm.tse.lt2c.satin.simhash.text.transform;

import java.util.Map;

import fr.ujm.tse.lt2c.satin.simhash.knn.ConversionMethod;
import fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.counting.WordCountingI;
import fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.nlp.NLPPipelineI;

/**
 * @author Julien Subercaze
 * 
 */
public class Conversion implements ConversionMethod {

	NLPPipelineI nlp;
	WordCountingI countingMethod;

	public Conversion(final NLPPipelineI nlp, final WordCountingI countingMethod) {
		super();
		this.nlp = nlp;
		this.countingMethod = countingMethod;
	}

	/**
	 * 
	 * @return a naive text to document conversion method, that counts word
	 *         occurrences. Default minimmum occurrence and word length set to
	 *         three. To set the values use {@link #naiveMethod(int, int)}
	 */
	public static ConversionMethod naiveMethod() {
		return new NaiveMethod();
	}

	public static ConversionMethod naiveMethod(final int minoccurrence,
			final int minlength) {
		return new NaiveMethod(minoccurrence, minlength);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ujm.tse.lt2c.satin.simhash.knn.ConversionMethod#convert(java.lang.
	 * String)
	 */
	@Override
	public Map<String, Double> convert(final String text) {
		return countingMethod.count(nlp.process(text));
	}

}
