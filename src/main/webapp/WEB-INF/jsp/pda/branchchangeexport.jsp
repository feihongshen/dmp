<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Truck,cn.explink.domain.Bale,cn.explink.domain.Switch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> bList = request.getAttribute("branchlist")==null?new ArrayList<Branch>():(List<Branch>)request.getAttribute("branchlist");
List<Branch> AllbList = request.getAttribute("branchalllist")==null?new ArrayList<Branch>():(List<Branch>)request.getAttribute("branchalllist");
List<User> uList = request.getAttribute("userList")==null?new ArrayList<User>():(List<User>)request.getAttribute("userList");
List<Truck> tList = request.getAttribute("truckList")==null?new ArrayList<Truck>():(List<Truck>)request.getAttribute("truckList");
List<Bale> balelist = request.getAttribute("balelist")==null?new ArrayList<Bale>():(List<Bale>)request.getAttribute("balelist");
List<Reason> reasonlist = request.getAttribute("reasonlist")==null?null:(List<Reason>)request.getAttribute("reasonlist");
//Switch ck_switch = (Switch) request.getAttribute("ck_switch");
Switch ckfb_switch = (Switch) request.getAttribute("ckfb_switch");
Map usermap = (Map) session.getAttribute("usermap");
String wavPath=request.getContextPath()+"/images/wavnums/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>中转出站扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
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
	$("#ysm_export").hide();
	$("#dzz_li").hide();
	$("#ysm_li").hide();
	
})
function showTab(tab){
	$("#dzz_li").hide();
	$("#ysm_li").hide();
	$("#dzz_title").removeClass("light");
	$("#ysm_title").removeClass("light");
	$("#"+tab+"_li").show();
	$("#"+tab+"_title").addClass("light");
	
	
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
}
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
		getOutSum('<%=request.getContextPath()%>',0);
		 $("#scancwb").focus();
	});
	//得到当前出库的站点的库存量
	function getOutSum(pname,branchid){
		$.ajax({
			type: "POST",
			url:pname+"/PDA/getOutSum?nextbranchid="+branchid,
			dataType:"json",
			success : function(data) {
				$("#chukukucundanshu").html(data.weichukucount);
				$("#alloutnum").html(data.yichukucount);
				$("#daizhongzhuan").html(data.daizhongzhuan);
				var cwbList=data.yisaomiaocwOrders;
				var customers=data.customerList;
				$("#ysm").html("");
				$("#ysm_nextbranchid").val(branchid);
				var tr="";
				if(cwbList.length>0){
					$("#ysm_export").show();
				}
				for(var i=0;i<cwbList.length;i++)
					{
					tr+= "<tr>"
					+"<td width='120' align='center'>"+cwbList[i].cwb+"</td>"
					+"<td width='120' align='center'>"+cwbList[i].transcwb+"</td>"
					+"<td width='120' align='center'>"+cwbList[i].packagecode+"</td>";
					for(var j=0;j<customers.length;j++){
						if(customers[j].customerid==cwbList[i].customerid){
					tr+="<td width='100' align='center'> "+customers[j].customername+"</td>";
						}
					}
					tr+="<td width='140' align='center'> "+cwbList[i].emaildate+"</td>"
					+"<td width='100' align='center'> "+cwbList[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+cwbList[i].receivablefee+"</td>"
					+"<td align='left'> "+cwbList[i].consigneeaddress+"</td>"
					+ "</tr>";
					}
				$("#ysm").append(tr);
			}
		});
	}
/**
 * 出库扫描
 */

//var alloutnum = 0;

function exportWarehouse(pname,scancwb,branchid,driverid,truckid,requestbatchno,baleno,ck_switch,confirmflag,reasonid,deliverybranchid){
	if(<%=bList.isEmpty()%>){
		alert("抱歉，系统未分配中转站点，请联络您公司管理员！");
		return;
	}
	if(branchid==deliverybranchid){
		alert("抱歉，下一站和目的站不能相同！");
		return;
	}
	if(scancwb.length>0){
		if($("#scanbaleTag").attr("class")=="light"){//出库根据包号扫描订单
			baleaddcwbCheck();
		}else{//出库
			var successnum = 0,errorcwbnum = 0;
			$.ajax({
				type: "POST",
				url:pname+"/PDA/cwbchangeexportwarhouse/"+scancwb+"?branchid="+branchid+"&driverid="+driverid+"&truckid="+truckid+"&confirmflag="+confirmflag+"&requestbatchno="+requestbatchno+"&baleno="+baleno+"&reasonid="+reasonid+"&deliverybranchid="+deliverybranchid,
				dataType:"json",
				success : function(data) {
					$("#scancwb").val("");
					if(data.statuscode=="000000"){
							$("#zhongzhuan").show();
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
							$("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
							$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
							if(data.body.packageCode!=""){
								numbervedioplay("<%=request.getContextPath()%>",data.body.successCount);
							}
							if(data.body.showRemark!=null){
							$("#cwbDetailshow").html("订单备注："+data.body.showRemark);
							}
							$("#branchid").val(data.body.cwbOrder.nextbranchid);
							$("#scansuccesscwb").val(scancwb);
							$("#showcwb").html("订 单 号："+scancwb);
							//getOutSum(pname,data.body.cwbOrder.nextbranchid);
							/*if(data.body.cwbOrder.sendcarnum>1){
								document.ypdj.play();
							}
							if(data.body.cwbbranchnamewav!=pname+"/wav/"){
								$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random());
							}else{
								$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
							}*/
						//}
					}else{
						//errorcwbnum += 1;
						
						$("#zhongzhuan").hide();
						$("#excelbranch").html("");
						$("#showcwb").html("");
						$("#cwbordertype").html("");
						$("#fee").html("");
						$("#msg").html(scancwb+"         （异常扫描）"+data.errorinfo);
						//errorvedioplay(pname,data);
					}
					$("#responsebatchno").val(data.responsebatchno);
					//alloutnum += successnum;
					//$("#alloutnum").html(alloutnum);
					//getOutSum(pname,$("#branchid").val());
					batchPlayWav(data.wavList);
				}
			});
		}
	}
}

	 function  falshdata(){
		 $("#refresh").attr("disabled","disabled");
		 $("#refresh").val("请稍后……");
		 getOutSum('<%=request.getContextPath()%>',0);
		 $("#refresh").val("刷新");
		 $("#refresh").removeAttr("disabled");
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
   		/* url:pname+"/PDA/cwbchangeexportwarhouse/"+scancwb+"?branchid="+branchid+"&driverid="+driverid+"&truckid="+truckid+"&confirmflag="+confirmflag+"&requestbatchno="+requestbatchno+"&baleno="+baleno+"&reasonid="+reasonid+"&deliverybranchid="+deliverybranchid, */
   		url:"<%=request.getContextPath()%>/bale/baleaddcwbCheck/"+$("#scancwb").val()+"/"+$("#baleno").val()+"?flag=3&branchid="+$("#branchid").val()+"&confirmflag="+confirmflag,
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
				numbervedioplay("<%=request.getContextPath()%>",data.body.successCount);
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

//=============按包出站按钮===============	
function chuku(){
	if($("#baleno").val()==""){
		alert("包号不能为空！");
		return;
	}
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/bale/balechuku/"+$("#baleno").val()+"?branchid="+$("#branchid").val()+"&driverid="+$("#driverid").val()+"&truckid="+$("#truckid").val()+"&reasonid="+$("#reasonid").val()+"&deliverybranchid="+$("#deliverybranchid").val(),
		dataType : "json",
		success : function(data) {
			$("#msg").html("");
			$("#msg").html(data.body.errorinfo);
			$("#scancwb").val("");
			$("#baleno").val("");
			
			if(data.body.errorListView!=null){
				errorvedioplay("<%=request.getContextPath()%>",data);
			}else{
				successvedioplay("<%=request.getContextPath()%>",data);
			}
		}
	});
}
</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
 <div id="emb"></div>
<div class="saomiao_box">
 
	<div class="saomiao_topnum">
		<!-- <dl class="blue" style="display: none;">
			<dt>待中转出站</dt>
			<dd id="chukukucundanshu">0</dd>
		</dl> -->
		<dl class="yellow">
			<dt>反馈待中转</dt>
			<dd id="daizhongzhuan" style="cursor: pointer;" onclick="showTab('dzz')">0</dd>
		</dl>
		<dl class="green">
			<dt>已扫描</dt>
			<dd id="alloutnum"  style="cursor: pointer;" onclick="showTab('ysm')">0</dd>
		</dl>
		
		<input type="button"  id="refresh" value="刷新" onclick="location.href='<%=request.getContextPath() %>/PDA/branchchangeexport'" style="float:left; width:100px; height:65px; cursor:pointer; border:none; background:url(../images/buttonbgimg1.gif) no-repeat; font-size:18px; font-family:'微软雅黑', '黑体'"/>
		
		
		<br clear="all"/>
	</div>
	
	<div class="saomiao_info">
		<div class="saomiao_inbox">
			<div class="saomiao_tab">
    		<ul  id="bigTag">
     		<li><a href="#" id="scancwbTag" onclick="clearMsg();$(function(){$('#baleno').parent().hide();$('#finish').parent().hide();$('#baleno').val('');$('#scancwb').val('');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();$('#scancwb').focus();$('#baleBtn').hide();})" class="light">扫描订单</a></li>
					<%if(ckfb_switch.getId()!=0&&ckfb_switch.getState().equals(SwitchEnum.ChuKuFengBao.getInfo())){ %>
					<li><a href="#" id="scanbaleTag" onclick="clearMsg();$(function(){$('#baleno').parent().show();$('#baleno').show();$('#finish').parent().show();$('#finish').show();$('#baleno').val('');$('#baleno').focus();$('#scancwb').val('');$('#scancwb').parent().hide();$('#baleBtn').show();})">合包出站</a></li>
					<%} %>
    		</ul>
 			 </div>
			<div class="saomiao_righttitle" id="pagemsg"></div>
			<!-- <form action="" method="get"> -->
			<div class="saomiao_selet">
				下一站：
				<select id="branchid" name="branchid" class="select1" onchange="getOutSum('<%=request.getContextPath()%>',$(this).val())">
					<!-- <option value="-1" selected>请选择</option> -->
					<%for(Branch b : bList){ %>
						<%if(b.getBranchid()!=Long.parseLong(usermap.get("branchid").toString())){ %>
						<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
					<%}} %>
				</select>
				站点：<input name="branchname" id="branchname" class="input_text1" onkeyup="selectnotallnexusbranch('<%=request.getContextPath()%>','deliverybranchid',$(this).val());"/>
				正确的配送站点：
				<select id="deliverybranchid" name="deliverybranchid" class="select1">
					<option value="-1">不选择</option>
					<%for(Branch b : AllbList){ %>
					<%if(b.getBranchid()!=Long.parseLong(usermap.get("branchid").toString())){ %>
						<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
					<%} }%>
				</select>
				<br/>
				<br/>
				驾驶员：
				<select id="driverid" name="driverid" class="select1">
					<option value="-1" selected>请选择</option>
					<%for(User u : uList){ %>
						<option value="<%=u.getUserid() %>" ><%=u.getRealname() %></option>
					<%} %>
		        </select>
				车辆：
				<select id="truckid" name="truckid" class="select1">
					<option value="-1" selected>请选择</option>
					<%for(Truck t : tList){ %>
						<option value="<%=t.getTruckid() %>" ><%=t.getTruckno() %></option>
					<%} %>
		        </select>
				中转原因：
					<select name="reasonid" id="reasonid" class="select1">
			        	<option value ="0">==请选择==</option>
			        	<%if(reasonlist!=null&&reasonlist.size()>0)for(Reason r : reasonlist){ %>
	           				<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent() %></option>
	           			<%} %>
			        </select>
			</div>
			<div class="saomiao_inwrith">
				<div class="saomiao_left">
					<%if(Long.parseLong(usermap.get("isImposedOutWarehouse").toString())==1){ %>
					<p>
						强制出库:</span><input type="checkbox" id="confirmflag" name="confirmflag" value="1"/>
					</p>
					<%} %>
					<%if(ckfb_switch.getId()!=0&&ckfb_switch.getState().equals(SwitchEnum.ChuKuFengBao.getInfo())){ %>
						<p style="display: none;">
						<span>包号：</span><input type="text" class="saomiao_inputtxt" name="baleno" id="baleno"  onKeyDown="if(event.keyCode==13&&$(this).val().length>0){if($(this).val().indexOf('@zd_')>-1){$('#branchid').val($(this).val().split('_')[1]);if($('#branchid').val()!=$(this).val().split('_')[1]){$('#msg').html('         （异常扫描）扫描站点失败');$('#branchid').val(0);}else{$('#msg').html('');}$(this).val('');return false;}$(this).attr('readonly','readonly');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();}" />
						<span>&nbsp;</span>
					<input type="button" id="randomCreate" value="随机生成" class="button"  onclick="ranCreate();"/>
					<input type="button" id="handCreate" value="手工输入"  class="button"  onclick="hanCreate();"/>
					
					</p>
					 <p><span>订单号：</span>
						<input type="text" class="saomiao_inputtxt" value="" id="scancwb" name="scancwb"  onKeyDown='if(event.keyCode==13&&$(this).val().length>0){exportWarehouse("<%=request.getContextPath()%>",$(this).val(),$("#branchid").val(),$("#driverid").val(),$("#truckid").val(),$("#requestbatchno").val(),$("#baleno").val(),$("#ck_switch").val(),$("#confirmflag").attr("checked")=="checked"?1:0,$("#reasonid").val(),$("#deliverybranchid").val());}'/>
					</p> 
					<p id="baleBtn" style="display: none">
						<span>&nbsp;</span>
						<!-- <input type="submit" name="finish" id="finish" value="完成扫描" class="button" onclick="$('#baleno').removeAttr('readonly');$('#baleno').val('');"/> -->
						<input type="button" name="fengbao" id="fengbao" value="封包" class="button" onclick="fengbao()"/>
						<input type="button" name="chuku" id="chuku" value="出站" class="button" onclick="chuku()"/>
					</p>
					<%}else{ %>
						<input type="hidden" id="baleno" name="baleno" value="" />
					 <p><span>订单号2：</span>
						<input type="text" class="saomiao_inputtxt" value="" id="scancwb" name="scancwb"  onKeyDown='if(event.keyCode==13&&$(this).val().length>0){exportWarehouse("<%=request.getContextPath()%>",$(this).val(),$("#branchid").val(),$("#driverid").val(),$("#truckid").val(),$("#requestbatchno").val(),$("#baleno").val(),$("#ck_switch").val(),$("#confirmflag").attr("checked")=="checked"?1:0,$("#reasonid").val(),$("#deliverybranchid").val());}'/>
					</p> 
					<%} %>
				</div>
				<div class="saomiao_right">
					<p id="msg" name="msg" ></p>
					<p id="zhongzhuan" style="display: none"><strong>中转单</strong></p>
					<p id="showcwb" name="showcwb"></p>
					<p id="cwbordertype" name="cwbordertype"></p>
					<p id="fee" name="fee"></p>
					<p id="excelbranch" name="excelbranch" ></p>
					<div style="display: none" id="EMBED">
					</div>
					<div style="display: none">
						<EMBED id='ypdj' name='ypdj' SRC='<%=request.getContextPath() %><%=ServiceUtil.waverrorPath %><%=CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl() %>' LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
					</div>
					<div style="display: none" id="errorvedio">
					</div>
				</div>
				<input type="hidden" id="requestbatchno" name="requestbatchno" value="0" />
		        <input type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" />
		        <input type="hidden" id="ck_switch" name="ck_switch" value="ck_02" />
			</div><!-- </form> -->
		</div>
	</div>
 
</div>
<div id="smallTag"class="saomiao_tab2">
			<ul>
				<li><a id="dzz_title" href="#" class="light" onclick="showTab('dzz')">反馈为待中转</a></li>
				<li><a id="ysm_title" href="#" onclick="showTab('ysm')">已扫描</a></li>
			</ul>
		</div>
		<div id="ViewList" class="tabbox">
<li id="dzz_li">			<form action="<%=request.getContextPath()%>/PDA/daizhongzhuanExport">
			<c:if test="${fn:length(dzzlist)>0 }">
				<input type ="submit" id="btnval0" value="导出Excel" class="input_button1" />
				</c:if>
				</form>
				<table width="100%" border="0" cellspacing="10" cellpadding="0" >
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
									<table  width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" id="dzz">
										<c:forEach items="${dzzlist}" var="co">
										<tr>
										<td width="120" align="center" bgcolor="#f1f1f1">${co.cwb}</td>
										<td width="120" align="center" bgcolor="#f1f1f1">${co.transcwb}</td>
										<td width="120" align="center" bgcolor="#f1f1f1">${co.packagecode}</td>
										<c:forEach items="${customerList}" var="cus">
										<c:if test="${cus.customerid==co.customerid}">
										<td width="100" align="center" bgcolor="#f1f1f1">${cus.customername}</td>
										</c:if>
										</c:forEach>
										<td width="140" align="center" bgcolor="#f1f1f1">${co.emaildate}</td>
										<td width="100" align="center" bgcolor="#f1f1f1">${co.consigneename}</td>
										<td width="100" align="center" bgcolor="#f1f1f1">${co.receivablefee}</td>
										<td align="center" bgcolor="#f1f1f1">${co.consigneeaddress}</td>
										</tr>
										</c:forEach>
									</table>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</li>
<li id="ysm_li">			<form action="<%=request.getContextPath()%>/PDA/daizhongzhuanysmExport">
				<input type ="submit" id="btnval0" value="导出Excel" class="input_button1" />
				<input type ="hidden" id="ysm_nextbranchid" name="nextbranchid" value="" class="input_button1" />
				</form>
				<table width="100%" border="0" cellspacing="10" cellpadding="0" >
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
									<table  width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" id="ysm">
								
									</table>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</li>
			</div>
			
</body>
</html>
