/**
 *
 */
package fr.ujm.tse.lt2c.satin.simhash.examples;

import fr.ujm.tse.lt2c.satin.simhash.knn.semanticsimhash.ConversionMethod;

/**
 * @author Julien
 *
 */
public class SimpleExampleWithDocIDs {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final ConversionMethod method = null;// FIXME
		// new Conversion(new SimpleTokenizer(),
		// new SimpleCounting());
		final String s1 = "The cat is playing outside.";
		final String s2 = "John is playing outside.";
		final String s3 = "This has nothing to do with the previous sentences";
		System.out.println("Three sentences to test");
		System.out.println("S1 : " + s1);
		System.out.println("S2 : " + s2);
		System.out.println("S3 : " + s3);
		// Converting
		System.out.println();
		System.out.println("Converting into map<string,double>");
		System.out.println(method.convert(s1));
		System.out.println(method.convert(s2));
		System.out.println(method.convert(s3));
		// FIXME new version
	}
}
