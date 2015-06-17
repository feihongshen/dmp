package cn.explink.util;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import cn.explink.util.ExcelUtils;

public class ExcelUtilsHandler {


	public static <T>  void exportExcelHandler(HttpServletResponse response,final String cloumnZ[],final String cloumnName[],String sheetName,String fileName,final List<T> objList ){
		
		ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
			@Override
			public void fillData(Sheet sheet, CellStyle style) {
				for (int k = 0; k < objList.size(); k++) {
					Row row = sheet.createRow(k + 1);
					row.setHeightInPoints(15);
					for (int i = 0; i < cloumnZ.length; i++) {
						Cell cell = row.createCell((short) i);
						cell.setCellStyle(style);
						Object a = null;
						// 给导出excel赋值
						a = setObjectValue(cloumnName, objList.get(k), i);
						cell.setCellValue(a == null ? "" : a.toString());
					}
				}
			}
		};
		try {
			excelUtil.excel(response, cloumnZ, sheetName, fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Object setObjectValue(String[] cloumnName3, Object obj,int line)   {
		Object a=null;
		try {
			a = obj.getClass().getMethod("get" + cloumnName3[line]).invoke(obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a==null?"":a;
	}

}
