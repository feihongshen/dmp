package cn.explink.b2c.tps;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.core.utils.StringUtils;

/**
 * 外单 推送给DO服务 service
 * @author gordon.zhou
 *
 */
@Service
public class ThirdPartyOrder2DOCfgService {
	@Autowired
	JiontDAO jiontDAO;
	
	/**
	 * 通过key获取 外单 推送给DO服务 配置信息
	 */
	public ThirdPartyOrder2DOCfg getThirdPartyOrder2DOCfg(int key) {
		if (this.getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
		ThirdPartyOrder2DOCfg thirdPartyOrder2DO = (ThirdPartyOrder2DOCfg) JSONObject.toBean(jsonObj, ThirdPartyOrder2DOCfg.class);
		return thirdPartyOrder2DO;
	}
	
	/**
	 * 保存或修改配置
	 */
	public void edit(HttpServletRequest request, int joint_num) {
		ThirdPartyOrder2DOCfg thirdPartyOrder2DO =new ThirdPartyOrder2DOCfg();
		Integer openFlag = StringUtils.isEmpty(request.getParameter("openFlag"))? 0 :Integer.parseInt(request.getParameter("openFlag"));
		Integer maxTryTime= StringUtils.isEmpty(request.getParameter("maxTryTime"))? 0 :Integer.parseInt(request.getParameter("maxTryTime"));
		String customerIds = StringUtils.isEmpty(request.getParameter("customerids")) ? "" : request.getParameter("customerids");
		
		thirdPartyOrder2DO.setOpenFlag(openFlag);
		thirdPartyOrder2DO.setMaxTryTime(maxTryTime);
		thirdPartyOrder2DO.setCustomerids(customerIds);
		JSONObject jsonObj = JSONObject.fromObject(thirdPartyOrder2DO);
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
