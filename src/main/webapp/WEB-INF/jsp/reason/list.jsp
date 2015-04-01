<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<Reason> reasonList = (List<Reason>)request.getAttribute("reasonList");
  Page page_obj = (Page)request.getAttribute("page_obj");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>常用语设置管理</title>
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
	$("#alert_box select", parent.document).val(0);
	$("#searchForm").submit();
}
function editInit(){
	var value = initReasontype.split(",")[0];
	var name = initReasontype.split(",")[1];
	$("#"+name, parent.document).val(value);
}
function editSuccess(data){
	$("#searchForm").submit();
}
function delSuccess(data){
	$("#searchForm").submit();
}
</script>
</head>

<body style="background:#eef9ff">

<div class="right_box">
	<div class="inputselect_box">
	<span><input name="" type="button" value="新建常用语" class="input_button1"  id="add_button"  />
	</span>
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
		 请选择类型：<select name ="reasontype" id="reasontype">
		               <option value ="0">全部</option>
		               <%for(ReasonTypeEnum ry : ReasonTypeEnum.values()){ %>
		               <option value ="<%=ry.getValue()%>"><%=ry.getText() %></option>
		               <%} %>
		           </select>
		 <input type="submit"  onclick="$('#searchForm').attr('action',1);return true;"  id="find" value="查询" class="input_button2" />
		 <input type="button"  onclick="location.href='1'" value="返回" class="input_button2" />
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		<tr class="font_1">
				<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">编号</td>
				<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">类型</td>
				<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">级别</td>
				<td width="30%" align="center" valign="middle" bgcolor="#eef6ff">常用语内容</td>
				<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
	<%for(Reason r:reasonList){ %>
		<tr>
			<td width="15%" align="center" valign="middle"><%=r.getReasonid()%></td>
			<td width="15%" align="center" valign="middle"><%for(ReasonTypeEnum rte : ReasonTypeEnum.values()){if(r.getReasontype()==rte.getValue()){%><%=rte.getText()%><%}}%></td>
			<td width="15%" align="center" valign="middle"><%=r.getWhichreason()==1?"1":r.getWhichreason()==0?"":"2" %></td>
			<td width="30%" align="center" valign="middle"><%=r.getReasoncontent()%></td>
			<td width="25%" align="center" valign="middle">
			[<a href="javascript:edit_button(<%=r.getReasonid() %>);">修改</a>]
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
			共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第
			<select id="selectPg"
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
$("#reasontype").val(<%=request.getParameter("reasontype")==null?"0":request.getParameter("reasontype") %>);
</script>
<!-- 创建常用于设置的ajax地址 -->
<input type="hidden" id="add" value="<%=request.getContextPath()%>/reason/add" />
<!-- 修改常用于设置的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/reason/edit/" />
</body>
</html>

