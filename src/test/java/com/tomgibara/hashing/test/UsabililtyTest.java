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
package com.tomgibara.hashing.test;

import java.awt.Point;

import junit.framework.TestCase;

import org.junit.Test;

import com.tomgibara.hashing.Hash;
import com.tomgibara.hashing.HashCode;
import com.tomgibara.hashing.HashSize;
import com.tomgibara.hashing.Hasher;
import com.tomgibara.hashing.Hashing;
import com.tomgibara.streams.StreamSerializer;

public class UsabililtyTest extends TestCase {

	private static final String str = "...mon panache";
	private static final Point pt = new Point();
	private static final StreamSerializer<String> someStream = (v,s) -> {};
	private static final long randomSeed = 0L;

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

		// derive seeded hashers to protect against collision attacks
		Hasher<String> murmurSafe = Hashing.murmur3Int()
				.seeded((s, out) -> out.writeChars(s), randomSeed);
		{
			murmurSafe.hash(str);
			// almost certainly not the same as murmur.hashValue(str)
		}

		// derive multiple hash values from a single hash function
		Hasher<String> multiple = murmur.ints();
		{
			HashCode value = multiple.hash(str);
			value.intValue(); // first hash value
			value.intValue(); // next hash value
			value.intValue(); // .. and so on ..
		}

		// derive multiple *distinct* hash values from a single hash function
		Hasher<Point> distinct = murmurPt.distinct(3, HashSize.BYTE_SIZE);
		{
			HashCode value = distinct.hash(pt);
			value.intValue(); // first hash value
			value.intValue(); // second hash value
			value.intValue(); // third hash value
			value.hasNext();  // returns false - supplies exactly 3 hashes
			                  // all guaranteed to be distinct
		}

		// produce hashes from message digests
		Hash sha1 = Hashing.SHA_1().asHash();

		// produce hashes by seeding a random number generator
		Hash prng = Hashing.prng(HashSize.LONG_SIZE);

		// use this capability to produce cryptographically secure hashes ...
		Hash secure = Hashing.prng("SHA1PRNG", HashSize.fromByteLength(16));

		// ... with arbitrarily large hash codes
		secure.hasher(someStream).bytesHashValue(str); // 128 bit hash code

		// "minimal perfect hashing" for strings is also provided
		Hasher<String> perfect = Hashing.minimalPerfect("mouse", "cat", "dog");
		perfect.intHashValue("cat"); // returns 0
		perfect.intHashValue("dog"); // returns 1
		perfect.intHashValue("mouse"); // returns 2


		// accuracy check
		assertEquals(str.hashCode(), obj.intHashValue(str));
		assertEquals(System.identityHashCode(str), ident.intHashValue(str));
		assertFalse(murmur.intHashValue(str) == murmurSafe.intHashValue(str));
		assertTrue(shortMurmur.intHashValue(str) < 65536);
		assertEquals(0, perfect.intHashValue("cat"));
		assertEquals(1, perfect.intHashValue("dog"));
		assertEquals(2, perfect.intHashValue("mouse"));
	}

}
