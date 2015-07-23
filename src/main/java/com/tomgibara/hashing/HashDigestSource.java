package com.tomgibara.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public interface HashDigestSource {

	static HashDigestSource SHA1() {
		return StandardHashDigestSource.SHA1();
	}

	static HashDigestSource from(String algorithm) throws NoSuchAlgorithmException {
		if (algorithm == null) throw new IllegalArgumentException("null algorithm");
		return new StandardHashDigestSource(algorithm);
	}

	static HashDigestSource from(String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
		if (algorithm == null) throw new IllegalArgumentException("null algorithm");
		if (provider == null) throw new IllegalArgumentException("null provider");
		return new StandardHashDigestSource(algorithm, provider);
	}

	static HashDigestSource from(MessageDigest digest) {
		if (digest == null) throw new IllegalArgumentException("null digest");
		return new StandardHashDigestSource(digest);
	}

	MessageDigest newDigest();

	default Hash<?> asHash() {
		return new DigestHash(this);
	}

}