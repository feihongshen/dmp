<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.chinamobile.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Chinamobile letv = (Chinamobile)request.getAttribute("chinamobileObject");

%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>移动接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/chinamobile/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(letv != null){ %>
						
						<li><span>承运商编号：</span>
	 						<input type ="text" id="expressid" name ="expressid"  maxlength="300" size="20" value="<%=letv.getExpressid()%>"  > 
						</li>
						<li><span>状态推送URL：</span>
	 						<input type ="text" id="feedback_url" name ="feedback_url"  size="40"  maxlength="300"  value="<%=letv.getFeedback_url()%>"   > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  size="10"  maxlength="300"  value="<%=letv.getCustomerid()%>"   > 
						</li>
						<li><span>每次反馈数量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="<%=letv.getMaxCount()%>"  size="10"> 
						</li>
						<li><span>FTP-HOST：</span>
	 						<input type ="text" id="ftp_host" name ="ftp_host"  maxlength="300"  value="<%=letv.getFtp_host()%>"  size="30"> 
						</li>
						<li><span>FTP-PORT：</span>
	 						<input type ="text" id="ftp_port" name ="ftp_port"  maxlength="300"  value="<%=letv.getFtp_port()%>"  size="10"> 
						</li>
						<li><span>FTP-USERNAME：</span>
	 						<input type ="text" id="ftp_username" name ="ftp_username"  maxlength="300"  value="<%=letv.getFtp_username()%>"  size="20"> 
						</li>
						<li><span>FTP-PASSWORD：</span>
	 						<input type ="text" id="ftp_password" name ="ftp_password"  maxlength="300"  value="<%=letv.getFtp_password()%>"  size="20"> 
						</li>
						<li><span>FTP目录：</span>
	 						<input type ="text" id="remotePath" name ="remotePath"  maxlength="300"  value="<%=letv.getRemotePath()%>"  size="30"> 
						</li>
						<li><span>下载目录：</span>
	 						<input type ="text" id="downloadPath" name ="downloadPath"  maxlength="300"  value="<%=letv.getDownloadPath()%>"  size="30"> 
						</li>
						<li><span>备份目录：</span>
	 						<input type ="text" id="downloadPath_bak" name ="downloadPath_bak"  maxlength="300"  value="<%=letv.getDownloadPath_bak()%>"  size="30"> 
						</li>
						<li><span>编码方式：</span>
	 						<input type ="text" id="charencode" name ="charencode"  maxlength="300"  value="<%=letv.getCharencode()%>"  size="10"> 
	 						
						</li>
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li><span>承运商编号：</span>
	 						<input type ="text" id="expressid" name ="expressid"  maxlength="300" size="20" value=""  > 
						</li>
						<li><span>状态推送URL：</span>
	 						<input type ="text" id="feedback_url" name ="feedback_url"  size="40"  maxlength="300"  value=""   > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  size="10"  maxlength="300"  value=""   > 
						</li>
						<li><span>每次反馈数量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value=""  size="10"> 
						</li>
						<li><span>FTP-HOST：</span>
	 						<input type ="text" id="ftp_host" name ="ftp_host"  maxlength="300"  value=""  size="30"> 
						</li>
						<li><span>FTP-PORT：</span>
	 						<input type ="text" id="ftp_port" name ="ftp_port"  maxlength="300"  value=""  size="10"> 
						</li>
						<li><span>FTP-USERNAME：</span>
	 						<input type ="text" id="ftp_username" name ="ftp_username"  maxlength="300"  value=""  size="20"> 
						</li>
						<li><span>FTP-PASSWORD：</span>
	 						<input type ="text" id="ftp_password" name ="ftp_password"  maxlength="300"  value=""  size="20"> 
						</li>
						<li><span>FTP目录：</span>
	 						<input type ="text" id="remotePath" name ="remotePath"  maxlength="300"  value=""  size="30"> 
						</li>
						<li><span>下载目录：</span>
	 						<input type ="text" id="downloadPath" name ="downloadPath"  maxlength="300"  value=""  size="30"> 
						</li>
						<li><span>备份目录：</span>
	 						<input type ="text" id="downloadPath_bak" name ="downloadPath_bak"  maxlength="300"  value=""  size="30"> 
						</li>
						<li><span>编码方式：</span>
	 						<input type ="text" id="charencode" name ="charencode"  maxlength="300"  value=""  size="10"> 
	 						
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

