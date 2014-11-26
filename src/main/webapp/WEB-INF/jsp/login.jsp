<%@page import="cn.explink.util.ResourceBundleUtil"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.explink.util.ManifestUtil"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>易普配送信息管理平台DMP</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script>
if($("#WORK_AREA",parent.document).attr("src")){
	parent.location.reload();
}
	$(function() {
		var Sys = {};
	     var ua = navigator.userAgent.toLowerCase();
	     if (window.ActiveXObject)
	         Sys.ie = ua.match(/msie ([\d.]+)/)[1];
	     else if (document.getBoxObjectFor)
	         Sys.firefox = ua.match(/firefox\/([\d.]+)/)[1];
	     else if (window.MessageEvent && !document.getBoxObjectFor)
	         Sys.chrome = ua.match(/chrome\/([\d.]+)/)[1];
	     else if (window.opera)
	         Sys.opera = ua.match(/opera.([\d.]+)/)[1];
	     else if (window.openDatabase)
	         Sys.safari = ua.match(/version\/([\d.]+)/)[1];
	     if(Sys.chrome||Sys.opera||Sys.safari){
	    	
	    	 $("#info").html("为了您更好的体验易派系统&nbsp;&nbsp;建议您使用IE8或火狐浏览器 ");
	     }
	    
		$("#loginform").submit(function(){
			if($("#username").val()==""){
				alert("用户名不能为空");
				return false;
			}
			if($("#password").val()==""){
				alert("密码不能为空");
				return false;
			}
		});
      
	});
</script>
</head>
<body bgcolor="#3c7fb5" style="position:relative; overflow:hidden">

<div class="login_bodybg">
	<div ><img src="<%=request.getContextPath()%>/images/pylogo.png" /></div>
</div>

<div class="login_formbg">
	<div class="login_form">
		<form id="loginform" action="<%=request.getContextPath()%>/resources/j_spring_security_check" method="post">
			<p>
				<label for="textfield">用户：</label>
				<input  type="text" id="username" name="j_username" class="login_forminput" value="${sessionScope['SPRING_SECURITY_LAST_USERNAME']}"/>
			</p>
			<p>
				<label for="textfield">密码：</label>
				<input name="j_password" type="password" id="password" maxlength="30" class="login_forminput"/>
			</p>
			<p>
				<label for="textfield">验证码：</label>
				<input name="validateCode" type="text" class="login_forminput" id="validateCode" maxlength="30" class="login_forminput"/>
				<img src="<%=request.getContextPath()%>/image?a=<%=System.currentTimeMillis() %>">
			</p>
			<p><label for="textfield">&nbsp;</label><input type="submit" value="登录" class="login_formbutton"/>&nbsp;<input type="reset" value="重置" class="login_formbutton"/></p>
			<p><font color="red">${SPRING_SECURITY_LAST_EXCEPTION.message}</font></p>
		</form>
	</div>
	<div style="font-size:24px;color:#FFF"class="login_form" ><span id="info"></span> </div>
</div>

<div class="login_bottomtxt">　</div><!-- ——同一用户名不允许多人登录使用 同一电脑不能开启多个IE登录使用—— -->
<div class="login_logo2">
<%if(ResourceBundleUtil.LOGOSWITCH.equals("1")){ %>
			<img src="<%=request.getContextPath()%>/<%=ResourceBundleUtil.LOGOIMGURL %>" />
		<%} %>
</div>
<div class="login_bg2"></div>
<div style="position:absolute; z-index:999; right:10px; bottom:10px; color:#FFF">版本：<%=ManifestUtil.getSvnRevision(request) %></div>

</body>
</html>

