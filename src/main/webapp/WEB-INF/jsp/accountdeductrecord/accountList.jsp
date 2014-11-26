<%@page language="java" import="java.util.*,java.math.BigDecimal" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.Branch,cn.explink.domain.AccountDeductRecord,cn.explink.enumutil.AccountFlowOrderTypeEnum"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="java.text.SimpleDateFormat"%>
<%
	Page page_obj = (Page)request.getAttribute("page_obj");
	String strtime=StringUtil.nullConvertToEmptyString(request.getAttribute("strtime"));
	String endtime=StringUtil.nullConvertToEmptyString(request.getAttribute("endtime")); 
	List<AccountDeductRecord> recordList=request.getAttribute("recordList")==null?null:(List<AccountDeductRecord>)request.getAttribute("recordList");
	long branchid=Long.parseLong(request.getParameter("branchid")==null?"0":request.getParameter("branchid").toString());
	long recordtype=Long.parseLong(request.getParameter("recordtype")==null?"0":request.getParameter("recordtype").toString());
	List<Branch> branchList=request.getAttribute("branchList")==null?null:(List<Branch>)request.getAttribute("branchList");
	Branch branch=request.getAttribute("branch")==null?new Branch():(Branch)request.getAttribute("branch");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>账户加减款明细</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
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
	
	$("#strtime").datepicker();
	$("#endtime").datepicker();
	
	$("#selectBranchid").val(<%=branchid%>);
	$("#recordtype").val(<%=recordtype%>);
	//财务选择站点
	$("#selectBranchid").change(function(){ 
		$("#branchid").val(this.value);
	});
})

function searchBtn(){
	if($("#strtime").val()>$("#endtime").val()){
		alert("开始时间要小于结束时间");
		return false;
	}
	$('#searchForm').attr('action',1);
	$("#searchForm").submit();
}

function returnBtn(){
	location.href="<%=request.getContextPath()%>/accountdeductrecord/accountBasic/1?branchid=<%=branchid%>";
	
}

//明细
function detailBtn(recordid){
	window.open("<%=request.getContextPath()%>/accountdeductrecord/detailByRecordList/1?recordid="+recordid);
}

function exportField(){	
	$("#clickExport").attr("disabled","disabled");
	$("#clickExport").val("请稍后");
 	$("#exportForm").submit(); 
}
</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="saomiao_box2">
	<div>
		<div class="inputselect_box" style="top: 0px ">
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
		&nbsp;
			站点:<%if(branchList!=null&&!branchList.isEmpty()){%>
				<select id=selectBranchid name=selectBranchid style="width:150px;">	
					<%for(Branch b: branchList){%>
						<option value="<%=b.getBranchid()%>"><%=b.getBranchname()%></option>
					<%}%>	
			    </select>
			 <%}else{
			 	out.print(StringUtil.nullConvertToEmptyString(branch.getBranchname()));
			 } %>&nbsp;&nbsp;
		类型
			<select id="recordtype" name="recordtype" style="width:150px;">	
				<option value="0">====请选择====</option>
				<option value="8">扣款</option>
				<option value="11">中转</option>
				<option value="12">退货</option>
				<option value="13">POS</option>
				<option value="10">充值</option>
				<option value="14">调账</option>
		    </select>
		<label for="select"></label>
		交易时间：
		<input type="text" name="strtime" id="strtime" value ="<%=strtime%>"/>
		至
		<input type="text" name="endtime" id="endtime" value ="<%=endtime%>"/>
		<input type="hidden" name="branchid" id="branchid" value="<%=branchid%>"/>
		<input type="button" onclick="searchBtn()" id="find" value="查询" class="input_button2" />
		<input onclick="returnBtn()" type="button" class="input_button2" value="返回" />
		<input onclick="exportField()" id="clickExport" type="button" class="input_button2" value="导出Excel" />
		<!-- <input type="button" class="input_button1" id="clickExport"  onclick="exportField();" value="导出Excel" /> -->
		</form>
	</div>
	<div style="height:35px"></div>
		<table width="100%" border="0" cellspacing="10" cellpadding="0">
			<tr>
				<td colspan="2" valign="top"><table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" id="gd_table" >
						<tr class="font_1">
							<td bgcolor="#f4f4f4">类型</td>
							<td bgcolor="#f4f4f4">订单号</td>
							<td bgcolor="#f4f4f4">交易金额[元]</td>
							<td bgcolor="#f4f4f4">交易前余额[元]</td>
							<td bgcolor="#f4f4f4">交易后余额[元]</td>
							<td bgcolor="#f4f4f4">交易前欠款[元]</td>
							<td bgcolor="#f4f4f4">交易后欠款[元]</td>
							<td bgcolor="#f4f4f4">备注</td>
							<td bgcolor="#f4f4f4">操作人</td>
							<td bgcolor="#f4f4f4">交易时间</td>
							<td bgcolor="#f4f4f4">操作</td>
						</tr>
						<%if(recordList!=null&&!recordList.isEmpty()){
							for(AccountDeductRecord list:recordList){
						%>
						<tr>
							<td><%for(AccountFlowOrderTypeEnum ft : AccountFlowOrderTypeEnum.values()){if(list.getRecordtype()==ft.getValue()){ %><%=ft.getText() %><%}} %></td>
							<td align="center"><%="".equals(list.getCwb())?"--":list.getCwb()%></td>
							<td align="right"><strong><%=list.getFee()%></strong></td>
							<td align="right"><strong><%=list.getBeforefee()%></strong></td>
							<td align="right"><strong><%=list.getAfterfee()%></strong></td>
							<td align="right"><strong><%=list.getBeforedebt()%></strong></td>
							<td align="right"><strong><%=list.getAfterdebt()%></strong></td>
							<td align="center"><%=list.getMemo()%></td>
							<td align="center"><%=list.getUsername()%></td>
							<td align="center"><%=list.getCreatetime()%></td>
							<td bgcolor="#f4f4f4">
							<%if(list.getRecordtype()!=AccountFlowOrderTypeEnum.TiaoZhang.getValue()&&list.getRecordtype()!=AccountFlowOrderTypeEnum.ChongZhi.getValue()&&list.getRecordtype()!=AccountFlowOrderTypeEnum.KouKuan.getValue()){%>
							<a href="#" onclick="detailBtn('<%=list.getRecordid()%>')">[明细]<a>
							<%}%>
							</td>
						</tr>
						<%}}else{%>
						<tr>
							<td colspan="11">暂无数据</td>
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
<form action="<%=request.getContextPath() %>/accountdeductrecord/exportAccountList" method="post" id="exportForm">
	<input type="hidden" name="branchid" id="branchid" value="<%=branchid%>"/>
	<input type="hidden" name="recordtype" id="recordtype" value="<%=recordtype%>"/>
	<input type="hidden" name="strtime" id="strtime" value="<%=strtime%>"/>
	<input type="hidden" name="endtime" id="endtime" value="<%=endtime%>"/>
</form>
<script type="text/javascript">
//站点下拉框赋值
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>
</html>