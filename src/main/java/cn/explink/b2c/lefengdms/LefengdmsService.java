package cn.explink.b2c.lefengdms;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.CustomerService;

@Service
public class LefengdmsService {
	private Logger logger = LoggerFactory.getLogger(LefengdmsService.class);

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
	@Autowired
	CustomerService customerService;

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	public String getObjectMethod(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Lefengdms getlefeng(int key) {
		if (this.getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
		Lefengdms smile = (Lefengdms) JSONObject.toBean(jsonObj, Lefengdms.class);
		return smile;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Lefengdms lefeng = new Lefengdms();
		String customerids = request.getParameter("customerids");
		lefeng.setCustomerids(customerids);
		lefeng.setSearch_number(Long.parseLong(request.getParameter("search_number")));
		lefeng.setSearch_url(request.getParameter("search_url"));
		lefeng.setPassword(request.getParameter("password"));
		lefeng.setAgentId(request.getParameter("agentId"));
		lefeng.setAgentName(request.getParameter("agentName"));
		lefeng.setAgentPhone(request.getParameter("agentPhone"));
		lefeng.setAgentWebsite(request.getParameter("agentWebsite"));
		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(lefeng);
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = this.getlefeng(joint_num).getCustomerids();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		this.customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);
		this.customerService.initCustomerList();
	}

	public void update(int joint_num, int state) {
		this.jiontDAO.UpdateState(joint_num, state);
	}

	public Lefengdms getLefengdms(int key) {
		if (this.getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
		Lefengdms smile = (Lefengdms) JSONObject.toBean(jsonObj, Lefengdms.class);
		return smile;
	}

}
