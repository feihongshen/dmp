<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--<%@ taglib prefix="t" uri="/easyui-tags"%>--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
    // Map<String, List<String>> cityDistrictMap = request.getAttribute("cityDistrictMap") == null ? new HashMap<String, List<String>>() : (Map<String, List<String>>) request.getAttribute("city");
//    List<CustomWareHouse> wareHouseList = request.getAttribute("wareHouseList") == null ? new ArrayList<CustomWareHouse>() : (List<CustomWareHouse>) request.getAttribute("wareHouseList");
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>客户账单核对</title>
    <%@ include file="common.jsp" %>
    <script src="${pageContext.request.contextPath}/js/express2/reserveOrder/handle.js" type="text/javascript"></script>
</head>

<body class="easyui-layout" leftmargin="0" topmargin="0">
	<div data-options="region:'center'"
		style="height: 100%; overflow-x: auto; overflow-y: auto;">
		<table id="dg_rsList" class="easyui-datagrid"
			url="<%=request.getContextPath()%>/express2/reserveOrder/queryList/handle" toolbar="#signFee_toolbar"
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
					<th field="cnorRemark" align="center" width="80px;">备注</th>
				</tr>
			</thead>
		</table>

		<div id="signFee_toolbar" style="padding: 10px">
		<form action="" id="search_form" style="margin:0px;">
			<table id="search_table">
				<tr>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">预约单号：</td>
					<td>
						<input id="reserveOrderNo" name="reserveOrderNo" type ="text" style="width:140px;"/>
					</td>
					<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">预约时间：</td>
					<td>
						<input type ="text" name ="appointTimeStart" id="appointTimeStart"  value="" readonly="readonly" style="width:150px;cursor:pointer" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:'#F{$dp.$D(\'appointTimeEnd\')}'})"/>
					</td>
					<td style="border: 0px; text-align: center; vertical-align: middle;width:65px;">至</td>
					<td>
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
							<option value="">请选择</option>
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
		    	<div class="btn btn-default" id="editPreOrderPanelBtn" style="margin-right:5px;"><i class="icon-plus"></i>修改</div>
		    	<div class="btn btn-default" id="deletePreOrderBtn" style="margin-right:5px;"><i class="icon-arrow-up"></i>关闭</div>
		    	<div class="btn btn-default" id="returnToCentralBtn" style="margin-right:5px;"><i class="icon-remove"></i>退回总部</div>
		    	<div class="btn btn-default" id="distributeBranchBtn" style="margin-right:5px;"><i class="icon-eye-open"></i>分配站点</div>
		    	<div class="btn btn-default" onclick="exportExcel();" style="margin-left:5px;"><i class="icon-download-alt"></i>导出</div>
		    </div>
		</div>
	</div>
	<div id="dialog1" title="修改预约单" style="display:none;">
	    <div style="margin-top: 20px; margin-left:10px;margin-right:10px;">
	        <table>
	            <tr>
	                <td style="border: 0px; text-align: right; vertical-align: middle;padding-left: 10px;width: 40%;">
	                    预约单号：
	                </td>
	                <td style="border: 0px; vertical-align: middle;">
	                    <input/>
	                </td>
	            </tr>
	            <tr>
	                <td style="border: 0px; text-align: right; vertical-align: middle;padding-left: 10px;">寄件人</td>
	                <td style="border: 0px; vertical-align: middle;">
	                    <input/>
	                </td>
	            </tr>
	            <tr>
	                <td style="border: 0px; text-align: right; vertical-align: middle;padding-left: 10px;">地址：</td>
	                <td style="border: 0px; vertical-align: middle;">
	                    <select id="province4edit" disabled="disabled" name="province4edit" style="width: 31%">
	                        <%-- <%for (CustomWareHouse cw : wareHouseList) { %>
	                         <option class="customerid2_<%=cw.getCustomerid() %>"
	                                 value="<%=cw.getWarehouseid() %>"><%=cw.getCustomerwarehouse() %>
	                         </option>
	                         <%} %>--%>
	                    </select>
	                    <select id="city4edit" name="city4edit" style="width: 31%">
	                        <option value="" selected="selected">市</option>
	                        <c:forEach items="${cityList}" var="list">
	                            <option value="${list.id}" code="${list.code}">${list.name}</option>
	                        </c:forEach>
	                    </select>
	                    <select id="county4edit" name="county4edit" style="width: 31%">
	                        <option value="">区/县</option>
	                    </select>
	                </td>
	            </tr>
	            <tr>
	                <td></td>
	                <td><input/></td>
	            </tr>
	            <tr>
	                <td style="border: 0px; text-align: right; vertical-align: middle;padding-left: 10px;">预约上门时间：</td>
	                <td style="border: 0px; vertical-align: middle; ">
	                    <input style="width: 94%;" type="text" name="start_time"
	                           id="start_time" <%--style="height:30px;"--%> value=""
	                           onFocus="WdatePicker({startDate: '%y-%M-%d 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
	                </td>
	            </tr>
	
	        </table>
	        <hr style="margin-top:20px;margin-bottom:20px; border-top:1px solid #cccccc;"/>
	        <div class="pull-right">
	            <div class="btn btn-default" style="margin-right:5px;" id="confirmEditPreOrderBtn"><i
	                    class="icon-ok"></i>确定
	            </div>
	            <div class="btn btn-default" id="closeEditPreOrderPanel"><i class="icon-remove"></i>取消</div>
	        </div>
	    </div>
	</div>
	<div id="dialog2" title="关闭预约单" style="display:none;">
	    <div style="margin-top: 20px; margin-left:10px;margin-right:10px;">
	        <table>
	            <tr>
	                <td style="border: 0px; text-align: left; vertical-align: middle;padding-left: 10px;width: 40%;">
	                    关闭原因：
	                </td>
	
	            </tr>
	            <tr>
	                <td style="border: 0px; vertical-align: middle;">
	                    <textarea id="" rows=5 name="" class="textarea easyui-validatebox"></textarea>
	                </td>
	            </tr>
	        </table>
	        <hr style="margin-top:20px;margin-bottom:20px; border-top:1px solid #cccccc;"/>
	        <div class="pull-right">
	            <div class="btn btn-default" style="margin-right:5px;" id="confirmClosePreOrderBtn"><i
	                    class="icon-ok"></i>确定
	            </div>
	            <div class="btn btn-default" id="closeClosePreOrderPanel"><i class="icon-remove"></i>取消</div>
	        </div>
	    </div>
	</div>
	<div id="dialog3" title="退回总部" style="display:none;">
	    <div style="margin-top: 20px; margin-left:10px;margin-right:10px;">
	        <table>
	            <tr>
	                <td style="border: 0px; text-align: left; vertical-align: middle;padding-left: 10px;width: 40%;">
	                    退回原因：
	                </td>
	
	            </tr>
	            <tr>
	                <td style="border: 0px; vertical-align: middle;">
	                    <textarea id="" rows=5 name="" class="textarea easyui-validatebox"></textarea>
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
	            <tr>
	                <td>快递员：</td>
	                <td>
	                    <select id="distributeKdySelect" name="distributeKdySelect">
	                        <option value="">请选择</option>
	                        <%--<option value="">---请选择---</option>--%>
	                        <%--<%for (BillTypeEnum bs : BillTypeEnum.values()) { %>--%>
	                        <%--<option value="<%=bs.getValue() %>"><%=bs.getText()%>--%>
	                        <%--</option>--%>
	                        <%--<%} %>--%>
	                    </select>
	                </td>
	            </tr>
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
</html>