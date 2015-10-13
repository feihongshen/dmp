package cn.explink.vo.delivery;

import java.io.IOException;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.KeyDeserializer;

import cn.explink.util.JsonUtil;

public class DeliveryRateTimeTypeDeSerializer extends KeyDeserializer {

	@Override
	public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return JsonUtil.readValue(key, DeliveryRateTimeType.class);
	}

}
