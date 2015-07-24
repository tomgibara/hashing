package com.tomgibara.hashing;

import java.math.BigInteger;
import java.util.Arrays;

import junit.framework.TestCase;

public abstract class HashingTest extends TestCase {

	void testCorrectlySizedInts(HashValue value, HashSize size, int quantity) {
		BigInteger s = size.getSize();
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
	
	void testCorrectlySizedLongs(HashValue value, HashSize size, int quantity) {
		BigInteger s = size.getSize();
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

	void testCorrectlySizedBigs(HashValue value, HashSize size, int quantity) {
		BigInteger s = size.getSize();
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
		int bound = values.length / 20;
		for (int i = 0; i < counts.length; i++) {
			assert(Math.abs(counts[i] - expected) <= bound);
		}
	}
}
