package cn.explink.util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil {

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static String translateToJson(Object obj) throws IOException, JsonGenerationException, JsonMappingException {
		return objectMapper.writeValueAsString(obj);
	}

	public static <T> T readValue(String json, Class<T> type) throws JsonParseException, JsonMappingException, IOException {
		return objectMapper.readValue(json, type);
	}
}
