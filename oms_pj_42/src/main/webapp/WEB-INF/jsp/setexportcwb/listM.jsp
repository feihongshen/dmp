<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.Common"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
  Page page_obj = (Page)request.getAttribute("page_obj");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导出设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
function addInit(){
	//无处理
}
function addSuccess(data){
	$("#alert_box input[type='text']" , parent.document).val("");
	//$("#alert_box select", parent.document).val(-1);
	//$("#alert_box input[type='checkbox']", parent.document).removeAttr("checked");
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
	<span><input name="" type="button" value="创建模板" class="input_button1"  id="add_button"  />
	<form method ="post" action ="<%=request.getContextPath()%>/setexportcwb/list/1" id ="searchForm"></form>
	</span>
	
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">序号</td>
			<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">角色</td>
			<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">模版名</td>
			<td width="40%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		<%
			for(Exportmould e : exportmouldlist){
		%>
		<tr>
			<td align="center" valign="middle"><%=e.getId()%></td>
			<td align="center" valign="middle"><%=e.getRolename()%></td>
			<td align="center" valign="middle"><%=e.getMouldname()%></td>
			<td align="center" valign="middle" >
			[<a href="javascript:getSetExportViewBox(<%=e.getId()%>);">对应字段详情查看</a>]
			[<a onclick="return confirm('确定要删除吗？');" href="javascript:del('<%=e.getId()%>');">删除</a>]
			[<a href="javascript:edit_button('<%=e.getId()%>');">修改</a>]
			</td>
		</tr>
		<%} %>
	</table>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
	<%if(page_obj.getMaxpage()>1){ %>
	<div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
					<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
					　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
							id="selectPg"
							onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
							<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
							<option value="<%=i %>"><%=i %></option>
							<% } %>
						</select>页
				</td>
			</tr>
		</table>
	</div>
    <%} %>
</div>
			
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
<!-- 创建导出模板的ajax地址 -->
<input type="hidden" id="add" value="<%=request.getContextPath()%>/setexportcwb/add" />
<!-- 修改导出模板的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/setexportcwb/edit/" />
<!-- 删除导出模板的ajax地址 -->
<input type="hidden" id="del" value="<%=request.getContextPath()%>/setexportcwb/del/" />
<!-- 导出字段详情查看的ajax地址 -->
<input type="hidden" id="view" value="<%=request.getContextPath()%>/setexportcwb/view/" />
</body>
</html>

