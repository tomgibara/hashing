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

class RerangedHasher<T> extends AdaptedHashing<T> implements Hasher<T> {

	final HashRange oldRange;
	final HashRange newRange;
	final boolean isSmaller;

	final BigInteger bigOldMin;
	final BigInteger bigNewMin;
	final BigInteger bigNewSize;


	RerangedHasher(Hashing<T> hashing, HashRange newRange) {
		super(hashing);
		this.newRange = newRange;
		oldRange = hashing.getRange();
		isSmaller = newRange.getSize().compareTo(oldRange.getSize()) < 0;
		
		bigOldMin = oldRange.getMinimum();
		bigNewMin = newRange.getMinimum();
		bigNewSize = newRange.getSize();
	}
	
	@Override
	public HashValue hashValue(T value) {
		HashValue hashValue = hashing.hashValue(value);
		return new AbstractHashValue() {

			@Override
			public BigInteger bigValue() {
				return adapt(hashValue.bigValue());
			}
			
			@Override
			public long longValue() {
				return adapt(hashValue.longValue());
			}
			
			@Override
			public int intValue() {
				return adapt(hashValue.intValue());
			}
		};
	}
	
	@Override
	public HashRange getRange() {
		return newRange;
	}

}
