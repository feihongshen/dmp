<%@page import="cn.explink.domain.BranchRoute,cn.explink.domain.Branch,cn.explink.enumutil.*,cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<BranchRoute> brList = (List<BranchRoute>)request.getAttribute("brList");
	List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");

	Page page_obj = (Page)request.getAttribute("page_obj");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>货物流向设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script type="text/javascript">
	function initSelect(){
		$("#toBranchId").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择操作下一环节' });
	}

</script>
<script type="text/javascript">


function addInit(){
	//window.parent.uploadFormInit("user_cre_Form");

	window.parent.initMultipSelect();
}
function addSuccess(data){
	$("#alert_box select", parent.document).val(0);
	$("#searchForm").submit();
}
function editInit(){
	window.parent.crossCapablePDA();
	//window.parent.uploadFormInit( "user_save_Form");
}
function editSuccess(data){
	$("#searchForm").submit();
}
function delSuccess(data){
	$("#searchForm").submit();
}

function openadd(){
	window.location.href="<%=request.getContextPath()%>/branchRouteControl/add.action";
}

</script>
</head>

<body style="background:#f5f5f5">

<div class="right_box">
	<div class="inputselect_box">
	<span><input name="" type="button" value="创建货物流向" class="input_button1"  id="add_button"  onclick="openadd()"/>
	</span>
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm" method="post" >
		当前站点：
			<select id="fromBranchId" name="fromBranchId" class="select1">
				<option value="0" selected>----请选择----</option>
				<%for(Branch b : branchlist){ %>
					<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
				<%} %>
			</select>
		目的站点：
         	<select id="toBranchId"  name="toBranchId" class="select1">
				<option value="0" selected>----请选择----</option>
				<%for(Branch b : branchlist){ %>
					<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
				<%} %>
			</select>
		流向方向：
	        <select id="type"  name="type" class="select1">
				<option value="0" selected>----请选择----</option>
				<%for(BranchRouteEnum br : BranchRouteEnum.values()){ %>
					<option value="<%=br.getValue() %>" ><%=br.getText() %></option>
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
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">当前站点</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">目的站点</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">流向方向</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		 <% for(BranchRoute br : brList){ %>
		<tr>
			<%for(Branch b : branchlist){if(br.getFromBranchId()==b.getBranchid()){ %>
				<%if(b.getBrancheffectflag().equals("1")){ %>
				<td width="10%" align="center" valign="middle" ><%=b.getBranchname() %></td>
				<% }else{%>
				<td width="10%" align="center" valign="middle" ><%=b.getBranchname() %>(失效)</td>
			<%}}} %>
			<%for(Branch b : branchlist){if(br.getToBranchId()==b.getBranchid()){ %>
				<td width="10%" align="center" valign="middle" ><%=b.getBranchname() %></td>
			<%}} %>
			<%for(BranchRouteEnum bre : BranchRouteEnum.values()){if(br.getType()==bre.getValue()){ %>
				<td width="10%" align="center" valign="middle" ><%=bre.getText() %></td>
			<%}} %>
			<td width="10%" align="center" valign="middle" >
			[<a href="javascript:if(confirm('确定要删除?')){del('<%=br.getFromBranchId() %>/<%=br.getToBranchId() %>/<%=br.getType() %>');}">删除</a>]
			[<a href="javascript:edit_button('<%=br.getFromBranchId() %>/<%=br.getToBranchId() %>/<%=br.getType() %>');">修改</a>]
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
$("#fromBranchId").val(<%=request.getParameter("fromBranchId") %>);
$("#toBranchId").val(<%=request.getParameter("toBranchId") %>);
$("#type").val(<%=request.getParameter("type") %>);

</script>
<!-- 创建货物流向的ajax地址 -->
<input type="hidden" id="add" value="<%=request.getContextPath()%>/branchRouteControl/add" />
<!-- 修改货物流向的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/branchRouteControl/edit/" />
<!-- 删除货物流向的ajax地址 -->
<input type="hidden" id="del" value="<%=request.getContextPath()%>/branchRouteControl/del/" />
</body>
</html>