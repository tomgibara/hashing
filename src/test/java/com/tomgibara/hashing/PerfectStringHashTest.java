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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PerfectStringHashTest extends HashingTest {

//	//TODO can't readily support because of inclusive ranges of HashRange - need to resolve this issue
//	public void disabledTestEmpty() {
//		Hashing.perfect();
//		Hasher<String> hash = Hashing.perfect();
//		assertTrue( hash.intHashValue("X") < 0 );
//	}

	// create s distinct random strings
	private static String[] uniqueStrings(int s) {
		Random r = new Random(s);
		Set<String> set = new HashSet<String>();
		List<String> list = new ArrayList<String>(s);
		for (int i = 0; i < s; i++) {
			while (true) {
				String str = Integer.toString( r.nextInt(1000000) );
				if (set.add(str)) {
					list.add(str);
					break;
				}
			}
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	public void testOne() {
		Hasher<String> hash = Hashing.minimalPerfect("a");
		assertEquals(0, hash.intHashValue("a"));
		try {
			hash.intHashValue("b");
			fail();
		} catch (IllegalArgumentException e) {
			/* expected */
		}
	}

	public void testAbsentClashes() {
		Hasher<String> hash = Hashing.minimalPerfect(new String[] {"a", "b"});
		assertEquals(0, hash.intHashValue("a"));
		assertEquals(1, hash.intHashValue("b"));
		try {
			hash.intHashValue("c");
			fail();
		} catch (IllegalArgumentException e) {
			/* expected */
		}

	}

	public void testSingleClash() {
		Hasher<String> hash = Hashing.minimalPerfect(new String[] {"Ab", "BC"});
		assertEquals(0, hash.intHashValue("Ab"));
		assertEquals(1, hash.intHashValue("BC"));
	}

	public void testClashCombos() {
		final String[] arr = new String[] {"Ab", "BC", "5u", "6V", "77"};
		Hasher<String> hash = Hashing.minimalPerfect(arr);
		for (int i = 0; i < arr.length; i++) {
			assertEquals(i, hash.intHashValue(arr[i]));
		}
	}

	public void testClashCombosPlus() {
		final String[] arr = new String[] {"Ab", "BC", "5u", "6V", "77", "1", "A", "b", "C", "d", "11", "AA", "bb", "CC", "dd"};
		Hasher<String> hash = Hashing.minimalPerfect(arr);
		assertEquals(0, hash.intHashValue("1"));
		assertEquals(14, hash.intHashValue("dd"));

		for (int i = 0; i < arr.length; i++) {
			assertEquals(i, hash.intHashValue(arr[i]));
		}
	}

	public void testDuplicates() {
		try {
			Hashing.minimalPerfect("a", "b", "a");
			fail();
		} catch (IllegalArgumentException e) {
			//expected
		}
	}

	public void testCorrectlySized() {
		Random r = new Random(0);
		for (int i = 0; i < 100; i++) {
			int s = 1 + r.nextInt(1000);
			testCorrectlySized(s);
		}
	}

	private void testCorrectlySized(int s) {
		String[] strs = uniqueStrings(s);
		Hasher<String> hasher = Hashing.minimalPerfect(strs);
		//test the values
		HashSize size = hasher.getSize();
		for (int i = 0; i < strs.length; i++) {
			String str = strs[i];
			testCorrectlySizedInts(hasher.hash(str), size, 1);
			testCorrectlySizedLongs(hasher.hash(str), size, 1);
			testCorrectlySizedBigs(hasher.hash(str), size, 1);
		}
	}

	public void testConsistent() {
		String[] strs = uniqueStrings(1000);
		Hasher<String> hasher = Hashing.minimalPerfect(strs);
		for (int i = 0; i < strs.length; i++) {
			testConsistent(hasher, strs[i]);
		}
	}

}
