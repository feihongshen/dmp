<%@page import="cn.explink.enumutil.PunishtimeEnum"%>
<%@page import="cn.explink.enumutil.PunishlevelEnum"%>
<%@page import="cn.explink.domain.Punish"%>
<%@page import="cn.explink.domain.PunishType"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
List<User> userList = (List<User>)request.getAttribute("userList");
List<PunishType> punishTypeList = (List<PunishType>)request.getAttribute("punishTypeList");
List<Punish> punishList = (List<Punish>)request.getAttribute("punishList");
Page page_obj = (Page)request.getAttribute("page_obj");
String cwb = request.getAttribute("cwb").toString();
long userid=Long.parseLong(request.getAttribute("userid").toString());
long branchid=Long.parseLong(request.getAttribute("branchid").toString());
long punishid=Long.parseLong(request.getAttribute("punishid").toString());
long punishlevel=Long.parseLong(request.getAttribute("punishlevel").toString());
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>扣罚类型等级</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/punish.js"></script>
<script type="text/javascript">
function addInit(){
	//无处理
}
function editInit(){
	//$("#searchForm").submit();
}
function editSuccess(data){
	$("#searchForm").submit();
}
function addSuccess(data){
	$("#alert_box input[type='text']" , parent.document).val("");
	$("#alert_box textarea" , parent.document).val("");
	$("#alert_box select", parent.document).val("");
	$("#searchForm").submit();
}
function delData(id){
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/punish/deletePunish",
		data:{"id":id},
		dataType:"json",
		success:function(data){
			alert(data.error);
			$("#searchForm").submit();
		}});
}
function state(id){
	$.ajax({
		type : "POST",
		url:"<%=request.getContextPath()%>/punish/state",
		data:{"id":id},
		dataType : "html",
		success : function(data) {
			// alert(data);
			$("#alert_box", parent.document).html(data);

		},
		complete : function() {
			addInit();// 初始化某些ajax弹出页面
			viewBox();
		}
	});
}
function importPage(){
	$.ajax({
		type : "POST",
		url:"<%=request.getContextPath()%>/punish/importPage",
		dataType : "html",
		success : function(data) {
			// alert(data);
			$("#alert_box", parent.document).html(data);

		},
		complete : function() {
			addInit();// 初始化某些ajax弹出页面
			viewBox();
		}
	});
}
</script>
</head>

<body style="background:#eef9ff">

<div class="right_box">
	<div class="inputselect_box">
	<span><input name="" type="button" value="扣罚登记" class="input_button1"  id="add_button"  />
		</span>
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
		扣罚类型:<select id="punishid" name="punishid">
		<option value="0">请选择</option>
		<%for(PunishType p:punishTypeList){ %>
		<option value="<%=p.getId() %>" <%if(p.getId()==punishid){ %>selected="selected"<%} %>><%=p.getName() %></option>
		<%} %>
		</select>
		扣罚站点:<select id="branchid" name="branchid" onchange="selectBranch($(this).val())">
		<option value="0">请选择</option>
		<%for(Branch b:branchlist){ %>
		<option value="<%=b.getBranchid()%>" <%if(b.getBranchid()==branchid) {%>selected="selected"<%} %>><%=b.getBranchname() %></option>
		<%} %>
		</select>
		扣罚人员:<select id="userid" name="userid" onchange="selectUser($(this).val())">
		<option value="0">请选择</option>
		<%for(User u:userList){ %>
		<option value="<%=u.getUserid()%>" <%if(u.getUserid()==userid) {%>selected="selected"<%} %>><%=u.getRealname() %></option>
		<%} %>
		</select>
		订单号:<input type="text" name="cwb" id="cwb" value="<%=cwb%>"/>
		优先级别:<select id="punishlevel" name="punishlevel">
		<option value="0">请选择</option>
		</select>
		<input type="hidden" id="isnow" name="isnow" value="1"/>
		<input type="submit" value="查询"/>
		<input type="button" value="导出" onclick="javascript:$('#exportExcle').submit()" <%if(punishList.size()==0){ %>disabled="disabled" <%} %>/>
		<input type="button" value="导入" onclick="importPage()"/>
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
<br></br>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	  <tr class="font_1">
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">扣罚类型</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">扣罚站点</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">扣罚人员</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">扣罚时效</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">优先级别</td>
			<td width="7%" align="center" valign="middle" bgcolor="#eef6ff">扣罚金额</td>
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">扣罚内容</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">创建人</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">创建时间</td>
			<td width="13%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		 <% for(Punish p : punishList){ %>
		<tr>
			<td width="8%" align="center" valign="middle"><%=p.getCwb()%></td>
			<td width="8%" align="center" valign="middle">
			<%for(PunishType pt:punishTypeList){ if(pt.getId()==p.getPunishid()){out.print(pt.getName());}}%>
			</td>
			<td width="8%" align="center" valign="middle">
			<%for(Branch b:branchlist){ if(b.getBranchid()==p.getBranchid()){out.print(b.getBranchname());}}%>
			</td>
			<td width="8%" align="center" valign="middle">
			<%for(User u:userList){ if(u.getUserid()==p.getUserid()){out.print(u.getRealname());}}%>
			</td>
			<td width="8%" align="center" valign="middle">
			<%=PunishtimeEnum.getText(p.getPunishtime()).getText() %>
			</td>
			<td width="8%" align="center" valign="middle">
			<%=PunishlevelEnum.getText(p.getPunishlevel()).getText() %>
			</td>
			<td width="7%" align="center" valign="middle">
			<%=p.getPunishfee() %>
			</td>
			<td width="15%" align="center" valign="middle">
			<%=p.getPunishcontent() %>
			</td>
			<td width="8%" align="center" valign="middle">
			<%for(User u:userList){ if(u.getUserid()==p.getCreateuser()){out.print(u.getRealname());}}%>
			</td>
			<td width="13%" align="center" valign="middle">
			<%=p.getCreatetime() %>
			</td>
		
			<td width="8%" align="center" valign="middle">
			[<a href="javascript:edit_button(<%=p.getId() %>);">修改</a>]
			[<a href="javascript:if(confirm('确定要删除?')){delData(<%=p.getId() %>);}">删除</a>]
			[<a href="javascript:state(<%=p.getId() %>);"><%=(p.getState()==1?"取消审核":"审核") %></a>] 
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
<form id="exportExcle" action="<%=request.getContextPath()%>/punish/exportExcle" method="post" >
<input type="hidden" name="cwb" value="<%=cwb%>"/>
<input type="hidden" name="branchid" value="<%=branchid%>"/>
<input type="hidden" name="userid" value="<%=userid%>"/>
<input type="hidden" name="punishid" value="<%=punishid%>"/>
<input type="hidden" name="punishlevel" value="<%=punishlevel%>"/>
</form>
		<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>	
	<div class="jg_10"></div>

	<div class="clear"></div>
	<input type="hidden" id="add" value="<%=request.getContextPath()%>/punish/add" />
	<input type="hidden" id="dmpurl" value="<%=request.getContextPath()%>" />
	<input type="hidden" id="edit" value="<%=request.getContextPath()%>/punish/edit/" />
</body>
</html>


