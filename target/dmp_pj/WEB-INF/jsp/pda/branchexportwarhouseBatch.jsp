<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Truck,cn.explink.domain.Bale,cn.explink.domain.Switch"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbDetailView> weichukuList = (List<CwbDetailView>)request.getAttribute("weichukulist");
List<CwbDetailView> yichukulist = (List<CwbDetailView>)request.getAttribute("yichukulist");
List<Customer> cList = (List<Customer>)request.getAttribute("customerlist");
List<JSONObject> objList = request.getAttribute("objList")==null?null:(List<JSONObject>)request.getAttribute("objList");
boolean showCustomerSign= request.getAttribute("showCustomerSign")==null?false:(Boolean)request.getAttribute("showCustomerSign");
List<Branch> bList = request.getAttribute("branchlist")==null?new ArrayList<Branch>():(List<Branch>)request.getAttribute("branchlist");
List<User> uList = request.getAttribute("userList")==null?new ArrayList<User>():(List<User>)request.getAttribute("userList");
Map usermap = (Map) session.getAttribute("usermap");
String did=request.getParameter("branchid")==null?"0":request.getParameter("branchid");
long branchid=Long.parseLong(did);

StringBuffer sb=new StringBuffer();
for(CwbDetailView ss:weichukuList){
	sb.append("'");
	sb.append(ss.getCwb());
	sb.append("',");
}
String weicwbs=sb.length()==0?"":sb.toString();
StringBuffer yichu=new StringBuffer();
for(CwbDetailView yi:yichukulist){
	yichu.append("'");
	yichu.append(yi.getCwb());
	yichu.append("',");
}
String yicwbs=yichu.length()==0?"":yichu.toString();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>站点出站批量扫描</title>
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
		getZhanDianChuZhanSum($("#branchid").val());
		getZhanDianYiChuZhanSum($("#branchid").val());
		$("#cwbs").focus();
	});
	
	
function tabView(tab){
	$("#"+tab).click();
}

	//得到当前出库的站点的库存量
	function getZhanDianChuZhanSum(deliverybranchid){
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/PDA/getZhanDianChuZhanSum?deliverybranchid="+deliverybranchid,
			dataType:"json",
			success : function(data) {
				$("#chukukucundanshu").html(data.size);
			}
		});
		
		//未出库明细、已出库明细、异常单明细只显示该下一站明细
		if(deliverybranchid>0){
			$("#weichukuTable tr").hide();
			$("#weichukuTable tr[deliverybranchid='"+deliverybranchid+"']").show();
			
			$("#successTable tr").hide();
			$("#successTable tr[deliverybranchid='"+deliverybranchid+"']").show();
			
			$("#errorTable tr").hide();
			$("#errorTable tr[deliverybranchid='"+deliverybranchid+"']").show();
		}else{
			$("#weichukuTable tr").show();
			$("#successTable tr").show();
			$("#errorTable tr").show();
		}
	}
	function getZhanDianYiChuZhanSum(deliverybranchid){
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/PDA/getZhanDianYiChuZhanSum?deliverybranchid="+deliverybranchid,
			dataType:"json",
			success : function(data) {
				$("#yichukukucundanshu").html(data.size);
			}
		});
		
		//未出库明细、已出库明细、异常单明细只显示该下一站明细
		if(deliverybranchid>0){
			$("#weichukuTable tr").hide();
			$("#weichukuTable tr[deliverybranchid='"+deliverybranchid+"']").show();
			
			$("#successTable tr").hide();
			$("#successTable tr[deliverybranchid='"+deliverybranchid+"']").show();
			
			$("#errorTable tr").hide();
			$("#errorTable tr[deliverybranchid='"+deliverybranchid+"']").show();
		}else{
			$("#weichukuTable tr").show();
			$("#successTable tr").show();
			$("#errorTable tr").show();
		}
	}

function exportField(flag,branchid){
	var cwbs = "";
	if(flag==1){
		cwbs=$("#weichuku").val();
	}else if(flag==2){
		cwbs=$("#yichuku").val();
	}else if(flag==3){//修改导出问题
		/* if(branchid>0){
			$("#errorTable tr[deliverybranchid='"+branchid+"']").each(function(){
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

function tohome(){
	window.location.href="<%=request.getContextPath() %>/PDA/cwbbranchexportwarhouseBatch?branchid="+$("#branchid").val();
}
</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="saomiao_box2">
 
	<div class="saomiao_topnum2">
		<dl class="blue">
			<dt>待出站</dt>
			<dd style="cursor:pointer" onclick="tabView('table_weichuku')"  id="chukukucundanshu">${weicount }</dd>
		</dl>
		
		<dl class="green">
			<dt>已扫描</dt>
			<dd style="cursor:pointer" onclick="tabView('table_yichuku')" id="yichukukucundanshu">${yicount }</dd>
		</dl>
		<br clear="all"/>
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="saomiao_righttitle2" id="pagemsg"></div>
			<form action="<%=request.getContextPath() %>/PDA/cwbbranchexportwarhouseBatch" method="post">
			<div class="saomiao_selet2">
				下一站：
				<select id="branchid" name="branchid" onchange="tohome()">
					<%for(Branch b : bList){ %>
						<%if(b.getBranchid()!=Long.parseLong(usermap.get("branchid").toString())){ %>
						<option value="<%=b.getBranchid() %>" <%if(branchid==b.getBranchid()) {%>selected <%} %> ><%=b.getBranchname() %></option>
					<%}} %>
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
					<p style="display: none;"><span>包号：</span><input type="text" class="saomiao_inputtxt2" name="baleno" id="baleno" onKeyDown="if(event.keyCode==13&&$(this).val().length>0){$(this).attr('readonly','readonly');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();}"/></p>
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
				<span style="float: right; padding: 10px"></span>
				<ul id="smallTag">
					<li><a id="table_weichuku" href="#" class="light">待出库明细</a></li>
					<li><a id="table_yichuku" href="#">已出库明细</a></li>
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
											<%for(CwbDetailView co : weichukuList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" deliverybranchid="<%=co.getDeliverybranchid() %>">
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
									</table>
									<!-- <div style="height: 160px; overflow-y: scroll">
										<table id="weichukuTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2"> -->
										<!-- </table>
									</div> -->
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
											<%for(CwbDetailView co : yichukulist){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" deliverybranchid="<%=co.getDeliverybranchid() %>">
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
									</table>
									<!-- <div style="height: 160px; overflow-y: scroll">
										<table id="successTable" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2"> -->
										<!-- </table>
									</div> -->
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
											<%if(objList!=null)for(JSONObject obj : objList){if(!obj.get("errorcode").equals("000000")){ %>
											<%JSONObject cwbOrder =  obj.get("cwbOrder")==null?null:JSONObject.fromObject(obj.get("cwbOrder"));%>
											<tr id="TR<%=obj.get("cwb") %>error" cwb="<%=obj.get("cwb") %>" deliverybranchid="<%=cwbOrder==null?"":cwbOrder.getLong("deliverybranchid") %>">
												<td width="120" align="center"><%=obj.get("cwb") %></td>
												<td width="100" align="center"><%=obj.get("customername") %></td>
												<td width="140" align="center"><%=cwbOrder==null?"":cwbOrder.getString("emaildate") %></td>
												<td width="100" align="center"><%=cwbOrder==null?"":cwbOrder.getString("consigneename") %></td>
												<td width="100" align="center"><%=cwbOrder==null?"":cwbOrder.getDouble("receivablefee") %></td>
												<%if(showCustomerSign){ %>
<td width="100"><%=obj.get("showRemark") %></td>
<%} %>
												<td width="380" align="left"><%=cwbOrder==null?"":cwbOrder.getString("consigneeaddress") %></td>
												<td align="center"><%=obj.get("errorinfo") %></td>
											</tr>
											<%}} %>
									</table>
									<!-- <div style="height: 160px; overflow-y: scroll">
										<table id="errorTable" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2"> -->
										<!-- </table>
									</div -->>
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
			<input type="hidden" name="yichuku" id="yichuku" value="<%=yicwbs%>"/>
			<input type="hidden" name="weichuku" id="weichuku" value="<%=weicwbs%>"/>
		</form>
 
</div>
</body>
</html>
