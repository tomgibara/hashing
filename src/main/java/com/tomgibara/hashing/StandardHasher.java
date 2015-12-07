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

import com.tomgibara.streams.StreamSerializer;
import com.tomgibara.streams.WriteStream;

class StandardHasher<S extends WriteStream,T> implements Hasher<T> {

	private final Hash<S> hash;
	private final StreamSerializer<T> serializer;

	StandardHasher(Hash<S> hash, StreamSerializer<T> serializer) {
		this.hash = hash;
		this.serializer = serializer;
	}

	@Override
	public HashSize getSize() {
		return hash.getSize();
	}

	@Override
	public int getQuantity() {
		return hash.getQuantity();
	}

	@Override
	public HashCode hash(T value) {
		return hash.hash(stream(value));
	}

	@Override
	public byte[] bytesHashValue(T value) {
		return hash.bytesHashValue(stream(value));
	}

	@Override
	public BigInteger bigHashValue(T value) {
		return hash.bigHashValue(stream(value));
	}

	@Override
	public long longHashValue(T value) {
		return hash.longHashValue(stream(value));
	}

	@Override
	public int intHashValue(T value) {
		return hash.intHashValue(stream(value));
	}

	S newStream() {
		return hash.newStream();
	}

	private S stream(T value) {
		S stream = newStream();
		serializer.serialize(value, stream);
		return stream;
	}

	@Override
	public int hashCode() {
		return hash.hashCode() + serializer.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof StandardHasher)) return false;
		StandardHasher<?, ?> that = (StandardHasher<?, ?>) obj;
		if (!this.hash.equals(that.hash)) return false;
		if (!this.serializer.equals(that.serializer)) return false;
		return true;
	}

	@Override
	public String toString() {
		return hash + " <- " + serializer;
	}
}
