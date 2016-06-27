<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch,cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
List<CwbDetailView> jinriweidaohuolist = (List<CwbDetailView>)request.getAttribute("jinriweidaohuolist");
List<CwbDetailView> historyweidaohuolist = (List<CwbDetailView>)request.getAttribute("historyweidaohuolist");
boolean showCustomerSign= request.getAttribute("showCustomerSign")==null?false:(Boolean)request.getAttribute("showCustomerSign");
List<CwbDetailView> yidaohuolist = (List<CwbDetailView>)request.getAttribute("yidaohuoViewlist");
List<CwbDetailView> lanjianweidaohuolist = (List<CwbDetailView>)request.getAttribute("lanjianweidaohuolist");

List<Customer> cList = (List<Customer>)request.getAttribute("customerlist");
List<User> uList = (List<User>)request.getAttribute("userList");
Switch ck_switch = (Switch) request.getAttribute("ck_switch");
long isscanbaleTag= request.getAttribute("isscanbaleTag")==null?1:Long.parseLong(request.getAttribute("isscanbaleTag").toString());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>到货详情扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$(function(){
	var $menuli1 = $("#Tag ul li");
	$menuli1.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
	});
	
	var $menuli2 = $("#smallTag ul li");
	$menuli2.click(function() {
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli2.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
})


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


//得到当前到货的库存量
function getcwbsdataForDaoHuo(cwb){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath() %>/PDA/getZhanDianInSum",
		data:{
			"cwb":cwb
		},
		dataType:"json",
		success : function(data) {
			$("#jinriweidaohuodanshu").html(data.jinriweidaohuocount);
			$("#historyweidaohuodanshu").html(data.historyweidaohuocount);
			$("#successcwbnum").html(data.yidaohuonum);
			$("#yuyuedaweidaohuocount").html(data.yuyuedaweidaohuocount);
			$("#yuyuedayidaohuocount").html(data.yuyuedayidaohuocount);
			$("#lanjianweidaozhancount").html(data.lanjianweidaozhancount);
		}
	});
}
$(function(){
	getcwbsdataForDaoHuo('');
	getDaoHuoQueSum();
	 $("#scancwb").focus();
	
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
var successnum = 0;
var branchimportdetailcwbStr="";
function submitBranchImport(pname,scancwb,driverid,requestbatchno,rk_switch,comment){
	if(scancwb.length>0){
		if($("#balenoTab").attr("class")=="light"){//入库根据包号扫描订单
			baledaohuo(scancwb,driverid,requestbatchno);
		}else{//入库
			$.ajax({
				type: "POST",
				url:pname+"/PDA/cwbsubstationGoods/"+scancwb+"?driverid="+driverid+"&requestbatchno="+requestbatchno,
				data:{
					"comment":""
				},
				dataType:"json",
				success : function(data) {
					$("#scancwb").val("");
					if(data.statuscode=="000000"){
						$("#cwbgaojia").hide();
						$("#excelbranch").show();
						$("#customername").show();
						$("#damage").show();
						$("#multicwbnum").show();
						$("#dingdanlanjie").hide();
						if(data.body.cwbOrder.deliverybranchid!=0){
							var html = "目的站：" + data.body.cwbdeliverybranchname;
							html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
							if(data.body.cwbOrder.exceldeliverid != 0) {
								html += "配送员：" + data.body.deliverName;
							} else {
								html += "尚未匹配配送员";
							}
							html += "<br/>下一站："+data.body.cwbbranchname;
							$("#excelbranch").html(html);
						}else{
							$("#excelbranch").html("尚未匹配站点和配送员");
						}
						if(data.body.dingdanlanjie != ""){
							$("#dingdanlanjie").show();
						}
						if(data.body.cwbOrder.customercommand.indexOf('预约')>=0&&data.yuyuedaService=='yes')
						{	
							$("#customercommand").html("预约派送");
						}
						else{$("#customercommand").html("");}
						if(data.body.showRemark!=null){
						$("#cwbDetailshow").html("订单备注："+data.body.showRemark);
						}
						$("#customername").html(data.body.cwbcustomername);
						$("#address").html(data.body.cwbOrder.consigneeaddress);
						$("#multicwbnum").val(data.body.cwbOrder.sendcarnum);
						$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
						var deliveryname=data.body.deliveryname;
						if(deliveryname.length>0){
							deliveryname="   <font color='red'>配送员："+deliveryname+"</font>";
						}
						$("#scansuccesscwb").val(scancwb);
						$("#showcwb").html("订 单 号："+data.body.cwbOrder.cwb+deliveryname);
						//getcwbsdataForDaoHuo(scancwb);
						//将成功扫描的订单放到已入库明细中
						//addAndRemoval(data.body.cwbOrder.cwb,"successTable",true);
						
						if(rk_switch=="rkbq_01"){
							$("#printcwb").attr("src",pname+"/printcwb?scancwb="+scancwb+"&a="+new Date());
						}
						
						/*if (data.body.cwbbranchnamewav != pname+ "/wav/") {
							$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ data.body.cwbbranchnamewav
												+ "&a="+ Math.random());
						}else{
							$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
						}
						
						if(data.body.cwbgaojia!=undefined&&data.body.cwbgaojia!=''){
							$("#cwbgaojia").parent().show();
							try{
							document.gaojia.Play();
							}catch (e) {}
						}
						if(data.body.cwbOrder.sendcarnum>1){
							try{
								document.ypdj.Play();
							}catch (e) {}
						}*/
					}else if(data.statuscode=="000001"){
						
						$("#excelbranch").hide();
						$("#customername").hide();
						$("#address").html("");
						$("#cwbgaojia").hide();
						$("cwbDetailshow").hide();
						$("#damage").hide();
						$("#multicwbnum").hide();
						$("#multicwbnum").val("1");
						$("#showcwb").html("");
						$("#customercommand").html("");

						$("#msg").html(data.errorinfo);
					}
					else{
						$("#excelbranch").hide();
						$("#customername").hide();
						$("#address").html("");
						$("#cwbgaojia").hide();
						$("cwbDetailshow").hide();
						$("#damage").hide();
						$("#multicwbnum").hide();
						$("#multicwbnum").val("1");
						$("#showcwb").html("");
						$("#customercommand").html("");
						$("#msg").html("         （异常扫描）"+data.errorinfo);
						addAndRemoval(scancwb,"errorTable",false);
						//errorvedioplay(pname,data);
					}
					$("#responsebatchno").val(data.responsebatchno);
					batchPlayWav(data.wavList);
				}
			});
		}
	}
}
/**
 * 入库扫描（包）
 */
/* function submitIntoWarehouseforbale(pname,driverid,baleno){
	if(scancwb.length==0&&baleno.length==0){
		$("#pagemsg").html("请先扫描");
		return ;
	}
	if(baleno.length>0){
		$.ajax({
			type: "POST",
			url:pname+"/PDA/cwbintowarhouseByPackageCode/"+baleno+"?driverid="+driverid,
			dataType:"json",
			success : function(data) {
				$("#baleno").val("");
				$("#msg").html(data.body.packageCode+"　（"+data.errorinfo+"）");
			}
		});
	}
} */
/**
 * 入库备注提交
 */
function intoWarehouseforremark(pname,scancwb,csremarkid,multicwbnum){
	if(csremarkid==4&&multicwbnum=="1"){
		$("#msg").html("抱歉，一票多件至少件数");
		return ;
	}else{
		$.ajax({
			type: "POST",
			url:pname+"/PDA/forremark/"+scancwb+"?csremarkid="+csremarkid+"&multicwbnum="+multicwbnum+"&requestbatchno=0",
			dataType:"json",
			success : function(data) {
				if(data.statuscode=="000000"){
					$("#msg").html("订单备注操作成功");
				}else{
					$("#msg").html(data.errorinfo);
					errorvedioplay(pname,data);
				}
			}
		});
	}
}


function exportField(flag){
	var cwbs = "";
	if(flag==1){
		/* $("#weidaohuoTable tr").each(function(){
			var cwb = $(this).attr("cwb");
			cwbs += "'" + cwb + "',";
		}); */
		$("#type").val(flag);
		$("#searchForm3").submit();
	}else if(flag==2){
		/* $("#successTable tr").each(function(){
			var cwb = $(this).attr("cwb");
			cwbs += "'" + cwb + "',";
		}); */
		$("#type").val(flag);
		$("#searchForm3").submit();
	}else if(flag==3){
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
		$("#type").val("ypdj");
		$("#searchForm3").submit();
	}else if(flag==5){
		$("#type").val(flag);
		$("#searchForm3").submit();
	}else if(flag==6){
		$("#type").val("lanjianwdz");
		$("#searchForm3").submit();
	}
}

function clearMsg(){
	$("#msg").html("");
	$("#showcwb").html("");
	$("#excelbranch").html("");
	$("#customername").html("");
	$("#address").html("");
	$("#cwbDetailshow").html("");
	$("#cwbgaojia").hide();
	
}
var jinriweipage=1;
var historyweipage=1;
var yipage=1;
var lanjianweipage=1;
function yidaohuo(){
	yipage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbranchimportyidaolist",
		data:{"page":yipage},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					<%if(showCustomerSign){ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'>"+data[i].packagecode+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].inSitetime+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td width='100' align='center'> "+data[i].remarkView==null?"":data[i].remarkView+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%}else{ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'>"+data[i].packagecode+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].inSitetime+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%} %>
				}
				$("#yidaohuo").remove();
				$("#successTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
				var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>8<%}else{ %>7<%} %>" style="cursor:pointer" onclick="yidaohuo();" id="yidaohuo">查看更多</td></tr>';
				$("#successTable").append(more);
				}
			}
		}
	});
};

function jinriweidaohuo(){
	jinriweipage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbranchimportjinriweidaolist",
		data:{"page":weipage},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					<%if(showCustomerSign){ %>
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'>"+data[i].packagecode+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].outstoreroomtime+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td width='100' align='center'> "+data[i].remarkView==null?"":data[i].remarkView+"</td>"
					+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
					+ "</tr>";
				<%}else{ %>
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"'>"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'>"+data[i].packagecode+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].outstoreroomtime+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
					+ "</tr>";
				<%} %>
				}
				$("#jinriweidaohuo").remove();
				$("#jinriweidaohuoTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
				var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>8<%}else{ %>7<%} %>" style="cursor:pointer" onclick="jinriweidaohuo();" id="jinriweidaohuo">查看更多</td></tr>';
				$("#jinriweidaohuoTable").append(more);
				}
			}
		}
	});
}

function historyweidaohuo(){
	historyweipage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbranchimporthistoryweidaolist",
		data:{"page":weipage},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					<%if(showCustomerSign){ %>
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'>"+data[i].packagecode+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].outstoreroomtime+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
					+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
					+ "</tr>";
				<%}else{ %>
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"'>"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'>"+data[i].packagecode+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].outstoreroomtime+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
					+ "</tr>";
				<%} %>
				}
				$("#historyweidaohuo").remove();
				$("#historyweidaohuoTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
				var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>8<%}else{ %>7<%} %>" style="cursor:pointer" onclick="historyweidaohuo();" id="historyweidaohuo">查看更多</td></tr>';
				$("#historyweidaohuoTable").append(more);
				}
			}
		}
	});
}
//得到到货缺件数
function getDaoHuoQueSum() {
	$.ajax({
		type : "POST",
		url : "<%=request.getContextPath()%>/PDA/getDaoHuoQueSum",
		dataType : "json",
		success : function(data) {
			$("#lesscwbnum").html(data.lesscwbnum);
		}
	});
}

//得到到货缺货件数的list列表
function getdaohuocwbquejiandataList(){
	$.ajax({
		type : "POST",
		url : "<%=request.getContextPath()%>/PDA/getDaoHuoQueList",
		dataType : "json",
		success : function(data) {
			if(data.length>0){
				var optionstring = "";
				$("#lesscwbTable").html("");
				for ( var i = 0; i < data.length; i++) {
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"'>"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='120' align='center'>"+data[i].transcwb+"</td>"
					+"<td width='120' align='center'>"+data[i].packagecode+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td align='left'> "+data[i].consigneeaddress+"</td>"
					+ "</tr>";
				}
				$("#lesscwbTable").append(optionstring);
			}
		}
	});
}

<%-- function getdaohuocwbquejianmoredataList(){
	$.ajax({
		type : "POST",
		url : "<%=request.getContextPath()%>/PDA/getDaoHuoQueList",
		dataType : "json",
		success : function(data) {
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='120' align='center'>"+data[i].transcwb+"</td>"
					+"<td width='120' align='center'>"+data[i].packagecode+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
					+ "</tr>";
				}
				$("#ypdjdh").remove();
				$("#lesscwbTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
					var more='<tr align="center"  ><td  colspan="6" style="cursor:pointer" onclick="getdaohuocwbquejianmoredataList();" id="ypdjdh">查看更多</td></tr>';
					$("#lesscwbTable").append(more);
				}
			}
		}
	});
} --%>

/**================== 揽件未到站 =========================*/
function getlanjianweidaozhan(){
	lanjianweipage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbranchimportlanjianweidaolist",
		data:{"page":lanjianweipage},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"'>"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'>"+data[i].customername+"</td>"
					+"<td width='100' align='center'> "+data[i].cwbordertype+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='140' align='left'> "+data[i].consigneeaddress+"</td>"
					+"<td  align='left'> "+data[i].pickaddress+"</td>"
					+ "</tr>";
				}
				$("#lanjianweidaozhan").remove();//删除‘查看更多’的按钮
				$("#lanjianweidaohuoTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
				var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>8<%}else{ %>7<%} %>" style="cursor:pointer" onclick="getlanjianweidaozhan();" id="lanjianweidaozhan">查看更多</td></tr>';
				$("#lanjianweidaohuoTable").append(more);
				}
			}
		}
	});
}



//=========合包到货=============
function baledaohuo(scancwb,driverid,requestbatchno){
	if($("#baleno").val()==""){
		alert("包号不能为空！");
		$("#scancwb").val("");
		return;
	}
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/bale/baledaohuo/"+$("#baleno").val()+"/"+scancwb+"?driverid="+driverid+"&requestbatchno="+requestbatchno,
		dataType : "json",
		success : function(data) {
			$("#msg").html("");
			$("#msg").html(data.body.errorinfo);
			$("#scancwb").val("");
			newPlayWav(data.wavPath);
			<%-- errorvedioplay("<%=request.getContextPath()%>",data); --%>
		}
	});
}
</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="saomiao_box2">

	<div class="saomiao_tab2">
		<ul>
			<li><a href="#"  class="light">逐单操作</a></li>		
			<li><a href="<%=request.getContextPath()%>/PDA/cwbbranchintowarhouseBatch">批量操作</a></li>
		</ul>
	</div>


	<div class="saomiao_topnum2">
		<dl class="blue">
			<dt>今日未到货</dt>
			<dd style="cursor:pointer" onclick="tabView('table_jinriweidaohuo')" id="jinriweidaohuodanshu"></dd>
		</dl>
		<dl class="yellow">
			<dt>历史未到货</dt>
			<dd style="cursor:pointer" onclick="tabView('table_historyweidaohuo')" id="historyweidaohuodanshu"></dd>
		</dl>
		<c:if test="${yuyuedaService=='yes'}">
			<dl class="green">
			<dt>预约达未到货</dt>
			<dd style="cursor:pointer"  id="yuyuedaweidaohuocount"></dd>
		</dl>
		<dl class="blue">
			<dt>预约达已扫描</dt>
			<dd style="cursor:pointer"  id="yuyuedayidaohuocount"></dd>
		</dl>
		</c:if>
		<dl class="green">
			<dt>已扫描</dt>
			<dd style="cursor:pointer" onclick="tabView('table_yidaohuo')" id="successcwbnum" name="successcwbnum">0</dd>
		</dl>
		<dl class="yellow">
			<dt>一票多件缺货件数</dt>
			<dd style="cursor:pointer" onclick="tabView('table_quejian');"  id="lesscwbnum" name="lesscwbnum" >0</dd>
		</dl>
		<!-- dmp4.2 oxo: 增加揽收未到站统计 by jinghui.pan@pjbest.com -->
		<dl class="green">
			<dt>揽收未到站</dt>
			<dd style="cursor:pointer" onclick="tabView('table_oxo_lanjianweidaozhan');"  id="lanjianweidaozhancount" >0</dd>
		</dl>
		
		<input type="button"  id="refresh" value="刷新" onclick="location.href='<%=request.getContextPath() %>/PDA/branchimortdetail'" style="float:left; width:100px; height:65px; cursor:pointer; border:none; background:url(../images/buttonbgimg1.gif) no-repeat; font-size:18px; font-family:'微软雅黑', '黑体'"/>
		
		<br clear="all"/>
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div id="Tag" class="saomiao_tab">
				<ul>
					<li><a href="#" id="cwbTab" onclick="clearMsg();$(function(){$('#baleno').parent().hide();$('#baleno').val('');$('#scancwb').val('');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();})" class="light">扫描订单</a></li>
					<li><a href="#" id="balenoTab" onclick="clearMsg();$(function(){$('#baleno').parent().show();$('#baleno').show();$('#baleno').val('');$('#baleno').focus();$('#scancwb').val('');$('#scancwb').hide();$('#scancwb').parent().hide();})">合包到货</a></li>
				</ul>
			</div>
			<div class="saomiao_righttitle" id="pagemsg"></div>
			<!-- <form action="" method="get"> -->
			<div class="saomiao_selet2">
				驾驶员：
				<select id="driverid" name="driverid" class="select1">
					<option value="-1" selected>请选择</option>
					<%for(User u : uList){ %>
						<option value="<%=u.getUserid() %>" ><%=u.getRealname() %></option>
					<%} %>
				</select>
			</div>
			<div class="saomiao_inwrith2">
				<div class="saomiao_left2">
					<p style="display: none;"><span>包号：</span>
						<input type="text" class="saomiao_inputtxt2" value=""  id="baleno" name="baleno" onKeyDown="if(event.keyCode==13&&$(this).val().length>0){$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();}"/>
					</p>
					<p><span>单/包号：</span>
						<input type="text" class="saomiao_inputtxt2" id="scancwb" name="scancwb" value="" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){submitBranchImport("<%=request.getContextPath()%>",$(this).val(),-1,$("#driverid").val(),$("#requestbatchno").val(),$("#rk_switch").val(),"");}'/>
					</p>
				</div>
				<div class="saomiao_right2">
					<p id="msg" name="msg" ></p>
					<p id="showcwb" name="showcwb"></p>
					<p id="cwbgaojia" name="cwbgaojia" style="display: none" >高价</p>
					<p id="dingdanlanjie" name="dingdanlanjie" style="display: none" ><font color="red" size="12">订单拦截</font></p>
					<p id="excelbranch" name="excelbranch" ></p>
					<p id="customername" name="customername" ></p>
					<p id="address" name="address" ></p>
					<p id="cwbDetailshow" name="cwbDetailshow"></p>
					<p id="customercommand" name="customercommand"></p>
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
		<div id="smallTag"class="saomiao_tab2">
			<span style="float: right; padding: 10px">
			<input  class="input_button1" type="button" name="littlefalshbutton" id="flash" value="刷新" onclick="location.href='<%=request.getContextPath() %>/PDA/branchimortdetail'" />
			</span>
			<ul>
				<li><a id="table_jinriweidaohuo" href="#" class="light">今日未到货</a></li>
				<li><a id="table_historyweidaohuo" href="#">历史未到货</a></li>
				<li><a id="table_yidaohuo" href="#">已扫描明细</a></li>
				<!-- <li><a id="table_youhuowudan" href="#">有货无单明细</a></li> -->
				<li><a id="table_quejian" href="#" onclick='getdaohuocwbquejiandataList();'>一票多件缺件</a></li>
				<li><a href="#">异常单明细</a></li>
				<li><a id="table_oxo_lanjianweidaozhan" href="#">揽收未到站</a></li>
			</ul>
		</div>
		<div id="ViewList" class="tabbox">
			<li>
				<input type ="button" id="btnval" value="导出Excel" class="input_button1" onclick='exportField(1);'/>
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr>
							<td width="100%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="2"
									class="table_5">
									<tr>
										<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
										<td width="100" align="center" bgcolor="#f1f1f1">包号</td>
										<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
										<td width="140" align="center" bgcolor="#f1f1f1">出库时间</td>
										<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
										<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
										<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
										<!-- <td width="140" align="center" bgcolor="#f1f1f1">出库时间</td> -->
										<td align="center" bgcolor="#f1f1f1">地址</td>
									</tr>
								</table>
								<div style="height: 160px; overflow-y: scroll">
									<table id="jinriweidaohuoTable" width="100%" border="0" cellspacing="1" cellpadding="2"
										class="table_2">
										<%for(CwbDetailView co : jinriweidaohuolist){ %>
										<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>">
											<td width="120" align="center"><%=co.getCwb() %></td>
											<td width="100" align="center"><%=co.getPackagecode() %></td>
											<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
											<td width="140"><%=co.getOutstoreroomtime() %></td>
											<td width="100"><%=co.getConsigneename() %></td>
											<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
											<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView()==null?"":co.getRemarkView() %></td>
												<%} %>
											<%-- <td width="140"><%=co.getOutstoreroomtime() %></td> --%>
											<td align="left"><%=co.getConsigneeaddress() %></td>
											
										</tr>
										<%} %>
										<%if(jinriweidaohuolist!=null&&jinriweidaohuolist.size()==Page.DETAIL_PAGE_NUMBER){ %>
											<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>8<%}else{ %>7<%} %>" style="cursor:pointer" onclick="jinriweidaohuo();" id="jinriweidaohuo">查看更多</td></tr>
										<%} %>
									</table>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</li>
			<li style="display: none">
				<input type ="button" id="btnval" value="导出Excel" class="input_button1" onclick='exportField(5);'/>
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr>
							<td width="100%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="2"
									class="table_5">
									<tr>
										<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
										<td width="100" align="center" bgcolor="#f1f1f1">包号</td>
										<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
										<td width="140" align="center" bgcolor="#f1f1f1">出库时间</td>
										<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
										<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
										<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
										<!-- <td width="140" align="center" bgcolor="#f1f1f1">出库时间</td> -->
										<td align="center" bgcolor="#f1f1f1">地址</td>
									</tr>
								</table>
								<div style="height: 160px; overflow-y: scroll">
									<table id="jinriweidaohuoTable" width="100%" border="0" cellspacing="1" cellpadding="2"
										class="table_2">
										<%for(CwbDetailView co : historyweidaohuolist){ %>
										<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>">
											<td width="120" align="center"><%=co.getCwb() %></td>
											<td width="100" align="center"><%=co.getPackagecode() %></td>
											<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
											<td width="140"><%=co.getOutstoreroomtime() %></td>
											<td width="100"><%=co.getConsigneename() %></td>
											<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
											<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView()==null?"":co.getRemarkView() %></td>
												<%} %>
											<%-- <td width="140"><%=co.getOutstoreroomtime() %></td> --%>
											<td align="left"><%=co.getConsigneeaddress()==null?"":co.getConsigneeaddress() %></td>
											
										</tr>
										<%} %>
										<%if(historyweidaohuolist!=null&&historyweidaohuolist.size()==Page.DETAIL_PAGE_NUMBER){ %>
											<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>8<%}else{ %>7<%} %>" style="cursor:pointer" onclick="historyweidaohuo();" id="historyweidaohuo">查看更多</td></tr>
										<%} %>
									</table>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</li>
			<li style="display: none">
				<input type ="button" id="btnval" value="导出Excel" class="input_button1" onclick="exportField(2);"/>
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
										<td width="140" align="center" bgcolor="#f1f1f1">到货时间</td>
										<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
										<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
										<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
										<!-- <td width="140" align="center" bgcolor="#f1f1f1">出库时间</td> -->
										<td align="center" bgcolor="#f1f1f1">地址</td>
										
									</tr>
								</table>
								<div style="height: 160px; overflow-y: scroll">
									<table id="successTable" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
									<%for(CwbDetailView co : yidaohuolist){ %>
										<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>">
											<td width="120" align="center"><%=co.getCwb() %></td>
											<td width="100" align="center"><%=co.getPackagecode() %></td>
											<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
											<td width="140"><%=co.getInSitetime() %></td>
											<td width="100"><%=co.getConsigneename() %></td>
											<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
											<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView()==null?"":co.getRemarkView() %></td>
												<%} %>
											<%-- <td width="140"><%=co.getOutstoreroomtime() %></td> --%>
											<td align="left"><%=co.getConsigneeaddress() %></td>
										</tr>
										<%} %>
										<%if(yidaohuolist!=null&&yidaohuolist.size()==Page.DETAIL_PAGE_NUMBER){ %>
											<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>8<%}else{ %>7<%} %>" style="cursor:pointer" onclick="yidaohuo();" id="yidaohuo">查看更多</td></tr>
										<%} %>
									</table>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</li>
			<li style="display: none">
				<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(4);'/>
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr>
							<td width="10%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="2"
									class="table_5">
									<tr>
										<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
										<td width="120" align="center" bgcolor="#f1f1f1">运单号</td>
										<td width="120" align="center" bgcolor="#f1f1f1">包号</td>
										<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
										<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
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
				<input type ="button" id="btnval" value="导出Excel" class="input_button1" onclick="exportField(3);"/>
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
										<td width="140" align="center" bgcolor="#f1f1f1">出库时间</td>
										<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
										<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
										<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
										<!-- <td width="140" align="center" bgcolor="#f1f1f1">出库时间</td> -->
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
			<!-- 揽件未到站table view-->
			<li style="display: none">
				<input type ="button" id="btnval" value="导出Excel" class="input_button1" onclick='exportField(6);'/>
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr>
							<td width="100%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="2"
									class="table_5">
									<tr>
										<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
										<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
										<td width="100" align="center" bgcolor="#f1f1f1">订单类型</td>
										<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
										<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>	
										<td width="250" align="center" bgcolor="#f1f1f1">收件地址</td>								
										<td  align="center" bgcolor="#f1f1f1">揽件地址</td>
			
									</tr>
								</table>
								<div style="height: 160px; overflow-y: scroll">
									<table id="lanjianweidaohuoTable" width="100%" border="0" cellspacing="1" cellpadding="2"
										class="table_2">
										<%for(CwbDetailView co : lanjianweidaohuolist){ %>
										<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>">
											<td width="120" align="center"><%=co.getCwb() %></td>
											<td width="100" align="center"><%=co.getCustomername() %></td>
											<td width="100"><%=co.getCwbordertype() %></td>
											<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
											<td width="100"><%=co.getConsigneename() %></td>
											<td width="250" align="left"><%=co.getConsigneeaddress() %></td>
											<td align="left"><%=co.getPickaddress() %></td>
										</tr>
										<%} %>
										<%if(lanjianweidaohuolist!=null&&lanjianweidaohuolist.size()==Page.DETAIL_PAGE_NUMBER){ %>
											<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>8<%}else{ %>7<%} %>" style="cursor:pointer" onclick="getlanjianweidaozhan();" id="lanjianweidaozhan">查看更多</td></tr>
										<%} %>
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
	<form action="<%=request.getContextPath() %>/PDA/branchimportexport" method="post" id="searchForm3">
		<input type="hidden" name="type" value="" id="type"/>
	</form>
</div>
</body>
</html>
