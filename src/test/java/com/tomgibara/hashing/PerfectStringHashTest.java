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

import junit.framework.TestCase;

public class PerfectStringHashTest extends TestCase {

//	//TODO can't readily support because of inclusive ranges of HashRange - need to resolve this issue
//	public void disabledTestEmpty() {
//		Hashing.perfect();
//		Hasher<String> hash = Hashing.perfect();
//		assertTrue( hash.intHashValue("X") < 0 );
//	}

	public void testOne() {
		Hasher<String> hash = Hashing.perfect("a");
		assertEquals(0, hash.intHashValue("a"));
		assertTrue( hash.intHashValue("b") < 0 );
	}

	public void testAbsentClashes() {
		Hasher<String> hash = Hashing.perfect(new String[] {"a", "b"});
		assertEquals(0, hash.intHashValue("a"));
		assertEquals(1, hash.intHashValue("b"));
		assertTrue(hash.intHashValue("c") < 0);

	}

	public void testSingleClash() {
		Hasher<String> hash = Hashing.perfect(new String[] {"Ab", "BC"});
		assertEquals(0, hash.intHashValue("Ab"));
		assertEquals(1, hash.intHashValue("BC"));
	}

	public void testClashCombos() {
		final String[] arr = new String[] {"Ab", "BC", "5u", "6V", "77"};
		Hasher<String> hash = Hashing.perfect(arr);
		for (int i = 0; i < arr.length; i++) {
			assertEquals(i, hash.intHashValue(arr[i]));
		}
	}

	public void testClashCombosPlus() {
		final String[] arr = new String[] {"Ab", "BC", "5u", "6V", "77", "1", "A", "b", "C", "d", "11", "AA", "bb", "CC", "dd"};
		Hasher<String> hash = Hashing.perfect(arr);
		assertEquals(0, hash.intHashValue("1"));
		assertEquals(14, hash.intHashValue("dd"));

		for (int i = 0; i < arr.length; i++) {
			assertEquals(i, hash.intHashValue(arr[i]));
		}
	}

	public void testDuplicates() {
		try {
			Hashing.perfect("a", "b", "a");
			fail();
		} catch (IllegalArgumentException e) {
			//expected
		}
	}

}
