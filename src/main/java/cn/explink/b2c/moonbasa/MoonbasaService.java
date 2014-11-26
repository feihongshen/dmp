package cn.explink.b2c.moonbasa;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.jingdong.JingDong;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.pos.tools.JacksonMapper;

@Service
public class MoonbasaService {
	private Logger logger = LoggerFactory.getLogger(MoonbasaService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Moonbasa getMoonBasa(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Moonbasa dangdang = (Moonbasa) JSONObject.toBean(jsonObj, Moonbasa.class);
		return dangdang;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		Moonbasa moonbasa = new Moonbasa();
		String customerid = request.getParameter("customerid");
		moonbasa.setCustomerid(customerid);
		moonbasa.setCustcode(request.getParameter("custcode"));
		moonbasa.setPwd(request.getParameter("pwd"));
		moonbasa.setSearch_url(request.getParameter("search_url"));

		JSONObject jsonObj = JSONObject.fromObject(moonbasa);

		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		String oldCustomerids = "";
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getMoonBasa(joint_num).getCustomerid();

			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerids, joint_num);

	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	/**
	 * 梦芭莎请求接口开始
	 * 
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */

	public String requestCwbSearchInterface(Moonbasa mbs, String custcode, String pwd, String from, String to) throws Exception {

		try {

			if (!mbs.getCustcode().equals(custcode) || !mbs.getPwd().equals(pwd)) {
				return "-1";
			}

			// 构建Xml
			String responseXML = "";
			StringBuffer sub = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			sub.append("<ArrayOfDeliveryInfo xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">");

			sub.append("</ArrayOfDeliveryInfo>");
			return responseXML;

		} catch (Exception e) {
			String error = "处理[梦芭莎]查询请求发生未知异常:" + e.getMessage();
			logger.error(error, e);
			return error;
		}

	}

}
