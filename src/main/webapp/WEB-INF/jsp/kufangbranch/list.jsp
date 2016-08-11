<%@page import="cn.explink.domain.Branch,cn.explink.enumutil.*,cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    Map<Long,String[]> kfNameMap = (Map<Long,String[]>)request.getAttribute("kfNameMap");
	List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");

	Page page_obj = (Page)request.getAttribute("page_obj");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>二级分拣库站点设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/css/multiple-select.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiple.select.js" type="text/javascript"></script>

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
	window.location.href="<%=request.getContextPath()%>/kufangBranchMap/add";
}

function openedit(fromId){
	window.location.href="<%=request.getContextPath()%>/kufangBranchMap/edit/"+fromId;
}

function opendel(fromId){
	$.ajax({
		type: "POST",
		url:'<%=request.getContextPath()%>/kufangBranchMap/del/'+fromId,
		data:{},
		dataType:"json",
		success : function(data) {   
			alert(data.error); 
			if (data.errorCode == 0) {
				location.href='<%=request.getContextPath()%>/kufangBranchMap/list?';
			}
		}
	});
} 
 
$(document).ready(function(){
	
    $("#fromBranchId").multipleSelect({
        placeholder: "----请选择----",
        filter: true,
        single: true
    });
    $("#toBranchId").multipleSelect({
        placeholder: "----请选择----",
        filter: true,
        single: true
    });
}) ;
</script>
</head>

<body style="background:#f5f5f5">

<div class="right_box">
	<div class="inputselect_box">
	<span></span>
	<form action="<%=request.getContextPath()%>/kufangBranchMap/list" method="post" id="searchForm" method="post" >
		分拣库：
			<select id="fromBranchId" name="fromBranchId">
				<option value="0" selected>----请选择----</option>
				<%for(Branch b : branchlist){ %>
					<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
				<%} %>
			</select>
		配送站点：
         	<select id="toBranchId"  name="toBranchId" >
				<option value="0" selected>----请选择----</option>
				<%for(Branch b : branchlist){ %>
					<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
				<%} %>
			</select>
			
		<input type="submit"  id="find" value="查询" class="input_button2" />
		<input name="" type="button" value="创建分拣流向" class="input_button1"  id="add1"  onclick="openadd()"/>
		<!-- <input type="button"  onclick="location.href='1'" value="返回" class="input_button2" /> -->
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	<tr class="font_1">
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">分拣库</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">配送站点</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		 <% 
		 Iterator it=kfNameMap.keySet().iterator();
		 while(it.hasNext()){
			 Long key=(Long)it.next();
			 String[] row=(String[])kfNameMap.get(key);
		 %>
		<tr>
			<td width="10%" align="center" valign="middle" ><%=row[0]%></td>
			<td width="80%" align="center" valign="middle" ><%=row[1]%></td>
			<td width="10%" align="center" valign="middle" >
			[<a href="javascript:if(confirm('这会删除此分拣库的所有分拣流向，确定要删除?')){opendel('<%=key%>');}">删除</a>]
			[<a href="javascript:openedit('<%=key%>');">修改</a>]
			</td>
		</tr>
		<%} %>
	</table>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
</div>
			
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#fromBranchId").val(<%=request.getParameter("fromBranchId") %>);
$("#toBranchId").val(<%=request.getParameter("toBranchId") %>);

</script>

</body>
</html>