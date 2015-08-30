package com.tomgibara.hashing;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.valueOf;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Assert;

public class HashCodeTest extends HashingTest {

	public void testIntArrays() {
		int[] is = new int[5];
		int count = new IntHashCode(1).intValues(is);
		Assert.assertEquals(5, count);
		int[] e = new int[5];
		Arrays.fill(e, 1);
		Assert.assertArrayEquals(e, is);
	}

	public void testLongArrays() {
		long[] ls = new long[5];
		int count = new LongHashCode(1L).longValues(ls);
		Assert.assertEquals(5, count);
		long[] e = new long[5];
		Arrays.fill(e, 1L);
		Assert.assertArrayEquals(e, ls);
	}

	public void testBigArrays() {
		BigInteger[] bs = new BigInteger[5];
		int count = new BigHashCode(BigInteger.ONE).bigValues(bs);
		Assert.assertEquals(5, count);
		BigInteger[] e = new BigInteger[5];
		Arrays.fill(e, BigInteger.ONE);
		Assert.assertArrayEquals(e, bs);
	}

	public void testIntsArrays() {
		int[] is = new int[10];
		int count = new IntsHashCode(0,1,2,3,4).intValues(is);
		Assert.assertEquals(5, count);
		int[] e = new int[10];
		for (int i = 0; i < 5; i++) {
			e[i] = i;
		}
		Assert.assertArrayEquals(e, is);
	}

	public void testLongsArrays() {
		long[] ls = new long[10];
		int count = new LongsHashCode(0L,1L,2L,3L,4L).longValues(ls);
		Assert.assertEquals(5, count);
		long[] e = new long[10];
		for (int i = 0; i < 5; i++) {
			e[i] = i;
		}
		Assert.assertArrayEquals(e, ls);
	}

	public void testBigsArrays() {
		BigInteger[] bs = new BigInteger[10];
		int count = new BigsHashCode(ZERO,ONE,valueOf(2L),valueOf(3L),valueOf(4L)).bigValues(bs);
		Assert.assertEquals(5, count);
		BigInteger[] e = new BigInteger[10];
		for (int i = 0; i < 5; i++) {
			e[i] = valueOf(i);
		}
		Assert.assertArrayEquals(e, bs);
	}
	
}
