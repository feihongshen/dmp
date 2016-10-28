package cn.explink.b2c.tps;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.core.utils.StringUtils;


@Service
public class PjdSignimgCfgService {
	@Autowired
	JiontDAO jiontDAO;
	
	public PjdSignimgCfg getPjdSignimgCfg(int key) {
		if (this.getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
		PjdSignimgCfg pjdSignimgCfg = (PjdSignimgCfg) JSONObject.toBean(jsonObj, PjdSignimgCfg.class);
		return pjdSignimgCfg;
	}
	
	
	/**
	 * 保存或修改配置
	 */
	public void edit(HttpServletRequest request, int joint_num) {
		PjdSignimgCfg pjdSignimgCfg =new PjdSignimgCfg();
		Integer openFlag = StringUtils.isEmpty(request.getParameter("openFlag"))? 0 :Integer.parseInt(request.getParameter("openFlag"));
				
		pjdSignimgCfg.setOpenFlag(openFlag);
		
		JSONObject jsonObj = JSONObject.fromObject(pjdSignimgCfg);
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
