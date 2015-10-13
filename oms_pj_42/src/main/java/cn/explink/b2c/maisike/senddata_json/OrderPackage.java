package cn.explink.b2c.maisike.senddata_json;

/**
 * 订单包裹列表
 * 
 * @author Administrator
 *
 */
public class OrderPackage {

	private String psn = "";// 包裹唯一编号
	private float pweight = 0;// 包裹重量，单位:公斤
	private float pvolume = 0; // 包裹体积，单位:立方米
	private float pmoney = 0; // 包裹价格

	public String getPsn() {
		return psn;
	}

	public void setPsn(String psn) {
		this.psn = psn;
	}

	public float getPweight() {
		return pweight;
	}

	public void setPweight(float pweight) {
		this.pweight = pweight;
	}

	public float getPvolume() {
		return pvolume;
	}

	public void setPvolume(float pvolume) {
		this.pvolume = pvolume;
	}

	public float getPmoney() {
		return pmoney;
	}

	public void setPmoney(float pmoney) {
		this.pmoney = pmoney;
	}

}
