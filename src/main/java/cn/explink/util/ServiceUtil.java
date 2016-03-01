package cn.explink.util;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import cn.explink.core.utils.SpringContextUtils;

public class ServiceUtil {

	public static final String jspPath = File.separator + "mould" + File.separator;
	public static final String xlsPath = "xls\\";
	public static final String wavPath = "/wav/";
	public static final String imgPath = "/uploadimg/";
	public static final String waverrorPath = "/images/waverror/";
	public static Map<Long, Long> nowImport = new HashMap<Long, Long>();

	@Autowired
	private static CacheManager cacheManager = null;

	private static final String cacheName = "serviceUtil";

	private static String macAddress = null;

	private static boolean show_msg = true;

	private static void print_exception(Exception e) {
		if (show_msg) {
			try {
				e.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			show_msg = false;
		}
	}

	private static InetAddress get_current_ip() {
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

	private static String lookup_mac() {
		try {
			InetAddress ip = get_current_ip();
			System.out.println("[ScheduledTaskEnv] Current IP address : " + ip.getHostAddress());
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			System.out.println("[ScheduledTaskEnv] Current MAC address : " + sb.toString());
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

	private static synchronized Cache getCache() {
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
		return null;
	}

	private static String makeValue(Long id) {
		return macAddress + "_" + id;
	}

	private static boolean isOtherServer(String value) {
		if (value != null) {
			return !value.startsWith(macAddress);
		}
		return false;
	}

	public static String uploadWavFile(MultipartFile file, String filePath, String name) {
		try {
			checkfilePath(filePath);
			File upfile = new File(filePath + name);
			file.transferTo(upfile);
			return name;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String uploadtxtFile(MultipartFile file, String filePath, String name) {
		try {
			checkfilePath(filePath);
			File upfile = new File(filePath + name);
			file.transferTo(upfile);
			return name;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String uploadimgFile(MultipartFile file, String filePath, String name) {
		try {
			checkfilePath(filePath);
			File upfile = new File(filePath + name);
			file.transferTo(upfile);
			return name;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void checkfilePath(String filePath) {
		File dirname = new File(filePath);
		if (!dirname.isDirectory()) { // 目录不存在
			dirname.mkdirs(); // 创建目录
		}
	}

	/**
	 * 是否正在导入当前这批数据
	 * 
	 * @param emaildateid
	 * @return
	 */
	public static boolean isNowImport(long emaildateid) {
		Cache cache = getCache();
		if (cache != null && cache.get(emaildateid) != null) {
			String value = (String) cache.get(emaildateid).get();
			if (value != null) {
				if (isOtherServer(value)) {
					return true;
				}
			}
		}
		return ServiceUtil.nowImport.get(emaildateid) != null && nowImport.get(emaildateid) > 0;
	}

	/**
	 * 开始导入标识
	 * 
	 * @param emaildateid
	 * @return
	 */
	public static void startNowImport(long emaildateid) {
		Cache cache = getCache();
		if (cache != null) {
			cache.put(emaildateid, makeValue(1L));
		}
		ServiceUtil.nowImport.put(emaildateid, 1L);
	}

	/**
	 * 导入结束调用
	 * 
	 * @param emaildateid
	 * @return
	 */
	public static void endNowImport(long emaildateid) {
		Cache cache = getCache();
		if (cache != null) {
			cache.evict(emaildateid);
		}
		ServiceUtil.nowImport.remove(emaildateid);
	}

}
