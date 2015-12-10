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

final class BigHashCode extends AbstractHashCode {

	private final BigInteger bigValue;

	@Override
	public boolean hasNext() {
		return true;
	}

	public BigHashCode(HashSize size, BigInteger bigValue) {
		super(size);
		if (bigValue == null) throw new IllegalArgumentException("null bigValue");
		this.bigValue = bigValue;
	}

	@Override
	public BigInteger bigValue() {
		return bigValue;
	}

	@Override
	public byte[] bytesValue() {
		return bigToBytes(size.getBytes(), bigValue);
	}
}
