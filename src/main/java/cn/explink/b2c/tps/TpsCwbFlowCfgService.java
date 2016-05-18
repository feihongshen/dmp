package cn.explink.b2c.tps;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.core.utils.StringUtils;


@Service
public class TpsCwbFlowCfgService {
	@Autowired
	JiontDAO jiontDAO;
	
	public TpsCwbFlowCfg getTpsCwbFlowCfg(int key) {
		if (this.getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
		TpsCwbFlowCfg tpsCwbFlowCfg = (TpsCwbFlowCfg) JSONObject.toBean(jsonObj, TpsCwbFlowCfg.class);
		return tpsCwbFlowCfg;
	}
	
	
	/**
	 * 保存或修改配置
	 */
	public void edit(HttpServletRequest request, int joint_num) {
		TpsCwbFlowCfg tpsCwbFlowCfg =new TpsCwbFlowCfg();
		Integer openFlag = StringUtils.isEmpty(request.getParameter("openFlag"))? 0 :Integer.parseInt(request.getParameter("openFlag"));
		Integer maxTryTime= StringUtils.isEmpty(request.getParameter("maxTryTime"))? 0 :Integer.parseInt(request.getParameter("maxTryTime"));
		String customerIds = StringUtils.isEmpty(request.getParameter("customerids")) ? "" : request.getParameter("customerids");
		Integer maxDataSize = StringUtils.isEmpty(request.getParameter("maxDataSize"))? 0 :Integer.parseInt(request.getParameter("maxDataSize"));
		Integer housekeepDay= StringUtils.isEmpty(request.getParameter("housekeepDay"))? 0 :Integer.parseInt(request.getParameter("housekeepDay"));
		
		tpsCwbFlowCfg.setOpenFlag(openFlag);
		tpsCwbFlowCfg.setMaxTryTime(maxTryTime);
		tpsCwbFlowCfg.setCustomerids(customerIds);
		tpsCwbFlowCfg.setMaxDataSize(maxDataSize);
		tpsCwbFlowCfg.setHousekeepDay(housekeepDay);
		JSONObject jsonObj = JSONObject.fromObject(tpsCwbFlowCfg);
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
