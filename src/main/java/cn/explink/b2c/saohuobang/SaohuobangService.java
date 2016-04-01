package cn.explink.b2c.saohuobang;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.saohuobang.xml.Items;
import cn.explink.b2c.saohuobang.xml.RequestOrder;
import cn.explink.b2c.saohuobang.xml.SaohuobangUnmarchal;
import cn.explink.b2c.saohuobang.xml.UpdateInfo;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tools.SaohuobangSign;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.CustomerService;
import cn.explink.support.transcwb.TransCwbDao;

@Service
public class SaohuobangService {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	JointService jointService;
	@Autowired
	TransCwbDao transCwbDao;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	SaohuobangDao saohuobangDao;
	@Autowired
	CustomerService customerService;
	private Logger logger = LoggerFactory.getLogger(Saohuobang.class);

	/*
	 * 扫货帮 订单下载接口
	 */
	public String getPushXMLImport(String xml, String sign) {
		RequestOrder rootnote = null;
		String retrunxml = "";
		Saohuobang saohuobang = null;
		try {

			rootnote = SaohuobangUnmarchal.Unmarchal(xml);
			List<Items> list = rootnote.getListitems();
			JointEntity entity = jiontDAO.getCountByClientID(rootnote.getClientID());
			if (entity == null) {
				return "";// 英文格式
			}
			int key = entity.getJoint_num();

			int isOpenFlag = jointService.getStateForJoint(key);
			saohuobang = (Saohuobang) getSaohuobang(key);

			if (isOpenFlag == 0) {
				logger.info("未开启[" + saohuobang.getClientID() + "][" + key + "]对接！---当前获取订单详情-----");
				return jiontXmlForRetrunSaohuobang("FALSE", "", saohuobang, "import", "未开启对接");
			}
			if (saohuobang.getIsopendownload() == 0) {
				logger.info("未开启[" + saohuobang.getClientID() + "][" + key + "]订单下载接口");
				return jiontXmlForRetrunSaohuobang("FALSE", "", saohuobang, "import", "未开启对接开口");
			}

			// 验证密码：如果是发送的，如果是取消订单的
			String password = SaohuobangSign.encryptSign_Method(xml, saohuobang.getKey());
			logger.info("我自己的password={}", password);
			String flag = "";

			if (!password.equals(sign)) {
				return jiontXmlForRetrunSaohuobang("FALSE", "", saohuobang, "import", "签名验证错误");
			}

			String transcwb = rootnote.getTxLogisticID();
			String cwb = rootnote.getMailNo();
			Track_Saohuobang.validate(rootnote);

			CwbOrderDTO cwbifexit = dataImportDAO_B2c.getCwbByCwbB2ctemp(cwb);
			if (cwbifexit != null) {
				flag = "DOUBLE";
			} else {
				List<Map<String, String>> xmlList = EncapsulationToMap(rootnote, list);
				dataImportInterface.Analizy_DataDealByB2c(Long.parseLong(saohuobang.getCustomerId()), B2cEnum.saohuobang.getMethod(), xmlList, saohuobang.getWarehouseid(), true);
				String str = "";
				for (int i = 0; i < list.size(); i++) {
					List<CargoInfo> datalist = new ArrayList<CargoInfo>();
					for (int j = 0; j < list.get(i).getListitem().size(); j++) {
						CargoInfo ddd = new CargoInfo();
						ddd.setItemsBrandname(list.get(i).getListitem().get(j).getItemBrandName());
						ddd.setItemsId(list.get(i).getListitem().get(j).getItemID());
						ddd.setItemsname(list.get(i).getListitem().get(j).getItemName());
						ddd.setNum(list.get(i).getListitem().get(j).getNumber());
						datalist.add(ddd);
					}
					str = JacksonMapper.getInstance().writeValueAsString(datalist);
				}
				saohuobangDao.getInsertInfo(rootnote, str);
				flag = "SUCCESS";
				logger.info("[扫货帮" + key + "]发过来的数据构建成Map{},订单号{}", cwb);
			}
			retrunxml = jiontXmlForRetrunSaohuobang(flag, transcwb, saohuobang, "0", "0");

		} catch (Exception e) {
			logger.error("[扫货帮]订单【存储】接口出现异常：异常原因:{}", e.getMessage());
			retrunxml = jiontXmlForRetrunSaohuobang("FALSE", rootnote.getTxLogisticID(), saohuobang, "Exception", e.getMessage());
		}
		logger.info("返回给扫货帮的xml={}", retrunxml);
		return retrunxml;
	}

	/*
	 * 扫货帮 取消订单接口 回传方式待定
	 */
	public String cancelImportXMl(String requestxml, String sign) {
		String retrunxml = "";
		UpdateInfo rootnote = null;
		Saohuobang saohuobang = null;
		try {
			rootnote = SaohuobangUnmarchal.CancelUpdateInfo(requestxml);
			String clientID = rootnote.getClientID();
			JointEntity entity = jiontDAO.getCountByClientID(clientID);

			if (entity == null) {
				return jiontXmlForRetrunSaohuobang("FALSE", "", saohuobang, "cancel", "未开启对接");
			}
			int key = entity.getJoint_num();
			saohuobang = (Saohuobang) getSaohuobang(key);

			int isOpenFlag = jointService.getStateForJoint(key);
			if (isOpenFlag == 0) {
				logger.info("未开启[扫货帮][" + key + "]对接！---当前获取订单详情-----");
				return jiontXmlForRetrunSaohuobang("FALSE", "", saohuobang, "cancel", "未开启对接");
			}
			if (saohuobang.getIsopendownload() == 0) {
				logger.info("未开启[扫货帮][" + key + "]订单下载接口");
				return jiontXmlForRetrunSaohuobang("FALSE", "", saohuobang, "cancel", "未开启对接下载接口");
			}

			// 签名验证
			String password = SaohuobangSign.encryptSign_Method(requestxml, saohuobang.getKey());

			if (!password.equals(sign)) {
				return jiontXmlForRetrunSaohuobang("FALSE", "", saohuobang, "cancel", "签名验证错误");
			}

			String transcwb = rootnote.getTxLogisticID();// cwb

			String cwb = rootnote.getMailNo();// transcwb
			if (rootnote.getInfoContent().equals("WITHDRAW")) {// 取消订单,state设置为0
				dataImportDAO_B2c.DeleteCwbAndTranscwbtob2ctemp(cwb);
				dataImportDAO_B2c.DeleteCwbAndTranscwbtodetail(cwb);
				saohuobangDao.getInfoForinsert(cwb, "0");
				transCwbDao.deleteTranscwb(cwb);
				return jiontXmlForRetrunSaohuobang("SUCCESS", transcwb, saohuobang, "cancel", "0");
			} else {
				String oldcwb = transCwbDao.getCwbByTransCwb(transcwb);
				/*
				 * 订单修改只支持导入数据，之后无法修改
				 */
				if (oldcwb != null && oldcwb.length() > 0) {// 有这单，cwb
					CwbOrderDTO dto = dataImportDAO_B2c.getTranscwbtemp(oldcwb);// 如果大于1的状态有——不可以修改
					if (dto == null) {
						return jiontXmlForRetrunSaohuobang("FALSE", "", saohuobang, "cancel", "此订单已经不可以修改");
					}

					dataImportDAO_B2c.DeleteCwbAndTranscwbtodetail(oldcwb);
					saohuobangDao.DeleteCwbAndTranscwbtodetail(cwb, oldcwb);
					transCwbDao.deleteTranscwb(oldcwb);
					String remark = "由" + oldcwb + "改变为" + cwb + "原因是：" + rootnote.getRemark();
					List<Map<String, String>> xmlList = encapsulationToMapForUpdate(dto, remark, cwb);
					logger.info("update扫货帮={}", remark);
					dataImportInterface.Analizy_DataDealByB2c(Long.parseLong(saohuobang.getCustomerId()), B2cEnum.saohuobang.getMethod(), xmlList, saohuobang.getWarehouseid(), true);
					logger.info("" + key + "取消订单已经正确执行oldcwb={},cwb={}", oldcwb, cwb);
					retrunxml = jiontXmlForRetrunSaohuobang("SUCCESS", transcwb, saohuobang, "cancel", "0");

				} else {
					retrunxml = jiontXmlForRetrunSaohuobang("FALSE", transcwb, saohuobang, "cancel", "没有要修改的订单");
				}
				logger.info("[扫货帮" + key + "]订单取消,发送的xml{}", requestxml);
				return retrunxml;
			}
		} catch (Exception e) {
			logger.error("[扫货帮]订单取消接口出现异常,异常原因", e);
			retrunxml = jiontXmlForRetrunSaohuobang("FALSE", "", saohuobang, "Exception", e.getMessage());
			return retrunxml;
		}

	}

	/*
	 * [扫货帮]创建map
	 */
	private List<Map<String, String>> EncapsulationToMap(RequestOrder rootnote, List<Items> list) {
		List<Map<String, String>> xmlList = new ArrayList<Map<String, String>>();
		Map<String, String> aMap = new HashMap<String, String>();

		aMap.put("consigneename", rootnote.getReceiver().getName());
		aMap.put("consigneemobile", rootnote.getReceiver().getMobile());
		aMap.put("consigneephone", rootnote.getReceiver().getPhone());
		aMap.put("cwbprovince", rootnote.getReceiver().getProv());
		aMap.put("cwbcity", rootnote.getReceiver().getCity());
		aMap.put("consigneeaddress", rootnote.getReceiver().getAddress());
		aMap.put("consigneepostcode", rootnote.getReceiver().getPostCode());
		aMap.put("cwb", rootnote.getMailNo());
		aMap.put("customerId", rootnote.getCustomerId());
		aMap.put("tradeNo", rootnote.getTradeNo());
		aMap.put("transcwb", rootnote.getTxLogisticID());// 面单号
		aMap.put("cwbordertypeid", rootnote.getType());// 订单类型
		aMap.put("flag", rootnote.getFlag());
		aMap.put("remark5", rootnote.getStoreID() + ":" + rootnote.getStoreName() + ":" + rootnote.getStoreAddress() + ":" + rootnote.getStoreTel() + ":" + rootnote.getOrderCreateTime());
		aMap.put("storeAddress", rootnote.getStoreAddress());
		aMap.put("storeTel", rootnote.getStoreTel());
		aMap.put("orderCreateTime", rootnote.getOrderCreateTime());
		aMap.put("sendStartTime", rootnote.getSendStartTime());
		aMap.put("sendEndTime", rootnote.getSendEndTime());
		aMap.put("itemsValue", rootnote.getItemsValue());
		aMap.put("cargorealweight", rootnote.getItemsWeight());// 重量
		aMap.put("caramount", rootnote.getItemsAllPrice());// 货物金额
		aMap.put("receivablefee", rootnote.getItemsTakePrice());// 代收金额
		aMap.put("customercommand", rootnote.getItemsSendDate());// 客户要求
		aMap.put("insuranceValue", rootnote.getInsuranceValue());
		aMap.put("packageOrNot", rootnote.getPackageOrNot());
		aMap.put("remark2", rootnote.getSupplierID() + ":" + rootnote.getSupplierName());
		String code = "";
		for (Special s : Special.values()) {
			if (s.getResp_code().equals(rootnote.getSpecial())) {
				code = s.getResp_msg();
			}
		}
		aMap.put("cwbremark", rootnote.getRemark() + "===" + code);// 订单备注
		aMap.put("remark1", "发件人地址：" + rootnote.getSend().getProv() + rootnote.getSend().getCity() + rootnote.getSend().getAddress() + "发件人电话：" + rootnote.getSend().getPhone() + " "
				+ rootnote.getSend().getMobile() + "发件人姓名：" + rootnote.getSend().getName() + "邮编" + rootnote.getSend().getPostCode());// 订单备注
		aMap.put("remark4", rootnote.getReserve1() + " " + rootnote.getReserve2() + "  " + rootnote.getReserve3() + "  " + rootnote.getReserve4() + "  " + rootnote.getReserve5());
		xmlList.add(aMap);

		return xmlList;

	}

	private List<Map<String, String>> encapsulationToMapForUpdate(CwbOrderDTO dto, String remark, String cwb) {
		List<Map<String, String>> xmlList = new ArrayList<Map<String, String>>();
		Map<String, String> aMap = new HashMap<String, String>();

		aMap.put("consigneename", dto.getConsigneename());
		aMap.put("consigneemobile", dto.getConsigneemobile());
		aMap.put("consigneephone", dto.getConsigneephone());
		aMap.put("cwbprovince", dto.getCwbprovince());
		aMap.put("cwbcity", dto.getCwbcity());
		aMap.put("consigneeaddress", dto.getConsigneeaddress());
		aMap.put("consigneepostcode", dto.getConsigneepostcode());
		aMap.put("cwb", cwb);
		aMap.put("customerId", String.valueOf(dto.getCustomerid()));
		aMap.put("transcwb", dto.getTranscwb());// 面单号
		aMap.put("cwbordertypeid", String.valueOf(dto.getCwbordertypeid()));// 订单类型
		aMap.put("remark5", dto.getRemark5());
		aMap.put("cargorealweight", String.valueOf(dto.getCargorealweight()));// 重量
		aMap.put("caramount", String.valueOf(dto.getCargoamount()));// 货物金额
		aMap.put("receivablefee", String.valueOf(dto.getReceivablefee()));// 代收金额
		aMap.put("customercommand", dto.getCustomercommand());// 客户要求
		aMap.put("remark2", dto.getRemark2());
		aMap.put("cwbremark", remark);// 订单备注
		aMap.put("remark1", dto.getRemark1());// 订单备注
		aMap.put("remark4", dto.getRemark4());
		// aMap.put("remark3",dto.getRemark3());
		xmlList.add(aMap);

		return xmlList;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Saohuobang saohuobang = new Saohuobang();
		saohuobang.setCallBackCount(Long.parseLong(request.getParameter("callbackcount")));
		saohuobang.setKey(request.getParameter("key"));
		saohuobang.setProviderID(request.getParameter("logisticProviderID"));
		saohuobang.setCustomerId(request.getParameter("customerid"));
		saohuobang.setIsopendownload(Integer.parseInt(request.getParameter("isopenDataDownload")));
		saohuobang.setTrackLog_URL(request.getParameter("trackLog_URL"));
		saohuobang.setClientID(request.getParameter("clientID"));
		saohuobang.setWarehouseid(Long.valueOf(request.getParameter("warehouseid")));
		JSONObject jsonObj = JSONObject.fromObject(saohuobang);

		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		String oldCustomerids = "";
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getSaohuobang(joint_num).getCustomerId();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(saohuobang.getCustomerId(), oldCustomerids, joint_num);
		this.customerService.initCustomerList();
	}

	// 拼接xml
	private String jiontXmlForRetrunSaohuobang(String flag, String cwb, Saohuobang saohuobang, String state, String reason) {
		String retrunxml = "";
		if (flag.equals("DOUBLE")) {
			retrunxml = "<Response>" + "<txLogisticID>" + cwb + "</txLogisticID>" + "<logisticProviderID>" + saohuobang.getProviderID() + "</logisticProviderID>" + "<success>false</success>"
					+ "<status>DUPLICATEORDER</status>" + "<reason>重复订单</reason>" + "</Response>";
		}
		if (flag.equals("SUCCESS")) {
			retrunxml = "<Response>" + "<txLogisticID>" + cwb + "</txLogisticID>" + "<logisticProviderID>" + saohuobang.getProviderID() + "</logisticProviderID>" + "<success>true</success>"
					+ "</Response>";
		}
		if (flag.equals("FALSE")) {
			retrunxml = "<Response>" + "<txLogisticID>" + cwb + "</txLogisticID>" + "<logisticProviderID>" + saohuobang.getProviderID() + "</logisticProviderID>" + "<success>false</success>"
					+ "<status>" + state + "</status>" + "<reason>" + reason + "</reason>" + "</Response>";
		}
		return retrunxml;
	}

	public Map<String, String> analysis_Saohuobangxml(String xml) throws Exception {
		InputStream iStream = new ByteArrayInputStream(xml.getBytes());
		SAXReader saxReader = new SAXReader();
		Map<String, String> returnMap = new HashMap<String, String>();
		Reader r = new InputStreamReader(iStream, "UTF-8");
		Document document = saxReader.read(r);
		Element employees = document.getRootElement();
		for (Iterator i = employees.elementIterator(); i.hasNext();) {
			Element employee = (Element) i.next();
			returnMap.put(employee.getName(), employee.getText());
			for (Iterator j = employee.elementIterator(); j.hasNext();) {
				Element node = (Element) j.next();
				returnMap.put(node.getName(), node.getText());
				for (Iterator m = node.elementIterator(); m.hasNext();) {
					Element last = (Element) m.next();
					returnMap.put(last.getName(), last.getText());
				}
			}
		}
		return returnMap;
	}

	public void update(int key, int state) {
		jiontDAO.UpdateState(key, state);

	}

	public Saohuobang getSaohuobang(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Saohuobang saohuobang = (Saohuobang) JSONObject.toBean(jsonObj, Saohuobang.class);
		return saohuobang;
	}

	private Object getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}
}
