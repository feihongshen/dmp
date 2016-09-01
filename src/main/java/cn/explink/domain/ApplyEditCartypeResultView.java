package cn.explink.domain;

public class ApplyEditCartypeResultView {
	
	public final static String REMARK_CWB_REPEAT    	    = "订单重复";
	public final static String REMARK_CWB_INVALID   	    = "订单不存在或已失效";
	public final static String REMARK_CWB_YIGUIBANSHENHE    = "订单已经归班审核，不能申请修改";
	public final static String REMARK_CAR_TYPE_BIG  		= "订单类型为大件，不能申请修改";
	public final static String REMARK_APPLY_REPEAT    	    = "订单货物类型修改申请重复提交";
	public final static String REMARK_APPLY_EXCEPTION       = "数据提交异常，请联系运维人员";
	
	public final static String REMARK_APPLY_REVIEWED        = "订单货物类型修改申请已审核，不能更改";
	public final static String REMARK_APPLY_BILL_CALCULATED = "订单已计费出账，无法通过审核";
	
	//订单号或运单号
	private String cwb="";
	//订单号或运单号
	private boolean isValid = false;
	//是否需要插入记录到数据库。如果此记录为重复提交的记录，不需提交到数据库
	//private boolean isNeedApply = true;
	//备注
	private String remark="";
	
	
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	
	
	
	

}
