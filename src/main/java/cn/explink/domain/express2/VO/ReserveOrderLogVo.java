package cn.explink.domain.express2.VO;

import java.io.Serializable;

/**
 * 预约单日志Vo
 * @date 2016年5月13日 下午6:04:39
 */
public class ReserveOrderLogVo implements Serializable {

	private static final long serialVersionUID = 7891336496501892005L;
	
	/**
	* 主键ID
	*/
	private Long id;
	/**
	* 预约单号
	*/
	private String reserveOrderNo;
	
	/**
	* 操作类型
	*/
	private String operateType;
	
	/**
	* 操作时间
	*/
	private long operateTime;
	
	/**
	* 操作人
	*/
	private String operator;
	
	/**
	* 物流状态信息
	*/
	private String trackDetail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReserveOrderNo() {
		return reserveOrderNo;
	}

	public void setReserveOrderNo(String reserveOrderNo) {
		this.reserveOrderNo = reserveOrderNo;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public long getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(long operateTime) {
		this.operateTime = operateTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getTrackDetail() {
		return trackDetail;
	}

	public void setTrackDetail(String trackDetail) {
		this.trackDetail = trackDetail;
	}

}
