package com.tomgibara.hashing;

import java.math.BigInteger;

final class BigHashValue extends AbstractHashValue {

	private final BigInteger bigValue;

	public BigHashValue(BigInteger bigValue) {
		if (bigValue == null) throw new IllegalArgumentException("null bigValue");
		this.bigValue = bigValue;
	}

	@Override
	public BigInteger bigValue() {
		return bigValue;
	}

}
