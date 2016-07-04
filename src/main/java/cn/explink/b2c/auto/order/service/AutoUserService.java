package cn.explink.b2c.auto.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.vipshop.VipShop;
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
		JointEntity jointEntity=jointService.getObjectMethod(B2cEnum.TPS_MQ.getKey());
		if(jointEntity==null){
			throw new RuntimeException("找不到自动化分拣库相关配置");
		}
		String objectMethod = jointEntity.getJoint_property();
		JSONObject jsonObj = JSONObject.fromObject(objectMethod);
		VipShop vipshop = (VipShop)JSONObject.toBean(jsonObj, VipShop.class);
		
		if(vipshop.getWarehouseid()==0){
			throw new RuntimeException("请先配置自动化分拣库id");
		}
		
		return vipshop.getWarehouseid();//?
	}
	
	   //获取自动化开关,0关 1 开
    public int getAutoFlag() {
		JointEntity obj = this.jointService.getObjectMethod(B2cEnum.TPS_MQ.getKey());
		if(obj == null){
			return 0;
		}
		JSONObject jsonObj = JSONObject.fromObject(obj.getJoint_property());
		VipShop vipshop = (VipShop) JSONObject.toBean(jsonObj, VipShop.class);
		return vipshop.getIsAutoInterface();
	}
}
