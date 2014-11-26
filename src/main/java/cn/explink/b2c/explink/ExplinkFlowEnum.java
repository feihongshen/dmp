package cn.explink.b2c.explink;

/**
 * 允许被查询到的状态
 * 
 * @author Administrator
 *
 */
public enum ExplinkFlowEnum {
	DaoRu("1", "导入数据"), Ruku("4", "库房入库"), ChuKu("6", "库房出库"), FenZhanDaoHuo("7", "分站到货"), FenZhanLingHuo("9", "派送员投递中"),

	// 反馈结果
	PeiSongChengGong("18", "配送成功"), ShangMenTuiChengGong("19", "上门退成功"), ShangMenHuanChengGong("20", "上门换成功"), JuShou("21", "拒收"), BuFenTuiHuo("22", "部分拒收"), FenZhanZhiLiu("23", "分站滞留"), ShangMenJuTui(
			"24", "上门拒退"), HuoWuDiuShi("25", "货物丢失"),

	// 反馈结果 --审核
	PeiSongChengGong_v("018", "审核为配送成功"), ShangMenTuiChengGong_v("019", "审核为上门退成功"), ShangMenHuanChengGong_v("020", "审核为上门换成功"), JuShou_v("021", "审核为拒收"), BuFenTuiHuo_v("022", "审核为部分拒收"), FenZhanZhiLiu_v(
			"023", "审核为分站滞留"), ShangMenJuTui_v("024", "审核为上门拒退"), HuoWuDiuShi_v("025", "审核为货物丢失"),

	TuiHuoZhanRuKu("15", "退货站入库"), TuiGongYingShangChuKu("27", "退供货商出库"), TuiHuoChuKuSaoMiao("40", "退货出站扫描"),

	;

	private String statuscode;

	public String getStatuscode() {
		return statuscode;
	}

	public void setStatuscode(String statuscode) {
		this.statuscode = statuscode;
	}

	private String text;

	private ExplinkFlowEnum(String flowordertype, String text) {
		this.statuscode = flowordertype;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
