<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer,cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Truck,cn.explink.domain.Bale,cn.explink.domain.Switch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<CwbOrder> weichuzhanlist = (List<CwbOrder>)request.getAttribute("weichuzhanlist");
	List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
	List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
	List<CwbOrder> yichuzhanlist = (List<CwbOrder>)request.getAttribute("yichuzhanlist");
	String yichuzhannums=request.getAttribute("yichuzhannums").toString();
	String weifandannums=request.getAttribute("size").toString();
	String customerandid=request.getAttribute("customerid")==null?"0":request.getAttribute("customerid").toString();
	List<Customer> customerList = request.getAttribute("customerList")==null?null:(List<Customer>)request.getAttribute("customerList");
	long customerid=Long.parseLong(request.getParameter("customerid")==null?"0":request.getParameter("customerid"));
	long timetype=Long.parseLong(request.getParameter("timetype")==null?"3":request.getParameter("timetype"));
	String starttime=request.getParameter("starttime")==null?new SimpleDateFormat("yyyy-MM-dd").format(new Date()):request.getParameter("starttime");
	String endtime=request.getParameter("endtime")==null?new SimpleDateFormat("yyyy-MM-dd").format(new Date()):request.getParameter("endtime");
	long flag=Long.parseLong(request.getParameter("flag")==null?"1":request.getParameter("flag"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>返单出站扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript">
var alloutnum = "<%=yichuzhannums%>";
var flag="<%=flag%>";
$(function(){
	var $menuli = $(".saomiao_tab2 ul li");
	$menuli.click(function() {
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
	
	$("#starttimewei").datepicker();
	$("#endtimewei").datepicker();
	$("#starttimeyi").datepicker();
	$("#endtimeyi").datepicker();
	$("#timetypewei").val('<%=timetype%>');
	
	if(flag==1){
		$("#table_weituihuochuku").addClass("light");
		$(".tabbox li").eq(0).show().siblings().hide();
	}else{
		$("#table_yituihuochuku").addClass("light");
		$(".tabbox li").eq(1).show().siblings().hide();
	}
})

function tabView(tab){
	$("#"+tab).click();
}

function addAndRemoval(cwb,tab,isRemoval){
	var trObj = $("#ViewList tr[id='TR"+cwb+"']");
	var trObj1 = $("#ViewList tr[id='TR"+cwb+"']");
	if(isRemoval){
		trObj.remove();
		console.info(trObj1);
		trObj1.children('td').eq(2).remove();
		trObj1.children('td').eq(3).remove();
		var d = new Date();
		console.info(d);
		var vYear = d.getFullYear();
		var vMon = d.getMonth() + 1;
		var vDay = d.getDate();
		var h = d.getHours(); 
		var m = d.getMinutes(); 
		var se = d.getSeconds();
		if(se<10)se+="0";
		if(vMon<10)vMon+="0";
		if(vDay<10)vDay+="0";
		if(h<10)h+="0";
		if(m<10)m+="0";
		trObj1.children('td').eq(2).html(vYear+"-"+vMon+"-"+vDay+"  "+h+":"+m+":"+se);
		console.info(trObj1);
		trObj1.clone(true).appendTo("#"+tab);
		
	}else{
		
		$("#ViewList #errorTable tr[id='TR"+cwb+"error']").remove();
		trObj.clone(true).appendTo("#"+tab);
		$("#ViewList #errorTable tr[id='TR"+cwb+"']").attr("id",trObj.attr("id")+"error");
	}
	$("#successTable tr").show();
	$("#errorTable tr").show();
}

/**
 * 返单出站扫描
 */

function cwbreturnCwbsbackexport(scancwb,branchid){
	if(<%=branchlist.isEmpty()%>){
		alert("抱歉，系统未分配退货站点，请联络您公司管理员！");
		return;
	}
	if(scancwb.length>0){
		var successnum = 0,errorcwbnum = 0;
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/returnCwbs/cwbreturnCwbsbackexport/"+scancwb+"?branchid="+branchid,
			dataType:"json",
			success : function(data) {
				$("#scancwb").val("");
				$("#msg").html(data.errorinfo);
				getReturnCwbsbackexportSum($("#customerandid").val(),$("#timetypewei").val(),$("#starttimewei").val(),$("#endtimewei").val());
				if(data.statuscode=="000000"){
					//将成功扫描的订单放到已入库明细中
					addAndRemoval(scancwb,"successTable",true);
					alloutnum=parseInt(alloutnum)+1;
					$("#alloutnum").html(alloutnum);
				}else{
					addAndRemoval(scancwb,"errorTable",false);
				}
			}
		});
	}
}

$(function(){
	//getReturnCwbsbackexportSum($("#customerandid").val(),$("#timetypewei").val(),$("#starttimewei").val(),$("#endtimewei").val());
	
	 $("#scancwb").focus();
});
//得到当前待返单出站的库存量
function getReturnCwbsbackexportSum(customerandid,timetypewei,starttimewei,endtimewei){
	var canshu=customerandid+"/"+timetypewei+"/"+starttimewei+"/"+endtimewei;
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/returnCwbs/getReturnCwbsbackexportSum/"+canshu,
		dataType:"json",
		success : function(data) {
			$("#chukukucundanshu").html(data.size);
		}
	});
	$("#weichukuTable tr").show();
	$("#successTable tr").show();
	$("#errorTable tr").show();
}


function exportField(flag){
	var cwbs = "";
	if(flag==1){
		$("#weituihuochukuTable tr").each(function(){
			var cwb = $(this).attr("cwb");
			cwbs += "'" + cwb + "',";
		});
	}else if(flag==2){
		$("#successTable tr").each(function(){
			var cwb = $(this).attr("cwb");
			cwbs += "'" + cwb + "',";
		});
	}else if(flag==3){
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

function search(flag){
	if(flag==1){//待返单
		if($("#starttimewei").val()==""){
			alert("请选择开始时间");
			return false;
		}
		if($("#endtimewei").val()==""){
			alert("请选择结束时间");
			return false;
		}
		if($("#starttimewei").val()>$("#endtimewei").val() && $("#endtimewei").val() !=''){
			alert("开始时间不能大于结束时间");
			return false;
		}
		
		$("#customerid").val($("#customeridwei").val());	
		$("#starttime").val($("#starttimewei").val());	
		$("#endtime").val($("#endtimewei").val());	
		$("#timetype").val($("#timetypewei").val());	
		$("#flag").val("1");
		$("#searchForm").submit();
	}
	if(flag==2){//已出站
		if($("#starttimeyi").val()==""){
			alert("请选择开始时间");
			return false;
		}
		if($("#endtimeyi").val()==""){
			alert("请选择结束时间");
			return false;
		}
		if($("#starttimeyi").val()>$("#endtimeyi").val() && $("#endtimeyi").val() !=''){
			alert("开始时间不能大于结束时间");
			return false;
		}
		$("#customerid").val($("#customeridyi").val());	
		$("#starttime").val($("#starttimeyi").val());	
		$("#endtime").val($("#endtimeyi").val());	
		$("#timetype").val(3);	
		$("#flag").val("2");
		$("#searchForm").submit();
	}
}
</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="saomiao_box">
 
	<div class="saomiao_topnum">
		<dl class="blue">
			<dt>今日返单待出站</dt>
			<dd  style="cursor:pointer" onclick="tabView('table_weituihuochuku')" id="chukukucundanshu"><%=weifandannums %></dd>
		</dl>
		
		<dl class="green">
			<dt>今日返单已出站</dt>
			<dd style="cursor:pointer" onclick="tabView('table_yituihuochuku')" id="alloutnum"><%=yichuzhannums%></dd>
		</dl>
			<input type="button"  id="refresh" value="刷新" onclick="location.href='<%=request.getContextPath() %>/returnCwbs/returnCwbsbackexport'" style="float:left; width:100px; height:65px; cursor:pointer; border:none; background:url(../images/buttonbgimg1.gif) no-repeat; font-size:18px; font-family:'微软雅黑', '黑体'"/>
		<br clear="all">
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="saomiao_righttitle2" id="pagemsg"></div>
			<div class="saomiao_selet2">
				下一站：
				<select id="branchid" name="branchid" class="select1">
					<%for(Branch b : branchlist){ %>
						<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
					<%} %>
				</select>
			</div>
			<div class="saomiao_inwrith2">
				<div class="saomiao_left2">
					<p><span>订单号：</span>
						<input type="text" class="saomiao_inputtxt" value="" id="scancwb" name="scancwb"  onKeyDown='if(event.keyCode==13&&$(this).val().length>0){cwbreturnCwbsbackexport($(this).val(),$("#branchid").val());}'/>
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
					<li><a id="table_weituihuochuku" href="#">返单待出站</a></li>
					<li><a id="table_yituihuochuku" href="#">已出站</a></li>
					<li><a href="#">异常单明细</a></li>
				</ul>
			</div>
			<div id="ViewList" class="tabbox">
				<li>
				 &nbsp;&nbsp;客户：
				 <select name="customeridwei" id="customeridwei" style="height: 20px;width:200px" class="select1">
					<option value="0">请选择</option>
					<%if(customerList!=null&&!customerList.isEmpty()){
						for(Customer c:customerList){ %>
					<option value="<%=c.getCustomerid() %>" <% if(c.getCustomerid()==customerid){out.print("selected");} %>><%=c.getCustomername() %></option>
					<%}}%>
				 </select>
				 &nbsp;&nbsp;操作时间：
				 <select name="timetypewei" id="timetypewei" style="height: 20px;width:100px" class="select1">
					<option value="1">发货时间</option>
					<option value="2">反馈时间</option>
					<option value="3">审核时间</option>
				 </select>
				 <input type="text" name="starttimewei" id="starttimewei" value ="<%=starttime%>" class="input_text1"/>
				 到<input type="text" name="endtimewei" id="endtimewei" value ="<%=endtime%>" class="input_text1"/>
					<input type ="button" id="btnval0" value="查询" class="input_button1" onclick='search(1);'/>
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(1);'/>
					<input type="hidden" id="customerandid" name="customerandid" value="<%=customerandid %>"/>
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
											 <td width="140" align="center" bgcolor="#f1f1f1">反馈时间</td>
											<td width="140" align="center" bgcolor="#f1f1f1">审核时间</td> 
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="weituihuochukuTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%for(CwbOrder co : weichuzhanlist){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%for(Customer c:customerlist){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate()%></td>
												<td width="140"><%=co.getFankuitime()%></td>
												<td width="140"><%=co.getShenhetime()%></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
												<%-- <tr id="TRL<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" style="display:none;">
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%for(Customer c:customerlist){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140" id="<%=co.getCwb() %>shenhetime"></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr> --%>
											
											
											<%} %>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
				
				<li style="display: none">
					&nbsp;&nbsp;客户：
				 <select name="customeridyi" id="customeridyi" style="height: 20px;width:200px" class="select1">
					<option value="0">请选择</option>
					<%if(customerList!=null&&!customerList.isEmpty()){
						for(Customer c:customerList){ %>
					<option value="<%=c.getCustomerid() %>" <% if(c.getCustomerid()==customerid){out.print("selected");} %>><%=c.getCustomername() %></option>
					<%}}%>
				 </select>
				 &nbsp;&nbsp;返单出站时间：
				 <input type="text" name="starttimeyi" id="starttimeyi" value ="<%=starttime%>" class="input_text1"/>
				 到<input type="text" name="endtimeyi" id="endtimeyi" value ="<%=endtime%>" class="input_text1"/>
					<input type ="button" id="btnval0" value="查询" class="input_button1" onclick='search(2);'/>
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
											<td width="140" align="center" bgcolor="#f1f1f1">出站时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<!-- <div style="height: 160px; overflow-y: scroll">
										<table id="successTable" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
										</table>
									</div> -->
									<div style="height: 160px; overflow-y: scroll">
										<table id="successTable" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
											<%for(CwbOrder co : yichuzhanlist){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%for(Customer c:customerlist){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getShenhetime()%></td>
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
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(3);'/>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">出站时间</td>
											<!-- <td width="140" align="center" bgcolor="#f1f1f1">反馈时间</td>
											<td width="140" align="center" bgcolor="#f1f1f1">审核时间</td> -->
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
 
 		<form action="<%=request.getContextPath()%>/returnCwbs/returnCwbsbackexport" method="post" id="searchForm">
			<input type="hidden" name="customerid" id="customerid" value=""/>
			<input type="hidden" name="starttime" id="starttime" />
			<input type="hidden" name="endtime" id="endtime" />
			<input type="hidden" name="timetype" id="timetype" />
			<input type="hidden" name="flag" id="flag" />
		</form>
</div>
</body>
</html>