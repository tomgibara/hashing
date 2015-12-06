package com.tomgibara.hashing;

abstract class AbstractHashCode implements HashCode {

	final HashSize size;
	
	AbstractHashCode(HashSize size) {
		if (size == null) throw new IllegalArgumentException("null size");
		this.size = size;
	}
	
	@Override
	public HashSize size() {
		return size;
	}
	
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
