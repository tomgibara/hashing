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

import java.math.BigInteger;
import java.util.Arrays;

abstract class JavaHasher<T> implements Hasher<T> {

	private static final ObjectHasher<?> object = new ObjectHasher<>();
	private static final IdentityHasher<?> identity = new IdentityHasher<>();

	final static BytesHasher bytesHasher = new BytesHasher();
	final static ShortsHasher shortsHasher = new ShortsHasher();
	final static IntsHasher intsHasher = new IntsHasher();
	final static LongsHasher longsHasher = new LongsHasher();
	final static BooleansHasher booleansHasher = new BooleansHasher();
	final static CharsHasher charsHasher = new CharsHasher();
	final static FloatsHasher floatsHasher = new FloatsHasher();
	final static DoublesHasher doublesHasher = new DoublesHasher();
	
	@SuppressWarnings("unchecked")
	static <T> JavaHasher<T> object() {
		return (JavaHasher<T>) object;
	}

	@SuppressWarnings("unchecked")
	static <T> JavaHasher<T> identity() {
		return (JavaHasher<T>) identity;
	}

	private JavaHasher() {
	}

	@Override
	public HashSize getSize() {
		return HashSize.INT_SIZE;
	}

	@Override
	public int intHashValue(T value) {
		return computeHash(value);
	}

	@Override
	public long longHashValue(T value) {
		return intHashValue(value);
	}

	@Override
	public BigInteger bigHashValue(T value) {
		return BigInteger.valueOf(intHashValue(value));
	}

	@Override
	public byte[] bytesHashValue(T value) {
		return AbstractHashCode.intToBytes(intHashValue(value));
	}

	@Override
	public HashCode hash(T value) {
		return new IntHashCode(intHashValue(value));
	}

	abstract int computeHash(T value);

	private final static class ObjectHasher<T> extends JavaHasher<T> {
		@Override int computeHash(T value) { return value == null ? 0 : value.hashCode(); }
	}

	private final static class IdentityHasher<T> extends JavaHasher<T> {
		@Override int computeHash(T value) { return System.identityHashCode(value); }
	}

	private final static class BytesHasher extends JavaHasher<byte[]> {
		@Override int computeHash(byte[] value) { return Arrays.hashCode(value); }
	}

	private final static class ShortsHasher extends JavaHasher<short[]> {
		@Override int computeHash(short[] value) { return Arrays.hashCode(value); }
	}

	private final static class IntsHasher extends JavaHasher<int[]> {
		@Override int computeHash(int[] value) { return Arrays.hashCode(value); }
	}

	private final static class LongsHasher extends JavaHasher<long[]> {
		@Override int computeHash(long[] value) { return Arrays.hashCode(value); }
	}

	private final static class BooleansHasher extends JavaHasher<boolean[]> {
		@Override int computeHash(boolean[] value) { return Arrays.hashCode(value); }
	}

	private final static class CharsHasher extends JavaHasher<char[]> {
		@Override int computeHash(char[] value) { return Arrays.hashCode(value); }
	}

	private final static class FloatsHasher extends JavaHasher<float[]> {
		@Override int computeHash(float[] value) { return Arrays.hashCode(value); }
	}

	private final static class DoublesHasher extends JavaHasher<double[]> {
		@Override int computeHash(double[] value) { return Arrays.hashCode(value); }
	}
}
