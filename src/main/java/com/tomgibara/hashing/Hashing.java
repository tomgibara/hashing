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
		return IntHasher.object();
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
		return IntHasher.identity();
	}

	/**
	 * A hasher which returns <code>Arrays.hashCode(byte[])</code>. The hasher
	 * returns zero for <code>null</code>.
	 * 
	 * @return a hasher which is consistent with
	 *         <code>Arrays.hashCode(byte[])</code>
	 */

	static Hasher<byte[]> bytesHasher() {
		return IntHasher.bytesHasher;
	}
	
	/**
	 * A hasher which returns <code>Arrays.hashCode(short[])</code>. The hasher
	 * returns zero for <code>null</code>.
	 * 
	 * @return a hasher which is consistent with
	 *         <code>Arrays.hashCode(short[])</code>
	 */

	static Hasher<short[]> shortsHasher() {
		return IntHasher.shortsHasher;
	}
	
	/**
	 * A hasher which returns <code>Arrays.hashCode(int[])</code>. The hasher
	 * returns zero for <code>null</code>.
	 * 
	 * @return a hasher which is consistent with
	 *         <code>Arrays.hashCode(int[])</code>
	 */

	static Hasher<int[]> intsHasher() {
		return IntHasher.intsHasher;
	}
	
	/**
	 * A hasher which returns <code>Arrays.hashCode(long[])</code>. The hasher
	 * returns zero for <code>null</code>.
	 * 
	 * @return a hasher which is consistent with
	 *         <code>Arrays.hashCode(long[])</code>
	 */

	static Hasher<long[]> longsHasher() {
		return IntHasher.longsHasher;
	}

	/**
	 * A hasher which returns <code>Arrays.hashCode(boolean[])</code>. The
	 * hasher returns zero for <code>null</code>.
	 * 
	 * @return a hasher which is consistent with
	 *         <code>Arrays.hashCode(boolean[])</code>
	 */

	static Hasher<boolean[]> booleansHasher() {
		return IntHasher.booleansHasher;
	}
	
	/**
	 * A hasher which returns <code>Arrays.hashCode(char[])</code>. The hasher
	 * returns zero for <code>null</code>.
	 * 
	 * @return a hasher which is consistent with
	 *         <code>Arrays.hashCode(char[])</code>
	 */

	static Hasher<char[]> charsHasher() {
		return IntHasher.charsHasher;
	}
	
	/**
	 * A hasher which returns <code>Arrays.hashCode(float[])</code>. The hasher
	 * returns zero for <code>null</code>.
	 * 
	 * @return a hasher which is consistent with
	 *         <code>Arrays.hashCode(float[])</code>
	 */

	static Hasher<float[]> floatsHasher() {
		return IntHasher.floatsHasher;
	}
	
	/**
	 * A hasher which returns <code>Arrays.hashCode(double[])</code>. The hasher
	 * returns zero for <code>null</code>.
	 * 
	 * @return a hasher which is consistent with
	 *         <code>Arrays.hashCode(double[])</code>
	 */

	static Hasher<double[]> doublesHasher() {
		return IntHasher.doublesHasher;
	}

	/**
	 * A hasher that generates its hash codes directly from the supplied
	 * <code>int</code> function. This method provides a simple way to convert
	 * an existing integer function. The returned hasher will have a size of
	 * {@link HashSize#INT_SIZE}. Null values are support only if they are
	 * supported by the prehash.
	 * 
	 * @param prehash
	 *            a function mapping values to ints.
	 * @return the prehash as a hasher
	 */

	static <E> Hasher<E> intDerivedHasher(IntPrehash<E> prehash) {
		if (prehash == null) throw new IllegalArgumentException("null prehash");
		return new IntHasher<>(prehash);
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
	 * Provides standard MD5 digests. Note that use of this algorithm should be
	 * restricted to cases where compatibility mandates it.
	 *
	 * @return a source of MD5 digests
	 */

	static HashDigest MD5() {
		return StandardHashDigest.MD5();
	}

	/**
	 * Provides a standard SHA-1 digests.
	 *
	 * @return a source of SHA-1 digests
	 */

	static HashDigest SHA_1() {
		return StandardHashDigest.SHA_1();
	}

	/**
	 * Provides a standard SHA-256 digests.
	 *
	 * @return a source of SHA-256 digests
	 */

	static HashDigest SHA_256() {
		return StandardHashDigest.SHA_256();
	}

	/**
	 * Creates a digests using the specified algorithm based on the
	 * platform's default provider.
	 *
	 * @param algorithm
	 *            the name of the digest algorithm
	 * @return a source of the specified digests
	 * @throws NoSuchAlgorithmException
	 *             if there is no digest provided for the specified algorithm
	 */

	static HashDigest digest(String algorithm) throws NoSuchAlgorithmException {
		if (algorithm == null) throw new IllegalArgumentException("null algorithm");
		return new StandardHashDigest(algorithm);
	}

	/**
	 * Creates a digests for the specified algorithm based on the supplied
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

	static HashDigest digest(String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
		if (algorithm == null) throw new IllegalArgumentException("null algorithm");
		if (provider == null) throw new IllegalArgumentException("null provider");
		return new StandardHashDigest(algorithm, provider);
	}

	/**
	 * Attempts to derive digests by cloning the supplied digest.
	 *
	 * @param digest
	 *            the digest to be cloned
	 * @return a source of digests based on cloning
	 */

	static HashDigest digest(MessageDigest digest) {
		if (digest == null) throw new IllegalArgumentException("null digest");
		return new StandardHashDigest(digest);
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
