package cn.explink.b2c.vipshop;

/**
 * VipShop的配置信息.
 * 
 * @author Administrator
 *
 */

public class VipShopConfig {

	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 字符编码格式 目前支持 UTF-8 或gbk
	public static final String input_charset = "UTF-8";
	// 签名方式 不需修改
	public static String sign_type = "MD5";
	// 版本号
	public static String version = "1.0";

	public static String nameSpace = "http://common.tmsinterface.tms.com";
	public static String requestMethodName = "carrierService";
	/** 正常订单请求逻辑编号*/
	public static String CODE_S101 = "S101";
	/** 改造订单请求逻辑编号*/
	public static String CODE_S131 = "S131";
	/** 订单请求回抛编号 */
	public static String CODE_S141 = "S141";

}
