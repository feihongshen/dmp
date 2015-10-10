package cn.explink.test;


/**
 * 所有的账务类型 属性
 * @author Administrator
 *
 */
public class AccountAttrVo {
	public static String chongzhi="chongzhi"; //充值金额
	public static String tuotou="tuotou"; //妥投
	public static String diushi="diushi"; //丢失
	public static String zhongzhuan="zhongzhuan"; //中转
	public static String tuihuo="tuihuo"; //退货
	
	public static String weiwanjie="weiwanjie"; //未完结订单
	
	
	
	////////////退货//////////////////////////////
	public static String tuihuo_weifankuan="tuihuo_weifankuan"; //退货未返款?（此状态不使用）
	
	public static String jsWTHCZAmount ="jsWTHCZAmount"; //拒收未退货出站
	public static String thczWrkAmount ="thczWrkAmount"; //退货出站（未入库）
	public static String tuihuozhanruku_weifankuan="tuihuozhanruku_weifankuan"; //退货站入库未返款
	
	
	
	////////////中转/////////////////
	public static String zhongzhuanchuzhan="zhongzhuanchuzhan"; //中转出站
	public static String zhongzhuanzhanruku_weifankuan="zhongzhuanzhanruku_weifankuan"; //中转站入库未返款
	
	///差异信息，这里暂时不统计
	public static String chongfuchuku="chongfuchuku";//重复出库
	public static String tuotou_chayi="tuotou_chayi"; //妥投差异
	public static String diushi_chayi="diushi_chayi"; //丢失差异
	
	
	
	
}
