<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.CwbOrderTypeBean"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建订单类型</h1>
		<form id="cwbordertype_cre_Form" name="cwbordertype_cre_Form" onSubmit="if(check_cwbOrderType()){submitCreateForm(this);}return false;" action="<%=request.getContextPath()%>/cwbordertype/create" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>订单类型：</span> 
					<select id="importtypeid" name="importtypeid" class="select1">
							<option value=-1>请选择类型</option>
							<option value="1">配送</option>
							<option value="2">上门退</option>
							<option value="3">上门换</option>
					</select>*</li>
					<li><span>对应文字：</span><input type="text" name="importtype" id="importtype" value=""  maxlength="50" class="input_text1"></li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="确认" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
