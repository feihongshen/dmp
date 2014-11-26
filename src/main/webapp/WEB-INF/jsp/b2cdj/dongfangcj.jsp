<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.dongfangcj.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
DongFangCJ cj = (DongFangCJ)request.getAttribute("dongfangcjObject");
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
		<h1><div id="close_box" onclick="closeBox()"></div>东方CJ接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/dongfangcj/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(cj != null){ %>
						
						
						<li><span>FTP主机名：</span>
	 						<input type ="text" id="ftp_host" name ="ftp_host"  maxlength="300" size="15" value="<%=cj.getFtp_host()%>"  > 
						</li>
						<li><span>FTP端口号：</span>
	 						<input type ="text" id="ftp_port" name ="ftp_port"  maxlength="300" size="15" value="<%=cj.getFtp_port()%>"  > 
						</li>
						<li><span>FTP用户名：</span>
	 						<input type ="text" id="ftp_username" name ="ftp_username"  size="15"  maxlength="300"  value="<%=cj.getFtp_username()%>"   > 
						</li>
						<li><span>FTP密码：</span>
	 						<input type ="text" id="ftp_password" name ="ftp_password"  maxlength="300"  value="<%=cj.getFtp_password()%>"  size="15"> 
						</li>
						<li><span>编码方式：</span>
	 						<input type ="text" id="charencode" name ="charencode"  maxlength="300"  value="<%=cj.getCharencode()%>"  size="10"> 
						</li>
						<li><span>远程位置(下载)：</span>
	 						<input type ="text" id="put_remotePath" name ="put_remotePath"  maxlength="300"  value="<%=cj.getPut_remotePath()%>"  size="25"> 
						</li>
						<li><span>远程位置(上传)：</span>
	 						<input type ="text" id="get_remotePath" name ="get_remotePath"  maxlength="300"  value="<%=cj.getGet_remotePath()%>"  size="25"> 
						</li>
						<li><span>本地位置(下载)：</span>
	 						<input type ="text" id="downloadPath" name ="downloadPath"  maxlength="300"  value="<%=cj.getDownloadPath()%>"  size="25"> 
						</li>
						<li><span>本地备份(下载)：</span>
	 						<input type ="text" id="downloadPath_bak" name ="downloadPath_bak"  maxlength="300"  value="<%=cj.getDownloadPath_bak()%>"  size="25"> 
						</li>
						
						<li><span>本地位置(上传)：</span>
	 						<input type ="text" id="uploadPath" name ="uploadPath"  maxlength="300"  value="<%=cj.getUploadPath()%>"  size="25"> 
						</li>
						<li><span>本地备份(上传)：</span>
	 						<input type ="text" id="uploadPath_bak" name ="uploadPath_bak"  maxlength="300"  value="<%=cj.getUploadPath_bak()%>"  size="25"> 
						</li>
						<li><span>每次查询大小：</span>
	 						<input type ="text" id="maxcount" name ="maxcount"  maxlength="300"  value="<%=cj.getMaxcount()%>"  size="15"> 
						</li>
						
						<li><span>唯一标识：</span>
	 						<input type ="text" id="partener" name ="partener"  maxlength="300"  value="<%=cj.getPartener()%>"  size="15"> 
						</li>
						
						<li><span>系统中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value="<%=cj.getCustomerids()%>"  size="15"> 
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
						<li><span>是否删除：</span>
	 						<input type ="radio" id="isdelDirFlag1" name ="isdelDirFlag"   value="1" <%if(cj.isIsdelDirFlag()==true){%>checked<%}%>   > 删除
	 						<input type ="radio" id="isdelDirFlag2" name ="isdelDirFlag"   value="0" <%if(cj.isIsdelDirFlag()==false){%>checked<%}%>  > 不删除
						</li>
						<li><span>查询URL：</span>
	 						<input type ="text" id="search_url" name ="search_url"  maxlength="300"  value="<%=cj.getSearch_url()%>"  size="25"><font color="red">*CJ请求URL</font>
						</li>
						<li><span>物流商ID：</span>
	 						<input type ="text" id="company_num" name ="company_num"  maxlength="300"  value="<%=cj.getCompany_num()%>"  size="25"><font color="red">*CJ唯一标识</font>
						</li>
						<li><span>密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"  value="<%=cj.getPrivate_key()%>"  size="25"><font color="red">*密钥</font>
						</li>
						
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="25" > 
						</li>
					<%}else{ %>
						
						<li><span>FTP主机名：</span>
	 						<input type ="text" id="ftp_host" name ="ftp_host"  maxlength="300" size="15" value=""  > 
						</li>
						<li><span>FTP端口号：</span>
	 						<input type ="text" id="ftp_port" name ="ftp_port"  maxlength="300" size="15" value=""  > 
						</li>
						<li><span>FTP用户名：</span>
	 						<input type ="text" id="ftp_username" name ="ftp_username"  size="15"  maxlength="300"  value=""   > 
						</li>
						<li><span>FTP密码：</span>
	 						<input type ="text" id="ftp_password" name ="ftp_password"  maxlength="300"  value=""  size="15"> 
						</li>
						<li><span>编码方式：</span>
	 						<input type ="text" id="charencode" name ="charencode"  maxlength="300"  value=""  size="10"> 
						</li>
						<li><span>远程位置(下载)：</span>
	 						<input type ="text" id="put_remotePath" name ="put_remotePath"  maxlength="300"  value=""  size="25"> 
						</li>
						<li><span>远程位置(上传)：</span>
	 						<input type ="text" id="get_remotePath" name ="get_remotePath"  maxlength="300"  value=""  size="25"> 
						</li>
						<li><span>本地位置(下载)：</span>
	 						<input type ="text" id="downloadPath" name ="downloadPath"  maxlength="300"  value=""  size="25"> 
						</li>
						<li><span>本地备份(下载)：</span>
	 						<input type ="text" id="downloadPath_bak" name ="downloadPath_bak"  maxlength="300"  value=""  size="25"> 
						</li>
						
						<li><span>本地位置(上传)：</span>
	 						<input type ="text" id="uploadPath" name ="uploadPath"  maxlength="300"  value=""  size="25"> 
						</li>
						<li><span>本地备份(上传)：</span>
	 						<input type ="text" id="uploadPath_bak" name ="uploadPath_bak"  maxlength="300"  value=""  size="25"> 
						</li>
						<li><span>每次查询大小：</span>
	 						<input type ="text" id="maxcount" name ="maxcount"  maxlength="300"  value=""  size="15"> 
						</li>
						
						
						<li><span>唯一标识：</span>
	 						<input type ="text" id="partener_delivery" name ="partener_delivery"  maxlength="300"  value=""  size="15"> 
						</li>
						
						
						
						<li><span>系统中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value=""  size="15"> 
						</li>
					
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>"  ><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						
						<li><span>是否删除：</span>
	 						<input type ="radio" id="isdelDirFlag1" name ="isdelDirFlag"   value="1" checked > 删除
	 						<input type ="radio" id="isdelDirFlag2" name ="isdelDirFlag"   value="0" > 不删除
						</li>
						
						<li><span>查询URL：</span>
	 						<input type ="text" id="search_url" name ="search_url"  maxlength="300"  value=""  size="25"><font color="red">*CJ请求URL</font>
						</li>
						<li><span>物流商ID：</span>
	 						<input type ="text" id="company_num" name ="company_num"  maxlength="300"  value=""  size="25"><font color="red">*CJ唯一标识</font>
						</li>
						<li><span>密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"  value=""  size="25"><font color="red">*密钥</font>
						</li>
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="25" > 
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

