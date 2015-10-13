<%@page import="cn.explink.domain.Operatelog"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Operatelog> operatelogList = (List<Operatelog>)request.getAttribute("operatelogList");
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>操作日志</h1>
		<div id="box_form">
		 <%if(operatelogList != null ){ %>
				<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
				<%for(Operatelog o:operatelogList){ %>
				<tr>
					<td><table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
							<tr>
								<td width="400"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
									<tr>
										<td width="100%" align="center" valign="middle" ><%=o.getOperatetime()+" "+ o.getOperateman()+" "+ o.getOperateremarks()%></td>
									</tr>
								</table></td>
							</tr>
			           </table>
			        </td>
				</tr>
				<%} %>
			</table>  
		  <%}else{%>
		     <table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
				<tr>
					<td><table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
							<tr>
								<td width="400"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
									<tr>
										<td width="100%" align="center" valign="middle" >无历史操作日志</td>
									</tr>
								</table></td>
							</tr>
			           </table>
			        </td>
				</tr>
			</table>  
		  <%} %>                       
		</div>
	</div>
</div>
<div id="box_yy"></div>
