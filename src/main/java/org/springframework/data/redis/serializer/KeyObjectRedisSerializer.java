/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.springframework.data.redis.serializer;

import java.nio.charset.Charset;

import org.springframework.util.Assert;

public class KeyObjectRedisSerializer implements RedisSerializer<Object> {
	private final Charset charset;

	public KeyObjectRedisSerializer() {
		this(Charset.forName("UTF8"));
	}

	public KeyObjectRedisSerializer(Charset charset) {
		Assert.notNull(charset);
		this.charset = charset;
	}

	public String deserialize(byte[] bytes) {
		return new String(bytes, this.charset);
	}

	@Override
	public byte[] serialize(Object object) throws SerializationException {
		return ((object == null) ? null : object.toString().getBytes(this.charset));
	}
}