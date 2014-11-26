package cn.explink.b2c.wanxiang;

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

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomerDAO;
import cn.explink.pos.tools.JacksonMapper;

@Service
public class WanxiangService {
	private Logger logger = LoggerFactory.getLogger(WanxiangService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Wanxiang getLianTong(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Wanxiang dangdang = (Wanxiang) JSONObject.toBean(jsonObj, Wanxiang.class);
		return dangdang;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		Wanxiang lt = new Wanxiang();
		String customerid = request.getParameter("customerid");
		lt.setCustomerid(customerid);
		lt.setPrivate_key(request.getParameter("private_key"));
		lt.setUrl(request.getParameter("url"));
		lt.setMaxCount(Integer.valueOf(request.getParameter("maxCount")));
		lt.setBranchname(request.getParameter("branchname"));
		lt.setUser_name(request.getParameter("user_name"));
		lt.setPass_word(request.getParameter("pass_word"));
		lt.setVersion(Integer.valueOf(request.getParameter("version")));

		JSONObject jsonObj = JSONObject.fromObject(lt);

		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		String oldCustomerids = "";
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getLianTong(joint_num).getCustomerid();

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
