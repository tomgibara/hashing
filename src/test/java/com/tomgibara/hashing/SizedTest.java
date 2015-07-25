package com.tomgibara.hashing;

import java.math.BigInteger;
import java.util.Random;

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
			HashValue value = h.hashValue(i);
			testCorrectlySizedInts(value, size, 1000);
			testCorrectlySizedLongs(value, size, 1000);
			testCorrectlySizedBigs(value, size, 1000);
		}
	}

}
