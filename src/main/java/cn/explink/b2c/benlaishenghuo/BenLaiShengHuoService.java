package cn.explink.b2c.benlaishenghuo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.benlaishenghuo.xml.ArrayOfRow;
import cn.explink.b2c.benlaishenghuo.xml.BenlaiUnmarchal;
import cn.explink.b2c.benlaishenghuo.xml.row;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.CwbOrder;
import cn.explink.service.CustomerService;
import cn.explink.util.MD5.MD5Util;

@Service
public class BenLaiShengHuoService {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	JointService jointService;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CustomWareHouseDAO customWarehouseDAO;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	CustomerService customerService;
	private Logger logger = LoggerFactory.getLogger(BenLaiShengHuo.class);

	public String PostXmlForBenlai(String xMLDOC, String sign, int key) {
		BenLaiShengHuo bn = getBenlaiShenghuo(key);
		String xml = "<ResponseOrders><Result>false</Result><Remark>解析xml或者运行出现未知异常</Remark></ResponseOrders>";
		try {
			int isOpenFlag = jointService.getStateForJoint(key);
			if (isOpenFlag == 0) {
				logger.info("未开启[本来生活][" + key + "]对接！---当前获取订单详情-----");
				return "";
			}
			if (bn.getIsopendownload() == 0) {
				logger.info("未开启[本来生活][" + key + "]订单下载接口");
				return "";
			}
			String password = MD5Util.getMD5String32Bytes(xMLDOC, bn.getUserkey());
			ArrayOfRow rootnote = BenlaiUnmarchal.Unmarchal(xMLDOC);
			long warehouseid = bn.getWarehouseid(); // 订单导入的库房Id
			List<row> rowlist = rootnote.getList();
			logger.info("【本来网】传递过来的sign={},[我自己]的password={}", sign, password);
			if (!password.equals(sign)) {
				xml = "<ResponseOrders><Result>false</Result><Remark>签名不正确</Remark></ResponseOrders>";
			} else {
				StringBuffer responsexml = new StringBuffer();
				List<Map<String, String>> listshow = getListForBenLai(rowlist, bn, password, responsexml);
				logger.info("准备插入[临时表......]--customerid:{},warehouseid:{}", bn.getCustomerid(), warehouseid);
				if (listshow.size() > 0) {
					dataImportService_B2c.Analizy_DataDealByB2c(Long.valueOf(bn.getCustomerid()), B2cEnum.benlaishenghuo.getMethod(), listshow, warehouseid, true);
				}
				xml = "<ResponseOrders><Result>true</Result><Remark></Remark>" + responsexml.toString() + "</ResponseOrders>";
			}
		} catch (Exception e) {
			logger.error("【本来生活】订单下载——出现异常", e);
		}
		logger.info("发送给[本来生活]的xml={}", xml);
		return xml;
	}

	private String IsSuccessReturn(BenLaiShengHuo bn, String cwb, String is, String remark) {
		String retrunxml = "";
		String pacakgexml = "<ResponseOrder>" + "<waybillCode>" + cwb + "</waybillCode>" + "<Success>" + is + "</Success>" + "<Remark>" + remark + "</Remark>" + "</ResponseOrder>";
		retrunxml += pacakgexml;

		return retrunxml;
	}

	private List<Map<String, String>> getListForBenLai(List<row> rowlist, BenLaiShengHuo bn, String password, StringBuffer responsexml) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (row l : rowlist) {
			CwbOrder cwbOrder = cwbDAO.getCwbByCwb(l.getWaybillCode());
			if (cwbOrder != null) {
				logger.info("【本来生活】订单重复--订单号{}", l.getWaybillCode());
				responsexml.append(IsSuccessReturn(bn, l.getWaybillCode(), "true", "订单重复"));
				continue;
			}
			String warhouseid = "0";
			if (l.getDeliveryhouseId() != null && l.getDeliveryhouseId().length() > 0) {

				if (!customWarehouseDAO.isExistsWarehouFlag(l.getDeliveryhouseId(), bn.getCustomerid())) {
					CustomWareHouse custwarehouse = new CustomWareHouse();
					custwarehouse.setCustomerid(Long.valueOf(bn.getCustomerid()));
					custwarehouse.setCustomerwarehouse(l.getDeliveryhouseId());
					custwarehouse.setWarehouse_no(l.getDeliveryhouseId());
					custwarehouse.setWarehouseremark("");
					custwarehouse.setIfeffectflag(1);
					customWarehouseDAO.creCustomer(custwarehouse);
					logger.info("【本来生活】没有库房【新建】{}={}", custwarehouse);
				}
				warhouseid = dataImportService_B2c.getCustomerWarehouseNo(l.getDeliveryhouseId(), bn.getCustomerid());
			}

			Map<String, String> map = new HashMap<String, String>();
			map.put("transcwb", l.getOrder_no());
			map.put("cwb", l.getWaybillCode());// 运单号
			map.put("cwbordertypeid", l.getOrder_type());
			map.put("emaildate", l.getShippDate());
			map.put("remark2", l.getDeliveryName() + ":" + l.getDeliveryGoods() + ":" + l.getGoodsNum());
			map.put("consigneename", l.getCustomerName());
			map.put("consigneemobile", l.getCustomerPhone());
			map.put("consigneephone", l.getCustomerPhone());
			map.put("consigneeaddress", l.getCustomerAddress());
			map.put("cwbprovince", l.getCustomerProvince());
			map.put("cwbcity", l.getCustomerCity());
			map.put("customerwarehouseid", warhouseid);
			map.put("remark1", l.getDeliveryName() + ":" + l.getDeliveryPhone() + "" + l.getDeliveryPhone() + ":" + l.getDeliveryAddress());
			map.put("caramount", l.getDeliveryAmount());
			map.put("cargorealweight", l.getWeight());
			map.put("receivablefee", l.getShouldReceive());
			map.put("cwbremark", l.getRemark());
			map.put("customercommand", l.getRequest());
			map.put("remark3", "订单来源——本来网接口");
			map.put("sendcarnum", "1");
			list.add(map);

			responsexml.append(IsSuccessReturn(bn, l.getWaybillCode(), "true", "成功"));
		}
		return list;
	}

	public Object getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public BenLaiShengHuo getBenlaiShenghuo(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		BenLaiShengHuo masker = (BenLaiShengHuo) JSONObject.toBean(jsonObj, BenLaiShengHuo.class);
		return masker;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		BenLaiShengHuo blsh = new BenLaiShengHuo();
		String customerid = request.getParameter("customerids");
		blsh.setCustomerid(customerid);
		blsh.setsShippedCode(request.getParameter("sShippedCode"));
		blsh.setUserkey(request.getParameter("userkey"));
		blsh.setTrack_url(request.getParameter("Track_url"));
		blsh.setWarehouseid(Long.parseLong(request.getParameter("warehouseid")));
		blsh.setPost_url(request.getParameter("pushCwb_URL"));
		blsh.setIsopendownload(Integer.parseInt(request.getParameter("isopenDataDownload")));
		JSONObject jsonObj = JSONObject.fromObject(blsh);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		String oldCustomerids = "";
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getBenlaiShenghuo(joint_num).getCustomerid();
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

}
