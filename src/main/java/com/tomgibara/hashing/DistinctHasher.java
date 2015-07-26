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

import com.tomgibara.choose.Choices;
import com.tomgibara.choose.Choose;

class DistinctHasher<E> implements Hasher<E> {

	public static BigInteger requiredHashSize(int max, int multiplicity) {
		return Choose.from(max, multiplicity).asBigInt();
	}

	//TODO could keep reference to choices instead
	private final Choose choose;
	private final HashSize size;
	private final Hasher<E> hasher;
	private final boolean longSized;

	DistinctHasher(HashSize size, int multiplicity, Hasher<E> hasher) {
		int intSize = size.asInt();
		this.choose = Choose.from(intSize, multiplicity);
		HashSize choiceSize = new HashSize(choose.asBigInt());
		this.hasher = hasher.sized(choiceSize);
		this.size = size;
		longSized = choiceSize.isLongSized();
	}

	@Override
	public HashSize getSize() {
		return size;
	}

	@Override
	public int getQuantity() {
		return choose.getK();
	}

	@Override
	public HashCode hash(E value) throws IllegalArgumentException {
		Choices choices = choose.choices();
		int[] intValues = longSized ?
				choices.choiceAsArray(hasher.longHashValue(value)) :
				choices.choiceAsArray(hasher.bigHashValue(value));
		return new IntsHashCode(intValues);
	}
}
