package com.tomgibara.hashing;

import java.math.BigInteger;

import com.tomgibara.streams.WriteStream;

public interface HashStream extends WriteStream {

	HashValue hashValue();

	default BigInteger bigHashValue() {
		return hashValue().bigValue();
	}

	default long longHashValue() {
		return hashValue().longValue();
	}

	default int intHashValue() {
		return hashValue().intValue();
	}

	
}
