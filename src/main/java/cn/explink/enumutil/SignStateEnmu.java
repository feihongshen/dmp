package cn.explink.enumutil;

public enum SignStateEnmu {
	BenRenQianShou(1, "本人签收"), QianTaiQianShou(2, "前台签收"), MenWeiQianShou(3, "门卫签收"), TaRenDaiQian(4, "他人代签");

	private int key;
	private String value;

	private SignStateEnmu(int key, String value) {
		this.key = key;
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static SignStateEnmu getText(long key) {
		for (SignStateEnmu e : SignStateEnmu.values()) {
			if (e.getKey() == key) {
				return e;
			}
		}
		return null;
	}

}
