package cn.explink.domain.express;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cn.explink.domain.User;
import cn.explink.domain.VO.express.ExpressPayImportParams;
import cn.explink.enumutil.express.ExpressEffectiveEnum;

/**
 * 
 * @author jiangyu 2015年8月20日
 *
 */
/*
CREATE TABLE `express_ops_freight_bill_detail_import` (
		  `id` INT(11) NOT NULL COMMENT '主键id',
		  `order_no` VARCHAR(100) NOT NULL  COMMENT '订单号',
		  `good_num` INT(11) DEFAULT 0 COMMENT '件数',
		  `collect_person` VARCHAR(100) DEFAULT NULL COMMENT '揽件人',
		  `delivery_person` VARCHAR(100) DEFAULT NULL COMMENT '配送人',
		  `sum_fee` DECIMAL(19,2) DEFAULT 0.00 COMMENT '费用合计（元）',
		  `delivery_fee` DECIMAL(19,2) DEFAULT 0.00 COMMENT '运费（元）',
		  `package_fee` DECIMAL(19,2) DEFAULT 0.00 COMMENT '包装费（元）',
		  `insured_fee` DECIMAL(19,2) DEFAULT 0.00 COMMENT '保价费用（元）',
		  `cod` DECIMAL(19,2) DEFAULT 0.00 COMMENT '代收货款（元）',
		  `delivery_branch_id` INT(11) DEFAULT 0 COMMENT '配送站点id',
		  `delivery_branch` VARCHAR(100) DEFAULT NULL COMMENT '配送站点',
		  `bill_id` INT(11) DEFAULT 0 COMMENT '账单id',
		  `bill_no` VARCHAR(100) DEFAULT NULL COMMENT '账单编号',
		  `effect_flag` TINYINT(1) DEFAULT 0 COMMENT '生效标识(0：无效，1：生效)',
		  `dismatch_reason` VARCHAR(100) DEFAULT NULL COMMENT '未匹配原因',
		  `import_person_id` INT(11) DEFAULT NULL COMMENT '导入人id',
		  `import_person` VARCHAR(100) DEFAULT NULL COMMENT '导入人',
		  `import_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '导入时间',
		  PRIMARY KEY (`id`),
		  KEY `bill_id_idx` (`bill_id`) USING BTREE,
		  KEY `bill_no_idx` (`bill_no`) USING BTREE
*/
public class ExpressFreightBillImportDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键id
	 */
	private Long id;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 件数
	 */
	private Integer goodNum;
	/**
	 * 揽件人
	 */
	private String collectPerson;
	/**
	 * 配送人
	 */
	private String deliveryPerson;
	/**
	 * 费用合计（元）
	 */
	private BigDecimal sumFee;
	/**
	 * 运费（元）
	 */
	private BigDecimal deliveryFee;
	/**
	 * 包装费（元）
	 */
	private BigDecimal packageFee;
	/**
	 * 保价费用（元）
	 */
	private BigDecimal insuredFee;
	/**
	 * 代收货款（元）
	 */
	private BigDecimal cod;
	/**
	 * 配送站点id
	 */
	private Long deliveryBranchId;
	/**
	 * 配送站点
	 */
	private String deliveryBranch;
	/**
	 * 账单id
	 */
	private Long billId;
	/**
	 * 账单编号
	 */
	private String billNo;
	/**
	 * 生效标识(0：无效，1：生效)
	 */
	private Integer effectFlag;
	/**
	 * 未匹配原因
	 */
	private String dismatchReason;
	/**
	 * 导入人id
	 */
	private Long importPersonId;
	/**
	 * 导入人
	 */
	private String importPerson;
	/**
	 * 导入时间
	 */
	private Date importTime;
	
	public ExpressFreightBillImportDetail() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getGoodNum() {
		return goodNum;
	}

	public void setGoodNum(Integer goodNum) {
		this.goodNum = goodNum;
	}

	public String getCollectPerson() {
		return collectPerson;
	}

	public void setCollectPerson(String collectPerson) {
		this.collectPerson = collectPerson;
	}

	public String getDeliveryPerson() {
		return deliveryPerson;
	}

	public void setDeliveryPerson(String deliveryPerson) {
		this.deliveryPerson = deliveryPerson;
	}

	public BigDecimal getSumFee() {
		return sumFee;
	}

	public void setSumFee(BigDecimal sumFee) {
		this.sumFee = sumFee;
	}

	public BigDecimal getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(BigDecimal deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public BigDecimal getPackageFee() {
		return packageFee;
	}

	public void setPackageFee(BigDecimal packageFee) {
		this.packageFee = packageFee;
	}

	public BigDecimal getInsuredFee() {
		return insuredFee;
	}

	public void setInsuredFee(BigDecimal insuredFee) {
		this.insuredFee = insuredFee;
	}

	public BigDecimal getCod() {
		return cod;
	}

	public void setCod(BigDecimal cod) {
		this.cod = cod;
	}

	public Long getDeliveryBranchId() {
		return deliveryBranchId;
	}

	public void setDeliveryBranchId(Long deliveryBranchId) {
		this.deliveryBranchId = deliveryBranchId;
	}

	public String getDeliveryBranch() {
		return deliveryBranch;
	}

	public void setDeliveryBranch(String deliveryBranch) {
		this.deliveryBranch = deliveryBranch;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Integer getEffectFlag() {
		return effectFlag;
	}

	public void setEffectFlag(Integer effectFlag) {
		this.effectFlag = effectFlag;
	}

	public String getDismatchReason() {
		return dismatchReason;
	}

	public void setDismatchReason(String dismatchReason) {
		this.dismatchReason = dismatchReason;
	}

	public Long getImportPersonId() {
		return importPersonId;
	}

	public void setImportPersonId(Long importPersonId) {
		this.importPersonId = importPersonId;
	}

	public String getImportPerson() {
		return importPerson;
	}

	public void setImportPerson(String importPerson) {
		this.importPerson = importPerson;
	}

	public Date getImportTime() {
		return importTime;
	}

	public void setImportTime(Date importTime) {
		this.importTime = importTime;
	}

	public ExpressFreightBillImportDetail(ExpressPayImportParams params) {
		super();
		User user = params.getUser();
		this.billId = params.getBillId();
		this.billNo = params.getBillNo();
		this.effectFlag = ExpressEffectiveEnum.UnEffective.getValue();
		this.importPersonId = user.getUserid();
		this.importPerson = user.getRealname();
		this.importTime = params.getDate();
	}
	
}
