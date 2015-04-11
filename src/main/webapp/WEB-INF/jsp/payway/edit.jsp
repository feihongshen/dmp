<%@page import="cn.explink.domain.PayWay"%>
<%@page import="cn.explink.enumutil.PaytypeEnum"%>
<%@page import="cn.explink.domain.ImportCwbOrderType"%>
<%@page import="cn.explink.domain.CwbOrderTypeBean"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
PayWay payway = (PayWay)request.getAttribute("payway");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改支付方式</h1>
		<form id="payway_save_Form" name="payway_save_Form" onSubmit="if(check_paywayid()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/payway/save/${payway.id }" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>支付方式：</span> 
					<select id="paywayid" name="paywayid" class="select1">
							<option value=-1>请选择支付方式</option>
							<%for(PaytypeEnum pe : PaytypeEnum.values()){ %>
								<option value="<%=pe.getValue()%>" <%=payway.getPaywayid()==pe.getValue()?"selected":"" %>><%=pe.getText() %></option>
							<%} %>
					</select>*</li>
					<li><span>对应文字：</span><input type="text" name="payway" id="payway" value="${payway.payway}"  maxlength="50" class="input_text1"></li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>

