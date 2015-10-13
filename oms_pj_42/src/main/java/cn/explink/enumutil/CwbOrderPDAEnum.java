package cn.explink.enumutil;

public enum CwbOrderPDAEnum {
	OK(0, "000000", ""), WU_CAO_ZUO_QUAN_XIAN(15, "100005", "无此操作权限"), YOU_HUO_WU_DAN(21, "200001", "有货无单入库"), CHONG_FU_RU_KU(22, "200002", "重复入库"), Y_H_W_D_WEI_XUAN_GONG_HUO_SHANG(23, "200003",
			"有货无单，未选择供货商不允许入库"), BU_YUN_XU(24, "200004", "有货无单，本站不允许有货无单入库"), WEI_XUAN_ZE_JIA_SHI_YUAN(219, "200019", "未选择驾驶员"), SYS_ERROR(1, "100001", "系统内部错误"), WEI_XUAN_ZE_ZHONG_ZHUAN_ZHAN(26,
			"200006", "未选择中转站"), WEI_XUAN_ZE_CHE_LIANG(27, "200007", "未选择车辆"), CHONG_FU_CHU_KU(225, "200225", "重复出库"), YONG_HU_BU_CUN_ZAI(12, "100002", "用户不存在"), MI_MA_CUO_WU(13, "100003", "密码错误"), YONG_HU_BEI_SUO_DING(
			14, "100004", "用户被锁定"), WEI_XUAN_ZE_ZHAN_DIAN(220, "20020", "未选择站点"), CHONG_FU_LING_HUO(221, "20021", "重复领货"), WEI_XUAN_ZE_XIAO_JIAN_YUAN(222, "20022", "未选择小件员"), CHONG_FU_FAN_KUI(223,
			"200023", "重复反馈"), XIAO_JIAN_YUAN_WEI_LING_HUO(224, "200224", "小件员未领货"), CHONG_FU_DAO_HUO(28, "200008", "重复到货");
	// DAN_HAO_BU_SHU_YU_WEI_DAO_HUO_SHU_JU(29,"200009","单号不属于未到货数据");

	private int value;
	private String code;
	private String error;

	private CwbOrderPDAEnum(int value, String code, String error) {
		this.value = value;
		this.code = code;
		this.error = error;
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

}
