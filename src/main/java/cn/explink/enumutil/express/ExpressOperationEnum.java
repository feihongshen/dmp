package cn.explink.enumutil.express;

import java.util.HashMap;
import java.util.Map;

/**
 * 快递的操作枚举
 * 
 * @author jiangyu 2015年9月7日
 *
 */
public enum ExpressOperationEnum {
	PreOrderFeedBack(1, "反馈揽件接口","PreOrderFeedBack"),
	PreOrderQuery(2, "查询预约单轨迹接口","PreOrderQuery"), 
	CreateTransNO(3, "创建运单接口","CreateTransNO"),
	TransNOFeedBack(4, "运单状态反馈接口","TransNOFeedBack"),
	TransNOTraceQuery(5, "运单轨迹查询接口","TransNOQuery"),
	PackOpereate(6, "上传打包信息接口","PackOpereate"),
	UnPackOperate(7, "上传拆包信息接口","UnPackOperate"),
	VerifyTransNo(8, "检测运单是否属于包接口","VerifyTransNo"),
	DMPCarrierHandOver(9, "落地配承运商下发接口","DMPCarrierHandOver"),
	SnifferTransNoFeedBack(10, "落地配抓取运单反馈接口","SnifferTransNoFeedBack"),
	SnifferPerOrderNoFeedBack(11, "落地配抓取预约单反馈接口","SnifferPerOrderNoFeedBack"),
	MainTransNoGenerate(12, "主单号生成接口","MainTransNoGenerate");

	private Integer value;

	private String text;
	
	private String uniqueCode;

	private ExpressOperationEnum(Integer value, String text,String uniqueCode) {
		this.value = value;
		this.text = text;
		this.uniqueCode = uniqueCode;
	}

	public Integer getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}
	

	public String getUniqueCode() {
		return uniqueCode;
	}

	public static ExpressOperationEnum getByValue(Integer index) {
		for (ExpressOperationEnum typeEnum : ExpressOperationEnum.values()) {
			if (typeEnum.getValue().intValue() == index) {
				return typeEnum;
			}
		}
		return null;
	}

	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (ExpressOperationEnum e : ExpressOperationEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}
}
