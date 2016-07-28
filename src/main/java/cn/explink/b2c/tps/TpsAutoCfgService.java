package cn.explink.b2c.tps;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.core.utils.StringUtils;


@Service
public class TpsAutoCfgService {
	@Autowired
	JiontDAO jiontDAO;
	
	public TpsAutoCfg getTpsAutoCfg(int key) {
		if (this.getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
		TpsAutoCfg tpsAutoCfg = (TpsAutoCfg) JSONObject.toBean(jsonObj, TpsAutoCfg.class);
		return tpsAutoCfg;
	}
	
	
	/**
	 * 保存或修改配置
	 */
	public void edit(HttpServletRequest request, int joint_num) {
		TpsAutoCfg tpsAutoCfg =new TpsAutoCfg();
		Integer autoOpenFlag = StringUtils.isEmpty(request.getParameter("autoOpenFlag"))? 0 :Integer.parseInt(request.getParameter("autoOpenFlag"));
		Integer batchnoOpenFlag = StringUtils.isEmpty(request.getParameter("batchnoOpenFlag"))? 0 :Integer.parseInt(request.getParameter("batchnoOpenFlag"));
		Long warehouseId = StringUtils.isEmpty(request.getParameter("warehouseId"))? 0 :Long.parseLong(request.getParameter("warehouseId"));
		
			
		tpsAutoCfg.setAutoOpenFlag(autoOpenFlag);
		tpsAutoCfg.setBatchnoOpenFlag(batchnoOpenFlag);
		tpsAutoCfg.setWarehouseid(warehouseId);
		
		JSONObject jsonObj = JSONObject.fromObject(tpsAutoCfg);
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
