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
	getExpressCount("<%=request.getContextPath()%>",$("#deliverid").val());
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
 
<%for(User deliver : deliverList){%>
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
					data : {
						"page" : todayTakedPage,
						"deliverid" : $("#deliverid").val()
					},
					success : function(data) {
						if (data.length > 0) {
							var optionstring = "";
							for (var i = 0; i < data.length; i++) {
								optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' deliverid='"+data[i].deliverid+"' >"
										+ "<td width='120' align='center'>"
										+ data[i].cwb
										+ "</td>"
										+ "<td width='100' align='center'> "
										+ data[i].customername
										+ "</td>"
										+ "<td width='140' align='center'> "
										+ data[i].emaildate
										+ "</td>"
										+ "<td width='100' align='center'> "
										+ data[i].consigneename
										+ "</td>"
										+ "<td width='100' align='center'> "
										+ data[i].receivablefee
										+ "</td>"
										+ "<td  align='left'> "
										+ data[i].consigneeaddress
										+ "</td>"
										+ "</tr>";
							}
							$("#moreTodayTaked").remove();
							$("#todayTakedTable").append(optionstring);
							if (data.length ==
<%=Page.DETAIL_PAGE_NUMBER%>
	) {
								var more = '<tr align="center"  ><td  colspan="6" style="cursor:pointer" onclick="moreTodayTaked();" id="moreTodayTaked">查看更多</td></tr>';
								$("#todayTakedTable").append(more);
							}
						}
					}
				});

	}
	
$(function() {
	$("#assign_button").click(function() {
		$.ajax({
			type : "POST",
			url : $("#assign").val(),
			dataType : "html",
			success : function(data) {
				$("#alert_box", parent.document).html(data);
				
			},
			complete : function() {
// 				addInit();// 初始化某些ajax弹出页面
				viewBox();
			}
		});
		
	});

});
$(function() {
	$("#superzone_button").click(function() {
		$.ajax({
			type : "POST",
			url : $("#superzone").val(),
			dataType : "html",
			success : function(data) {
				$("#alert_box", parent.document).html(data);
				
			},
			complete : function() {
// 				addInit();// 初始化某些ajax弹出页面
				viewBox();
			}
		});
		
	});

});
function viewBox() {
	$("#alert_box", parent.document).show();
	$("#dress_box", parent.document).css("visibility", "hidden");
	window.parent.centerBox();
}
	
</script>
</head>
<body style="background: #f5f5f5" marginwidth="0" marginheight="0">

	<div class="inputselect_box">
		<span><input name="" type="button" value="站点超区" class="input_button1" id="superzone_button" /> </span>
		<span><input name="" type="button" value="分配" class="input_button1" id="assign_button" /> </span>
		<form action="<%=request.getAttribute("page") == null ? "1" : request.getAttribute("page")%>"
			method="post" id="searchForm">
			分配情况：<select id="customerid" name="customerid" class="select1">
				<option value=-1>全部</option>
			</select> 小件员：<select id="customerid" name="customerid" class="select1">
				<option value=-1>全部</option>
				<%
					for (User deliver : deliverList) {
				%>
				<option value=<%=deliver.getUserid()%>><%=deliver.getRealname()%></option>
				<%
					}
				%>
			</select> <input type="submit" id="find" onclick="$('#searchForm').attr('action',1);return true;"
				value="查询" class="input_button2" />
		</form>
	</div>

	<div class="saomiao_box2">
		<div>
			<input type="button" id="btnval0" value="导出Excel" class="input_button1"
				onclick='exportField(1,$("#deliverid").val());' />
			<table width="100%" border="0" cellspacing="10" cellpadding="0">
				<tbody>
					<tr>
						<td width="10%" height="26" align="left" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
								<tr>
								    <td width="2" align="center" bgcolor="#f1f1f1">
								    	<input id="machbranch" name="machbranch" type="checkbox" />
								    </td>
									<td width="50" align="center" bgcolor="#f1f1f1">预订单编号</td>
									<td width="50" align="center" bgcolor="#f1f1f1">寄件人</td>
									<td width="50" align="center" bgcolor="#f1f1f1">手机号</td>
									<td width="50" align="center" bgcolor="#f1f1f1">固话</td>
									<td width="50" align="center" bgcolor="#f1f1f1">预约时间</td>
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
										<td width="120" align="center">
										<input id="machbranch" name="machbranch" type="checkbox" value="" /></td>
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
										<td colspan="6" style="cursor: pointer" onclick="moreTodayToTake();" id="moreTodayToTake">查看更多</td>
									</tr>
									<%
										}
									%>
								</table>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
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
	<!-- 分配的ajax地址 -->
    <input type="hidden" id="assign" value="<%=request.getContextPath()%>/stationOperation/openAssignDlg" />
    <!-- 站点超区的ajax地址 -->
    <input type="hidden" id="superzone" value="<%=request.getContextPath()%>/stationOperation/openSuperzoneDlg" />
</body>
</html>