package cn.explink.enumutil;

public enum CwbOrderPDAEnum {
	OK(0, "000000", "成功", "success.wav"), SYS_ERROR(11, "100001", "系统内部错误", "fail.wav"), YONG_HU_BU_CUN_ZAI(12, "100002", "用户不存在", "fail.wav"), MI_MA_CUO_WU(13, "100003", "密码错误", "fail.wav"), YONG_HU_BEI_SUO_DING(
			14, "100004", "用户被锁定", "fail.wav"), WU_CAO_ZUO_QUAN_XIAN(15, "100005", "无此操作权限", "fail.wav"), YI_CHANG_DAN_HAO(20, "200000", "异常单号，含有非法字符", "fail.wav"), YOU_HUO_WU_DAN_RU_KU(21, "200001",
			"有货无单入库", "fail.wav"), CHONG_FU_RU_KU(22, "200002", "重复入库", "fail.wav"), Y_H_W_D_WEI_XUAN_GONG_HUO_SHANG(23, "200003", "有货无单，未选择供货商不允许入库", "fail.wav"), BU_YUN_XU(24, "200004",
			"有货无单，本站不允许有货无单入库", "fail.wav"), KU_FANG_CHU_KU_YI_CHANG_DING_DAN_BU_CUN_ZAI(25, "200005", "库房出库异常，订单不在可出库数据中", "fail.wav"), YOU_HUO_WU_DAN(26, "200006", "有货无单", "fail.wav"), CHONG_FU_DAO_HUO(
			28, "200008", "重复到货", "fail.wav"),
	/*
	 * DAN_HAO_BU_SHU_YU_WEI_DAO_HUO_SHU_JU(29,"200009","单号不属于未到货数据"),
	 * BEN_ZHAN_BU_YUN_XU_FEN_ZHAN_YOU_HUO_WU_DAN_PAI_SONG
	 * (211,"200011","本站不允许分站有货无单派送"),
	 * BEN_ZHAN_BU_YUN_XU_FEN_ZHAN_YI_CHANG_DAN_PAI_SONG
	 * (212,"200012","本站不允许分站异常单派送"),
	 * BEN_ZHAN_WEI_DAO_HUO_QUE_REN_DING_DAN_BU_YUN_XU_PAI_SONG
	 * (213,"200013","本站未到货确认订单,不允许派送"),
	 */
	FEN_ZHAN_TUI_HUO_YI_CHANG_DING_DAN_BU_ZAI_KE_TUI_HUO_SHU_JU_ZHONG(215, "200015", "分站退货异常，订单不在可退货数据中", "fail.wav"), FEN_ZHAN_ZHONG_ZHUAN_YI_CHANG_DING_DAN_BU_ZAI_KE_ZHONG_ZHUAN_SHU_JU_ZHONG(216,
			"200016", "分站中转异常，订单不在可退货数据中", "fail.wav"), CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI(217, "200017", "无此单号", "fail.wav"), PAN_DIAN_DUO_HUO(218, "200018", "盘点多货", "fail.wav"), TUI_GONG_YING_SHANG_CHU_KU_YI_CHANG_FEI_BEN_GONG_YING_SHANG_DING_DAN(
			219, "200019", "退供货商出库异常，非本供货商订单", "fail.wav"), TUI_GONG_YING_SHANG_CHU_KU_YI_CHANG_FEI_YING_TUI_HUO_DING_DAN(220, "200020", "退供货商出库异常，非应退货订单", "fail.wav"), JU_TUI_FAN_KU_YI_CHANG_FEI_YING_FAN_KU_YI_CHANG(
			221, "200021", "拒收返库异常，非应返库订单", "fail.wav"), CHONG_FU_CHU_KU(222, "200022", "重复出库", "fail.wav"), CHONG_FU_FAN_KUI(224, "200024", "重复反馈", "fail.wav"), MEI_YOU_PI_CI_JI_LU(225, "200025",
			"没有批次记录", "fail.wav"), PI_CI_YI_FENG_BAO(226, "200026", "批次已封包", "fail.wav"), GONG_YING_SHANG_XUAN_ZE_CUO_WU(227, "200027", "供货商选择错误", "fail.wav"), YI_CHANG_DING_DAN_WEI_LING_HUO_FAN_KUI(
			228, "200028", "异常订单，未领货反馈", "fail.wav"), YI_PIAO_DUO_JIAN(229, "200029", "一票多件", "ypdj.wav"), WEI_PI_PEI_ZHAN_DIAN(230, "200030", "未匹配站点", "fail.wav"), DING_DAN_WEI_SHEN_HE(231,
			"200031", "订单未审核", "fail.wav"), QING_ZUO_DAO_HUO_SAO_MIAO(232, "200032", "请做到货扫描", "fail.wav"), KU_FANG_BU_YUN_XU(233, "200033", "有货无单，库房不允许有货无单入库", "fail.wav"), YI_TUI_HUO(234, "200034",
			"已退货", "fail.wav"), YI_PEI_SONG_CHENG_GONG(235, "200035", "已配送成功", "fail.wav"), GAO_JIA(236, "200036", "高价", "gj.wav"), YI_ZHI_LIU(237, "200037", "已滞留", "fail.wav"), YI_HUAN_HUO(238,
			"200038", "已换货", "fail.wav"), YI_JU_SHUO(239, "200039", "已拒收", "fail.wav"), MEI_YOU_FEN_HUO_ZHAN_DIAN(240, "200040", "没有分货站点", "fail.wav"), FEI_PEI_SONG_DING_DAN(241, "200041", "非配送类型订单",
			""), FEI_SHANG_MEN_TUI_DING_DAN(242, "2000042", "非上门退类型订单", ""), FEI_SHANG_MEN_HUAN_DING_DAN(243, "200043", "非上门换类型订单", ""), ZHONG_ZHUAN_HUO(244, "200044", "中转货", "fail.wav"), FANG_CUO_HUO(
			245, "200045", "放错货", "fail.wav"), WEI_ZAI_TOU(246, "200046", "未再投", "fail.wav"), YI_CHANG_CAO_ZUO_ZHAO_BU_DAO_ZHONG_ZHUAN_ZHAN(247, "200047", "异常操作，找不到中转站", "fail.wav"), YI_CHANG_CAO_ZUO_ZHAO_BU_DAO_TUI_HUO_ZHAN(
			248, "200048", "异常操作，找不到退货站", "fail.wav"), SYS_SCAN_ERROR(31, "300001", "已欠费", "fail.wav"), MEI_YOU_FEN_HUO_KU_FANG(250, "200050", "没有分货库房", "fail.wav"), FEI_BEN_KU_FAN_HUO_WU(251,
			"200051", "非本库房货物", "fail.wav"), BU_SHI_ZHE_GE_XIAO_JIAN_YUAN_DE_HUO(252, "200052", "不是这个小件员的货", "fail.wav"), WEI_GUI_BAN_BU_NENG_ZAI_LING(253, "200053", "未归班的订单不能再领", "fali.wav"), WU_FA_ZI_DONG_PAN_DUAN_XIA_YI_ZHAN(
			254, "200054", "无法自动判断下一站", "fali.wav"), BAO_HAO_BU_CUN_ZAI(255, "200055", "包号不可以为0", "fali.wav"), BU_SHI_ZHE_GE_MU_DI_DI(256, "200056", "不是这个目的地的货", "fali.wav"), FEI_BEN_ZHAN_HUO(258,
			"200058", "非本站货", "fail.wav"), Wei_Shua_Ka_Yuan_Yin(260, "200060", "请选择未刷卡原因", "fail.wav"), Feng_Bao(261, "200061", "封包异常", "fail.wav"),
			TUI_HUO_RU_KU(262, "200062", "退货", "return.wav"),ZHONG_ZHUAN_RU_KU(263, "200063", "中转", "change.wav"),Gui_Ping(264,"200064","贵品","gp.wav"),Gao_Jia(265,"200065","高价","gj.wav"),
			
			
			LOGIN_ERROR(266,"200066","账号或密码错误，请重新输入","fail.wav"),
			LOGIN_TIMEOUT(267,"200067","登录超时，请重新登录","fail.wav");

	private int value;
	private String code;
	private String error;
	private String vediourl;

	private CwbOrderPDAEnum(int value, String code, String error, String vediourl) {
		this.value = value;
		this.code = code;
		this.error = error;
		this.vediourl = vediourl;
	}

	public int getValue() {
		return value;
	}

	public String getCode() {
		return code;
	}

	public String getError() {
		return error;
	}

	public String getVediourl() {
		return vediourl;
	}

}
