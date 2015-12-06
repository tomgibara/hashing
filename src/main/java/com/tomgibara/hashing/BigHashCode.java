package com.tomgibara.hashing;

import java.math.BigInteger;

final class BigHashCode extends AbstractHashCode {

	private final BigInteger bigValue;

	@Override
	public boolean hasNext() {
		return true;
	}

	public BigHashCode(HashSize size, BigInteger bigValue) {
		super(size);
		if (bigValue == null) throw new IllegalArgumentException("null bigValue");
		this.bigValue = bigValue;
	}

	@Override
	public BigInteger bigValue() {
		return bigValue;
	}

}
