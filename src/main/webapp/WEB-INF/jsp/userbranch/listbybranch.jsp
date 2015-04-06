<%@page import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.UserBranch,cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
	List<User> userlist = (List<User>)request.getAttribute("userlist");
	List<UserBranch> userbranchList = (List<UserBranch>)request.getAttribute("userbranchList");
	Map<String,List<UserBranch>> mapList = (Map<String,List<UserBranch>>)request.getAttribute("mapList");
	Page page_obj = (Page)request.getAttribute("page_obj");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户区域权限设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
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
</script>
</head>

<body style="background:#f5f5f5">
<div class="right_box">
	<div class="kfsh_tabbtn">
		<ul>
			<li><a href="<%=request.getContextPath() %>/userBranchControl/list/1">按用户设置</a></li>
			<li><a href="<%=request.getContextPath() %>/userBranchControl/listbybranch/1" class="light">按机构设置</a></li>
		</ul>
	</div>
	<div class="tabbox">
	<div class="kfsh_search">
	<span><input name="" type="button" value="创建用户区域权限" class="input_button1"  id="add" onclick="location='<%=request.getContextPath()%>/userBranchControl/addbybranch'" />
	</span>
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm" method="post" >
		可查询站点：
         	<select id="branchid"  name="branchid">
				<option value="0" selected>----请选择----</option>
				<%for(Branch b : branchlist){ %>
					<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
				<%} %>
			</select>
		用户：
			<select id="userid" name="userid">
				<option value="0" selected>----请选择----</option>
				<%for(User u : userlist){ %>
					<option value="<%=u.getUserid() %>" >(<%for(Branch b : branchlist){if(u.getBranchid()==b.getBranchid()){ out.print(b.getBranchname()); }} %>)<%=u.getRealname() %></option>
				<%} %>
			</select>
		<input type="submit" onclick="$('#searchForm').attr('action',1);return true;" id="find" value="查询" class="input_button2" />
		<!-- <input type="button"  onclick="location.href='1'" value="返回" class="input_button2" /> -->
	</form>
	</div>
	<div class="jg_10"></div>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	<tr class="font_1">
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">可查询站点</td>
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">用户</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		
		<%if(userbranchList != null && userbranchList.size()>0 && mapList != null && !mapList.isEmpty()){ %>
		 <% for(UserBranch ub : userbranchList){ String branchname=""; %>
		 	<%for(Branch b : branchlist){if(b.getBranchid()==ub.getBranchid()){ %>
				<%branchname=b.getBranchname() ;break;
			 }} %>
			<%if(branchname.length()!=0){ %>
			<tr>
				<td width="10%" align="center" valign="middle" >
				<%=branchname%>
				</td>
				<td width="10%" align="left" valign="middle" >
				<%for(UserBranch csc : mapList.get(ub.getBranchid()+"List")){ %>
				  <%for(User u : userlist){if(u.getUserid()==csc.getUserid()){ %>
					<%=u.getRealname() %><%if(mapList.get(ub.getBranchid()+"List").size()>1){ %> | <%} %>
				  <%break;}
					} %>
				<%} %>
				</td>
				<td width="10%" align="center" valign="middle" >
				[<a href="javascript:if(confirm('确定要删除?')){del('<%=ub.getBranchid() %>');}">删除</a>]
				[<a href="<%=request.getContextPath()%>/userBranchControl/editbybranch/<%=ub.getBranchid() %>;">修改</a>]
				</td>
			</tr>
		<%}}}%>
	</table>
	<div class="jg_10"></div><div class="jg_10"></div>
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
</div>
<div class="jg_10"></div>
<div class="clear"></div>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#userid").val(<%=request.getParameter("userid") %>);
$("#branchid").val(<%=request.getParameter("branchid") %>);

</script>
<!-- 创建用户区域权限的ajax地址 -->
<input type="hidden" id="add" value="<%=request.getContextPath()%>/userBranchControl/addbybranch" />
<!-- 修改用户区域权限的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/userBranchControl/editbybranch/" />
<!-- 删除用户区域权限的ajax地址 -->
<input type="hidden" id="del" value="<%=request.getContextPath()%>/userBranchControl/delbybranch/" />
</body>
</html>