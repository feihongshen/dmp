l<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.util.Page"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Entrance,cn.explink.domain.Customer,cn.explink.domain.Switch"%>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbDetailView> weirukuList = (List<CwbDetailView>)request.getAttribute("weirukulist");
List<CwbDetailView> yirukulist = (List<CwbDetailView>)request.getAttribute("yirukulist");
List<Customer> cList = (List<Customer>)request.getAttribute("customerlist");
List<Entrance> eList = (List<Entrance>)request.getAttribute("entrancelist");
List<User> uList = (List<User>)request.getAttribute("userList");
Switch ck_switch = (Switch) request.getAttribute("ck_switch");
int sitetype=(Integer)request.getAttribute("sitetype");
String RUKUPCandPDAaboutYJDPWAV = request.getAttribute("RUKUPCandPDAaboutYJDPWAV").toString();
String cid=request.getParameter("customerid")==null?"0":request.getParameter("customerid");
long customerid=Long.parseLong(cid);
String  showMassage=(String)request.getAttribute("showMassage");
boolean showCustomerSign= request.getAttribute("showCustomerSign")==null?false:(Boolean)request.getAttribute("showCustomerSign");
long isscanbaleTag= request.getAttribute("isscanbaleTag")==null?1:Long.parseLong(request.getAttribute("isscanbaleTag").toString());
String isprintnew = request.getAttribute("isprintnew").toString();
String ifshowtag=(String)request.getAttribute("ifshowtag");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
	<param name="CompanyName" value="北京易普联科信息技术有限公司" />
	<param name="License" value="653717070728688778794958093190" />
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 CompanyName="北京易普联科信息技术有限公司" License="653717070728688778794958093190"></embed>
</object>
<title>中转入库扫描（明细）</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/intowarehousePrint.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/intowarehousePrintNew.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/intowarehousePrintFor3025.js" type="text/javascript"></script>
<script type="text/javascript">

var App = {ctx:"${pageContext.request.contextPath}"};

$(function(){
	if('${isOpenDialog}'=='open'){
		$('#find').dialog('close');
	}
	if('${auto_allocat}'=="1"){
		$('#autoallocating_switch').show();	
	}
	else if('${auto_allocat}'=="0"){
		$('#autoallocating_switch').hide();
	}
	$("#cwbnohide").click(function(){
		
		$("#cwbno").toggle();
		$("#div0011").toggle();
		
	});
	
});


function closeDialog(){
	$('#find').dialog('close');
	$("#scancwb").focus();
}

var LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  

var data;
var startIndex=0;
var step=4;
var preStep;
function initEmailDateUI(emaildate){
	 $.ajax({
		 type: "POST",
			url:"<%=request.getContextPath()%>/emaildate/getEmailDateList",
			data:{customerids:$("#customerid").val(),state:"-1"},
			success:function(optionData){
				data=optionData;
				var optionstring="";
				var high ="";
				var preStep;
				for(var j=0;j<data.length;j++){
					if(data[j].emaildateid==emaildate){
						preStep=j;
					}
				}
				moreOpt();
				if(emaildate){
					$("#emaildate").val(emaildate);
				}
			
			}
		});
}
	function moreOpt(){
		step=startIndex+4;
		if(preStep>step){
			step=preStep;
		}
		for(var i=startIndex;i<data.length;i++){
			if(i>step){
				continue;
			}
			optionstring="<option value='"+data[i].emaildateid+"'>"+
			data[i].emaildatetime+(data[i].state==0?"（未到货）":"")+" "+
			data[i].customername+"_"+data[i].warehousename+"_"+data[i].areaname
			+"</option>";
			var opt=$(optionstring);
			$("#emaildate").append(opt);
			startIndex=i+1;
		}
	}
var emaildate=0;
	$(function(){
		$("#more").click(moreOpt);
		emaildate=GetQueryString("emaildate");
		initEmailDateUI(emaildate);
		getcwbsdataForCustomer($("#customerid").val(),'',emaildate);
		getcwbsquejiandataForCustomer($("#customerid").val());
		$("#scancwb").focus();
		$("#updateswitch").click(function(){
			var switchstate = "rkbq_02";
			if($("#updateswitch").attr("checked")=="checked"){
				switchstate = $("#updateswitch").val();
				$("#rk_switch").val(switchstate);
			}else{
				switchstate = "rkbq_02";
				$("#rk_switch").val("rkbq_02");
			}
			//按照生哥指导，不需要更新数据库
// 			$.ajax({
// 				type: "POST",
<%-- 				url:"<%=request.getContextPath()%>/switchcontroller/updateswitch?switchstate="+switchstate, --%>
// 				dataType:"json",
// 				success : function(data) {
// 					if(data.errorCode==1){
// 						alert(data.error);
// 					}
// 				}                 
// 			});
		});
	});
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
	
	function focusCwb(){
		$("#scancwb").focus();
	}
	function tabView(tab){
		$("#"+tab).click();
	}
	
	function addAndRemoval(cwb,tab,isRemoval,customerid){
		var trObj = $("#ViewList tr[id='TR"+cwb+"']");
		if(isRemoval){
			$("#"+tab).append(trObj);
		}else{
			$("#ViewList #errorTable tr[id='TR"+cwb+"error']").remove();
			trObj.clone(true).appendTo("#"+tab);
			$("#ViewList #errorTable tr[id='TR"+cwb+"']").attr("id",trObj.attr("id")+"error");
		}
		//已入库明细只显示该供货商明细、异常单明细只显示该供货商明细
		if(customerid>0){
			$("#successTable tr").hide();
			$("#successTable tr[customerid='"+customerid+"']").show();
			
			$("#errorTable tr").hide();
			$("#errorTable tr[customerid='"+customerid+"']").show();
		}else{
			$("#successTable tr").show();
			$("#errorTable tr").show();
		}
	}

	//得到当前入库的供应商的库存量
	function getcwbsdataForCustomer(customerid, cwb,emaildate) {
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath() %>/PDA/getInSum",
			data : {
				"customerid" : customerid,
				"cwb" : cwb,
				"emaildate": emaildate
			},
			dataType : "json",
			success : function(data) {
				$("#rukukucundanshu").html(data.weirukucount);
				$("#rukukucunjianshu").html(data.weirukusum);
				$("#successcwbnum").html(data.yirukunum);
			}
		});
		
	
	}
	
	
	//得到入库缺货件数的统计
	function getcwbsquejiandataForCustomer(customerid) {
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/PDA/getInQueSum",
			data : {
				"customerid" : customerid,
				"emaildate":emaildate
			},
			dataType : "json",
			success : function(data) {
				$("#lesscwbnum").html(data.lesscwbnum);
			}
		});
	}
	
	//得到入库缺货件数的list列表
	function getrukucwbquejiandataList(customerid){
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/PDA/getInQueList",
			data : {
				"customerid" : customerid,
				"emaildate":emaildate
			},
			dataType : "html",
			success : function(data) {
				$("#lesscwbTable").html(data);
			}
		});
		
	}
	
	//得到入库缺货件数的list列表
	function orderbygetrukucwbquejiandataList(type,customerid){
		asc++;
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/PDA/orderbygetrukucwbquejiandataList",
			data : {"asc":asc,
				"orderby":type,
				"customerid" : customerid,
				"emaildate":emaildate
			},
			dataType : "html",
			success : function(data) {
				$("#lesscwbTable").empty();
				$("#lesscwbTable").html(data);
			}
		});
		
	}
function callfunction(cwb){//getEmailDateByIds
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/PDA/getEmaildateid/"+cwb,
		data:{emaildate:$("#emaildate").val(),state:'0'},
		success:function(data){
			if(data.tip==1){
			alert(data.cwb+"订单号不在本批次中，请选择"+data.emaildatename+"的批次");
			}
		}
	});
}

	/**
	 * 入库扫描
	 */
	function submitIntoWarehouse(pname, scancwb, customerid, driverid,
			requestbatchno, rk_switch, comment) {
		
		var flag=false;
		if('${auto_allocat}'=="1"&&$("#entryselect").val()=='-1'){
			alert("请选择自动分拨机入口");
			return;
		}
		if (scancwb.length > 0) {
			$("#close_box").hide();

			if($("#scanbaleTag").attr("class")=="light"){//入库根据包号扫描订单
				baledaohuo(scancwb,driverid,comment);
			}else{//入库
				//是否按批次过滤？
				if($("#emaildate").val()>0){
					var recheck=false;
					$(".yirukucwbids").each(function(i,val){
						if(scancwb==val.id.substr(2)){
							alert(scancwb+"订单重复入库");
							recheck=true;
							return;
						}
					} )
					if(recheck){
						return;
					}
					var flag=false;
					$(".cwbids").each(function(i,val){
						if(scancwb==val.id.substr(2)){
							flag=true;
						}
					} )
/* 					if(!flag){
						 callfunction(scancwb);
						return;
					} */
				}
				$.ajax({
							type : "POST",
							url : pname + "/PDA/cwbintowarhouse/" + scancwb
									+ "?customerid=" + customerid
									+ "&driverid=" + driverid
									+ "&requestbatchno=" + requestbatchno,
							data : {
								"comment" : comment,
								"youhuowudanflag":$("#youhuowudanflag").val(),
								"autoallocatid":$("#entryselect").val(),
								"direction" :$("input[type='radio']:checked").val()
							},
							dataType : "json",
							success : function(data) {
								
								
								if (data.statuscode == "000000") {
									$("#msg").val("");
									$("#scancwb").val("");
									$("#cwbgaojia").hide();

									$("#excelbranch").show();
									$("#customername").show();
									$("#damage").show();
									$("#multicwbnum").show();

									$("#customerid").val(data.body.cwbOrder.customerid);

									if(data.body.showRemark!=null){
										$("#cwbDetailshow").html("订单备注："+data.body.showRemark);
										}
									if (data.body.cwbOrder.deliverybranchid != 0) {
										$("#excelbranch").html("目的站："+ data.body.cwbdeliverybranchname
																+ "<br/>下一站："+ data.body.cwbbranchname);
									} else {
										$("#excelbranch").html("尚未匹配站点");
									}
									$("#customername").html(
											data.body.cwbcustomername);
									$("#multicwbnum").val(
											data.body.cwbOrder.sendcarnum);
									$("#msg").html(scancwb+ data.errorinfo + "（共"+ data.body.cwbOrder.sendcarnum
													+ "件，已扫"+ data.body.cwbOrder.scannum+ "件）" );
									
									//将成功扫描的订单放到已入库明细中
									//addAndRemoval(data.body.cwbOrder.cwb,"successTable",true,$("#customerid").val());
									
									
									
									/* if (data.body.cwbOrder.scannum == 1) {
										$("#successcwbnum").html(parseInt($("#successcwbnum").html()) + 1);
									} */
									if($("#updateswitch").attr('checked')=='checked'){
										if (rk_switch == "rkbq_01") {
											$("#printcwb",parent.document).attr("src",pname + "/printcwb?scancwb="+ scancwb + "&a="+ new Date());
										//广州通路
										}else if (rk_switch == "rkbq_03") {
											printIntowarehouse(scancwb);
											/* $("#printcwb",parent.document).attr("src",pname + "/printcwb/printCwbnew?scancwb="+ scancwb + "&a="+ new Date()); */
										//重庆华宇
										}else if (rk_switch == "rkbq_05") {
											printIntowarehouseNew(scancwb);
											/* $("#printcwb",parent.document).attr("src",pname + "/printcwb/printCwbnew?scancwb="+ scancwb + "&a="+ new Date()); */
										}else if (rk_switch == "rkbq_04") {						
											$("#printcwb",parent.document).attr("src",pname + "/printcwb/printCwbruku?scancwb="+ scancwb + "&a="+ new Date());
										//福建飞远30*25
										}else if (rk_switch == "rkbq_06") {						
											printIntowarehouse3025(scancwb);
										} 
									}

									$("#scansuccesscwb").val(scancwb);
									$("#showcwb").html("订 单 号：" + data.body.cwbOrder.cwb);
									$("#consigneeaddress").html("地 址："+ data.body.cwbOrder.consigneeaddress);
									if(data.body.showRemark!=null){
									$("#cwbDetailshow").html("订单备注："+data.body.showRemark);
									}
									if(data.body.cwbOrder.emaildateid==0){
										$("#morecwbnum").html(parseInt($("#morecwbnum").html()) + 1);
									}
								} else {
									$("#excelbranch").hide();
									$("#customername").hide();
									$("#cwbgaojia").hide();
									$("#damage").hide();
									$("#multicwbnum").hide();
									$("#multicwbnum").val("1");
									$("#showcwb").html("");
									$("#cwbDetailshow").html("");
									$("#consigneeaddress").html("");
									$("#scancwb").html("");
									$("#msg").html("");
									if(data.statuscode=="3"){
										$("#scancwb").val("");
										if("${isOpenDialog}" != "open"){
											alert("（异常扫描）"+data.errorinfo);
										}else{
											$("#msg1").html("（异常扫描）"+data.errorinfo);
											$('#find').dialog('open');
											$("#scancwb").blur();
										}
									}else if(data.statuscode=="13"){
										$("#scancwb").val("");
										if("${isOpenDialog}" != "open"){
											alert("（异常扫描）"+data.errorinfo+",订单流程未设置入库，不允许入库！");
										}else{
											
											$("#msg1").html("（异常扫描）"+data.errorinfo+",订单流程未设置入库，不允许入库！");
											$('#find').dialog('open');
											$("#scancwb").blur();
										}
									}else if(data.statuscode=="105"){
										<%-- if(!confirm("无数据，有货无单,是否放弃入库？")){
											$("#youhuowudanflag").val("1");
											data.wavList = [];
											submitIntoWarehouse("<%=request.getContextPath()%>",$("#scancwb").val(),$("#customerid").val(),$("#driverid").val(),$("#requestbatchno").val(),$("#rk_switch").val(),"");
										}else{ --%>
											
											$("#scancwb").val("");
											/* if("${isOpenDialog}" != "open"){ */
												$("#msg").html("（异常扫描）" + data.errorinfo);
											/* }else{
												$("#msg1").html("（异常扫描）" + data.errorinfo);
												$('#find').dialog('open');
												$("#scancwb").blur();
											} */
										<%-- } --%>
									}else if(data.statuscode=="102"){
										if(!confirm("尚未匹配站点，是否放弃入库？")){
											$("#youhuowudanflag").val("1");
											data.wavList = [];
											submitIntoWarehouse("<%=request.getContextPath()%>",$("#scancwb").val(),$("#customerid").val(),$("#driverid").val(),$("#requestbatchno").val(),$("#rk_switch").val(),"");
										}else{
											$("#scancwb").val("");
											if("${isOpenDialog}" != "open"){
												$("#msg").html("（异常扫描）" + data.errorinfo);
											}else{
												$("#msg1").html("（异常扫描）" + data.errorinfo);
												$('#find').dialog('open');
												$("#scancwb").blur();
											}
										}
	}else if(data.statuscode=="103"){
								$("#scancwb").val("");
								if("${isOpenDialog}" != "open"){
									$("#msg").html(data.errorinfo);
								}else{
									$("#msg1").html(data.errorinfo);
									$('#find').dialog('open');
									$("#scancwb").blur();
								}
									}
									else{
										$("#scancwb").val("");
										if("${isOpenDialog}" != "open"){
											$("#msg").html("（异常扫描）" + data.errorinfo);
										}else{
											$("#msg1").html("（异常扫描）" + data.errorinfo);
											$('#find').dialog('open');
											$("#scancwb").blur();
												
										}
									}
									addAndRemoval(scancwb,"errorTable",false,$("#customerid").val());
									
								}
								$("#youhuowudanflag").val("0");
								$("#responsebatchno").val(data.responsebatchno);
								batchPlayWav(data.wavList);
							}
				});
		}
	}
}
	/**
	 * 入库备注提交
	 */
	function intoWarehouseforremark(pname, scancwb, csremarkid, multicwbnum) {
		if (csremarkid == 4 && multicwbnum == "1") {
			$("#msg").html("抱歉，一票多件至少件数为2");
			return;
		} else {
			$.ajax({
				type : "POST",
				url : pname + "/PDA/forremark/" + scancwb + "?csremarkid="
						+ csremarkid + "&multicwbnum=" + multicwbnum
						+ "&requestbatchno=0",
				dataType : "json",
				success : function(data) {
					if (data.statuscode == "000000") {
						$("#msg").html("订单备注操作成功");
					} else {
						$("#msg").html(data.errorinfo);
						errorvedioplay(pname, data);
					}
				}
			});
		}
	}
	
	
function exportField(flag,customerid){
	var cwbs = "";
	$("#expemailid").val($("#emaildate").val());
	if(flag==1){
		$("#expcustomerid").val($("#customerid").val());
		$("#type").val("weiruku");
		$("#searchForm3").submit();
	}else if(flag==2){
		$("#expcustomerid").val($("#customerid").val());
		$("#type").val("yiruku");
		$("#searchForm3").submit();
	}else if(flag==3){//修改导出问题
		/* if(customerid>0){
			$("#errorTable tr[customerid='"+customerid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}else{ */
			$("#errorTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		/* } */
		if(cwbs.length>0){
			cwbs = cwbs.substring(0, cwbs.length-1);
		}
		if(cwbs!=""){
			$("#exportmould2").val($("#exportmould").val());
			$("#cwbs").val(cwbs);
			$("#btnval").attr("disabled","disabled");
		 	$("#btnval").val("请稍后……");
		 	$("#searchForm2").submit();
		}else{
			alert("没有订单信息，不能导出！");
		};
	}else if(flag==4){
		$("#expcustomerid").val($("#customerid").val());
		$("#type").val("ypdj");
		$("#searchForm3").submit();
	}
	
}

var weipage=1;
var yipage=1;
function weiruku(){
	weipage+=1;
	$.ajax({
		type:"post",
		data:{"page":weipage,"customerid":$("#customerid").val()},
		url:"<%=request.getContextPath()%>/PDA/getimportweirukulist",
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					<%if(showCustomerSign){ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'>"+data[i].packagecode+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%}else{ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='120' align='center'>"+data[i].packagecode+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%} %>
				}
				$("#weiruku").remove();
				$("#weirukuTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
				var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>8<%}else{ %>7<%} %>" style="cursor:pointer" onclick="weiruku();" id="weiruku">查看更多</td></tr>';
				$("#weirukuTable").append(more);
				}
			}
		}
	});
	
	
}
var asc=0;
function orderbyweiruku(type)
{  asc++;
	$.ajax({
		type:"post",
		data:{"asc":asc,"orderby":type,"customerid":$("#customerid").val()},
		url:"<%=request.getContextPath()%>/PDA/orderbyweiruku",
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					<%if(showCustomerSign){ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'>"+data[i].packagecode+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%}else{ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'>"+data[i].packagecode+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%} %>
				}
				$("#weiruku").remove();
			//	alert($("#weirukuTable").remove());
				$("#weirukuTable").empty();
				$("#weirukuTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
				var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>8<%}else{ %>7<%} %>" style="cursor:pointer" onclick="weiruku();" id="weiruku">查看更多</td></tr>';
				$("#weirukuTable").append(more);
				}
			}
		}
	});
	}

function yiruku(){
	yipage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getimportyiruku",
		data:{"page":yipage,"customerid":$("#customerid").val()},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					<%if(showCustomerSign){ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'>"+data[i].packagecode+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%}else{ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'>"+data[i].packagecode+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%} %>
				}
				$("#yiruku").remove();
				$("#successTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
				var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>8<%}else{ %>7<%} %>" style="cursor:pointer" onclick="yiruku();" id="yiruku">查看更多</td></tr>'
				$("#successTable").append(more);
				}
			}
		}
	});
	
}
function orderbyyiruku(type){
	asc+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/orderbyyiruku",
		data:{"asc":asc,"orderby":type,"customerid":$("#customerid").val()},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					
					<%if(showCustomerSign){ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'>"+data[i].packagecode+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%}else{ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'>"+data[i].packagecode+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%} %>
				}
				$("#yiruku").remove();
				$("#successTable").empty();
				$("#successTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
				var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>8<%}else{ %>7<%} %>" style="cursor:pointer" onclick="yiruku();" id="yiruku">查看更多</td></tr>'
				$("#successTable").append(more);
				}
			}
		}
	});
	
}
function ranCreate(){
	var timestamp = new Date().getTime();
	$("#baleno").val(timestamp);
	$("#baleno").attr('readonly','readonly');
	$('#scancwb').parent().show();
	$('#scancwb').show();
	$('#scancwb').focus();
}
function hanCreate(){
	$('#baleno').removeAttr('readonly');
	$('#baleno').val('');
	$('#baleno').focus();
}
function clearMsg(){
	$("#msg").html("");
	$("#showcwb").html("");
	$("#excelbranch").html("");
	$("#customername").html("");
	$("#consigneeaddress").html("");
	$("#cwbDetailshow").html("");
	$("#cwbgaojia").hide();
}

function tohome(){
	var isscanbaleTag = 1;
	if($("#scanbaleTag").hasClass("light")){
		isscanbaleTag=1;
	}else{
		isscanbaleTag=0;
	}
	var temp=$("#customerid").val();
	if("-1"==temp||temp<0){
		$("#emaildate").val("0")
	}
	window.location.href="<%=request.getContextPath() %>/PDA/intowarhouse?customerid="+$("#customerid").val()+"&isscanbaleTag="+isscanbaleTag+"&emaildate="+$("#emaildate").val();
}
$(function(){
	if(<%=isscanbaleTag==1%> ){
		$("#scanbaleTag").click();
		//$("#scancwbTag").removeClass("light");
	}else{
		//$("#scanbaleTag").removeClass("light");
		$("#scancwbTag").click();
	}
});
$(function(){
	var $menuli = $(".saomiao_tab ul li");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		/* $(".tabbox li").eq(index).show().siblings().hide(); */
	});
	
}) 


//=========合包到货=============
 function baledaohuo(scancwb,driverid,comment){
 	if($("#baleno").val()==""){
 		alert("包号不能为空！");
 		$("#scancwb").val("");
 		return;
 	}
 	$.ajax({
 		type: "POST",
 		url:"<%=request.getContextPath()%>/bale/balezhongzhuandaohuo/"+$("#baleno").val()+"/"+scancwb+"?driverid="+driverid,
 		data:{
			"comment":comment
		},
 		dataType : "json",
 		success : function(data) {
 			clearMsg();
 			if("${isOpenDialog}" != "open"){
	 			$("#msg").html(data.body.errorinfo);
			}else{
				$("#msg1").html(data.body.errorinfo);
				$('#find').dialog('open');
				$("#scancwb").blur();
			}
 			$("#scancwb").val("");
 			errorvedioplay("<%=request.getContextPath()%>",data);
 		}
 	});
 }
function openLogin(){
	   document.getElementById("win").style.display="";
	}
	function closeLogin(){
	   document.getElementById("win").style.display="none";
	}
	
function connect(){
	if($("#entryselect").val()=='-1'){
		alert("请选择自动分拨机入口");
		return;
	}
	$.ajax({
 		type: "POST",
 		url: "<%=request.getContextPath()%>/PDA/connect",
 		data:{
			"entranceno":$("#entryselect").val()
		},
 		dataType : "json",
 		success : function(data) {
 			
 		}
 	});
}

function flush(){
	if($("#entryselect").val()=='-1'){
		alert("请选择自动分拨机入口");
		return;
	}
	$.ajax({
 		type: "POST",
 		url: "<%=request.getContextPath()%>/PDA/flush",
 		data:{
			"entranceno":$("#entryselect").val()
		},
 		dataType : "json",
 		success : function(data) {
 			
 		}
 	});
}
/* $(function(){
	$("#customerid").combobox();
}) */
</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="saomiao_box2" style="margin-top:-18px;">
<!-- 新添加 --><div class="saomiao_tab2">
		<ul>
			<li><a href="#"  class="light">逐单操作</a></li>		
			<li><a href="<%=request.getContextPath()%>/PDA/cwbintowarhouseBatch">批量操作</a></li>
		</ul>
	</div><!-- 新添加 -->
	<div class="saomiao_topnum2">
		<dl class="blue">
			<dt>未入库合计</dt>
			<dd style="cursor:pointer" onclick="tabView('table_weiruku')" id="rukukucundanshu">0</dd>
		</dl>
		<dl class="yellow">
			<dt>未入库件数合计</dt>
			<dd id="rukukucunjianshu">0</dd>
		</dl>
		
		<dl class="green">
			<dt>已入库</dt>
			<dd style="cursor:pointer" onclick="tabView('table_yiruku')" id="successcwbnum" name="successcwbnum">0</dd>
		</dl>
		
		
		<dl class="yellow">
			<dt>一票多件缺货件数</dt>
			<dd style="cursor:pointer" onclick="tabView('table_quejian');getrukucwbquejiandataList($('#customerid').val());"  id="lesscwbnum" name="lesscwbnum" >0</dd>
		</dl>
		
		<dl class="red">
			<dt>有货无单</dt>
			<dd <%-- style="cursor:pointer" onclick="tabView('table_youhuowudan')" --%> id="morecwbnum" name="morecwbnum" >0</dd>
		</dl>
		<input type="button"  id="refresh" value="刷新" onclick="location.href='<%=request.getContextPath() %>/PDA/intowarhouse'"  style="float:left; width:100px; height:65px; cursor:pointer; border:none; background:url(../images/buttonbgimg1.gif) no-repeat; font-size:18px; font-family:'微软雅黑', '黑体'"/>
		<br clear="all"/>
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="saomiao_tab">
			<ul id="bigTag">
					<li><a href="#" id="scancwbTag" onclick="clearMsg();$(function(){$('#baleno').parent().hide();$('#finish').parent().hide();$('#baleno').val('');$('#scancwb').val('');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();})" class="light">扫描订单</a></li>
					<%if(sitetype==4){ %>
					<li><a href="#" id="scanbaleTag" onclick="clearMsg();$(function(){$('#baleno').parent().show();$('#baleno').show();$('#finish').parent().show();$('#finish').show();$('#baleno').val('');$('#baleno').focus();$('#scancwb').val('');$('#scancwb').parent().hide();})">合包到货</a></li>
					<%} %>
				</ul>
				</div>
			<div class="saomiao_selet2">
				客户：
				<select id="customerid" name="customerid" onchange="tohome();" class="select1">
					<option value="-1" selected>全部供应商</option>
					<%for(Customer c : cList){ %>
						<option value="<%=c.getCustomerid() %>" <%if(customerid==c.getCustomerid()){ %> selected=selected <%} %> ><%=c.getCustomername() %></option>
					<%} %>
				</select>
				发货批次：
				<select id="emaildate" name="emaildate" onchange="tohome();" style="height: 20px;width: 280px" class="select1">
					<option value='0' id="option2">请选择(供货商_供货商仓库_结算区域)</option> 
				</select>
				<a href="#" id="more" style="color:#222222">更多</a>
				驾驶员：
				<select id="driverid" name="driverid" class="select1">
					<option value="-1" selected>请选择</option>
					<%for(User u : uList){ %>
						<option value="<%=u.getUserid() %>" ><%=u.getRealname() %></option>
					<%} %>
				</select>
			</div>
			<div>
					<span>
						<%
							if(isprintnew.equals("2")){ 
						%>
						<input type="checkbox" id="updateswitch" value="rkbq_04" name="updateswitch"/>打印入库标签
						<%
							} else if (isprintnew.equals("1")) {
						%>
						<input type="checkbox" id="updateswitch" value="<%=ck_switch.getState() %>" name="updateswitch" <%-- <%if(ck_switch.getState().equals("rkbq_03")){ %>checked="checked"<%} %> --%>/>打印新标签
						<%
							} else {
						%>
						<input type="checkbox" id="updateswitch" value="rkbq_01" name="updateswitch" <%--  <%if(ck_switch.getState().equals("rkbq_01")){ %>checked="checked"<%} %> --%>/>打印标签
						<%
							}
						%>
						</span>
						<span id='autoallocating_switch' type="text" style="display:none;width:500px"> &nbsp;&nbsp;&nbsp;&nbsp;自动分拨机入口选择*：<select id="entryselect" name="entryselect" style="height: 20px; width: 150px">
						<option value="-1" selected>请选择</option>
						<%
							for (Entrance e : eList) {
						%>
						<option value="<%=e.getEntranceno()%>"><%=e.getEntranceno()+"("+e.getEntranceip()+")"%></option>
						<%
							}
						%>
						</select> 
						<input type="button" id="connect" onclick="connect()"  value="连接" />
						<input type="button" id="flush" onclick="flush()"  value="清空队列" />
						<input type="radio" name="direction" id="forward" value="0" checked="checked" />正向
						<input type="radio"  name="direction" id="backward" value="1" />逆向
						</span>					
					</div>
			<div class="saomiao_inwrith2">
				<div class="saomiao_left2">								
					<p style="display: none;">
					<span>包号：</span>
						<input type="text" class="saomiao_inputtxt2" value=""  id="baleno" name="baleno" onKeyDown="if(event.keyCode==13&&$(this).val().length>0){$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();}"/>
						<span>&nbsp;</span>
					</p>
					<p id="cwbno"><span>单/包号：</span>
						<input type="text" class="saomiao_inputtxt" id="scancwb" name="scancwb" value="" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){submitIntoWarehouse("<%=request.getContextPath()%>",$(this).val(),$("#customerid").val(),$("#driverid").val(),$("#requestbatchno").val(),$("#rk_switch").val(),"");}'/>
					</p>
					<input type="hidden" id="youhuowudanflag" name="youhuowudanflag" value="0" />
					<p>
				<%				
					if(("yes").equals(ifshowtag)){			
				%> 					
					<input type="checkbox" id="cwbnohide" />
					<span>入库标签补签</span>
					<div style="display: none" id="div0011">
					<input type="text" name="scancwbprint" id="scancwbprint" class="saomiao_inputtxt" />			
					<input type="button" value="生成" onclick="javascript:BuprintTag('<%=request.getContextPath()%>');"/> 
					</div>
					<%
					}
					%>
				</p>	
				</div>
				<c:if test="${isOpenDialog=='open'}">
					<div  id="find" class="easyui-dialog" data-options="modal:true" title="提示信息"  style="width:400px;height:200px;">
				 		<div class="saomiao_right2">
							<p id="msg1" name="msg" ></p>
							<p id="showcwb" name="showcwb"></p>
							<p id="cwbgaojia" name="cwbgaojia" style="display: none" >高价</p>
							<p id="consigneeaddress" name="consigneeaddress"></p>
							<p id="excelbranch" name="excelbranch" ></p>
							<p id="customername" name="customername" ></p>
							<p id="cwbDetailshow" name="cwbDetailshow" ></p>
							<div style="display: none" id="EMBED">
							</div>
							<div style="display: none">
								<EMBED id='ypdj' name='ypdj' SRC='<%=request.getContextPath() %><%=ServiceUtil.waverrorPath %><%=CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl() %>' LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
							</div>
							<div style="display: none">
								<EMBED id='gaojia' name='gaojia' SRC='<%=request.getContextPath() %><%=ServiceUtil.waverrorPath %><%=CwbOrderPDAEnum.GAO_JIA.getVediourl() %>' LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
							</div>
							<div style="display: none" id="errorvedio"></div>
						</div>
							<div  align="center" valign="bottom" style="margin-top:100px;">
					         	<input type="button" class="input_button1" value="关闭" onclick="closeDialog();"/>
				         	</div>
					</div>
			 	</c:if>
				
				<div class="saomiao_right2">
					<p id="msg" name="msg" ></p>
					<p id="showcwb" name="showcwb"></p>
					<p id="cwbgaojia" name="cwbgaojia" style="display: none" >高价</p>
					<p id="consigneeaddress" name="consigneeaddress"></p>
					<p id="excelbranch" name="excelbranch" ></p>
					<p id="customername" name="customername" ></p>
					<p id="cwbDetailshow" name="cwbDetailshow" ></p>
					<div style="display: none" id="EMBED">
					</div>
					<div style="display: none">
						<EMBED id='ypdj' name='ypdj' SRC='<%=request.getContextPath() %><%=ServiceUtil.waverrorPath %><%=CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl() %>' LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
					</div>
					<div style="display: none">
						<EMBED id='gaojia' name='gaojia' SRC='<%=request.getContextPath() %><%=ServiceUtil.waverrorPath %><%=CwbOrderPDAEnum.GAO_JIA.getVediourl() %>' LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
					</div>
					<div style="display: none" id="errorvedio"></div>
				</div>
				<p style="display: none;">
						<input type="button" class="input_btn1" id="moregoods" name="moregoods" value="一票多物" onclick='intoWarehouseforremark("<%=request.getContextPath()%>",$("#scansuccesscwb").val(),4,$("#multicwbnum").val());'/>
						<span>一票多物件数：</span><input type="text" id="multicwbnum" name="multicwbnum" value="1" class="input_txt1"/>
					</p>
					<p style="display: none;">
						<input type="button" class="input_btn1" id="damage" name="damage" value="破损" onclick='intoWarehouseforremark("<%=request.getContextPath()%>",$("#scansuccesscwb").val(),1,$("#multicwbnum").val());'/>
						<input type="button" class="input_btn1" id="superbig" name="superbig" value="超大" onclick='intoWarehouseforremark("<%=request.getContextPath()%>",$("#scansuccesscwb").val(),2,$("#multicwbnum").val());'/>
						<input type="button" class="input_btn1" id="superweight" name="superweight" value="超重" onclick='intoWarehouseforremark("<%=request.getContextPath()%>",$("#scansuccesscwb").val(),3,$("#multicwbnum").val());'/>
					</p>
					<input type="hidden" id="requestbatchno" name="requestbatchno" value="0" />
			        <input type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" />
			        <input type="hidden" id="rk_switch" name="rk_switch" value="<%=ck_switch.getState() %>" />
			</div><!-- </form> -->
		</div>
	</div>

		<div>
			<div class="saomiao_tab2">
				<span style="float: right; padding: 10px">
					<input  class="input_button1" type="button" name="littlefalshbutton" id="flash" value="刷新" onclick="location.href='<%=request.getContextPath() %>/PDA/intowarhouse'" />
				</span>
				<ul id="smallTag">
					<li><a id="table_weiruku" href="#" class="light">未入库明细</a></li>
					<li><a id="table_yiruku" href="#">已入库明细</a></li>
					<li><a id="table_quejian" href="#" onclick='getrukucwbquejiandataList($("#customerid").val());'>一票多件缺件</a></li>
					<li><a href="#">异常单明细</a></li>
				</ul>
			</div>
			<div id="ViewList" class="tabbox">
				<li>
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(1,$("#customerid").val());'/>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">包号</td>
											<td width="100" align="center" bgcolor="#f1f1f1"><span style="cursor: pointer;" onclick="orderbyweiruku('customerid')">供货商</span></td>
											<td width="140" align="center" bgcolor="#f1f1f1"><span style="cursor: pointer;" onclick="orderbyweiruku('emaildate')">发货时间</span></td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="weirukuTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%for(CwbDetailView co : weirukuList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" class="cwbids">
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%=co.getCustomername() %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<%if(showCustomerSign){ %>
												<td width="100"><%=co.getRemarkView()==null?"":co.getRemarkView().toString() %></td>
												<%} %>
												<td align="left"><%=co.getConsigneeaddress() %></td>
												</tr>
											<%} %>
											 <%if(weirukuList!=null&&weirukuList.size()==Page.DETAIL_PAGE_NUMBER){ %>
												<tr align="center"  >
												<td  colspan="<%if(showCustomerSign){ %>8<%}else{ %>7<%} %>" style="cursor:pointer" onclick="weiruku();" id="weiruku">查看更多</td></tr>
											<%} %>
		
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
				<li style="display: none">
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(2,$("#customerid").val());'/>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">包号</td>
											<td width="100" align="center" bgcolor="#f1f1f1"><span style="cursor: pointer;" onclick="orderbyyiruku('customerid')">供货商</span></td>
											<td width="140" align="center" bgcolor="#f1f1f1"><span style="cursor: pointer;" onclick="orderbyyiruku('emaildate')">发货时间</span></td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="successTable" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
											<%for(CwbDetailView co : yirukulist){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" class="yirukucwbids">
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%=co.getCustomername() %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView() %></td>
												<%} %>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
											<%if(yirukulist!=null&&yirukulist.size()==Page.DETAIL_PAGE_NUMBER){ %>
												<tr  aglin="center"><td colspan="<%if(showCustomerSign){ %>8<%}else{ %>7<%} %>" style="cursor:pointer" onclick="yiruku();" id="yiruku">查看更多</td></tr>
											<%} %>	
											
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
				
				<li style="display: none">
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(4,$("#customerid").val());'/>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="120" align="center" bgcolor="#f1f1f1">运单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1"><span style="cursor: pointer;" onclick="orderbygetrukucwbquejiandataList('customerid',$('#customerid').val())">供货商</span></td>
											<td width="140" align="center" bgcolor="#f1f1f1"><span style="cursor: pointer;" onclick="orderbygetrukucwbquejiandataList('emaildate',$('#customerid').val())">发货时间</span></td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="lesscwbTable" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
				
				<li style="display: none">
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(3,$("#customerid").val());'/>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">包号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="errorTable" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
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
		<input type="hidden" name="cwbs" id="cwbs" value=""/>
		<input type="hidden" name="exportmould2" id="exportmould2" />
	</form>
	<form action="<%=request.getContextPath() %>/PDA/exportByCustomerid" method="post" id="searchForm3">
		<input  type="hidden"  name="customerid" value="0" id="expcustomerid" />
		<input  type="hidden"  name="emaildate" value="0" id="expemailid" />
		<input type="hidden" name="type" value="" id="type"/>
	</form>
	</div>

</body>
</html>
