package cn.explink.pos.yalian;

/**
 * @author Administrator
 *
 */
public class YalianApp {
	public String Code_liantong;// 编码
	public String Code_dianxin;// 编码
	public String Code_yidong;// 编码
	public String customerid;// 供货商id
	public String request_url;// 接收url
	public String trick_url;// 发送url

	public String getCode_liantong() {
		return Code_liantong;
	}

	public void setCode_liantong(String code_liantong) {
		Code_liantong = code_liantong;
	}

	public String getCode_dianxin() {
		return Code_dianxin;
	}

	public void setCode_dianxin(String code_dianxin) {
		Code_dianxin = code_dianxin;
	}

	public String getCode_yidong() {
		return Code_yidong;
	}

	public void setCode_yidong(String code_yidong) {
		Code_yidong = code_yidong;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getRequest_url() {
		return request_url;
	}

	public void setRequest_url(String request_url) {
		this.request_url = request_url;
	}

	public String getTrick_url() {
		return trick_url;
	}

	public void setTrick_url(String trick_url) {
		this.trick_url = trick_url;
	}

}
