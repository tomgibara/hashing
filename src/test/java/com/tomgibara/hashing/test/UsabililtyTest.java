package com.tomgibara.hashing.test;

import java.awt.Point;

import org.junit.Test;

import com.tomgibara.hashing.Hash;
import com.tomgibara.hashing.HashSize;
import com.tomgibara.hashing.HashStreamer;
import com.tomgibara.hashing.HashValue;
import com.tomgibara.hashing.Hasher;
import com.tomgibara.hashing.Hashing;
import com.tomgibara.streams.WriteStream;

import junit.framework.TestCase;

public class UsabililtyTest extends TestCase {

	private static final String str = "...mon panache";
	private static final Point pt = new Point();
	private static final HashStreamer<String> someStream = new HashStreamer<String>() {
		@Override
		public void stream(String value, WriteStream stream) { }
	};

	@Test
	public void testSample() {
		
		// returns the object hashCode
		Hasher<String> obj = Hashing.objectHasher();
		obj.intHashValue(str); // returns str.hashCode()

		// returns System.identityHashCode
		Hasher<String> ident = Hashing.identityHasher();
		ident.intHashValue(str); // returns System.identityHashCode(str)

		// creates a 32 bit murmur hash code...
		Hasher<String> murmur = Hashing.murmur3Int()
				.hasher((s, out) -> out.writeChars(s));
		
		// ...different object types are easily supported ...
		Hasher<Point> murmurPt = Hashing.murmur3Int()
				.hasher((p, s) -> { s.writeInt(p.x); s.writeInt(p.y); });
		
		// .. different sized hashes are easily derived.
		Hasher<String> shortMurmur = murmur.sized(HashSize.SHORT_SIZE);
		shortMurmur.intHashValue(str); // guaranteed to be in the range 0-65535
		
		// derive multiple hash values from a single hash function
		Hasher<String> multiple = murmur.ints();
		{
			HashValue value = multiple.hashValue(str);
			value.intValue(); // first hash value
			value.intValue(); // next hash value
			value.intValue(); // .. and so on ..
		}

		// derive multiple *distinct* hash values from a single hash function
		Hasher<Point> distinct = murmurPt.distinct(3, HashSize.BYTE_SIZE);
		{
			HashValue value = distinct.hashValue(pt);
			value.intValue(); // first hash value
			value.intValue(); // second hash value
			value.intValue(); // third hash value
			value.hasNext();  // returns false - supplies exactly 3 hashes
			                  // all guaranteed to be distinct
		}
		
		// produce hashes by seeding a random number generator...
		Hash<?> prng = Hashing.prng(HashSize.LONG_SIZE);
		
		// ... and use this to produce cryptographically secure hashes ...
		Hash<?> secure = Hashing.prng("SHA1PRNG", HashSize.fromByteLength(16));
		// ... with abritrarily large hash codes
		secure.hasher(someStream).bigHashValue(str); // 128 bit hash code
		
		// perfect hashes for strings is also provided
		Hasher<String> perfect = Hashing.perfect("mouse", "cat", "dog");
		perfect.intHashValue("cat"); // returns 0
		perfect.intHashValue("dog"); // returns 1
		perfect.intHashValue("mouse"); // returns 2


		// accuracy check
		assertEquals(str.hashCode(), obj.intHashValue(str));
		assertEquals(System.identityHashCode(str), ident.intHashValue(str));
		assertTrue(shortMurmur.intHashValue(str) < 65536);
		assertEquals(0, perfect.intHashValue("cat"));
		assertEquals(1, perfect.intHashValue("dog"));
		assertEquals(2, perfect.intHashValue("mouse"));
	}
	
}
