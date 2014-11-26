<%@page import="cn.explink.domain.OperationRule,cn.explink.domain.User,cn.explink.enumutil.*,cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<OperationRule> orList = (List<OperationRule>)request.getAttribute("orList");
	List<User> userList = (List<User>)request.getAttribute("userList");
	Page page_obj = (Page)request.getAttribute("page_obj");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>操作检验设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">

function addInit(){
	//window.parent.uploadFormInit("user_cre_Form");
}
function addSuccess(data){
	$("#alert_box select", parent.document).val(0);
	$("#searchForm").submit();
}
function editInit(){
	window.parent.crossCapablePDA();
	//window.parent.uploadFormInit("user_save_Form");
}
function editSuccess(data){
	$("#searchForm").submit();
}
function delSuccess(data){
	$("#searchForm").submit();
}
function check_cwballstatecontrol(){
	if($("#flowordertype").val()==-1){
		alert("当前状态不能为空");
		return false;
	}if($("#filed").val()==0){
		alert("必选字段不能为空");
		return false;
	}
	return true;
}
</script>
</head>

<body style="background:#eef9ff">

<div class="right_box">
	<div class="inputselect_box">
	<span><input name="" type="button" value="创建操作检验设置" class="input_button1"  id="" onclick="window.location.href='<%=request.getContextPath()%>/operationRule/add';" />
	</span>
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm" method="post" >
		环节：
			<select id="flowordertype" name="flowordertype">
				<option value="-1" selected>----请选择----</option>
					<%for(FlowOrderTypeEnum ft : FlowOrderTypeEnum.values()){ %>
						<option value="<%=ft.getValue() %>" ><%=ft.getText() %></option>
					<%} %>
			</select>
		<input type="submit" onclick="$('#searchForm').attr('action',1);return true;" id="find" value="查询" class="input_button2" />
		<!-- <input type="button"  onclick="location.href='1'" value="返回" class="input_button2" /> -->
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	<tr class="font_1">
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">规则名称</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">规则表达式</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">环节</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">校验结果</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">校验失败返回信息</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">创建人</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">创建时间</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">修改人</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">修改时间</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		 <% for(OperationRule or : orList){ %>
		<tr>
			<td width="10%" align="center" valign="middle" ><%=or.getName() %></td>
			<td width="10%" align="center" valign="middle" ><%=or.getExpression() %></td>
			<td width="10%" align="center" valign="middle" >
			<%for(FlowOrderTypeEnum ce : FlowOrderTypeEnum.values()){if(or.getFlowordertype()==ce.getValue()){ %>
				<%=ce.getText() %>
			<%}} %>
			</td>
			<td width="10%" align="center" valign="middle" >
			<%if(or.getResult()==OpertaionResultEnum.Success){%>
			成功
			<%} else if(or.getResult()==OpertaionResultEnum.Continue){%>
			继续
			<%} else if(or.getResult()==OpertaionResultEnum.Fail){%>
			失败
			<%}%>
			</td>
			<td width="10%" align="center" valign="middle" ><%=or.getErrormessage() %></td>
			<td width="10%" align="center" valign="middle" >
			<%for(User u : userList){if(or.getCreator()==u.getUserid()){ %>
				<%=u.getRealname() %>
			<%}} %>
			</td>
			<td width="10%" align="center" valign="middle" ><%=or.getModifyDate() %></td>
			<td width="10%" align="center" valign="middle" >
			<%for(User u : userList){if(or.getModifier()==u.getUserid()){ %>
				<%=u.getRealname() %>
			<%}} %>
			</td>
			<td width="10%" align="center" valign="middle" ><%=or.getCreateDate() %></td>
			
			<td width="10%" align="center" valign="middle" >
			[<a href="javascript:if(confirm('确定要删除?')){del('<%=or.getId() %>');}">删除</a>]
			[<a href="<%=request.getContextPath()%>/operationRule/edit/<%=or.getId() %> ">修改</a>]
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
$("#flowordertype").val(<%=request.getParameter("flowordertype") %>);
</script>
<!-- 删除订单流程的ajax地址 -->
<input type="hidden" id="del" value="<%=request.getContextPath()%>/operationRule/del/" />
</body>
</html>