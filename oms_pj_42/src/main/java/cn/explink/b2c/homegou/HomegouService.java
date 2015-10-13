package cn.explink.b2c.homegou;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.dongfangcj.DongFangCJXMLNote;
import cn.explink.b2c.tools.B2cTools;

@Service
public class HomegouService {
	private Logger logger = LoggerFactory.getLogger(HomegouService.class);

	@Autowired
	private B2cTools b2ctools;

	// 获取配置信息
	public Homegou getHomegouSettingMethod(int key) {
		Homegou cj = null;
		String objectMethod = b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			cj = (Homegou) JSONObject.toBean(jsonObj, Homegou.class);
		} else {
			cj = new Homegou();
		}
		return cj;
	}

	public HomegouXMLNote getXMLNoteObjectsMethod(String jsoncontent) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(jsoncontent, HomegouXMLNote.class);
	}

}
