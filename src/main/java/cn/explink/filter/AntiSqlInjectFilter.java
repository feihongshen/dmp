package cn.explink.filter;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.explink.core.utils.StringUtils;

public class AntiSqlInjectFilter extends HttpRequestWordFilter {
	
	final static String SQL_ERROR_PRE = "SqlErr";
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		 //捕获你抛出的业务异常
		try {
        	super.doFilter(req, res, chain);
		} catch (Exception e) {
			String message = e.getMessage();
			//如果是SQL错误，错误信息不抛出
			if(isSqlGrammarException(message)){
				//用于在日志上标识出唯一错误编号
				String ticketNo = SQL_ERROR_PRE + "-" + System.currentTimeMillis();
				logger.error(SQL_ERROR_PRE,ticketNo + " :" + getPrintStack(e));
				throw new RuntimeException("错误代号："+ticketNo + "   请联系技术支持同事。");
			//如果不是SQL错误，错误信息抛出
			}else{
				throw e;
			}
		}
	}
	
	/**
	 * 判断是否为sql类型错误
	 * @param message
	 * @return
	 */
	private boolean isSqlGrammarException(String message){
		if(message==null || message.trim().equals("")){
			return false;
		}
		String tempMsg = message.trim().toUpperCase();
		if(tempMsg.indexOf("BADSQLGRAMMAR")>0 
				|| tempMsg.indexOf("BAD SQL GRAMMAR")>0
				|| tempMsg.indexOf("MYSQL")>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取错误堆栈信息
	 * @param exception
	 * @return
	 */
	 public String getPrintStack(Exception exception){
		if(exception==null){
			return "";
		}
		StringBuffer info = new StringBuffer();
		info.append(exception.getMessage());
		Writer result = null;
		PrintWriter printWriter = null;
		try{
			result = new StringWriter();
			printWriter = new PrintWriter(result);
			exception.printStackTrace(printWriter);
			info.append(result.toString());
		}catch(Exception e){
			
		}finally{
			if(result!=null){
				try{
					result.close();
				}
				catch(Exception e){
				}
			}
			if(printWriter!=null){
				try{
					printWriter.close();
				}
				catch(Exception e){
				}
			}
		}
		return info.toString();
	}

	@Override
	protected String filterParamValue(String content) {
		// 如果为空字符串，则直接返回
		if (StringUtils.isEmpty(content)) {
			return content;
		}
		// 过滤information_schema
		String tempContent = content.replace("'", "\\'").replaceAll("(?i)information_schema", "ＩＮＦＯＲＭＡＴＩＯＮ＿ＳＣＨＥＭＡ"); 
		// 下面是正则替换
		StringBuffer regxpSb = new StringBuffer();
		// 过滤select from
		// ([\\s\t\n\r]{1,})表示至少含1个空格或回车或tab,([^\\s]{1,})表示至少含1个非空格或回车或tab
		regxpSb.append("select([\\s\t\n\r]{1,})([^\\s\t\n\r]{1,})([\\s\t\n\r]{1,})from");
		regxpSb.append("|");
		// 过滤union select
		regxpSb.append("union([\\s\t\n\r]{1,})select");
		regxpSb.append("|");
		// 过滤union all select
		regxpSb.append("union([\\s\t\n\r]{1,})all([\\s\t\n\r]{1,})select");
		regxpSb.append("|");
		//or[1个或多个空格]
		regxpSb.append("or([\\s\t\n\r]{1,})");
		regxpSb.append("|");
		regxpSb.append("and([\\s\t\n\r]{1,})");
		regxpSb.append("|");
		regxpSb.append("case([\\s\t\n\r]{1,})");
		regxpSb.append("|");
		regxpSb.append("like([\\s\t\n\r]{1,})");
		regxpSb.append("|");
		regxpSb.append("regexp([\\s\t\n\r]{1,})");
		regxpSb.append("|");
		// 过滤SYSTEM_USER()
		regxpSb.append("SYSTEM_USER[\\s\t\n\r]*\\(");
		regxpSb.append("|");
		regxpSb.append("USER[\\s\t\n\r]*\\(");
		regxpSb.append("|");
		regxpSb.append("DATABASE[\\s\t\n\r]*\\(");
		regxpSb.append("|");
		regxpSb.append("IF[\\s\t\n\r]*\\(");
		regxpSb.append("|");
		regxpSb.append("MID[\\s\t\n\r]*\\(");
		regxpSb.append("|");
		regxpSb.append("SUBSTR[\\s\t\n\r]*\\(");
		regxpSb.append("|");
		regxpSb.append("EXTRACTVALUE[\\s\t\n\r]*\\(");
		regxpSb.append("|");
		regxpSb.append("BENCHMARK[\\s\t\n\r]*\\(");
		regxpSb.append("|");
		regxpSb.append("ELT[\\s\t\n\r]*\\(");
		regxpSb.append("|");
		regxpSb.append("UPDATEXML[\\s\t\n\r]*\\(");
		regxpSb.append("|");
		regxpSb.append("CONCAT[\\s\t\n\r]*\\(");
		regxpSb.append("|");
		regxpSb.append("CONCAT_WS[\\s\t\n\r]*\\(");
		regxpSb.append("|");
		regxpSb.append("SLEEP[\\s\t\n\r]*\\(");

		Pattern pattern = Pattern.compile(regxpSb.toString(), Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(tempContent);
		boolean hasNext = matcher.find();
		if (!hasNext) {
			return tempContent;
		}
		StringBuffer encodeSb = new StringBuffer();
		do {
			// 获取字符串里对应的tag
			String tagIncontent = matcher.group();
			// (?i)表示不区分大小写
			tagIncontent = tagIncontent.replaceAll("(?i)select", "ＳＥＬＥＣＴ").replaceAll("(?i)from", "ＦＲＯＭ")
					.replaceAll("(?i)union", "ＵＮＩＯＮ").replaceAll("(?i)all", "ＡＬＬ").replaceAll("(?i)or", "ＯＲ")
					.replaceAll("(?i)SYSTEM_USER", "ＳＹＳＴＥＭ＿ＵＳＥＲ").replaceAll("(?i)USER", "ＵＳＥＲ")
					.replaceAll("(?i)DATABASE", "ＤＡＴＡＢＡＳＥ").replaceAll("(?i)IF", "ＩＦ")
					.replaceAll("(?i)MID", "ＭＩＤ").replaceAll("(?i)SUBSTR", "ＳＵＢＳＴＲ")
					.replaceAll("(?i)and", "ＡＮＤ").replaceAll("(?i)case", "ＣＡＳＥ")
					.replaceAll("(?i)like", "ＬＩＫＥ").replaceAll("(?i)regexp", "ＲＥＧＥＸＰ")
					.replaceAll("(?i)EXTRACTVALUE", "ＥＸＴＲＡＣＴＶＡＬＵＥ").replaceAll("(?i)BENCHMARK", "ＢＥＮＣＨＭＡＲＫ")
					.replaceAll("(?i)ELT", "ＥＬＴ").replaceAll("(?i)UPDATEXML", "ＵＰＤＡＴＥＸＭＬ")
					.replaceAll("(?i)CONCAT_WS", "ＣＯＮＣＡＴ＿ＷＳ").replaceAll("(?i)CONCAT", "ＣＯＮＣＡＴ")
					.replaceAll("(?i)SLEEP", "ＳＬＥＥＰ");
			matcher.appendReplacement(encodeSb, tagIncontent);
		} while (matcher.find());

		matcher.appendTail(encodeSb);
		return encodeSb.toString();
	}
	
	//测试用函数
	public static void main(String[] args){
		AntiSqlInjectFilter filter = new AntiSqlInjectFilter();
		String sqlStr  = "DATABASE";
		String sqlStr1 = "DATABASE (";
		System.out.println(filter.filterParamValue(sqlStr));
		System.out.println(filter.filterParamValue(sqlStr1));
	}

}
