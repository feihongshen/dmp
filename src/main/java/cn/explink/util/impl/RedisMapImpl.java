package cn.explink.util.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import cn.explink.core.utils.SpringContextUtils;
import cn.explink.util.RedisMap;
import org.apache.commons.codec.binary.Base64;

/**
 * @author pengfei.wan 
 * 实现 redis map，用于集群
 */
public class RedisMapImpl<T1, T2> implements RedisMap<T1, T2> {

	private static String macAddress = null;

	private String cacheName = null;

	public Map<T1, T2> threadMap = new HashMap<T1, T2>();

	private CacheManager cacheManager = null;

	private boolean show_msg = true;

	public RedisMapImpl(String cacheName) {
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

	private InetAddress get_current_ip() {
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) networkInterfaces.nextElement();
				Enumeration<InetAddress> nias = ni.getInetAddresses();
				while (nias.hasMoreElements()) {
					InetAddress ia = (InetAddress) nias.nextElement();
					if (!ia.isLinkLocalAddress() && !ia.isLoopbackAddress() && ia instanceof Inet4Address) {
						return ia;
					}
				}
			}
		} catch (SocketException e) {
			print_exception(e);
		}
		return null;
	}

	private String lookup_mac() {
		try {
			InetAddress ip = get_current_ip();
			System.out.println("[RedisMapImpl] Current IP address : " + ip.getHostAddress());
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			System.out.println("[RedisMapImpl] Current MAC address : " + sb.toString());
			if (sb.length() > 0) {
				return sb.toString();
			}
		} catch (SocketException e) {
			print_exception(e);
		} catch (Exception e) {
			print_exception(e);
		}
		return null;
	}

	private synchronized Cache getCache() {
		try {
			if (cacheName == null) {
				return null;
			}
			if (macAddress == null && (macAddress = lookup_mac()) == null) {
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

	private static Object fromString(String s) throws IOException, ClassNotFoundException {
		byte[] data = Base64.decodeBase64(s);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		Object o = ois.readObject();
		ois.close();
		return o;
	}

	private static String toString(Serializable o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return new String(Base64.encodeBase64(baos.toByteArray()));
	}

	private String makeValue(T2 value) {
		String v = "";
		try {
			v = toString((Serializable) value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return macAddress + "_" + v;
	}

	private boolean isOtherServer(String value) {
		if (value != null) {
			return !value.startsWith(macAddress);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private T2 getValue(String value) {
		try {
			if (value != null) {
				int pos = value.indexOf('_');
				if (pos > 0) {
					String v = value.substring(pos + 1);
					if (v != null && v.length() > 0) {
						return (T2) fromString(v);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public T2 get(T1 key) {
		Cache cache = getCache();
		if (cache != null && cache.get(key) != null) {
			String value = (String) cache.get(key).get();
			if (value != null) {
				if (isOtherServer(value)) {
					return getValue(value);
				}
			}
		}
		return threadMap.get(key);
	}

	@Override
	public void put(T1 key, T2 value) {
		Cache cache = getCache();
		if (cache != null) {
			cache.put(key, makeValue(value));
		}
		threadMap.put(key, value);
	}

	@Override
	public T2 remove(T1 key) {
		Cache cache = getCache();
		if (cache != null) {
			cache.evict(key);
		}
		return threadMap.remove(key);
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
		threadMap.clear();
	}

}
