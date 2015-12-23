package cn.explink.enumutil.express;
/**
 * 反馈的时候导出标识
 * @author jiangyu 2015年8月7日
 *
 */
public enum ExpressFeedBackExportFlagEnum {

	AlreadyFeedBack("alreadyFeedBack","已反馈"), 
	UnFeedBack("unFeedBack","未反馈");
	
	private String value;
	private String descp;

	private ExpressFeedBackExportFlagEnum(String value,String descp) {
		this.value = value;
		this.descp = descp;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescp() {
		return descp;
	}

	public void setDescp(String descp) {
		this.descp = descp;
	}
}
