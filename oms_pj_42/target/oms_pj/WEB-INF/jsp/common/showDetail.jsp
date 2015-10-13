<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.CwbOrderAll"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CustomWareHouse"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum" %>
<%@page import="cn.explink.enumutil.PaytypeEnum" %>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum" %>
<%@page import="cn.explink.enumutil.DeliveryStateEnum" %>
<%@page import="cn.explink.domain.Exportmould"%>
<%
List<CwbOrderAll> cwborderList = (List<CwbOrderAll>)request.getAttribute("cwborderList");
Page page_obj = (Page)request.getAttribute("page_obj"); 
Map<String,String> comMap = (Map<String,String>)request.getAttribute("comMap");
Map<String,String> stateTimeMap = (Map<String,String>)request.getAttribute("stateTimeMap");
List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
</head>
<script type="text/javascript">
function check(){
	if(<%=page_obj.getTotal() > 0 %>){
		$('#searchForm2').submit();
	}else{
		alert("没有数据，不能导出！");
	}
}

</script>
<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<form action="<%=request.getContextPath()%>/commonemail/exportExcel" method="post" id="searchForm2">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:10px">
	<tr>
		<td align="left">
		
		<input type ="button" id="back" value="返回" class="input_button2" onclick="$('#searchForm1').submit()"/>
		&nbsp;<select name ="exportmould2" id ="exportmould">
		          <option value ="0">默认导出模板</option>
		          <%if(exportmouldlist!=null&&exportmouldlist.size()>0){ for(Exportmould e:exportmouldlist){%>
		           <option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
		          <%}}%>
			</select>
			&nbsp;&nbsp;<input type ="button" id="btnval" value="导出excel" class="input_button2"  onclick="check();"  />
		</td>
	</tr>
	</table>
	<input type="hidden" name="emaildateid" value="<%=request.getAttribute("emaildateid")==null?"0":request.getAttribute("emaildateid")%>" >
	</form>
	<form id="searchForm1" action ="<%=request.getContextPath()%>/commonemail/list/1" method = "post">
	</form>
	</div>
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<div class="right_title">
	<div style="height:20px"></div>
	<div style="overflow-x:scroll; width:100% " id="scroll">
	<table width="1500" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
				<td  align="center" valign="middle" bgcolor="#eef6ff" >订单号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >供货商</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">承运商</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >发货时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >出库时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >接货时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >订单类型</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >当前状态</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >发货仓库</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >入库仓库</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >下一站</td>
				
		</tr>
		<%if(cwborderList != null){ %>
		<% for(CwbOrderAll c : cwborderList){
			String statetime="";
			if(stateTimeMap.containsKey(c.getCwb())){
				if(stateTimeMap.get(c.getCwb()).length()>2){
					statetime=stateTimeMap.get(c.getCwb());
				}
			}
			%>
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle"><a  target="_blank" href="<%=request.getAttribute("dmpUrl")%>/order/queckSelectOrder/<%=c.getCwb() %>"><%=c.getCwb() %></a></td>
					<td  align="center" valign="middle"><%=c.getCustomername() %></td>
					<td  align="center" valign="middle"><%=comMap.get(c.getCommonnumber()) %></td>
					<td  align="center" valign="middle"><%=c.getEmaildate() %></td>
					<td  align="center" valign="middle"><%=c.getOutstoreroomtime() %></td>
					<td  align="center" valign="middle"><%=statetime %></td>
					<td  align="center" valign="middle"><%=c.getOrderType() %></td>
					<td  align="center" valign="middle"><%=FlowOrderTypeEnum.getText((int)c.getFlowordertype())==null?"":FlowOrderTypeEnum.getText((int)c.getFlowordertype()).getText() %></td>
					<td  align="center" valign="middle"><%=c.getCustomerwarehousename() %></td>
					<td  align="center" valign="middle"><%=c.getStartbranchname()%></td>
					<td  align="center" valign="middle"><%=c.getNextbranchname() %></td>
					
				 </tr>
		 <%} }%>
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
	<form  id="searchForm" action="<%=request.getContextPath()%>/commonemail/show/<%=request.getAttribute("emaildateid")%>/1">
	</form>		
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>
</html>




