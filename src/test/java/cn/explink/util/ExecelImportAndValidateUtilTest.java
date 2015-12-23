package cn.explink.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Test;

import cn.explink.domain.express.CwbOrderForValidation;
import cn.explink.util.exception.ValidationExeception;

public class ExecelImportAndValidateUtilTest {
	@Test
	public void test_execel_validation() {
		String filename = "excel-validator/express/cod-bill-template.xml";// ExecelImportAndValidateUtilTest
		String execelName = "src/test/java/cn/explink/util/跨省代收货款对账（应收）账单明细导入测试.xls";// ExecelImportAndValidateUtilTest

		InputStream is = null;
		try {
			is = new FileInputStream(execelName);
		} catch (FileNotFoundException e) {
		}
		ExecelImportAndValidateUtil eu = null;
		try {
			eu = new ExecelImportAndValidateUtil(is, filename, CwbOrderForValidation.class);
		} catch (ValidationExeception e) {
			System.out.println(e.toString());
		}
		while (eu.hasNext()) {
			if (eu.validate()) {
				CwbOrderForValidation wh = (CwbOrderForValidation) eu.getObjectBean();
				System.out.println(wh.getTranscwb());
			} else {
				System.out.println(eu.getDescription());
				return;
			}
		}
	}

}
