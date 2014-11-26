package cn.explink.enumutil;

public enum B2cPushStateEnum {
	WeiTuiSong(0, "未推送"), DaiTuiSong(1, "待推送"), TuiSongChengGong(2, "推送成功"), TuiSongShiBai(3, "推送失败"), ;
	private int value;
	private String text;

	private B2cPushStateEnum(int value, String text) {
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
