<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.Common"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.util.Page"%>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%

  List<CwbOrder> orderlist = (List<CwbOrder>)request.getAttribute("cwbdetaillist");
  Page page_obj = (Page)request.getAttribute("page_obj");

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单详情</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script>
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
</script>
</head>
<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
<body style="background:#eef9ff">
<div style="overflow-x: scroll; " id="scroll2">
	<table width="1500" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
			    <td width="50" align="center" valign="middle" bgcolor="#eef6ff"  >序号</td>
				<td width="80" align="center" valign="middle" bgcolor="#eef6ff" >供货商</td>
				<td width="100" align="center" valign="middle" bgcolor="#eef6ff"  >订单号</td>
				<td width="100" align="center" valign="middle" bgcolor="#eef6ff" >收件人</td>
				<td width="" align="center" valign="middle" bgcolor="#eef6ff" >收件地址</td>
				<td width="120" align="center" valign="middle" bgcolor="#eef6ff"  >电话 </td>
				<td width="120" align="center" valign="middle" bgcolor="#eef6ff"  >手机</td>
				<td width="120" align="center" valign="middle" bgcolor="#eef6ff"  >邮编</td>
				<td width="120" align="center" valign="middle" bgcolor="#eef6ff" >货物金额</td>
				
				<td width="120" align="center" valign="middle" bgcolor="#eef6ff">应退金额</td>
				<td width="120" align="center" valign="middle" bgcolor="#eef6ff">当前状态</td>
		</tr>
		<% 
		long k=0;
		for(CwbOrder c : orderlist){ 
			k++;
			String flowtypename="";
			long flowtypeid=c.getFlowordertype();
				for (FlowOrderTypeEnum f : FlowOrderTypeEnum.values()) {
		  			if (f.getValue()==flowtypeid) {
		     			flowtypename =f.getText();
		     			break;
		     		}
		     	}
				int isbackflag=0;
		%>
				<tr bgcolor="#FF3300">
					<td align="center" valign="middle"><%=k%></td>
					<td align="center" valign="middle"><%=c.getCustomername()%></td>
					<td align="center" valign="middle"><%=c.getCwb()%></td>
					<td align="center" valign="middle"><%=c.getConsigneename()%></td>
					<td align="left" valign="middle"><%=c.getConsigneeaddress()%></td>
					<td align="center" valign="middle"><%=c.getConsigneephone()%></td>
					<td align="center" valign="middle"><%=c.getConsigneemobile()%></td>
					<td align="center" valign="middle"><%=c.getConsigneepostcode()%></td>
					<td align="right" valign="middle"><%=c.getCaramount()%></td>
					<td align="right" valign="middle"><%=c.getReceivablefee()%></td>
					
					<td align="center" valign="middle"><%=flowtypename%></td>
				 </tr>
		 <%} %>
	</table>
	</div>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
	<center>
	<input type ="button"  value="导出excel" class="input_button2" onclick="excelExp();"/>&nbsp;&nbsp;<input type ="button"  value="返回上级" class="input_button2" onclick="back();"/>
	</center>
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
			
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#datetype").val(<%=request.getParameter("datetype")==null?"1":request.getParameter("datetype")%>);
</script>

<script type="text/javascript">

function excelExp(){
	window.location.href="<%=request.getContextPath()%>/funds/searchdetail_back_excel";
	return true;
}
function back(){
	window.location.href="<%=request.getContextPath()%>/funds/payment_back_return";
	return true;
}

</script>
<!-- 标记ajax地址 -->
<input type="hidden" id="updateMack_url" value="<%=request.getContextPath()%>/order/updateMack" />
<!-- 操作日志的ajax地址 -->
<input type="hidden" id="view" value="<%=request.getContextPath()%>/order/makelog/" />
</body>
</form>
</html>

