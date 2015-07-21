package com.tomgibara.hashing;

import java.math.BigInteger;

import com.tomgibara.streams.WriteStream;

class StandardHasher<S extends WriteStream,T> implements Hasher<T> {

	private final Hash<S> hash;
	private final HashStreamer<T> streamer;
	
	StandardHasher(Hash<S> hash, HashStreamer<T> streamer) {
		this.hash = hash;
		this.streamer = streamer;
	}
	
	@Override
	public HashRange getRange() {
		return hash.getRange();
	}
	
	@Override
	public HashValue hashValue(T value) {
		return hash.hashValue(stream(value));
	}
	
	@Override
	public BigInteger bigHashValue(T value) {
		return hash.bigHashValue(stream(value));
	}
	
	@Override
	public long longHashValue(T value) {
		return hash.longHashValue(stream(value));
	}
	
	@Override
	public int intHashValue(T value) {
		return hash.intHashValue(stream(value));
	}
	
	private S stream(T value) {
		S stream = hash.newStream();
		streamer.stream(value, stream);
		return stream;
	}
	
	@Override
	public int getQuantity() {
		return hash.getQuantity();
	}
	
}
