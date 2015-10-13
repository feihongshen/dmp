<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<CwbOrder> backgoodslist = (List<CwbOrder>)request.getAttribute("backgoodslist");
  Page page_obj = (Page)request.getAttribute("page_obj");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>退货批量审核</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">

$(document).ready(function() {
	$("#seach1").click(function(){
		$("#typeID").val(0);
		$("#searchForm").submit();
	});
	$("#seach2").click(function(){
		$("#typeID").val(1);
		$("#searchForm").submit();
	});
	$("#seach3").click(function(){
		$("#typeID").val(2);
		$("#searchForm").submit();
	});
});
function delSuccess(data){
	$("#searchForm").submit();
}

function orderForm(ordername){
    
	if($("#orderByTypeId").val()=="ASC"){
    	$("#orderByNameId").val(ordername);
    	$("#orderByTypeId").val("DESC");
    }else {
    	$("#orderByNameId").val(ordername);
    	$("#orderByTypeId").val("ASC");
    }
	$("#searchForm").submit();
	
}

function audit(cwb){
	var type = "0";
	if($("#auditstate_"+cwb).html()=="审核"){
		type ="1";
	}
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/order/audit/"+cwb,
		data:{auditstate:type},
		dataType:"json",
		success : function(data) {
			if(data.errorCode==0){
				alert(data.error);
				if($("#auditstate_"+cwb).html()=="审核"){
					$("#auditstate_"+cwb).html("撤销审核");
				}else{
					$("#auditstate_"+cwb).html("审核");
				}
			}else{
				alert(data.error);
			}
		}
	});
}
</script>
</head>

<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box" style="height:89px">
		<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
			
			<input type="hidden" id="orderByNameId" name="orderByName" value="shiptime"/>
			<input type="hidden" id="orderByTypeId" name="orderByType" value="<%=request.getParameter("orderByType")==null?"DESC":request.getParameter("orderByType") %>"/>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="600" rowspan="3" valign="top" >
					   订单号/运单号：<textarea rows="4" cols="35" id ="cwbandtranscwb"  name ="cwbandtranscwb"><%=request.getParameter("cwbandtranscwb")==null?"":request.getParameter("cwbandtranscwb") %></textarea>
					</td>
					<td align="left">
					 <input type="button"  value="查询" class="input_button1"  id="seach1"/>
					</td>
				</tr>
				<tr>
					<td align="left">
					 <input type="button"  value="批量退货审核"  id="seach2" class="input_button1"/>
					</td>
				</tr>
				<tr>
					<td align="left">
					 <input type="button"  value="批量撤销审核" class="input_button1" id="seach3"/>
					 <input type="hidden" name="typeName" value="0" id="typeID"/>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="height:89px">&nbsp;</div>
<form method ="post" action ="<%=request.getContextPath()%>/order/batch"  id ="form1">
	<div class="right_title">
		<div class="list_topbar2">
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
				<tr class="font_1">
					<td width="5%" align="center" valign="middle" bgcolor="#eef6ff"  onclick="orderForm('auditstate');" >是否审核</td>     
					<td width="7%" align="center" valign="middle" bgcolor="#eef6ff"   onclick="orderForm('flowordertype');">快件状态</td>  
					<td width="8%" align="center" valign="middle" bgcolor="#eef6ff"   onclick="orderForm('customerid');">供货商</td>      
					<td width="8%" align="center" valign="middle" bgcolor="#eef6ff"   onclick="orderForm('commonnumber');">承运商</td>    
					<td width="20%" align="center" valign="middle" bgcolor="#eef6ff"   onclick="orderForm('cwbremark');">备注</td>         
					<td width="10%" align="center" valign="middle" bgcolor="#eef6ff"   onclick="orderForm('cwb');">订单号</td>              
					<td width="10%" align="center" valign="middle" bgcolor="#eef6ff"   onclick="orderForm('transcwb');">运单号</td>         
					<td width="12%" align="center" valign="middle" bgcolor="#eef6ff"   onclick="orderForm('shiptime');">发货时间</td>         
					<td width="5%" align="center" valign="middle" bgcolor="#eef6ff"   onclick="orderForm('receivablefee');">代收款</td>    
					<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
				</tr>
			</table>
		</div>
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
			<tr class="font_1">
				<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">是否审核</td>
				<td width="7%" align="center" valign="middle" bgcolor="#eef6ff">快件状态</td>
				<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
				<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">承运商</td>
				<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">备注</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">运单号</td>
				<td width="12%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
				<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">代收款</td>
				<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
			</tr>
		<% for(CwbOrder c : backgoodslist){ %>
			<tr>
				<td align="center" valign="middle"><%=c.getAuditstate()==0?"未审核":"已审核"%></td>
				<td align="center" valign="middle"><%=c.getFlowordertypeMethod()%></td>
				<td align="center" valign="middle"><%=c.getCustomername()%></td>
				<td align="center" valign="middle"><%=c.getCommonname()%></td>
				<td align="center" valign="middle"><%if(c.getCwbremark().length()>50){%>……<%=c.getCwbremark().substring(c.getCwbremark().length()-50, c.getCwbremark().length())%><%} else{%><%=c.getCwbremark()%><%} %></td>
				<td align="center" valign="middle"><%=c.getCwb()%></td>
				<td align="center" valign="middle"><%=c.getTranscwb()%></td>
				<td align="center" valign="middle"><%=c.getEmaildate()%></td>
				<td align="center" valign="middle"><%=c.getReceivablefee()%></td>
				<td align="center" valign="middle" >[<a id="auditstate_<%=c.getCwb()%>" href ="javascript:audit('<%=c.getCwb()%>')"><%=c.getAuditstate()==0?"审核":"撤销审核" %></a>]
				[<a href ="javascript:getViewBox('<%=c.getCwb()%>')">操作日志</a>]</td>
			</tr>
		<%} %>
		</table>
	</div>
	</form>
	<div class="jg_30"></div>
	<%if(page_obj.getMaxpage()>1){ %>
	<div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr >
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff"><a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
					　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
							id="selectPg"
							onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
							<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
							<option value="<%=i %>"><%=i %></option>
							<% } %>
						</select>页</td>
			</tr>
		</table>
	</div>
	<%} %>
</div>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
<!-- 操作日志的ajax地址 -->
<input type="hidden" id="view" value="<%=request.getContextPath()%>/order/makelog/" />
</body>

</html>

