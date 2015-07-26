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

import com.tomgibara.streams.WriteStream;

/**
 * <p>
 * A hash creates streams that it can later convert into {@link HashValue}s.
 * Streams are ordinarily populated by a {@link HashSerializer} implementation.
 * Applications are generally expected to use instances of this class indirectly
 * by deriving {@link Hasher}s from them.
 * 
 * <p>
 * New hash implementations can be introduced by implementing this interface.
 * Hashes that generate multiple hash values will want to indicate this via the
 * {@link #getQuantity()} method.
 * 
 * @author Tom Gibara
 *
 * @param <S>
 *            the type of stream which is created and then hashed
 */

public interface Hash<S extends WriteStream> extends Hashing<S> {

	/**
	 * Creates new stream that can later be passed back to the object to derive
	 * a {@link HashValue}.
	 * 
	 * @return a new stream for accumulating object data
	 */

	S newStream();

	/**
	 * Derives a {@link Hasher} by combining this hash with a
	 * {@link HashSerializer}: a new stream will be created by the hash,
	 * populated by the serializer, and converted into a hash value.
	 * 
	 * @param serializer
	 *            serializes objects of the specified type
	 * @param <T>
	 *            the type of objects serialized
	 * @return a new hasher that uses this hash to compute the hash values
	 */

	default <T> Hasher<T> hasher(HashSerializer<T> serializer) {
		if (serializer == null) throw new IllegalArgumentException("null serializer");
		return new StandardHasher<S, T>(this, serializer);
	}

	/**
	 * Derives a {@link Hasher} by combining this hash with a
	 * {@link HashSerializer}. The supplied seed changes the hash values that
	 * will be created by the derived hasher based on this hash. This can be
	 * useful when creating data structures that need to be resiliant to
	 * orchestrated collision attacks.
	 * 
	 * @param serializer
	 *            serializes objects of the specified type
	 * @param seed
	 *            transforms the computed hash values.
	 * @param <T>
	 *            the type of objects serialized
	 * @return a new hasher that uses this hash to compute the hash values
	 */

	default <T> Hasher<T> seeded(HashSerializer<T> serializer, long seed) {
		if (serializer == null) throw new IllegalArgumentException("null serializer");
		return new SeededHasher<S, T>(this, serializer, seed);
	}
}
