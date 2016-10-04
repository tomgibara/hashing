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

import com.google.common.hash.HashFunction;

public class Murmur3IntHashTest extends HashingTest {

	public void testMatchesGuava() {
		Random r = new Random(0L);
		Hasher<byte[]> hasher = Hashing.murmur3Int().hasher((bs, s) -> s.writeBytes(bs));
		HashFunction hash = com.google.common.hash.Hashing.murmur3_32();

		// test empty
		{
			byte[] b = new byte[0];
			assertEquals(hash.hashBytes(b).asInt(), hasher.intHashValue(b));
		}

		// test single byte
		{
			byte[] b = new byte[1];
			for (byte i = -128; i < 127; i++) {
				b[0] = i;
				assertEquals(hash.hashBytes(b).asInt(), hasher.intHashValue(b));
			}
		}

		for (int i = 0; i < 10000; i++) {
			byte[] bytes = new byte[r.nextInt(100)];
			r.nextBytes(bytes);
			int h1 = hash.hashBytes(bytes).asInt();
			int h2 = hasher.intHashValue(bytes);
			assertEquals(h1, h2);
		}
	}

	public void testCorrectlySized() {
		Hasher<Integer> hasher = Hashing.murmur3Int().hasher((i, s) -> s.writeInt(i));
		HashSize size = hasher.getSize();
		for (int i = 0; i < 1000; i++) {
			testCorrectlySizedInts(hasher.hash(i), size, 1);
			testCorrectlySizedLongs(hasher.hash(i), size, 1);
			testCorrectlySizedBigs(hasher.hash(i), size, 1);
			testCorrectlySizedBytes(hasher.hash(i), size, 1);
		}
	}

	public void testDistribution() {
		Hasher<Integer> hasher = Hashing.murmur3Int().hasher((i, s) -> s.writeInt(i));
		int[] ints = new int[10000];
		for (int i = 0; i < ints.length; i++) {
			ints[i] = hasher.intHashValue(i);
		}
		testDistribution(ints);
	}

	public void testConsistency() {
		Hasher<Integer> hasher = Hashing.murmur3Int().hasher((i, s) -> s.writeInt(i));
		for (int i = 0; i < 1000; i++) {
			testConsistent(hasher, i);
		}
	}
}
