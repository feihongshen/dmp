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
	public static final String UPLOADIMGPATH = ResourceBundleUtil.rbint.getString("uploadimgPath");
	// 上传声音路径
	public static final String WAVPATH = ResourceBundleUtil.rbint.getString("wavPath");
	// 上传文件路径
	public static final String FILEPATH = ResourceBundleUtil.rbint.getString("filePath");
	//异常件证据存放地址
	public static final String EXCEPTPATH = ResourceBundleUtil.rbint.getString("excepturl");

	public static final String LOGOSWITCH = ResourceBundleUtil.rbint.getString("logoswitch");

	public static final String WORD = ResourceBundleUtil.rbint.getString("word");

	public static final String LOGOIMGURL = ResourceBundleUtil.rbint.getString("logoimgurl");

	public static final String logotxd = ResourceBundleUtil.rbint.getString("logotxd");
	public static final String LABEL = ResourceBundleUtil.rbint.getString("label");

	public static final String addresscustomerid = ResourceBundleUtil.rbint.getString("addresscustomerid");

	public static final String addressid = ResourceBundleUtil.rbint.getString("addressid");

	public static final String addresspassword = ResourceBundleUtil.rbint.getString("addresspassword");

	// 上传通联证书路径
	public static final String FileTongLianPath = ResourceBundleUtil.rbint.getString("fileTongLianPath");
}
