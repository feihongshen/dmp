package cn.explink.b2c.yangguang;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.explink.util.DateTimeUtil;

public class YangGuangFTPUtils {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static FTPClient ftpClient = new FTPClient(); // FTP 客户端代理

	private String host; // IP 地址
	private int port; // 端口
	private String uname; // 用户名
	private String pwd; // 密码
	private String charencode = "GBK"; // 编码方式UTF-8;
	private int timeout = 20000; // 设置超时时间 单位：毫秒 默认20秒
	private boolean isDeleteFile = false; // 是否删除文件 默认true
	private static String SERVER_SYS = "WINDOWS";

	public YangGuangFTPUtils(String host, int port, String uname, String pwd, String charencode, int timeout, boolean isDeleteFile, String SERVER_SYS) {
		this.host = host;
		this.port = port;
		this.uname = uname;
		this.pwd = pwd;
		this.charencode = charencode;
		this.isDeleteFile = isDeleteFile;
		this.timeout = timeout;
		this.SERVER_SYS = SERVER_SYS;
		if (SERVER_SYS.equalsIgnoreCase("linux")) {
			this.SERVER_SYS = "UNIX";
		}
	}

	/**
	 * 连接到服务器
	 *
	 * @return true 连接服务器成功，false 连接服务器失败
	 */
	public boolean connectServer() {
		int reply = 0;
		try {
			ftpClient = new FTPClient();
			ftpClient.setControlEncoding(charencode);
			ftpClient.configure(getFtpConfig());
			ftpClient.connect(host);
			ftpClient.login(uname, pwd);
			ftpClient.setDefaultPort(port);
			ftpClient.enterLocalPassiveMode(); // ftpClient.enterRemotePassiveMode();
												// //20130528新增 //windows不生效
			// ftpClient.enterRemotePassiveMode();
			reply = ftpClient.getReplyCode();
			ftpClient.setDataTimeout(timeout);

			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				logger.warn("FTP 服务拒绝连接！");
				return false;
			}
			logger.info("登录FTP服务器{}成功!", host);

			ftpClient.setSoTimeout(30000); // 设置读超时

			return true;

		} catch (Exception e) {
			logger.error("登录ftp服务器 " + host + " 失败,连接超时！", e);
			return false;
		}
	}

	/**
	 * 设置FTP客服端的配置--一般可以不设置 windows SYST_NT 、linux/uniux
	 * 
	 * @return ftpConfig
	 */
	private static FTPClientConfig getFtpConfig() {
		FTPClientConfig ftpConfig = new FTPClientConfig(SERVER_SYS);
		ftpConfig.setServerLanguageCode(FTP.DEFAULT_CONTROL_ENCODING);
		return ftpConfig;
	}

	/**
	 * 从FTP下载文件
	 * 
	 * @param remotePath
	 * @param localPath
	 * @throws IOException
	 */
	public void downloadFileforFTP(String remotePath, String localPath, String localPath_bak) throws Exception {
		boolean isConnflag = connectServer();
		if (!isConnflag) {
			logger.warn("连接远程FTP异常,host={},username={}", host, uname);
			return;
		}

		FTPFile[] ftpFiles = null;
		try {
			List<File> result = new ArrayList<File>();
			InputStream is = null;
			ftpClient.changeWorkingDirectory(remotePath); // 移动至远程文件夹的目录
			ftpFiles = ftpClient.listFiles();
			if (ftpFiles == null || ftpFiles.length == 0) {
				logger.warn("通过FPT获取文件为空,关闭本次连接host={}", host);
				return;
			}
			// ftpClient.addProtocolCommandListener(new PrintCommandListener(new
			// PrintWriter(System.out)));
			FileOutputStream os = null;

			for (FTPFile file : ftpFiles) {
				int flag = 0;
				try {
					String filedate = DateTimeUtil.formatDateDay(file.getTimestamp().getTime());
					if (!file.getName().endsWith(".txt")) {
						continue;
					}
					if (!filedate.equals(DateTimeUtil.getNowDate())) {
						// logger.warn("下载时间不对,已过滤filename={},filedate={}",file.getName(),filedate);
						continue;
					}

					is = ftpClient.retrieveFileStream(file.getName());
					if (is == null) {
						ftpClient.completePendingCommand();
						continue;
					}

					File MyDir = new File(localPath_bak);
					String[] filelist = MyDir.list();
					for (String fi : filelist) {
						if (file.getName().equals(fi)) {
							logger.warn("该文件已存在不需要下载,已过滤filename={},filedate={}", file.getName(), filedate);
							ftpClient.completePendingCommand();
							flag = 1;
							break;
						}
					}

					if (flag == 1) {
						continue;
					}

					if (localPath != null && !localPath.endsWith(File.separator)) {
						localPath = localPath + File.separator;
						File path = new File(localPath);
						if (!path.exists()) { // 如果不存在,创建一个子目录
							path.mkdirs();
						}
					}
					File fileOut = new File(localPath + file.getName());
					os = new FileOutputStream(fileOut);

					byte[] bytes = new byte[1024];
					int c;
					while ((c = is.read(bytes)) != -1) {
						os.write(bytes, 0, c);
					}

					result.add(fileOut);
					logger.info("从FTP-host={}" + remotePath + "下载文件成功" + file.getName(), host);
				} catch (Exception e) {
					logger.error("从FTP-host={}" + remotePath + "下载文件失败" + file.getName() + host, e);
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			logger.error("从FTP下载文件发生未知异常,return", e);
		} finally {

			if (isDeleteFile && ftpFiles != null && ftpFiles.length > 0) { // 删除文件
				for (FTPFile defile : ftpFiles) {
					ftpClient.deleteFile(defile.getName());
					logger.info("从-FTP-host={}-" + remotePath + "删除文件={}", host, defile.getName());
				}
			}

			ftpClient.logout();
			if (ftpClient.isConnected()) {
				ftpClient.disconnect();
				ftpClient = null;
			}
			logger.info("FTP下载订单完毕,关闭连接,host={}", host);
		}

	}

	public static void main(String[] args) {
		List<Map.Entry<String, String>> mappingList = null;

		Map<String, String> map = new HashMap<String, String>();

		map.put("2013-08-09 20:00:00_1", "month");
		map.put("2013-08-10 00:00:00_2", "bread");
		map.put("2013-08-09 20:00:00_3", "attack");

		// 通过ArrayList构造函数把map.entrySet()转换成list
		mappingList = new ArrayList<Map.Entry<String, String>>(map.entrySet());
		// 通过比较器实现比较排序
		Collections.sort(mappingList, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> mapping1, Map.Entry<String, String> mapping2) {
				return mapping1.getKey().compareTo(mapping2.getKey());
			}
		});

		for (Map.Entry<String, String> mapping : mappingList) {
			System.out.println(mapping.getKey() + ":" + mapping.getValue());
		}

	}

}
