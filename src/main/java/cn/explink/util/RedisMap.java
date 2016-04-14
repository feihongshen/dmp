package cn.explink.util;

/**
 * @author pengfei.wan 
 * 实现 redis map，用于集群
 */
public interface RedisMap<T1, T2> {

	public String getName();

	public T2 get(T1 key);

	public void put(T1 key, T2 value);

	public T2 remove(T1 key);

	public void clear();

}
