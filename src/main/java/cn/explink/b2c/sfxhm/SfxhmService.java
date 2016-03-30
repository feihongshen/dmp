package cn.explink.b2c.sfxhm;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
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
public class SfxhmService {

	@Autowired
	JiontDAO jiontDAO;

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

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Sfxhm getSfxhm(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Sfxhm efast = (Sfxhm) JSONObject.toBean(jsonObj, Sfxhm.class);
		return efast;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Sfxhm sm = new Sfxhm();
		String customerid = request.getParameter("customerid");

		sm.setCustomerid(customerid);
		sm.setWarehouseid(Long.parseLong(request.getParameter("warehouseid")));
		sm.setDownloadCount(Integer.parseInt(request.getParameter("downloadCount")));
		sm.setMaxCount(Integer.valueOf(request.getParameter("maxCount")));
		sm.setDriver(request.getParameter("driver"));
		sm.setUname(request.getParameter("uname"));
		sm.setPwd(request.getParameter("pwd"));
		sm.setCompanyname(request.getParameter("companyname"));

		String oldCustomerids = "";
		JSONObject jsonObj = JSONObject.fromObject(sm);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getSfxhm(joint_num).getCustomerid();
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

}
