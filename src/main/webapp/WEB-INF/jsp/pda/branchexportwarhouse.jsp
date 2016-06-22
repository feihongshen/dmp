<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page
	import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page
	import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Truck,cn.explink.domain.Bale,cn.explink.domain.Switch"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<CwbDetailView> weichukuList = (List<CwbDetailView>)request.getAttribute("weichukulist");
List<CwbDetailView> yichukulist = (List<CwbDetailView>)request.getAttribute("yichukulist");

List<Customer> cList = (List<Customer>)request.getAttribute("customerlist");
boolean showCustomerSign= request.getAttribute("showCustomerSign")==null?false:(Boolean)request.getAttribute("showCustomerSign");
List<Branch> bList = request.getAttribute("branchlist")==null?new ArrayList<Branch>():(List<Branch>)request.getAttribute("branchlist");
List<User> uList = request.getAttribute("userList")==null?new ArrayList<User>():(List<User>)request.getAttribute("userList");
Map usermap = (Map) session.getAttribute("usermap");
String did=request.getParameter("branchid")==null?"0":request.getParameter("branchid");
long branchid=Long.parseLong(did);

	StringBuffer sb=new StringBuffer();
for(CwbDetailView ss:weichukuList){
	sb.append("'");
	sb.append(ss.getCwb());
	sb.append("',");
}
String weicwbs=sb.length()==0?"":sb.toString();
StringBuffer yichu=new StringBuffer();
for(CwbDetailView yi:yichukulist){
	yichu.append("'");
	yichu.append(yi.getCwb());
	yichu.append("',");
}
String yicwbs=yichu.length()==0?"":yichu.toString();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>出库扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css"
	type="text/css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<script language="javascript"
	src="<%=request.getContextPath()%>/js/js.js"></script>
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
	
});

	$(function(){
		getZhanDianChuZhanSum($("#branchid").val());
		getZhanDianYiChuZhanSum($("#branchid").val());
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

function tabView(tab){
	$("#"+tab).click();
}

function addAndRemoval(cwb,tab,isRemoval,branchid){
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
		$("#successTable tr[deliverybranchid='"+branchid+"']").show();
		
		$("#errorTable tr").hide();
		$("#errorTable tr[deliverybranchid='"+branchid+"']").show();
	}else{
		$("#successTable tr").show();
		$("#errorTable tr").show();
	}
}
	
	//得到当前出库的站点的库存量
	function getZhanDianChuZhanSum(deliverybranchid){
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/PDA/getZhanDianChuZhanSum?deliverybranchid="+deliverybranchid,
			dataType:"json",
			success : function(data) {
				$("#chukukucundanshu").html(data.size);
			}
		});
		
		//未出库明细、已出库明细、异常单明细只显示该下一站明细
		if(deliverybranchid>0){
			$("#weichukuTable tr").hide();
			$("#weichukuTable tr[deliverybranchid='"+deliverybranchid+"']").show();
			
			$("#successTable tr").hide();
			$("#successTable tr[deliverybranchid='"+deliverybranchid+"']").show();
			
			$("#errorTable tr").hide();
			$("#errorTable tr[deliverybranchid='"+deliverybranchid+"']").show();
		}else{
			$("#weichukuTable tr").show();
			$("#successTable tr").show();
			$("#errorTable tr").show();
		}
	}
	function getZhanDianYiChuZhanSum(deliverybranchid){
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/PDA/getZhanDianYiChuZhanSum?deliverybranchid="+deliverybranchid,
			dataType:"json",
			success : function(data) {
				$("#yichukukucundanshu").html(data.size);
			}
		});
		
		//未出库明细、已出库明细、异常单明细只显示该下一站明细
		if(deliverybranchid>0){
			$("#weichukuTable tr").hide();
			$("#weichukuTable tr[deliverybranchid='"+deliverybranchid+"']").show();
			
			$("#successTable tr").hide();
			$("#successTable tr[deliverybranchid='"+deliverybranchid+"']").show();
			
			$("#errorTable tr").hide();
			$("#errorTable tr[deliverybranchid='"+deliverybranchid+"']").show();
		}else{
			$("#weichukuTable tr").show();
			$("#successTable tr").show();
			$("#errorTable tr").show();
		}
	}
/**
 * 出库扫描
 */
var branchStr=[];
var Cwbs="";
function branchexportWarehouse(pname,scancwb,branchid,driverid,ck_switch,confirmflag){
	if(<%=bList!=null&&bList.isEmpty()%>){
		alert("抱歉，系统未分配下一站点，请联络您公司管理员！");
		return;
	}
	if(scancwb.length>0){
		
		$.ajax({
			type: "POST",
			url:pname+"/PDA/cwbbranchexportwarhouse/"+scancwb+"?branchid="+branchid+"&driverid="+driverid+"&confirmflag="+confirmflag,
			dataType:"json",
			success : function(data) {
				$("#scancwb").val("");
				if(data.statuscode=="000000"){
						if(data.body.cwbOrder.scannum==1){
							if(Cwbs.indexOf("|"+scancwb+"|")==-1){
								Cwbs += "|"+scancwb+"|";
								$("#singleoutnum").html(parseInt($("#singleoutnum").html())+1);
								$("#alloutnum").html(parseInt($("#alloutnum").html())+1);
								branchStr[data.body.cwbOrder.nextbranchid] = $("#singleoutnum").html();
							}
						}
						
						$("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
						$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
						
						$("#scansuccesscwb").val(scancwb);
						$("#showcwb").html("订 单 号："+scancwb);
						
						/*if(data.body.cwbOrder.sendcarnum>1){
							document.ypdj.Play();
						}
						if(data.body.cwbbranchnamewav!=""&&data.body.cwbbranchnamewav!=pname+"/wav/"){
							$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random());
						}else{
							$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
						}*/
				}else if(data.statuscode=="000001"){
					$("#msg").html(data.errorinfo);
				}else{
					$("#excelbranch").html("");
					$("#showcwb").html("");
					$("#msg").html(scancwb+"         （异常扫描）"+data.errorinfo);
					addAndRemoval(scancwb,"errorTable",false,$("#branchid").val());
					//errorvedioplay(pname,data);
				}
				$("#responsebatchno").val(data.responsebatchno);
				batchPlayWav(data.wavList);
			}
		});
	}
}
function getsum(branchid){
	if(branchStr[branchid]>0){
		$("#singleoutnum").html(branchStr[branchid]);
	}else{
		$("#singleoutnum").html(0);
	}
}

function clearMsg(){
	$("#msg").html("");
	$("#showcwb").html("");
	$("#excelbranch").html("");
}


function exportField(flag,branchid){
	var cwbs = "";
	if(flag==1){
		cwbs=$("#weichuku").val();
	}else if(flag==2){
		cwbs=$("#yichuku").val();
	}else if(flag==3){//修改导出问题
			$("#errorTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}
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
function  falshdata(){
	 $("#refresh").attr("disabled","disabled");
	 $("#flash").attr("disabled","disabled");
	 $("#refresh").val("请稍后……");
	 $("#flash").val("请稍后……");
	getZhanDianChuZhanSum($("#branchid").val());
	getZhanDianYiChuZhanSum($("#branchid").val());
	getdaichuzhan();
	getyisaomiao();
	$("#refresh").val("刷新");
	 $("#flash").val("刷新");
	 $("#refresh").removeAttr("disabled");
	 $("#flash").removeAttr("disabled");
	
}

function getyisaomiao(){
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbranchexportwarehouseyisaomiao",
		data:{branchid:$("#branchid").val()},
		success:function(data){
			var optionstring = "";
			for ( var i = 0; i < data.length; i++) {
				<%if(showCustomerSign){%>
				optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' deliverybranchid='"+data[i].deliverybranchid+"' >"
				+"<td width='120' align='center'>"+data[i].cwb+"</td>"
				+"<td width='100' align='center'> "+data[i].customername+"</td>"
				+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
				+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
				+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
				+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
				+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
				+ "</tr>";
			<%}else{%>
				optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' deliverybranchid='"+data[i].deliverybranchid+"'>"
				+"<td width='120' align='center'>"+data[i].cwb+"</td>"
				+"<td width='100' align='center'> "+data[i].customername+"</td>"
				+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
				+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
				+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
				+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
				+ "</tr>";
			<%}%>
			}
			$("#successTable").html(optionstring);
		}
	});
	
}


function getdaichuzhan(){
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbranchexportwarehousedaichuku",
		data:{branchid:$("#branchid").val()},
		success:function(data){
			var optionstring = "";
			for ( var i = 0; i < data.length; i++) {
				<%if(showCustomerSign){%>
				optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' deliverybranchid='"+data[i].deliverybranchid+"'>"
				+"<td width='120' align='center'>"+data[i].cwb+"</td>"
				+"<td width='100' align='center'> "+data[i].customername+"</td>"
				+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
				+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
				+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
				+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
				+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
				+ "</tr>";
			<%}else{%>
				optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' deliverybranchid='"+data[i].deliverybranchid+"' >"
				+"<td width='120' align='center'>"+data[i].cwb+"</td>"
				+"<td width='100' align='center'> "+data[i].customername+"</td>"
				+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
				+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
				+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
				+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
				+ "</tr>";
			<%}%>
			}
			$("#weichukuTable").html(optionstring);
		}
	});
	
	
	
}
function tohome(){
	window.location.href="<%=request.getContextPath()%>/PDA/branchexportwarhouse?branchid="
				+ $("#branchid").val();
	}
$(function(){
	$("#branchid").combobox();
	})
</script>
</head>
<body style="background: #f5f5f5" marginwidth="0" marginheight="0">
	<div class="saomiao_box2">

		<div class="saomiao_topnum2">
			<dl class="blue">
				<dt>待出站</dt>
				<dd style="cursor: pointer" onclick="tabView('table_weichuku')"
					id="chukukucundanshu">0</dd>
			</dl>

			<dl class="green">
				<dt>已扫描</dt>
				<dd style="cursor: pointer" onclick="tabView('table_yichuku')"
					id="yichukukucundanshu">0</dd>
			</dl>
			<input type="button" id="refresh" value="刷新"
				onclick="location.href='<%=request.getContextPath()%>/PDA/branchexportwarhouse'"
				style="float: left; width: 100px; height: 65px; cursor: pointer; border: none; background: url(../images/buttonbgimg1.gif) no-repeat; font-size: 18px; font-family: '微软雅黑', '黑体'" />

			<!-- <dl class="yellow">
			<dt>本次合计出库</dt>
			<dd id="alloutnum">0</dd>
		</dl> -->
			<br clear="all" />
		</div>

		<div class="saomiao_info2">
			<div class="saomiao_inbox2">
				<!--<div class="kfsh_tabbtn">
				 <ul id="bigTag">
					<li><a href="#" onclick="clearMsg();$(function(){$('#baleno').parent().hide();$('#finish').parent().hide();$('#baleno').val('');$('#scancwb').val('');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();})" class="light">扫描订单</a></li>
				</ul>
			</div> -->
				<div class="saomiao_righttitle2" id="pagemsg"></div>
				<!-- <form action="" method="get"> -->
				<div class="saomiao_selet2">
					下一站： <select id="branchid" name="branchid" onchange="tohome()" class="select1">
						<%
							for(Branch b : bList){
						%>
						<%
							if(b.getBranchid()!=Long.parseLong(usermap.get("branchid").toString())){
						%>
						<option value="<%=b.getBranchid()%>"
							<%if(branchid==b.getBranchid()) {%> selected <%}%>><%=b.getBranchname()%></option>
						<%
							}}
						%>
					</select> 驾驶员： <select id="driverid" name="driverid" class="select1">
						<option value="-1" selected>请选择</option>
						<%
							for(User u : uList){
						%>
						<option value="<%=u.getUserid()%>"><%=u.getRealname()%></option>
						<%
							}
						%>
					</select>
				</div>
				<div class="saomiao_inwrith2">
					<div class="saomiao_left2">
						<%
							if(Long.parseLong(usermap.get("isImposedOutWarehouse").toString())==1){
						%>
						<p>
							强制出库:</span><input type="checkbox" id="confirmflag" name="confirmflag"
								value="1" />
						</p>
						<%
							}
						%>
						<p style="display: none;">
							<span>包号：</span><input type="text" class="saomiao_inputtxt2"
								name="baleno" id="baleno"
								onKeyDown="if(event.keyCode==13&&$(this).val().length>0){$(this).attr('readonly','readonly');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();}" />
						</p>
						<p>
							<span>单/包号：</span> <input type="text" class="saomiao_inputtxt2"
								value="" id="scancwb" name="scancwb"
								onKeyDown='if(event.keyCode==13&&$(this).val().length>0){branchexportWarehouse("<%=request.getContextPath()%>",$(this).val(),$("#branchid").val(),$("#driverid").val(),$("#ck_switch").val(),$("#confirmflag").attr("checked")=="checked"?1:0);}' />
						</p>
						<p style="display: none;">
							<span>&nbsp;</span><input type="submit" name="finish" id="finish"
								value="完成扫描" class="button"
								onclick="$('#baleno').removeAttr('readonly');$('#baleno').val('');">
						</p>
					</div>
					<div class="saomiao_right2">
						<p id="msg" name="msg"></p>
						<p id="showcwb" name="showcwb"></p>
						<p id="excelbranch" name="excelbranch"></p>
						<div style="display: none" id="EMBED"></div>
						<div style="display: none">
							<EMBED id='ypdj' name='ypdj'
								SRC='<%=request.getContextPath()%><%=ServiceUtil.waverrorPath%><%=CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl()%>'
								LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0
								HEIGHT=0></EMBED>
						</div>
						<div style="display: none" id="errorvedio"></div>
					</div>
					<input type="hidden" id="requestbatchno" name="requestbatchno"
						value="0" /> <input type="hidden" id="scansuccesscwb"
						name="scansuccesscwb" value="" /> <input type="hidden"
						id="ck_switch" name="ck_switch" value="ck_02" />
				</div>
				<!-- </form> -->
			</div>
		</div>


		<div>
			<div class="saomiao_tab2">
				<span style="float: right; padding: 10px"> <input
					class="input_button1" type="button" name="littlefalshbutton"
					id="flash" value="刷新"
					onclick="location.href='<%=request.getContextPath()%>/PDA/branchexportwarhouse'" />
				</span>
				<ul id="smallTag">
					<li><a id="table_weichuku" href="#" class="light">待出库明细</a></li>
					<li><a id="table_yichuku" href="#">已出库明细</a></li>
					<li><a href="#">异常单明细</a></li>
				</ul>
			</div>
			<div id="ViewList" class="tabbox">
				<li><input type="button" id="btnval0" value="导出Excel"
					class="input_button1"
					onclick='exportField(1,$("#branchid").val());' />
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
											<%
												if(showCustomerSign){
											%>
											<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%
												}
											%>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
										<%
											for(CwbDetailView co : weichukuList){
										%>
										<tr id="TR<%=co.getCwb()%>" cwb="<%=co.getCwb()%>"
											customerid="<%=co.getCustomerid()%>"
											deliverybranchid="<%=co.getDeliverybranchid()%>">
											<td width="120" align="center"><%=co.getCwb()%></td>
											<td width="100" align="center">
												<%
													for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}}
												%>
											</td>
											<td width="140"><%=co.getEmaildate()%></td>
											<td width="100"><%=co.getConsigneename()%></td>
											<td width="100"><%=co.getReceivablefee().doubleValue()%></td>
											<%
												if(showCustomerSign){
											%>
											<td width="100"><%=co.getRemarkView()%></td>
											<%
												}
											%>
											<td align="left"><%=co.getConsigneeaddress()%></td>
										</tr>
										<%
											}
										%>

									</table>
								</td>
							</tr>
						</tbody>
					</table></li>
				<li style="display: none"><input type="button" id="btnval0"
					value="导出Excel" class="input_button1"
					onclick='exportField(2,$("#branchid").val());' />
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
											<%
												if(showCustomerSign){
											%>
											<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%
												}
											%>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
										<%
											for(CwbDetailView co : yichukulist){
										%>
										<tr id="TR<%=co.getCwb()%>" cwb="<%=co.getCwb()%>"
											customerid="<%=co.getCustomerid()%>"
											deliverybranchid="<%=co.getDeliverybranchid()%>">
											<td width="120" align="center"><%=co.getCwb()%></td>
											<td width="100" align="center">
												<%
													for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}}
												%>
											</td>
											<td width="140"><%=co.getEmaildate()%></td>
											<td width="100"><%=co.getConsigneename()%></td>
											<td width="100"><%=co.getReceivablefee().doubleValue()%></td>
											<%
												if(showCustomerSign){
											%>
											<td width="100"><%=co.getRemarkView()%></td>
											<%
												}
											%>
											<td align="left"><%=co.getConsigneeaddress()%></td>
										</tr>
										<%
											}
										%>
									</table>
								</td>
							</tr>
						</tbody>
					</table></li>

				<li style="display: none"><input type="button" id="btnval0"
					value="导出Excel" class="input_button1"
					onclick='exportField(3,$("#branchid").val());' />
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
											<%
												if(showCustomerSign){
											%>
											<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%
												}
											%>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
										<table id="errorTable" width="100%" border="0" cellspacing="1"
											cellpadding="2" class="table_2"></table>
										</td>
										</tr>
										</tbody>
									</table>
									</li>
									</div>
									</div>

									<form action="<%=request.getContextPath()%>/PDA/exportExcle"
										method="post" id="searchForm2">
										<input type="hidden" name="cwbs" id="cwbs" value="" /> <input
											type="hidden" name="exportmould2" id="exportmould2" /> <input
											type="hidden" name="yichuku" id="yichuku" value="<%=yicwbs%>" />
										<input type="hidden" name="weichuku" id="weichuku"
											value="<%=weicwbs%>" />
									</form>

									</div>
</body>
</html>
