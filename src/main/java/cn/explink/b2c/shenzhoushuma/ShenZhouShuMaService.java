package cn.explink.b2c.shenzhoushuma;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomerDAO;
import cn.explink.service.CustomerService;

/**
 * 神州数码
 * @author yurong.liang
 */
@Service
public class ShenZhouShuMaService {
	@Autowired
	private JiontDAO jiontDAO;
	@Autowired
	private CustomerDAO customerDAO;
	@Autowired
	private CustomerService customerService;
	
	 /**
     * 编辑接口配置信息
     */
	public void edit(HttpServletRequest request, int joint_num) {
		ShenZhouShuMa config = new ShenZhouShuMa();
		String logisticProvider=request.getParameter("logisticProvider").trim();		
		String logisticProviderId=request.getParameter("logisticProviderId").trim();		
		String customerId=request.getParameter("customerId").trim();		
		String maxCount =request.getParameter("maxCount").trim();
		String privateKey=request.getParameter("privateKey").trim();		
		String feedBackUrl=request.getParameter("feedBackUrl").trim();		
		
		config.setLogisticProvider(logisticProvider);
		config.setLogisticProviderId(logisticProviderId);
		config.setMaxCount(maxCount==""?50:Integer.valueOf(maxCount));
		config.setCustomerId(customerId==""?0:Integer.valueOf(customerId));
		config.setPrivateKey(privateKey);
		config.setFeedBackUrl(feedBackUrl);
		String oldCustomerids = "";
		
		JSONObject jsonObj = JSONObject.fromObject(config);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {//新增
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {//修改
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		
		//保存 枚举到供货商表中
		this.customerDAO.updateB2cEnumByJoint_num(customerId, oldCustomerids, joint_num);
		this.customerService.initCustomerList();
	}
	
	/**
	 * 更新接口状态
	 */
	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}
	
	//获取接口配置信息
	public ShenZhouShuMa getShenZhouShuMa(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		if (obj == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(obj.getJoint_property());
		ShenZhouShuMa shenZhouShuMa = (ShenZhouShuMa) JSONObject.toBean(jsonObj, ShenZhouShuMa.class);
		return shenZhouShuMa;
	}
}
