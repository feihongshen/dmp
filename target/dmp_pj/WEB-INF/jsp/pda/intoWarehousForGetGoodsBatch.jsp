<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbOrder> weiTiHuolist = (List<CwbOrder>)request.getAttribute("weiTiHuolist");
List<Customer> cList = (List<Customer>)request.getAttribute("customerlist");
List<User> uList = (List<User>)request.getAttribute("userList");
List<JSONObject> objList = request.getAttribute("objList")==null?null:(List<JSONObject>)request.getAttribute("objList");
long SuccessCount = request.getAttribute("SuccessCount")==null?0:Long.parseLong(request.getAttribute("SuccessCount").toString());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>提货扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
	$(function(){
		getcwbsdataForCustomer('-1');
		$("#scancwb").focus();
	});
	$(function(){
		var $menuli = $(".saomiao_tab ul li");
		$menuli.click(function(){
			$(this).children().addClass("light");
			$(this).siblings().children().removeClass("light");
			var index = $menuli.index(this);
			$(".tabbox li").eq(index).show().siblings().hide();
		});
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
	
	function addAndRemoval(cwb,tab,isRemoval,customerid){
		var trObj = $("#ViewList tr[id='TR"+cwb+"']");
		if(isRemoval){
			$("#"+tab).append(trObj);
		}else{
			$("#ViewList #errorTable tr[id='TR"+cwb+"error']").remove();
			trObj.clone(true).appendTo("#"+tab);
			$("#ViewList #errorTable tr[id='TR"+cwb+"']").attr("id",trObj.attr("id")+"error");
		}
		//已入库明细只显示该供货商明细、异常单明细只显示该供货商明细
		if(customerid>0){
			$("#successTable tr").hide();
			$("#successTable tr[customerid='"+customerid+"']").show();
			
			$("#errorTable tr").hide();
			$("#errorTable tr[customerid='"+customerid+"']").show();
		}else{
			$("#successTable tr").show();
			$("#errorTable tr").show();
		}
	}

	//得到当前入库的供应商的库存量
	function getcwbsdataForCustomer(customerid) {
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/PDA/getDaoRuSum",
			data : {
				"customerid" : customerid
			},
			dataType : "json",
			success : function(data) {
				if ($("#tihuokucundanshu", parent.document).length > 0) {
					$("#tihuokucundanshu", parent.document).html(data.count);
				} else {
					$("#tihuokucundanshu").html(data.count);
				}
			}
		});
		
		//未入库明细、已入库明细、异常单明细只显示该供货商明细
		if(customerid>0){
			$("#weitihuoTable tr").hide();
			$("#weitihuoTable tr[customerid='"+customerid+"']").show();
			
			$("#successTable tr").hide();
			$("#successTable tr[customerid='"+customerid+"']").show();
			
			$("#errorTable tr").hide();
			$("#errorTable tr[customerid='"+customerid+"']").show();
		}else{
			$("#weitihuoTable tr").show();
			$("#successTable tr").show();
			$("#errorTable tr").show();
		}
	}

function exportField(flag,customerid){
	var cwbs = "";
	if(flag==1){
		if(customerid>0){
			$("#weitihuoTable tr[customerid='"+customerid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}else{
			$("#weitihuoTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}
	}else if(flag==2){
		if(customerid>0){
			$("#successTable tr[customerid='"+customerid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}else{
			$("#successTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}
	}else if(flag==3){ //修改导出问题
		/* if(customerid>0){
			$("#errorTable tr[customerid='"+customerid+"']").each(function(){
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

function sub(){
	if($.trim($("#cwbs").val()).length==0){
		$("#pagemsg").html("确定批量处理前请输入订单号，多个订单用回车分割");
		return false;
	}
	$("#subButton").val("正在处理！请稍候...");
	$("#subButton").attr('disabled','disabled');
	$("#subForm").submit();
	
}

</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="saomiao_box2">

	<div class="saomiao_topnum2">
		<dl class="blue">
			<dt>待提货</dt>
			<dd style="cursor:pointer" onclick="tabView('table_weitihuo')" id="tihuokucundanshu">${yitihuo }</dd>
		</dl>
		
		<dl class="green">
			<dt>已提货</dt>
			<dd style="cursor:pointer" onclick="tabView('table_yitihuo')" id="successcwbnum" name="successcwbnum"><%=SuccessCount %></dd>
		</dl>
		
		<br clear="all">
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="saomiao_righttitle2" id="pagemsg"></div>
			<form id="subForm" action="cwbintoWarehousForGetGoodsBatch" method="post">
			<input name="succesCount" value="<%=SuccessCount%>" type="hidden"/>
			<div class="saomiao_selet2">
				供应商：
				<select id="customerid" name="customerid" onchange="getcwbsdataForCustomer($('#customerid').val())">
					<option value="-1" selected>全部供应商</option>
					<%for(Customer c : cList){ %>
						<option value="<%=c.getCustomerid() %>" ><%=c.getCustomername() %></option>
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
					<p><span>订单号：</span>
						<textarea name="cwbs" cols="45" rows="3" id="cwbs"></textarea>
					</p>
				</div>
					<div class="saomiao_right2">
					<p>
						<input name="button" type="button" class="input_button1" id="button" value="批量提货" onclick="sub();"/>
					</p>
				</div>
				<div class="saomiao_right2">
					<p id="msg" name="msg" ><%if(SuccessCount>0){ %>成功扫描<%=SuccessCount %>单<%} %></p>
				</div>
			</div></form>
		</div>
	</div>



		<div>
			<div class="saomiao_tab2">
				<span style="float: right; padding: 10px"></span>
				<ul>
					<li><a id="table_weitihuo" href="#" class="light">未提货明细</a></li>
					<li><a id="table_yitihuo" href="#">已提货明细</a></li>
					<li><a href="#">异常单明细</a></li>
				</ul>
			</div>
			<div id="ViewList" class="tabbox">
				<li>
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(1,$("#customerid").val());'/>
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
										<table id="weitihuoTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%for(CwbOrder co : weiTiHuolist){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>">
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
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(2,$("#customerid").val());'/>
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
										<%if(objList!=null)for(JSONObject obj:objList){
											if("000000".equals(obj.getString("errorcode"))){
											JSONObject co = JSONObject.fromObject(obj.getString("cwbOrder"));
										%>
											<tr id="TR<%=obj.getString("cwb") %>" cwb="<%=obj.getString("cwb") %>" customerid="<%=co.getLong("customerid") %>">
												<td width="120" align="center" bgcolor="#f1f1f1"><%=obj.getString("cwb") %></td>
												<td width="100" align="center" bgcolor="#f1f1f1"><%for(Customer c:cList){if(c.getCustomerid()==co.getLong("customerid")){out.print(c.getCustomername());break;}} %></td>
												<td width="140" align="center" bgcolor="#f1f1f1"><%=co.getString("emaildate") %></td>
												<td width="100" align="center" bgcolor="#f1f1f1"><%=co.getString("consigneename") %></td>
												<td width="100" align="center" bgcolor="#f1f1f1"><%=co.getDouble("receivablefee") %></td>
												<td align="center" bgcolor="#f1f1f1"><%=co.getString("consigneeaddress") %></td>
											</tr>
										<%}} %>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
				
				<li style="display: none">
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(3,$("#customerid").val());'/>
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
											<td width="300" align="center" bgcolor="#f1f1f1">地址</td>
											<td align="center" bgcolor="#FFE6E6">异常原因</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="errorTable" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
											<%if(objList!=null)for(JSONObject obj:objList){
												if("000000".equals(obj.getString("errorcode"))){
													continue;
												}
												JSONObject co =  obj.get("cwbOrder")==null?null:JSONObject.fromObject(obj.get("cwbOrder"));
											%>
											<tr id="TR<%=obj.getString("cwb") %>" cwb="<%=obj.getString("cwb") %>" customerid="<%if(co!=null)%><%=co.getLong("customerid") %>">
												<td width="120" align="center" bgcolor="#f1f1f1"><%=obj.getString("cwb") %></td>
												<td width="100" align="center" bgcolor="#f1f1f1"><%if(co!=null)%><%for(Customer c:cList){if(c.getCustomerid()==co.getLong("customerid")){out.print(c.getCustomername());break;}} %></td>
												<td width="140" align="center" bgcolor="#f1f1f1"><%=co==null?"":co.getString("emaildate") %></td>
												<td width="100" align="center" bgcolor="#f1f1f1"><%=co==null?"":co.getString("consigneename") %></td>
												<td width="100" align="center" bgcolor="#f1f1f1"><%=co==null?"0.00":co.get("receivablefee") %></td>
												<td width="300" align="left" bgcolor="#f1f1f1"><%=co==null?"":co.getString("consigneeaddress") %></td>
												<td align="center"><%=obj.getString("errorinfo") %></td>
											</tr>
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

	</div>
</body>
</html>
