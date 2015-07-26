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

/**
 * Records the size of hash values that can be generated from a {@link Hashing}.
 *
 * @author Tom Gibara
 */

public final class HashSize implements Comparable<HashSize> {

	// statics

	final static long LONG_UINT = 1L << 32;
	final static BigInteger BIG_BYTE = BigInteger.valueOf(256);
	final static BigInteger BIG_SHORT = BigInteger.valueOf(65536);
	final static BigInteger BIG_UINT = BigInteger.valueOf(LONG_UINT);
	final static BigInteger BIG_ULONG = BigInteger.ONE.shiftLeft(64);

	/**
	 * A hash size with maximum byte capacity; hash values may span all 2^8
	 * possible byte values.
	 */

	public static final HashSize BYTE_SIZE = new HashSize(BIG_BYTE);

	/**
	 * A hash size with maximum short capacity; hash values may span all 2^16
	 * possible short values.
	 */

	public static final HashSize SHORT_SIZE = new HashSize(BIG_SHORT);

	/**
	 * A hash size with maximum int capacity; hash values may span all 2^32
	 * possible int values.
	 */

	public static final HashSize INT_SIZE = new HashSize(BIG_UINT);

	/**
	 * A hash size with maximum long capacity; hash values may span all 2^64
	 * possible long values.
	 */

	public static final HashSize LONG_SIZE = new HashSize(BIG_ULONG);

	// to call forXXX?
	public static HashSize fromByteLength(int byteLength) {
		if (byteLength <= 0) throw new IllegalArgumentException("non-positive byte length");
		switch (byteLength) {
		case 1: return BYTE_SIZE;
		case 2: return SHORT_SIZE;
		case 4: return INT_SIZE;
		case 8: return LONG_SIZE;
		default:
			return new HashSize(BigInteger.ONE.shiftLeft(8 * byteLength));
		}
	}

	// to call forXXX?
	public static HashSize fromBitLength(int bitLength) {
		if (bitLength <= 0) throw new IllegalArgumentException("non-positive bit length");
		switch (bitLength) {
		case  8: return BYTE_SIZE;
		case 16: return SHORT_SIZE;
		case 32: return INT_SIZE;
		case 64: return LONG_SIZE;
		default:
			return new HashSize(BigInteger.ONE.shiftLeft(bitLength));
		}
	}

	public static HashSize fromBig(BigInteger size) {
		if (size == null) throw new IllegalArgumentException("null size");
		if (size.signum() <= 0) throw new IllegalArgumentException("non-positive size");
		return new HashSize(size);
	}

	public static HashSize fromInt(int size) {
		if (size == 0) return INT_SIZE;
		return size > 0 ? new HashSize(size) : new HashSize(LONG_UINT - size);
	}

	public static HashSize fromLong(long size) {
		if (size == 0L) return LONG_SIZE;
		if (size > 0) return new HashSize(size);
		return new HashSize(BIG_UINT.subtract(BigInteger.valueOf(size)));
	}

	// fields
	// masks only a valid mask if powerOfTwo and suitable capacity

	private final BigInteger bigSize;
	private final int bits;
	private final boolean powerOfTwo;
	private final BigInteger mask;

	private final boolean intSized;
	private final boolean intCapacity;
	private final int intSize;
	private final int intMask;

	private final boolean longSized;
	private final boolean longCapacity;
	private final long longSize;
	private final long longMask;

	// constructors

	HashSize(BigInteger size) {
		bigSize = size;
		BigInteger sizeMinusOne = size.subtract(BigInteger.ONE);
		bits = sizeMinusOne.bitLength();
		powerOfTwo = size.bitCount() == 1;
		mask = powerOfTwo ? sizeMinusOne : BigInteger.ONE.shiftLeft(bits).subtract(BigInteger.ONE);
		intSized = bits < 32;
		intCapacity = bits <= 32;
		intSize = size.intValue();
		intMask = intSize - 1;
		longSized = bits < 64;
		longCapacity = bits <= 64;
		longSize = size.longValue();
		longMask = longSize - 1;
	}

	HashSize(int size) {
		bigSize = BigInteger.valueOf(size);
		bits = 32 - Integer.numberOfLeadingZeros(size - 1);
		powerOfTwo = Integer.highestOneBit(size) == size;
		mask = (powerOfTwo ? this.bigSize : BigInteger.ONE.shiftLeft(bits)).subtract(BigInteger.ONE);
		intSized = true;
		intCapacity = true;
		intSize = size;
		intMask = size - 1;
		longSized = true;
		longCapacity = true;
		longSize = size;
		longMask = size - 1;
	}

	HashSize(long size) {
		bigSize = BigInteger.valueOf(size);
		bits = 64 - Long.numberOfLeadingZeros(size - 1L);
		powerOfTwo = Long.highestOneBit(size) == size;
		mask = (powerOfTwo ? this.bigSize : BigInteger.ONE.shiftLeft(bits)).subtract(BigInteger.ONE);
		intSized = bits < 32;
		intCapacity = bits <= 32;
		intSize = (int) size;
		intMask = intSize - 1;
		longSized = true;
		longCapacity = true;
		longSize = size;
		longMask = size - 1;
	}

	// accessors

	/**
	 * The size as a big integer.
	 * 
	 * @return the size, always greater than zero
	 */

	public BigInteger asBig() {
		return bigSize;
	}

	/**
	 * The number of bits required to store hashes of this size.
	 * 
	 * @return the number of bits, always greater than zero
	 */

	public int getBits() {
		return bits;
	}

	/**
	 * Whether hash values of this size are composed of an exact number of bits.
	 * 
	 * @return whether the size is a power of two
	 */
	
	public boolean isPowerOfTwo() {
		return powerOfTwo;
	}

	/**
	 * Whether this size is small enough to be represented by a signed int.
	 * 
	 * @return whether the hash size is int-sized
	 */

	public boolean isIntSized() {
		return intSized;
	}

	/**
	 * Whether hashes of this size can be accommodated in the bits of a single
	 * int.
	 * 
	 * @return whether an int has the capacity to store hashes of this size
	 */

	public boolean isIntCapacity() {
		return intCapacity;
	}

	/**
	 * The size as an int.
	 * 
	 * @return the size, always greater than zero
	 * @throws ArithmeticException
	 *             if not int-sized
	 * @see #intSized
	 */

	public int asInt() throws ArithmeticException {
		if (!intSized) throw new ArithmeticException("not int sized");
		return intSize;
	}

	/**
	 * Whether this size is small enough to be represented by a signed long.
	 * 
	 * @return whether the hash size is long-sized
	 */

	public boolean isLongSized() {
		return longSized;
	}

	/**
	 * Whether hashes of this size can be accommodated in the bits of a single
	 * long.
	 * 
	 * @return whether a long has the capacity to store hashes of this size
	 */

	public boolean isLongCapacity() {
		return longCapacity;
	}

	/**
	 * The size as a long.
	 * 
	 * @return the size, always greater than zero
	 * @throws ArithmeticException
	 *             if not long-sized
	 * @see #longSized
	 */

	public long asLong() {
		if (!longSized) throw new ArithmeticException("not long sized");
		return longSize;
	}

	// methods

	/**
	 * Maps an arbitrary integer into the range of this size by computing the
	 * remainder of the int modulo this size. For sizes that are a power of two,
	 * this is equivalent to returning the number of least significant bits
	 * indicated by {@link #getBits()}. Note that for sizes that are not
	 * int-sized (ie. sizes exceeding 2^31-1) the resulting value may be
	 * negative.
	 * 
	 * @param value
	 *            the value to be mapped into the range of this size.
	 * @return an int whose unsigned value falls into the range of this size
	 */

	public int mapInt(int value) {
		if (!intCapacity) return value;
		if (powerOfTwo) return value & intMask;
		if (intSized && value >= 0) return value % intSize;
		return (int) ((LONG_UINT - value) % longSize);
	}

	/**
	 * Maps an arbitrary long into the range of this size by computing the
	 * remainder of the long modulo this size. For sizes that are a power of
	 * two, this is equivalent to returning the number of least significant bits
	 * indicated by {@link #getBits()}. Note that for sizes that are not
	 * long-sized (ie. sizes exceeding 2^63-1) the resulting value may be
	 * negative.
	 * 
	 * @param value
	 *            the value to be mapped into the range of this size.
	 * @return a long whose unsigned value falls into the range of this size
	 */

	public long mapLong(long value) {
		if (!longCapacity) return value;
		if (powerOfTwo) return value & longMask;
		if (longSized & value >= 0L) return value % longSize;
		return BigInteger.valueOf(value).mod(bigSize).longValue();
	}

	/**
	 * Maps an arbitrary big integer into the range of this size by computing
	 * its remainder modulo this size. For sizes that are a power of two, this
	 * is equivalent to returning the number of least significant bits indicated
	 * by {@link #getBits()}.
	 * 
	 * @param value
	 *            the value to be mapped into the range of this size.
	 * @return a big integer less than this size
	 */

	public BigInteger mapBig(BigInteger value) {
		return powerOfTwo ? value.and(mask) : value.mod(bigSize);
	}

	// comparable methods

	@Override
	public int compareTo(HashSize that) {
		return this.bigSize.compareTo(that.bigSize);
	}

	// object methods

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof HashSize)) return false;
		HashSize that = (HashSize) obj;
		return this.bigSize.equals(that.bigSize);
	}

	@Override
	public int hashCode() {
		return bigSize.hashCode();
	}

	@Override
	public String toString() {
		return bigSize.toString();
	}

}
