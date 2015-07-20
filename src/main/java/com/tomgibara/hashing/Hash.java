package com.tomgibara.hashing;

public interface Hash {

	HashRange getRange();
	
	int getQuantity();
	
	HashStream newStream();
	
}
