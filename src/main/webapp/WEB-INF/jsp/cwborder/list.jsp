
<%@page import="cn.explink.domain.AccountArea"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeEnum"%>
<%@page import="cn.explink.domain.EmailDate"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<CwbOrder> cwbOrderList = (List<CwbOrder>)request.getAttribute("cwborderList");
  List<CwbOrder> cwbOrderListNoLimit = (List<CwbOrder>)request.getAttribute("cwborderListNoLimit");
  List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
  List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
  List<AccountArea> accountareaList = (List<AccountArea>)request.getAttribute("accountareaList");
  List<EmailDate> emaildateList = (List<EmailDate>)request.getAttribute("emaildateList");
  Customer customer = (Customer)request.getAttribute("customer");
  Page page_obj =(Page)request.getAttribute("page_obj");
  String showMess = request.getParameter("showMess")==null?"":(String)request.getParameter("showMess");
  
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/explink.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<style type="text/css">

li{ float:left; padding:0px 5px; list-style:none; width: 100px; border:1px dashed blue;float:left;}
</style>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>

<script type="text/javascript">
$(function(){

	$("#beginemaildate").datepicker();
	$("#endemaildate").datepicker();
});

</script>
</HEAD>
<BODY style="background:#eef9ff">
<form id="searchForm" action ="1" method = "post">
客户名称： <select id ="customerid" name = "customerid" >
            <option value =-1>全部</option>
            <%for(Customer cus : customerList){%>
              <option value ="<%=cus.getCustomerid()%>"><%=cus.getCustomername() %></option>
            <%}%>
          </select>

发货时间批次 ：<select id ="emaildate" name ="emaildate">
               <option value ="">==请选择邮件的时间批次==</option>
               <%for(EmailDate emaildate : emaildateList){%>
                <option value ="<%=emaildate.getEmaildatetime() %>"><%=emaildate.getEmaildatetime() %></option>
               <%}%>
              </select><br/>
订单入库站点： <select id="branchid" name="branchid">
               <option value =-1>全部</option>
              <%for(Branch b : branchList){ %>
                <option value ="<%=b.getBranchid()%>"><%=b.getBranchname() %></option>
              <%} %>
              </select>

 派送区域：<select id ="servicearea" name = "servicearea">
            <option value = -1>全部</option>
            <%for(AccountArea s : accountareaList){ %>
                <option value ="<%=s.getAreaid()%>"><%=s.getAreaname() %></option>
            <%} %>
         </select>      
订单入库状态:<select id ="emailfinishflag" name ="emailfinishflag">
             <option value ="">全部</option>
             <option value ="<%=CwbOrderTypeEnum.NormalInLibrary.getValue()%>"><%=CwbOrderTypeEnum.NormalInLibrary.getText()%></option>
             <option value ="<%=CwbOrderTypeEnum.HaveListNoGoods.getValue()%>"><%=CwbOrderTypeEnum.HaveListNoGoods.getText()%></option>
             <option value ="<%=CwbOrderTypeEnum.HaveGoodsNoList.getValue()%>"><%=CwbOrderTypeEnum.HaveGoodsNoList.getText()%></option>
            </select><br/>
发货时间段：
 <input type ="text" name ="beginemaildate" id ="beginemaildate" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("beginemaildate")) %>">　到　

 <input type ="text" name= "endemaildate" id ="endemaildate" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("endemaildate")) %>"><br/>
订单号：
 <textarea rows="3" cols="12" name ="ordercwb" id ="ordercwb"><%=StringUtil.nullConvertToEmptyString(request.getParameter("ordercwb")) %></textarea>[多个订单号用回车键隔开]
<input type ="hidden" name ="showMess" value ="1">
<input type ="submit"  value ="查询">
<input type="button"  onclick="location.href='1'" value="返回" class="input_button2" />
</form>
<%if(page_obj.getTotal()!=0){ %>
<%if(showMess.equals("1")){ %>
 <div>
<a href="javascript:;" onclick="$('#searchForm').attr('action','1');$('#searchForm').submit()">第一页</a>
<a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getPrevious() %>');$('#searchForm').submit()">上一页</a>
<a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getNext() %>');$('#searchForm').submit()" >下一页</a>
<a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getMaxpage() %>');$('#searchForm').submit()" >最后一页</a>
	<div> 共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录　</div>
	第<%=request.getAttribute("page")%>页 
</div>
	<select id="selectPg" onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
		<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
			<option value="<%=i %>"><%=i %></option>
		<% } %>
	</select>
<script type="text/javascript">$("#selectPg").val(<%=request.getAttribute("page") %>)</script>
<form action = "../outfile/1" method ="post">
  <input type="submit" value="导出数据">
</form>

<% for(CwbOrder c : cwbOrderList){ %>
	<div id ="divpg" style ="width:630px;height:25px;">
		<ul>
			<li><a href="../../orderflow/view/<%=c.getCwb()%>" ><%=c.getCwb()%></a></li>
			<li><%=c.getConsigneemobile()%></li><li><%=c.getCustomercommand()%></li>
		</ul>
	</div>
	<div><a href="../selectforpda/<%=c.getCwb()%>" >更多操作</a></div>
<%} %>
<%} %>
<%} %>
<script type="text/javascript">
	$("#customerid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("customerid"))%>);
	$("#branchid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("branchid"))%>);
	$("#emaildate").val('<%=StringUtil.nullConvertToEmptyString(request.getParameter("emaildate"))%>');
	$("#servicearea").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("servicearea"))%>);
	$("#emailfinishflag").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("emailfinishflag"))%>);
</script>


</BODY>
</HTML>
