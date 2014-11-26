package cn.explink.b2c.jiuxian;

import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomerDAO;

@Service
public class JiuxianService {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;

	public void edit(HttpServletRequest request, int joint_num) {
		JiuxianWang jiuxian = new JiuxianWang();
		String customerid = request.getParameter("customerids");
		jiuxian.setCustomerid(customerid);
		jiuxian.setsShippedCode(request.getParameter("sShippedCode"));
		jiuxian.setUserkey(request.getParameter("userkey"));
		jiuxian.setTrack_url(request.getParameter("Track_url"));
		jiuxian.setMaxcount(request.getParameter("maxcount"));
		JSONObject jsonObj = JSONObject.fromObject(jiuxian);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		String oldCustomerids = "";
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getJiuxianShenghuo(joint_num).getCustomerid();
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

	public JiuxianWang getJiuxianShenghuo(int yhd_key) {
		if (getObjectMethod(yhd_key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(yhd_key));
		JiuxianWang jiuxianwang = (JiuxianWang) JSONObject.toBean(jsonObj, JiuxianWang.class);
		return jiuxianwang;
	}

	public Object getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

}
