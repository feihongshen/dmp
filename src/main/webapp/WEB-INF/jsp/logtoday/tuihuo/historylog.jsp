<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>站点日志（本站）</title>
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>

</head>
<%
List<TuihuoSiteTodaylog> tList = (List<TuihuoSiteTodaylog>)request.getAttribute("tuihuoSiteTodaylogList");
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
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="right_box">
	<div class="inputselect_box" style="top: 0px; ">
		
		<form action="<%=request.getContextPath()%>/tuihuoLog/historylog" method="post" id="searchForm">
		&nbsp;&nbsp;
		选择日期： <input type ="text" name ="createdate" id="createdate" 
 value ="<%=request.getAttribute("createdate")==null || "".equals(request.getAttribute("createdate"))?
	 new SimpleDateFormat("yyyy-MM-dd").format(new Date()):request.getAttribute("createdate") %>">
		<input type="submit" name="button2" id="button2" value="查看" class="input_button2"> 
		
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
			   		<td  align="center" valign="middle" >站点应退</td>
					<td   align="center" valign="middle" >退货出站在途</td>
					<td  align="center" valign="middle" >退货站入库</td>
					<td  align="center" valign="middle" >退货站出库</td>
					<td  align="center" valign="middle" >退供货商出库</td>
					<td   align="center" valign="middle" >供货商确认退货</td>
					<td  align="center" valign="middle" >供货商拒收返库</td>
					<td  align="center" valign="middle" >统计开始时间</td>
					<td  align="center" valign="middle" >日志生成时间</td>
				</tr>
			   <%if(tList != null && tList.size()>0){ %>
			   <%for(TuihuoSiteTodaylog tveiw:tList){ %>
			   	<tr class="font_1" height="30" >
			   		<td  align="center" valign="middle" ><%=tveiw.getZhandianyingtui() %></td>
					<td  align="center" valign="middle" ><%=tveiw.getZhandiantuihuochukuzaitu() %></td>
					<td  align="center" valign="middle" ><%=tveiw.getTuihuozhanruku() %></td>
					<td  align="center" valign="middle" ><%=tveiw.getTuihuozhantuihuochukuzaitu() %></td>
					<td  align="center" valign="middle" ><%=tveiw.getTuigonghuoshangchuku() %></td>
					<td  align="center" valign="middle"><%=tveiw.getGonghuoshangshouhuo() %></td>
					<td  align="center" valign="middle" ><%=tveiw.getGonghuoshangjushoufanku() %></td>
					<td  align="center" valign="middle" ><%=tveiw.getStarttime() %></td>
					<td  align="center" valign="middle" ><%=tveiw.getEndtime() %></td>
				</tr>
				<%} }%>
			   	</tbody>
		</table>

	</div>
	</div>
</div>
</div>
</body>
</html>