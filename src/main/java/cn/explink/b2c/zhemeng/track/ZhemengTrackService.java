package cn.explink.b2c.zhemeng.track;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomerDAO;
import cn.explink.service.CustomerService;

/**
 * 哲盟_轨迹 接口对接
 * @author yurong.liang 2016-0527
 */
@Service
public class ZhemengTrackService {
	private Logger logger = LoggerFactory.getLogger(ZhemengTrackService.class);
	
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CustomerService customerService;
	
	
	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public ZhemengTrack getZhenMeng(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		ZhemengTrack jx = (ZhemengTrack) JSONObject.toBean(jsonObj, ZhemengTrack.class);
		return jx;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		ZhemengTrack zm = new ZhemengTrack();
		zm.setPrivate_key(request.getParameter("private_key"));
		zm.setTms_service_code(request.getParameter("tms_service_code"));
		zm.setCustomerid(request.getParameter("customerid"));
		zm.setSend_url(request.getParameter("send_url"));

		String customerid = request.getParameter("customerid");
		String oldCustomerid = "";

		JSONObject jsonObj = JSONObject.fromObject(zm);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerid = getZhenMeng(joint_num).getCustomerid();
			} catch (Exception e) {
				logger.error("编辑【哲盟_轨迹】接口配置信息出现未知异常"+e);
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerid, joint_num);
		this.customerService.initCustomerList();
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public int getState(int key) {
		JointEntity obj = null;
		int state = 0;
		try {
			obj = jiontDAO.getJointEntity(key);
			state = obj.getState();
		} catch (Exception e) {
			logger.error("获取【哲盟_轨迹】接口配置信息出现未知异常: "+e);
		}
		return state;
	}
}
