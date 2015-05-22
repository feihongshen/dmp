<%@page import="cn.explink.domain.orderflow.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<%
   String results = request.getAttribute("result")==null?"":request.getAttribute("result").toString();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<title>修复数据</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

</head>

<body style="background:#f5f5f5">
<div class="right_box">
	<div class="right_title">
		
		<form action ="<%=request.getContextPath() %>/gome/codxiufu" method ="post" id ="batchForm">
		<br>订单号：<br>
		&nbsp;&nbsp;&nbsp;<textarea name="cwb" cols="20" rows="20" ></textarea>修改金额：<textarea name="cod" cols="20" rows="20" ></textarea>
		<input type="submit" name="button3" id="btnval2" value="确定" class="button" />
		<br/>结果：<br>&nbsp;&nbsp;&nbsp;<textarea name="cwbss" cols="200" rows="50" ><%=results %></textarea>
		
		</form>
	</div>
	
</div>
</body>

</HTML>

<%-- 



    <!-- 设置输出格式,以及编码格式--> 
    <%@ page contentType="application/vnd.ms-excel; charset=GBK"%> 
    <% 
    String path = request.getContextPath(); 
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; 
    String fileName = "所有学生信息.xls"; 
    //设置生成的excel的标题 
    response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("GBK"),"iso-8859-1")); 
    %> 
     
    <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
    <html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"> 
        <head> 
            <meta http-equiv="Content-Type" content="text/html; charset=GBK"> 
            <style> 
                td{ 
                    /*指定单元格的数据属性,避免excel自动转换格式,比如0001不自动转换成1*/ 
                    /*mso-number-forma 是office提供的格式 mso:microsoft*/ 
                    mso-number-format:"\@"; 
                    background-color:#8DB4E3; 
                    white-space: nowrap; 
                } 
                .color0 {background-color:#DBE5F1;} 
                .color1 {background-color:#B8CCE4;} 
            </style> 
            <title>学生信息</title> 
        </head> 
       
      <body> 
       <table border="1" bordercolor="#538EDA"> 
                <thead> 
                    <tr height="23"> 
                        <th nowrap="nowrap"><font color="white">编号</font></th> 
                        <th nowrap="nowrap"><font color="white">姓名</font></th> 
                        <th nowrap="nowrap"><font color="white">性别</font></th> 
                        <th nowrap="nowrap"><font color="white">年龄</font></th> 
                        <th nowrap="nowrap"><font color="white">学号</font></th> 
                        <th nowrap="nowrap"><font color="white">班级</font></th> 
                        <th nowrap="nowrap"><font color="white">专业</font></th> 
                    </tr> 
                </thead> 
                <tbody> 
                    <tr align="center" height="23"> 
                        <td class="color${status.index mod 2}" title="编号" nowrap="nowrap">00001</td> 
                        <td class="color${status.index mod 2}" title="姓名" nowrap="nowrap">张三</td> 
                        <td class="color${status.index mod 2}" title="性别" nowrap="nowrap">男</td> 
                        <td class="color${status.index mod 2}" title="年龄" nowrap="nowrap">21</td> 
                        <td class="color${status.index mod 2}" title="学号" nowrap="nowrap">070920081</td> 
                        <td class="color${status.index mod 2}" title="班级" nowrap="nowrap">计科0703</td> 
                        <td class="color${status.index mod 2}" title="专业" nowrap="nowrap">计算机科学与技术</td> 
                    </tr> 
                </tbody> 
            </table> 
      </body> 
    </html>  --%>




