<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.domain.Reason"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
Reason reason = (Reason)request.getAttribute("reason");
%>

<script type="text/javascript">
var initReasontype = "<%=reason.getReasontype() %>,reasontype";
</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改常用语</h1>
		<form id="reason_save_Form" name="reason_save_Form"  onSubmit="if(check_reason()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/reason/save/${reason.reasonid}" method="post">
		<div id="box_form">
				<ul>
					<li><span>类型：</span> 
					<select name ="reasontype" id="reasontype">
				       <option value ="0">请选择</option>
		               <%for(ReasonTypeEnum ry : ReasonTypeEnum.values()){ %>
		               <option value ="<%=ry.getValue()%>"><%=ry.getText() %></option>
		               <%} %>
		           </select>*</li>
					<li><span>内容：</span><input type ="text" id="reasoncontent" name ="reasoncontent" value ="${reason.reasoncontent}" maxlength="30" ></li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>

