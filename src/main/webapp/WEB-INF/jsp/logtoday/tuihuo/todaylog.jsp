<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>库房今日日志</title>
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/tuihuoLog.js" type="text/javascript"></script>
</head>
<%
TuihuoSiteTodaylog tveiw = (TuihuoSiteTodaylog)request.getAttribute("tuihuoSiteTodaylogview");
List<Branch> branchnameList = (List<Branch>)request.getAttribute("tuihuoList");

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
		getYingtuiCount('<%=request.getContextPath() %>','yingtuiCount');
});
</script>


</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="right_box">
	<div class="inputselect_box" style="top: 0px; ">
	         &nbsp;&nbsp;当前日期：<%=new SimpleDateFormat("yyyy-MM-dd").format(new Date()) %> &nbsp;&nbsp; 当前统计的是从
			<font color="red"><%=request.getAttribute("startTime") %></font> 到当前时间
</div>
  <div style="background:#FFF">
	<div style="height:25px"></div>
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
				</tr>
			   	<tr class="font_1" height="30" >
			   		<td  align="center" valign="middle" id="yingtuiCount"><a href="<%=request.getContextPath()%>/tuihuoLog/show/zhandianyingtuihuo/1" ><%=tveiw.getZhandianyingtui() %></a></td>
					<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/tuihuoLog/show/zhandiantuihuochukuzaitu/1" ><%=tveiw.getZhandiantuihuochukuzaitu() %></a></td>
					<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/tuihuoLog/show/tuihuozhanruku/1" ><%=tveiw.getTuihuozhanruku() %></a></td>
					<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/tuihuoLog/show/tuihuozhantuihuochukuzaitu/1" ><%=tveiw.getTuihuozhantuihuochukuzaitu() %></a></td>
					<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/tuihuoLog/show/tuigonghuoshangchuku/1" ><%=tveiw.getTuigonghuoshangchuku() %></a></td>
					<td  align="center" valign="middle"><a href="<%=request.getContextPath()%>/tuihuoLog/show/gonghuoshangshouhuo/1" ><%=tveiw.getGonghuoshangshouhuo() %></a></td>
					<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/tuihuoLog/show/gonghuoshangjushoufanku/1" ><%=tveiw.getGonghuoshangjushoufanku() %></a></td>
				</tr>
			   	</tbody>
			</table>
		    </div>
		</div>
	</div>
</div>
</div>
</body>
</html>