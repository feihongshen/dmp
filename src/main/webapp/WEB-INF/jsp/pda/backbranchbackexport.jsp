<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer,cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Truck,cn.explink.domain.Bale,cn.explink.domain.Switch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbOrder> weichukuList = (List<CwbOrder>)request.getAttribute("weichukulist");
List<CwbOrder> yichukulist = (List<CwbOrder>)request.getAttribute("yichukulist");

List<Customer> cList = (List<Customer>)request.getAttribute("customerlist");

List<Branch> bList = (List<Branch>)request.getAttribute("branchlist");
List<User> uList = (List<User>)request.getAttribute("userList");
List<Truck> tList = (List<Truck>)request.getAttribute("truckList");
List<Bale> balelist = (List<Bale>)request.getAttribute("balelist");
//Switch ck_switch = (Switch) request.getAttribute("ck_switch");
Switch ckfb_switch = (Switch) request.getAttribute("ckfb_switch");
Map usermap = (Map) session.getAttribute("usermap");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>退货站退货出站扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
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
function exportUntreadWarehouse(pname,scancwb,branchid,driverid,truckid,requestbatchno,baleno,ck_switch,confirmflag){
	if(<%=bList.isEmpty()%>){
		alert("抱歉，系统未分配退货站点，请联络您公司管理员！");
		return;
	}
	if(scancwb.length>0){
		$.ajax({
			type: "POST",
			url:pname+"/PDA/cwbbackexportUntreadWarhouse/"+scancwb+"?branchid="+branchid+"&driverid="+driverid+"&truckid="+truckid+"&confirmflag="+confirmflag+"&requestbatchno="+requestbatchno+"&baleno="+baleno,
			dataType:"json",
			success : function(data) {
				$("#scancwb").val("");
				if(data.statuscode=="000000"){
					/* if(data.body.cwbOrder.nextbranchid!=0&&data.body.cwbOrder.nextbranchid!=branchid&&branchid!=-1){
						
						if(ck_switch=="ck_01"){//允许出库
							var con = confirm("出库站点与分拣站点不一致，是否强制出库？");
							if(con == false){
								$("#excelbranch").html("");
								$("#tuihuo").hide();
								$("#showcwb").html("");
								$("#msg").html(scancwb+"         （异常扫描）出库站点与分拣站点不一致");
								addAndRemoval(scancwb,"errorTable",false,$("#branchid").val());
								$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+pname+"/images/waverror/fail.wav&a="+Math.random());
							}else{
								$.ajax({
									type: "POST",
									url:pname+"/PDA/cwbbackexportUntreadWarhouse/"+scancwb+"?branchid="+branchid+"&driverid="+driverid+"&truckid="+truckid+"&confirmflag=1&requestbatchno="+requestbatchno+"&baleno="+baleno,
									dataType:"json",
									success : function(data) {
										if(data.statuscode=="000000"){
											$("#tuihuo").show();
											$("#showcwb").html("订 单 号："+scancwb);
											$("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
											$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
											
											$("#branchid").val(data.body.cwbOrder.nextbranchid);
											$("#scansuccesscwb").val(scancwb);
											getBackBranchOutSum(pname,data.body.cwbOrder.nextbranchid);
											//将成功扫描的订单放到已入库明细中
											addAndRemoval(data.body.cwbOrder.cwb,"successTable",true,$("#branchid").val());
											if(data.body.cwbOrder.sendcarnum>1){
												document.ypdj.Play();
											}
											if(data.body.cwbbranchnamewav!=pname+"/wav/"){
												$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random());
											}else{
												$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
											}
										}else{
											$("#tuihuo").hide();
											$("#excelbranch").html("");
											$("#showcwb").html("");
											$("#msg").html(scancwb+"         （异常扫描）"+data.errorinfo);
										}
									}
								});
							}
						}else{
							$("#tuihuo").hide();
							$("#excelbranch").html("");
							$("#showcwb").html("");
							$("#msg").html(scancwb+"         （异常扫描）出库站点与分拣站点不一致");
							addAndRemoval(scancwb,"errorTable",false,$("#branchid").val());
							$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+pname+"/images/waverror/fail.wav&a="+Math.random());
						}
					}else{ */
						$("#tuihuo").show();
						$("#showcwb").html("订 单 号："+scancwb);
						$("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
						$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
						
						$("#branchid").val(data.body.cwbOrder.nextbranchid);
						$("#scansuccesscwb").val(scancwb);
						//getBackBranchOutSum(pname,data.body.cwbOrder.nextbranchid);
						//将成功扫描的订单放到已入库明细中
						//addAndRemoval(data.body.cwbOrder.cwb,"successTable",true,$("#branchid").val());
						if(data.body.cwbOrder.sendcarnum>1){
							document.ypdj.Play();
						}
						if(data.body.cwbbranchnamewav!=pname+"/wav/"){
							$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random());
						}else{
							$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
						}
					//}
				}else{
					$("#tuihuo").hide();
					$("#excelbranch").html("");
					$("#showcwb").html("");
					$("#msg").html(scancwb+"         （异常扫描）"+data.errorinfo);
					addAndRemoval(scancwb,"errorTable",false,$("#branchid").val());
					errorvedioplay(pname,data);
				}
				$("#responsebatchno").val(data.responsebatchno);
				getBackBranchOutSum(pname,$("#branchid").val());
			}
		});
	}
}

$(function(){
	 getBackBranchOutSum('<%=request.getContextPath()%>','<%=bList.size()==0?0:bList.get(0).getBranchid()%>');
	 $("#scancwb").focus();
});


function getBackBranchOutSumAndList(pname,branchid) {
	getBackBranchOutSum(pname, branchid);
	getweichuzhan();
	getyichuzhan();
}
//得到当前出库的站点的库存量
function getBackBranchOutSum(pname,branchid){
	$.ajax({
		type: "POST",
		url:pname+"/PDA/getBackBranchBackOutSum?nextbranchid="+branchid,
		dataType:"json",
		success : function(data) {
			$("#chukukucundanshu").html(data.weichukucount);
			$("#alloutnum").html(data.yichukucount);
		}
	});
	//未出库明细、已出库明细、异常单明细只显示该下一站明细
	if(branchid>0){
		$("#weichukuTable tr").hide();
		$("#weichukuTable tr[nextbranchid='"+branchid+"']").show();
		
		$("#successTable tr").hide();
		$("#successTable tr[nextbranchid='"+branchid+"']").show();
		
		$("#errorTable tr").hide();
		$("#errorTable tr[nextbranchid='"+branchid+"']").show();
	}else{
		$("#weichukuTable tr").show();
		$("#successTable tr").show();
		$("#errorTable tr").show();
	}
}


function exportField(flag,branchid){
	var cwbs = "";
	if(flag==1){
		if(branchid>0){
			$("#weituihuochukuTable tr[nextbranchid='"+branchid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}else{
			$("#weituihuochukuTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}
	}else if(flag==2){
		if(branchid>0){
			$("#successTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}else{
			$("#successTable tr[nextbranchid='"+branchid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}
	}else if(flag==3){
		if(branchid>0){
			$("#errorTable tr[nextbranchid='"+branchid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}else{
			$("#errorTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}
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
	 getBackBranchOutSum('<%=request.getContextPath()%>',$("#branchid").val());
	 getweichuzhan();
	 getyichuzhan();
	 $("#refresh").val("刷新");
	 $("#flash").val("刷新");
	 $("#refresh").removeAttr("disabled");
	 $("#flash").removeAttr("disabled");
}
function getyichuzhan(){
	
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbackbranchbackexportyichuzhanlist",
		data:{branchid:$("#branchid").val()},
		success:function(data){
			var optionstring = "";
			for ( var i = 0; i < data.length; i++) {
				optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"'customerid='"+data[i].customerid+"' nextbranchid='"+data[i].nextbranchid+"' >"
				+"<td width='120' align='center'>"+data[i].cwb+"</td>"
				+"<td width='100' align='center'> "+data[i].customername+"</td>"
				+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
				+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
				+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
				+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
				+ "</tr>";
			}
			$("#successTable").html(optionstring);
		}
	});
};

function getweichuzhan(){
	
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbackbranchbackexportweichuzhanlist",
		data:{branchid:$("#branchid").val()},
		success:function(data){
			var optionstring = "";
			for ( var i = 0; i < data.length; i++) {
				optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"'customerid='"+data[i].customerid+"' nextbranchid='"+data[i].nextbranchid+"' >"
				+"<td width='120' align='center'>"+data[i].cwb+"</td>"
				+"<td width='100' align='center'> "+data[i].customername+"</td>"
				+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
				+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
				+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
				+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
				+ "</tr>";
			}
			$("#weituihuochukuTable").html(optionstring);
		}
	});
};

</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="saomiao_box">

	<div class="saomiao_topnum">
		<dl class="blue">
			<dt>待退货出站</dt>
			<dd  style="cursor:pointer" onclick="tabView('table_weituihuochuku')" id="chukukucundanshu">0</dd>
		</dl>
		
		<dl class="green">
			<dt>已扫描</dt>
			<dd style="cursor:pointer" onclick="tabView('table_yituihuochuku')" id="alloutnum">0</dd>
		</dl>
		<input type="button"  id="refresh" value="刷新" onclick="location.href='<%=request.getContextPath() %>/PDA/backbranchbackexport'"style="float:left; width:100px; height:65px; cursor:pointer; border:none; background:url(../images/buttonbgimg1.gif) no-repeat; font-size:18px; font-family:'微软雅黑', '黑体'"/>
		
		<br clear="all">
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<!-- <div class="saomiao_lefttitle">扫描订单</div> -->
			<div class="saomiao_righttitle2" id="pagemsg"></div>
			<!-- <form action="" method="get"> -->
			<div class="saomiao_selet2">
			
				下一站：
				<select id="branchid" name="branchid" onchange="getBackBranchOutSumAndList('<%=request.getContextPath()%>',$(this).val());">
					<%for(Branch b : bList){ %>
						<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
					<%} %>
				</select>
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
					<%if(Long.parseLong(usermap.get("isImposedOutWarehouse").toString())==1){ %>
						<p>
							强制出库:</span><input type="checkbox" id="confirmflag" name="confirmflag" value="1"/>
						</p>
					<%} %>
					<%if(ckfb_switch.getId()!=0&&ckfb_switch.getState().equals(SwitchEnum.ChuKuFengBao.getInfo())){ %>
						<p style="display: none;"><span>包号：</span><input type="text" class="saomiao_inputtxt" name="baleno" id="baleno" /></p>
					<%}else{ %>
						<input type="hidden" id="baleno" name="baleno" value="" />
					<%} %>
					<p><span>订单号：</span>
						<input type="text" class="saomiao_inputtxt" value="" id="scancwb" name="scancwb"  onKeyDown='if(event.keyCode==13&&$(this).val().length>0){exportUntreadWarehouse("<%=request.getContextPath()%>",$(this).val(),$("#branchid").val(),$("#driverid").val(),0,$("#requestbatchno").val(),$("#baleno").val(),$("#ck_switch").val(),$("#confirmflag").attr("checked")=="checked"?1:0);}'/>
					</p>
				</div>
				<div class="saomiao_right2">
					<p id="tuihuo" style="display: none"><strong>退货单</strong></p>
					<p id="msg" name="msg" ></p>
					<p id="showcwb" name="showcwb"></p>
					<p id="excelbranch" name="excelbranch" ></p>
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
				<input  class="input_button2" type="button" name="littlefalshbutton" id="flash" value="刷新" onclick="location.href='<%=request.getContextPath() %>/PDA/backbranchbackexport'" />
				</span>
				<ul>
					<li><a id="table_weituihuochuku" href="#" class="light">待出库明细</a></li>
					<li><a id="table_yituihuochuku" href="#">已出库明细</a></li>
					<li><a href="#">异常单明细</a></li>
				</ul>
			</div>
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
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="weituihuochukuTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%if(weichukuList!=null&&weichukuList.size()>0)for(CwbOrder co : weichukuList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid()%>">
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
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
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="successTable" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
											<%if(yichukulist!=null&&yichukulist.size()>0)for(CwbOrder co : yichukulist){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid()%>">
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
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
 
 
</div>
</body>
</html>