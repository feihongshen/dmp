package cn.explink.b2c.gztlfeedback;

import cn.explink.enumutil.FlowOrderTypeEnum;

public enum GztlFeedbackEnum {
	Ruku(FlowOrderTypeEnum.RuKu.getValue(), "出入库", "到件确认", "", ""), 
	ChukuSaomiao(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "出入库", "发件", "",""),
	FenZhanDaoHuo(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "出入库", "到件", "",""),
	Deliverying(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "出入库", "派件", "",""), 
	TuiGongYingShangChuKu(FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), "出入库","退供应商", "",""), 
	TuiHuoZhanRuKu(FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), "出入库", "退件到件", "",""),
	TuiHuoChuZhan(FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), "出入库", "退件发件", "",""),
	GongYingShangJuShouFanKu(FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), "出入库", "退件失败", "",""), 
	ZhongZhuanChuZhan(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),"出入库","转件","",""),
	ZhongzhuanZhanRuKu(FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue(),"出入库","转件到件","",""),
	ZhuangZhuanZhanChuKuSaoMiao(FlowOrderTypeEnum.ZhongZhuanZhanChuKuSaoMiao.getValue(),"出入库","发件","",""),
	ShouGongdiushi(FlowOrderTypeEnum.YiShenHe.getValue(), "反馈", "包裹丢失", "包裹丢失",""),
	BufenJushou(FlowOrderTypeEnum.YiShenHe.getValue(), "反馈", "部分拒收", "部分拒收",""), 
	KehuYanqi(FlowOrderTypeEnum.YiShenHe.getValue(), "反馈", "客户延期", "客户延期",""), 
	Peisongchenggong(FlowOrderTypeEnum.YiShenHe.getValue(), "反馈", "配送成功", "配送成功",""), 
	Peisongshibai(FlowOrderTypeEnum.YiShenHe.getValue(), "反馈", "配送失败", "配送失败",""), 
	Peisongyanchi(FlowOrderTypeEnum.YiShenHe.getValue(), "反馈", "配送延迟", "配送延迟","")

	;
	private long flowtype;//本系统中的订单流程状态
	private String optype;//反馈类型
	private String state;//订单状态
	private String returnState;//网点反馈状态
	private String returnMsg; // 原因

	public long getFlowtype() {
		return this.flowtype;
	}

	public String getReturnMsg() {
		return this.returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public void setFlowtype(long flowtype) {
		this.flowtype = flowtype;
	}

	public String getOptype() {
		return this.optype;
	}

	public void setOptype(String optype) {
		this.optype = optype;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getReturnState() {
		return this.returnState;
	}

	public void setReturnState(String returnState) {
		this.returnState = returnState;
	}

	private GztlFeedbackEnum(long flowtype, String optype, String state, String returnState,String returnMsg) {
		this.flowtype = flowtype;
		this.optype = optype;
		this.state = state;
		this.returnState = returnState;
		this.returnMsg=returnMsg;
	}
}
