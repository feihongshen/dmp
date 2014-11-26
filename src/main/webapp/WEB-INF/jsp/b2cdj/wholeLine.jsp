<%@page import="java.util.List"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.b2c.Wholeline.WholeLine"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
WholeLine cj = (WholeLine)request.getAttribute("happyObject");
List<Branch> warehouselist=(List<Branch>)request.getAttribute("warehouselist");
%>
<title>Insert title here</title>
</head>
<body>
	<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>全线快递接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/wholeLine/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(cj != null){ %>
						<li><span>承运商：</span>
	 						<input type ="text" id="usercode" name ="usercode"  maxlength="300"  value="<%=cj.getUsercode()%>"  size="15"> 
						</li>
						
						
						
						<li><span>comeCode：</span>
	 						<input type ="text" id="comecode" name ="comecode"  maxlength="300"  value="<%=cj.getComeCode()%>"  size="15"> 
						</li>
						<li><span>方法名：</span>
	 						<input type ="text" id="method" name ="method"  maxlength="300"  value="<%=cj.getMethod()%>"  size="15"> 
						</li>
						<li><span>url：</span>
	 						<input type ="text" id="postUrl" name ="postUrl"  maxlength="300"  value="<%=cj.getUrl()%>"  size="15"> 
						</li>
						<li><span>拒收异常码：</span>
	 						<input type ="text" id="expt_code" name ="expt_code"  maxlength="300"  value="<%=cj.getExpt_code()%>"  size="15"> 
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li><span>承运商：</span>
	 						<input type ="text" id="usercode" name ="usercode"  maxlength="300"  value=""  size="15"> 
						</li>
						
						<li><span>comeCode：</span>
	 						<input type ="text" id="comecode" name ="comecode"  maxlength="300"  value=""  size="15"> 
						</li>
						<li><span>方法名：</span>
	 						<input type ="text" id="method" name ="method"  maxlength="300"  value=""  size="15"> 
						</li>
						<li><span>url：</span>
	 						<input type ="text" id="postUrl" name ="postUrl"  maxlength="300"  value=""  size="15"> 
						</li>
						<li><span>拒收异常码：</span>
	 						<input type ="text" id="expt_code" name ="expt_code"  maxlength="300"  value=""  size="15"> 
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