<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE>未推送EMS订单列表</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=request.getContextPath()%>/js/jquery-1.8.0.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/layer.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/easyui-extend/plugins/easyui/jquery-easyui-1.3.6/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
	
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/easyui/themes/default/easyui.css" type="text/css" media="all" />
	<link href="<%=request.getContextPath()%>/dmp40/plug-in/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/eap/sys/js/eapTools.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40//plug-in/tools/curdtools.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40//plug-in/tools/easyuiextend.js"></script>
	<script src="<%=request.getContextPath()%>/js/easyui-extend/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<%-- 	<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script> --%>
	<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiple.select.js" type="text/javascript"></script>
<%-- 	<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script> --%>
	<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<%-- 	<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" /> --%>
	<link href="<%=request.getContextPath()%>/js/multiSelcet/multiple-select.css" rel="stylesheet" type="text/css" />
<style type="text/css">
.show-grid{
		margin-top: 2px;
		margin-bottom: 2px;
	}
form select,input[type="text"],input[type="number"]{
		font-size: 12px;
		height:30px ;
		width:120px
}
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
.datepicker{
		height:30px !important;
		width:170px !important;
	}
.validatebox-invalid {
	  border-color: #ffa8a8 !important;
	  background-color: #fff3f3 !important;
	  color: #000 !important;
}
</style>
</HEAD>
<script type="text/javascript">var _ctx = "<%=request.getContextPath()%>";</script>
<body class="easyui-layout" leftmargin="0" topmargin="0">
  <div  data-options="region:'center'" style="overflow-x:auto;overflow-y:auto;width:100%;height:98%;">
      <table id="unpush_order_dg" toolbar="#toolbar" class="easyui-datagrid" 
            showFooter="true" fit="true" fitColumns="false"  singleSelect="true" checkOnSelect = "false" selectOnCheck="false"
			width="100%" pageSize="10" rownumbers="true" pagination="true"  pageList="[10,30,50]" >
         <thead>
            <th field="id"  checkbox="true" align="center" ></th>
			<th field="orderNumber" align="center" width="100px;">订单号</th>
			<th field="orderTypeDesc" align="center" width="80px;">订单类型</th>
			<th field="orderStatusDesc" align="center" width="80px;">订单状态</th>
			<th field="orderCurrentStatusDesc" align="center" width="120px;">订单当前状态</th>
			<th field="branchName" align="center" width="100px;">配送站点</th>
			<th field="deliveryTime" align="center" width="100px;">发货时间</th>
			<th field="deliveryCustomer" align="center" width="100px;">发货客户</th>
			<th field="recipientAddress" align="center" width="150px;">收货人地址</th>
			<th field="recipientMobile" align="center" width="100px;">收货人电话</th>
         </thead>
   </table>
   <div id="toolbar" width="100%">
    <form action="" id="search_form" style="margin:0px;">
       <div class="row " style="margin-left:10px">
	     <table class="table" style="border: 1px;align:center; margin-bottom:0px;width:95%;padding-left:10px;">
	       <tr>
	          <td style="border: 0px; text-align: right; vertical-align: middle; width:10%;height:20px;">
	              <font color="red">*</font>发货客户：</td>
			  <td style="border: 0px; vertical-align: middle;width:10%;padding:0px;">
			     <select  name="customerIds" id="customerIds" multiple="multiple" style = "width:120px;">
			        <c:forEach items="${customerList}" var="customer">
					    <option value="${customer.customerid}">${customer.customername}</option>
					 </c:forEach>
			     </select>
		      </td>
		      <td style="border: 0px; text-align: right; vertical-align: middle; width:10%;height:20px;">订单类型：</td>
		      <td style="border: 0px; vertical-align: middle;width:10%;padding:0px;">
		         <select type ="text" name="orderType" id="orderType" >
		            <option value = "-2">全部</option>
		            <c:forEach items="${orderTypes}" var="type">
					    <option value="${type.value}">${type.text}</option>
					 </c:forEach>
		         </select>
		      </td>
              <td style="border: 0px; text-align: right; vertical-align: middle; width:10%;height:20px;">发货时间：</td>
		      <td style="border: 0px; vertical-align: middle;width:50%;padding:0px;">
		         <input type ="text" name="startTime" id="startTime" class="Wdate datepicker" value="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
		           &nbsp; 至  &nbsp;
		         <input type ="text" name="endTime" id="endTime" class="Wdate datepicker" value="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
		      </td>	           
	       </tr>
	     </table>
	     <div class="span12 show-grid">
			 <div class="row-fluid">
			    <div class="span12">
			    	<div class="btn btn-default" onclick="query();" style="margin-right:5px;"><i class="icon-search"></i>查询</div>
			       	<div class="btn btn-default" onclick="exportExcel();" style="margin-right:5px;"><i class="icon-download-alt"></i>导出</div>
			       	<div class="btn btn-default" onclick="resetQueryCondition();" style="margin-right:5px;"><i class="icon-refresh"></i>重置</div>
			     </div>
			  </div>
		 </div>
	   </div>
	 </form>
   </div>
  </div>
<!-- 导出Excel -->
<div id = "export_excel_dlg" style = "display:none;width:800px;">
   <form id = "export_excel_form" method = "post" style = "display:none" target = "exportFrame" />
</div>
<script src="<%=request.getContextPath()%>/js/emsOrder/emsOrderUnpushList.js" type="text/javascript"></script>
</body>
</HTML>
