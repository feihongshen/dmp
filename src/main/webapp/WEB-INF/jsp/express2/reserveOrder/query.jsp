<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>快递预约单查询</title>
<%@ include file="common.jsp" %>
</head>
<body class="easyui-layout" leftmargin="0" topmargin="0">
	<div data-options="region:'center'"
		style="height: 100%; overflow-x: auto; overflow-y: auto;">
		<table id="dg_rsList"></table>
		<div id="signFee_toolbar" style="padding: 10px">
		<form action="" id="search_form" style="margin:0px;">
			<table id="search_table">
				<tr>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">预约单号：</td>
					<td>
						<input id="reserveOrderNo" name="reserveOrderNo" type ="text" style="width:140px;"/>
					</td>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">预约时间：</td>
					<td colspan="3">
						<input type ="text" name ="appointTimeStart" id="appointTimeStart"  value="" readonly="readonly" style="width:150px;cursor:pointer" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:'#F{$dp.$D(\'appointTimeEnd\')}'})"/>
						至
						<input type ="text" name ="appointTimeEnd" id="appointTimeEnd"  value=""  readonly="readonly" style="width:150px;cursor:pointer" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'#F{$dp.$D(\'appointTimeStart\')}'})"/>
					</td>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">市区：</td>
					<td>
						<select name="cnorProv"  id="cnorProv" style="width:100px;">
							<option value="" selected="selected">市</option>
							<c:forEach items="${cityList}" var="list">
								<option value="${list.id}" code="${list.code}">${list.name}</option>
							</c:forEach>
						</select>
						<select name="cnorCity"  id="cnorCity" style="width:100px;">
							<option value="">区/县</option>
						</select>
					</td>
				</tr>
				<tr>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">手机/固话：</td>
					<td>
						<input id="cnorMobile" name="cnorMobile" type ="text" style="width:140px;"/>
					</td>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">站点：</td>
					<td>
						<select name="acceptOrg"  id="acceptOrg" style="width:140px;">
							<option value="">请选择</option>
							<c:forEach items="${branchList}" var="list">
								<option value="${list.branchid}">${list.branchname}</option>
							</c:forEach>
						</select>
					</td>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">快递员：</td>
					<td>
						<select name="courier"  id="courier" style="width:140px;">
							<option value="">快递员</option>
						</select>
					</td>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">预约单状态：</td>
					<td>
						<select name="reserveOrderStatusList" id="reserveOrderStatusList" style="width:140px;">
							<option value="">请选择</option>
							<c:forEach items="${orderStatusList }" var="orderStatus">
								<option value="${orderStatus.index}">${orderStatus.name}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
			</table>
		</form>
			<div style="margin:0px;" >
		    		<div class="btn btn-default" onclick="doSearch();" style="margin-right:5px;" id= "searchData"><i class="icon-search"></i>查询</div>
		    		<div class="btn btn-default" onclick="exportExcel();" style="margin-left:5px;"><i class="icon-download-alt"></i>导出</div>
		    </div>
		</div>
	</div>
</body>
<script type="text/javascript">
	/**
	 * 初始化表格
	 */
	function initDataGrid() {
		$('#dg_rsList').datagrid({
			url: contextPath + '/express2/reserveOrder/queryList/query',
			toolbar: "#signFee_toolbar",
			showFooter: true,
			fit: true,
			fitColumns: false,
			singleSelect: true,
			pageSize: 10,
			rownumbers: true,
			pagination: true,
			pageList: [10,30,50],
		    columns:[[
		        {field:'omReserveOrderId', title:'预约单id', hidden:true},
		        {field:'reserveOrderNo', title:'预约单号', width:130, align:'center',
		        	formatter: reserveOrderNoFormatter
		       	},
		        {field:'appointTimeStr', title:'下单时间', width:130, align:'center'},
		        {field:'cnorName', title:'寄件人', width:100, align:'center'},
		        {field:'cnorMobile', title:'手机', width:100, align:'center'},
		        {field:'cnorTel', title:'固话', width:100, align:'center'},
		        {field:'cnorAddr', title:'寄件地址', width:130, align:'center'},
		        {field:'requireTimeStr', title:'预约上门时间', width:130, align:'center'},
		        {field:'reservrOrderStatusName', title:'预约单状态', width:100, align:'center'},
		        {field:'reason', title:'原因', width:130, align:'center'},
		        {field:'transportNo', title:'运单号', width:100, align:'center'},
		        {field:'acceptOrgName', title:'站点', width:100, align:'center'},
		        {field:'courierName', title:'快递员', width:80, align:'center'},
		        {field:'cnorRemark', title:'备注', width:80, align:'center'}
		    ]]
		});
	}
	
	function doSearch() {
		//查询list
		$('#dg_rsList').datagrid('load',{
			reserveOrderNo:$("#reserveOrderNo").val(),
			appointTimeStart:$("#appointTimeStart").val(),
			appointTimeEnd:$("#appointTimeEnd").val(),
			cnorProv:$("#cnorProv").val(),
			cnorCity:$("#cnorCity").val(),
			cnorMobile:$("#cnorMobile").val(),
			acceptOrg:$("#acceptOrg").val(),
			courier:$("#courier").val(),
			reserveOrderStatusList:$("#reserveOrderStatusList").val()
		});
	}
	
	$(function() {
		//单选模糊查询下拉框
		//$("#search_table select").combobox();
		
		//初始化表格
		initDataGrid();
	});
</script>
</html>