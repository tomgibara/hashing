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

import java.math.BigInteger;
import java.util.Random;

import com.tomgibara.streams.StreamSerializer;

public class RandomHashTest extends HashingTest {

	private static final StreamSerializer<Integer> streamer = (v, s) -> s.writeInt(v);

	public void testSize() throws Exception {
		testSize(HashSize.fromInt(50));
		testSize(HashSize.fromLong(1L << 48));
		testSize(HashSize.fromLong(0x51de5add1eL));
		testSize(HashSize.fromBig(BigInteger.ONE.shiftLeft(175)));
		testSize(HashSize.fromBig(new BigInteger("237497854858736458783445")));
	}

	private void testSize(HashSize size) {
		testSize(Hashing.prng(size));
		testSize(Hashing.prng("SHA1PRNG", size));
	}

	private void testSize(Hash hash) {
		int quantity = 10;
		Hasher<Integer> hasher = hash.hasher(streamer);
		final HashSize s = hasher.getSize();
		for (int i = 0; i < 1000; i++) {
			HashCode h = hasher.hash(i);
			testCorrectlySizedInts(h, s, quantity);
			testCorrectlySizedLongs(h, s, quantity);
			testCorrectlySizedBigs(h, s, quantity);
			testCorrectlySizedBytes(h, s, quantity);
		}
	}

	public void testDistribution() {
		testDistribution(Hashing.prng(HashSize.INT_SIZE));
		testDistribution(Hashing.prng("SHA1PRNG", HashSize.INT_SIZE));
	}

	private void testDistribution(Hash hash) {
		Hasher<Integer> hasher = hash.hasher(streamer);
		Random r = new Random(0L);
		int[] ints = new int[10000];
		for (int i = 0; i < 50; i++) {
			HashCode value = hasher.hash(r.nextInt());
			for (int j = 0; j < ints.length; j++) {
				ints[j] = value.intValue();
			}
			testDistribution(ints);
		}
	}

	public void testConsistent() {
		testConsistent("");
		testConsistent("SHA1PRNG");
	}

	private void testConsistent(String algorithm) {
		testConsistent(newHash(algorithm, HashSize.fromInt(10000)));
		testConsistent(newHash(algorithm, HashSize.SHORT_SIZE));
		testConsistent(newHash(algorithm, HashSize.INT_SIZE));
		testConsistent(newHash(algorithm, HashSize.fromBitLength(63)));
		testConsistent(newHash(algorithm, HashSize.LONG_SIZE));
		testConsistent(newHash(algorithm, HashSize.fromBitLength(77)));
	}

	private static Hash newHash(String algorithm, HashSize size) {
		return algorithm.isEmpty() ? Hashing.prng(size) : Hashing.prng(algorithm, size);
	}

	private void testConsistent(Hash hash) {
		Hasher<Integer> hasher = hash.hasher(streamer);
		for (int i = 0; i < 1000; i++) {
			testConsistent(hasher, i);
		}
	}

}
