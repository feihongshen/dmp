<%@page import="cn.explink.util.StringUtil"%>

<%@page import="cn.explink.pos.alipayapp.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Alipayapp alipay = (Alipayapp)request.getAttribute("alipayapplist");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>支付宝APP对接设置</h1>
		<form id="alipay_save_Form" name="alipay_save_Form"  onSubmit="submitSaveForm(this);return false;" action="<%=request.getContextPath()%>/alipayapp/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<li><span>物流公司代码：</span>
 						<input type ="text" id="logistics_code" name ="logistics_code" value ="<%=StringUtil.nullConvertToEmptyString(alipay.getLogistics_code())%>" maxlength="1000"  > 
					</li>
			
					<li><span>编码方式：</span>
 						<select name="input_charset">
 							<option value="UTF-8" <%if("UTF-8".equals(alipay.getInput_charset())){%>selected<%} %>>UTF-8</option>
 							<option value="GBK" <%if("GBK".equals(alipay.getInput_charset())){%>selected<%} %>>GBK</option>
 						</select> 
					</li>
					<li><span>签名方式：</span>
						<select name="sign_type">
 							<option value="MD5" <%if("MD5".equals(alipay.getSign_type())){%>selected<%} %>>MD5</option>
 							<option value="DSA" <%if("DSA".equals(alipay.getSign_type())){%>selected<%} %>>DSA</option>
 							<option value="RSA" <%if("RSA".equals(alipay.getSign_type())){%>selected<%} %>>RSA</option>
 						</select> 
					</li>
					<li><span>密钥：</span>
 						<input type ="text" id="private_key" name ="private_key" value ="<%=StringUtil.nullConvertToEmptyString(alipay.getPrivate_key())%>" maxlength="1000"  > 
					</li>
					<li><span>提供的URL：</span>
 						<input type ="text" id="url" name ="url" value ="<%=StringUtil.nullConvertToEmptyString(alipay.getUrl())%>" maxlength="1000"  > 
					</li>
					
					
					<li><span>密码：</span>
 						<input type ="password" id="password" name ="password" value ="" maxlength="30"  > 
					</li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button"  /></div>
		<input type="hidden" name="joint_num" value="${joint_num}"/>
	</form>
	</div>
</div>
<div id="box_yy"></div>

