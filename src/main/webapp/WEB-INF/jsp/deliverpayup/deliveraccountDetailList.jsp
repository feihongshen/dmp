<%@page import="cn.explink.domain.FinanceDeliverPayupDetail"%>
<%@page import="cn.explink.enumutil.FinanceDeliverPayUpDetailTypeEnum"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.enumutil.UserEmployeestatusEnum"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="java.math.BigDecimal"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<FinanceDeliverPayupDetail> detailList = request.getAttribute("detailList")==null?null:(List<FinanceDeliverPayupDetail>)request.getAttribute("detailList");
User user = request.getAttribute("user")==null?null:(User)request.getAttribute("user");
List<User> userList = request.getAttribute("userList")==null?null:(List<User>)request.getAttribute("userList");
List<Branch> bList = request.getAttribute("bList")==null?null:(List<Branch>)request.getAttribute("bList");
Page page_obj = (Page)request.getAttribute("page_obj");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>小件员交款账户交易明细</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script>
$(function(){

	
	$("table#gd_table tr:odd").css("backgroundColor","#f9fcfd");
	$("table#gd_table tr:odd").hover(function(){
		$(this).css("backgroundColor","#fff9ed");									  
	},function(){
		$(this).css("backgroundColor","#f9fcfd");	
	});
	$("table#gd_table tr:even").hover(function(){
		$(this).css("backgroundColor","#fff9ed");									  
	},function(){
		$(this).css("backgroundColor","#fff");	
	});//表单颜色交替和鼠标滑过高亮
	
	
	$("table#gd_table2 tr").click(function(){
			$(this).css("backgroundColor","#FFA500");
			$(this).siblings().css("backgroundColor","#ffffff");
		});
	
	$(".index_dh li").mouseover(function(){
		//$(this).show(0)
		var $child = $(this).children(0);
		$child.show();
	});
	$(".index_dh li").mouseout(function(){
 
		$(".menu_box").hide();
	});
	
});
$(function() {
	$("#strtime").datetimepicker({
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
function sub(){
	$("#subnum").val(parseInt($("#subnum").val())+1);
	$('#searchForm').submit();
}
</script>
</head>

<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="right_box">
	<div class="inputselect_box" style="top: 0px; ">
		<form action="1" method="post" id="searchForm"  >
			&nbsp;&nbsp;交款时间：
			<input type ="text" name ="startTime" id="strtime"  value="<%=request.getParameter("startTime")==null?"":request.getParameter("startTime") %>"/>
			到
			<input type ="text" name ="endTime" id="endtime"  value="<%=request.getParameter("endTime")==null?"":request.getParameter("endTime") %>"/>			

			工作状态：
			<select name ="type" id ="type">
				<option value="-1">全部</option>
		          <%for(FinanceDeliverPayUpDetailTypeEnum type : FinanceDeliverPayUpDetailTypeEnum.values()){ %>
			 		<option value ="<%=type.getValue() %>" <%if(type.getValue()==Integer.parseInt(request.getParameter("type")==null?"-1":request.getParameter("type"))){ %>selected <%} %> ><%=type.getText() %></option>
		          <% }%>
			</select>
			<input type="hidden" name="subnum" id="subnum" value="<%=request.getParameter("subnum")==null?"1":request.getParameter("subnum") %>" />
		<input id="find" type="button" value="查询" onclick="sub()" class="input_button2"/>
		<input id="find" type="button" value="返回" onclick="history.go(-<%=request.getParameter("subnum")==null?"1":request.getParameter("subnum") %>)"  class="input_button2"/>
		</form>
	</div>
	<div class="right_title"  >
	<div class="jg_35"></div>
	<div class="jg_35">
	人员：<%=user.getRealname() %>　
	所属机构：<%for(Branch b : bList){if(b.getBranchid()==user.getBranchid()){out.print(b.getBranchname());}} %>　
	工作状态：<%=user.getEmployeestatusName() %>　
	现金余额：<%=user.getDeliverAccount() %>　
	POS余额：<%=user.getDeliverPosAccount() %>
	</div>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
	   <tbody>
	   	<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
	   		<td width="60px" align="center" valign="middle" bgcolor="#f3f3f3">交易编号</td>
	   		<td width="60px" align="center" valign="middle" bgcolor="#f3f3f3">操作类型</td>
			<td align="center" width="70px" valign="middle" bgcolor="#FFEFDF">现金应交</td>
			<td align="center" width="70px" valign="middle" bgcolor="#FFEFDF">现金实交</td>
			<td align="center" width="70px" valign="middle" bgcolor="#FFEFDF">交易时现金余额</td>
			<td align="center" width="70px" valign="middle" bgcolor="#E7F4E3">交易后现金余额</td>
			<td align="center" width="70px" valign="middle" bgcolor="#FFEFDF">POS应交</td>
			<td align="center" width="70px" valign="middle" bgcolor="#FFEFDF">POS实交</td>
			<td align="center" width="70px" valign="middle" bgcolor="#FFEFDF">交易时POS余额</td>
			<td align="center" width="70px" valign="middle" bgcolor="#E7F4E3">交易后POS余额</td>
			<td align="center" width="80px" valign="middle" bgcolor="#E7F4E3">操作人</td>
			<td align="center" width="150px" valign="middle" bgcolor="#E7F4E3">交易时间</td>
			<td align="center"  valign="middle" bgcolor="#E7F4E3">备注</td>
			<td align="center" width="70px" valign="middle" bgcolor="#E7F4E3">操作</td>
		</tr>
		<%
		if(detailList!=null)for(FinanceDeliverPayupDetail detail: detailList){
		%>
		
		<tr style="background-color: rgb(249, 252, 253); ">
			<td align="center" valign="middle" bgcolor="#f3f3f3"><%=detail.getId() %></td>
			<td align="center" valign="middle"><%=detail.getTypeName() %></td>
			
			<%if(detail.getType()==FinanceDeliverPayUpDetailTypeEnum.JiaoKuan.getValue()||detail.getType()==FinanceDeliverPayUpDetailTypeEnum.ChongZhiShenHe.getValue()){ %>
			<td align="right" valign="middle"><strong style="color:blue;"><%=detail.getPayupamount() %></strong></td>
			<td align="right" valign="middle"><strong style="color:blue;"><%=detail.getDeliverpayupamount() %></strong></td>
			<%}else{ %>
			<td align="center" valign="middle">-</td>
			<td align="center" valign="middle">-</td>
			<%} %>
			<td align="right" valign="middle"><strong style="color:green;"><%=detail.getDeliverAccount() %></strong></td>
			<td align="right" valign="middle"><strong style="color:<%=detail.getDeliverpayuparrearage().doubleValue()<0?"red":"blue" %>;"><%=detail.getDeliverpayuparrearage() %></strong></td>
			
			<%if(detail.getType()==FinanceDeliverPayUpDetailTypeEnum.JiaoKuan.getValue()||detail.getType()==FinanceDeliverPayUpDetailTypeEnum.ChongZhiShenHe.getValue()){ %>
			<td align="right" valign="middle"><strong style="color:blue;"><%=detail.getPayupamount_pos() %></strong></td>
			<td align="right" valign="middle"><strong style="color:blue;"><%=detail.getDeliverpayupamount_pos() %></strong></td>
			<%}else{ %>
			<td align="center" valign="middle">-</td>
			<td align="center" valign="middle">-</td>
			<%} %>
			
			<td align="right" valign="middle"><strong style="color:green;"><%=detail.getDeliverPosAccount() %></strong></td>
			<td align="right" valign="middle"><strong style="color:<%=detail.getDeliverpayuparrearage_pos().doubleValue()<0?"red":"blue" %>;"><%=detail.getDeliverpayuparrearage_pos() %></strong></td>
			<td align="center" valign="middle"><%for(User u : userList){if(u.getUserid()==detail.getAudituserid()){out.print(u.getRealname());}} %></td>
			<td align="center" valign="middle"><%=detail.getCredate() %></td>
			<td align="center" valign="middle"><%=detail.getRemark() %></td>
			<td align="center" valign="middle">
			<%if(detail.getType()==FinanceDeliverPayUpDetailTypeEnum.JiaoKuan.getValue()){ %>
			[<a href="javascript:getViewBoxd(<%=detail.getGcaid() %>,'<%=request.getContextPath()%>/delivery/viewOldSub/');">交易明细</a>]
			<%}else if(detail.getType()==FinanceDeliverPayUpDetailTypeEnum.ChongZhiShenHe.getValue()){ %>
			[<a href="javascript:;">交易明细</a>]
			<%}else{ %>
			-
			<%} %>
			</td>
		</tr>
		<%} %>
		</tbody>
	</table>
	</div>
	
	<div class="jg_35"></div>
	<%if(page_obj.getMaxpage()>1){ %>
	<div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
					<a href="javascript:$('#searchForm').attr('action','1');sub();" >第一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');sub();">上一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');sub();" >下一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');sub();" >最后一页</a>
					　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第
						<select id="selectPg" onchange="$('#searchForm').attr('action',$(this).val());sub();">
							<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
							<option value="<%=i %>" <%=request.getAttribute("page")!=null&&((Long)request.getAttribute("page")==i)?"selected":"" %>><%=i %></option>
							<% } %>
						</select>页
				</td>
			</tr>
		</table>
	</div>
    <%} %>
</div>
</body>

</html>
