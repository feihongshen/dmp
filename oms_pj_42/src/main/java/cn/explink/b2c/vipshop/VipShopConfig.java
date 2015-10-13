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
	public static String statusBackMethodName = "sendTrackInfo";

	public static String statusBackMethodNameNew = "carrierService"; // 新的webservice推送方法，唯品会已上线。

	public static String PEISONG_TYPE = "S103"; // 配送
	public static String TUIHUO_TYPE = "S113"; // 揽退

}
