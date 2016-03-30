package cn.explink.b2c.smiled;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomerDAO;

@Service
public class SmiledService {
	private Logger logger = LoggerFactory.getLogger(SmiledService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Smiled getSmile(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Smiled smile = (Smiled) JSONObject.toBean(jsonObj, Smiled.class);
		return smile;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		Smiled smile = new Smiled();

		smile.setFeedback_url(request.getParameter("feedback_url"));
		String maxCount = "".equals(request.getParameter("maxCount")) ? "0" : request.getParameter("maxCount");
		String loopcount = "".equals(request.getParameter("loopcount")) ? "0" : request.getParameter("loopcount");
		String resendcount = "".equals(request.getParameter("resendcount")) ? "0" : request.getParameter("resendcount");

		smile.setResendcount(Long.valueOf(resendcount));
		smile.setMaxCount(Long.valueOf(maxCount));
		smile.setExpress_id(request.getParameter("express_id"));
		smile.setLoopcount(Long.valueOf(loopcount));
		smile.setPrivate_key(request.getParameter("private_key"));
		smile.setSend_url(request.getParameter("send_url"));

		JSONObject jsonObj = JSONObject.fromObject(smile);
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

	public int getStateForYihaodian(int key) {
		JointEntity obj = null;
		int state = 0;
		try {
			obj = jiontDAO.getJointEntity(key);
			state = obj.getState();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return state;
	}

}
