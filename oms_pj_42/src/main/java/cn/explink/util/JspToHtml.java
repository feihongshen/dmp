package cn.explink.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class JspToHtml {

	/**
	 * 
	 * @param vmSourcePath
	 *            模板文件的路径
	 * @param htmlSourcePath
	 *            生成html文件的路径
	 * @param htmlSourceFileName
	 *            生成html的文件名称
	 * @throws Exception
	 *             当读取文件路径错误、数据流读写异常时向外抛出异常
	 */
	public static void jspToHtml(String vmSourcePath, String htmlSourcePath, String htmlSourceFileName) throws Exception {

		String templateContent = "";

		FileInputStream fileinputstream = new FileInputStream(vmSourcePath);// 读取模块文件
		int lenght = fileinputstream.available();// 读取文件长度
		byte bytes[] = new byte[lenght];// 将文件转换成字符流

		fileinputstream.read(bytes);// 读取字符流
		fileinputstream.close();// 关闭读取

		templateContent = new String(bytes);

		// 文件输出 生成html文件

		FileOutputStream fileoutputstream = new FileOutputStream(htmlSourcePath + "/" + htmlSourceFileName);// 建立文件输出流
		byte tag_bytes[] = templateContent.getBytes("utf-8");
		String t = new String(tag_bytes);// bytep[]转换为String
		fileoutputstream.write(tag_bytes);
		fileoutputstream.close();

	}

	public static void main(String[] args) {
		System.out.println("---------");
	}
}
