package cn.explink.domain;

/**
 *
 * 值对.
 *
 * @author zhaoshb
 * @since DMP3.0
 */
public class Pair<K, V> {
	private K v1 = null;

	private V v2 = null;

	public Pair(K k, V v) {
		this.v1 = k;
		this.v2 = v;
	}

	public K getV1() {
		return this.v1;
	}

	public V getV2() {
		return this.v2;
	}
}