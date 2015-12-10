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

import com.tomgibara.streams.WriteStream;

final class DigestHash implements Hash {

	private final HashDigest digestSource;
	private final HashSize size;

	DigestHash(HashDigest digestSource) {
		this.digestSource = digestSource;
		size = HashSize.fromByteLength(digestSource.newDigest().getDigestLength());
	}

	DigestHash(StandardHashDigest digestSource) {
		this.digestSource = digestSource;
		size = digestSource.size;
	}

	@Override
	public HashSize getSize() {
		return size;
	}

	@Override
	public WriteStream newStream() {
		return new DigestStream(digestSource.newDigest());
	}

	@Override
	public HashCode hash(WriteStream stream) {
		return new BigHashCode(size, bigHashValue(stream));
	}

	@Override
	public byte[] bytesHashValue(WriteStream stream) {
		return ((DigestStream) stream).getDigest().digest();
	}

	@Override
	public BigInteger bigHashValue(WriteStream stream) {
		return AbstractHashCode.bigFromBytes(bytesHashValue(stream));
	}

	@Override
	public long longHashValue(WriteStream stream) {
		return AbstractHashCode.longFromBytes(bytesHashValue(stream));
	}

	@Override
	public int intHashValue(WriteStream stream) {
		return AbstractHashCode.intFromBytes(bytesHashValue(stream));
	}

	static class DigestStream implements WriteStream {

		private final MessageDigest digest;

		/**
		 * Creates a new stream which writes values to the supplied digest
		 *
		 * @param digest
		 *            digests the resulting byte stream
		 */

		DigestStream(MessageDigest digest) {
			this.digest = digest;
		}

		public MessageDigest getDigest() {
			return digest;
		}

		@Override
		public void writeByte(byte v) {
			digest.update(v);
		}

		@Override
		public void writeBytes(byte[] vs) {
			digest.update(vs);
		}

		@Override
		public void writeBytes(byte[] vs, int off, int len) {
			digest.update(vs, off, len);
		}

		@Override
		public void writeBoolean(boolean v) {
			digest.update((byte) (v ? -1 : 0));
		}

		@Override
		public void writeInt(int v) {
			digest.update((byte) (v >> 24));
			digest.update((byte) (v >> 16));
			digest.update((byte) (v >>  8));
			digest.update((byte) (v      ));
		}

		@Override
		public void writeShort(short v) {
			digest.update((byte) (v >>  8));
			digest.update((byte) (v      ));
		}

		@Override
		public void writeLong(long v) {
			digest.update((byte) (v >> 56));
			digest.update((byte) (v >> 48));
			digest.update((byte) (v >> 40));
			digest.update((byte) (v >> 32));
			digest.update((byte) (v >> 24));
			digest.update((byte) (v >> 16));
			digest.update((byte) (v >>  8));
			digest.update((byte) (v      ));
		}

		@Override
		public void writeChar(char v) {
			digest.update((byte) (v >>  8));
			digest.update((byte) (v      ));
		}

		@Override
		public void writeChars(char[] vs, int off, int len) {
			final int lim = off + len;
			for (int i = off; i < lim; i++) {
				final char v = vs[i];
				digest.update((byte) (v >>  8));
				digest.update((byte) (v      ));
			}
		}

	}

}
