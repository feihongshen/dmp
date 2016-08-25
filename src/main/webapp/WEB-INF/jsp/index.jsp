<%@ taglib prefix="t" uri="/easyui-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.List,java.util.ArrayList,java.util.Map"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%
	Map usermap = (Map) session.getAttribute("usermap");
%>
<!DOCTYPE html >
<html>
<head>
<title>配送信息系统</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css"
	type="text/css" media="all" />

<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/plug-in/tools/dataformat.js"></script>
<link id="easyuiTheme" rel="stylesheet"
	href="<%=request.getContextPath()%>/dmp40/plug-in/easyui/themes/default/easyui.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/dmp40/plug-in/easyui/themes/icon.css"
	type="text/css"></link>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/dmp40/plug-in/accordion/css/accordion.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/dmp40/plug-in/easyui/jquery.easyui.min.1.3.2.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/dmp40/plug-in/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/plug-in/tools/syUtil.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/dmp40/plug-in/easyui/extends/datagrid-scrollview.js"></script>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/dmp40/plug-in/jquery/jquery-autocomplete/jquery.autocomplete.css"
	type="text/css"></link>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/dmp40/plug-in/jquery/jquery-autocomplete/jquery.autocomplete.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/layer.min.js"></script>
<link rel="shortcut icon" href="<%=request.getContextPath()%>/dmp40/eap/sys/image/login/explink.ico">
<link rel="stylesheet" href="<%=request.getContextPath()%>/dmp40/plug-in/accordion/css/icons.css"
	type="text/css"></link>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/dmp40/plug-in/accordion/js/leftmenu.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<!--快递操作  引用js -->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/src/jquery.tabs.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/express/feedback/comm.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/express/stationOperation.js"></script>
<script src="<%=request.getContextPath()%>/js/datePlugin/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/layer.min.js"></script>

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

#ui-datepicker-div {
	z-index: 1000000000000000000 !important;
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
							<img src="<%=request.getContextPath()%>/dmp40/eap/sys/image/login/pylogo.png" alt=""
								style="height: 60px;">
						</div>
						<div>
							<h2>
								<label style="font-size: 1.1em; color: #46affc">唯易配送信息系统</label>
							</h2>
						</div>
						<applet id="scaleApplet" name="scaleApplet" code="ScaleApplet.class"
							archive="ScaleApplet.jar,javax.comm.jar" width="1" height="1"> </applet>
					</div>
					<div class="pull-right" style="text-align: right; margin-right: 10px;">
						<div style="float: right; margin-top: 10px;">
							<table>
								<tr>
									<td colspan="3">
										<div style="float:right">
											<img src="<%=request.getContextPath()%>/images/horn.png" alt="" />
										</div>
										<div style="float:right">
											<marquee id="noticeMarquee" direction="left" behavior="scroll" scrollamount="3" onmouseover="this.stop();" onmouseout="this.start();" style="width:850px">
											</marquee>
										</div>
									</td>
								</tr>
								<tr>
									<td style="width:300">
									</td>
									<td><div>
											<strong>用户：<%=usermap.get("realname")%>&nbsp;&nbsp;部门：<%=usermap.get("branchname") == null ? "-" : usermap
					.get("branchname")%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											</strong>
											<lable> 快速查询： </lable>
											<input id="playSearch" type="text">
										</div></td>
									<td><div>
											<a href="javascript:void(0);" class="easyui-menubutton" menu="#layout_north_kzmbMenu"><i
												class="icon-th"></i>控制面板</a> <a href="javascript:void(0);" class="easyui-menubutton"
												menu="#layout_north_zxMenu"><i class="icon-share-alt"></i>注销</a>
										</div></td>
								</tr>
							</table>
						</div>
						<div id="layout_north_kzmbMenu" style="width: 100px; display: none;">
							<div
								onclick="addTab('下载管理','${omsUrl}/download/list/1?&dmpid=<%=session.getId() %>','folder')">
								<i class="icon-user" style="position: relative; left: -25px;"></i>下载管理
							</div>
							<div class="menu-sep"></div>
							<div onclick="addTab('通讯录','contact/list/1?&clickFunctionId=10000','folder')">
								<i class="icon-user" style="position: relative; left: -25px;"></i>通讯录
							</div>
							<div class="menu-sep"></div>
							<div onclick="addTab('修改密码','passwordupdate?&clickFunctionId=10001','folder')">
								<i class="icon-lock" style="position: relative; left: -25px;"></i>修改密码
							</div>
							<div class="menu-sep"></div>
							<div onclick="addTab('历史版本','taskShow/historyList/1?&clickFunctionId=10001','folder')">
								<i class="icon-lock" style="position: relative; left: -25px;"></i>历史版本
							</div>
						</div>
						<div id="layout_north_zxMenu" style="width: 100px; display: none;">
							<div
								onclick="if(confirm('确定退出系统吗？')){window.location='<%=request.getContextPath()%>/resources/j_spring_security_logout'}">
								<i class="icon-off" style="position: relative; left: -25px;"></i>退出系统
							</div>
						</div>
						<div id="dlg" class="easyui-dialog" title="新增版本发布说明" style="width:700px;height:550px;padding:10px" data-options="closed:true";>
					   		<div id="showDetail" style="width:600px;height:400px;padding:10px">
					   		</div>
					   		<!-- <div style="margin-bottom:10px;height:50px;float:bottom;" >
					   			<hr>
					   		    <div style="float:left"><input id="readBut" onclick="" type="checkbox">本人已阅读此版本发布说明</div>
					   		    <div style="float:right"><input id="closeBut" onclick="closeDlg()" type="button" value="关闭"></div>
					   		</div> -->
					    </div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 左侧-->
	<div region="west" split="true" title="导航菜单" style="width: 150px; padding: 1px;">
		<t:menu></t:menu>
	</div>
	<!-- 中间-->
	<div id="mainPanle" region="center" style="overflow: hidden;">
		<div id="maintabs" class="easyui-tabs" fit="true" border="false">
			<div class="easyui-tab" title="首页" href="" style="padding: 2px; overflow-y: initial;"></div>
		</div>
	</div>
	<iframe id="printOut" name="printOut" src="" width="500" height="500" style="display: none"
		frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes"></iframe>
	<iframe id="printcwb" name="printcwb" src="<%=request.getContextPath()%>/printcwb"
		target="printcwb" width="0" height="0" style="display: none" frameborder="0" scrolling="auto"
		marginheight="0" marginwidth="0" allowtransparency="yes"></iframe>
</body>
<script type="text/javascript">
	$("#playSearch").keydown(
			function(event) {
				if (event.keyCode == 13) {
					window.open ('<%=request.getContextPath() %>/order/queckSelectOrder/'+ this.value);
					$("#playSearch").val('');
					}
			});
	
	$(document).ready(function() {
		//点击对话框字段关闭按钮事件
		$('#dlg').dialog({
			onBeforeClose:function(){
				if($('#readBut').attr('checked')!='checked'){
					alert("必须勾选\"本人已阅读此版本的发布说明\",才能关闭！")
					return false;
				}
		    },
		    onClose:function(){
		    	sendReadRecord();
		    }
		});
		
		//获取最新版本说明
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			url : "<%=request.getContextPath()%>/taskShow/getLatestVersion",
			success : function(result) {
				if(result.latestVersion.isSuccess==false){
					return;
					//alert("从tps获取当前版本发布说明异常！")
				}else if(result.latestVersion.data!=null&&result.latestVersion.data.versionNo!=""){
					openWindow(result.latestVersion.data);
				}
				
			}
		});
	});
	
	//根据返回内容新建对话框
	function openWindow(data){
		var date = new Date(data.onlineTime);
		var updateTime = "";
		if(data.onlineTime!=null&&data.onlineTime!=undefined){
			Y = date.getFullYear() + '/';
			M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '/';
			D = date.getDate() + ' ';
			updateTime=Y+M+D;
		}
		var showTime = new Date().getTime();
		var divshow = $("#showDetail");
		divshow.append("<div><b>上线时间："+updateTime+"</b></div>");
		divshow.append("<div style='display:none' id='showTime'><b>"+showTime+"</b></div>");
		divshow.append("<div><b>版本名称："+data.name+"</b></div>");
		divshow.append("<div style='display:none' id='versionNo'><b>"+data.versionNo+"</b></div>");
		divshow.append("<br/>");
		divshow.append("<div>"+data.added+"</div>");
		divshow.append("<hr>");
		divshow.append("<div style=\"float:left\"><input id=\"readBut\" type=\"checkbox\">本人已阅读此版本发布说明</div>");
		divshow.append("<div style=\"float:right\"><input id=\"closeBut\" onclick=\"closeDlg()\" type=\"button\" value=\"关闭\"></div>");
		    
		
		$('#readBut').removeAttr('checked');
		$('#dlg').dialog('open');
	}
	
	//点击关闭按钮事件
	function closeDlg() {
		$('#dlg').dialog('close');
	}
	
	function sendReadRecord(){
		var versionNo = $("#versionNo").text();
		var showTime = $("#showTime").text();
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			data : {
				versionNo:versionNo,
				showTime:showTime
			},
			url : "<%=request.getContextPath()%>/taskShow/sendReadRecord",
			success : function(result) {
			}
		});
	}
	
	// added by wangwei, 20160823, 页面滚动公告栏, start
	$(function() {
		showNotice();
		var tenMinutes = 1000 * 60 * 10;		//每10分钟刷新一次
		setInterval("showNotice()", tenMinutes);
	}); 
	
	function showNotice(){
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/taskShow/getLatestNoticeContent",
			dataType:"json",
			success : function(data) {
				if(!!data && !!data.message){
					var noticeContent = data.message;
					var noticeContentHtml = '<font color="blue">' + noticeContent + '</font>';
					$('#noticeMarquee').html(noticeContentHtml)			
				}
			}                 
		});	
	}
	// added by wangwei, 20160823, 页面滚动公告栏, end
</script>
</html>