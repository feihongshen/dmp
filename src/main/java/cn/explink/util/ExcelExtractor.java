package cn.explink.util;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service("excelExtractor")
public abstract class ExcelExtractor {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ExcelExtractor.class);
	
	public String strtovalid(String str) {
		str = str.replaceAll("—", "-").replaceAll("，", ",");
		String cwb = "";
		for (int i = 0; i < str.length(); i++) {
			int asc = (int) str.charAt(i);
			if (asc >= 32 && asc <= 127 || // 英文字符，标点符号，数字
					(str.charAt(i) + "").matches("[\u4e00-\u9fa5]+")) { // //判断字符是否为中文
				cwb += str.charAt(i);
			}
		}
		return cwb;
	}

    private boolean isExsit(BigDecimal fee) {
    	if(fee!=null&&fee.compareTo(BigDecimal.ZERO)>0){
    		return true;
    	}else
    		return false;
	}


	private String removeZero(String cwb) {
		int end = cwb.indexOf(".0");
		if (end > -1) {
			cwb = cwb.substring(0, end);
		}
		return cwb;
	}

	public abstract String getXRowCellData(Object row, int cwbindex);

	public abstract String getXRowCellData(Object row, int cwbindex, boolean escapeAddress);

	public abstract String getXRowCellDateData(Object row, int cwbindex);

	public abstract List<Object> getRows(InputStream f);
	
	public abstract int getRowLength(Object row);
	
}