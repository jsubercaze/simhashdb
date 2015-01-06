/**
 *
 */
package fr.ujm.tse.lt2c.satin.simhash.examples;

import java.util.Arrays;

import com.google.common.hash.Hashing;

import fr.ujm.tse.lt2c.satin.simhash.core.hashing.SimHash;
import fr.ujm.tse.lt2c.satin.simhash.knn.db.hashes.SimpleInMemoryHashDB;
import fr.ujm.tse.lt2c.satin.simhash.text.transform.Conversion;
import fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.counting.SimpleCounting;
import fr.ujm.tse.lt2c.satin.simhash.text.transform.utils.SimpleTokenizer;

/**
 * @author Julien
 * 
 */
public class SimpleExample {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Conversion method = new Conversion(new SimpleTokenizer(),
				new SimpleCounting());
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
		System.out.println();
		System.out.println("Adding documents to DB");
		final SimHash hasher = new SimHash(Hashing.md5());
		final SimpleInMemoryHashDB db = new SimpleInMemoryHashDB(hasher.bits());
		db.putDocument(hasher.hashToArray(method.convert(s1)), "1");
		db.putDocument(hasher.hashToArray(method.convert(s2)), "2");
		db.putDocument(hasher.hashToArray(method.convert(s2)), "3");

		// Query from document
		System.out.println();
		System.out.println("Closest document to S1");
		System.out.println(Arrays.toString(db.kNNSorted(
				hasher.hashToArray(method.convert(s1)), 3)));
		System.out.println("Closest document to S2");
		System.out.println(Arrays.toString(db.kNNSorted(
				hasher.hashToArray(method.convert(s2)), 3)));
		System.out.println("Closest document to S3");
		System.out.println(Arrays.toString(db.kNNSorted(
				hasher.hashToArray(method.convert(s3)), 3)));

	}
}
