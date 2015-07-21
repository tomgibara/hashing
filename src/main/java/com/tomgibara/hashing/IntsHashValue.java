package com.tomgibara.hashing;

import java.math.BigInteger;
import java.util.NoSuchElementException;

final class IntsHashValue extends AbstractHashValue {

	private final int[] intValues;
	private int index = 0;
	
	IntsHashValue(int[] intValues) {
		this.intValues = intValues;
	}
	
	@Override
	public int intValue() {
		checkIndex();
		return intValues[index++];
	}
	
	@Override
	public long longValue() {
		checkIndex();
		return intValues[index++];
	}
	
	@Override
	public BigInteger bigValue() {
		checkIndex();
		return BigInteger.valueOf(intValues[index++]);
	}

	@Override
	public boolean hasNext() {
		return index < intValues.length;
	}

	@Override
	public HashValue next() {
		checkIndex();
		index ++;
		return this;
	}

	private void checkIndex() {
		if (index == intValues.length) throw new NoSuchElementException();
	}

}
