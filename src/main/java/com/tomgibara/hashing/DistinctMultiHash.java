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

public class DistinctMultiHash<E> extends AbstractMultiHash<E> {

	public static BigInteger requiredHashSize(int max, int multiplicity) {
		return Choose.from(max, multiplicity).asBigInt();
	}
	
	//TODO could keep reference to choices instead
	private final Choose choose;
	private final HashRange range;
	private final Hasher<E> hash;
	private final boolean longSized;
	
	public DistinctMultiHash(int max, int multiplicity, Hasher<E> hash) {
		if (max < 0) throw new IllegalArgumentException();
		if (multiplicity > max) throw new IllegalArgumentException();
		
		final Choose choose = Choose.from(max, multiplicity);
		final HashRange range = new HashRange(BigInteger.ZERO, choose.asBigInt().subtract(BigInteger.ONE));
		
		this.choose = choose;
		this.range = new HashRange(0, max);
		this.hash = Hashes.rangeAdjust(range, Hashes.asMultiHash(hash));
		longSized = range.isLongSized();
	}
	
	@Override
	public HashRange getRange() {
		return range;
	}
	
	@Override
	public int getMaxMultiplicity() {
		return choose.getK();
	}

	@Override
	public int[] hashAsInts(E value, int[] array) {
		Choices choices = choose.choices();
		if (longSized) {
			choices.choiceAsArray(hash.longHashValue(value), array);
		} else {
			choices.choiceAsArray(hash.bigHashValue(value), array);
		}
		return array;
	}
	
	@Override
	public long[] hashAsLongs(E value, long[] array) {
		return copy(hashAsInts(value, array.length), array);
	}
	
	@Override
	public BigInteger[] hashAsBigInts(E value, BigInteger[] array) {
		return copy(hashAsInts(value, array.length), array);
	}
	
}