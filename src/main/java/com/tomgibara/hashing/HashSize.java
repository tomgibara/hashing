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
	
	private final BigInteger size;
	private final boolean intSized;
	private final boolean intCapacity;
	private final boolean longSized;
	private final boolean longCapacity;
	
	// constructors
	
	HashSize(BigInteger size) {
		this.size = size;
		intSized = size.compareTo(BIG_SINT) <= 0;
		intCapacity = intSized || size.compareTo(BIG_UINT) <= 0;
		longSized = intCapacity || size.compareTo(BIG_SLONG) <= 0;
		longCapacity = longSized || size.compareTo(BIG_ULONG) <= 0;
	}

	HashSize(int size) {
		this.size = BigInteger.valueOf(size);
		intSized = true;
		intCapacity = true;
		longSized = true;
		longCapacity = true;
	}
	
	HashSize(long size) {
		this.size = BigInteger.valueOf(size);
		intSized = size <= LONG_SINT;
		intCapacity = size <= LONG_UINT;
		longSized = true;
		longCapacity = true;
	}

	// accessors
	
	public BigInteger getSize() {
		return size;
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
