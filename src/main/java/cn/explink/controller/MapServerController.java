package cn.explink.controller;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.TruckDAO;
import cn.explink.domain.Truck;
/**
 * 
 * @author aaa
 *
 */
@RequestMapping("/gps")
@Controller
public class MapServerController {
	@Autowired
	TruckDAO truckdao;
	
	@RequestMapping("/getTruckInfo")
	@ResponseBody
	public List<Truck> getTruckInfo(){
		
		List<Truck> trucklist=truckdao.getAllTruck();	
			
		return trucklist;
		
	}
	
	
	
	 @RequestMapping(value = "/excelexport")    
	    public void exportExcel(HttpServletRequest request, HttpServletResponse response)     
	    throws Exception {    
	            
		 	List<Truck> trucklist=truckdao.getAllTruck();	    	        
	        XSSFWorkbook wb = export(trucklist);    
	        response.setContentType("application/vnd.ms-excel");    
	        response.setHeader("Content-disposition", "attachment;filename=TruckInfo.xls");    
	        OutputStream ouputStream = response.getOutputStream();    
	        wb.write(ouputStream);    
	        ouputStream.flush();    
	        ouputStream.close();    
	   }    
	
	  
    public static XSSFWorkbook export(List<Truck> list) {    
    	String[] excelHeader = { "did", "sid", "pid"};    
    	XSSFWorkbook wb = new XSSFWorkbook();    
    	XSSFSheet sheet = wb.createSheet("CarInfo");    
    	XSSFRow row = sheet.createRow((int) 0);    
    	
        for (int i = 0; i < excelHeader.length; i++) {    
        	XSSFCell cell = row.createCell(i);    
            cell.setCellValue(excelHeader[i]);    
            /*cell.setCellStyle(style);   */ 
            sheet.autoSizeColumn(i);    
        }    
    
        for (int i = 0; i < list.size(); i++) {    
            row = sheet.createRow(i + 1);    
            Truck truck = list.get(i);    
            row.createCell(0).setCellValue(truck.getTruckTerminalId());    
            row.createCell(1).setCellValue(truck.getTruckSimNum());    
            row.createCell(2).setCellValue(truck.getTruckno());    
        }      
        return wb;    
    }    
		
}
