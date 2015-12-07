/*
 * Copyright 2015 Tom Gibara
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.tomgibara.hashing;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
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

	static Hash murmur3Int() {
		return Murmur3IntHash.instance();
	}

	/**
	 * A hasher which applies the Murmur3 32-bit hash function, using the
	 * initial seed state, to streams.
	 * 
	 * @param seed a value which initializes the hash function
	 * @return a hasher based on the Murmur3 32-bit hash function
	 */

	static Hash murmur3Int(int seed) {
		return Murmur3IntHash.instance(seed);
	}

	/**
	 * A hash function based on Java's standard random number generator.
	 * 
	 * @param size
	 *            the range of generated hash values
	 * @return a PRNG-based hasher over the indicated range
	 */

	static Hash prng(HashSize size) {
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

	static Hash prng(String algorithm, HashSize size) {
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

	static Hash prng(String algorithm, String provider, HashSize size) {
		if (algorithm == null) throw new IllegalArgumentException("null algorithm");
		if (algorithm.isEmpty()) throw new IllegalArgumentException("empty algorithm");
		if (provider == null) throw new IllegalArgumentException("null provider");
		if (provider.isEmpty()) throw new IllegalArgumentException("empty provider");
		if (size == null) throw new IllegalArgumentException("null size");
		return new RandomHash(algorithm, provider, size);
	}

	/**
	 * Provides a standard source of SHA-1 digests.
	 * 
	 * @return a source of SHA-1 digests
	 */

	static HashDigestSource SHA1() {
		return StandardHashDigestSource.SHA1();
	}

	/**
	 * Creates a digest source for the specified algorithm based on the
	 * platform's default provider.
	 * 
	 * @param algorithm
	 *            the name of the digest algorithm
	 * @return a source of the specified digests
	 * @throws NoSuchAlgorithmException
	 *             if there is no digest provided for the specified algorithm
	 */

	static HashDigestSource digestSource(String algorithm) throws NoSuchAlgorithmException {
		if (algorithm == null) throw new IllegalArgumentException("null algorithm");
		return new StandardHashDigestSource(algorithm);
	}

	/**
	 * Creates a digest source for the specified algorithm based on the supplied
	 * platform.
	 * 
	 * @param algorithm
	 *            the name of the digest algorithm
	 * @param provider
	 *            the name of the digest provider
	 * @return a source of digests using the chosen provider
	 * @throws NoSuchProviderException
	 *             if there is no provider with the given name
	 * @throws NoSuchAlgorithmException
	 *             if the provider does not support an algorithm with the given
	 *             name
	 */

	static HashDigestSource digestSource(String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
		if (algorithm == null) throw new IllegalArgumentException("null algorithm");
		if (provider == null) throw new IllegalArgumentException("null provider");
		return new StandardHashDigestSource(algorithm, provider);
	}

	/**
	 * Attempts to derive an digest source by cloning the supplied digest.
	 * 
	 * @param digest
	 *            the digest to be cloned
	 * @return a source of digests based on cloning
	 */

	static HashDigestSource digestSource(MessageDigest digest) {
		if (digest == null) throw new IllegalArgumentException("null digest");
		return new StandardHashDigestSource(digest);
	}

	/**
	 * <p>
	 * A "minimal perfect hash" for strings. An array of <em>n</em> unique
	 * non-null strings will generator a hasher that returns a unique hash code
	 * <em>h</em> (0 &lt;= h &lt; n) for any string <em>s</em> in the array.
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
	 * <strong>IMPORTANT NOTE:</strong> The array of strings supplied to the
	 * constructor will be mutated: it is re-ordered so that
	 * <code>hash(a[i]) == i</code>. Application code must generally use this
	 * information to map hash values back onto the appropriate string value.
	 * </p>
	 *
	 * <p>
	 * <strong>NOTE:</strong> Good performance of this algorithm is predicated on
	 * string hash values being cached by the <code>String</code> class.
	 * Experience indicates that is is a good assumption.
	 * </p>
	 * 
	 * @param values
	 *            the strings from which the hash is to be derived
	 * @return a minimal perfect for the supplied strings
	 * @throws IllegalArgumentException
	 *             if a null or duplicate string is supplied
	 */

	static Hasher<String> minimalPerfect(String... values) throws IllegalArgumentException {
		if (values == null) throw new IllegalArgumentException("null values");
		if (values.length == 0) throw new IllegalArgumentException("no values");
		return new PerfectStringHasher(values);
	}

	/**
	 * Indicates the size of the hashes generated by this hashing.
	 * 
	 * @return the size of the hashes generated by this hashing.
	 */
	HashSize getSize();

	/**
	 * The number of hash values generated for each object. Hashing
	 * implementations which, in principle, generate an unlimited number of hash
	 * are expected to return {@link Integer#MAX_VALUE}.
	 * 
	 * @return the number of hash values created on each hashing
	 */

	default int getQuantity() {
		return 1;
	}
	
	/**
	 * Computes a hash code for the supplied object.
	 * 
	 * @param value
	 *            the object to be hashed
	 * @return the computed hash code
	 * @throws IllegalArgumentException
	 *             if the supplied value cannot be hashed by this object
	 */

	HashCode hash(T value) throws IllegalArgumentException;

	/**
	 * <p>
	 * The hash code as a {@link BigInteger}. This method may be useful in
	 * circumstances where the generated hash is too large to be accommodated in
	 * a single primitive value, eg. if cryptographic hashes are being used.
	 * 
	 * <p>
	 * If multiple values are required for a single hashing then
	 * {@link HashCode#bytesValue()} must be used.
	 *
	 * @param value
	 *            the object to be hashed
	 * @return the computed hash value
	 * @throws IllegalArgumentException
	 *             if the supplied value cannot be hashed by this object
	 */

	default byte[] bytesHashValue(T value) throws IllegalArgumentException {
		return hash(value).bytesValue();
	}

	/**
	 * <p>
	 * The hash code as a {@link BigInteger}. This method may be useful in
	 * circumstances where the generated hash is too large to be accommodated in
	 * a single primitive value, eg. if cryptographic hashes are being used.
	 * 
	 * <p>
	 * If multiple values are required for a single hashing then
	 * {@link HashCode#bigValue()} must be used.
	 *
	 * @param value
	 *            the object to be hashed
	 * @return the computed hash value
	 * @throws IllegalArgumentException
	 *             if the supplied value cannot be hashed by this object
	 */

	default BigInteger bigHashValue(T value) throws IllegalArgumentException {
		return hash(value).bigValue();
	}

	/**
	 * <p>
	 * The hash value as a long. This method should provide better performance
	 * for long-capacity hashes over the {@link #hash(Object)} method.
	 * 
	 * <p>
	 * If multiple values are required for a single hashing then
	 * {@link HashCode#longValue()} must be used.
	 *
	 * @param value
	 *            the object to be hashed
	 * @return the object's hash code
	 * @throws IllegalArgumentException
	 *             if the supplied value cannot be hashed by this object
	 */

	default long longHashValue(T value) throws IllegalArgumentException {
		return hash(value).longValue();
	}

	/**
	 * <p>
	 * The hash value as an int. This method should provide better performance
	 * for integer-capacity hashes over the {@link #hash(Object)} method.
	 * 
	 * <p>
	 * If multiple values are required for a single hashing then
	 * {@link HashCode#intValue()} must be used.
	 *
	 * @param value
	 *            the object to be hashed
	 * @return the object's hash code
	 * @throws IllegalArgumentException
	 *             if the supplied value cannot be hashed by this object
	 */

	default int intHashValue(T value) throws IllegalArgumentException {
		return hash(value).intValue();
	}

}
