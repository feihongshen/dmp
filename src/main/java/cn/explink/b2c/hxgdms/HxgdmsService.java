package cn.explink.b2c.hxgdms;

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

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.CustomWareHouse;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.CustomerService;
import cn.explink.util.MD5.MD5Util;

@Service
public class HxgdmsService {
	private Logger logger = LoggerFactory.getLogger(HxgdmsService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	CustomerService customerService;

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Hxgdms getHxgDms(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Hxgdms smile = (Hxgdms) JSONObject.toBean(jsonObj, Hxgdms.class);
		return smile;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Hxgdms dms = new Hxgdms();
		String customerids = request.getParameter("customerids");
		dms.setCustomerids(customerids);
		dms.setFeedbackUrl(request.getParameter("feedbackUrl"));
		dms.setImportUrl(request.getParameter("importUrl"));
		String maxcount = "".equals(request.getParameter("maxcount")) ? "0" : request.getParameter("maxcount");
		dms.setMaxCount(Integer.parseInt(maxcount));
		dms.setSecretKey(request.getParameter("secretKey"));
		String warehouseid = request.getParameter("warehouseid");
		dms.setWarehouseid(Long.valueOf(warehouseid));
		dms.setDcode(request.getParameter("dcode"));
		dms.setDeleServerMobile(request.getParameter("deleServerMobile"));
		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(dms);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getHxgDms(joint_num).getCustomerids();
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

	/**
	 * 
	 * @param result
	 *            True or False
	 * @param remark
	 *            备注信息，如果订单重复， 则True ,remark="运单重复"
	 * @return
	 */
	public String buildResponseJSON(String result, String remark) {
		String info = "{\"Rtn_Code\":\"" + result + "\",\"Rtn_Msg\":\"" + remark + "\"}";
		logger.info("返回好享购DMS={}", info);

		return info;
	}

	/**
	 * 处理思迈请求数据，并且导入系统 数据，一单一单的导入
	 * 
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	public String dealwithHxgdmsOrders(String Request, String Signed, String Action, String Dcode) throws NumberFormatException, Exception {

		int smileKey = B2cEnum.Hxgdms.getKey();
		int isOpenFlag = jiontDAO.getStateByJointKey(smileKey);
		Hxgdms dms = this.getHxgDms(smileKey);
		if (isOpenFlag == 0) {
			return buildResponseJSON("1", "未开启接口");
		}

		if (!Dcode.equals(dms.getDcode())) {
			return buildResponseJSON("1", "指定Dcode不正确");
		}

		String localSign = MD5Util.md5(Request + dms.getSecretKey(), "UTF-8");
		if (!localSign.equalsIgnoreCase(Signed)) {
			return buildResponseJSON("1", "签名验证失败");
		}

		DmsOrder dmsOrder = JacksonMapper.getInstance().readValue(Request, DmsOrder.class);

		List<Map<String, String>> orderlist = parseCwbArrByOrderDto(dmsOrder, dms);

		if (orderlist == null || orderlist.size() == 0) {
			logger.warn("好享购DMS-请求没有封装参数，订单号可能为空");
			return buildResponseJSON("0", "运单重复或者是部分不需要的回单");
		}

		long warehouseid = dms.getWarehouseid(); // 订单导入的库房Id
		dataImportInterface.Analizy_DataDealByB2c(Long.parseLong(dms.getCustomerids()), B2cEnum.Hxgdms.getMethod(), orderlist, warehouseid, true);

		logger.info("好享购DMS-订单导入成功");

		return buildResponseJSON("0", "成功");
	}

	/**
	 * 返回一个转化为导入接口可识别的对象
	 */
	private List<Map<String, String>> parseCwbArrByOrderDto(DmsOrder dmsOrder, Hxgdms dms) {
		List<Map<String, String>> cwbList = new ArrayList<Map<String, String>>();

		if (dmsOrder != null) {

			CwbOrderDTO cwbOrder = dataImportDAO_B2c.getCwbByCwbB2ctemp(dmsOrder.getWorkCode());
			if (cwbOrder != null) {
				logger.warn("获取0好享购DMS0订单中含有重复数据cwb={}", dmsOrder.getWorkCode());
				return null;
			}

			int WorkType = dmsOrder.getWorkType(); // 0:正常，1：换货运单，2：退货单（拒收）
			
			/*************mod yurong.liang 2016-08-18**************/
			if(WorkType==WorkTypeEnum.TuiHuoDan.getDmsState()||WorkType==WorkTypeEnum.TuiHuoDan_posun.getDmsState()
					||WorkType==WorkTypeEnum.TuiHuo_cancel.getDmsState()){
				logger.warn("该好享购订单为dmp系统不需要的回单，系统不处理,cwb={},WorkType={}", dmsOrder.getWorkCode(),WorkType);
				return null;
			}
			/*************mod end**************/
			
			int cwbordertypeid = 1;
			cwbordertypeid = getCwbOrdertypeId(WorkType, cwbordertypeid);
			double replCost = dmsOrder.getReplCost().doubleValue();

			String remark3 = getRemark2Str(dmsOrder); // 寄件地址 拼接
			String workCode = (dmsOrder.getyWorkCode() == null || dmsOrder.getyWorkCode().isEmpty()) ? "" : ("原运单号:" + dmsOrder.getyWorkCode());
			int sendcarnum = dmsOrder.getOrderCount() == 0 ? 1 : dmsOrder.getOrderCount(); // 发货数量
			if (cwbordertypeid != CwbOrderTypeIdEnum.Peisong.getValue()) {
				sendcarnum = 0;
			}
			double paybackfee = 0;
			if (cwbordertypeid == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				paybackfee = replCost;
				replCost = 0;
			}
			String consigneeaddress = dmsOrder.getGetProvice() + dmsOrder.getGetCity() + dmsOrder.getGetCounty() + dmsOrder.getGetStreet() + dmsOrder.getGetAddress(); // 收件地址
			String siteNo = dmsOrder.getSiteNo();

			String isRecInvoices = dmsOrder.getIsRecInvoices() == 0 ? "不回收" : "回收";

			Map<String, String> cwbMap = new HashMap<String, String>();
			cwbMap.put("cwb", dmsOrder.getWorkCode()); // cwb 订单号
			cwbMap.put("transcwb", dmsOrder.getClientCode()); // transcwb 客户单号
			cwbMap.put("consigneename", dmsOrder.getGetPerson());
			cwbMap.put("consigneeaddress", consigneeaddress);
			cwbMap.put("consigneephone", dmsOrder.getGetTel());
			cwbMap.put("consigneemobile", dmsOrder.getGetPhone());
			cwbMap.put("cargorealweight", (dmsOrder.getOrderHav() == null || dmsOrder.getOrderHav().isEmpty()) ? "0" : dmsOrder.getOrderHav()); // 订单重量(KG)
			cwbMap.put("cargosize", (dmsOrder.getOrderSize() == null || dmsOrder.getOrderSize().isEmpty()) ? "" : dmsOrder.getOrderSize()); // 订单体积（立方厘米）
			cwbMap.put("sendcarnum", sendcarnum + ""); // 发货数量
			cwbMap.put("receivablefee", String.valueOf(replCost));
			cwbMap.put("paybackfee", String.valueOf(paybackfee));
			cwbMap.put("cwbordertypeid", String.valueOf(cwbordertypeid));
			cwbMap.put("cwbremark", dmsOrder.getNote());
			cwbMap.put("remark1", workCode); // 原运单号
			cwbMap.put("remark2", dmsOrder.getrWorkCode()); // 退货号
			cwbMap.put("remark3", remark3); // 寄件地址
			cwbMap.put("customerwarehouseid", String.valueOf(getOrCreateCustomerWarehouse(dms.getCustomerids(), siteNo)));

			cwbMap.put("customercommand", "是否回收发票:" + isRecInvoices); // 是否回收发票

			String sendcarname = "";
			double cargoAmount = 0;
			List<Good> goods = dmsOrder.getGoods();
			for (Good good : goods) {
				String goodsTypeItem = good.getGoodsTypeItem() == 1 ? "普通" : "贵品";
				sendcarname += good.getGoodsName() + "(" + goodsTypeItem + "),";
				cargoAmount += good.getGoodsAmt().doubleValue();
			}

			cwbMap.put("sendcarname", sendcarname); // 发货货物名称
			cwbMap.put("caramount", String.valueOf(cargoAmount));

			cwbList.add(cwbMap);

		}
		return cwbList;
	}

	private long getOrCreateCustomerWarehouse(String customerid, String warehousename) {
		long customerwarehouseid = 0;
		CustomWareHouse custwarehouse = customWareHouseDAO.getCustomWareHouseByName(warehousename, customerid);
		if (custwarehouse == null) {
			customerwarehouseid = customWareHouseDAO.creCustomerGetId(Long.valueOf(customerid), warehousename);
		} else {
			customerwarehouseid = custwarehouse.getWarehouseid();
		}
		return customerwarehouseid;
	}

	private String getRemark2Str(DmsOrder dmsOrder) {
		String trustPerson = dmsOrder.getTrustPerson(); // 发件人
		String trustAddress = dmsOrder.getTrustAddress(); // 发件地址
		String trustTel = dmsOrder.getTrustTel() + "," + dmsOrder.getTrustPhone(); // 寄件人电话
		String remark2 = "发件人:" + trustPerson + ",发件地址:" + trustAddress + ",联系方式:" + trustTel;
		return remark2;
	}

	private int getCwbOrdertypeId(int WorkType, int cwbordertypeid) {
		if (WorkType == WorkTypeEnum.PeiSongDan.getDmsState()) {
			cwbordertypeid = CwbOrderTypeIdEnum.Peisong.getValue();
		} else if (WorkType == WorkTypeEnum.HuanHuodan.getDmsState()) {
			cwbordertypeid = CwbOrderTypeIdEnum.Shangmenhuan.getValue();
		} else if (WorkType == WorkTypeEnum.TuiHuoDan.getDmsState() || WorkType == WorkTypeEnum.TuiHuoDan_posun.getDmsState() || WorkType == WorkTypeEnum.TuiHuo_cancel.getDmsState()
				|| WorkType == WorkTypeEnum.TuiHuo_TuiKuan.getDmsState() || WorkType == WorkTypeEnum.TuiHuo_BuTuiKuan.getDmsState() || WorkType == WorkTypeEnum.HuanHuo_tuihui.getDmsState()) {
			cwbordertypeid = CwbOrderTypeIdEnum.Shangmentui.getValue();
		}
		return cwbordertypeid;
	}

}
