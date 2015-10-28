<%@page import="cn.explink.domain.Customer"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
	String nowdate = (String)request.getAttribute("nowdate");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
	<title>订单生命周期报表</title>
	
	<script src="<%=request.getContextPath()%>/js/jquery-1.8.0.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/layer.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js" type="text/javascript"></script>
	
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/easyui/themes/default/easyui.css" type="text/css" media="all" />
	<link href="<%=request.getContextPath()%>/dmp40/plug-in/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/eap/sys/js/eapTools.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40//plug-in/tools/curdtools.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40//plug-in/tools/easyuiextend.js"></script>
	
	
	
	<script src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<%-- 	<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script> --%>
	<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiple.select.js" type="text/javascript"></script>
<%-- 	<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script> --%>
	<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<%-- 	<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" /> --%>
	<link href="<%=request.getContextPath()%>/js/multiSelcet/multiple-select.css" rel="stylesheet" type="text/css" />
	
	
<%-- 	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" /> --%>
<%-- 	<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script> --%>
<%-- 	<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script> --%>
		
	
	
	<style type="text/css">
	.multiSelect{
		display: inline-block;
	}
	.multiSelect input{
	 	margin: 0px;
	  	padding: 0px;
	  	line-height: 15px;
	  	border: 0px;
		height:25px;
	}
</style>
</head>

<body class="easyui-layout" leftmargin="0" topmargin="0">
	<div data-options="region:'center'" style="height:100%;overflow-x: auto; overflow-y: auto;">
		<table id="dg"
				class="easyui-datagrid" toolbar="#signFee_toolbar" 
				showFooter="true" fitColumns="false" fit="true" singleSelect="false" width="100%">
				<thead>
					<tr>
						<th field="customername" rowspan="2" align="center" width="100px;">客户</th>
						<th colspan="2" align="center" width="100px;">导入未收货</th>
						<th colspan="2" align="center" width="100px;">提货未入库</th>
						<th colspan="2" align="center" width="120px;">分拣入库未出库</th>
						<th colspan="2" align="center" width="120px;">分拣出库未到站</th>
						<th colspan="2" align="center" width="120px;">站点在站货物</th>
						<th colspan="2" align="center" width="120px;">站点未返代收货款</th>
						<th colspan="2" align="center" width="120px;">退货出站在途</th>
						<th colspan="2" align="center" width="120px;">中转出站在途</th>
						<th colspan="2" align="center" width="120px;">中转库入库未出库</th>
						<th colspan="2" align="center" width="120px;">中转出库未到站</th>
						<th colspan="2" align="center" width="120px;">退货入库未出库</th>
						<th colspan="2" align="center" width="120px;">退货再投未到站</th>
						<th colspan="2" align="center" width="120px;">退客户在途</th>
						<th colspan="2" align="center" width="120px;">退客户未收款</th>
					</tr>
					<tr>
						<th field="count1" align="center" width="60px;">票数</th>
						<th field="amount1" align="center" width="100px;">金额</th>
						<th field="count2" align="center" width="60px;">票数</th>
						<th field="amount2" align="center" width="100px;">金额</th>
						<th field="count3" align="center" width="60px;">票数</th>
						<th field="amount3" align="center" width="100px;">金额</th>
						<th field="count4" align="center" width="60px;">票数</th>
						<th field="amount4" align="center" width="100px;">金额</th>
						<th field="count5" align="center" width="60px;">票数</th>
						<th field="amount5" align="center" width="100px;">金额</th>
						<th field="count6" align="center" width="60px;">票数</th>
						<th field="amount6" align="center" width="100px;">金额</th>
						<th field="count7" align="center" width="60px;">票数</th>
						<th field="amount7" align="center" width="100px;">金额</th>
						<th field="count8" align="center" width="60px;">票数</th>
						<th field="amount8" align="center" width="100px;">金额</th>
						<th field="count9" align="center" width="60px;">票数</th>
						<th field="amount9" align="center" width="100px;">金额</th>
						<th field="count10" align="center" width="60px;">票数</th>
						<th field="amount10" align="center" width="100px;">金额</th>
						<th field="count11" align="center" width="60px;">票数</th>
						<th field="amount11" align="center" width="100px;">金额</th>
						<th field="count12" align="center" width="60px;">票数</th>
						<th field="amount12" align="center" width="100px;">金额</th>
						<th field="count13" align="center" width="60px;">票数</th>
						<th field="amount13" align="center" width="100px;">金额</th>
						<th field="count14" align="center" width="60px;">票数</th>
						<th field="amount14" align="center" width="100px;">金额</th>

					</tr>
				</thead>
			</table>
			<div id="signFee_toolbar">
				<div class="form-inline" style="padding:10px">
				    <label for="customers">客户：</label>
				    <select name="customers" id="customers" multiple="multiple" style="width: 150px;">
						<%for(Customer customer : customerList){%>
							<option value="<%=customer.getCustomerid()%>"><%=customer.getCustomername()%></option>
						<%}%>
					</select>
<!-- 						[<a href="javascript:multiSelectAll('customers',1,'请选择客户');">全选</a>] -->
<!-- 						[<a href="javascript:multiSelectAll('customers',0,'请选择客户');">取消全选</a>] -->
				    <label for="querydate">日期：</label>
				    <input type="text" name="querydate" id="querydate" value="<%=nowdate%>" onclick="WdatePicker({startDate: '%y-%M-%d',dateFmt:'yyyy-MM-dd'})" onchange="initEndTime()" />
				  <div class="btn btn-default" onclick="selectQuery();" style="margin-right:5px;" ><i class="icon-search"></i>查询</div>
				  <div class="btn btn-default" onclick="billExportExecute();" style="margin-right:5px;" ><i class="icon-download-alt"></i>导出</div>
				  <span class="text-info" style="vertical-align: bottom">
				  	 说明：该报表为日报，每日0:00生成截止前一日23:59:59的报表数据
				  </span>
				</div>
		</div>
	</div>
	<div id="dlg_Common" class="easyui-dialog" style="width: 1000px; height: 420px; padding: 10px 10px"	closed="true" buttons="#dlgCommon-buttons1">
<!-- 		<div id="html_Content1"></div> -->
        <div class="btn btn-default" onclick="billDetailExportExecute();" style="margin-right:5px;"><i class="icon-download-alt"></i>导出</div>
		<div class="container">
			<div class="row">
				<div data-options="region:'center'" style="overflow-x: hidden; overflow-y: auto;">
					<table id="ord_dg"
							class="easyui-datagrid"  pagination="true"
							showFooter="false" fitColumns="true" fit="false" singleSelect="false">
							<thead>
								<tr>
									<th field="cwb" align="center" width="50px;">订单号</th>
									<th field="cwbordertypeDesc" align="center" width="30px;">订单类型</th>
									<th field="cwbstateDesc" align="center" width="30px;">订单状态</th>
									<th field="flowordertypeDesc" align="center" width="50px;">订单操作状态</th>
									<th field="sendcarnum" align="center" width="30px;">件数</th>
									<th field="emaildate" align="center" width="30px;">发货日期</th>
									<th field="receivablefee" align="center" width="30px;">应收金额</th>
									<th field="paybackfee" align="center" width="30px;">应退金额</th>
									<th field="shouldfare" align="center" width="30px;">应收运费</th>
									<th field="currentbranchDesc" align="center" width="30px;">当前站点</th>
									<th field="startbranchDesc" align="center" width="30px;">上一站点</th>
									<th field="nextbranchDesc" align="center" width="30px;">下一站点</th>
								</tr>
							</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div id="dlgCommon-buttons1">
		<div class="btn btn-default" id="importButton2" onclick="closeWindow()">
			关闭
		</div>
	</div>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/fnc/common_rpt/order_lifecycle_rpt.js"></script>
	<script type="text/javascript">var _ctx = "<%=request.getContextPath()%>";</script>
</body>
</html>