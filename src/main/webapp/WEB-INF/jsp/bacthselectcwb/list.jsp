<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<CwbOrderView> orderlist = (List<CwbOrderView>)request.getAttribute("orderlist");
  List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
  CwbOrder cosum = request.getAttribute("cosum")==null?new CwbOrder():(CwbOrder)request.getAttribute("cosum");
  long count = request.getAttribute("count")==null?0:Long.parseLong(request.getAttribute("count").toString());
  Page page_obj = (Page)request.getAttribute("page_obj");
  
  String batchcwb = request.getParameter("batchcwb")==null?"":request.getParameter("batchcwb");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单批量查询</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>

<script type="text/javascript">
$(document).ready(function() {
	$("#find").click(function(){
		if($.trim($("#batchcwb").val()).length==0){
			alert("请输入订单号");
			return false;
		}
		/* if($("#batchcwb").val().split("\n").length>10000){
			alert("查询不能多于10000条");
			return false;
		} */
	 	$("#isshow").val(1);
    	$("#searchForm").submit();
    	$("#find").attr("disabled","disabled");
		$("#find").val("请稍等..");
	});
	
	$("#findCwbs").click(function(){
		if($.trim($("#batchcwb").val()).length==0){
			alert("请输入订单号");
			return false;
		}
		$("#cwbs").val($("#batchcwb").val());
		$("#selectOrdersForm").submit();
		<%-- window.open ("<%=request.getContextPath() %>/order/batchselectorders?batchcwb="+$("#batchcwb").val()); --%>
	});
});
function exportField(){
    if($.trim($("#batchcwb").val()).length==0 && $.trim($("#batchcwb2").val()).length==0){
		alert("请输入订单号");
		return false;
	}
	/* if($("#batchcwb").val().split("\n").length>10000){
		alert("查询不能多于10000条");
		return false;
	} */
	if($.trim($("#batchcwb").val()).length!=0){
		$("#batchcwb2").val($("#batchcwb").val());
	}
	$("#exportmould2").val($("#exportmould").val());
	$("#btnval").attr("disabled","disabled"); 
 	$("#btnval").val("请稍后……");
	$("#searchForm2").submit();	
}

function allremark(){
	if(<%=orderlist == null || orderlist.size()==0%>){
		alert("请您先查询！");
	}else{
		$("#allremarkButton").hide();
		$('#remarkdiv').show();
	}
}
function remarkButton(){
	if($("#cwbremark").val().length==0){
		alert("请填写备注！");
		return false;
	}
	if($("#cwbremark").val().length>200){
		alert("备注不能超过200个字！");
		return false;
	}
	$("#remarkSubmit").attr("disabled","disabled");
	$("#remarkSubmit").val("请稍候");
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/batchselectcwb/batchRemark",
		data:{remarkcwbs:$("#remarkcwbs").val(),
			cwbremark:$("#cwbremark").val()},
		dataType:"json",
		success : function(data) {
			if(data.errorCode==0){}
				$("#remarkSubmit").val(data.error);
			}
	});
}
</script>
</head>
<body style="background:#f5f5f5">
	<div class="right_box">
		<div class="inputselect_box">
		<form action="1" method="post" id="searchForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:90px">
				<tr>
					<td align="left">
						<textarea cols="24" rows="4"  name ="batchcwb" id="batchcwb" ><%=batchcwb %></textarea>
						<input type="hidden" name="isshow" id ="isshow" value ="<%=request.getParameter("isshow")==null?"0":request.getParameter("isshow") %>" />
						<input type="button" id="find" value="查询" class="input_button2" />
						<input type="button" id="allremarkButton" value="批量备注" class="input_button2" onclick="allremark();"/>
						<input type="button" id="findCwbs" value="查询过程" class="input_button2" />
						<font id="remarkdiv" style="display: none;">
						<textarea style="display: none;" cols="24" rows="4"  name ="remarkcwbs" id="remarkcwbs"><%=batchcwb %></textarea>
						<textarea cols="24" rows="4"  name ="cwbremark" id="cwbremark" ></textarea>
						<input type="button" id="remarkSubmit" value="确定" class="input_button2" onclick="remarkButton();"/>
						</font>
						<%if(orderlist != null && orderlist.size()>0){  %>
						<select name ="exportmould" id ="exportmould" class="select1">
				          <option value ="0">默认导出模板</option>
				          <%for(Exportmould e:exportmouldlist){%>
				           <option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
				          <%} %>
						</select>
			　　   			<input type ="button" id="btnval" value="导出excel" class="input_button2" onclick="exportField();"/><%} %>
					</td>
				</tr>
			</table>
		</form>
		<form action="<%=request.getContextPath()%>/batchselectcwb/batchSelectExpore" method="post" id="searchForm2">
			<input type="hidden" name="batchcwb2" id="batchcwb2" value ="<%=batchcwb %>"/>
			<input type="hidden" name="exportmould2" id="exportmould2" />
		</form>
		</div>
		<div class="right_title"><%if(orderlist != null && orderlist.size()>0){  %>
			<div style="height:120px"></div>
			<div style="overflow-x:scroll; width:100% " id="scroll">
				<table width="5000" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
				   <tr class="font_1">
						<td  align="center" valign="middle" bgcolor="#eef6ff" >订单号</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >发货时间</td>
					    <td  align="center" valign="middle" bgcolor="#eef6ff" >发货重量（kg）</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >货物尺寸</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >发货件数</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >订单类型</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >地址</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >收件人</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >收件人手机号</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >收件人电话</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >收货邮编</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >签收人</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >签收时间</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >供货商</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >发货仓库</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >入库库房</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >当前站</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >下一站</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >配送站点</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >小件员</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >称重重量（kg）</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >货品备注</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >应收金额</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >保价金额</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >应退金额</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >原支付方式</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >现支付方式</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >备注1</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >备注2</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >备注3</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >备注4</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >备注5</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >当前订单状态</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >退货备注</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >入库时间</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >出库时间</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >到站时间</td>
					    <td  align="center" valign="middle" bgcolor="#eef6ff" >小件员领货时间</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >配送成功时间</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >反馈时间</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >归班时间</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >最后更新时间</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >退货原因</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >滞留原因</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >供货商异常原因</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >配送结果</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >配送结果备注</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >入库备注</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >POS备注</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >支票号备注</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >反馈结果备注</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >供货商拒收返库备注</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >货物类型</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff" >配送类型</td>
					</tr>
					
					<% for(CwbOrderView  c : orderlist){ %>
							<tr bgcolor="#FF3300">
								<td  align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath()%>/order/queckSelectOrder/<%=c.getCwb() %>"><%=c.getCwb() %></a></td>
								<td  align="center" valign="middle"><%=c.getEmaildate() %></td>
								<td  align="center" valign="middle"><%=c.getCarrealweight() %></td>
								<td  align="center" valign="middle"><%=c.getCarsize() %></td>
								<td  align="center" valign="middle"><%=c.getSendcarnum() %></td>
								<td  align="center" valign="middle"><%=c.getOrderType() %></td>
								<td  align="left" valign="middle"><%=c.getConsigneeaddress() %></td>
								<td  align="center" valign="middle"><%=c.getConsigneename() %></td>
								<td  align="center" valign="middle"><%=c.getConsigneemobile() %></td>
								<td  align="center" valign="middle"><%=c.getConsigneephone() %></td>
								<td  align="center" valign="middle"><%=c.getConsigneepostcode() %></td>
								<td  align="center" valign="middle"><%=c.getSigninman() %></td>
								<td  align="center" valign="middle"><%=c.getSignintime() %></td>
								<td  align="center" valign="middle"><%=c.getCustomername()  %></td>
								<td  align="center" valign="middle"><%=c.getCustomerwarehousename()%></td>
								<td  align="center" valign="middle"><%=c.getInhouse() %></td>
								<td  align="center" valign="middle"><%=c.getCurrentbranchname() %></td>
								<td  align="center" valign="middle"><%=c.getNextbranchname() %></td>
								<td  align="center" valign="middle"><%=c.getDeliverybranch() %></td>
								<td  align="center" valign="middle"><%=c.getDelivername() %></td>
								<td  align="center" valign="middle"><%=c.getRealweight() %></td>
								<td  align="center" valign="middle"><%=c.getGoodsremark()%></td>
								<td  align="right" valign="middle"><%=c.getReceivablefee() %></td>
								<td  align="right" valign="middle"><%=c.getCaramount()%></td>
								<td  align="right" valign="middle"><%=c.getPaybackfee() %></td>
								<td  align="center" valign="middle"><%=c.getPaytype_old() %></td>
								<td  align="center" valign="middle"><%=c.getPaytypeName() %></td>
								<td  align="center" valign="middle"><%=c.getRemark1() %></td>
								<td  align="center" valign="middle"><%=c.getRemark2() %></td>
								<td  align="center" valign="middle"><%=c.getRemark3() %></td>
								<td  align="center" valign="middle"><%=c.getRemark4() %></td>
								<td  align="center" valign="middle"><%=c.getRemark5() %></td>
								<td  align="center" valign="middle"><%=c.getFlowordertypeMethod() %></td>
								<td  align="center" valign="middle"><%=c.getReturngoodsremark() %></td>
								<td  align="center" valign="middle"><%=c.getInstoreroomtime() %></td>
								<td  align="center" valign="middle"><%=c.getOutstoreroomtime() %></td>
								<td  align="center" valign="middle"><%=c.getInSitetime() %></td>
								<td  align="center" valign="middle"><%=c.getPickGoodstime()  %></td>
								<td  align="center" valign="middle"><%=c.getSendSuccesstime() %></td>
								<td  align="center" valign="middle"><%=c.getGobacktime() %></td>
								<td  align="center" valign="middle"><%=c.getGoclasstime()%></td>
								<td  align="center" valign="middle"><%=c.getNowtime() %></td>
								<td  align="center" valign="middle"><%=c.getBackreason() %></td>
								<td  align="center" valign="middle"><%=c.getLeavedreasonStr() %></td>
								<td  align="center" valign="middle"><%=c.getExpt_msg() %></td>
								<td  align="center" valign="middle"><%=c.getDeliverStateText() %></td>
								<td  align="center" valign="middle"><%=c.getPodremarkStr() %></td>
								<td  align="center" valign="middle"><%=c.getInwarhouseremark() %></td>
								<td  align="center" valign="middle"><%=c.getPosremark() %></td>
								<td  align="center" valign="middle"><%=c.getCheckremark() %></td>
								<td  align="center" valign="middle"><%=c.getDeliverstateremark() %></td>
								<td  align="center" valign="middle"><%=c.getCustomerbrackhouseremark() %></td>
								<td  align="center" valign="middle"><%=c.getCartype() %></td>
								<td  align="center" valign="middle"><%=c.getCwbdelivertypeStr() %></td>
							 </tr>
					 <% }%>
				</table>
				<div class="jg_10"></div><div class="jg_10"></div>
					<table width="100%" border="0" cellspacing="1" cellpadding="0" >
						<tr>
							<td class="high">订单总计：<font class="high" color="red"><%=count %></font>&nbsp;单  </td>
						</tr>
						<tr>
							<td class="high">代收金额总计：<font class="high" color="red"><%=cosum.getReceivablefee() %></font>&nbsp;元 </td>
						</tr>
						<tr>
							<td class="high">代退金额总计：<font class="high" color="red"><%=cosum.getPaybackfee() %></font>&nbsp;元 </td>
						</tr>
					</table>
			</div><%} %>
			<div class="jg_10"></div><div class="jg_10"></div>
			<%if(page_obj.getMaxpage()>1){ %>
				<div class="iframe_bottom">
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
						<tr>
							<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
								<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
								<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
								<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
								<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
								　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
										id="selectPg"
										onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
										<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
										<option value="<%=i %>"><%=i %></option>
										<% } %>
									</select>页
							</td>
						</tr>
					</table>
				</div>
			<%} %>
		</div>
	</div>	
	<div class="jg_10"></div>
	<div class="clear"></div>

<form action="<%=request.getContextPath() %>/order/batchselectorders" target="_blank" method="post" id="selectOrdersForm">
	<textarea rows="0" cols="0" id="cwbs" name="cwbs" style="height:0px;"></textarea>
</form>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>
</html>

