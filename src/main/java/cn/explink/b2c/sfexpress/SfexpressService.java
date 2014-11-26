package cn.explink.b2c.sfexpress;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomerDAO;
import cn.explink.pos.tools.JacksonMapper;

@Service
public class SfexpressService {
	private static Logger logger = LoggerFactory.getLogger(SfexpressService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Sfexpress getMmb(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Sfexpress dangdang = (Sfexpress) JSONObject.toBean(jsonObj, Sfexpress.class);
		return dangdang;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		Sfexpress sf = new Sfexpress();

		sf.setMaxCount(Integer.valueOf(request.getParameter("maxCount")));
		sf.setSend_url(request.getParameter("send_url"));
		sf.setCommoncode(request.getParameter("commoncode"));
		sf.setLoopcount(Long.valueOf(request.getParameter("loopcount")));
		sf.setExpressCode(request.getParameter("expressCode"));
		sf.setServiceContact(request.getParameter("serviceContact"));
		sf.setServicePhone(request.getParameter("servicePhone"));
		sf.setCompanyName(request.getParameter("companyName"));
		sf.setJ_address(request.getParameter("j_address"));
		sf.setCustid(request.getParameter("custid"));
		sf.setCheckword(request.getParameter("checkword"));

		JSONObject jsonObj = JSONObject.fromObject(sf);

		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}

	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

}
