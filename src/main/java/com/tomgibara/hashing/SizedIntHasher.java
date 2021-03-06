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

final class SizedIntHasher<T> extends SizedHasher<T> {

	SizedIntHasher(Hashing<T> hashing, HashSize newSize) {
		super(hashing, newSize);
	}

	@Override
	public int intHashValue(T value) {
		int h = hashing.intHashValue(value);
		return isSmaller ? newSize.mapInt(h) : h;
	}

	@Override
	int sizedIntValue(HashCode code) {
		int h = code.intValue();
		return isSmaller ? newSize.mapInt(h) : h;
	}

	@Override
	long sizedLongValue(HashCode code) {
		long h = code.longValue();
		return isSmaller ? newSize.mapLong(h) : h;
	}

	@Override
	BigInteger sizedBigValue(HashCode code) {
		BigInteger h = code.bigValue();
		return isSmaller ? newSize.mapBig(h) : h;
	}

	@Override
	byte[] sizedBytesValue(HashCode code) {
		byte[] h = code.bytesValue();
		return isSmaller ? newSize.mapBytes(h) : h;
	}

}
