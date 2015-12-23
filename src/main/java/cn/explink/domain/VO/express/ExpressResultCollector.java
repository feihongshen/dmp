package cn.explink.domain.VO.express;

import cn.explink.domain.express.ExpressFreightBillImportDetail;

/**
 * 导入的时候结果的收集
 * @author jiangyu 2015年8月21日
 *
 */
public class ExpressResultCollector {
	
	private ExpressFreightBillImportDetail record;
	
	private Boolean status;
	
	private String failMsg;
	
	public ExpressResultCollector() {
	}

	public ExpressFreightBillImportDetail getRecord() {
		return record;
	}

	public void setRecord(ExpressFreightBillImportDetail record) {
		this.record = record;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getFailMsg() {
		return failMsg;
	}

	public void setFailMsg(String failMsg) {
		this.failMsg = failMsg;
	}
}
