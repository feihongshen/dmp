package cn.explink.domain.addressvo;

import java.util.ArrayList;
import java.util.List;

/**
 * 地址匹配日志状态枚举
 * @author neo01.huang
 * 2016-4-13
 */
public enum AddressMatchLogMatchStatusEnum {

	SUCCESS("0", "成功"),
	NOT_MAINTENANCE_KEYWORD("1", "未维护关键字"),
	KEYWORD_NO_STATION("2", "关键字无对应站点"),
	MULTIPLE_RESULT("98", "multipleResult"),
	EXCEPTION_RESULT("99", "exceptionResult"),
	;
	
	private AddressMatchLogMatchStatusEnum(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public static AddressMatchLogMatchStatusEnum initKey(String key){
		for(AddressMatchLogMatchStatusEnum r: AddressMatchLogMatchStatusEnum.values()){
			if(key.equals(r.key())){
				return r;
			}
		}
		return null;
	}
	
	public static AddressMatchLogMatchStatusEnum initValue(String value){
		for(AddressMatchLogMatchStatusEnum r: AddressMatchLogMatchStatusEnum.values()){
			if(value.equals(r.value())){
				return r;
			}
		}
		return null;
	}
	
	public static String key2value(String key){
		if(key==null) return null;
		AddressMatchLogMatchStatusEnum rslt = initKey(key);
		if(rslt==null) return null;
		return rslt.value();
	}
	
	public static String value2key(String value){
		if(value==null) return null;
		AddressMatchLogMatchStatusEnum rslt = initValue(value);
		if(rslt==null) return null;
		return rslt.key()+"";
	}
	
	public static String[][] keyvalues(){
		AddressMatchLogMatchStatusEnum[] enmus = AddressMatchLogMatchStatusEnum.values();
		String[][] keyvalues = new String[enmus.length][2];
		for(int i=0;i<enmus.length;i++){
			AddressMatchLogMatchStatusEnum r = enmus[i];
			keyvalues[i] = new String[]{r.key(),r.value()};
		}
		return keyvalues;
	}
	
	public static List<String[]> strValues(){
		AddressMatchLogMatchStatusEnum[] enumsArr = AddressMatchLogMatchStatusEnum.values();
		if(enumsArr==null || enumsArr.length==0) return null;
		List<String[]> strValues = new ArrayList<String[]>();
		for(AddressMatchLogMatchStatusEnum enumsEntity : enumsArr){
			strValues.add(new String[]{enumsEntity.key()+"",enumsEntity.value()});
		}
		return strValues;
	}

	private String key;
	
	private String value;
	
	public String key() {
		return getKey();
	}
	
	public String value() {
		return getValue();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
