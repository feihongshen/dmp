<%@page
	import="cn.explink.domain.addressvo.AddressCustomerStationVO,cn.explink.domain.Customer,cn.explink.domain.Branch"%>
<%@page
	import="cn.explink.enumutil.*,cn.explink.util.Page,java.lang.String"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%
	List<AddressCustomerStationVO> listRalations = (List<AddressCustomerStationVO>) request
	.getAttribute("listRalations");
	Page page_obj = (Page) request.getAttribute("page_obj");
	List<Customer> listCustomers = (List<Customer>) request.getAttribute("listCustomers");
	List<Branch> listBranchs = (List<Branch>) request.getAttribute("listBranchs");
	String stationString=(String)request.getAttribute("station");
	String execute_branchidString=(String)request.getAttribute("execute_branchid");
	Long pageNext=(Long)request.getAttribute("page");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统设置</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/multiple-select.css"
	type="text/css" />
<script language="javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.8.0.min.js"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/multiple-select.js"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/js.js"></script>
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
function del(id){
	$.ajax({
		type: "POST",
		url:$("#del").val()+id,
		dataType:"json",
		success : function(data) {
			$("#searchForm").submit();
		}
	});
}

function changeCustomer(){
	$("#searchForm").submit();
}


$(function(){
	$("#customerid").val($("#customerValue").val());
	/*$("#station").val($("#stationValue").val());
	$("#excute_branchid").val($("#branckValue").val()); */

	
$("#station").multipleSelect({
    filter: true
});


$("#excute_branchid").multipleSelect({
    filter: true
});
$("#station").change(function (){
	var thisStation=$(this).val();
	$("#searchForm").submit();	
});
$("#excute_branchid").change(function (){
	var thisBranch=$(this).val();
	$("#searchForm").submit();
});
})

</script>
</head>

<body style="background: #f5f5f5">

	<div class="right_box">
		<div class="inputselect_box">
			<span><input name="" type="button" value="创建映射关系"
				class="input_button1" id=""
				onclick="window.location.href='<%=request.getContextPath()%>/addressCustomerStationMap/add';" />
			</span>
			<form action="1" id="searchForm" method="post">
				地址库站点：<select id="station" name="station" class="select1"
					multiple="multiple">
				 <%
						if(stationString!=null && !"".equals(stationString)){
									String[] stations=stationString.split(",");
								for (Branch branch : listBranchs) {
					%>
					<option value="<%=branch.getBranchid()%>"
						<%for (int i = 0; i < stations.length; i++) {
						if (Long.parseLong(stations[i]) == branch.getBranchid()) {%>
						selected="selected" <%}}%>><%=branch.getBranchname()%></option>
					<%
						}
									}else{
									for (Branch branch : listBranchs) {
					%>
					<option value="<%=branch.getBranchid()%>"><%=branch.getBranchname()%></option>
					<%
						}}
					%> 
				
					

				</select> 客户名称：<select id="customerid" name="customerid" class="select1"
					onChange="changeCustomer();">
					<option></option>
					<%
						for (Customer customer : listCustomers) {
					%>
					<option value="<%=customer.getCustomerid()%>"><%=customer.getCustomername()%></option>
					<%
						}
					%>
				</select>执行站点：<select id="excute_branchid" name="execute_branchid"
					class="select1" multiple="multiple">
					<%
						if(execute_branchidString!=null && !"".equals(execute_branchidString)){
						String[] brancks=execute_branchidString.split(",");
								for (Branch branch : listBranchs) {
					%>
					<option value="<%=branch.getBranchid()%>"
						<%for (int i = 0; i < brancks.length; i++) {
						if (Long.parseLong(brancks[i]) == branch.getBranchid()) {%>
						selected="selected" <%}}%>><%=branch.getBranchname()%></option>
					<%
						}
									}else{
									for (Branch branch : listBranchs) {
					%>
					<option value="<%=branch.getBranchid()%>"><%=branch.getBranchname()%></option>
					<%
						}}
					%>
				</select>

			</form>
		</div>
		<div class="right_title">
			<div class="jg_10"></div>
			<div class="jg_10"></div>
			<div class="jg_10"></div>

			<table width="100%" border="0" cellspacing="1" cellpadding="0"
				class="table_2" id="gd_table">
				<tr class="font_1">
					<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">地址库站点</td>
					<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">客户</td>
					<td width="30%" align="center" valign="middle" bgcolor="#eef6ff">执行站点</td>
					<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
				</tr>
				<%
					for (AddressCustomerStationVO addressCustomerStationVO : listRalations) {
				%>
				<tr>
					<td width="15%" align="center" valign="middle">
						<%
							String[] vobranids = addressCustomerStationVO.getBranchid().split(",");

																																for (int i = 0; i < vobranids.length; i++) {
																																	for (Branch branch : listBranchs) {
																																		if (Long.parseLong(vobranids[i]) == branch.getBranchid()&& branch.getBrancheffectflag().equals("1")) {
						%> <%=branch.getBranchname()%> <%
 	}else if(Long.parseLong(vobranids[i]) == branch.getBranchid()){
 %> <%=branch.getBranchname()+"(停用)"%> <%
 	}
       			}
       		}
 %>
					</td>
					<td width="15%" align="center" valign="middle"><%=addressCustomerStationVO.getCustomerName()%></td>
					<td width="15%" align="center" valign="middle">
						<%
							String[] exvobranids = addressCustomerStationVO.getExecute_branchid().split(",");

																																for (int i = 0; i < exvobranids.length; i++) {
																																	for (Branch branch : listBranchs) {
																																		if (Long.parseLong(exvobranids[i]) == branch.getBranchid()&&branch.getBrancheffectflag().equals("1")) {
						%> <%=branch.getBranchname()%> <%
 	}else if(Long.parseLong(exvobranids[i]) == branch.getBranchid()){
 %> <%=branch.getBranchname()+"(停用)"%> <%
 	}
       			}

       		}
 %>
					</td>
					<td width="10%" align="center" valign="middle">[<a
						href="javascript:if(confirm('确定要删除?')){del(<%=addressCustomerStationVO.getId()%>);}">删除</a>]
						[<a
						href="<%=request.getContextPath()%>/addressCustomerStationMap/edit/<%=addressCustomerStationVO.getId()%>">修改</a>]
					</td>
				</tr>
				<%
					}
				%>

			</table>
			<div class="jg_10"></div>
			<div class="jg_10"></div>
		</div>
		<%
			if (page_obj.getMaxpage() > 1) {
		%>
		<div class="iframe_bottom">
			<table width="100%" border="0" cellspacing="1" cellpadding="0"
				class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
						<a
						href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();">第一页</a>
						<a
						href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious() < 1 ? 1 : page_obj.getPrevious()%>');$('#searchForm').submit();">上一页</a>
						<a
						href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext() < 1 ? 1 : page_obj.getNext()%>');$('#searchForm').submit();">下一页</a>
						<a
						href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage() < 1 ? 1 : page_obj.getMaxpage()%>');$('#searchForm').submit();">最后一页</a>
						共<%=page_obj.getMaxpage()%>页 共<%=page_obj.getTotal()%>条记录 当前第<select
						id="selectPg"
						onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
							<%
								for (int i = 1; i <= page_obj.getMaxpage(); i++) {
							%>
							<option <%if(((Long)pageNext).intValue()==i){%>selected="selected"<%} %> value="<%=i%>"><%=i%></option>
							<%
								}
							%>
					</select>页
					</td>
				</tr>
			</table>
		</div>
		<%
			}
		%>
	</div>
	<div class="jg_10"></div>
	<div class="clear"></div>

	<input type="hidden" id="customerValue" value="${customerid}">
		<%-- 	<input type="hidden" id="stationValue" value="${station}">
	<input type="hidden" id="branckValue" value="${execute_branchid}"> --%>
	<script type="text/javascript">
		
<%-- $("#selectPg").val(<%=request.getAttribute("page") %>); --%>
<%-- $("#name").val(<%=request.getParameter("name") %>); --%>
<%-- $("#value").val(<%=request.getParameter("value") %>); --%>
</script>
	<!-- 删除订单流程的ajax地址 -->
	<input type="hidden" id="del"
		value="<%=request.getContextPath()%>/addressCustomerStationMap/del/" />
</body>
</html>