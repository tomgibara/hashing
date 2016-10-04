package com.tomgibara.hashing;

import java.math.BigInteger;
import java.util.Arrays;

final class IntHasher<T> implements Hasher<T> {

	// statics

	private static final IntHasher<?> object         = new IntHasher<>(v -> v == null ? 0 : v.hashCode());
	private static final IntHasher<?> identity       = new IntHasher<>(v -> System.identityHashCode(v));

	final static IntHasher<byte[]>    bytesHasher    = new IntHasher<>(v -> Arrays.hashCode(v));
	final static IntHasher<short[]>   shortsHasher   = new IntHasher<>(v -> Arrays.hashCode(v));
	final static IntHasher<int[]>     intsHasher     = new IntHasher<>(v -> Arrays.hashCode(v));
	final static IntHasher<long[]>    longsHasher    = new IntHasher<>(v -> Arrays.hashCode(v));
	final static IntHasher<boolean[]> booleansHasher = new IntHasher<>(v -> Arrays.hashCode(v));
	final static IntHasher<char[]>    charsHasher    = new IntHasher<>(v -> Arrays.hashCode(v));
	final static IntHasher<float[]>   floatsHasher   = new IntHasher<>(v -> Arrays.hashCode(v));
	final static IntHasher<double[]>  doublesHasher  = new IntHasher<>(v -> Arrays.hashCode(v));

	@SuppressWarnings("unchecked")
	static <T> IntHasher<T> object() {
		return (IntHasher<T>) object;
	}

	@SuppressWarnings("unchecked")
	static <T> IntHasher<T> identity() {
		return (IntHasher<T>) identity;
	}

	// fields

	private final IntPrehash<T> prehash;

	// constructors

	IntHasher(IntPrehash<T> prehash) {
		this.prehash = prehash;
	}

	// hasher methods

	@Override
	public HashSize getSize() {
		return HashSize.INT_SIZE;
	}

	@Override
	public int intHashValue(T value) {
		return prehash.preHash(value);
	}

	@Override
	public long longHashValue(T value) {
		return intHashValue(value) & 0xffffffffL;
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

	// object methods
	
	@Override
	public int hashCode() {
		return prehash.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof IntHasher)) return false;
		IntHasher<?> that = (IntHasher<?>) obj;
		return this.prehash.equals(that.prehash);
	}

	@Override
	public String toString() {
		return "IntHasher(" + prehash + ")";
	}
}
