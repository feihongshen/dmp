package cn.explink.b2c.weisuda;

public enum WeisudsInterfaceEnum {

	pushOrders(1, "订单与快递员绑定关系同步接口", "pushOrders"),
	getUnVerifyOrders(2, "APP包裹签收信息同步接口", "getUnVerifyOrders"),
	updateUnVerifyOrders(3, "APP包裹签收信息同步结果反馈接口", "updateUnVerifyOrders"),
	updateOrders(4,"包裹签收信息修改通知接口", "updateOrders"),
	siteUpdate(5, "站点更新接口", "siteUpdate"),
	siteDel(6, "站点撤销接口", "siteDel"),
	courierUpdate(7, "快递员信息更新接口", "courierUpdate"),
	carrierDel(8, "快递员删除接口","carrierDel"),
	unboundOrders(9,"快递员解绑接口","unboundOrders"),
	getback_boundOrders(10,"退货单与收件员绑定关系同步接口","getback_boundOrders"),
	getback_getAppOrders(11,"上门揽退信息同步接口","getback_getAppOrders"),
	getback_confirmAppOrders(12,"上门揽退信息同步结果反馈接口","getback_confirmAppOrders"),
	getback_updateOrders(13,"上门揽退信息修改通知接口","getback_updateOrders")
	;

	private int value;
	private String name;
	private String text;

	private WeisudsInterfaceEnum(int value, String text, String name) {
		this.value = value;
		this.text = text;
		this.name = name;
	}

	public int getValue() {
		return this.value;
	}

	public String getName() {
		return this.name;
	}

	public String getText() {
		return this.text;
	}

}