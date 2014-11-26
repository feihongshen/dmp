package cn.explink.b2c.hxgdms;

import cn.explink.enumutil.CwbOrderTypeIdEnum;

public enum WorkTypeEnum {

	PeiSongDan(0, CwbOrderTypeIdEnum.Peisong.getValue(), "销售单-配送单"), HuanHuodan(1, CwbOrderTypeIdEnum.Shangmenhuan.getValue(), "换货单"), TuiHuoDan(2, CwbOrderTypeIdEnum.Shangmentui.getValue(),
			"拒收产生的退货单"), TuiHuoDan_posun(3, CwbOrderTypeIdEnum.Shangmentui.getValue(), "破损产生的退货单"), TuiHuo_cancel(4, CwbOrderTypeIdEnum.Shangmentui.getValue(), "取消产生的退货单"), HuanHuo_tuihui(5,
			CwbOrderTypeIdEnum.Shangmentui.getValue(), "换货退货对应拿回来退货单"), TuiHuo_TuiKuan(6, CwbOrderTypeIdEnum.Shangmentui.getValue(), "签收后退货(退货且退款)"), TuiHuo_BuTuiKuan(7,
			CwbOrderTypeIdEnum.Shangmentui.getValue(), "签收后退货(退货不退款)");

	private int dmsState;
	private int ownState;
	private String text;

	private WorkTypeEnum(int dmsState, int ownState, String text) {
		this.dmsState = dmsState;
		this.ownState = ownState;
		this.text = text;
	}

	public int getDmsState() {
		return dmsState;
	}

	public void setDmsState(int dmsState) {
		this.dmsState = dmsState;
	}

	public int getOwnState() {
		return ownState;
	}

	public void setOwnState(int ownState) {
		this.ownState = ownState;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
