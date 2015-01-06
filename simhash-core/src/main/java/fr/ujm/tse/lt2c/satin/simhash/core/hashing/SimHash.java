/**
 *
 */
package fr.ujm.tse.lt2c.satin.simhash.core.hashing;

import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;

/**
 * @author Julien Subercaze
 * 
 * 
 *         Simhash Implementation that supports hashing function defined by the
 *         Guava's {@link HashFunction}
 */
public class SimHash {
	/**
	 * Function used as base for the SimHash
	 */
	private final HashFunction hashFunction;

	public SimHash(final HashFunction hashFunction) {
		super();
		this.hashFunction = hashFunction;

	}

	/**
	 * Compute the unique hash for the weighted elements
	 * 
	 * @param weigthedStrings
	 *            Maps that contains pairs of <String, Weight>
	 * @param charset
	 *            Encoding charset used for the Strings
	 * @return a unique hash
	 */
	public BitSet hash(final Map<String, Double> weightedStrings,
			final Charset charset) {
		// Weight array for summing
		final double[] weightVector = new double[hashFunction.bits()];
		for (final Entry<String, Double> entry : weightedStrings.entrySet()) {
			final HashCode hash = hashFunction.hashString(entry.getKey(),
					charset);
			weightHashCode(hash, entry.getValue(), weightVector);
		}
		final BitSet result = new BitSet(hashFunction.bits());
		for (int i = 0; i < hashFunction.bits(); i++) {
			result.set(i, weightVector[i] >= 0 ? true : false);
		}
		return result;
	}

	/**
	 * Uses default charset to hash strings, warning !
	 * 
	 * @param weightedStrings
	 * @return a unique hash
	 */
	public BitSet hash(final Map<String, Double> weightedStrings) {
		return hash(weightedStrings, Charset.defaultCharset());
	}

	public long[] hashToArray(final Map<String, Double> weightedStrings) {
		final long[] res = new long[hashFunction.bits()];
		final BitSet bits = this.hash(weightedStrings);
		System.arraycopy(bits.toLongArray(), 0, res, 0,
				bits.toLongArray().length);
		return res;
	}

	private void weightHashCode(final HashCode hash, final double weight,
			final double[] weighted) {
		// Add the weighted hashcode in the array
		final BitSet b = BitSet.valueOf(hash.asBytes());

		for (int i = 0; i < hash.bits(); i++) {
			weighted[i] += (b.get(i) ? 1 : -1) * weight;
		}
	}

	public int bits() {
		return hashFunction.bits();
	}

	/**
	 * 
	 * @param set1
	 * @param set2
	 * @return the normalized distance between 0 and 1 for this bitlength. 0
	 *         when close, 1 when far
	 */
	public double distance(final BitSet set1, final BitSet set2) {
		final BitSet clone = (BitSet) set1.clone();
		clone.xor(set2);
		return (double) clone.cardinality() / (double) hashFunction.bits();
	}

	/**
	 * @return the hashFunction
	 */
	public final HashFunction getHashFunction() {
		return hashFunction;
	}

}
