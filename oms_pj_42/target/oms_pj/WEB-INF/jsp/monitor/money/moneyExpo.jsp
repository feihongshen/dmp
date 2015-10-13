
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.MonitorDTO"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Branch"%>
 <%
 List<Branch> branchnameList =(List<Branch>) request.getAttribute("branchnameList");
 Branch branchObjectNow =(Branch) request.getAttribute("branchObjectNow");
 long branchidNow =Long.parseLong(request.getAttribute("branchidNow")==null?"-1":request.getAttribute("branchidNow").toString()) ;
 
 Map exptionMap =(Map) request.getAttribute("exptionMap");
 %>
 <%@page contentType="application/msexcel" %>  
<% response.setContentType("application/vnd.ms-excel;charset=UTF-8"); %> 
<% response.setHeader("Content-disposition","attachment; filename=dateMonitor.xls");%>
    <html>

<head>
<style type="text/css">
body{
	margin: 6px;
	padding: 0;
	font-size: 12px;
	font-family: tahoma, arial;
	background: #fff;
}

table{
	width: 100%;
}
tr.odd{
	background: #fff;
}

tr.even{
	background: #eee;
}

div.datagrid_div{
	width: 100%;
	border: 1px solid #999;
}


table.datagrid{
	border-collapse: collapse; 
}

table.datagrid th{
	text-align: left;
	background: #9cf;
	padding: 3px;
	border: 1px #333 solid;
}

table.datagrid td{
	padding: 3px;
	border: none;
	border:1px #333 solid;
}

tr:hover,
tr.hover{
	background: #9cf;
}
</style>
</head>

<body>
 
<div style="text-align:center;">
	<table class="datagrid">
	
	<tr>
		<th >站点</th>
		<th >有单无货</th>
		<th >有货无单</th>
		<th >异常单</th>
	</tr>
	
	<%if(branchidNow< 0){ %>
	<%if(branchnameList!= null && branchnameList.size()>0){ %>
	<%for(int i =0;i<branchnameList.size();i++){ %>
	<tr>
		<td>
		<%=branchnameList.get(i).getBranchname()%>
		</td>
		<td>
		<%if(exptionMap != null && 
		exptionMap.get(branchnameList.get(i).getBranchid())!=null
		&& ((Map)exptionMap.get(branchnameList.get(i).getBranchid())) != null
		&& ((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("youdanwuhuo_"+branchnameList.get(i).getBranchid()) != null){ %>
		<a href="<%=request.getContextPath()%>/monitorEx/dateshow/7?flowType=<%=request.getAttribute("youdanwuhuoDateType")%>&branchPramid=<%=branchnameList.get(i).getBranchid()%>">
		<%=((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("youdanwuhuo_"+branchnameList.get(i).getBranchid())%>
		</a>
		<%} else{%>
		0
		<%} %>
		</td>             
		<td>
		<%if(exptionMap != null && 
		exptionMap.get(branchnameList.get(i).getBranchid())!=null
		&& ((Map)exptionMap.get(branchnameList.get(i).getBranchid())) != null
		&& ((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("youhuowudan_"+branchnameList.get(i).getBranchid()) != null){ %>
		<a href="<%=request.getContextPath()%>/monitorEx/dateshow/7?flowType=<%=request.getAttribute("youhuowudanDateType")%>&branchPramid=<%=branchnameList.get(i).getBranchid()%>">
		<%=((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("youhuowudan_"+branchnameList.get(i).getBranchid())%>
		</a>
		<%} else{%>
		0
		<%} %>
		</td> 
		<td>
		<%if(exptionMap != null && 
		exptionMap.get(branchnameList.get(i).getBranchid())!=null
		&& ((Map)exptionMap.get(branchnameList.get(i).getBranchid())) != null
		&& ((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("yichangdan_"+branchnameList.get(i).getBranchid()) != null){ %>
		<a href="<%=request.getContextPath()%>/monitorEx/dateshow/7?flowType=<%=request.getAttribute("yichangdanDateType")%>&branchPramid=<%=branchnameList.get(i).getBranchid()%>">
		<%=((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("yichangdan_"+branchnameList.get(i).getBranchid())%>
		</a>
		<%} else{%>
		0
		<%} %>
		</td>          
	</tr>
	<%} 
	}
	}else{%>
	<tr>
		<td>
		<%=branchObjectNow.getBranchname()%>
		</td>
		<td>
		<%if(exptionMap != null 
		&& ((Map)exptionMap.get(branchidNow)) != null
		&& ((Map)exptionMap.get(branchidNow)).get("youdanwuhuo_"+branchidNow) != null){ %>
		<a href="<%=request.getContextPath()%>/monitorEx/dateshow/7?flowType=<%=request.getAttribute("youdanwuhuoDateType")%>&branchPramid=<%=branchidNow%>">
		<%=((Map)exptionMap.get(branchidNow)).get("youdanwuhuo_"+branchidNow)%>
		</a>
		<%} else{%>
		0
		<%} %>
		</td>             
		<td>
		<%if(exptionMap != null 
		&& ((Map)exptionMap.get(branchidNow)) != null
		&& ((Map)exptionMap.get(branchidNow)).get("youhuowudan_"+branchidNow) != null){ %>
		<a href="<%=request.getContextPath()%>/monitorEx/dateshow/7?flowType=<%=request.getAttribute("youhuowudanDateType")%>&branchPramid=<%=branchidNow%>">
		<%=((Map)exptionMap.get(branchidNow)).get("youhuowudan_"+branchidNow)%>
		</a>
		<%} else{%>
		0
		<%} %>
		</td> 
		<td>
		<%if(exptionMap != null 
		&& ((Map)exptionMap.get(branchidNow)) != null
		&& ((Map)exptionMap.get(branchidNow)).get("yichangdan_"+branchidNow) != null){ %>
		<a href="<%=request.getContextPath()%>/monitorEx/dateshow/7?flowType=<%=request.getAttribute("yichangdanDateType")%>&branchPramid=<%=branchidNow%>">
		<%=((Map)exptionMap.get(branchidNow)).get("yichangdan_"+branchidNow)%>
		</a>
		<%} else{%>
		0
		<%} %>
		</td>          
	</tr>
	<%} %>
	</table>
</body>

</html>  