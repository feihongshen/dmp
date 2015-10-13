
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.ImportCwbOrderType"%>
<%@page import="cn.explink.domain.CwbOrderTypeBean"%>
<%@page import="cn.explink.domain.CwbOrderType"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbOrderTypeBean> cwborderTypeBean = (List<CwbOrderTypeBean>)request.getAttribute("cwborderTypeBean");
List<ImportCwbOrderType> importCwbOrderTypes = (List<ImportCwbOrderType>)request.getAttribute("importCwbOrderTypes");
ImportCwbOrderType importCwbOrderType = (ImportCwbOrderType)request.getAttribute("importCwbOrderType");
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
<script type="text/javascript">$("#selectPg").val(<%=request.getAttribute("page") %>)</script>
</HEAD>
<script type="text/javascript">

</script>
<BODY>
<div>
<form  action ="1" method="post" id="cwbForm"> 
       订单类型：
       <select name="cwbordertype" id="cwbordertypes">
        	<option value="-1">请选择类型</option>
        	<option value="1">配送</option>
        	<option value="2">上门退货</option>
        	<option value="3">上门换货</option>
        </select> <br>
        <input type ="submit" id="find" value ="查询">
        <a href="../add">新建</a>
     </form>
     <%if(page_obj.getTotal()!=0){ %>
            <div>
	<a href="javascript:;" onclick="$('#cwbForm').attr('action','1');$('#cwbForm').submit()">第一页</a>
	<a href="javascript:;" onclick="$('#cwbForm').attr('action','<%=page_obj.getPrevious() %>');$('#cwbForm').submit()">上一页</a>
	<a href="javascript:;" onclick="$('#cwbForm').attr('action','<%=page_obj.getNext() %>');$('#cwbForm').submit()" >下一页</a>
	<a href="javascript:;" onclick="$('#cwbForm').attr('action','<%=page_obj.getMaxpage() %>');$('#cwbForm').submit()" >最后一页</a>
	<div> 共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录　
		<select id="selectPg" onchange="$('#cwbForm').attr('action',$(this).val());$('#cwbForm').submit()">
		<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
			<option value="<%=i %>"><%=i %></option>
		<% } %>
		</select>
	</div>
	<script type="text/javascript">$("#selectPg").val(<%=request.getAttribute("page") %>)</script>
</div>
<table>
	<tr>
     <td>类型</td>
     <td>备注</td>
     <td width="100">操作</td>
     </tr>
      <% for(ImportCwbOrderType i : importCwbOrderTypes){ %>
      <tr>
      <% if(i.getImporttypeid()==1){ %>
         <td>配送</td>
         <%}else if(i.getImporttypeid()==2){ %>
         <td>上门退</td>
         <%}else if(i.getImporttypeid()==3){ %>
         <td>上门换</td>
         <%} %>
         <td><%=i.getImporttype()%></td>
         <td width="40"><a onclick="return confirm('确定要删除?')" href="../del/<%=i.getImportid()%>">删除</a></td>
         <td width="40"><a href="../edit/<%=i.getImportid()%>">修改</a></td>
	</tr>
	<%} %>
  </table>  
</div>	
	<%} %>
	 <script type="text/javascript">
          $("#cwbordertypes").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("cwbordertype"))%>);
        </script>
</BODY>
</HTML>
