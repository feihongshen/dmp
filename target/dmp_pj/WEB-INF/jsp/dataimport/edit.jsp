<%@page import="cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
CwbOrder co = (CwbOrder) request.getAttribute("co");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改</h1>
		<form id="data_edit_Form" name="data_edit_Form" onSubmit="submitSaveForm(this);return false;" action="<%=request.getContextPath()%>/dataimport/editexcel/<%=co.getCwb()%>" method="post"  >
		<div id="box_form">
				<ul>
           		<li><span>匹配站点：</span><input type="text" name="excelbranch" id="excelbranch" maxlength="50" value="<%=co.getExcelbranch()%>"/></li>
           		<%-- <li><span>匹配小件员：</span><input type="text" name="exceldeliver" id="exceldeliver" maxlength="50" value="<%=co.getExceldeliver()%>"/></li> --%>
	         </ul>
		</div>
		<div align="center"><input type="submit" value="确认" class="button"/></div>
		</form>	
	</div>
</div>

<div id="box_yy"></div>

