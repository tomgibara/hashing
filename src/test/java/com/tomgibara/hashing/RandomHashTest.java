/*
 * Copyright 2010 Tom Gibara
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.tomgibara.hashing;

import java.math.BigInteger;

import com.tomgibara.streams.WriteStream;

public class RandomHashTest extends HashingTest {

	private static final HashStreamer<Integer> streamer = new HashStreamer<Integer>() {

		@Override
		public void stream(Integer value, WriteStream stream) {
			stream.writeInt(value);
		}
	};

	public void testBasic() throws Exception {
		test(HashSize.fromIntSize(50), 10);
		test(HashSize.fromLongSize(1L << 48), 10);
		test(HashSize.fromLongSize(0x51de5add1eL), 10);
		test(HashSize.fromBigSize(BigInteger.ONE.shiftLeft(175)), 10);
		test(HashSize.fromBigSize(new BigInteger("237497854858736458783445")), 10);
	}

	private void test(HashSize size, int quantity) {
		test(Hashing.prng(size), 10);
		test(Hashing.prng("SHA1PRNG", size), 10);
	}

	private void test(Hash<?> hash, int quantity) {
		Hasher<Integer> hasher = hash.hasher(streamer);
		final BigInteger s = hasher.getSize().getSize();
		for (int i = 0; i < 10000; i++) {
			HashValue h = hasher.hashValue(i);
			testCorrectlySizedInts(h, hasher.getSize(), quantity);
			testCorrectlySizedLongs(h, hasher.getSize(), quantity);
			testCorrectlySizedBigs(h, hasher.getSize(), quantity);
			//TODO need to execute a statistical test for uniform distribution here
		}
	}
}
