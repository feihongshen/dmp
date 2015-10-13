package cn.explink.b2c.maisike;

/**
 * 发送二级站方法
 */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.xmldto.OrderFlowDto;
import cn.explink.b2c.maisike.feedback_json.OrderStatus;
import cn.explink.b2c.maisike.feedback_json.RespStatus;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.CommonSendDataDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.WarehouseCommenDAO;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class MaisikeService_Feedback {
	private Logger logger = LoggerFactory.getLogger(MaisikeService_Feedback.class);
	@Autowired
	private GetDmpDAO getDmpDAO;
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	WarehouseCommenDAO WarehouseCommenDAO;
	@Autowired
	CommonSendDataDAO commonSendDataDAO;
	@Autowired
	WarehouseCommenDAO warehouseCommenDAO;

	public Maisike getSmileSettingMethod(int key) {
		Maisike maisike = null;
		if (b2ctools.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(b2ctools.getObjectMethod(key));
			maisike = (Maisike) JSONObject.toBean(jsonObj, Maisike.class);
		} else {
			maisike = new Maisike();
		}
		return maisike;
	}

	/**
	 * 推送出库二级站信息
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public String receivedOrderFeedback(String fn, String appname, String apptime, String appkey, String appdata) throws Exception {
		RespStatus respStatus = new RespStatus();
		int userCode = B2cEnum.Maisike.getKey();
		if (!b2ctools.isB2cOpen(userCode)) {
			logger.info("未开出库【迈思可】的对接!");
			return "未开迈思可对接";
		}

		Maisike maisike = getSmileSettingMethod(userCode);

		String local_fn = "order.statuslist.get"; // 指定方法名 写死

		String local_sign = MD5Util.md5(appname + maisike.getApp_key() + apptime); // 签名
		try {
			validateParamsSecurity(fn, appkey, local_fn, local_sign); // 校验参数签名是否正确
		} catch (RuntimeException e) {
			logger.error("验证下游请求业务逻辑异常", e);
			respStatus.setCode(MaisikeExpEmum.DataValidateFailed.getErrCode());
			respStatus.setMsg(MaisikeExpEmum.DataValidateFailed.getErrMsg());
			return JacksonMapper.getInstance().writeValueAsString(respStatus);
		}

		OrderStatus orderStatus = JacksonMapper.getInstance().readValue(appdata, OrderStatus.class);
		String cwb = orderStatus.getOsn();

		long isexistscwbflag = warehouseCommenDAO.getCountByCwb(cwb);
		if (isexistscwbflag == 0) {
			respStatus.setCode(MaisikeExpEmum.DataValidateFailed.getErrCode());
			respStatus.setMsg("订单不存在");
			return JacksonMapper.getInstance().writeValueAsString(respStatus);
		}

		int flowordertype = getFlowordertype(orderStatus.getOstatus());
		long deliverystate = getDeliveryState(orderStatus.getOstatus());

		// 验证是否有重发订单插入
		long isrepeatFlag = commonSendDataDAO.isExistsCwbFlag(cwb, String.valueOf(userCode), orderStatus.getOtime(), String.valueOf(flowordertype));
		if (isrepeatFlag > 0) {
			respStatus.setCode(MaisikeExpEmum.DataValidateFailed.getErrCode());
			respStatus.setMsg("订单状态同一时间重复回传");
			return JacksonMapper.getInstance().writeValueAsString(respStatus);
		}

		OrderFlowDto orderFlowDto = buildOrderFlowDto(userCode, orderStatus, cwb, flowordertype, deliverystate); // 构建公共对接回传所需要的对象

		String jsonContent = JacksonMapper.getInstance().writeValueAsString(orderFlowDto);

		// 插入上游OMS临时表
		commonSendDataDAO.creCommenSendData(cwb, 0, String.valueOf(userCode), DateTimeUtil.getNowTime(), orderStatus.getOtime(), jsonContent, deliverystate, flowordertype, "0");

		// 自动补审核
		if (flowordertype == FlowOrderTypeEnum.YiFanKui.getValue()) {
			flowordertype = FlowOrderTypeEnum.YiShenHe.getValue();
			// 插入上游OMS临时表
			commonSendDataDAO.creCommenSendData(cwb, 0, String.valueOf(userCode), DateTimeUtil.getNowTime(), orderStatus.getOtime(), jsonContent, deliverystate, flowordertype, "0");
		}

		respStatus.setCode(MaisikeExpEmum.Success.getErrCode());
		respStatus.setMsg(MaisikeExpEmum.Success.getErrMsg());
		return JacksonMapper.getInstance().writeValueAsString(respStatus);

	}

	private OrderFlowDto buildOrderFlowDto(int userCode, OrderStatus orderStatus, String cwb, int flowordertype, long deliverystate) throws UnsupportedEncodingException {
		OrderFlowDto orderFlowDto = new OrderFlowDto();
		orderFlowDto.setCustid("0");
		orderFlowDto.setCwb(cwb);
		orderFlowDto.setDeliverystate(String.valueOf(deliverystate));
		orderFlowDto.setFloworderdetail(URLDecoder.decode(orderStatus.getOremark(), "UTF-8"));
		orderFlowDto.setFlowordertype(String.valueOf(flowordertype));
		orderFlowDto.setOperatortime(orderStatus.getOtime());
		orderFlowDto.setPaytype(translatePaytype(orderStatus)); // 支付方式 待定义
		orderFlowDto.setRequestTime(DateTimeUtil.getNowTime("yyyyMMddHHmmss"));
		orderFlowDto.setUserCode(String.valueOf(userCode));
		return orderFlowDto;
	}

	private int translatePaytype(OrderStatus orderStatus) {
		String orealpaytype = orderStatus.getOrealpaytype();
		for (MaisikePaytypeEmum enms : MaisikePaytypeEmum.values()) {
			if (enms.getCode().equals(orealpaytype)) {
				return enms.getPaytypeid();
			}
		}
		return 0;
	}

	private int getFlowordertype(String order_status) {
		// 入库
		if (order_status.equals(MaisikeStatusEmum.IN_STORE.getMskstatus()) || order_status.equals(MaisikeStatusEmum.PICKUP_IN_STORE.getMskstatus())) {
			return FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue();
		}
		// 领货派送中
		if (order_status.equals(MaisikeStatusEmum.OUT_DELIVERY.getMskstatus()) || order_status.equals(MaisikeStatusEmum.OUT_PICKUP.getMskstatus())) {
			return FlowOrderTypeEnum.FenZhanLingHuo.getValue();
		}
		// 成功
		if (order_status.equals(MaisikeStatusEmum.COMPLETE.getMskstatus()) || order_status.equals(MaisikeStatusEmum.BACKED.getMskstatus())
				|| order_status.equals(MaisikeStatusEmum.REPLACEED.getMskstatus())) {
			return FlowOrderTypeEnum.YiFanKui.getValue();
		}
		if (order_status.equals(MaisikeStatusEmum.RETURNED.getMskstatus()) || order_status.equals(MaisikeStatusEmum.DENY_BACKED.getMskstatus())) { // 拒收
			return FlowOrderTypeEnum.YiFanKui.getValue();
		}
		if (order_status.equals(MaisikeStatusEmum.OUT_BACKED.getMskstatus())) { // 退货出站
			return FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
		}

		return 0;
	}

	private long getDeliveryState(String order_status) {

		if (order_status.equals(MaisikeStatusEmum.COMPLETE.getMskstatus()) || order_status.equals(MaisikeStatusEmum.BACKED.getMskstatus())
				|| order_status.equals(MaisikeStatusEmum.REPLACEED.getMskstatus())) {
			return DeliveryStateEnum.PeiSongChengGong.getValue();
		}
		if (order_status.equals(MaisikeStatusEmum.RETURNED.getMskstatus()) || order_status.equals(MaisikeStatusEmum.DENY_BACKED.getMskstatus())) {
			return DeliveryStateEnum.QuanBuTuiHuo.getValue();
		}

		return 0;
	}

	private void validateParamsSecurity(String fn, String appkey, String local_fn, String local_sign) throws RuntimeException {
		if (!local_fn.equals(fn)) {
			throw new RuntimeException("请求参数fn=" + fn + "错误,local_fn=" + local_fn);
		}
		if (!local_sign.equalsIgnoreCase(appkey)) {
			// throw new RuntimeException("签名验证失败");
		}

	}

}
