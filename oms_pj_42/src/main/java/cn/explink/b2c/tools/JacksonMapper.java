package cn.explink.b2c.tools;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * @autho Jackson handler 饿汉式单例模式
 * 
 */
public class JacksonMapper {

	/** 
     *  
     */
	private static final ObjectMapper mapper = new ObjectMapper();

	/** 
     *  
     */
	private JacksonMapper() {

	}

	/**
	 * 
	 * @return
	 */
	public static ObjectMapper getInstance() {
		return mapper;
	}

}