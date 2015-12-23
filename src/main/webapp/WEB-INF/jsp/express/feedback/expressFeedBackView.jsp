<%@page import="cn.explink.domain.VO.express.ExpressFeedBackDTO"%>
<%@page import="cn.explink.domain.VO.express.ExpressFeedBackView"%>
<%@page import=" cn.explink.domain.Reason"%>
<%@page import=" cn.explink.enumutil.express.ExpressFeedBackTongjiEnum"%>
<%@page import=" cn.explink.domain.*"%>
<%@page import=" net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.express.ExcuteStateEnum"%>
<%@page import="cn.explink.domain.VO.express.ExpressFeedBackView"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>

<%
List<JSONObject> deliverList = request.getAttribute("deliverList")==null?new ArrayList<JSONObject>():(List<JSONObject>) request.getAttribute("deliverList");
Map<Long,Long>  summaryMap = request.getAttribute("summaryMap")==null?null:(Map<Long,Long>)request.getAttribute("summaryMap");
Map<Long,Map<Long,Long>>  deliveryInCountMap = request.getAttribute("deliveryInCountMap")==null?null:(Map<Long,Map<Long,Long>>)request.getAttribute("deliveryInCountMap");

ExpressFeedBackDTO expDTO = request.getAttribute("feedBackDTO")==null?null:(ExpressFeedBackDTO)request.getAttribute("feedBackDTO");

Map usermap = (Map) session.getAttribute("usermap");
//反馈状态
Long executeStateType = (Long)session.getAttribute("executeState");
//用户角色id
long userroleid = request.getAttribute("userroleid")==null?0:(Long)request.getAttribute("userroleid");
String isGuiBanUseZanBuChuLi = request.getAttribute("isGuiBanUseZanBuChuLi")==null?"no":(String)request.getAttribute("isGuiBanUseZanBuChuLi");

String isReAllocateExpress = request.getAttribute("isReAllocateExpress")==null?"no":(String)request.getAttribute("isReAllocateExpress");

ExpressFeedBackView feedBackView = (ExpressFeedBackView)request.getAttribute("feedBackView")==null?new ExpressFeedBackView(): (ExpressFeedBackView)request.getAttribute("feedBackView");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>揽件反馈</title>
<link href="<%=request.getContextPath()%>/css/reset.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/css/index.css" rel="stylesheet" type="text/css" />
<%-- <script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script> --%>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/express/feedback/comm.js"></script>
<script src="<%=request.getContextPath()%>/js/datePlugin/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script>

$(function(){
	$("#deliveryid").combobox();
	//$("#deliveryid").bind("change",getDeliveryStateByDeliveryId);   此处在控件上直接绑定了onchange事件
	var type=$("#executeStateType").val();//配送类型
	switch(type){
		case '3'://延迟揽件
			$("#pick_delay_list").show();
			break;
		case '4'://失败
			$("#pick_failed_list").show();
			break;
		case '5'://站点超区
			$("#area_wrong_list").show();
			break;
		case '6'://揽件失败
			$("#pick_wrong_list").show();
			break;
		case '7'://揽件成功
			$("#pick_successed_list").show();
			break;
		default :$("[id$='_list']").hide();
	}
	
	//选择反馈的结果显示内容表格
	$("input[id$='_box']").click(function(){
		if($(this).attr("checked")){
			$("#"+$(this).val()+"_list").show();
		}
 		else{
			$("#"+$(this).val()+"_list").hide();
		} 
	});
	
	//回车按下的时候
	$("input[id$='_C']").keydown(function(event){
		if(event.keyCode==13) {
			//清空历史背景色
			$("td[style$='font-weight:bold']").attr("style","");
			
			if($("tr[id='"+$(this).val()+"']").length==0){
				alert("订单号“"+$(this).val()+"”已归班审核过或者不是这个小件员的货！");
			}else if($(this).attr("id")=="fankui_C"){
				if($("tr[id$='_list'] tr[id='"+$(this).val()+"']").length==0){
					alert("订单号“"+$(this).val()+"”不在已反馈订单列表里！");
				}else{
					addClassToIsC($(this).val());
				}
			}else if($(this).attr("id")=="weifankui_C"){
				if($("tr[id='un_feedback'] tr[id='"+$(this).val()+"']").length==0){
					alert("订单号“"+$(this).val()+"”不在未反馈订单列表里！");
				}else{
					$("tr[id='"+$(this).val()+"'] td").attr("style","color:#FFF; background:#09C; font-weight:bold");
					edit_button($(this).val());
				}
			}
			$(this).val("");
		}
	});
});
 
var preOrder_C=""; //记录扫描过的单号
function addClassToIsC(preOrderNo){
	$("#"+$("tr[id='"+preOrderNo+"']").attr("keyName")+"_box").attr("checked",true);
	$("#"+$("tr[id='"+preOrderNo+"']").attr("keyName")+"_list").show();
	if($("tr[id$='_list'] tr[id='"+preOrderNo+"']").attr("gcaid")==-1){
		$("tr[id$='_list'] tr[id='"+preOrderNo+"'] td").attr("style","color:#FFF; background:#F33; font-weight:bold");
	}else{
		$("tr[id$='_list'] tr[id='"+preOrderNo+"'] td").attr("style","color:#FFF; background:#09C; font-weight:bold");
	}
	if(preOrder_C.indexOf(preOrderNo)==-1){
		preOrder_C = preOrder_C+","+preOrderNo;
	}
}

/**
 * 编辑时候的初始化
 */
function editInit(){
	for(var i =0 ; i < initFeedBoxEditArray.length ; i ++){
		var value = initFeedBoxEditArray[i].split(",")[0];
		var name = initFeedBoxEditArray[i].split(",")[1];
		if(name.indexOf("SecondLevel")>0&&parseInt(value)>0){
			var firstName = name.replace('Second','First');
			var value_temp = $("#"+firstName,parent.document).val();
			var URL = "<%=request.getContextPath()%>/reason/getSecondreason";
			$.ajax({
				url : URL, // 后台处理程序
				type : "POST",// 数据发送方式
				async: false, //设为false就是同步请求
				data : {
					firstreasonid : value_temp
				},
				dataType : 'json',// 接受数据格式
				success : function(json) {
					$("#"+name,parent.document).empty();// 清空下拉框//$("#select").html('');
					var html = "<option value ='0'>==请选择==</option>";
					for (var j = 0; j < json.length; j++) {
						var valueId = (json[j].reasonid);//.replace(/(^\s*)|(\s*$)/g, "")
						html += "<option value='"+valueId+"'>" + json[j].reasoncontent + "</option>";
					}
					$("#"+name,parent.document).append(html);					
				}
			});
		}
		$("#"+name, parent.document).val(value);
	}

	//自定义
	initDialogBox();
	window.parent.click_pickResult(<%=feedBackView.getExcuteState()%>,
			<%=ExcuteStateEnum.DelayedEmbrace.getValue()%>,
			<%=ExcuteStateEnum.Succeed.getValue()%>,
   			<%=ExcuteStateEnum.fail.getValue()%>,
   			<%=ExcuteStateEnum.StationSuperzone.getValue()%>,
   			<%=ExcuteStateEnum.EmbraceSuperzone.getValue() %>,false);
	$("input[type='text']", parent.document).focus(function(){
		//选中文本框的文本
		$(this).select();
	});
}
//

function initDialogBox(){
	//反馈结果的select
	$("#pickResultId", parent.document).change(function(){
		//监控配送状态变化 对显示字段做相应处理
		var pickResultId =  $("#pickResultId", parent.document).val();
		init_feedBackState();
		if(pickResultId==<%=ExcuteStateEnum.DelayedEmbrace.getValue()%>){
			initFeedBackDelay();
		}else if(pickResultId==<%=ExcuteStateEnum.Succeed.getValue()%>){
			initFeedBackSuccess();
		}else if(pickResultId==<%=ExcuteStateEnum.fail.getValue()%>){
			initFeedBackFail();
		}else if(pickResultId==<%=ExcuteStateEnum.StationSuperzone.getValue()%>){
			initFeedBackAreaWrong();
		}else if(pickResultId==<%=ExcuteStateEnum.EmbraceSuperzone.getValue() %>){
			initFeedBackPickWrong();
		}
	});
}



/**
 *	获取反馈状态【总览和分别选择小件员】
*/
function getDeliveryStateByDeliveryId(){
	debugger;
	if($("#deliveryid").val()=="0"){
		location.href="expressFeedbackView";
	}
	location.href="expressFeedbackIndex?deliveryId="+$("#deliveryid").val();
}


/**
 * 暂不处理
 */
function noSub(cwb,amount,cash,pos,zhiliu,tuihuo){
	$.ajax({
		type: "POST",
		url:"noSub/"+cwb,
		success : function(data) {
			$("#sub_"+cwb).html("恢复");
			$("#"+cwb).attr("gcaid","-1");
			$("#sub_"+cwb).attr("href","javascript:reSub('"+cwb+"',"+amount+","+cash+","+pos+","+zhiliu+","+tuihuo+")");
			$("tr[id='"+cwb+"'] td").attr("style","color:#FFF; background:#CCC");
			$("#subNumber").html(parseFloat($("#subNumber").html())-1);
			$("#totalAmount").html(((parseFloat($("#totalAmount").html()).toFixed(2)*100).toFixed(2)-(amount*100))/100);
			$("#cashAmount").html(((parseFloat($("#cashAmount").html()).toFixed(2)*100).toFixed(2)-(cash*100))/100);
			$("#posAmount").html(((parseFloat($("#posAmount").html()).toFixed(2)*100).toFixed(2)-(pos*100))/100);
			$("#zhiliuNumber").html(((parseFloat($("#zhiliuNumber").html()).toFixed(2)*100).toFixed(2)-(zhiliu*100))/100);
			$("#tuihuoNumber").html(((parseFloat($("#tuihuoNumber").html()).toFixed(2)*100).toFixed(2)-(tuihuo*100))/100);
		}
	});
}
//重新提交
function reSub(cwb,amount,cash,pos,zhiliu,tuihuo){
	$.ajax({type: "POST",
		url:"reSub/"+cwb,
		success : function(data) {
			$("#sub_"+cwb).html("暂不处理");
			$("#"+cwb).attr("gcaid","0");
			$("#sub_"+cwb).attr("href","javascript:noSub('"+cwb+"',"+amount+","+cash+","+pos+","+zhiliu+","+tuihuo+")");
			$("tr[id='"+cwb+"'] td").attr("style","");
			$("#subNumber").html(parseFloat($("#subNumber").html())+1);
			$("#totalAmount").html(((parseFloat($("#totalAmount").html()).toFixed(2)*100)+(amount*100))/100);
			$("#cashAmount").html(((parseFloat($("#cashAmount").html()).toFixed(2)*100)+(cash*100))/100);
			$("#posAmount").html(((parseFloat($("#posAmount").html()).toFixed(2)*100)+(pos*100))/100);
			$("#zhiliuNumber").html(((parseFloat($("#zhiliuNumber").html()).toFixed(2)*100)+(zhiliu*100))/100);
			$("#tuihuoNumber").html(((parseFloat($("#tuihuoNumber").html()).toFixed(2)*100)+(tuihuo*100))/100);
			
		}
	});
	
}


$("#subButton").click(function(){
	var trArray = $("tr[gcaid='0']");
	var subTrStr = "";
	for(var i=0;i<trArray.length;i++){
		subTrStr = subTrStr+"'"+$("tr[gcaid='0']").eq(i).attr("id")+"',";
	}
	subTrStr = subTrStr.substring(0, subTrStr.length-1);
	
	trArray = $("tr[keyName='un_feedback']");
	var noSubTrStr = "";
	for(var i=0;i<trArray.length;i++){
		noSubTrStr = noSubTrStr+"'"+$("tr[keyName='un_feedback']").eq(i).attr("id")+"',";
	}
	noSubTrStr = noSubTrStr.substring(0, noSubTrStr.length-1);
	
	trArray = $("tr[gcaid='-1']");
	var zanbuchuliTrStr = "";
	for(var i=0;i<trArray.length;i++){
		zanbuchuliTrStr = zanbuchuliTrStr+"'"+$("tr[gcaid='-1']").eq(i).attr("id")+"',";
	}
	zanbuchuliTrStr = zanbuchuliTrStr.substring(0, zanbuchuliTrStr.length-1);
	if($("#yfkcwb").val()=="0"){
		alert("没有要归班的订单");
	}else{
		var params ={
			zanbuchuliTrStr: zanbuchuliTrStr ,
			subTrStr :subTrStr,
			nocwbs:noSubTrStr,
			deliveryId:$("#deliveryid").val()
		}
		$.ajax({type: "POST",
			url:"sub",
			data: params,
			success : function(data) {
				$("#alert_box",parent.document).html(data);
				viewBox();
			}
		});	
	}
	//加载页面后 自动处理“暂不处理”状态的订单背景颜色
	$("tr[gcaid='-1'] td").attr("style","color:#FFF; background:#CCC");
});

//导出已反馈
function exportAlreadyFeedBack_fun(){
	<%
	 if(expDTO!=null&&expDTO.getAlreadyFeedBackCount()>0){
	%>
	location.href="<%=request.getContextPath() %>/expressFeedback/expressExportOpe?deliveryId="+$("#deliveryid").val()+"&exportFlag=alreadyFeedBack";
	<%}else{%>
	alert("没有数据不能导出");
	<%}%>
}
//导出未反馈
function exportNotFeedBack_fun(){
	<%
	 if(expDTO!=null&&expDTO.getUnfeedBackNumber()>0){
	%>
	location.href="<%=request.getContextPath() %>/expressFeedback/expressExportOpe?deliveryId="+$("#deliveryid").val()+"&exportFlag=unFeedBack";
	<%}else{%>
	alert("没有数据不能导出");
	<%}%>
}

</script>
</head>

<body style="background: #f5f5f5">
	<div class="menucontant">
		<div class="saomiao_tab2">
			<ul>
				<li><a href="#"  class="light">揽件反馈</a></li>
			</ul>
		</div>
		<form name="form1" method="post" action="commonupload" >
			<!-- 提示信息 -->
			<div class="form_topbg">
				本站投递中订单： 
				<select id="deliveryid" name="deliveryid" class="select1" onchange="getDeliveryStateByDeliveryId();"><!-- onchange="getDeliveryStateByDeliveryId();" -->
					<option value="0">总览</option>
					<%
						String nowDeliverName = "";
						long allDeliverCount = 0;
						if(deliverList!=null && deliverList.size()>0){
							for(JSONObject deliver : deliverList){ 
								allDeliverCount+=deliver.getLong("num");
					%>
					<option value="<%=deliver.getString("deliveryid") %>" <%if(deliver.getString("deliveryid").equals(request.getParameter("deliveryId"))){out.print("selected");nowDeliverName=deliver.getString("delivername");} %> ><%=deliver.getString("delivername") %>{<%=deliver.getString("num") %>}</option>
					<%
							}
						} %>
				</select>
			</div>


			<%if(deliveryInCountMap != null){ %>
				<div class="right_title">
					<div style="position:relative; z-index:0; width:100% ">
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" style="height:30px">
						<tbody>
							<!-- 表头 -->
							<tr>
								<td width="250px" align="center" valign="middle" bgcolor="#EEF6FF">小件员</td>
								<td width="250px" align="center" valign="middle" bgcolor="#EEF6FF">已揽收</td>
								<td width="250px" align="center" valign="middle" bgcolor="#EEF6FF">已反馈</td>
								<td width="250px" align="center" valign="middle" bgcolor="#EEF6FF">今日未反馈</td>
								<td width="250px" align="center" valign="middle" bgcolor="#EEF6FF">昨日之前未反馈</td>
							</tr>
							<%if(deliverList != null && deliverList.size()>0){ %>
								<%for(JSONObject deliver : deliverList){ %>
									<!-- 根据小件员获取数据 -->
									<% Map<Long,Long> map= deliveryInCountMap.get(deliver.getLong("deliveryid")); %>
									<!-- 填充数据 -->
									<tr>
										<td width="120" align="center" valign="middle" bgcolor="#f8f8f8" ><%=deliver.getString("delivername") %></td>
										<td width="120" align="center" valign="middle"><strong><%=map.get(ExpressFeedBackTongjiEnum.Picked.getValue()) %></strong></td>
										<td width="120" align="center" valign="middle"><strong><%=map.get(ExpressFeedBackTongjiEnum.FeedBacked.getValue()) %></strong></td>
										<td width="120" align="center" valign="middle"><strong><%=map.get(ExpressFeedBackTongjiEnum.TodayNotFeedBack.getValue()) %></strong></td>
										<td width="120" align="center" valign="middle"><strong><%=map.get(ExpressFeedBackTongjiEnum.YestodayNotFeedBack.getValue()) %></strong></td>
									</tr>
								<%} %>
							<%} %>
							<!-- 汇总 -->
							<%if(summaryMap != null && !summaryMap.isEmpty()){ %>
								<tr>
									<td width="120" align="center" valign="middle" bgcolor="#f8f8f8" >合计</td>
									<td width="120" align="center" valign="middle"><strong><%=summaryMap.get(ExpressFeedBackTongjiEnum.Picked.getValue()) %></strong></td>
									<td width="120" align="center" valign="middle"><strong><%=summaryMap.get(ExpressFeedBackTongjiEnum.FeedBacked.getValue()) %></strong></td>
									<td width="120" align="center" valign="middle"><strong><%=summaryMap.get(ExpressFeedBackTongjiEnum.TodayNotFeedBack.getValue()) %></strong></td>
									<td width="120" align="center" valign="middle"><strong><%=summaryMap.get(ExpressFeedBackTongjiEnum.YestodayNotFeedBack.getValue()) %></strong></td>
								</tr>
							<%} %>
						</tbody>
					</table>
			<%}else if(expDTO!=null){ %>
			<!-- 第一行显示的内容   【总领货：142单 （今日领货：0单， 遗留货物：141单， 历史未归班货物：0单， 暂不处理货物：1单）】 -->
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
				<tr id="customertr" class=VwCtr style="display:">
					<td width="250">小件员：<%=nowDeliverName %></td>
					<td>总领货：<b  style="color:#ff9900"><%=expDTO.getAllNumber() %></b>单
					（今日领货：<%=expDTO.getNowNumber() %>单，未反馈<%=expDTO.getUnfeedBackNumber() %>单）</td>
				</tr>
				
				<!-- 左边显示各种状态的订单数量   begin-->
				<tr>
					<td>
						<strong>已反馈订单：<%=expDTO.getAlreadyFeedBackCount() %>单<span style="height: 25"></span><%if(expDTO.getAlreadyFeedBackCount()>0){ %><input type="button" class="input_button2" value="导出"  onclick="exportAlreadyFeedBack_fun()"  id="exportAlreadyFeedBack" /><%} %></strong>
						<input id="yfkcwb" type="hidden" value="<%=expDTO.getAlreadyFeedBackCount()%>"/>
					</td>
					<td><span id="yfk" style="height: 25" >请扫描确认：<input id="fankui_C"  type="text" value="" /></span></td>
				</tr>
				
				
				
				<!-- 揽件成功 -->
				<%if(expDTO.getFeedBackSuccess()>0){ %>
				
				<tr><input type="hidden" id="executeStateType" value="<%=executeStateType %>"/>
					<td><input type="checkbox"	value="pick_successed" id="pick_successed_box"/> 
					<span style="height: 25" >揽收成功：<%=expDTO.getFeedBackSuccess() %> </span></td>
					<td>&nbsp;</td>
				</tr>
				
				<tr id="pick_successed_list"  style="display: none">
					<td colspan="2">
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
							<tr class="font_1">
								<td width="15%" height="38" align="center" valign="middle" bgcolor="#eef6ff">预订单编号</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">领货时间</td>
								<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">寄件人</td>
								<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">手机号</td>
								<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">固话</td>
								<td width="35%" align="center" valign="middle" bgcolor="#eef6ff">收件人地址</td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
								<%} %>
							</tr>
							
							
							<%for(ExpressFeedBackView  efb:expDTO.getFeedBackSuccessList()){
								%>
							<tr id="<%=efb.getPreOrderNo() %>" keyName="pick_successed" gcaid="<%=efb.getGcaid() %>">
<%-- 								<td width="10%" align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath() %>/preOrderSelect/queckSelectPreOrder/<%=efb.getPreOrderNo()%>" ><%=efb.getPreOrderNo()+efb.isHistory()%></a></td> --%>
								<td width="10%" align="center" valign="middle"><%=efb.getPreOrderNo()+efb.isHistory()%></td>
								<td width="15%"  align="center" valign="middle" ><%=efb.getDistributeDelivermanTimeStr() %></td>
								<td width="8%"  align="center" valign="middle" ><%=efb.getSendPerson() %></td>
								<td width="10%" align="center" valign="middle" ><%=efb.getCellphone() %></td>
								<td width="9%"  align="center"><%=efb.getTelephone() %></td>
								<td width="15%" align="center"><%=efb.getCollectAddress() %></td>
								<td width="15%" align="center">已反馈</td>
								<%-- 
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
									<td width="10%" align="center">
									<a href="javascript:if(<%=efb.getHandleUserId() %>==0||<%=efb.getHandleUserId() %>==<%=usermap.get("userid") %>){edit_button('<%=efb.getPreOrderNo() %>');}else{alert('不是同一个操作人！');}">[修改]</a>
									<%if(isGuiBanUseZanBuChuLi.equals("yes")){ %>
									<%if(efb.getGcaid()==-1){ %>
										[<a id="sub_<%=efb.getPreOrderNo() %>" href="javascript:reSub('<%=efb.getPreOrderNo() %>');">恢复</a>]
									<%}else{ %>
										[<a id="sub_<%=efb.getPreOrderNo() %>" href="javascript:noSub('<%=efb.getPreOrderNo() %>');">暂不处理</a>]
									<%}} %>
									</td>
								<%} %>
								 --%>
							</tr>
							<%} %>
						</table>
					</td>
				</tr>
				<%} %>
				
				<!-- 延迟揽件 -->
				<%if(expDTO.getFeedBackDelayPicked()>0){ %>
				<tr>
					<td><input type="checkbox"	value="pick_delay" id="pick_delay_box"/> 
					<span style="height: 25" >延迟揽件：<%=expDTO.getFeedBackDelayPicked() %> </span></td>
					<td>&nbsp;</td>
				</tr>
				
				<tr id="pick_delay_list"  style="display: none">
					<td colspan="2">
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
							<tr class="font_1">
								<td width="15%" height="38" align="center" valign="middle" bgcolor="#eef6ff">预订单编号</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">领货时间</td>
								<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">寄件人</td>
								<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">手机号</td>
								<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">固话</td>
								<td width="35%" align="center" valign="middle" bgcolor="#eef6ff">收件人地址</td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
								<%} %>
							</tr>
							
							
							<%for(ExpressFeedBackView  efb:expDTO.getFeedBackDelayPickedList()){
								%>
							<tr id="<%=efb.getPreOrderNo() %>" keyName="pick_delay" gcaid="<%=efb.getGcaid() %>">
<%-- 								<td width="10%" align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath() %>/preOrderSelect/queckSelectPreOrder/<%=efb.getPreOrderNo()%>" ><%=efb.getPreOrderNo()%></a></td> --%>
								<td width="10%" align="center" valign="middle"><%=efb.getPreOrderNo()%></td>
								<td width="8%"  align="center" valign="middle" ><%=efb.getDistributeDelivermanTimeStr() %></td>
								<td width="8%"  align="center" valign="middle" ><%=efb.getSendPerson() %></td>
								<td width="10%" align="center" valign="middle" ><%=efb.getCellphone() %></td>
								<td width="9%"  align="center"><%=efb.getTelephone() %></td>
								<td width="15%" align="center"><%=efb.getCollectAddress() %></td>
								
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
									<td width="10%" align="center">
									<%if(isReAllocateExpress.equals("yes")){ %>
										<%-- [<a id="sub_<%=efb.getPreOrderNo() %>" href="javascript:reSub('<%=efb.getPreOrderNo() %>');">恢复</a>] --%>
										已反馈
									<%}else{ %>
										<a href="javascript:if(<%=efb.getFeedbackUserId() %>==0||<%=efb.getFeedbackUserId() %>==<%=usermap.get("userid") %>){debugger;edit_button('<%=efb.getPreOrderNo() %>');}else{alert('不是同一个操作人！');}">[修改]</a>
									<%}%>
								
									</td>
								<%} %>
							</tr>
							<%} %>
						</table>
					</td>
				</tr>
				<%} %>
				
				<!-- 揽收失败 -->
				<%if(expDTO.getFeedBackFailed()>0){ %>
				<tr>
					<td><input type="checkbox"	value="pick_failed" id="pick_failed_box"/> 
					<span style="height: 25" >揽收失败：<%=expDTO.getFeedBackFailed() %> </span></td>
					<td>&nbsp;</td>
				</tr>
				
				<tr id="pick_failed_list"  style="display: none">
					<td colspan="2">
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
							<tr class="font_1">
								<td width="15%" height="38" align="center" valign="middle" bgcolor="#eef6ff">预订单编号</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">领货时间</td>
								<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">寄件人</td>
								<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">手机号</td>
								<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">固话</td>
								<td width="35%" align="center" valign="middle" bgcolor="#eef6ff">收件人地址</td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
								<%} %>
							</tr>
							
							
							<%for(ExpressFeedBackView  efb:expDTO.getFeedBackFailedList()){
								%>
							<tr id="<%=efb.getPreOrderNo() %>" keyName="pick_failed" gcaid="<%=efb.getGcaid() %>">
<%-- 								<td width="10%" align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath() %>/preOrderSelect/queckSelectPreOrder/<%=efb.getPreOrderNo()%>" ><%=efb.getPreOrderNo()+efb.isHistory()%></a></td> --%>
								<td width="10%" align="center" valign="middle"><%=efb.getPreOrderNo()+efb.isHistory()%></td>
								<td width="8%"  align="center" valign="middle" ><%=efb.getDistributeDelivermanTimeStr() %></td>
								<td width="8%"  align="center" valign="middle" ><%=efb.getSendPerson() %></td>
								<td width="10%" align="center" valign="middle" ><%=efb.getCellphone() %></td>
								<td width="9%"  align="center"><%=efb.getTelephone() %></td>
								<td width="15%" align="center"><%=efb.getCollectAddress() %></td>
								<td width="15%" align="center">已反馈</td>
								<%-- 
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
									<td width="10%" align="center">
									<a href="javascript:if(<%=efb.getHandleUserId() %>==0||<%=efb.getHandleUserId() %>==<%=usermap.get("userid") %>){edit_button('<%=efb.getPreOrderNo() %>');}else{alert('不是同一个操作人！');}">[修改]</a>
									<%if(isGuiBanUseZanBuChuLi.equals("yes")){ %>
									<%if(efb.getGcaid()==-1){ %>
										[<a id="sub_<%=efb.getPreOrderNo() %>" href="javascript:reSub('<%=efb.getPreOrderNo() %>');">恢复</a>]
									<%}else{ %>
										[<a id="sub_<%=efb.getPreOrderNo() %>" href="javascript:noSub('<%=efb.getPreOrderNo() %>');">暂不处理</a>]
									<%}} %>
									</td>
								<%} %>
								 --%>
							</tr>
							<%} %>
						</table>
					</td>
				</tr>
				<%} %>
				
				<!-- 站点超区 -->
				<%if(expDTO.getFeedBackAreaWrong()>0){ %>
				<tr>
					<td><input type="checkbox"	value="area_wrong" id="area_wrong_box"/> 
					<span style="height: 25" >站点超区：<%=expDTO.getFeedBackAreaWrong() %> </span></td>
					<td>&nbsp;</td>
				</tr>
				
				<tr id="area_wrong_list"  style="display: none">
					<td colspan="2">
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
							<tr class="font_1">
								<td width="15%" height="38" align="center" valign="middle" bgcolor="#eef6ff">预订单编号</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">领货时间</td>
								<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">寄件人</td>
								<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">手机号</td>
								<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">固话</td>
								<td width="35%" align="center" valign="middle" bgcolor="#eef6ff">收件人地址</td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
								<%} %>
							</tr>
							
							
							<%for(ExpressFeedBackView  efb:expDTO.getFeedBackAreaWrongList()){
								%>
							<tr id="<%=efb.getPreOrderNo() %>" keyName="area_wrong" gcaid="<%=efb.getGcaid() %>">
<%-- 								<td width="10%" align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath() %>/preOrderSelect/queckSelectPreOrder/<%=efb.getPreOrderNo()%>" ><%=efb.getPreOrderNo()+efb.isHistory()%></a></td> --%>
								<td width="10%" align="center" valign="middle"><%=efb.getPreOrderNo()+efb.isHistory()%></td>
								<td width="8%"  align="center" valign="middle" ><%=efb.getDistributeDelivermanTimeStr() %></td>
								<td width="8%"  align="center" valign="middle" ><%=efb.getSendPerson() %></td>
								<td width="10%" align="center" valign="middle" ><%=efb.getCellphone() %></td>
								<td width="9%"  align="center"><%=efb.getTelephone() %></td>
								<td width="15%" align="center"><%=efb.getCollectAddress() %></td>
								<td width="15%" align="center">已反馈</td>
								<%-- 
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
									<td width="10%" align="center">
									<a href="javascript:if(<%=efb.getHandleUserId() %>==0||<%=efb.getHandleUserId() %>==<%=usermap.get("userid") %>){edit_button('<%=efb.getPreOrderNo() %>');}else{alert('不是同一个操作人！');}">[修改]</a>
									<%if(isGuiBanUseZanBuChuLi.equals("yes")){ %>
									<%if(efb.getGcaid()==-1){ %>
										[<a id="sub_<%=efb.getPreOrderNo() %>" href="javascript:reSub('<%=efb.getPreOrderNo() %>');">恢复</a>]
									<%}else{ %>
										[<a id="sub_<%=efb.getPreOrderNo() %>" href="javascript:noSub('<%=efb.getPreOrderNo() %>');">暂不处理</a>]
									<%}} %>
									</td>
								<%} %>
								 --%>
							</tr>
							<%} %>
						</table>
					</td>
				</tr>
				<%} %>
				
				<!-- 揽件超区 -->
				<%if(expDTO.getFeedBackPickWrong()>0){ %>
				<tr>
					<td><input type="checkbox"	value="pick_wrong" id="pick_wrong_box"/> 
					<span style="height: 25" >揽件超区：<%=expDTO.getFeedBackPickWrong() %> </span></td>
					<td>&nbsp;</td>
				</tr>
				
				<tr id="pick_wrong_list"  style="display: none">
					<td colspan="2">
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
							<tr class="font_1">
								<td width="15%" height="38" align="center" valign="middle" bgcolor="#eef6ff">预订单编号</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">领货时间</td>
								<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">寄件人</td>
								<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">手机号</td>
								<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">固话</td>
								<td width="35%" align="center" valign="middle" bgcolor="#eef6ff">收件人地址</td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
								<%} %>
							</tr>
							
							
							<%for(ExpressFeedBackView  efb:expDTO.getFeedBackPickWrongList()){
								%>
							<tr id="<%=efb.getPreOrderNo() %>" keyName="pick_wrong" gcaid="<%=efb.getGcaid() %>">
<%-- 								<td width="10%" align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath() %>/preOrderSelect/queckSelectPreOrder/<%=efb.getPreOrderNo()%>" ><%=efb.getPreOrderNo()+efb.isHistory()%></a></td> --%>
								<td width="10%" align="center" valign="middle"><%=efb.getPreOrderNo()+efb.isHistory()%></td>
								<td width="8%"  align="center" valign="middle" ><%=efb.getDistributeDelivermanTimeStr() %></td>
								<td width="8%"  align="center" valign="middle" ><%=efb.getSendPerson() %></td>
								<td width="10%" align="center" valign="middle" ><%=efb.getCellphone() %></td>
								<td width="9%"  align="center"><%=efb.getTelephone() %></td>
								<td width="15%" align="center"><%=efb.getCollectAddress() %></td>
								<td width="15%" align="center">已反馈</td>
								<%-- 
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
									<td width="10%" align="center">
									<a href="javascript:if(<%=efb.getHandleUserId() %>==0||<%=efb.getHandleUserId() %>==<%=usermap.get("userid") %>){edit_button('<%=efb.getPreOrderNo() %>');}else{alert('不是同一个操作人！');}">[修改]</a>
									<%if(isGuiBanUseZanBuChuLi.equals("yes")){ %>
									<%if(efb.getGcaid()==-1){ %>
										[<a id="sub_<%=efb.getPreOrderNo() %>" href="javascript:reSub('<%=efb.getPreOrderNo() %>');">恢复</a>]
									<%}else{ %>
										[<a id="sub_<%=efb.getPreOrderNo() %>" href="javascript:noSub('<%=efb.getPreOrderNo() %>');">暂不处理</a>]
									<%}} %>
									</td>
								<%} %>
								 --%>
							</tr>
							<%} %>
						</table>
					</td>
				</tr>
				<%} %>
				
				<tr>
					<td><strong>未反馈订单：<%=expDTO.getUnfeedBackNumber() %>单</strong><%if(expDTO.getUnfeedBackNumber()>0){ %>  <input type="button" class="input_button2" value="导出"  onclick="exportNotFeedBack_fun();" id="exportNotFeedBack" /><%} %></td>
					<td id="wfk">
						<span id="wfkcwb" style="height: 25">请扫描反馈：  <input id="weifankui_C" type="text"/></span>
					</td>
				</tr>
				
				<!-- 未反馈的订单 --><!-- 默认显示的是未反馈的订单 -->
				<tr id="un_feedback">
					<td colspan="2">
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
							<tr class="font_1">
								<td width="15%" height="38" align="center" valign="middle" bgcolor="#eef6ff">预订单编号</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">领货时间</td>
								<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">寄件人</td>
								<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">手机号</td>
								<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">固话</td>
								<td width="35%" align="center" valign="middle" bgcolor="#eef6ff">收件人地址</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
							</tr>
							<%for(ExpressFeedBackView  efb:expDTO.getUnFeedBackList()){
								%>
							<tr id="<%=efb.getPreOrderNo() %>" keyName="un_feedback" gcaid="<%=efb.getGcaid() %>">
<%-- 								<td width="10%" align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath() %>/preOrderSelect/queckSelectPreOrder/<%=efb.getPreOrderNo()%>" ><%=efb.getPreOrderNo()+efb.isHistory()%></a></td> --%>
								<td width="10%" align="center" valign="middle"><%=efb.getPreOrderNo()+efb.isHistory()%></td>
								<td width="8%"  align="center" valign="middle" ><%=efb.getDistributeDelivermanTimeStr() %></td>
								<td width="8%"  align="center" valign="middle" ><%=efb.getSendPerson() %></td>
								<td width="10%" align="center" valign="middle" ><%=efb.getCellphone() %></td>
								<td width="9%"  align="center"><%=efb.getTelephone() %></td>
								<td width="15%" align="center"><%=efb.getCollectAddress() %></td>
								<td id="<%=efb.getPreOrderNo() %>_cz" width="10%" align="center">
								<a href="javascript:edit_button('<%=efb.getPreOrderNo()%>');">[反馈]</a>　
								</td>
							</tr>
							<%} %>
						</table>
					</td>
				</tr>
			</table>
			
			<div class="jg_10"></div>
			<div class="jg_10"></div>
			<div class="jg_10"></div>
			<div class="jg_10"></div>
			<div class="jg_10"></div>
			<div class="jg_10"></div>
			<div class="jg_10"></div>
			<div class="jg_10"></div>
			<%} %>
		</form>
	</div>
	<!-- 
	<div>
		<div id="dlgSubmitExpressBox" class="easyui-dialog" style="width: 880px; height: 300px; top:100px;left:100px; padding: 10px 20px;z-index:400px;" closed="true" buttons="#btnsOfExpress">
			<input type="hidden" id="exp_selectIds" value="0" />
			<div style="margin-left: 10px;" id="submitContent">
				
			</div>
			<br/>
			<div align="center">
				<input type="button" class="input_button2" id="feedBackOpeation" value="保存" onclick="feedBackExpressOperate();"/>
			</div>
		</div>
	</div>	
	 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/expressFeedback/getnowDeliveryState/" />
</body>
</html>
