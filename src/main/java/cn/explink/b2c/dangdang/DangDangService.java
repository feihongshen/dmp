package cn.explink.b2c.dangdang;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomerDAO;
import cn.explink.service.CustomerService;

@Service
public class DangDangService {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CustomerService customerService;

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public DangDang getDangDang(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		DangDang dangdang = (DangDang) JSONObject.toBean(jsonObj, DangDang.class);
		return dangdang;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		DangDang dangdang = new DangDang();
		dangdang.setExpress_id(request.getParameter("express_id"));
		dangdang.setCount(Integer.parseInt(request.getParameter("count")));
		dangdang.setCustomerids(request.getParameter("customerids"));
		dangdang.setRuku_url(request.getParameter("ruku_url"));
		dangdang.setChukuPaiSong_url(request.getParameter("chukuPaiSong_url"));
		dangdang.setPrivate_key(request.getParameter("private_key"));
		dangdang.setDeliverystate_url(request.getParameter("deliverystate_url"));
		dangdang.setTrackinfo_url(request.getParameter("trackinfo_url"));

		String customerids = request.getParameter("customerids");
		String oldCustomerids = "";
		JSONObject jsonObj = JSONObject.fromObject(dangdang);

		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getDangDang(joint_num).getCustomerids();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}

		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);
		this.customerService.initCustomerList();
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

}
