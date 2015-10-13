package cn.explink.b2c.gztl;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.lefeng.BuildLefengB2cData;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.CacheBaseListener;
import cn.explink.dao.CommonSendDataDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BuildGztlB2cData {
	private Logger logger = LoggerFactory.getLogger(BuildLefengB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;

	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	GztlService gztlService;
	@Autowired
	CommonSendDataDAO commonSendDataDAO;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	CacheBaseListener cacheBaseListener;
	private static final String PEISONG = "正常配送";
	private static final String HUANHUO = "换货";
	private static final String WEITUOQUJIAN = "委托取件";

	/**
	 * 根据在本系统中的订单状态转化为广州通路所需要的xml字符串对应的GztlXmlNote
	 *
	 * @param orderFlow
	 * @param flowOrdertype
	 * @param cwbOrder
	 * @param delivery_state
	 * @param dmpDeliveryState
	 * @param objectMapper
	 * @return
	 * @throws IOException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 */
	public String buildGztlMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, DmpDeliveryState dmpDeliveryState,Customer customer, ObjectMapper objectMapper)
			throws IOException, JsonGenerationException, JsonMappingException {
		
		GztlEnum cmstate = this.gztlService.filterFlowState(delivery_state, cwbOrder, flowOrdertype, delivery_state, Integer.parseInt(cwbOrder.getCwbordertypeid())) == null ? null : this.gztlService
				.filterFlowState(delivery_state, cwbOrder, flowOrdertype, delivery_state, Integer.parseInt(cwbOrder.getCwbordertypeid()));

		if (cmstate == null) {
			this.logger.warn("该流程不属于广州通路推送流程cwb={},deliverystate={}", cwbOrder.getCwb(), delivery_state);
			return null;
		}
		this.logger.info("订单号：{}封装成0广州通路0所需要的json----开始,状态：{}", cwbOrder.getCwb(), flowOrdertype);

		GztlXmlNote gztlXmlNote = new GztlXmlNote();
		
		gztlXmlNote.setId(cwbOrder.getOpscwbid() + "");// ??序列号，用于接收成功后返回标识
		gztlXmlNote.setLogisticid(cwbOrder.getCwb());// 订单号
		gztlXmlNote.setMyNo(cwbOrder.getCwb());// 运单编号
		gztlXmlNote.setCustorderno(gztlService.removezero(cwbOrder.getRemark1()));// 客户订单号
		// gztlXmlNote.setOpType(cmstate.getText());// ??反馈类型(由飞远提供)
		gztlXmlNote.setOpType(cmstate.getOptype());
		// gztlXmlNote.setState(cmstate.getText());// ??订单状态(由飞远提供)
		gztlXmlNote.setState(cmstate.getState());
		// gztlXmlNote.setReturnState(cmstate.getText());// ??网点反馈状态(由飞远提供)
		gztlXmlNote.setReturnState(cmstate.getReturnState());
		gztlXmlNote.setReturnCause(cmstate.getReturnMsg());// ??网点反馈原因(由飞远提供)?????????
		gztlXmlNote.setReturnRemark(cmstate.getReturnMsg());// ??网点反馈备注(由飞远提供)????????
		gztlXmlNote.setLspabbr(cwbOrder.getRemark3());// 可以为空,配送区域
		String sign_man = "";
		if (dmpDeliveryState != null) {
			sign_man = (dmpDeliveryState.getSign_man() == null) || dmpDeliveryState.getSign_man().isEmpty() ? cwbOrder.getConsigneenameOfkf() : dmpDeliveryState.getSign_man();
			
		}
		
		gztlXmlNote.setSignname(sign_man);// 签收人
		gztlXmlNote.setOpDt(DateTimeUtil.formatDateLong(orderFlow.getCredate(), "yyyy-MM-dd HH:mm:ss"));// ??网点反馈时间/签收时间/导入时间/出入库时间

		User user = this.cacheBaseListener.getUser(orderFlow.getUserid());

		gztlXmlNote.setEmp(user.getRealname());// ???网点反馈用户
		gztlXmlNote.setUnit(this.cacheBaseListener.getBranch(orderFlow.getBranchid()).getBranchname());// ??反馈网点名称

		if (flowOrdertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			User deliveryUser = this.cacheBaseListener.getUser(dmpDeliveryState.getDeliveryid());
			gztlXmlNote.setEmpSend(deliveryUser.getRealname());// ??派件员/反馈人/导入信息人员/出入库人
		} else {
			gztlXmlNote.setEmpSend(user.getRealname());// ??派件员/反馈人/导入信息人员/出入库人
		}

		gztlXmlNote.setReturnStatedesc(this.orderFlowDetail.getDetail(orderFlow));// 订单状态详情
		
		CuscodeAndCustomerNameEnum	customerandCode = getCustomerEnum(customer);
		
		gztlXmlNote.setCuscode(customerandCode.getCuscode());// ??供货商代码(由飞远提供)
		gztlXmlNote.setReceiverName(cwbOrder.getConsigneenameOfkf());// 收件人姓名
		String consigneephone = "";
		if (!cwbOrder.getConsigneemobileOfkf().isEmpty() && !cwbOrder.getConsigneephoneOfkf().isEmpty()) {
			consigneephone = gztlService.removezero(cwbOrder.getConsigneemobileOfkf() + "," + cwbOrder.getConsigneephoneOfkf());
		} else if (!cwbOrder.getConsigneephoneOfkf().isEmpty()) {
			consigneephone = gztlService.removezero(cwbOrder.getConsigneephoneOfkf());
		} else {
			consigneephone = gztlService.removezero(cwbOrder.getConsigneemobileOfkf());
		}
		gztlXmlNote.setReceiverMobile(consigneephone);// 收件人电话

		gztlXmlNote.setCustomername(customerandCode.getCustomerName());// 供货商
		gztlXmlNote.setSenderName("");// 可以为空，寄件人
		gztlXmlNote.setSenderMobile("");// 可以为空，寄件人手机
		String receiveable="";
		if (cwbOrder.getReceivablefee().toString().equals("0.00")) {
			receiveable=cwbOrder.getPaybackfee().toString();
		}else {
			receiveable=cwbOrder.getReceivablefee().toString();
		}
		gztlXmlNote.setPayinamount(receiveable);// 代收货款
		gztlXmlNote.setArrivedate(cwbOrder.getEmaildate());// ??最初扫描时间
		//gztlXmlNote.setLspabbr("");// 可以为空,配送区域
		gztlXmlNote.setPcs(String.valueOf(cwbOrder.getSendcarnum()==0?cwbOrder.getBackcarnum():cwbOrder.getSendcarnum()));
		String cwbOrderType = "";
		if (cwbOrder.getCwbordertypeid().equals("1")) {
			cwbOrderType = BuildGztlB2cData.PEISONG;
		} else if (cwbOrder.getCwbordertypeid().equals("2")) {
			cwbOrderType = BuildGztlB2cData.WEITUOQUJIAN;
		} else {
			cwbOrderType = BuildGztlB2cData.HUANHUO;
		}
		gztlXmlNote.setBusiness(cwbOrderType);
		// DateTimeUtil.formatDate(orderFlow.getCredate())
		// lefengXmlNote.setTime(DateTimeUtil.formatDate(orderFlow.getCredate(),
		// "yyyy-MM-dd'T'HH:mm:ss"));
		this.logger.info("订单号：{}封装成0广州通路0所需要的json----结束,状态：{}", cwbOrder.getCwb(), cwbOrder.getFlowordertype());
		return objectMapper.writeValueAsString(gztlXmlNote);

	}
	/**
	 * private String id;// 序列号，用于接收成功后返回标识 private String myNo;// 运单编号 private
	 * String logisticid;// 订单号 private String custorderno;// 客户订单号 private
	 * String opType;// 反馈类型(由飞远提供) private String state;// 订单状态(由飞远提供) private
	 * String returnState;// 网点反馈状态(由飞远提供) private String returnCause;//
	 * 网点反馈原因(由飞远提供) private String returnRemark;// 网点反馈备注(由飞远提供) private String
	 * signname;// 签收人 private String opDt;// 网点反馈时间/签收时间/导入时间/出入库时间 private
	 * String emp;// 网点反馈用户 private String unit;// 反馈网点名称 private String
	 * empSend;// 派件员/反馈人/导入信息人员/出入库人 private String returnStatedesc;// 订单状态详情
	 * private String cuscode;// 供货商代码(由飞远提供) private String receiverName;// 收件人
	 * private String receiverMobile;// 收件人电话 private String customername;//
	 * 供货商(由飞远提供) private String senderName;// 寄件人?? private String
	 * senderMobile;// 寄件手机?? private String payinamount;// 代收货款 private String
	 * arrivedate;// 最初扫描时间 private String lspabbr;// 配送区域
	 */

	private CuscodeAndCustomerNameEnum getCustomerEnum(Customer customer) {
		for (CuscodeAndCustomerNameEnum element : CuscodeAndCustomerNameEnum.values()) {
			if (element.getCuscode().equals(customer.getCustomercode())) {
				return element;
			}

		}
		return null;
	}
}
