package cn.explink.b2c.Wholeline;

import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomerDAO;
import cn.explink.util.StringUtil;

@Service
public class WholeLineService {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;

	public void edit(HttpServletRequest request, int joint_num) {
		WholeLine whole = new WholeLine();
		String comecode = StringUtil.nullConvertToEmptyString(request.getParameter("comecode"));
		String Url = StringUtil.nullConvertToEmptyString(request.getParameter("postUrl"));
		String method = StringUtil.nullConvertToEmptyString(request.getParameter("method"));
		String usercode = StringUtil.nullConvertToEmptyString(request.getParameter("usercode"));
		String expt_code = StringUtil.nullConvertToEmptyString(request.getParameter("expt_code"));
		whole.setComeCode(comecode);
		whole.setMethod(method);
		whole.setUrl(Url);
		whole.setUsercode(usercode);
		whole.setExpt_code(expt_code);
		JSONObject jsonObj = JSONObject.fromObject(whole);
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
		// 保存 枚举到供货商表中
		// customerDAO.updateB2cEnumByJoint_num(whole.getCustomerid(),
		// oldCustomerids, joint_num);
	}

	public WholeLine getWholeline(int key) {
		WholeLine wholeline = new WholeLine();
		if (!"".equals(getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			wholeline = (WholeLine) JSONObject.toBean(jsonObj, WholeLine.class);
		} else {
			wholeline = new WholeLine();
		}
		return wholeline == null ? new WholeLine() : wholeline;
	}

	private String getObjectMethod(int key) {
		JointEntity obj = null;
		String posValue = "";
		try {
			obj = jiontDAO.getJointEntity(key);
			posValue = obj.getJoint_property();
		} catch (Exception e) {
		}
		return posValue;
	}

}
