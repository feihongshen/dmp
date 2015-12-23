package cn.explink.enumutil.express;

import java.util.ArrayList;
import java.util.List;

public enum SendTpsExpressOrderResultEnum {
	Success(1, "成功"), Failure(0, "失败");

	private int value;
	private String text;

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	SendTpsExpressOrderResultEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	/**
	 *
	 * @Title: getAllStatus
	 * @description 获取该枚举的所有值
	 * @author 刘武强
	 * @date  2015年11月10日下午6:02:32
	 * @param  @return
	 * @return  List<SendTpsExpressOrderResultEnum>
	 * @throws
	 */
	public static List<SendTpsExpressOrderResultEnum> getAllStatus() {
		List<SendTpsExpressOrderResultEnum> list = new ArrayList<SendTpsExpressOrderResultEnum>();
		list.add(Success);
		list.add(Failure);
		return list;
	}

	/**
	 *
	 * @Title: getTextByValue
	 * @description 根据value获取text
	 * @author 刘武强
	 * @date  2015年11月10日下午6:04:45
	 * @param  @param value
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	public static String getTextByValue(int value) {
		List<SendTpsExpressOrderResultEnum> list = SendTpsExpressOrderResultEnum.getAllStatus();
		for (SendTpsExpressOrderResultEnum temp : list) {
			if (temp.getValue() == value) {
				return temp.getText();
			}
		}
		return "";
	}
}
