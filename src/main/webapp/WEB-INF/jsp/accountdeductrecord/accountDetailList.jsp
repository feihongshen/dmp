<%@page language="java" import="java.util.*,java.math.BigDecimal" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.Branch,cn.explink.domain.AccountDeducDetail,cn.explink.enumutil.AccountFlowOrderTypeEnum"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="java.text.SimpleDateFormat"%>
<%
	Page page_obj = (Page)request.getAttribute("page_obj");
	List<AccountDeducDetail> recordList=request.getAttribute("recordList")==null?null:(List<AccountDeducDetail>)request.getAttribute("recordList");
	long  recordid=Long.parseLong(request.getParameter("recordid")==null?"0":request.getParameter("recordid"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>账户订单明细</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script language="javascript">
$(function(){
	var $menuli = $(".kfsh_tabbtn ul li");
	var $menulilink = $(".kfsh_tabbtn ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
})

function exportField(){
	$("#clickExport").attr("disabled","disabled");
	$("#clickExport").val("请稍后");
 	$("#exportForm").submit(); 
}
</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="saomiao_box2">
	<div class="inputselect_box" style="top: 0px ">
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
		<input type="button" class="input_button1" id="clickExport"  onclick="exportField();" value="导出Excel" />
			<input type="hidden" name="recordid" id="recordid" value="<%=recordid%>"/>
		</form>
	</div>
	<div style="height:35px"></div>
		<table width="100%" border="0" cellspacing="10" cellpadding="0">
			<tr>
				<td colspan="2" valign="top"><table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" id="gd_table" >
						<tr class="font_1">
							<td bgcolor="#e7f4e3">站点</td>
							<td bgcolor="#e7f4e3">订单号</td>
							<td bgcolor="#e7f4e3">类型</td>
							<td bgcolor="#e7f4e3">交易金额[元]</td>
							<td bgcolor="#e7f4e3">备注</td>
							<td bgcolor="#e7f4e3">操作时间</td>
							<td bgcolor="#e7f4e3">操作人</td>
						</tr>
						<%if(recordList!=null&&!recordList.isEmpty()){
							for(AccountDeducDetail list:recordList){
						%>
						<tr>
							<td><%=list.getBranchname()%></td>
							<td><%=list.getCwb()%></td>
							<td><%for(AccountFlowOrderTypeEnum ft : AccountFlowOrderTypeEnum.values()){
									if(list.getFlowordertype()==ft.getValue()){ 
										if(list.getFlowordertype()==AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue()){
											out.print("中转");
										}else{
											out.print(ft.getText());
										}
								}} 
								%></td>
							<td align="right"><strong><%=list.getFee()%></strong></td>
							<td><%=list.getMemo()%></td>
							<td><%=list.getCreatetime()%></td>
							<td><%=list.getUsername()%></td>
						</tr>
						<%}}else{%>
						<tr>
							<td colspan="10">暂无数据</td>
						</tr>
						<%} %>
					</table></td>
			</tr>
		</table>
		<!--翻页 -->
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

<form action="<%=request.getContextPath() %>/accountdeductrecord/exportByRecord" method="post" id="exportForm">
	<input type="hidden" name="recordid" id="recordid" value="<%=recordid%>"/>
</form>
<script type="text/javascript">
//站点下拉框赋值
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>
</html>