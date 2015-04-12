<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer,cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Truck,cn.explink.domain.Bale,cn.explink.domain.Switch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbDetailView> yichuzhanlist = (List<CwbDetailView>)request.getAttribute("yichuzhanlist");
List<CwbDetailView> ypeisonglist = (List<CwbDetailView>)request.getAttribute("ypeisong");
List<CwbDetailView> yshangmenhuanlist = (List<CwbDetailView>)request.getAttribute("yshangmenhuan");
List<CwbDetailView> yshangmentuilist = (List<CwbDetailView>)request.getAttribute("yshangmentui");

List<CwbDetailView> weichukuList = (List<CwbDetailView>)request.getAttribute("weichukulist");
List<CwbDetailView> wpeisongList = (List<CwbDetailView>)request.getAttribute("wpeisong");
List<CwbDetailView> wshangmenhuanList = (List<CwbDetailView>)request.getAttribute("wshangmenhuan");
List<CwbDetailView> wshangmentuiList = (List<CwbDetailView>)request.getAttribute("wshangmentui");

boolean showCustomerSign= request.getAttribute("showCustomerSign")==null?false:(Boolean)request.getAttribute("showCustomerSign");
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");

List<Branch> bList = (List<Branch>)request.getAttribute("branchlist");
List<User> uList = (List<User>)request.getAttribute("userList");
List<Truck> tList = (List<Truck>)request.getAttribute("truckList");
List<Bale> balelist = (List<Bale>)request.getAttribute("balelist");
//Switch ck_switch = (Switch) request.getAttribute("ck_switch");
Switch ckfb_switch = (Switch) request.getAttribute("ckfb_switch");
Map usermap = (Map) session.getAttribute("usermap");
String did=request.getParameter("branchid")==null?"0":request.getParameter("branchid");
long branchid=Long.parseLong(did);
long isscanbaleTag= request.getAttribute("isscanbaleTag")==null?1:Long.parseLong(request.getAttribute("isscanbaleTag").toString());
String wavPath=request.getContextPath()+"/images/wavnums/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>退货出站扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$(function(){
	var $menulilink = $(".kfsh_tabbtn ul li a");
	showbybranchid($("#branchid").val());
});
$(function(){
	var $menuli = $(".saomiao_tab ul li");
	$menuli.click(function(){	
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		/* $(".tabbox li").eq(index).show().siblings().hide(); */
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
function tohome(){
	var isscanbaleTag = 1;
	if($("#scanbaleTag").hasClass("light")){
		isscanbaleTag=1;
	}else{
		isscanbaleTag=0;
	}
	window.location.href="<%=request.getContextPath() %>/PDA/branchbackexport?branchid="+$("#branchid").val()+"&isscanbaleTag="+isscanbaleTag;
	
}
function tabView(tab){
	$("#"+tab).click();
} 
function tabView(tab,tip){
	$("#"+tab).click();
	$("div[id^='y']").attr('style','display: none;');
	$("div[id^='w']").attr('style','display: none;');
	$("#"+tip).attr('style','display;height: 160px; overflow-y: scroll;');
	$("#extype").attr('value',tip);
	
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
	//已出库明细只显示该下一站明细、异常单明细只显示该下一站明细
	if(branchid>0){
		$("#successTable tr").hide();
		$("#successTable tr[nextbranchid='"+branchid+"']").show();
		
		$("#errorTable tr").hide();
		$("#errorTable tr[nextbranchid='"+branchid+"']").show();
	}else{
		$("#successTable tr").show();
		$("#errorTable tr").show();
	}
}

/**
 * 出库扫描
 */
 //var alloutnum = 0;
function exportUntreadWarehouse(pname,scancwb,branchid,driverid,truckid,requestbatchno,baleno,ck_switch,confirmflag){
	if(<%=bList.isEmpty()%>){
		alert("抱歉，系统未分配退货站点，请联络您公司管理员！");
		return;
	}
	if(scancwb.length>0){
		if($("#scanbaleTag").attr("class")=="light"){//出库根据包号扫描订单
			baleaddcwbCheck();
		}else{//出库
			var successnum = 0,errorcwbnum = 0;
			$.ajax({
				type: "POST",
				url:pname+"/PDA/cwbexportUntreadWarhouse/"+scancwb+"?branchid="+branchid+"&driverid="+driverid+"&truckid="+truckid+"&confirmflag="+confirmflag+"&requestbatchno="+requestbatchno+"&baleno="+baleno,
				dataType:"json",
				success : function(data) {
					$("#scancwb").val("");
					if(data.statuscode=="000000"){
						if(data.body.packageCode!=""){
							$("#msg").html(data.body.packageCode+"　（"+data.errorinfo+"）");
							playWav("'"+data.body.successCount+"'");
						}
							$("#tuihuo").show();
							$("#showcwb").html("订 单 号："+scancwb);
							$("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
							$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
							
							$("#branchid").val(data.body.cwbOrder.nextbranchid);
							$("#scansuccesscwb").val(scancwb);
							/*if(data.body.cwbOrder.sendcarnum>1){
								document.ypdj.play();
							}
							if(data.body.cwbbranchnamewav!=pname+"/wav/"){
								$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random());
							}else{
								$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
							}*/
					}else{
						errorcwbnum += 1;
						$("#tuihuo").hide();
						$("#excelbranch").html("");
						$("#showcwb").html("");
						$("#msg").html(scancwb+"         （异常扫描）"+data.errorinfo);
						
						addAndRemoval(scancwb,"errorTable",false,$("#branchid").val());
						showbybranchid($("#branchid").val());
						//errorvedioplay(pname,data);
					}
					$("#responsebatchno").val(data.responsebatchno);
					batchPlayWav(data.wavList);
				}
			});
		}
	}
}

$(function(){
<%-- 	getTuiHuoOutSum('<%=request.getContextPath()%>',$("#branchid").val());
	getTuiHuoYiOutSum($("#branchid").val()); --%>
	 $("#scancwb").focus();
});
//得到当前出库的站点的库存量
function getTuiHuoOutSum(pname,branchid){
	$.ajax({
		type: "POST",
		url:pname+"/PDA/getTuiHuoOutSum?nextbranchid="+branchid,
		dataType:"json",
		success : function(data) {
			$("#chukukucundanshu").html(data.size);
		}
	});

}

function getTuiHuoYiOutSum(branchid){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/PDA/getTuiHuoYiOutSum?nextbranchid="+branchid,
		dataType:"json",
		success : function(data) {
			$("#alloutnum").html(data.size);
		}
	});
	//未出库明细、已出库明细、异常单明细只显示该下一站明细
	
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
$(function(){
	if(<%=isscanbaleTag==1%> ){
		$("#scanbaleTag").click();
		//$("#scancwbTag").removeClass("light");
	}else{
		//$("#scanbaleTag").removeClass("light");
		$("#scancwbTag").click();
	}
});
function exportField(flag,branchid){
	var cwbs = "";
	if(flag==1){
		if(branchid>0){
			if($("#extype").val()=='wall')
			{
			$("#weituihuochukuTable tr[nextbranchid='"+branchid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
			}
			else if($("#extype").val()=='wpeisong')
			{
			$("#weituihuochukuTablepeisong tr[nextbranchid='"+branchid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
			}
			else if($("#extype").val()=='wshangmengtui')
			{
			$("#weituihuochukuTableshangmengtui tr[nextbranchid='"+branchid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
			}
			else if($("#extype").val()=='wshangmenghuan')
			{
			$("#weituihuochukuTableshangmenghuan tr[nextbranchid='"+branchid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
			}
		}else{
			/* $("#weituihuochukuTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			}); */
			if($("#extype").val()=='wall')
			{
			$("#weituihuochukuTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
			}
			else if($("#extype").val()=='wpeisong')
			{
			$("#weituihuochukuTablepeisong tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
			}
			else if($("#extype").val()=='wshangmengtui')
			{
			$("#weituihuochukuTableshangmengtui tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
			}
			else if($("#extype").val()=='wshangmenghuan')
			{
			$("#weituihuochukuTableshangmenghuan tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
			}
		}
	}else if(flag==2){
		if(branchid>0){
			/* $("#successTable tr[nextbranchid='"+branchid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			}); */
			if($("#extype").val()=='yall')
			{
			$("#successTable tr[nextbranchid='"+branchid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
			}
			else if($("#extype").val()=='ypeisong')
			{
			$("#successTablepeisong tr[nextbranchid='"+branchid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
			}
			else if($("#extype").val()=='yshangmengtui')
			{
			$("#successTableshangmengtui tr[nextbranchid='"+branchid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
			}
			else if($("#extype").val()=='yshangmenghuan')
			{
			$("#successTableshangmenghuan tr[nextbranchid='"+branchid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
			}
		}else{
			/* $("#successTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			}); */
			if($("#extype").val()=='yall')
			{
			$("#successTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
			}
			else if($("#extype").val()=='ypeisong')
			{
			$("#successTablepeisong tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
			}
			else if($("#extype").val()=='yshangmengtui')
			{
			$("#successTableshangmengtui tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
			}
			else if($("#extype").val()=='yshangmenghuan')
			{
			$("#successTableshangmenghuan tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
			}
		}
	}else if(flag==3){
		/* if(branchid>0){
			$("#errorTable tr[nextbranchid='"+branchid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}else{ */
			$("#errorTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}
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
}
function clearMsg(){
	$("#msg").html("");
	$("#showcwb").html("");
	$("#excelbranch").html("");
	$("#cwbDetailshow").html("");
}
function  showbybranchid(branchid){
	if(branchid>0){
		//$("#weituihuochukuTable tr").hide();
		$("#weituihuochukuTable tr[nextbranchid='"+branchid+"']").show();
		$("#successTable tr").hide();
		$("#successTable tr[nextbranchid='"+branchid+"']").show();
		
		$("#errorTable tr").hide();
		$("#errorTable tr[nextbranchid='"+branchid+"']").show();
	}
}

///播放声音文件开始//////
//调用方法  playWav('12345')
function playWav(str){
	var strSplit=str.split("");
	for(var i=0;i<strSplit.length;i++) {
		  (function(i){
		    setTimeout(function(){
		    	url="<%=wavPath%>"+strSplit[i]+".wav";
		    	var src="<EMBED id='wav' name='wav' src='"+url+"' LOOP=false AUTOSTART=true MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>"+
		    	"<div id='FlashMusicBox' class='FlashMusicBox' style='position:absolute; overflow:hidden; left:-10000px; top:-10000px; width:10px; height:10px; border:solid 1px #F00;'>"+
		    	  "<object class='FlashMusic' classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' codebase='http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0' width='990' height='222'>"+
		    		"<param id='movie' name='movie' value='"+url+"'/><param name='quality' value='high' /><param name='wmode' value='transparent' />  <param name='LOOP' value='0' />"+ 
		    		"<embed class='FlashMusic' src='"+url+"' quality='high' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash' width='990' height='222' wmode='transparent' loop='-1' ></embed>"+
		    	"</object></div>"; 
		    	$("#emb").html(src);
		    	
		    	/* document.getElementById('wav').play(); */
		    	playWavcommit("bofang()",200);
		  },400*i);
		 })(i);
	}
	}
	
	function playWavcommit(){
		document.getElementById('wav').play();
	}
	///播放声音文件结束//////
	
	
//=============出库根据包号扫描订单检查===============
function baleaddcwbCheck(){
	if($("#branchid").val()==0){
		alert("请选择下一站！");
		return;
	}
	if($("#baleno").val()==""){
		alert("包号不能为空！");
		return;
	}
	if($("#scancwb").val()==""){
		alert("订单号不能为空！");
		return;
	}
	var confirmflag=$("#confirmflag").attr("checked")=="checked"?1:0;
	
	$.ajax({
   		type: "POST",
   		url:"<%=request.getContextPath()%>/bale/baleaddcwbCheck/"+$("#scancwb").val()+"/"+$("#baleno").val()+"?flag=2&branchid="+$("#branchid").val()+"&confirmflag="+confirmflag,
   		dataType : "json",
   		success : function(data) {
   			$("#msg").html("");
   			if(data.body.errorcode=="111111"){
   				if(data.body.errorenum=="Bale_ChongXinFengBao"){//此订单已在包号：XXX中封包，确认要重新封包吗?
   					if(confirm(data.body.errorinfo)){
   						baleaddcwb();//出库根据包号扫描订单 
   					}
   				}else{
   					$("#scancwb").val("");
	   				$("#msg").html("（异常扫描）"+data.body.errorinfo);
	   				errorvedioplay("<%=request.getContextPath()%>",data);
   				}
   				return;
   			} 
   			baleaddcwb();//出库根据包号扫描订单
   		}
   	});
}
	
//=============出库根据包号扫描订单===============	
function baleaddcwb(){
	if($("#baleno").val()==""){
		alert("包号不能为空！");
		$("#scancwb").val("");
		return;
	}
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/bale/baleaddcwb/"+$("#scancwb").val()+"/"+$("#baleno").val()+"?branchid="+$("#branchid").val(),
		dataType : "json",
		success : function(data) {
			$("#msg").html("");
			$("#scancwb").val("");
			if(data.body.errorcode=="000000"){
				$("#msg").html("（扫描成功）"+$("#baleno").val()+"包号共"+data.body.successCount+"件");
				playWav("'"+data.body.successCount+"'");
			}else{
				$("#msg").html("（异常扫描）"+data.body.errorinfo);
				errorvedioplay("<%=request.getContextPath()%>",data);
			}
		}
	});
}

//=============封包按钮===============	
function fengbao(){
	if($("#baleno").val()==""){
		alert("包号不能为空！");
		return;
	}
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/bale/fengbao/"+$("#baleno").val()+"?branchid="+$("#branchid").val(),
		dataType : "json",
		success : function(data) {
			$("#msg").html("");
			if(data.body.errorcode=="000000"){
				$("#msg").html($("#baleno").val()+"包号封包成功！");
			}else{
				$("#msg").html("（封包异常）"+data.body.errorinfo);
			}
			$("#scancwb").val("");
			errorvedioplay("<%=request.getContextPath()%>",data);
		}
	});
}

//=============按包出站按钮===============	
function chuku(){
	if($("#baleno").val()==""){
		alert("包号不能为空！");
		return;
	}
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/bale/baletuihuochuzhan/"+$("#baleno").val()+"?branchid="+$("#branchid").val()+"&driverid="+$("#driverid").val(),
		dataType : "json",
		success : function(data) {
			$("#msg").html("");
			$("#msg").html(data.body.errorinfo);
			$("#errorTable").html("");
			if(data.body.errorListView!=null){
	 			$.each(data.body.errorListView, function(key, value) {
	 				var tr = document.getElementById("errorTable").insertRow();
					tr.id="error_"+key;
					tr.cwb=value.cwb;
					tr.customerid=value.customerid;
					tr.nextbranchid=value.nextbranchid;
					var td = tr.insertCell(0);
					td.setAttribute("width",120);
					td.innerHTML = value.cwb;
					var td = tr.insertCell(1);
					td.setAttribute("width",100);
					td.innerHTML = value.customername;
					var td = tr.insertCell(2);
					td.setAttribute("width",140);
					td.innerHTML = value.emaildate;
					var td = tr.insertCell(3);
					td.setAttribute("width",100);
					td.innerHTML = value.consigneename;
					var td = tr.insertCell(4);
					td.setAttribute("width",100);
					td.innerHTML = value.receivablefee;
					var td = tr.insertCell(5);
					td.setAttribute("width",100);
					td.innerHTML = value.cwbremark;
					var td = tr.insertCell(6);
					td.setAttribute("width",200);
					td.innerHTML = value.consigneeaddress;
					var td = tr.insertCell(7);
					td.innerHTML = value.errorreasion;
			 	});
			}
			$("#scancwb").val("");
			$("#baleno").val("");
			errorvedioplay("<%=request.getContextPath()%>",data);
		}
	});
}
</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
 <div id="emb"></div>
<div class="saomiao_box">

<div class="saomiao_tab2">
		<ul>
			<li><a href="#"  class="light">逐单操作</a></li>		
			<li><a href="<%=request.getContextPath()%>/PDA/cwbbranchbackexportBatch">批量操作</a></li>
		</ul>
	</div>
	<div class="saomiao_topnum">
		<dl class="blue" >
			<dt>待退货出站</dt>
			<!-- <dd  style="cursor:pointer" onclick="tabView('table_weituihuochuku')" id="chukukucundanshu">0</dd> -->
			
					<table  style="width: 100%;height:60%;font-size: 15px;" cellpadding="0px" cellspacing="0px">
						<tr align="center" valign="middle" bgcolor="#ADCFF3" >
							<td style="color: #030;"><strong>配送</strong></td>
							<td style="color: #030;"><strong>上门退</strong></td>
							<td style="color: #030;"><strong>上门换</strong></td>
						</tr>
						<tr align="center" valign="bottom">
							<td style="cursor:pointer" onclick="tabView('table_weituihuochuku','wpeisong')" ><strong><%=wpeisongList.size() %></strong></td>
							<td style="cursor:pointer" onclick="tabView('table_weituihuochuku','wshangmengtui')" ><strong><%=wshangmentuiList.size()%></strong></td>
							<td style="cursor:pointer" onclick="tabView('table_weituihuochuku','wshangmenghuan')" ><strong><%=wshangmenhuanList.size() %></strong></td>
						</tr>
					</table>
					
		</dl>
		
		<dl class="green">
			<dt>已扫描</dt>
			<!-- <dd style="cursor:pointer" onclick="tabView('table_yituihuochuku')" id="alloutnum">0</dd> -->
					<table  style="width: 100%;height:60%;font-size: 15px;" cellpadding="0px" cellspacing="0px">
						<tr align="center" valign="middle" bgcolor="#D0E9BC" style="color: #131313;">
							<td style="color: #030;"><strong>配送</strong></td>
							<td style="color: #030;"><strong>上门退</strong></td>
							<td style="color: #030;"><strong>上门换</strong></td>
						</tr>
						<tr align="center" valign="bottom">
							<td style="cursor:pointer" onclick="tabView('table_yituihuochuku','ypeisong')" ><strong><%=ypeisonglist.size() %></strong></td>
							<td style="cursor:pointer" onclick="tabView('table_yituihuochuku','yshangmengtui')" ><strong><%=yshangmentuilist.size() %></strong></td>
							<td style="cursor:pointer" onclick="tabView('table_yituihuochuku','yshangmenghuan')" ><strong><%=yshangmenhuanlist.size() %></strong></td>
						</tr>
					</table>
		</dl>
		<input type="button"  id="refresh" value="刷新" onclick="location.href='<%=request.getContextPath() %>/PDA/branchbackexport'" style="float:left; width:100px; height:65px; cursor:pointer; border:none; background:url(../images/buttonbgimg1.gif) no-repeat; font-size:18px; font-family:'微软雅黑', '黑体'"/>
		<br clear="all">
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="saomiao_tab">
    		<ul  id="bigTag">
     		<li><a href="#" id="scancwbTag" onclick="clearMsg();$(function(){$('#baleno').parent().hide();$('#finish').parent().hide();$('#baleno').val('');$('#scancwb').val('');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();$('#baleBtn').hide();})" class="light">扫描订单</a></li>
					<%if(ckfb_switch.getId()!=0&&ckfb_switch.getState().equals(SwitchEnum.ChuKuFengBao.getInfo())){ %>
					<li><a href="#" id="scanbaleTag" onclick="clearMsg();$(function(){$('#baleno').parent().show();$('#baleno').show();$('#finish').parent().show();$('#finish').show();$('#baleno').val('');$('#baleno').focus();$('#scancwb').val('');$('#scancwb').parent().hide();$('#baleBtn').show();})">合包出站</a></li>
					<%} %>
    		</ul>
 			 </div>

			<div class="saomiao_selet2">
			
				下一站：
				<select id="branchid" name="branchid" onchange="tohome();" class="select1">
					<%for(Branch b : bList){ %>
						<%if(b.getBranchid()!=Long.parseLong(usermap.get("branchid").toString())){ %>
						<option value="<%=b.getBranchid() %>"  <%if(branchid==b.getBranchid()) {%>selected <%} %> ><%=b.getBranchname() %></option>
					<%} }%>
				</select>
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
					<%if(Long.parseLong(usermap.get("isImposedOutWarehouse").toString())==1){ %>
					<p>
						强制出库:<input type="checkbox" id="confirmflag" name="confirmflag" value="1"/>
					</p>
					<%} %>
					<p style="display: none;">
						<span>包号：</span>
						<input type="text" class="saomiao_inputtxt" name="baleno" id="baleno" onKeyDown="if(event.keyCode==13&&$(this).val().length>0){if($('#branchid').val()==0){alert('请选择下一站');return;}$(this).attr('readonly','readonly');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();}" />
						<!-- <input type="text" class="saomiao_inputtxt2" name="baleno" id="baleno" onKeyDown="if(event.keyCode==13&&$(this).val().length>0){if($('#branchid').val()==0){alert('请选择下一站');return;}$(this).attr('readonly','readonly');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();}"/> -->
						<span>&nbsp;</span>
					<input type="button" id="randomCreate" value="随机生成" class="button"  onclick="ranCreate();"/>
					<input type="button" id="handCreate" value="手工输入"  class="button"  onclick="hanCreate();"/>
					
					</p>
					<p><span>订单号：</span>
						<input type="text" class="saomiao_inputtxt" value="" id="scancwb" name="scancwb"  onKeyDown='if(event.keyCode==13&&$(this).val().length>0){exportUntreadWarehouse("<%=request.getContextPath()%>",$(this).val(),$("#branchid").val(),$("#driverid").val(),0,$("#requestbatchno").val(),$("#baleno").val(),$("#ck_switch").val(),$("#confirmflag").attr("checked")=="checked"?1:0);}'/>
					</p>
					<p id="baleBtn" style="display: none">
						<span>&nbsp;</span>
						<!-- <input type="submit" name="finish" id="finish" value="完成扫描" class="button" onclick="$('#baleno').removeAttr('readonly');$('#baleno').val('');"/> -->
						<input type="button" name="fengbao" id="fengbao" value="封包" class="button" onclick="fengbao()"/>
						<input type="button" name="chuku" id="chuku" value="出站" class="button" onclick="chuku()"/>
					</p>
				</div>
				<div class="saomiao_right2">
					<p id="tuihuo" style="display: none"><strong>退货单</strong></p>
					<p id="msg" name="msg" ></p>
					<p id="showcwb" name="showcwb"></p>
					<p id="excelbranch" name="excelbranch" ></p>
					<p id="cwbDetailshow" name="cwbDetailshow" ></p>
					<div style="display: none" id="EMBED">
					</div>
					<div style="display: none">
						<EMBED id='ypdj' name='ypdj' SRC='<%=request.getContextPath() %><%=ServiceUtil.waverrorPath %><%=CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl() %>' LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
					</div>
					<div style="display: none" id="errorvedio">
					</div>
					<input type="hidden" id="requestbatchno" name="requestbatchno" value="0" />
			        <input type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" />
			        <input type="hidden" id="ck_switch" name="ck_switch" value="ck_02" />
				</div>
			</div><!-- </form> -->
		</div>
	</div>
 
 
 <div>
			<div class="saomiao_tab2">
				<span style="float: right; padding: 10px">
				<input  class="input_button1" type="button" name="littlefalshbutton" id="flash" value="刷新" onclick="location.href='<%=request.getContextPath() %>/PDA/branchbackexport'" />
				</span>
				<ul id="smallTag">
					<li><a id="table_weituihuochuku" href="javascript:tabView('table_weituihuochuku','wall')" class="light">待出库明细</a></li>
					<li><a id="table_yituihuochuku" href="javascript:tabView('table_yituihuochuku','yall')">已出库明细</a></li>
					<li><a href="#">异常单明细</a></li>
				</ul>
			</div>
			<input id="extype" type="hidden" value="wall"/> 
			<div id="ViewList" class="tabbox">
				<li>
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(1,$("#branchid").val());'/>
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
											<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div id="wall" style="height: 160px; overflow-y: scroll;">
										<table id="weituihuochukuTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%for(CwbDetailView co : weichukuList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:customerlist){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView() %></td>
												<%} %>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="wpeisong" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="weituihuochukuTablepeisong" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%for(CwbDetailView co : wpeisongList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:customerlist){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView() %></td>
												<%} %>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="wshangmengtui" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="weituihuochukuTableshangmengtui" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%for(CwbDetailView co : wshangmentuiList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:customerlist){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView() %></td>
												<%} %>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="wshangmenghuan" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="weituihuochukuTableshangmenghuan" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%for(CwbDetailView co : wshangmenhuanList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:customerlist){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView() %></td>
												<%} %>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
				<li style="display: none">
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(2,$("#branchid").val());'/>
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
											<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div id="yall" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="successTable" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
											<%for(CwbDetailView co : yichuzhanlist){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:customerlist){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView() %></td>
												<%} %>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="yshangmenghuan" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="successTableshangmenghuan" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
											<%for(CwbDetailView co : yshangmenhuanlist){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:customerlist){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView() %></td>
												<%} %>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="yshangmengtui" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="successTableshangmengtui" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
											<%for(CwbDetailView co : yshangmentuilist){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:customerlist){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView() %></td>
												<%} %>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
									<div id="ypeisong" style="height: 160px; overflow-y: scroll;display: none;" >
										<table id="successTablepeisong" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
											<%for(CwbDetailView co : ypeisonglist){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:customerlist){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView() %></td>
												<%} %>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
				
				<li style="display: none">
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(3,$("#branchid").val());'/>
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
											<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
											<td align="center" bgcolor="#f1f1f1">地址</td>
											<%-- <td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
											<td width="200" align="center" bgcolor="#f1f1f1">地址</td>
											<td align="center" bgcolor="#f1f1f1">异常原因</td> --%>
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
 
 
</div>
</body>
</html>