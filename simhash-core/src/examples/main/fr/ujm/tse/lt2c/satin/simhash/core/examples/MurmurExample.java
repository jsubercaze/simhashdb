/**
 * 
 */
package fr.ujm.tse.lt2c.satin.simhash.core.examples;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import com.google.common.hash.Hashing;

import fr.ujm.tse.lt2c.satin.simhash.core.hashing.SimHash;

/**
 * In this example we perform the following operations :
 * <ol>
 * <li>Add sample values to three Map</li>
 * <li>Instantiate a Simhash backed by a murmur 128</li>
 * <li>Compare the distance between the weighted maps</li>
 * <ol>
 * 
 * 
 * 
 * @author Julien Subercaze
 * 
 */
public class MurmurExample {

	public static void main(final String[] args) {

		// Maps
		final Map<String, Double> first = new HashMap<>();
		first.put("home", 3D);
		first.put("garden", 2D);
		first.put("car", 1D);
		final Map<String, Double> second = new HashMap<>();
		second.put("home", 3D);
		second.put("garden", 2D);
		second.put("pool", 1D);
		final Map<String, Double> third = new HashMap<>();
		third.put("dog", 1D);
		third.put("cat", 5D);
		third.put("bird", 2D);

		// Print Maps
		System.out.println("#1 :" + first);
		System.out.println("#2 :" + second);
		System.out.println("#3 :" + third);

		// Hasher
		final SimHash hasher = new SimHash(Hashing.murmur3_128());
		final BitSet hashFirst = hasher.hash(first);
		final BitSet hashSecond = hasher.hash(second);
		final BitSet hashThird = hasher.hash(third);

		// Compute distances and outputs
		final BitSet c1 = (BitSet) hashFirst.clone();
		c1.xor(hashSecond);
		System.out.println("Distance #1 - #2 : " + c1.cardinality() * 100
				/ hasher.bits());
		hashFirst.xor(hashThird);
		System.out.println("Distance #1 - #3: " + hashFirst.cardinality() * 100
				/ hasher.bits());
		hashSecond.xor(hashThird);
		System.out.println("Distance #2 - #3: " + hashSecond.cardinality()
				* 100 / hasher.bits());

	}
}
