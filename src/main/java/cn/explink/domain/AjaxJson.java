package cn.explink.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AjaxJson {
	/**
	 * 操作成功标识
	 */
	private boolean status = true;
	/**
	 * 提示信息
	 */
	private String msg = "";
	/**
	 * 动态对象
	 */
	private Object obj = null;
	/**
	 * 拓展属性
	 */
	private Map map = new HashMap();
	
	private List<Object> objList=new ArrayList<Object>();
	
	public List<Object> getObjList() {
		return objList;
	}
	public void setObjList(List<Object> objList) {
		this.objList = objList;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
	
}
