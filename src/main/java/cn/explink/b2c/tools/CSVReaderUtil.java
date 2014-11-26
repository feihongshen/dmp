package cn.explink.b2c.tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVReaderUtil {

	private InputStreamReader fr = null;
	private BufferedReader br = null;

	public CSVReaderUtil(String f, String charencode) throws IOException {
		fr = new InputStreamReader(new FileInputStream(f), charencode);
	}

	/**
	 * 解析csv文件 到一个list中 每个单元个为一个String类型记录，每一行为一个list。 再将所有的行放到一个总list中
	 */
	public List<List<String>> readCSVFile() throws IOException {
		br = new BufferedReader(fr);
		String rec = null;// 一行
		String str;// 一个单元格
		List<List<String>> listFile = new ArrayList<List<String>>();
		try {
			// 读取一行
			while ((rec = br.readLine()) != null) {
				Pattern pCells = Pattern.compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");
				Matcher mCells = pCells.matcher(rec);
				List<String> cells = new ArrayList<String>();// 每行记录一个list
				// 读取每个单元格
				while (mCells.find()) {
					str = mCells.group();
					str = str.replaceAll("(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");
					str = str.replaceAll("(?sm)(\"(\"))", "$2");
					cells.add(str);
				}
				listFile.add(cells);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fr != null) {
				fr.close();
			}
			if (br != null) {
				br.close();
			}
		}
		return listFile;
	}

	public static void main(String[] args) throws Throwable {
		CSVReaderUtil test = new CSVReaderUtil("D:/Data/test.csv", "GBK");
		List<List<String>> csvList = test.readCSVFile();

		for (List<String> rowlist : csvList) {

			String cwb = rowlist.get(0);
			String transcwb = rowlist.get(1);
			String backtime = rowlist.get(2);
			String backreason = rowlist.get(3);
			String csremark = backtime + "收到移动对接审核未通过信息，需处理，原因：" + backreason;

			// System.out.println("当前处理移动对接CSV文件，内容："+csremark+",订单号="+cwb+",运单号="+transcwb);
		}
	}

}