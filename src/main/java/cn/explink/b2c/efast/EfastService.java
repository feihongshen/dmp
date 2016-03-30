package cn.explink.b2c.efast;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.service.CustomerService;

/**
 * 中兴云购，ERP系统接口service
 * 
 * @author Administrator
 *
 */
@Service
public class EfastService {

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	EfastDAO efastDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	JointService jointService;
	@Autowired
	CustomerService customerService;
	private Logger logger = LoggerFactory.getLogger(EfastService.class);

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Efast getEfast(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Efast efast = (Efast) JSONObject.toBean(jsonObj, Efast.class);
		return efast;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Efast efast = new Efast();
		String customerid = request.getParameter("customerid");

		efast.setCustomerid(customerid);
		efast.setWarehouseid(Long.parseLong(request.getParameter("warehouseid")));
		efast.setApp_key(request.getParameter("app_key"));
		efast.setApp_nick(request.getParameter("app_nick"));
		efast.setApp_secret(request.getParameter("app_secret"));
		efast.setApp_url(request.getParameter("app_url"));
		efast.setBeforhours(Integer.valueOf(request.getParameter("beforhours")));
		efast.setLoopcount(Integer.valueOf(request.getParameter("loopcount")));
		efast.setShipping_code(request.getParameter("shipping_code"));
		efast.setErpEnum(request.getParameter("erpEnum"));

		String oldCustomerids = "";
		JSONObject jsonObj = JSONObject.fromObject(efast);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getEfast(joint_num).getCustomerid();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerids, joint_num);
		this.customerService.initCustomerList();
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public int getStateForYiXun(int key) {
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

	public static void main(String[] args) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("app_key", "8888"); // 请求唯一标识
		params.put("app_secret", "8888"); // 请求密钥
		params.put("app_nick", "openapi"); // 请求接口标识

		params.put("app_act", "efast.payment.list.get"); // 请求参数名
		params.put("page_no", "1"); // 系统编号
		params.put("page_size", "20"); // 查询的字段
		String response = RestHttpServiceHanlder.sendHttptoServer(params, "http://61.161.205.105/efast/efast_api/webservice/web/index.php");
		// response=URLDecoder.decode(response,"GBK");
		System.out.println(response);

	}

}
