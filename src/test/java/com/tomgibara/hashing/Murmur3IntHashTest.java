package com.tomgibara.hashing;

import java.util.Random;

import com.google.common.hash.HashFunction;
import com.tomgibara.streams.WriteStream;

import junit.framework.TestCase;

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
			testCorrectlySizedInts(hasher.hashValue(i), size, 1);
			testCorrectlySizedLongs(hasher.hashValue(i), size, 1);
			testCorrectlySizedBigs(hasher.hashValue(i), size, 1);
		}
	}
	
	public void testDistribution() {
		Hasher<Integer> hasher = Hashing.murmur3Int().hasher((i, s) -> s.writeInt(i));
//		for (int i = 0; i < 100; i++) {
//			int[] ints = new int[1000];
//			for (int j = 0; j < ints.length; j++) {
//				ints[j] = hasher.intHashValue(i);
//			}
//			testDistribution(ints);
//		}
		int[] ints = new int[10000];
		for (int i = 0; i < ints.length; i++) {
			ints[i] = hasher.intHashValue(i);
		}
		testDistribution(ints);
	}
}
