<%@page import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Role,cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<User> userList = (List<User>)request.getAttribute("userList");
	List<Branch> branchList = (List<Branch>)request.getAttribute("branches");
	List<Role> roleList = (List<Role>)request.getAttribute("roles");
	Page page_obj = (Page)request.getAttribute("page_obj");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户管理</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
var BranchKeyAndValue = new Array();
<%for(Branch b : branchList) {%> BranchKeyAndValue[<%=b.getBranchid() %>]='<%=b.getBranchname() %>';<%} %>
function getBranchValue(key,userid){
	$("#Branchid_"+key+"_"+userid).html(BranchKeyAndValue[key]);
}
var RoleKeyAndValue = new Array();
<%for(Role r : roleList) {%> RoleKeyAndValue[<%=r.getRoleid() %>]='<%=r.getRolename() %>';<%} %>
function getRoleValue(key,userid){
	$("#Roleid_"+key+"_"+userid).html(RoleKeyAndValue[key]);
}

function userInit(){
	$("#realname", parent.document).blur(function(event){
		window.parent.checkRealname()
	});
	$("#username", parent.document).keydown(function(event){
		if(event.keyCode==13) window.parent.checkUsername()
	});
	$("#username", parent.document).blur(function(event){
		window.parent.checkUsername()
	});
	$("#password1", parent.document).blur(function(event){
		if($("#password", parent.document).val()!=$("#password1", parent.document).val()){
			alert("两次输入密码不一致！");
		}
	});
}

function addInit(){
	userInit();
	//window.parent.uploadFormInit("user_cre_Form");
}
function addSuccess(data){
	$("#alert_box input[type='text']" , parent.document).val("");
	$("#alert_box select", parent.document).val(-1);
	$("#searchForm").submit();
}
function editInit(){
	for(var i =0 ; i < initUser.length ; i ++){
		var value = initUser[i].split(",")[0];
		var name = initUser[i].split(",")[1];
		$("#"+name, parent.document).val(value);
	}
	userInit();
	//window.parent.uploadFormInit("user_save_Form");
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
	<span><input name="" type="button" value="创建用户" class="input_button1"  id="add_button"  />
	</span>
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm" method="post" >
		用户登录名：<input  class="input_text1" value="<%=request.getParameter("username")==null?"":request.getParameter("username") %>" name="username">
		姓名：<input   class="input_text1" value="<%=request.getParameter("realname")==null?"":request.getParameter("realname") %>" name="realname">
		<input type="submit" onclick="$('#searchForm').attr('action',1);return true;" id="find" value="搜索" class="input_button2" />
		<input type="button"  onclick="location.href='1'" value="返回" class="input_button2" />
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	<tr class="font_1">
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">编号</td>
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">所属机构</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">姓名</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">角色</td>
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">电话</td>
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">手机</td>
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">Email/QQ/MSN</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">状态</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		 <% for(User u : userList){ %>
		<tr>
			<td width="5%" align="center" valign="middle" ><%=u.getUserid() %></td>
			<td width="15%" align="center" valign="middle"  id="Branchid_<%=u.getBranchid() %>_<%=u.getUserid() %>">
			<script>getBranchValue(<%=u.getBranchid() %>,<%=u.getUserid() %>)</script></td>
			<td width="10%" align="center" valign="middle" ><%=u.getRealname() %></td>
			<td width="10%" align="center" valign="middle"  id="Roleid_<%=u.getRoleid() %>_<%=u.getUserid() %>">
			<script>getRoleValue(<%=u.getRoleid() %>,<%=u.getUserid() %>)</script></td>
			<td width="15%" align="center" valign="middle" ><%=u.getUserphone() %></td>
			<td width="15%" align="center" valign="middle" ><%=u.getUsermobile() %></td>
			<td width="15%" align="center" valign="middle" ><%=u.getUseremail() %></td>
			<td width="5%" align="center" valign="middle" ><%=u.getEmployeestatusName() %></td>
			<td width="10%" align="center" valign="middle" >
			<%-- [<a href="javascript:if(confirm('确定要删除?')){del(<%=u.getUserid() %>);}">删除</a>] --%>
			[<a href="javascript:edit_button(<%=u.getUserid() %>);">修改</a>]
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
<!-- 创建订单类型的ajax地址 -->
<input type="hidden" id="add" value="<%=request.getContextPath()%>/user/add" />
<!-- 修改订单类型的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/user/edit/" />
<!-- 删除订单类型的ajax地址 -->
<input type="hidden" id="del" value="<%=request.getContextPath()%>/user/del/" />
</body>
</html>