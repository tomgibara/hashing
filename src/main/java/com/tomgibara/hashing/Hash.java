package com.tomgibara.hashing;

import com.tomgibara.streams.WriteStream;

public interface Hash<S extends WriteStream> extends Hashing<S> {

	S newStream();
	
}
