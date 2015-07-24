package com.tomgibara.hashing;

import junit.framework.TestCase;

public class TestDistinctHasher extends TestCase {

	public void testObjectHash() {

		Hasher<Object> hasher = Hashing.identityHasher().distinct(3, HashSize.fromIntSize(1000));
		int[] ints = new int[3];
		for (int i = 0; i < 100000; i++) {
			HashValue value = hasher.hashValue(i);
			ints[0] = value.intValue();
			assertTrue(value.hasNext());
			ints[1] = value.intValue();
			assertTrue(value.hasNext());
			ints[2] = value.intValue();
			assertFalse(value.hasNext());
			checkDistinct3(ints);
		}

	}

	private void checkDistinct3(int[] ints) {
		if (ints.length != 3) throw new IllegalArgumentException();
		if (ints[0] == ints[1]) fail("duplicate at 0 and 1");
		if (ints[1] == ints[2]) fail("duplicate at 1 and 2");
		if (ints[2] == ints[0]) fail("duplicate at 0 and 2");
	}

}