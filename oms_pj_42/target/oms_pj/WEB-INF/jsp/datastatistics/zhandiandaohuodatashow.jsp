<%@page import="cn.explink.enumutil.ModelEnum"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Branch"%>
<%
List<CwbOrder> orderlist = request.getAttribute("orderlist")==null?new ArrayList<CwbOrder>():(List<CwbOrder>)request.getAttribute("orderlist");
Page page_obj = (Page)request.getAttribute("page_obj"); 

String starttime=request.getParameter("begindate")==null?DateTimeUtil.getDateBefore(1):request.getParameter("begindate");
String endtime=request.getParameter("enddate")==null?DateTimeUtil.getNowTime():request.getParameter("enddate");
Long branchid = request.getParameter("branchid")==null?0:Long.parseLong(request.getParameter("branchid"));
List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
function gologin(){
	if(<%=request.getAttribute("nouser") != null%>){
		alert("登录已失效，请重新登录！");
	}
}
</script>
</head>
<body style="background:#eef9ff" onload="gologin();">
<div class="right_box">
	<div class="inputselect_box">
	<form id="searchForm2" action ="<%=request.getContextPath()%>/datastatistics/exportExcle" method = "post" > 
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:10px">
			<tr>
				<td align="left">
				<input type ="button" id="back" value="返回" class="input_button2" onclick="location.href='<%=request.getContextPath() %>/datastatistics/zhandiandaohuodata?begindate=<%=starttime %>&enddate=<%=endtime %>&isshow=1'"/>
				<select name ="exportmould" id ="exportmould">
		          <option value ="0">默认导出模板</option>
		          <%for(Exportmould e:exportmouldlist){%>
		           <option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
		          <%} %>
				</select>
				<input name="branchid" id="branchid" value="<%=branchid %>" type="hidden"/>
				<input name="begindate" value="<%=starttime %>" type="hidden"/>
				<input name="enddate" value="<%=endtime %>" type="hidden"/>
				<input type="hidden" name="count" id="count1" value="<%=page_obj.getTotal() %>"/>
				<input type="hidden" name="sign" id="sign" value="<%=ModelEnum.ZhanDianDaoHuohuizong.getValue() %>"/>
				&nbsp;&nbsp;<input type ="button" id="btnval0" value="导出" class="input_button1" onclick="exportField();"/>
			</td>
			</tr>
			</table>
	</form>
	</div>
	<div class="right_title">
	<div style="height:40px"></div>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
				<td  align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">到货时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">配送站点</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">当前状态</td>
		</tr>
		<%if(orderlist!=null) for(CwbOrder c : orderlist){ %>
			<tr bgcolor="#FF3300">
				<td  align="center" valign="middle"><a  target="_blank" href="<%=request.getAttribute("dmpUrl")%>/order/queckSelectOrder/<%=c.getCwb() %>"><%=c.getCwb() %></a></td>
				<td  align="center" valign="middle"><%=c.getCustomername() %></td>
				<td  align="center" valign="middle"><%=c.getInSitetime() %></td>
				<td  align="center" valign="middle"><%=c.getDeliverybranch() %></td>
				<td  align="center" valign="middle"><%=c.getFlowordertypeMethod() %></td>
			 </tr>
		 <%} %>
	</table>
	<div class="jg_10"></div>
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
	<form  id="searchForm" action="<%=request.getContextPath() %>/datastatistics/customerfahuodatashow/1" method="post">
		<input name="branchid" value="<%=branchid %>" type="hidden"/>
		<input name="begindate" value="<%=starttime %>" type="hidden"/>
		<input name="enddate" value="<%=endtime %>" type="hidden"/>
	</form>
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
function exportField(){
	if(<%=orderlist != null && orderlist.size()>0  %>){
		$("#exportmould2").val($("#exportmould").val());
		$("#btnval").attr("disabled","disabled");
		$("#btnval").val("请稍后……");
		$.ajax({
			type: "POST",
			url:$("#searchForm2").attr("action"),
			data:$("#searchForm2").serialize(),
			dataType:"json",
			success : function(data) {
				if(data.errorCode==0){
					alert("已进入导出任务队列！");
					$("#downCount",parent.document).html(Number($("#downCount",parent.document).html())+1);
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