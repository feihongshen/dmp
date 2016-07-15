package cn.explink.b2c.tps;

public class OrderTraceToTPSCfg extends ThirdPartyOrder2DOCfg{
	/**************add start*******************/
	//add by 周欢    推送轨迹给tps增加每次推送轨迹数量属性   2016-07-15
	private int sendMaxCount;//每次推送轨迹数量

	public int getSendMaxCount() {
		return sendMaxCount;
	}

	public void setSendMaxCount(int sendMaxCount) {
		this.sendMaxCount = sendMaxCount;
	}
	/**************add end*******************/
}
