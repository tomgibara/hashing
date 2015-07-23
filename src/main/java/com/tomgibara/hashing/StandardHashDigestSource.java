package com.tomgibara.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;

class StandardHashDigestSource implements HashDigestSource {

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
	
	private static HashDigestSource getDigestSource(String algorithm) {
		try {
			return new StandardHashDigestSource(algorithm);
		} catch (NoSuchAlgorithmException e) {
			// unlikely
			throw new RuntimeException(e);
		}
	}
	
	private static HashDigestSource sha1 = null;

	static HashDigestSource SHA1() { return sha1 == null ? sha1 = getDigestSource("SHA1") : sha1; }


	private final String algorithm;
	private final Provider provider;
	private final MessageDigest digest;
	final HashSize size;

	StandardHashDigestSource(String algorithm) throws NoSuchAlgorithmException {
		this( MessageDigest.getInstance(algorithm) );
	}

	StandardHashDigestSource(String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
		this( MessageDigest.getInstance(algorithm, provider) );
	}

	StandardHashDigestSource(MessageDigest digest) {
		algorithm = digest.getAlgorithm();
		provider = digest.getProvider();
		if (isCloneable(digest)) throw new IllegalArgumentException("digest not cloneable");
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
	public Hash<?> asHash() {
		return new DigestHash(this);
	}
}