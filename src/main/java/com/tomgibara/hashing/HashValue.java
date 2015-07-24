package com.tomgibara.hashing;

import java.math.BigInteger;
import java.util.Iterator;

//TODO should note possible arithmetic exceptions
public interface HashValue {

	static HashValue fromInt(int intValue) { return new IntHashValue(intValue); }

	static HashValue fromLong(long longValue) { return new LongHashValue(longValue); }

	static HashValue fromBig(BigInteger bigValue) { return new BigHashValue(bigValue); }

	static HashValue fromInts(int... intValues) {
		if (intValues == null) throw new IllegalArgumentException("null intValues");
		return new IntsHashValue(intValues);
	}

	BigInteger bigValue();

	default long longValue() {
		return bigValue().longValueExact();
	}

	default int intValue() {
		return bigValue().intValueExact();
	}

	default boolean hasNext() {
		return false;
	}

}
