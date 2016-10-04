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

public class JavaHasherTest extends HashingTest {

	public void testHashCodeCorrect() {
		Hasher<String> hasher = Hashing.objectHasher();
		HashSize size = hasher.getSize();
		assertEquals(HashSize.INT_SIZE, size);
		for (int i = 100000; i < 110000; i += 10) {
			String str = Integer.toString(i);
			assertEquals(str.hashCode(), hasher.intHashValue(str));
			testConsistent(hasher, str);
			testCorrectlySizedInts(hasher.hash(str), size, 1);
			testCorrectlySizedLongs(hasher.hash(str), size, 1);
			testCorrectlySizedBigs(hasher.hash(str), size, 1);
			testCorrectlySizedBytes(hasher.hash(str), size, 1);
		}
	}

	public void testIndentityCorrect() {
		Hasher<String> hasher = Hashing.identityHasher();
		HashSize size = hasher.getSize();
		assertEquals(HashSize.INT_SIZE, size);
		for (int i = 100000; i < 110000; i += 10) {
			String str = Integer.toString(i);
			assertEquals(System.identityHashCode(str), hasher.intHashValue(str));
			testConsistent(hasher, str);
			testCorrectlySizedInts(hasher.hash(str), size, 1);
			testCorrectlySizedLongs(hasher.hash(str), size, 1);
			testCorrectlySizedBigs(hasher.hash(str), size, 1);
			testCorrectlySizedBytes(hasher.hash(str), size, 1);
		}
	}

}
