/*<br /><br />
Code highlighting produced by Actipro CodeHighlighter (freeware)<br />
http://www.CodeHighlighter.com/<br /><br />
 */
package cn.explink.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.cypher.internal.compiler.v2_1.docbuilders.internalDocBuilder;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class WordUtils {
	private Configuration configuration = null;

	@SuppressWarnings("deprecation")
	public WordUtils() {
		configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
	}

	public void createDoc(String templateFile, String exportFile,
			Map<String, Object> dataMap) {
		// 要填入模本的数据文件
		/*
		 * Map<String, Object> dataMap = new HashMap<String, Object>();
		 * getData(dataMap);
		 */
		// 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
		// 这里我们的模板是放在com.havenliu.document.template包下面
		configuration.setClassForTemplateLoading(this.getClass(), "/template");
		Template t = null;
		try {
			// test.ftl为要装载的模板
			t = configuration.getTemplate(templateFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 输出文档路径及名称 For example: outFile.doc
		String exportPath = "D:/exportFile/" + exportFile;
		File outFile = new File(exportPath);
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outFile), "utf-8"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			t.process(dataMap, out);
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 注意dataMap里存放的数据Key值要与模板中的参数相对应
	 * 
	 * @param dataMap
	 */
	private void getData(Map<String, Object> dataMap) {
		dataMap.put("author", "张三");
		dataMap.put("remark", "这是测试备注信息");
	}

	public static void main(String[] args) {
		WordUtils wordUtils = new WordUtils();
		String templateFile = "branchDeliveryFeeBill.ftl";
		String exportFile = "branchDeliveryFeeBill.doc";
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		dataMap.put("nowYear", year);
		dataMap.put("nowMonth", month);
		System.out.println("------------------------------year:" + year);
		wordUtils.createDoc(templateFile, exportFile, dataMap);
	}
}
