/**
 * 
 */
package fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.counting;

import java.util.List;
import java.util.Map;

/**
 * @author Julien Subercaze
 * 
 */
public interface WordCountingI {

	public Map<String, Double> count(final List<List<String>> sentences);

}
