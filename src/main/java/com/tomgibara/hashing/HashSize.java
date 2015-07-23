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
 * Records the size of values that a hash value may generate.
 *  
 * @author tomgibara
 *
 */

//TODO add accessors for long and int sizes?
public final class HashSize implements Comparable<HashSize> {

	// statics
	
//	private final static BigInteger INT_MINIMUM = BigInteger.valueOf(Integer.MIN_VALUE);
//	private final static BigInteger INT_MAXIMUM = BigInteger.valueOf(Integer.MAX_VALUE);
//	private final static BigInteger LONG_MINIMUM = BigInteger.valueOf(Long.MIN_VALUE);
//	private final static BigInteger LONG_MAXIMUM = BigInteger.valueOf(Long.MAX_VALUE);

	private final static long LONG_SINT = 1L << 31;
	private final static long LONG_UINT = 1L << 32;
	private final static BigInteger BIG_BYTE = BigInteger.valueOf(256);
	private final static BigInteger BIG_SHORT = BigInteger.valueOf(65536);
	private final static BigInteger BIG_SINT = BigInteger.valueOf(LONG_SINT);
	private final static BigInteger BIG_UINT = BigInteger.valueOf(LONG_UINT);
	private final static BigInteger BIG_SLONG = BigInteger.ONE.shiftLeft(63);
	private final static BigInteger BIG_ULONG = BigInteger.ONE.shiftLeft(64);
	
	// has byte capacity
	public static final HashSize BYTE_SIZE = new HashSize(BIG_BYTE);
	// has short capacity
	public static final HashSize SHORT_SIZE = new HashSize(BIG_SHORT);
	// has int capacity
	public static final HashSize INT_SIZE = new HashSize(BIG_UINT);
	// has long capacity
	public static final HashSize LONG_SIZE = new HashSize(BIG_ULONG);
	
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
	
	public static HashSize fromBigSize(BigInteger size) {
		if (size == null) throw new IllegalArgumentException("null size");
		if (size.signum() <= 0) throw new IllegalArgumentException("non-positive size");
		return new HashSize(size);
	}
	
	public static HashSize fromIntSize(int size) {
		if (size == 0) return INT_SIZE;
		return size > 0 ? new HashSize(size) : new HashSize(LONG_UINT - size);
	}
	
	public static HashSize fromLongSize(long size) {
		if (size == 0L) return LONG_SIZE;
		if (size > 0) return new HashSize(size);
		return new HashSize(BIG_UINT.subtract(BigInteger.valueOf(size)));
	}
	
	// fields
	// masks only a valid mask if powerOfTwo and suitable capacity
	
	private final BigInteger size;
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
		this.size = size;
		//TODO is there a faster way than this?
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
		this.size = BigInteger.valueOf(size);
		bits = 32 - Integer.numberOfLeadingZeros(size - 1);
		powerOfTwo = Integer.highestOneBit(size) == size;
		mask = (powerOfTwo ? this.size : BigInteger.ONE.shiftLeft(bits)).subtract(BigInteger.ONE);
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
		this.size = BigInteger.valueOf(size);
		bits = 64 - Long.numberOfLeadingZeros(size - 1L);
		powerOfTwo = Long.highestOneBit(size) == size;
		mask = (powerOfTwo ? this.size : BigInteger.ONE.shiftLeft(bits)).subtract(BigInteger.ONE);
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
	
	public BigInteger getSize() {
		return size;
	}
	
	public int getBits() {
		return bits;
	}
	
	public boolean isPowerOfTwo() {
		return powerOfTwo;
	}
	
	public boolean isIntSized() {
		return intSized;
	}

	public boolean isIntCapacity() {
		return intCapacity;
	}
	
	public boolean isLongSized() {
		return longSized;
	}
	
	public boolean isLongCapacity() {
		return longCapacity;
	}
	
	// methods
	
	public int mapInt(int value) {
		if (!intCapacity) return value;
		if (powerOfTwo) return value & intMask;
		if (intSized && value >= 0) return value % intSize;
		return (int) ((LONG_UINT - value) % longSize);
	}
	
	public long mapLong(long value) {
		if (!longCapacity) return value;
		if (powerOfTwo) return value & longMask;
		if (longSized & value >= 0L) return value % longSize;
		return BigInteger.valueOf(value).mod(size).longValue();
	}
	
	public BigInteger mapBig(BigInteger value) {
		return powerOfTwo ? value.and(mask) : value.mod(size);
	}
	
	// comparable methods
	
	public int compareTo(HashSize that) {
		return this.size.compareTo(that.size);
	}
	
	// object methods
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof HashSize)) return false;
		HashSize that = (HashSize) obj;
		return this.size.equals(that.size);
	}
	
	@Override
	public int hashCode() {
		return size.hashCode();
	}
	
	@Override
	public String toString() {
		return size.toString();
	}
	
}
