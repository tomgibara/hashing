/*
 * Copyright 2011 Tom Gibara
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
package com.tomgibara.hashing.legacy;

import com.tomgibara.hashing.HashRange;
import com.tomgibara.hashing.HashStreamer;

/**
 * Hashes objects into the full range of longs, rather than Java's more limited
 * int ranged hash. Hashing null is supported if the supplied {@link HashStreamer}
 * also supports null values.
 * 
 * @author tomgibara
 * 
 * @param <T>
 *            the type of object over which hashes may be generated
 */

public class LongHash<T> extends AbstractHash<T> {

	private final HashStreamer<T> source;
	
	/**
	 * Creates a new hash using the supplied source.
	 * 
	 * @param source
	 *            converts the object into basic data for hashing
	 * @throws IllegalArgumentException
	 *             if the source is null
	 */
	
	public LongHash(HashStreamer<T> source) {
		if (source == null) throw new IllegalArgumentException("null source");
		this.source = source;
	}
	
	/**
	 * @return {@linkplain HashRange.FULL_LONG_RANGE}
	 */
	
	@Override
	public HashRange getRange() {
		return HashRange.FULL_LONG_RANGE;
	}
	
	@Override
	public long longHashValue(T value) {
		CondensingWriteStream out = new CondensingWriteStream();
		source.stream(value, out);
		return out.getCondensedValue();
	}
	
}