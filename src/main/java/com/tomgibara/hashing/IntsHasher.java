/*
 * Copyright 2010 Tom Gibara, Benjamin Manes
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

import com.tomgibara.hashing.legacy.AbstractMultiHash;

/**
 * A MultiHash implementation that uses double-hashing to generate an arbitrary
 * number of hash-values based on an integer hash value. This implementation is
 * derived from
 * 
 * http://code.google.com/p/concurrentlinkedhashmap/wiki/BloomFilter
 * by Benjamin Manes.
 * 
 * @author tomgibara
 * 
 * @param <T>
 *            the type of objects for which hashes will be generated
 */

class IntsHasher<T> implements Hasher<T> {

	private static int spread(int hashCode) {
		// Spread bits using variant of single-word Wang/Jenkins hash
		hashCode += (hashCode <<  15) ^ 0xffffcd7d;
		hashCode ^= (hashCode >>> 10);
		hashCode += (hashCode <<   3);
		hashCode ^= (hashCode >>>  6);
		hashCode += (hashCode <<   2) + (hashCode << 14);
		return hashCode ^ (hashCode >>> 16);
	}
	
	private final Hasher<T> hasher;

	public IntsHasher(Hasher<T> hasher) {
		hasher = hasher.ranged(HashRange.FULL_INT_RANGE);
		this.hasher = hasher;
	}
	
	@Override
	public HashRange getRange() {
		return HashRange.FULL_INT_RANGE;
	}
	
	@Override
	public int getQuantity() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public HashValue hashValue(T value) {
		return new MultiHashValue(hasher.hashValue(value).intValue());
	}
	
	private final static class MultiHashValue extends AbstractHashValue {

		private final int probe;
		private final int h;
		private int i = 0;

		MultiHashValue(int hashCode) {
			probe = hashCode == Integer.MIN_VALUE ? 1 : 1 + Math.abs(hashCode);
			h = spread(hashCode);
		}

		@Override
		public int intValue() {
			return h ^ i * probe;
		}

		@Override
		public long longValue() {
			return intValue();
		}

		@Override
		public BigInteger bigValue() {
			return BigInteger.valueOf(intValue());
		}

		@Override
		public MultiHashValue next() {
			i++;
			return this;
		}

		@Override
		public boolean hasNext() {
			return true;
		}

	}

	@Override
	public int hashCode() {
		return hasher.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof IntsHasher<?>)) return false;
		IntsHasher<?> that = (IntsHasher<?>) obj;
		return this.hasher.equals(that.hasher);
	}
	
}
