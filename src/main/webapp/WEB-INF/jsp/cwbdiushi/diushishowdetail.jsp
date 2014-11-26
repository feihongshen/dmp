<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.CwbDiuShi"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
CwbDiuShi cwbDiuShi = (CwbDiuShi) request.getAttribute("cwbDiuShi");
CwbOrder cwborder = (CwbOrder) request.getAttribute("cwborder");
String datetime = request.getAttribute("datetime").toString();
Map usermap = (Map) session.getAttribute("usermap");
List<User> userlist = request.getAttribute("userlist")==null?null:(List<User>)request.getAttribute("userlist");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>&nbsp;</h1>
		<form id="diushidetailForm" onSubmit="if(checkPayamount()){submitSaveFormAndCloseBox(this);}return false;" 
			 action="<%=request.getContextPath()%>/cwbdiushi/diushihandle/<%=cwbDiuShi.getId()%>" method="post"  >
		<div id="box_form">
			<ul>
				<li><span>订单号：</span><%=cwbDiuShi.getCwb()%></li>
           		<li><span>当前状态：</span><%for(FlowOrderTypeEnum ft : FlowOrderTypeEnum.values()){if(cwborder.getFlowordertype()==ft.getValue()){%><%=ft.getText() %><%}} %></li>
           		<li><span>小件员：</span><%if(userlist!=null&&userlist.size()>0)for(User u : userlist){if(cwborder.getDeliverid()==u.getUserid()){%><%=u.getRealname() %><%}} %></li>
           		<li><span>货物金额：</span><input type="hidden" name="caramount" id="caramount" value="<%=cwbDiuShi.getCaramount() %>"/><%=cwbDiuShi.getCaramount() %></li>
		        <li><span>赔偿金额：</span><input type="text" name="payamount" id="payamount" maxlength="50"/></li>
			    <li><span>处理人：</span><%=usermap.get("realname") %></li>
				 <li><span>处理时间：</span><%=datetime %></li>
	         </ul>
		</div>
		<div align="center"><input type="submit" value="确认" class="button" id="sub" /></div>
		</form>
	</div>
</div>

<div id="box_yy"></div>

