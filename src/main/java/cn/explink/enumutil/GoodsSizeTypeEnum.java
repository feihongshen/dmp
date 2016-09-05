package cn.explink.enumutil;

import java.util.ArrayList;
import java.util.List;

/**
 * express_ops_cwb_detail里goods_size_type的枚举值<br/>
 * 货物尺寸类型<br/>
 * date: 2016年8月29日 下午7:17:10 <br/>  
 *  
 * @author zhili01.liang  
 * @version   
 * @since JDK 1.7
 */
public enum GoodsSizeTypeEnum {
	normal(0, "普件"), big(1, "大件");

	private int value;
	private String text;

	private GoodsSizeTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}


	public static List<GoodsSizeTypeEnum> getAllType() {
		List<GoodsSizeTypeEnum> list = new ArrayList<GoodsSizeTypeEnum>();
		list.add(normal);
		list.add(big);
		return list;
	}


	public static String getTextByValue(int value) {
		List<GoodsSizeTypeEnum> allTypeList = GoodsSizeTypeEnum.getAllType();
		for (GoodsSizeTypeEnum status : allTypeList) {
			if (status.getValue()==value) {
				return status.getText();
			}
		}
		return "";
	}

	
	public static GoodsSizeTypeEnum getEnumByValue(int value) {
		List<GoodsSizeTypeEnum> allStatusList = GoodsSizeTypeEnum.getAllType();
		for (GoodsSizeTypeEnum status : allStatusList) {
			if (status.getValue()==value) {
				return status;
			}
		}
		return null;
	}
}
