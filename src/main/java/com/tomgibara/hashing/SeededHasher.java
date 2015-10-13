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

import com.tomgibara.streams.StreamSerializer;
import com.tomgibara.streams.WriteStream;

class SeededHasher<S extends WriteStream,T> extends StandardHasher<S,T> {

	private final long seed;

	SeededHasher(Hash<S> hash, StreamSerializer<T> serializer, long seed) {
		super(hash, serializer);
		this.seed = seed;
	}

	@Override
	S newStream() {
		S stream = super.newStream();
		stream.writeLong(seed);
		return stream;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() + Long.hashCode(seed);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		if (obj == this) return true;
		if (!(obj instanceof SeededHasher)) return false;
		SeededHasher<?, ?> that = (SeededHasher<?, ?>) obj;
		return this.seed == that.seed;
	}

	@Override
	public String toString() {
		return super.toString() + " seeded by " + seed;
	}
	
}
