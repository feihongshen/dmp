package cn.explink.core.utils;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * jason 工具类
 * @author gaoll
 *
 */
public class JsonUtil {

	private static ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.getSerializationConfig().withSerializationInclusion(Inclusion.NON_EMPTY);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
	}

	public static String translateToJson(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException("failed translate object to json", e);
		}
	}

	public static <T> T readValue(String json, Class<T> type) {
		try {
			return objectMapper.readValue(json, type);
		} catch (Exception e) {
			throw new RuntimeException("failed read object from json", e);
		}
	}

}