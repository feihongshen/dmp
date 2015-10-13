package cn.explink.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

/**
 * 
 * @classname: Tools
 * @description: 生成静态页类
 * @author: 蓝生
 * @date: 2011-1-7 上午10:07:52
 */
public class Tools {
	final static Object lock = new Object();

	/**
	 * 返回properties文件里相应的key 的值
	 * 
	 * @param propertiesName
	 *            properties文件名称
	 * @param key
	 *            key参数
	 * @return 相应key的值
	 */
	public static String getProValue(String propertiesName, String key) {
		Tools loadProp = new Tools();
		InputStream in = loadProp.getClass().getResourceAsStream("/" + propertiesName);
		Properties prop = new Properties();
		try {
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop.getProperty(key);
	}

	/**
	 * @Title: makeHtml
	 * @Description: 向外部提供的默认方法，字符集为utf-8
	 * @param ： pageUrl 请求页面的URL地址
	 * @param ： filePath 生成html页面地址
	 * @param ： fileName 生成html页面名称
	 */
	public static boolean makeHtml(String pageUrl) {
		// 调用核心方法
		return makeHtml(pageUrl, "UTF-8");

	}

	/**
	 * @Title: makeHtml
	 * @Description: 核心方法，外部可以直接调该方法，用于重设字符集
	 * @param ： pageUrl 请求页面的URL地址
	 * @param ： filePath 生成html页面地址
	 * @param ： fileName 生成html页面名称
	 * @param ： chartset 重设的字符集
	 */
	public static boolean makeHtml(String pageUrl, String chartset) {
		synchronized (lock) {
			HttpURLConnection huc = null; // 创建url连接对象
			BufferedReader bufferedReader = null; // 创建文件读对象
			BufferedWriter bufferedWriter = null;// 创建文件写对象

			try {
				huc = (HttpURLConnection) new URL(pageUrl).openConnection(); // 打开请求页面
				System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 请求超时设置
																					// 这里单位是毫秒
				System.setProperty("sun.net.client.defaultReadTimeout", "30000");// 读取超时设置这里单位是毫秒
				huc.connect();// 载入页面
				InputStream stream = huc.getInputStream();// 创建输入流

				/* 判断路径是否存在 不存在就自动创建 end */
				bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/"), chartset));// 设置写文件的路径和文件名称
				bufferedReader = new BufferedReader(new InputStreamReader(stream, chartset));// 获取输入流
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					if (line.trim().length() > 0) {
						bufferedWriter.write(line);// 把输入流写到目标文件
						bufferedWriter.newLine();// 行写入方式
					}
				}
				return true;
			} catch (Exception e) {
				return false;
			} finally {
				try {
					bufferedReader.close();// 关闭读状态
					bufferedWriter.close();// 关闭写状态
					huc.disconnect();// 关闭连接
				} catch (Exception e) {
				}
			}
		}
	}
}
