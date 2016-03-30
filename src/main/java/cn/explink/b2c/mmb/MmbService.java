package cn.explink.b2c.mmb;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomerDAO;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.CustomerService;

@Service
public class MmbService {
	private Logger logger = LoggerFactory.getLogger(MmbService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CustomerService customerService;

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Mmb getMmb(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Mmb dangdang = (Mmb) JSONObject.toBean(jsonObj, Mmb.class);
		return dangdang;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Mmb mmb = new Mmb();
		String customerid = request.getParameter("customerid");
		mmb.setCustomerid(customerid);
		mmb.setKey(request.getParameter("key"));
		mmb.setMaxCount(Integer.valueOf(request.getParameter("maxCount")));
		mmb.setSend_url(request.getParameter("send_url"));
		mmb.setCompanyname(request.getParameter("companyname"));
		JSONObject jsonObj = JSONObject.fromObject(mmb);

		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		String oldCustomerids = "";
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getMmb(joint_num).getCustomerid();

			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerids, joint_num);
		this.customerService.initCustomerList();
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

}
