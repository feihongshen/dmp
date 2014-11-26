<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch,cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<User> uList = (List<User>)request.getAttribute("userList");
List<CwbOrder> weituihuorukuList = (List<CwbOrder>)request.getAttribute("weituihuorukuList");
List<CwbOrder> yituihuorukuList = (List<CwbOrder>)request.getAttribute("yituihuorukuList");
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
		url:"<%=request.getContextPath() %>/PDA/getBackInSum",
		dataType:"json",
		success : function(data) {
			$("#rukukucundanshu").html(data.weirukucount);
			$("#successcwbnum").html(data.yirukucount);
		}
	});
}


/**
 * 退货站入库扫描
 */
function submitBackIntoWarehouse(pname,scancwb,driverid,comment){
	if(scancwb.length>0){
		if($("#scanbaleTag").attr("class")=="light"){//入库根据包号扫描订单
			baledaohuo(scancwb,driverid,comment);
		}else{//入库
			$.ajax({
				type: "POST",
				url:pname+"/PDA/cwbbackintowarhouse/"+scancwb+"?driverid="+driverid,
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

function exportField(flag){
	var cwbs = "";
	if(flag==1){
		$("#type").val("weiruku");
		$("#searchForm3").submit();
	}else if(flag==2){
		$("#type").val("yiruku");
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
<div class="saomiao_box2">

	<div class="saomiao_topnum2">
		<dl style="cursor:pointer" onclick="tabView('table_weituihuoruku')"  class="blue">
			<dt>未入库合计</dt>
			<dd id="rukukucundanshu"></dd>
		</dl>
		
		<dl style="cursor:pointer" onclick="tabView('table_yituihuoruku')" class="green">
			<dt>已入库</dt>
			<dd id="successcwbnum" name="successcwbnum">0</dd>
		</dl>
		<input type="button"  id="refresh" value="刷新" onclick="location.href='<%=request.getContextPath() %>/PDA/backimport'"  style="float:left; width:100px; height:65px; cursor:pointer; border:none; background:url(../images/buttonbgimg1.gif) no-repeat; font-size:18px; font-family:'微软雅黑', '黑体'"/>
		<br clear="all">
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
		<div id="Tag" class="saomiao_tab">
				<ul id="bigTag">
					<li><a href="#" id="scancwbTag" onclick="clearMsg();$(function(){$('#baleno').parent().hide();$('#baleno').val('');$('#scancwb').val('');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();})" class="light">扫描订单</a></li>
					<li><a href="#" id="scanbaleTag" onclick="clearMsg();$(function(){$('#baleno').parent().show();$('#baleno').show();$('#finish').parent().show();$('#finish').show();$('#baleno').val('');$('#baleno').focus();$('#scancwb').val('');$('#scancwb').parent().hide();})">合包到货</a></li>
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
	
		<div>
			<div class="saomiao_tab2">
				<span style="float: right; padding: 10px">
					<input  class="input_button2" type="button" name="littlefalshbutton" id="flash" value="刷新" onclick="location.href='<%=request.getContextPath() %>/PDA/backimport'" />
				</span>
				<ul>
					<li><a id="table_weituihuoruku" href="#" class="light">未入库明细</a></li>
					<li><a id="table_yituihuoruku" href="#">已入库明细</a></li>
					<li><a href="#">异常单明细</a></li>
				</ul>
			</div>
			<div id="ViewList" class="tabbox">
				<li>
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(1);'/>
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
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="weituihuorukuTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%for(CwbOrder co : weituihuorukuList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>">
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
											<%if(weituihuorukuList!=null&&weituihuorukuList.size()==Page.DETAIL_PAGE_NUMBER){ %>
												<tr align="center"  ><td  colspan="6" style="cursor:pointer" onclick="weiruku();" id="weiruku">查看更多</td></tr>
											<%} %>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
				<li style="display: none">
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick="exportField(2);"/>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="successTable" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
										<%for(CwbOrder co : yituihuorukuList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>">
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
											<%if(yituihuorukuList!=null&&yituihuorukuList.size()==Page.DETAIL_PAGE_NUMBER){ %>
												<tr  aglin="center"><td colspan="6" style="cursor:pointer" onclick="yiruku();" id="yiruku">查看更多</td></tr>
											<%} %>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
				
				<li style="display: none">
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick="exportField(3);"/>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
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
	<form action="<%=request.getContextPath()%>/PDA/backimportexport" method="post" id="searchForm3">
		<input  type="hidden"  name="type"  id="type"/>
	</form>
	
</div>
</body>
</html>
