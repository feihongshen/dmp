<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>



<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建供货商</h1>
		<form id="customer_cre_Form" name="customer_cre_Form" onSubmit="if(check_customer()){submitCreateForm(this);}return false;" action="<%=request.getContextPath()%>/customer/create" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>供货商公司名称：</span><input type ="text" id ="customername" name ="customername" maxlength="30"/>*</li>
					<li><span>地址：</span><input type ="text" id ="customeraddress" name ="customeraddress"  maxlength="100"/></li>
					<li><span>联系人：</span><input type ="text" id ="customercontactman" name ="customercontactman"  maxlength="30"/></li>
					<li><span>电话：</span><input type ="text" id ="customerphone" name ="customerphone"  maxlength="30"/></li>
				</ul>
		</div>
		 <div align="center"><input type="submit" value="确认" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
