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

import com.tomgibara.streams.WriteStream;

/**
 * <p>
 * Implementations of this interface can generate one hash value for a given
 * object. Depending upon the implementation, null values may be supported.
 * </p>
 * 
 * @author tomgibara
 * 
 * @param <T>
 *            the type of objects for which hashes may be generated
 */

public interface Hasher<T> extends Hashing<T> {

	static <S extends WriteStream,T> Hasher<T> from(final Hash<S> hash, final HashStreamer<T> streamer) {
		if (hash == null) throw new IllegalArgumentException("null hash");
		if (streamer == null) throw new IllegalArgumentException("null streamer");
		return new Hasher<T>() {
			
			@Override
			public HashRange getRange() {
				return hash.getRange();
			}
			
			@Override
			public HashValue hashValue(T value) {
				return hash.hashValue(stream(value));
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
			
			private S stream(T value) {
				S stream = hash.newStream();
				streamer.stream(value, stream);
				return stream;
			}
			
			@Override
			public int getQuantity() {
				return hash.getQuantity();
			}
			
		};
	}
	
}
