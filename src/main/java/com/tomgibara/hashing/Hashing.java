package com.tomgibara.hashing;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * The main entry point for this API and provides a common basis for the
 * {@link Hash} and {@link Hasher} abstractions.
 * 
 * @author Tom Gibara
 *
 * @param <T>
 *            the type of object for which hashes are generated
 */

public interface Hashing<T> {

	/**
	 * A hasher which returns <code>Object.hashCode()</code> for all objects,
	 * and zero for null.
	 * 
	 * @param <T>
	 *            the type of object to be hashed
	 * @return a hasher which is consistent with <code>Object.hashCode()</code>
	 */
	
	static <T> Hasher<T> objectHasher() {
		return JavaHasher.object();
	}

	/**
	 * A hasher which returns <code>System.identityHashCode()</code> for all
	 * objects, and zero for null.
	 * 
	 * @param <T>
	 *            the type of object to be hashed
	 * @return a hasher which is consistent with
	 *         <code>System.identityHashCode()</code>
	 */
	
	static <T> Hasher<T> identityHasher() {
		return JavaHasher.identity();
	}

	/**
	 * A hasher which applies the Murmur3 32-bit hash function to streams.
	 * 
	 * @return a hasher based on the Murmur3 32-bit hash function
	 */

	static Hash<?> murmur3Int() {
		return Murmur3IntHash.instance();
	}

	/**
	 * A hasher which applies the Murmur3 32-bit hash function, using the
	 * initial seed state, to streams.
	 * 
	 * @param seed a value which initializes the hash function
	 * @return a hasher based on the Murmur3 32-bit hash function
	 */

	static Hash<?> murmur3Int(int seed) {
		return Murmur3IntHash.instance(seed);
	}

	/**
	 * A hash function based on Java's standard random number generator.
	 * 
	 * @param size
	 *            the range of generated hash values
	 * @return a PRNG-based hasher over the indicated range
	 */

	static Hash<?> prng(HashSize size) {
		if (size == null) throw new IllegalArgumentException("null size");
		return new RandomHash(null, null, size);
	}

	/**
	 * A hash function based on a {@link SecureRandom} number generator.
	 * 
	 * @param algorithm
	 *            the algorithm used for the PRNG
	 * @param size
	 *            the range of generated hash values
	 * @return a PRNG-based hasher over the indicated range
	 */

	static Hash<?> prng(String algorithm, HashSize size) {
		if (algorithm == null) throw new IllegalArgumentException("null algorithm");
		if (algorithm.isEmpty()) throw new IllegalArgumentException("empty algorithm");
		if (size == null) throw new IllegalArgumentException("null size");
		return new RandomHash(algorithm, null, size);
	}

	/**
	 * A hash function based on a {@link SecureRandom} number generator.
	 * 
	 * @param algorithm
	 *            the algorithm used for the PRNG
	 * @param provider
	 *            the provider of the secure random number generator
	 * @param size
	 *            the range of generated hash values
	 * @return a PRNG-based hasher over the indicated range
	 */

	static Hash<?> prng(String algorithm, String provider, HashSize size) {
		if (algorithm == null) throw new IllegalArgumentException("null algorithm");
		if (algorithm.isEmpty()) throw new IllegalArgumentException("empty algorithm");
		if (provider == null) throw new IllegalArgumentException("null provider");
		if (provider.isEmpty()) throw new IllegalArgumentException("empty provider");
		if (size == null) throw new IllegalArgumentException("null size");
		return new RandomHash(algorithm, provider, size);
	}

	/**
	 * <p>
	 * A "minimal perfect hash" for strings. An array of <em>n</em> unique
	 * non-null strings will generator a hasher that returns a unique hash value
	 * <em>h</em> (0 <= h < n) for any string <em>s</em> in the array.
	 * </p>
	 *
	 * <p>
	 * The supplied array is <em>not</em> retained. This means that the hasher
	 * cannot necessarily confirm that a string is not in the supplied array.
	 * Where hasher cannot distinguish that a string is not in the array, a
	 * 'valid' hash value may be returned. Under no circumstances will a hash
	 * value be returned that is greater than or equal to <em>n</em>.
	 * </p>
	 *
	 * <p>
	 * <strong>IMPORTANT NOTE:<strong> The array of strings supplied to the
	 * constructor will be mutated: it is re-ordered so that
	 * <code>hash(a[i]) == i</code>. Application code must generally use this
	 * information to map hash values back onto the appropriate string value.
	 * </p>
	 *
	 * <p>
	 * <strong>NOTE:<strong> Good performance of this algorithm is predicated on
	 * string hash values being cached by the <code>String</code> class.
	 * Experience indicates that is is a good assumption.
	 * </p>
	 */

	static Hasher<String> perfect(String... values) {
		if (values == null) throw new IllegalArgumentException("null values");
		if (values.length == 0) throw new IllegalArgumentException("no values");
		return new PerfectStringHasher(values);
	}

	HashSize getSize();

	default int getQuantity() {
		return 1;
	}

	HashValue hashValue(T value) throws IllegalArgumentException;

	/**
	 * The hash value as a {@link BigInteger}. This method may be useful in
	 * circumstances where the generated hash is too large to be accommodated in
	 * a single primitive value, eg. if cryptographic hashes are being used.
	 *
	 * @param value
	 *            the object to be hashed
	 * @return the object's hash code, never null
	 * @throws IllegalArgumentException
	 *             if the value cannot be hashed
	 */

	default BigInteger bigHashValue(T value) throws IllegalArgumentException {
		return hashValue(value).bigValue();
	}

	/**
	 * The hash value as a long. This method should provide better performance
	 * for long-capacity hashes. This value is not guaranteed to lie within the
	 * indicated {@link HashSize} unless {@link HashSize#isLongCapacity()} is
	 * true.
	 *
	 * @param value
	 *            the object to be hashed
	 * @return the object's hash code
	 * @throws IllegalArgumentException
	 *             if the value cannot be hashed
	 */

	default long longHashValue(T value) throws IllegalArgumentException {
		return hashValue(value).longValue();
	}

	/**
	 * The hash value as an int. This method should provide better performance
	 * for integer-capacity hashes. This value is not guaranteed to lie within the
	 * indicated {@link HashSize} unless {@link HashSize#isIntCapacity()} is
	 * true.
	 *
	 * @param value
	 *            the object to be hashed
	 * @return the object's hash code
	 * @throws IllegalArgumentException
	 *             if the value cannot be hashed
	 */

	default int intHashValue(T value) throws IllegalArgumentException {
		return hashValue(value).intValue();
	}

}
