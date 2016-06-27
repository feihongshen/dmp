package cn.explink.domain.express;

import java.util.List;
import java.util.Map;

import cn.explink.enumutil.express.ExpressOperationEnum;

import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderRequest;
import com.pjbest.deliveryorder.dcpackage.service.PjDcPackageModel;
import com.pjbest.deliveryorder.dcpackage.service.PjUnPackRequest;
import com.pjbest.deliveryorder.service.PjSaleOrderFeedbackRequest;
import com.pjbest.deliveryorder.service.PjTransportFeedbackRequest;

/**
 * 调用TPS接口的公共的信息存储实体
 * 
 * @author jiangyu 2015年9月7日
 *
 */
public class ExpressOperationInfo {
	/**
	 * 反馈操作的接口传递的参数
	 */
	private PjSaleOrderFeedbackRequest preOrderfeedBack;
	/**
	 * 预订单号【查询预约单轨迹接口】
	 */
	private String reserveOrderNo;
	/**
	 * 创建运单接口
	 */
	private List<PjDeliveryOrderRequest> requestlist;
	/**
	 * 运单状态反馈结果 -------LX
	 */
	private PjTransportFeedbackRequest transNoFeedBack;
	/**
	 * 运单轨迹查询接口[运单号]
	 */
	private String transNo;
	/**
	 * 上传打包信息接口
	 */
	private PjDcPackageModel packModel;
	/**
	 * 上传拆包信息接口
	 */
	private PjUnPackRequest unPackRequest;
	/**
	 * 检测运单是否属于包接口[包号]
	 */
	private String packageNo;
	/**
	 * 落地配抓取预约单/运单反馈接口[单号集合]
	 */
	private List<String> listStrNo;
	/**
	 * 运单号或包号规则代码
	 */
	private String seqRuleNo;
	/**
	 * 运单号或包号规则参数
	 */
	private Map<String, String> contextVars;

	/**
	 * 对应的业务操作类型枚举
	 */
	private Integer operationType;
	/**
	 * 承运商编号
	 */
	private String carrierCode;
	/**
	 * 运单id集合
	 */
	private List<String> transNoIdList;
	/**
	 * 订单的操作状态【对应订单操作环节的枚举】
	 */
	private Long flowOrderType = 0L;
	/**
	 * 备注
	 */
	private String remark;

	public ExpressOperationInfo() {
		// TODO Auto-generated constructor stub
	}

	public ExpressOperationInfo(ExpressOperationEnum operation) {
		super();
		this.operationType = operation.getValue();
	}

	public PjSaleOrderFeedbackRequest getPreOrderfeedBack() {
		return preOrderfeedBack;
	}

	public void setPreOrderfeedBack(PjSaleOrderFeedbackRequest preOrderfeedBack) {
		this.preOrderfeedBack = preOrderfeedBack;
	}

	public String getReserveOrderNo() {
		return reserveOrderNo;
	}

	public void setReserveOrderNo(String reserveOrderNo) {
		this.reserveOrderNo = reserveOrderNo;
	}

	public List<PjDeliveryOrderRequest> getRequestlist() {
		return requestlist;
	}

	public void setRequestlist(List<PjDeliveryOrderRequest> requestlist) {
		this.requestlist = requestlist;
	}

	public PjTransportFeedbackRequest getTransNoFeedBack() {
		return transNoFeedBack;
	}

	public void setTransNoFeedBack(PjTransportFeedbackRequest transNoFeedBack) {
		this.transNoFeedBack = transNoFeedBack;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public PjDcPackageModel getPackModel() {
		return packModel;
	}

	public void setPackModel(PjDcPackageModel packModel) {
		this.packModel = packModel;
	}

	public PjUnPackRequest getUnPackRequest() {
		return unPackRequest;
	}

	public void setUnPackRequest(PjUnPackRequest unPackRequest) {
		this.unPackRequest = unPackRequest;
	}

	public String getPackageNo() {
		return packageNo;
	}

	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}

	public List<String> getListStrNo() {
		return listStrNo;
	}

	public void setListStrNo(List<String> listStrNo) {
		this.listStrNo = listStrNo;
	}

	public String getSeqRuleNo() {
		return seqRuleNo;
	}

	public void setSeqRuleNo(String seqRuleNo) {
		this.seqRuleNo = seqRuleNo;
	}

	public Map<String, String> getContextVars() {
		return contextVars;
	}

	public void setContextVars(Map<String, String> contextVars) {
		this.contextVars = contextVars;
	}

	public Integer getOperationType() {
		return operationType;
	}

	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public List<String> getTransNoIdList() {
		return transNoIdList;
	}

	public void setTransNoIdList(List<String> transNoIdList) {
		this.transNoIdList = transNoIdList;
	}

	public Long getFlowOrderType() {
		return flowOrderType;
	}

	public void setFlowOrderType(Long flowOrderType) {
		this.flowOrderType = flowOrderType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
