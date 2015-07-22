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

	// TODO can we stretch to make hash fit larger range?
	default Hasher<T> ranged(HashRange newRange) {
		if (newRange == null) throw new IllegalArgumentException("null newRange");
		final HashRange oldRange = getRange();
		if (oldRange.equals(newRange)) return this;
		if (newRange.isIntBounded() && newRange.isIntSized() && oldRange.isIntBounded() && oldRange.isIntSized()) return new IntRerangedHasher<T>(this, newRange);
		if (newRange.isLongBounded() && newRange.isLongSized() && oldRange.isLongBounded() && oldRange.isLongSized()) return new LongRerangedHasher<T>(this, newRange);
		return new BigRerangedHasher<>(this, newRange);
	}
	
	default Hasher<T> distinct(int quantity, HashRange range) {
		if (quantity < 1) throw new IllegalArgumentException("non-positive quantity");
		if (range == null) throw new IllegalArgumentException("null range");
		if (!range.isIntSized()) throw new IllegalArgumentException("range not int sized");
		if (!range.isIntBounded()) throw new IllegalArgumentException("range not int bounded");
		if (quantity > range.getSize().intValue()) throw new IllegalArgumentException("quantity exceeds size of range");
		return new DistinctHasher<>(range, quantity, this);
	}
	
	default Hasher<T> ints() {
		return new IntsHasher<>(this);
	}
	
	default Hasher<T> longs() {
		return new LongsHasher<>(hasher);
	}
	
}
