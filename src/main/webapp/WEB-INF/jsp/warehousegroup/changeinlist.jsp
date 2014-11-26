
<%@page import="cn.explink.enumutil.OutwarehousegroupOperateEnum"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.OutWarehouseGroup"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<OutWarehouseGroup> owgList = (List<OutWarehouseGroup>)request.getAttribute("owgList");
List<User> driverList = (List<User>)request.getAttribute("driverList");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>中转站入库交接单打印</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$(function(){
	$("#beginemaildate").datepicker();
	$("#endemaildate").datepicker();
});
</script>
</head>
<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<span>
	</span>
	<form action="1" method="post" id="searchForm" name="searchForm">
		 驾驶员：<select name="userid" id="userid">
        <option value="0">请选择驾驶员</option>
        <%for(User u :driverList){ %>
           <option value="<%=u.getUserid()%>"><%=u.getRealname()%></option>
        <%} %>
        </select>　
		发货时间段：
		 <input type ="text" name ="beginemaildate" id ="beginemaildate" class="input_text1" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("beginemaildate")) %>" />&nbsp;到
		 <input type ="text" name= "endemaildate" id ="endemaildate" class="input_text1" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("endemaildate")) %>" />     
	      　　<input type="submit" id="find" value="查询" class="input_button2" />
	      <input type="button"  onclick="location.href='1'" value="返回" class="input_button2" />
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
		<tr class="font_1">
			<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">批次号</td>
			<td width="30%" align="center" valign="middle" bgcolor="#eef6ff">操作时间</td>
			<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">操作类型</td>
			<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">打印交接单</td>
		</tr>
		</table>
	 <%if(owgList!=null){ 
	 Page page_obj = (Page)request.getAttribute("page_obj");
      %>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		 <%for(OutWarehouseGroup o:owgList){ %>
		 <tr>
			<td width="25%" align="center" valign="middle"><%=o.getId() %></td>
			<td width="30%" align="center" valign="middle" ><%=o.getCredate() %></td>
			  <%for(OutwarehousegroupOperateEnum ooe : OutwarehousegroupOperateEnum.values()){
    	          if(o.getOperatetype()==ooe.getValue()){%>
		    		<td width="20%" align="center" valign="middle">  <%=ooe.getText()%></td>
		       <% }
		    }%>
			<td width="25%" align="center" valign="middle" >
				<a href ="../changeinlistprint/<%=o.getId()%>">中转站入库交接单打印</a>
	    	<%if(o.getPrinttime().length()!=0){ %>
			（<%=o.getPrinttime() %>已打印）
			<%} %>
			</td>
		</tr>
		<%} %>
	</table>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
	<%if(page_obj.getMaxpage()>1){ %>
	<div class="iframe_bottom">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
	<tr>
		<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
			<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
			<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
			<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
			<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
			　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
					id="selectPg"
					onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
					<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
					<option value="<%=i %>"><%=i %></option>
					<% } %>
				</select>页
		</td>
	</tr> 
	</table>
	</div>
	<%} %>
	<%} %>
</div>			
	<div class="jg_10"></div>
	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#userid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("userid"))%>);
</script>
</body>
</html>
