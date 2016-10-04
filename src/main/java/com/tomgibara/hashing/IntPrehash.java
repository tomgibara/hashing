package com.tomgibara.hashing;

@FunctionalInterface
public interface IntPrehash<T> {

	int preHash(T value);

}
