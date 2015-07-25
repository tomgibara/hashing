package com.tomgibara.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * Generates digests from which hashes may be derived.
 * 
 * @author Tom Gibara
 *
 */

public interface HashDigestSource {

	/**
	 * Provides a standard source of SHA-1 digests.
	 * 
	 * @return a source of SHA-1 digests
	 */

	static HashDigestSource SHA1() {
		return StandardHashDigestSource.SHA1();
	}

	/**
	 * Creates a digest source for the specified algorithm based on the
	 * platform's default provider.
	 * 
	 * @param algorithm
	 *            the name of the digest algorithm
	 * @return a source of the specified digests
	 * @throws NoSuchAlgorithmException
	 *             if there is no digest provided for the specified algorithm
	 */

	static HashDigestSource from(String algorithm) throws NoSuchAlgorithmException {
		if (algorithm == null) throw new IllegalArgumentException("null algorithm");
		return new StandardHashDigestSource(algorithm);
	}

	/**
	 * Creates a digest source for the specified algorithm based on the supplied
	 * platform.
	 * 
	 * @param algorithm
	 *            the name of the digest algorithm
	 * @param provider
	 *            the name of the digest provider
	 * @return a source of digests using the chosen provider
	 * @throws NoSuchProviderException
	 *             if there is no provider with the given name
	 * @throws NoSuchAlgorithmException
	 *             if the provider does not support an algorithm with the given
	 *             name
	 */

	static HashDigestSource from(String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
		if (algorithm == null) throw new IllegalArgumentException("null algorithm");
		if (provider == null) throw new IllegalArgumentException("null provider");
		return new StandardHashDigestSource(algorithm, provider);
	}

	/**
	 * Attempts to derive an digest source by cloning the supplied digest.
	 * 
	 * @param digest
	 *            the digest to be cloned
	 * @return a source of digests based on cloning
	 */

	static HashDigestSource from(MessageDigest digest) {
		if (digest == null) throw new IllegalArgumentException("null digest");
		return new StandardHashDigestSource(digest);
	}

	/**
	 * Creates a new digest instance
	 * 
	 * @return a digest
	 */
	MessageDigest newDigest();

	/**
	 * Derives a hash from the digest. The {@link HashSize} of the resulting
	 * hash is determined by the length of the digest.
	 * 
	 * @return a {@link Hash} implementation that uses the digests generated by
	 *         this source to derive the hash values.
	 */

	default Hash<?> asHash() {
		return new DigestHash(this);
	}

}