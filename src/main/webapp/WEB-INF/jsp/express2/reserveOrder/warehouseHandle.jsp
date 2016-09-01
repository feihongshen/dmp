<%@page import="com.pjbest.deliveryorder.enumeration.ReserveOrderStatusEnum"%>
<%@ page import="cn.explink.enumutil.ReserveOrderQueryTypeEnum" %>
<%@ page import="cn.explink.service.express2.ReserveOrderService" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>站点快递预约单处理</title>
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
                        <input id="reserveOrderNo" name="reserveOrderNo" type="text" style="width:140px;"/>
                    </td>
                    <td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">下单时间：</td>
                    <td colspan="3">
                        <input type="text" name="appointTimeStart" id="appointTimeStart" value="" readonly="readonly"
                               style="background-color:#fff;width:150px;cursor:pointer" class="Wdate"
                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:'#F{$dp.$D(\'appointTimeEnd\')}'})"/>
                        至
                        <input type="text" name="appointTimeEnd" id="appointTimeEnd" value="" readonly="readonly"
                               style="background-color:#fff;width:150px;cursor:pointer" class="Wdate"
                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'#F{$dp.$D(\'appointTimeStart\')}'})"/>
                    </td>
                    <%--<td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">市区：</td>
                    <td>
                    	<select name="cnorCity" id="cnorCity" style="width: 100px;">
                    		<option value="" selected="selected">市</option>
								<c:forEach items="${cityList}" var="list">
									<option value="${list.id}" code="${list.code}">${list.name}</option>
								</c:forEach>
						</select>
						<select name="cnorRegion" id="cnorRegion" style="width: 100px;">
							<option value="">区/县</option>
						</select>
					</td>--%>
                    <td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">手机/固话：</td>
                    <td>
                        <input id="cnorMobile" name="cnorMobile" type="text" style="width:140px;"/>
                    </td>
                    <td style="border: 0px; text-align: right; vertical-align: middle;width:65px;">快递员：</td>
                    <td>
                        <select name="courier" id="courier" style="width:140px;">
                            <option value="">请选择</option>
                            <c:forEach items="${courierList }" var="list">
                                <option value="${list.userid}">${list.realname}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
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
        <div style="margin:0px;">
            <div class="btn btn-default" onclick="doSearch();" style="margin-right:5px;" id="searchData"><i
                    class="icon-search"></i>查询
            </div>
            <div class="btn btn-default" id="editReserveOrderPanelBtn" style="margin-right:5px;"><i
                    class="icon-plus"></i>修改
            </div>
            <div class="btn btn-default" handleType="handleWarehouse" id="returnToCentralBtn" style="margin-right:5px;"><i class="icon-arrow-up"></i>退回
            </div>
            <div class="btn btn-default" id="distributeBranchBtn" style="margin-right:5px;"><i
                    class="icon-eye-open"></i>分配快递员
            </div>
            <div class="btn btn-default" id="feedbackBtn" style="margin-right:5px;"><i class="icon-comment"></i>反馈
            </div>
            <div class="btn btn-default" onclick="exportExcel();" style="margin-left:5px;"><i
                    class="icon-download-alt"></i>导出
            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/express2/reserveOrder/updateReserveOrder.jsp" %>
<div id="dialog2" title="反馈" style="display:none;">
    <div style="margin-top: 20px; margin-left:10px;margin-right:10px;">
        <table>
            <tr>
                <td style="border: 0px; text-align: left; vertical-align: middle;padding-left: 10px;width: 40%;">
                    反馈：
                </td>

            </tr>
            <tr>
                <td style="border: 0px; text-align: right; vertical-align: middle;padding-left: 10px;width: 40%;">
                    预约单号：
                </td>
                <td style="border: 0px; vertical-align: middle;">
                    <input id="reserveOrderNo4Feedback" readonly="true"/>
                </td>
            </tr>
            <tr>
                <td style="border: 0px; text-align: right; vertical-align: middle;padding-left: 10px;width: 40%;">
                    快递员：
                </td>
                <td style="border: 0px; vertical-align: middle;">
                    <input id="courierName4Feedback" readonly="true"/>
                </td>
            </tr>
            <tr>
                <td style="border: 0px; text-align: right; vertical-align: middle;padding-left: 10px;width: 40%;">
                    反馈为：
                </td>
                <td style="border: 0px; vertical-align: middle;">
                    <select id="optCode4Feedback" name="optCode4Feedback">
                        <option value="">请选择</option>
                        <c:forEach items="${feedbackOptCodes}" var="list">
                            <option value="${list.value}">${list.displayText}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr id="reason4FeedbackTR">
                <td style="border: 0px; text-align: right; vertical-align: middle;padding-left: 10px;width: 40%;">
                    原因：
                </td>
                <td style="border: 0px; vertical-align: middle;">
                    <select id="reason4Feedback" name="reason4Feedback">
                        <option value="">请选择</option>
<%--
                        <div id="reverseExceptionReason">
--%>
                            <c:forEach items="${reverseExceptionReason}" var="list">
                                <option name="reverseExceptionReasonOpt" value="${list.codeValue}">${list.displayValue}</option>
                            </c:forEach>
<%--    </div>
                        <div id="reverseRetentionReason">--%>
                            <c:forEach items="${reverseRetentionReason}" var="list">
                                <option name="reverseRetentionReasonOpt" value="${list.codeValue}">${list.displayValue}</option>
                            </c:forEach>
                      <%--  </div>--%>
                    </select>
                </td>
            </tr>
            <%--<tr>--%>
                <%--<td style="border: 0px; text-align: right; vertical-align: middle;padding-left: 10px;">预约上门时间：</td>--%>
                <%--<td style="border: 0px; vertical-align: middle; ">--%>
                    <%--<input style="width: 94%;" type="text" name="requireTimeStr4Feedback"--%>
                           <%--id="requireTimeStr4Feedback" &lt;%&ndash;style="height:30px;"&ndash;%&gt; value="" disabled="disabled"--%>
                           <%--onFocus="WdatePicker({startDate: '%y-%M-%d 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>--%>
                <%--</td>--%>
            <%--</tr>--%>
            <tr>
                <td style="border: 0px; text-align: right; vertical-align: middle;padding-left: 10px;">备注</td>
                <td style="border: 0px; vertical-align: middle; ">
                    <textarea id="cnorRemark4Feedback" rows=5 name="" class="textarea easyui-validatebox"></textarea>
                </td>
            </tr>
        </table>
        <hr style="margin-top:20px;margin-bottom:20px; border-top:1px solid #cccccc;"/>
        <div class="pull-right">
            <div class="btn btn-default" style="margin-right:5px;" id="confirmFeedbackBtn"><i
                    class="icon-ok"></i>确定
            </div>
            <div class="btn btn-default" id="closeFeedbackPanel"><i class="icon-remove"></i>取消</div>
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
<div id="dialog4" title="分配快递员" style="display:none;">
    <div style="margin-top: 20px; margin-left:10px;margin-right:10px;">
        <table>
            <tr>
                <td>快递员：</td>
                <td>
                    <select id="distributeCourierSelect" name="distributeCourierSelect">
                        <option value="">请选择</option>
                        <c:forEach items="${courierList }" var="list">
                            <option value="${list.userid}">${list.realname}</option>
                        </c:forEach>
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
<script type="text/javascript">
	var queryType = "<%=ReserveOrderQueryTypeEnum.WAREHOUSE_HANDLE.getValue()%>;"
    
    var isHandlePage = false;
    /**
     * 初始化表格
     */
    function initDataGrid() {
        $('#dg_rsList').datagrid({
            url: contextPath + '/express2/reserveOrder/queryList/' + queryType,
            toolbar: "#signFee_toolbar",
            showFooter: true,
            fit: true,
            fitColumns: false,
            singleSelect: false,
            pageSize: 10,
            rownumbers: true,
            pagination: true,
            pageList: [10, 30, 50],
            columns: [[
                {field: 'omReserveOrderId', title: '预约单id', checkbox: true},
                {
                    field: 'reserveOrderNo', title: '预约单号', width: 130,
                    formatter: reserveOrderNoFormatter
                },
                {field: 'appointTimeStr', title: '下单时间', width: 130},
                {field: 'cnorName', title: '寄件人', width: 100},
                {field:'custName', title:'寄件公司', width:100},
                {field: 'cnorMobile', title: '手机', width: 100},
                {field: 'cnorTel', title: '固话', width: 100},
                {field: 'cnorAddr', title: '寄件地址', width: 130, hidden: 'true'},
                {field: 'cnorDetailAddr', title: '寄件地址', width: 130},
                {field: 'requireTimeStr', title: '预约上门时间', width: 130},
                {field: 'cnorRemark', title: '寄件人备注', width: 80},
                {field: 'reserveOrderStatusName', title: '预约单状态', width: 100},
                {field: 'reason', title: '原因', width: 130},
//                {field: 'transportNo', title: '运单号', width: 100},
//                {field: 'acceptOrgName', title: '站点', width: 100},
                {field: 'courierName', title: '快递员', width: 80},
                {field:'recordVersion',hidden : true, width:80}
//                {field: 'remark', title: '备注', width: 80}
            ]]
        });
    }

    function doSearch() {
        //查询list
        $('#dg_rsList').datagrid('load', {
            reserveOrderNo: $("#reserveOrderNo").val(),
            appointTimeStart: $("#appointTimeStart").val(),
            appointTimeEnd: $("#appointTimeEnd").val(),
//            cnorProv:$("#cnorProv").val(),
//            cnorCity:$("#cnorCity").val(),
            cnorMobile: $("#cnorMobile").val(),
//            acceptOrg: $("#acceptOrg").val(),
            courier: $("#courier").val(),
            reserveOrderStatusList: $("#reserveOrderStatusList").val()
        });
    }

    function exportExcel() {
        var $searchForm = $("#searchForm");
        //保存现场
        var action = $searchForm.attr("action");
        var target = $searchForm.attr("target");
        //提交请求
        $searchForm.attr("action", contextPath + "/express2/reserveOrder/exportExcel/" + queryType);
        $searchForm.attr("target", "_blank");
        $searchForm.submit();
        //恢复现场
        $searchForm.attr("action", action);
        $searchForm.attr("target", target);
    }

    // 站点退回 - 退回类型为站点超区
    var returnType = "<%= ReserveOrderService.PJReserverOrderOperationCode.ZhanDianChaoQu.getValue()%>";
    
    var hadAllocationPro = "<%= ReserveOrderStatusEnum.HadAllocationPro.getIndex()%>";
    //已分配站点
    var hadAllocationStation = "<%= ReserveOrderStatusEnum.HadAllocationStation.getIndex()%>";
    var haveStationOutZone = "<%= ReserveOrderStatusEnum.HaveStationOutZone.getIndex()%>";
    //揽件超区
    var haveReciveOutZone = "<%= ReserveOrderStatusEnum.HaveReciveOutZone.getIndex()%>";


    $("#optCode4Feedback").change(function () {
        changeReason4Feedback($(this).val());
    })

    var LAN_JIAN_SHI_BAI = "<%= ReserveOrderService.PJReserverOrderOperationCode.LanJianShiBai.getValue()%>";
    var FAN_KUI_JI_LIU = "<%= ReserveOrderService.PJReserverOrderOperationCode.FanKuiJiLiu.getValue()%>";

    function changeReason4Feedback(operateType) {
        $('#reason4Feedback').val("");
        if (operateType == LAN_JIAN_SHI_BAI) {
            $('#reason4FeedbackTR').show();
            $.each($("#reason4Feedback option"), function (index, value) {
                if ($(this).attr('name')) {
                    if ($(this).attr('name') == "reverseExceptionReasonOpt") {
                        $(this).show();
                    } else if ($(this).attr('name') == "reverseRetentionReasonOpt") {
                        $(this).hide();
                    }
                }
            });
        } else if (operateType == FAN_KUI_JI_LIU) {
            $('#reason4FeedbackTR').show();
            $.each($("#reason4Feedback option"), function (index, value) {
                if ($(this).attr('name')) {
                    if ($(this).attr('name') == "reverseExceptionReasonOpt") {
                        $(this).hide();
                    } else if ($(this).attr('name') == "reverseRetentionReasonOpt") {
                        $(this).show();
                    }
                }
            });
        } else {
//            $.each($("#reason4Feedback option"), function (index, value) {
//                $(this).hide();
//            });
            $('#reason4FeedbackTR').hide();
        }
    }

    $(function () {
        //初始化表格
        initDataGrid();

    });
</script>
</html>