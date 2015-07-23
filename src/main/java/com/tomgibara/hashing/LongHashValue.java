package com.tomgibara.hashing;

import java.math.BigInteger;

final class LongHashValue extends AbstractHashValue {

	private final long longValue;

	public LongHashValue(long longValue) {
		this.longValue = longValue;
	}

	@Override
	public int intValue() {
		int intValue = (int) longValue;
		if (intValue != longValue) throw new ArithmeticException("hash value too large");
		return intValue;
	}

	@Override
	public long longValue() {
		return longValue;
	}

	@Override
	public BigInteger bigValue() {
		return BigInteger.valueOf(longValue);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj instanceof LongHashValue) return this.longValue == ((LongHashValue) obj).longValue;
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return Long.toString(longValue);
	}

}
