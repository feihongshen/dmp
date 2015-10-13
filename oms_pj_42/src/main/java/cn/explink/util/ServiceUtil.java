package cn.explink.util;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class ServiceUtil {

	public static final String wavPath = "/wav/";
	public static final String jspPath = File.separator + "mould" + File.separator;
	public static final String xlsPath = "xls\\";

	public static String uploadWavFile(MultipartFile file, String filePath, String name) {
		try {

			File upfile = new File(filePath + name);
			file.transferTo(upfile);
			return name;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String uploadtxtFile(MultipartFile file, String filePath, String name) {
		try {
			File upfile = new File(filePath + name);
			file.transferTo(upfile);
			return name;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

}
