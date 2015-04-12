<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer,cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Truck,cn.explink.domain.Bale,cn.explink.domain.Switch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbDetailView> weichuzhanlist = (List<CwbDetailView>)request.getAttribute("weichuzhanlist");
List<CwbDetailView> yichuzhanlist = (List<CwbDetailView>)request.getAttribute("yichuzhanlist");
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
List<JSONObject> objList = request.getAttribute("objList")==null?null:(List<JSONObject>)request.getAttribute("objList");
List<Branch> bList = (List<Branch>)request.getAttribute("branchlist");
List<User> uList = (List<User>)request.getAttribute("userList");
List<Bale> balelist = (List<Bale>)request.getAttribute("balelist");
Map usermap = (Map) session.getAttribute("usermap");
String did=request.getParameter("branchid")==null?"0":request.getParameter("branchid");
long branchid=Long.parseLong(did);
boolean showCustomerSign= request.getAttribute("showCustomerSign")==null?false:(Boolean)request.getAttribute("showCustomerSign");
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

function tabView(tab){
	$("#"+tab).click();
}

$(function(){
	getTuiHuoOutSum('<%=request.getContextPath()%>',$("#branchid").val());
	getTuiHuoYiOutSum($("#branchid").val());
	showbybranchid($("#branchid").val());
	 $("#cwbs").focus();
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
	//未出库明细、已出库明细、异常单明细只显示该下一站明细
	if(branchid>0){
		$("#weituihuochukuTable tr").hide();
		$("#weituihuochukuTable tr[nextbranchid='"+branchid+"']").show();
		
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
	if(branchid>0){
		$("#weituihuochukuTable tr").hide();
		$("#weituihuochukuTable tr[nextbranchid='"+branchid+"']").show();
		
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
			$("#successTable tr[nextbranchid='"+branchid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}else{
			$("#successTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}
	}else if(flag==3){//修改导出问题
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
		$("#exportcwbs").val(cwbs);
		$("#btnval").attr("disabled","disabled");
	 	$("#btnval").val("请稍后……");
	 	$("#searchForm2").submit();
	}else{
		alert("没有订单信息，不能导出！");
	};
}

function changevalue(){
	var value = $("#confirmflag").attr("checked")=="checked"?1:0;
	$("#confirmflag").val(value);
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
function tohome(){
	window.location.href="<%=request.getContextPath() %>/PDA/cwbbranchbackexportBatch?branchid="+$("#branchid").val();
}


</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="saomiao_box">

<div class="saomiao_tab2">
		<ul>
			<li><a href="<%=request.getContextPath()%>/PDA/branchbackexport" >逐单操作</a></li>		
			<li><a href="#" class="light" >批量操作</a></li>
		</ul>
	</div>
 
	<div class="saomiao_topnum">
		<dl class="blue">
			<dt>待退货出站</dt>
			<dd  style="cursor:pointer" onclick="tabView('table_weituihuochuku')" id="chukukucundanshu">${weiCount }</dd>
		</dl>
		
		<dl class="green">
			<dt>已扫描</dt>
			<dd style="cursor:pointer" onclick="tabView('table_yituihuochuku')" id="alloutnum">${yiCount }</dd>
		</dl>
		<br clear="all">
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="saomiao_righttitle2" id="pagemsg"></div>
			<form action="<%=request.getContextPath() %>/PDA/cwbbranchbackexportBatch" method="post">
			<div class="saomiao_selet2">
			
				下一站：
				<select id="branchid" name="branchid" onchange="tohome();" class="select1">
					<%for(Branch b : bList){ %>
					<%if(b.getBranchid()!=Long.parseLong(usermap.get("branchid").toString())){ %>
						<option value="<%=b.getBranchid() %>"  <%if(branchid==b.getBranchid()) {%>selected <%} %> ><%=b.getBranchname() %></option>
					<%}} %>
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
						<span>强制出库:</span><input type="checkbox" id="confirmflag" name="confirmflag" value="" onclick="changevalue();"/>
					</p>
					<%} %>
					<p><span>订单号：</span>
						<textarea cols="24" rows="4"  name ="cwbs" id="cwbs" ></textarea>
					</p>
					<span>&nbsp;</span><input type="submit" name="finish" id="finish" value="确认批量" class="button" />
				</div>
				<div class="saomiao_right2">
					<p id="msg" name="msg" >${msg }</p>
				</div>
			</div></form>
		</div>
	</div>
 
 
 <div>
			<div class="saomiao_tab2">
				<span style="float: right; padding: 10px"><!-- <input
					type="button" class="input_button2" value="导出" /> --></span>
				<ul id="smallTag">
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
											<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="weituihuochukuTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%for(CwbDetailView co : weichuzhanlist){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
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
									<div style="height: 160px; overflow-y: scroll">
										<table id="successTable" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
											<%for(CwbDetailView co : yichuzhanlist){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
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
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
											<td width="380" align="center" bgcolor="#f1f1f1">地址</td>
											<td align="center" bgcolor="#f1f1f1">异常原因</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="errorTable" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
											<%if(objList!=null)for(JSONObject obj : objList){if(!obj.get("errorcode").equals("000000")){ %>
											<%JSONObject cwbOrder =  obj.get("cwbOrder")==null?null:JSONObject.fromObject(obj.get("cwbOrder"));%>
											<tr id="TR<%=obj.get("cwb") %>error" cwb="<%=obj.get("cwb") %>" nextbranchid="<%=cwbOrder==null?"":cwbOrder.getLong("nextbranchid") %>">
												<td width="120" align="center"><%=obj.get("cwb") %></td>
												<td width="100" align="center"><%=obj.get("customername") %></td>
												<td width="140" align="center"><%=cwbOrder==null?"":cwbOrder.getString("emaildate") %></td>
												<td width="100" align="center"><%=cwbOrder==null?"":cwbOrder.getString("consigneename") %></td>
												<td width="100" align="center"><%=cwbOrder==null?"":cwbOrder.getDouble("receivablefee") %></td>
												<%if(showCustomerSign){ %>
												<td width="100" align="center"><%=obj.get("showRemark") %></td>
											<%} %>
												<td width="380" align="left"><%=cwbOrder==null?"":cwbOrder.getString("consigneeaddress") %></td>
												<td align="center"><%=obj.get("errorinfo") %></td>
											</tr>
											<%}} %>
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
 
 
</div>
</body>
</html>