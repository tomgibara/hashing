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

//TODO should values be checked against size?
final class IntsHashCode extends AbstractHashCode {

	private final int[] intValues;
	private int index = 0;

	IntsHashCode(int... intValues) {
		super(HashSize.INT_SIZE);
		this.intValues = intValues;
	}

	IntsHashCode(HashSize size, int... intValues) {
		super(size);
		if (!size.isIntCapacity()) throw new IllegalArgumentException("size exceeds int capacity");
		this.intValues = intValues;
	}

	@Override
	public int intValue() {
		checkIndex();
		return intValues[index++];
	}

	@Override
	public long longValue() {
		checkIndex();
		return intValues[index++];
	}

	@Override
	public BigInteger bigValue() {
		checkIndex();
		return BigInteger.valueOf(intValues[index++]);
	}

	@Override
	public boolean hasNext() {
		return index < intValues.length;
	}

	private void checkIndex() {
		if (index == intValues.length) throw new NoSuchElementException();
	}

}
