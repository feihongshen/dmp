package cn.explink.enumutil;

//账单批次、账单状态、客户名称、账单创建日期、账单核销日期
public enum SortReasonEnum {
	ZhangDanPiCi("billBatches","账单批次"),ZhangDanZhuangTai("billState","账单状态"),KeHuMingCheng("customerId","客户名称"),
	ZhangDanChuangJianRiQi("dateCreateBill","账单创建日期"),ZhangDanHeXiaoRiQi("dateVerificationBill","账单核销日期");
	
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
