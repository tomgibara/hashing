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
package com.tomgibara.hashing.legacy;

import com.tomgibara.hashing.SizedBigHasher;
import com.tomgibara.hashing.HashSize;
import com.tomgibara.hashing.Hasher;
import com.tomgibara.hashing.SizedIntHasher;
import com.tomgibara.hashing.SizedLongHasher;

/**
 * Utility methods for working with objects in this package.
 * 
 * @author tomgibara
 *
 */

public class Hashes {

	private Hashes() {}

	/**
	 * Returns a {@link MultiHash} implementation that returns hashes in a
	 * specified range, based on the hash values produced by another
	 * {@link MultiHash} implementation. If the range is being widened, there is
	 * no guarantee that every value in the new range will be used. If the new
	 * range is equal to that of the supplied {@link MultiHash}, the original
	 * hash will be returned, unmodified. To use this method with a plain
	 * {@link Hasher}, first pass it to {@link #asMultiHash(Hasher)}.
	 * 
	 * @param <T>
	 *            the type of objects for which hashes may be generated
	 * @param newRange
	 *            the range to which generated hash values should be constrained
	 * @param multiHash
	 *            supplies the hash values
	 * @return a {@link MultiHash} that returns values within the specified
	 *         range
	 * @throws IllegalArgumentException
	 *             if any of the arguments to the method is null
	 */
	
	public static <T> MultiHash<T> rangeAdjust(HashSize newRange, MultiHash<T> multiHash) throws IllegalArgumentException {
		if (newRange == null) throw new IllegalArgumentException("null newRange");
		if (multiHash == null) throw new IllegalArgumentException("null multiHash");
		final HashSize oldRange = multiHash.getSize();
		if (oldRange.equals(newRange)) return multiHash;
		if (newRange.isIntBounded() && newRange.isIntSized() && oldRange.isIntBounded() && oldRange.isIntSized()) return new IntRerangedHasher<T>(multiHash, newRange);
		if (newRange.isLongBounded() && newRange.isLongSized() && oldRange.isLongBounded() && oldRange.isLongSized()) return new LongRerangedHasher<T>(multiHash, newRange);
		return new BigRerangedHasher<T>(multiHash, newRange);
	}
	
	/**
	 * Adapts a {@link Hasher} implementation into a {@link MultiHash}
	 * implementation. If the supplied object already implements the
	 * {@link MultiHash} interface, the original object is returned, otherwise,
	 * a new object will be created that returns the same hash values through
	 * the {@link MultiHash} interface.
	 * 
	 * @param <T>
	 *            the type of objects for which hashes may be generated
	 * @param hash
	 *            the {@link Hasher} implementation for which a {@link MultiHash}
	 *            is needed
	 * @return a {@link MultiHash} implementation that returns the hash values
	 *         from the supplied {@link Hasher}
	 * @throws IllegalArgumentException
	 *             if the supplied hash is null
	 */
	
	public static <T> MultiHash<T> asMultiHash(Hasher<T> hash) {
		if (hash == null) throw new IllegalArgumentException("null hash");
		if (hash instanceof MultiHash<?>) return (MultiHash<T>) hash;
		return new SingletonMultiHash<T>(hash);
	}
	
	// methods for hashing primitives
	
	public static int hashCode(boolean value) {
        return value ? 1231 : 1237;
	}

	public static int hashCode(byte value) {
		return value;
	}
	
	public static int hashCode(short value) {
		return value;
	}
	
	public static int hashCode(char value) {
		return value;
	}

	public static int hashCode(int value) {
		return value;
	}
	
	public static int hashCode(long value) {
        return (int)(value ^ (value >>> 32));
	}

	public static int hashCode(float value) {
        return Float.floatToIntBits(value);
	}
	
	public static int hashCode(double value) {
        long bits = Double.doubleToLongBits(value);
        return (int)(bits ^ (bits >>> 32));
	}
	
}
