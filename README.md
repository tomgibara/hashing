Hashing
=======

Overview
--------

This library provides an API for defining hash functions and computing their
values. It aims for generality while providing for efficient implementation.
The core abstractions are:

 * `HashValue` - the result of computing a hash.
 * `HashStreamer` - decomposes objects into a stream for them to be hashed. 
 * `Hash` - converts the contents of a stream into a `HashValue`
 * `Hasher` can converts an object into a `HashValue`,
    often by way of a `Hash` combined with a `HashStreamer`
 * `Hashing` - the entry-point to the API and the basis for obtaining
    provided `Hash` and `Hasher` implementations.

Examples
--------

```java
// returns the object hashCode
Hasher<String> obj = Hashing.objectHasher();
obj.intHashValue(str); // returns str.hashCode()

// returns System.identityHashCode
Hasher<String> ident = Hashing.identityHasher();
ident.intHashValue(str); // returns System.identityHashCode(str)

// creates a 32 bit murmur hash code...
Hasher<String> murmur = Hashing.murmur3Int()
		.hasher((s, out) -> out.writeChars(s));

// ...different object types are easily supported ...
Hasher<Point> murmurPt = Hashing.murmur3Int()
		.hasher((p, s) -> { s.writeInt(p.x); s.writeInt(p.y); });

// .. different sized hashes are easily derived.
Hasher<String> shortMurmur = murmur.sized(HashSize.SHORT_SIZE);
shortMurmur.intHashValue(str); // guaranteed to be in the range 0-65535

// derive seeded hashers to protect against collision attacks
Hasher<String> murmurSafe = Hashing.murmur3Int()
		.seeded((s, out) -> out.writeChars(s), randomSeed);
{
	murmurSafe.hash(str);
	// almost certainly not the same as murmur.hashValue(str)
}

// derive multiple hash values from a single hash function
Hasher<String> multiple = murmur.ints();
{
	HashCode value = multiple.hash(str);
	value.intValue(); // first hash value
	value.intValue(); // next hash value
	value.intValue(); // .. and so on ..
}

// derive multiple *distinct* hash values from a single hash function
Hasher<Point> distinct = murmurPt.distinct(3, HashSize.BYTE_SIZE);
{
	HashCode value = distinct.hash(pt);
	value.intValue(); // first hash value
	value.intValue(); // second hash value
	value.intValue(); // third hash value
	value.hasNext();  // returns false - supplies exactly 3 hashes
	                  // all guaranteed to be distinct
}

// produce hashes from message digests
Hash<?> sha1 = HashDigestSource.SHA1().asHash();

// produce hashes by seeding a random number generator
Hash<?> prng = Hashing.prng(HashSize.LONG_SIZE);

// use this capability to produce cryptographically secure hashes ...
Hash<?> secure = Hashing.prng("SHA1PRNG", HashSize.fromByteLength(16));

// ... with arbitrarily large hash codes
secure.hasher(someStream).bigHashValue(str); // 128 bit hash code

// "minimal perfect hashing" for strings is also provided
Hasher<String> perfect = Hashing.minimalPerfect("mouse", "cat", "dog");
perfect.intHashValue("cat"); // returns 0
perfect.intHashValue("dog"); // returns 1
perfect.intHashValue("mouse"); // returns 2
```

Comparison with Guava
---------------------

A high quality hashing library is already available for Java as part of the
Guava library [*][0] but this library has some capabilities that Guava does not
currently have. Any of these may be necessary for your application:

 * Direct emulation of `Object.hashCode()` and `System.identityHashCode()`
   (so existing Java hash codes can be exposed through the API).
 * Support for multi-valued hashes
   (useful for probabalistic data structures such as Bloom filters).
 * Hashes that do not necessarily span an exact number of bits
   (this includes support for perfect hashing of strings).

Usage
-----

The hashing library is available from the Maven central repository:

> Group ID:    `com.tomgibara.hashing`
> Artifact ID: `hashing`
> Version:     `1.0.0`

The Maven dependency being:

    <dependency>
      <groupId>com.tomgibara.hashing</groupId>
      <artifactId>hashing</artifactId>
      <version>1.0.0</version>
    </dependency>

Release History
---------------

**2015.07.27** Version 1.0.0

Initial release

[0]: https://github.com/google/guava
