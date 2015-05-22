<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.domain.Customer"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<JSONObject> quejianList = (List<JSONObject>)request.getAttribute("quejianList");
List<Customer> cList = (List<Customer>)request.getAttribute("customerlist");
long flowordertype = request.getAttribute("flowordertype")==null?0:Long.parseLong(request.getAttribute("flowordertype").toString());
long nextbranchid = request.getAttribute("nextbranchid")==null?0:Long.parseLong(request.getAttribute("nextbranchid").toString());
long customerid = request.getAttribute("customerid")==null?0:Long.parseLong(request.getAttribute("customerid").toString());
//long page = request.getAttribute("page")==null?0:Long.parseLong(request.getAttribute("page").toString());
%>
<%for(JSONObject obj : quejianList){
	/* String transcwb = ""; 
	if(obj.getString("transcwb").indexOf("explink")>-1){
		
	}else if(obj.getString("transcwb").indexOf("havetranscwb")>-1){
		transcwb = obj.getString("transcwb").split("_")[1];
	} */
%>
<tr id="<%=obj.getString("cwb") %>" cwb="<%=obj.getString("cwb") %>" customerid="<%=obj.getLong("customerid") %>"  nextbranchid="<%=obj.getLong("nextbranchid") %>">
<td width="120" align="center"><%=obj.getString("cwb") %></td>
<td width="120" align="center"><%=obj.getString("transcwb") %></td>
<td width="100" align="center"><%=obj.getString("customername") %></td>
<td width="140"><%=obj.getString("emaildate") %></td>
<td width="100"><%=obj.getString("consigneename") %></td>
<td width="100"><%=obj.getDouble("receivablefee") %></td>
<td align="left"><%=obj.getString("consigneeaddress") %></td>
</tr>
<%} %>
<%if(quejianList!=null&&quejianList.size()==Page.DETAIL_PAGE_NUMBER){ %>
<%if(flowordertype==FlowOrderTypeEnum.RuKu.getValue()){ %>
	<tr align="center"  ><td colspan="7" style="cursor:pointer" onclick="ypdjruku('<%=request.getContextPath() %>',<%=Page.DETAIL_PAGE_NUMBER %>,${page } ,<%=customerid %>);" id="ypdjrk">查看更多</td></tr>
<%}else{ %>
	<tr align="center"  ><td colspan="7" style="cursor:pointer" onclick="ypdjchuku('<%=request.getContextPath() %>',<%=Page.DETAIL_PAGE_NUMBER %>,${page } ,<%=nextbranchid %>);" id="ypdjck">查看更多</td></tr>
<%} %>
<%} %>

