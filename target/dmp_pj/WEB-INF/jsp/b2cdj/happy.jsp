<%@page import="cn.explink.domain.Branch"%>
<%@page import="java.util.List"%>
<%@page import="cn.explink.b2c.happyGo.HappyGo"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
HappyGo cj = (HappyGo)request.getAttribute("happyObject");
List<Branch> warehouselist=(List<Branch>)request.getAttribute("warehouselist");
%>

</head>
<body>
	<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>快乐购接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/happyGo/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(cj != null){ %>
						
						
						<li><span>customerId：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value="<%=cj.getCustomerid()%>"  size="15"> 
						</li>
						
						<li><span>key：</span>
	 						<input type ="text" id="key" name ="key"  maxlength="300"  value="<%=cj.getKey()%>"  size="15"> 
						</li>
						<li><span>应用编号：</span>
	 						<input type ="text" id="code_happ" name ="code_happ"  maxlength="300"  value="<%=cj.getCode()%>"  size="15"> 
						</li>
						<li><span>查询数量：</span>
	 						<input type ="text" id="pagesize" name ="pagesize"  maxlength="300"  value="<%=cj.getPagesize()%>"  size="15"> 
						</li>
						<li><span>时间：</span>
	 						<input type ="text" id="time" name ="time"  maxlength="300"  value="<%=cj.getTime()%>"  size="15"> 
						</li>
						<li><span>url：</span>
	 						<input type ="text" id="postUrl" name ="postUrl"  maxlength="300"  value="<%=cj.getPostUrl()%>"  size="15"> 
						</li>
					
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(cj.getWarehouseid()==b.getBranchid()){%>selected<%}%> ><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						
						<li><span>证书位置：</span>
	 						<input type ="text" id="locationpost" name ="locationpost"  maxlength="300"  value="<%=cj.getLocation()%>"  size="15"> 
						</li>
						<li><span>证书秘钥：</span>
	 						<input type ="text" id="postkey" name ="postkey"  maxlength="300"  value="<%=cj.getPostkey()%>"  size="15"> 
						</li>
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						
						<li><span>customerId：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value=""  size="15"> 
						</li>
						
						<li><span>key：</span>
	 						<input type ="text" id="key" name ="key"  maxlength="300"  value=""  size="15"> 
						</li>
						<li><span>应用编号：</span>
	 						<input type ="text" id="code_happ" name ="code_happ"  maxlength="300"  value=""  size="15"> 
						</li>
						<li><span>查询数量：</span>
	 						<input type ="text" id="pagesize" name ="pagesize"  maxlength="300"  value=""  size="15"> 
						</li>
						<li><span>时间：</span>
	 						<input type ="text" id="time" name ="time"  maxlength="300"  value=""  size="15"> 
						</li>
						<li><span>url：</span>
	 						<input type ="text" id="postUrl" name ="postUrl"  maxlength="300"  value=""  size="15"> 
						</li>
					
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" ></option>
								<%}%>
							</select>
						</li>
						
						<li><span>证书位置：</span>
	 						<input type ="text" id="locationpost" name ="locationpost"  maxlength="300"  value=""  size="15"> 
						</li>
						<li><span>证书秘钥：</span>
	 						<input type ="text" id="postkey" name ="postkey"  maxlength="300"  value=""  size="15"> 
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
</body>
</html>