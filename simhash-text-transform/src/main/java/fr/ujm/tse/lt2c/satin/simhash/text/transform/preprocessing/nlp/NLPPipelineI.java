/**
 * 
 */
package fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.nlp;

import java.util.List;

/**
 * @author Julien
 * 
 */
public interface NLPPipelineI {

	public List<List<String>> process(String text);
}
