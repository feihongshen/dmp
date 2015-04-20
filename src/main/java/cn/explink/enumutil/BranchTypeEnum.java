package cn.explink.enumutil;
/**
 * 
 * @author wangzhiyong
 * 直营
二级站
三级站
加盟
加盟二级
加盟三级
 *
 */
public enum BranchTypeEnum {
	
	ZhiYing(1,"直营"),ErJiZhan(2,"二级站"),SanJiZhan(3,"三级站"),JiaMeng(4,"加盟"),JiaMengErJi(5,"加盟二级"),JiaMengSanJi(6,"加盟三级");
	private int value;
	private String text;
		
	private BranchTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}
	public int getValue() {
		return value;
	}
	
	public String getText() {
		return text;
	}
	
}
