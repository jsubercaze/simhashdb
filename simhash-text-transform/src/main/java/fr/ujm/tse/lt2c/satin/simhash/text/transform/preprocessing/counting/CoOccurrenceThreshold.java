/**
 * 
 */
package fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.counting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.common.collect.Table;

/**
 * Set values of word to one if co-occurring only with ones, to its average (not
 * including one) otherwise
 * 
 * 
 * @author Julien Subercaze
 * 
 */
public class CoOccurrenceThreshold extends CoOccurrenceCounting {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = Logger
			.getLogger(CoOccurrenceThreshold.class);
	private int threshold = 2;

	public CoOccurrenceThreshold(final int threshold) {
		super();
		this.threshold = threshold;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.counting.
	 * WordCountingI#count(java.util.List)
	 */
	@Override
	public Map<String, Double> count(final List<List<String>> sentences) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Starting counting coooc for " + sentences);
		}
		final Table<String, String, Integer> matrix = computeCoOccurrence(sentences);
		final Set<String> values = matrix.columnKeySet();
		final Map<String, Double> result = new HashMap<>();

		for (final String s : values) {
			// Surebet
			if (s.length() <= 3) {
				continue;
			}
			final Map<String, Integer> occurences = matrix.column(s).size() > matrix
					.row(s).size() ? matrix.column(s) : matrix.row(s);
			int max = 0;
			int sum = 0;
			for (final Entry<String, Integer> entry : occurences.entrySet()) {
				max = max < entry.getValue() ? entry.getValue() : max;
				sum += entry.getValue();
			}
			if (max >= threshold) {
				final int roundedAverage = sum;
				if (LOGGER.isTraceEnabled()) {
					LOGGER.trace("Adding string " + s + " " + roundedAverage);
				}
				result.put(s, (double) roundedAverage);
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Counting : " + result);
		}
		return result;
	}
}
