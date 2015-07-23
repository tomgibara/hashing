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
 * Convenience base class for creating {@link MultiHash} implementations that
 * adjust the hash values returned by another {@link MultiHash} implementation.
 *
 * @author tomgibara
 *
 * @param <T> the type of object over which hashes may be generated
 */

//TODO consider eliminating
public class AdaptedHashing<T> implements Hashing<T> {

	protected final Hashing<T> hashing;

	public AdaptedHashing(Hashing<T> hashing) {
		if (hashing == null) throw new IllegalArgumentException("null hashing");
		this.hashing = hashing;
	}

	@Override
	public HashSize getSize() {
		return hashing.getSize();
	}

	@Override
	public int getQuantity() {
		return hashing.getQuantity();
	}

	@Override
	public HashValue hashValue(T value) throws IllegalArgumentException {
		return adapt(hashing.hashValue(value));
	}

	@Override
	public int intHashValue(T value) {
		return adapt(hashing.intHashValue(value));
	}

	@Override
	public long longHashValue(T value) {
		return adapt(hashing.longHashValue(value));
	}

	@Override
	public BigInteger bigHashValue(T value) {
		return adapt(hashing.bigHashValue(value));
	}

	protected int adapt(int hash) {
		return hash;
	}

	protected long adapt(long hash) {
		return hash;
	}

	protected BigInteger adapt(BigInteger hash) {
		return hash;
	}

	protected HashValue adapt(HashValue value) {
		return value;
	}

}
