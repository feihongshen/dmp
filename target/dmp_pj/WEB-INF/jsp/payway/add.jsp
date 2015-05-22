<%@page import="cn.explink.enumutil.PaytypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建支付方式</h1>
		<form id="payway_cre_Form" name="payway_cre_Form" onSubmit="if(check_paywayid()){submitCreateForm(this);}return false;" action="<%=request.getContextPath()%>/payway/create" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>支付方式：</span> 
					<select id="paywayid" name="paywayid" class="select1">
							<option value=-1>请选择支付方式</option>
							<%for(PaytypeEnum pe : PaytypeEnum.values()){ %>
								<option value="<%=pe.getValue()%>"><%=pe.getText() %></option>
							<%} %>
							<!-- <option value="1">现金</option>
							<option value="2">POS</option>
							<option value="3">支票</option>
							<option value="4">其他</option> -->
					</select>*</li>
					<li><span>对应文字：</span><input type="text" name="payway" id="payway" value=""  maxlength="50" class="input_text1"></li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="确认" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
