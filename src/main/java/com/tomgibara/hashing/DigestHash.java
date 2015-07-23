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
