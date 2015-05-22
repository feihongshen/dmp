
<%@page import="cn.explink.domain.ImportCwbOrderType"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> kflist = (List<Branch>)request.getAttribute("kflist");
WarehouseKey warehouseKey = (WarehouseKey)request.getAttribute("warehouseKey");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改对应记录</h1>
		<form id="warehousekey_save_Form" name="warehousekey_save_Form" onSubmit="if(check_warehousekey()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/warehousekey/save/${warehouseKey.id }" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>目&nbsp;标&nbsp;&nbsp;仓&nbsp;库：</span> 
						<select name="targetcarwarehouseid" id="targetcarwarehouseid">
							<option value="-1">请选择目标仓库</option>
							<%for(Branch b : kflist){ %>
								<option value="<%=b.getBranchid()%>" <%if(warehouseKey.getTargetcarwarehouseid()==b.getBranchid()){ %>selected <%} %>><%=b.getBranchname() %></option>
							<%} %>
						</select>
					</li>
					<li><span>对应省市字段：</span><input type="text" name="keyname" id="keyname" value="${warehouseKey.keyname}"  maxlength="50"></li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>

