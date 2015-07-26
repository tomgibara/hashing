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

final class LongsHashValue extends AbstractHashValue {

	private final long[] longValues;
	private int index = 0;

	LongsHashValue(long[] longValues) {
		this.longValues = longValues;
	}

	@Override
	public int intValue() {
		return (int) longValue();
	}

	@Override
	public long longValue() {
		checkIndex();
		return longValues[index++];
	}

	@Override
	public BigInteger bigValue() {
		return HashSize.BIG_ULONG.add(BigInteger.valueOf(longValue()));
	}

	@Override
	public boolean hasNext() {
		return index < longValues.length;
	}

	private void checkIndex() {
		if (index == longValues.length) throw new NoSuchElementException();
	}

}
