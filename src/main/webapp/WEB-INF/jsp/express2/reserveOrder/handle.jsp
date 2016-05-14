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
    <script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/plug-in/jquery/jquery-1.8.3.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/layer.min.js"></script>
    <link id="skinlayercss" href="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/skin/layer.css"
          rel="stylesheet"
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
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/dmp40/plug-in/lhgDialog/lhgdialog.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/plug-in/tools/curdtools.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/plug-in/tools/easyuiextend.js"></script>
    <%--<script type="text/javascript" src="eap/sys/js/eapTools.js"></script>--%>
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
    <%--  <link href="webpage/fnc/js/multiSelcet/multiple-select.css" rel="stylesheet" type="text/css">
      <script src="webpage/fnc/js/multiSelcet/jquery.multiple.select.js" type="text/javascript"></script>
      <script type="text/javascript" src="webpage/fnc/js/upload/swfupload.js"></script>
      <script type="text/javascript" src="webpage/fnc/js/upload/jquery.swfupload.js"></script>
      <script type="text/javascript" src="webpage/fnc/js/upload/swfupload.queue.js"></script>--%>
    <script src="${pageContext.request.contextPath}/js/express2/reserveOrder/handle.js" type="text/javascript"></script>
</head>

<body class="easyui-layout" leftmargin="0" topmargin="0">
<div data-options="region:'center'" style="overflow-x:hidden;overflow-y:auto;">

    <table id="dg" style="height:400px;" width="100%" toolbar="#toolbar" showFooter="true"
           url="query" fitColumns="false" singleSelect="false" checkOnSelect="true"
           selectOnCheck="false"
           pageSize="10" pagination="true" pageList="[10,50,100,200,300]">
        <thead>
        <tr>
            <th field="cb" checkbox="true"></th>
            <th field="billType" hidden="true"></th>
            <th field="billNo" width="160">账单编号</th>
            <th field="customerName" width="105">客户名称</th>
            <th field="dateTypeName" width="80">日期类型</th>
            <th field="settleTime" width="130">结算日期</th>
            <th field="verifyTime" width="130">核销日期</th>
            <th field="billTypeTitle" width="110">账单类型</th>
            <th field="deliverOrgName" width="110">发货仓库</th>
            <th field="billCount" width="60">单数</th>
            <th field="billAmount" width="80">金额</th>
            <th field="remark" width="100">备注</th>
            <th field="mustRukuName" width="50">必须入库</th>
            <th field="statusTitle" width="50">状态</th>
            <th field="startTime" width="130">起始日期</th>
            <th field="endTime" width="130">截止日期</th>
            <th field="creator" width="85">创建人</th>
            <th field="confirmUser" width="85">确认人</th>
            <th field="verifyUser" width="85">核销人</th>
        </tr>
        </thead>
    </table>
    <div id="toolbar">
        <table width="100%" border="0">
            <tr>
                <td colspan="2">
                    <table width="100%" height="82" border="0">
                        <tr>
                            <td>预约单号：</td>
                            <td><input id="billNos" name="billNos"/></td>
                            <td>下单时间：</td>
                            <td>
                                <input type="text" name="beginTime" id="beginTime" value=""
                                       onFocus="WdatePicker({startDate: '%y-%M-%d 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
                            </td>
                            <td>到</td>
                            <td>
                                <input type="text" name="endTime" id="endTime" value=""
                                       onFocus="WdatePicker({startDate: '%y-%M-%d 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
                            </td>
                            <td>市区：</td>
                            <td width="10%">
                                <select id="city" name="city">
                                    <option value="" selected="selected">市</option>
                                    <c:forEach items="${cityList}" var="list">
                                        <option value="${list.id}" code="${list.code}">${list.name}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td width="10%">
                                <select id="county" name="county">
                                    <option value="">区/县</option>
                                    <%--<%for (BillStatueEnum bs : BillStatueEnum.values()) { %>--%>
                                    <%--<option value="<%=bs.getValue() %>"><%=bs.getText()%>--%>
                                    <%--</option>--%>
                                    <%--<%} %>--%>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>手机/固话：</td>
                            <td>
                                <input/>
                            </td>
                            <td>站点：</td>
                            <td>
                                <select id="branch" name="branch">
                                    <option value="">请选择</option>
                                    <c:forEach items="${branchList}" var="list">
                                        <option value="${list.branchid}"
                                                selected="selected">${list.branchname}</option>
                                    </c:forEach>
                                    <%--<option value="">---请选择---</option>--%>
                                    <%--<%for (BillTypeEnum bs : BillTypeEnum.values()) { %>--%>
                                    <%--<option value="<%=bs.getValue() %>"><%=bs.getText()%>--%>
                                    <%--</option>--%>
                                    <%--<%} %>--%>
                                </select>
                            </td>
                            <td>快递员：</td>
                            <td>
                                <select id="kdy" name="kdy">
                                    <option value="">请选择</option>
                                    <%--<option value="">---请选择---</option>--%>
                                    <%--<%for (BillTypeEnum bs : BillTypeEnum.values()) { %>--%>
                                    <%--<option value="<%=bs.getValue() %>"><%=bs.getText()%>--%>
                                    <%--</option>--%>
                                    <%--<%} %>--%>
                                </select>
                            </td>
                            <td>预约订单状态：</td>
                            <td colspan="2">
                                <select id="status" name="billType">
                                    <%--<option value="">---请选择---</option>--%>
                                    <%--<%for (BillTypeEnum bs : BillTypeEnum.values()) { %>--%>
                                    <%--<option value="<%=bs.getValue() %>"><%=bs.getText()%>--%>
                                    <%--</option>--%>
                                    <%--<%} %>--%>
                                </select>
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
                                <div class="btn btn-default" onclick="doQuery();" style="margin-right:5px;"><i
                                        class="icon-search"></i>查询
                                </div>
                                <div class="btn btn-default" id="openEditPreOderPanelBtn" style="margin-right:5px;"><i
                                        class="icon-plus"></i>修改
                                </div>
                                <div class="btn btn-default" onclick="openImportBtnPanel();" style="margin-right:5px;">
                                    <i class="icon-arrow-up"></i>关闭
                                </div>
                                <div class="btn btn-default" onclick="doBatchRemoveBill();" style="margin-right:5px;"><i
                                        class="icon-remove"></i>退回总部
                                </div>
                                <div class="btn btn-default" onclick="updateBillStatusToChecked();"
                                     style="margin-right:5px;"><i class="icon-eye-open"></i>分配站点
                                </div>
                                <div class="btn btn-default" onclick="doExportBill();" style="margin-right:5px;"><i
                                        class="icon-download-alt"></i>导出
                                </div>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
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
</body>
</html>