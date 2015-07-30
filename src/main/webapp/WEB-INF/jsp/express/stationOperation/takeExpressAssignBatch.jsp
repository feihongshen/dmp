<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<User> deliverList = (List<User>)request.getAttribute("deliverList");
List<JSONObject> objList = (List<JSONObject>)request.getAttribute("objList");
List<CwbDetailView> todayToTakeList = (List<CwbDetailView>)request.getAttribute("todayToTakeList");
List<CwbDetailView> historyweilinghuolist = (List<CwbDetailView>)request.getAttribute("historyweilinghuolist");
List<CwbDetailView> yilinghuolist = (List<CwbDetailView>)request.getAttribute("yilinghuolist");
List<Customer> cList = (List<Customer>)request.getAttribute("customerlist");
String did=request.getParameter("deliverid")==null?"0":request.getParameter("deliverid");
long deliverid=Long.parseLong(did);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>批量揽件分配/调整</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/express/takeExpressAssign.js"></script>
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
	getExpressCount("<%=request.getContextPath() %>",$("#deliverid").val());
	 $("#preOrderNos").focus();
});

function tabView(tab){
	$("#"+tab).click();
}
function sub(){
	if($("#deliverid").val()==-1){
		$("#msg").html("确定批量处理前请选择小件员");
		return false;
	}else if($.trim($("#preOrderNos").val()).length==0){
		$("#msg").html("确定批量处理前请输入订单号，多个订单用回车分割");
		return false;
	}
	$("#subButton").val("正在处理！请稍候...");
	$("#subButton").attr('disabled','disabled');
	$("#subForm").submit();
	
}

function exportField(flag,deliverid){
	var cwbs = "";
	if(flag==1){
		$("#type").val(flag);
		$("#searchForm3").submit();
	}else if(flag==2){
		$("#type").val(flag);
		$("#searchForm3").submit();
	}else if(flag==3){
			$("#errorTable tr[name='export']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		
		if(cwbs.length>0){
			cwbs = cwbs.substring(0, cwbs.length-1);
		}
		if(cwbs!=""){
			$("#exportcwbs").val(cwbs);
			$("#btnval").attr("disabled","disabled");
		 	$("#btnval").val("请稍后……");
		 	$("#searchForm2").submit();
		}else{
			alert("没有订单信息，不能导出！");
		};
	}else if(flag==4){
		$("#type").val(flag);
		$("#searchForm3").submit();
	}
}

var toweilingpage=1;var historyweilingpage=1;var yipage=1;
function todayweilinghuo(){
	toweilingpage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbranchideliverweilinglist",
		data:{"page":toweilingpage,"deliverid":$("#deliverid").val()},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' deliverid='"+data[i].deliverid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].inSitetime+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
					+ "</tr>";
				}
				$("#todayweilinghuo").remove();
				$("#todayweilinghuoTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
					var more='<tr align="center"  ><td  colspan="6" style="cursor:pointer" onclick="todayweilinghuo();" id="todayweilinghuo">查看更多</td></tr>';
				$("#todayweilinghuoTable").append(more);
				}
			}
			
		}
	});
	
	
}

function  historyweilinghuo(){
	historyweilingpage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbranchideliverweilinghistorylist",
		data:{"page":historyweilingpage,"deliverid":$("#deliverid").val()},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' deliverid='"+data[i].deliverid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].inSitetime+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
					+ "</tr>";
				}
				$("#historyweilinghuo").remove();
				$("#historyweilinghuoTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
					var more='<tr align="center"  ><td  colspan="6" style="cursor:pointer" onclick="historyweilinghuo();" id="historyweilinghuo">查看更多</td></tr>';
				$("#historyweilinghuoTable").append(more);
				}
			}
			
		}
	});
	
	
}  

function tohome(){
	window.location.href="<%=request.getContextPath() %>/stationOperation/takeExpressAssignBatch?deliverid="+$("#deliverid").val();
}
</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="saomiao_box">


	<div class="saomiao_tab2">
		<ul>
			<li><a href="<%=request.getContextPath()%>/stationOperation/takeExpressAssign" >逐单操作</a></li>		
			<li><a href="#" class="light" >批量操作</a></li>
		</ul>
	</div>
 
 
	<div class="saomiao_topnum">
		<dl class="blue">
			<dt>今日待揽收</dt>
			<dd style="cursor:pointer" onclick="tabView('table_todayToTake')" id="todayToTakeCount">0</dd>
		</dl>
		<dl class="yellow">
			<dt>今日已揽收</dt>
			<dd style="cursor:pointer" onclick="tabView('table_todayTaked')" id="todayTakedCount">0</dd>
		</dl>
		
		<br clear="all">
	</div>
		<input type="hidden" name="clist" id="clist" value="<%=cList %>"/>
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="saomiao_righttitle2" id="pagemsg"></div>
			<form id="subForm" action="takeExpressAssignBatch" method="post">
			<div class=saomiao_inwrith2>
				<div class="saomiao_left2">
				<p><span>小件员：</span>
					<select id="deliverid" name="deliverid" onchange="tohome();" class="select1">
						<option value="-1" selected>请选择</option>
						<%for(User deliver : deliverList){ %>
							<option value="<%=deliver.getUserid() %>"  <%if(deliverid==deliver.getUserid()) {%>selected=selected<%} %> ><%=deliver.getRealname() %></option>
						<%} %>
			        </select>*
				</p>
			    <p><span>预订单编号：</span>
					<textarea name="preOrderNos" cols="45" rows="3" id="preOrderNos"></textarea>
				</p>
				<span>&nbsp;</span><input type="button" id="subButton" value="确定批量处理" onclick="sub()" class="button" />
				</div>
				<div class="saomiao_right2">
					<p id="msg" name="msg" >${msg }</p>
				</div>
			</div>
			</form>
		</div>
	</div>
	<div>
	<div class="saomiao_tab2">
			<span style="float: right; padding: 10px"><!-- <input
				type="button" class="input_button2" value="导出" /> --></span>
			<ul id="smallTag">
				<li><a id="table_todayToTake" href="#" class="light">今日待揽收明细</a></li>
				<li><a id="table_todayTaked" href="#">今日已揽收明细</a></li>
			</ul>
		</div>
		<div id="ViewList" class="tabbox">
			<li>
				<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(1,$("#deliverid").val());'/>
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr>
							<td width="10%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="2"
									class="table_5">
									<tr>
										<td width="50" align="center" bgcolor="#f1f1f1">预订单编号</td>
										<td width="50" align="center" bgcolor="#f1f1f1">寄件人</td>
										<td width="50" align="center" bgcolor="#f1f1f1">手机号</td>
										<td width="50" align="center" bgcolor="#f1f1f1">固话</td>
										<td width="100" align="center" bgcolor="#f1f1f1">取件地址</td>
									</tr>
								</table>
								<div style="height: 160px; overflow-y: scroll">
									<table id="todayweilinghuoTable" width="100%" border="0" cellspacing="1" cellpadding="2"
										class="table_2">
										<%if(todayToTakeList!=null&&!todayToTakeList.isEmpty())for(CwbDetailView todayToTake : todayToTakeList){ %>
										<tr id="TR<%=todayToTake.getCwb() %>" cwb="<%=todayToTake.getCwb() %>" customerid="<%=todayToTake.getCustomerid() %>" deliverid="<%=todayToTake.getDeliverid()%>">
											<td width="120" align="center"><%=todayToTake.getCwb() %></td>
											<td width="140"><%=todayToTake.getInSitetime() %></td>
											<td width="100"><%=todayToTake.getConsigneename() %></td>
											<td width="100"><%=todayToTake.getReceivablefee().doubleValue() %></td>
											<td align="left"><%=todayToTake.getConsigneeaddress() %></td>
										</tr>
										<%} %>
										<%if(todayToTakeList!=null&&todayToTakeList.size()==Page.DETAIL_PAGE_NUMBER){ %>
										<tr align="center"  ><td  colspan="6" style="cursor:pointer" onclick="todayweilinghuo();" id="todayweilinghuo">查看更多</td></tr>
										<%} %>
									</table>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</li>
			<li style="display: none">
				<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(4,$("#deliverid").val());'/>
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr>
							<td width="10%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="2"
									class="table_5">
									<tr>
										<td width="50" align="center" bgcolor="#f1f1f1">预订单编号</td>
										<td width="50" align="center" bgcolor="#f1f1f1">寄件人</td>
										<td width="50" align="center" bgcolor="#f1f1f1">手机号</td>
										<td width="50" align="center" bgcolor="#f1f1f1">固话</td>
										<td width="100" align="center" bgcolor="#f1f1f1">取件地址</td>
									</tr>
								</table>
								<div style="height: 160px; overflow-y: scroll">
									<table id="historyweilinghuoTable" width="100%" border="0" cellspacing="1" cellpadding="2"
										class="table_2">
										<%if(historyweilinghuolist!=null&&historyweilinghuolist.size()>0)for(CwbDetailView co : historyweilinghuolist){ %>
										<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" deliverid="<%=co.getDeliverid()%>">
											<td width="120" align="center"><%=co.getCwb() %></td>
											<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
											<td width="140"><%=co.getInSitetime() %></td>
											<td width="100"><%=co.getConsigneename() %></td>
											<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
											<td align="left"><%=co.getConsigneeaddress() %></td>
										</tr>
										<%} %>
										<%if(historyweilinghuolist!=null&&historyweilinghuolist.size()==Page.DETAIL_PAGE_NUMBER){ %>
										<tr align="center"  ><td  colspan="6" style="cursor:pointer" onclick="historyweilinghuo();" id="historyweilinghuo">查看更多</td></tr>
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
		<input type="hidden" name="cwbs" id="exportcwbs" value=""/>
		<input type="hidden" name="exportmould2" id="exportmould2" />
	
	</form> 
	<form action="<%=request.getContextPath() %>/PDA/exportByDeliverid" method="post" id="searchForm3">
		<input  type="hidden"  name="deliverid" value="<%=request.getParameter("deliverid")==null?"0":request.getParameter("deliverid") %>" id="deliverid" />
		<input type="hidden" name="type" value="" id="type"/>
	</form>
</div>
</body>
</html>

