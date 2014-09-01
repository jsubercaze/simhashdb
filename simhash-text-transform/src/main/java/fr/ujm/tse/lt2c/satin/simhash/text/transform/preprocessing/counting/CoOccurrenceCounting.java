/**
 * 
 */
package fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.counting;

import java.util.List;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * Count CoOccurrences of words between sentences, then process filtering to
 * reduce the dataset
 * 
 * 
 * @author Julien Subercaze
 * 
 */
public abstract class CoOccurrenceCounting implements WordCountingI {

	protected Table<String, String, Integer> computeCoOccurrence(
			final List<List<String>> sentences) {
		// Table is an upper left matrix, with string sorted using standard
		// comparator
		final Table<String, String, Integer> coocurrenceMatrix = HashBasedTable
				.create();
		String rankFirst;
		String rankSecond;

		for (final List<String> sentence : sentences) {
			for (int i = 0; i < sentence.size(); i++) {
				final String first = sentence.get(i);

				for (int j = i; j < sentence.size(); j++) {
					final String second = sentence.get(j);
					rankFirst = first.compareToIgnoreCase(second) < 1 ? first
							: second;
					rankSecond = first.compareToIgnoreCase(second) < 1 ? second
							: first;
					if (coocurrenceMatrix.contains(rankFirst, rankSecond)) {
						int occ = coocurrenceMatrix.get(rankFirst, rankSecond);
						occ++;
						coocurrenceMatrix.put(rankFirst, rankSecond, occ);
						coocurrenceMatrix.put(rankSecond, rankFirst, occ);

					} else {
						coocurrenceMatrix.put(rankFirst, rankSecond, 1);
						coocurrenceMatrix.put(rankSecond, rankSecond, 1);
					}
				}
			}
		}
		return coocurrenceMatrix;
	}

}
