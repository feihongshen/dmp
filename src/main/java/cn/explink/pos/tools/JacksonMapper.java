package cn.explink.pos.tools;

import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @autho Jackson handler
 * 
 */
public class JacksonMapper {

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
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		return mapper;
	}

}