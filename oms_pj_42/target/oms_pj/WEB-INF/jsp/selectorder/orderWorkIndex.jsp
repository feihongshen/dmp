<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>

<script>
function dgetViewBox(key,durl){
	window.parent.getViewBoxd(key,durl);
}

</script>
<script type="text/javascript">
$(function(){
$("#right_hideboxbtn").click(function(){
			var right_hidebox = $("#right_hidebox").css("right");
			if(
				right_hidebox == -400+'px'
			){
				$("#right_hidebox").css("right","10px");
				$("#right_hideboxbtn").css("background","url(../../images/right_hideboxbtn2.gif)");
				
			};
			
			if(right_hidebox == 10+'px'){
				$("#right_hidebox").css("right","-400px");
				$("#right_hideboxbtn").css("background","url(../../images/right_hideboxbtn.gif)");
			};
	});
});
</script>

</HEAD>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="right_box">
	<div class="menucontant">
		<table width="100%" border="0" cellspacing="10" cellpadding="0">
			<tbody>
				<tr>
					<td valign="top"><iframe src="<%=request.getContextPath()%>/order/selectOrder/1?cwb=<%=request.getAttribute("cwb")%>" scrolling="no" height="495" width="100%" marginheight="0" marginwidth="0" frameborder="0" id="WORK_AREA_RIGHT"></iframe></td>
					<td width="400" valign="top" id="right_hidebox"><div id="right_hideboxbtn"></div>
						<div class="right_scrollbox">
							<iframe src="<%=request.getContextPath()%>/order/selectAll/1" scrolling="auto" height="493" width="100%" marginheight="0" marginwidth="0" frameborder="0" id="WORK_AREA_LEFT"></iframe>
						</div></td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
</body>
</html>