package cn.explink.enumutil.express;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账单的审核状态
 * @author jiangyu 2015年8月11日
 *
 */
public enum ExpressBillStateEnum {
	//账单状态( 0:未审核，1：已审核，2：已核销)
	UnAudit(0, "未审核"), Audited(1, "已审核"), Verificated(2, "已核销");

	private ExpressBillStateEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public static ExpressBillStateEnum getByValue(Integer index) {
		for (ExpressBillStateEnum typeEnum : ExpressBillStateEnum.values()) {
			if (typeEnum.getValue().intValue() == index) {
				return typeEnum;
			}
		}
		return null;
	}

	/**
	 *
	 * @Title: getAllStatus
	 * @description 获取枚举里面的所有状态
	 * @author 刘武强
	 * @date  2015年8月13日下午2:34:25
	 * @param  @return
	 * @return  List<BillStateEnum>
	 * @throws
	 */
	public static List<ExpressBillStateEnum> getAllStatus() {
		List<ExpressBillStateEnum> expressBillStateEnum = new ArrayList<ExpressBillStateEnum>();
		expressBillStateEnum.add(UnAudit);
		expressBillStateEnum.add(Audited);
		expressBillStateEnum.add(Verificated);
		return expressBillStateEnum;
	}

	/**
	 *
	 * @Title: getTextByValue
	 * @description 通过id获取text
	 * @author 刘武强
	 * @date  2015年8月14日上午11:36:29
	 * @param  @param value
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	public static String getTextByValue(int value) {
		List<ExpressBillStateEnum> allStatus = ExpressBillStateEnum.getAllStatus();
		for (ExpressBillStateEnum expressBillState : allStatus) {
			if (expressBillState.getValue() == value) {
				return expressBillState.getText();
			}
		}
		return "";
	}

	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (ExpressBillStateEnum e : ExpressBillStateEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}

	private Integer value;

	private String text;

	public String getText() {
		return this.text;
	}

	public Integer getValue() {
		return this.value;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
