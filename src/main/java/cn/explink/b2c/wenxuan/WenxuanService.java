package cn.explink.b2c.wenxuan;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
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
public class WenxuanService {

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

	public Wenxuan getWenxuan(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Wenxuan efast = (Wenxuan) JSONObject.toBean(jsonObj, Wenxuan.class);
		return efast;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Wenxuan hg = new Wenxuan();
		String customerid = request.getParameter("customerid");

		hg.setCustomerid(customerid);
		hg.setWarehouseid(Long.parseLong(request.getParameter("warehouseid")));
		hg.setApikey(request.getParameter("apikey"));
		hg.setApiSecret(request.getParameter("apiSecret"));
		hg.setDownloadUrl(request.getParameter("downloadUrl"));
		hg.setWabillUrl(request.getParameter("wabillUrl"));
		hg.setBeforhours(Integer.valueOf(request.getParameter("beforhours")));
		hg.setLoopcount(Integer.valueOf(request.getParameter("loopcount")));
		hg.setMaxCount(Integer.valueOf(request.getParameter("maxCount")));
		hg.setPagesize(Integer.valueOf(request.getParameter("pagesize")));

		String oldCustomerids = "";
		JSONObject jsonObj = JSONObject.fromObject(hg);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getWenxuan(joint_num).getCustomerid();
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
