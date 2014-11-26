<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> kflist = (List<Branch>)request.getAttribute("kflist");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建对应记录</h1>
		<form id="warehousekey_cre_Form" name="warehousekey_cre_Form" onSubmit="if(check_warehousekey()){submitCreateForm(this);}return false;" action="<%=request.getContextPath()%>/warehousekey/create" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>目&nbsp;标&nbsp;&nbsp;仓&nbsp;库：</span> 
						<select name="targetcarwarehouseid" id="targetcarwarehouseid">
							<option value="-1">请选择目标仓库</option>
							<%for(Branch b : kflist){ %>
								<option value="<%=b.getBranchid()%>"><%=b.getBranchname() %></option>
							<%} %>
						</select>
					</li>
					<li><span>对应省市字段：</span><input type="text" name="keyname" id="keyname" value=""  maxlength="50"></li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="确认" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
