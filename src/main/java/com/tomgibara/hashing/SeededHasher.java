package com.tomgibara.hashing;

import com.tomgibara.streams.WriteStream;

class SeededHasher<S extends WriteStream,T> extends StandardHasher<S,T> {

	private final long seed;

	SeededHasher(Hash<S> hash, HashStreamer<T> streamer, long seed) {
		super(hash, streamer);
		this.seed = seed;
	}

	@Override
	S newStream() {
		S stream = super.newStream();
		stream.writeLong(seed);
		return stream;
	}

}
