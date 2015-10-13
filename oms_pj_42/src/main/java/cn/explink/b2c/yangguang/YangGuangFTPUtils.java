package cn.explink.b2c.yangguang;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.explink.util.JMath;

public class YangGuangFTPUtils {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static FTPClient ftpClient = new FTPClient(); // FTP 客户端代理

	private String host; // IP 地址
	private int port; // 端口
	private String uname; // 用户名
	private String pwd; // 密码
	private String charencode = "GBK"; // 编码方式UTF-8;
	private int timeout = 20000; // 设置超时时间 单位：毫秒 默认20秒
	private boolean isDeleteFile = true; // 是否删除文件 默认true
	private static String SERVER_SYS = "WINDOWS";

	public YangGuangFTPUtils(String host, int port, String uname, String pwd, String charencode, int timeout, boolean isDeleteFile, String SERVER_SYS) {
		this.host = host;
		this.port = port;
		this.uname = uname;
		this.pwd = pwd;
		this.charencode = charencode;
		this.isDeleteFile = isDeleteFile;
		this.timeout = timeout;
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
			reply = ftpClient.getReplyCode();
			ftpClient.setDataTimeout(timeout);

			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				logger.warn("FTP 服务拒绝连接！");
				return false;
			}
			logger.info("登录FTP服务器{}成功!", host);
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
	public void downloadFileforFTP(String remotePath, String localPath) throws Exception {
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

			for (FTPFile file : ftpFiles) {
				is = ftpClient.retrieveFileStream(file.getName());
				if (is == null) {
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
				FileOutputStream os = new FileOutputStream(fileOut);

				byte[] bytes = new byte[1024];
				int c;
				while ((c = is.read(bytes)) != -1) {
					os.write(bytes, 0, c);
				}

				result.add(fileOut);
				ftpClient.completePendingCommand();
				os.flush();
				is.close();
				os.close();
				logger.info("从FTP-host={}" + remotePath + "下载文件" + file.getName(), host);

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

	/**
	 * FTP上传
	 * 
	 * @param dirupload
	 * @param diruploadbak
	 * @param filename
	 * @throws IOException
	 */
	public void uploadFileToFTPServer(String dirupload, String diruploadbak, String filename, String remotePath) throws Exception {
		String[] files = { filename };
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				JMath.moveFile(dirupload + files[i], diruploadbak + files[i]);
				logger.info("移动本地文件[{}]到备份文件夹[{}]中...", files[i], diruploadbak);
			}
			// 移到bak文件夹之后再上传
			String[] localpathfile = new String[files.length];
			for (int i = 0; i < files.length; i++) {
				localpathfile[i] = diruploadbak + files[i];
			}
			if (files.length > 0) {
				boolean flag = connectServer();

				if (flag) {
					uploadFile(localpathfile, files, remotePath); // 上传完毕
					closeConnect();
				}
			}
		}
	}

	/**
	 * 关闭连接
	 */
	public void closeConnect() {
		try {
			if (ftpClient != null) {
				ftpClient.logout();
				ftpClient.disconnect();
				ftpClient = null;
				logger.info("关闭 FTP 服务器连接...");
			}
		} catch (Exception e) {
			logger.error("关闭FTP服务器失败，FTP服务器无法打开！", e);
		}
	}

	/**
	 * 上传多个文件到ftp
	 *
	 * @param localFilePath
	 *            本地文件路径及名称(F:\cms\gg.txt)
	 * @param remoteFileName
	 *            FTP 服务器文件名称(gg2.txt)
	 * @param remoteDir
	 *            FTP 服务器目录(/text) 如果ftp服务器text目录不存在，将会自动创建
	 * @return
	 */
	public boolean uploadFile(String[] localFilePath, String[] remoteFileName, String remoteDir) {

		boolean flag = connectServer();
		if (!"".equals(remoteDir)) {
			makeDirs(remoteDir);
		}
		BufferedInputStream inStream = null;
		boolean success = false;

		try {
			if (flag) {
				for (int i = 0; i < localFilePath.length; i++) {
					inStream = new BufferedInputStream(new FileInputStream(localFilePath[i]));
					logger.info("===================本地文件localFilePath==" + localFilePath[i]);
					if (!"".equals(remoteDir)) {
						changeWorkingDirectory(remoteDir);
					}
					success = ftpClient.storeFile(remoteFileName[i], inStream);
					logger.info("====================[FTP] 反馈txt文件" + remoteFileName[i] + " 上传FTP完毕 返回" + success);
				}
			} else {
				logger.info("===================上传文件失败  FTP连接异常===============");
			}
		} catch (FileNotFoundException e) {
			logger.error("文件未找到");
		} catch (IOException e) {
			logger.error("IO异常");
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					logger.error("IO异常");
				}
			}
		}

		return success;
	}

	/**
	 * 创建此抽象路径名指定的目录，包括所有必需但不存在的父目录
	 *
	 * @param remoteDir
	 *            ftp目录
	 * @return 状态值
	 */
	public boolean makeDirs(String remoteDir) {
		boolean flag = false;
		String dirs[] = remoteDir.split("/");
		String parentName = "";
		for (int i = 0; i < dirs.length; i++) {
			if (i + 1 != dirs.length) {
				if (i == 0) {
					flag = isExists(dirs[i + 1], "/");
					parentName = "/";
					isCreateDir(flag, dirs[i + 1], parentName);
				} else {
					flag = isExists(dirs[i + 1], parentName + dirs[i]);
					parentName += dirs[i] + "/";
					isCreateDir(flag, dirs[i + 1], parentName);
				}
			}
		}
		return true;
	}

	/**
	 * 代替检查目录或文件是否存在
	 *
	 * @param dirName
	 *            本地目录名称(d:f.txt)
	 * @param remoteDir
	 *            ftp目录名称(/f)
	 * @return 状态
	 */
	public boolean isExists(String dirName, String remoteDir) {
		connectServer();
		List<String> retList = null;
		boolean falg = false;
		try {
			FTPFile[] ftpFiles = ftpClient.listFiles(remoteDir);
			retList = new ArrayList<String>();
			if (ftpFiles == null) {
				return false;
			}
			for (int i = 0; i < ftpFiles.length; i++) {
				FTPFile file = (FTPFile) ftpFiles[i];
				if (file.isDirectory()) {
					if (dirName.equals(file.getName().toString()))
						return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return falg;
	}

	/**
	 * 没有文件夹创建该文件夹
	 *
	 * @param flag
	 * @param dirName
	 * @param remoteDir
	 */
	private void isCreateDir(boolean flag, String dirName, String remoteDir) {
		if (!flag) {
			connectServer();
			makeDir(dirName, remoteDir);

		}
	}

	/**
	 * 进入到服务器的某个目录下
	 * 
	 * @param directory
	 */
	public void changeWorkingDirectory(String directory) {
		try {

			ftpClient.changeWorkingDirectory(directory);
		} catch (IOException ioe) {
			logger.error("登录ftp服务器目录 " + directory + " 失败！", ioe);
		}
	}

	/**
	 * 创建一个给定的新的子目录路径是在FTP服务器上的当前
	 *
	 * @param dirPath
	 *            目录名称
	 * @param remoteDir
	 *            ftp目录
	 * @return 状态值 true 创建成功 false 创建失败
	 */
	public boolean makeDir(String dirPath, String remoteDir) {
		connectServer();
		boolean success = false;
		try {
			changeWorkingDirectory(remoteDir);
			success = ftpClient.makeDirectory(dirPath);
		} catch (IOException e) {
			System.out.println("IO异常");
			e.printStackTrace();
		}
		return success;
	}
}
