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

import com.tomgibara.hashing.legacy.AbstractMultiHash;
import com.tomgibara.hashing.legacy.MultiHash;

/**
 * A {@link MultiHash} implementation that returns the single hash value
 * produced by an arbitrary {@link Hasher} instance.
 * 
 * @author tomgibara
 * 
 * @param <T>
 *            the type of objects for which hashes may be generated
 */

public class SingletonMultiHash<T> extends AbstractMultiHash<T> {

	// fields
	
	private final Hasher<T> hash;

	// constructors
	
	public SingletonMultiHash(Hasher<T> hash) {
		if (hash == null) throw new IllegalArgumentException("null hash");
		this.hash = hash;
	}
	
	// hash methods
	
	@Override
	public HashRange getRange() {
		return hash.getRange();
	}

	@Override
	public BigInteger bigHashValue(T value) {
		return hash.bigHashValue(value);
	}

	@Override
	public int intHashValue(T value) {
		return hash.intHashValue(value);
	}

	@Override
	public long longHashValue(T value) {
		return hash.longHashValue(value);
	}
	
	// object methods
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof SingletonMultiHash<?>)) return false;
		SingletonMultiHash<T> that = (SingletonMultiHash<T>) obj;
		return this.hash.equals(that.hash);
	}
	
	@Override
	public int hashCode() {
		return hash.hashCode();
	}
	
	@Override
	public String toString() {
		return "Multi: " + hash;
	}

}
