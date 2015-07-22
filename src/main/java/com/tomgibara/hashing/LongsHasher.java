/*
 * Copyright 2015 Tom Gibara, Benjamin Manes
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

class LongsHasher<T> implements Hasher<T> {

	//https://code.google.com/p/fast-hash/
	private static long spread(long hashCode) {
		hashCode ^= hashCode >> 23;
		hashCode *= 0x2127599bf4325c37L;
		hashCode ^= hashCode >> 47;
		return hashCode;
	}
	
	private final Hasher<T> hasher;

	public LongsHasher(Hasher<T> hasher) {
		hasher = hasher.ranged(HashRange.FULL_LONG_RANGE);
		this.hasher = hasher;
	}
	
	@Override
	public HashRange getRange() {
		return HashRange.FULL_LONG_RANGE;
	}
	
	@Override
	public int getQuantity() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public HashValue hashValue(T value) {
		return new MultiHashValue(hasher.hashValue(value).intValue());
	}
	
	private static class MultiHashValue extends AbstractHashValue {

		private final long probe;
		private final long h;
		private int i = 0;

		MultiHashValue(long hashCode) {
			probe = hashCode == Long.MIN_VALUE ? 1L : 1 + Math.abs(hashCode);
			h = spread(hashCode);
		}

		@Override
		public int intValue() {
			long longValue = longValue();
			int intValue = (int) longValue;
			if (intValue != longValue) throw new ArithmeticException("hash value too large");
			return intValue;
		}

		@Override
		public long longValue() {
			return h ^ i * probe;
		}

		@Override
		public BigInteger bigValue() {
			return BigInteger.valueOf(longValue());
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
		if (!(obj instanceof LongsHasher<?>)) return false;
		LongsHasher<?> that = (LongsHasher<?>) obj;
		return this.hasher.equals(that.hasher);
	}
	
}
