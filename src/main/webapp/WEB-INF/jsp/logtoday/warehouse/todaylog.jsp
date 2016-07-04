<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.logdto.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="net.sf.json.JSONObject"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>库房今日日志</title>
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
</head>
<%
Branch nowbranch = (Branch)request.getAttribute("nowbranch");
List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");

Map<Long ,Long> weirukuMap = (Map<Long ,Long>)request.getAttribute("weirukuMap");
Map<Long ,Long> yirukuMap = (Map<Long ,Long>)request.getAttribute("yirukuMap");
Map<Long ,Long> dacuohuoMap = (Map<Long ,Long>)request.getAttribute("dacuohuoMap");
Map<Long ,Long> jinrichukuMap = (Map<Long ,Long>)request.getAttribute("jinrichukuMap");
Map<Long ,Long> kucunMap = (Map<Long ,Long>)request.getAttribute("kucunMap");
List<Branch> branchnameList = (List<Branch>)request.getAttribute("kufangList");

Map zuirikucunMap = (Map)request.getAttribute("zuirikucunMap");
%>
<script>
function dgetViewBox(key,durl){
	window.parent.getViewBoxd(key,durl);
}
</script>
<script type="text/javascript">
$(function(){
$("#right_hideboxbtn").click(function(){
			var right_hidebox = $("#right_hidebox").css("right")
			if(
				right_hidebox == -400+'px'
			){
				$("#right_hidebox").css("right","10px");
				$("#right_hideboxbtn").css("background","url(right_hideboxbtn2.gif)");
				
			};
			
			if(right_hidebox == 10+'px'){
				$("#right_hidebox").css("right","-400px");
				$("#right_hideboxbtn").css("background","url(right_hideboxbtn.gif)");
			};
	});
});

$(function(){
	$("#branid").combobox();
	$("span.combo-arrow").css({"margin-right":"-18px","margin-top":"-20px"});
	})
</script>


</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="right_box">
	<div class="inputselect_box" style="top: 0px; ">
	<form id="searchForm" action ="<%=request.getContextPath()%>/warehouseLog/nowlog" method = "post">
            <div style="float: left;">  库房：<select name ="branchid" id="branid" class="select1" onchange="$('#searchForm').submit()">
	              <%if(branchnameList != null && branchnameList.size()>0){ %>
	               <%for( Branch b:branchnameList){ %>
	               		<option value ="<%=b.getBranchid()%>" 
	               		<%if(b.getBranchid() == new Long(request.getParameter("branchid")==null?"-1":request.getParameter("branchid"))) {%>selected="selected"<%} %>><%=b.getBranchname() %></option>
	               <%} }%>
	              </select></div>
	               &nbsp;&nbsp;当前日期：<%=new SimpleDateFormat("yyyy-MM-dd").format(new Date()) %> &nbsp;&nbsp; 当前统计的是从
			<font color="red"><%=request.getAttribute("startTime") %></font> 到当前时间
	              </form> 
			
</div>
  <div style="background:#FFF">
	<div style="height:25px"></div>
	<div class="tabbox">
	  <div style="position:relative; z-index:0 " >
 		<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
	   <tbody>
	   	<tr class="font_1" height="30" >
	   	<td width="20%" rowspan="2" align="center" valign="middle" bgcolor="#f3f3f3">供货商</td>
			<td width="20%" rowspan="2" align="center" valign="middle" bgcolor="#E7F4E3">昨日库存</td>
			<td colspan="4" align="center" valign="middle" bgcolor="#E7F4E3">今日入库</td>
			<td width="10%" rowspan="2" align="center" valign="middle" bgcolor="#E1F0FF">今日出库</td>
			<td width="10%"  rowspan="2" align="center" valign="middle" bgcolor="#E1F0FF"><p >当前库存</p>
				<p >（单）</p></td>
			</tr>
	   	<tr class="font_1" height="30" >
	   		<td width="10%" align="center" valign="middle" bgcolor="#E7F4E3"><p>未入库</p>
	   			<p>（单）</p></td>
	   		<td width="10%" align="center" valign="middle" bgcolor="#E7F4E3"><p>已入库</p>
	   			<p>（单）</p></td>
	   		<td width="10%" align="center" valign="middle" bgcolor="#E7F4E3"><p>到错货</p>
	   			<p>（单）</p></td>
	   		<td width="10%" align="center" valign="middle" bgcolor="#E7F4E3"><p>入库合计</p>
	   			<p>（单）</p></td>
	   		<!-- <td width="10%" align="center" valign="middle" bgcolor="#FFFF33"><p >历史出库在途</p>
	   		<p >（单）</p>
	   		</td>
	   		<td width="10%" align="center" valign="middle" bgcolor="#E1F0FF"><p >今日出库在途</p>
	   			<p >（单）</p></td>
	   		<td width="10%" align="center" valign="middle" bgcolor="#E1F0FF"><p >出库已到站</p>
	   			<p >（单）</p></td>
	   		<td width="10%" align="center" valign="middle" bgcolor="#E1F0FF"><p >漏扫已到站</p>
	   			<p >（单）</p></td> -->
	   		</tr>
	   	</tbody>
	</table>
	
    </div>
	<div style="height:84px"></div>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2" >
	  <tbody>
	    <%
	    	
	        long zuorikucunHeji = 0; 
	        long weirukuHeji = 0; 
	    	long yirukuHeji = 0; 
	    	long dacuohuHeji = 0; 
	    	long rukuhejiHeji = 0; 
	    	long chukuzaituHeji = 0; 
	    	long dangqiankucunHeji = 0; 
	    %>
	    <%if(customerList != null){ %>
	    <%for(Customer customer : customerList){ %>
	    <tr height="30">
	    	<td width="20%" align="center" valign="middle" bgcolor="#f3f3f3"><%=customer.getCustomername() %></td>
	    	<td width="20%" align="center" valign="middle" bgcolor="#f3f3f3"><%=zuirikucunMap.get(customer.getCustomerid())==null?0:zuirikucunMap.get(customer.getCustomerid()) %></td>
	    	<% zuorikucunHeji += Long.parseLong(zuirikucunMap.get(customer.getCustomerid())==null?"0":zuirikucunMap.get(customer.getCustomerid()).toString()); %>
	    	<%if(weirukuMap!=null && !weirukuMap.isEmpty()){ %>
		    	<%if(weirukuMap.get(customer.getCustomerid()) != null){ %>
		    		<td width="10%" align="center" valign="middle">
		    		<a href="<%=request.getContextPath()%>/warehouseLog/show/<%=customer.getCustomerid()%>/weiruku/<%=weirukuMap.get(customer.getCustomerid()) %>/<%=request.getAttribute("branchid")==null?0:request.getAttribute("branchid")%>/1" ><%=weirukuMap.get(customer.getCustomerid()) %></a></td>
		    		<%weirukuHeji += weirukuMap.get(customer.getCustomerid()); %>
		    	<%}else{ %>
		    		<td width="10%" align="center" valign="middle">0</td>
		    	<%} %>
	    	<%} else{%>
	    		<td width="10%" align="center" valign="middle">0</td>
	    	<%} %>
	    	<%if(yirukuMap!=null && !yirukuMap.isEmpty()){ %>
		    	<%if(yirukuMap.get(customer.getCustomerid()) != null){ %>
		    		<td width="10%" align="center" valign="middle">
		    		<a href="<%=request.getContextPath()%>/warehouseLog/show/<%=customer.getCustomerid()%>/yiruku/<%=yirukuMap.get(customer.getCustomerid()) %>/<%=request.getAttribute("branchid")==null?0:request.getAttribute("branchid")%>/1" ><%=yirukuMap.get(customer.getCustomerid()) %></a></td>
		    		<%yirukuHeji += yirukuMap.get(customer.getCustomerid()); %>
		    	<%}else{ %>
		    		<td width="10%" align="center" valign="middle">0</td>
		    	<%} %>
	    	<%} else{%>
	    		<td width="10%" align="center" valign="middle">0</td>
	    	<%} %>
	    	<%if(dacuohuoMap!=null && !dacuohuoMap.isEmpty()){ %>
		    	<%if(dacuohuoMap.get(customer.getCustomerid()) != null){ %>
		    		<td width="10%" align="center" valign="middle">
		    		<a href="<%=request.getContextPath()%>/warehouseLog/show/<%=customer.getCustomerid()%>/daocuohuo/<%=dacuohuoMap.get(customer.getCustomerid()) %>/<%=request.getAttribute("branchid")==null?0:request.getAttribute("branchid")%>/1" ><%=dacuohuoMap.get(customer.getCustomerid()) %></a></td>
		    		<%dacuohuHeji += dacuohuoMap.get(customer.getCustomerid()); %>
		    	<%}else{ %>
		    		<td width="10%" align="center" valign="middle">0</td>
		    	<%} %>
	    	<%} else{%>
	    		<td width="10%" align="center" valign="middle">0</td>
	    	<%} %>
	    	     <% long rukuheji=0; 
	    	    if((yirukuMap!=null && !yirukuMap.isEmpty())||(dacuohuoMap!=null && !dacuohuoMap.isEmpty())){ 
			    	if(yirukuMap.get(customer.getCustomerid()) != null ){
			    	    	rukuheji += yirukuMap.get(customer.getCustomerid());
			    	    }
			    	if(dacuohuoMap.get(customer.getCustomerid()) != null){  
			    	    rukuheji += dacuohuoMap.get(customer.getCustomerid()); 
			    	 } 
			     }
		    	%>
	    		<td width="10%" align="center" valign="middle"><%=rukuheji %></td>
	    	<%if(jinrichukuMap!=null && !jinrichukuMap.isEmpty()){ %>
		    	<%if(jinrichukuMap.get(customer.getCustomerid()) != null){ %>
		    		<td width="10%" align="center" valign="middle">
		    		<a href="<%=request.getContextPath()%>/warehouseLog/show/<%=customer.getCustomerid()%>/jinrichukuzaitu/<%=jinrichukuMap.get(customer.getCustomerid()) %>/<%=request.getAttribute("branchid")==null?0:request.getAttribute("branchid")%>/1" ><%=jinrichukuMap.get(customer.getCustomerid()) %></a></td>
		    		<%chukuzaituHeji += jinrichukuMap.get(customer.getCustomerid()); %>
		    	<%}else{ %>
		    		<td width="10%" align="center" valign="middle">0</td>
		    	<%} %>
	    	<%} else{%>
	    		<td width="10%" align="center" valign="middle">0</td>
	    	<%} %>
	    	
	    	<%if(kucunMap!=null && !kucunMap.isEmpty()){ %>
		    	<%if(kucunMap.get(customer.getCustomerid()) != null){ %>
		    		<td width="10%"  align="center" valign="middle">
		    		<a href="<%=request.getContextPath()%>/warehouseLog/show/<%=customer.getCustomerid()%>/kucun/<%=kucunMap.get(customer.getCustomerid()) %>/<%=request.getAttribute("branchid")==null?0:request.getAttribute("branchid")%>/1" ><%=kucunMap.get(customer.getCustomerid()) %></a></td>
		    		<%dangqiankucunHeji += kucunMap.get(customer.getCustomerid());  %>
		    	<%}else{ %>
		    		<td width="10%"  align="center" valign="middle">0</td>
		    	<%} %>
	    	<%} else{%>
	    		<td width="10%"  align="center" valign="middle">0</td>
	    	<%} %>
	    	
	    	
    	</tr>
    	<%} }%>
    	
	    
	    <tr height="30">
	    	<td align="center" valign="middle" bgcolor="#f3f3f3"><strong>合计</strong></td>
	    	<td align="center" valign="middle" bgcolor="#f3f3f3"><strong><%=zuorikucunHeji %></strong></td>
	    	<td align="center" valign="middle" bgcolor="#f3f3f3"><strong><a href="<%=request.getContextPath()%>/warehouseLog/show/0/weiruku/<%=weirukuHeji %>/<%=request.getAttribute("branchid")==null?0:request.getAttribute("branchid")%>/1" ><%=weirukuHeji %></a></strong></td>
	    	<td align="center" valign="middle" bgcolor="#f3f3f3"><strong><a href="<%=request.getContextPath()%>/warehouseLog/show/0/yiruku/<%=yirukuHeji %>/<%=request.getAttribute("branchid")==null?0:request.getAttribute("branchid")%>/1" ><%=yirukuHeji %></a></strong></td>
	    	<td align="center" valign="middle" bgcolor="#f3f3f3"><strong><a href="<%=request.getContextPath()%>/warehouseLog/show/0/daocuohuo/<%=dacuohuHeji %>/<%=request.getAttribute("branchid")==null?0:request.getAttribute("branchid")%>/1" ><%=dacuohuHeji %></a></strong></td>
	    	<td align="center" valign="middle" bgcolor="#f3f3f3"><strong><%=dacuohuHeji+yirukuHeji %></strong></td>
	    	<td align="center" valign="middle" bgcolor="#f3f3f3"><strong><a href="<%=request.getContextPath()%>/warehouseLog/show/0/jinrichukuzaitu/<%=chukuzaituHeji %>/<%=request.getAttribute("branchid")==null?0:request.getAttribute("branchid")%>/1" ><%=chukuzaituHeji %></a></strong></td>
	    	<td align="center" valign="middle" bgcolor="#f3f3f3"><strong><a href="<%=request.getContextPath()%>/warehouseLog/show/0/kucun/<%=dangqiankucunHeji %>/<%=request.getAttribute("branchid")==null?0:request.getAttribute("branchid")%>/1" ><%=dangqiankucunHeji %></a></strong></td>
    	</tr>
	    </tbody>
	  </table>

	</div>
	</div>
</div>
</div>
</body>
</html>