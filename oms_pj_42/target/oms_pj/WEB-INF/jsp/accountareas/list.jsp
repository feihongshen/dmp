<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.AccountArea,cn.explink.domain.Customer,cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<AccountArea> accountAreaList = (List<AccountArea>)request.getAttribute("accountAreaes");
	List<Customer> customerList = (List<Customer>)request.getAttribute("customeres");
	Page page_obj = (Page)request.getAttribute("page_obj");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
div{ float:left; padding:0px 5px; list-style:none; width: 800px; }
li{ float:left; padding:0px 5px; list-style:none; width: 150px; border:1px;border-color:blue;}
</style>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script type="text/javascript">
function editIsEffectFlag(id){
		$.ajax({
			url:"<%=request.getContextPath()%>/accountareas/editIsEffectFlag/"+id,
			data:{},
			success:function(data){
				if(data)alert("操作成功");
				else alert("操作失败");
			}
		});
}

var CustomerKeyAndValue = new Array();
<%for(Customer c : customerList) {%> CustomerKeyAndValue[<%=c.getCustomerid() %>]='<%=c.getCustomername() %>';<%} %>
function getCustomerValue(key,areaid){
	$("#customerid_"+key+"_"+areaid).html(CustomerKeyAndValue[key]);
}

</script>
</HEAD>
<BODY>

<div>
<form id="searchForm" name="form1" method="POST" action="1" target="_self">
	<select id="customerid" name="customerid">
		<option value="-1">全部</option>
	<% for(Customer c : customerList){ %>
		<option value="<%=c.getCustomerid() %>"><%=c.getCustomername() %></option>
	<%} %>
	</select>
<input type="submit" id="search" value="查询">
<a href="../add">新建</a>
</form>
</div>
<%if(page_obj.getTotal()!=0){ %>
<div>
<a href="javascript:;" onclick="$('#searchForm').attr('action','1');$('#searchForm').submit()">第一页</a>
<a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getPrevious() %>');$('#searchForm').submit()">上一页</a>
<a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getNext() %>');$('#searchForm').submit()" >下一页</a>
<a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getMaxpage() %>');$('#searchForm').submit()" >最后一页</a>
	<div> 共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录　
		<select id="selectPg" onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
		<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
			<option value="<%=i %>" ><%=i %></option>
		<% } %>
		</select>
	</div>
</div>
<script type="text/javascript">$("#selectPg").val(<%=request.getAttribute("page") %>)</script>


<div>

	<% for(AccountArea a : accountAreaList){ %>
	<ul>
		<li><%=a.getAreaid() %>　</li>
		<li>|<%=a.getAreaname() %>　</li>
		<li>|<%=a.getArearemark() %>　</li>
		<li id="customerid_<%=a.getCustomerid() %>_<%=a.getAreaid() %>"><script>getCustomerValue(<%=a.getCustomerid() %>,<%=a.getAreaid() %>)</script>　</li>
		<li>|<%=a.isEffectFlag() %>　</li>
		<li>[<a href="../edit/<%=a.getAreaid() %>">修改</a>][<a href="javascript:;" onclick="editIsEffectFlag(<%=a.getAreaid() %>)">停用</a>]
		</li>
	</ul>
	<%} %>

</div>
<%} %>
<script type="text/javascript">
$("#customerid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("customerid")) %>);
</script>
</BODY>
</HTML>
