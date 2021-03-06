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

import com.tomgibara.streams.WriteStream;

// See http://smhasher.googlecode.com/svn/trunk/MurmurHash3.cpp
// based on http://guava-libraries.googlecode.com/git/guava/src/com/google/common/hash/Murmur3_32HashFunction.java

final class Murmur3IntHash implements Hash {

	static final int c1 = 0xcc9e2d51;
	static final int c2 = 0x1b873593;

	private static Murmur3IntHash instance = new Murmur3IntHash(0);

	static Murmur3IntHash instance() { return instance; }

	static Murmur3IntHash instance(int seed) { return seed == 0 ? instance : new Murmur3IntHash(seed); }

	private final int seed;

	private Murmur3IntHash(int seed) {
		this.seed = seed;
	}

	@Override
	public HashSize getSize() {
		return HashSize.INT_SIZE;
	}

	@Override
	public WriteStream newStream() {
		return new MurmurStream(seed);
	}

	@Override
	public int intHashValue(WriteStream s) {
		return cast(s).hash();
	}

	@Override
	public long longHashValue(WriteStream s) {
		return cast(s).hash() & 0xffffffffL;
	}

	@Override
	public BigInteger bigHashValue(WriteStream s) {
		return BigInteger.valueOf(longHashValue(s));
	}

	@Override
	public byte[] bytesHashValue(WriteStream s) {
		return AbstractHashCode.intToBytes(intHashValue(s));
	}

	@Override
	public HashCode hash(WriteStream s) {
		return new IntHashCode(cast(s).hash());
	}

	// object methods

	@Override
	public int hashCode() {
		return seed;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof Murmur3IntHash)) return false;
		Murmur3IntHash that = (Murmur3IntHash) obj;
		return this.seed == that.seed;
	}

	@Override
	public String toString() {
		return "Murmur3 32 bits with seed " + seed;
	}

	// private helper methods

	private MurmurStream cast(WriteStream stream) {
		return (MurmurStream) stream;
	}

	// inner classes

	static class MurmurStream implements WriteStream {

		private int k1;
		private int h1;
		private int len;

		MurmurStream(int seed) {
			k1 = 0;
			h1 = seed;
			len = 0;
		}

		@Override
		public void writeByte(byte v) {
			k1 = (v << 24) | (k1 >>> 8);

			// process body

			if (((++len) & 3) == 0) {
				k1 *= c1;
				k1 = Integer.rotateLeft(k1, 15);
				k1 *= c2;

				h1 ^= k1;
				h1 = Integer.rotateLeft(h1, 13);
				h1 = h1 * 5 + 0xe6546b64;
			}
		}

		int hash() {

			// process tail

			int rem = len & 3;
			if (rem != 0) {
				k1 >>>= (4 - rem) << 3;

				k1 *= c1;
				k1 = Integer.rotateLeft(k1, 15);
				k1 *= c2;
				h1 ^= k1;
			}

			// finalize

			h1 ^= len;

			h1 ^= h1 >>> 16;
			h1 *= 0x85ebca6b;
			h1 ^= h1 >>> 13;
			h1 *= 0xc2b2ae35;
			h1 ^= h1 >>> 16;

			// return

			return h1;
		}

	}

}
