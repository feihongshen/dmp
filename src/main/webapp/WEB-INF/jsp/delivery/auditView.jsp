<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.controller.DeliveryStateView"%>
<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.controller.DeliveryStateDTO"%>
<%@page import=" cn.explink.domain.DeliveryState"%>
<%@page import=" cn.explink.domain.Customer"%>
<%@page import=" cn.explink.controller.DeliveryStateView"%>
<%@page import=" cn.explink.domain.Reason"%>
<%@page import=" cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import=" cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import=" cn.explink.enumutil.DeliveryTongjiEnum"%>
<%@page import=" cn.explink.domain.*"%>
<%@page import=" net.sf.json.JSONObject"%>
<%@page import="cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<JSONObject> deliverList = request.getAttribute("deliverList")==null?new ArrayList<JSONObject>():(List<JSONObject>) request.getAttribute("deliverList");
DeliveryStateDTO dsDTO = request.getAttribute("deliveryStateDTO")==null?null:(DeliveryStateDTO)request.getAttribute("deliveryStateDTO");
DeliveryStateView deliverystate =request.getAttribute("deliverystate")==null?new DeliveryStateView(): (DeliveryStateView)request.getAttribute("deliverystate");

/* List<Customer> customers = request.getAttribute("customers")==null?new ArrayList<Customer>():(List<Customer>)request.getAttribute("customers"); */
List<Reason> returnlist = (List<Reason>)request.getAttribute("returnlist");
List<Reason> staylist = (List<Reason>)request.getAttribute("staylist");
List<Customer> customerList = request.getAttribute("customerList")==null?null:(List<Customer>)request.getAttribute("customerList");
Map<Long,Map<Long,Map<Long,Long>>>  deliveryInCountMap = request.getAttribute("deliveryMap")==null?null:(Map<Long,Map<Long,Map<Long,Long>>>)request.getAttribute("deliveryMap");
Map<Long,Map<Long,Long>>  huizongMap = request.getAttribute("huizongMap")==null?null:(Map<Long,Map<Long,Long>>)request.getAttribute("huizongMap");
Map usermap = (Map) session.getAttribute("usermap");

Switch pl_switch = (Switch) request.getAttribute("pl_switch");
String showposandqita = request.getAttribute("showposandqita")==null?"no":(String)request.getAttribute("showposandqita");
String isGuiBanUseZanBuChuLi = request.getAttribute("isGuiBanUseZanBuChuLi")==null?"no":(String)request.getAttribute("isGuiBanUseZanBuChuLi");
String isReasonRequired=(String)request.getAttribute("isReasonRequired");

Long deliveryStateType=(Long)session.getAttribute("deliveryStateType"); 
//用户角色id
long userroleid = request.getAttribute("userroleid")==null?0:((Long)request.getAttribute("userroleid")).longValue();
String deliveryxiaojianyuan=request.getAttribute("deliveryxiaojianyuan")==null?"yes":(String)request.getAttribute("deliveryxiaojianyuan");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查询系统</title>
<link href="<%=request.getContextPath()%>/css/reset.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/css/index.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
<script>
function thisCheck(){
	$("#podresultid", parent.document).change(function(){//监控配送状态变化 对显示字段做相应处理
		var podresultid =  $("#podresultid", parent.document).val();
		init_deliverystate();
		
		if(podresultid==<%=DeliveryStateEnum.PeiSongChengGong.getValue() %>){//配送成功
			peisongObj(podresultid);
		}else if(podresultid==<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue()%>){//上门退成功
			shagnmentuiObj();
		}else if(podresultid==<%=DeliveryStateEnum.ShangMenHuanChengGong.getValue()%>){//上门换成功
			shangmenhuanObj(podresultid);
		}else if(podresultid==<%=DeliveryStateEnum.JuShou.getValue()%>){//拒收
			quantuiObj();
		}else if(podresultid==<%=DeliveryStateEnum.BuFenTuiHuo.getValue()%>){//部分退货
			bufentuihuoObj(podresultid);
		}else if(podresultid==<%=DeliveryStateEnum.FenZhanZhiLiu.getValue()%>){//分站滞留
			zhiliuObj();
		}else if(podresultid==<%=DeliveryStateEnum.ShangMenJuTui.getValue()%>){//上门拒退
			shangmenjutuiObj();
		}else if(podresultid==<%=DeliveryStateEnum.HuoWuDiuShi.getValue()%>){//货物丢失
			huowudiushiObj();
		}
		else if(podresultid==<%=DeliveryStateEnum.DaiZhongZhuan.getValue()%>){//待中转
			zhongzhuanObj();
		}
	});
}

function getDeliveryStateByDeliveryId(){
	if($("#deliveryid").val()=="0"){
		location.href="auditView";
	}
	location.href="auditView?deliveryId="+$("#deliveryid").val();
}
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
var cwb_C=""; //记录扫描过的单号
function addClassToIsC(cwb){
	$("#"+$("tr[id='"+cwb+"']").attr("keyName")+"_box").attr("checked",true);
	$("#"+$("tr[id='"+cwb+"']").attr("keyName")+"_list").show();
	if($("tr[id$='_list'] tr[id='"+cwb+"']").attr("gcaid")==-1){
		$("tr[id$='_list'] tr[id='"+cwb+"'] td").attr("style","color:#FFF; background:#F33; font-weight:bold");
	}else{
		$("tr[id$='_list'] tr[id='"+cwb+"'] td").attr("style","color:#FFF; background:#09C; font-weight:bold");
	}
	if(cwb_C.indexOf(cwb)==-1){
		cwb_C = cwb_C+","+cwb;
	}
}
$(function(){
	var type=$("#deliveryStateType").val();

	
		switch(type)
		{
		case '1':
			$("#peisong_chenggong_list").show();
			break;
		case '2':
			$("#shangmentui_chenggong_list").show();
			break;
		case '3':
			$("#shangmenhuan_chenggong_list").show();
			break;
		case '4':
			$("#tuihuo_list").show();
			break;
		case '5':
			$("#bufentuihuo_list").show();
			break;
		case '6':
			$("#zhiliu_list").show();
			break;
		case '7':
			$("#shangmentui_jutui_list").show();
			break;
		case '8':
			$("#diushi_list").show();
			break;
		
		case '10':
			$("#zhongzhuan_list").show();
			break;
			
			default :$("[id$='_list']").hide();
		
		
	}
	$("input[id$='_box']").click(function(){
		if($(this).attr("checked")){
			$("#"+$(this).val().trim()+"_list").show();
		}
 		else{
			$("#"+$(this).val().trim()+"_list").hide();
		} 
	}
	);
	
	$("input[id$='_C']").keydown(function(event){
		if(event.keyCode==13) {
			//清空历史背景色
			$("td[style$='font-weight:bold']").attr("style","");
			
			if($("tr[id='"+$(this).val().trim()+"']").length==0){
				alert("订单号“"+$(this).val().trim()+"”已归班审核过或者不是这个小件员的货！");
			}else if($(this).attr("id")=="fankui_C"){
				if($("tr[id$='_list'] tr[id='"+$(this).val().trim()+"']").length==0){
					alert("订单号“"+$(this).val().trim()+"”不在已反馈订单列表里！");
				}else{
					addClassToIsC($(this).val().trim());
				}
			}else if($(this).attr("id")=="weifankui_C"){
				if($("tr[id='weifankui'] tr[id='"+$(this).val().trim()+"']").length==0){
					alert("订单号“"+$(this).val().trim()+"”不在未反馈订单列表里！");
				}else{
					$("tr[id='"+$(this).val().trim()+"'] td").attr("style","color:#FFF; background:#09C; font-weight:bold");
					edit_button($(this).val().trim());
				}
			}
			$(this).val("");
		}
		
		<%-- <%for(Reason r:reasons){ %>
		$("td[podremarkKey='<%=r.getReasonid()%>']").html("<%=r.getReasoncontent()%>");
		<%} %> --%>
});

$("#subButton").click(function(){
	var shenehTypeCheckbox=$("input[type='checkbox']");
	var checkedArray=[];
	var k=0;
	$.each(shenehTypeCheckbox,function (i,a){
		if($(a).attr("checked")){
			checkedArray[k]=$(a).val();
			k=k+1;
		}
	});
	var subTrStr = "";
	for(var i=0;i<checkedArray.length;i++){
		var trArray = $("tr[gcaid='0'][keyName="+checkedArray[i]+"]");
		for(var j=0;j<trArray.length;j++){
			subTrStr = subTrStr+"'"+$("tr[gcaid='0'][keyName="+checkedArray[i]+"]").eq(j).attr("id")+"',";
		}
	}
	subTrStr = subTrStr.substring(0, subTrStr.length-1); 
		/* var trArray = $("tr[gcaid='0']");
		var subTrStr = "";
		for(var i=0;i<trArray.length;i++){
			subTrStr = subTrStr+"'"+$("tr[gcaid='0']").eq(i).attr("id")+"',";
		}
		subTrStr = subTrStr.substring(0, subTrStr.length-1); */
		
		trArray = $("tr[keyName='weifankui']");
		var noSubTrStr = "";
		for(var i=0;i<trArray.length;i++){
			noSubTrStr = noSubTrStr+"'"+$("tr[keyName='weifankui']").eq(i).attr("id")+"',";
		}
		noSubTrStr = noSubTrStr.substring(0, noSubTrStr.length-1);
		
		trArray = $("tr[gcaid='-1']");
		var zanbuchuliTrStr = "";
		for(var i=0;i<trArray.length;i++){
			zanbuchuliTrStr = zanbuchuliTrStr+"'"+$("tr[gcaid='-1']").eq(i).attr("id")+"',";
		}
		zanbuchuliTrStr = zanbuchuliTrStr.substring(0, zanbuchuliTrStr.length-1);
		if(subTrStr == ""){
			alert("请勾选已反馈的订单！");
		}else{
			$.ajax({type: "POST",
				url:"sub",
				data: {zanbuchuliTrStr: zanbuchuliTrStr ,subTrStr :subTrStr,nocwbs:noSubTrStr,deliveryId:$("#deliveryid").val()},
				success : function(data) {
					$("#alert_box",parent.document).html(data);
					viewBox();
				}
			});	
		}
	});
	
	
	
	//加载页面后 自动处理“暂不处理”状态的订单背景颜色
	$("tr[gcaid='-1'] td").attr("style","color:#FFF; background:#CCC");
	
	
});

function addInit(){
}
function addSuccess(data){
}
function editInit(){
	for(var i =0 ; i < initEditArray.length ; i ++){
		var value = initEditArray[i].split(",")[0];
		var name = initEditArray[i].split(",")[1];
		$("#"+name, parent.document).val(value);
	}
	thisCheck();
	window.parent.click_podresultid(<%=deliverystate.getDeliverystate()%>,<%=DeliveryStateEnum.PeiSongChengGong.getValue()%>,<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue()%>,
   			<%=DeliveryStateEnum.ShangMenHuanChengGong.getValue()%>,<%=DeliveryStateEnum.JuShou.getValue()%>,
   			<%=DeliveryStateEnum.BuFenTuiHuo.getValue() %>,<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>,<%=DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue() %>,
   			<%=DeliveryStateEnum.ShangMenJuTui.getValue() %>,<%=DeliveryStateEnum.HuoWuDiuShi.getValue() %>,<%=DeliveryStateEnum.DaiZhongZhuan.getValue() %>,
   			$("#backreasonid", parent.document).val(),$("#leavedreasonid", parent.document).val(),$("#firstlevelreasonid", parent.document).val(),$("#podremarkid", parent.document).val(),$("#newpaywayid", parent.document).val(),
   			$("#weishuakareasonid", parent.document).val(),$("#losereasonid", parent.document).val(),false);
	$("input[type='text']", parent.document).focus(function(){
		$(this).select();
	});
}
function editSuccess(data){
	location.href="<%=request.getContextPath()%>/delivery/auditView?deliveryId="+$("#deliveryid").val()+"&cwb_C="+cwb_C;
}



function delSuccess(data){
}

 
$(function(){
	<% if(request.getParameter("cwb_C")!=null&&request.getParameter("cwb_C").length()>2){
		for( String cwb : request.getParameter("cwb_C").substring(1).split(",")){%>
	addClassToIsC('<%=cwb %>');<% }}%>
});


//批量反馈根据不同的配送结果显示相应的原因以及支付方式
function checkpodresultid(podresultid){
	if(podresultid==<%=DeliveryStateEnum.PeiSongChengGong.getValue() %>){
		$("#paywayid_p").show();
		$("#backreasonid_p").hide();
		$("#leavedreasonid").hide();
		$("#firstlevelreasonid").hide();
		$("#content").hide();
	}else if(podresultid==<%=DeliveryStateEnum.JuShou.getValue() %>){
		$("#paywayid_p").hide();
		$("#backreasonid_p").show();
		$("#leavedreasonid").hide();
		$("#firstlevelreasonid").hide();
		$("#content").show();
	}else if(podresultid==<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>){
		$("#paywayid_p").hide();
		$("#backreasonid_p").hide();
		$("#leavedreasonid").show();
		$("#firstlevelreasonid").show();
		$("#content").show();
	}else{
		$("#paywayid_p").hide();
		$("#backreasonid_p").hide();
		$("#leavedreasonid").hide();
		$("#firstlevelreasonid").hide();
		$("#content").hide();
	}
	$("#paywayid_p").val("0");
	$("#backreasonid_p").val("0");
	$("#leavedreasonid").val("0");
	$("#firstlevelreasonid").val("0");
}

function isshowpl(){
	if($("#isshow").attr("checked")=="checked"){
		$("#plfk").show();
		$("#wfkcwb").hide();
		$("#yfk").hide();
	}else{
		$("#plfk").hide();
		$("#wfkcwb").show();
		$("#yfk").show();
	}
}
function exportWeifankui(){
	<%
	 if(dsDTO!=null&&dsDTO.getWeifankuiNumber()>0){
	%>
	location.href="<%=request.getContextPath() %>/delivery/exportweifankui?deliveryId="+$("#deliveryid").val();
	<%}else{%>
	alert("没有数据不能导出");
	<%}%>
	
}


function exportYifankui(){
	<%
	 if(dsDTO!=null&&dsDTO.getYifankuiNumber()>0){
	%>
	location.href="<%=request.getContextPath() %>/delivery/exportyifankui?deliveryId="+$("#deliveryid").val();
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
			<li><a href="#"  class="light">归班反馈</a></li>		
			<li><a href="<%=request.getContextPath()%>/delivery/batchEditDeliveryState">批量反馈</a></li>
		</ul>
	</div>

		<form name="form1" method="post" action="commonupload" >

			<div class="form_topbg">
				本站投递中订单： <select id="deliveryid" name="deliveryid" class="select1" onchange="getDeliveryStateByDeliveryId()" >
					<option value="0">总览</option>
					<%
					String nowDeliverName = "";
					long allDeliverCount = 0;
					if(deliverList!=null && deliverList.size()>0){
					for(JSONObject deliver : deliverList){ allDeliverCount+=deliver.getLong("num");%>
					<option value="<%=deliver.getString("deliveryid") %>" <%if(deliver.getString("deliveryid").equals(request.getParameter("deliveryId"))){out.print("selected");nowDeliverName=deliver.getString("delivername");} %> ><%=deliver.getString("delivername") %>{<%=deliver.getString("num") %>}</option>
					<%}} %>
				</select>　　<font color="red"><b>{<%if(deliverList.size()>0){ %><%=deliverList.size() %>人投递中  共计<%=allDeliverCount %>单 <%}else{out.print("当前无人投递");} %>}</b></font>
				<a href="list/1" >历史审核记录 >></a>
			</div>


			<%if(deliveryInCountMap != null){ %>
			<%if(customerList != null && customerList.size()>0){ %>
			<div class="right_title">
				<div style="position:relative; z-index:0; width:100% ">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" style="height:30px">
							<tbody>
								<tr class="font_1" style="background-color: rgb(255, 255, 255); ">
									<td width="120" align="center" valign="middle" bgcolor="#EEF6FF">小件员</td>
									<td colspan="4" align="center" valign="middle" bgcolor="#eef6ff" >供货商</td>
								</tr>
							</tbody>
						</table>
					<div style="width: 122px; position: absolute; left: 0; top: 29px; overflow: hidden; z-index: 8; border-right: 1PX solid #9CC">
						<table width="130" border="0" cellspacing="1" cellpadding="0" class="table_2" >
							<tbody>
							<%if(deliverList != null && deliverList.size()>0){ %>
								<%for(JSONObject jd : deliverList){ %>
								<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
								
									<td width="120"  rowspan="3" align="center" valign="middle"><%=jd.getString("delivername") %></td>
									<td align="center" valign="middle" bgcolor="#FFFFFF" >&nbsp;</td>
								</tr>
								<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
									<td align="center" valign="middle">&nbsp;</td>
								</tr>
								<tr height="30"  style="background-color: rgb(249, 252, 253); ">
									<td align="center" valign="middle">&nbsp;</td>
								</tr>
							<%}} %>
								<tr  height="29"  style="background-color: rgb(249, 252, 253); ">
									<td align="center" valign="middle" bgcolor="#F1F1F1">合计</td>
									<td align="center" valign="middle" bgcolor="#F1F1F1">&nbsp;</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div style="overflow-x:scroll; width:100%;">
					<table width="<%=request.getAttribute("width")==null?1400:request.getAttribute("width") %>" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table" style="margin-left:120px">
							<tbody>
							<%if(deliverList != null && deliverList.size()>0){ %>
								<%for(JSONObject jd : deliverList){ %>
								<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
								<%for(Customer c: customerList){ %>
								    <td colspan="4" align="center" valign="middle" bgcolor="#f8f8f8" ><%=c.getCustomername() %></td>
								<%} %>
								</tr>
								<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
									<%for(Customer c: customerList){ %>
									<td align="center" valign="middle" bgcolor="#f8f8f8">领货</td>
									<td align="center" valign="middle" bgcolor="#f8f8f8">今日未反馈</td>
									<td align="center" valign="middle" bgcolor="#f8f8f8">已反馈</td>
									<td align="center" valign="middle" bgcolor="#f8f8f8">昨日未反馈</td>
									<%} %>
								</tr>
								<tr  class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
									<%for(Customer c: customerList){ %>
									<% Map<Long,Long> map= deliveryInCountMap.get(jd.getLong("deliveryid")).get(c.getCustomerid()); %>
									<td width="100" align="center" valign="middle"><strong><%=map.get(DeliveryTongjiEnum.LingHuo.getValue()) %></strong></td>
									<td width="100" align="center" valign="middle"><strong><%=map.get(DeliveryTongjiEnum.JinriWeifankui.getValue()) %></strong></td>
									<td width="100" align="center" valign="middle"><strong><%=map.get(DeliveryTongjiEnum.JinriYIfankui.getValue()) %></strong></td>
									<td width="100" align="center" valign="middle"><strong><%=map.get(DeliveryTongjiEnum.zuoriWeifankui.getValue()) %></strong></td>
									<%} %>                                         
								</tr>
								<%} }%>
								<%if(huizongMap != null && !huizongMap.isEmpty()){ %>
								<tr  height="28"  style="background-color: rgb(249, 252, 253); ">
									<%for(Customer c: customerList){ %>
									<% Map<Long,Long> map= huizongMap.get(c.getCustomerid()); %>
									<td align="center" valign="middle"><%=map.get(DeliveryTongjiEnum.LingHuo.getValue()) %></td>
									<td align="center" valign="middle"><%=map.get(DeliveryTongjiEnum.JinriWeifankui.getValue()) %></td>
									<td align="center" valign="middle"><%=map.get(DeliveryTongjiEnum.JinriYIfankui.getValue()) %></td>
									<td align="center" valign="middle"><%=map.get(DeliveryTongjiEnum.zuoriWeifankui.getValue()) %></td>
									<%} %>
								</tr>
								<%} %>
							</tbody>
						</table></div>
				
				</div>
			</div>
			
			<%} %>
			<%}else if(dsDTO!=null){ %>
			<table width="100%" height="23" border="0" cellpadding="0"
				cellspacing="5" class="right_set1">
				<tr id="customertr" class=VwCtr style="display:">
					<td width="250">小件员：<%=nowDeliverName %></td>
					<td>总领货：<b  style="color:#ff9900"><%=dsDTO.getAllNumber() %></b>单
					（今日领货：<%=dsDTO.getNowNumber() %>单，
					<%if(dsDTO.getYiliu()>=0){ %>
					遗留货物：<%=dsDTO.getYiliu() %>单，
					<%} %>
					历史未归班货物：<%=dsDTO.getLishi_weishenhe() %>单，
					暂不处理货物：<%=dsDTO.getZanbuchuli() %>单）</td>
				</tr>
				
				<tr>
					<td>
						<strong>已反馈订单：<%=dsDTO.getYifankuiNumber() %>单<span style="height: 25"></span><%if(dsDTO.getYifankuiNumber()>0){ %><input type="button" class="input_button2" value="导出"  onclick="exportYifankui()"  id="exportyifankui" /><%} %></strong>
						<input id="yfkcwb" type="hidden" value="<%=dsDTO.getYifankuiNumber()%>"/>
					</td>
					<td><span id="yfk" style="height: 25" >请扫描确认：<input id="fankui_C"  type="text" value="" /></span></td>
				</tr>
				<%if(dsDTO.getFankui_peisong_chenggong()>0){ %>
				<tr><input type="hidden" id="deliveryStateType" value="<%=deliveryStateType%>"/>
					<td><input type="checkbox"	value="peisong_chenggong" id="peisong_chenggong_box"/> 
					<span style="height: 25" >配送成功：<%=dsDTO.getFankui_peisong_chenggong() %>单，共收款<%=dsDTO.getAmount(dsDTO.getFankui_peisong_chenggongList()) %>元 </span></td>
					<td>&nbsp;</td>
				</tr>
				<tr id="peisong_chenggong_list"  style="display: none">
					<td colspan="2"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tr class="font_1">
								<td width="10%" height="38" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
								<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
								<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
								<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">收件地址</td>
								<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">应收金额</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">实收金额</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">收款方式</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">备注信息</td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
								<%} %>
							</tr>
							<%for(DeliveryStateView  ds:dsDTO.getFankui_peisong_chenggongList()){
								%>
							<tr id="<%=ds.getCwb() %>" keyName="peisong_chenggong" gcaid="<%=ds.getGcaid() %>">
								<td width="10%" align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath() %>/order/queckSelectOrder/<%=ds.getCwb()%>" ><%=ds.getCwb()+ds.isHistory() %></a></td>
								<td width="5%" align="center" valign="middle" >
								<%for(CwbOrderTypeIdEnum ce : CwbOrderTypeIdEnum.values()){if(ds.getCwbordertypeid()==ce.getValue()){ %>
									<%=ce.getText() %>
								<%}} %>
								</td>
								<td width="8%" align="center" valign="middle" customerKey="<%=ds.getCustomerid() %>"><%=StringUtil.nullConvertToEmptyString( ds.getCustomername()) %></td>
								<td width="8%" align="center" valign="middle" ><%=ds.getEmaildate() %></td>
								<td width="10%" align="left" valign="middle" ><%=ds.getConsigneeaddress() %></td>
								<td width="9%" align="right"><%=ds.getBusinessfee() %>元</td>
								<td width="15%" align="right"><%=ds.getReceivedfee() %>元</td>
								<td width="10%" align="center"><%=ds.getPaymentPattern() %></td>
								<td width="15%" align="center"><%=ds.getDeliverstateremark() %></td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center">
								<% if(ds.getCodpos().compareTo(BigDecimal.ZERO)==0){ %>
								<a href="javascript:if(<%=ds.getUserid() %>==0||<%=ds.getUserid() %>==<%=usermap.get("userid") %>){edit_button('<%=ds.getCwb()%>');}else{alert('不是同一个操作人！');}">[修改]</a>
								<%} %>
								<%-- <a href="javascript:edit_button('<%=ds.getCwb()%>');">[修改]</a> --%>
								<%if(isGuiBanUseZanBuChuLi.equals("yes")){ %>
								<%if(ds.getGcaid()==-1){ %>
									[<a id="sub_<%=ds.getCwb() %>" href="javascript:reSub('<%=ds.getCwb() %>',<%=ds.getReceivedfee() %>,<%=ds.getCash() %>,<%=ds.getPos() %>,0,0);">恢复</a>]
								<%}else{ %>
									[<a id="sub_<%=ds.getCwb() %>" href="javascript:noSub('<%=ds.getCwb() %>',<%=ds.getReceivedfee() %>,<%=ds.getCash() %>,<%=ds.getPos() %>,0,0);">暂不处理</a>]
								<%}} %>
								<%if(ds.getSign_img()!=null&&ds.getSign_img().length()>0){%>
									<a href="#" onclick="javascript:$('#signimg').attr('src','pjdimg?'+new Date().getTime()+'&cwb=<%=ds.getCwb()%>');javascript:$('#dlg-signimg').dialog('open');">[查看签名]</a>
								<%} %>
								</td>
								<%} %>
							</tr>
							<%} %>
						</table></td>
				</tr>
				<%} %>
				
				
				
				<%if(dsDTO.getFankui_tuihuo()>0){ %>
				<tr>
					<td><input  type="checkbox"	value="tuihuo" id="tuihuo_box" /> 
					<span style="height: 25" >拒收：<%=dsDTO.getFankui_tuihuo() %>单 </span></td>
					<td>&nbsp;</td>
				</tr>
				<tr id="tuihuo_list" style="display: none">
					<td colspan="2"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tr class="font_1">
								<td width="10%" height="38" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
								<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">收件地址</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">退货原因</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">原因备注</td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
								<%} %>
							</tr>
							<%for(DeliveryStateView  ds:dsDTO.getFankui_tuihuoList()){ %>
							<tr id="<%=ds.getCwb() %>" keyName="tuihuo" gcaid="<%=ds.getGcaid() %>">
								<td width="10%" align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath() %>/order/queckSelectOrder/<%=ds.getCwb()%>" ><%=ds.getCwb()+ds.isHistory() %></a></td>
								<td width="5%" align="center" valign="middle" >
								<%for(CwbOrderTypeIdEnum ce : CwbOrderTypeIdEnum.values()){if(ds.getCwbordertypeid()==ce.getValue()){ %>
									<%=ce.getText() %>
								<%}} %>
								</td>
								<td width="15%" align="center" valign="middle" customerKey="<%=ds.getCustomerid() %>"><%=StringUtil.nullConvertToEmptyString(ds.getCustomername()) %></td>
								<td width="15%" align="center" valign="middle" ><%=ds.getEmaildate() %></td>
								<td width="15%" align="left" valign="middle" ><%=ds.getConsigneeaddress() %></td>
								<td width="15%" align="center" podremarkKey="" ><%=StringUtil.nullConvertToEmptyString(ds.getBackreason()) %></td>
								<td width="15%" align="center"><%=ds.getDeliverstateremark() %></td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center">
								
								<a href="javascript:edit_button('<%=ds.getCwb()%>');">[修改]</a>
								<%if(isGuiBanUseZanBuChuLi.equals("yes")){ %>
								<%if(ds.getGcaid()==-1){ %>
									[<a id="sub_<%=ds.getCwb() %>" href="javascript:reSub('<%=ds.getCwb() %>',0,0,0,0,1);">恢复</a>]
								<%}else{ %>
									[<a id="sub_<%=ds.getCwb() %>" href="javascript:noSub('<%=ds.getCwb() %>',0,0,0,0,1);">暂不处理</a>]
								<%}} %>
								
								</td>
								<%} %>
							</tr>
							<%} %>
						</table></td>
				</tr>
				<%} %>
				
				
				
				<%if(dsDTO.getFankui_bufentuihuo()>0){ %>
				<tr>
					<td><input  type="checkbox"	value="bufentuihuo" id="bufentuihuo_box" /> 
					<span style="height: 25">部分拒收：<%=dsDTO.getFankui_bufentuihuo() %>单 </span></td>
					<td>&nbsp;</td>
				</tr>
				<tr id="bufentuihuo_list" style="display: none">
					<td colspan="2"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tr class="font_1">
								<td width="10%" height="38" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
								<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">收件地址</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">退货原因</td>
								<!-- <td width="10%" align="center" valign="middle" bgcolor="#eef6ff">退回货物</td> -->
								<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">应收金额</td>
								<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">实收金额</td>
								<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">收款方式</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">反馈备注</td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
								<%} %>
							</tr>
							<%for(DeliveryStateView ds:dsDTO.getFankui_bufentuihuoList()){ %>
							<tr id="<%=ds.getCwb() %>" keyName="bufentuihuo" gcaid="<%=ds.getGcaid() %>">
								<td width="10%" align="center" valign="middle"><a target="_blank" href="<%=request.getContextPath() %>/order/queckSelectOrder/<%=ds.getCwb()%>" ><%=ds.getCwb()+ds.isHistory() %></a></td>
								<td width="5%" align="center" valign="middle" >
								<%for(CwbOrderTypeIdEnum ce : CwbOrderTypeIdEnum.values()){if(ds.getCwbordertypeid()==ce.getValue()){ %>
									<%=ce.getText() %>
								<%}} %>
								</td>
								<td width="10%" align="center" valign="middle" customerKey="<%=ds.getCustomerid()%>"><%=StringUtil.nullConvertToEmptyString(ds.getCustomername()) %></td>
								<td width="15%" align="center" valign="middle" ><%=ds.getEmaildate() %></td>
								<td width="15%" align="left" valign="middle" ><%=ds.getConsigneeaddress() %></td>
								<td width="10%" align="center" podremarkKey="" ><%=StringUtil.nullConvertToEmptyString(ds.getBackreason()) %></td>
<%-- 								<td width="10%" align="center"><%=StringUtil.nullConvertToEmptyString(ds.getBackcarname()) %></td> --%>	
								<td width="5%" align="right"><%=ds.getBusinessfee()%>元</td>
								<td width="5%" align="right"><%=ds.getReceivedfee() %>元</td>
								<td width="5%" align="center"><%=ds.getPaymentPattern() %></td>
								<td width="10%" align="center"><%=ds.getDeliverstateremark() %></td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center">
								
									<!-- <a href="javascript:if(<%=ds.getUserid() %>==0||<%=ds.getUserid() %>==<%=usermap.get("userid") %>){edit_button('<%=ds.getCwb()%>');}else{alert('不是同一个操作人！');}">[修改]</a> -->
									<a href="javascript:edit_button('<%=ds.getCwb()%>');">[修改]</a>
								　<%if(isGuiBanUseZanBuChuLi.equals("yes")){ %>
								<%if(ds.getGcaid()==-1){ %>
									[<a id="sub_<%=ds.getCwb() %>" href="javascript:reSub('<%=ds.getCwb() %>',<%=ds.getReceivedfee() %>,<%=ds.getCash() %>,<%=ds.getPos() %>,0,1);">恢复</a>]
								<%}else{ %>
									[<a id="sub_<%=ds.getCwb() %>" href="javascript:noSub('<%=ds.getCwb() %>',<%=ds.getReceivedfee() %>,<%=ds.getCash() %>,<%=ds.getPos() %>,0,1);">暂不处理</a>]
								<%}} %>
								</td>
								<%} %>
							</tr>
							<%} %>
						</table></td>
				</tr>
				<%} %>
				
				
				<%if(dsDTO.getFankui_zhiliu()>0){ %>
				<tr>
					<td><input type="checkbox"	value="zhiliu" id="zhiliu_box" /> 
					<span style="height: 25" >滞留：<%=dsDTO.getFankui_zhiliu() %>单 </span></td>
					<td>&nbsp;</td>
				</tr>
				<tr id="zhiliu_list" style="display: none">
					<td colspan="2"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tr class="font_1">
								<td width="10%" height="38" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
								<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">收件人姓名</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">收件人电话</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">收件地址</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">滞留原因</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">反馈备注</td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
								<%} %>
							</tr>
							<%for(DeliveryStateView ds:dsDTO.getFankui_zhiliuList()){ %>
							<tr id="<%=ds.getCwb() %>" keyName="zhiliu" gcaid="<%=ds.getGcaid() %>">
								<td width="10%" align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath() %>/order/queckSelectOrder/<%=ds.getCwb()%>" ><%=ds.getCwb()+ds.isHistory() %></a></td>
								<td>
								<%for(CwbOrderTypeIdEnum ce : CwbOrderTypeIdEnum.values()){if(ds.getCwbordertypeid()==ce.getValue()){ %>
									<%=ce.getText() %>
								<%}} %>
								</td>
								<td width="10%" align="center" valign="middle" customerKey="<%=ds.getCustomerid() %>"><%=StringUtil.nullConvertToEmptyString(ds.getCustomername())  %></td>
								<td width="10%" align="center" valign="middle" ><%=ds.getConsigneename() %></td>
								<td width="10%" align="center" valign="middle" ><%=ds.getConsigneemobile() %><%if(ds.getConsigneemobile()!=""){%>/<%}%><%=ds.getConsigneephone() %></td>
								<td width="10%" align="center" valign="middle" ><%=ds.getEmaildate() %></td>
								<td width="15%" align="left" valign="middle" ><%=ds.getConsigneeaddress() %></td>
								<td width="10%" align="center" podremarkKey="" ><%=StringUtil.nullConvertToEmptyString(ds.getLeavedreason()) %></td>
								<td width="10%" align="center"><%=ds.getDeliverstateremark() %></td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center">
								
									<!-- <a href="javascript:if(<%=ds.getUserid() %>==0||<%=ds.getUserid() %>==<%=usermap.get("userid") %>){edit_button('<%=ds.getCwb()%>');}else{alert('不是同一个操作人！');}">[修改]</a> -->
									<a href="javascript:edit_button('<%=ds.getCwb()%>');">[修改]</a>
								<%if(isGuiBanUseZanBuChuLi.equals("yes")){ %>
								<%if(ds.getGcaid()==-1){ %>
									[<a id="sub_<%=ds.getCwb() %>" href="javascript:reSub('<%=ds.getCwb() %>',0,0,0,1,0);">恢复</a>]
								<%}else{ %>
									[<a id="sub_<%=ds.getCwb() %>" href="javascript:noSub('<%=ds.getCwb() %>',0,0,0,1,0);">暂不处理</a>]
								<%}} %>
								</td>
								<%} %>
							</tr>
							<%} %>
						</table></td>
				</tr>
				<%} %>
				
				
				
				
				
				<%if(dsDTO.getFankui_shangmentui_chenggong()>0){ %>
				<tr>
					<td><input type="checkbox"	value="shangmentui_chenggong" id="shangmentui_chenggong_box" /> 
					<span style="height: 25" >上门退成功：<%=dsDTO.getFankui_shangmentui_chenggong() %>单 </span></td>
					<td>&nbsp;</td>
				</tr>
				<tr id="shangmentui_chenggong_list" style="display: none" >
					<td colspan="2"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tr class="font_1">
								<td width="10%" height="38" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
								<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">收件地址</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">取回商品</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">应退金额</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">实退金额</td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
								<%} %>
							</tr>
							<%for(DeliveryStateView ds:dsDTO.getFankui_shangmentui_chenggongList()){ %>
							<tr id="<%=ds.getCwb() %>" keyName="shangmentui_chenggong" gcaid="<%=ds.getGcaid() %>">
								<td width="10%" align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath() %>/order/queckSelectOrder/<%=ds.getCwb()%>" ><%=ds.getCwb()+ds.isHistory() %></a></td>
								<td width="5%" align="center" valign="middle" >
								<%for(CwbOrderTypeIdEnum ce : CwbOrderTypeIdEnum.values()){if(ds.getCwbordertypeid()==ce.getValue()){ %>
									<%=ce.getText() %>
								<%}} %>
								</td>
								<td width="10%" align="center" valign="middle" customerKey="<%=ds.getCustomerid()%>"><%=StringUtil.nullConvertToEmptyString(ds.getCustomername())  %></td>
								<td width="15%" align="center" valign="middle" ><%=ds.getEmaildate() %></td>
								<td width="15%" align="left" valign="middle" ><%=ds.getConsigneeaddress() %></td>
								<td width="10%" align="center" valign="middle" ><%=ds.getBackcarname() %></td>
								<td width="10%" align="right"><%=ds.getBusinessfee() %>元</td>
								<td width="15%" align="right"><%=ds.getReturnedfee() %>元</td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center">			
									<a href="javascript:if(!<%=ds.getEditFlag() %>){alert('唯品会上门退成功的订单不允许进行反馈修改')}else if(<%=ds.getUserid() %>==0||<%=ds.getUserid() %>==<%=usermap.get("userid") %>){edit_button('<%=ds.getCwb()%>');}else{alert('不是同一个操作人！');}">[修改]</a> 
								　<!-- <a href="javascript:edit_button('<%=ds.getCwb()%>');">[修改]</a>-->								
								<%if(isGuiBanUseZanBuChuLi.equals("yes")){ %>
								<%if(ds.getGcaid()==-1){ %>
									[<a id="sub_<%=ds.getCwb() %>" href="javascript:reSub('<%=ds.getCwb() %>',-<%=ds.getReturnedfee() %>,-<%=ds.getReturnedfee() %>,0,0,1);">恢复</a>]
								<%}else{ %>
									[<a id="sub_<%=ds.getCwb() %>" href="javascript:noSub('<%=ds.getCwb() %>',-<%=ds.getReturnedfee() %>,-<%=ds.getReturnedfee() %>,0,0,1);">暂不处理</a>]
								<%}} %>
								</td>
								<%} %>
							</tr>
							<%} %>
						</table></td>
				</tr>
				<%} %>


				<%if(dsDTO.getFankui_shangmentui_jutui()>0){ %>
				<tr>
					<td><input type="checkbox"	value="shangmentui_jutui" id="shangmentui_jutui_box" /> 
					<span style="height: 25" >上门退拒退：<%=dsDTO.getFankui_shangmentui_jutui() %>单 </span></td>
					<td>&nbsp;</td>
				</tr>
				<tr id="shangmentui_jutui_list" style="display: none">
					<td colspan="2"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tr class="font_1">
								<td width="10%" height="38" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
								<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">收件地址</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">退货原因</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">反馈备注</td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
								<%} %>
							</tr>
							<%for(DeliveryStateView ds:dsDTO.getFankui_shangmentui_jutuiList()){ %>
							<tr id="<%=ds.getCwb() %>" keyName="shangmentui_jutui" gcaid="<%=ds.getGcaid() %>">
								<td width="10%" align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath() %>/order/queckSelectOrder/<%=ds.getCwb()%>" ><%=ds.getCwb()+ds.isHistory() %></a></td>
								<td width="5%" align="center" valign="middle" >
								<%for(CwbOrderTypeIdEnum ce : CwbOrderTypeIdEnum.values()){if(ds.getCwbordertypeid()==ce.getValue()){ %>
									<%=ce.getText() %>
								<%}} %>
								</td>
								<td width="15%" align="center" valign="middle" customerKey="<%=ds.getCustomerid() %>"><%=StringUtil.nullConvertToEmptyString(ds.getCustomername())  %></td>
								<td width="15%" align="center" valign="middle" ><%=ds.getEmaildate() %></td>
								<td width="15%" align="left" valign="middle" ><%=ds.getConsigneeaddress() %></td>
								<td width="15%" align="center" podremarkKey="" ><%=StringUtil.nullConvertToEmptyString(ds.getBackreason()) %></td>
								<td width="15%" align="center"><%=ds.getDeliverstateremark() %></td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center">
								
									<!-- <a href="javascript:if(<%=ds.getUserid() %>==0||<%=ds.getUserid() %>==<%=usermap.get("userid") %>){edit_button('<%=ds.getCwb()%>');}else{alert('不是同一个操作人！');}">[修改]</a> -->
								　<a href="javascript:edit_button('<%=ds.getCwb()%>');">[修改]</a>
								<%if(isGuiBanUseZanBuChuLi.equals("yes")){ %>
								<%if(ds.getGcaid()==-1){ %>
									[<a id="sub_<%=ds.getCwb() %>" href="javascript:reSub('<%=ds.getCwb() %>',0,0,0,0,0);">恢复</a>]
								<%}else{ %>
									[<a id="sub_<%=ds.getCwb() %>" href="javascript:noSub('<%=ds.getCwb() %>',0,0,0,0,0);">暂不处理</a>]
								<%}} %>
								</td>
								<%} %>
							</tr>
							<%} %>
						</table></td>
				</tr>
				<%} %>


				<%if(dsDTO.getFankui_shangmenhuan_chenggong()>0){ %>
				<tr>
					<td><input type="checkbox"	value="shangmenhuan_chenggong" id="shangmenhuan_chenggong_box" /> 
					<span style="height: 25" >上门换成功：<%=dsDTO.getFankui_shangmenhuan_chenggong() %>单 </span></td>
					<td>&nbsp;</td>
					
				</tr>
				<tr id="shangmenhuan_chenggong_list" style="display: none" >
					<td colspan="2"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tr class="font_1">
								<td width="10%" height="38" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
								<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
								<td width="10%"align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
								<td width="10%"align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">收件地址</td>
								<td width="8%"align="center" valign="middle" bgcolor="#eef6ff">发出商品</td>
								<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">取回商品</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">差异款金额</td>
								<td width="5%"  align="center" valign="middle" bgcolor="#eef6ff">实际金额</td>
								<td width="5%"  align="center" valign="middle" bgcolor="#eef6ff">收款方式</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">反馈备注</td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
								<%} %>
							</tr>
							<%for(DeliveryStateView ds:dsDTO.getFankui_shangmenhuan_chenggongList()){ %>
							<tr id="<%=ds.getCwb() %>" keyName="shangmenhuan_chenggong" gcaid="<%=ds.getGcaid() %>">
								<td width="10%" align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath() %>/order/queckSelectOrder/<%=ds.getCwb()%>" ><%=ds.getCwb()+ds.isHistory() %></a></td>
								<td width="4%" align="center" valign="middle" >
								<%for(CwbOrderTypeIdEnum ce : CwbOrderTypeIdEnum.values()){if(ds.getCwbordertypeid()==ce.getValue()){ %>
									<%=ce.getText() %>
								<%}} %>
								</td>
								<td width="10%" align="center" valign="middle" customerKey="<%=ds.getCustomerid()%>"><%=StringUtil.nullConvertToEmptyString(ds.getCustomername())  %></td>
								<td width="10%" align="center" valign="middle" ><%=ds.getEmaildate() %></td>
								<td width="10%" align="left" valign="middle" ><%=ds.getConsigneeaddress() %></td>
								<td width="8%" align="center" valign="middle" ><%=ds.getSendcarname() %></td>
								<td width="8%" align="center" valign="middle" ><%=ds.getBackcarname() %></td>
								<td width="10%" align="right"><%=ds.getBusinessfee() %>元</td>
								<td width="5%"  align="right"><%=ds.getDifference() %>元</td>
								<td width="5%"  align="center"><%=ds.getPaymentPattern() %></td>
								<td width="10%" align="center"><%=ds.getRemarks() %></td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center">
								
									<a href="javascript:if(<%=ds.getUserid() %>==0||<%=ds.getUserid() %>==<%=usermap.get("userid") %>){edit_button('<%=ds.getCwb()%>');}else{alert('不是同一个操作人！');}">[修改]</a> 
									<!-- <a href="javascript:edit_button('<%=ds.getCwb()%>');">[修改]</a>-->
								<%if(isGuiBanUseZanBuChuLi.equals("yes")){ %>
								<%if(ds.getGcaid()==-1){ %>
									[<a id="sub_<%=ds.getCwb() %>" href="javascript:reSub('<%=ds.getCwb() %>',<%=ds.getReceivedfee().subtract(ds.getReturnedfee()) %>,<%=ds.getCash().subtract(ds.getReturnedfee()) %>,<%=ds.getPos() %>,0,1);">恢复</a>]
								<%}else{ %>
									[<a id="sub_<%=ds.getCwb() %>" href="javascript:noSub('<%=ds.getCwb() %>',<%=ds.getReceivedfee().subtract(ds.getReturnedfee()) %>,<%=ds.getCash().subtract(ds.getReturnedfee()) %>,<%=ds.getPos() %>,0,1);">暂不处理</a>]
								<%}} %>
								</td>
								<%} %>
							</tr>
							<%} %>
						</table></td>
				</tr>
				<%} %>

				
				<%if(dsDTO.getFankui_diushi()>0){ %>
				<tr>
					<td><input type="checkbox"	value="diushi" id="diushi_box" /> 
					<span style="height: 25" >丢失：<%=dsDTO.getFankui_diushi() %>单 </span></td>
					<td>&nbsp;</td>
					
				</tr>
				<tr id="diushi_list" style="display: none" >
					<td colspan="2"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tr class="font_1">
								<td width="10%" height="38" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
								<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">收件地址</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">责任人</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">赔偿金额</td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
								<%} %>
							</tr>
							<%for(DeliveryStateView ds:dsDTO.getFankui_diushiList()){ %>
							<tr id="<%=ds.getCwb() %>" keyName="diushi" gcaid="<%=ds.getGcaid() %>">
								<td width="10%" align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath() %>/order/queckSelectOrder/<%=ds.getCwb()%>" ><%=ds.getCwb()+ds.isHistory() %></a></td>
								<td width="5%" align="center" valign="middle" >
								<%for(CwbOrderTypeIdEnum ce : CwbOrderTypeIdEnum.values()){if(ds.getCwbordertypeid()==ce.getValue()){ %>
									<%=ce.getText() %>
								<%}} %>
								</td>
								<td width="15%" align="center" valign="middle" customerKey="<%=ds.getCustomerid() %>"><%=StringUtil.nullConvertToEmptyString(ds.getCustomername())   %></td>
								<td width="15%" align="center" valign="middle" ><%=ds.getEmaildate() %></td>
								<td width="15%" align="left" valign="middle" ><%=ds.getConsigneeaddress() %></td>
								<td width="15%" align="center"><%=ds.getDeliverealname() %></td>
								<td width="15%" align="right"><%=ds.getBusinessfee() %>元</td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center">
								
									<!-- <a href="javascript:if(<%=ds.getUserid() %>==0||<%=ds.getUserid() %>==<%=usermap.get("userid") %>){edit_button('<%=ds.getCwb()%>');}else{alert('不是同一个操作人！');}">[修改]</a> -->
									<a href="javascript:edit_button('<%=ds.getCwb()%>');">[修改]</a>
								<%if(isGuiBanUseZanBuChuLi.equals("yes")){ %>
								<%if(ds.getGcaid()==-1){ %>
									[<a id="sub_<%=ds.getCwb() %>" href="javascript:reSub('<%=ds.getCwb() %>',<%=ds.getBusinessfee() %>,<%=ds.getCash() %>,0,0,0);">恢复</a>]
								<%}else{ %>
									[<a id="sub_<%=ds.getCwb() %>" href="javascript:noSub('<%=ds.getCwb() %>',<%=ds.getBusinessfee() %>,<%=ds.getCash() %>,0,0,0);">暂不处理</a>]
								<%}} %>
								</td>
								<%} %>
							</tr>
							<%} %>
						</table></td>
				</tr>
				<%} %>
				
				
				
				
					<%if(dsDTO.getFankui_zhongzhuan()>0){ %>
				<tr>
					<td><input type="checkbox"	value="zhongzhuan" id="zhongzhuan_box" /> 
					<span style="height: 25" >待中转：<%=dsDTO.getFankui_zhongzhuan() %>单 </span></td>
					<td>&nbsp;</td>
				</tr>
				<tr id="zhongzhuan_list" style="display: none">
					<td colspan="2"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tr class="font_1">
								<td width="10%" height="38" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
								<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">收件人姓名</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">收件人电话</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">收件地址</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">中转原因</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">反馈备注</td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
								<%} %>
							</tr>
							<%for(DeliveryStateView ds:dsDTO.getFankui_zhongzhuanList()){ %>
							<tr id="<%=ds.getCwb() %>" keyName="zhongzhuan" gcaid="<%=ds.getGcaid() %>">
								<td width="10%" align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath() %>/order/queckSelectOrder/<%=ds.getCwb()%>" ><%=ds.getCwb()+ds.isHistory() %></a></td>
								<td>
								<%for(CwbOrderTypeIdEnum ce : CwbOrderTypeIdEnum.values()){if(ds.getCwbordertypeid()==ce.getValue()){ %>
									<%=ce.getText() %>
								<%}} %>
								</td>
								<td width="10%" align="center" valign="middle" customerKey="<%=ds.getCustomerid() %>"><%=StringUtil.nullConvertToEmptyString(ds.getCustomername())  %></td>
								<td width="10%" align="center" valign="middle" ><%=ds.getConsigneename() %></td>
								<td width="10%" align="center" valign="middle" ><%=ds.getConsigneemobile() %><%if(ds.getConsigneemobile()!=""){%>/<%}%><%=ds.getConsigneephone() %></td>
								<td width="10%" align="center" valign="middle" ><%=ds.getEmaildate() %></td>
								<td width="15%" align="left" valign="middle" ><%=ds.getConsigneeaddress() %></td>
								<td width="10%" align="center" valign="middle" ><%=StringUtil.nullConvertToEmptyString(ds.getChangereason()) %></td>
								<td width="10%" align="center"><%=ds.getDeliverstateremark() %></td>
								<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
								<td width="10%" align="center">
								
									<!-- <a href="javascript:if(<%=ds.getUserid() %>==0||<%=ds.getUserid() %>==<%=usermap.get("userid") %>){edit_button('<%=ds.getCwb()%>');}else{alert('不是同一个操作人！');}">[修改]</a> -->
									<a href="javascript:edit_button('<%=ds.getCwb()%>');">[修改]</a>
								<%if(isGuiBanUseZanBuChuLi.equals("yes")){ %>
								<%if(ds.getGcaid()==-1){ %>
									[<a id="sub_<%=ds.getCwb() %>" href="javascript:reSub('<%=ds.getCwb() %>',0,0,0,1,0);">恢复</a>]
								<%}else{ %>
									[<a id="sub_<%=ds.getCwb() %>" href="javascript:noSub('<%=ds.getCwb() %>',0,0,0,1,0);">暂不处理</a>]
								<%}} %>
								</td>
								<%} %>
							</tr>
							<%} %>
						</table></td>
				</tr>
				<%} %>
				
				
				

				<tr>
					<td><strong>未反馈订单：<%=dsDTO.getWeifankuiNumber() %>单</strong><%if(dsDTO.getWeifankuiNumber()>0){ %>  <input type="button" class="input_button2" value="导出"  onclick="exportWeifankui();" id="exportweifankui" /><%} %></td>
					<td id="wfk">
						<span id="wfkcwb" style="height: 25">请扫描反馈：  <input id="weifankui_C" type="text"/></span>
						<input id="isshow" type="checkbox" onclick="isshowpl()"/><font style="font-size: 22px;font-weight: bold;color: red;" >批量反馈</font>
					</td>
				</tr>
				<tr id="plfk" style="display: none;" bgcolor="edefad">
					<td colspan="2">批量反馈：
						<select name="podresultid_p" id="podresultid_p" onchange="checkpodresultid($(this).val())">
							<option value="0">请选择</option>
							<option value="<%=DeliveryStateEnum.PeiSongChengGong.getValue() %>">配送成功</option>
							<option value="<%=DeliveryStateEnum.JuShou.getValue()%>">拒收</option>
							<option value="<%=DeliveryStateEnum.FenZhanZhiLiu.getValue()%>">分站滞留</option>
						</select>
						<select id="paywayid_p" name="paywayid_p" style="display: none;">
							<option value="0">请选择</option>
							<option value="1">现金</option>
							<%if(pl_switch.getState()!=null&&pl_switch.getState().equals(SwitchEnum.PiLiangFanKuiPOS.getInfo())){ %>
								<option value="2">POS</option>
							<%} %>
							<option value="3">支票</option>
							<%if(showposandqita.equals("yes")){ %>
								<option value="4">其他</option>
							<%} %>
				        </select>
				        <select id="backreasonid_p" name="backreasonid_p" style="display: none;">
							<option value="0" selected>退货原因</option>
							<%for(Reason r1 : returnlist){ %>
								<option value="<%=r1.getReasonid() %>" ><%=r1.getReasoncontent() %></option>
							<%} %>
				        </select>
				        <select name="firstlevelreasonid" id="firstlevelreasonid"  onchange="getSecondReasonByFirstreasonid('<%=request.getContextPath()%>/reason/getSecondreason','leavedreasonid',this.value)"  >
				        	<option value ="0" selected>==滞留一级原因==</option>
				        	<%
				        	for(Reason r :staylist){
				        		if(r.getWhichreason()!=1){
				        			continue;
				        		}
				        	%>
				        		<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent() %></option>
				        	<%}%>
				        </select>
				        <select name="leavedreasonid" id="leavedreasonid">
				        	<option value ="0">==滞留二级原因==</option>
				        </select>
				        <span id="content" style="display: none;">反馈备注输入内容：<input type="text" name="deliverstateremark" id="deliverstateremark" value ="" maxlength="50"></span>
				    	请扫描反馈： <span style="height: 25"> <input id="scancwb_p" name="scancwb_p" value="" onkeydown='if(event.keyCode==13&&$("#scancwb_p").val().length>0){deliverpod("<%=request.getContextPath()%>",$("#deliveryid").val(),$("#scancwb_p").val(),$("#podresultid_p").val(),$("#paywayid_p").val(),$("#backreasonid_p").val(),$("#leavedreasonid").val(),$("#firstlevelreasonid").val(),$("#deliverstateremark").val(),"N","<%=isReasonRequired %>",<%=DeliveryStateEnum.JuShou.getValue()%>,<%=DeliveryStateEnum.FenZhanZhiLiu.getValue()%>);}'/><font color="red">（批量反馈的操作只针对正常配送的订单）</font></span>
				    	
				    	<input id="flashpage" style="display: none;" type="button" value="完成" onclick="getDeliveryStateByDeliveryId();"/>
					</td>
				</tr>
				<tr id="weifankui">
					<td colspan="2"><table width="100%" border="0" cellspacing="1"
							cellpadding="0" class="table_2" id="gd_table">
							<tr class="font_1">
								<td width="10%" height="38" align="center" valign="middle"	bgcolor="#eef6ff">订单号</td>
								<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">收件人姓名</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">收件人电话</td>
								<td width="12%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
								<td width="13%" align="center" valign="middle" bgcolor="#eef6ff">领货时间</td>
								<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">收件地址</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">应处理金额</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
							</tr>
							<%for(DeliveryStateView ds:dsDTO.getWeifankuiList()){ %>
							<tr id="<%=ds.getCwb() %>" keyName="weifankui">
								<td width="10%" align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath() %>/order/queckSelectOrder/<%=ds.getCwb()%>"  ><%=ds.getCwb()+ds.isHistory() %></a></td>
								<td width="5%" align="center" valign="middle" >
								<%for(CwbOrderTypeIdEnum ce : CwbOrderTypeIdEnum.values()){if(ds.getCwbordertypeid()==ce.getValue()){ %>
									<%=ce.getText() %>
								<%}} %>
								</td>
								<td width="10%" align="center" valign="middle" customerKey="<%=ds.getCustomerid() %>"><%=StringUtil.nullConvertToEmptyString(ds.getCustomername()) %></td>
								<td width="10%" align="center" valign="middle" ><%=ds.getConsigneename() %></td>
								<td width="10%" align="center" valign="middle" ><%=ds.getConsigneemobile() %><%if(ds.getConsigneemobile()!=""){%>/<%}%><%=ds.getConsigneephone() %></td>
								<td width="12%" align="center" valign="middle" ><%=ds.getEmaildate() %></td>
								<td width="13%" align="center" valign="middle" ><%=ds.getCreatetime() %></td>
								<td width="15%" align="left" valign="middle" ><%=ds.getConsigneeaddress() %></td>
								<td width="10%" align="right"><%=ds.getBusinessfee() %>元</td>
								<td id="<%=ds.getCwb() %>_cz" width="10%" align="center">
								<a href="javascript:edit_button('<%=ds.getCwb()%>');">[反馈]</a>　
								</td>
							</tr>
							<%} %>
						</table></td>
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
			<%if(!"no".equals(request.getAttribute("useAudit"))){ %>
			<div class="form_btnbox" >
			<table width="100%" height="15" border="0" cellpadding="0"
				cellspacing="5" class="right_set1">
				<tr>
					<td colspan="2" align="center">
					合计订单：<label id="subNumber"><%=dsDTO.getSubNumber() %></label>单，
					总计收款：<label id="totalAmount"><%=dsDTO.getTotal() %></label>元，
					现金收款：<label id="cashAmount"><%=dsDTO.getCash_amount() %></label>元，
					POS收款：<label id="posAmount"><%=dsDTO.getPos_amount() %></label>元，
					支付宝COD扫码收款：<label id="posAmount"><%=dsDTO.getCodpos_amount() %></label>元，
					滞留：<label id="zhiliuNumber"><%=dsDTO.getFankui_zhiliu()-dsDTO.getFankui_zhiliu_zanbuchuli() %></label>单，
					退货：<label id="tuihuoNumber"><%=dsDTO.getTuihuoAllNumber() %></label>单</td>
				</tr>
			</table>
			<%//当前登录用户为小件员隐藏按钮
				if("yes".equals(deliveryxiaojianyuan)){%>
					<input name="button35" type="button" id="subButton" class="button" value="确认审核" />
			<%	} %>
			</div>
			<%} else{%>
			<div class="form_btnbox2" >
			<table width="100%" height="15" border="0" cellpadding="0"
				cellspacing="5" class="right_set1">
				<tr>
					<td colspan="2" align="center">
					合计订单：<label id="subNumber"><%=dsDTO.getSubNumber() %></label>单，
					总计收款：<label id="totalAmount"><%=dsDTO.getTotal() %></label>元，
					现金收款：<label id="cashAmount"><%=dsDTO.getCash_amount() %></label>元，
					POS收款：<label id="posAmount"><%=dsDTO.getPos_amount() %></label>元，
					支付宝COD扫码收款：<label id="codposAmount"><%=dsDTO.getCodpos_amount() %></label>元，
					滞留：<label id="zhiliuNumber"><%=dsDTO.getFankui_zhiliu()-dsDTO.getFankui_zhiliu_zanbuchuli() %></label>单，
					退货：<label id="tuihuoNumber"><%=dsDTO.getTuihuoAllNumber() %></label>单</td>
				</tr>
			</table>
			</div>
			<%} %>
			<%} %>
			
		</form>

	</div>

<input type="hidden" id="edit" value="<%=request.getContextPath()%>/delivery/getnowDeliveryState/" />

   <div id="dlg-signimg" class="easyui-dialog" style="padding:5px;width:600px;height:400px;" closed="true"
            title="电子签名图片" iconCls="icon-ok" resizable="true"
             buttons="#dlg-buttons-signimg">
        <img id="signimg" src="" alt="" />
    </div>

    <div id="dlg-buttons-signimg">
        <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg-signimg').dialog('close')">关闭</a>
    </div>
 </body>
</html>
