/*
 * Copyright 2011 Tom Gibara
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

class ResizedHasher<T> implements Hasher<T> {

	final Hashing<T> hashing;

	//TODO remove?
	final HashSize oldSize;
	final HashSize newSize;
	final boolean isSmaller;

	ResizedHasher(Hashing<T> hashing, HashSize newSize) {
		this.hashing = hashing;
		this.newSize = newSize;
		oldSize = hashing.getSize();
		isSmaller = newSize.getSize().compareTo(oldSize.getSize()) < 0;
	}

	@Override
	public HashSize getSize() {
		return newSize;
	}

	@Override
	public int getQuantity() {
		return hashing.getQuantity();
	}

	@Override
	public int intHashValue(T value) {
		return bigHashValue(value).intValue();
	}
	
	@Override
	public long longHashValue(T value) {
		return bigHashValue(value).longValue();
	}
	
	@Override
	public BigInteger bigHashValue(T value) {
		BigInteger h = hashing.bigHashValue(value);
		return isSmaller ? h.mod(newSize.getSize()) : h;
	}

	@Override
	public HashValue hashValue(T value) {
		return new AbstractHashValue() {

			@Override
			public BigInteger bigValue() {
				return bigHashValue(value);
			}

			@Override
			public long longValue() {
				return longHashValue(value);
			}

			@Override
			public int intValue() {
				return intHashValue(value);
			}
		};
	}
	
}
