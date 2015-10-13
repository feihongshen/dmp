<%@page import="cn.explink.domain.Common"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
Common common = (Common)request.getAttribute("common");
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改承运商</h1>
		<form id="common_save_Form" name="common_save_Form" onSubmit="if(check_common()){submitSaveForm(this);}return false;"  action="<%=request.getContextPath()%>/common/save/${common.id}" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>承运商名称：</span><input type ="text" id ="commonname" name ="commonname" value ="${common.commonname}"  maxlength="30"/>*</li>
					<li><span>承运商编号：</span><input type ="text" id ="commonnumber" name ="commonnumber" value ="${common.commonnumber}"  maxlength="30"/>*</li>
					<li><span>订单号前缀：</span><input type ="text" id ="orderprefix" name ="orderprefix"  value ="${common.orderprefix}"  maxlength="30"/></li>
					<li><font color="red">说明：多个订单号前缀用英文逗号隔开</font></li>
				</ul>
		</div>
		 <div align="center"><input type="submit" value="保存" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
