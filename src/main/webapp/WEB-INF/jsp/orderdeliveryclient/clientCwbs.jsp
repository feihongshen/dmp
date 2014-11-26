<%@page language="java" import="java.util.*,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="net.sf.json.JSONObject"%>
<%
	List<CwbOrderView> yilinghuoList=request.getAttribute("yilinghuoList")==null?null:(List<CwbOrderView>)request.getAttribute("yilinghuoList");
	/* long yilinghuoNum=request.getAttribute("yilinghuoNum")==null?0:Long.parseLong(request.getAttribute("yilinghuoNum").toString()); */
	List<CwbOrderView> weipaiList=request.getAttribute("weipaiList")==null?null:(List<CwbOrderView>)request.getAttribute("weipaiList");
	/* long weipaiNum=request.getAttribute("weipaiNum")==null?0:Long.parseLong(request.getAttribute("weipaiNum").toString()); */
	List<User> uList = (List<User>)request.getAttribute("uList");
	List<JSONObject> objList =request.getAttribute("objList")==null?null:(List<JSONObject>)request.getAttribute("objList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>领货委托派送（批量）</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script language="javascript">
$(function(){
	var $menuli = $(".kfsh_tabbtn2 ul li");
	var $menulilink = $(".kfsh_tabbtn2 ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
	
	getSum(0);
});
$(function(){
	var $menuli = $(".uc_midbg ul li");
	var $menulilink = $(".uc_midbg ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
	
});

function sub(){
	
	if($("#clientid").val()==-1){
		$("#msg").html("确定批量处理前请选择小件员");
		$("#cwbs").val("");
		return false;
	}
	if($.trim($("#cwbs").val()).length==0){
		$("#msg").html("确定批量处理前请输入订单号，多个订单用回车分割");
		$("#cwbs").val("");
		return false;
	}
	$("#subButton").val("正在处理！请稍候...");
	$("#subButton").attr('disabled','disabled');
	$("#subForm").submit();
}

//导出Excel
function exportField(flag){
	$("#flag").val(flag);
	$("#btnval"+flag).attr("disabled","disabled"); 
 	$("#btnval"+flag).val("请稍后……");
	$("#exportForm").submit();
}

function getSum(clientid){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/orderdeliveryclient/getSum?clientid="+clientid,
		dataType:"json",
		success : function(data) {
			$("#yilinghuoNum").html(data.yilinghuoNum);
			$("#weipaiNum").html(data.weipaiNum);
		}
	});
}
</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="saomiao_box2">

	<div class="saomiao_topnum2">
		<dl class="blue">
			<dt>已领货</dt>
			<dd id="yilinghuoNum">0</dd>
		</dl>
		
		<dl class="green">
			<dt>已委派</dt>
			<dd id="weipaiNum">0</dd>
		</dl>
		<input type="button"  id="refresh" value="刷新" onclick="location.href='<%=request.getContextPath() %>/orderdeliveryclient/clientCwbs'" style="float:left; width:100px; height:65px; cursor:pointer; border:none; background:url(../images/buttonbgimg1.gif) no-repeat; font-size:18px; font-family:'微软雅黑', '黑体'"/>
		<br clear="all"/>
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
		<form id="subForm" action="<%=request.getContextPath() %>/orderdeliveryclient/clientCwbs" method="post">
				<div class="saomiao_selet2">委托派送小件员：
					<select id="clientid" name="clientid" onChange="getSum($(this).val());">
						<option value="-1" selected>请选择</option>
						<%if(uList!=null&&!uList.isEmpty()){
						for(User u : uList){ %>
							<option value="<%=u.getUserid()%>"><%=u.getRealname()%></option>
						<%}}%>
			        </select>
					<strong style="color:red">*</strong>
				</div>
				<div class="saomiao_inwrith2">
				<div class="saomiao_left2">
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tr>
							<td valign="top">订单号：
								<textarea name="cwbs" cols="45" rows="3" id="cwbs"></textarea>
									&nbsp;
								<input type="button" id="subButton" value="确定批量处理" onclick="sub()" class="input_button1" />
							</td>
						</tr>
					</table>
				</div>
				<div class="saomiao_right2">
					<p id="msg" name="msg">${msg}</p>
				</div>
				</form>
			</div>
		</div>
	</div>
	
	<div>
		<div class="kfsh_tabbtn2">
			<ul>
				<li><a href="#" class="light">已领货</a></li>
				<li><a href="#">已委派扫描明细</a></li>
				<li><a href="#">异常单明细</a></li>
			</ul>
		</div>
	<div class="tabbox">
			<li>
				<%if(yilinghuoList!=null&!yilinghuoList.isEmpty()){%>
					<input type ="button" id="btnval1" value="导出Excel" class="input_button1" onclick="exportField('1');"/>
				<%}%>
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr >
							<td width="10%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" >
									<tr>
										<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
										<td width="100" align="center" bgcolor="#f1f1f1">小件员</td>
										<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
										<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
										<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
										<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
										<td width="120" align="center" bgcolor="#f1f1f1">备注</td>
										<td align="center" bgcolor="#f1f1f1">地址</td>
									</tr>
								</table>
								<div style="height:160px; overflow-y:scroll">
								<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
									<%if(yilinghuoList!=null&!yilinghuoList.isEmpty()){
										for(CwbOrderView list:yilinghuoList){
										%>
									<tr>
										<td width="120" align="center"><%=list.getCwb()%></td>
										<td width="100" align="center"><%=list.getDelivername()%></td>
										<td width="100" align="center"><%=list.getCustomername()%></td>
										<td width="140"><%=list.getEmaildate()%></td>
										<td width="100"><%=list.getConsigneename()%></td>
										<td width="100"><%=list.getReceivablefee()%></td>
										<td width="120" align="left"><%=list.getCwbremark()%></td>
										<td align="left"><%=list.getConsigneeaddress()%></td>
									</tr>
									<%}}%>
								</table>
							</div></td>
						</tr>
					</tbody>
				</table>
			</li>
			<li style="display:none">
				<%if(weipaiList!=null&!weipaiList.isEmpty()){%>
					<input type ="button" id="btnval2" value="导出Excel" class="input_button1"  onclick="exportField('2');"/>
				<%}%>
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr >
							<td width="10%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" >
									<tr>
										<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
										<td width="100" align="center" bgcolor="#f1f1f1">委派人</td>
										<td width="100" align="center" bgcolor="#f1f1f1">原小件员</td>
										<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
										<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
										<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
										<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
										<td width="120" align="center" bgcolor="#f1f1f1">备注</td>
										<td align="center" bgcolor="#f1f1f1">地址</td>
									</tr>
								</table>
								<div style="height:160px; overflow-y:scroll">
								<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
									<%
									if(weipaiList!=null&!weipaiList.isEmpty()){
										for(CwbOrderView list:weipaiList){
									%>
									<tr>
										<td width="120" align="center"><%=list.getCwb()%></td>
										<td width="100" align="center"><%=list.getFdelivername()%></td>
										<td width="100" align="center"><%=list.getDelivername()%></td>
										<td width="100" align="center"><%=list.getCustomername()%></td>
										<td width="140"><%=list.getEmaildate()%></td>
										<td width="100"><%=list.getConsigneename()%></td>
										<td width="100"><%=list.getReceivablefee()%></td>
										<td width="120" align="left"><%=list.getCwbremark()%></td>
										<td align="left"><%=list.getConsigneeaddress()%></td>
									</tr>
									<%}}%>
								</table>
							</div></td>
						</tr>
					</tbody>
				</table>
			</li>
			
			
				<li style="display:none">
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr >
							<td width="10%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" >
									<tr>
										<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
										<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
										<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
										<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
										<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
										<td width="120" align="center" bgcolor="#f1f1f1">备注</td>
										<td width="450" align="center" bgcolor="#f1f1f1">地址</td>
										<td align="center" bgcolor="#f1f1f1">异常信息</td>
									</tr>
								</table>
								<div style="height:160px; overflow-y:scroll">
								<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
									<%if(objList!=null){
										for(JSONObject obj : objList){
											if(obj.get("errorcode").equals("000000")){
												JSONObject cwbOrder =obj.get("cwbOrder")==null?null:JSONObject.fromObject(obj.get("cwbOrder"));
									%>
									<tr>
										<td width="120" align="center"><%=cwbOrder.getString("cwb")%></td>
										<td width="100" align="center"><%=cwbOrder.getString("cwb")%></td>
										<td width="140" align="center"><%=cwbOrder.getString("emaildate")%></td>
										<td width="100" align="center"><%=cwbOrder.getString("consigneename")%></td>
										<td width="100"><%=cwbOrder.getString("receivablefee")%></td>
										<td width="120"><%=cwbOrder.getString("cwbremark")%></td>
										<td width="450"><%=cwbOrder.getString("consigneeaddress")%></td>
									<%}else{%>
									<tr>
										<td width="120" align="center"><%=obj.get("cwb")%></td>
										<td width="100" align="center"></td>
										<td width="140" align="center"></td>
										<td width="100" align="center"></td>
										<td width="100"></td>
										<td width="120"></td>
										<td width="450"></td>
									<%}%>
										<td><%=obj.get("error") %></td>
									</tr>
									<%}
									}%>
								</table>
							</div></td>
						</tr>
					</tbody>
				</table>
			</li>
		</div>
	</div>
<form action="<%=request.getContextPath() %>/orderdeliveryclient/export" method="post" id="exportForm">
	<input type="hidden" name="flag" id="flag"/>
</form>
</body>
</html>