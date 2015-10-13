
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.ProxyConf"%>
<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<ProxyConf> proxyList = (List<ProxyConf>)request.getAttribute("proxyConfList");
  Page page_obj = (Page)request.getAttribute("page_obj");
%>




<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>代理管理</title>
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
	$("#alert_box input[type='password']" , parent.document).val("");
	$("#alert_box select", parent.document).val(0);
	$("#searchForm").submit();
}
function editInit(){

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
		<span><input name="" type="button" value="新建新代理" class="input_button1"  id="add_button"  />
		</span>
		<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
		可用状态：<select name ="state" id ="state" >
		          <option value ="-1" <%if(-1 == Integer.parseInt(request.getParameter("state")==null?"-1":request.getParameter("state"))  ){ %>selected="selected" <%} %>>所有</option>
		          <option value ="1" <%if(1 == Integer.parseInt(request.getParameter("state")==null?"-1":request.getParameter("state"))  ){ %>selected="selected" <%} %>>可用</option>
		          <option value ="0" <%if(0 == Integer.parseInt(request.getParameter("state")==null?"-1":request.getParameter("state"))  ){ %>selected="selected" <%} %>>不可用</option>
		        </select>
			<input type="submit" id="find" onclick="$('#searchForm').attr('action',1);return true;" value="搜索"  class="input_button2" />
		</form>
	</div>
				<div class="right_title">
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>

				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
				<tr class="font_1">
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">id</td>
						<td width="42%" align="center" valign="middle" bgcolor="#eef6ff">代理IP</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">端口号</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">正在使用</td>
						<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">可用状态</td>
						<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">速度状态</td>
						<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">是否默认代理</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
						
					</tr>
					  <% for(ProxyConf b : proxyList){ %>
					<tr>
					 	<td width="10%"  align="center" valign="middle"><%=b.getId() %></td>
						<td width="42%" align="center" valign="middle"><%=b.getIp() %></td>
						<td width="10%"  align="center" valign="middle"><%=b.getPort() %></td>
						<td width="10%"  align="center" valign="middle"><%=b.getType() == 1?"是":"否" %></td>
						<td width="5%" align="center" valign="middle"><%=b.getState()== 1?"可用":"不可用" %></td>
						<td width="5%" align="center" valign="middle"><%=b.getWith()== 1?"快":"慢" %></td>
						<td width="8%"align="center" valign="middle"><%=b.getIsnoDefault()== 1?"是":"否" %></td>
						<td width="10%"  align="center" valign="middle" >
						[<a href="javascript:delproxy(<%=b.getId()%>)">删除</a>] 
						[<a href="javascript:edit_button(<%=b.getId()%>);">修改</a>]
						</td>
					</tr>
					<%} %>
				</table>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
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
<!-- 创建的ajax地址 -->
<input type="hidden" id="add" value="<%=request.getContextPath()%>/proxy/add" />
<!-- 修改的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/proxy/edit/" />
<!-- 删除的ajax地址 -->
<input type="hidden" id="delproxy" value="<%=request.getContextPath()%>/proxy/del/" />
</body>
</html>
