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
 * Generic hash generator, produces a single hash which is the result of calling
 * {@link #hashCode()} on the object, or zero for null.
 * 
 * @author tomgibara
 * 
 */

abstract class JavaHasher<T> implements Hasher<T> {

	private static ObjectHasher<?> object = new ObjectHasher<>();
	private static IdentityHasher<?> identity = new IdentityHasher<>();
	
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
	public HashRange getRange() {
		return HashRange.FULL_INT_RANGE;
	}
	
	@Override
	public int intHashValue(T value) {
		return hash(value);
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
	public HashValue hashValue(T value) {
		return new IntHashValue(intHashValue(value));
	}

	abstract int hash(T value);

	private final static class ObjectHasher<T> extends JavaHasher<T> {

		@Override
		int hash(T value) {
			return value == null ? 0 : value.hashCode();
		}

	}

	private final static class IdentityHasher<T> extends JavaHasher<T> {

		@Override
		int hash(T value) {
			return System.identityHashCode(value);
		}

	}
}
