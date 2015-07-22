package com.tomgibara.hashing;

import com.tomgibara.streams.WriteStream;

public interface Hash<S extends WriteStream> extends Hashing<S> {

	S newStream();

	default <T> Hasher<T> hasher(HashStreamer<T> streamer) {
		return new StandardHasher<S, T>(this, streamer);
	}

	default <T> Hasher<T> seeded(HashStreamer<T> streamer, long seed) {
		return new SeededHasher<S, T>(this, streamer, seed);
	}
}
