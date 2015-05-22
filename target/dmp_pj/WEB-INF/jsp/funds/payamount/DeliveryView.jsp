<%@page import="cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.math.BigDecimal"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.domain.DeliveryState"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<JSONObject> deliveryStatelist = (List<JSONObject>)request.getAttribute("deliveryStatelist");
List<User> userList = (List<User>)request.getAttribute("userList");

Page page_obj =(Page)request.getAttribute("page_obj");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$(function(){
	$("#beginemaildate").datepicker();
	$("#endemaildate").datepicker();
});
</script>
</HEAD>

<body style="background:#f5f5f5">
<div class="right_box">
	<div class="inputselect_box">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:10px">
	<tr>
		<td align="left">
		<input type ="button" id="btnval" value="导出excel" class="input_button2" onclick="javascript:location.href='<%=request.getContextPath()%>/funds/showExp/<%=request.getAttribute("payupid")%>?branchid=<%=request.getAttribute("branchid")==null?"-1":request.getAttribute("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getAttribute("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getAttribute("endBranchpaydatetime"))%>&upstate=<%=request.getAttribute("upstate")==null?"0":request.getAttribute("upstate") %>'"/>
 	    <input type ="button" id="btnval" value="返回" class="input_button2" onclick="javascript:location.href='<%=request.getContextPath()%>/funds/payamount?branchid=<%=request.getAttribute("branchid")==null?"-1":request.getAttribute("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getAttribute("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getAttribute("endBranchpaydatetime"))%>&upstate=<%=request.getAttribute("upstate")==null?"0":request.getAttribute("upstate") %>'" />
		</td>
	</tr>
</table>
	</div>
	<div class="right_title">
	<div style="height:20px"></div>
	<div style="overflow-x:scroll; width:100%" id="scroll">
	<table width="1800" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
				<tr>
					<td colspan="4">
					   <table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tr class="font_1">
								<td   height="38" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
								<td   align="center" valign="middle" bgcolor="#eef6ff">小件员</td>
								<td   align="center" valign="middle" bgcolor="#eef6ff">订单应收金额</td>
								<td   align="center" valign="middle" bgcolor="#eef6ff">退还金额</td>
								<td   align="center" valign="middle" bgcolor="#eef6ff">应处理金额</td>
								<td   align="center" valign="middle" bgcolor="#eef6ff">现金实收</td>
								<td   align="center" valign="middle" bgcolor="#eef6ff">pos实收</td>
								<td   align="center" valign="middle" bgcolor="#eef6ff">支付宝COD扫码</td>
								<td   align="center" valign="middle" bgcolor="#eef6ff">备注</td>
								<td   align="center" valign="middle" bgcolor="#eef6ff">反馈时间</td>
								<td   align="center" valign="middle" bgcolor="#eef6ff">其他金额</td>
								<td   align="center" valign="middle" bgcolor="#eef6ff">支票实收</td>
								<td   align="center" valign="middle" bgcolor="#eef6ff">支票号备注</td>
								<td   align="center" valign="middle" bgcolor="#eef6ff">站点上缴时间</td>
								<td   align="center" valign="middle" bgcolor="#eef6ff">归班审核时间</td>
								<td   align="center" valign="middle" bgcolor="#eef6ff">配送结果</td>
							</tr>
							<%if(deliveryStatelist != null && deliveryStatelist.size()>0){ %>
							<% for(JSONObject obj : deliveryStatelist){if(obj.getLong("id")!=0){ %>
							<tr valign="middle">
										<td align="center" valign="middle" ><%=obj.getString("cwb")%></td>
										<td align="center" valign="middle" >
										<%for(User u : userList){if(obj.getLong("deliveryid")==u.getUserid()){ %>
											<%=u.getRealname()%>
										<%}} %>
										</td>
										<td align="right" valign="middle" ><%=BigDecimal.valueOf(obj.getDouble("receivedfee")) %></td>
										<td align="right" valign="middle" ><%=BigDecimal.valueOf(obj.getDouble("returnedfee")) %> </td>
										<td align="right" valign="middle" ><%=BigDecimal.valueOf(obj.getDouble("businessfee")) %></td>
										<td align="right" valign="middle" ><strong><%=BigDecimal.valueOf(obj.getDouble("cash")) %></strong></td>
										<td align="right" valign="middle" ><strong><%=BigDecimal.valueOf(obj.getDouble("pos")) %></strong></td>
										<td align="right" valign="middle" ><strong><%=BigDecimal.valueOf(obj.getDouble("codpos")) %></strong></td>
										<td align="center" valign="middle" ><%=StringUtil.nullConvertToEmptyString(obj.getString("posremark"))%></td>
										<td align="center" valign="middle" ><%=obj.getString("createtime")%></td>
										<td align="right" valign="middle" ><strong><%=BigDecimal.valueOf(obj.getDouble("otherfee"))%></strong></td>
										<td align="right" valign="middle" ><strong><%=BigDecimal.valueOf(obj.getDouble("checkfee"))%></strong></td>
										<td align="center" valign="middle" ><%=StringUtil.nullConvertToEmptyString(obj.getString("checkremark"))%></td>
										<td align="left" valign="middle" ><%=obj.getString("credatetime").substring(0, obj.getString("credatetime").length()-2)%></td>
										<td align="center" valign="middle" ><%=obj.getString("auditingtime")%></td>
										<td align="center" valign="middle" >
										<%for(DeliveryStateEnum ds : DeliveryStateEnum.values()){if(obj.getLong("deliverystate")==ds.getValue()){ %>
										<%=ds.getText() %>
										<%}} %>
										</td>
									</tr>
							 <%}} }%> 
					   </table>
					</td>
				</tr>
			</table>
	</div>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
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
	<form name="form1" id = "searchForm" action ="1" method ="post"></form>
	
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>

</HTML>
