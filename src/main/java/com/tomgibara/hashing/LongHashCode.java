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

final class LongHashCode extends AbstractHashCode {

	private final long longValue;

	public LongHashCode(long longValue) {
		super(HashSize.LONG_SIZE);
		this.longValue = longValue;
	}

	public LongHashCode(HashSize size, long longValue) {
		super(size);
		if (!size.isLongCapacity()) throw new IllegalArgumentException("size exceeds long capacity");
		this.longValue = longValue;
	}
	
	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public int intValue() {
		return (int) longValue;
	}

	@Override
	public long longValue() {
		return longValue;
	}

	@Override
	public BigInteger bigValue() {
		return BigInteger.valueOf(longValue);
	}

	@Override
	public byte[] bytesValue() {
		return longToBytes(longValue);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj instanceof LongHashCode) return this.longValue == ((LongHashCode) obj).longValue;
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return Long.toString(longValue);
	}

}
