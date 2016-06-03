package cn.explink.util.impl;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import cn.explink.core.utils.SpringContextUtils;
import cn.explink.util.RedisMap;

/**
 * @author pengfei.wan 
 * 实现 redis common map，用于集群
 */
public class RedisMapCommonImpl<T1, T2> implements RedisMap<T1, T2> {

	private String cacheName = null;

	private CacheManager cacheManager = null;

	private boolean show_msg = true;

	public RedisMapCommonImpl(String cacheName) {
		super();
		this.cacheName = cacheName;
	}

	private void print_exception(Exception e) {
		if (show_msg) {
			try {
				e.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			show_msg = false;
		}
	}

	private synchronized Cache getCache() {
		try {
			if (cacheName == null) {
				return null;
			}
			if (cacheManager != null) {
				return cacheManager.getCache(cacheName);
			}
			ApplicationContext ac = SpringContextUtils.getContext();
			if (ac != null) {
				cacheManager = ac.getBean("cacheManager", CacheManager.class);
			}
			if (cacheManager != null) {
				return cacheManager.getCache(cacheName);
			}
		} catch (Exception e) {
			print_exception(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T2 get(T1 key) {
		T2 value = null;
		Cache cache = getCache();
		if (cache != null && cache.get(key) != null) {
			value = (T2) cache.get(key).get();
		}
		return value;
	}

	@Override
	public void put(T1 key, T2 value) {
		Cache cache = getCache();
		if (cache != null) {
			cache.put(key, value);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T2 remove(T1 key) {
		T2 value = null;
		Cache cache = getCache();
		if (cache != null) {
			if (cache.get(key) != null) {
				value = (T2) cache.get(key).get();
			}
			cache.evict(key);
		}
		return value;
	}

	@Override
	public String getName() {
		return cacheName;
	}

	@Override
	public void clear() {
		Cache cache = getCache();
		if (cache != null) {
			cache.clear();
		}
	}

}
