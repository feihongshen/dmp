<%@page import="com.pjbest.deliveryorder.enumeration.ReserveOrderStatusEnum"%>
<%@ page import="cn.explink.service.express2.ReserveOrderService" %>
<%@ page import="cn.explink.enumutil.ReserveOrderQueryTypeEnum" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>快递预约单处理</title>
    <%@ include file="common.jsp" %>
    <script src="${pageContext.request.contextPath}/js/express2/reserveOrder/handle.js" type="text/javascript"></script>
</head>

<body class="easyui-layout" leftmargin="0" topmargin="0">
	<div data-options="region:'center'"
		style="height: 100%; overflow-x: auto; overflow-y: auto;">
		<table id="dg_rsList"></table>
		<div id="signFee_toolbar" style="padding: 10px">
		<form action="" id="searchForm" style="margin:0px;">
			<table id="search_table">
        <tr>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">预约单号：</td>
					<td>
						<input id="reserveOrderNo" name="reserveOrderNo" type ="text" style="width:140px;"/>
					</td>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">下单时间：</td>
	                    <td colspan="3">
							<input type ="text" name ="appointTimeStart" id="appointTimeStart"  value="" readonly="readonly" style="background-color:#fff;width:150px;cursor:pointer" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:'#F{$dp.$D(\'appointTimeEnd\')}'})"/>
							至
							<input type ="text" name ="appointTimeEnd" id="appointTimeEnd"  value=""  readonly="readonly" style="background-color:#fff;width:150px;cursor:pointer" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'#F{$dp.$D(\'appointTimeStart\')}'})"/>
						</td>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">市区：</td>
					<td>
						<select name="cnorCity"  id="cnorCity" style="width:100px;">
                                    <option value="" selected="selected">市</option>
                                    <c:forEach items="${cityList}" var="list">
                                        <option value="${list.id}" code="${list.code}">${list.name}</option>
                                    </c:forEach>
                                </select>
						<select name="cnorRegion"  id="cnorRegion" style="width:100px;">
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
                                        <option value="${list.branchid}" tpsbranchcode="${list.tpsbranchcode }">${list.branchname}</option>
                                    </c:forEach>
                                </select>
                            </td>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">快递员：</td>
                            <td>
						<select name="courier"  id="courier" style="width:140px;">
                                    <option value="">请选择</option>
                                </select>
                            </td>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:80px;">预约单状态：</td>
					<td>
						<select name="reserveOrderStatusList" id="reserveOrderStatusList" style="width:140px;">
							<option value="">请选择</option>
							<c:forEach items="${reserveOrderStatusList }" var="orderStatus">
								<option value="${orderStatus.index}">${orderStatus.name}</option>
							</c:forEach>
                                </select>
                            </td>
                        </tr>
                    </table>
               		</form>
			<div style="margin:0px;" >
		    	<div class="btn btn-default" onclick="doSearch();" style="margin-right:5px;" id= "searchData"><i class="icon-search"></i>查询</div>
		    	<div class="btn btn-default" id="editReserveOrderPanelBtn" style="margin-right:5px;"><i class="icon-plus"></i>修改</div>
		    	<div class="btn btn-default" id="deleteReserveOrderBtn" style="margin-right:5px;"><i class="icon-remove"></i>关闭</div>
		    	<div class="btn btn-default" handleType="handle" id="returnToCentralBtn" style="margin-right:5px;"><i class="icon-arrow-up"></i>退回</div>
		    	<div class="btn btn-default" id="distributeBranchBtn" style="margin-right:5px;"><i class="icon-eye-open"></i>分配站点</div>
		    	<div class="btn btn-default" onclick="exportExcel();" style="margin-left:5px;"><i class="icon-download-alt"></i>导出</div>
                                </div>
                                </div>
                                </div>
 <%@ include file="/WEB-INF/jsp/express2/reserveOrder/updateReserveOrder.jsp" %>
<div id="dialog2" title="关闭预约单" style="display:none;">
    <div style="margin-top: 20px; margin-left:10px;margin-right:10px;">
        <table>
            <tr>
                <td style="border: 0px; text-align: left; vertical-align: middle;padding-left: 10px;width: 40%;">
                    备注：
                </td>

            </tr>
            <tr>
                <td style="border: 0px; vertical-align: middle;">
                    <textarea id="closeReason" rows=5 name="" class="textarea easyui-validatebox"></textarea>
                </td>
            </tr>
        </table>
        <hr style="margin-top:20px;margin-bottom:20px; border-top:1px solid #cccccc;"/>
        <div class="pull-right">
            <div class="btn btn-default" style="margin-right:5px;" id="confirmCloseReserveOrderBtn"><i
                    class="icon-ok"></i>确定
            </div>
            <div class="btn btn-default" id="closeCloseReserveOrderPanel"><i class="icon-remove"></i>取消</div>
        </div>
    </div>
</div>
<div id="dialog3" title="退回" style="display:none;">
    <div style="margin-top: 20px; margin-left:10px;margin-right:10px;">
        <table>
            <tr>
                <td style="border: 0px; text-align: left; vertical-align: middle;padding-left: 10px;width: 40%;">
                    备注：
                </td>

            </tr>
            <tr>
                <td style="border: 0px; vertical-align: middle;">
                    <textarea id="returnReason" rows=5 name="" class="textarea easyui-validatebox"></textarea>
                </td>
            </tr>
        </table>
        <hr style="margin-top:20px;margin-bottom:20px; border-top:1px solid #cccccc;"/>
        <div class="pull-right">
            <div class="btn btn-default" style="margin-right:5px;" id="confirmReturnToCentralBtn"><i
                    class="icon-ok"></i>确定
            </div>
            <div class="btn btn-default" id="closeReturnToCentralPanel"><i class="icon-remove"></i>取消</div>
        </div>
    </div>
</div>
<div id="dialog4" title="分配站点" style="display:none;">
    <div style="margin-top: 20px; margin-left:10px;margin-right:10px;">
        <table>
            <tr>
                <td>站点：</td>
                <td>
                    <select id="distributeBranchSelect" name="distributeBranchSelect">
                        <option value="">请选择</option>
                        <c:forEach items="${branchList}" var="list">
                            <option value="${list.branchid}">${list.branchname}</option>
                        </c:forEach>
                        <%--<option value="">---请选择---</option>--%>
                        <%--<%for (BillTypeEnum bs : BillTypeEnum.values()) { %>--%>
                        <%--<option value="<%=bs.getValue() %>"><%=bs.getText()%>--%>
                        <%--</option>--%>
                        <%--<%} %>--%>
                    </select>
                </td>

            </tr>
            <%--<tr>
                <td>快递员：</td>
                <td>
                    <select id="distributeCourierSelect" name="distributeCourierSelect">
                        <option value="">请选择</option>
                        &lt;%&ndash;<option value="">---请选择---</option>&ndash;%&gt;
                        &lt;%&ndash;<%for (BillTypeEnum bs : BillTypeEnum.values()) { %>&ndash;%&gt;
                        &lt;%&ndash;<option value="<%=bs.getValue() %>"><%=bs.getText()%>&ndash;%&gt;
                        &lt;%&ndash;</option>&ndash;%&gt;
                        &lt;%&ndash;<%} %>&ndash;%&gt;
                    </select>
                </td>
            </tr>--%>
        </table>
        <hr style="margin-top:20px;margin-bottom:20px; border-top:1px solid #cccccc;"/>
        <div class="pull-right">
            <div class="btn btn-default" style="margin-right:5px;" id="confirmDistributeBranchBtn"><i
                    class="icon-ok"></i>确定
            </div>
            <div class="btn btn-default" id="closeDistributeBranchPanel"><i class="icon-remove"></i>取消</div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">

	var queryType = "<%=ReserveOrderQueryTypeEnum.HANDLE.getValue()%>;"
    var isHandlePage = true;

	/**
	 * 初始化表格
	 */
	function initDataGrid() {
		$('#dg_rsList').datagrid({
			url: contextPath + '/express2/reserveOrder/queryList/handle',
			toolbar: "#signFee_toolbar",
			showFooter: true,
			fit: true,
			fitColumns: false,
			singleSelect: false,
			pageSize: 10,
			rownumbers: true,
			pagination: true,
			pageList: [10,30,50],
		    columns:[[
		        {field:'omReserveOrderId', title:'预约单id', checkbox: true},
		        {field:'reserveOrderNo', title:'预约单号', width:130,
		        	formatter: reserveOrderNoFormatter
		       	},
		        {field:'appointTimeStr', title:'下单时间', width:130},
		        {field:'cnorName', title:'寄件人', width:100},
		        {field:'custName', title:'寄件公司', width:100},
		        {field:'cnorMobile', title:'手机', width:100},
		        {field:'cnorTel', title:'固话', width:100},
		        {field:'cnorAddr', title:'寄件地址', width:130,hidden:'true'},
		        {field:'cnorDetailAddr', title:'寄件地址', width:130},
		        {field:'requireTimeStr', title:'预约上门时间', width:130},
                {field:'cnorRemark', title:'寄件人备注', width:80},
//		        {field:'reserveOrderStatus', title:'预约单状态', width:100,display:'none'},
		        {field:'reserveOrderStatusName', title:'预约单状态', width:100},
		        {field:'reason', title:'原因', width:130},
//		        {field:'transportNo', title:'运单号', width:100},
		        {field:'acceptOrgName', title:'站点', width:100},
		        {field:'courierName', title:'快递员', width:80},
//		        {field:'remark', title:'备注', width:80},
		        {field:'recordVersion',hidden : true, width:80}
		    ]]
		});
	}
	
	function doSearch() {
		//查询list
		$('#dg_rsList').datagrid('load',{
			reserveOrderNo:$("#reserveOrderNo").val(),
			appointTimeStart:$("#appointTimeStart").val(),
			appointTimeEnd:$("#appointTimeEnd").val(),
			cnorRegion:$("#cnorRegion").val(),
			cnorCity:$("#cnorCity").val(),
			cnorMobile:$("#cnorMobile").val(),
			acceptOrg:$("#acceptOrg option:selected").attr("tpsbranchcode"),
			courier:$("#courier").val(),
			reserveOrderStatusList:$("#reserveOrderStatusList").val()
		});
	}
	
	function exportExcel() {
		var $searchForm = $("#searchForm");
		//保存现场
		var action = $searchForm.attr("action");
		var target = $searchForm.attr("target");
		//提交请求
		$searchForm.attr("action", contextPath + "/express2/reserveOrder/exportExcel/handle");
		$searchForm.attr("target", "_blank");
		$searchForm.submit();
		//恢复现场
		$searchForm.attr("action", action);
		$searchForm.attr("target", target);
	}
    // 省点退回 - 退回类型为省公司超区
    var returnType = "<%= ReserveOrderService.PJReserverOrderOperationCode.ShengGongSiChaoQu.getValue()%>";

    //已分配省公司
    var hadAllocationPro = "<%= ReserveOrderStatusEnum.HadAllocationPro.getIndex()%>";
    var hadAllocationStation = "<%= ReserveOrderStatusEnum.HadAllocationStation.getIndex()%>";
    //站点超区
    var haveStationOutZone = "<%= ReserveOrderStatusEnum.HaveStationOutZone.getIndex()%>";
    //省公司超区
    <%--var haveProOutZone = "<%= ReserveOrderStatusEnum.HaveStationOutZone.getIndex()%>";--%>

    $(function() {
		//单选模糊查询下拉框
		//$("#search_table select").combobox();
		
		//初始化表格
		initDataGrid();
	});
</script>
</html>