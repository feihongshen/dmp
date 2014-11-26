<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbOrder> kucunlist = (List<CwbOrder>)request.getAttribute("kucunlist");
List<CwbOrder> linghuokucunlist = request.getAttribute("linghuokucunlist")==null?null:(List<CwbOrder>)request.getAttribute("linghuokucunlist");

List<Customer> cList = (List<Customer>)request.getAttribute("customerlist");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>库存盘点</title>
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
<param name="CompanyName" value="北京易普联科信息技术有限公司" />
<param name="License" value="653717070728688778794958093190" />
<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 companyname="北京易普联科信息技术有限公司" license="653717070728688778794958093190"></embed>
</object>
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js" type="text/javascript"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
var LODOP; //声明为全局变量 
function prn1_preview() {	
	CreateOneFormPage();
	LODOP.PREVIEW();	
};
function prn1_print() {		
	CreateOneFormPage();
	LODOP.PRINT();	
};
function CreateOneFormPage(cwbs){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
	LODOP.PRINT_INIT("盘点数据总计");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	LODOP.ADD_PRINT_HTM(15,21,740,1100,document.getElementById("form1").innerHTML);
};

$(function(){
	getStockSum();
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

function focusCwb(){
	$("#scancwb").focus();
}
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

//得到当前库存
function getStockSum() {
	$.ajax({
		type : "POST",
		url : "<%=request.getContextPath()%>/PDA/getStockSum",
		dataType : "json",
		success : function(data) {
			$("#dangqiankucun").html(data.kucunnum);
			$("#linghuokucun").html(data.linghuokucunnum);
		}
	});
}

function gettakestockfinish() {
	$("#scancwb").attr('readonly','readonly');
	$("#finish").attr("disabled","disabled");
	$.ajax({
		type : "POST",
		url : "<%=request.getContextPath()%>/PDA/takestockfinish",
		dataType : "json",
		success : function(data) {
			$("#panying").html(data.winnum);
			$("#pankui").html(data.kuinum);
			var yingstring = "";
			var kuistring = "";
			if(data.winlist.length>0){
				for(var i=0;i<data.winlist.length;i++){
					yingstring +="<tr id='TR"+data.winlist[i].cwb+"' cwb="+data.winlist[i].cwb+">"+
					"<td width='120' align='center'>"+data.winlist[i].cwb+"</td>"+
					"<td width='120' align='center'>"+data.winlist[i].transcwb+"</td>"+
					"<td width='100' align='center'>"+data.winlist[i].customername+"</td>"+
					"<td width='140'>"+data.winlist[i].emaildate+"</td>"+
					"<td width='100'>"+data.winlist[i].consigneename+"</td>"+
					"<td width='100'>"+data.winlist[i].receivablefee+"</td>"+
					"<td align='left'>"+data.winlist[i].consigneeaddress+"</td></tr>";
				}
			}
			if(data.kuilist.length>0){
				for(var i=0;i<data.kuilist.length;i++){
					kuistring +="<tr id='TR"+data.kuilist[i].cwb+"' cwb="+data.kuilist[i].cwb+">"+
					"<td width='120' align='center'>"+data.kuilist[i].cwb+"</td>"+
					"<td width='120' align='center'>"+data.kuilist[i].transcwb+"</td>"+
					"<td width='100' align='center'>"+data.kuilist[i].customername+"</td>"+
					"<td width='140'>"+data.kuilist[i].emaildate+"</td>"+
					"<td width='100'>"+data.kuilist[i].consigneename+"</td>"+
					"<td width='100'>"+data.kuilist[i].receivablefee+"</td>"+
					"<td align='left'>"+data.kuilist[i].consigneeaddress+"</td></tr>";
				}
			}
			
			$("#panyingTable").html(yingstring);
			$("#pankuiTable").html(kuistring);
		}
	});
}

var stocknum = 0;
function submitTakestock(scancwb) {
	if (scancwb.length > 0) {
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/PDA/cwbtakestock?cwb=" + scancwb,
			dataType : "json",
			success : function(data) {
				$("#scancwb").val("");
				//将成功扫描的订单放到已盘明细中
				stocknum++;
				$("#yipandingdan").html(stocknum);
				$("#msg").html(data.error);
				addAndRemoval(data.cwb,"yipanTable",true);
			}
		});
	}
}
	
function exportField(flag){
	var cwbs = "";
	if(flag==1){
		$("#dangqiankucunTable tr").each(function(){
			var cwb = $(this).attr("cwb");
			cwbs += "'" + cwb + "',";
		});
	}else if(flag==2){
		$("#linghuokucunTable tr").each(function(){
			var cwb = $(this).attr("cwb");
			cwbs += "'" + cwb + "',";
		});
	}else if(flag==3){
		$("#yipanTable tr").each(function(){
			var cwb = $(this).attr("cwb");
			cwbs += "'" + cwb + "',";
		});
	}else if(flag==4){
		$("#pankuiTable tr").each(function(){
			var cwb = $(this).attr("cwb");
			cwbs += "'" + cwb + "',";
		});
	}else if(flag==5){
		$("#panyingTable tr").each(function(){
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
</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="saomiao_box2">
	<form id="form1">
	<div class="saomiao_topnum2">
		<dl class="blue">
			<dt>当前库存</dt>
			<dd style="cursor:pointer" onclick="tabView('table_dangqiankucun')" id="dangqiankucun">0</dd>
		</dl>
		<%if(linghuokucunlist!=null){ %>
			<dl class="red">
				<dt>领货库存</dt>
				<dd style="cursor:pointer" onclick="tabView('table_linghuokucun')" id="linghuokucun">0</dd>
			</dl>
		<%} %>
		<dl class="yellow">
			<dt>已盘订单</dt>
			<dd style="cursor:pointer" onclick="tabView('table_yipan')" id="yipandingdan">0</dd>
		</dl>
		<dl class="green">
			<dt>盘亏(件)</dt>
			<dd style="cursor:pointer" onclick="tabView('table_pankui')" id="pankui" name="pankui">0</dd>
		</dl>
		<dl class="yellow">
			<dt>盘盈(件)</dt>
			<dd style="cursor:pointer" onclick="tabView('table_panying');"  id="panying" name="panying" >0</dd>
		</dl>
		<br clear="all"/>
	</div>
	</form>
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="saomiao_righttitle2"></div>
			<div class="saomiao_inwrith2">
				<div class="saomiao_left2">
					<p><span>盘点扫描：</span>
						<input type="text" class="saomiao_inputtxt" id="scancwb" name="scancwb" value="" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){submitTakestock($(this).val());$("#finishdiv").show();}'/>
					</p>
					<p id="finishdiv" style="display: none;">
						<span>&nbsp;</span><input type="button" name="button" id="finish" value="盘点完成" class="button" onclick="gettakestockfinish();"/>
						<!-- <input type="button" name="button" id="button" value="打印盘点数据" class="button" onclick="prn1_print();"/>
						<a href="javascript:prn1_preview()">预览</a> -->
					</p>
				</div>
				<div class="saomiao_right2">
					<p id="msg" name="msg" ></p>
				</div>
			</div>
		</div>
	</div>
	<div>
		<div class="saomiao_tab2">
			<span style="float: right; padding: 10px"></span>
			<ul>
				<li><a id="table_dangqiankucun" href="#" class="light">当前库存明细</a></li>
				<%if(linghuokucunlist!=null){ %>
					<li><a id="table_linghuokucun" href="#">领货库存明细</a></li>
				<%} %>
				<li><a id="table_yipan" href="#">已盘订单明细</a></li>
				<li><a id="table_pankui" href="#">盘亏明细</a></li>
				<li><a id="table_panying" href="#">盘盈明细</a></li>
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
										<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
										<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
										<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
										<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
										<td align="center" bgcolor="#f1f1f1">地址</td>
									</tr>
								</table>
								<div style="height: 160px; overflow-y: scroll">
									<table id="dangqiankucunTable" width="100%" border="0" cellspacing="1" cellpadding="2"
										class="table_2">
										<%for(CwbOrder co : kucunlist){ %>
										<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>">
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
			<%if(linghuokucunlist!=null){ %>
			<li style="display: none">
				<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(2);'/>
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
									<table id="linghuokucunTable" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
										<%if(linghuokucunlist.size()>0)for(CwbOrder co : linghuokucunlist){ %>
										<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>">
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
			<%} %>
			<li style="display: none">
				<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(3);'/>
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr>
							<td width="10%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="2"
									class="table_5">
									<tr>
										<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
										<td width="100" align="center" bgcolor="#f1f1f1">运单号</td>
										<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
										<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
										<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
										<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
										<td align="center" bgcolor="#f1f1f1">地址</td>
									</tr>
								</table>
								<div style="height: 160px; overflow-y: scroll">
									<table id="yipanTable" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
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
										<td width="100" align="center" bgcolor="#f1f1f1">运单号</td>
										<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
										<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
										<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
										<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
										<td align="center" bgcolor="#f1f1f1">地址</td>
									</tr>
								</table>
								<div style="height: 160px; overflow-y: scroll">
									<table id="pankuiTable" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
									</table>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</li>
			
			<li style="display: none">
				<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(5);'/>
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
									<table id="panyingTable" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
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
