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
package com.tomgibara.hashing;

import java.math.BigInteger;

/**
 * The result of hashing an object. Multiple values may be provided in a single
 * result. In this case {@link #hasNext()} will return true until every value
 * has been retrieved.
 * 
 * @author Tom Gibara
 *
 */

public interface HashCode {

	HashSize size();
	
	/**
	 * A hash code with an integer value.
	 * 
	 * @param intValue
	 *            the integer hash value
	 * @return a hash code
	 */

	static HashCode fromInt(int intValue) { return new IntHashCode(intValue); }

	/**
	 * A hash code with a long integer value.
	 * 
	 * @param longValue
	 *            the long integer hash value
	 * @return a hash code
	 */

	static HashCode fromLong(long longValue) { return new LongHashCode(longValue); }

	/**
	 * A hash code with a big integer value.
	 * 
	 * @param bigValue
	 *            the big integer hash value
	 * @return a hash code
	 */

	static HashCode fromBig(HashSize size, BigInteger bigValue) { return new BigHashCode(size, bigValue); }

	/**
	 * A hash value consisting of a multiplicity of integer values.
	 * 
	 * @param intValues
	 *            the integer hash values
	 * @return a hash code
	 */

	static HashCode fromInts(int... intValues) {
		if (intValues == null) throw new IllegalArgumentException("null intValues");
		return new IntsHashCode(HashSize.INT_SIZE, intValues);
	}
	
	/**
	 * A hash value consisting of a multiplicity of long integer values.
	 * 
	 * @param longValues
	 *            the long hash values
	 * @return a hash code
	 */

	static HashCode fromLongs(long... longValues) {
		if (longValues == null) throw new IllegalArgumentException("null longValues");
		return new LongsHashCode(HashSize.LONG_SIZE, longValues);
	}

	/**
	 * A hash value consisting of a multiplicity of big integer values.
	 * 
	 * @param bigValues
	 *            the big hash values
	 * @return a hash code
	 */

	static HashCode fromBigs(HashSize size, BigInteger... bigValues) {
		if (size == null) throw new IllegalArgumentException("null size");
		if (bigValues == null) throw new IllegalArgumentException("null bigValues");
		for (BigInteger big : bigValues) {
			if (big == null) throw new IllegalArgumentException("null big value");
			if (size.containsBig(big)) throw new IllegalArgumentException("invalid big value");
		}
		return new BigsHashCode(HashSize.LONG_SIZE, bigValues);
	}

	/**
	 * The next hash value a big integer.
	 * 
	 * @return a big integer hash value
	 */

	BigInteger bigValue();
	
	/**
	 * The next hash value a long integer.
	 * 
	 * @return a long integer hash value
	 */

	default long longValue() {
		return bigValue().longValue();
	}

	/**
	 * The next hash value an integer.
	 * 
	 * @return an integer hash value
	 */

	default int intValue() {
		return bigValue().intValue();
	}

	/**
	 * Whether there are more values available.
	 * 
	 * @return true if more hash values are available
	 */

	default boolean hasNext() {
		return false;
	}

	/**
	 * Writes the next hash values into the supplied array. The hash code may
	 * contains fewer values than the length of the array and in this case not
	 * all elements of the array will be written to.
	 * 
	 * @param bigs
	 *            the array into which the array values should be written
	 * @return the number of values written
	 */

	default int bigValues(BigInteger[] bigs) {
		if (bigs == null) throw new IllegalArgumentException("null bigs");
		int i;
		for (i = 0; i < bigs.length && hasNext(); i++) {
			bigs[i] = bigValue();
		}
		return i;
	}

	/**
	 * Writes the next hash values into the supplied array. The hash code may
	 * contains fewer values than the length of the array and in this case not
	 * all elements of the array will be written to.
	 * 
	 * @param bigs
	 *            the array into which the array values should be written
	 * @return the number of values written
	 */

	default int longValues(long[] longs) {
		if (longs == null) throw new IllegalArgumentException("null longs");
		int i;
		for (i = 0; i < longs.length && hasNext(); i++) {
			longs[i] = longValue();
		}
		return i;
	}

	/**
	 * Writes the next hash values into the supplied array. The hash code may
	 * contains fewer values than the length of the array and in this case not
	 * all elements of the array will be written to.
	 * 
	 * @param bigs
	 *            the array into which the array values should be written
	 * @return the number of values written
	 */

	default int intValues(int[] ints) {
		if (ints == null) throw new IllegalArgumentException("null ints");
		int i;
		for (i = 0; i < ints.length && hasNext(); i++) {
			ints[i] = intValue();
		}
		return i;
	}

}
