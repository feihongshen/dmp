package cn.explink.b2c.hzabc;

import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 杭州ABC推送状态反馈的枚举
 * 
 * @author Administrator
 *
 */
public enum HangZhouFlowEnum {
	// 配送中
	SC01("SC01", FlowOrderTypeEnum.RuKu.getValue(), "已接收（已入库）", 0), SC06_1("SC06", FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "库房出库扫描，所有的中转扫描", 0), SC06_2("SC06",
			FlowOrderTypeEnum.ZhongZhuanChuKuSaoMiao.getValue(), "中转中，所有的中转扫描", 0), SC06_3("SC06", FlowOrderTypeEnum.TuiHuoChuKuSaoMiao.getValue(), "分站退货出站", 0), SC06_4("SC06",
			FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), "退货站入库", 0), SC02("SC02", FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "配送中，所有分配配送员的扫描", 0),

	// 退供货商出库中
	SC08("SC08", FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), "退货（退甲方，出库时）", 0),

	// 配送结果
	SC03("SC03", DeliveryStateEnum.PeiSongChengGong.getValue(), "妥投（提交配送结果时）", 1), SC09("SC09", DeliveryStateEnum.ShangMenTuiChengGong.getValue(), "改货款（提交配送结果时）-上门退成功", 1), // 上门退成功
	SC13("SC13", DeliveryStateEnum.ShangMenHuanChengGong.getValue(), "换货成功（提交配送结果时）", 1), SC10("SC10", DeliveryStateEnum.ShangMenJuTui.getValue(), "上门据退（提交配送结果时）", 1), // 上门据退
	SC04("SC04", DeliveryStateEnum.FenZhanZhiLiu.getValue(), "滞留（提交配送结果时）", 1), SC05("SC05", DeliveryStateEnum.QuanBuTuiHuo.getValue(), "拒收（提交配送结果时）", 1), SC07("SC07", DeliveryStateEnum.HuoWuDiuShi
			.getValue(), "丢失（提交配送结果时）", 1),

	SC14("SC14", DeliveryStateEnum.BuFenTuiHuo.getValue(), "半签半退（提交配送结果时）", 1),

	;

	private String abc_state;
	private int flowordertype;
	private String text;
	private int flag; // 0->流程状态 ，1最终结果

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	private HangZhouFlowEnum(String abc_state, int flowordertype, String text, int flag) {
		this.abc_state = abc_state;
		this.flowordertype = flowordertype;
		this.text = text;
		this.flag = flag;
	}

	public String getAbc_state() {
		return abc_state;
	}

	public void setAbc_state(String abc_state) {
		this.abc_state = abc_state;
	}

	public int getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(int flowordertype) {
		this.flowordertype = flowordertype;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
