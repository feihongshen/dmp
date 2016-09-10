package cn.explink.domain.VO.express;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @description 记录快递补录导入的结果
 * @author  刘武强
 * @data   2015年10月10日
 */
public class EmbracedImportResult implements Serializable {
	private String id;

	private boolean isFinished;

	private boolean isStoped = false;
	/*
	 * 解析校验后，正常的数据
	 */
	private List<EmbracedOrderVO> successList;
	/*
	 * 解析校验后，不正常的数据
	 */
	private List<EmbracedImportErrOrder> failList;
	/*
	 * 解析过程中，出现的错误信息（这个正常情况下不会有，有了就代表程序异常）
	 */
	private String resultErrMsg;
	/*
	 * 解析后得到的数据，用于确认导入之后的数据校验和保存
	 */
	private List<EmbracedImportOrderVO> analysisList;

	public boolean isFinished() {
		return this.isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public boolean isStoped() {
		return this.isStoped;
	}

	public void setStoped(boolean isStoped) {
		this.isStoped = isStoped;
	}

	public List<EmbracedImportErrOrder> getFailList() {
		return this.failList;
	}

	public void setFailList(List<EmbracedImportErrOrder> failList) {
		this.failList = failList;
	}

	public String getResultErrMsg() {
		return this.resultErrMsg;
	}

	public void setResultErrMsg(String resultErrMsg) {
		this.resultErrMsg = resultErrMsg;
	}

	public List<EmbracedOrderVO> getSuccessList() {
		return this.successList;
	}

	public void setSuccessList(List<EmbracedOrderVO> successList) {
		this.successList = successList;
	}

	public List<EmbracedImportOrderVO> getAnalysisList() {
		return this.analysisList;
	}

	public void setAnalysisList(List<EmbracedImportOrderVO> analysisList) {
		this.analysisList = analysisList;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
