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

final class SizedBigHasher<T> extends SizedHasher<T> {

	SizedBigHasher(Hashing<T> hashing, HashSize newSized) {
		super(hashing, newSized);
	}

	@Override
	int sizedIntValue(HashCode code) {
		return sizedBigValue(code).intValue();
	}

	@Override
	long sizedLongValue(HashCode code) {
		return sizedBigValue(code).longValue();
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