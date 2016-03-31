package cn.explink.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class ServiceUtil {

	private static Logger logger = LoggerFactory.getLogger(ServiceUtil.class);
	
	private static ResourceBundle dmp = ResourceBundle.getBundle("filepath");
	public static final String omsSysUrl = dmp.getString("omsUrl");
	public static final String financeSysUrl = dmp.getString("financeUrl");
	public static final String jspPath = File.separator + "mould" + File.separator;
	public static final String xlsPath = "xls\\";
	public static final String wavPath = "/wav/";
	public static final String imgPath = "/uploadimg/";
	public static final String waverrorPath = "/images/waverror/";
	public static Map<Long, Long> nowImport = new HashMap<Long, Long>();

	public static String uploadWavFile(MultipartFile file, String filePath, String name) {
		try {
			checkfilePath(filePath);
			File upfile = new File(filePath + name);
			file.transferTo(upfile);
			return name;
		} catch (IOException e) {
			logger.error("", e);
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
			logger.error("", e);
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
			logger.error("", e);
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
		return ServiceUtil.nowImport.get(emaildateid) != null && nowImport.get(emaildateid) > 0;
	}

	/**
	 * 开始导入标识
	 * 
	 * @param emaildateid
	 * @return
	 */
	public static void startNowImport(long emaildateid) {
		ServiceUtil.nowImport.put(emaildateid, 1L);
	}

	/**
	 * 导入结束调用
	 * 
	 * @param emaildateid
	 * @return
	 */
	public static void endNowImport(long emaildateid) {
		ServiceUtil.nowImport.remove(emaildateid);
	}

}
