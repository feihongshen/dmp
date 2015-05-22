
<%@page import="cn.explink.domain.ImportCwbOrderType"%>
<%@page import="cn.explink.domain.CwbOrderTypeBean"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
ImportCwbOrderType importCwbOrderType = (ImportCwbOrderType)request.getAttribute("importCwbOrderType");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改订单类型</h1>
		<form id="cwbordertype_save_Form" name="cwbordertype_save_Form" onSubmit="if(check_cwbOrderType()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/cwbordertype/save/${importCwbOrderType.importid }" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>订单类型：</span> 
					<select id="importtypeid" name="importtypeid" class="select1">
							<option value=-1>请选择类型</option>
							<option value="1" <%=importCwbOrderType.getImporttypeid()==1?"selected":"" %>>配送</option>
							<option value="2" <%=importCwbOrderType.getImporttypeid()==2?"selected":"" %>>上门退</option>
							<option value="3" <%=importCwbOrderType.getImporttypeid()==3?"selected":"" %>>上门换</option>
					</select>*</li>
					<li><span>对应文字：</span><input type="text" name="importtype" id="importtype" value="${importCwbOrderType.importtype}"  maxlength="50" class="input_text1"></li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>

