package cn.explink.pos.etong;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cTools;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.User;
import cn.explink.enumutil.pos.PosEnum;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.util.WebServiceHandler;

import com.hyyt.elog.dmp.DmpElogService;
import com.hyyt.elog.dmp.ObjectFactory;
import com.hyyt.elog.dmp.OrderAttemperRequestType;
import com.hyyt.elog.dmp.OrderAttemperResponseType;
import com.hyyt.elog.dmp.OrderAttemperResult;
import com.hyyt.elog.dmp.OrderAttemperType;

/**
 * 手机App 实体类 version=物流E通（2.0平移过来）
 * 
 * @author Administrator
 * @user 恒宇运通
 */
@Service
public class EtongService {
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	GetDmpDAO getDmpDAO;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private ObjectFactory etFactory = EtongFactory.getInstance(); // 初始化推送物流E通手机端接口

	public void BuildDeliveryingServiceSendEtong(CwbOrderWithDeliveryState cwbOrderWithDeliveryState) {

		logger.info("执行了物流E通手机App接口");

		OrderAttemperRequestType requestType = etFactory.createOrderAttemperRequestType();

		DmpCwbOrder cwbOrder = cwbOrderWithDeliveryState.getCwbOrder();
		// DmpDeliveryState ds = cwbOrderWithDeliveryState.getDeliveryState();

		User deliverUser = getDmpDAO.getUserById(cwbOrder.getDeliverid());

		Etong etong = this.getEtong(PosEnum.MobileEtong.getKey());

		String logparms = "cwb=" + cwbOrder.getCwb() + ",consigneeaddress=" + cwbOrder.getConsigneeaddress() + ",receivablefee=" + cwbOrder.getReceivablefee() + ",consigneemobile="
				+ cwbOrder.getConsigneemobile() + ",sendcarname=" + cwbOrder.getSendcarname() + ",cwbremark=" + cwbOrder.getCwbremark() + ",customercommand=" + cwbOrder.getCustomercommand()
				+ ",usermobile=" + deliverUser.getUsermobile() + ",domain=" + etong.getExpress_id();

		logger.info("订单派件分配派送员推送物流E通={},parms={}", deliverUser.getRealname(), logparms);

		OrderAttemperType order = buildOrderAttemper(cwbOrder, deliverUser, etong.getExpress_id()); // 构建发送e通
																									// 的数据

		requestType.getOrderAttempers().add(order);

		EtongServiceClient lgs = new EtongServiceClient(etong.getSender_url(), 30000);

		OrderAttemperResponseType responseType = lgs.orderAttemper(requestType); // 返回的信息

		if (responseType.getErrorCount() == 0) {

			logger.info("物流E通返回信息,派件成功cwb=" + cwbOrder.getCwb() + ",usermobile=" + deliverUser.getUsermobile());

		} else {
			for (OrderAttemperResult orderResponse : responseType.getResultBody()) {
				// StatusCode就是错误的状态码，
				String statusCode = orderResponse.getStatusCode();
				// errorInfo是中文错误信息
				String errorInfo = orderResponse.getErrorInfo();
				String status = orderResponse.getStatus();
				String cwb = orderResponse.getCwb();

				String responseinfo = "电信e通返回[异常]statusCode=" + statusCode + ",cwb=" + cwb + ",status=" + status + ",errorInfo=" + errorInfo;
				logger.info(responseinfo);

			}
		}

	}

	private OrderAttemperType buildOrderAttemper(DmpCwbOrder cwbOrder, User deliverUser, String expressId) {
		OrderAttemperType order = etFactory.createOrderAttemperType();
		order.setCwb(cwbOrder.getCwb());
		order.setConsigneeAddress(cwbOrder.getConsigneeaddress());
		order.setConsigneeName(cwbOrder.getConsigneename());
		order.setReceivableFee(String.valueOf(cwbOrder.getReceivablefee()));
		order.setConsigneeMobile(cwbOrder.getConsigneemobile());
		order.setSendCargoName(cwbOrder.getSendcarname());
		order.setCwbRemark(cwbOrder.getCwbremark());
		order.setCustomerCommand(cwbOrder.getCustomercommand());
		order.setDeliver(deliverUser.getUsermobile()); // 派送员手机号码
		order.setDomain(expressId); // 电信分配的企业标识
		return order;
	}

	// 获取配置信息
	public Etong getEtong(int key) {
		Etong et = null;
		String objectMethod = b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			et = (Etong) JSONObject.toBean(jsonObj, Etong.class);
		} else {
			et = new Etong();
		}
		return et;
	}

}
