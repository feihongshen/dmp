package cn.explink.domain;

import java.sql.Timestamp;

public class TimeEffectiveVO {

	public static final String ID = "id";

	public static final String CODE = "code";

	public static final String NAME = "name";

	public static final String TIME_TYPE = "time_type";

	public static final String SCOPE = "scope";

	public static final String TS = "ts";

	public static final String DR = "dr";

	private int id = -1;

	private String code = null;

	private String name = null;

	private TimeTypeEnum timeType = null;

	private int timeTypeOrdinal = 0;

	private long scope = 0;

	private Timestamp ts = null;

	private int dr = 0;

	public TimeEffectiveVO() {
	}

	public TimeEffectiveVO(String id, String timeType, String hours, String minutes) {
		this.id = Integer.valueOf(id).intValue();
		this.timeType = TimeTypeEnum.values()[Integer.valueOf(timeType).intValue()];
		this.scope = this.getSeconds(hours, minutes);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TimeTypeEnum getTimeType() {
		return this.timeType;
	}

	public void setTimeType(TimeTypeEnum timeType) {
		this.timeType = timeType;
		this.timeTypeOrdinal = timeType.ordinal();
	}

	public int getTimeTypeOrdinal() {
		return this.timeTypeOrdinal;
	}

	public void setTimeTypeOrdinal(int timeTypeOrdinal) {
		this.timeTypeOrdinal = timeTypeOrdinal;
	}

	public long getScope() {
		return this.scope;
	}

	public void setScope(long scope) {
		this.scope = scope;
	}

	public Timestamp getTs() {
		return this.ts;
	}

	public void setTs(Timestamp ts) {
		this.ts = ts;
	}

	public int getDr() {
		return this.dr;
	}

	public void setDr(int dr) {
		this.dr = dr;
	}

	private long getSeconds(String hours, String minutes) {
		int hour = Integer.valueOf(hours).intValue();
		int minute = Integer.valueOf(minutes).intValue();
		return (hour * 3600) + (minute * 60);
	}

}
