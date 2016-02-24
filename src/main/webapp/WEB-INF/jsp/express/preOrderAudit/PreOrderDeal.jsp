<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="cn.explink.domain.express.ExpressPreOrderVOForDeal"%>
<%@page import="cn.explink.domain.VO.PreOrderVO"%>
<%@page import="cn.explink.util.*"%>

<%
  List<ExpressPreOrderVOForDeal> preOrderList = (List<ExpressPreOrderVOForDeal>)request.getAttribute("preOrderList");
  Page page_obj = (Page)request.getAttribute("page_obj");
//   Integer status = request.getAttribute("status");
//   List stationlist = request.getAttribute("stationlist");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/express/intoStation.css" type="text/css"  />
<script type="text/javascript">
	var App = {ctx:"${pageContext.request.contextPath}"};
<%-- 	alert(<%=request.getContextPath()%>); --%>
</script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/express/preOrder/preOrderDeal.js" type="text/javascript"></script>
</head>
<body style="background:#eef9ff">
	<div style="background:#eef9ff;margin-left:-7px;margin-top:-7px">
	<div style="height: 30px;padding-top: 10px;">
		<form id="searchForm_t" action="<%=request.getContextPath()%>/preOrderOperationController/toPreOrderDeal/1" method="post">
			<table width="100%">
			<tr>
				<td></td>
				<td style="width: 400px;">
					预订单编号<input id="preOrderNo" name="preOrderNo"/>
				</td>
				<td style="width: 400px;">
					匹配状态<select id="status" name="status" onchange="autoChange();">
						<option value="0">已分配省公司</option>
						<option value="5">站点超区</option>
						<%-- <c:forEach items="${excuteTypelist}" var="list">
							<option value="${list.key}">${list.value}</option>	
						</c:forEach> --%>
						
					</select>
				</td>
				<td>
					<input id="query" type="button" name="fuck" value="查询"  onclick="query();" style="width:80px"/>
				</td>
			</tr>
			</table>
		</form>
	</div>
	
	
	<div style="border:1px solid #000;height:30px;text-align:center;vertical-align:middle;">
		<input type="button" id='autoMatchButton' value="自动匹配站点"  style="width: 100px;float:right;margin-top: 5px;margin-right: 20px;" onclick="autoMatch('preOrderTable');" />
		<input type="button" id="handMatchButtom" value="手工分配站点" style="width: 100px;float:right;margin-top: 5px;margin-right: 20px;" onclick="getSelet('preOrderTable','handMatch');"/>
		<input type="button" id='returnButton' value="退回总部" style="width: 100px;float:right;margin-top: 5px;margin-right: 20px;" onclick="getSelet('preOrderTable','returnHeadQuarters');"/>
		<input type="button" id='closeButton' value="关闭预订单" style="width: 100px;float:right;margin-top: 5px;margin-right: 20px;" onclick="getSelet('preOrderTable','closeOrder');"/>
	</div>
	
	
	<div class="right_title">
		<div style="overflow: auto;">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2"  id="preOrderTable">
			<tr>
				<th><input type="checkbox" name="checkAll" onclick="checkAll('preOrderTable');"></th>
				<th>预订单号</th>
				<th>生成时间</th>
				<th>寄件人</th>
				<th>手机号</th>
				<th>固话</th>
				<th>反馈人</th>
				<th>反馈站点</th>
				<th style="width:200px">取件地址</th>
				<th>一级原因</th>
				<th>二级原因</th>
			</tr>
			<%for(ExpressPreOrderVOForDeal expressPreOrder:preOrderList){ %>
			<tr>
				<td align="center" valign="middle"><input type="checkbox" name="checkBox" value="<%=expressPreOrder.getId()%>" onclick="initColor(this);"/></td>
				<td id="<%=expressPreOrder.getId()%>preOrderNo" align="center" valign="middle"><%=expressPreOrder.getPreOrderNo()%></td>
				<td align="center" valign="middle"><%=expressPreOrder.getCreateTime()%></td>
				<td align="center" valign="middle"><%=expressPreOrder.getSendPerson()%></td>
				<td align="center" valign="middle"><%=expressPreOrder.getCellphone()%></td>
				<td align="center" valign="middle"><%=expressPreOrder.getTelephone()%></td>
				<td align="center" valign="middle"><%=expressPreOrder.getFeedbackUserName()%></td>
				<td align="center" valign="middle"><%=expressPreOrder.getOperationBranchName()%></td>
				<td id="<%=expressPreOrder.getId()%>collectAddress" align="center" valign="middle" style="width:200px"><%=expressPreOrder.getCollectAddress()%></td>
				<td id="<%=expressPreOrder.getId()%>reason" align="center" valign="middle"><%=expressPreOrder.getFeedbackFirstReason()%></td>
				<td id="<%=expressPreOrder.getId()%>reason" align="center" valign="middle"><%=expressPreOrder.getFeedbackSecondReason()%></td>
			</tr>
			<%} %>
		</table>
	</div>
</div>

	<div class="iframe_bottom" >
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
		<tr>
			<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
				<a href="javascript:$('#searchForm_t').attr('action','1');$('#searchForm_t').submit();" >第一页</a>　
				<a href="javascript:$('#searchForm_t').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm_t').submit();">上一页</a>　
				<a href="javascript:$('#searchForm_t').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm_t').submit();" >下一页</a>　
				<a href="javascript:$('#searchForm_t').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm_t').submit();" >最后一页</a>
				　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
						id="selectPg"
						onchange="$('#searchForm_t').attr('action',$(this).val());$('#searchForm_t').submit()">
						<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
						<option value="<%=i %>"><%=i %></option>
						<% } %>
					</select>页
			</td>
		</tr>
		</table>
	</div>
	
 	<div>
 		<div id="dlgSubmitExpressBox" class="easyui-dialog" style="width: 450px; height: 250px; top:200px;left:380px; padding: 10px 20px;z-index:1000px;" closed="true" buttons="#dlg2Match">
			<div style="margin-left: 20px;" id="dispatchItem">
				<div style="float: left;"><lable>分配至站点:</lable></div>
				<div>
					<select id="siteInfo" >
						<c:forEach items="${stationlist}" var="list">
							<option value="${list.key}">${list.value}</option>	
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
		<div id="dlg2Match" style="text-align: center;">
			<input type="button" value="确认" id="confirmMatch" class="btn btn-default" onclick="confirmMatch('preOrderTable');"/>
			<input type="button" value="取消" id="cancleMatch" class="btn btn-default" />
		</div>
	</div>
	 <div>
 		<div id="closeOrderDialog" class="easyui-dialog" style="width: 450px; height: 250px; top:200px;left:380px; padding: 10px 20px;z-index:1000px;" closed="true" buttons="#dlg2Match">
			<div style="margin-left: 20px;" id="dispatchItem">
				<div style="float: left;"><lable>备注:</lable></div>
				<div>
					<textarea id="closrOrderText" cols=40 rows=4></textarea>
				</div>
			</div>
		</div>
		<div id="dlg2Match" style="text-align: center;">
			<input type="button" value="确认" id="confirmClose" class="btn btn-default" onclick="confirmClose('preOrderTable');"/>
			<input type="button" value="取消" id="cancleClose" class="btn btn-default" />
		</div>
	</div>
	
	 <div>
 		<div id="returnOrderDialog" class="easyui-dialog" style="width: 450px; height: 250px; top:200px;left:380px; padding: 10px 20px;z-index:1000px;" closed="true" buttons="#dlg2Match">
			<div style="margin-left: 20px;" id="dispatchItem">
				<div style="float: left;"><lable>备注:</lable></div>
				<div>
					<textarea id="returnOrderText" cols=40 rows=4></textarea>
				</div>
			</div>
		</div>
		<div id="dlg2Match" style="text-align: center;">
			<input type="button" value="确认" id="confirmReturn" class="btn btn-default" onclick="confirmReturn('preOrderTable');"/>
			<input type="button" value="取消" id="cancleReturn" class="btn btn-default"/>
		</div>
	</div>
	<input type="hidden" value="${page}" id="selectPage" />
	<input type="hidden" value="${status}" id="statusValue" />
	</div>
 </body>
 
</html>