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
import java.util.NoSuchElementException;

final class BigsHashCode extends AbstractHashCode {

	private final BigInteger[] bigValues;
	private int index = 0;

	BigsHashCode(HashSize size, BigInteger... bigValues) {
		super(size);
		this.bigValues = bigValues;
	}

	@Override
	public BigInteger bigValue() {
		checkIndex();
		return bigValues[index++];
	}

	@Override
	public byte[] bytesValue() {
		return bigToBytes(size.getBytes(), bigValue());
	}

	@Override
	public boolean hasNext() {
		return index < bigValues.length;
	}

	private void checkIndex() {
		if (index == bigValues.length) throw new NoSuchElementException();
	}

}
