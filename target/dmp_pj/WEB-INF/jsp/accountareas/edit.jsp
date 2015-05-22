<%@page import="cn.explink.domain.AccountArea,cn.explink.domain.Customer"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<Customer> customerList = (List<Customer>)request.getAttribute("customeres");
	AccountArea accountArea = (AccountArea)request.getAttribute("accountArea");
%>
<script type="text/javascript">
var initCustomerid = "<%=accountArea.getCustomerid() %>,customerid";
</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改供货商结算区域</h1>
		<form id="accountareas_save_Form" name="accountareas_save_Form" onSubmit="if(check_accountareas()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/accountareas/save/<%=accountArea.getAreaid() %>" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>供货商：</span> 
					<select id="customerid" name="customerid">
							<option value="-1">----请选择----</option>
							<% for(Customer c : customerList){ %>
								<option value="<%=c.getCustomerid() %>"><%=c.getCustomername() %></option>
							<%} %>
				    </select>*</li>
					<li><span>结算区域：</span><input type="text" id="areaname" name="areaname" value="<%=accountArea.getAreaname() %>" maxlength="50"/>*</li>
					<li><span>备注：</span><input type="text" id="arearemark" name="arearemark" value="<%=accountArea.getArearemark() %>" maxlength="50"/></li>
				</ul>
				 
		</div>
		<div align="center"><input type="submit" value="保存" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
