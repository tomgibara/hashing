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

import junit.framework.TestCase;

public abstract class HashingTest extends TestCase {

	void testCorrectlySizedInts(HashCode value, HashSize size, int quantity) {
		BigInteger s = size.asBig();
		// test ints
		int[] ints = new int[quantity];
		for (int j = 0; j < quantity; j++) {
			ints[j] = value.intValue();
		}
		for (int j = 0; j < quantity; j++) {
			BigInteger big = BigInteger.valueOf(ints[j] & 0xffffffffL);
			assertTrue(big + " < " + s, big.compareTo(s) < 0);
			assertTrue(big + " +ve", big.signum() >= 0);
		}
	}

	void testCorrectlySizedLongs(HashCode value, HashSize size, int quantity) {
		BigInteger s = size.asBig();
		// test longs
		long[] longs = new long[quantity];
		for (int j = 0; j < quantity; j++) {
			longs[j] = value.longValue();
		}
		for (int j = 0; j < quantity; j++) {
			BigInteger big = new BigInteger(Long.toUnsignedString(longs[j]));
			assertTrue(big + " < " + s, big.compareTo(s) < 0);
			assertTrue(big + " +ve", big.signum() >= 0);
		}
	}

	void testCorrectlySizedBigs(HashCode value, HashSize size, int quantity) {
		BigInteger s = size.asBig();
		BigInteger[] bigs = new BigInteger[quantity];
		for (int j = 0; j < quantity; j++) {
			bigs[j] = value.bigValue();
		}
		for (int j = 0; j < quantity; j++) {
			BigInteger big = bigs[j];
			assertTrue(big + " < " + s, big.compareTo(s) < 0);
			assertTrue(big + " +ve", big.signum() >= 0);
		}
	}

	void testDistribution(int... values) {
		int[] counts = new int[32];
		for (int value : values) {
			for (int i = 0; i < 32; i++) {
				if ((value & (1 << i)) != 0) counts[i]++;
			}
		}
		int expected = values.length / 2;
		//TODO this is terrible - must put an improved bound here
		int bound = values.length / 20;
		for (int i = 0; i < counts.length; i++) {
			assert(Math.abs(counts[i] - expected) <= bound);
		}
	}

	<T> void testConsistent(Hasher<T> hasher, T obj) {
		BigInteger bigValue = hasher.bigHashValue(obj);
		long longValue = hasher.longHashValue(obj);
		int intValue = hasher.intHashValue(obj);

		assertEquals(bigValue, hasher.hash(obj).bigValue());
		assertEquals(longValue, hasher.hash(obj).longValue());
		assertEquals(intValue, hasher.hash(obj).intValue());

		assertEquals(longValue, hasher.hash(obj).bigValue().longValue());
		assertEquals(intValue, hasher.hash(obj).bigValue().intValue());
	}
}
