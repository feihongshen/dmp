package cn.explink.b2c.yixun;

import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * tmall推送状态反馈的枚举
 * 
 * @author Administrator
 *
 */
public enum YiXunFlowEnum {
	Ruku(0, FlowOrderTypeEnum.RuKu.getValue(), "入库信息反馈", 0), ChuKu(0, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "库房出库扫描", 0), FenZhanDaoHuo(0, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),
			"分站到货扫描", 0), FenZhanLingHuo(0, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "分站领货派送", 0),

	PeiSongChengGong(DeliveryStateEnum.PeiSongChengGong.getValue(), FlowOrderTypeEnum.YiShenHe.getValue(), "配送成功", 1), ShangMenTuiChengGong(DeliveryStateEnum.ShangMenTuiChengGong.getValue(),
			FlowOrderTypeEnum.YiShenHe.getValue(), "上门退成功", 1), ShangMenHuanChengGong(DeliveryStateEnum.ShangMenHuanChengGong.getValue(), FlowOrderTypeEnum.YiShenHe.getValue(), "上门换成功", 1),

	JuShou(DeliveryStateEnum.QuanBuTuiHuo.getValue(), FlowOrderTypeEnum.YiShenHe.getValue(), "拒收", 2), FenZhanZhiLiu(DeliveryStateEnum.FenZhanZhiLiu.getValue(), FlowOrderTypeEnum.YiShenHe.getValue(),
			"分站滞留", 0), ShangMenJuTui(DeliveryStateEnum.ShangMenJuTui.getValue(), FlowOrderTypeEnum.YiShenHe.getValue(), "上门拒退", 2), BuFenTuiHuo(DeliveryStateEnum.BuFenTuiHuo.getValue(),
			FlowOrderTypeEnum.YiShenHe.getValue(), "部分退货", 2), HuoWuDiuShi(DeliveryStateEnum.HuoWuDiuShi.getValue(), FlowOrderTypeEnum.YiShenHe.getValue(), "货物丢失", -1), ;

	private int deliveryState;
	private int flowordertype;
	private String text;
	private int saveState;// 是否存储的 0 不存储 直接反馈 非0 存储 并切 对应易迅的反馈要求 -1 为问题件

	private YiXunFlowEnum(int deliveryState, int flowordertype, String text, int saveState) {
		this.deliveryState = deliveryState;
		this.flowordertype = flowordertype;
		this.text = text;
		this.saveState = saveState;
	}

	public int getDeliveryState() {
		return deliveryState;
	}

	public void setDeliveryState(int deliveryState) {
		this.deliveryState = deliveryState;
	}

	public int getSaveState() {
		return saveState;
	}

	public void setSaveState(int saveState) {
		this.saveState = saveState;
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
