<%@page import="cn.explink.domain.CwbOrder,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
CwbOrder co = (CwbOrder)request.getAttribute("cwborder");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>到错货处理</h1>
		<form method="post"  action="<%=request.getContextPath()%>/cwborder/daocuohuobeizhu/<%=co.getCwb() %>" onsubmit="if(checkbeizhu()){submitSaveForm(this);};return false;">
		<div id="box_form2">
			<ul>
				<li><span>订&nbsp;&nbsp;单&nbsp;号：</span><%=co.getCwb() %></li>
				<li><span>备&nbsp;&nbsp;&nbsp;&nbsp;注：</span><input type="text" class="inputtext_2" id="comment" name="comment" value="" maxlength="50" /></li>
			</ul>
			 
		</div>
		<div align="center">
		<input type="submit" id="finish" name="finish" value="处理" class="button"/>
        </div>
        </form>
	</div>
</div>

<div id="box_yy"></div>

