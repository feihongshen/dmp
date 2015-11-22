<%@page import="cn.explink.b2c.haoyigou.HaoYiGou"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
HaoYiGou hyg = request.getAttribute("hyg")==null?null:(HaoYiGou)request.getAttribute("hyg");
%>

<script type="text/javascript">   

</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg" style="width: 500px;">
		<h1><div id="close_box" onclick="closeBox()"></div>好易购对接设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="submitSaveForm(this);return false;" action="<%=request.getContextPath()%>/haoyigou/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(hyg != null){ %>
						
						<li><span>货运公司CODE：</span>
	 						<input type ="text" id="sendCode" name ="sendCode"  maxlength="300"  value="<%=hyg.getSendCode()%>" size="15" /> 
						</li>
						<li><span>客户代码CODE：</span>
	 						<input type ="text" id="customercode" name ="customercode"  maxlength="300"  value="<%=hyg.getCustomercode()%>" size="15" /> 
						</li>
						<li><span>电商标识：</span>
	 						<input type ="text" id="companyid" name ="companyid"  maxlength="300"  value="<%=hyg.getCompanyid()%>" size="15" /> 
						</li>
						<li><span>电商id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value="<%=hyg.getCustomerid()%>" size="15" /> 
						</li>
						
						<li><span>PARTERNER：</span>
	 						<input type ="text" id="partener" name ="partener"  maxlength="300"  value="<%=hyg.getPartener()%>" size="15" /> 
						</li>
						<li><span>MAXcount：</span>
	 						<input type ="text" id="maxcount" name ="maxcount"  maxlength="300"  value="<%=hyg.getMaxcount()%>" size="15" /> 
						</li>
						<li><span>FTP主机名 IP地址：</span>
	 						<input type ="text" id="ftp_host" name ="ftp_host"  maxlength="300"  value="<%=hyg.getFtp_host()%>" size="15" /> 
						</li>
						<li><span>FTP用户名：</span>
	 						<input type ="text" id="ftp_username" name ="ftp_username"  maxlength="300"  value="<%=hyg.getFtp_username()%>"  size="15" /> 
						</li>
						<li><span>FTP访问密码：</span>
	 						<input type ="text" id="ftp_password" name ="ftp_password"  maxlength="300"  value="<%=hyg.getFtp_password()%>"  size="15" /> 
						</li>
						<li><span>FTP端口号：</span>
	 						<input type ="text" id="ftp_port" name ="ftp_port"  maxlength="300"  value="<%=hyg.getFtp_port()%>"  size="15" /> 
						</li>
						<li><span>编码格式：</span>
	 						<input type ="text" id="charencode" name ="charencode"  maxlength="300"  value="<%=hyg.getCharencode()%>"  size="15" /> 
						</li>
						<li><span>FTP配送目录：</span>
	 						<input type ="text" id="upload_remotePathps" name ="upload_remotePathps"  maxlength="300"  value="<%=hyg.getUpload_remotePathps()%>"  size="15" /> 
						</li>
						<li><span>FTP退货目录：</span>
	 						<input type ="text" id="upload_remotePathth" name ="upload_remotePathth"  maxlength="300"  value="<%=hyg.getUpload_remotePathth()%>"  size="15" /> 
						</li>
						<li><span>服务器位置：</span>
	 						<input type ="text" id="uploadPath" name ="uploadPath"  maxlength="300"  value="<%=hyg.getUploadPath()%>"  size="15" /> 
						</li>
						<li><span>服务器位置(备份)：</span>
	 						<input type ="text" id="uploadPath_bak" name ="uploadPath_bak"  maxlength="300"  value="<%=hyg.getUploadPath_bak()%>"  size="15" /> 
						</li>	
						<li><span>设置密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30"  value="${password}" size="15" /> 
						</li>
					<%}else{ %>
						<li><span>货运公司CODE：</span>
	 						<input type ="text" id="sendCode" name ="sendCode"  maxlength="300"  value="" size="15" /> 
						</li>
						<li><span>客户代码CODE：</span>
	 						<input type ="text" id="customercode" name ="customercode"  maxlength="300"  value="" size="15" /> 
						</li>
						<li><span>电商标识：</span>
	 						<input type ="text" id="companyid" name ="companyid"  maxlength="300"  value="" size="15" /> 
						</li>
						<li><span>电商id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value=""  size="15" /> 
						</li>
						<li><span>PARTERNER：</span>
	 						<input type ="text" id="partener" name ="partener"  maxlength="300"  value="" size="15" /> 
						</li>
						===========FTP设置如下===========<br/>
						<li><span>MAXcount：</span>
	 						<input type ="text" id="maxcount" name ="maxcount"  maxlength="300"  value="" size="15" /> 
						</li>
						<li><span>FTP主机名 IP地址：</span>
	 						<input type ="text" id="ftp_host" name ="ftp_host"  maxlength="300"  value="" size="15" /> 
						</li>
						<li><span>FTP用户名：</span>
	 						<input type ="text" id="ftp_username" name ="ftp_username"  maxlength="300"  value=""  size="15" /> 
						</li>
						<li><span>FTP访问密码：</span>
	 						<input type ="text" id="ftp_password" name ="ftp_password"  maxlength="300"  value=""  size="15" /> 
						</li>
						<li><span>FTP端口号：</span>
	 						<input type ="text" id="ftp_port" name ="ftp_port"  maxlength="300"  value=""  size="15" /> 
						</li>
						<li><span>编码格式：</span>
	 						<input type ="text" id="charencode" name ="charencode"  maxlength="300"  value=""  size="15" /> 
						</li>
						<li><span>FTP配送目录：</span>
	 						<input type ="text" id="upload_remotePathps" name ="upload_remotePathps"  maxlength="300"  value=""  size="15" /> 
						</li>
						<li><span>FTP退货目录：</span>
	 						<input type ="text" id="upload_remotePathth" name ="upload_remotePathth"  maxlength="300"  value=""  size="15" /> 
						</li>
						<li><span>服务器位置：</span>
	 						<input type ="text" id="uploadPath" name ="uploadPath"  maxlength="300"  value=""  size="15" /> 
						</li>
						<li><span>服务器位置(备份)：</span>
	 						<input type ="text" id="uploadPath_bak" name ="uploadPath_bak"  maxlength="300"  value=""  size="15" /> 
						</li>
						<li><span>设置密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="15" /> 
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

