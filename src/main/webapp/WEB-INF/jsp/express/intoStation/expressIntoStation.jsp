<%@page import="javax.management.modelmbean.RequiredModelMBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%@page import="cn.explink.domain.User"%>
<jsp:useBean id="now" class="java.util.Date" scope="page"/>
<%
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

<title>揽件入站</title>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/express/intoStation.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/easyui-extend/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/commonUtil.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/workorder/csPushSmsList.js" type="text/javascript"></script>

<script type="text/javascript">
$(function(){
	var paytype_temp = <%=paytype %>;
	var processState_temp = <%=processState %>;
	//初始化的时候默认不能选择
	$("#processState").val(processState_temp);
	$("#payType").val(paytype_temp);
});
</script>
</head>

<body style="background:#eef9ff">

<div class="right_box">
	<div class="inputselect_box">
		<form id="queryCondition" method="post" action="<%=request.getContextPath()%>/expressIntoStation/expressQueryList/1">
			
			<table style="width: 100%">
			    <tr>
			    	<td align="center"><font size="2"><span>&nbsp;</span></font></td>
			    	<td>
				    	<font size="2">状态:</font><select id="processState" name="processState" class="select1" onchange="controlDisplayBtn(this);">
				    		<option value="1">未处理</option>
				    		<option value="2">已处理</option>
				    	</select>
			    	</td>
			    	<td align="center"><font size="2"><span>&nbsp;</span></font></td>
			    	<td>
			    		<font size="2">小件员:</font><select id="deliveryId" name="deliveryId" class="select1" ><!-- onchange="controlBtnEnable(this);" -->
			    		<option value="-1"></option>
						<%for(User u : uList){ %>
							<option value="<%=u.getUserid() %>"  <%if(deliverid==u.getUserid()) {%>selected='selected'<%} %>    ><%=u.getRealname() %></option>
						<%} %>
			        </select>*
			    	</td>
			    	<td align="center"><font size="2"><span>&nbsp;</span></font></td>
			    	<td>
			    		<font size="2">付款方式:</font><select id="payType" name="payType" class="select1">
			    			<option value="1">现结</option>
				    		<option value="2">月结</option>
				    		<option value="3">到付</option>
			    		</select>
			    	</td>
				    <td>
					    <input class="input_button2" type="button" onclick="queryData();" value="查询"/>
					    <input class="input_button2" type="button" id="confirm_btn" onclick="confirmIntoStation('listTable');" value="揽件确认"/>
					    <input class="input_button2" type="button" onclick="printFunc();" value="打印"/>
			    	</td>
			    </tr>
		 </table>
		</form>
		
	</div>
	
	
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div>
		<div class="right_title">
			<div style="overflow: auto;">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="listTable">
					<tr>
						<td height="30px"  valign="middle"><input type="checkbox" name="checkAll" onclick="checkAll('listTable')"/> </td>
						<td align="center" valign="middle" style="font-weight: bold;"> 运单号</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 件数</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 小件员</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 揽件时间 </td>
						<td align="center" valign="middle" style="font-weight: bold;"> 付款方式 </td>
						<td align="center" valign="middle" style="font-weight: bold;"> 运费合计 </td>
						<td align="center" valign="middle" style="font-weight: bold;"> 运费</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 包装费用</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 保价费用</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 收件省份</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 收件城市</td>
					</tr>
					
					<c:forEach items="${expresseList}" var="list">
					<tr>
						<td height="30px" align="center"  valign="middle">
							<input type="checkbox" name="checkBox" value="${list.id}" onclick="initColor(this);"/>
						</td>
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
						<td align="center" valign="middle" > ${list.city}</td>
					</tr>
					</c:forEach>
				</table>
			</div>
		</div>
</div>
<div>
	<div id="dlgSubmitExpressBox" class="easyui-dialog" style="width: 880px; height: 250px; top:100px;left:100px; padding: 10px 20px;z-index:400px;" closed="true" buttons="#btnsOfExpress">
		<input type="hidden" id="exp_selectIds" value="0" />
		<input type="hidden" id="deliveryManId" value="0"/>
		<div style="margin-left: 10px;" id="submitContent">
			<div style="float: left;">快递员：<span id="deliveryman"></span></div>
			<div style="margin-left:20px;float: left;">交件时间：<span id="submitTime"><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) %></span></div>
			<br/>
			<br/>
			<div style="width: 830px;height: 100px;">
				<!-- black -->
				<div class="div_level01">
					<div class="div_20">
						<span class="span_black_no">汇总单量</span><br/>
						<span>&nbsp;</span><br/>
						<span id="totalOrderCount" class="span_black">0</span>
					</div>
					<div class="div_30">
						<span class="span_black_no">汇总运费</span><br/>
						<span>&nbsp;</span><br/>
						<span id="totalTransFee" class="span_black">0</span>
					</div>
				</div>
				
				<!-- blue -->
				<div class="div_level01" >
					<div class="div_20">
						<span class="span_blue_no">现付汇总单量</span><br/>
						<span>&nbsp;</span><br/>
						<span id="nowPayOrderCount" class="span_blue" >0</span>
					</div>
					<div class="div_30">
						<span class="span_blue_no">现付汇总运费</span><br/>
						<span>&nbsp;</span><br/>
						<span id="nowPayTransFee" class="span_blue">0</span>
					</div>
				</div>
				<!-- green -->
				<div class="div_level01">
					<div class="div_20">
						<span class="span_green_no">到付付单量</span><br/><br/>
						<span id="arrivePayOrderCount" class="span_green">0</span>
					</div>
					<div class="div_30">
						<span class="span_green_no">到付汇总运费</span><br/>
						<span>&nbsp;</span><br/>
						<span id="arrivePayTransFee" class="span_green">0</span>
					</div>
				</div>
				<!--red  -->
				<div class="div_level01">
					<div class="div_20">
						<span class="span_red_no">月结单量</span><br/>
						<span>&nbsp;</span><br/>
						<span id="monthPayOrderCount" class="span_red">0</span>
					</div>
					<div class="div_30">
						<span class="span_red_no">月结汇总运费</span><br/>
						<span>&nbsp;</span><br/>
						<span id="monthPayTransFee" class="span_red">0</span>
					</div>
					
				</div>
			</div>
		</div>
		<br/>
		<div align="center">
			<input type="button" class="input_button1" id="comfirmSubmitExpress" value="确认交件" onclick="submitExpressOperate();"/>
			<input type="button" class="input_button1" id="comfirmSubmitExpress" value="确认交件并打印" onclick="submitExpressOperate();"/>
		</div>
	</div>
</div>	

<div class="iframe_bottom"> 
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
		<tr>
			<td height="30" align="center" valign="middle" bgcolor="#eef6ff" style="font-size: 15px;">
			<a href="javascript:$('#queryCondition').attr('action','${pageContext.request.contextPath}/expressIntoStation/expressQueryList/1');$('#queryCondition').submit();" >第一页</a>　
			<a href="javascript:$('#queryCondition').attr('action','${pageContext.request.contextPath}/expressIntoStation/expressQueryList/${page_obj.previous<1 ? 1 : page_obj.previous }');$('#queryCondition').submit();">上一页</a>　
			<a href="javascript:$('#queryCondition').attr('action','${pageContext.request.contextPath}/expressIntoStation/expressQueryList/${page_obj.next<1 ? 1 : page_obj.next }');$('#queryCondition').submit();" >下一页</a>　
			<a href="javascript:$('#queryCondition').attr('action','${pageContext.request.contextPath}/expressIntoStation/expressQueryList/${page_obj.maxpage<1 ? 1 : page_obj.maxpage }');$('#queryCondition').submit();" >最后一页</a>
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
<script src="<%=request.getContextPath()%>/js/express/intoStation/intoStation.js" type="text/javascript"></script>
</body>

</html>