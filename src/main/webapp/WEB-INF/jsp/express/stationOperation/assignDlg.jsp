<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CustomWareHouse"%>
<%@page import="cn.explink.domain.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	List<User> deliverList = (List<User>)request.getAttribute("deliverList");
    String selectedPreOrders = (String)request.getAttribute("selectedPreOrders");
%>
	<div id="box_bg"></div>
	<div id="box_contant">
		<div id="box_top_bg"></div>
		<div id="box_in_bg">
			<h1>
				<div id="close_box" onclick="closeBox()"></div>
				分配小件员
			</h1>
			<div id="box_form">
				<ul>
					<li><span>小件员：</span> <select id="deliverid" name="deliverid" class="select1">
							<option value="-1" selected>请选择</option>
							<%
								for (User deliver : deliverList) {
							%>
							<option value=<%=deliver.getUserid()%>><%=deliver.getRealname()%></option>
							<%
								}
							%>
					</select></li>
				</ul>
			</div>
			<input type="button" value="分配"  class="button" onclick="doAssign();"/> 
			<input type="button" value="分配并导出" class="button" onclick="doAssignAndExport();"/>
		    <input type="button" value="关闭" class="button"  onclick="closeBox()"/>
		</div>
	</div>
	<div id="box_yy"></div>
	<!-- 分配的ajax地址 -->
	<input type="hidden" id="doAssign" value="<%=request.getContextPath()%>/stationOperation/doAssign" />
	<!-- 导出的ajax地址 -->
	<input type="hidden" id="exportExcel" value="<%=request.getContextPath()%>/stationOperation/exportExcel" />
	<input type="hidden" id="selectedPreOrders" value="<%=selectedPreOrders%>" />
