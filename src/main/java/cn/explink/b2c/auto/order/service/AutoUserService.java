package cn.explink.b2c.auto.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tps.TpsAutoCfg;
import cn.explink.domain.User;
import net.sf.json.JSONObject;

@Service
public class AutoUserService {
	@Autowired
	private JointService jointService;
	
	public User getSessionUser() {
		User user=new User();
		user.setUserid(1);//admin
		user.setBranchid(getPickBranch());
		user.setRealname("admin");//
		user.setIsImposedOutWarehouse(1);//
		return user;
	}
	
	public long getPickBranch() {
		JointEntity jointEntity=jointService.getObjectMethod(B2cEnum.TPS_AUTO.getKey());
		if(jointEntity==null){
			throw new RuntimeException("找不到自动化分拣库相关配置");
		}
		String objectMethod = jointEntity.getJoint_property();
		JSONObject jsonObj = JSONObject.fromObject(objectMethod);
		TpsAutoCfg tpsAutoCfg = (TpsAutoCfg)JSONObject.toBean(jsonObj, TpsAutoCfg.class);
		
		if(tpsAutoCfg.getWarehouseid()==0){
			throw new RuntimeException("请先配置自动化分拣库id");
		}
		
		return tpsAutoCfg.getWarehouseid();//?
	}
	
	   //获取自动化开关,0关 1 开
    public int getAutoFlag() {
		JointEntity obj = this.jointService.getObjectMethod(B2cEnum.TPS_AUTO.getKey());
		if(obj == null){
			return 0;
		}
		JSONObject jsonObj = JSONObject.fromObject(obj.getJoint_property());
		TpsAutoCfg tpsAutoCfg = (TpsAutoCfg) JSONObject.toBean(jsonObj, TpsAutoCfg.class);
		return tpsAutoCfg.getAutoOpenFlag();
	}
    
	//获取交接单号开关,0关 1 开
    public int getBatchnoOpenFlag() {
		JointEntity obj = this.jointService.getObjectMethod(B2cEnum.TPS_AUTO.getKey());
		if(obj == null){
			return 0;
		}
		JSONObject jsonObj = JSONObject.fromObject(obj.getJoint_property());
		TpsAutoCfg tpsAutoCfg = (TpsAutoCfg) JSONObject.toBean(jsonObj, TpsAutoCfg.class);
		return tpsAutoCfg.getBatchnoOpenFlag();
	}
}
