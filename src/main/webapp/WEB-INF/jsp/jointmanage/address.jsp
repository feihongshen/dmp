
<%@page import="java.util.List"%>
<%@page import="cn.explink.b2c.tools.*"%>
<%@page import="cn.explink.pos.tools.*"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%
	List<JointEntity> posList = (List<JointEntity>) request
			.getAttribute("posList");
%>

<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css"
	type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/swfupload/swfupload.queue.js"></script>
<script src="<%=request.getContextPath()%>/js/js.js"
	type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/layer.min.js"></script>
<link id="skinlayercss" href="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/skin/layer.css" rel="stylesheet" type="text/css">
</head>
</head>
<script type="text/javascript">
	$(function() {
		$("#batchSyncAddress").click(function() {
			$.ajax({
				type: "POST",
		        url: "<%=request.getContextPath() %>/user/batchSyncAdress",
		        dataType: "json",
		        beforeSend: function() {
		        	$("#batchSyncAddress").val("同步中...");
		        	$("#batchSyncAddress").attr("disabled", true);
		        },
		        success: function(data) {
		        	$("#batchSyncAddress").val("同步");
		        	$("#batchSyncAddress").attr("disabled", false);
		        	if(data.code == 0) {
		        		var $sync_table = $("#sync_table");
			        	$sync_table.find("tr:gt(0)").remove();
			        	$.each(data.batchSyncAdressResultVoList, function(index, item) {
			        		$sync_table.append('<tr>'
									+ '<td valign="middle">' + item.username + '</td>'
									+ '<td valign="middle">' + item.realname + '</td>'
									+ '<td valign="middle">' + item.result  + '</td>'
									+ '<td valign="middle">' + item.message + '</td>'
									+ '</tr>');
			        	});
		        		$.layer({
		                    type: 1,
		                    title: "同步成功！成功：" + data.success + "，失败：" + data.failure + "。失败明细如下：",
		                    shadeClose: false,
		                    maxmin: false,
		                    fix: false,
		                    area: ['800px', '480px'],
		                    page: {
		                        dom: '#batchSyncAdressResult'
		                    }
		                });
		        	} else {
		        		alert(data.errorMsg);
		        	}
		        },
		        error: function() {
		        	$("#batchSyncAddress").val("同步");
		        	$("#batchSyncAddress").attr("disabled", false);
		        	alert("同步小件员时，发生了错误！"); 
		        }    
		    });
		});
	})
</script>
<body style="background: #f5f5f5">
	<div class="menucontant">
		<div class="uc_midbg">
			<ul>
				<li><a href="<%=request.getContextPath()%>/jointpower/">对接权限设置</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/jointb2c">电商对接</a></li>
				<li><a href="<%=request.getContextPath()%>/jointManage/jointpos">POS对接</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/poscodemapp/1">POS/商户映射</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/exptreason/1">异常码设置</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/exptcodejoint/1">异常码关联</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/epaiApi/1">系统环形对接</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/encodingsetting/1">供货商编码设置</a></li>
				<li><a href="#" class="light">地址库同步</a></li>
			</ul>
		</div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<form id="searchForm" action="#" method="post">
					<div class="right_box">
						<div class="right_title">
							<table width="100%" border="0" cellspacing="1" cellpadding="0"
								class="table_2" id="gd_table">
								<tr class="font_1">
									<td width="60%" align="center" valign="middle"
										bgcolor="#eef6ff">类型</td>
									<td width="40%" align="center" valign="middle"
										bgcolor="#eef6ff">操作</td>

								</tr>
								<tr>
									<td width="60%" align="center" valign="middle">批量同步小件员到地址库<font color="red">（同步需要一定时间，请耐心等待）</font></td>
									<td width="40%" align="center" valign="middle">
										<input type="button" value="同步" id="batchSyncAddress" class="input_button2" />
									</td>
							</table>
						</div>
					</div>
				</form>
				</td>
			</tr>
		</table>
		<div id="batchSyncAdressResult" style="display:none;">
			<table width="800" border="0" cellspacing="1" cellpadding="0" class="table_2" id="sync_table">
				<tr class="font_1">
					<td width="120" align="center" valign="middle" bgcolor="#eef6ff">登录名</td>
					<td width="120" align="center" valign="middle" bgcolor="#eef6ff">姓名</td>
					<td width="60" align="center" valign="middle" bgcolor="#eef6ff">结果</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">信息</td>
				</tr>
			</table>
		</div>
</body>
</html>
