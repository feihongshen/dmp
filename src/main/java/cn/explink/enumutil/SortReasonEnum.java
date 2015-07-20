package cn.explink.enumutil;

//账单批次、账单状态、客户名称、账单创建日期、账单核销日期
public enum SortReasonEnum {
	ZhangDanPiCi("bill_batches","账单批次"),ZhangDanZhuangTai("bill_state","账单状态"),KeHuMingCheng("customer_id","客户名称"),
	ZhangDanChuangJianRiQi("date_create_bill","账单创建日期"),ZhangDanHeXiaoRiQi("date_verification_bill","账单核销日期");
	
	private String value;
	private String text;
	
	private SortReasonEnum(String value,String text){
		this.value=value;
		this.text=text;
	}
	public String getValue() {
		return value;
	}
	public String getText() {
		return text;
	}
	
	
	
	
	

}
