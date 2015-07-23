package com.tomgibara.hashing;

import java.util.Random;

import com.google.common.hash.HashFunction;
import com.tomgibara.streams.WriteStream;

import junit.framework.TestCase;

public class Murmur3_32HashTest extends TestCase {

	public void testMatchesGuava() {
		Random r = new Random(0L);
		Hasher<byte[]> hasher = Hashing.murmur3Int().hasher(new ByteStreamer());
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
	
	private static class ByteStreamer implements HashStreamer<byte[]> {
		
		@Override
		public void stream(byte[] value, WriteStream stream) {
			stream.writeBytes(value);
		}
		
	}
	
}
