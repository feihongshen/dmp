<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>易普配送信息管理平台DMP</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script>
	$(function() {
		//$("#submitButton").button();
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
	<div ><img src="<%=request.getContextPath()%>/images/pylogo.png" width="217" height="64" alt="" /></div>
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
			<p><label for="textfield">&nbsp;</label><input type="submit" value="登录" class="login_formbutton"/>&nbsp;<input type="reset" value="重置" class="login_formbutton"/></p>
			<p><font color="red"></font></p>
		</form>
	</div>
</div>
<div class="login_bottomtxt">——同一用户名不允许多人登录使用 同一电脑不能开启多个IE登录使用——</div>
<div class="login_logo2"><img src="<%=request.getContextPath()%>/images/login_logo2.png" width="640" height="244" /></div>
<div class="login_bg2"></div>
</body>
</html>

