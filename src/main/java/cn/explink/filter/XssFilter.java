package cn.explink.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import cn.explink.core.utils.StringUtils;

public class XssFilter  extends HttpRequestWordFilter{

		@Override
		protected String filterParamValue(String paramValue) {
			return xssEncode(paramValue);
		}   


		/** 
	     * 将容易引起xss漏洞的半角字符直接替换成全角字符 </br>
	     * 如需再过滤其他HTML标签可在此配置
	     * @param s 
	     * @return 
	     */  
	    private  String xssEncode(String s) {   
	        return encodeHtmlTag(s, "script");
	    }
	    
	    /**
		 * 将HTML标签里的尖括号转换为html转义字符 
		 * @param content 待处理字符串内容
		 * @param tag html标签名
		 * @return
		 */
	    private  String encodeHtmlTag(String content, String tag) {
	    	//如果为空字符串，则直接返回
	    	if(StringUtils.isEmpty(content)){
	    		return content;
	    	}
	        //String regxp = "<\\s*"+tag+"[^>]*?>"+"|"+"<\\s*\\/\\s*"+tag+"[^>]*?>"; 
	    	String regxp = "([\\s\t\n\r>]*)<\\s*"+tag+"[^>]*?>"+"|"+"<\\s*\\/\\s*"+tag+"[^>]*?>([\\s\t\n\r<]*)";
	        Pattern pattern = Pattern.compile(regxp, Pattern.CASE_INSENSITIVE);   
	        Matcher matcher = pattern.matcher(content); 
	        boolean hasNext = matcher.find();  
	        if(!hasNext){
	        	return content;
	        }
	        StringBuffer encodeSb = new StringBuffer();  
	        do{ 
	        	//获取字符串里对应的tag
	        	String tagIncontent = matcher.group();
	        	tagIncontent = tagIncontent.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	            matcher.appendReplacement(encodeSb, tagIncontent);     
	        }while (matcher.find());
	        
	        matcher.appendTail(encodeSb);   
	        return encodeSb.toString();   
	    }
		

}
