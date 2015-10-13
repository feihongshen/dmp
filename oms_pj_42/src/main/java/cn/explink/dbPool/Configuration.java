package cn.explink.dbPool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取配置文件类
 * 
 * @author lu
 *
 */
public class Configuration {
	private Properties prop;

	public Configuration(String path) {
		InputStream is = null;
		prop = new Properties();
		try {
			is = getClass().getClassLoader().getResourceAsStream(path);
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getValue(String key) {
		return prop.getProperty(key);
	}

	public static Configuration getInstance(String path) {
		Configuration conf = new Configuration(path);
		return conf;
	}
}
