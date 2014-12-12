/**
 * 
 */
package fr.ujm.tse.lt2c.satin.simhash.examples;

import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import fr.ujm.tse.lt2c.satin.simhash.core.hashing.SimHash;

/**
 * @author Julien
 * 
 */
public class PaperExample {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final HashFunction function = Hashing.murmur3_32();
		final SimHash hasher = new SimHash(function);

		final Map<String, Double> map1 = new LinkedHashMap<>();
		map1.put("scientist", 1.0);
		map1.put("talk", 1.0);
		map1.put("symposium", 1.0);

		// Print single hash
		for (final String s : map1.keySet()) {
			final HashCode code = function.hashBytes(s.getBytes());
			System.out.println(s + " " + bytesToHex(code.asBytes()));
		}

		// Print result
		final BitSet hash = hasher.hash(map1);
		final byte[] res = hash.toByteArray();
		System.out.println(bytesToHex(res));

		// Second example
		final Map<String, Double> map2 = new LinkedHashMap<>();
		map2.put("researcher", 1.0);
		map2.put("present", 1.0);
		// map1.put("talk", 1.0);
		map2.put("workshop", 1.0);
		// Print single hash
		for (final String s : map2.keySet()) {
			final HashCode code = function.hashBytes(s.getBytes());
			System.out.println(s + " " + bytesToHex(code.asBytes()));

		}
		final BitSet hash2 = hasher.hash(map2);

		final byte[] res2 = hash2.toByteArray();
		System.out.println(bytesToHex(res2));

		// Distance
		hash.xor(hash2);
		System.out.println(hash.cardinality());
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(final byte[] bytes) {
		final char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			final int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
}
