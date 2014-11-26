<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.enumutil.UserEmployeestatusEnum"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="java.math.BigDecimal"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<User> userList = request.getAttribute("userList")==null?null:(List<User>)request.getAttribute("userList");
List<Branch> bList = request.getAttribute("bList")==null?null:(List<Branch>)request.getAttribute("bList");
List<Branch> branchAllZhanDianList = request.getAttribute("branchAllZhanDianList")==null?null:(List<Branch>)request.getAttribute("branchAllZhanDianList");

Page page_obj = (Page)request.getAttribute("page_obj");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>小件员交款财务</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
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

function sub(){
	if((!isFloat($("#deliverAccountStart").val())&&$("#deliverAccountStart").val().length!=0)
	  ||(!isFloat($("#deliverAccountEnd").val())&&$("#deliverAccountEnd").val().length!=0)
	  ||(!isFloat($("#deliverPosAccountStart").val())&&$("#deliverPosAccountStart").val().length!=0)
	  ||(!isFloat($("#deliverPosAccountEnd").val())&&$("#deliverPosAccountEnd").val().length!=0)){
		alert("金额条件只能是数字格式或者不填！");
		return false;
	}else{
		$('#searchForm').submit();
	}
}
function saveSuccess(data){
	$("#searchForm").attr("action",$("#selectPg").val());
	$("#searchForm").submit();
}
</script>
</head>

<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="right_box">
	<div class="inputselect_box" style="top: 0px; ">
		<form action="1" method="post" id="searchForm"  >
			所属站点：
			<select name ="branchid" id ="branchid">
				<option value="-1">全部</option>
		          <%if(bList!=null && bList.size()>0) {%>
		          <%for(Branch b : bList){ %>
			 		<option value ="<%=b.getBranchid() %>" 
			     <%if(b.getBranchid()==Integer.parseInt(request.getParameter("branchid")==null?"-1":request.getParameter("branchid"))){ %>selected="selected" <%} %> ><%=b.getBranchname()%></option>
		          <%} }%>
			</select>
			姓名：<input type ="text" size="12"  name ="realname" id="realname"  value="<%=request.getParameter("realname")==null?"":request.getParameter("realname") %>"/>
			　　（<input type ="text" size="8" name="deliverAccountStart" id="deliverAccountStart"  value="<%=request.getParameter("deliverAccountStart")==null?"":request.getParameter("deliverAccountStart")%>"/>
			&lt; 现金余额  &lt; <input type ="text" size="8" name ="deliverAccountEnd" id="deliverAccountEnd"  value="<%=request.getParameter("deliverAccountEnd")==null?"":request.getParameter("deliverAccountEnd") %>"/>）
			　　（<input type ="text" size="8" name="deliverPosAccountStart" id="deliverPosAccountStart"  value="<%=request.getParameter("deliverAccountStart")==null?"":request.getParameter("deliverPosAccountStart") %>"/>
			&lt; POS余额  &lt; <input type ="text" size="8" name ="deliverPosAccountEnd" id="deliverPosAccountEnd"  value="<%=request.getParameter("deliverAccountEnd")==null?"":request.getParameter("deliverPosAccountEnd") %>"/>）
			工作状态：
			<select name ="uee" id ="uee">
				<option value="-1">全部</option>
		          <%for(UserEmployeestatusEnum uee : UserEmployeestatusEnum.values()){ %>
			 		<option value ="<%=uee.getValue() %>" <%if(uee.getValue()==Integer.parseInt(request.getParameter("uee")==null?"-1":request.getParameter("uee"))){ %>selected <%} %> ><%=uee.getText() %></option>
		          <% }%>
			</select>
		<input id="find" type="button" value="查询" onclick="sub()" class="input_button2"/>
		</form>
	</div>
	<div class="right_title"  >
	<div class="jg_35"></div>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
	   <tbody>
	   	<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
	   		<td width="100px" align="center" valign="middle" bgcolor="#f3f3f3">站点</td>
	   		<td width="70px" align="center" valign="middle" bgcolor="#f3f3f3">姓名</td>
			<td align="center" width="60px" valign="middle" bgcolor="#FFEFDF">现金余额</td>
			<td align="center" width="34px" valign="middle" bgcolor="#E7F4E3">POS余额</td>
			
			<td align="center" width="70px" valign="middle" bgcolor="#E7F4E3">操作</td>
		</tr>
		<%
		if(userList!=null)for(User u: userList){
		%>
		
		<tr style="background-color: rgb(249, 252, 253); ">
			<td align="center" valign="middle" bgcolor="#f3f3f3"><% for(Branch b : branchAllZhanDianList){ if(b.getBranchid()==u.getBranchid()){out.print(b.getBranchname());} } %></td>
			<td align="center" valign="middle" bgcolor="#f3f3f3"><%=u.getRealname() %></td>
			<td align="right" valign="middle"><strong style="color:<%=u.getDeliverAccount().doubleValue()<0?"red":"blue" %>;"><%=u.getDeliverAccount() %></strong></td>
			<td align="right" valign="middle"><strong style="color:<%=u.getDeliverPosAccount().doubleValue()<0?"red":"blue" %>;"><%=u.getDeliverPosAccount() %></strong></td>
			
			<td align="center" valign="middle">
			[<a href="<%=request.getContextPath()%>/deliverpayup/deliveraccountDetailList/<%=u.getUserid() %>/1">查看交易明细</a>]　
			[<a href="javascript:getViewBoxd(<%=u.getUserid() %>,'<%=request.getContextPath()%>/deliverpayup/deliveraccountEdit/');">调账</a>]</td>
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
