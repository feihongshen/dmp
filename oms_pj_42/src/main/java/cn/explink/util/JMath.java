package cn.explink.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class JMath {
	public JMath() {
	}

	public static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");

	public static boolean checknumletter(String str) {
		return getRealString(str).matches("^[a-zA-Z0-9-*_]+$");
	}

	public static String getRealString(String parameter) {
		return parameter != null ? parameter.replaceAll(String.valueOf((char) (127)), "").replaceAll(String.valueOf((char) (28)), "").replaceAll(String.valueOf((char) (29)), "")
				.replaceAll(String.valueOf((char) (31)), "") : "";

	}

	public static boolean checkdate(String somedate) throws ParseException {
		if (sdf1.parse(somedate) == null) {
			return false;
		} else if (sdf2.parse(somedate) == null) {
			return false;
		} else if (sdf3.parse(somedate) == null) {
			return false;
		}
		return true;
	}

	public static boolean checkPhoneNumber(String phoneNumber) {
		if (phoneNumber.length() != 11) {
			return false;
		}
		try {
			Long.parseLong(phoneNumber);
		} catch (NumberFormatException e) {
			return false;
		}

		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(phoneNumber);
		return m.matches();
	}

	public static String getmobileinstr(String str) {
		String mobile = "";
		if (str.length() == 11) {
			if (checkPhoneNumber(str.trim())) {
				mobile = str;
			}
		} else if (str.length() > 11) {
			for (int i = 0; i <= str.length() - 11; i++) {
				String temp = str.substring(i, i + 11).trim();
				if (checkPhoneNumber(temp)) {
					mobile = temp;
					break;
				}
			}
		}
		return mobile;
	}

	/**
	 * 取小数点后两位。
	 * 
	 * @param number
	 *            double
	 * @return String
	 */
	public static String numstring(double number) {
		java.text.DecimalFormat ad = new java.text.DecimalFormat("0.00"); // 取小数点后两位。
		return ad.format(number);

	}

	/**
	 * 取小数点后3位。
	 * 
	 * @param number
	 *            double
	 * @return String
	 */
	public static String num3string(double number) {
		java.text.DecimalFormat ad = new java.text.DecimalFormat("0.000"); // 取小数点后3位。
		return ad.format(number);

	}

	/**
	 * 导出excel设置cell值
	 * 
	 * @param row
	 * @param cols
	 * @param value
	 */
	public static void setCellValue(HSSFRow row, int cols, String value) { // 设置列数据
		HSSFCell cell = row.createCell((short) cols); // 设置第cols列
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(value);
	}

	/**
	 * 导出excel设置cell 数值型值
	 * 
	 * @param row
	 * @param cols
	 * @param value
	 */
	public static void setCellNumberValue(HSSFRow row, int cols, String value) { // 设置列数据
		HSSFCell cell = row.createCell((short) cols); // 设置第cols列
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(Double.parseDouble(value));
	}

	public static void CreateExcelHeader(HttpServletResponse response, String excelbranch) {
		try {
			excelbranch = new String(excelbranch.getBytes("GBK"), "iso8859-1");
		} catch (UnsupportedEncodingException e1) {

			e1.printStackTrace();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		GregorianCalendar thisday = new GregorianCalendar(); // 现在的时间
		String filename = format.format(thisday.getTime()).toString() + excelbranch; // 文件名
		response.setContentType("application/ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
	}

	public static void CreateOutPutExcel(HttpServletResponse response, HSSFWorkbook workbook) {
		ServletOutputStream fOut;
		try {
			fOut = response.getOutputStream();// 把相应的Excel 工作簿存盘
			workbook.write(fOut);
			fOut.flush();
			fOut.close();// 操作结束，关闭文件
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("文件生成...");
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
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		} catch (Exception e) {
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
			e.printStackTrace();
		}

	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @param sortflag
	 *            是否排序
	 * @return 拼接后字符串
	 * 
	 */
	public static String createLinkString(Map<String, String> params, boolean sortflag) {

		List<String> keys = new ArrayList<String>(params.keySet());
		if (sortflag) {
			Collections.sort(keys);
		}

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

}
