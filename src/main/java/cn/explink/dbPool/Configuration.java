package cn.explink.dbPool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读取配置文件类
 * 
 * @author lu
 *
 */
public class Configuration {
	
	private static Logger logger = LoggerFactory.getLogger(Configuration.class);
	
	private Properties prop;

	public Configuration(String path) {
		InputStream is = null;
		prop = new Properties();
		try {
			is = getClass().getClassLoader().getResourceAsStream(path);
			prop.load(is);
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				logger.error("", e);
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
