package cn.explink.domain.VO.express;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.explink.domain.WavObject;

/**
 * 快递操作返回
 * 
 * @author jiangyu 2015年8月4日
 *
 */
public class ExpressOpeAjaxResult {

	/**
	 * 操作之后返回的对象
	 */
	private Object obj = null;
	/**
	 * 提示信息
	 */
	private String msg = "操作成功";
	/**
	 * 操作之后的状态设置
	 */
	private Boolean status = true;
	/**
	 * 操作返回给前台的属性
	 */
	private Map<String, Object> attributes;
	/**
	 * 操作记录数
	 */
	private Integer recordCount;
	
	private List<WavObject> wavList = null;
	
	private String wavPath = "";

	public ExpressOpeAjaxResult() {
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public Integer getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}
	
	public void addWav(int time, String url) {
		this.getWavList().add(new WavObject(time, url));
	}

	public void addShortWav(String fullPath) {
		if (fullPath == null) {
			return;
		}
		this.getWavList().add(new WavObject(500, fullPath));
	}

	public void addLongWav(String fullPath) {
		if (fullPath == null) {
			return;
		}
		this.getWavList().add(new WavObject(800, fullPath));
	}

	public void addLastWav(String fullPath) {
		if (fullPath == null) {
			return;
		}
		this.getWavList().add(new WavObject(0, fullPath));
	}

	public List<WavObject> getWavList() {
		if (this.wavList == null) {
			this.wavList = new ArrayList<WavObject>();
		}
		return this.wavList;
	}

	public String getWavPath() {
		return wavPath;
	}

	public void setWavPath(String wavPath) {
		this.wavPath = wavPath;
	}
	
}
