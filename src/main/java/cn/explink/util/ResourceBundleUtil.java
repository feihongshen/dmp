package cn.explink.util;

import java.util.ResourceBundle;

/**
 * 
 * 读取文件路径配置文件
 * 
 */
public class ResourceBundleUtil {
	private static ResourceBundle rbint = ResourceBundle.getBundle("filepath");
	// 承运商模版上传图片路径
	public static final String UPLOADIMGPATH = rbint.getString("uploadimgPath");
	// 上传声音路径
	public static final String WAVPATH = rbint.getString("wavPath");
	// 上传文件路径
	public static final String FILEPATH = rbint.getString("filePath");
	
	//异常件证据存放地址
	public static final String EXCEPTPATH=rbint.getString("excepturl");

	public static final String LOGOSWITCH = rbint.getString("logoswitch");

	public static final String WORD = rbint.getString("word");

	public static final String LOGOIMGURL = rbint.getString("logoimgurl");

	public static final String logotxd = rbint.getString("logotxd");
	public static final String LABEL = rbint.getString("label");

	public static final String addresscustomerid = rbint.getString("addresscustomerid");

	public static final String addressid = rbint.getString("addressid");

	public static final String addresspassword = rbint.getString("addresspassword");

}
