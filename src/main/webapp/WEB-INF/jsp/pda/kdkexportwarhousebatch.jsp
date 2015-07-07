<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Truck,cn.explink.domain.Bale,cn.explink.domain.Switch"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<JSONObject> objList = request.getAttribute("objList")==null?null:(List<JSONObject>)request.getAttribute("objList");
List<CwbDetailView> weichukuList = (List<CwbDetailView>)request.getAttribute("weichukulist");
List<CwbDetailView> yichukulist = (List<CwbDetailView>)request.getAttribute("yichukulist");
List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");

List<Branch> bList = request.getAttribute("branchlist")==null?new ArrayList<Branch>():(List<Branch>)request.getAttribute("branchlist");
List<User> uList = request.getAttribute("userList")==null?new ArrayList<User>():(List<User>)request.getAttribute("userList");
List<Truck> tList = request.getAttribute("truckList")==null?new ArrayList<Truck>():(List<Truck>)request.getAttribute("truckList");
Map usermap = (Map) session.getAttribute("usermap");
boolean showCustomerSign= request.getAttribute("showCustomerSign")==null?false:(Boolean)request.getAttribute("showCustomerSign");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>库对库出库扫描（批量）</title>
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
	getOutSum($("#branchid").val());
	$("#scancwbs").focus();
});
	
function tabView(tab){
	$("#"+tab).click();
}
//得到当前出库的站点的库存量
function getOutSum(branchid){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/PDA/getkdkOutSum?nextbranchid="+branchid,
		dataType:"json",
		success : function(data) {
			$("#chukukucundanshu").html(data.weichukucount);
			$("#singleoutnum").html(data.yichukucount);
		}
	});
	
	//未出库明细、已出库明细、异常单明细只显示该下一站明细
	if(branchid>0){
		$("#weichukuTable tr").hide();
		$("#weichukuTable tr[nextbranchid='"+branchid+"']").show();
		
		$("#successTable tr").hide();
		$("#successTable tr[nextbranchid='"+branchid+"']").show();
		
		/* $("#errorTable tr").hide();
		$("#errorTable tr[nextbranchid='"+branchid+"']").show(); */
	}else{
		$("#weichukuTable tr").show();
		$("#successTable tr").show();
		//$("#errorTable tr").show();
	}
}
/**
 * 出库扫描
 */
function kdkexportWarehousepl(branchid,driverid,truckid,confirmflag){
	if(<%=bList.isEmpty()%>){
		alert("抱歉，系统未分配库房机构，请联络您公司管理员！");
		return;
	}
	
	if($.trim($("#scancwbs").val()).length==0){
		$("#msg").html("确定批量处理前请输入订单号，多个订单用回车分割");
		return;
	}
	if($.trim($("#scancwbs").val()).length>0){
		$("#confirmflag").val(confirmflag);
		$("#subButton").val("正在处理！请稍候...");
		$("#subButton").attr('disabled','disabled');
		$("#subForm").submit();
		$("#subButton").val("确定批量处理");
		$("#subButton").removeAttr("disabled");
	}
	
}


function exportField(flag,branchid){
	var cwbs = "";
	if(flag==1){
		if(branchid>0){
			$("#weichukuTable tr[nextbranchid='"+branchid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}else{
			$("#weichukuTable tr").each(function(){
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
	}else if(flag==3){
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
		//}
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
var weipage=1;
var yipage=1;
function weiruku(){
	 weipage+=1;
	  $.ajax({
			type:"post",
			url:"<%=request.getContextPath()%>/PDA/getexportweichukulist",
			data:{"page":weipage,"branchid":$("#branchid").val()},
			success:function(data){
				if(data.length>0){
					var optionstring = "";
					for ( var i = 0; i < data.length; i++) {
						<%if(showCustomerSign){ %>
							optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' nextbranchid='"+data[i].nextbranchid+"' >"
							+"<td width='120' align='center'>"+data[i].cwb+"</td>"
							+"<td width='100' align='center'> "+data[i].customername+"</td>"
							+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
							+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
							+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
							+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
							+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
							+ "</tr>";
						<%}else{ %>
							optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' nextbranchid='"+data[i].nextbranchid+"' >"
							+"<td width='120' align='center'>"+data[i].cwb+"</td>"
							+"<td width='100' align='center'> "+data[i].customername+"</td>"
							+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
							+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
							+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
							+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
							+ "</tr>";
						<%} %>
					}
					$("#weiruku").remove();
					$("#weichukuTable").append(optionstring);
					if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
					var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="weiruku();" id="weiruku">查看更多</td></tr>';
					$("#weichukuTable").append(more);
					}
				}
			}
		});
};

function yiruku(){
	yipage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getexportyichukulist",
		data:{"page":yipage,"branchid":$("#branchid").val()},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					<%if(showCustomerSign){ %>
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' nextbranchid='"+data[i].nextbranchid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
					+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
					+ "</tr>";
				<%}else{ %>
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' nextbranchid='"+data[i].nextbranchid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
					+ "</tr>";
				<%} %>
				}
				$("#yiruku").remove();
				$("#successTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
				var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="yiruku();" id="yiruku">查看更多</td></tr>';
				$("#successTable").append(more);
				}
			}
		}
	});
};
function tohome(){
	window.location.href="<%=request.getContextPath() %>/PDA/cwbkdkexportwarhouseBatch?branchid="+$("#branchid").val();
}
</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="saomiao_box2">
  <!-- 新添加 --><div class="saomiao_tab2">
		<ul>
			<li><a href="<%=request.getContextPath()%>/PDA/kdkexportwarhouse">逐单操作</a></li>		
			<li><a href="#"  class="light">批量操作</a></li>
		</ul>
	</div><!-- 新添加 -->
	<div class="saomiao_topnum2">
		<dl class="blue">
			<dt>待出库</dt>
			<dd style="cursor:pointer" onclick="tabView('table_weichuku')"  id="chukukucundanshu">${weichukucount }</dd>
		</dl>
		
		<dl class="green">
			<dt>本次已出库</dt>
			<dd style="cursor:pointer" onclick="tabView('table_yichuku')" id="singleoutnum">${yichukucount}</dd>
		</dl>
		
		<br clear="all"/>
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="saomiao_tab">
				<ul id="bigTag">
					<li><a href="#" onclick="clearMsg();$(function(){$('#baleno').parent().hide();$('#finish').parent().hide();$('#baleno').val('');$('#scancwb').val('');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();})" class="light">扫描订单</a></li>
				</ul>
			</div>
			<div class="saomiao_righttitle2" id="pagemsg"></div>
			<form id="subForm" action="cwbkdkexportwarhouseBatch" method="post">
			<div class="saomiao_selet2">
				下一站：
				<select id="branchid" name="branchid" onchange="tohome();">
					<%if(bList.size()!=0)for(Branch b : bList){ %>
						<option value="<%=b.getBranchid() %>" <%if((request.getParameter("branchid")==null?bList.get(0).getBranchid():Long.parseLong(request.getParameter("branchid")))==b.getBranchid()){ %>selected<%} %>><%=b.getBranchname() %></option>
					<%} %>
				</select>
				驾驶员：
				<select id="driverid" name="driverid">
					<option value="-1" selected>请选择</option>
					<%if(uList.size()!=0)for(User u : uList){ %>
						<option value="<%=u.getUserid() %>" ><%=u.getRealname() %></option>
					<%} %>
		        </select>
				车辆：
				<select id="truckid" name="truckid">
					<option value="-1" selected>请选择</option>
					<%if(tList.size()!=0)for(Truck t : tList){ %>
						<option value="<%=t.getTruckid() %>" ><%=t.getTruckno() %></option>
					<%} %>
		        </select>
			</div>
			<div class="saomiao_inwrith2">
				<div class="saomiao_left2">
					<%if(Long.parseLong(usermap.get("isImposedOutWarehouse").toString())==1){ %>
					<p>
						强制出库:</span><input type="checkbox" id="confirmflag" name="confirmflag" value="0"/>
					</p>
					<%} %>
					 订单号：
					<label for="textfield"></label>
					<textarea name="scancwbs" cols="45" rows="3" id="scancwbs"></textarea>
					<input type="button" id="subButton" value="确定批量处理" onclick='kdkexportWarehousepl($("#branchid").val(),$("#driverid").val(),$("#truckid").val(),$("#confirmflag").attr("checked")=="checked"?1:0);' class="input_button1" />
				</div>
				<div class="saomiao_right2">
					<p id="msg" name="msg" >${msg }</p>
				</div>
			</div></form>
		</div>
	</div>
	
		<div>
			<div class="saomiao_tab2">
				<span style="float: right; padding: 10px"></span>
				<ul id="smallTag">
					<li><a id="table_weichuku" href="#" class="light">待出库明细</a></li>
					<li><a id="table_yichuku" href="#">已出库明细</a></li>
					<li><a href="#">异常单明细</a></li>
				</ul>
			</div>
			<div id="ViewList" class="tabbox">
				<li>
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(1,$("#branchid").val());' />
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
										<table id="weichukuTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%if(weichukuList!=null&&weichukuList.size()!=0)for(CwbDetailView co : weichukuList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>"  nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%if(customerList!=null)for(Customer c1:customerList){if(co.getCustomerid()==c1.getCustomerid()){%><%=c1.getCustomername() %><%}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView() %></td>
												<%} %>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
											<%if(weichukuList!=null&&weichukuList.size()==Page.DETAIL_PAGE_NUMBER){ %>
												<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="weiruku();" id="weiruku">查看更多</td></tr>
											<%} %>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
				<li style="display: none">
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(2,$("#branchid").val());' />
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
											<%if(yichukulist!=null&&yichukulist.size()!=0)for(CwbDetailView co : yichukulist){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%if(customerList!=null)for(Customer c1:customerList){if(co.getCustomerid()==c1.getCustomerid()){%><%=c1.getCustomername() %><%}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView() %></td>
												<%} %>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
											<%if(yichukulist!=null&&yichukulist.size()==Page.DETAIL_PAGE_NUMBER){ %>
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
											<td width="240" align="center" bgcolor="#f1f1f1">地址</td>
											<td align="center" bgcolor="#f1f1f1">异常原因</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="errorTable" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
										<%if(objList!=null&&objList.size()!=0)for(JSONObject obj:objList){
											if("000000".equals(obj.getString("errorcode"))){
												continue;
											}if(obj.getJSONObject("cwbOrder")!=null&&!obj.getJSONObject("cwbOrder").isNullObject()){
										%>
										<tr id="TR<%=obj.getString("cwb") %>error" cwb="<%=obj.getString("cwb") %>" customerid="<%=obj.getJSONObject("cwbOrder").getLong("customerid") %>"  >
											<td width="120" align="center" valign="middle"><%=obj.getString("cwb") %></td>
											<td width="100" align="center" valign="middle"><%=obj.getString("customername")%></td>
											<td width="140" align="center" valign="middle"><%=obj.getJSONObject("cwbOrder").getString("emaildate") %></td>
											<td width="100" align="center" valign="middle"><%=obj.getJSONObject("cwbOrder").getString("consigneename") %></td>
											<td width="100" align="center" valign="middle"><%=obj.getJSONObject("cwbOrder").getString("receivablefee") %></td>
											<td width="240" align="center" valign="middle"><%=obj.getJSONObject("cwbOrder").getString("consigneeaddress") %></td>
											<td align="center" valign="middle"><%=obj.getString("errorinfo") %></td>
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
			<input type="hidden" name="cwbs" id="cwbs" value=""/>
			<input type="hidden" name="exportmould2" id="exportmould2" />
		</form>
 
</div>
</body>
</html>
