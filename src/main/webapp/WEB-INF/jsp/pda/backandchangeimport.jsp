<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch,cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%


List<CwbOrder> weituihuorukuList = (List<CwbOrder>)request.getAttribute("weituihuorukuList");
List<CwbOrder> yituihuorukuList = (List<CwbOrder>)request.getAttribute("yituihuorukuList");

List<CwbOrder> ypeisonglist = (List<CwbOrder>)request.getAttribute("ypeisong");
List<CwbOrder> yshangmenhuanlist = (List<CwbOrder>)request.getAttribute("yshangmenhuan");
List<CwbOrder> yshangmentuilist = (List<CwbOrder>)request.getAttribute("yshangmentui");
List<CwbOrder> wpeisongList = (List<CwbOrder>)request.getAttribute("wpeisong");
List<CwbOrder> wshangmenhuanList = (List<CwbOrder>)request.getAttribute("wshangmenhuan");
List<CwbOrder> wshangmentuiList = (List<CwbOrder>)request.getAttribute("wshangmentui");

List<CwbOrder> zweituihuorukuList = (List<CwbOrder>)request.getAttribute("zweituihuorukuList");
List<CwbOrder> zyituihuorukuList = (List<CwbOrder>)request.getAttribute("zyituihuorukuList");

List<CwbOrder> zypeisong = (List<CwbOrder>)request.getAttribute("zypeisong");
List<CwbOrder> zyshangmenhuan = (List<CwbOrder>)request.getAttribute("zyshangmenhuan");
List<CwbOrder> zyshangmentui = (List<CwbOrder>)request.getAttribute("zyshangmentui");
List<CwbOrder> zwpeisong = (List<CwbOrder>)request.getAttribute("zwpeisong");
List<CwbOrder> zwshangmenhuan = (List<CwbOrder>)request.getAttribute("zwshangmenhuan");
List<CwbOrder> zwshangmentui = (List<CwbOrder>)request.getAttribute("zwshangmentui");

List<User> uList = (List<User>)request.getAttribute("userList");
List<Customer> cList = (List<Customer>)request.getAttribute("customerlist");
long isscanbaleTag= request.getAttribute("isscanbaleTag")==null?1:Long.parseLong(request.getAttribute("isscanbaleTag").toString());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>退货站入库扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$(function(){
	getcwbsdataForBack();
	$("#scancwb").focus();
});

$(function() {
	var $menuli = $(".saomiao_tab ul li");
	$menuli.click(function() {
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
});

function tabView(tab){
	$("#"+tab).click();
}
function tabView(tab,tip){

	$("#"+tab).click();
	$("div[id^='y']").attr('style','display: none;');
	$("div[id^='w']").attr('style','display: none;');
	$("#"+tip).attr('style','height: 160px; overflow-y: scroll;');
	$("#extype").attr('value',tip);
	$('#view').attr('style','display:');
	$('#extype').val(tip);
	
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
	$("#consigneeaddress").html("");
	$("#customername").html("");
	$("#cwbgaojia").hide();
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
function getcwbsdataForBack(){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath() %>/PDA/getBackAndChangeInSum",
		dataType:"json",
		success : function(data) {
			$("#yps").html(data.yps);
			$("#ysmt").html(data.ysmt);
			$("#ysmh").html(data.ysmh);
			$("#wps").html(data.wps);
			$("#wsmt").html(data.wsmt);
			$("#wsmh").html(data.wsmh);
			$("#zyps").html(data.zyps);
			$("#zysmt").html(data.zysmt);
			$("#zysmh").html(data.zysmh);
			$("#zwps").html(data.zwps);
			$("#zwsmt").html(data.zwsmt);
			$("#zwsmh").html(data.zwsmh);
		}
	});
}


/**
 * 退货站入库扫描
 */
function submitBackIntoWarehouse(pname,scancwb,driverid,comment){
/* 	alert($('#tip').val()); */
	if(scancwb.length>0){
		if($('#tip').val()=='goods')
			{
						$("#tr>table").remove();
						$("#view").attr('style','display:none');
						$("#msg").html("");
						$("#cwbgoods").attr('value',$("#scancwb").val());
						$("#excelbranch").html("");
						$("#customername").html("");
						$("#showcwb").html("");
						$("#consigneeaddress").html("");
			$.ajax({
				type: "POST",
				url:pname+"/PDA/showgoodsdetail/"+scancwb,
				dataType:"json",
				success : function(data) {
					$("#scancwb").val("");
					if(data.statuscode=="000000"){
						$('#tt').attr('style','display:');
						var goods=data.body.orderGoodsList;
						var tr="<table width='100%' border='0' cellspacing='1' cellpadding='2' class='table_2' >"
								+ "<tr><td nowrap='nowrap'>应退商品编码</td>"
								+ "<td nowrap='nowrap'>应退商品名称</td>"
								+ "<td nowrap='nowrap'>应退商品规格</td>"
								+ "<td nowrap='nowrap'>应退商品数量</td>"
								+ "<td nowrap='nowrap'>实退商品数量</td>"
								+ "<td nowrap='nowrap'>退货出站数量</td>"
								+ "<td nowrap='nowrap'>退货站入库数量</td>"
								+ "<td nowrap='nowrap'>未退商品数量</td>"
								+ "<td nowrap='nowrap'>特批退货数量</td>"
								+ "<td nowrap='nowrap'>未退商品原因</td>"
								+ "<td nowrap='nowrap'>备注</td></tr>";
						for ( var i=0;i<goods.length;i++) {
							
							tr+="<tr>";
							tr+="<td>"+goods[i].goods_code+"</td>";
							tr+="<td>"+goods[i].goods_name+"</td>";
							tr+="<td>"+goods[i].goods_spec+"</td>";
							tr+="<td>"+goods[i].goods_num+"</td>";
							tr+="<td>"+goods[i].shituicount+"</td>";
							tr+="<td>"+goods[i].shituicount+"</td>";
							tr+="<td><input type='text' id='thzrkcount' onblur='goodCounts("+goods[i].shituicount+",$(this).val())' name='thzrkcount' counts="+goods[i].shituicount+" value="+goods[i].shituicount+" size='6'/></td>";
							tr+="<td>"+goods[i].weituicount+"</td>";
							tr+="<td>"+goods[i].tepituicount+"</td>";
							tr+="<td>"+goods[i].weituireason+"</td>";
							tr+="<td>"+goods[i].remark1+"</td>";
							tr+="<input type='hidden' id='id' name='id' value="+goods[i].id+" size='6'/>";
							//tr+="<input type='hidden' id='thzrkcount' name='thzrkcount' value="+goods[i].thzrkcount+" size='6'/>";
							tr+="</tr>";
						}
						tr+="</table>";
						$("#tr").append(tr);
				}
					else{
						if(data.statuscode=="222222")
							{$("#msg").html("         无此单号！");}
						else
						$("#msg").html("         该订单不存在任何商品，请选择其它入库方式！");
					}
				}}
			);
			return false;
			}
		if($("#scanbaleTag").attr("class")=="light"){//入库根据包号扫描订单
			baledaohuo(scancwb,driverid,comment);
		}else{//入库
			$.ajax({
				type: "POST",
				url:pname+"/PDA/cwbbackintowarhouse/"+scancwb+"?driverid="+driverid+"&customerid="+$("#customerid").val()+"&checktype=1",
				data:{
					"comment":comment
				},
				dataType:"json",
				success : function(data) {
					$("#scancwb").val("");
					if(data.statuscode=="000000"){
						$("#cwbgaojia").hide();
						if(data.body.cwbOrder.deliverybranchid!=0){
							$("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
						}else{
							$("#excelbranch").html("尚未匹配站点");
						}
						
						$("#customername").html(data.body.cwbcustomername);
						$("#multicwbnum").val(data.body.cwbOrder.sendcarnum);
						$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");

						if(data.body.cwbbranchnamewav!=pname+"/wav/"){
							$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random());
						}else{
							$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
						}
						/*
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
						$("#scansuccesscwb").val(scancwb);
						$("#showcwb").html("订 单 号："+scancwb);
						$("#consigneeaddress").html("地 址："+data.body.cwbOrder.consigneeaddress);
					}else{
						$("#excelbranch").hide();
						$("#customername").hide();
						$("#cwbgaojia").hide();
						$("#damage").hide();
						$("#multicwbnum").hide();
						$("#multicwbnum").val("1");
						$("#showcwb").html("");
						$("#consigneeaddress").html("");
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
function goodCounts(counts,val)
{
	//alert(counts+","+val);
/*     if(!/^[0-9]*$/.test(val)){
        alert("请输入数字!");
        return false;
    }
    if(val>counts){
        alert("退货站入库数量:"+val+" 不能大于实退商品数量:"+counts);
        return false;
    }
    if(""==val){
        alert("退货站入库数量不能为空！");
        return false;
    } */
	}
function updategoods()
{ var flag=true;
var json="{ thzrkcount:[";

	var sum=0;
	$("input[name=thzrkcount]").each(function(){
    nameValue = $(this).val();
    counts = $(this).attr("counts");
    sum+=nameValue;
    if(!/^[0-9]*$/.test(nameValue)){
        alert("请输入正确的数字!");
        $(this).focus();
        flag=false;
    }
    if(""==nameValue){
        alert("退货站入库数量不能为空！");
        $(this).focus();
        flag=false;
    }
    if(nameValue>counts){
    	alert("退货站入库数量:"+nameValue+" 不能大于实退商品数量:"+counts);
    	   $(this).focus();
        flag=false;
    }
    json+=nameValue+","
    
});
	json=json.substring(0, json.length-1);
	json+="],id:["
		$("input[name=id]").each(function(){
		    id = $(this).val();
		    json+=id+","    
		});
	json=json.substring(0, json.length-1);
	json+="]}"
	
	if(sum==0)
	{$("input[name=thzrkcount]")[0].focus();
		alert("入库商品数量全部为0!");
		return false;
	}

if(flag){
 	var jsonuserinfo = $('#form1').serializeObject();  
	var jsonval=JSON.stringify(jsonuserinfo);
	/* var a = eval('(' + jsonval + ')');
	alert(a.id); */
	var pname="<%=request.getContextPath()%>";
	
	//alert(urlstr);
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/PDA/updategoodthzrkcount?cwb="+$("#cwbgoods").val()+"&driverid="+$("#driverid").val()+"&comment="+$("#comment").val()+"&customerid="+$("#customerid").val()+"&checktype=1",
		//data:{"thzrkcount":$("#thzrkcount").val(),"id":$("#id").val()},
		//data:"jasonval="+ encodeURI(jsonval),
		data:"jasonval="+ encodeURI(json),
		//data:{id : a},
		dataType:"json",
		success : function(data) {
			$("#scancwbTag1").click();
			$("#tr>table").remove();
			//$("#view").attr('style','display:none');
				if(data.statuscode=="000000")
					{
					$("#cwbgaojia").hide();
					if(data.body.cwbOrder.deliverybranchid!=0){
						$("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
					}else{
						$("#excelbranch").html("尚未匹配站点");
					}
					
					$("#customername").html(data.body.cwbcustomername);
					$("#multicwbnum").val(data.body.cwbOrder.sendcarnum);
					$("#msg").html($("#cwbgoods").val()+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
					$("#scansuccesscwb").val($("#cwbgoods").val());
					$("#showcwb").html("订 单 号："+$("#cwbgoods").val());
					$("#consigneeaddress").html("地 址："+data.body.cwbOrder.consigneeaddress);
					if(data.body.cwbbranchnamewav!=pname+"/wav/"){
						$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random());
					}else{
						$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
					}
					}
				else
					{
					$("#excelbranch").hide();
					$("#customername").hide();
					$("#cwbgaojia").hide();
					$("#damage").hide();
					$("#multicwbnum").hide();
					$("#multicwbnum").val("1");
					$("#showcwb").html("");
					$("#consigneeaddress").html("");
					$("#msg").html("         （异常扫描）"+data.errorinfo);
					addAndRemoval($("#cwbgoods").val(),"errorTable",false);
					}
				$("#responsebatchno").val(data.responsebatchno);
				batchPlayWav(data.wavList);
		}
		}
	);
}
	}
	
$.fn.serializeObject = function()    
{    
   var o = {};    
   var a = this.serializeArray();    
   $.each(a, function() {    
       if (o[this.name]) {    
           if (!o[this.name].push) {    
               o[this.name] = [o[this.name]];    
           }    
           o[this.name].push(this.value || '');    
       } else {    
           o[this.name] = this.value || '';    
       }    
   });    
   return o;    
};
function onClik(){  
    //var data = $("#form1").serializeArray(); //自动将form表单封装成json  
    //alert(JSON.stringify(data));  
    var jsonuserinfo = $('#form1').serializeObject();  
    alert(JSON.stringify(jsonuserinfo));  
}  
function exportField(){
	var cwbs = "";
	var extype = $("#extype").val();
	 if(extype == "yichang"){
		$("#errorTable tr").each(function(){
			var cwb = $(this).attr("cwb");
			cwbs += "'" + cwb + "',";
		});
		if(cwbs.length>0){
			cwbs = cwbs.substring(0, cwbs.length-1);
		}
		if(cwbs!=""){
			$("#exportmould2").val($("#exportmould").val());
			$("#cwbs").val(cwbs);
			$("#btnval").attr("disabled","disabled");
		 	$("#btnval").val("请稍后……");
		 	alert("----");
			$("#searchForm").submit();
		}else{
			alert("没有订单信息，不能导出！");
		};
	}else{
	 $("#searchForm2").submit();
	}
}


var weipage=1;
var yipage=1;
function yiruku(){
	yipage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbackimportyirukulist",
		data:{"page":yipage},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"'customerid='"+data[i].customerid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
					+ "</tr>";
				}
				$("#yiruku").remove();
				$("#successTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
					var more='<tr align="center"  ><td  colspan="6" style="cursor:pointer" onclick="yiruku();" id="yiruku">查看更多</td></tr>';
					$("#successTable").append(more);
				}
				
			}
		}
	});
};
//包号
 $(function(){
	var $menuli = $(".saomiao_tab ul li");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		/* $(".tabbox li").eq(index).show().siblings().hide(); */
	});
	
}) 

 function weiruku(){
	 weipage+=1;
	  $.ajax({
			type:"post",
			url:"<%=request.getContextPath()%>/PDA/getbackimportweirukulist",
			data:{"page":weipage},
			success:function(data){
				if(data.length>0){
					var optionstring = "";
					for ( var i = 0; i < data.length; i++) {
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"'customerid='"+data[i].customerid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					}
					$("#weiruku").remove();
					$("#weituihuorukuTable").append(optionstring);
					if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
					var more='<tr align="center"  ><td  colspan="6" style="cursor:pointer" onclick="weiruku();" id="weiruku">查看更多</td></tr>';
					$("#weituihuorukuTable").append(more);
					}
				}
			}
		});
 };
 
 
//=========合包到货=============
 function baledaohuo(scancwb,driverid,comment){
 	if($("#baleno").val()==""){
 		alert("包号不能为空！");
 		$("#scancwb").val("");
 		return;
 	}
 	$.ajax({
 		type: "POST",
 		url:"<%=request.getContextPath()%>/bale/baletuihuodaohuo/"+$("#baleno").val()+"/"+scancwb+"?driverid="+driverid,
 		data:{
			"comment":comment
		},
 		dataType : "json",
 		success : function(data) {
 			clearMsg();
 			$("#msg").html(data.body.errorinfo);
 			$("#scancwb").val("");
 			errorvedioplay("<%=request.getContextPath()%>",data);
 		}
 	});
 }
</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="saomiao_box">

	<div class="saomiao_topnum">
		<!-- <dl style="cursor:pointer" onclick="tabView('table_weituihuoruku')"  class="blue">
			<dt>未入库合计</dt>
			<dd id="rukukucundanshu"></dd>
		</dl> -->
		<dl class="blue" >
			<dt>退货未入库</dt>
			<!-- <dd  style="cursor:pointer" onclick="tabView('table_weituihuochuku')" id="chukukucundanshu">0</dd> -->
			
					<table  style="width: 100%;height:60%;font-size: 15px;" cellpadding="0px" cellspacing="0px">
						<tr align="center" valign="middle" bgcolor="#ADCFF3" >
							<td style="color: #030;"><strong>配送</strong></td>
							<td style="color: #030;"><strong>上门退</strong></td>
							<td style="color: #030;"><strong>上门换</strong></td>
						</tr>
						<tr align="center" valign="bottom">
							<td style="cursor:pointer" onclick="tabView('table_weituihuoruku','wpeisong')" id="chukukucundanshu"><strong><span id="wps">0</span></strong></td>
							<td style="cursor:pointer" onclick="tabView('table_weituihuoruku','wshangmengtui')" id="chukukucundanshu"><strong><span id="wsmt">0</span></strong></td>
							<td style="cursor:pointer" onclick="tabView('table_weituihuoruku','wshangmenghuan')" id="chukukucundanshu"><strong><span id="wsmh">0</span></strong></td>
						</tr>
					</table>
					
		</dl>
		<!-- <dl style="cursor:pointer" onclick="tabView('table_yituihuoruku')" class="green">
			<dt>已入库</dt>
			<dd id="successcwbnum" name="successcwbnum">0</dd>
		</dl> -->
		<dl class="green">
			<dt>退货已入库</dt>
			<!-- <dd style="cursor:pointer" onclick="tabView('table_yituihuochuku')" id="alloutnum">0</dd> -->
					<table  style="width: 100%;height:60%;font-size: 15px;" cellpadding="0px" cellspacing="0px">
						<tr align="center" valign="middle" bgcolor="#D0E9BC" style="color: #131313;">
							<td style="color: #030;"><strong>配送</strong></td>
							<td style="color: #030;"><strong>上门退</strong></td>
							<td style="color: #030;"><strong>上门换</strong></td>
						</tr>
						<tr align="center" valign="bottom">
							<td style="cursor:pointer" onclick="tabView('table_yituihuoruku','ypeisong')" id="chukukucundanshu"><strong><span id="yps">0</span></strong></td>
							<td style="cursor:pointer" onclick="tabView('table_yituihuoruku','yshangmengtui')" id="chukukucundanshu"><strong><span id="ysmt">0</span></strong></td>
							<td style="cursor:pointer" onclick="tabView('table_yituihuoruku','yshangmenghuan')" id="chukukucundanshu"><strong><span id="ysmh">0</span></strong></td>
						</tr>
					</table>
		</dl>
		<dl class="blue" >
			<dt>中转未入库</dt>
			<!-- <dd  style="cursor:pointer" onclick="tabView('table_weituihuochuku')" id="chukukucundanshu">0</dd> -->
			
					<table  style="width: 100%;height:60%;font-size: 15px;" cellpadding="0px" cellspacing="0px">
						<tr align="center" valign="middle" bgcolor="#ADCFF3" >
							<td style="color: #030;"><strong>配送</strong></td>
							<td style="color: #030;"><strong>上门退</strong></td>
							<td style="color: #030;"><strong>上门换</strong></td>
						</tr>
						<tr align="center" valign="bottom">
							<td style="cursor:pointer" onclick="tabView('table_zweituihuoruku','wpeisongz')" id="zpchukukucundanshu"><strong><span id="zwps">0</span></strong></td>
							<td style="cursor:pointer" onclick="tabView('table_zweituihuoruku','wshangmengtuiz')" id="zstchukukucundanshu"><strong><span id="zwsmt">0</span></strong></td>
							<td style="cursor:pointer" onclick="tabView('table_zweituihuoruku','wshangmenghuanz')" id="zshchukukucundanshu"><strong><span id="zwsmh">0</span></strong></td>
						</tr>
					</table>
					
		</dl>
		<!-- <dl style="cursor:pointer" onclick="tabView('table_yituihuoruku')" class="green">
			<dt>已入库</dt>
			<dd id="successcwbnum" name="successcwbnum">0</dd>
		</dl> -->
		<dl class="green">
			<dt>中转已入库</dt>
			<!-- <dd style="cursor:pointer" onclick="tabView('table_yituihuochuku')" id="alloutnum">0</dd> -->
					<table  style="width: 100%;height:60%;font-size: 15px;" cellpadding="0px" cellspacing="0px">
						<tr align="center" valign="middle" bgcolor="#D0E9BC" style="color: #131313;">
							<td style="color: #030;"><strong>配送</strong></td>
							<td style="color: #030;"><strong>上门退</strong></td>
							<td style="color: #030;"><strong>上门换</strong></td>
						</tr>
						<tr align="center" valign="bottom">
							<td style="cursor:pointer" onclick="tabView('table_zyituihuoruku','ypeisongz')" id="zwpchukukucundanshu"><strong><span id="zyps">0</span></strong></td>
							<td style="cursor:pointer" onclick="tabView('table_zyituihuoruku','yshangmengtuiz')" id="zwstchukukucundanshu"><strong><span id="zysmt">0</span></strong></td>
							<td style="cursor:pointer" onclick="tabView('table_zyituihuoruku','yshangmenghuanz')" id="zwshchukukucundanshu"><strong><span id="zysmh">0</span></strong></td>
						</tr>
					</table>
		</dl>
		<input type="button"  id="refresh" value="刷新" onclick="location.href='<%=request.getContextPath() %>/PDA/backandchangeimport'"  style="float:left; width:100px; height:65px; cursor:pointer; border:none; background:url(../images/buttonbgimg1.gif) no-repeat; font-size:18px; font-family:'微软雅黑', '黑体'"/>
		<br clear="all">
	</div>
	<input id="tip" type="hidden" />
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
		<div id="Tag" class="saomiao_tab">
				<ul id="bigTag">
					<li><a href="#" id="scancwbTag" onclick="clearMsg();$(function(){$('#tt').attr('style','display:none');$('#view').attr('style','display:');$('#goods').attr('style','display:none');$('#goods').attr('style','display:none');$('#tip').val('');$('#baleno').parent().hide();$('#baleno').val('');$('#scancwb').val('');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();})" class="light">扫描订单</a></li>
					<!-- <li><a href="#" id="scanbaleTag" onclick="clearMsg();$(function(){$('#tt').attr('style','display:none');$('#view').attr('style','display:');$('#goods').attr('style','display:none');$('#tip').val('');$('#baleno').parent().show();$('#baleno').show();$('#finish').parent().show();$('#finish').show();$('#baleno').val('');$('#baleno').focus();$('#scancwb').val('');$('#scancwb').parent().hide();})">合包到货</a></li>
					<li><a href="#" id="scancwbTag1" onclick="clearMsg();$(function(){$('#tt').attr('style','display:none');$('#view').attr('style','display:none');$('#goods').attr('style','display:');$('#tip').val('goods');$('#baleno').parent().hide();$('#baleno').val('');$('#scancwb').val('');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();})" class="light1">按商品明细入库</a></li> -->
				</ul>
			</div>
			<div class="saomiao_righttitle2" id="pagemsg"></div>
			<div class="saomiao_selet2">
				驾驶员：
				<select id="driverid" name="driverid">
					<option value="-1" selected>请选择</option>
					<%for(User u : uList){ %>
						<option value="<%=u.getUserid() %>" ><%=u.getRealname() %></option>
					<%} %>
				</select>
				&nbsp;&nbsp;供货商：
				<select name ="customerid" id ="customerid">
	          <option value ="-1">请选择</option>
	          <%if(cList!=null&&cList.size()>0){ %>
	            <%for(Customer c : cList){ %>
		          <option value ="<%=c.getCustomerid()%>"  ><%=c.getCustomername()%></option>
		        <%}}%>
			</select> 
			</div>
			<div class="saomiao_inwrith2">
				<div class="saomiao_left2">
					<p><span>备注：</span>
						<input type="text" class="inputtext_2" id="comment" name="comment" value="" maxlength="50" />
					</p>
					<p style="display: none;"><span>包号：</span>
						<input type="text" class="saomiao_inputtxt2" value=""  id="baleno" name="baleno" onKeyDown="if(event.keyCode==13&&$(this).val().length>0){$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();}"/>
					</p>
					<p><span>订单号：</span>
						<input type="text" class="saomiao_inputtxt2" id="scancwb" name="scancwb" value="" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){submitBackIntoWarehouse("<%=request.getContextPath()%>",$(this).val(),$("#driverid").val(),$("#comment").val());}'/>
					</p>
					<%-- <p><span>订单号：</span>
						<input type="text" class="saomiao_inputtxt3" id="scancwb1" name="scancwb1" value="" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){submitBackIntoWarehouse("<%=request.getContextPath()%>",$(this).val(),$("#driverid").val(),$("#comment").val());}'/>
					</p> --%>
				</div>
			
				<div class="saomiao_right2">
					<p id="msg" name="msg" ></p>
					<p id="showcwb" name="showcwb"></p>
					<p id="cwbgaojia" name="cwbgaojia" style="display: none" >高价</p>
					<p id="consigneeaddress" name="consigneeaddress"></p>
					<p id="excelbranch" name="excelbranch" ></p>
					<p id="customername" name="customername" ></p>
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
					<input type="hidden" id="requestbatchno" name="requestbatchno" value="0" />
			        <input type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" />
			</div>
		</div>
	</div>
		<div  id="goods" style="display: none;">
		<form id="form1">
					<div id="tt">
					<br><br>
					<div style='height: 200px; overflow-y: scroll;' id="tr"></div>
					<center>
					<input type="button" value="确认" onclick="updategoods()"/>
					<input type="reset" value="取消"/>
					</center>
					</div>
					<input type="hidden" id="cwbgoods"/> 
		</form>
		</div>
		<div id="view">
			<div class="saomiao_tab">
				<span style="float: right; padding: 10px">
					<input  class="input_button2" type="button" name="littlefalshbutton" id="flash" value="刷新" onclick="location.href='<%=request.getContextPath() %>/PDA/backandchangeimport'" />
				</span>
				<ul>
					<li><a id="table_weituihuoruku" href="javascript:tabView('table_weituihuoruku','wall')" class="light">退货未入库明细</a></li>
					<li><a id="table_yituihuoruku" href="javascript:tabView('table_yituihuoruku','yall')">退货已入库明细</a></li>
					<li><a id="table_zweituihuoruku" href="javascript:tabView('table_zweituihuoruku','wallz')">中转未入库明细</a></li>
					<li><a id="table_zyituihuoruku" href="javascript:tabView('table_zyituihuoruku','yallz')">中转已入库明细</a></li>
					<li><a id="table_yichang" href="javascript:tabView('table_yichang','yichang')">异常单明细</a></li>
				</ul>
			</div>
			<div id="ViewList" class="tabbox">
				<li>
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField();'/>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
											<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">订单类型</td>
											<td width="100" align="center" bgcolor="#f1f1f1">包号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div id="wall" style="height: 160px; overflow-y: scroll">
										<table id="weituihuorukuTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
										<%for(CwbOrder co : weituihuorukuList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><% for(Customer c:cList){ if(c.getCustomerid()==co.getCustomerid()) {out.print(c.getCustomername());break;} } %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="wpeisong" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="weituihuochukuTablepeisong" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%for(CwbOrder co : wpeisongList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}}  %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="wshangmengtui" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="weituihuochukuTableshangmengtui" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%for(CwbOrder co : wshangmentuiList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
											    <td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}}  %></td>
 												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="wshangmenghuan" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="weituihuochukuTableshangmenghuan" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%for(CwbOrder co : wshangmenhuanList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
 												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
 												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="yall" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="successTable" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
										<%for(CwbOrder co : yituihuorukuList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="yshangmenghuan" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="successTableshangmenghuan" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
											<%for(CwbOrder co : yshangmenhuanlist){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="yshangmengtui" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="successTableshangmengtui" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
											<%for(CwbOrder co : yshangmentuilist){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="ypeisong" style="height: 160px; overflow-y: scroll;display: none;" >
										<table id="successTablepeisong" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
											<%for(CwbOrder co : ypeisonglist){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="wallz" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="weituihuorukuTablez" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
										<%for(CwbOrder co : zweituihuorukuList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><% for(Customer c:cList){ if(c.getCustomerid()==co.getCustomerid()) {out.print(c.getCustomername());break;} } %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="wpeisongz" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="weituihuochukuTablepeisongz" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%for(CwbOrder co : zwpeisong){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}}  %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="wshangmengtuiz" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="weituihuochukuTableshangmengtuiz" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%for(CwbOrder co : zwshangmentui){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
											    <td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}}  %></td>
 												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="wshangmenghuanz" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="weituihuochukuTableshangmenghuanz" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%for(CwbOrder co : zwshangmenhuan){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
 												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
 												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="yallz" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="successTablez" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
										<%for(CwbOrder co : zyituihuorukuList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="yshangmenghuanz" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="successTableshangmenghuanz" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
											<%for(CwbOrder co : zyshangmenhuan){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="yshangmengtuiz" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="successTableshangmengtuiz" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
											<%for(CwbOrder co : zyshangmentui){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="ypeisongz" style="height: 160px; overflow-y: scroll;display: none;" >
										<table id="successTablepeisongz" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
											<%for(CwbOrder co : zypeisong){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									
									<div id="yichang" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="errorTable" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
				<!-- 

				<li style="display: none">
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick="exportField(3);"/>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号2</td>
											<td width="100" align="center" bgcolor="#f1f1f1">订单类型</td>
											<td width="100" align="center" bgcolor="#f1f1f1">包号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div id="yichang" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="errorTable" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li> -->
			</div>
		</div>
	
	
	<form action="<%=request.getContextPath()%>/PDA/exportExcle" method="post" id="searchForm">
		<input type="hidden" name="cwbs" id="cwbs" value=""/>
		<input type="hidden" name="exportmould2" id="exportmould2" />
	</form>
	<form action="<%=request.getContextPath()%>/PDA/backandchangeimportexport" method="post" id="searchForm2">
		<input  type="hidden"  name="extype" value="wall" id="extype"/>
		<input type="hidden" name="exportmould2" id="exportmould2" />
	</form>
	
	
</div>
</body>
</html>
