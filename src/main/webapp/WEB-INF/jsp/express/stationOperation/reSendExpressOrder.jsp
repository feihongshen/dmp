<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="cn.explink.enumutil.express.SendTpsExpressOrderResultEnum"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//Dth HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dth">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运单重发页面</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css"
	type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<style type="text/css">
.tdright {
	text-align: right;
}

.table1 tr {
	line-height: 35px;
}

.tdleft {
	text-align: left;
}

.table1 table {
	border: 1px solid #ccc;
	border-collapse: collapse;
}
</style>
<script type="text/javascript">
/*
 * 初始化日期插件
 */
$(function(){
	 $('#beginTime_id').datepicker();
	 $('#endTime_id').datepicker();  
});

/**
 * 设置颜色
 */
function initColor(td_$){
	if(td_$.checked==null||td_$.checked==undefined){
		td_$.checked = false;
	}
	if(td_$.checked){
		$(td_$).parent().parent().css("background-color","#FFD700");
	}else{
		$(td_$).parent().parent().css("background-color","#FFFFFF");
	}
}

//全选
function checkAll(id){ 
	var chkAll = $("#"+ id +" input[type='checkbox'][name='checkAll']")[0].checked;
	var chkBoxes = $("#"+ id +" input[type='checkbox'][name='checkBox']");
	$(chkBoxes).each(function() {
		$(this)[0].checked = chkAll;
	});
}
/*
 *重发逻辑 
 */
function reSend(id){
	var ids = [];
	//拿到操作的id
	var chkBoxes = $("#"+ id +" input[type='checkbox'][name='checkBox']");
	if($("#opeFlag_id").val() == <%=SendTpsExpressOrderResultEnum.Success.getValue()%>){
		alert("改运单已经成功发送信息，不需要重新发送！");
		return;
	}
	$(chkBoxes).each(function() {
		if($(this)[0].checked){
			//ids +=$(this).val()+ ",";
			ids.push($(this).val());
		}
	});
	if(ids.length==0){
		alert("请选中要操作的数据 ！");
		return;
	}
	//请求后台操作
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url :  "<%=request.getContextPath()%>/reSendExpressOrderController/reSend",
		data : {
			"ids" : ids.join()
		},
		datatype : "json",
		success : function(data) {
			alert("重发成功的运单有："+data.success+"，重发失败的运单有："+data.failure);
			window.location.reload();
		}
	});
}
</script>

</head>
<body>
	<div>
		<form action="${requestScope.page == null ? '1' : requestScope.page }" id=PageFromW method="post">
			<table width="100%" class="table1">
				<colgroup>
					<col width="10%">
					<col width="15%">
					<col width="5%">
					<col width="10%">
					<col width="15%">
					<col width="10%">
					<col width="10%">
					<col width="10%">
					<col width="7%">
					<col width="8%">
				</colgroup>
				<tr>
					<td class="tdright">创建时间:</td>
					<td class="tdleft"><input type="text" name="beginTime" id="beginTime_id" class="input_text1" value="${beginTime}" /></td>
					<td >到</td>
					<td class="tdleft">
						<input type="text" name="endTime" id="endTime_id"  class="input_text1" value="${endTime}" />
					</td>
					<td class="tdright">运单号:</td>
					<td class="tdleft"><input type="text" name="transNo" id="transNo_id"
						class="input_text1" value="${transNo}" /></td>
					<td class="tdright">执行状态:</td>
					<td class="tdleft">
						<select name="opeFlag" id="opeFlag_id" class="select1" readonly="readonly">
							<c:forEach items="${ResultEnum}" var="list">
								<c:if test="${list.value == opeFlag}">
									<option value="${list.value}" selected="true">${list.text}</option>
								</c:if>
								<c:if test="${list.value != opeFlag}">
									<option value="${list.value}">${list.text}</option>
								</c:if>
							</c:forEach>
						</select>
					</td>
					<td class="tdright"><input type="submit" value="查询" onclick="$('#PageFromW').attr('action',1);return true;"/></td>
					<td class="tdright"><input type="button" value="重发" onclick="reSend('callertb')"/></td>
				</tr>
			</table>
		</form>
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="callertb">
			<colgroup>
				<col width="5%">
				<col width="15%">
				<col width="15%">
				<col width="20%">
				<col width="45%">
			</colgroup>
			<tr class="font_1">
				<td height="30px"  valign="middle"><input type="checkbox" name="checkAll" onclick="checkAll('callertb')"/></td>
				<th bgcolor="#eef6ff">运单号</th>
				<th bgcolor="#eef6ff">接口调用状态</th>
				<th bgcolor="#eef6ff">创建时间</th>
				<th bgcolor="#eef6ff">异常</th>
			</tr>

			<c:forEach items="${list}" var="list">
				<tr>
					<td height="30px" align="center"  valign="middle">
						<input type="checkbox" name="checkBox" value="${list.id}" onclick="initColor(this);"/>
					</td>
					<td>${list.transNo}</td>
					<td>
						<c:forEach items="${ResultEnum}" var="list1">
							<c:if test="${list1.value == list.opeFlag}">
								${list1.text}
							</c:if>
						</c:forEach>
					</td>
					<td>${list.createTime}</td>
					<td>${list.errMsg}</td>
				</tr>
			</c:forEach>
		</table>
		<div class="iframe_bottom">
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1" id="pageid">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff"><a
						href="javascript:$('#PageFromW').attr('action','1');$('#PageFromW').submit();">第一页</a> <a
						href="javascript:$('#PageFromW').attr('action','${page_obj.previous < 1 ? 1 : page_obj.previous}');$('#PageFromW').submit();">上一页</a>
						<a
						href="javascript:$('#PageFromW').attr('action','${page_obj.next < 1 ? 1 : page_obj.next}');$('#PageFromW').submit();">下一页</a>
						<a
						href="javascript:$('#PageFromW').attr('action','${page_obj.maxpage < 1 ? 1 : page_obj.maxpage}');$('#PageFromW').submit();">最后一页</a>
						共${page_obj.maxpage}页 共${page_obj.total}条记录 当前第 <select id="selectPg"
						onchange="$('#PageFromW').attr('action',$(this).val());$('#PageFromW').submit()">
							<c:forEach begin="1" end="${page_obj.maxpage}" var="i">
								<c:if test="${page == i}">
									<option value="${i}" selected="true">${i}</option>
								</c:if>
								<c:if test="${page != i}">
									<option value="${i}">${i}</option>
								</c:if>
							</c:forEach>
					</select>页</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>