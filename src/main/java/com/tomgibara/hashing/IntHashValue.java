package com.tomgibara.hashing;

import java.math.BigInteger;

final class IntHashValue extends AbstractHashValue {

	private final int intValue;

	public IntHashValue(int intValue) {
		this.intValue = intValue;
	}

	@Override
	public int intValue() {
		return intValue;
	}

	@Override
	public long longValue() {
		return intValue;
	}

	@Override
	public BigInteger bigValue() {
		return BigInteger.valueOf(intValue);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj instanceof IntHashValue) return this.intValue == ((IntHashValue) obj).intValue;
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return Integer.toString(intValue);
	}

}
