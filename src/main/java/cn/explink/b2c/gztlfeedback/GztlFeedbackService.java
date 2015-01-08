package cn.explink.b2c.gztlfeedback;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.pos.tools.JacksonMapper;

@Service
public class GztlFeedbackService {
	private Logger logger = LoggerFactory.getLogger(GztlFeedbackService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ExplinkService explinkService;
	@Autowired
	UserDAO userDAO;

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	public String getObjectMethod(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public void edit(HttpServletRequest request, int joint_num) {
		GztlFeedback gztl = new GztlFeedback();
		String customerids = request.getParameter("customerids");
		gztl.setCustomerids(customerids);
		gztl.setSearch_number(Long.parseLong(request.getParameter("search_number")));
		gztl.setSearch_url(request.getParameter("search_url"));
		gztl.setPassword(request.getParameter("password"));
		gztl.setWarehouseid(Long.parseLong(request.getParameter("warehouseid")));
		gztl.setPrivate_key(request.getParameter("private_key"));
		gztl.setSign(request.getParameter("sign"));
		gztl.setCode(request.getParameter("code"));
		gztl.setInvokeMethod(request.getParameter("invokeMethod"));
		gztl.setReceive_url(request.getParameter("receive_url"));
		gztl.setLoopCount(Integer.parseInt(request.getParameter("loopCount")));
		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(gztl);
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = this.getGztlFeedback(joint_num).getCustomerids();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		this.customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);
	}

	public void update(int joint_num, int state) {
		this.jiontDAO.UpdateState(joint_num, state);
	}

	public GztlFeedback getGztlFeedback(int key) {
		if (this.getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
		GztlFeedback smile = (GztlFeedback) JSONObject.toBean(jsonObj, GztlFeedback.class);
		return smile;
	}

}
