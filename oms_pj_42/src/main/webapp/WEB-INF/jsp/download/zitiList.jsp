<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%
List<CwbOrder> cwborderList = request.getAttribute("zitiList")==null?new ArrayList<CwbOrder>():(List<CwbOrder>)request.getAttribute("zitiList");
Page page_obj = (Page)request.getAttribute("page_obj"); 
List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
long count = (Long)request.getAttribute("count");
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
<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<%if(!cwborderList.isEmpty()){ %>
			<select name ="exportmould" id ="exportmould">
	          <option value ="0">默认导出模板</option>
	          <%for(Exportmould e:exportmouldlist){%>
	           <option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
	          <%} %>
			</select><%} %>
			<%if(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)==1){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval0" value="导出1-<%=count %>" class="input_button1" onclick="exportField('0','0');"/>
			<%}else{for(int j=0;j<count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0);j++){ %>
				<%if(j==0){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval<%=j %>" value="导出1-<%=((j+1)*Page.EXCEL_PAGE_NUMBER)/10000.0 %>万" class="input_button1" onclick="exportField('0','<%=j%>');"/>
				<%}else if(j!=0&&j!=(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)-1)){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval<%=j %>" value="导出<%=j*Page.EXCEL_PAGE_NUMBER+1 %>-<%=((j+1)*Page.EXCEL_PAGE_NUMBER)/10000.0 %>万" class="input_button1" onclick="exportField('<%=j*Page.EXCEL_PAGE_NUMBER %>','<%=j%>');"/>
				<%}else if(j==(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)-1)){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval<%=j %>" value="导出<%=j*Page.EXCEL_PAGE_NUMBER+1 %>万-<%=count %>" class="input_button1" onclick="exportField('<%=j*Page.EXCEL_PAGE_NUMBER %>','<%=j%>');"/>
				<%} %>
			<%}} %>
	</div>
	<div class="right_title">
	<div style="height:20px"></div>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2"  id="tableList">
	   <tr class="font_1">
				<td  align="center" valign="middle" bgcolor="#eef6ff" >订单号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >发货时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >到站时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >收件人</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >收件人电话</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >代收款</td>
		</tr>
		<% for(CwbOrder c : cwborderList){ %>
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle"><%=c.getCwb() %></td>
					<td  align="center" valign="middle"><%=c.getEmaildate() %></td>
					<td  align="center" valign="middle"><%=c.getInSitetime() %></td>
					<td  align="center" valign="middle"><%=c.getConsigneename() %></td>
					<td  align="center" valign="middle"><%=c.getConsigneemobile() %></td>
					<td  align="center" valign="middle"><%=c.getReceivablefee() %></td>
				 </tr>
		 <%} %>
	</table>
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
	<form  id="searchForm" action="<%=request.getContextPath()%>/download/zitilist/1">
	</form>		
	<div class="jg_10"></div>

	<div class="clear"></div>
<form action="<%=request.getContextPath()%>/datastatistics/exportExcle" method="post" id="searchForm2">
		<input type="hidden" name="branchid" id="count" value="<%=page_obj.getTotal() %>"/>
		<input type="hidden" name="begin" id="begin" value="1"/>
		<input type="hidden" name="sign" id="sign" value="10000"/>
</form>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>

<script type="text/javascript">
function exportField(page,j){
	if($("#isshow").val()=="1"&&<%=cwborderList != null && cwborderList.size()>0  %>){
		$("#exportmould2").val($("#exportmould").val());
		$("#btnval"+j).attr("disabled","disabled");
		$("#btnval"+j).val("请稍后……");
		$("#begin").val(page);
	 	$.ajax({
			type: "POST",
			url:$("#searchForm2").attr("action"),
			data:$("#searchForm2").serialize(),
			dataType:"json",
			success : function(data) {
				if(data.errorCode==0){
					alert("已进入导出任务队列！");
					$.ajax({
						type: "POST",
						url:"<%=request.getContextPath()%>/datastatistics/commitExportExcle",
						data:$("#searchForm2").serialize(),
						dataType:"json",
						success : function(data) {
							
						}
					});
				}else{
					alert(data.remark);
				}
			}
		});
	}else{
		alert("没有做查询操作，不能导出！");
	}
}

</script>
</body>
</html>




