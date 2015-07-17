package cn.explink.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SalaryQueryService {
	private static final String MOHUPIPEI="模糊匹配";
	public String listToString(List<String> list){
		String zhuiString="'";
		String batchs="";
		StringBuffer buffer=new StringBuffer();
		if (list!=null&&list.size()!=0) {
			for (String string : list) {
			buffer.append(zhuiString).append(string).append(zhuiString).append(",");
			}
		}
		if (buffer.length()>0) {
			batchs=buffer.substring(0, buffer.length()-1).toString();
		}
		return batchs;
	}
	public String checkParameterisNull(String parameter){
		String parameterString="";
		if (!SalaryQueryService.MOHUPIPEI.equals(parameter)) {
			parameterString=parameter;
		}
		return parameterString;
	}
}
