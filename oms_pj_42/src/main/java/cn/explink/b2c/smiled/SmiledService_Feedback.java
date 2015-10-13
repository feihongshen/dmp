package cn.explink.b2c.smiled;

/**
 * 发送二级站方法
 */
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.xmldto.OrderFlowDto;
import cn.explink.b2c.smiled.xmldto.SmiledOrderStatus;
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

@Service
public class SmiledService_Feedback {
	private Logger logger = LoggerFactory.getLogger(SmiledService_Feedback.class);
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

	public Smiled getSmileSettingMethod(int key) {
		Smiled Smiled = null;
		if (b2ctools.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(b2ctools.getObjectMethod(key));
			Smiled = (Smiled) JSONObject.toBean(jsonObj, Smiled.class);
		} else {
			Smiled = new Smiled();
		}
		return Smiled;
	}

	/**
	 * 接收思迈的状态反馈
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public String receivedOrderFeedback(String requestXml) {
		try {
			int userCode = B2cEnum.Smiled.getKey();
			if (!b2ctools.isB2cOpen(userCode)) {
				logger.info("未开【思迈下游反馈】的对接!");
				return getResponseXML("F");
			}

			Smiled Smiled = getSmileSettingMethod(userCode);

			SmiledOrderStatus orderstatus;

			orderstatus = SmiledUnmarchal.UnmarchalStatus(requestXml);

			long isexistscwbflag = warehouseCommenDAO.getCountByCwb(orderstatus.getWorkCode());
			if (isexistscwbflag == 0) {
				return getResponseXML("F");
			}

			int flowordertype = getFlowordertype(orderstatus.getOperationType());
			long deliverystate = getDeliveryState(orderstatus.getOperationType());

			// 验证是否有重发订单插入
			long isrepeatFlag = commonSendDataDAO.isExistsCwbFlag(orderstatus.getWorkCode(), String.valueOf(userCode), orderstatus.getOperationTime(), String.valueOf(flowordertype));
			if (isrepeatFlag > 0) {
				logger.info("同一时间不能回传同一个状态" + orderstatus.getWorkCode());
				return getResponseXML("F");
			}

			OrderFlowDto orderFlowDto = buildOrderFlowDto(userCode, orderstatus, flowordertype, deliverystate); // 构建公共对接回传所需要的对象

			String jsonContent = JacksonMapper.getInstance().writeValueAsString(orderFlowDto);

			// 插入上游OMS临时表
			commonSendDataDAO.creCommenSendData(orderstatus.getWorkCode(), 0, String.valueOf(userCode), DateTimeUtil.getNowTime(), orderstatus.getOperationTime(), jsonContent, deliverystate,
					flowordertype, "0");

			// 自动补审核
			if (flowordertype == FlowOrderTypeEnum.YiFanKui.getValue()) {
				flowordertype = FlowOrderTypeEnum.YiShenHe.getValue();
				// 插入上游OMS临时表
				commonSendDataDAO.creCommenSendData(orderstatus.getWorkCode(), 0, String.valueOf(userCode), DateTimeUtil.getNowTime(), orderstatus.getOperationTime(), jsonContent, deliverystate,
						flowordertype, "0");
			}

		} catch (Exception e) {
			logger.error("思迈下游状态回传未知异常" + requestXml, e);
		}
		return getResponseXML("T");

	}

	private OrderFlowDto buildOrderFlowDto(int userCode, SmiledOrderStatus orderstatus, int flowordertype, long deliverystate) throws UnsupportedEncodingException {
		OrderFlowDto orderFlowDto = new OrderFlowDto();
		orderFlowDto.setCustid("0");
		orderFlowDto.setCwb(orderstatus.getWorkCode());
		orderFlowDto.setDeliverystate(String.valueOf(deliverystate));
		orderFlowDto.setFloworderdetail(orderstatus.getRemark());
		orderFlowDto.setFlowordertype(String.valueOf(flowordertype));
		orderFlowDto.setOperatortime(orderstatus.getOperationTime());
		orderFlowDto.setPaytype(1); // 支付方式 待定义
		orderFlowDto.setRequestTime(DateTimeUtil.getNowTime("yyyyMMddHHmmss"));
		orderFlowDto.setUserCode(String.valueOf(userCode));
		return orderFlowDto;
	}

	private int getFlowordertype(String order_status) {
		// 入库
		if (order_status.equals(SmiledFlowEnum.RuKuSaoMiao.getSmile_code())) {
			return FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue();
		}
		// 领货派送中
		if (order_status.equals(SmiledFlowEnum.PaiJianSaoMiao.getSmile_code())) {
			return FlowOrderTypeEnum.FenZhanLingHuo.getValue();
		}
		// 签收
		if (order_status.equals(SmiledFlowEnum.QianShou.getSmile_code())) {
			return FlowOrderTypeEnum.YiFanKui.getValue();
		}
		// 异常
		if (order_status.equals(SmiledFlowEnum.YiChangLuRu.getSmile_code())) {
			return FlowOrderTypeEnum.YiFanKui.getValue();
		}
		// 拒收
		if (order_status.equals(SmiledFlowEnum.TuiHuoSaoMiao.getSmile_code())) {
			return FlowOrderTypeEnum.YiFanKui.getValue();
		}

		// 退货出站
		if (order_status.equals(SmiledFlowEnum.TuiHuoZhanRuKu.getSmile_code())) { // 退货出站
			return FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
		}

		return 0;
	}

	private long getDeliveryState(String order_status) {

		if (order_status.equals(SmiledFlowEnum.QianShou.getSmile_code())) {
			return DeliveryStateEnum.PeiSongChengGong.getValue();
		}
		if (order_status.equals(SmiledFlowEnum.YiChangLuRu.getSmile_code())) {
			return DeliveryStateEnum.FenZhanZhiLiu.getValue();
		}
		if (order_status.equals(SmiledFlowEnum.TuiHuoSaoMiao.getSmile_code())) {
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

	public static String getResponseXML(String is_success) {
		String xml = "<Response>" + "<IS_SUCCESS>" + is_success + "</IS_SUCCESS>" + "</Response>";

		return xml;
	}

}
