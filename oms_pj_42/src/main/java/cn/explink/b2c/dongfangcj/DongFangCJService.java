package cn.explink.b2c.dongfangcj;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cTools;

@Service
public class DongFangCJService {
	private Logger logger = LoggerFactory.getLogger(DongFangCJService.class);

	@Autowired
	private B2cTools b2ctools;

	// 获取配置信息
	public DongFangCJ getDongFangCJSettingMethod(int key) {
		DongFangCJ cj = null;
		String objectMethod = b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			cj = (DongFangCJ) JSONObject.toBean(jsonObj, DongFangCJ.class);
		} else {
			cj = new DongFangCJ();
		}
		return cj;
	}

	public DongFangCJXMLNote getXMLNoteObjectsMethod(String jsoncontent) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(jsoncontent, DongFangCJXMLNote.class);
	}

}
