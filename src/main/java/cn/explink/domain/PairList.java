package cn.explink.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 值对列表.
 *
 * @author zhaoshb
 * @since DMP3.0
 */
public class PairList<K, V> extends ArrayList<Pair<K, V>> {

	private static final long serialVersionUID = 8855045155045335560L;

	public void add(K v1, V v2) {
		this.add(new Pair<K, V>(v1, v2));
	}

	public List<K> getV1List() {
		List<K> v1List = new ArrayList<K>();
		for (Pair<K, V> tmp : this) {
			v1List.add(tmp.getV1());
		}
		return v1List;
	}

	public List<V> getV2List() {
		List<V> v2List = new ArrayList<V>();
		for (Pair<K, V> tmp : this) {
			v2List.add(tmp.getV2());
		}
		return v2List;
	}
}
