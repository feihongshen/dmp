package cn.explink.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.sf.json.JSONObject;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;

@Service
public class B2cUtil {
	@Autowired
	JiontDAO jiontDAO;
	
	/**
	 * 获取页面配置对象
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getViewBean(int key,Class<T> cla) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key)); 
		return (T)JSONObject.toBean(jsonObj, cla);
	}
	public Object getObjectMethod(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}
}
