package com.tomgibara.hashing;

public class JavaHasherTest extends HashingTest {

	public void testHashCodeCorrect() {
		Hasher<String> hasher = Hashing.objectHasher();
		HashSize size = hasher.getSize();
		assertEquals(HashSize.INT_SIZE, size);
		for (int i = 100000; i < 110000; i += 10) {
			String str = Integer.toString(i);
			assertEquals(str.hashCode(), hasher.intHashValue(str));
			testConsistent(hasher, str);
			testCorrectlySizedInts(hasher.hashValue(str), size, 1);
			testCorrectlySizedLongs(hasher.hashValue(str), size, 1);
			testCorrectlySizedBigs(hasher.hashValue(str), size, 1);
		}
	}

	public void testIndentityCorrect() {
		Hasher<String> hasher = Hashing.identityHasher();
		HashSize size = hasher.getSize();
		assertEquals(HashSize.INT_SIZE, size);
		for (int i = 100000; i < 110000; i += 10) {
			String str = Integer.toString(i);
			assertEquals(System.identityHashCode(str), hasher.intHashValue(str));
			testConsistent(hasher, str);
			testCorrectlySizedInts(hasher.hashValue(str), size, 1);
			testCorrectlySizedLongs(hasher.hashValue(str), size, 1);
			testCorrectlySizedBigs(hasher.hashValue(str), size, 1);
		}
	}

}
