<%@page import="javax.management.modelmbean.RequiredModelMBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.express.ExpressFreightBill" %>
<jsp:useBean id="now" class="java.util.Date" scope="page"/>
<%
List<ExpressFreightBill> billList = (List<ExpressFreightBill>)request.getAttribute("billList");
List<User> uList = (List<User>)request.getAttribute("userList");
Long delivermanId=(Long)session.getAttribute("pageDeliver"); 
String paytypeStr = request.getParameter("payType")==null?"1":request.getParameter("payType");
String processStateStr = request.getParameter("processState")==null?"1":request.getParameter("processState");
int paytype = Integer.valueOf(paytypeStr);
int processState = Integer.valueOf(processStateStr);
long deliverid=delivermanId==null?0L:delivermanId;
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>跨省到付运费审核（应收）</title>
<style type="text/css">
      .page{ margin:12px 0 20px;  text-align:center}
      .page span{margin:5px; font-size:14px}


</style>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/easyui-extend/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/commonUtil.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/express/accrossReceFreight/accrossProvincePayFreightAudit.js" type="text/javascript"></script>
</head>

<body style="background:#eef9ff">

<div class="right_box">
	<div class="inputselect_box">
		<table style="width: 100%">
		    <tr>
			    <td>
				    <input class="input_button2" type="button" id="confirm_btn" onclick="edit('acrossProvinceAuditTable');" value="查看/修改"/>
				    <input class="input_button2" type="button" id="query" value="查询"/>
		    	</td>
		    </tr>
		</table>
	</div>
	
	
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div>
		<div class="right_title">
			<div style="overflow: auto;">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2"  id="acrossProvinceAuditTable">
					<tr>
						<td height="30px"  valign="middle"> </td>
						<td align="center" valign="middle" style="font-weight: bold;"> 账单编号</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 账单状态</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 应收省份</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 应付省份</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 运费金额合计 </td>
						<td align="center" valign="middle" style="font-weight: bold;"> 代收货款合计 </td>
						<td align="center" valign="middle" style="font-weight: bold;"> 创建人 </td>
						<td align="center" valign="middle" style="font-weight: bold;"> 创建日期</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 审核人</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 审核日期</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 备注</td>
					</tr>
					
					<c:forEach items="${billList}" var="list">
					<tr>
						<td height="30px" align="center"  valign="middle">
							<input type="checkbox" id="${list.id}" name="checkBoxx" value="${list.id}" onclick="initColor(this);"/>
						</td>
						<td align="center" valign="middle" > ${list.billNo}</td>
						<td align="center" valign="middle" > ${list.billState}</td>
						<td align="center" valign="middle" > ${list.customerName}</td>
						<td align="center" valign="middle" > ${list.customerName}</td>
						<td align="center" valign="middle" > ${list.freight}</td>
						<td align="center" valign="middle" > ${list.cod}</td>
						<td align="center" valign="middle" > ${list.creatorName}</td>
						<td align="center" valign="middle" > ${list.createTime}</td>
						<td align="center" valign="middle" > ${list.auditorName}</td>
						<td align="center" valign="middle" > ${list.auditTime}</td>
						<td align="center" valign="middle" > ${list.remark}</td>
					</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</div>

	<div class="iframe_bottom"> 
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
				<a href="javascript:$('#queryCondition').attr('action','${pageContext.request.contextPath}/acrossProvincePayFreightAudit/toAcrossProvincePayFreightAudit/1');$('#queryCondition').submit();" >第一页</a>　
				<a href="javascript:$('#queryCondition').attr('action','${pageContext.request.contextPath}/acrossProvincePayFreightAudit/toAcrossProvincePayFreightAudit/${page_obj.previous<1 ? 1 : page_obj.previous }');$('#queryCondition').submit();">上一页</a>　
				<a href="javascript:$('#queryCondition').attr('action','${pageContext.request.contextPath}/acrossProvincePayFreightAudit/toAcrossProvincePayFreightAudit/${page_obj.next<1 ? 1 : page_obj.next }');$('#queryCondition').submit();" >下一页</a>　
				<a href="javascript:$('#queryCondition').attr('action','${pageContext.request.contextPath}/acrossProvincePayFreightAudit/toAcrossProvincePayFreightAudit/${page_obj.maxpage<1 ? 1 : page_obj.maxpage }');$('#queryCondition').submit();" >最后一页</a>
				　共${page_obj.maxpage}页　共${page_obj.total}条记录 　当前第
				<select id="selectPg" onchange="$('#queryCondition').attr('action',$(this).val());$('#queryCondition').submit()">
					<c:forEach var="i" begin='1' end='${page_obj.maxpage}'>
						<option value='${i}' ${page==i ? 'selected=seleted' : ''}>${i}</option>
					</c:forEach>
				</select>页
				</td>
			</tr>
		</table>
	</div>
	<!-- 点击查询按钮的查询条件弹出框 -->
 	<div>
 		<div id="QueryExpressBox" class="easyui-dialog" style="width: 800px; height: 250px; top:200px;left:200px; padding: 10px 20px;z-index:1000px;" closed="true" buttons="#dlg2Match">
			<form id="queryCondition" method="post" action="<%=request.getContextPath()%>/acrossProvincePayFreightAudit/toAcrossProvincePayFreightAudit/1">
			<div style="margin-left: 20px;" id="dispatchItem">
				<table>
					<tr>
						<td>账单编号</td>
						<td><input id="billNo" name="billNo" style="width:210px"/></td>
						<td>账单状态</td>
						<td>
							<select id="billState" name="billState" style="width:210px">
								<option value="">全部</option>
								<option value="0">未审核</option>
								<option value="1">已审核</option>
								<option value="2">已核销</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>账单创建日期</td>
						<td><input id="startCreatTime" name="startCreatTime" class="easyui-datebox" style="width:100px"/>至<input id="endCreatTime" name="endCreatTime" class="easyui-datebox" style="width:100px"/></td>
						<td>账单核销日期</td>
						<td><input id="startAuditTime" name="startAuditTime" class="easyui-datebox" style="width:100px"/>至<input id="endAuditTime" name="endAuditTime" class="easyui-datebox" style="width:100px"/></td>
					</tr>
					<tr>
						<td>应收省份</td>
						<td>
							<select id="receivableProvinceId" name="receivableProvinceId" style="width:210px">
							</select>
						</td>
						<td>排序</td>
						<td>
							<select id="sortOption" name="sortOption" style="width:180px">
								<option value="bill_no">账单编号</option>
								<option value="bill_state">账单状态</option>
								<option value="create_time">账单创建日期</option>
								<option value="cav_time">账单核销日期</option>
							</select>
							<select id="sortRule" name="sortRule">
								<option value="asc">升序</option>
								<option value="desc">降序</option>
							</select>
						</td>
					</tr>
				
				</table>
			</div>
			</form>
		<div id="dlg2Match" style="text-align: center;">
			<input type="button" value="查询" id="confirmQuery" class="btn btn-default"/>
			<input type="button" value="关闭" id="cancleQuery" class="btn btn-default"/>
		</div>
	</div>
	<!-- 点击查看编辑按钮的弹出框 -->
 	<div>
 		<div id="EditExpressBox" class="easyui-dialog" style="width: 900px; height: 450px; top:100px;left:200px; padding: 10px 20px;z-index:1000px;" closed="true" buttons="#dlg2Match">
			<div style="margin-left: 20px;" id="dispatchItem">
				<div class="right_box">
					<div class="inputselect_box" style="width:97%">
						<form id="queryCondition" method="post" action="<%=request.getContextPath()%>/acrossProvincePayFreightAudit/toAcrossProvincePayFreightAudit/1">
							
							<table style="width: 100%">
							    <tr>
								    <td>
									    <input class="input_button2" type="button"  id="returnButton" value="返回"/>
									    <input class="input_button2" type="button"  id="auditButton" value="审核"/>
									    <input class="input_button2" type="button"  id="cancalAuditButton" value="取消审核"/>
									    <input class="input_button2" type="button"  id="checkAuditButton" value="核销"/>
<!-- 									    <input class="input_button2" type="button"  id="export" style="margin-left: 500px;" value="导出"/> -->
							    	</td>
							    </tr>
						 </table>
						 	<table style="height: 200px;">
								<tr>
									<td><div style="width:100px">账单编号</div></td>
									<td><input id="billNoEdit" disabled=true/></td>
									<td>账单状态</td>
									<td><input id="billStateEdit" disabled=true/></td>
									<td>应收省份</td>
									<td><input id="receivableProvinceNameEdit" disabled=true/></td>
								</tr>
								<tr>
									<td>应付省份</td>
									<td><input id="payableProvinceNameEdit" disabled=true/></td>
									<td>运费金额合计</td>
									<td><input id="freightEdit" disabled=true/></td>
									<td>代收货款合计</td>
									<td><input id="codEdit" disabled=true/></td>
								</tr>
								<tr>
									<td>创建日期</td>
									<td><input id="createTimeEdit" disabled=true/></td>
									<td>审核日期</td>
									<td><input id="auditTimeEdit" disabled=true/></td>
									<td>核销日期</td>
									<td><input id="cavTimeEdit" disabled=true/></td>
								</tr>
								<tr>
									<td>创建人</td>
									<td><input id="creatorNameEdit" disabled=true/></td>
									<td>审核人</td>
									<td><input id="auditorNameEdit" disabled=true/></td>
									<td>核销人</td>
									<td><input id="cavNameEdit" disabled=true/></td>
								</tr>
								<tr>
									<td>妥投审核截止日期</td>
									<td><input id="closingDateEdit" disabled=true/></td>
								</tr>
								<tr>
									<td>备注</td>
									<td colspan="5" ><textarea id="remarkEdit" disabled=true style="width:100%"></textarea></td>
								</tr>
							</table>
						 
						</form>
					</div>
					
					<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
					<div class="jg_10"></div>
					<div class="right_title" style="width: 97%;">
						<div style="overflow: auto;">
							<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2"  id="acrossProvinceAuditEdit" style="margin-top: 199px;">
								<tr id="firstTR">
									<td align="center" valign="middle" style="font-weight: bold;"> 运单号</td>
									<td align="center" valign="middle" style="font-weight: bold;"> 件数</td>
									<td align="center" valign="middle" style="font-weight: bold;"> 揽件员</td>
									<td align="center" valign="middle" style="font-weight: bold;"> 派件员 </td>
									<td align="center" valign="middle" style="font-weight: bold;"> 运费合计 </td>
									<td align="center" valign="middle" style="font-weight: bold;"> 运费</td>
									<td align="center" valign="middle" style="font-weight: bold;"> 包装运费</td>
									<td align="center" valign="middle" style="font-weight: bold;"> 保价运费</td>
									<td align="center" valign="middle" style="font-weight: bold;"> 代收货款</td>
									<td align="center" valign="middle" style="font-weight: bold;"> 揽件站点</td>
								</tr>
								
								<c:forEach items="${expresseList}" var="list">
								<tr>
									<td align="center" valign="middle" > ${list.transNo}</td>
									<td align="center" valign="middle" > ${list.orderCount}</td>
									<td align="center" valign="middle" > ${list.deliveryMan}</td>
									<td align="center" valign="middle" > ${list.pickTime}</td>
									<td align="center" valign="middle" > ${list.payType}</td>
									<td align="center" valign="middle" > ${list.transportFeeTotal}</td>
									<td align="center" valign="middle" > ${list.transportFee}</td>
									<td align="center" valign="middle" > ${list.packFee}</td>
									<td align="center" valign="middle" > ${list.saveFee}</td>
									<td align="center" valign="middle" > ${list.province}</td>
								</tr>
								</c:forEach>
							</table>
						</div>
					</div>
					<div class="page" id="page" style="text-align: center;"></div>
					</div>
			</div>
		<div id="dlg2Match" style="text-align: center;">
		</div>
	</div>
<!-- 存储查询条件的隐藏域 -->
<div>
	<input type="hidden" id="billNoValue" value="${expressFreightAuditBillVO.billNo}"/>
	<input type="hidden" id="billStateValue" value="${expressFreightAuditBillVO.billState}"/>
	<input type="hidden" id="startCreatTimeValue" value="${expressFreightAuditBillVO.startCreatTime}"/>
	<input type="hidden" id="endCreatTimeValue" value="${expressFreightAuditBillVO.endCreatTime}"/>
	<input type="hidden" id="startAuditTimeValue" value="${expressFreightAuditBillVO.startAuditTime}"/>
	<input type="hidden" id="endAuditTimeValue" value="${expressFreightAuditBillVO.endAuditTime}"/>
	<input type="hidden" id="receivableProvinceIdValue" value="${expressFreightAuditBillVO.receivableProvinceId}"/>
	<input type="hidden" id="sortOptionValue" value="${expressFreightAuditBillVO.sortOption}"/>
	<input type="hidden" id="sortRuleValue" value="${expressFreightAuditBillVO.sortRule}"/>
</div>
<%-- <form id="exportForm" method="post" action="<%=request.getContextPath()%>/acrossProvincePayFreightAudit/exportBill">
	<input type="hidden" name="id" id="id"/>
	<input type="hidden" name="pageNo" id="pageNo" />
</form> --%>
</body>
</html>