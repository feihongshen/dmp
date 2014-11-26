<%@page import="cn.explink.b2c.sfexpress.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="java.util.List"%>

<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
	Sfexpress mkl = (Sfexpress)request.getAttribute("sfObject");
	
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>顺丰接口</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;"  action="<%=request.getContextPath()%>/sfexpress/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(mkl != null){ %>
						<li><span>承运商Code：</span>
	 						<input type ="text" id="commoncode" name ="commoncode"  maxlength="300"  value="<%=mkl.getCommoncode()%>"  size="15" > <font color="red">*与承运商设置的code需一致</font>
						</li>
						<li><span>推送URL：</span>
	 						<input type ="text" id="send_url" name ="send_url"  maxlength="300"  value="<%=mkl.getSend_url()%>"  size="40" > 
						</li>
						
						<li><span>接入编码：</span>
	 						<input type ="text" id="expressCode" name ="expressCode"  maxlength="300"  value="<%=mkl.getExpressCode()%>"  size="15" > 
						</li>
						<li><span>checkword：</span>
	 						<input type ="text" id="checkword" name ="checkword"  maxlength="300"  value="<%=mkl.getCheckword()%>"  size="15" > 
						</li>
					
						<li><span>返回单量最大值：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="30" size="20"  value="<%=mkl.getMaxCount()%>" > 
						</li>
						<li><span>失败重发次数：</span>
	 						<input type ="text" id="loopcount" name ="loopcount"  maxlength="30" size="20"  value="<%=mkl.getLoopcount()%>" > 
						</li>
						
						<li><span>公司名称：</span>
	 						<input type ="text" id="companyName" name ="companyName"  maxlength="30" size="20"  value="<%=mkl.getCompanyName()%>" > 
						</li>
						<li><span>客服名称：</span>
	 						<input type ="text" id="serviceContact" name ="serviceContact"  maxlength="30" size="20"  value="<%=mkl.getServiceContact()%>" > 
						</li>
						<li><span>客服电话：</span>
	 						<input type ="text" id="servicePhone" name ="servicePhone"  maxlength="30" size="20"  value="<%=mkl.getServicePhone()%>" > 
						</li>
						<li><span>寄件地址：</span>
	 						<input type ="text" id="j_address" name ="j_address"  maxlength="300" size="40"  value="<%=mkl.getJ_address()%>" > 
						</li>
						<li><span>月结卡号：</span>
	 						<input type ="text" id="custid" name ="custid"  maxlength="300" size="40"  value="<%=mkl.getCustid()%>" > 
						</li>
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li><span>承运商Code：</span>
	 						<input type ="text" id="commoncode" name ="commoncode"  maxlength="300"  value=""  size="15" > <font color="red">*与承运商设置的code需一致</font>
						</li>
						
						<li><span>推送URL：</span>
	 						<input type ="text" id="send_url" name ="send_url"  maxlength="300"  value=""  size="40" > 
						</li>
						<li><span>接入编码：</span>
	 						<input type ="text" id="expressCode" name ="expressCode"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>checkword：</span>
	 						<input type ="text" id="checkword" name ="checkword"  maxlength="300"  value=""  size="15" > 
						</li>
					
						<li><span>返回单量最大值：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="30" size="20"  value="" > 
						</li>
						<li><span>失败重发次数：</span>
	 						<input type ="text" id="loopcount" name ="loopcount"  maxlength="30" size="20"  value="" > 
						</li>
						
						<li><span>公司名称：</span>
	 						<input type ="text" id="companyName" name ="companyName"  maxlength="30" size="20"  value="" > 
						</li>
						<li><span>客服名称：</span>
	 						<input type ="text" id="serviceContact" name ="serviceContact"  maxlength="30" size="20"  value="" > 
						</li>
						<li><span>客服电话：</span>
	 						<input type ="text" id="servicePhone" name ="servicePhone"  maxlength="30" size="20"  value="" > 
						</li>
						<li><span>寄件地址：</span>
	 						<input type ="text" id="j_address" name ="j_address"  maxlength="300" size="40"  value="" > 
						</li>
						<li><span>月结卡号：</span>
	 						<input type ="text" id="custid" name ="custid"  maxlength="300" size="40"  value="" > 
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
