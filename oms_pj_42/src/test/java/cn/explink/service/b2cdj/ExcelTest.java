package cn.explink.service.b2cdj;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Test;

import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpOrderFlow;

public class ExcelTest {
	/*
	@Test
	public void testLargeExcel(Object obj) throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{

		Class clazz = obj.getClass(); 
		
//		String heheh="getCredate";
//		
//		Method m1=clazz.getDeclaredMethod(heheh);
//		System.out.println("22222222222222222222:"+m1);
//		
//		//Method m1=clazz.getDeclaredMethod("getCredate");
//		
//		System.out.println(m1.invoke(clazz)); 
		
		Method method = clazz.getMethod("getCredate");
		
		System.out.println(method.invoke(obj));
	}
	
	public void testObjectArray(){
		
		String str="Dddddd";
		String abc="1";
		
		Object [] dd=new Object[]{"333","4",1};
		
		for(int i=0;i<dd.length;i++){
			
			System.out.println(dd[i]);
			
			if(dd[i] instanceof String){
				
				System.out.println("是字符串的是:"+dd[i]);
				
			}
		}
		
		if(abc instanceof String){
			
			System.out.println("dfdsfasdfasdf");
			
		}
		
		
		
		System.out.println(Character.isUpperCase(str.charAt(0)));
		
		
		System.out.println();
		
		int x=0;
		for(int i=0;i<10;i++){
			System.out.println("x:"+(++x));
			
		}
	}
	
	public static void main(String[] args) {
		try {
			
			System.out.println("adsfasdfadsfd");
			Timestamp ts =null;
			String tsStr = "2011-05-09 11:49:45";
			try {
				ts = Timestamp.valueOf(tsStr);
				System.out.println(ts);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			DmpOrderFlow test=new DmpOrderFlow();
			
			System.out.println("3333333333333333");
			
			test.setCredate(ts);
			
			
			
			new ExcelTest().testLargeExcel(test);
			
			new ExcelTest().testObjectArray();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
*/
}
