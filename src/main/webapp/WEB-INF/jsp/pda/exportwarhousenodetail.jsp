<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page
	import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Truck,cn.explink.domain.Bale,cn.explink.domain.Switch"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%

List<Branch> bList = request.getAttribute("branchlist")==null?new ArrayList<Branch>():(List<Branch>)request.getAttribute("branchlist");
List<User> uList = request.getAttribute("userList")==null?new ArrayList<User>():(List<User>)request.getAttribute("userList");
List<Truck> tList = request.getAttribute("truckList")==null?new ArrayList<Truck>():(List<Truck>)request.getAttribute("truckList");
List<Bale> balelist = request.getAttribute("balelist")==null?new ArrayList<Bale>():(List<Bale>)request.getAttribute("balelist");
Switch ckfb_switch = (Switch) request.getAttribute("ckfb_switch");
Map usermap = (Map) session.getAttribute("usermap");
String wavPath=request.getContextPath()+"/images/wavnums/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>出库扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
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
	
})

	$(function(){
		getOutSum('<%=request.getContextPath()%>',0);
		getcwbsquejiandataForBranchid('-1');
		$("#scancwb").focus();
	});
	
	
	
$(function() {
	var $menuli = $(".uc_midbg ul li");
	$menuli.click(function() {
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
});

	//得到当前出库的站点的库存量
	function getOutSum(pname,branchid){
		$.ajax({
			type: "POST",
			url:pname+"/PDA/getOutSum?nextbranchid="+branchid,
			dataType:"json",
			success : function(data) {
				$("#chukukucundanshu").html(data.weichukucount);
				$("#chukukucunjianshu").html(data.weichukusum);
				$("#singleoutnum").html(data.yichukucount);
			}
		});
	}
	
	//得到出库缺货件数的统计
	function getcwbsquejiandataForBranchid(nextbranchid) {
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/PDA/getOutQueSum",
			data : {
				"nextbranchid" : nextbranchid
			},
			dataType : "json",
			success : function(data) {
				$("#lesscwbnum").html(data.lesscwbnum);
			}
		});
	}
/**
 * 出库扫描
 */
var branchStr=[];
var Cwbs="";
function exportWarehouse(pname,scancwb,branchid,driverid,truckid,requestbatchno,baleno,ck_switch,confirmflag){
	if(scancwb.length>0){
		if(scancwb.indexOf("@zd_")>-1){
			$("#branchid").val(scancwb.split('_')[1]);
			if($("#branchid").val()!=scancwb.split('_')[1]){
				$("#msg").html("         （异常扫描）扫描站点失败");
				$("#branchid").val(-1);
			}else{
				$("#msg").html("");
			}
			$("#scancwb").val("");
			return false;
		}
		if($("#scanbaleTag").attr("class")=="light"){//出库根据包号扫描订单
			baleaddcwbCheck();
		}else{//出库
			$.ajax({
				type: "POST",
				url:pname+"/PDA/cwbexportwarhouse/"+scancwb+"?branchid="+branchid+"&driverid="+driverid+"&truckid="+truckid+"&confirmflag="+confirmflag+"&requestbatchno="+requestbatchno+"&baleno="+baleno,
				dataType:"json",
				success : function(data) {
					$("#scancwb").val("");
					if(data.statuscode=="000000"){
						if(data.body.packageCode!=""){
							$("#msg").html(data.body.packageCode+"　（"+data.errorinfo+"）");
							
							if(data.body.cwbOrder.scannum==1){
								if(Cwbs.indexOf("|"+scancwb+"|")==-1){
									Cwbs += "|"+scancwb+"|";
									/* $("#singleoutnum").html(parseInt($("#singleoutnum").html())+1);
									$("#alloutnum").html(parseInt($("#alloutnum").html())+1);
									branchStr[data.body.cwbOrder.nextbranchid] = $("#singleoutnum").html(); */
								}
								//getOutSum(pname,data.body.cwbOrder.nextbranchid);
								//getcwbsquejiandataForBranchid(data.body.cwbOrder.nextbranchid);
							}
							
							if(data.body.cwbOrder.sendcarnum>1){
								playWav("'"+data.body.successCount+"'");
								//document.ypdj.play();
							}
							if(data.body.cwbbranchnamewav!=""&&data.body.cwbbranchnamewav!=pname+"/wav/"){
								playWav("'"+data.body.successCount+"'");
								//$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random());
							}else{
								$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
								playWav("'"+data.body.successCount+"'");
							}
						}/* else if(data.body.cwbOrder.nextbranchid!=0&&branchid!=-1&&branchid!=0&&data.body.cwbOrder.nextbranchid!=branchid){
							if(ck_switch=="ck_01"){//允许出库
								var con = confirm("出库站点与分拣站点不一致，是否强制出库？");
								if(con == false){
									$("#excelbranch").html("");
									$("#showcwb").html("");
									$("#msg").html(scancwb+"         （异常扫描）出库站点与分拣站点不一致");
									$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+pname+"/images/waverror/fail.wav&a="+Math.random());
								}else{
									$.ajax({
										type: "POST",
										url:pname+"/PDA/cwbexportwarhouse/"+scancwb+"?branchid="+branchid+"&driverid="+driverid+"&truckid="+truckid+"&confirmflag=1&requestbatchno="+requestbatchno+"&baleno="+baleno,
										dataType:"json",
										success : function(data) {
											if(data.statuscode=="000000"){
												$("#branchid").val(data.body.cwbOrder.nextbranchid);
												$("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
												$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
												
												if(data.body.cwbOrder.scannum==1){
													if(Cwbs.indexOf("|"+scancwb+"|")>-1){
														Cwbs += "|"+scancwb+"|";
														 $("#singleoutnum").html(parseInt($("#singleoutnum").html())+1);
														$("#alloutnum").html(parseInt($("#alloutnum").html())+1);
														branchStr[data.body.cwbOrder.nextbranchid] = $("#singleoutnum").html();
													}
													getOutSum(pname,data.body.cwbOrder.nextbranchid);
													getcwbsquejiandataForBranchid(data.body.cwbOrder.nextbranchid);
												}
												
												$("#scansuccesscwb").val(scancwb);
												$("#showcwb").html("订 单 号："+scancwb);
												
												if(data.body.cwbOrder.sendcarnum>1){
													document.ypdj.play();
												}
												if(data.body.cwbbranchnamewav!=""&&data.body.cwbbranchnamewav!=pname+"/wav/"){
													$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random());
												}else{
													$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
												}
											}else{
												$("#excelbranch").html("");
												$("#showcwb").html("");
												$("#msg").html(scancwb+"         （异常扫描）"+data.errorinfo);
												errorvedioplay(pname,data);
											}
										}
									});
								}
							}else{
								$("#excelbranch").html("");
								$("#showcwb").html("");
								$("#msg").html(scancwb+"         （异常扫描）出库站点与分拣站点不一致");
								$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+pname+"/images/waverror/fail.wav&a="+Math.random());
							}
						} */else{
							$("#branchid").val(data.body.cwbOrder.nextbranchid);
							if(data.body.cwbOrder.scannum==1){
								if(Cwbs.indexOf("|"+scancwb+"|")==-1){
									Cwbs += "|"+scancwb+"|";
									/* $("#singleoutnum").html(parseInt($("#singleoutnum").html())+1);
									$("#alloutnum").html(parseInt($("#alloutnum").html())+1);
									branchStr[data.body.cwbOrder.nextbranchid] = $("#singleoutnum").html(); */
								}
								//getOutSum(pname,data.body.cwbOrder.nextbranchid);
								//getcwbsquejiandataForBranchid(data.body.cwbOrder.nextbranchid);
							}
							
							$("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
							$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
							if(data.body.showRemark!=null){
							$("#cwbDetailshow").html("订单备注："+data.body.showRemark);
							}
							$("#scansuccesscwb").val(scancwb);
							$("#showcwb").html("订 单 号："+scancwb);
							//getOutSum(pname,data.body.cwbOrder.nextbranchid);
							//getcwbsquejiandataForBranchid(data.body.cwbOrder.nextbranchid);
							
							
							/*if(data.body.cwbOrder.sendcarnum>1){
								document.ypdj.play();
							}
							if(data.body.cwbbranchnamewav!=""&&data.body.cwbbranchnamewav!=pname+"/wav/"){
								$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random());
							}else{
								$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
							}*/
							}
					}else{
						$("#excelbranch").html("");
						$("#showcwb").html("");
						$("#msg").html(scancwb+"         （异常扫描）"+data.errorinfo);
						//errorvedioplay(pname,data);
					}
					$("#responsebatchno").val(data.responsebatchno);
					batchPlayWav(data.wavList);
				}
			});
		}
	}
}
/* function getsum(branchid){
	if(branchStr[branchid]>0){
		$("#singleoutnum").html(branchStr[branchid]);
	}else{
		$("#singleoutnum").html(0);
	}
} */

function clearMsg(){
	$("#msg").html("");
	$("#showcwb").html("");
	$("#excelbranch").html("");
	$("#cwbDetailshow").html("");
}

function  falshdata(){
	$("#refresh").attr("disabled","disabled");
	 $("#refresh").val("请稍后……");
	getOutSum('<%=request.getContextPath()%>',0);
	getcwbsquejiandataForBranchid('-1');
	$("#refresh").val("刷新");
	 $("#refresh").removeAttr("disabled");
}

function ranCreate(){
	if($("#branchid").val()==-1){
		alert('请选择下一站');
		return;
	}
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
   		url:"<%=request.getContextPath()%>/bale/baleaddcwbCheck/"+$("#scancwb").val()+"/"+$("#baleno").val()+"?flag=1&branchid="+$("#branchid").val()+"&confirmflag="+confirmflag,
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
				$("#msg").html("（扫描成功）"+$("#baleno").val()+"包号共"+data.body.successCount+"单,共"+data.body.scannum+"件");
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
				successvedioplay("<%=request.getContextPath()%>",data);
			}else{
				$("#msg").html("（封包异常）"+data.body.errorinfo);
				errorvedioplay("<%=request.getContextPath()%>",data);
			}
			$("#scancwb").val("");
			
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
		url:"<%=request.getContextPath()%>/bale/balechuku/"+$("#baleno").val()+"?branchid="+$("#branchid").val()+"&driverid="+$("#driverid").val()+"&truckid="+$("#truckid").val(),
		dataType : "json",
		success : function(data) {
			$("#msg").html("");
			$("#msg").html(data.body.errorinfo);
			$("#scancwb").val("");
			$("#baleno").val("");
			
			if(data.body.errorcode=='111111'){
				errorvedioplay("<%=request.getContextPath()%>",data);
			}else if(data.statuscode=="000000"){
				successvedioplay("<%=request.getContextPath()%>",data);
			}
			
		}
	});
}
</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div id="emb"></div>
<div class="saomiao_box2">
 
	<div class="saomiao_topnum2">
		<dl class="blue">
			<dt>待出库</dt>
			<dd id="chukukucundanshu">0</dd>
		</dl>
		<dl class="yellow">
			<dt>待出库件数合计</dt>
			<dd id="chukukucunjianshu"></dd>
		</dl>
		<dl class="green">
			<dt>本次已出库</dt>
			<dd id="singleoutnum">0</dd>
		</dl>
		<dl class="yellow">
			<dt>一票多件缺货件数</dt>
			<dd id="lesscwbnum" name="lesscwbnum" >0</dd>
		</dl>
			<input type="button" id="refresh" value="刷新"
				onclick="location.href='<%=request.getContextPath()%>/PDA/exportwarhousenodetail'"
				style="float: left; width: 100px; height: 65px; cursor: pointer; border: none; background: url(../images/buttonbgimg1.gif) no-repeat; font-size: 18px; font-family: '微软雅黑', '黑体'" />
		<br clear="all"/>
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="kfsh_tabbtn">
				<ul id="bigTag">
						<li><a href="#" id="scancwbTag"
							onclick="clearMsg();$(function(){$('#baleno').parent().hide();$('#finish').parent().hide();$('#baleno').val('');$('#scancwb').val('');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();$('#baleBtn').hide();})"
							class="light">扫描订单</a></li>
						<%
							if (ckfb_switch.getId() != 0 && ckfb_switch.getState().equals(SwitchEnum.ChuKuFengBao.getInfo())) {
						%>
						<li><a href="#" id="scanbaleTag"
							onclick="clearMsg();$(function(){$('#baleno').parent().show();$('#baleno').show();$('#finish').parent().show();$('#finish').show();$('#baleno').val('');$('#baleno').focus();$('#scancwb').val('');$('#scancwb').parent().hide();$('#baleBtn').show();})">合包出库</a></li>
						<%
							}
						%>
				</ul>
			</div>
			<div class="saomiao_righttitle2" id="pagemsg"></div>
			<!-- <form action="" method="get"> -->
			<div class="saomiao_selet2">
					下一站： <select id="branchid" name="branchid"
						onchange="getOutSum('<%=request.getContextPath()%>',$(this).val());getcwbsquejiandataForBranchid($(this).val());">
					<option value="-1" selected>请选择</option>
						<%
							for (Branch b : bList) {
						%>
						<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
						<%
							}
						%>
					</select> 驾驶员： <select id="driverid" name="driverid">
					<option value="-1" selected>请选择</option>
						<%
							for (User u : uList) {
						%>
						<option value="<%=u.getUserid() %>" ><%=u.getRealname() %></option>
						<%
							}
						%>
					</select> 车辆： <select id="truckid" name="truckid">
					<option value="-1" selected>请选择</option>
						<%
							for (Truck t : tList) {
						%>
						<option value="<%=t.getTruckid() %>" ><%=t.getTruckno() %></option>
						<%
							}
						%>
		        </select>
			</div>
			<div class="saomiao_inwrith2">
				<div class="saomiao_left2">
						<%
							if (Long.parseLong(usermap.get("isImposedOutWarehouse").toString()) == 1) {
						%>
					<p>
						强制出库:<input type="checkbox" id="confirmflag" name="confirmflag" value="1"/>
					</p>
						<%
							}
						%>
						<p style="display: none;">
							<span>包号：</span> <input type="text" class="saomiao_inputtxt2" name="baleno" id="baleno"
								onKeyDown="if(event.keyCode==13&&$(this).val().length>0){if($('#branchid').val()==-1){alert('请选择下一站');return;}$(this).attr('readonly','readonly');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();}" />
							<span>&nbsp;</span> <input type="button" id="randomCreate" value="随机生成" class="button"
								onclick="ranCreate();" /> <input type="button" id="handCreate" value="手工输入" class="button"
								onclick="hanCreate();" />
					</p>
					
						<p>
							<span>订单号：</span> <input type="text" class="saomiao_inputtxt2" value="" id="scancwb"
								name="scancwb"
								onKeyDown='if(event.keyCode==13&&$(this).val().length>0){exportWarehouse("<%=request.getContextPath()%>",$(this).val(),$("#branchid").val(),$("#driverid").val(),$("#truckid").val(),$("#requestbatchno").val(),$("#baleno").val(),$("#ck_switch").val(),$("#confirmflag").attr("checked")=="checked"?1:0);}' />
					</p>
					<p id="baleBtn" style="display: none">
						<span>&nbsp;</span>
						<!-- <input type="submit" name="finish" id="finish" value="完成扫描" class="button" onclick="$('#baleno').removeAttr('readonly');$('#baleno').val('');"/> -->
							<input type="button" name="fengbao" id="fengbao" value="封包" class="button"
								onclick="fengbao()" /> <input type="button" name="chuku" id="chuku" value="出库"
								class="button" onclick="chuku()" />
					</p>
					<!-- 
					<p style="display: none;">
						<span>&nbsp;</span><input type="submit" name="finish" id="finish" value="完成扫描" class="button" onclick="$('#baleno').removeAttr('readonly');$('#baleno').val('');">
					</p> -->
				</div>
				<div class="saomiao_right2">
					<p id="msg" name="msg" ></p>
					<p id="showcwb" name="showcwb"></p>
					<p id="excelbranch" name="excelbranch" ></p>
					<p id="cwbDetailshow" name="cwbDetailshow" ></p>
					
						<div style="display: none" id="EMBED"></div>
					<div style="display: none">
							<EMBED id='ypdj' name='ypdj'
								SRC='<%=request.getContextPath()%><%=ServiceUtil.waverrorPath%><%=CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl()%>'
								LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
					</div>
						<div style="display: none" id="errorvedio"></div>
					</div>
					<input type="hidden" id="requestbatchno" name="requestbatchno" value="0" /> <input
						type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" /> <input type="hidden"
						id="ck_switch" name="ck_switch" value="ck_02" />
				</div>
				<!-- </form> -->
		</div>
	</div>
</div>
</body>
</html>
