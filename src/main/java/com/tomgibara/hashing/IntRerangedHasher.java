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

final class IntRerangedHasher<T> extends RerangedHasher<T> {

	private final int intOldMin;
	private final int intNewMin;
	private final int intNewSize;
	
	public IntRerangedHasher(Hashing<T> hashing, HashRange newRange) {
		super(hashing, newRange);
		intOldMin = bigOldMin.intValue();
		intNewMin = bigNewMin.intValue();
		intNewSize = bigNewSize.intValue();
	}
	
	@Override
	public int intHashValue(T value) {
		int h = hashing.intHashValue(value);
		h -= intOldMin;
		if (isSmaller) h = h % intNewSize;
		return h + intNewMin;
	}

	@Override
	public long longHashValue(T value) {
		long h = hashing.longHashValue(value);
		h -= intOldMin;
		if (isSmaller) h = h % intNewSize;
		return h + intNewMin;
	}
	
}
