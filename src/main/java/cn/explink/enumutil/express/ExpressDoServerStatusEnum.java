package cn.explink.enumutil.express;

import java.util.HashMap;
import java.util.Map;
/**
 * TPS DO服务中预约单状态
 * @author jiangyu 2015年8月28日
 *
 */
public enum ExpressDoServerStatusEnum {
	PlaceOrder(0, "已下单"), 
	MatchCarrier(10, "已分配省公司"), 
	DistributionOrg(20, "已分配站点"), 
	DistributionCourier(30, "已分配快递员"), 
	ReceiveSuccess(40, "揽件成功"), 
	ProvinceSurpass(50, "省公司超区"), 
	SiteSurpass(60, "站点超区"), 
	ReceiveSurpass(70, "揽件超区"), 
	ReceiveFail(80, "揽件失败"), 
	TicklingDelay(90, "反馈滞留"), 
	ReserveClose(100, "关闭");

	private Integer value;

	private String text;

	private ExpressDoServerStatusEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public Integer getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static ExpressDoServerStatusEnum getByValue(Integer index) {
		for (ExpressDoServerStatusEnum typeEnum : ExpressDoServerStatusEnum.values()) {
			if (typeEnum.getValue().intValue() == index) {
				return typeEnum;
			}
		}
		return null;
	}

	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (ExpressDoServerStatusEnum e : ExpressDoServerStatusEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}
}
