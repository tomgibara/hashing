package com.tomgibara.hashing;

import java.math.BigInteger;

public final class BigHashValue implements HashValue {

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
	
	@Override
	public int hashCode() {
		return bigValue.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof HashValue)) return false;
		return this.bigValue().equals(((HashValue) obj).bigValue());
	}
	
	@Override
	public String toString() {
		return bigValue.toString();
	}

}
