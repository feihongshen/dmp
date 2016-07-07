package cn.explink.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import net.sf.json.JSONObject;

import org.apache.camel.CamelContext;
import org.apache.camel.Header;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.MqExceptionBuilder.MessageSourceEnum;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.JsonUtil;

import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderService;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderServiceHelper;
import com.pjbest.deliveryorder.bizservice.PjUpdateOrderRequest;
import com.pjbest.deliveryorder.bizservice.RjUpdateOrderResponse;
import com.vip.osp.core.context.InvocationContext;
import com.vip.osp.core.exception.OspException;
/**
 * VIP上门退 订单绑定/解绑小件员同步到tps服务类  2016-06-20
 *
 */

@Service
public class CwbOrderDeliveryBindService {
	
	@Autowired
	private CamelContext camelContext;
	
	@Autowired
	CwbDAO cwbDAO;
	
	@Autowired
	CustomerDAO customerDao;
	
	@Autowired
	JiontDAO jiontDAO;
	
	@Autowired
	UserDAO userDao;
	
	@Autowired
	BranchDAO branchDao;
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	public static final int OSP_INVOKE_TIMEOUT = 60000;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String MQ_FROM_URI = "jms:queue:VirtualTopicConsumers.cwbbind.orderFlow";

	private List<Integer> flowList = new ArrayList<Integer>(); // 存储需要的环节
	
	@PostConstruct
	public void init() {
		try {

			this.flowList.add(FlowOrderTypeEnum.YiFanKui.getValue()); // 反馈
			this.camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					this.from(MQ_FROM_URI + "?concurrentConsumers=10").to("bean:cwbOrderDeliveryBindService?method=cwbDeliveryBind").routeId("cwbbind_");
				}
			});

		} catch (Exception e) {
			this.logger.error("订单绑定/解绑小件员同步到tps 监听JMS异常！" + e);
		}
	}
	
	public void cwbDeliveryBind (@Header("orderFlow") String parm, @Header("MessageHeaderUUID") String messageHeaderUUID) {
		try {
			doCwbDeliveryBind(parm);
		} catch (Exception e1) {
			this.logger.error("error while handle orderflow", e1);
			// 把未完成MQ插入到数据库中, start
			//消费MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".cwbDeliveryBind")
					.buildExceptionInfo(e1.toString()).buildTopic(MQ_FROM_URI)
					.buildMessageHeader("orderFlow", parm)
					.buildMessageHeaderUUID(messageHeaderUUID).buildMessageSource(MessageSourceEnum.receiver.getIndex()).getMqException());
			// 把未完成MQ插入到数据库中, end
		}
	}
	
	private void doCwbDeliveryBind(String parm) throws Exception{
		try {
			OrderFlow orderFlow = JsonUtil.readValue(parm, OrderFlow.class);
			int floworderType = orderFlow.getFlowordertype();
			if (!this.flowList.contains(floworderType)) {
				this.logger.warn("取消上门退订单绑定小件员，忽略订单操作状态"+orderFlow.getFlowordertype()+"，订单号："+orderFlow.getCwb());
				return;
			}
			CwbOrderWithDeliveryState cwbOrderWithDeliveryState = JsonUtil.readValue(orderFlow.getFloworderdetail(), CwbOrderWithDeliveryState.class);
			CwbOrder cwborder = cwbOrderWithDeliveryState.getCwbOrder();
			if (cwborder != null && cwborder.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) {
				if (DeliveryStateEnum.JuShou.getValue() == cwbOrderWithDeliveryState.getDeliveryState().getDeliverystate()
						|| DeliveryStateEnum.BuFenTuiHuo.getValue() == cwbOrderWithDeliveryState.getDeliveryState().getDeliverystate()) {
					if (getShipper_no(cwborder) != null && !getShipper_no(cwborder).isEmpty()) {
						List<CwbOrder> shangMenTuiCwbList = cwbDAO.queryRelatedShangMenTuiCwbList(cwborder.getCwb());
						if (shangMenTuiCwbList != null && !shangMenTuiCwbList.isEmpty()) {
							for (CwbOrder co : shangMenTuiCwbList) {
								if (co.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {//配送关联揽退单已领货
									logger.info("do 取消上门退订单绑定小件员 ，订单号：" + orderFlow.getCwb());
									PjUpdateOrderRequest req = new PjUpdateOrderRequest();
									
									req.setCustOrderNo(co.getCwb());
									req.setCarrierCode(getShipper_no(cwborder));
									req.setOrgCode(getTpsBranchCode(co.getDeliverybranchid()));
									req.setDistributer(null);//解绑
									req.setUpdateTime(System.currentTimeMillis());
									
									InvocationContext.Factory.getInstance().setTimeout(OSP_INVOKE_TIMEOUT);
									PjDeliveryOrderService service = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
									RjUpdateOrderResponse response = service.updateOrderInfo(req);
									if (response != null) {
										if (response.getIsSucess()) {
											logger.info("do 取消上门退订单绑定小件员 成功！ 订单号：" + response.getCustOrderNo());
										} else {
											logger.info("do 取消上门退订单绑定小件员 失败！ 订单号：" + response.getCustOrderNo()+ " 失败原因：" + response.getMsg());
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (OspException e) {
			logger.error("订单绑定/解绑小件员同步到tps 发生OSP异常", e);
		}
		
	}
	
	private String getShipper_no (CwbOrder cwborder) {
		String shipper_no = "";
		if (cwborder == null) {
			return shipper_no;
		}
		Customer customer = customerDao.getCustomerById(cwborder.getCustomerid());
		if (customer.getB2cEnum().equals(this.getB2cEnumKeys(customer, "vipshop"))) {
			JointEntity jiont = jiontDAO.getJointEntity(Integer.valueOf(customer.getB2cEnum()));
			if (jiont != null && jiont.getState() == 1) {
				VipShop vipshop = null;
				try {
					JSONObject jsonObj = JSONObject.fromObject(jiont.getJoint_property());
					vipshop = (VipShop) JSONObject.toBean(jsonObj, VipShop.class);
				} catch (Exception e) {
					vipshop = null;
				}
				if (vipshop != null) {
					shipper_no = vipshop.getShipper_no();
				}
			}
		}
		return shipper_no;
	}
	
	public String getB2cEnumKeys(Customer customer, String constainsStr) {
		for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains(constainsStr)) {
				if (customer.getB2cEnum().equals(String.valueOf(enums.getKey()))) {
					return String.valueOf(enums.getKey());
				}
			}
		}
		return null;
	}
	
	public String getTpsBranchCode (Long id) {
		Branch branch = branchDao.getBranchById(id);
		if (branch == null) {
			return "";
		}
		return branch.getTpsbranchcode();
	}
}
