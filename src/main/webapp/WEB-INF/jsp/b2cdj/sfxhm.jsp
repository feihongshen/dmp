<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.sfxhm.*"%>
<%@page import="cn.explink.domain.Branch"%>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Sfxhm efast = (Sfxhm)request.getAttribute("sfxhmObject");
List<Branch> warehouselist=(List<Branch>)request.getAttribute("warehouselist");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>顺丰-小红帽对接设置</h1>
		<form id="yixun_save_Form" name="yixun_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/sfxhm/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(efast != null){ %>
						
						
						<li><span>连接驱动：</span>
	 						<input type ="text" id="driver" name ="driver"  maxlength="300" size="20"  value="<%=efast.getDriver() %>"  > 
						</li>
						
						<li><span>用户名：</span>
	 						<input type ="text" id="uname" name ="uname" maxlength="300"  size="20"  value="<%=efast.getUname() %>"  > 
						</li>
						
						
						<li><span>密码：</span>
	 						<input type ="text" id="pwd" name ="pwd"  size="20"  value="<%=efast.getPwd()%>"  > 
						</li>
						<li><span>回传条数：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  size="20"  value="<%=efast.getMaxCount() %>"  > 
						</li>
						
						<li><span>物流公司名称：</span>
	 						<input type ="text" id="companyname" name ="companyname"  maxlength="300"  value="<%=efast.getCompanyname()%>" size="20"   > 
						</li>
						<li><span>供货商ID：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value="<%=efast.getCustomerid()%>" size="20"   > 
						</li>
						<li><span>下载条数：</span>
	 						<input type ="text" id="downloadCount" name ="downloadCount"  maxlength="300"  value="<%=efast.getMaxCount()%>" size="20"   > 
						</li>
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(efast.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
						
						
						
					<%}else{ %>
						<li><span>连接驱动：</span>
	 						<input type ="text" id="driver" name ="driver"  maxlength="300" size="20"  value=""  > 
						</li>
						
						<li><span>用户名：</span>
	 						<input type ="text" id="uname" name ="uname" maxlength="300"  size="20"  value=""  > 
						</li>
						
						
						<li><span>密码：</span>
	 						<input type ="text" id="pwd" name ="pwd"  size="20"  value=""  > 
						</li>
						<li><span>回传条数：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  size="20"  value=""  > 
						</li>
						
						<li><span>物流公司名称：</span>
	 						<input type ="text" id="companyname" name ="companyname"  maxlength="300"  value="" size="20"   > 
						</li>
						<li><span>供货商ID：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value="" size="20"   > 
						</li>
						<li><span>下载条数：</span>
	 						<input type ="text" id="downloadCount" name ="downloadCount"  maxlength="300"  value="" size="20"   > 
						</li>
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" ><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
						
					<%} %>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button"  /></div>
		<input type="hidden" name="joint_num" value="${joint_num}"/>
	</form>
	</div>
</div>
<div id="box_yy"></div>

