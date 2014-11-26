package cn.explink.b2c.yemaijiu;

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
public class YeMaiJiuService {
	private Logger logger = LoggerFactory.getLogger(YeMaiJiuService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public YeMaiJiu getYeMaiJiu(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		YeMaiJiu dangdang = (YeMaiJiu) JSONObject.toBean(jsonObj, YeMaiJiu.class);
		return dangdang;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		YeMaiJiu yemaijiu = new YeMaiJiu();
		String customerid = request.getParameter("customerid");
		yemaijiu.setCustomerid(customerid);
		yemaijiu.setExpressCode(request.getParameter("expressCode"));
		yemaijiu.setMaxCount(Integer.valueOf(request.getParameter("maxCount")));
		yemaijiu.setPrivate_key(request.getParameter("private_key"));
		yemaijiu.setSend_url(request.getParameter("search_url"));

		JSONObject jsonObj = JSONObject.fromObject(yemaijiu);

		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		String oldCustomerids = "";
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getYeMaiJiu(joint_num).getCustomerid();

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

}
