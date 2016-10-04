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
import java.util.Arrays;

abstract class AbstractHashCode implements HashCode {

	static byte[] intToBytes(int v) {
		return new byte[] {
				(byte) (v >> 24),
				(byte) (v >> 16),
				(byte) (v >>  8),
				(byte) (v      )
		};
	}

	static byte[] longToBytes(long v) {
		return new byte[] {
			(byte) (v >> 56),
			(byte) (v >> 48),
			(byte) (v >> 40),
			(byte) (v >> 32),
			(byte) (v >> 24),
			(byte) (v >> 16),
			(byte) (v >>  8),
			(byte) (v      )
		};
	}

	static byte[] bigToBytes(int len, BigInteger v) {
		byte[] bytes = v.toByteArray();
		if (bytes.length == len) return bytes;
		if (bytes.length > len) {
			return Arrays.copyOfRange(bytes, bytes.length - len, bytes.length);
		}
		byte[] bs = new byte[len];
		System.arraycopy(bytes, 0, bs, len - bytes.length, bytes.length);
		return bs;
	}

	static int intFromBytes(byte[] bs) {
		int len = bs.length;
		switch (len) {
		case 0:
			return 0;
		case 1:
			return bs[0] & 0xff;
		case 2:
			return
					((bs[0] & 0xff) <<  8) |
					((bs[1] & 0xff)      ) ;
		case 3:
			return
					((bs[0] & 0xff) << 16) |
					((bs[1] & 0xff) <<  8) |
					((bs[2] & 0xff)      ) ;
		default:
			return
					((bs[len - 4]       ) << 24) |
					((bs[len - 3] & 0xff) << 16) |
					((bs[len - 2] & 0xff) <<  8) |
					((bs[len - 1] & 0xff)      ) ;
		}
	}

	static long longFromBytes(byte[] bs) {
		int len = bs.length;
		if (len >= 8) {
			return
					((bs[len - 8] & 0xffL) << 56) |
					((bs[len - 7] & 0xffL) << 48) |
					((bs[len - 6] & 0xffL) << 40) |
					((bs[len - 5] & 0xffL) << 32) |
					((bs[len - 4] & 0xffL) << 24) |
					((bs[len - 3] & 0xffL) << 16) |
					((bs[len - 2] & 0xffL) <<  8) |
					((bs[len - 1] & 0xffL)      ) ;
		}
		if (len == 0) return 0L;
		long acc = bs[0] & 0xff;
		for (int i = 1; i < len; i++) {
			acc = (acc << 8) | (bs[i] & 0xffL);
		}
		return acc;
	}

	static BigInteger bigFromBytes(byte[] bs) {
		return new BigInteger(1, bs);
	}

	final HashSize size;

	AbstractHashCode(HashSize size) {
		if (size == null) throw new IllegalArgumentException("null size");
		this.size = size;
	}

	byte[] trim(byte[] bytes) {
		int length = bytes.length;
		int bs = size.getBytes();
		return bs == bytes.length ? bytes : Arrays.copyOfRange(bytes, length - bs, length);
	}

	@Override
	public HashSize size() {
		return size;
	}

	@Override
	public int hashCode() {
		return intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof HashCode)) return false;
		return this.bigValue().equals(((HashCode) obj).bigValue());
	}

	@Override
	public String toString() {
		return bigValue().toString();
	}

}
