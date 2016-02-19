package cn.explink.schedule;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * 任务调度运行环境，引入CacheManager改造成分布式缓存，后续待完善。
 */
public class ScheduledTaskEnv {

	private static ScheduledTaskEnv instance = new ScheduledTaskEnv();

	@Autowired
	private CacheManager cacheManager = null;

	private static final String cacheName = "scheduledTaskEnv";

	private static String macAddress = null;

	private InetAddress get_current_ip() {
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
					.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) networkInterfaces
						.nextElement();
				Enumeration<InetAddress> nias = ni.getInetAddresses();
				while (nias.hasMoreElements()) {
					InetAddress ia = (InetAddress) nias.nextElement();
					if (!ia.isLinkLocalAddress() && !ia.isLoopbackAddress()
							&& ia instanceof Inet4Address) {
						return ia;
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String lookup_mac() {
		try {
			InetAddress ip = get_current_ip();
			System.out.println("[ScheduledTaskEnv] Current IP address : "
					+ ip.getHostAddress());
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i],
						(i < mac.length - 1) ? "-" : ""));
			}
			System.out.println("[ScheduledTaskEnv] Current MAC address : "
					+ sb.toString());
			if (sb.length() > 0) {
				return sb.toString();
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private synchronized Cache getCache() {
		if (macAddress == null && (macAddress = lookup_mac()) == null) {
			return null;
		}
		if (cacheManager != null) {
			return cacheManager.getCache(cacheName);
		}
		return null;
	}

	/**
	 * 已经提交到线程池的任务id
	 */
	private Set<Long> taskIds = new HashSet<Long>();

	private ScheduledTaskEnv() {
	}

	public static ScheduledTaskEnv getInstance() {
		return instance;
	}

	public void addTask(Long taskId) {
		Cache cache = getCache();
		if (cache != null) {
			cache.put(taskId, macAddress);
		}
		taskIds.add(taskId);
	}

	public void removeTask(Long taskId) {
		Cache cache = getCache();
		if (cache != null) {
			cache.evict(taskId);
		}
		taskIds.remove(taskId);
	}

	public boolean hasTask(Long taskId) {
		Cache cache = getCache();
		if (cache != null) {
			if (cache.get(taskId) != null) {
				String value = (String) cache.get(taskId).get();
				if (value != null && value.compareTo(macAddress) != 0) {
					return true;
				}
			}
		}
		return taskIds.contains(taskId);
	}

}
