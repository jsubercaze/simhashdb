/**
 * 
 */
package fr.ujm.tse.lt2c.satin.simhash.examples.news;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

/**
 * Precision recall F1
 * 
 * @author Julien
 * 
 */
public class Precision {

	double[][] precision;
	/**
	 * Number of documents per db
	 */
	int documents;
	/**
	 * Number of databases
	 */
	int dbs;

	double[] avgPrecision;

	public Precision(final int documents, final int dbs) {
		super();
		this.documents = documents;
		this.dbs = dbs;

		precision = new double[dbs][];

	}

	public void addScore(final int docid, final int db, final double relevant,
			final double retrieved) {
		if (precision[db] == null) {
			precision[db] = new double[documents];

		}
		precision[db][docid] = retrieved / relevant;
	}

	public double[] getAveragePrecision() {
		if (avgPrecision == null) {
			avgPrecision = new double[dbs];
		}
		int i = 0;
		final Mean mean = new Mean();
		for (final double[] row : precision) {
			avgPrecision[i] = mean.evaluate(row);
			i++;
		}
		return avgPrecision;
	}

}
