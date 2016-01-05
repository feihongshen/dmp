package cn.explink.b2c.tps;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.core.utils.StringUtils;
import cn.explink.util.StringUtil;

/**
 * TPS 运单状态太 service
 * @author yurong.liang 2015/12/19
 */
@Service
public class TPSCarrierOrderStatusService {
	@Autowired
	JiontDAO jiontDAO;
	
	/**
	 * 通过key获取 TPS运单状态接口配置信息
	 */
	public TPSCarrierOrderStatus getTPSCarrierOrderStatus(int key) {
		if (this.getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
		TPSCarrierOrderStatus tPSCarrierOrderStatus = (TPSCarrierOrderStatus) JSONObject.toBean(jsonObj, TPSCarrierOrderStatus.class);
		return tPSCarrierOrderStatus;
	}
	
	/**
	 * 保存或修改配置
	 */
	public void edit(HttpServletRequest request, int joint_num) {
		TPSCarrierOrderStatus carrierOrderStatus =new TPSCarrierOrderStatus();
		carrierOrderStatus.setShipper_no(request.getParameter("shipper_no"));
		Integer maxCount= StringUtils.isEmpty(request.getParameter("getMaxCount"))? 0 :Integer.parseInt(request.getParameter("getMaxCount"));
		Integer seq= StringUtils.isEmpty(request.getParameter("seq"))? 0 :Integer.parseInt(request.getParameter("seq"));
		carrierOrderStatus.setGetMaxCount(maxCount);
		carrierOrderStatus.setSeq(seq);
		JSONObject jsonObj = JSONObject.fromObject(carrierOrderStatus);
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {//新增
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Create(jointEntity);
		} else {//修改
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Update(jointEntity);
		}
	
	}
	/**
	 * 修改连接状态
	 */
	public void updateState(int joint_num, int state) {
		this.jiontDAO.UpdateState(joint_num, state);
	}
	
	//通过key从数据库中获取连接实体信息
	public String getObjectMethod(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}
}
