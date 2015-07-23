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
 * Implementations of this interface can generate one hash value for a given
 * object. Depending upon the implementation, null values may be supported.
 * </p>
 * 
 * @author tomgibara
 * 
 * @param <T>
 *            the type of objects for which hashes may be generated
 */

public interface Hasher<T> extends Hashing<T> {

	// TODO can we stretch to make hash fit larger size?
	default Hasher<T> sized(HashSize newSize) {
		if (newSize == null) throw new IllegalArgumentException("null newSize");
		final HashSize oldSize = getSize();
		if (oldSize.equals(newSize)) return this;
		if (oldSize.isIntCapacity()) return new SizedIntHasher<>(this, newSize);
		if (oldSize.isLongCapacity()) return new SizedLongHasher<>(this, newSize);
		return new SizedBigHasher<>(this, newSize);
	}
	
	default Hasher<T> distinct(int quantity, HashSize size) {
		if (quantity < 1) throw new IllegalArgumentException("non-positive quantity");
		if (size == null) throw new IllegalArgumentException("null size");
		if (!size.isIntSized()) throw new IllegalArgumentException("size not int sized");
		if (quantity > size.getSize().intValue()) throw new IllegalArgumentException("quantity exceeds size of size");
		return new DistinctHasher<>(size, quantity, this);
	}
	
	default Hasher<T> ints() {
		return new IntsHasher<>(this);
	}
	
	default Hasher<T> longs() {
		return new LongsHasher<>(this);
	}
	
}
