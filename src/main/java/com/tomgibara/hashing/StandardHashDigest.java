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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;

class StandardHashDigest implements HashDigest {

	private static boolean isCloneable(MessageDigest digest) {
		try {
			digest.clone();
			return true;
		} catch (CloneNotSupportedException e) {
			return false;
		}
	}

	private static int lengthInBytes(MessageDigest digest) {
		int length = digest.getDigestLength();
		if (length < 1) throw new IllegalArgumentException("digest length unknown");
		return length;
	}

	private static HashDigest getDigestSource(String algorithm) {
		try {
			return new StandardHashDigest(algorithm);
		} catch (NoSuchAlgorithmException e) {
			// unlikely
			throw new RuntimeException(e);
		}
	}

	private static HashDigest md5 = null;
	private static HashDigest sha1 = null;
	private static HashDigest sha256 = null;

	static HashDigest MD5() { return md5 == null ? md5 = getDigestSource("MD5") : md5; }
	static HashDigest SHA_1() { return sha1 == null ? sha1 = getDigestSource("SHA-1") : sha1; }
	static HashDigest SHA_256() { return sha256 == null ? sha256 = getDigestSource("SHA-256") : sha256; }


	private final String algorithm;
	private final Provider provider;
	private final MessageDigest digest;
	final HashSize size;

	StandardHashDigest(String algorithm) throws NoSuchAlgorithmException {
		this( MessageDigest.getInstance(algorithm) );
	}

	StandardHashDigest(String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
		this( MessageDigest.getInstance(algorithm, provider) );
	}

	StandardHashDigest(MessageDigest digest) {
		algorithm = digest.getAlgorithm();
		provider = digest.getProvider();
		size = HashSize.fromByteLength( lengthInBytes(digest) );
		this.digest = isCloneable(digest) ? digest : null;
	}

	@Override
	public MessageDigest newDigest() {
		if (digest == null) {
			try {
				return provider == null ? MessageDigest.getInstance(algorithm) : MessageDigest.getInstance(algorithm, provider);
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		} else {
			try {
				return (MessageDigest) digest.clone();
			} catch (CloneNotSupportedException e) {
				//shouldn't happen
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public Hash asHash() {
		return new DigestHash(this);
	}

	@Override
	public int hashCode() {
		return algorithm.hashCode() + provider.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof StandardHashDigest)) return false;
		StandardHashDigest that = (StandardHashDigest) obj;
		if (!this.algorithm.equals(that.algorithm)) return false;
		if (!this.provider.equals(that.provider)) return false;
		return true;
	}

	@Override
	public String toString() {
		return algorithm + " from " + provider.getName();
	}
}