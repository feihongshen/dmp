package cn.explink.b2c.haoxgou;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.pos.tools.JacksonMapper;

@Service
public class HaoXiangGouService {
	private Logger logger = LoggerFactory.getLogger(HaoXiangGouService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ExplinkService explinkService;
	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	CustomWareHouseDAO customWarehouseDAO;

	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	JointService jointService;

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public HaoXiangGou getHaoXiangGou(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		HaoXiangGou smile = (HaoXiangGou) JSONObject.toBean(jsonObj, HaoXiangGou.class);
		return smile;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		HaoXiangGou hxg = new HaoXiangGou();
		String customerids = request.getParameter("customerids");
		hxg.setCustomerids(customerids);
		hxg.setDlver_cd(request.getParameter("dlver_cd"));
		hxg.setRequst_url(request.getParameter("requst_url"));
		hxg.setDes_key(request.getParameter("des_key"));
		hxg.setPassword(request.getParameter("pwd"));
		hxg.setWarehouseid(Long.valueOf(request.getParameter("warehouseid")));
		hxg.setMaxCount(Long.valueOf(request.getParameter("maxCount")));
		hxg.setIsopentestflag(Integer.valueOf(request.getParameter("isopentestflag")));
		hxg.setStarttime(request.getParameter("starttime"));
		hxg.setEndtime(request.getParameter("endtime"));
		hxg.setSelectHours(Integer.valueOf(request.getParameter("selectHours")));
		hxg.setPospaycode(request.getParameter("pospaycode"));

		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(hxg);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getHaoXiangGou(joint_num).getCustomerids();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

}
