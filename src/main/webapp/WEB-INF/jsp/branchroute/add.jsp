<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Role,cn.explink.enumutil.BranchRouteEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建货物流向</h1>
		<form id="branchroute_cre_Form" name="branchroute_cre_Form"
			 onSubmit="if(check_branchroute()){submitCreateForm(this);}return false;" 
			 action="<%=request.getContextPath()%>/branchRouteControl/create;jsessionid=<%=session.getId()%>" method="post"  >
			<div id="box_form">
				<ul>
					<li><span>当前站点：</span>
						<select id="fromBranchId" name="fromBranchId">
							<option value="0" selected>----请选择----</option>
							<%for(Branch b : branchlist){ %>
								<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
							<%} %>
						</select>*
					</li>
	           		<li><span>目的站点：</span>
	           			<select id="toBranchId"  name="toBranchId">
							<option value="0" selected>----请选择----</option>
							<%for(Branch b : branchlist){ %>
								<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
							<%} %>
						</select>*
					</li>
					<li><span>流向方向：</span>
	           			<select id="type"  name="type">
							<option value="0" selected>----请选择----</option>
							<%for(BranchRouteEnum br : BranchRouteEnum.values()){ %>
								<option value="<%=br.getValue() %>" ><%=br.getText() %></option>
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
