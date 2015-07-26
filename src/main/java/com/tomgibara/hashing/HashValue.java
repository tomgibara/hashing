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

public interface HashValue {

	static HashValue fromInt(int intValue) { return new IntHashValue(intValue); }

	static HashValue fromLong(long longValue) { return new LongHashValue(longValue); }

	static HashValue fromBig(BigInteger bigValue) { return new BigHashValue(bigValue); }

	static HashValue fromInts(int... intValues) {
		if (intValues == null) throw new IllegalArgumentException("null intValues");
		return new IntsHashValue(intValues);
	}

	BigInteger bigValue();

	default long longValue() {
		return bigValue().longValueExact();
	}

	default int intValue() {
		return bigValue().intValueExact();
	}

	default boolean hasNext() {
		return false;
	}

}
