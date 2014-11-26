package cn.explink.b2c.dpfoss;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.explink.pos.tools.JacksonMapper;

public class JsonUtil {
	private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

	private static ObjectMapper mapper;
	static {
		// 设置时间转换格式，将date对象格式化输出(eg：yyyy-MM-dd HH:mm:ss)
		mapper = JacksonMapper.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mapper.setDateFormat(format);
	}

	public static String fromObject(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			logger.error("Json格式转化发生未知异常", e);
			e.printStackTrace();
		}
		return null;
	}
}
