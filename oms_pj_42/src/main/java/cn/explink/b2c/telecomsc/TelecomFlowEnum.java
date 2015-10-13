package cn.explink.b2c.telecomsc;

import cn.explink.enumutil.FlowOrderTypeEnum;

public enum TelecomFlowEnum {

	RuKu("01", FlowOrderTypeEnum.RuKu.getValue(), "库房入库", 0), KuFangChuku("03", FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "库房出库", 0), FenZhanDaoHuo("04", FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao
			.getValue(), "分站到货", 0), ZhanDianChuku("07", FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "分站投递", 0),

	ChengGong("0801", FlowOrderTypeEnum.YiFanKui.getValue(), "配送成功", 1), JuShou("0802", FlowOrderTypeEnum.YiFanKui.getValue(), "全部退货", 1),

	ChengGong_shenhe("08", FlowOrderTypeEnum.YiShenHe.getValue(), "配送成功-审核", 1), JuShou_shenhe("080200", FlowOrderTypeEnum.YiShenHe.getValue(), "全部退货-审核", 1),

	FenZhanZhiLiu("0806", FlowOrderTypeEnum.YiShenHe.getValue(), "分站滞留", 1), HuowuDiuShi("0805", FlowOrderTypeEnum.YiShenHe.getValue(), "货物丢失", 1),

	;
	private String b2cCode;
	private int onwer_code;
	private String desribe;
	private int isResultFlag; // 是否签收结果

	private TelecomFlowEnum(String b2cCode, int onwer_code, String desribe, int isResultFlag) {
		this.b2cCode = b2cCode;
		this.onwer_code = onwer_code;
		this.desribe = desribe;
		this.isResultFlag = isResultFlag;
	}

	public String getB2cCode() {
		return b2cCode;
	}

	public void setB2cCode(String b2cCode) {
		this.b2cCode = b2cCode;
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

	public void setIsResultFlag(int isResultFlag) {
		this.isResultFlag = isResultFlag;
	}

}
