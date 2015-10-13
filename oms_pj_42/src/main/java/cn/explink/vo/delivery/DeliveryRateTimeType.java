package cn.explink.vo.delivery;

import java.util.HashMap;
import java.util.Map;

public class DeliveryRateTimeType {

	public static DeliveryRateTimeType hour12 = new DeliveryRateTimeType("hour12", 12, "12小时");

	public static DeliveryRateTimeType hour24 = new DeliveryRateTimeType("hour24", 24, "24小时");

	public static DeliveryRateTimeType hour36 = new DeliveryRateTimeType("hour36", 36, "36小时");

	public static DeliveryRateTimeType hour48 = new DeliveryRateTimeType("hour48", 48, "48小时");

	public static DeliveryRateTimeType hour60 = new DeliveryRateTimeType("hour60", 60, "60小时");

	public static DeliveryRateTimeType hour72 = new DeliveryRateTimeType("hour72", 72, "72小时");

	public static DeliveryRateTimeType hour96 = new DeliveryRateTimeType("hour96", 96, "96小时");

	public static DeliveryRateTimeType hour108 = new DeliveryRateTimeType("hour108", 108, "108小时");

	public static DeliveryRateTimeType hour120 = new DeliveryRateTimeType("hour120", 120, "120小时");

	public static DeliveryRateTimeType all = new DeliveryRateTimeType("all", 0, "120小时以上");

	public static DeliveryRateTimeType[] values = { hour12, hour24, hour36, hour48, hour60, hour72, hour96, hour108, hour120, all };

	private static Map<String, DeliveryRateTimeType> mapping = new HashMap<String, DeliveryRateTimeType>();
	static {
		mapping.put("hour12", hour12);
		mapping.put("hour24", hour24);
		mapping.put("hour36", hour36);
		mapping.put("hour12", hour12);
		mapping.put("hour48", hour48);
		mapping.put("hour60", hour60);
		mapping.put("hour72", hour72);
		mapping.put("hour96", hour96);
		mapping.put("hour108", hour108);
		mapping.put("hour120", hour120);
		mapping.put("all", all);
	}

	private String name;

	private Integer value;

	private String desc;

	private Boolean customization;

	private String cdTime;

	private CustomizedDeliveryDateType cdDateType;

	public DeliveryRateTimeType() {

	}

	public DeliveryRateTimeType(String name, Integer value, String desc) {
		this.name = name;
		this.value = value;
		this.desc = desc;
	}

	public static DeliveryRateTimeType valueOf(String timeType) {
		return mapping.get(timeType);
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Boolean isCustomization() {
		return customization;
	}

	public void setCustomization(Boolean customization) {
		this.customization = customization;
	}

	public String getCdTime() {
		return cdTime;
	}

	public void setCdTime(String cdTime) {
		this.cdTime = cdTime;
	}

	public CustomizedDeliveryDateType getCdDateType() {
		return cdDateType;
	}

	public void setCdDateType(CustomizedDeliveryDateType cdDateType) {
		this.cdDateType = cdDateType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return toJson();
	}

	public String toJson() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		boolean isFirst = true;
		if (value != null) {
			if (!isFirst) {
				builder.append(", ");
			}
			isFirst = false;
			builder.append("\"value\":\"").append(value).append("\"");
		}
		if (customization != null) {
			if (!isFirst) {
				builder.append(", ");
			}
			isFirst = false;
			builder.append("\"customization\":\"").append(customization).append("\"");
		}
		if (name != null) {
			if (!isFirst) {
				builder.append(", ");
			}
			isFirst = false;
			builder.append("\"name\":\"").append(name).append("\"");
		}
		if (desc != null) {
			if (!isFirst) {
				builder.append(", ");
			}
			isFirst = false;
			builder.append("\"desc\":\"").append(desc).append("\"");
		}
		if (cdTime != null) {
			if (!isFirst) {
				builder.append(", ");
			}
			isFirst = false;
			builder.append("\"cdTime\":\"").append(cdTime).append("\"");
		}
		if (cdDateType != null) {
			if (!isFirst) {
				builder.append(", ");
			}
			isFirst = false;
			builder.append("\"cdDateType\":\"").append(cdDateType).append("\"");
		}
		builder.append("}");
		return builder.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DeliveryRateTimeType)) {
			return false;
		}
		DeliveryRateTimeType other = (DeliveryRateTimeType) obj;
		if (this.name == null && other.name != null) {
			return false;
		}
		if (this.name != null && !this.name.equals(other.name)) {
			return false;
		}
		if (this.customization != other.customization) {
			return false;
		}
		if (this.cdDateType != other.cdDateType) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		StringBuilder sb = new StringBuilder();
		if (name != null) {
			sb.append(name);
		}
		if (value != null) {
			sb.append(value);
		}
		if (customization != null) {
			sb.append(customization);
		}
		if (cdDateType != null) {
			sb.append(cdDateType.toString());
		}

		return sb.toString().hashCode();
	}

}
