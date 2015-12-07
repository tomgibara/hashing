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


/**
 * <p>
 * Implementations of this interface generate hash codes from objects. Depending
 * upon the implementation, null values may be supported.
 * </p>
 *
 * @author Tom Gibara
 *
 * @param <T>
 *            the type of object for which hashes are generated
 */

public interface Hasher<T> extends Hashing<T> {

	// TODO can/should we stretch to make hash fit larger size?
	/**
	 * Derives a new hasher that generates hash codes within a different
	 * (generally smaller) range. Unless the new size divides the size of this
	 * hasher, there is no guarantee that values will be evenly distributed over
	 * new range. At present, no attempt is made to distribute the hash values
	 * evenly over a larger range.
	 * 
	 * @param newSize
	 *            specifies a size for the derived hasher
	 * @return a hasher that redistributes hash values within the specified
	 *         range
	 */

	default Hasher<T> sized(HashSize newSize) {
		if (newSize == null) throw new IllegalArgumentException("null newSize");
		final HashSize oldSize = getSize();
		if (oldSize.equals(newSize)) return this;
		if (oldSize.isIntCapacity()) return new SizedIntHasher<>(this, newSize);
		if (oldSize.isLongCapacity()) return new SizedLongHasher<>(this, newSize);
		if (newSize.isPowerOfTwo()) return new SizedBytesHasher<>(this, newSize);
		return new SizedBigHasher<>(this, newSize);
	}
	
	/**
	 * Derives a hasher that produces a fixed quantity of distinct hash codes
	 * within a specified range. To ensure that all value combinations are
	 * possible, the size of this hasher must meet or exceed 'size choose
	 * quantity'.
	 * 
	 * @param quantity
	 *            the number of hash values derived from a single hash code of
	 *            this hasher
	 * @param size
	 *            the range over which distinct values will be generated
	 * @return a hasher that generates multiple distinct hash values
	 * @throws IllegalArgumentException
	 *             if the size is not integer sized or the quantity is outside
	 *             the range [1,size]
	 */
	
	default Hasher<T> distinct(int quantity, HashSize size) throws IllegalArgumentException {
		if (quantity < 1) throw new IllegalArgumentException("non-positive quantity");
		if (size == null) throw new IllegalArgumentException("null size");
		if (!size.isIntSized()) throw new IllegalArgumentException("size not int sized");
		if (quantity > size.asInt()) throw new IllegalArgumentException("quantity exceeds size of size");
		return new DistinctHasher<>(size, quantity, this);
	}
	
	/**
	 * Derives a hasher that produces an endless stream of hash values in the
	 * range {@link HashSize#INT_SIZE} generated from a single hash value
	 * created by this hasher. It is recommended that any hasher used to derive
	 * values in this way should have a size which is at least
	 * {@link HashSize#INT_SIZE}.
	 * 
	 * @return a hasher that derives an endless stream of hash values for each
	 *         object hashed by this hasher.
	 */

	default Hasher<T> ints() {
		return new IntsHasher<>(this);
	}

	/**
	 * Derives a hasher that produces an endless stream of hash values in the
	 * range {@link HashSize#LONG_SIZE} generated from a single hash value
	 * created by this hasher. It is recommended that any hasher used to derive
	 * values in this way should have a size which is at least
	 * {@link HashSize#LONG_SIZE}.
	 * 
	 * @return a hasher that derives an endless stream of hash values for each
	 *         object hashed by this hasher.
	 */

	default Hasher<T> longs() {
		return new LongsHasher<>(this);
	}

}
