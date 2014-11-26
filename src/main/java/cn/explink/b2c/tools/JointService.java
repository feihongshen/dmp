package cn.explink.b2c.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JointService {
	private Logger logger = LoggerFactory.getLogger(JointService.class);
	@Autowired
	JiontDAO jiontDAO;

	/**
	 * 是否开启对接
	 * 
	 * @param key
	 * @return
	 */
	public int getStateForJoint(int key) {
		JointEntity obj = null;
		int state = 0;
		try {
			obj = jiontDAO.getJointEntity(key);
			state = obj.getState();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return state;
	}

	/**
	 * 获取b2c 配置信息的接口
	 * 
	 * @param key
	 * @return
	 */
	public JointEntity getObjectMethod(int key) {
		try {
			return jiontDAO.getJointEntity(key);
		} catch (Exception e) {
			logger.warn("error while getting b2c entity with key {}, will defualt false", key);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取b2c 配置信息的接口
	 * 
	 * @param key
	 * @return
	 */
	public JointEntity getObjectMethodByKeys(String keys, String customerid) {
		try {
			return jiontDAO.getJointEntityByKeys(keys, customerid);
		} catch (Exception e) {
			logger.warn("error while getting b2c entity with key {},customerid={}, will defualt false", keys, customerid);
			e.printStackTrace();
			return null;
		}
	}

}
