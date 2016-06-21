package cn.explink.enumutil;
/**
 * EMS与DMP,退货原因对照
 * @author huan.zhou
 */
public enum EmsToDmpReasonEnum {

	ShouJianRenMingZhiYouWu("101", "收件人名址有误",590 ),
	ChaWuChiRen("102", "查无此人",590 ),
	ShouJianRenBuZaiZhiDingDiZhi("103", "收件人不在指定地址",587 ),
	JuShouTuiHui("104", "拒收退回",807 ),
	ShouJianRenYaoQiuYanChi("105", "收件人要求延迟投递",592 ),
	WuTou("106", "误投",590 ),
	YouJianCuoFa("107", "邮件错发",597 ),
	PoSunWuFaTouChu("108", "收到时破损，无法投出",585 ),
	DaiShouFeiHou("111", "待收费后",592 ),
	WuRenRenLing("112", "无人认领",590 ),
	WuFaZhaoDaoShouJianRen("113", "无法找到收件人",590 ),
	BuKeKangYuanYin("114", "因不可抗力原因，邮件未投出",592 ),
	ShouJianRenYaoQiuZiQu("115", "收件人要求自取",592 ),
	FaDingJiaRiWuFaTouDi("116", "法定假日，无法投递",590 ),
	//YouJianDiuShi("117", "邮件丢失",590 ),
	RenYiTaWang("118", "人已他往",587 ),
	//ShouJianRenYouXinXiang("119", "收件人有信箱",590 ),
	//AnPaiTouDi("120", "安排投递",590 ),
	//ZhengZaiTouDi("121", "正在投递",590 ),
	ChaWuCiDanWei("122", "查无此单位",590 ),
	DiZhiBuXiangDianHuaBuTong("123", "地址不祥，无电话，电话不对",590 ),
	DiZhiBuXiang("124", "地址不详",590 ),
	CheHui("125", "撤回",581 ),
	QianYiXinZhiBuMing("126", "迁移新址不明",590 ),
	YuQiWeiLing("127", "逾期未领",592 ),
	//ShouJianRenYouWu("128", "投递到包裹站",590 ),
	YuQiShouHui("129", "逾期投递员收回",592 );
    //qita("130", "其他",590 )
	
	private String key;
	private String text;
	private int reasonId;//dmp退货原因编号


	private EmsToDmpReasonEnum(String key, String text, int reasonId) {
		this.key = key;
		this.text = text;
		this.reasonId = reasonId;
	}

	public static EmsToDmpReasonEnum getEnumByKey(String key){
		EmsToDmpReasonEnum[] enums = EmsToDmpReasonEnum.values();
		for(EmsToDmpReasonEnum entity : enums){
			if(entity.getKey() == key){
				return entity;
			}
		}
		return null;
	}
	
	//根据ems退货编号获取dmp退货编号
	public static Integer getReasonIdByKey(String key){
		for (EmsToDmpReasonEnum reason : EmsToDmpReasonEnum.values()) {
			if(reason.getKey().equals(key)){
				return reason.getReasonId();
			}
		}
		return null;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getReasonId() {
		return reasonId;
	}

	public void setReasonId(int reasonId) {
		this.reasonId = reasonId;
	}

}
