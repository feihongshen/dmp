<%@page import="cn.explink.util.Page"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch,cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<User> uList = (List<User>)request.getAttribute("userList");
List<CwbOrder> weituihuorukuList = (List<CwbOrder>)request.getAttribute("weituihuorukuList");
List<CwbOrder> yituihuorukuList = (List<CwbOrder>)request.getAttribute("yituihuorukuList");
List<JSONObject> objList = request.getAttribute("objList")==null?null:(List<JSONObject>)request.getAttribute("objList");
List<Customer> cList = (List<Customer>)request.getAttribute("customerlist");

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
	$("#cwbs").focus();
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
		$("#exportcwbs").val(cwbs);
		$("#btnval").attr("disabled","disabled");
	 	$("#btnval").val("请稍后……");
	 	$("#searchForm2").submit();
	}else{
		alert("没有订单信息，不能导出！");
	};
	};
}

var weipage=1;
var yipage=1;
function weiruku(){
	 weipage+=1;
	  $.ajax({
			type:"post",
			url:"<%=request.getContextPath()%>/PDA/getbackintowarehouseweirukulist",
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

function yiruku(){
	yipage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbackintowarehouseyirukulist",
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
 



</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="saomiao_box2">
	
		<div class="saomiao_tab2">
		<ul>
			<li><a href="<%=request.getContextPath()%>/PDA/backimport" >入库明细</a></li>		
			<li><a href="#" class="light" >入库批量</a></li>
		</ul>
	</div>


	<div class="saomiao_topnum2">
		<dl style="cursor:pointer" onclick="tabView('table_weituihuoruku')"  class="blue">
			<dt>未入库合计</dt>
			<dd id="rukukucundanshu">${weirukucount }</dd>
		</dl>
		
		<dl style="cursor:pointer" onclick="tabView('table_yituihuoruku')" class="green">
			<dt>已入库</dt>
			<dd id="successcwbnum" name="successcwbnum">${yirukucount }</dd>
		</dl>
		
		<br clear="all">
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="saomiao_righttitle2" id="pagemsg"></div>
			<form method="post" action="<%=request.getContextPath()%>/PDA/cwbbackintowarhouseBatch">
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
					<p><span>订单号：</span>
						<textarea cols="24" rows="4"  name ="cwbs" id="cwbs" ></textarea>
					</p>
					<span>&nbsp;</span><input type="submit" name="finish" id="finish" value="确认批量入库" class="button" />
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
				<span style="float: right; padding: 10px"></span>
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
											<td width="350" align="center" bgcolor="#f1f1f1">地址</td>
											<td align="center" bgcolor="#f1f1f1">异常原因</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="errorTable" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
											<%if(objList!=null)for(JSONObject obj : objList){if(!obj.get("errorcode").equals("000000")){ %>
											<%JSONObject cwbOrder =  obj.get("cwbOrder")==null?null:JSONObject.fromObject(obj.get("cwbOrder"));%>
											<tr id="TR<%=obj.get("cwb") %>" cwb="<%=obj.get("cwb") %>" customerid="<%=cwbOrder==null?"":cwbOrder.getString("customerid") %>">
												<td width="120" align="center"><%=obj.get("cwb") %></td>
												<td width="100" align="center"><%=obj.getString("customername") %></td>
												<td width="140" align="center"><%=cwbOrder==null?"":cwbOrder.getString("emaildate") %></td>
												<td width="100" align="center"><%=cwbOrder==null?"":cwbOrder.getString("consigneename") %></td>
												<td width="100" align="center"><%=cwbOrder==null?"":cwbOrder.getDouble("receivablefee") %></td>
												<td width="350" align="left"><%=cwbOrder==null?"":cwbOrder.getString("consigneeaddress") %></td>
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
	<form action="<%=request.getContextPath()%>/PDA/backimportexport" method="post" id="searchForm3">
		<input  type="hidden"  name="type"  id="type"/>
	</form>
</div>
</body>
</html>
