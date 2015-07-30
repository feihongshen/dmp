<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page
	import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch,cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
    //今日待揽收
	List<CwbDetailView> todayToTakeList = (List<CwbDetailView>)request.getAttribute("todayToTakeList");
    //今日已揽收
	List<CwbDetailView> todayTakedList = (List<CwbDetailView>)request.getAttribute("todayTakedList");
	List<User> deliverList = (List<User>)request.getAttribute("deliverList");
	String did=request.getParameter("deliverid")==null?"0":request.getParameter("deliverid");
	long deliverid=Long.parseLong(did);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>揽件分配/调整</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/express/takeExpressAssign.js"></script>
<script type="text/javascript">
$(function(){
	var $menuli = $(".kfsh_tabbtn ul li");
	var $menulilink = $(".kfsh_tabbtn ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
})

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
})

$(function(){
	getExpressCount("<%=request.getContextPath() %>",$("#deliverid").val());
	 $("#preOrderNo").focus();
});

function tabView(tab){
	$("#"+tab).click();
}

function addAndRemoval(cwb,tab,isRemoval){
	var trObj = $("#ViewList tr[id='TR"+cwb+"']");
	if(isRemoval){
		$("#"+tab).append(trObj);
	}else{
		$("#ViewList #errorTable tr[id='TR"+cwb+"error']").remove();
		trObj.clone(true).appendTo("#"+tab);
		$("#ViewList #errorTable tr[id='TR"+cwb+"']").attr("id",trObj.attr("id")+"error");
	}
}


/**
 * 小件员领货扫描
 */
 var deliverStr=[];
 
<%for(User deliver : deliverList){ %>
	deliverStr[<%=deliver.getUserid()%>]="";
<%}%>
function assign(path,preOrderNo,deliverid){
	if(deliverid==-1){
		$("#msg").html("扫描前请选择小件员");
		return ;
	}else if(preOrderNo.length>0){
		var allnum = 0;
		
		$.ajax({
			type: "POST",
			url:path+"/stationOperation/assign/"+preOrderNo+"?deliverid="+deliverid,
			dataType:"json",
			success : function(data) {
				$("#preOrderNo").val("");
				$("#pagemsg").html("");
				//var linghuoSuccessCount = deliverStr[deliverid].split(",").length-1;
				if(data.statuscode=="000000"){
					$("#msg").html(preOrderNo+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
					
					$("#scansuccesscwb").val(preOrderNo);
					$("#showcwb").html("预订单编号："+preOrderNo);
					$("#consigneeaddress").html("地 址："+data.body.cwbOrder.consigneeaddress);
					if(data.body.cwbOrder.customercommand.indexOf('预约')>=0&&data.yuyuedaService=='yes')
					{	
						$("#customercommand").html("预约派送");
					}
					else{$("#customercommand").html("");}
					if(data.body.cwbOrder.cwbordertypeid==<%=CwbOrderTypeIdEnum.Peisong.getValue()%>){
						$("#cwbordertype").html("订单类型：配送订单");
						$("#fee").html("应收金额："+data.body.cwbOrder.receivablefee);
					}else if(data.body.cwbOrder.cwbordertypeid==<%=CwbOrderTypeIdEnum.Shangmenhuan.getValue()%>){
						$("#cwbordertype").html("订单类型：上门换订单");
						$("#fee").html("应收金额："+data.body.cwbOrder.receivablefee);
					}else if(data.body.cwbOrder.cwbordertypeid==<%=CwbOrderTypeIdEnum.Shangmentui.getValue()%>){
						$("#cwbordertype").html("订单类型：上门退订单");
						$("#fee").html("应退金额："+data.body.cwbOrder.paybackfee);
					}
					if(data.body.showRemark!=null){
					$("#cwbDetailshow").html("订单备注："+data.body.showRemark);
					}
					$("#exceldeliverid").html(data.body.cwbdelivername);
					$("#deliver").html("已领货（"+data.body.cwbdelivername+"）");
				}else{
					$("#exceldeliverid").html("");
					$("#showcwb").html("");
					$("#consigneeaddress").html("");
					$("#cwbordertype").html("");
					$("#cwbDetailshow").html("");
					$("#deliver").html("已领货");
					$("#fee").html("");
					$("#customercommand").html("");
					$("#msg").html(preOrderNo+"         （异常扫描）"+data.errorinfo);
					addAndRemoval(preOrderNo,"errorTable",false);
				}
				$("#responsebatchno").val(data.responsebatchno);
				batchPlayWav(data.wavList);
			}
		});
	}
}
function exportField(flag,deliverid){
	var cwbs = "";
	if(flag==1){
		$("#type").val(flag);
		$("#searchForm3").submit();
	}else if(flag==2){
		$("#type").val(flag);
		$("#searchForm3").submit();
	}else if(flag==3){//修改导出问题
			$("#errorTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
			if(cwbs.length>0){
				cwbs = cwbs.substring(0, cwbs.length-1);
			}
			if(cwbs!=""){
				$("#cwbs").val(cwbs);
				$("#btnval").attr("disabled","disabled");
			 	$("#btnval").val("请稍后……");
			 	$("#searchForm2").submit();
			}else{
				alert("没有订单信息，不能导出！");
			};
	}else if(flag==4){
		$("#type").val(flag);
		$("#searchForm3").submit();
	}
}

var todayToTakePage=1;var todayTakedPage=1;var yipage=1;
//查看更多今日待揽收
function moreTodayToTake(){
	todayToTakePage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/stationOperation/getMoreTodayToTakeList",
		data:{"page":todayToTakePage,"deliverid":$("#deliverid").val()},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' nextbranchid='"+data[i].nextbranchid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
					+ "</tr>";
				}
				$("#moreTodayToTake").remove();
				$("#todayToTakeTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
					var more='<tr align="center"  ><td  colspan="6" style="cursor:pointer" onclick="moreTodayToTake();" id="moreTodayToTake">查看更多</td></tr>';
				$("#todayToTakeTable").append(more);
				}
			}
		}
	});
	
	
}

function  moreTodayTaked(){
	todayTakedPage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/stationOperation/getMoreTodayTakedList",
		data:{"page":todayTakedPage,"deliverid":$("#deliverid").val()},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' deliverid='"+data[i].deliverid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
					+ "</tr>";
				}
				$("#moreTodayTaked").remove();
				$("#todayTakedTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
					var more='<tr align="center"  ><td  colspan="6" style="cursor:pointer" onclick="moreTodayTaked();" id="moreTodayTaked">查看更多</td></tr>';
				$("#todayTakedTable").append(more);
				}
			}
		}
	});
	
	
}  

function tohome(){
	window.location.href="<%=request.getContextPath()%>/stationOperation/takeExpressAssign?deliverid="
				+ $("#deliverid").val();
	}
</script>
</head>
<body style="background: #f5f5f5" marginwidth="0" marginheight="0">
	<div class="saomiao_box2">

		<div class="saomiao_tab2">
			<ul>
				<li><a href="#" class="light">逐单操作</a></li>
				<li><a href="<%=request.getContextPath()%>/stationOperation/takeExpressAssignBatch">批量操作</a></li>
			</ul>
		</div>


		<div class="saomiao_topnum2">
			<dl class="blue">
				<dt>今日待揽收</dt>
				<dd style="cursor: pointer" onclick="tabView('table_todayToTake')" id="todayToTakeCount">0</dd>
			</dl>
			<dl class="yellow">
				<dt>今日已揽收</dt>
				<dd style="cursor: pointer" onclick="tabView('table_todayTaked')"
					id="todayTakedCount">0</dd>
			</dl>
			<input type="button" id="refresh" value="刷新"
				onclick="location.href='<%=request.getContextPath()%>/stationOperation/takeExpressAssign'"
				style="float: left; width: 100px; height: 65px; cursor: pointer; border: none; background: url(../images/buttonbgimg1.gif) no-repeat; font-size: 18px; font-family: '微软雅黑', '黑体'" />
			<br clear="all">
		</div>

		<div class="saomiao_info2">
			<div class="saomiao_inbox2">
				<div class="saomiao_righttitle" id="pagemsg"></div>
				<div class="saomiao_selet2">
					小件员： <select id="deliverid" name="deliverid" onchange="tohome();" class="select1">
						<option value="-1" selected>请选择</option>
						<%
							for (User deliver : deliverList) {
						%>
						<option value="<%=deliver.getUserid()%>" <%if (deliverid == deliver.getUserid()) {%> selected=selected
							<%}%>><%=deliver.getRealname()%></option>
						<%
							}
						%>
					</select>*
				</div>
				<div class="saomiao_inwrith2">
					<div class="saomiao_left2">
						<p>
							<span>预订单编号：</span> <input type="text" class="saomiao_inputtxt2" id="preOrderNo" name="preOrderNo"
								value=""
								onKeyDown='if(event.keyCode==13&&$(this).val().length>0){assign("<%=request.getContextPath()%>",$(this).val(),$("#deliverid").val());}' />
						</p>
					</div>
					<div class="saomiao_right2">
						<p id="msg" name="msg"></p>
						<p id="cwbordertype" name="cwbordertype"></p>
						<p id="showcwb" name="showcwb"></p>
						<p id="cwbgaojia" name="cwbgaojia" style="display: none">高价</p>
						<p id="consigneeaddress" name="consigneeaddress"></p>
						<p id="fee" name="fee"></p>
						<p id="exceldeliverid" name="exceldeliverid"></p>
						<p id="cwbDetailshow" name="cwbDetailshow"></p>
						<p id="customercommand" name="customercommand"></p>
						<div style="display: none" id="EMBED"></div>
						<div style="display: none">
							<EMBED id='ypdj' name='ypdj'
								SRC='<%=request.getContextPath()%><%=ServiceUtil.waverrorPath%><%=CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl()%>'
								LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
						</div>
						<div style="display: none" id="errorvedio"></div>
					</div>
					<input type="hidden" id="requestbatchno" name="requestbatchno" value="0" /> 
					<input type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" />
				</div>
			</div>
		</div>
		<div>
			<div class="saomiao_tab2">
				<span style="float: right; padding: 10px"> <input class="input_button1" type="button"
					name="littlefalshbutton" id="flash" value="刷新"
					onclick="location.href='<%=request.getContextPath()%>/stationOperation/takeExpressAssign'" />
				</span>
				<ul id="smallTag">
					<li><a id="table_todayToTake" href="#" class="light">今日待揽收明细</a></li>
					<li><a id="table_todayTaked" href="#">今日已揽收明细</a></li>
				</ul>
			</div>
			<div id="ViewList" class="tabbox">
				<li><input type="button" id="btnval0" value="导出Excel" class="input_button1"
					onclick='exportField(1,$("#deliverid").val());' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
										<tr>
											<td width="50" align="center" bgcolor="#f1f1f1">预订单编号</td>
											<td width="50" align="center" bgcolor="#f1f1f1">寄件人</td>
											<td width="50" align="center" bgcolor="#f1f1f1">手机号</td>
											<td width="50" align="center" bgcolor="#f1f1f1">固话</td>
											<td width="100" align="center" bgcolor="#f1f1f1">取件地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="todayToTakeTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%
												if (todayToTakeList != null && !todayToTakeList.isEmpty())
													for (CwbDetailView todayToTake : todayToTakeList) {
											%>
											<tr id="TR<%=todayToTake.getCwb()%>" cwb="<%=todayToTake.getCwb()%>"
												customerid="<%=todayToTake.getCustomerid()%>" deliverid="<%=todayToTake.getDeliverid()%>">
												<td width="120" align="center"><%=todayToTake.getCwb()%></td>
												<td width="140"><%=todayToTake.getInSitetime()%></td>
												<td width="100"><%=todayToTake.getConsigneename()%></td>
												<td width="100"><%=todayToTake.getReceivablefee().doubleValue()%></td>
												<td align="left"><%=todayToTake.getConsigneeaddress()%></td>
											</tr>
											<%
												}
											%>
											<%
												if (todayToTakeList != null && todayToTakeList.size() == Page.DETAIL_PAGE_NUMBER) {
											%>
											<tr align="center">
												<td colspan="6" style="cursor: pointer"
													onclick="moreTodayToTake();" id="moreTodayToTake">查看更多</td>
											</tr>
											<%
												}
											%>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table></li>
				<li style="display: none"><input type="button" id="btnval0" value="导出Excel"
					class="input_button1" onclick='exportField(4,$("#deliverid").val());' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
										<tr>
											<td width="50" align="center" bgcolor="#f1f1f1">预订单编号</td>
											<td width="50" align="center" bgcolor="#f1f1f1">寄件人</td>
											<td width="50" align="center" bgcolor="#f1f1f1">手机号</td>
											<td width="50" align="center" bgcolor="#f1f1f1">固话</td>
											<td width="100" align="center" bgcolor="#f1f1f1">取件地址</td>
											
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="todayTakedTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%
												if (todayTakedList != null && !todayTakedList.isEmpty())
													for (CwbDetailView todayTaked : todayTakedList) {
											%>
											<tr id="TR<%=todayTaked.getCwb()%>" cwb="<%=todayTaked.getCwb()%>"
												customerid="<%=todayTaked.getCustomerid()%>" deliverid="<%=todayTaked.getDeliverid()%>">
												<td width="120" align="center"><%=todayTaked.getCwb()%></td>
												<td width="140"><%=todayTaked.getInSitetime()%></td>
												<td width="100"><%=todayTaked.getConsigneename()%></td>
												<td width="100"><%=todayTaked.getReceivablefee().doubleValue()%></td>
												<td align="left"><%=todayTaked.getConsigneeaddress()%></td>
											</tr>
											<%
												}
											%>
											<%
												if (todayTakedList != null && todayTakedList.size() == Page.DETAIL_PAGE_NUMBER) {
											%>
											<tr align="center">
												<td colspan="6" style="cursor: pointer"
													onclick="moreTodayTaked();" id="moreTodayTaked">查看更多</td>
											</tr>
											<%
												}
											%>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table></li>
			</div>
		</div>
		<form action="<%=request.getContextPath()%>/PDA/exportExcle" method="post" id="searchForm2">
			<input type="hidden" name="cwbs" id="cwbs" value="" /> <input type="hidden" name="exportmould2"
				id="exportmould2" />
		</form>
		<form action="<%=request.getContextPath()%>/PDA/exportByDeliverid" method="post" id="searchForm3">
			<input type="hidden" name="deliverid"
				value="<%=request.getParameter("deliverid") == null ? "0" : request.getParameter("deliverid")%>"
				id="deliverid" /> <input type="hidden" name="type" value="" id="type" />
		</form>
	</div>
</body>
</html>