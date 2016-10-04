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

import java.util.Random;


public class DistinctHasherTest extends HashingTest {

	public void testDistinct() {
		Hasher<Object> hasher = Hashing.identityHasher().distinct(3, HashSize.fromInt(1000));
		int[] ints = new int[3];
		for (int i = 0; i < 100000; i++) {
			HashCode value = hasher.hash(i);
			ints[0] = value.intValue();
			assertTrue(value.hasNext());
			ints[1] = value.intValue();
			assertTrue(value.hasNext());
			ints[2] = value.intValue();
			assertFalse(value.hasNext());
			checkDistinct3(ints);
		}
	}

	public void testCorrectlySized() {
		Hasher<Object> hasher = Hashing.identityHasher().distinct(3, HashSize.fromInt(1000));
		for (int i = 0; i < 1000; i++) {
			testCorrectlySizedInts(hasher.hash(i), hasher.getSize(), 3);
			testCorrectlySizedLongs(hasher.hash(i), hasher.getSize(), 3);
			testCorrectlySizedBigs(hasher.hash(i), hasher.getSize(), 3);
			testCorrectlySizedBytes(hasher.hash(i), hasher.getSize(), 3);
		}
	}

	private void checkDistinct3(int[] ints) {
		if (ints.length != 3) throw new IllegalArgumentException();
		if (ints[0] == ints[1]) fail("duplicate at 0 and 1");
		if (ints[1] == ints[2]) fail("duplicate at 1 and 2");
		if (ints[2] == ints[0]) fail("duplicate at 0 and 2");
	}

	public void testConsistent() {
		Random r = new Random(0L);
		for (int i = 1; i < 16; i++) {
			Hasher<Object> hasher = Hashing.identityHasher().distinct(2, HashSize.fromInt(1 << i));
			for (int j = 0; j < 100; j++) {
				testConsistent(hasher,r.nextInt());
			}
		}
	}

}
