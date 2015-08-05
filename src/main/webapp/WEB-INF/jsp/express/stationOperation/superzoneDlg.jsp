<%@page import="cn.explink.util.StringUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  String selectedPreOrders = (String)request.getAttribute("selectedPreOrders");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1>
			<div id="close_box" onclick="closeBox()"></div>
			站点超区
		</h1>
			<div id="box_form">
				<table>
					<tr>
						<td>备注：</td>
						<td><textarea id="note" cols="3"></textarea></td>
					</tr>
				</table>
			</div>
			<input type="button" value="确认" class="button" onclick="doSuperzone();"/> 
			<input type="button" value="关闭" class="button" onclick="closeBox()"/>
	</div>
</div>
<div id="box_yy"></div>
<!-- 站点超区的ajax地址 -->
<input type="hidden" id="doSuperzone" value="<%=request.getContextPath()%>/stationOperation/doSuperzone" />
<input type="hidden" id="selectedPreOrders" value="<%=selectedPreOrders%>"/>
