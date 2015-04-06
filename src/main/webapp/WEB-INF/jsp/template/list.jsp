<%@page import="cn.explink.print.template.*,cn.explink.util.Page"%>
<%@page import="cn.explink.enumutil.PrintTemplateOpertatetypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<PrintTemplate> ptList = (List<PrintTemplate>)request.getAttribute("ptList");
Page page_obj = (Page)request.getAttribute("page_obj");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>交接单模版设置</title>
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
	$("#alert_box input[type='checkbox']", parent.document).attr("checked","");
	$("#searchForm").submit();
}
function editInit(){
	//$("#searchForm").submit();
}
function editSuccess(data){
	$("#searchForm").submit();
}
function delSuccess(data){
	$("#searchForm").submit();
}
function editRoleAndMenuInit(){
	if(initMenuList){
		for(var i=0 ; i<initMenuList.length ; i++){
			window.parent.initMenu(initMenuList[i]);
		}
	}
}

function editRoleAndMenuSuccess(data){
	$("#searchForm").submit();
}


</script>
</head>

<body style="background:#f5f5f5">

<div class="right_box">
		<div class="inputselect_box">
		<span><input name="" type="button" value="创建模版" class="input_button1"  onclick='window.location.href="<%=request.getContextPath()%>/printtemplate/add";'/>
		</span>
		<form action="1" method="post" id="searchForm">
			模版名称：<input type="text" name="name" id="name" value="" />
			交接单类型：
					<select id="opertatetype" name="opertatetype">
						<option value="0">请选择</option>
						<%for(PrintTemplateOpertatetypeEnum pe : PrintTemplateOpertatetypeEnum.values()){ %>
						<option value="<%=pe.getValue()%>"><%=pe.getText() %></option>
						<%} %>
					</select>
			<input type="submit" id="find"  value="查询" class="input_button2" />
			<input type="button"  onclick="location.href='1'" value="返回" class="input_button2" />
		</form>
		
		
		</div>
		<div class="right_title">
		<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		    <tr class="font_1">
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">模版编号</td>
				<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">表头前缀</td>
				<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">模版名称</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">交接单类型</td>
				<!-- <td width="20%" align="center" valign="middle" bgcolor="#eef6ff">模版类型</td> -->
				<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
			</tr>
			 <% for(PrintTemplate pt : ptList){ %>
			<tr>
				<td width="10%" align="center" valign="middle"><%=pt.getId() %></td>
				<td width="20%" align="center" valign="middle"><%=pt.getCustomname() %></td>
				<td width="20%" align="center" valign="middle"><%=pt.getName() %></td>
				<td width="10%" align="center" valign="middle">
					<%for(PrintTemplateOpertatetypeEnum ptot : PrintTemplateOpertatetypeEnum.values()){if(ptot.getValue()==pt.getOpertatetype()){ %>
						<%=ptot.getText() %>
					<%}} %>
				</td>
				<%-- <td width="20%" align="center" valign="middle">
					<%if(pt.getTemplatetype()==1){ %>
					按单
					<%}else if(pt.getTemplatetype()==2){ %>
					汇总
					<%} %>
				</td> --%>
				<td width="20%" align="center" valign="middle" >
				[<a href="<%=request.getContextPath()%>/printtemplate/edit/<%=pt.getId() %>">修改</a>]
				[<a href="javascript:if(confirm('删除后不可恢复,是否确定删除?'))del(<%=pt.getId() %>);">删除</a>]
				</td>
			</tr>
			<%} %>
		</table>
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

<!-- 创建模版的ajax地址 -->
<input type="hidden" id="add" value="" />
<!-- 修改模版的ajax地址 -->
<%-- <input type="hidden" id="edit" value="<%=request.getContextPath()%>/printtemplate/edit/" /> --%>
<input type="hidden" id="del" value="<%=request.getContextPath()%>/printtemplate/del/" />
<script>
$("#opertatetype").val(<%=request.getParameter("opertatetype")==null?"0":Long.parseLong(request.getParameter("opertatetype"))%>);
</script>
</body>
</html>


