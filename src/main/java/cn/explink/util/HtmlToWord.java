package cn.explink.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class HtmlToWord {

	public static boolean writeWordFile(String content, String fileName) {
		boolean w = false;
		String path = "d:/exprotFile";
		try {
			if (!"".equals(path)) {
				if (!path.endsWith(File.separator)) {
					path = path + File.separator;
				}
				// 检查目录是否存在
				File fileDir = new File(path);
				if (!fileDir.exists()) {
					if (!fileDir.mkdirs()) {
						return w;
					}
				}
				byte b[] = content.getBytes("GBK");
				ByteArrayInputStream bais = new ByteArrayInputStream(b);
				POIFSFileSystem poifs = new POIFSFileSystem();
				DirectoryEntry directory = poifs.getRoot();
				DocumentEntry documentEntry = directory.createDocument(
						"WordDocument", bais);
				FileOutputStream ostream = new FileOutputStream(path + fileName);
				poifs.writeFilesystem(ostream);
				bais.close();
				ostream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return w;
	}

	public static void main(String[] args) {
		String content = "";
		// 生成临时文件名称
		String fileName = "派费汇总表.doc";
		writeWordFile(content, fileName);
	}
}
