package com.tomgibara.hashing;

import java.math.BigInteger;

final class BigHashValue extends AbstractHashValue {

	private final BigInteger bigValue;
	
	public BigHashValue(BigInteger bigValue) {
		if (bigValue == null) throw new IllegalArgumentException("null bigValue");
		this.bigValue = bigValue;
	}
	
	@Override
	public int intValue() {
		return bigValue.intValueExact();
	}

	@Override
	public long longValue() {
		return bigValue.longValueExact();
	}

	@Override
	public BigInteger bigValue() {
		return bigValue;
	}
	
}
