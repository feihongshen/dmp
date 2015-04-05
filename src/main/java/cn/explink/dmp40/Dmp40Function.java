package cn.explink.dmp40;

import java.util.ArrayList;
import java.util.List;

public class Dmp40Function {

	private String id;
	private int functionLevel;
	private String functionName;
	private String functionOrder;
	private String functionUrl;
	private String parentFunctionId;
	private Dmp40Function dmp40Function;
	private List<Dmp40Function> dmp40FunctionList = new ArrayList<Dmp40Function>();

	public Dmp40Function getDmp40Function() {
		return dmp40Function;
	}

	public void setDmp40Function(Dmp40Function dmp40Function) {
		this.dmp40Function = dmp40Function;
	}

	public List<Dmp40Function> getDmp40FunctionList() {
		return dmp40FunctionList;
	}

	public void setDmp40FunctionList(List<Dmp40Function> dmp40FunctionList) {
		this.dmp40FunctionList = dmp40FunctionList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getFunctionLevel() {
		return functionLevel;
	}

	public void setFunctionLevel(int functionLevel) {
		this.functionLevel = functionLevel;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getFunctionOrder() {
		return functionOrder;
	}

	public void setFunctionOrder(String functionOrder) {
		this.functionOrder = functionOrder;
	}

	public String getFunctionUrl() {
		return functionUrl;
	}

	public void setFunctionUrl(String functionUrl) {
		this.functionUrl = functionUrl;
	}

	public String getParentFunctionId() {
		return parentFunctionId;
	}

	public void setParentFunctionId(String parentFunctionId) {
		this.parentFunctionId = parentFunctionId;
	}
}
