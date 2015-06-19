<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>赔付类型管理</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css" type="text/css"  />
<script src="${pageContext.request.contextPath}/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="${pageContext.request.contextPath}/js/js.js"></script>
<script type="text/javascript">
function addInit(){
	//无处理
}
function addSuccess(data){
	$("#alert_box input[type='text']" , parent.document).val("");
	$("#alert_box select", parent.document).val("");
	$("#searchForm").submit();
}
function editInit(){
}
function editSuccess(data){
	$("#searchForm").submit();
}
function delSuccess(data){
	$("#searchForm").submit();
}
function delData(key) {
	$.ajax({
		type : "POST",
		url : $("#delData").val() + key,
		dataType : "json",
		success : function(data) {
			 alert(data.error);
			 if (data.errorCode == 0) {
					delSuccess(data);
				}
		}
	});
}
function del(key,state) {
	$.ajax({
		type : "POST",
		url : $("#del").val() + key,
		dataType : "json",
		data:{"state":state},
		success : function(data) {
			 alert(data.error);
			 if (data.errorCode == 0) {
					delSuccess(data);
				}
		}
	});
}
</script>
</head>

<body style="background:#eef9ff">

<div class="right_box">
	<div class="inputselect_box">
	<span><input name="" type="button" value="创建赔付类型" class="input_button1"  id="add_button"  />
	</span>
	<form action="${page}" method="post" id="searchForm">
		赔付类型<input type ="text" id ="text" name ="text"  class="input_text1" value = "${text}"  style ="border:1px solid black;"/>　　
		 <input type="submit" id="find" onclick="$('#searchForm').attr('action',1);return true;"  value="查询" class="input_button2" />
		 <input type="button"  onclick="location.href='1'" value="返回" class="input_button2" />
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	  <tr class="font_1">
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">赔付类型</td>
			<td width="35%" align="center" valign="middle" bgcolor="#eef6ff">赔付详情</td>
			<td width="35%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		<c:forEach items="${penalizeTypeList}" var="type">
		<tr>
			<td width="35%" align="center" valign="middle">${type.type==1?'赔付大类':'赔付小类'}</td>
			<td width="35%" align="center" valign="middle">${type.text}</td>
			<td width="35%" align="center" valign="middle">
			[<a href="javascript:del(${type.id},${type.state});">${type.state==1?'停用':'启用'}</a>]
			[<a href="javascript:edit_button(${type.id});">修改</a>]
			[<a href="javascript:if(confirm('确定要删除?')){delData(${type.id});}">删除</a>] 
			</td>
		</tr>
	</c:forEach>
	</table>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
	<c:if test='${page_obj.maxpage>1}'>
	<div class="iframe_bottom">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
	<tr>
		<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
			<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
			<a href="javascript:$('#searchForm').attr('action','${page_obj.previous<1?1:page_obj.previous}');$('#searchForm').submit();">上一页</a>　
			<a href="javascript:$('#searchForm').attr('action','${page_obj.next<1?1:page_obj.next }');$('#searchForm').submit();" >下一页</a>　
			<a href="javascript:$('#searchForm').attr('action','${page_obj.maxpage<1?1:page_obj.maxpage}');$('#searchForm').submit();" >最后一页</a>
			　共${page_obj.maxpage}页　共${page_obj.total}条记录 　当前第<select
					id="selectPg"
					onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
					<c:forEach var="i" begin="1" end="${page_obj.maxpage}">
					<option value='${i}' ${page==i?'selected=seleted':''}>${i}</option>
					</c:forEach>
				</select>页
		</td>
	</tr>
	</table>
	</div>
	</c:if>
</div>
			
	<div class="jg_10"></div>

	<div class="clear"></div>
	
<script type="text/javascript">
$("#selectPg").val('${page}');
</script>
<!-- 创建赔付类型的ajax地址 -->
<input type="hidden" id="add" value="${pageContext.request.contextPath}/penalizeType/add" />
<!-- 修改赔付类型的ajax地址 -->
<input type="hidden" id="edit" value="${pageContext.request.contextPath}/penalizeType/edit/" />
<!-- 停用与不停用的赔付类型的ajax地址 -->
<input type="hidden" id="del" value="${pageContext.request.contextPath}/penalizeType/del/" />
<!-- 删除赔付类型 -->
<input type="hidden" id="delData" value="${pageContext.request.contextPath}/penalizeType/delData/" />
</body>
</html>


