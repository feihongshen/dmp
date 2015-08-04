<%@ taglib prefix="t" uri="/easyui-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.List,java.util.ArrayList,java.util.Map"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%
	Map usermap = (Map) session.getAttribute("usermap");
%>
<!DOCTYPE html >
<html>
<head>
<title>配送信息系统</title>
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<script type="text/javascript"	src="<%=request.getContextPath()%>/dmp40/plug-in/tools/dataformat.js"></script>
<link id="easyuiTheme" rel="stylesheet"	href="<%=request.getContextPath()%>/dmp40/plug-in/easyui/themes/default/easyui.css"	type="text/css"></link>
<link rel="stylesheet"	href="<%=request.getContextPath()%>/dmp40/plug-in/easyui/themes/icon.css"	type="text/css"></link>
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/dmp40/plug-in/accordion/css/accordion.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/dmp40/plug-in/easyui/jquery.easyui.min.1.3.2.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/dmp40/plug-in/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/dmp40/plug-in/tools/syUtil.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/dmp40/plug-in/easyui/extends/datagrid-scrollview.js"></script>
<link rel="stylesheet"	href="<%=request.getContextPath()%>/dmp40/plug-in/jquery/jquery-autocomplete/jquery.autocomplete.css"	type="text/css"></link>
<script type="text/javascript"	src="<%=request.getContextPath()%>/dmp40/plug-in/jquery/jquery-autocomplete/jquery.autocomplete.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/layer.min.js"></script>
<link rel="shortcut icon"	href="<%=request.getContextPath()%>/dmp40/eap/sys/image/login/explink.ico">
<link rel="stylesheet"	href="<%=request.getContextPath()%>/dmp40/plug-in/accordion/css/icons.css"	type="text/css"></link>
<script type="text/javascript"	src="<%=request.getContextPath()%>/dmp40/plug-in/accordion/js/leftmenu.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js"	type="text/javascript"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/express/takeExpressAssign.js"></script>

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

</head>
<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
	<audio id="wavPlay1"></audio>
	<!-- 弹出对话框-->
	<div id="alert_box"></div>
	<div class="tishi_box"></div>
	<!-- 顶部-->
	<div region="north" border="false" id="main_top_div" title="工具栏"
		style="height: 90px; overflow: hidden;">
		<div class="navbar" id="main_top_container">
			<div class="navbar-inner">
				<div class="container">
					<div class="brand">
						<div style="margin-left: 10px;">
							<img
								src="<%=request.getContextPath()%>/dmp40/eap/sys/image/login/pylogo.png"
								alt="" style="height: 60px;">
						</div>
						<div>
							<h2>
								<label style="font-size: 1.1em; color: #46affc">唯易配送信息系统</label>
							</h2>

						</div>
					</div>
					<div class="pull-right"
						style="text-align: right; margin-right: 10px;">
						<div style="float: right; margin-top: 10px;">
							<table>
								<tr>
									<td><div>
											<strong>用户：<%=usermap.get("realname")%>&nbsp;&nbsp;部门：<%=usermap.get("branchname") == null ? "-" : usermap
					.get("branchname")%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											</strong>
											<lable> 快速查询： </lable>
											<input id="playSearch" type="text">
										</div></td>
									<td><div>
											<a href="javascript:void(0);" class="easyui-menubutton"
												menu="#layout_north_kzmbMenu"><i class="icon-th"></i>控制面板</a>
											<a href="javascript:void(0);" class="easyui-menubutton"
												menu="#layout_north_zxMenu"><i class="icon-share-alt"></i>注销</a>
										</div></td>
								</tr>
							</table>
						</div>
						<div id="layout_north_kzmbMenu"
							style="width: 100px; display: none;">
							<div
								onclick="addTab('下载管理','${omsUrl}/download/list/1?&dmpid=<%=session.getId() %>','folder')">
								<i class="icon-user" style="position: relative; left: -25px;"></i>下载管理
							</div>
							<div class="menu-sep"></div>
							<div
								onclick="addTab('通讯录','contact/list/1?&clickFunctionId=10000','folder')">
								<i class="icon-user" style="position: relative; left: -25px;"></i>通讯录
							</div>
							<div class="menu-sep"></div>
							<div
								onclick="addTab('修改密码','passwordupdate?&clickFunctionId=10001','folder')">
								<i class="icon-lock" style="position: relative; left: -25px;"></i>修改密码
							</div>
						</div>
						<div id="layout_north_zxMenu" style="width: 100px; display: none;">
							<div
								onclick="if(confirm('确定退出系统吗？')){window.location='<%=request.getContextPath()%>/resources/j_spring_security_logout'}">
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
	<iframe id="printOut" name="printOut" src="" width="500" height="500"
		style="display: none" frameborder="0" scrolling="auto"
		marginheight="0" marginwidth="0" allowtransparency="yes"></iframe>
	<iframe id="printcwb" name="printcwb"
		src="<%=request.getContextPath()%>/printcwb" target="printcwb"
		width="0" height="0" style="display: none" frameborder="0"
		scrolling="auto" marginheight="0" marginwidth="0"
		allowtransparency="yes"></iframe>
</body>
<script type="text/javascript">
	$("#playSearch").keydown(
			function(event) {
				if (event.keyCode == 13) {
					window.open ('<%=request.getContextPath() %>/order/queckSelectOrder/'+ this.value);
					$("#playSearch").val('');
				}
			});
</script>
</html>