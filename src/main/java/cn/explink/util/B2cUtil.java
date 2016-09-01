package cn.explink.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.sf.json.JSONObject;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.domain.Customer;

@Service
public class B2cUtil {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
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
	
	/**
	 * 判断供货商是否为vip的客户
	 * 刘武强
	 * 20160901
	 * @param customer
	 * @param constainsStr（判断是否为唯品时，该参数为"vipshop"）
	 * @return
	 */
	public String getB2cEnumKeys(Customer customer, String constainsStr) {
		for (B2cEnum enums : B2cEnum.values()) {
			if (enums.getMethod().contains(constainsStr)) {
				if (customer.getB2cEnum().equals(String.valueOf(enums.getKey()))) {
					int isOpenFlag = this.jointService.getStateForJoint(enums.getKey());
					if (isOpenFlag == 1) {
						return String.valueOf(enums.getKey());
					}
					return null;

				}
			}
		}
		return null;
	}
}
