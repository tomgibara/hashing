/*
 * Copyright 2010 Tom Gibara
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.tomgibara.hashing;

import java.math.BigInteger;

/**
 * <p>
 * Implementations of this interface can generate one hash value for a given
 * object. Depending upon the implementation, null values may be supported.
 * </p>
 * 
 * @author tomgibara
 * 
 * @param <T>
 *            the type of objects for which hashes may be generated
 */

public interface Hasher<T> {

	static <T> Hasher<T> from(final Hash hash, final HashStreamer<T> source) {
		if (hash == null) throw new IllegalArgumentException("null hash");
		if (source == null) throw new IllegalArgumentException("null source");
		return new Hasher<T>() {
			
			@Override
			public HashRange getRange() {
				return hash.getRange();
			}
			
			@Override
			public HashValue hashValue(T value) {
				return stream(value).hashValue();
			}
			
			@Override
			public BigInteger bigHashValue(T value) {
				return stream(value).bigHashValue();
			}
			
			@Override
			public long longHashValue(T value) {
				return stream(value).longHashValue();
			}
			
			@Override
			public int intHashValue(T value) {
				return stream(value).intHashValue();
			}
			
			private HashStream stream(T value) {
				HashStream stream = hash.newStream();
				source.stream(value, stream);
				return stream;
			}
			
			@Override
			public int getQuantity() {
				return hash.getQuantity();
			}
			
		};
	}
	
	HashRange getRange();

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
	 * for long-ranged hashes. This value is not guaranteed to lie within the
	 * indicated {@link HashRange} unless {@link HashRange#isLongRange()} is
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
	 * for integer-ranged hashes. This value is not guaranteed to lie within the
	 * indicated {@link HashRange} unless {@link HashRange#isIntRange()} is
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
