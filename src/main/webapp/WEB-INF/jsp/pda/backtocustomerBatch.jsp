<%@page import="cn.explink.util.Page"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.domain.Customer,cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Customer> cList = (List<Customer>)request.getAttribute("customerlist");
List<CwbOrder> weitghsckList = request.getAttribute("weitghsckList")==null?null:(List<CwbOrder>)request.getAttribute("weitghsckList");
List<CwbOrder> yitghsckList = request.getAttribute("yitghsckList")==null?null:(List<CwbOrder>)request.getAttribute("yitghsckList");
List<JSONObject> objList = request.getAttribute("objList")==null?null:(List<JSONObject>)request.getAttribute("objList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>退供应商出库扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$(function(){
	$("#scancwb").focus();
});
$(function(){
	var $menuli = $(".saomiao_tab2 ul li");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
})

function tabView(tab){
	$("#"+tab).click();
}
function exportField(flag){
	
	if(flag==1){
		$("#type").val("weichuku");
		$("#exportForBack").submit();
	}else if(flag==2){
		$("#type").val("yichuku");
		$("#exportForBack").submit();
	}else if(flag==3){
		var cwbs = "";
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

var yipage=1,weipage=1;

function weichuku(){
	weipage++;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbacktocustomerdaichukulist",
		data:{"page":weipage},
		success:function(data){
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
			$("#addwei").remove();			
			$("#weitghsckTable").append(optionstring);
			if(data.length==<%=Page.DETAIL_PAGE_NUMBER %>){
				var addmore="<tr id='addwei' align='center' ><td colspan='6' style='cursor:pointer' onclick='weichuku()' >查看更多</td></tr>";
				$("#weitghsckTable").append(addmore);
			}
		}
	});
};

function yichuku(){
	yipage++;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbacktocustomeryichukulist",
		data:{"page":yipage},
		success:function(data){
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
			$("#addyi").remove();
			$("#successTable").append(optionstring);
			if(data.length==<%=Page.DETAIL_PAGE_NUMBER %>){
				var addmore="<tr id='addyi' align='center' ><td colspan='6' style='cursor:pointer' onclick='yichuku()'> 查看更多</td> </tr>";
				$("#successTable").append(addmore);
			}
						
		}
	});
	
}








</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="saomiao_box2">
 
	<div class="saomiao_topnum2">
		<dl class="blue">
			<dt style="cursor:pointer" onclick="tabView('table_weichuku')" >待出库</dt>
			<dd id="tuicun">${count }</dd>
		</dl>
		<dl class="green">
			<dt style="cursor:pointer" onclick="tabView('table_yichuku')" >已出库</dt>
			<dd id="allsuccess">${yichukucount }</dd>
		</dl>
		<br clear="all">
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="saomiao_righttitle2" id="pagemsg"></div>
			<div class="saomiao_selet2">
			</div>
			<form action="<%=request.getContextPath()%>/PDA/cwbbacktocustomerBatch" method="post">
			<div class="saomiao_inwrith2">
				<div class="saomiao_left2">
					<p>
						供货商：<select id="customerid" name="customerid">
						<option value="-1">请选择供货商</option>
						<%for(Customer customer:cList){ %>
						<option value="<%=customer.getCustomerid()%>"><%=customer.getCustomername() %></option>
						<%} %>
						</select>
						</p>
					<p><span>订单号：</span>
						<textarea cols="24" rows="4"  name ="cwbs" id="cwbs" ></textarea>
					</p>
					<span>&nbsp;</span><input type="submit" name="finish" id="finish" value="确认批量处理" class="button" >
				</div>
				<div class="saomiao_right2">
					<p id="msg" name="msg" >${msg }</p>
					<p id="tuihuo" style="display:none;"><strong>退货单</strong></p>
					<p id="showcwb" name="showcwb"></p>
					<p id="customername" name="customername"></p>
				</div>
			</div>
			</form>
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
										<table id="weitghsckTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%if(weitghsckList!=null&&weitghsckList.size()>0)for(CwbOrder co : weitghsckList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>">
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%if(cList!=null&&cList.size()>0)for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
											<%if(weitghsckList!=null&&weitghsckList.size()==Page.DETAIL_PAGE_NUMBER){ %>
												<tr  id="addwei"  align="center"> <td style="cursor:pointer"  onclick="weichuku()" colspan="6"  >查看更多</td> </tr>
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
									<div>
										<table id="successTable" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
											<%if(yitghsckList!=null&&yitghsckList.size()>0)for(CwbOrder co : yitghsckList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>">
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%if(cList!=null&&cList.size()>0)for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
											<%if(yitghsckList!=null&&yitghsckList.size()==Page.DETAIL_PAGE_NUMBER){ %>
												<tr id="addyi" align="center"  ><td colspan="6"  style="cursor:pointer"  onclick="yichuku()">查看更多</td></tr>
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
											<td width="100" align="center" bgcolor="#f1f1f1">异常原因</td>
										</tr>
									</table>
									<div>
										<table id="errorTable" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
										<%if(objList!=null)for(JSONObject obj : objList){if(!obj.get("errorcode").equals("000000")){ %>
										<%JSONObject cwbOrder =  obj.get("cwbOrder")==null?null:JSONObject.fromObject(obj.get("cwbOrder"));%>
											<tr id="TR<%=obj.get("cwb") %>" cwb="<%=obj.get("cwb") %>" >
												<td width="120" align="center"><%=obj.get("cwb") %></td>
												<td width="100" align="center"><%=obj.get("customername") %></td>
												<td width="140" align="center"><%=cwbOrder==null?"":cwbOrder.getString("emaildate") %></td>
												<td width="100" align="center"><%=cwbOrder==null?"":cwbOrder.getString("consigneename") %></td>
												<td width="100" align="center"><%=cwbOrder==null?"":cwbOrder.getDouble("receivablefee") %></td>
												<td width="100" align="center"><%=obj.get("errorinfo") %></td>
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
 		<form action="<%=request.getContextPath() %>/PDA/exportExcleForBackToCustomer" id="exportForBack">
	 		<input  type="hidden" name="type" value="" id="type"/>
 		</form>
	
	
</div>
</body>
</html>

