package cn.explink.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.naming.directory.DirContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.bridge.AbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.controller.DeliveryPercentController;

public final class FilesHelperUtils {
	
	private static Logger logger = LoggerFactory.getLogger(DeliveryPercentController.class);
	
	// 私有的构造函数，使此类不能实例化
	private FilesHelperUtils() {
		throw new Error("The class Cannot be instance !");
	}

	/**
	 * spring mvc文件上传方法(transferTo方法)MultipartFile使用transferTo方法上传
	 * 
	 * @param request
	 * @param multipartFile
	 * @param filePath
	 * @return
	 */
	public static String filesUploadSpringMvc(HttpServletRequest request, MultipartFile multipartFile,
			String filePath) {
		if (multipartFile != null) {
			// 获取文件后缀
			String suffix = multipartFile.getOriginalFilename()
					.substring(multipartFile.getOriginalFilename().lastIndexOf("."));
			// filepath+fileName 复杂的文件名称
			Map<String, String> map = getAndSetAbsolutePath(request, filePath, suffix);
			String savePath = map.get("savePath");
			String uuidName = map.get("uuidName");
			String absolutePath = savePath+uuidName;
			// 返回相对路径
			String relativePath = getRelativePath(filePath, suffix);
			try {
				// 上传文件
				multipartFile.transferTo(new File(absolutePath));
				// 返回相对路径
				return relativePath;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 流类型上传
	 * 
	 * @param request
	 * @param multipartFile
	 * @param filePath
	 * @return
	 */
	public static String fileUploadStream(HttpServletRequest request, MultipartFile multipartFile, String filePath) {
		if (multipartFile != null) {
			// 获取文件后缀
			String suffix = multipartFile.getOriginalFilename()
					.substring(multipartFile.getOriginalFilename().lastIndexOf("."));
			// 复杂的文件名
			Map<String, String> map = getAndSetAbsolutePath(request, filePath, suffix);
			String savePath = map.get("savePath");
			String uuidName = map.get("uuidName");
			String absolutePath = savePath+uuidName;
			logger.info("图片已存在服务器，存储位置为"+absolutePath+"");
			// 相对路径
			String relativePath = getRelativePath(filePath, uuidName);
			try {
				InputStream inputStream = multipartFile.getInputStream();
				FileOutputStream fileOutputStream = new FileOutputStream(absolutePath);
				// 缓存区
				byte buffer[] = new byte[1024];
				long fileSize = multipartFile.getSize();
				if (fileSize <= buffer.length) {
					buffer = new byte[(int) fileSize];
				}
				// 写入数据
				int line = 0;
				while ((line = inputStream.read(buffer)) > 0) {
					fileOutputStream.write(buffer, 0, line);
				}
				fileOutputStream.close();
				inputStream.close();
				// 返回相对路径
				return relativePath;
 			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return null;

	}

	/**
	 * 文档下载
	 * 
	 * @param request
	 * @param response
	 * @param filePath
	 */
	public static void fileDownloadStream(HttpServletRequest request, HttpServletResponse response, String filePath) {
		// 获取服务器的路径
		String realPath = request.getSession().getServletContext().getRealPath(filePath);
		File file = new File(realPath);
		String fileName = file.getName();
		// 流方式下载文件
		InputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[inputStream.available()];
			inputStream.read(buffer);
			inputStream.close();
			response.reset();
			// 先去掉文件名称中的空格,然后转换编码格式为utf-8,保证不出现乱码,这个文件名称用于浏览器的下载框中自动显示的文件名
			response.addHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.replace(" ", "")).getBytes("UTF-8"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			outputStream.write(buffer);
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 返回相对路径
	private static String getRelativePath(String filePath, String uuidName) {
		return filePath + File.separator + getDataPath() + File.separator + uuidName;
	}

	// 真正返回服务器绝对路径
	private static Map<String, String> getAndSetAbsolutePath(HttpServletRequest request, String filePath, String suffix) {
		String savePath = getServerPath(request, filePath) + File.separator + getDataPath() + File.separator;
		checkDir(savePath);
		Map<String, String> map = new HashMap<String, String>();
		map.put("savePath", savePath);
		map.put("uuidName", getUUIDName(suffix));
		return map;
	}

	// 返回一个UUID名称参数(后缀封面'。')例子:”.jpg . txt”
	private static String getUUIDName(String suffix) {
		return UUID.randomUUID().toString() + suffix;
	}

	// 检查路径是否已经存在,如果不创建它
	private static void checkDir(String savePath) {
		File file = new File(savePath);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdir();
		}
	}

	// 返回今天的日期
	private static String getDataPath() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}

	// 返回服务器绝对路径
	private static String getServerPath(HttpServletRequest request, String filePath) {
		return request.getSession().getServletContext().getRealPath(filePath);
	}
}
