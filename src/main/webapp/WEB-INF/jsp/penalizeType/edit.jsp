<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改赔付类型</h1>
		<form id="truck_save_Form" name="truck_save_Form"  onSubmit="submitSaveForm(this);return false;" action="${pageContext.request.contextPath}/penalizeType/save" method="post"  >
		<div id="box_form">
<table>		
					<tr>
					<td align="right">赔付类型：</td>
					<td>
					<select name="type" id="type" onchange="penalizeType()">
					<option value="0">请选择</option>
					<option value="1" ${penalizeType.type==1?'selected=selected':'' }>赔付大类</option>
					<option value="2" ${penalizeType.type==2?'selected=selected':'' }>赔付小类</option>
					</select>
					<td>
					</tr>
					<tr id="isshow" <c:if test="${penalizeType.type==1}"> style="display: none;"</c:if> >
					<td align="right">赔付大类：</td>
					<td>
					<select name="parent" id="parent">
					<option value="0">请选择</option>
					<c:forEach items="${penalizeTypeList}" var="type">
					<option value="${type.id }"  ${penalizeType.parent==type.id?'selected=selected':'' }>${type.text }</option>
					</c:forEach>
					</select> 
					</td>
					</tr>
					<tr>
					<td align="right">赔付说明：</td>
					<td >
					<textarea name="text" id="text">${penalizeType.text}</textarea>
					</td>
					</tr>
				</table>		
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button" /></div>
		<input type="hidden" name="id" id="id" value="${penalizeType.id}">
	</form>
	</div>
</div>
<div id="box_yy"></div>