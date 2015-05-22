<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>站点日志（本站）</title>
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>

</head>
<%
List<WarehouseTodaylog> warehouseLogList = (List<WarehouseTodaylog>)request.getAttribute("warehouseLogList");
Map customerMap = (Map)request.getAttribute("customerMap");
List<Branch> branchnameList = (List<Branch>)request.getAttribute("kufangList");
%>
<script>
function dgetViewBox(key,durl){
	window.parent.getViewBoxd(key,durl);
}
</script>
<script>
$(function() {
	$("#createdate").datepicker();
});
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

</script>


</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="right_box">
	<div class="inputselect_box" style="top: 0px; ">
		
		<form action="<%=request.getContextPath()%>/warehouseLog/historylog" method="post" id="searchForm">
		   库房：<select name ="branchid" id="branid" class="select1" onchange="$('#searchForm').submit()">
	              <%if(branchnameList != null && branchnameList.size()>0){ %>
	               <%for( Branch b:branchnameList){ %>
	               		<option value ="<%=b.getBranchid()%>" 
	               		<%if(b.getBranchid() == new Long(request.getParameter("branchid")==null?"-1":request.getParameter("branchid"))) {%>selected="selected"<%} %>><%=b.getBranchname() %></option>
	               <%} }%>
	              </select>
	              &nbsp;&nbsp;
		选择日期： <input type ="text" name ="createdate" id="createdate" class="input_text1" style="height:20px;"
 value ="<%=request.getAttribute("createdate")==null || "".equals(request.getAttribute("createdate"))?
	 new SimpleDateFormat("yyyy-MM-dd").format(new Date()):request.getAttribute("createdate") %>">
		<input type="submit" name="button2" id="button2" value="查看" class="input_button2"> 
		<%if(warehouseLogList != null && warehouseLogList.size()>0){ %>
		当前查看生成的日志 ，所统计的时间范围是：<font color="red">
		<%=warehouseLogList.get(0).getStarttime() %></font>&nbsp; 到 &nbsp;  <font color="red">
		<%=warehouseLogList.get(0).getEndtime() %></font>
	 <%} %>
	</form>
</div>
  <div style="background:#FFF">
	<div class="jg_35"></div>
	<div class="tabbox">
	<div style="position:relative; z-index:0 " >
 	<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
 	
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
	   <tbody>
	   	<tr class="font_1" height="30" >
	   	<td width="20%" rowspan="2" align="center" valign="middle" bgcolor="#f3f3f3">供货商</td>
			<td width="20%"  rowspan="2" align="center" valign="middle" bgcolor="#E7F4E3">昨日库存</td>
			<td colspan="4" align="center" valign="middle" bgcolor="#E7F4E3">今日入库</td>
			<td width="10%" rowspan="2" align="center" valign="middle" bgcolor="#E1F0FF">今日出库</td>
			<td width="10%" valign="middle" rowspan="2" align="center" valign="middle" bgcolor="#E1F0FF"><p >今日库存</p>
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
	   		<td  width="10%" align="center" valign="middle" bgcolor="#E1F0FF"><p >漏扫已到站</p>
	   			<p >（单）</p></td> -->
	   		</tr>
	   	</tbody>
	</table>
	
    </div>
	<div style="height:84px"></div>
	<%if(warehouseLogList != null && warehouseLogList.size()>0){ %>
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
	  
	  <%for(WarehouseTodaylog w : warehouseLogList){ %>
	    <tr height="30">
	    	<td width="20%" align="center" valign="middle" bgcolor="#f3f3f3"><%=customerMap.get(w.getCustomerid()) %></td>
	    	<td width="20%" align="center" valign="middle"><%=w.getZuori_kucun() %></td>
	    	<td width="10%" align="center" valign="middle"><%=w.getJinri_weiruku() %></td>
	    	<td width="10%" align="center" valign="middle"><%=w.getJinri_yiruku() %></td>
	    	<td width="10%" align="center" valign="middle"><%=w.getJinri_daocuohuo() %></td>
	    	<td width="10%" align="center" valign="middle"><%=w.getJinri_yiruku()+w.getJinri_daocuohuo() %></td>
	    	<td width="10%" align="center" valign="middle"><%=w.getJinri_chukuzaitu() %></td>
	    	<td width="10%" align="center" valign="middle"><%=w.getJinri_kucun() %></td>
	    	  <%
	    	zuorikucunHeji += w.getZuori_kucun();  
	    	weirukuHeji += w.getJinri_weiruku(); 
	    	yirukuHeji += w.getJinri_yiruku(); 
	    	dacuohuHeji += w.getJinri_daocuohuo(); 
	    	rukuhejiHeji += w.getJinri_yiruku()+w.getJinri_daocuohuo(); 
	    	chukuzaituHeji += w.getJinri_chukuzaitu(); 
	    	dangqiankucunHeji += w.getJinri_kucun(); 
	      %>
	    	
    	</tr>
	  <%}%>
	  <tr height="30">
	    	<td width="20%" width="center"  valign="middle" bgcolor="#f3f3f3"><strong>合计</strong></td>
	    	<td width="20%" width="center"  valign="middle" bgcolor="#f3f3f3"><strong><%=zuorikucunHeji %></strong></td>
	    	<td align="center"  valign="middle" bgcolor="#f3f3f3"><strong><%=weirukuHeji %></strong></td>
	    	<td align="center"  valign="middle" bgcolor="#f3f3f3"><strong><%=yirukuHeji %></strong></td>
	    	<td align="center"  valign="middle" bgcolor="#f3f3f3"><strong><%=dacuohuHeji %></strong></td>
	    	<td align="center"  valign="middle" bgcolor="#f3f3f3"><strong><%=dacuohuHeji+yirukuHeji %></strong></td>
	    	<td align="center"  valign="middle" bgcolor="#f3f3f3"><strong><%=chukuzaituHeji %></strong></td>
	    	<td align="center"  valign="middle" bgcolor="#f3f3f3"><strong><%=dangqiankucunHeji %></strong></td>
    	</tr>  
	  
	   
	    </tbody>
	  </table>
	<% } %>

	</div>
	</div>
</div>
</div>
</body>
</html>