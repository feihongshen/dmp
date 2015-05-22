<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.Branch,cn.explink.domain.AccountCwbSummary"%>
<%@page import="cn.explink.enumutil.AccountTongjiEnum,cn.explink.enumutil.AccountFlowOrderTypeEnum"%>
<%@page import="cn.explink.util.Page,cn.explink.util.StringUtil"%>
<% 
	Map usermap = (Map) session.getAttribute("usermap");
	long userbranchid=Long.parseLong(usermap.get("branchid").toString());
	List<Branch> branchList=request.getAttribute("branchList")==null?null:(List<Branch>)request.getAttribute("branchList");
	List<AccountCwbSummary> summaryList=request.getAttribute("summaryList")==null?null:(List<AccountCwbSummary>)request.getAttribute("summaryList");
	Page page_obj = (Page)request.getAttribute("page_obj");
	String checkoutstate=request.getParameter("checkoutstate")==null?"0":request.getParameter("checkoutstate");
	String starttime=StringUtil.nullConvertToEmptyString(request.getParameter("starttime"));
	String endtime=StringUtil.nullConvertToEmptyString(request.getParameter("endtime"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>配送结果结算审核列表</title>
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
	$("#starttime").datepicker();
	$("#endtime").datepicker();
	getCheckTime();
});

function checkBtn(summaryid){
	location.href="<%=request.getContextPath()%>/accountcwbdetail/getDelivery/"+summaryid;
}

function detailCwbs(flowOrderType,nums,summaryid){
	if(nums!=0){
		window.open("<%=request.getContextPath()%>/accountcwbdetail/deliverycwbs/1?summaryid="+summaryid+"&flowOrderType="+flowOrderType);
	}
}

function getCheckTime(){
	if($("#checkoutstate").val()==0){
		$("#checkTimeView").html("&nbsp;&nbsp;提交时间：");
	}else{
		$("#checkTimeView").html("&nbsp;&nbsp;审核时间：");
	}
}


function exportField(){
	$("#branchidExcel").val($("#branchid").val());
	$("#checkoutstateExcel").val($("#checkoutstate").val());
	$("#starttimeExcel").val($("#starttime").val());
	$("#endtimeExcel").val($("#endtime").val());
	
	$("#clickExport").attr("disabled","disabled");
	$("#clickExport").val("请稍后");
 	$("#exportForm").submit(); 
}
</script>
</head>
<body style="background:#fff" marginwidth="0" marginheight="0">
<div class="inputselect_box" style="top: 0px ">
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
		&nbsp;&nbsp;站点：
		<select id="branchid" name="branchid" style="width:150px;">	
			<option value="0">==全部==</option>
			<%if(!branchList.isEmpty()){
				for(Branch b: branchList){%>
				<option value="<%=b.getBranchid()%>"><%=b.getBranchname()%></option>
			<%}}%>	
	    </select>
	    &nbsp;&nbsp;审核状态：
		<select name="checkoutstate" id="checkoutstate" onchange="getCheckTime()">
			<option value="0" <%=("0").equals(checkoutstate)?"selected":"" %>>待审核</option>
			<option value="1" <%=("1").equals(checkoutstate)?"selected":"" %>>已审核</option>
		</select>
		<font id="checkTimeView">
			&nbsp;&nbsp;审核时间：
		</font>	
			<input type="text" name="starttime" id="starttime" value ="<%=starttime%>"/>
			到<input type="text" name="endtime" id="endtime" value ="<%=endtime%>"/>
		<input type="submit" onclick="$('#searchForm').attr('action',1);return true;" id="find" value="查询" class="input_button2" />
		<%if(summaryList!=null&&!summaryList.isEmpty()){%>
			<input type="button" class="input_button2" id="clickExport"  onclick="exportField();" value="导出Excel" />
		<%}%>
	</form>
</div>
<div style="height:35px"></div>
<form action="" method="get">
	<div style="overflow-x:scroll; width:100%">
	<table width="2500" border="0" cellspacing="1" cellpadding="2" class="table_2" >
		<tr class="font_1">
			<td colspan="2" bgcolor="#f4f4f4">&nbsp;</td>
			<td colspan="2" bgcolor="#f4f4f4">冲减款</td>
			
			<td colspan="8" bgcolor="#f4f4f4">&nbsp;</td>
			
			<td colspan="12" bgcolor="#f4f4f4">&nbsp;</td>
			
			<td colspan="5" bgcolor="#f4f4f4">&nbsp;</td>
		</tr>
		<tr class="font_1">
			<td colspan="2" bgcolor="#f4f4f4">&nbsp;</td>
			<td bgcolor="#f4f4f4">加款</td>
			<td bgcolor="#f4f4f4">减款</td>
			
			<td colspan="2" bgcolor="#f4f4f4">欠款</td>
			<td colspan="6" bgcolor="#f4f4f4">本次应交</td>
			
			<td colspan="6" bgcolor="#f4f4f4">本次实交</td>
			<td colspan="6" bgcolor="#f4f4f4">本次欠款</td>
			<td colspan="4" bgcolor="#f4f4f4">&nbsp;</td>
			</tr>
		<tr>
			<td bgcolor="#f4f4f4">站点</td>
			<td bgcolor="#f4f4f4">结算类型</td>
			<td bgcolor="#f4f4f4">金额（元）</td>
			<td bgcolor="#f4f4f4">金额（元）</td>
			
			<td bgcolor="#f4f4f4">订单数</td>
			<td bgcolor="#f4f4f4">金额（元）</td>
			
			<td bgcolor="#f4f4f4">订单数</td>
			<td bgcolor="#f4f4f4">现金（元）</td>
			<td bgcolor="#f4f4f4">POS（元）</td>
			<td bgcolor="#f4f4f4">支票（元）</td>
			<td bgcolor="#f4f4f4">其他（元）</td>
			<td bgcolor="#f4f4f4">合计（元）</td>
			
			<td bgcolor="#f4f4f4">订单数</td>
			<td bgcolor="#f4f4f4">现金（元）</td>
			<td bgcolor="#f4f4f4">POS（元）</td>
			<td bgcolor="#f4f4f4">支票（元）</td>
			<td bgcolor="#f4f4f4">其他（元）</td>
			<td bgcolor="#f4f4f4">合计（元）</td>
			
			<td bgcolor="#f4f4f4">订单数</td>
			<td bgcolor="#f4f4f4">现金（元）</td>
			<td bgcolor="#f4f4f4">POS（元）</td>
			<td bgcolor="#f4f4f4">支票（元）</td>
			<td bgcolor="#f4f4f4">其他（元）</td>
			<td bgcolor="#f4f4f4">合计（元）</td>
			
			<td bgcolor="#f4f4f4">提交人</td>
			<td bgcolor="#f4f4f4">提交时间</td>
			<td bgcolor="#f4f4f4">备注</td>
			<td bgcolor="#f4f4f4">操作</td>
		</tr>
		<%
			if(summaryList!=null&&!summaryList.isEmpty()){
				for(AccountCwbSummary list:summaryList){
		%>
		<tr>
			<td><%=list.getBranchname()%></td>
			<td><%=list.getAccounttype()==1?"买单结算":"配送结果结算"%></td>
			<td align="right"><strong><%=list.getOtheraddfee()%></strong></td>
			<td align="right"><strong><%=list.getOthersubtractfee()%></strong></td>
			
			<td><a href="#" onclick="detailCwbs('<%=AccountTongjiEnum.QianKuan.getValue()%>','<%=list.getQknums()%>','<%=list.getSummaryid()%>')"><%=list.getQknums()%></a></td>
			<td><strong><%=list.getQkcash()%></strong></td>
			<td><a href="#" onclick="detailCwbs('<%=AccountTongjiEnum.ToYingJiao.getValue()%>','<%=list.getTonums()%>','<%=list.getSummaryid()%>')"><%=list.getTonums()%></a></td>
			<td align="right"><strong><%=list.getTocash()%></strong></td>
			<td align="right"><strong><%=list.getTopos()%></strong></td>
			<td align="right"><strong><%=list.getTocheck()%></strong></td>
			<td align="right"><strong><%=list.getToother()%></strong></td>
			<td align="right"><strong><%=list.getTofee()%></strong></td>
			
			<td><a href="#" onclick="detailCwbs('<%=AccountTongjiEnum.YingJiao.getValue()%>','<%=list.getYjnums()%>','<%=list.getSummaryid()%>')"><%=list.getYjnums()%></a></td>
			<td align="right"><strong><%=list.getYjcash()%></strong></td>
			<td align="right"><strong><%=list.getYjpos()%></strong></td>
			<td align="right"><strong><%=list.getYjcheck()%></strong></td>
			<td align="right"><strong><%=list.getYjother()%></strong></td>
			<td align="right"><strong><%=list.getYjfee()%></strong></td>
			
			<td><a href="#" onclick="detailCwbs('<%=AccountTongjiEnum.WeiJiao.getValue()%>','<%=list.getWjnums()%>','<%=list.getSummaryid()%>')"><%=list.getWjnums()%></a></td>
			<td align="right"><strong><%=list.getWjcash()%></strong></td>
			<td align="right"><strong><%=list.getWjpos()%></strong></td>
			<td align="right"><strong><%=list.getWjcheck()%></strong></td>
			<td align="right"><strong><%=list.getWjother()%></strong></td>
			<td align="right"><strong><%=list.getWjfee()%></strong></td>
			
			<%-- <td><strong><%=list.getHjfee()%></strong></td> --%>
			<td><%=list.getUsername()%></td>
			<td><%if("0".equals(checkoutstate)){out.print(list.getSavecreatetime());}else{out.print(list.getCreatetime());}%></td>
			<td><%=list.getMemo()==null?"":list.getMemo()%></td>
			<td><a href="#" onclick="checkBtn(<%=list.getSummaryid()%>)">
				&nbsp;&nbsp;
				<%if(userbranchid==list.getBranchid()){
					out.print("[明细]");
				}else{if("0".equals(checkoutstate)){out.print("[审核]");}else{out.print("[明细]");}}%>
				
				
			</a></td>
		</tr>
		<%}}%>
		
	</table>
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
</form>
<script type="text/javascript">
//站点下拉框赋值
$("#branchid").val("<%=request.getParameter("branchid")%>");
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>

<form action="<%=request.getContextPath() %>/accountcwbdetail/exportByDeliverySummary" method="post" id="exportForm">
	<input type="hidden" id="branchidExcel" name="branchidExcel" value=""/>
	<input type="hidden" id="checkoutstateExcel" name="checkoutstateExcel" value=""/>
	<input type="hidden" id="starttimeExcel" name="starttimeExcel" value=""/>
	<input type="hidden" id="endtimeExcel" name="endtimeExcel" value=""/>
</form>
</body>
</html>