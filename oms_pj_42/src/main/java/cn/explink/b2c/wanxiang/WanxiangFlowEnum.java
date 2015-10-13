package cn.explink.b2c.wanxiang;

import cn.explink.enumutil.FlowOrderTypeEnum;

public enum WanxiangFlowEnum {

	RuKu("50010", FlowOrderTypeEnum.RuKu.getValue(), "库房入库", 0), KuFangChuku("59999", FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "库房出库", 0), FenZhanDaoHuo("60010",
			FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "分站到货", 0), ZhanDianChuku("60030", FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "站内派件", 0), DaoCuoHuo("60102",
			FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "出站转运(到错货)", 0),

	FenZhanZhiLiu("60101", FlowOrderTypeEnum.YiShenHe.getValue(), "站内延缓", 1), PeiSongChengGong("99999", FlowOrderTypeEnum.YiShenHe.getValue(), "成功-审核", 1), JuShou("60103", FlowOrderTypeEnum.YiFanKui
			.getValue(), "拒收-审核", 1),

	TuiGongHuoShangChuKu("99997", FlowOrderTypeEnum.TuiHuoChuKuSaoMiao.getValue(), "分站退货出站（退件确认）", 0), // 退件确认
	;
	private String wx_code;
	private int onwer_code;
	private String desribe;
	private int isResultFlag; // 是否签收结果

	private WanxiangFlowEnum(String wx_code, int onwer_code, String desribe, int isResultFlag) {
		this.wx_code = wx_code;
		this.onwer_code = onwer_code;
		this.desribe = desribe;
		this.isResultFlag = isResultFlag;
	}

	public void setWx_code(String wx_code) {
		this.wx_code = wx_code;
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

	public int getIsResultFlag() {
		return isResultFlag;
	}

	public String getWx_code() {
		return wx_code;
	}

	public void setIsResultFlag(int isResultFlag) {
		this.isResultFlag = isResultFlag;
	}

}
