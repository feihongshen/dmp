<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建赔付类型</h1>
		<form id="penalizeType_cre_Form" name="penalizeType_cre_Form"  onSubmit="submitCreateForm(this);return false;" action="${pageContext.request.contextPath}/penalizeType/create" method="post"  >
		<div id="box_form">
				<table>		
					<tr>
					<td align="right">赔付类型：</td>
					<td>
					<select name="type" id="type" onchange="penalizeType()">
					<option value="0">请选择</option>
					<option value="1">赔付大类</option>
					<option value="2">赔付小类</option>
					</select>
					<td>
					</tr>
					<tr id="isshow" style="display: none;">
					<td align="right">赔付大类：</td>
					<td>
					<select name="parent" id="parent">
					<option value="0">请选择</option>
					<c:forEach items="${penalizeTypeList}" var="type">
					<option value="${type.id }">${type.text }</option>
					</c:forEach>
					</select> 
					</td>
					</tr>
					<tr>
					<td align="right">赔付说明：</td>
					<td >
					<textarea name="text" id="text"></textarea>
					</td>
					</tr>
				</table>		
		</div>
		<div align="center"><input type="submit" value="确认" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
