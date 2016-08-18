<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>登录-配送信息系统</title>
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
	filter: alpha(opacity =                                                        
		 60);
	_bottom: auto;
	_width: 100%;
	_position: absolute;
	_top: expression(eval(document.documentElement.scrollTop + 
		             document.documentElement.clientHeight-this.offsetHeight-
		( 
		                          parseInt(this.currentStyle.marginTop, 10)||0)-(parseInt(this.currentStyle.marginBottom, 10)||0)));
}

.copyright {
	margin-top: 20px;
}

h1, h2, h3, h4, h5, h6 {
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
							<label style="font-size: 1.1em; color: #46affc">唯易配送信息系统</label>
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
					<td>
						<div id="loginlogo">
							<img
								src="<%=request.getContextPath()%>/dmp40/eap/sys/image/login/banner-login.png"
								alt="">
						</div>
					</td>
					<td>
						<div id="login" style="width: 350px;">
							<div class="inner">
								<div class="logo"
									style="width: 290px; margin-left: -65px; text-align: left;">
									<h4 class="header lighter bigger">
										<i class="ace-icon fa fa-coffee"
											style="color: #69aa46 !important;"></i> <small
											style="font-size: 1em; color: #69aa46; font-weight: normal;">系统登录</small>
									</h4>
								</div>
								<div class="formLogin" style="text-align: left">
									<form name="formLogin" id="formLogin"
										action="<%=request.getContextPath()%>/resources/j_spring_security_check"
										method="post">
										<div style="padding-left: 30px;" class="tip">
											<input class="userName" name="j_username" type="text"
												id="userName" title="用户名" iscookie="true" nullmsg="请输入用户名!"
												style="width: 200px; height: 25px" />												
											<label id="userName-error" class="error" for="userName" 
												style="color: red; font-size: 12px; text-decoration: none; display: inline-block; width: 90px; height: 25px">
												<%if (request.getParameter("login_error") != null
														&& request.getParameter("login_error").toString().equals("t")){
													%>${SPRING_SECURITY_LAST_EXCEPTION.message}<%
												}%>
											</label>
										</div>
										<div style="padding-left: 30px;" class="tip">
											<input class="password" name="j_password" type="password"
												id="password" title="密码" nullmsg="请输入密码!"
												style="width: 200px; height: 25px" />
										</div>
										<div style="padding-left: 30px;">
											<input name="validateCode" type="text"
												class="login_forminput" id="validateCode" maxlength="4"
												style="width: 120px; height: 25px" />&nbsp;&nbsp;
												<img id="randomImage" src="<%=request.getContextPath()%>/image?a=javascript:Math.random()">
												<a href="javascript:changeImg();" style="color: red; font-size: 12px; text-decoration: none;">换一张</a>
										</div>
										<div style="padding-left: 30px;">
											<input type="submit" value=" "
												style="background:url(<%=request.getContextPath()%>/dmp40/eap/sys/image/login/denglu.png);width:80px;height:33px;" />
											<input type="reset" value=" "
												style="background:url(<%=request.getContextPath()%>/dmp40/eap/sys/image/login/chongzhi.png);width:80px;height:33px;" />
										</div>
										<div class="clear"></div>
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
		
	<script type="text/javascript">	
	function changeImg(){
		$("#randomImage").attr("src","<%=request.getContextPath()%>/image?a="+Math.random());
	}
	
	//点击登录
	$("#formLogin").submit( function(event){
		if(isOtherUserExists()){
			if(!confirm("当前浏览器已有其它用户登录。请退出其它用户，然后点“确定”继续登录；或点“取消”退出。")){
				return false;
			}
		}
	});
	//是否已有用户登录
	function isOtherUserExists(){
		if(<%=(request.getSession() != null)%>
				&& <%=(request.getSession().getAttribute("SPRING_SECURITY_LAST_USERNAME") != null)%>
				&& "<%=request.getSession().getAttribute("SPRING_SECURITY_LAST_USERNAME")%>" != $('#userName').val()
		) {
			return true;
		}
		return false;
	}
  </script>
</body>
</html>