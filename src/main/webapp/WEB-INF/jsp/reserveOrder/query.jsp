<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/plug-in//jquery/jquery-1.8.3.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/layer.min.js"></script>
    <link id="skinlayercss" href="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/skin/layer.css" rel="stylesheet"
          type="text/css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/plug-in/tools/dataformat.js"></script>
    <link id="easyuiTheme" rel="stylesheet"
          href="<%=request.getContextPath()%>/dmp40/plug-in/easyui/themes/default/easyui.css" type="text/css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/dmp40/plug-in/easyui/themes/icon.css" type="text/css">
    <link rel="stylesheet" type="text/css"
          href="<%=request.getContextPath()%>/dmp40/plug-in/accordion/css/accordion.css">
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/dmp40/plug-in/easyui/jquery.easyui.min.1.3.2.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/dmp40/plug-in/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/plug-in/tools/syUtil.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/dmp40/plug-in/easyui/extends/datagrid-scrollview.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/dmp40/plug-in/My97DatePicker/WdatePicker.js"></script>
    <link type="text/css" rel="stylesheet"
          href="<%=request.getContextPath()%>/dmp40/plug-in/My97DatePicker/skin/WdatePicker.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/dmp40/plug-in/tools/css/common.css" type="text/css">
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/plug-in/tools/curdtools.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/plug-in/tools/easyuiextend.js"></script>
    
    <link rel="stylesheet"
          href="<%=request.getContextPath()%>/dmp40/plug-in/jquery/jquery-autocomplete/jquery.autocomplete.css"
          type="text/css">
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/dmp40/plug-in/jquery/jquery-autocomplete/jquery.autocomplete.min.js"></script>
    <link href="<%=request.getContextPath()%>/dmp40/plug-in/bootstrap/css/bootstrap.min.css" rel="stylesheet"
          type="text/css">
    <link href="<%=request.getContextPath()%>/dmp40/plug-in/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet"
          type="text/css">
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/dmp40/plug-in/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/dmp40/plug-in/bootstrap/js/bootstrap-table.js"></script>
      <link href="<%=request.getContextPath()%>/js/multiSelcet/multiple-select.css" rel="stylesheet" type="text/css">
      <script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiple.select.js" type="text/javascript"></script>
</head>
<body class="easyui-layout" leftmargin="0" topmargin="0">
	<div data-options="region:'center'"
		style="height: 100%; overflow-x: auto; overflow-y: auto;">
		<table id="dg_rsList" class="easyui-datagrid"
			url="queryList" toolbar="#signFee_toolbar"
			showFooter="true" fit="true" fitColumns="false"  singleSelect="true"
			width="100%" pageSize="10" rownumbers="true" pagination="true"  pageList="[10,30,50]">
			<thead>
				<tr>
					<th field="omReserveOrderId"  hidden="true" align="center" width="180px;">id</th>
					<th field="reserveOrderNo" align="center" width="130px;">预约单号</th>
					<th field="appointTimeStr" align="center" width="130px;">下单时间</th>
					<th field="cnorName" align="center" width="100px;">寄件人</th>
					<th field="cnorMobile" align="center" width="100px;">手机</th>
					<th field="cnorTel" align="center" width="100px;">固话</th>
					<th field="cnorAddr" align="center" width="130px;">寄件地址</th>
					<th field="requireTimeStr" align="center" width="130px;">预约上门时间</th>
					<th field="reservrOrderStatusName" align="center" width="100px;">预约单状态</th>
					<th field="reason" align="center" width="130px;">原因 </th>
					<th field="transportNo" align="center" width="100px;">运单号 </th>
					<th field="acceptOrgName" align="center" width="100px;">站点</th>
					<th field="courierName" align="center" width="80px;">快递员</th>
					<<th field="cnorRemark" align="center" width="80px;">备注</th>
				</tr>
			</thead>
		</table>

		<div id="signFee_toolbar" style="padding: 10px">
		<form action="" id="search_form" style="margin:0px;">
			<table id="search_table">
				<tr>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">预约单号：</td>
					<td>
						<input type ="text" style="width:140px;"/>
					</td>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">预约时间：</td>
					<td>
						<input type ="text" name ="createTimeStart" id="createTimeStart"  value=""  style="width:140px;" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"/>
					</td>
					<td style="border: 0px; text-align: center; vertical-align: middle;width:65px;">至</td>
					<td>
						<span id="createTimeEnd_div">
							<input type ="text" name ="createTimeEnd" id="createTimeEnd"  value="" style="width:140px;" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"/>
						</span>
					</td>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">市区：</td>
					<td>
						<select name="cityId"  id="cityId" style="width:100px;">
							<option value=-1>全部</option>
							<option value ="1">测试1</option>
							<option value ="2">测试2</option>
						</select>
						<select name="areaId"  id="areaId" style="width:100px;">
							<option value=-1>全部</option>
							<option value ="1">测试1</option>
							<option value ="2">测试2</option>
						</select>
					</td>
				</tr>
				<tr>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">手机/固话：</td>
					<td>
						<input type ="text" style="width:140px;"/>
					</td>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">站点：</td>
					<td>
						<input type ="text" style="width:140px;"/>
					</td>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">快递员：</td>
					<td>
						<select name="kId"  id="kId" style="width:140px;">
							<option value=-1>全部</option>
							<option value ="1">测试1</option>
							<option value ="2">测试2</option>
						</select>
					</td>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">预约状态：</td>
					<td>
						<select name="yId"  id="yId" style="width:140px;">
							<option value=-1>全部</option>
							<option value ="1">测试1</option>
							<option value ="2">测试2</option>
						</select>
					</td>
				</tr>
			</table>
		</form>
			<div style="margin:0px;text-align:center;" >
		    		<div class="btn btn-default" onclick="doSearch();" style="margin-right:5px;" id= "searchData"><i class="icon-search"></i>查询</div>
		    		<div class="btn btn-default" onclick="exportExcel();" style="margin-left:20px;"><i class="icon-download-alt"></i>导出</div>
		    </div>
		</div>
	</div>
	<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
	<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
</body>
<script type="text/javascript">
	$(function() {
		//单选模糊查询下拉框
		//$("#search_table select").combobox();
	});
	
	function doSearch() {
		//查询list
		$('#dg_rsList').datagrid('load',{
			
		});
	}
</script>
</html>