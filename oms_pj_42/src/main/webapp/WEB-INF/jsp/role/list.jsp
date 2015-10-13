<%@page import="cn.explink.domain.Role"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<Role> roleList = (List<Role>)request.getAttribute("roles");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>角色权限管理</title>
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
	$("#alert_box select", parent.document).val(-1);
	$("#searchForm").submit();
}
function editInit(){
	//$("#searchForm").submit();
}
function editSuccess(data){
	$("#searchForm").submit();
}
function delSuccess(data){
	$("#searchForm").submit();
}
function editRoleAndMenuInit(){
	if(initMenuList){
		for(var i=0 ; i<initMenuList.length ; i++){
			window.parent.initMenu(initMenuList[i]);
		}
	}
}

function editRoleAndMenuSuccess(data){
	$("#searchForm").submit();
}
</script>
</head>

<body style="background:#eef9ff">

<div class="right_box">
		<div class="inputselect_box">
		<span><input name="" type="button" value="创建角色" class="input_button1"  id="add_button"  />
		</span>
		<form action="list" method="post" id="searchForm">
					角色名：<input type="text" name="rolename" value="<%=request.getParameter("rolename")==null?"":request.getParameter("rolename") %>" />
					<input type="submit" id="find"  value="搜索" class="input_button2" />
					<input type="button"  onclick="location.href='list'" value="返回" class="input_button2" />
				</form>
		
		
		</div>
		<div class="right_title">
		<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		    <tr class="font_1">
				<td width="60%" align="center" valign="middle" bgcolor="#eef6ff">角色</td>
				<td width="40%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
			</tr>
			 <% for(Role r : roleList){ %>
			<tr>
				<td width="60%" align="center" valign="middle"><%=r.getRolename() %></td>
				<td width="40%" align="center" valign="middle" >
				[<a href="javascript:getEditRoleAndMenu(<%=r.getRoleid() %>);">设置权限</a>]　
				<%if(r.getType()>0){ %>
				[<a href="javascript:edit_button(<%=r.getRoleid() %>);">修改</a>]
				<%} %>
				</td>
			</tr>
			<%} %>
		</table>
		</div>
		
	</div>
			
	<div class="jg_10"></div>

	<div class="clear"></div>

<!-- 创建用户的ajax地址 -->
<input type="hidden" id="add" value="<%=request.getContextPath()%>/role/add" />
<!-- 修改用户的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/role/edit/" />
<!-- 设置权限的ajax地址 -->
<input type="hidden" id="editRoleAndMenu" value="<%=request.getContextPath()%>/role/editRoleAndMenu/" />
</body>
</html>


