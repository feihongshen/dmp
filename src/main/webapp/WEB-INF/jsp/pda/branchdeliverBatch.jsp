<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import="cn.explink.enumutil.CwbFlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<User> uList = (List<User>)request.getAttribute("userList");
List<JSONObject> objList = (List<JSONObject>)request.getAttribute("objList");
List<CwbDetailView> todayweilinghuolist = (List<CwbDetailView>)request.getAttribute("todayweilinghuolist");
List<CwbDetailView> historyweilinghuolist = (List<CwbDetailView>)request.getAttribute("historyweilinghuolist");
List<CwbDetailView> yilinghuolist = (List<CwbDetailView>)request.getAttribute("yilinghuolist");
List<Customer> cList = (List<Customer>)request.getAttribute("customerlist");
String did=request.getParameter("deliverid")==null?"0":request.getParameter("deliverid");
long deliverid=Long.parseLong(did);
boolean showCustomerSign= request.getAttribute("showCustomerSign")==null?false:(Boolean)request.getAttribute("showCustomerSign");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>批量领货扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$(function(){
	var $menuli1 = $("#bigTag li");
	$menuli1.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
	});
	
	var $menuli2 = $("#smallTag li");
	$menuli2.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli2.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
	
	$("input[name='selectAll']").click(function() {
		var $this = $(this);
		var $li = $this.parents("li:first");
		if($this.is(":checked")) {
			$li.find("input[name='selectCwb']").attr("checked", true);
		} else {
			$li.find("input[name='selectCwb']").attr("checked", false);
		}
		copySelectCwb($li.find("input[name='selectCwb']:checked"));
	});
	
	$("input[name='selectCwb']").click(function() {
		var $this = $(this);
		var $li = $this.parents("li:first");
		if($this.is(":checked")) {
			var unSelectLen = $li.find("input[name='selectCwb']").not(":checked").length;
			if(unSelectLen == 0) {
				$li.find("input[name='selectAll']").attr("checked", true);
			}
		} else {
			$li.find("input[name='selectAll']").attr("checked", false);
		}
		copySelectCwb($li.find("input[name='selectCwb']:checked"));
	});
});

function copySelectCwb($selectCwb) {
	var cwbArray = new Array();
	$selectCwb.each(function() {
		cwbArray.push($(this).val());
	});
	$("#cwbs").val(cwbArray.join("\r\n"));
}
$(function(){
	getweilingdata($("#deliverid").val());
	$("#cwbs").focus();
})
function tabView(tab){
	$("#"+tab).click();
}
function sub(){
	if($("#deliverid").val()==-1){
		$("#msg").html("确定批量处理前请选择小件员");
		return false;
	}else if($.trim($("#cwbs").val()).length==0){
		$("#msg").html("确定批量处理前请输入订单号，多个订单用回车分割");
		return false;
	}
	$("#subButton").val("正在处理！请稍候...");
	$("#subButton").attr('disabled','disabled');
	$("#subForm").submit();
	
}

function exportField(flag,deliverid){
	var cwbs = "";
	if(flag==1){
		/* if(deliverid>0){
			$("#todayweilinghuoTable tr[deliverid='"+deliverid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}else{
			$("#todayweilinghuoTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		} */
		$("#type").val(flag);
		$("#searchForm3").submit();
	}else if(flag==2){
		/* if(deliverid>0){
			$("#successTable tr[deliverid='"+deliverid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}else{
			$("#successTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		} */
		$("#type").val(flag);
		$("#searchForm3").submit();
	}else if(flag==3){
			
			$("#errorTable tr[name='export']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		
		if(cwbs.length>0){
			cwbs = cwbs.substring(0, cwbs.length-1);
		}
		if(cwbs!=""){
			$("#exportcwbs").val(cwbs);
			$("#btnval").attr("disabled","disabled");
		 	$("#btnval").val("请稍后……");
		 	$("#searchForm2").submit();
		}else{
			alert("没有订单信息，不能导出！");
		};
	}else if(flag==4){
		/* if(deliverid>0){
			$("#historyweilinghuoTable tr[deliverid='"+deliverid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}else{
			$("#historyweilinghuoTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		} */
		$("#type").val(flag);
		$("#searchForm3").submit();
	}
}
//得到当前待领货的库存量
function getweilingdata(deliverid){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath() %>/PDA/getWeiLingHuoSum",
		data:{"deliverid":deliverid},
		dataType:"json",
		success : function(data) {
			$("#todayweilingdanshu").html(data.todayweilinghuocount);
			$("#historyweilingdanshu").html(data.historyweilinghuocount);
			$("#linghuoSuccessCount").html(data.yilinghuo);
		}
	});
}

var toweilingpage=1;var historyweilingpage=1;var yipage=1;
function todayweilinghuo(){
	toweilingpage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbranchideliverweilinglist",
		data:{"page":toweilingpage,"deliverid":$("#deliverid").val()},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					<%if(showCustomerSign){ %>
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' deliverid='"+data[i].deliverid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].inSitetime+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
					+"<td width='230' align='center'> "+data[i].consigneeaddress+"</td>"
					+"<td width='60' align='center'>"+data[i].cwbstatetext+"</td>"
					+"<td width='60' align='center'>"+data[i].flowordertypetext+"</td>"
					+"<td align='center'>"+data[i].checkstateresultname+"</td>"
					+ "</tr>";
				<%}else{ %>
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' deliverid='"+data[i].deliverid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].inSitetime+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td width='230' align='center'> "+data[i].consigneeaddress+"</td>"
					+"<td width='60' align='center'>"+data[i].cwbstatetext+"</td>"
					+"<td width='60' align='center'>"+data[i].flowordertypetext+"</td>"
					+"<td align='center'>"+data[i].checkstateresultname+"</td>"
					+ "</tr>";
				<%} %>
				}
				$("#todayweilinghuo").remove();
				$("#todayweilinghuoTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
					var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="todayweilinghuo();" id="todayweilinghuo">查看更多</td></tr>';
				$("#todayweilinghuoTable").append(more);
				}
			}
			
		}
	});
	
	
}

function  historyweilinghuo(){
	historyweilingpage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbranchideliverweilinghistorylist",
		data:{"page":historyweilingpage,"deliverid":$("#deliverid").val()},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					<%if(showCustomerSign){ %>
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' deliverid='"+data[i].deliverid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].inSitetime+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
					+"<td width='230' align='center'> "+data[i].consigneeaddress+"</td>"
					+"<td width='60' align='center'>"+data[i].cwbstatetext+"</td>"
					+"<td width='60' align='center'>"+data[i].flowordertypetext+"</td>"
					+"<td align='center'>"+data[i].checkstateresultname+"</td>"
					+ "</tr>";
				<%}else{ %>
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' deliverid='"+data[i].deliverid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].inSitetime+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td width='230' align='center'> "+data[i].consigneeaddress+"</td>"
					+"<td width='60' align='center'>"+data[i].cwbstatetext+"</td>"
					+"<td width='60' align='center'>"+data[i].flowordertypetext+"</td>"
					+"<td align='center'>"+data[i].checkstateresultname+"</td>"
					+ "</tr>";
				<%} %>
				}
				$("#historyweilinghuo").remove();
				$("#historyweilinghuoTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
					var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="historyweilinghuo();" id="historyweilinghuo">查看更多</td></tr>';
				$("#historyweilinghuoTable").append(more);
				}
			}
			
		}
	});
	
	
}  

function yiling(){
	yipage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbranchideliveryilinglist",
		data:{"page":yipage,"deliverid":$("#deliverid").val()},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					<%if(showCustomerSign){ %>
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' deliverid='"+data[i].deliverid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].pickGoodstime+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
					+"<td width='230' align='center'> "+data[i].consigneeaddress+"</td>"
					+"<td width='60' align='center'>"+data[i].cwbstatetext+"</td>"
					+"<td width='60' align='center'>"+data[i].flowordertypetext+"</td>"
					+"<td align='center'>"+data[i].checkstateresultname+"</td>"
					+ "</tr>";
				<%}else{ %>
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' deliverid='"+data[i].deliverid+"'>"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].pickGoodstime+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td width='230' align='center'> "+data[i].consigneeaddress+"</td>"
					+"<td width='60' align='center'>"+data[i].cwbstatetext+"</td>"
					+"<td width='60' align='center'>"+data[i].flowordertypetext+"</td>"
					+"<td align='center'>"+data[i].checkstateresultname+"</td>"
					+ "</tr>";
				<%} %>
				}
				$("#yiling").remove();
				$("#successTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
					var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="yiling();" id="yiling">查看更多</td></tr>';
				$("#successTable").append(more);
				}
			}
			
		}
	});
	
}

function tohome(){
	window.location.href="<%=request.getContextPath() %>/PDA/branchdeliverBatch?deliverid="+$("#deliverid").val();
}
</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="saomiao_box">


	<div class="saomiao_tab2">
		<ul>
			<li><a href="<%=request.getContextPath()%>/PDA/branchdeliverdetail" >逐单操作</a></li>	
			<li><a href="#" class="light" >批量操作</a></li>	
		</ul>
	</div>
 
 
	<div class="saomiao_topnum">
		<dl class="blue">
			<dt>今日待领货合计</dt>
			<dd style="cursor:pointer" onclick="tabView('table_todayweilinghuo')" id="todayweilingdanshu">${todayweilinghuocount }</dd>
		</dl>
		<dl class="yellow">
			<dt>历史待领货合计</dt>
			<dd style="cursor:pointer" onclick="tabView('table_historyweilinghuo')" id="historyweilingdanshu">${historyweilinghuocount }</dd>
		</dl>
		
		<dl class="green">
			<dt >本次已领货</dt>
			<dd style="cursor:pointer" onclick="tabView('table_yilinghuo')" id="linghuoSuccessCount">${yilinghuo}</dd>
		</dl>
		<br clear="all">
	</div>
		<input type="hidden" name="showCustomerSign" id="showCustomerSign" value="<%=showCustomerSign %>"/>
		<input type="hidden" name="clist" id="clist" value="<%=cList %>"/>
		<input type="hidden" name="alertErrorMsg" id="alertErrorMsg" value="${alertErrorMsg }"/>
		<input type="hidden" name="alertWarnMsg" id="alertWarnMsg" value="${alertWarnMsg }"/>
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="saomiao_righttitle2" id="pagemsg"></div>
			<form id="subForm" action="branchdeliverBatch" method="post">
			<div class=saomiao_inwrith2>
				<div class="saomiao_left2">
				<p><span>小件员：</span>
					<select id="deliverid" name="deliverid" onchange="tohome();" class="select1">
						<option value="-1" selected>请选择</option>
						<%for(User u : uList){ %>
							<option value="<%=u.getUserid() %>"  <%if(deliverid==u.getUserid()) {%>selected=selected<%} %> ><%=u.getRealname() %></option>
						<%} %>
			        </select>*
			               超区领货：<input type="checkbox" id="isChaoqu" name="isChaoqu"/>
				</p>
			    <p><span>订单号：</span>
					<textarea name="cwbs" cols="45" rows="3" id="cwbs"></textarea>
				</p>
				<span>&nbsp;</span><input type="button" id="subButton" value="确定批量处理" onclick="sub()" class="button" />
				</div>
				<div class="saomiao_right2">
					<p id="msg" name="msg" >${msg }</p>
				</div>
			</div>
			</form>
		</div>
	</div>
	<div>
	<div class="saomiao_tab2">
			<span style="float: right; padding: 10px"><!-- <input
				type="button" class="input_button2" value="导出" /> --></span>
			<ul id="smallTag">
				<li><a id="table_todayweilinghuo" href="#" class="light">未领货明细</a></li>
				<li><a id="table_historyweilinghuo" href="#">历史未领货</a></li>
				<li><a id="table_yilinghuo" href="#">已领货明细</a></li>
				<li><a href="#">异常单明细</a></li>
			</ul>
		</div>
		<div id="ViewList" class="tabbox">
			<li>
				<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(1,$("#deliverid").val());'/>
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr>
							<td width="10%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="2"
									class="table_5">
									<tr>
										<td width="30" align="center"><input name="selectAll" type="checkbox"/></td>
										<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
										<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
										<td width="140" align="center" bgcolor="#f1f1f1">到货时间</td>
										<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
										<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
										<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
										<td width="230" align="center" bgcolor="#f1f1f1">地址</td>
											<!-- hps_Concerto create 2016年5月25日11:57:40 -->
										<td width="60" align="center" bgcolor="#f1f1f1">订单状态</td>
										<td width="60" align="center" bgcolor="#f1f1f1">操作状态</td>
										<td align="center" bgcolor="#f1f1f1">退货出站审核结果</td>
										<!-- ******************************************** -->
									</tr>
								</table>
								<div style="height: 160px; overflow-y: scroll">
									<table id="todayweilinghuoTable" width="100%" border="0" cellspacing="1" cellpadding="2"
										class="table_2">
										<%if(todayweilinghuolist!=null&&todayweilinghuolist.size()>0)for(CwbDetailView co : todayweilinghuolist){ %>
										<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" deliverid="<%=co.getDeliverid()%>">
											<td width="30" align="center"><input name="selectCwb" type="checkbox" value="<%=co.getCwb() %>"/></td>
											<td width="120" align="center"><%=co.getCwb() %></td>
											<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
											<td width="140"><%=co.getInSitetime() %></td>
											<td width="100"><%=co.getConsigneename() %></td>
											<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
											<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView() %></td>
												<%} %>
											<td width="230" align="left"><%=co.getConsigneeaddress() %></td>
											<!-- hps_Concerto create 2016年5月25日11:57:40 -->
											<td width="60" align="center">
											<% for (CwbStateEnum  cwb   : CwbStateEnum.values()) {if (cwb.getValue()==co.getCwbstate()) {%>
											<%=cwb.getText()%>
											<% }}%>
											</td>
											<td width="60" align="center"><%
											if(CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText()=="已审核"){%>
											审核为：<%=DeliveryStateEnum.getByValue(co.getDeliverystate()).getText() %>
											<%}else if(CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText()=="已反馈") {%>
											反馈为：<%=DeliveryStateEnum.getByValue(co.getDeliverystate()).getText() %>
											<%}else{ %>
											<%=CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText()%><%} %></td>
											<td align="center"><%=co.getCheckstateresultname() %></td>
											<!-- ****************************** -->
										</tr>
										<%} %>
										<%if(todayweilinghuolist!=null&&todayweilinghuolist.size()==Page.DETAIL_PAGE_NUMBER){ %>
										<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="todayweilinghuo();" id="todayweilinghuo">查看更多</td></tr>
										<%} %>
									</table>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</li>
			<li style="display: none">
				<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(4,$("#deliverid").val());'/>
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr>
							<td width="10%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="2"
									class="table_5">
									<tr>
										<td width="30" align="center"><input name="selectAll" type="checkbox"/></td>
										<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
										<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
										<td width="140" align="center" bgcolor="#f1f1f1">到货时间</td>
										<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
										<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
										<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
										<td width="230" align="center" bgcolor="#f1f1f1">地址</td>
											<!-- hps_Concerto create 2016年5月25日11:57:40 -->
										<td width="60" align="center" bgcolor="#f1f1f1">订单状态</td>
										<td width="60" align="center" bgcolor="#f1f1f1">操作状态</td>
										<td align="center" bgcolor="#f1f1f1">退货出站审核结果</td>
										<!-- ******************************************** -->
									</tr>
								</table>
								<div style="height: 160px; overflow-y: scroll">
									<table id="historyweilinghuoTable" width="100%" border="0" cellspacing="1" cellpadding="2"
										class="table_2">
										<%if(historyweilinghuolist!=null&&historyweilinghuolist.size()>0)for(CwbDetailView co : historyweilinghuolist){ %>
										<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" deliverid="<%=co.getDeliverid()%>">
											<td width="30" align="center"><input name="selectCwb" type="checkbox" value="<%=co.getCwb() %>"/></td>
											<td width="120" align="center"><%=co.getCwb() %></td>
											<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
											<td width="140"><%=co.getInSitetime() %></td>
											<td width="100"><%=co.getConsigneename() %></td>
											<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
											<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView() %></td>
												<%} %>
											<td width="230" align="left"><%=co.getConsigneeaddress() %></td>
											<!-- hps_Concerto create 2016年5月25日11:57:40 -->
											<td width="60" align="center">
											<% for (CwbStateEnum  cwb   : CwbStateEnum.values()) {if (cwb.getValue()==co.getCwbstate()) {%>
											<%=cwb.getText()%>
											<% }}%>
											</td>
											<td width="60" align="center"><%
											if(CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText()=="已审核"){%>
											审核为：<%=DeliveryStateEnum.getByValue(co.getDeliverystate()).getText() %>
											<%}else if(CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText()=="已反馈") {%>
											反馈为：<%=DeliveryStateEnum.getByValue(co.getDeliverystate()).getText() %>
											<%}else{ %>
											<%=CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText()%><%} %></td>
											<td align="center"><%=co.getCheckstateresultname() %></td>
											<!-- ****************************** -->
										</tr>
										<%} %>
										<%if(historyweilinghuolist!=null&&historyweilinghuolist.size()==Page.DETAIL_PAGE_NUMBER){ %>
										<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="historyweilinghuo();" id="historyweilinghuo">查看更多</td></tr>
										<%} %>
									</table>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</li>
			<li style="display: none">
				<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(2,$("#deliverid").val());'/>
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr>
							<td width="10%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="2"
									class="table_5">
									<tr>
										<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
										<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
										<td width="140" align="center" bgcolor="#f1f1f1">领货时间</td>
										<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
										<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
										<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
										<td width="230" align="center" bgcolor="#f1f1f1">地址</td>
											<!-- hps_Concerto create 2016年5月25日11:57:40 -->
										<td width="60" align="center" bgcolor="#f1f1f1">订单状态</td>
										<td width="60" align="center" bgcolor="#f1f1f1">操作状态</td>
										<td align="center" bgcolor="#f1f1f1">退货出站审核结果</td>
										<!-- ******************************************** -->
									</tr>
								</table>
								<div style="height: 160px; overflow-y: scroll">
									<table id="successTable" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
									<%if(yilinghuolist!=null&&yilinghuolist.size()>0)for(CwbDetailView co : yilinghuolist){ %>
										<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" deliverid="<%=co.getDeliverid() %>">
											<td width="120" align="center"><%=co.getCwb() %></td>
											<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
											<td width="140"><%=co.getPickGoodstime() %></td>
											<td width="100"><%=co.getConsigneename() %></td>
											<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
											<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView()==null?"":co.getRemarkView() %></td>
												<%} %>
											<td width="230" align="left"><%=co.getConsigneeaddress() %></td>
											<!-- hps_Concerto create 2016年5月25日11:57:40 -->
											<td width="60" align="center">
											<% for (CwbStateEnum  cwb   : CwbStateEnum.values()) {if (cwb.getValue()==co.getCwbstate()) {%>
											<%=cwb.getText()%>
											<% }}%>
											</td>
											<td width="60" align="center"><%
											if(CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText()=="已审核"){%>
											审核为：<%=DeliveryStateEnum.getByValue(co.getDeliverystate()).getText() %>
											<%}else if(CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText()=="已反馈") {%>
											反馈为：<%=DeliveryStateEnum.getByValue(co.getDeliverystate()).getText() %>
											<%}else{ %>
											<%=CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText()%><%} %></td>
											<td align="center"><%=co.getCheckstateresultname() %></td>
											<!-- ****************************** -->
										</tr>
										<%} %>
										<%if(yilinghuolist!=null&&yilinghuolist.size()==Page.DETAIL_PAGE_NUMBER){ %>
										<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="yiling();" id="yiling">查看更多</td></tr>
										<%} %>
									</table>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</li>
			
			<li style="display: none">
				<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(3,$("#deliverid").val());'/>
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr>
							<td width="10%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="2"
									class="table_5">
									<tr>
										<td width="100" align="center" bgcolor="#f1f1f1">订单号</td>
										<td width="80" align="center" bgcolor="#f1f1f1">供货商</td>
										<td width="120" align="center" bgcolor="#f1f1f1">到货时间</td>
										<td width="120" align="center" bgcolor="#f1f1f1">批量处理操作时间</td>
										<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
										<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
										<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
										<td width="160" align="center" bgcolor="#f1f1f1">地址</td>
											<!-- hps_Concerto create 2016年5月25日11:57:40 -->
										<td width="60" align="center" bgcolor="#f1f1f1">订单状态</td>
										<td width="60" align="center" bgcolor="#f1f1f1">操作状态</td>
										<td width="120" align="center" bgcolor="#f1f1f1">退货出站审核结果</td>
										<!-- ******************************************** -->
										<td align="center" bgcolor="#f1f1f1">异常信息</td>
									</tr>
								</table>
								<div style="height: 160px; overflow-y: scroll">
									<table id="errorTable" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
										<%if(objList!=null)for(JSONObject obj : objList){if(!obj.get("errorcode").equals("000000")){ %>
											<%JSONObject cwbOrder =  obj.get("cwbOrder")==null?null:JSONObject.fromObject(obj.get("cwbOrder"));%>
											<tr id="TR<%=obj.get("cwb") %>"  <%if(!obj.get("errorinfo").equals("无此单号")){ %> name="export" <%} %>  cwb="<%=obj.get("cwb") %>" customerid="<%=cwbOrder==null?"":cwbOrder.getString("customerid") %>"  deliverid="<%=cwbOrder==null?"":cwbOrder.getString("deliverid")%>">
												<td width="100" align="center"><%=obj.get("cwb") %></td>
												<td width="80" align="center"><%=obj.getString("customername") %></td>
												<td width="120" align="center"><%=obj.getString("inSitetime") %></td>
												<td width="120" align="center"><%=obj.getString("createtime") %></td>
												<td width="100" align="center"><%=cwbOrder==null?"":cwbOrder.getString("consigneename") %></td>
												<td width="100" align="center"><%=cwbOrder==null?"":cwbOrder.getDouble("receivablefee") %></td>
												<%if(showCustomerSign){ %>
												<td width="100"><%=obj.get("showRemark")==null?"":obj.get("showRemark") %></td>
											<%} %>
												<td width="160" align="left"><%=cwbOrder==null?"":cwbOrder.getString("consigneeaddress") %></td>
												<!-- hps_Concerto create 2016年5月25日11:57:40 -->
											<td width="60" align="center">
											<% for (CwbStateEnum  cwb   : CwbStateEnum.values()) {if ((obj.get("cwbstate")!=null&&cwb.getValue()==(Integer)obj.get("cwbstate"))) {%>
											<%=cwb.getText()%>
											<% }}%>
											</td>
											<td width="60" align="center"><%
											if(obj.get("flowordertype")!=null){
											if(CwbFlowOrderTypeEnum.getText((Integer)obj.get("flowordertype")).getText()=="已审核"){%>
											审核为：<%=DeliveryStateEnum.getByValue((Integer)obj.get("deliverystate")).getText() %>
											<%}else if(CwbFlowOrderTypeEnum.getText((Integer)obj.get("flowordertype")).getText()=="已反馈") {%>
											反馈为：<%=DeliveryStateEnum.getByValue((Integer)obj.get("deliverystate")).getText() %>
											<%}else{ %>
											<%=CwbFlowOrderTypeEnum.getText((Integer)obj.get("flowordertype"))==null?"":CwbFlowOrderTypeEnum.getText((Integer)obj.get("flowordertype")).getText()%>
											<%}} %></td>
											<td width="120" align="center"><%=obj.get("checkstateresultname")==null?"":obj.get("checkstateresultname")%></td>
											<!-- ****************************** -->
												<td align="center"><%=obj.get("errorinfo") %></td>
											</tr>
											<%}} %>
									</table>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</li>
		</div>
	</div>
	<form action="<%=request.getContextPath()%>/PDA/exportExcle" method="post" id="searchForm2">
		<input type="hidden" name="cwbs" id="exportcwbs" value=""/>
		<input type="hidden" name="exportmould2" id="exportmould2" />
	
	</form> 
	<form action="<%=request.getContextPath() %>/PDA/exportByDeliverid" method="post" id="searchForm3">
		<input  type="hidden"  name="deliverid" value="<%=request.getParameter("deliverid")==null?"0":request.getParameter("deliverid") %>" id="deliverid" />
		<input type="hidden" name="type" value="" id="type"/>
	</form>
</div>
</body>

<script type="text/javascript">
$(function(){
	var alertErrorMsg = $("#alertErrorMsg").val();
	if (alertErrorMsg != null && alertErrorMsg.length > 0) {
		alert(alertErrorMsg);
	} else {
		var alertWarnMsg = $("#alertWarnMsg").val();
		if (alertWarnMsg != null && alertWarnMsg.length > 0) {
			alert(alertWarnMsg);
		}
	}
})
</script>
</html>

