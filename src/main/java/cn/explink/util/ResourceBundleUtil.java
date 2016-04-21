package cn.explink.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 读取文件路径配置文件
 *
 */
public class ResourceBundleUtil {
	
	private static Logger logger = LoggerFactory.getLogger(ResourceBundleUtil.class);
	
	private static ResourceBundle rbint = null;
	//初始化
	 static {   
        String proFilePath = "/apps/conf/javaconf/"+ System.getProperty("company") + "/dmp-webapp.properties";
	    //开发使用
	    if(StringUtil.length(System.getProperty("development")) > 0){
	    	proFilePath = "dmp-webapp";//开发直接读取classpath下文件
	    	rbint = ResourceBundle.getBundle(proFilePath);
	    }else{
	    	BufferedInputStream inputStream = null;
	        try {  
	            inputStream = new BufferedInputStream(new FileInputStream(proFilePath));
	            rbint = new PropertyResourceBundle(inputStream);  
	        } catch (FileNotFoundException e) {  
	        	logger.error("生成配置，找不到配置文件");
	            e.printStackTrace();  
	        } catch (IOException e) {  
	        	logger.error("生成配置，读取配置文件失败"); 
	            e.printStackTrace();  
	        }  
	    }
	}  
	
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
	//快递 承运商编码
	public static final String expressCarrierCode = rbint.getString("expressCarrierCode");
	
	// 上传通联证书路径
	public static final String FileTongLianPath = ResourceBundleUtil.rbint.getString("fileTongLianPath");
	
	//sql_server的数据库配置信息
	public static final String sqlServerDrivers = ResourceBundleUtil.rbint.getString("sqlserver_drivers");
	public static final String sqlServerExpressurlMaxconn = ResourceBundleUtil.rbint.getString("sqlserver_expressurl.maxconn");
	public static final String sqlServerExpressurlUrl = ResourceBundleUtil.rbint.getString("sqlserver_expressurl.url");
	public static final String sqlServerExpressurlUser = ResourceBundleUtil.rbint.getString("sqlserver_expressurl.user");
	public static final String sqlServerExpressurlPassword = ResourceBundleUtil.rbint.getString("sqlserver_expressurl.password");
	public static final String sqlServerExpressurlBatchNum = ResourceBundleUtil.rbint.getString("sqlserver_expressurl.batchNum");
		
	// redis 前缀
	public static final String RedisPrefix = ResourceBundleUtil.rbint.getString("redis.prefix");
		
}
