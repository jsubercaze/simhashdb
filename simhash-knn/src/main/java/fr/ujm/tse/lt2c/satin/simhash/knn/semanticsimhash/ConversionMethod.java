/**
 * 
 */
package fr.ujm.tse.lt2c.satin.simhash.knn.semanticsimhash;

import java.util.Map;

/**
 * Convert a String into a Document that can be hashed using Simhash.
 * 
 * A conversion method contains two stages:
 * <ol>
 * <li>Text processing (Stemming, Chunking, POS filtering, ...)</li>
 * <li>Counting method that convert the processed text into a {@link Map}</li>
 * </ol>
 * 
 * 
 * 
 * @author Julien Subercaze
 * 
 */
public interface ConversionMethod {

	public Map<String, Double> convert(String text);
}
