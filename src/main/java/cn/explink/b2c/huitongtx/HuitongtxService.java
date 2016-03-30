package cn.explink.b2c.huitongtx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.huitongtx.addressmatch.HttxEditBranchDAO;
import cn.explink.b2c.huitongtx.response.datadetail;
import cn.explink.b2c.huitongtx.response.response;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.CustomerService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class HuitongtxService {

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	HuitongtxDAO tmallDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	CustomWareHouseDAO customWarehouseDAO;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	HttxEditBranchDAO httxEditBranchDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerService customerService;

	public static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	private Logger logger = LoggerFactory.getLogger(HuitongtxService.class);

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Huitongtx getHuitongtx(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Huitongtx httx = (Huitongtx) JSONObject.toBean(jsonObj, Huitongtx.class);
		return httx;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Huitongtx httx = new Huitongtx();
		httx.setApp_key(request.getParameter("app_key"));

		httx.setService_code(request.getParameter("service_code"));
		httx.setPrivate_key(request.getParameter("private_key"));
		httx.setPost_url(request.getParameter("post_url"));
		httx.setGetInfo_url(request.getParameter("getInfo_url"));
		httx.setCustomerids(request.getParameter("customerids"));
		httx.setWarehouseid(Long.parseLong(request.getParameter("warehouseid")));
		String customerids = request.getParameter("customerids");
		httx.setServiceTel(request.getParameter("serviceTel"));
		httx.setSelectMaxCount(Integer.valueOf(request.getParameter("selectMaxCount")));

		httx.setAddressMaxCount(Long.valueOf(request.getParameter("addressMaxCount")));
		httx.setAddressReceiver_url(request.getParameter("addressReceiver_url"));
		httx.setAddressSender_url(request.getParameter("addressSender_url"));
		httx.setIsopenaddress(Long.valueOf(request.getParameter("isopenaddress")));

		httx.setB2c_enum(joint_num); // 存入枚举到实体对象
		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(httx);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getHuitongtx(joint_num).getCustomerids();

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

	public int getStateForTmall(int key) {
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
	 * 处理tmall请求 被动接收订单数据接口
	 * 
	 * @throws IOException
	 * @throws JDOMException
	 */
	public String requestMethod(Huitongtx httx, String app_key, String sign, String timestamp, String method, String data) throws Exception {

		String nowtime = DateTimeUtil.getNowTime();

		// 验证基础信息是否通过
		validateBaseInfo(app_key, sign, httx.getPrivate_key(), timestamp, method, data, httx);

		Map<String, String> xmlMap = null;

		List<HuitongtxXMLNote> httxbeanList = JacksonMapper.getInstance().readValue(data, new TypeReference<List<HuitongtxXMLNote>>() {
		});

		response resp = new response();
		resp.setCode("0");
		resp.setMessage("成功");

		List<datadetail> datadetailList = new ArrayList<datadetail>();
		resp.setData(datadetailList);

		// 插入临时表的反馈结果
		List<Map<String, String>> xmlList = getDetailParamList(httx.getCustomerids(), nowtime, httxbeanList, httx, datadetailList);
		long warehouseid = getHuitongtx(httx.getB2c_enum()).getWarehouseid(); // 订单入库库房

		dataImportInterface.Analizy_DataDealByB2c(Long.valueOf(httx.getCustomerids()), B2cEnum.Huitongtx.getMethod(), xmlList, warehouseid, true);

		logger.info("汇通天下数据插入临时表成功");

		return responseMessage("0", "", datadetailList);

	}

	// private void IsAutoCreateCustomerWarehouse(Huitongtx httx,
	// HuitongtxXMLNote httxBean) {
	// String wms_code =
	// httxBean.getWms_code()!=null&&!httxBean.getWms_code().isEmpty()?httxBean.getWms_code():"";
	// if (!customWarehouseDAO.isExistsWarehouFlag(wms_code,
	// httx.getCustomerids())) {
	// CustomWareHouse custwarehouse=new CustomWareHouse();
	// custwarehouse.setCustomerid(Long.valueOf(httx.getCustomerids()));
	// custwarehouse.setCustomerwarehouse(httxBean.getWms_code());
	// custwarehouse.setWarehouse_no(httxBean.getWms_code());
	// custwarehouse.setWarehouseremark(httxBean.getWms_address());
	// custwarehouse.setIfeffectflag(1);
	// customWarehouseDAO.creCustomer(custwarehouse);
	// }
	// }

	/**
	 * 验证基础的配置是否正确
	 * 
	 * @param partner
	 * @param signtype
	 * @param sign
	 * @param notify_id
	 * @param notify_type
	 * @param content
	 * @return
	 */
	private void validateBaseInfo(String app_key, String sign, String private_key, String timestamp, String method, String data, Huitongtx httx) {

		if (!httx.getApp_key().equals(app_key)) {
			throw new RuntimeException("指定TP唯一appkey=" + app_key + "不正确!");
		}

		Map<String, String> params = HuitongtxConfig.buildParmsMap(method, timestamp, app_key, private_key, data);
		String signstr = httx.getPrivate_key() + HuitongtxConfig.createLinkString(params) + httx.getPrivate_key();
		logger.info("汇通天下签名字符串=" + signstr);
		if (!sign.equalsIgnoreCase(MD5Util.md5(signstr))) {
			logger.info("加密结果=" + MD5Util.md5(signstr));
			throw new RuntimeException("签名验证异常sign=" + sign);
		}
	}

	/**
	 * 反馈给汇通天下信息 失败信息
	 * 
	 * @param log_result
	 * @param log_event
	 * @return
	 */
	public String responseMessage(String code, String log_event, List<datadetail> data) {
		String strs = "";
		response resp = new response();
		resp.setMessage(log_event);
		resp.setCode(code);
		if (data != null) {
			resp.setData(data);
		}

		try {
			strs = jacksonmapper.writeValueAsString(resp);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info(strs);
		return strs;

	}

	/**
	 * 解析xml转化为list 把map转换为list 参数
	 * 
	 * @param customer_id
	 * @param nowtime
	 * @param httxBean
	 * @param notify_id
	 * @return
	 */
	public List<Map<String, String>> getDetailParamList(String customer_id, String nowtime, List<HuitongtxXMLNote> httxBeanList, Huitongtx httx, List<datadetail> datadetailList) {
		List<Map<String, String>> paralist = new ArrayList<Map<String, String>>();

		for (HuitongtxXMLNote httxBean : httxBeanList) {
			String taskcode = httxBean.getTaskcode();

			Map<String, String> dataMap = createCwbOrderMapps(customer_id, nowtime, httxBean, taskcode);

			paralist.add(dataMap);

			datadetail dl = new datadetail();
			dl.setTaskcode(taskcode);
			dl.setStatus(1);
			datadetailList.add(dl);

			// IsAutoCreateCustomerWarehouse(httx, httxBean); //仓库设置

		}

		return paralist;
	}

	private Map<String, String> createCwbOrderMapps(String customer_id, String nowtime, HuitongtxXMLNote httxBean, String taskcode) {
		String transcwb = httxBean.getTms_order_code();
		String cwb = httxBean.getOrder_code();
		String order_flag = httxBean.getOrder_flag() + "";

		String receivablefee = httxBean.getTotal_amount() + "";

		// 客户要求
		String schedule_start = httxBean.getSchedule_start() == null ? "" : httxBean.getSchedule_start();
		String schedule_end = httxBean.getSchedule_end() == null ? "" : httxBean.getSchedule_end();
		String schedule_type = httxBean.getSchedule_type() + "";
		String customercommand = Analyz_CustomerCommand(schedule_type);
		if (!"".equals(schedule_start)) {
			customercommand += ";晚于" + schedule_start;
		}
		if (!"".equals(schedule_end)) {
			customercommand += ";早于" + schedule_end;
		}

		String consigneename = httxBean.getReceiver_name() == null ? "" : httxBean.getReceiver_name();
		String consigneepostcode = httxBean.getReceiver_zip() == null ? "" : httxBean.getReceiver_zip();
		String cwbprovince = httxBean.getReceiver_province() == null ? "" : httxBean.getReceiver_province();
		String cwbcity = httxBean.getReceiver_city() == null ? "" : httxBean.getReceiver_city();
		String cwbcounty = httxBean.getReceiver_district() == null ? "" : httxBean.getReceiver_district();
		String consigneeaddress = httxBean.getReceiver_address() == null ? "" : httxBean.getReceiver_address();
		String consigneemobile = httxBean.getReceiver_mobile() == null ? "" : httxBean.getReceiver_mobile();
		String consigneephone = httxBean.getReceiver_phone() == null ? "" : httxBean.getReceiver_phone();
		String cargorealweight = httxBean.getPackage_weight() == null || httxBean.getPackage_weight().isEmpty() ? "0" : httxBean.getPackage_weight();

		String sendcargoname = "[发出商品]";
		String cwbordertypeid = "1";
		String cwbdelivertypeid = "1";

		// / 地址是省市区之和
		String consigneeaddress_all = cwbprovince + cwbcity + cwbcounty + consigneeaddress;

		// 重量
		double cargorealweight_a = (Double.parseDouble(cargorealweight)) / 1000;

		double receivablefee_a = (Double.parseDouble(receivablefee)) / 100;
		if (!order_flag.contains("1")) { // cod类型，有代收款
			if (order_flag.contains("8")) { // 上门换
				cwbordertypeid = "3";
			} else if (order_flag.contains("9")) { // 上门退
				cwbordertypeid = "2";
			}
			receivablefee_a = 0;
		}

		customercommand += ";" + Analyz_tmallOrderFlag(order_flag);
		double cargoamount = (Double.parseDouble(receivablefee)) / 100; // 货物金额

		// 插入订单入库库房
		String branch_id = "0";

		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("cwb", cwb);
		dataMap.put("transcwb", transcwb);
		dataMap.put("order_flag", order_flag);
		dataMap.put("nowtime", nowtime);
		dataMap.put("consigneename", consigneename);
		dataMap.put("consigneepostcode", consigneepostcode);
		dataMap.put("consigneeaddress", consigneeaddress_all);
		dataMap.put("consignoraddress", ""); // tmall仓库地址，这个要存.
		dataMap.put("cwbprovince", cwbprovince);
		dataMap.put("cwbcity", cwbcity);

		dataMap.put("cwbcounty", cwbcounty);
		dataMap.put("consigneemobile", consigneemobile);
		dataMap.put("consigneephone", consigneephone);
		dataMap.put("cargorealweight", cargorealweight_a + "");

		dataMap.put("customercommand", customercommand);
		dataMap.put("cargoamount", cargoamount + ""); // 货物金额
		dataMap.put("receivablefee", receivablefee_a + ""); // 代收款
		dataMap.put("sendcargoname", sendcargoname); // 发货商品
		dataMap.put("sendcarnum", "1"); // 发货商品
		dataMap.put("customerwarehouseid", "0"); // 发货仓库
		dataMap.put("cwbordertypeid", cwbordertypeid); // 订单类型
		dataMap.put("cwbdelivertypeid", cwbdelivertypeid); // 配送方式
		dataMap.put("startbranchid", branch_id); // 导入库房id
		dataMap.put("customerid", customer_id); // 配送方式
		dataMap.put("tmall_notify_id", taskcode); // 唯一标示，控制不重复。
		dataMap.put("remark2", taskcode);

		dataMap.put("multi_shipcwb", transcwb);

		dataMap.put("cwbremark", customercommand);
		return dataMap;
	}

	private String Analyz_CustomerCommand(String schedule_type) {
		String customercommand = "";
		if ("1".equals(schedule_type)) {
			customercommand = "工作日送";
		} else if ("2".equals(schedule_type)) {
			customercommand = "节假日送";
		} else if ("101".equals(schedule_type)) {
			customercommand = "当日达";
		} else if ("102".equals(schedule_type)) {
			customercommand = "次晨达";
		} else if ("103".equals(schedule_type)) {
			customercommand = "次日达";
		} else if ("104".equals(schedule_type)) {
			customercommand = "二日达";
		} else if ("105".equals(schedule_type)) {
			customercommand = "三日达";
		} else if ("106".equals(schedule_type)) {
			customercommand = "迟到免单";
		} else if ("111".equals(schedule_type)) {
			customercommand = "活动标";
		}
		return customercommand;
	}

	private String Analyz_tmallOrderFlag(String order_flag) {
		String customercommand = "";
		if (order_flag.contains("1")) {
			customercommand += "cod –货到付款";
		}
		if (order_flag.contains("2")) {
			customercommand += "limit-限时配送";
		}
		if (order_flag.contains("8")) {
			customercommand += "换货";
		}
		if (order_flag.contains("9")) {
			customercommand += "上门";
		}
		return customercommand;
	}

}
