<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>登录-唯易配送信息系统</title>
<link
	href="<%=request.getContextPath()%>/dmp40/plug-in/login/css/zice.style.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/dmp40/plug-in/login/css/buttons.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/dmp40/plug-in/login/css/icon.css"
	rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/dmp40/plug-in/login/css/tipsy.css"
	media="all" />
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/dmp40/plug-in/bootstrap/css/bootstrap.min.css">
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/dmp40/plug-in/bootstrap/css/bootstrap-responsive.min.css">
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/font-awesome/css/font-awesome.min.css">
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/ace/ace.css">
<style type="text/css">
label.iPhoneCheckLabelOn span {
	padding-left: 0px
}

#versionBar {
	background-color: #212121;
	position: fixed;
	width: 100%;
	height: 35px;
	bottom: 0;
	left: 0;
	text-align: center;
	line-height: 35px;
	z-index: 11;
	-webkit-box-shadow: black 0px 10px 10px -10px inset;
	-moz-box-shadow: black 0px 10px 10px -10px inset;
	box-shadow: black 0px 10px 10px -10px inset;
}

.copyright {
	text-align: center;
	font-size: 10px;
	color: #CCC;
}

.copyright a {
	color: #A31F1A;
	text-decoration: none
}

.on_off_checkbox {
	width: 0px;
}

#login .logo {
	width: 500px;
	height: 51px;
}

#login {
	border: 0px;
}

#login input {
	background-position: 2px 8px;
}

.navbar-inner {
	-webkit-border-radius: 0px;
}

.brand {
	text-overflow: hidden;
	white-space: nowrap;
}

.brand div {
	float: left;
	padding-right: 10px;
}

.brand h2 {
	font-size: 18px;
	margin-top: 20px;
	border-bottom: 0px;
}

.brand small {
	font-weight: normal;
}

.brand label {
	font-weight: bold;
}

.container table {
	margin-top: 100px;
}

.formLogin {
	
}

.header {
	line-height: 28px;
	margin-bottom: 16px;
	margin-top: 18px;
	padding-bottom: 4px;
	border-bottom: 1px solid #CCC;
}

.inner {
	-webkit-border-radius: 0px !important;
	-moz-border-radius: 0px !important;
	border-radius: 0px !important;
	-webkit-box-shadow: 0px 0px 3px -1px;
	-moz-box-shadow: 0px 0px 3px -1px;
	box-shadow: 0px 0px 3px -1px;
}

.login-footer {
	position: fixed;
	bottom: 0;
	background: #444;
	width: 100%;
	height: 60px;
	line-height: 23px;
	z-index: 9999;
	opacity: .60;
	filter: alpha(opacity =         60);
	_bottom: auto;
	_width: 100%;
	_position: absolute;
	_top: expression(eval(document.documentElement.scrollTop +     
		     document.documentElement.clientHeight-this.offsetHeight- (     
		   parseInt(this.currentStyle.marginTop, 10)||0 )-(parseInt(this.currentStyle.marginBottom, 10)||0
		) ) );
}

.copyright {
	margin-top: 20px;
}

h1,h2,h3,h4,h5,h6 {
	font-family: "Microsoft YaHei", STHeiti, SimHei, "STHeiti Light",
		Arial !important;
}

#preloader {
	width: 100px;
}
</style>
</head>
<body style="background-color: #efefef;">
	<div id="alertMessage"></div>
	<div id="successLogin"></div>
	<div class="text_success">
		<img
			src="<%=request.getContextPath()%>/dmp40/plug-in/login/images/loader_green.gif"
			alt="Please wait" /> <span>登陆成功!请稍后....</span>
	</div>
	<div class="navbar">
		<div class="navbar-inner">
			<div class="container">
				<div class="brand">
					<div>
						<img
							src="<%=request.getContextPath()%>/dmp40/eap/sys/image/login/pylogo.png"
							alt="" style="height: 70px;">
					</div>
					<div>
						<h2>
							<label style="font-size: 1em;">唯易配送信息系统v4.1</label>
						</h2>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="container">
			<table>
				<tr>
					<td><img
						src="<%=request.getContextPath()%>/dmp40/eap/sys/image/login/banner-login.png"
						alt=""></td>
					<td>
						<div id="login" style="width: 330px;">
							<div class="inner">
								<div class="logo"
									style="width: 290px; margin-left: -65px; text-align: left;">
									<h4 class="header lighter bigger">
										<i class="ace-icon fa fa-coffee"
											style="color: #69aa46 !important;"></i> <small
											style="font-size: 1em; color: #69aa46; font-weight: normal;">系统登陆</small>
									</h4>
								</div>
								<div class="formLogin">
									<form name="formLogin" id="formLogin"
										action="loginController.do?login"
										check="loginController.do?checkuser" method="post">
										<input name="userKey" type="hidden" id="userKey"
											value="D1B5CC2FE46C4CC983C073BCA897935608D926CD32992B5900" />
										<div class="tip">
											<input class="userName" name="userName" type="text"
												id="userName" title="用户名" iscookie="true" nullmsg="请输入用户名!" />
										</div>
										<div class="tip">
											<input class="password" name="password" type="password"
												id="password" title="密码" nullmsg="请输入密码!" />
										</div>
										<div class="loginButton">
											<div>
												<button type="button"
													class="width-35 pull-right btn btn-sm btn-primary"
													id="but_login">
													<i class="ace-icon fa fa-key"></i> <span class="bigger-110">登录</span>
												</button>
											</div>
											<div class="clear"></div>
										</div>
									</form>
								</div>
							</div>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="row login-footer" style="margin: 0px;">
		<div class="container copyright">
			<label>©2015&nbsp;北京易普联科信息技术有限公司</label>
		</div>
	</div>
	<!-- Link JScript-->
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/dmp40/plug-in/jquery/jquery-1.8.3.min.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/dmp40/plug-in/jquery/jquery.cookie.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/dmp40/plug-in/login/js/jquery-jrumble.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/dmp40/plug-in/login/js/jquery.tipsy.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/dmp40/plug-in/login/js/iphone.check.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/dmp40/plug-in/login/js/login.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/dmp40/plug-in/lhgDialog/lhgdialog.min.js"></script>
</body>
</html>