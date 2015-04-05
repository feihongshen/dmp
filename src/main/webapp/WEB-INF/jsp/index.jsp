<%@ taglib prefix="t" uri="/easyui-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
<head>
<title>唯易配送信息系统</title>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/dmp40/plug-in/jquery/jquery-1.8.3.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/dmp40/plug-in/tools/dataformat.js"></script>
<link id="easyuiTheme" rel="stylesheet"
	href="<%=request.getContextPath()%>/dmp40/plug-in/easyui/themes/default/easyui.css"
	type="text/css"></link>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/dmp40/plug-in/easyui/themes/icon.css"
	type="text/css"></link>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/dmp40/plug-in/accordion/css/accordion.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/dmp40/plug-in/easyui/jquery.easyui.min.1.3.2.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/dmp40/plug-in/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/dmp40/plug-in/tools/syUtil.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/dmp40/plug-in/easyui/extends/datagrid-scrollview.js"></script>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/dmp40/plug-in/jquery/jquery-autocomplete/jquery.autocomplete.css"
	type="text/css"></link>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/dmp40/plug-in/jquery/jquery-autocomplete/jquery.autocomplete.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/layer.min.js"></script>
<link rel="shortcut icon"
	href="<%=request.getContextPath()%>/dmp40/eap/sys/image/login/explink.ico">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/dmp40/plug-in/accordion/css/icons.css"
	type="text/css"></link>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/dmp40/plug-in/accordion/js/leftmenu.js"></script>
<style type="text/css">
.navbar {
	margin: 0px -2px 0px -2px;
}

.brand {
	text-overflow: ellipsis;
	white-space: nowrap;
}

.brand div {
	float: left;
	padding-right: 10px;
}

.brand h2 {
	font-size: 18px;
	margin-top: 20px;
}

.brand small {
	font-weight: normal;
}
</style>
<SCRIPT type="text/javascript">
	$(function() {

	});
</SCRIPT>
</head>
<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
	<!-- 顶部-->
	<div region="north" border="false" id="main_top_div" title="工具栏"
		style="height: 90px; overflow: hidden;">
		<div class="navbar" id="main_top_container">
			<div class="navbar-inner">
				<div class="container">
					<div class="brand">
						<div>
							<img
								src="<%=request.getContextPath()%>/dmp40/eap/sys/image/login/pylogo.png"
								alt="" style="height: 60px;">
						</div>
						<div>
							<h2>
								<label style="font-size: 1em;">唯易配送信息系统v4.1</label>
							</h2>
						</div>
					</div>
					<div class="pull-right" style="text-align: right;">
						<div>
							<a href="javascript:void(0);" class="easyui-menubutton"
								menu="#layout_north_kzmbMenu"><i class="icon-th"></i>控制面板</a> <a
								href="javascript:void(0);" class="easyui-menubutton"
								menu="#layout_north_zxMenu"><i class="icon-share-alt"></i>注销</a>
						</div>
						<div id="layout_north_kzmbMenu"
							style="width: 100px; display: none;">
							<div onclick="openwindow('用户信息','userController.do?userinfo')">
								<i class="icon-user" style="position: relative; left: -25px;"></i>个人信息
							</div>
							<div class="menu-sep"></div>
							<div onclick="add('修改密码','userController.do?changepassword')">
								<i class="icon-lock" style="position: relative; left: -25px;"></i>修改密码
							</div>
						</div>
						<div id="layout_north_zxMenu" style="width: 100px; display: none;">
							<div onclick="exit('loginController.do?logout','确定退出该系统吗 ?',1);">
								<i class="icon-off" style="position: relative; left: -25px;"></i>退出系统
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 左侧-->
	<div region="west" split="true" title="导航菜单"
		style="width: 150px; padding: 1px;">
		<t:menu></t:menu>
	</div>
	<!-- 中间-->
	<div id="mainPanle" region="center" style="overflow: hidden;">
		<div id="maintabs" class="easyui-tabs" fit="true" border="false">
			<div class="easyui-tab" title="首页" href=""
				style="padding: 2px; overflow-y: initial;"></div>
		</div>
	</div>
</body>
</html>