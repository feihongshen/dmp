/**
 *
 */
package cn.explink.domain;

import java.math.BigDecimal;

/**
 * @author Administrator
 *
 */
public class PenalizeOut {
	private int penalizeOutId; //对外赔付id
	private String cwb;//订单号
	private Long customerid;//客户名称id
	private Long flowordertype;//订单状态
	private BigDecimal receivablefee;//订单金额
	private BigDecimal penalizeOutfee;//对外赔付金额
	private int penalizeOutbig;//赔付大类
	private int penalizeOutsmall;//赔付小类
	private String penalizeOutContent;//赔付说明
	private long createruser;//创建人
	private String createrdate;//创建日期
	private long canceluser;//撤销人
	private String canceldate;//撤销日期
	private String cancelContent;//撤销说明
	private int penalizeOutstate;//赔付状态
	private int state;//状态
	/**
	 * @return the penalizeOutId
	 */
	public int getPenalizeOutId() {
		return this.penalizeOutId;
	}
	/**
	 * @param penalizeOutId the penalizeOutId to set
	 */
	public void setPenalizeOutId(int penalizeOutId) {
		this.penalizeOutId = penalizeOutId;
	}
	/**
	 * @return the cwb
	 */
	public String getCwb() {
		return this.cwb;
	}
	/**
	 * @param cwb the cwb to set
	 */
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	/**
	 * @return the customerid
	 */
	public Long getCustomerid() {
		return this.customerid;
	}
	/**
	 * @param customerid the customerid to set
	 */
	public void setCustomerid(Long customerid) {
		this.customerid = customerid;
	}
	/**
	 * @return the flowordertype
	 */
	public Long getFlowordertype() {
		return this.flowordertype;
	}
	/**
	 * @param flowordertype the flowordertype to set
	 */
	public void setFlowordertype(Long flowordertype) {
		this.flowordertype = flowordertype;
	}
	/**
	 * @return the receivablefee
	 */
	public BigDecimal getReceivablefee() {
		return this.receivablefee;
	}
	/**
	 * @param receivablefee the receivablefee to set
	 */
	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}
	/**
	 * @return the penalizeOutfee
	 */
	public BigDecimal getPenalizeOutfee() {
		return this.penalizeOutfee;
	}
	/**
	 * @param penalizeOutfee the penalizeOutfee to set
	 */
	public void setPenalizeOutfee(BigDecimal penalizeOutfee) {
		this.penalizeOutfee = penalizeOutfee;
	}
	/**
	 * @return the penalizeOutbig
	 */
	public int getPenalizeOutbig() {
		return this.penalizeOutbig;
	}
	/**
	 * @param penalizeOutbig the penalizeOutbig to set
	 */
	public void setPenalizeOutbig(int penalizeOutbig) {
		this.penalizeOutbig = penalizeOutbig;
	}
	/**
	 * @return the penalizeOutsmall
	 */
	public int getPenalizeOutsmall() {
		return this.penalizeOutsmall;
	}
	/**
	 * @param penalizeOutsmall the penalizeOutsmall to set
	 */
	public void setPenalizeOutsmall(int penalizeOutsmall) {
		this.penalizeOutsmall = penalizeOutsmall;
	}

	/**
	 * @return the penalizeOutContent
	 */
	public String getPenalizeOutContent() {
		return this.penalizeOutContent;
	}
	/**
	 * @param penalizeOutContent the penalizeOutContent to set
	 */
	public void setPenalizeOutContent(String penalizeOutContent) {
		this.penalizeOutContent = penalizeOutContent;
	}
	/**
	 * @return the createruser
	 */
	public long getCreateruser() {
		return this.createruser;
	}
	/**
	 * @param createruser the createruser to set
	 */
	public void setCreateruser(long createruser) {
		this.createruser = createruser;
	}
	/**
	 * @return the createrdate
	 */
	public String getCreaterdate() {
		return this.createrdate;
	}
	/**
	 * @param createrdate the createrdate to set
	 */
	public void setCreaterdate(String createrdate) {
		this.createrdate = createrdate;
	}
	/**
	 * @return the canceluser
	 */
	public long getCanceluser() {
		return this.canceluser;
	}
	/**
	 * @param canceluser the canceluser to set
	 */
	public void setCanceluser(long canceluser) {
		this.canceluser = canceluser;
	}
	/**
	 * @return the canceldate
	 */
	public String getCanceldate() {
		return this.canceldate;
	}
	/**
	 * @param canceldate the canceldate to set
	 */
	public void setCanceldate(String canceldate) {
		this.canceldate = canceldate;
	}

	/**
	 * @return the cancelContent
	 */
	public String getCancelContent() {
		return this.cancelContent;
	}
	/**
	 * @param cancelContent the cancelContent to set
	 */
	public void setCancelContent(String cancelContent) {
		this.cancelContent = cancelContent;
	}
	/**
	 * @return the penalizeOutstate
	 */
	public int getPenalizeOutstate() {
		return this.penalizeOutstate;
	}
	/**
	 * @param penalizeOutstate the penalizeOutstate to set
	 */
	public void setPenalizeOutstate(int penalizeOutstate) {
		this.penalizeOutstate = penalizeOutstate;
	}
	/**
	 * @return the state
	 */
	public int getState() {
		return this.state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}


}
