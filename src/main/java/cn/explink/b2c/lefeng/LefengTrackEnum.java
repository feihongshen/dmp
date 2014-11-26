package cn.explink.b2c.lefeng;

import cn.explink.enumutil.CwbFlowOrderTypeEnum;

/**
 * 关于乐蜂网接口查询列表，配置需要查询的状态
 * 
 * @author Administrator
 *
 */
public enum LefengTrackEnum {

	RuKu("N10", CwbFlowOrderTypeEnum.RuKu.getValue(), "订单入库"), ChukuSaomiao("N10", CwbFlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "库房出库扫描"), FenZhanDaoHuo("N10",
			CwbFlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "分站到货扫描"), Deliverying("N10", CwbFlowOrderTypeEnum.FenZhanLingHuo.getValue(), "订单派送中"), Received("S00", CwbFlowOrderTypeEnum.YiShenHe
			.getValue(), "配送成功-签收"), DeliveryFaild("E30", CwbFlowOrderTypeEnum.YiShenHe.getValue(), "配送失败-拒收"), DeliveryExpt("E31", CwbFlowOrderTypeEnum.YiShenHe.getValue(), "配送异常-未能签收"), DeliveryExpt1(
			"E32", CwbFlowOrderTypeEnum.YiShenHe.getValue(), "配送异常-地址错误,未能送达"), ;

	private String lfw_code;

	public int getOnwer_code() {
		return onwer_code;
	}

	public void setOnwer_code(int onwer_code) {
		this.onwer_code = onwer_code;
	}

	public String getDesribe() {
		return desribe;
	}

	private int onwer_code;
	private String desribe;

	public String getLfw_code() {
		return lfw_code;
	}

	public void setLfw_code(String lfwCode) {
		lfw_code = lfwCode;
	}

	public void setDesribe(String desribe) {
		this.desribe = desribe;
	}

	private LefengTrackEnum(String lfw_code, int onwer_code, String desribe) {
		this.lfw_code = lfw_code;
		this.onwer_code = onwer_code;
		this.desribe = desribe;
	}

}
