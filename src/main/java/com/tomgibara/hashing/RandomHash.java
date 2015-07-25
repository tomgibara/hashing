package com.tomgibara.hashing;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Random;

import com.tomgibara.streams.AbstractWriteStream;
import com.tomgibara.streams.ByteWriteStream;
import com.tomgibara.streams.WrappedWriteStream;
import com.tomgibara.streams.WriteStream;

class RandomHash implements Hash<RandomHash.SeedingStream> {

	private enum Type {
		INT, FULL_INT, LONG, LONG_BITS, FULL_LONG, BIG_BITS, BIG;

		static Type from(HashSize size) {
			if (size.isIntSized()) return INT;
			int bits = size.getBits();
			if (size.isPowerOfTwo()) {
				if (bits == 32) return FULL_INT;
				if (bits < 64) return LONG_BITS;
				if (bits == 64) return FULL_LONG;
				return BIG_BITS;
			}
			if (bits < 64) return LONG;
			return BIG;
		}
	}

	private final String algorithm;
	private final String provider;
	private final HashSize size;
	private final Type type;

	// constructors

	RandomHash(String algorithm, String provider, HashSize size) {
		this.algorithm = algorithm;
		this.provider = provider;
		this.size = size;
		type = Type.from(size);
		//verify algorithm exists
		if (algorithm != null) newRandom();
	}

	@Override
	public SeedingStream newStream() {
		if (algorithm == null) {
			return new LongSeedingStream(new Random());
		} else {
			return new BytesSeedingStream(newRandom());
		}
	}

	@Override
	public HashSize getSize() {
		return size;
	}

	@Override
	public HashValue hashValue(SeedingStream value) {
		final Random random = value.getRandom();
		return new HashValue() {

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public int intValue() {
				switch (type) {
				case INT:
					return random.nextInt(size.asInt());
				case FULL_INT:
					return random.nextInt();
				case LONG:
				case LONG_BITS:
				case FULL_LONG:
					return (int) longValue();
				case BIG_BITS:
				case BIG:
					return bigValue().intValue();
					default: throw new IllegalStateException();
				}
			}

			@Override
			public long longValue() {
				switch (type) {
				case INT:
				case FULL_INT:
					return intValue() & 0xffffffffL;
				case LONG:
					long s = size.asLong();
					while (true) {
						long bits = random.nextLong() & ~Long.MIN_VALUE;
						long value = bits % s;
						if (bits - value + (s - 1) >= 0) return value;
					}
				case LONG_BITS:
					return size.mapLong(random.nextLong());
				case FULL_LONG:
					return random.nextLong();
				case BIG_BITS:
				case BIG:
					return bigValue().longValue();
					default: throw new IllegalStateException();
				}
			}

			@Override
			public BigInteger bigValue() {
				switch (type) {
				case INT:
				case FULL_INT:
					return BigInteger.valueOf(intValue() & 0xffffffffL);
				case LONG:
				case LONG_BITS:
					return BigInteger.valueOf(longValue());
				case FULL_LONG:
					return HashSize.BIG_ULONG.add(BigInteger.valueOf(longValue()));
				case BIG_BITS:
					return new BigInteger(size.getBits(), random);
				case BIG:
					//TODO want a better solution for this
					return size.mapBig(new BigInteger(size.getBits() + 16, random));
					default: throw new IllegalStateException();
				}
			}
		};
	}

	// private methods

	private SecureRandom newRandom() {
		try {
			return provider == null ? SecureRandom.getInstance(algorithm) : SecureRandom.getInstance(algorithm, provider);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchProviderException e) {
			throw new RuntimeException(e);
		}
	}

	// inner classes

	interface SeedingStream extends WriteStream {

		Random getRandom();

	}

	private static class LongSeedingStream extends AbstractWriteStream implements SeedingStream {

		private final Random random;
		private long seed = 0L;

		LongSeedingStream(Random random) {
			this.random = random;
		}

		@Override
		public void writeByte(byte v) {
			seed = seed * 31 + v & 0xff;
		}

		@Override
		public void writeChar(char v) {
			seed = seed * 31 + v;
		}

		@Override
		public void writeShort(short v) {
			seed = seed * 31 + v & 0xffff;
		}

		@Override
		public void writeInt(int v) {
			seed = seed * 31 + v & 0xffffffffL;
		}

		@Override
		public void writeLong(long v) {
			seed = seed * 31 + v;
		}

		@Override
		public Random getRandom() {
			random.setSeed(seed);
			return random;
		}

	}

	private static class BytesSeedingStream extends WrappedWriteStream<ByteWriteStream> implements SeedingStream {

		private final SecureRandom random;

		BytesSeedingStream(SecureRandom random) {
			super(new ByteWriteStream());
			this.random = random;
		}

		@Override
		public Random getRandom() {
			random.setSeed(wrapped.getBytes());
			return random;
		}

	}
}
