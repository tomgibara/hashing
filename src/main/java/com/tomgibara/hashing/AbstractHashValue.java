package com.tomgibara.hashing;

abstract class AbstractHashValue implements HashValue {

	@Override
	public int hashCode() {
		return intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof HashValue)) return false;
		return this.bigValue().equals(((HashValue) obj).bigValue());
	}

	@Override
	public String toString() {
		return bigValue().toString();
	}

}
