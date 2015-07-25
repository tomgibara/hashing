package com.tomgibara.hashing;

import com.tomgibara.streams.WriteStream;

/**
 * <p>
 * A hash creates streams that it can later convert into {@link HashValue}s.
 * Streams are ordinarily populated by a {@link HashStreamer} implementation.
 * Applications are generally expected to use instances of this class indirectly
 * by deriving {@link Hasher}s from them.
 * 
 * <p>
 * New hash implementations can be introduced by implementing this interface.
 * Hashes that generate multiple hash values will want to indicate this via the
 * {@link #getQuantity()} method.
 * 
 * @author Tom Gibara
 *
 * @param <S>
 *            the type of stream which is created and then hashed
 */

public interface Hash<S extends WriteStream> extends Hashing<S> {

	/**
	 * Creates new stream that can later be passed back to the object to derive
	 * a {@link HashValue}.
	 * 
	 * @return a new stream for accumulating object data
	 */

	S newStream();

	/**
	 * Derives a {@link Hasher} by combining this hash with a
	 * {@link HashStreamer}: a new stream will be created by the hash, populated
	 * by streamer, and converted into a hash value.
	 * 
	 * @param streamer
	 *            serializes objects of the specified type
	 * @param <T>
	 *            the type of objects serialized by the streamer
	 * @return a new hasher that uses this hash to compute the hash values
	 */

	default <T> Hasher<T> hasher(HashStreamer<T> streamer) {
		if (streamer == null) throw new IllegalArgumentException("null streamer");
		return new StandardHasher<S, T>(this, streamer);
	}

	/**
	 * Derives a {@link Hasher} by combining this hash with a
	 * {@link HashStreamer}. The supplied seed changes the hash values that will
	 * be created by the derived hasher based on this hash. This can be useful
	 * when creating data structures that need to be resiliant to orchestrated
	 * collision attacks.
	 * 
	 * @param streamer
	 *            serializes objects of the specified type
	 * @param seed
	 *            transforms the computed hash values.
	 * @param <T>
	 *            the type of objects serialized by the streamer
	 * @return a new hasher that uses this hash to compute the hash values
	 */

	default <T> Hasher<T> seeded(HashStreamer<T> streamer, long seed) {
		if (streamer == null) throw new IllegalArgumentException("null streamer");
		return new SeededHasher<S, T>(this, streamer, seed);
	}
}
