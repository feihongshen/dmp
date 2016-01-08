package cn.explink.enumutil;

import java.util.Map;

public enum TransCwbStateEnum {
DIUSHI(1,"丢失"),POSUN(2,"破损"),PEISONG(3,"配送"),TUIHUO(4,"退货"),TUIGONGYINGSHANG(5,"退供应商"),ZHONGZHUAN(6,"中转");
	
	
	private int value;
	private String text;
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	

	
	private TransCwbStateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}
	public String getText() {
		return text;
	}
	public void setText(String test) {
		this.text = test;
	}
	public static TransCwbStateEnum getByValue(long value){
		for (TransCwbStateEnum em : TransCwbStateEnum.values()) {
			if(value==em.getValue()){
				return em;
			}
		}
		return null;
		
	}
	public static Map<Integer, String> getMap(){
		Map<Integer, String> map=new java.util.HashMap<Integer, String>();
		for (TransCwbStateEnum em : TransCwbStateEnum.values()) {
			map.put(em.getValue(), em.getText());
		}
		return map;
		
		
	}
	

}
