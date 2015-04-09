<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Truck,cn.explink.domain.Bale,cn.explink.domain.Switch"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbDetailView> weichukuList = (List<CwbDetailView>)request.getAttribute("weiChuKuList");
List<CwbDetailView> yichukuList = (List<CwbDetailView>)request.getAttribute("yiChuKuList");
List<Customer> cList = (List<Customer>)request.getAttribute("customerList");
List<JSONObject> objList = request.getAttribute("objList")==null?null:(List<JSONObject>)request.getAttribute("objList");
List<Branch> bList = request.getAttribute("branchList")==null?new ArrayList<Branch>():(List<Branch>)request.getAttribute("branchList");
List<User> uList = request.getAttribute("userList")==null?new ArrayList<User>():(List<User>)request.getAttribute("userList");
List<Truck> tList = request.getAttribute("truckList")==null?new ArrayList<Truck>():(List<Truck>)request.getAttribute("truckList");
Map usermap = (Map) session.getAttribute("usermap");
long  branchid=request.getParameter("branchid")==null?0:Long.parseLong(request.getParameter("branchid"));
boolean showCustomerSign= request.getAttribute("showCustomerSign")==null?false:(Boolean)request.getAttribute("showCustomerSign");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>出库扫描</title>
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
		$("#cwbs").focus();
		getcwbsquejiandataForBranchid($("#branchid").val());
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


function exportField(flag,branchid){
	var cwbs = "";
	if(flag==1){
		$("#type").val("weichuku");
		$("#searchForm3").submit();
	}else if(flag==2){
		$("#type").val("yichuku");
		$("#searchForm3").submit();
	}else if(flag==3){//修改导出问题
		
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
	}else if(flag==4){
		$("#type").val("ypdj");
		$("#searchForm3").submit();
		
	}
	
}

function changevalue(){
	var value = $("#confirmflag").attr("checked")=="checked"?1:0;
	$("#confirmflag").val(value);
}
//得到当前出库的站点的库存量
function getChangeOutSum(branchid){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/PDA/getChangeOutSum?nextbranchid="+branchid,
		dataType:"json",
		success : function(data) {
			$("#chukukucundanshu").html(data.weichukucount);
			$("#chukukucunjianshu").html(data.weichukusum);
			$("#singleoutnum").html(data.yichukucount);
		}
	});
	//未出库明细、已出库明细、异常单明细只显示该下一站明细
	/* if(branchid>0){
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
	} */
}

//得到出库缺货件数的统计
function getcwbsquejiandataForBranchid(nextbranchid) {
	$.ajax({
		type : "POST",
		url : "<%=request.getContextPath()%>/PDA/getOutQueSum",
		data : {
			"nextbranchid" : nextbranchid
		},
		dataType : "json",
		success : function(data) {
			$("#lesscwbnum").html(data.lesscwbnum);
		}
	});
}

//得到出库缺货件数的list列表
function getchukucwbquejiandataList(nextbranchid){
	$.ajax({
		type : "POST",
		url : "<%=request.getContextPath()%>/PDA/getChangeOutQueList",
		data : {
			"nextbranchid" : nextbranchid
		},
		dataType : "html",
		success : function(data) {
			$("#lesscwbTable").html(data);
		}
	});
	
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
	window.location.href="<%=request.getContextPath() %>/PDA/cwbchangeoutwarhouseBatch?branchid="+$("#branchid").val();	
}


</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="saomiao_box2">

<div class="saomiao_tab2">
		<ul>
			<li><a href="<%=request.getContextPath()%>/PDA/changeexportwarhouse" >逐单操作</a></li>		
			<li><a href="#" class="light" >批量操作</a></li>
		</ul>
	</div>
 
	<div class="saomiao_topnum2">
		<dl class="blue">
			<dt>待出库</dt>
			<dd style="cursor:pointer" onclick="tabView('table_weichuku')"  id="chukukucundanshu">${count }</dd>
		</dl>
		<dl class="yellow">
			<dt>待出库件数合计</dt>
			<dd id="chukukucunjianshu">${sum }</dd>
		</dl>
		
		<dl class="green">
			<dt>本次已出库</dt>
			<dd style="cursor:pointer" onclick="tabView('table_yichuku')" id="singleoutnum">${yichukucount }</dd>
		</dl>
		<dl class="yellow">
			<dt>一票多件缺货件数</dt>
			<dd style="cursor:pointer" onclick="tabView('table_quejian');getchukucwbquejiandataList($('#branchid').val());"  id="lesscwbnum" name="lesscwbnum" >${lesscwbnum }</dd>
		</dl>
		<br clear="all"/>
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="saomiao_righttitle2" id="pagemsg"></div>
			<form action="<%=request.getContextPath()%>/PDA/cwbchangeoutwarhouseBatch" id="submitform" method="post">
			<div class="saomiao_selet2">
				下一站：
				<select id="branchid" name="branchid" onchange="tohome();">
					<option value="-1" selected>请选择</option>
					<%
					for(Branch b : bList){ %>
						<option value="<%=b.getBranchid() %>" <%if(branchid==b.getBranchid()) {%> selected=selected<% }%> ><%=b.getBranchname() %></option>
					<%} %>
				</select>
				驾驶员：
				<select id="driverid" name="driverid">
					<option value="-1" selected>请选择</option>
					<%for(User u : uList){ %>
						<option value="<%=u.getUserid() %>" ><%=u.getRealname() %></option>
					<%} %>
		        </select>
				车辆：
				<select id="truckid" name="truckid">
					<option value="-1" selected>请选择</option>
					<%for(Truck t : tList){ %>
						<option value="<%=t.getTruckid() %>" ><%=t.getTruckno() %></option>
					<%} %>
		        </select>
			</div>
			<div class="saomiao_inwrith2">
				<div class="saomiao_left2">
					<%if(Long.parseLong(usermap.get("isImposedOutWarehouse").toString())==1){ %>
					<p>
						<span>强制出库:</span><input type="checkbox" id="confirmflag" name="confirmflag" value=''onclick="changevalue();"/>
					</p>
					<%} %>
					<p><span>订单号：</span>
						<%-- <input type="text" class="saomiao_inputtxt2" value="" id="scancwb" name="scancwb"  onKeyDown='if(event.keyCode==13&&$(this).val().length>0){exportWarehouse("<%=request.getContextPath()%>",$(this).val(),$("#branchid").val(),$("#driverid").val(),$("#truckid").val(),$("#requestbatchno").val(),$("#baleno").val(),$("#ck_switch").val(),$("#confirmflag").attr("checked")=="checked"?1:0);}'/> --%>
						<textarea cols="24" rows="4"  name ="cwbs" id="cwbs" ></textarea>
					</p>
					<span>&nbsp;</span><input type="submit" name="finish" id="finish" value="确认批量出库" class="button" >
				</div>
				<div class="saomiao_right2">
					<p id="msg" name="msg" >${msg }</p>
					<div style="display: none" id="EMBED">
					</div>
					<div style="display: none">
						<EMBED id='ypdj' name='ypdj' SRC='<%=request.getContextPath() %><%=ServiceUtil.waverrorPath %><%=CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl() %>' LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
					</div>
					<div style="display: none" id="errorvedio">
					</div>
				</div>
				<input type="hidden" id="requestbatchno" name="requestbatchno" value="0" />
		        <input type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" />
		        <input type="hidden" id="ck_switch" name="ck_switch" value="ck_02" />
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
					<li><a id="table_quejian" href="#" onclick="getchukucwbquejiandataList($('#branchid').val());">一票多件缺件</a></li>
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
										<table id="weichukuTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%for(CwbDetailView co : weichukuList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
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
									<div  style="height: 160px; overflow-y: scroll">
										<table id="successTable" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
											<%for(CwbDetailView co : yichukuList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>">
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView() %></td>
												<%} %>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
											<%if(yichukuList!=null&&yichukuList.size()==Page.DETAIL_PAGE_NUMBER){ %>
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
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(4,$("#customerid").val());'/>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="120" align="center" bgcolor="#f1f1f1">运单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="lesscwbTable" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
										<%-- <%for(JSONObject obj : quejianList){ %>
											<%
												String transcwb = ""; 
												if(obj.getString("transcwb").indexOf("explink")>-1){
													
												}else if(obj.getString("transcwb").indexOf("havetranscwb")>-1){
													transcwb = obj.getString("transcwb").split("_")[1];
												}
											%>
											<tr id="TR<%=obj.getString("cwb") %>" cwb="<%=obj.getString("cwb") %>" customerid="<%=obj.getLong("customerid") %>">
											<td width="120" align="center"><%=obj.getString("cwb") %></td>
											<td width="120" align="center"><%=transcwb %></td>
											<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==obj.getLong("customerid")){out.print(c.getCustomername());break;}} %></td>
											<td width="140"><%=obj.getString("emaildate") %></td>
											<td width="100"><%=obj.getString("consigneename") %></td>
											<td width="100"><%=obj.getDouble("receivablefee") %></td>
											<td align="left"><%=obj.getString("consigneeaddress") %></td>
											</tr>	
										<%} %> --%>
										
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
												<%if(showCustomerSign){ %>
													<td width="100"><%=obj.get("showRemark") %></td>
												<%} %>
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
 		<form action="<%=request.getContextPath() %>/PDA/exportBybranchid" method="post" id="searchForm3">
			<input  type="hidden"  name="branchid" value="<%=request.getParameter("branchid")==null?"0":request.getParameter("branchid")%>" id="expbranchid" />
			<input type="hidden" name="type" value="" id="type"/>
		</form>
</div>
</body>
</html>
