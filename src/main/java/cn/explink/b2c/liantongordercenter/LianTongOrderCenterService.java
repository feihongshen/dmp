package cn.explink.b2c.liantongordercenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import cn.explink.b2c.liantongordercenter.xmlDto.CargoDto;
import cn.explink.b2c.liantongordercenter.xmlDto.OrderDto;
import cn.explink.b2c.liantongordercenter.xmlDto.RequestConfirmDto;
import cn.explink.b2c.liantongordercenter.xmlDto.RequestOrderDto;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.CwbOrderService;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.util.JAXBUtil;
import cn.explink.util.StringUtil;
import cn.explink.util.MD5.SignUtils;
import net.sf.json.JSONObject;

@Service
public class LianTongOrderCenterService {
	private Logger logger = LoggerFactory.getLogger(LianTongOrderCenterService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	private TransCwbDao transCwbDao;
	
	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	/**
	 * 从dao获取实体对象
	 *
	 * @param key
	 * @return
	 */
	public String getObjectMethod(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	/**
	 * 获取联通商城对象
	 *
	 * @param key
	 * @return
	 */
	public LianTongOrderCenter getLianTong(int key) {
		if (this.getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
		LianTongOrderCenter dangdangsc = (LianTongOrderCenter) JSONObject.toBean(jsonObj, LianTongOrderCenter.class);
		return dangdangsc;
	}

	/**
	 * 获取页面上的值
	 *
	 * @param request
	 * @param joint_num
	 */
	public void edit(HttpServletRequest request, int joint_num) {
		LianTongOrderCenter lt = new LianTongOrderCenter();
		// 库房id
		String customerid = request.getParameter("customerid");
		lt.setCustomerid(customerid);
		// 私钥
		lt.setPrivate_key(request.getParameter("private_key"));
		// 查询URL
		lt.setSearch_url(request.getParameter("search_url"));
		String warehouseid = request.getParameter("warehouseid").isEmpty() ? "0" : request.getParameter("warehouseid");
		lt.setWarehouseid(Long.valueOf(warehouseid));
		// 订单导入URL
		lt.setRequest_url(request.getParameter("request_url"));
		// 物流回传url
		lt.setFeedback_url(request.getParameter("feedback_url"));
		// 每次查询大小
		lt.setMaxCount(Long.valueOf(request.getParameter("maxCount").isEmpty() ? "20" : request.getParameter("maxCount")));
		lt.setAppsecret(request.getParameter("appsecret"));
		lt.setAppkey(request.getParameter("appkey"));
		// java对象转换成jsonObject
		JSONObject jsonObj = JSONObject.fromObject(lt);
		// b2c共同的实体类
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);

		String oldCustomerids = "";
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			// b2c枚举key
			jointEntity.setJoint_num(joint_num);
			// json对象
			jointEntity.setJoint_property(jsonObj.toString());
			// 写入到express_set_joint
			this.jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = this.getLianTong(joint_num).getCustomerid();

			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		this.customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerids, joint_num);

	}

	public void update(int joint_num, int state) {
		this.jiontDAO.UpdateState(joint_num, state);
	}

	/**
	 * 调用订单导入方法
	 *
	 * @param content
	 * @param verifyCode
	 * @return
	 */
	public String invokeDealWithCwbOrderImport(String content, String verifyCode) {
		try {
			// 获取联通商城对象
			LianTongOrderCenter lianTongSC = this.getLianTong(B2cEnum.LianTongOrderCenter.getKey());
			// MD5加密
			String sign = SignUtils.getMd5Hex16Base64(content.trim(), lianTongSC.getPrivate_key());
			this.logger.info("联通签名={},本地签名={}", verifyCode, sign);
			// 校验MD5
			if (!verifyCode.equals(sign)) {
				String response = "<Response service=\"OrderService\"><Head>ERR</Head><Error >" + "签名错误" + "</Error></Response>";
				return response;
			}
			// 获取请求参数兑现
			RequestOrderDto requestDto = JAXBUtil.convertObject(RequestOrderDto.class, content);
			this.logger.info("联通商城请求参数=" + requestDto);
			// 校验参数为null
			if (null == requestDto) {
				String response = "<Response service=\"OrderService\"><Head>ERR</Head><Error >" + "参数为null" + "</Error></Response>";
				return response;
			}
			// 转换成list<Map>集合
			List<Map<String, String>> datalist = this.buildOrders(requestDto);
			// 校验订单重复
			if ((datalist == null) || (datalist.size() == 0)) {
				String response = "<Response service=\"OrderService\"><Head>ERR</Head><Error >" + "订单重复" + "</Error></Response>";
				return response;
			}
			// 写入到临时表
			this.dataImportInterface.Analizy_DataDealByB2c(Long.valueOf(lianTongSC.getCustomerid()), B2cEnum.LianTongOrderCenter.getMethod(), datalist, lianTongSC.getWarehouseid(), true);
			this.logger.info("处理联通商城导入后的订单信息成功,cwb={}", requestDto.getBodyDto().getOrder().getOrderId());
			// 返回联通商城的信息
			String response = "<Response service=\"OrderService\"><Head>OK</Head><Body><OrderResponse orderid=\"" + requestDto.getBodyDto().getOrder().getOrderId() + "\" mailno=\""
					+ requestDto.getBodyDto().getOrder().getOrderId() + "\" origincode=\"755\" destcode=\"010\" /></Body></Response>";
			return response;
		} catch (Exception e) {
			this.logger.error("content:" + content, e);
			String response = "<Response service=\"OrderService\"><Head>ERR</Head><Error >" + "未知异常" + "</Error></Response>";
			return response;
		}
	}

	private List<Map<String, String>> buildOrders(RequestOrderDto requestDto) {
		List<Map<String, String>> datalist = new ArrayList<Map<String, String>>();
		Map<String, String> orderMap = new HashMap<String, String>();
		String cwb = requestDto.getBodyDto().getOrder().getOrderId();
		if (!StringUtils.hasText(cwb)) {
			this.logger.warn("获取0联通商城0订单中cwb={}", cwb);
			return null;
		}
		CwbOrderDTO cwbOrderTmp = this.dataImportDAO_B2c.getCwbByCwbB2ctemp(cwb);
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		if ((null != cwbOrderTmp) || (null != cwbOrder)) {
			this.logger.warn("获取0联通商城0订单中含有重复数据cwb={}", cwb);
			return null;
		}
		String head = requestDto.getHead();// 接入码
		OrderDto orderDto = requestDto.getBodyDto().getOrder();
		String orderId = StringUtil.nullConvertToEmptyString(orderDto.getOrderId());// 客户订单号
		String mailNo = StringUtil.nullConvertToEmptyString(orderDto.getMailNo());// 物流公司运单号
		int payMethod = orderDto.getPayMethod();// 快递费付款方式：1：寄方付2：收方付
		int isGenGillNo = orderDto.getIsGenGillNo();// 是否要求返回运单号：1、要求 其它：不返回运单号
		String jCompany = StringUtil.nullConvertToEmptyString(orderDto.getjCompany());// 寄件方公司名称
		String jContact = StringUtil.nullConvertToEmptyString(orderDto.getjContact());// 寄件方联系人
		String jTel = StringUtil.nullConvertToEmptyString(orderDto.getjTel());// 寄件方联系电话
		String jMobile = StringUtil.nullConvertToEmptyString(orderDto.getjMobile());// 寄件方手机
		String jProvince = StringUtil.nullConvertToEmptyString(orderDto.getjProvince());// 寄件方所在省分
		// 字段要求：必须是标准的省名称称谓，如：南京市，直辖市，请直接填写：北京市、上海市、重庆市
		String jCity = StringUtil.nullConvertToEmptyString(orderDto.getjCity());// 寄方所在的城市名称标准称谓
		String jCounty = StringUtil.nullConvertToEmptyString(orderDto.getdCounty());// 寄方所在县/区标准称谓，示例：东城区
		String jAddress = StringUtil.nullConvertToEmptyString(orderDto.getjAddress());// 寄件方详细地址，如：广东省深圳市福田区新洲十一街万基商务大厦
																						// 10
																						// 楼
		String jPostCode = StringUtil.nullConvertToEmptyString(orderDto.getjPostCode());// 邮政编码
		String dCompany = StringUtil.nullConvertToEmptyString(orderDto.getdCompany());// 收件方公司
		String dContact = StringUtil.nullConvertToEmptyString(orderDto.getdContact());// 收件方联系人
		String dTel = StringUtil.nullConvertToEmptyString(orderDto.getdTel());// 收件方电话，当d_mobile不为空时，选填。
		String dMobile = StringUtil.nullConvertToEmptyString(orderDto.getdMobile());// 收件方手机，当d_tel不为空时，选填。
		String dProvince = StringUtil.nullConvertToEmptyString(orderDto.getdProvince());// 收件方所在省分
		// 字段要求：必须是标准的省名称称谓，如：南京市，直辖市，请直接填写：北京市、上海市、重庆市。
		String dCity = StringUtil.nullConvertToEmptyString(orderDto.getdCity());// 收件方所在的城市名称标准称谓。
		String dCounty = StringUtil.nullConvertToEmptyString(orderDto.getdCounty());// 收件所在县/区标准称谓，示例：东城区
		String dAddress = StringUtil.nullConvertToEmptyString(orderDto.getdAddress());// 收件方详细地址，如：广东省深圳市福田区新洲十一街万基商务大厦
																						// 10
																						// 楼
		String dPostCode = StringUtil.nullConvertToEmptyString(orderDto.getdPostCode());// 邮政编码
		String remark = StringUtil.nullConvertToEmptyString(orderDto.getRemark());// 备注
		// 详细地址=省+市+区+地址
		String RevAddress_detail = dProvince + dCity + dCounty + dAddress;

		String sendcarname = "";// 发货商品名称
		int sendcarnum = 0;// 发货数量
		double weight = 0;// 重量
		double cargoamount = 0;
		List<CargoDto> cargoDtos = orderDto.getCargoDtos();
		if (null != cargoDtos) {
			for (CargoDto cargoDto : cargoDtos) {
				sendcarname += cargoDto.getName() + ",";
				sendcarnum += Long.valueOf(cargoDto.getCount());
				weight += Long.valueOf(cargoDto.getWeight() == "" ? "0" : cargoDto.getWeight());
				cargoamount += Long.valueOf(cargoDto.getAmount() == "" ? "0" : cargoDto.getAmount());
			}
		}
		orderMap.put("cwb", cwb);
		orderMap.put("transcwb", orderDto.getMailNo() == null ? cwb : orderDto.getMailNo());
		orderMap.put("consigneename", dContact);
		orderMap.put("consigneepostcode", dPostCode);
		orderMap.put("consigneemobile", dMobile);
		orderMap.put("consigneephone", dTel);
		orderMap.put("consigneeaddress", RevAddress_detail);
		orderMap.put("cwbprovince", dProvince);
		orderMap.put("cwbcity", dCity);
		orderMap.put("cwbcounty", dCounty);
		orderMap.put("cargoamount", String.valueOf(cargoamount));
		orderMap.put("sendcarnum", "1");
		orderMap.put("sendcarname", sendcarname);
		orderMap.put("cargorealweight", String.valueOf(weight));
		orderMap.put("cwbremark", remark);
		orderMap.put("remark1", payMethod == 1 ? "寄方付" : "收方付");
		orderMap.put("remark2", "商品数量：" + sendcarnum);
		datalist.add(orderMap);
		return datalist;

	}

	/**
	 * 联通 订单取消 （只可取消入库前）
	 *
	 * @param jsonContent
	 * @param requestTime
	 * @param sign
	 * @return
	 */
	/**
	 * @param content
	 * @param verifyCode
	 * @return
	 */
	@SuppressWarnings("unused")
	public String cancelCwbOrder(String content, String verifyCode) {
		try {
			LianTongOrderCenter lianTongSC = this.getLianTong(B2cEnum.LianTongOrderCenter.getKey());
			String sign = SignUtils.getMd5Hex16Base64(content.trim(), lianTongSC.getPrivate_key());
			this.logger.info("联通签名={},本地签名={}", verifyCode, sign);
			if (!verifyCode.equals(sign)) {
				String response = "<Response service=\"OrderConfirmService\"><Head>ERR</Head><Error >" + "签名错误" + "</Error></Response>";
				return response;
			}
			RequestConfirmDto requestConfirmDto = JAXBUtil.convertObject(RequestConfirmDto.class, content);
			if (null == requestConfirmDto) {
				String response = "<Response service=\"OrderConfirmService\"><Head>ERR</Head><Error >" + "参数不能为空" + "</Error></Response>";
				return response;
			}
			if (!"1".equals(requestConfirmDto.getRequestConfirmDto().getOrderConfirm().getDealType()) && !"2".equals(requestConfirmDto.getRequestConfirmDto().getOrderConfirm().getDealType())) {
				String response = "<Response service=\"OrderConfirmService\"><Head>ERR</Head><Error >" + "操作标示异常，非退操作！" + "</Error></Response>";
				return response;
			}

			if ("1".equals(requestConfirmDto.getRequestConfirmDto().getOrderConfirm().getDealType())) {
				CwbOrderDTO cwbOrderTmp = this.dataImportDAO_B2c.getCwbByCwbB2ctemp(requestConfirmDto.getRequestConfirmDto().getOrderConfirm().getOrderId());
				if (null == cwbOrderTmp) {
					String response = "<Response service=\"OrderConfirmService\"><Head>ERR</Head><Error >" + "单号不存在" + "</Error></Response>";
					return response;
				}
				CwbOrder tempOrder = this.cwbDAO.getCwbByCwb(cwbOrderTmp.getCwb());
				if (null == tempOrder) {
					String response = "<Response service=\"OrderConfirmService\"><Head>ERR</Head><Error >" + "单号不存在" + "</Error></Response>";
					return response;
				}
				String response = "<Response service=\"OrderConfirmService\"><Head>OK</Head><Body><OrderConfirmResponse orderid=\"" + tempOrder.getCwb() + "\" mailno=\"" + tempOrder.getTranscwb()
						+ " \" res_status=\"2\" /></Body></Response>";
				return response;
			}
			CwbOrderDTO cwbOrderTmp = this.dataImportDAO_B2c.getCwbByCwbB2ctemp(requestConfirmDto.getRequestConfirmDto().getOrderConfirm().getOrderId());
			if (null == cwbOrderTmp) {
				String response = "<Response service=\"OrderConfirmService\"><Head>ERR</Head><Error >" + "单号不存在" + "</Error></Response>";
				return response;
			}
			CwbOrder tempOrder = this.cwbDAO.getCwbByCwb(cwbOrderTmp.getCwb());
			if (null == tempOrder) {
				String response = "<Response service=\"OrderConfirmService\"><Head>ERR</Head><Error >" + "单号不存在" + "</Error></Response>";
				return response;
			}
			List<FlowOrderTypeEnum> includeFlowTypes = new ArrayList<FlowOrderTypeEnum>();
			includeFlowTypes.add(FlowOrderTypeEnum.DaoRuShuJu);
			includeFlowTypes.add(FlowOrderTypeEnum.TiHuo);
			includeFlowTypes.add(FlowOrderTypeEnum.TiHuoYouHuoWuDan);

			if (tempOrder != null) {
				if (includeFlowTypes.contains(FlowOrderTypeEnum.getText(tempOrder.getFlowordertype()))) {
					this.dataImportDAO_B2c.dataLoseB2ctempByCwb(requestConfirmDto.getRequestConfirmDto().getOrderConfirm().getOrderId());
					this.cwbDAO.dataLoseByCwb(this.cwbOrderService.translateCwb(requestConfirmDto.getRequestConfirmDto().getOrderConfirm().getOrderId()));
					this.transCwbDao.deleteTransCwbByCwb(this.cwbOrderService.translateCwb(requestConfirmDto.getRequestConfirmDto().getOrderConfirm().getOrderId()));
				} else {
					String response = "<Response service=\"OrderConfirmService\"><Head>ERR</Head><Error >" + "已开始配送操作订单无法取消！" + "</Error></Response>";
					return response;
				}
			} else if (cwbOrderTmp != null) {
				this.dataImportDAO_B2c.dataLoseB2ctempByCwb(requestConfirmDto.getRequestConfirmDto().getOrderConfirm().getOrderId());
			}
			this.logger.info("处理联通商城订单取消成功,cwb={}", requestConfirmDto.getRequestConfirmDto().getOrderConfirm().getOrderId());
			String response = "<Response service=\"OrderConfirmService\"><Head>OK</Head><Body><OrderConfirmResponse orderid=\""
					+ requestConfirmDto.getRequestConfirmDto().getOrderConfirm().getOrderId() + "\" mailno=\"" + requestConfirmDto.getRequestConfirmDto().getOrderConfirm().getOrderId()
					+ " \" res_status=\"2\" /></Body></Response>";
			return response;
		} catch (Exception e) {
			this.logger.error("处理联通商城订单异常 ,content={}", content, e);
			String response = "<Response service=\"OrderConfirmService\"><Head>ERR</Head><Error >" + "未知异常" + "</Error></Response>";
			return response;
		}
	}

}
