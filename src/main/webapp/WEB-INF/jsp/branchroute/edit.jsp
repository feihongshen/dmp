<%@page import="cn.explink.domain.Branch,cn.explink.domain.BranchRoute,cn.explink.enumutil.BranchRouteEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
BranchRoute branchroute = (BranchRoute)request.getAttribute("branchroute");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改货物流向</h1>
		<form onSubmit="if(check_branchroute()){submitSaveForm(this);}return false;" 
			 action="<%=request.getContextPath()%>/branchRouteControl/save/<%=branchroute.getFromBranchId()%>/<%=branchroute.getToBranchId()%>/<%=branchroute.getType() %>" method="post"  >
			<div id="box_form">
				<ul>
					<li><span>当前站点：</span>
						<select id="fromBranchId" name="fromBranchId">
							<option value="0" selected>----请选择----</option>
							<%for(Branch b : branchlist){ %>
								<option value="<%=b.getBranchid() %>" <%if(branchroute.getFromBranchId()==b.getBranchid()){ %>selected<%}%>><%=b.getBranchname() %></option>
							<%} %>
						</select>*
					</li>
	           		<li><span>目的站点：</span>
	           			<select id="toBranchId"  name="toBranchId">
							<option value="0" selected>----请选择----</option>
							<%for(Branch b : branchlist){ %>
								<option value="<%=b.getBranchid() %>" <%if(branchroute.getToBranchId()==b.getBranchid()){ %>selected<%}%>><%=b.getBranchname() %></option>
							<%} %>
						</select>*
					</li>
					<li><span>流向方向：</span>
	           			<select id="type"  name="type">
							<option value="0" selected>----请选择----</option>
							<%for(BranchRouteEnum br : BranchRouteEnum.values()){ %>
								<option value="<%=br.getValue() %>" <%if(branchroute.getType()==br.getValue()){ %>selected<%}%>><%=br.getText() %></option>
							<%} %>
						</select>*
					</li>
	         </ul>
		</div>
		<div align="center">
        <input type="submit" value="确认" class="button" id="sub" /></div>
	</form>
	</div>
</div>

<div id="box_yy"></div>

</BODY>
</HTML>
