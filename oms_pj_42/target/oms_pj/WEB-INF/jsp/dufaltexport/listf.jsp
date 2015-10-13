<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.Common"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>默认导出模版管理</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
function addInit(){
	//无处理
}
function addSuccess(data){
	$("#searchForm").submit();
}
function editInit(){
	//无处理
	if(initSetExportList){
		for(var i=0 ; i<initSetExportList.length ; i++){
			window.parent.initSetExport(initSetExportList[i]);
		}
	}
}
function editSuccess(data){
	$("#searchForm").submit();
}
function delSuccess(data){
	$("#searchForm").submit();
}
</script>
</head>

<body style="background:#eef9ff">

<div class="right_box">
	<div class="inputselect_box">
	<span>
	<form method ="post" action ="<%=request.getContextPath()%>/setdefaultexport/list" id ="searchForm"></form>
	</span>
	
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
			<td width="50%" align="center" valign="middle" bgcolor="#eef6ff">信息</td>
			<td width="50%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>	
		<tr>
			<td align="center" valign="middle">默认导出模版</td>
			<td align="center" valign="middle" >
			[<a href="javascript:setDefaultExportViewBox();">对应字段详情查看</a>]
			[<a href="javascript:editDefailtExpButton();">修改</a>]
			</td>
		</tr>
	</table>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
</div>
			
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
<!-- 修改导出模板的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/setdefaultexport/edit/" />
<!-- 导出字段详情查看的ajax地址 -->
<input type="hidden" id="view" value="<%=request.getContextPath()%>/setdefaultexport/view/" />
</body>
</html>

