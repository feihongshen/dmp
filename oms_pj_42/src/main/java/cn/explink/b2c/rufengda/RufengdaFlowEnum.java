package cn.explink.b2c.rufengda;

import cn.explink.enumutil.FlowOrderTypeEnum;

public enum RufengdaFlowEnum {

	RuKu(1, FlowOrderTypeEnum.RuKu.getValue(), "库房入库"), RuZhanFenZhan(1, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "分站入站"), FenPeiXiaoJianYuan(2, FlowOrderTypeEnum.FenZhanLingHuo.getValue(),
			"派送分拣"), TuoTou(3, FlowOrderTypeEnum.YiShenHe.getValue(), "妥投"), FenZhanZhiLiu(4, FlowOrderTypeEnum.YiShenHe.getValue(), "分站滞留"), JuShou(5, FlowOrderTypeEnum.YiShenHe.getValue(), "拒收"), ;
	private int rfd_code;
	private int onwer_code;
	private String desribe;

	private RufengdaFlowEnum(int rfd_code, int onwer_code, String desribe) {
		this.rfd_code = rfd_code;
		this.onwer_code = onwer_code;
		this.desribe = desribe;
	}

	public int getRfd_code() {
		return rfd_code;
	}

	public void setRfd_code(int rfd_code) {
		this.rfd_code = rfd_code;
	}

	public int getOnwer_code() {
		return onwer_code;
	}

	public void setOnwer_code(int onwer_code) {
		this.onwer_code = onwer_code;
	}

	public String getDesribe() {
		return desribe;
	}

	public void setDesribe(String desribe) {
		this.desribe = desribe;
	}

}
