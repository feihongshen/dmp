<%@page import="cn.explink.domain.SetExportField"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<%
List<SetExportField> exportfieldlist = (List<SetExportField>)request.getAttribute("listSetExportField");
%> 
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript">
$("document").ready(function(){  
	$("#checkall").click(function(){  
	  if($("#checkall").attr("checked")){
			$("[name='fieldname']").attr("checked",'true');//全选  
		}else{
		   $("[name='fieldname']").removeAttr("checked");//取消全选  
		}	
	});
 });
</script>


</HEAD>
<BODY>
<font color ="red">${exportSuccess}</font><br/>
<input type ="checkbox" id ="checkall" >全选<br/>
<form method ="post" action ="<%=request.getContextPath()%>/exportcwb/save" id ="form1">
	<%for(SetExportField s : exportfieldlist){ %>
	<input type ="checkbox" name="fieldid" value ="<%= s.getId()%>" <%if(s.getExportstate()==1){%> checked="checked"<%} %>><%=s.getFieldname()%><br/>
	
	<%} %>
	<!-- <input type ="checkbox" name="fieldname" value ="订单类型">订单类型<br/>
	<input type ="checkbox" name="fieldname" value ="发货单号">发货单号<br/>
	<input type ="checkbox" name="fieldname" value ="修改时间">修改时间<br/>
	<input type ="checkbox" name="fieldname" value ="结算区域">结算区域<br/> -->
	<input type ="submit" id ="sub" value ="确定">
	
</form>
</BODY>
</HTML>
