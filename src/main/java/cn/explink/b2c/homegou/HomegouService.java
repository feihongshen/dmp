package cn.explink.b2c.homegou;


import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.service.CustomerService;

@Service
public class HomegouService {
	private Logger logger = LoggerFactory.getLogger(HomegouService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	ExplinkService explinkService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerService customerService;

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Homegou getHomeGou(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Homegou smile = (Homegou) JSONObject.toBean(jsonObj, Homegou.class);
		return smile;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Homegou cj = new Homegou();
		String customerids = request.getParameter("customerids");
		cj.setCustomerids(customerids);
		cj.setFtp_host(request.getParameter("ftp_host"));
		cj.setFtp_username(request.getParameter("ftp_username"));
		cj.setFtp_password(request.getParameter("ftp_password"));
		cj.setFtp_port(Integer.valueOf(request.getParameter("ftp_port")));
		cj.setCharencode(request.getParameter("charencode"));
		cj.setPut_remotePath(request.getParameter("put_remotePath")); // 下载
		cj.setGet_remotePath(request.getParameter("get_remotePath")); // 上传
		cj.setDownloadPath(request.getParameter("downloadPath"));
		cj.setDownloadPath_bak(request.getParameter("downloadPath_bak"));
		cj.setUploadPath(request.getParameter("uploadPath"));
		cj.setUploadPath_bak(request.getParameter("uploadPath_bak"));
		cj.setMaxcount(request.getParameter("maxcount"));
		cj.setCustomerids(request.getParameter("customerids"));
		cj.setCustomerid_tuihuo(request.getParameter("customerid_tuihuo"));
		cj.setWarehouseid(Long.valueOf(request.getParameter("warehouseid")));
		cj.setPartener(request.getParameter("partener"));
		cj.setReadtxtCharcode(request.getParameter("readtxtCharcode"));

		cj.setIsdelDirFlag(request.getParameter("isdelDirFlag").equals("1") ? true : false);

		String oldCustomerids = "";
		String completeCustomerid = customerids + "," + cj.getCustomerid_tuihuo();

		JSONObject jsonObj = JSONObject.fromObject(cj);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getHomeGou(joint_num).getCustomerids() + "," + getHomeGou(joint_num).getCustomerid_tuihuo();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(completeCustomerid, oldCustomerids, joint_num);
		this.customerService.initCustomerList();
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public int getStateForYihaodian(int key) {
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
