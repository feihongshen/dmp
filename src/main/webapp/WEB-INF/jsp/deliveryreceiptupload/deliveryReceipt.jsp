<%@ page import="cn.explink.util.Page"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page language="java" import="java.util.*"%>
<%
	Page page_obj = (Page) request.getAttribute("page_obj");
	String starttime = request.getParameter("starttime") == null
			? new SimpleDateFormat("yyyy-MM-dd").format(new Date())
			: request.getParameter("starttime");
	String endtime = request.getParameter("endtime") == null
			? new SimpleDateFormat("yyyy-MM-dd").format(new Date())
			: request.getParameter("endtime");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>交接单上传</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<style type="text/css">
.file {
	position: relative;
	display: inline-block;
	/* background: #D0EEFF; */
	border: 1px solid #99D3F5;
	border-radius: 4px;
	padding: 4px 12px;
	margin-bottom: -10px;
	overflow: hidden;
	color: #1E88C7;
	text-decoration: none;
	text-indent: 0;
	line-height: 20px;
	width: 120px;
}

.file input {
	position: absolute;
	font-size: 100px;
	right: 0;
	top: 0;
	opacity: 0;
}

.file:hover {
	background: #AADFFD;
	border-color: #78C3F3;
	color: #004974;
	text-decoration: none;
}

.btn {
	position: relative;
	display: inline-block;
	background: #D0EEFF;
	border: 1px solid #99D3F5;
	border-radius: 4px;
	padding: 4px 12px;
	margin-bottom: -10px;
	overflow: hidden;
	color: #1E88C7;
	text-decoration: none;
	text-indent: 0;
	line-height: 20px;
	width: 100px;
}
</style>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"
	type="text/javascript"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/js.js"></script>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css"
	type="text/css" media="all" />
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
<script type="text/javascript">
	$(function() {
		$("#starttime").datepicker();
		$("#endtime").datepicker();
		//显示最终发送请求的结果
		if ("${statecode}" != "") {
			alert("${statecode}");
			location.href="<%=request.getContextPath()%>/deliveryreceiptupload/deliveryReceipt/1?";
		}

		$(".check").click(function() {
			var mailNo = $(this).parent().next().text();
			var consigneeaddress = $(this).parent().parent().children().eq(2).text()
			if ($("#mailNo").val() == "" || $("#mailNo").val() == null) {
				$("#mailNo").val(mailNo);
				$("#cwb").val(mailNo);
				$("#consigneeaddress").val(consigneeaddress);
			} else {
				$("#mailNo").val($("#mailNo").val() + "," + mailNo)
			}

		})
		if ($("#starttime").val() == "") {
			alert("请选择开始时间");
			return false;
		}
		if ($("#endtime").val() == "") {
			alert("请选择结束时间");
			return false;
		}
		if ($("#starttime").val() > $("#endtime").val()
				&& $("#endtime").val() != '') {
			alert("开始时间不能大于结束时间");
			return false;
		}
	})

	//下面用于图片上传预览功能
	function setImagePreview(avalue) {
		var docObj = document.getElementById("doc");

		var imgObjPreview = document.getElementById("preview");
		if (docObj.files && docObj.files[0]) {
			//火狐下，直接设img属性
			/* imgObjPreview.style.display = 'block';
			imgObjPreview.style.width = '150px';
			imgObjPreview.style.height = '180px'; 
			imgObjPreview.src = docObj.files[0].getAsDataURL(); */
			//火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式
			imgObjPreview.src = window.URL.createObjectURL(docObj.files[0]);
		} else {
			//IE下，使用滤镜
			docObj.select();
			var imgSrc = document.selection.createRange().text;
			var localImagId = document.getElementById("localImag");
			//必须设置初始大小
			/* localImagId.style.width = "150px";
			localImagId.style.height = "180px"; */
			//图片异常的捕捉，防止用户修改后缀来伪造图片
			try {
				localImagId.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
				localImagId.filters
						.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc;
			} catch (e) {
				alert("您上传的图片格式不正确，请重新选择!");
				return false;
			}
			imgObjPreview.style.display = 'none';
			document.selection.empty();
		}
		return true;
	}
	function serch() {
		$('#uploadform').attr('action', 1);
		$("#uploadform").submit();
	}
	function submitupload() {
		if($('#cwb').val() != null && $('#cwb').val() != ""){
			if($('#doc').val() != null &&  $('#doc').val() != ""){
				$('#uploadform').attr('action','<%=request.getContextPath()%>/deliveryreceiptupload/uploadFileStream');
				$('#uploadform').attr('enctype', 'multipart/form-data');
				$('#uploadform').submit();
				$('#uploadform').removeAttr('enctype');
			}else{
				alert("请选择您要上传的照片");
			}
		}else{
			alert('请选择您要上传的订单信息');
		}
		
	}
</script>
</head>
<body style="background: #f5f5f5" marginwidth="0" marginheight="0">
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="saomiao_righttitle2" id="pagemsg"></div>
			<div class="saomiao_selet2">当前站点：</div>
			<div class="saomiao_inwrith2">
				<form
					action="<%=request.getContextPath()%>/deliveryreceiptupload/deliveryReceipt/<%=request.getAttribute("page") == null ? "1" : request.getAttribute("page")%>"
					method="post" id="uploadform">
					<table border="0" cellspacing="0" cellpadding="10">
						<tr height="70">
							<td width="410" align="left">收货单图片： <a href="javascript:;"
								class="file" style="width: 200px;">单机添加图片<input type="file"
									name="file_upload" id="doc" style="width: 150px"
									onchange="javascript:setImagePreview();"></a></td>
							<td width="240" align="left">订单编号：<input type="text"
								name="cwb" class="file" id="cwb" style="width: 150px;"/></td>
							<td rowspan="2" width="100" align="center"><input
								type="submit" id="find" onclick="serch()" value="查询" class="btn"
								style="margin-top: 30px;" /></td>
							<td rowspan="2" width="220" id="localImag" align="center"><input
								type="hidden" name="mailNo" id="mailNo"> <img
								id="preview"
								src="<%=request.getContextPath()%>/src/main/webapp/images/default.jpg"
								style="width: 200px; height: 100px; position: absolute; padding-top: 20px;">
								<input type="button" value="提交" class="btn"
								onclick="submitupload()"
								style="margin-left: 220px; margin-top: 30px;" /></td>
						</tr>
						<tr height="70" >
							<td align="left">订单导入时间： <input type="text" name="starttime"
								id="starttime" value="<%=starttime%>" class="file" />&nbsp;到&nbsp;<input
								type="text" name="endtime" id="endtimewei" value="<%=endtime%>"
								class="file" /></td>
							<td align="left">收货地址：<input type="text"
								name="consigneeaddress" class="file" id="consigneeaddress" style="width: 150px;"/></td>
						</tr>
					</table>
				</form>
				<div class="saomiao_right2">
					<p id="msg" name="msg"></p>
				</div>
			</div>
		</div>
	</div>
	<div>
		<div class="saomiao_tab2">
			<p style="padding-top: 10px;padding-left: 10px;">已反馈的订单：</p>
		</div>
		<div>
			<table width="100%" border="0" cellspacing="0" cellpadding="2"
				class="table_5">
				<tr>
					<td width="50" align="center" bgcolor="#f1f1f1">选择</td>
					<td width="140" align="center" bgcolor="#f1f1f1">订单号</td>
					<td width="300" align="center" bgcolor="#f1f1f1">收货地址</td>
					<td width="140" align="center" bgcolor="#f1f1f1">订单导入时间</td>
					<td width="140" align="center" bgcolor="#f1f1f1">收货人</td>
					<td width="140" align="center" bgcolor="#f1f1f1">收货人联系方式</td>
					<td width="150" align="center" bgcolor="#f1f1f1">派件员</td>
				</tr>
				<c:forEach items="${yishouhuolist }" var="ysh">
					<tr>
						<td align="center"><input type="checkbox" name="check"
							class="check" value="${ysh.cwb }"></td>
						<td align="center">${ysh.cwb }</td>
						<td align="center">${ysh.consigneeaddress }</td>
						<td align="center">${ysh.inputdatetime }</td>
						<td align="center">${ysh.consigneename }</td>
						<td align="center">${ysh.consigneephone }</td>
						<td align="center">${ysh.flowordertype }</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<%
			if (page_obj != null) {
				if (page_obj.getMaxpage() > 1) {
		%>
		<div class="iframe_bottom">
			<table width="100%" border="0" cellspacing="1" cellpadding="0"
				class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
						<a
						href="javascript:$('#uploadform').attr('action','1');$('#uploadform').submit();">第一页</a>
						<a
						href="javascript:$('#uploadform').attr('action','<%=page_obj.getPrevious() < 1 ? 1 : page_obj.getPrevious()%>');$('#uploadform').submit();">上一页</a>
						<a
						href="javascript:$('#uploadform').attr('action','<%=page_obj.getNext() < 1 ? 1 : page_obj.getNext()%>');$('#uploadform').submit();">下一页</a>
						<a
						href="javascript:$('#uploadform').attr('action','<%=page_obj.getMaxpage() < 1 ? 1 : page_obj.getMaxpage()%>');$('#uploadform').submit();">最后一页</a>
						共<%=page_obj.getMaxpage()%>页 共<%=page_obj.getTotal()%>条记录 当前第<select
						id="selectPg"
						onchange="$('#uploadform').attr('action',$(this).val());$('#uploadform').submit()">
							<%
								for (int i = 1; i <= page_obj.getMaxpage(); i++) {
							%>
							<option value="<%=i%>"><%=i%></option>
							<%
								}
							%>
					</select>页
					</td>
				</tr>
			</table>
		</div>
		<%
			}
			}
		%>
	</div>

</body>
</html>