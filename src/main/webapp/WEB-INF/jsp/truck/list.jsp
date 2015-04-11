<%@page import="cn.explink.domain.Truck"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<Truck> truckList = (List<Truck>)request.getAttribute("truckList");
Page page_obj = (Page)request.getAttribute("page_obj");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入订单类型管理</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
function addInit(){
	//无处理
}
function addSuccess(data){
	$("#alert_box input[type='text']" , parent.document).val("");
	$("#alert_box select", parent.document).val("");
	$("#searchForm").submit();
}
function editInit(){
	for(var i =0 ; i < initEditArray.length ; i ++){
		var value = initEditArray[i].split(",")[0];
		var name = initEditArray[i].split(",")[1];
		$("#"+name, parent.document).val(value);
	}
}
function editSuccess(data){
	$("#searchForm").submit();
}
function delSuccess(data){
	$("#searchForm").submit();
}
</script>
</head>

<body style="background:#f5f5f5">

<div class="right_box">
	<div class="inputselect_box">
	<span><input name="" type="button" value="创建车辆" class="input_button1"  id="add_button"  />
	</span>
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
		 车牌号<input type ="text" id ="truckno" name ="truckno"  class="input_text1" value = "<%=request.getParameter("truckno")==null?"":request.getParameter("truckno") %>"/>　　
		 型号 <input type ="text" id ="trucktype" name ="trucktype"  class="input_text1" value = "<%=request.getParameter("trucktype")==null?"":request.getParameter("trucktype") %>"/>
		 <input type="submit" id="find" onclick="$('#searchForm').attr('action',1);return true;"  value="查询" class="input_button2" />
		 <input type="button"  onclick="location.href='1'" value="返回" class="input_button2" />
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	  <tr class="font_1">
			<td width="35%" align="center" valign="middle" bgcolor="#eef6ff">车牌号</td>
			<td width="30%" align="center" valign="middle" bgcolor="#eef6ff">型号</td>
			<td width="35%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		 <% for(Truck truck : truckList){ %>
		<tr>
			<td width="35%" align="center" valign="middle"><%=truck.getTruckno() %></td>
			<td width="30%" align="center" valign="middle"><%=truck.getTrucktype() %></td>
			<td width="35%" align="center" valign="middle">
			[<a href="javascript:del(<%=truck.getTruckid() %>);"><%=(truck.getTruckflag()==1?"停用":"启用") %></a>]
			[<a href="javascript:edit_button(<%=truck.getTruckid() %>);">修改</a>] 
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
</div>
			
	<div class="jg_10"></div>

	<div class="clear"></div>
	
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
<!-- 创建车辆的ajax地址 -->
<input type="hidden" id="add" value="<%=request.getContextPath()%>/truck/add" />
<!-- 修改车辆的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/truck/edit/" />
<!-- 删除车辆的ajax地址 -->
<input type="hidden" id="del" value="<%=request.getContextPath()%>/truck/del/" />
</body>
</html>


