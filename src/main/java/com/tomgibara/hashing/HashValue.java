package com.tomgibara.hashing;

import java.math.BigInteger;
import java.util.Iterator;

public interface HashValue extends Iterator<HashValue> {

	BigInteger bigValue();
	
	default long longValue() {
		return bigValue().longValueExact();
	}
	
	default int intValue() {
		return bigValue().intValueExact();
	}

	@Override
	boolean hasNext();
	
	@Override
	default HashValue next() {
		return this;
	}
	
	@Override
	default void remove() {
		throw new UnsupportedOperationException();
	}
	
}
