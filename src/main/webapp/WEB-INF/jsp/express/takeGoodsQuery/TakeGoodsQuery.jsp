<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> --%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.express.ExpressPreOrder"%>
<%@page import="cn.explink.domain.express.ExpressCwb4TakeGoodsQuery" %>
<%@page import="cn.explink.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	ExpressCwb4TakeGoodsQuery cwb4TakeGoodsQuery = (ExpressCwb4TakeGoodsQuery)request.getAttribute("cwb4TakeGoodsQuery");
Page page_obj = (Page)request.getAttribute("page_obj");
%>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/commonUtil.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/preOrderDeal.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/datePlugin/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<%-- <script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script> --%>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<style>
.select2 { 
border:1px solid #09C; 
background:url(../images/repeatx.png) repeat-x 0 -175px; 
width:130px; 
height:20px; 
line-height:20px; 
color:#000
}
</style>
 <script type="text/javascript">
<%--  $("#selectPg").val(<%=request.getAttribute("page") %>); --%>
 $(function(){
	 $("#selectAll").click(function() {
			var $this = $(this);
			if($this.is(":checked")) {
				$("input[name='cwb']").attr("checked", true);
			} else {
				$("input[name='cwb']").attr("checked", false);
			}
		});
		
		$("input[name='cwb']").click(function() {
			var $this = $(this);
			if($this.is(":checked")) {
				var unSelectLen = $("input[name='cwb']").not(":checked").length;
				if(unSelectLen == 0) {
					$("#selectAll").attr("checked", true);
				}
			} else {
				$("#selectAll").attr("checked", false);
			}
		});
		
		$("#print").click(function() {
			var $cwbChecked = $("input[name='cwb']:checked");
			if($cwbChecked.length == 0) {
				alert("请选择订单！");
			} else {
				window.location.href = "<%= request.getContextPath()%>/cwbLablePrint/printOrderLabel?" + $cwbChecked.serialize();
			}
		});
	$("#selectPg").val($("#selectPgValue").val());
// 	 $('#startTime').datepicker();
// 	 $('#endTime').datepicker();  
	 $("#status").val($("#statusValue").val());
	 if($("#statusValue").val()!=null && $("#statusValue").val()==3){
		 $("#timeDiv").html("出站时间");
		 $("#timeHeadDiv").html("出站时间");
	 }else if($("#statusValue").val()==2){
		 $("#timeDiv").html("入站时间");
		 $("#timeHeadDiv").html("入站时间");
	 }else if($("#statusValue").val()==1){
		 $("#timeDiv").html("揽收时间");
		 $("#timeHeadDiv").html("揽收时间");
	 }
	 $("#station").val($("#stationValue").val());
	 $("#collectorid").val($("#collectoridValue").val());
	 $("#payWay").val($("#payWayValue").val());
	 $("#startTime").val($("#startTimeValue").val());
	 $("#endTime").val($("#endTimeValue").val());
	 $("#totalFee").html($("#totalFeeValue").val());
	 $("#totalOrderCount").html($("#totalOrderCountValue").val());
	 $("#rightnowOrderFee").html($("#rightnowOrderFeeValue").val());
	 $("#rightnowOrderCount").html($("#rightnowOrderCountValue").val());
	 $("#arriveOrderFee").html($("#arriveOrderFeeValue").val());
	 $("#arriveOrderCount").html($("#arriveOrderCountValue").val());
	 $("#monthOrderFee").html($("#monthOrderFeeValue").val());
	 $("#monthOrderCount").html($("#monthOrderCountValue").val());
	 if(!$("#endTimeValue").val()){
		 $("#endTime").val(formatDate(new Date(),"yyyy-MM-dd hh:mm:ss"));
	 }
// 	 $("#endTime").val(new Date().toLocaleDateString());
// 	 alert(new Date().format("yyyy-MM-dd")));
// 	 alert(new Date().toLocaleDateString());
});
// alert(formatDate(new Date(),"yyyy-MM-dd hh:mm:ss"));
// alert("lasjkf;a");

//格式化日期,
function formatDate(date,format){
  var paddNum = function(num){
    num += "";
    return num.replace(/^(\d)$/,"0$1");
  }
  //指定格式字符
  var cfg = {
     yyyy : date.getFullYear() //年 : 4位
    ,yy : date.getFullYear().toString().substring(2)//年 : 2位
    ,M  : date.getMonth() + 1  //月 : 如果1位的时候不补0
    ,MM : paddNum(date.getMonth() + 1) //月 : 如果1位的时候补0
    ,d  : date.getDate()   //日 : 如果1位的时候不补0
    ,dd : paddNum(date.getDate())//日 : 如果1位的时候补0
    ,hh : date.getHours()  //时
    ,mm : date.getMinutes() //分
    ,ss : date.getSeconds() //秒
  }
  format || (format = "yyyy-MM-dd hh:mm:ss");
  return format.replace(/([a-z])(\1)*/ig,function(m){return cfg[m];});
} 



 
 
 
 function addInit(){
 	//无处理
 }
 /**
  * 设置颜色
  */
 function initColor(td_$){
 	if(td_$.checked==null||td_$.checked==undefined){
 		td_$.checked = false;
 	}
 	if(td_$.checked){
 		$(td_$).parent().parent().css("background-color","#FFD700");
 	}else{
 		$(td_$).parent().parent().css("background-color","#f5f5f5");
 	}
 }

 
 //查询提交方法
 function query(){
 	$("#searchForm_t").submit();
 }
 
 //导出方法
 function exportData(){
// 	 $("#searchForm_t").attr("action","");
	 $("#exportFlag").val("true");
	 $("#searchForm_t").attr("action",$("#selectPg").val());
	 $("#searchForm_t").submit();
	 $("#exportFlag").val("false");
// 	 $("#searchForm_t").attr("action","");
 }
	
	$(function(){
		//单选模糊查询下拉框
		 $("#collectorid").combobox();
// 		 <input type="text" class="combo-text validatebox-text validatebox-f textbox" autocomplete="off" style="width: 130px; height: 20px; line-height: 20px;">
		$("input.combo-text.validatebox-text.validatebox-f.textbox").css("width","130px");
	 })

 function statusOnchange(){
		 if($("#status").val()==3){
			 $("#timeDiv").html("出站时间");
			 $("#timeHeadDiv").html("出站时间");
		 }else if($("#status").val()==2){
			 $("#timeDiv").html("入站时间");
			 $("#timeHeadDiv").html("入站时间");
		 }else if($("#status").val()==1){
			 $("#timeDiv").html("揽收时间");
			 $("#timeHeadDiv").html("揽收时间");
		 }

	}
 </script>


</head>
<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box" >
		<form id="searchForm_t" action="<%=request.getContextPath()%>/takeGoodsQuery/toTakeGoodsQuery/1" method="post">
			<table width="100%">
			<tr>
				<td style="text-align: right;">状态</td>
				<td>
					<select id="status" name="status" class="select2" onchange="statusOnchange();">
<!-- 						<option value="">全部</option> -->
						<option value="1">未入站</option>
						<option value="2">已入站</option>
						<option value="3">已出站</option>
					</select>
				</td>
				<td style="text-align: right;">站点</td>
				<td>
					<input type="text" id="station" name="station" disabled="disabled" width="120px;" height="20px;" style="margin-top: -5px;">
				</td>
				<td style="text-align: right;width:300px;">小件员<!-- </td>
				<td> -->
					<select id="collectorid" name="collectorid" class="select2" >
							<option value="">全部</option>
						<c:forEach items="${deliveryManList}" var="list">
							<option value="${list.userid}">${list.realname}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td style="text-align: right;">付款方式</td>
				<td>
					<select id="payWay" name="payWay" class="select2">
						<option value="">全部</option>
						<option value="0">月结</option>
						<option value="1">现付</option>
						<option value="2">到付</option>
					</select>
				</td>
				<td style="text-align: right;"><div id="timeDiv">揽收时间</div></td>
				<td width="349px;" colspan="1">
					<input type="text" name="startTime"  id="startTime" readonly="readonly" style="margin-top: -5px;width:150px"
					    onclick="WdatePicker({'readOnly':true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" class="Wdate"/>
					至<input type="text" name="endTime" id="endTime" readonly="readonly" style="margin-top: -5px;width:150px"
					    onclick="WdatePicker({'readOnly':true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" class="Wdate"/>
				</td>
				<td style="margin-left: 50px;margin-top: -5px;width:90px">
					<input type="hidden" id="exportFlag" name="exportFlag" value="false"/>
					<input type="button" name="fuck" value="查询"  onclick="query();"/>
					<input type="button" name="fuck" value="导出" onclick="exportData();"/>
					<input type="button" name="print" id="print" value="打印">
				</td>
				<td></td>
			</tr>
			</table>
		</form>
	</div>
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	
 	<div style="border:1px solid #000;height:80px;text-align:center;vertical-align:middle;background: white;margin-top: 30;">
 		<table style="margin-top: 10px;width:100%">
 			<tr>
 				<td style="width:25%">
 					<div style="border:1px solid #000;height:80px;margin-left: -4px;margin-top: -4px;">
 						<div style="float:right;margin-right: 50px;margin-top: 10px;">汇总费用</div><div style="margin-left: 50px;margin-top: 10px;">汇总单量</div>
						<div id="totalFee" style="float:right;margin-right: 50px;font-size: 28;">5000</div><div id="totalOrderCount" style="margin-left: 50px;font-size: 28;">2000</div>

 					<div>
 				</td>
 				<td style="width:25%">
 					<div style="border:1px solid #000;height:80px;margin-left: -4px;margin-top: -4px;color: blue;">
 						<div style="float:right;margin-right: 50px;margin-top: 10px;">现付单量</div><div style="margin-left: 50px;margin-top: 10px;">现付汇总费用</div>
						<div id="rightnowOrderCount" style="float:right;margin-right: 50px;font-size: 28;">5000</div><div id="rightnowOrderFee" style="margin-left: 50px;font-size: 28;">2000</div>

 					<div>
 				</td>
 				 <td style="width:25%">
 					<div style="border:1px solid #000;height:80px;margin-left: -4px;margin-top: -4px;color: green;">
 						<div style="float:right;margin-right: 50px;margin-top: 10px;">到付单量</div><div style="margin-left: 50px;margin-top: 10px;">到付汇总费用</div>
						<div id="arriveOrderCount" style="float:right;margin-right: 50px;font-size: 28;">5000</div><div id="arriveOrderFee" style="margin-left: 50px;font-size: 28;">2000</div>

 					<div>
 				</td>
 				 <td style="width:25%">
 					<div style="border:1px solid #000;height:80px;margin-left: -4px;margin-top: -4px;color: red;">
 						<div style="float:right;margin-right: 50px;margin-top: 10px;">月结单量</div><div style="margin-left: 50px;margin-top: 10px;">月结汇总费用</div>
						<div id="monthOrderCount" style="float:right;margin-right: 50px;font-size: 28;">5000</div><div id="monthOrderFee" style="margin-left: 50px;font-size: 28;">2000</div>

 					<div>
 				</td>
 			</tr>
 		</table>
	</div>
 	
		<div class="right_title">
			<div style="overflow: auto;">
				<table width="1800px" border="0" cellspacing="1" cellpadding="0" class="table_2" id="listTable">
					<tr>
						<td align="center" valign="middle" style="font-weight: bold;">
							<input id="selectAll" type="checkbox"/>
						</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 运单单号</td>
						<td align="center" valign="middle" style="font-weight: bold;"> <div id="timeHeadDiv">揽收时间</div></td>
						<td align="center" valign="middle" style="font-weight: bold;"> 件数</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 小件员 </td>
						<td align="center" valign="middle" style="font-weight: bold;"> 付款方式 </td>
						<td align="center" valign="middle" style="font-weight: bold;"> 费用合计</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 运费</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 包装费用</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 保价费用</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 寄件人</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 寄件公司</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 寄件省份</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 寄件城市</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 寄件人手机 </td>
						<td align="center" valign="middle" style="font-weight: bold;"> 寄件人固话 </td>
						<td align="center" valign="middle" style="font-weight: bold;"> 收件人</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 收件公司</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 收件省份</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 收件城市</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 收件人手机</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 收件人固话</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 拖物内容名称</td>
					</tr>
 					<c:forEach items="${cwborderList}" var="list">
					<tr>
						<%-- <td height="30px" align="center"  valign="middle">
							<input type="checkbox" name="checkBox" value="${list.opscwbid}" onclick="initColor(this);"/>
						</td> --%>
						<td align="center" valign="middle">
							<c:if test="${nowTimestamp - list.emaildateTimestamp lt 60 * 60 * 24 * 7 }">
								<input name="cwb" type="checkbox" value="${list.cwb}"/>
							</c:if>
						</td>
						<td align="center" valign="middle" > ${list.cwb}</td>
						<%if(cwb4TakeGoodsQuery.getStatus()!=null && cwb4TakeGoodsQuery.getStatus()==3){%>
						<td align="center" valign="middle" > ${list.outstationdatetime}</td>
						<%}else{ %>
						<td align="center" valign="middle" > ${list.instationdatetime}</td>
						<%} %>
						<td align="center" valign="middle" > ${list.sendcarnum}</td>
						<td align="center" valign="middle" > ${list.collectorname}</td>
						<td align="center" valign="middle" > ${list.paymethod}</td>
						<td align="center" valign="middle" > ${list.totalfee}</td>
						<td align="center" valign="middle" > ${list.shouldfare}</td>
						<td align="center" valign="middle" > ${list.packagefee}</td>
						<td align="center" valign="middle" > ${list.insuredfee}</td>
						<td align="center" valign="middle" > ${list.sendername}</td>
						<td align="center" valign="middle" > ${list.sendercompany}</td>
						<td align="center" valign="middle" > ${list.senderprovince}</td>
						<td align="center" valign="middle" > ${list.sendercity}</td>
						<td align="center" valign="middle" > ${list.sendercellphone}</td>
						<td align="center" valign="middle" > ${list.sendertelephone}</td>
						<td align="center" valign="middle" > ${list.consigneename}</td>
						<td align="center" valign="middle" > ${list.reccompany}</td>
						<td align="center" valign="middle" > ${list.cwbprovince}</td>
						<td align="center" valign="middle" > ${list.cwbcity}</td>
						<td align="center" valign="middle" > ${list.consigneemobile}</td>
						<td align="center" valign="middle" > ${list.consigneephone}</td>
						<td align="center" valign="middle" > ${list.entrustname}</td>
					</tr>
					</c:forEach>
				</table>
			</div>
	</div>
</div>
		<input type="hidden" id="statusValue" value="${cwb4TakeGoodsQuery.status}" />
		<input type="hidden" id="stationValue" value="${cwb4TakeGoodsQuery.station}">
		<input type="hidden" id="collectoridValue" value="${cwb4TakeGoodsQuery.collectorid}">
		<input type="hidden" id="payWayValue" value="${cwb4TakeGoodsQuery.payWay}">
		<input type="hidden" id="startTimeValue" value="${cwb4TakeGoodsQuery.startTime}">
		<input type="hidden" id="endTimeValue" value="${cwb4TakeGoodsQuery.endTime}">
		<input type="hidden" id="totalFeeValue" value="${cwb4TakeGoodsQuery.shouldfareAll}">
		<input type="hidden" id="totalOrderCountValue" value="${cwb4TakeGoodsQuery.countAll}">
		<input type="hidden" id="rightnowOrderFeeValue" value="${cwb4TakeGoodsQuery.shouldfareNow}">
		<input type="hidden" id="rightnowOrderCountValue" value="${cwb4TakeGoodsQuery.countNow}">
		<input type="hidden" id="arriveOrderFeeValue" value="${cwb4TakeGoodsQuery.shouldfareArrive}">
		<input type="hidden" id="arriveOrderCountValue" value="${cwb4TakeGoodsQuery.countArrive}">
		<input type="hidden" id="monthOrderFeeValue" value="${cwb4TakeGoodsQuery.shouldfareMonth}">
		<input type="hidden" id="monthOrderCountValue" value="${cwb4TakeGoodsQuery.countMonth}">
		<input type="hidden" id="selectPgValue" value="${page}" >
<div class="iframe_bottom" >
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
		<tr>
			<td height="30" align="center" valign="middle" bgcolor="#eef6ff">
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
 </body>

</html>