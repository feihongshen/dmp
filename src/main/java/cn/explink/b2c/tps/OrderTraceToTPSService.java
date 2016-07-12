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
public class OrderTraceToTPSService {
	@Autowired
	JiontDAO jiontDAO;
	
	/**
	 * 通过key获取 外单 推送给DO服务 配置信息
	 */
	public OrderTraceToTPSCfg getOrderTraceToTPSCfg(int key) {
		if (this.getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
		OrderTraceToTPSCfg orderTraceToTPSCfg = (OrderTraceToTPSCfg) JSONObject.toBean(jsonObj, OrderTraceToTPSCfg.class);
		return orderTraceToTPSCfg;
	}
	
	/**
	 * 保存或修改配置
	 */
	public void edit(HttpServletRequest request, int joint_num) {
		OrderTraceToTPSCfg orderTraceToTPSCfg =new OrderTraceToTPSCfg();
		String customerIds = StringUtils.isEmpty(request.getParameter("customerids")) ? "" : request.getParameter("customerids");
		Integer trackMaxTryTime= StringUtils.isEmpty(request.getParameter("trackMaxTryTime"))? 0 :Integer.parseInt(request.getParameter("trackMaxTryTime"));
		
		orderTraceToTPSCfg.setCustomerids(customerIds);
		orderTraceToTPSCfg.setTrackMaxTryTime(trackMaxTryTime);
		JSONObject jsonObj = JSONObject.fromObject(orderTraceToTPSCfg);
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
	
	/**
	 * 是否外单客户
	 */
	public boolean isThirdPartyCustomer(long customerid,ThirdPartyOrder2DOCfg pushCfg){
		if(pushCfg == null){
			return false;
		}
		String customeridsCfg = pushCfg.getCustomerids();
		String[] customerIdsCfgArray = customeridsCfg.split(",|，");
		//是否是外单客户
		boolean isTPCust  = false;
		for(String customeridStr : customerIdsCfgArray){
			try{
				if( Long.valueOf(customeridStr).longValue() == customerid){
					isTPCust = true;
					break;
				}
			}catch(Exception e){
				//do nothing
			}
		}
		return isTPCust;
	}
}
