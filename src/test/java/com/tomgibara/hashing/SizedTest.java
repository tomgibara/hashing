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
import java.util.Arrays;
import java.util.Random;

import org.junit.Assert;

public class SizedTest extends HashingTest {

	Hasher<Integer> hasher = Hashing.prng(HashSize.fromByteLength(16)).hasher( (x, s) -> s.writeInt(x) );

	public void testCorrectlySized() {
		for (int i = 1; i < 9; i++) {
			testCorrectlySized(HashSize.fromByteLength(i));
		}
		Random r = new Random(0L);
		for (int i = 10; i < 120; i += 10) {
			BigInteger prime = BigInteger.probablePrime(i, r);
			testCorrectlySized(HashSize.fromBig(prime));
		}
	}

	private void testCorrectlySized(HashSize size) {
		Hasher<Integer> h = hasher.sized(size);
		assertEquals(size, h.getSize());
		for (int i = 0; i < 10; i++) {
			HashCode value = h.hash(i);
			testCorrectlySizedInts(value, size, 1000);
			testCorrectlySizedLongs(value, size, 1000);
			testCorrectlySizedBigs(value, size, 1000);
		}
	}
	
	public void testMultipleValues() {
		int count = 10;
		HashCode hash = hasher.hash(0);
		int[] expected = new int[count];
		for (int i = 0; i < count; i++) {
			expected[i] = hash.intValue() & 0xffff;
		}
		Hasher<Integer> sized = hasher.sized(HashSize.SHORT_SIZE);
		HashCode sizedHash = sized.hash(0);
		int[] actual = new int[count];
		for (int i = 0; i < count; i++) {
			actual[i] = sizedHash.intValue();
		}
		Assert.assertArrayEquals(expected, actual);
	}

}
