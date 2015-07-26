package com.tomgibara.hashing;

abstract class AbstractHashCode implements HashCode {

	@Override
	public int hashCode() {
		return intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof HashCode)) return false;
		return this.bigValue().equals(((HashCode) obj).bigValue());
	}

	@Override
	public String toString() {
		return bigValue().toString();
	}

}
