<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建常用语</h1>
		<form id="reason_cre_Form" name="reason_cre_Form" onSubmit="if(check_reason()){submitCreateForm(this);}return false;" action="<%=request.getContextPath()%>/reason/create" method="post"  >
		<div id="box_form">
			
				<ul>
					<li><span>类型：</span> 
					<select name ="reasontype" id="reasontype" class="select1">
				       <option value ="0">请选择</option>
		               <%for(ReasonTypeEnum ry : ReasonTypeEnum.values()){ %>
		               <option value ="<%=ry.getValue()%>"><%=ry.getText() %></option>
		               <%} %>
		           </select>*</li>
					<li><span>内容：</span><input type ="text" id="reasoncontent" name ="reasoncontent" maxlength="30" class="input_text1"></li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="确认" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
