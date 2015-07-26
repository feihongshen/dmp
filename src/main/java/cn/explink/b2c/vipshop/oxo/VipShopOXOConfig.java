package cn.explink.b2c.vipshop.oxo;

/**
 * vip OXO订单下发接口配置信息
 * @author gordon.zhou
 *
 */

public class VipShopOXOConfig {

	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 字符编码格式 目前支持 UTF-8 或gbk
	public static final String input_charset = "UTF-8";
	// 签名方式 不需修改
	public static String sign_type = "MD5";
	// 版本号
	public static String version = "1.0";

	public static String nameSpace = "http://tps.vip.com/";
	public static String requestMethodName = "carrierService";

}
