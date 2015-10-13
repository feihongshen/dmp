
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<Branch> branches = (List<Branch>)request.getAttribute("branches");
  Page page_obj = (Page)request.getAttribute("page_obj");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
div{ float:left; padding:0px 5px; list-style:none; width: 650px; }
li{ float:left; padding:0px 5px; list-style:none; width: 200px; border:1px;border-color:blue;}
</style>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
</HEAD>

<BODY>

<form id ="branchForm" name ="formBranch" action ="1" method="post"> 
 
  <table>
     <tr>
        <td>站名名称：</td>
        <td><input type ="text" name ="branchname" value = "<%=request.getParameter("branchname")==null?"":request.getParameter("branchname") %>"></td>
     </tr>
     <tr>
        <td>站点地址：</td>
        <td><input type ="text" name ="branchaddress" value = "<%=request.getParameter("branchaddress")==null?"":request.getParameter("branchaddress") %>"></td>
     </tr>
      <tr>
        <td colspan ="2"><input type ="submit" value ="查询"></td>
        <td><a href="<%=request.getContextPath()%>/branch/add">新建</a></td>
     </tr>
  </table>  
   <input type="hidden" name ="showMessage" value ="showMessage">
</form>
<%if(page_obj.getTotal()>0){%>
<%if(request.getParameter("showMessage")!=null){ %>
   <a href="javascript:;" onclick="$('#branchForm').attr('action','1');$('#branchForm').submit()">第一页</a>
   <a href="javascript:;" onclick="$('#branchForm').attr('action','<%=page_obj.getPrevious() %>');$('#branchForm').submit()">上一页</a>
   <a href="javascript:;" onclick="$('#branchForm').attr('action','<%=page_obj.getNext() %>');$('#branchForm').submit()" >下一页</a>
   <a href="javascript:;" onclick="$('#branchForm').attr('action','<%=page_obj.getMaxpage() %>');$('#branchForm').submit()" >最后一页</a>
	<div> 共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录　
	 <select id="selectPg" onchange="$('#branchForm').attr('action',$(this).val());$('#branchForm').submit()">
		<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
			<option value="<%=i %>"><%=i %></option>
		<% } %>
	</select>
	<script type="text/javascript">$("#selectPg").val(<%=request.getAttribute("page") %>)</script>
<table > 
  <tr>
  </tr>
   <tr>
     <td>ID</td>
     <td>站点名称</td>
     <td>站点所在省区名称</td>
     <td>站点所在城市名称</td>
     <td>站点地址</td>
     <td>站点联系人</td>
     <td>站点电话</td>
     <td>站点手机</td>
    <!--  <td>站点传真</td>
     <td>站点电子邮件</td>
     <td>站点类别</td>
     <td>站点类型</td>
     <td>允许出库的下一站id</td>
     <td>站点交款是否允许修改金额</td>
     <td>全退件是否可再投</td>
     <td>站点上交总部控制</td>
     <td>当日业务完毕控制</td>
     <td>站点预付保证金</td>
     <td>站点名称语音文件名</td>
     <td>站点提货授信额度</td>
     <td>站点是否有效</td>
     <td>加盟站佣金比例</td>
     <td>站点条码编号</td>
     <td>无邮件是否允许入库</td>
     <td>异常到货是否允许派送</td>
     <td>是否允许异常到货确认</td>
     <td>站点编码声音文件名</td>
     <td>入库提示声音类型</td>
     <td>出库提示声音类型</td>
     <td>无邮件是否允许投递</td>
     <td>站点启用与否</td>
     <td>修改</td>
 -->
   </tr>

  <% for(Branch b : branches){ %>
      <tr>
         <td><%=b.getBranchid() %></td>
         <td><%=b.getBranchname() %></td>
         <td><%=b.getBranchprovince() %></td>
         <td><%=b.getBranchcity() %></td>
         <td><%=b.getBranchaddress() %></td>
         <td><%=b.getBranchcontactman() %></td>
         <td><%=b.getBranchphone() %></td>
         <td><%=b.getBranchmobile() %></td>
        <%--  <td><%=b.getBranchfax() %></td>
         <td><%=b.getBranchemail() %></td>
         <td><%=b.getContractflag() %></td>
         <td><%=b.getBranchtypeflag() %></td>
         <td><%=b.getCwbtobranchid() %></td>
         <td><%=b.getPayfeeupdateflag() %></td>
         <td><%=b.getBacktodeliverflag() %></td>
         <td><%=b.getBranchpaytoheadflag() %></td>
         <td><%=b.getBranchfinishdayflag() %></td>
         <td><%=b.getBranchinsurefee() %></td>
         <td><%=b.getBranchwavfile() %></td>
         <td><%=b.getCreditamount() %></td>
         <td><%=b.getBrancheffectflag() %></td>
         <td><%=b.getContractrate() %></td>
         <td><%=b.getBranchcode() %></td>
         <td><%=b.getNoemailimportflag() %></td>
         <td><%=b.getErrorcwbdeliverflag() %></td>
         <td><%=b.getErrorcwbbranchflag() %></td>
         <td><%=b.getBranchcodewavfile() %></td>
         <td><%=b.getImportwavtype() %></td>
         <td><%=b.getExportwavtype() %></td>
         <td><%=b.getNoemaildeliverflag() %></td>
         <td><%=b.getSendstartbranchid() %></td> --%>
         <td><a href ="../edit/<%=b.getBranchid()%>">修改</a></td>
      </tr>
  <%} %>
	</table>
</div>	
<%} %>
<%} %>

</BODY>
</HTML>
