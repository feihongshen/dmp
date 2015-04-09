<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.Customer,cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
List<CwbOrder> weitghsckList = (List<CwbOrder>)request.getAttribute("weitghsckList");
List<CwbOrder> wpeisongList = (List<CwbOrder>)request.getAttribute("wpeisong");
List<CwbOrder> wshangmenhuanList = (List<CwbOrder>)request.getAttribute("wshangmenhuan");
List<CwbOrder> wshangmentuiList = (List<CwbOrder>)request.getAttribute("wshangmentui");
List<CwbOrder> yitghsckList = (List<CwbOrder>)request.getAttribute("yitghsckList");
List<CwbOrder> ypeisonglist = (List<CwbOrder>)request.getAttribute("ypeisong");
List<CwbOrder> yshangmenhuanlist = (List<CwbOrder>)request.getAttribute("yshangmenhuan");
List<CwbOrder> yshangmentuilist = (List<CwbOrder>)request.getAttribute("yshangmentui");
String wavPath=request.getContextPath()+"/images/wavnums/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>退供应商出库扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$(function(){
	getcwbsdataForCustomer();
	$("#scancwb").focus();
});
$(function() {
	var $menuli = $(".saomiao_tab2 ul li");
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
	
}
$(function(){
	var $menuli = $(".saomiao_tab ul li");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		/* $(".tabbox li").eq(index).show().siblings().hide(); */
	});
	
}) 
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

function getcwbsdataForCustomer(){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/PDA/getTGYSCKSum",
		dataType:"json",
		success : function(data) {
			$("#yps").html(data.yps);
			$("#ysmt").html(data.ysmt);
			$("#ysmh").html(data.ysmh);
			$("#wps").html(data.wps);
			$("#wsmt").html(data.wsmt);
			$("#wsmh").html(data.wsmh);
		}
	});
}

//退供应商出库
function cwbbacktocustomer(scancwb,baleno){
	if(scancwb.length>0){
		if($("#scanbaleTag").attr("class")=="light"){//出库根据包号扫描订单
			baleaddcwbCheck();
		}else{//出库
			$.ajax({
				type: "POST",
				url:"<%=request.getContextPath()%>/PDA/cwbbacktocustomer/"+scancwb+"?baleno="+baleno,
				dataType:"json",
				success : function(data) {
					$("#scancwb").val("");
					if(data.statuscode=="000000"){
						if( baleno!="" ){
							$("#msg").html(baleno+"　（"+data.errorinfo+"）");
							playWav("'"+data.body.successCount+"'");
							/*if(data.body.cwbOrder.scannum==1){
								if(Cwbs.indexOf("|"+scancwb+"|")==-1){
									Cwbs += "|"+scancwb+"|";
									$("#singleoutnum").html(parseInt($("#singleoutnum").html())+1);
									$("#alloutnum").html(parseInt($("#alloutnum").html())+1);
									branchStr[data.body.cwbOrder.nextbranchid] = $("#singleoutnum").html(); 
								}
								//getOutSum(pname,data.body.cwbOrder.nextbranchid);
								//getcwbsquejiandataForBranchid(data.body.cwbOrder.nextbranchid);
								//getchukucwbquejiandataList(data.body.cwbOrder.nextbranchid);
								//将成功扫描的订单放到已入库明细中
								//addAndRemoval(data.body.cwbOrder.cwb,"successTable",true,$("#branchid").val());
							}*/
						}else{
							$("#msg").html(scancwb+"         （成功扫描）");
							
							$("#showcwb").html("订 单 号："+scancwb);
							$("#tuihuo").show();
							
							$("#customername").html(data.customername);
						}
						//getcwbsdataForCustomer();
						//将成功扫描的订单放到已入库明细中
						//addAndRemoval(data.body.cwb,"successTable",true);
					}else{
						$("#showcwb").html("");
						$("#tuihuo").hide();
						$("#msg").html(scancwb+"         （异常扫描）"+data.errorinfo);
						addAndRemoval(scancwb,"errorTable",false);
						$("#customername").html("");
						//errorvedioplay(<%=request.getContextPath()%>,data);
					}
					batchPlayWav(data.wavList);
				}
			});
		}
	}
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
	$("#tuihuo").hide();
}
function exportField(flag){
	
	if(flag==1){
		$("#type").val("weichuku");
		$("#exportForBack").submit();
	}else if(flag==2){
		$("#type").val("yichuku");
		$("#exportForBack").submit();
	}else if(flag==3){
		var cwbs = "";
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
		 	$("#searchForm2").submit();
		}else{
			alert("没有订单信息，不能导出！");
		};
		
	}
	
}
var yipage=1,weipage=1;

function weichuku(){
	weipage++;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbacktocustomerdaichukulist",
		data:{"page":weipage},
		success:function(data){
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
			$("#addwei").remove();			
			$("#weitghsckTable").append(optionstring);
			if(data.length==<%=Page.DETAIL_PAGE_NUMBER %>){
				var addmore="<tr id='addwei' align='center' ><td colspan='6' style='cursor:pointer' onclick='weichuku()' >查看更多</td></tr>";
				$("#weitghsckTable").append(addmore);
			}
		}
	});
};

function yichuku(){
	yipage++;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbacktocustomeryichukulist",
		data:{"page":yipage},
		success:function(data){
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
			$("#addyi").remove();
			$("#successTable").append(optionstring);
			if(data.length==<%=Page.DETAIL_PAGE_NUMBER %>){
				var addmore="<tr id='addyi' align='center' ><td colspan='6' style='cursor:pointer' onclick='yichuku()'> 查看更多</td> </tr>";
				$("#successTable").append(addmore);
			}
						
		}
	});
	
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
   		url:"<%=request.getContextPath()%>/bale/baleaddcwbCheck/"+$("#scancwb").val()+"/"+$("#baleno").val()+"?flag=4",
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
		url:"<%=request.getContextPath()%>/bale/baleaddcwb/"+$("#scancwb").val()+"/"+$("#baleno").val(),
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
		url:"<%=request.getContextPath()%>/bale/fengbao/"+$("#baleno").val(),
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
	
//=============按包出库按钮===============	
function chuku(){
	if($("#baleno").val()==""){
		alert("包号不能为空！");
		return;
	}
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/bale/baletocustomerchuku/"+$("#baleno").val(),
		dataType : "json",
		success : function(data) {
			$("#msg").html("");
			$("#msg").html(data.body.errorinfo);
			$("#errorTable").html("");
			if(data.body.errorListView!=null){
	 			$.each(data.body.errorListView, function(key, value) {
	 				var tr = document.getElementById("errorTable").insertRow();
					tr.id="error_"+key;
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
			errorvedioplay("<%=request.getContextPath()%>", data);
			}
		});
	}
</script>
</head>
<body style="background: #f5f5f5" marginwidth="0" marginheight="0">
	<div class="saomiao_box">
	
	<div class="saomiao_tab2">
		<ul>
			<li><a href="#"  class="light">逐单操作</a></li>		
			<li><a href="<%=request.getContextPath()%>/PDA/cwbbacktocustomerBatch">批量操作</a></li>
		</ul>
	</div>
	
	
	
		<div id="emb"></div>
		<div class="saomiao_topnum">
			<!-- <dl class="blue">
				<dt style="cursor: pointer" onclick="tabView('table_weichuku')">待出库</dt>
				<dd id="tuicun">0</dd>
			</dl> -->
			<dl class="blue" >
			<dt>待出库</dt>
			<!-- <dd  style="cursor:pointer" onclick="tabView('table_weituihuochuku')" id="chukukucundanshu">0</dd> -->
			
					<table  style="width: 100%;height:60%;font-size: 15px;" cellpadding="0px" cellspacing="0px">
						<tr align="center" valign="middle" bgcolor="#ADCFF3" >
							<td style="color: #030;"><strong>配送</strong></td>
							<td style="color: #030;"><strong>上门退</strong></td>
							<td style="color: #030;"><strong>上门换</strong></td>
						</tr>
						<tr align="center" valign="bottom">
							<td style="cursor:pointer" onclick="tabView('table_weituihuochuku','wpeisong')" id="chukukucundanshu"><strong><span id="wps">0</span></strong></td>
							<td style="cursor:pointer" onclick="tabView('table_weituihuochuku','wshangmengtui')" id="chukukucundanshu"><strong><span id="wsmt">0</span></strong></td>
							<td style="cursor:pointer" onclick="tabView('table_weituihuochuku','wshangmenghuan')" id="chukukucundanshu"><strong><span id="wsmh">0</span></strong></td>
						</tr>
					</table>
					
		</dl>
<!-- 			<dl class="green">
				<dt style="cursor: pointer" onclick="tabView('table_yichuku')">已出库</dt>
				<dd id="yichuku">0</dd>
			</dl> -->
				<dl class="green">
			<dt>已出库</dt>
			<!-- <dd style="cursor:pointer" onclick="tabView('table_yituihuochuku')" id="alloutnum">0</dd> -->
					<table  style="width: 100%;height:60%;font-size: 15px;" cellpadding="0px" cellspacing="0px">
						<tr align="center" valign="middle" bgcolor="#D0E9BC" style="color: #131313;">
							<td style="color: #030;"><strong>配送</strong></td>
							<td style="color: #030;"><strong>上门退</strong></td>
							<td style="color: #030;"><strong>上门换</strong></td>
						</tr>
						<tr align="center" valign="bottom">
							<td style="cursor:pointer" onclick="tabView('table_yituihuochuku','ypeisong')" id="chukukucundanshu"><strong><span id="yps">0</span></strong></td>
							<td style="cursor:pointer" onclick="tabView('table_yituihuochuku','yshangmengtui')" id="chukukucundanshu"><strong><span id="ysmt">0</span></strong></td>
							<td style="cursor:pointer" onclick="tabView('table_yituihuochuku','yshangmenghuan')" id="chukukucundanshu"><strong><span id="ysmh">0</span></strong></td>
						</tr>
					</table>
		</dl>
			<input type="button" id="refresh" value="刷新"
				onclick="location.href='<%=request.getContextPath()%>/PDA/backtocustomer'"
				style="float: left; width: 100px; height: 65px; cursor: pointer; border: none; background: url(../images/buttonbgimg1.gif) no-repeat; font-size: 18px; font-family: '微软雅黑', '黑体'" />
			<br clear="all">
		</div>

		<div class="saomiao_info2">
			<div class="saomiao_inbox2">
				<div class="saomiao_tab">
					<ul id="bigTag">
						<li><a href="#" id="scancwbTag"
							onclick="clearMsg();$(function(){$('#baleno').parent().hide();$('#baleno').val('');$('#scancwb').val('');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();$('#baleBtn').hide();})"
							class="light">扫描订单</a></li>
						<li><a href="#" id="scanbaleTag"
							onclick="clearMsg();$(function(){$('#baleno').parent().show();$('#baleno').show();$('#finish').parent().show();$('#finish').show();$('#baleno').val('');$('#baleno').focus();$('#scancwb').val('');$('#scancwb').parent().hide();$('#baleBtn').show();})">合包出库</a></li>
					</ul>
				</div>
				<div class="saomiao_selet2"></div>
				<div class="saomiao_inwrith2">
					<div class="saomiao_left2">
						<p style="display: none;">
							<span>包号：</span> <input type="text" class="saomiao_inputtxt2" value="" id="baleno"
								name="baleno"
								onKeyDown="if(event.keyCode==13&&$(this).val().length>0){$(this).attr('readonly','readonly');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();}" />
							<span>&nbsp;</span> <input type="button" id="randomCreate" value="随机生成" class="button"
								onclick="ranCreate();" /> <input type="button" id="handCreate" value="手工输入" class="button"
								onclick="hanCreate();" />
						</p>
						<p>
							<span>订单号：</span> <input class="saomiao_inputtxt2" type="text" id="scancwb" name="scancwb"
								value=""
								onKeyDown="if(event.keyCode==13&&$(this).val().length>0){cwbbacktocustomer($(this).val(),$('#baleno').val());}" />
						</p>
						<p id="baleBtn" style="display: none;">
							<span>&nbsp;</span> <input type="button" name="fengbao" id="fengbao" value="封包"
								class="button" onclick="fengbao()" /> <input type="button" name="chuku" id="chuku"
								value="出库" class="button" onclick="chuku()" />
						</p>
					</div>
					<div class="saomiao_right2">
						<p id="msg" name="msg"></p>
						<p id="tuihuo" style="display: none;">
							<strong>退货单</strong>
						</p>
						<p id="showcwb" name="showcwb"></p>
						<p id="customername" name="customername"></p>
					</div>
					<div style="display: none" id="errorvedio"></div>
					<input type="hidden" id="responsebatchno" name="responsebatchno" value="0" /> <input
						type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" />
				</div>
			</div>
		</div>



		<div>
			<div class="saomiao_tab2">
				<span style="float: right; padding: 10px"> <input style="" input_button2" type="button"
					name="littlefalshbutton" id="flash" value="刷新"
					onclick="location.href='<%=request.getContextPath()%>/PDA/backtocustomer'" />
				</span>
				<ul id="smallTag">
			<li><a id="table_weituihuochuku" href="javascript:tabView('table_weituihuochuku','wall')" class="light">待出库明细</a></li>
					<li><a id="table_yituihuochuku" href="javascript:tabView('table_yituihuochuku','yall')">已出库明细</a></li>
					<li><a href="#">异常单明细</a></li>
				</ul>
			</div>
			
			<div id="ViewList" class="tabbox">
				<li><input type="button" id="btnval0" value="导出Excel" class="input_button1"
					onclick='exportField(1);' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
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
					<div id="wall" style="height: 160px; overflow-y: scroll;">
										<table id="weituihuochukuTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%for(CwbOrder co : weitghsckList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:customerlist){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
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
												<td width="100" align="center"><%for(Customer c:customerlist){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
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
												<td width="100" align="center"><%for(Customer c:customerlist){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
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
												<td width="100" align="center"><%for(Customer c:customerlist){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table></li>
				<li style="display: none"><input type="button" id="btnval0" value="导出Excel"
					class="input_button1" onclick="exportField(2);" />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
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
									<div id="yall" style="height: 160px; overflow-y: scroll;display: none;">
										<table id="successTable" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
											<%for(CwbOrder co : yitghsckList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:customerlist){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
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
												<td width="100" align="center"><%for(Customer c:customerlist){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
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
												<td width="100" align="center"><%for(Customer c:customerlist){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
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
												<td width="100" align="center"><%for(Customer c:customerlist){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table></li>

				<li style="display: none"><input type="button" id="btnval0" value="导出Excel"
					class="input_button1" onclick="exportField(3);" />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<td width="200" align="center" bgcolor="#f1f1f1">地址</td>
											<td align="center" bgcolor="#f1f1f1">异常原因</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="errorTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
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
		<form action="<%=request.getContextPath()%>/PDA/exportExcleForBackToCustomer" id="exportForBack">
			<input type="hidden" name="type" value="" id="type" />
			<input id="extype" name="extype" type="hidden" value="wall"/> 
		</form>


	</div>
</body>
</html>

