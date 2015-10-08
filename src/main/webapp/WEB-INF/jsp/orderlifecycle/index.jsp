<%@page import="cn.explink.domain.Customer"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
	<title>订单生命周期报表</title>
	<t:base type="jquery,easyui,DatePicker,autocomplete,bootsprap,layer"></t:base>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/dmp40/plug-in/tools/css/common.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
	
	<link href="<%=request.getContextPath()%>/dmp40/plug-in/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/eap/sys/js/eapTools.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40//plug-in/tools/curdtools.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40//plug-in/tools/easyuiextend.js"></script>
	
	<script src="<%=request.getContextPath()%>/js/jquery-1.8.0.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js" type="text/javascript"></script>
	
	<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
	<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
	
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/easyui/themes/default/easyui.css" type="text/css" media="all" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
	<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
		
	
	
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
	<div data-options="region:'center'" style="overflow-x: hidden; overflow-y: auto;">
		<table id="dg"
				class="easyui-datagrid" toolbar="#signFee_toolbar" pagination="true"
				showFooter="true" fitColumns="true" fit="true" singleSelect="false">
				<thead>
					<tr>
						<th field="branchName" rowspan="2" align="center" width="100px;">站点</th>
						<th field="initialBalance" rowspan="2" align="center" width="100px;">期初余额</th>
						<th colspan="2" align="center" width="400px;">配送成功(代收货款)</th>
						<th colspan="1" align="center" width="200px;">配送成功(上门退运费)</th>
						<th colspan="2" align="center" width="400px;">实际回款</th>
						<th field="differenceCODAmount" rowspan="2" align="center" width="200px;">调整金额(代收货款)</th>
						<th field="differenceSmtFreightAmount" rowspan="2" align="center" width="200px;">调整金额(上门退运费)</th>
						<th field="notReturningAmount" rowspan="2" align="center" width="100px;">未返金额</th>
						<th field="branchInventory" rowspan="2" align="center" width="100px;">站点库存</th>
						<th field="branchTransit" rowspan="2" align="center" width="100px;">站点在途</th>
						<th field="branchBail" rowspan="2" align="center" width="100px;">站点保证金</th>
						<th field="riskExposure" rowspan="2" align="center" width="100px;">风险敞口</th>
					</tr>
					<tr>
						<th field="cash" align="center" width="100px;">现金</th>
						<th field="pos" align="center" width="100px;">POS刷卡</th>
						<th field="smtFreightCash" align="center" width="200px;">现金</th>
						<!-- <th field="scan" align="center" width="60px;">COD扫码</th>
						<th field="other" align="center" width="60px;">其他</th> -->
						
						<th field="remittanceCash" align="center" width="100px;">汇款金额</th>
						<th field="remittancePos" align="center" width="100px;">POS收款</th>
						<!-- <th field="actualscan" align="center" width="60px;">COD扫码</th>
						<th field="actualother" align="center" width="60px;">其他</th> -->
					</tr>
				</thead>
			</table>
			<div id="signFee_toolbar">
			<table  cellspacing="0" border="0" cellpadding="0">
				<tr>
					<td colspan="2">
						<table width="100%" height="82" border="0">
							<tr>
								<td>客户：</td>
								<td><select name="customers" id="customers" multiple="multiple" style="width: 150px;">
									<%for(Customer customer : customerList){%>
										<option value="<%=customer.getCustomerid()%>"><%=customer.getCustomername()%></option>
									<%}%>
								</select>
									[<a href="javascript:multiSelectAll('branchids',1,'请选择站点');">全选</a>]
									[<a href="javascript:multiSelectAll('branchids',0,'请选择站点');">取消全选</a>]
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<table width="100%" height="20" border="0">
							<tr>
								<td>
									<div class="btn btn-default" onclick="selectQuery();" style="margin-right:5px;" ><i class="icon-search"></i>查询</div>
					          		<div class="btn btn-default" onclick="billExportExecute();" style="margin-right:5px;" ><i class="icon-download-alt"></i></i>导出</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div id="dlg_Common" class="easyui-dialog" style="width: 1000px; height: 420px; padding: 10px 10px"	closed="true" buttons="#dlgCommon-buttons1">
		<div id="html_Content1"></div>
	</div>
	<div id="dlgCommon-buttons1">
		<div class="btn btn-default" id="importButton2" onclick="closeWindow()">
			关闭
		</div>
	</div>
<!-- 	<script type="text/javascript" src="webpage/fnc/js/multiSelcet/jquery.multiSelect.js"></script> -->
<!-- 	<script type="text/javascript" src="webpage/fnc/js/multiSelcet/jquery.bgiframe.min.js"></script> -->
<!-- 	<script type="text/javascript" src="webpage/fnc/js/jquery.ui.message.min.js"></script> -->
<!-- 	<script type="text/javascript" src="webpage/fnc/js/multiSelcet/MyMultiSelect.js"></script> -->
	<script type="text/javascript" src="fnc_rpt_orderlifecycle.js"></script>
	<script type="text/javascript">var _ctx = "<%=request.getContextPath()%>";</script>
</body>
</html>