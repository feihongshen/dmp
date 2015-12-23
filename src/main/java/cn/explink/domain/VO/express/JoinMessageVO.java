package cn.explink.domain.VO.express;

import cn.explink.service.express.tps.enums.FeedbackOperateTypeEnum;

public class JoinMessageVO {
	/*
	 * 时间
	 */
	private String operateTime;
	/*
	 * 轨迹信息
	 */
	private String transportDetail;
	/*
	 * 运单号
	 */
	private String transportNo;
	/*
	 * 箱号
	 */
	private String boxNo;
	/*
	 * 操作类型(通过枚举对应TPS业务名)
	 */
	private int operateType;
	/*
	 * 操作员
	 */
	private String Operator;
	/*
	 * 站点
	 */
	private String operateOrg;
	/*
	 * 站点code
	 */
	private String operateOrgCode;
	/*
	 * 站点名字
	 */
	private String operateOrgName;
	/*
	 *
	 */
	private String carrierCode;
	/*
	 *
	 */
	private String carrierName;
	/*
	 *快递员
	 */
	private String courier;
	/*
	 *快递员电话
	 */
	private String courierTel;
	/*
	 *异常原因
	 */
	private String exceptionReason;
	/*
	 *下一站code
	 */
	private String nextOrg;
	/*
	 *下一站名字
	 */
	private String nextOrgName;
	/*
	 *签收人
	 */
	private String signMan;
	/*
	 *描述
	 */
	private String description;

	public String getOperateTime() {
		return this.operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

	public String getTransportDetail() {
		return this.transportDetail;
	}

	public void setTransportDetail(String transportDetail) {
		this.transportDetail = transportDetail;
	}

	public String getTransportNo() {
		return this.transportNo;
	}

	public void setTransportNo(String transportNo) {
		this.transportNo = transportNo;
	}

	public String getBoxNo() {
		return this.boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public int getOperateType() {
		return this.operateType;
	}

	public void setOperateType(int operateType) {
		this.operateType = operateType;
	}

	public String getOperator() {
		return this.Operator;
	}

	public void setOperator(String operator) {
		this.Operator = operator;
	}

	public String getOperateOrg() {
		return this.operateOrg;
	}

	public void setOperateOrg(String operateOrg) {
		this.operateOrg = operateOrg;
	}

	public String getOperateOrgCode() {
		return this.operateOrgCode;
	}

	public void setOperateOrgCode(String operateOrgCode) {
		this.operateOrgCode = operateOrgCode;
	}

	public String getOperateOrgName() {
		return this.operateOrgName;
	}

	public void setOperateOrgName(String operateOrgName) {
		this.operateOrgName = operateOrgName;
	}

	public String getCarrierCode() {
		return this.carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public String getCarrierName() {
		return this.carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getCourier() {
		return this.courier;
	}

	public void setCourier(String courier) {
		this.courier = courier;
	}

	public String getcourierTel() {
		return this.courierTel;
	}

	public void setcourierTel(String courierTel) {
		this.courierTel = courierTel;
	}

	public String getExceptionReason() {
		return this.exceptionReason;
	}

	public void setExceptionReason(String exceptionReason) {
		this.exceptionReason = exceptionReason;
	}

	public String getNextOrg() {
		return this.nextOrg;
	}

	public void setNextOrg(String nextOrg) {
		this.nextOrg = nextOrg;
	}

	public String getNextOrgName() {
		return this.nextOrgName;
	}

	public void setNextOrgName(String nextOrgName) {
		this.nextOrgName = nextOrgName;
	}

	public String getSignMan() {
		return this.signMan;
	}

	public void setSignMan(String signMan) {
		this.signMan = signMan;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 *
	 * @Title: connectMessage
	 * @description 根据轨迹返回的信息，拼接轨迹详情，并使该详情带有前台样式（不用获取到的轨迹详情）
	 * @author 刘武强
	 * @date  2015年10月28日上午9:46:42
	 * @param
	 * @return  void
	 * @throws
	 */
	public void connectMessage() {
		StringBuffer temp = new StringBuffer();
		if (((FeedbackOperateTypeEnum.RegisteScan.getValue()) == this.operateType) || ((FeedbackOperateTypeEnum.InboundScan.getValue()) == this.operateType) || ((FeedbackOperateTypeEnum.PackingScan
				.getValue()) == this.operateType)) {
			//【芳村一站】揽件扫描，操作员  张三1---揽件反馈
			temp.append("【<span style=\"color:red;\" onmouseover=\"showTitle(this)\" id=\"span_").append(this.operateOrgCode).append("_").append(this.carrierCode).append("_")
					.append((int) (Math.random() * 100)).append("\"> ").append(this.operateOrgName).append("</span>】").append("<span style=\"color:red;\">")
					.append(FeedbackOperateTypeEnum.getTextByValue(this.operateType)).append("</span>,").append("操作员").append("<span style=\"color:red;\">").append(this.Operator).append("</span>");
		} else if (((FeedbackOperateTypeEnum.OutboundScan.getValue()) == this.operateType) || ((FeedbackOperateTypeEnum.TransferScan.getValue()) == this.operateType)) {
			//【芳村一站】出站扫描，下一站广州分拨中心，操作员 王**
			temp.append("【<span style=\"color:red;\" onmouseover=\"showTitle(this)\" id=\"span_").append(this.operateOrgCode).append("_").append(this.carrierCode).append("_")
					.append((int) (Math.random() * 100)).append("\"> ").append(this.operateOrgName).append("</span>】").append("<span style=\"color:red;\">")
					.append(FeedbackOperateTypeEnum.getTextByValue(this.operateType)).append("</span>").append(",下一站").append("<span style=\"color:red;\">").append(this.nextOrgName).append("</span>")
					.append(",").append("操作员:").append("<span style=\"color:red;\">").append(this.Operator).append("</span>");
		} else if ((FeedbackOperateTypeEnum.DeliveryScan.getValue()) == this.operateType) {
			//【芳村二站】派件中，快递员：陈** 电话：13482345678
			temp.append("【<span style=\"color:red;\" onmouseover=\"showTitle(this)\" id=\"span_").append(this.operateOrgCode).append("_").append(this.carrierCode).append("_")
					.append((int) (Math.random() * 100)).append("\"> ").append(this.operateOrgName).append("</span>】派件中，快递员：<span style=\"color:red;\">").append(this.courier).append("</span>")
					.append("电话：").append("<span style=\"color:red;\">").append(this.courierTel).append("</span>");
		} else if ((FeedbackOperateTypeEnum.SignInScan.getValue()) == this.operateType) {
			//【芳村二站】签收扫描，签收人：本人签收  操作员：陈**
			temp.append("【<span style=\"color:red;\" onmouseover=\"showTitle(this)\" id=\"span_").append(this.operateOrgCode).append("_").append(this.carrierCode).append("_")
					.append((int) (Math.random() * 100)).append("\"> ").append(this.operateOrgName).append("</span>】").append("<span style=\"color:red;\">")
					.append(FeedbackOperateTypeEnum.getTextByValue(this.operateType)).append("</span>").append("，签收人：").append("<span style=\"color:red;\">").append(this.signMan).append("</span>")
					.append(" 操作员:").append("<span style=\"color:red;\">").append(this.Operator).append("</span>");
		} else if ((FeedbackOperateTypeEnum.RetensionScan.getValue()) == this.operateType) {
			//【芳村二站】滞留扫描，原因：收件人不在家  操作员：陈**
			temp.append("【<span style=\"color:red;\" onmouseover=\"showTitle(this)\" id=\"span_").append(this.operateOrgCode).append("_").append(this.carrierCode).append("_")
					.append((int) (Math.random() * 100)).append("\"> ").append(this.operateOrgName).append("</span>】").append("<span style=\"color:red;\">")
					.append(FeedbackOperateTypeEnum.getTextByValue(this.operateType)).append("</span>").append("，原因：").append("<span style=\"color:red;\">").append(this.exceptionReason)
					.append("</span>").append(" 操作员:").append("<span style=\"color:red;\">").append(this.Operator).append("</span>");
		} else if ((FeedbackOperateTypeEnum.ExceptionScan.getValue()) == this.operateType) {
			if ("".equals(this.transportDetail)) {
				temp.append("");
			} else if (!"".equals(this.transportDetail)) {
				//【芳村二站】反馈为丢失，  原因：货物丢失  操作员：陈**
				temp.append("【<span style=\"color:red;\" onmouseover=\"showTitle(this)\" id=\"span_").append(this.operateOrgCode).append("_").append(this.carrierCode).append("_")
						.append((int) (Math.random() * 100)).append("\"> ").append(this.operateOrgName).append("</span>】").append("反馈为丢失，原因：").append("<span style=\"color:red;\">")
						.append(this.exceptionReason).append("</span>").append(" 操作员:").append("<span style=\"color:red;\">").append(this.Operator).append("</span>");
				;
			}
		} else if ((FeedbackOperateTypeEnum.JuShou.getValue()) == this.operateType) {
			//【芳村二站】拒收扫描，原因：收件人 说不要了  操作人：陈**
			temp.append("【<span style=\"color:red;\" onmouseover=\"showTitle(this)\" id=\"span_").append(this.operateOrgCode).append("_").append(this.carrierCode).append("_")
					.append((int) (Math.random() * 100)).append("\"> ").append(this.operateOrgName).append("</span>】").append("拒收扫描，原因：").append("<span style=\"color:red;\">")
					.append(this.exceptionReason).append("</span>").append(" 操作员:").append("<span style=\"color:red;\">").append(this.Operator).append("</span>");
			;
		}
		this.description = temp.toString();
	}

	/**
	 *
	 * @Title: replaceDetail
	 * @description 根据获取的轨迹详情，替换里面的某些字段，从而使其有样式
	 * @author 刘武强
	 * @date  2015年10月28日上午9:49:49
	 * @param
	 * @return  void
	 * @throws
	 */
	public void replaceDetail() {
		StringBuffer temp = new StringBuffer();
		if ((this.transportDetail != null) && !this.transportDetail.isEmpty()) {
			if ((this.operateOrgName != null) && !this.operateOrgName.isEmpty() && this.transportDetail.contains(this.operateOrgName)) {
				this.transportDetail = this.transportDetail.replace(this.operateOrgName.trim(), temp.append("<span style=\"color:red;\" onmouseover=\"showTitle(this)\" id=\"span_")
						.append(this.operateOrgCode).append("_").append(this.carrierCode).append("_").append((int) (Math.random() * 100)).append("\"> ").append(this.operateOrgName.trim())
						.append("</span>").toString());
			}
			temp.setLength(0);
			if (this.transportDetail.contains(FeedbackOperateTypeEnum.getTextByValue(this.operateType))) {
				this.transportDetail = this.transportDetail.replace(FeedbackOperateTypeEnum.getTextByValue(this.operateType), temp.append("<span style=\"color:red;\">")
						.append(FeedbackOperateTypeEnum.getTextByValue(this.operateType)).append("</span>").toString());
			}
			temp.setLength(0);
			if ((this.Operator != null) && !this.Operator.isEmpty() && this.transportDetail.contains(this.Operator)) {
				this.transportDetail = this.transportDetail.replace(this.Operator.trim(), temp.append("<span style=\"color:red;\">").append(this.Operator.trim()).append("</span>").toString());
			}
			temp.setLength(0);
			if ((this.nextOrgName != null) && !this.nextOrgName.isEmpty() && this.transportDetail.contains(this.nextOrgName)) {
				this.transportDetail = this.transportDetail.replace(this.nextOrgName.trim(), temp.append("<span style=\"color:red;\">").append(this.nextOrgName.trim()).append("</span>").toString());
			}
			temp.setLength(0);
			if ((this.courier != null) && !this.courier.isEmpty() && this.transportDetail.contains(this.courier)) {
				this.transportDetail = this.transportDetail.replace(this.courier.trim(), temp.append("<span style=\"color:red;\">").append(this.courier.trim()).append("</span>").toString());
			}
			temp.setLength(0);
			if ((this.courierTel != null) && !this.courierTel.isEmpty() && this.transportDetail.contains(this.courierTel)) {
				this.transportDetail = this.transportDetail.replace(this.courierTel.trim(), temp.append("<span style=\"color:red;\">").append(this.courierTel.trim()).append("</span>").toString());
			}
			temp.setLength(0);
			if ((this.signMan != null) && !this.signMan.isEmpty() && this.transportDetail.contains(this.signMan)) {
				this.transportDetail = this.transportDetail.replace(this.signMan.trim(), temp.append("<span style=\"color:red;\">").append(this.signMan.trim()).append("</span>").toString());
			}
			temp.setLength(0);
			if ((this.exceptionReason != null) && !this.exceptionReason.isEmpty() && this.transportDetail.contains(this.exceptionReason)) {
				this.transportDetail = this.transportDetail.replace(this.exceptionReason.trim(), temp.append("<span style=\"color:red;\">").append(this.exceptionReason.trim()).append("</span>")
						.toString());
			}
		}
	}
}
