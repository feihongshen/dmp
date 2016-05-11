<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.GroupDetail"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<GroupDetail> list = request.getAttribute("list")==null?null:(List<GroupDetail>)request.getAttribute("list");
    List<Branch>  branchList = (List<Branch>)request.getAttribute("branchList");
    Page page_obj =(Page)request.getAttribute("page_obj");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>出库交接单打印(按包号)历史记录</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<script type="text/javascript">
$(function() {
	$("#starttime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});

	
});

function bdprint(baleid){
	location.href="<%=request.getContextPath() %>/warehousegroupdetail/outbalelist_print?isprint="+baleid;
}

</script>
</head>
<body style="background:#f5f5f5">
<div class="right_box">
	<div class="inputselect_box">
				<span>
				</span>
				<form action="<%=request.getContextPath() %>/warehousegroupdetail/outbalelist_history/1/${type}" method="post" id="searchForm">
					下一站：<select id ="nextbranchid" name ="nextbranchid"> 
					              <option value ="0">全部</option>
					              <%for(Branch b:branchList){ %>
					                <option value ="<%=b.getBranchid()%>"><%=b.getBranchname() %></option>
					              <%} %>
					            </select>　
					        上次打印时间：从<input type ="text" name ="starttime"  class="input_text1" id ="starttime" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("starttime")) %>"/>&nbsp;到
					              <input type ="text" name= "endtime"  class="input_text1" id ="endtime" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("endtime")) %>"/>
				      　　<input type="submit" id="find" value="查询" class="input_button2" />
				      <a href="<%=request.getContextPath() %>/warehousegroupdetail/outbalelist/1">返回未打印列表 >></a>
				</form>
				</div>
				<div class="right_title">
				<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
					<tr class="font_1">
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">包号(批次号)</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">发往</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">上次打印时间</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">操作类型</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">打印交接单</td>
					</tr>
					<% if(list!=null&&!list.isEmpty()){
					   		for(GroupDetail og : list){ %>
					<tr>
						<td align="center"><%=og.getBaleno()%></td>
						<td align="center">
						 <%for(Branch branch:branchList){if(og.getNextbranchid()==branch.getBranchid()){ %>
							<%=branch.getBranchname() %>
					     <%}} %></td>
						<td align="center"><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(og.getIssignprint())%></td>
						<td align="center"><%for(FlowOrderTypeEnum ft : FlowOrderTypeEnum.values()){if(og.getFlowordertype()==ft.getValue()){ %><%=ft.getText() %><%}} %></td>
						<td align="center">
							<a href ="javascript:;" onclick="bdprint('<%=og.getBaleid()%>')">交接单打印 </a>
							（<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(og.getIssignprint())%>已打印）
						</td>
					</tr>
					<%}}%>
				</table>
				
							<%-- <select name="printtemplateid<%=og.getId() %>" id="printtemplateid<%=og.getId() %>">
					  			<%for(PrintTemplate pt : pList){ %>
					  				<option value="<%=pt.getId()%>"><%=pt.getName() %>（<%if(pt.getTemplatetype()==1){ %>按单<%}else if(pt.getTemplatetype()==2){ %>汇总<%} %>）</option>
					  			<%} %>
							</select>
							<a href ="javascript:;" onclick="bdprint(<%=og.getId() %>,$('#printtemplateid<%=og.getId() %>').val());">交接单打印</a>
							<%if(og.getPrinttime().length()!=0){ %>
							（<%=og.getPrinttime() %>已打印）
							<%} %> --%>
				<div class="jg_10"></div><div class="jg_10"></div>
				</div>
				
				<!--底部翻页 -->
	<div class="jg_35"></div>
	<%if(page_obj.getMaxpage()>1){%>
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
$("#branchid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("branchid"))%>);
</script>
</body>
</html>

