<%@page language="java" import="java.util.*,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page import="cn.explink.domain.User"%>
<%
	List<CwbOrderView> yilinghuoList=request.getAttribute("yilinghuoList")==null?null:(List<CwbOrderView>)request.getAttribute("yilinghuoList");
	/* long yilinghuoNum=request.getAttribute("yilinghuoNum")==null?0:Long.parseLong(request.getAttribute("yilinghuoNum").toString()); */
	List<CwbOrderView> weipaiList=request.getAttribute("weipaiList")==null?null:(List<CwbOrderView>)request.getAttribute("weipaiList");
	/* long weipaiNum=request.getAttribute("weipaiNum")==null?0:Long.parseLong(request.getAttribute("weipaiNum").toString()); */
	List<User> uList = (List<User>)request.getAttribute("uList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>领货委托派送（明细）</title>
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
	
})

function submitClient(){
	clearShow();
	$.ajax({
  		type: "POST",
  		url:"<%=request.getContextPath()%>/orderdeliveryclient/saveCwb/"+$("#cwb").val(),
  		data:"clientid="+$("#clientid").val(),
  		dataType : "json",
  		success : function(data) {
  			if(data.statuscode==0){
 				$("#cwbShow").html("订单号："+data.body.cwbOrder.cwb);
 				$("#delivernameShow").html("小件员："+data.body.delivername);
 				$("#clientnameShow").html("委托派送小件员："+data.body.clientname);
 				$("#addressShow").html("地 址："+data.body.cwbOrder.consigneeaddress);
  			}else{
  				$("#msg").html("（异常扫描）"+data.body.error);
  			}
  			$("#cwb").val("");
  		}
  	});
}

function clearShow(){
	$("#msg").html("");
	$("#cwbShow").html("");
	$("#delivernameShow").html("");
	$("#clientnameShow").html("");
	$("#addressShow").html("");
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
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
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
		<input type="button"  id="refresh" value="刷新" onclick="location.href='<%=request.getContextPath() %>/orderdeliveryclient/clientCwb'" style="float:left; width:100px; height:65px; cursor:pointer; border:none; background:url(../images/buttonbgimg1.gif) no-repeat; font-size:18px; font-family:'微软雅黑', '黑体'"/>
		<br clear="all">
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
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
					<p><span>订单号：</span>
						<input type="text" id="cwb" name="cwb" class="saomiao_inputtxt2" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){submitClient();}'/>
					</p>
				</div>
				<div class="saomiao_right2">
					<p id="msg" name="msg"></p>
					<p id="cwbShow"></p>
					<p id="delivernameShow"></p>
					<p id="clientnameShow"></p>
					<p id="addressShow" name="addressShow"></p>
				</div>
			</div>
		</div>
	</div>
	
	<div>
		<div class="kfsh_tabbtn2">
			<ul>
				<li><a href="#" class="light">已领货</a></li>
				<li><a href="#">已委派扫描明细</a></li>
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
										<td width="120"><%=list.getCwbremark()%></td>
										<td><%=list.getConsigneeaddress()%></td>
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
										<td width="100" align="center" bgcolor="#f1f1f1">小件员</td>
										<td width="100" align="center" bgcolor="#f1f1f1">委派员</td>
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
										<td width="100" align="center"><%=list.getDelivername()%></td>
										<td width="100" align="center"><%=list.getFdelivername()%></td>
										<td width="100" align="center"><%=list.getCustomername()%></td>
										<td width="140"><%=list.getEmaildate()%></td>
										<td width="100"><%=list.getConsigneename()%></td>
										<td width="100"><%=list.getReceivablefee()%></td>
										<td width="120"><%=list.getCwbremark()%></td>
										<td><%=list.getConsigneeaddress()%></td>
									</tr>
									<%}}%>
								</table>
							</div></td>
						</tr>
					</tbody>
				</table>
			</li>
		</div>
	</div>
</div>
<form action="<%=request.getContextPath() %>/orderdeliveryclient/export" method="post" id="exportForm">
	<input type="hidden" name="flag" id="flag"/>
</form>
</body>
</html>