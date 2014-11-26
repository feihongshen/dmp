package cn.explink.b2c.letv;

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
public class LetvService {
	private Logger logger = LoggerFactory.getLogger(LetvService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Letv getYeMaiJiu(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Letv dangdang = (Letv) JSONObject.toBean(jsonObj, Letv.class);
		return dangdang;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		Letv letv = new Letv();
		String customerid = request.getParameter("customerid");
		letv.setCustomerid(customerid);
		letv.setExpressid(request.getParameter("expressid"));
		letv.setMaxCount(Integer.valueOf(request.getParameter("maxCount")));
		letv.setPrivate_key(request.getParameter("private_key"));
		letv.setSend_url(request.getParameter("search_url"));
		letv.setService_code(request.getParameter("service_code"));
		JSONObject jsonObj = JSONObject.fromObject(letv);

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
