package com.tomgibara.hashing;

import java.math.BigInteger;

public interface Hashing<T> {

	static <T> Hasher<T> objectHasher() {
		return JavaHasher.object();
	}
	
	static <T> Hasher<T> identityHasher() {
		return JavaHasher.identity();
	}
	
	static Hash<?> murmur3Int() {
		return MurmurIntHash.instance();
	}
	
	static Hash<?> murmur3Int(int seed) {
		return MurmurIntHash.instance(seed);
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
