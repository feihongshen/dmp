package cn.explink.b2c.yangguang;

import java.io.File;
import java.util.Calendar;

public class FileUtils {

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
	 * 不存在就创建 文件夹
	 * 
	 * @param uploadPath_bak
	 */
	public static void ifInExistsFileDirCreate(String file) {
		File Fupload_bak = new File(file);
		if (!Fupload_bak.exists()) {
			Fupload_bak.mkdirs();
		}
	}

}
