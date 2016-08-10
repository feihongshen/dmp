<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true" pageEncoding="UTF-8"%>  
<%@ page import="java.io.*,java.util.*"%>  
<%@ page import="org.slf4j.*"%>  
<%	response.setStatus(HttpServletResponse.SC_OK); 
	//日志前缀
	String SQL_ERROR_PRE = "SqlErr";
	//用于在日志上标识出唯一错误编号
	String ticketNo = SQL_ERROR_PRE + "-" + System.currentTimeMillis();
	//打印在页面的错误信息
	String printInfo = getPrintStack(exception);
	if(isSqlGrammarException(exception.getMessage())){
		String logErr = ticketNo + " :"+ printInfo;
		//打印日志到后台：
		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.error(SQL_ERROR_PRE,logErr);
		printInfo = "错误代号："+ticketNo + "   请联系技术支持同事。";
	}
	
 %>  
 <%!
 //获取错误堆栈信息
 public String getPrintStack(Throwable exception){
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
//判断错误类型是否为sql语法异常
public boolean isSqlGrammarException(String message){
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
%>
<body> 
<h2><font color=#DB1260>HTTP 500 ERROR</font></h2>   
    程序发生了错误，有可能该页面正在调试或者是设计上的缺陷.<br/>  
   你可 <a href="javascript:history.go(-1)">返回上一页</a>  进行重试<br/>
   如仍有问题，请联系技术支持的同事<br/>
    <hr width=98%>  
    
      
    <p>An exception was thrown: <b> <%=exception.getClass()%></b></p>  
    
    <pre>
     <%=printInfo.replaceAll("(?)Caused by", "<font color='blue'>Caused by</font>") %>
    </pre>
     
 
    <hr width=98%>  
    </body>  