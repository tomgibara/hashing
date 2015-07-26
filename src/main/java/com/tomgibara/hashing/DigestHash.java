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
import java.security.MessageDigest;

import com.tomgibara.streams.DigestWriteStream;

final class DigestHash implements Hash<DigestWriteStream> {

	private final HashDigestSource digestSource;
	private final HashSize size;

	DigestHash(HashDigestSource digestSource) {
		this.digestSource = digestSource;
		size = HashSize.fromByteLength(digestSource.newDigest().getDigestLength());
	}

	DigestHash(StandardHashDigestSource digestSource) {
		this.digestSource = digestSource;
		size = digestSource.size;
	}

	@Override
	public HashSize getSize() {
		return size;
	}

	@Override
	public DigestWriteStream newStream() {
		MessageDigest digest = digestSource.newDigest();
		return new DigestWriteStream(digest);
	}

	@Override
	public HashValue hashValue(DigestWriteStream stream) {
		return new BigHashValue(bigHashValue(stream));
	}

	@Override
	public BigInteger bigHashValue(DigestWriteStream stream) {
		return new BigInteger(1, stream.getDigest().digest());
	}

	@Override
	public long longHashValue(DigestWriteStream stream) {
		return bigHashValue(stream).longValueExact();
	}

	@Override
	public int intHashValue(DigestWriteStream stream) {
		return bigHashValue(stream).intValueExact();
	}

}
