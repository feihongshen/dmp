<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.ImportCwbOrderType"%>
<%@page import="cn.explink.domain.CwbOrderTypeBean"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbOrderTypeBean> cwborderTypeBean = (List<CwbOrderTypeBean>)request.getAttribute("cwborderTypeBean");
List<ImportCwbOrderType> importCwbOrderTypes = (List<ImportCwbOrderType>)request.getAttribute("importCwbOrderTypes");
ImportCwbOrderType importCwbOrderType = (ImportCwbOrderType)request.getAttribute("importCwbOrderType");
Page page_obj = (Page)request.getAttribute("page_obj");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入订单类型管理</title>
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
	$("#alert_box select", parent.document).val(-1);
	$("#searchForm").submit();
}
function editInit(){
	//无处理
}
function editSuccess(data){
	$("#searchForm").submit();
}
function delSuccess(data){
	$("#searchForm").submit();
}
</script>
</head>

<body style="background:#f5f5f5">

<div class="right_box">
	<div class="inputselect_box">
	<span><input name="" type="button" value="创建订单类型" class="input_button1"  id="add_button"  />
	</span>
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
		订单类型： <select name="cwbordertype" id="cwbordertypes" class="select1">
			<option value="-1">请选择类型</option>
			<option value="1">配送</option>
			<option value="2">上门退</option>
			<option value="3">上门换</option>
		</select><input type="submit"  onclick="$('#searchForm').attr('action',1);return true;"  id="find" value="查询" class="input_button2" />
		<input type="button"  onclick="location.href='1'" value="返回" class="input_button2" />
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	  <tr class="font_1">
			<td width="30%" align="center" valign="middle" bgcolor="#eef6ff">类型</td>
			<td width="30%" align="center" valign="middle" bgcolor="#eef6ff">备注</td>
			<td width="40%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		 <% for(ImportCwbOrderType i : importCwbOrderTypes){ %>
		<tr>
			 <% if(i.getImporttypeid()==1){ %>
	         <td width="30%" align="center" valign="middle">配送</td>
	         <%}else if(i.getImporttypeid()==2){ %>
	         <td width="30%" align="center" valign="middle">上门退</td>
	         <%}else if(i.getImporttypeid()==3){ %>
	         <td width="30%" align="center" valign="middle">上门换</td>
	         <%} %>
			<td width="30%" align="center" valign="middle"><%=i.getImporttype()%></td>
			<td width="40%" align="center" valign="middle" >
			[<a href="javascript:if(confirm('确定要删除?')){del(<%=i.getImportid()%>);}">删除</a>]
			[<a href="javascript:edit_button(<%=i.getImportid()%>);">修改</a>]
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
</div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#cwbordertypes").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("cwbordertype")) %>);
</script>
<!-- 创建订单类型的ajax地址 -->
<input type="hidden" id="add" value="<%=request.getContextPath()%>/cwbordertype/add" />
<!-- 修改订单类型的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/cwbordertype/edit/" />
<!-- 删除订单类型的ajax地址 -->
<input type="hidden" id="del" value="<%=request.getContextPath()%>/cwbordertype/del/" />
</body>
</html>
