<%@page import="cn.explink.domain.ParameterDetail,cn.explink.enumutil.*,cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<ParameterDetail> pdList = (List<ParameterDetail>)request.getAttribute("pdList");
	Page page_obj = (Page)request.getAttribute("page_obj");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>操作设置</title>
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
	<span><input name="" type="button" value="创建操作设置" class="input_button1"  id="" onclick="window.location.href='<%=request.getContextPath()%>/parameterDetail/add';" />
	</span>
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm" method="post" >
		当前环节：
			<select id="flowordertype" name="flowordertype">
				<option value="-1" selected>----请选择----</option>
				<option value="<%=FlowOrderTypeEnum.TiHuo.getValue() %>" ><%=FlowOrderTypeEnum.TiHuo.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.RuKu.getValue() %>" ><%=FlowOrderTypeEnum.RuKu.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue() %>" ><%=FlowOrderTypeEnum.ChuKuSaoMiao.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.FenZhanLingHuo.getValue() %>" ><%=FlowOrderTypeEnum.FenZhanLingHuo.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue() %>" ><%=FlowOrderTypeEnum.TuiGongYingShangChuKu.getText() %></option>
				<option value="<%=FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() %>" ><%=FlowOrderTypeEnum.GongYingShangJuShouFanKu.getText() %></option>
			</select>
		必选字段：
         	<select id="filed"  name="filed">
				<option value="" selected>----请选择----</option>
				<option value="branchid" >下一站</option>
				<option value="customerid" >供货商</option>
				<option value="driverid" >司机</option>
				<option value="truckid" >车辆</option>
				<option value="baleid" >包号</option>
				<option value="comment" >备注</option>
			</select>
		<input type="submit" onclick="$('#searchForm').attr('action',1);return true;" id="find" value="查询" class="input_button2" />
		<!-- <input type="button"  onclick="location.href='1'" value="返回" class="input_button2" /> -->
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	<tr class="font_1">
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">当前环节</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">必选字段</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		 <% for(ParameterDetail pd : pdList){ %>
		<tr>
			<%for(FlowOrderTypeEnum ce : FlowOrderTypeEnum.values()){if(pd.getFlowordertype()==ce.getValue()){ %>
				<td width="10%" align="center" valign="middle" ><%=ce.getText() %></td>
			<%}} %>
			<td width="10%" align="center" valign="middle" ><%=pd.getName() %></td>
			<td width="10%" align="center" valign="middle" >
			[<a href="javascript:if(confirm('确定要删除?')){del('<%=pd.getId() %>');}">删除</a>]
			[<a href="<%=request.getContextPath()%>/parameterDetail/edit/<%=pd.getId() %> ">修改</a>]
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
$("#filed").val(<%=request.getParameter("filed") %>);
</script>
<!-- 删除订单流程的ajax地址 -->
<input type="hidden" id="del" value="<%=request.getContextPath()%>/parameterDetail/del/" />
</body>
</html>