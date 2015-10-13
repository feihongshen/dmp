package cn.explink.b2c.homegou;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
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

/**
 * FTP的工具类，包括上传，下载，等
 * 
 * @author Administrator
 *
 */
public class HomegouFTPUtils {
	private static Logger logger = LoggerFactory.getLogger(HomegouFTPUtils.class);

	private static FTPClient ftpClient = new FTPClient(); // FTP 客户端代理
	public static int sssssss = 1; // FTP状态码

	private String ftp_host; // ftp主机名 IP地址
	private String ftp_username; // 访问的用户名
	private String ftp_password; // 访问的密码
	private int ftp_port; // 端口号
	private String charencode; // 编码方式
	private boolean isDeleteFile = true; // 是否删除文件

	public HomegouFTPUtils(String ftp_host, String ftp_username, String ftp_password, int ftp_port, String charencode, boolean isdeleteFlag) {
		this.ftp_host = ftp_host;
		this.ftp_username = ftp_username;
		this.ftp_password = ftp_password;
		this.ftp_port = ftp_port;
		this.charencode = charencode;
		this.isDeleteFile = isdeleteFlag;
	}

	public HomegouFTPUtils() {

	}

	public static void main(String[] args) throws Exception {
		// DongFangCJ cj=new DongFangCJ();
		// cj.setFtp_host("221.204.213.42");
		// cj.setFtp_username("test_z");
		// cj.setFtp_password("123456");
		// cj.setFtp_port(21);
		// cj.setCharencode("UTF-8");
		// DongFangCJFTPUtils ftp=new
		// DongFangCJFTPUtils(cj.getFtp_host(),cj.getFtp_username(),cj.getFtp_password(),cj.getFtp_port(),cj.getCharencode());
		//
		// boolean isConnflag= openFtpConnection();
		// if(!isConnflag){
		// logger.warn("连接远程FTP异常,host={},username={},password="+ftp_password,cj.getFtp_host(),cj.getFtp_username());
		// return;
		// }
		// String remotePath="G:\\doc";
		// String localPath="F:\\doc1";
		//
		// downloadFileforFTP(remotePath,localPath);
	}

	/**
	 * 从FTP下载文件
	 * 
	 * @param remotePath
	 *            远程文件夹位置
	 * @param localPath
	 *            本地文件夹位置
	 * @throws IOException
	 */
	public void downloadFileforFTP(String remotePath, String localPath) throws Exception {
		boolean isConnflag = connectServer();
		if (!isConnflag) {
			logger.warn("连接远程FTP异常,host={},username={},password=" + this.ftp_password, this.ftp_host, this.ftp_username);
			return;
		}

		FTPFile[] ftpFiles = null;
		try {
			List<File> result = new ArrayList<File>();
			InputStream is = null;

			ftpClient.changeWorkingDirectory(remotePath); // 移动至远程文件夹的目录
			ftpFiles = ftpClient.listFiles();
			if (ftpFiles == null || ftpFiles.length == 0) {
				logger.warn("通过FPT获取文件为空,关闭本次连接 HOST=" + this.ftp_host);
				return;
			}

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
				logger.info("从0家有购物0-FTP-" + remotePath + "下载文件" + file.getName() + ",HOST={}", ftp_host);

			}

		} catch (Exception e) {
			logger.error("从FTP下载文件发生未知异常", e);
		} finally {

			deleteRemoteFtpServerFiles(remotePath, ftpFiles);

			ftpClient.logout();
			if (ftpClient.isConnected()) {
				ftpClient.disconnect();
				ftpClient = null;
			}
			logger.warn("FTP下载订单完毕,关闭连接,HOST=" + this.ftp_host);
		}

	}

	/**
	 * 删除远程服务器文件
	 * 
	 * @param remotePath
	 * @param ftpFiles
	 * @throws IOException
	 */
	private void deleteRemoteFtpServerFiles(String remotePath, FTPFile[] ftpFiles) throws IOException {
		if (isDeleteFile && ftpFiles != null && ftpFiles.length > 0) { // 删除文件
			for (FTPFile defile : ftpFiles) {
				ftpClient.deleteFile(defile.getName());
				logger.info("从0家有购物0-FTP-" + remotePath + "删除文件" + defile.getName() + ",HOST={}", ftp_host);
			}
		}
	}

	/**
	 * 列出当前工作目录下所有文件
	 *
	 * @param regStr
	 *            ftp目录
	 *
	 */
	public String[] listRemoteFileNames(String regStr) {

		String[] files = null;
		try {
			changeWorkingDirectory(regStr);
			files = ftpClient.listNames();
			if (files == null)
				return null;
			else {
				return files;
			}
		} catch (Exception e) {

			System.out.println("列出服务器上文件和目录失败");
			e.printStackTrace();
		}
		return files;
	}

	/**
	 * FTP下载文件 (单个)
	 * 
	 * @param remoteFileName
	 *            服务器上的文件名 (gg2.txt)
	 * @param localFileName
	 *            本地文件路径及文件名(F:/FTP/gg.txt)
	 * @param remoteDir
	 *            ftp目录（/text） return 下载到本地的文件名（F:/FTP/66.txt）
	 */
	public static boolean downloadFile(String[] remoteFileName, String[] localFileName, String remoteDir) {

		BufferedOutputStream buffOut = null;
		boolean flag = false;
		try {

			for (int i = 0; i < remoteFileName.length; i++) {
				buffOut = new BufferedOutputStream(new FileOutputStream(localFileName[i]));
				flag = ftpClient.retrieveFile(remoteFileName[i], buffOut);
				logger.info("FPT-DownLoading-remoteDir={},remoteFileName={}", remoteDir, remoteFileName[i]);
			}
		} catch (Exception e) {
			logger.error("从FTP下载信息发生不可预知的异常", e);
			return flag;
		} finally {
			try {
				if (buffOut != null)
					buffOut.close();
				closeConnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	// /**
	// * 检查FTP 是否关闭 ，如果关闭打开FTP
	// * @throws Exception
	// */
	// public boolean openFtpConnection() {
	// return connectServer();
	// }

	/**
	 * 关闭连接
	 */
	public static void closeConnect() {
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
	 * 连接到服务器
	 *
	 * @return true 连接服务器成功，false 连接服务器失败
	 */
	public boolean connectServer() {
		boolean flag = true;
		int reply = 0;
		try {
			ftpClient = new FTPClient();
			ftpClient.setControlEncoding(charencode);
			ftpClient.configure(getFtpConfig());
			ftpClient.connect(ftp_host);
			ftpClient.login(ftp_username, ftp_password);

			ftpClient.setDefaultPort(ftp_port);

			// ftpClient.enterRemotePassiveMode(); //20130528新增
			ftpClient.enterLocalPassiveMode();

			reply = ftpClient.getReplyCode();

			ftpClient.setDataTimeout(120000);

			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				logger.warn("FTP 服务拒绝连接！");
				flag = false;
			}
			sssssss++;
			logger.info("登录FTP服务器[{}]成功!", ftp_host);

		} catch (Exception e) {
			flag = false;
			logger.error("登录ftp服务器 " + ftp_host + " 失败,连接超时！", e);
			e.printStackTrace();
		}

		return flag;
	}

	/**
	 * 设置FTP客服端的配置--一般可以不设置
	 * 
	 * @return ftpConfig
	 */
	private static FTPClientConfig getFtpConfig() {
		FTPClientConfig ftpConfig = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
		ftpConfig.setServerLanguageCode(FTP.DEFAULT_CONTROL_ENCODING);
		return ftpConfig;
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
	 * 删除指定 文件
	 *
	 * @param remoteFileName
	 *            删除的文件名称
	 * @param remoteDir
	 *            删除文件所在服务器上的目录
	 * @return true 删除成功 false 删除失败
	 */
	public boolean deleteFile(String remoteFileName, String remoteDir) {

		try {
			changeWorkingDirectory(remoteDir);
			ftpClient.deleteFile(remoteFileName);
			return true;
		} catch (IOException e) {
			logger.error("IO异常-删除指定文件,", e);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取某个文件夹下的全部文件 根据创建时间排序 按时间的降序排序
	 * 
	 * @param localdownload
	 *            本地下载文件的路径
	 * @param localdownload_bak
	 *            本地下载文件路径备份
	 */
	public List<Map<String, String>> getCJdataListMultiDays(String localdownload, String localdownload_bak, int days) {
		File MyDir = new File(localdownload);
		String[] filelist = MyDir.list();
		if (!MyDir.exists()) { // 如果不存在,创建一个子目录
			MyDir.mkdirs();
		}
		File MyDirBak = new File(localdownload_bak);
		if (!MyDirBak.exists()) { // 如果不存在,创建一个子目录
			MyDirBak.mkdirs();
		}

		OrderByCreateTime(localdownload, filelist); // 排序
		List<Map<String, String>> showfilelist = BuildFileNameListAndMoveBak(localdownload, localdownload_bak, days, filelist); // 构建和移动文件

		return showfilelist;
	}

	/**
	 * 装载本地路径文件 ，备份文件
	 * 
	 * @param localdownload
	 * @param localdownload_bak
	 * @param days
	 * @param filelist
	 * @return
	 */
	private List BuildFileNameListAndMoveBak(String localdownload, String localdownload_bak, int days, String[] filelist) {
		// 把按顺序排好的文件装载到List中，显示在页面上
		List<Map<String, String>> showfilelist = new ArrayList<Map<String, String>>();
		if (filelist != null && filelist.length > 0) {
			for (int i = 0; i < filelist.length; i++) {
				String updatetime = getFileLastUpdateTime(localdownload + "/" + filelist[i]);
				if (DateTimeUtil.getDaysFromToTime(updatetime, DateTimeUtil.getNowTime()) <= days) { // 最近n天的
					Map map = new HashMap();
					map.put("filename", filelist[i]);
					map.put("filetime", updatetime);
					showfilelist.add(map);
					logger.info(filelist[i] + "创建时间:" + updatetime);
				} else { // n天之前的文件 移动到 localdownload_bak 目录
					String oldfilepathname = localdownload + "/" + filelist[i];
					String newfilepathname = localdownload_bak + "/" + filelist[i];
					moveFile(oldfilepathname, newfilepathname);
					logger.info("移动[" + days + "天前]文件[{}]到[{}]", oldfilepathname, newfilepathname);

				}
			}
		}
		return showfilelist;
	}

	/**
	 * 根据创建时间排序 按时间的降序排序
	 * 
	 * @param localdownload
	 * @param filelist
	 */
	private void OrderByCreateTime(String localdownload, String[] filelist) {

		if (filelist != null && filelist.length > 0) {
			int length = filelist.length; // 计算数组的长度
			for (int i = length - 1; i >= 0; i--) { // i表示最值被移到的位置
				for (int j = 0; j < i; j++) { // 通过冒泡寻找最值
					String filename = filelist[j];
					String filenamenext = filelist[j + 1];
					String updatetime = getFileLastUpdateTime(localdownload + "/" + filename);
					String updatetimenext = getFileLastUpdateTime(localdownload + "/" + filenamenext);
					String tempfile = "";
					if (updatetime.compareTo(updatetimenext) < 0) { // 如果逆序则交换
						tempfile = filelist[j];
						filelist[j] = filelist[j + 1];
						filelist[j + 1] = tempfile;
					}
				}
			}
		}
	}

	/**
	 * 获取文件最后修改时间
	 */
	public String getFileLastUpdateTime(String filepathandname) {
		File file = new File(filepathandname);
		Long time = file.lastModified();
		Calendar cd = Calendar.getInstance();
		cd.setTimeInMillis(time);
		java.util.Date d = cd.getTime();
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(d);
	}

	/**
	 * 移动文件到指定目录
	 * 
	 * @param oldPath
	 *            String 如：c:/fqf.txt
	 * @param newPath
	 *            String 如：d:/fqf.txt
	 */
	public static void moveFile(String oldPathFile, String newPathFile) {
		copyFile(oldPathFile, newPathFile);
		delFile(oldPathFile);
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				// int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					// System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 */
	public static void delFile(String filePathAndName) {
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			java.io.File myDelFile = new java.io.File(filePath);
			if (myDelFile.exists()) {
				myDelFile.delete();
			}

		} catch (Exception e) {
			logger.error("删除文件操作出错", e);
			e.printStackTrace();
		}

	}

	/**
	 * 文件上传
	 * 
	 * @param dirupload
	 * @param diruploadbak
	 * @param filename
	 * @throws IOException
	 */
	public void uploadFileToFTPByDongFangCJ(String dirupload, String diruploadbak, String filename, Homegou cj) throws Exception {
		String[] files = { filename };
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				moveFile(dirupload + files[i], diruploadbak + files[i]);
				logger.info("移动本地dongfangCJ文件[{}]到备份文件夹[{}]中...", files[i], diruploadbak);
			}
			// 移到bak文件夹之后再上传
			String[] localpathfile = new String[files.length];
			for (int i = 0; i < files.length; i++) {
				localpathfile[i] = diruploadbak + files[i];
			}
			if (files.length > 0) {
				boolean flag = connectServer();

				if (flag) {
					uploadFile(localpathfile, files, cj.getGet_remotePath()); // 上传完毕
					closeConnect();
				}
			}
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

		boolean flag = openFtpConnection();
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
					logger.info("====================[家有购物] 反馈txt文件" + remoteFileName[i] + " 上传FTP完毕 返回" + success);
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
		openFtpConnection();
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
			openFtpConnection();
			makeDir(dirName, remoteDir);

		}
	}

	/**
	 * 检查FTP 是否关闭 ，如果关闭打开FTP
	 *
	 * @throws Exception
	 */
	public boolean openFtpConnection() {

		if (null == ftpClient)
			return false;
		boolean flag = true;
		try {
			if (!ftpClient.isConnected()) {
				flag = connectServer();
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
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
		openFtpConnection();
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
