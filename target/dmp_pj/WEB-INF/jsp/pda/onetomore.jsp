<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>一票多件补入</title>
<script type="text/javascript">

<%
String message=request.getAttribute("message")==null?null:(String)request.getAttribute("message");
	if(message!=null){
%>
	alert('<%=message%>');
<%}%>
	function cwbtranscwb(){
		var cwb = $("#cwb").val();
		var transcwb = $("#transcwb").val();
		console.info(cwb);
		console.info(transcwb);
		console.info(transcwb.length);
		console.info(cwb.length);
		console.info(cwb==null);
		if(cwb==null||cwb.length==0){
			alert("请输入订单号");
			return false;
		}else if(transcwb==null||transcwb.length==0){
			alert("请输入运单号");
			return false;
		}else if((cwb==null||cwb.length==0)&&(transcwb==null||transcwb.length==0)){
			alert("请输入订单号和运单号");
			return false;
		}
		return true;
	}
	
	function showview(which){
		if(which=='show'){
			$("#showorhide").style.display="block";
		}else{
			$("#showorhide").style.display="none";
		}
	} 
	
</script>

</head>
<body>
	<form onsubmit="return cwbtranscwb();" action="<%=request.getContextPath()%>/PDA/oneTOmore" method="post">
		<h2>此功能用来填补一票多件问题中对接运单数据不全的问题</h2>
		<span>订单号：</span>
		<input type="text" id="cwb" name="cwb" value="<%=request.getParameter("cwb")==null?"":request.getParameter("cwb") %>"/></br>
		<p></p>
		<span>运单号：</span>
		<%--<input type="text" name="transcwb" value="<%=transcwb%>"/></br>--%>
		<textarea name="transcwb" id="transcwb" rows="4" cols="15" class="text"><%=request.getParameter("transcwb")==null?"":request.getParameter("transcwb") %></textarea> 
		<input type="submit" value="补入" ></br>
		<hr>
		<%-- <div ><%=message==null?"":message %></div> --%>
		<!-- <span onmouseover="showview('show')" onmouseout="showview('hide') " id="showorhide">提示!
			<div id="attention">
				此功能用来填补一票多件问题中对接运单数据不全的问题！
			</div>
		</span> -->
	</form>
</body>
</html>