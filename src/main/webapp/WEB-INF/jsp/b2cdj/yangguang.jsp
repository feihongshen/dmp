<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.yangguang.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
YangGuang yangguang = (YangGuang)request.getAttribute("yangguangObject");
List<Branch> warehouselist=(List<Branch>)request.getAttribute("warehouselist");

List<YangGuangdiff> multidifflist=(List<YangGuangdiff>)request.getAttribute("multidifflist");

%>


<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>央广购物接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/yangguang/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(yangguang != null){ %>
						
						
						<li><span>FTP主机名：</span>
	 						<input type ="text" id="host" name ="host"  maxlength="300" size="30" value="<%=yangguang.getHost()%>"  > 
						</li>
						<li><span>FTP端口号：</span>
	 						<input type ="text" id="port" name ="port"  size="15"  maxlength="300"  value="<%=yangguang.getPort()%>"   > 
						</li>
						<li>
						<span>多个配置：</span>
						</li>
						<table  width="600" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table"> 
						<tr  class="font_1">
							<td>列表</td>
							<td>FTP用户名</td>
							<td>FTP密码</td>
							<td>唯一标识</td>
							<td>系统中id</td>
							<td>导入库房</td>
							<td>是否开启</td>
						</tr>
						<tr >
							<td>天津</td>
							<td><input type ="text" id="username1" name ="username1"  maxlength="300"  value="<%=multidifflist.get(0).getUsername()%>"  size="10"> </td>
							<td><input type ="text" id="pwd1" name ="pwd1"  maxlength="300"  value="<%=multidifflist.get(0).getPwd()%>"   size="10"></td>
							<td><input type ="text" id="express_id1" name ="express_id1"  maxlength="300"  value="<%=multidifflist.get(0).getExpress_id()%>"   size="10"></td>
							<td><input type ="text" id="customerids1" name ="customerids1"  maxlength="300"  value="<%=multidifflist.get(0).getCustomerids()%>"   size="10"></td>
							<td><select name="warehouseid1">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(multidifflist.get(0).getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
								</select>
							</td>
							<td>
								<input type ="radio" name="isopen1" value="1"  <%if(multidifflist.get(0).getIsopen()==1){%>checked<%}%>/>开启
								<input type ="radio" name="isopen1" value="0"  <%if(multidifflist.get(0).getIsopen()==0){%>checked<%}%> />关闭
							</td>
						</tr>
						<tr >
							<td>北京</td>
							<td><input type ="text" id="username2" name ="username2"  maxlength="300"  value="<%=multidifflist.get(1).getUsername()%>"   size="10"> </td>
							<td><input type ="text" id="pwd2" name ="pwd2"  maxlength="300"  value="<%=multidifflist.get(1).getPwd()%>"  size="10"></td>
							<td><input type ="text" id="express_id2" name ="express_id2"  maxlength="300"  value="<%=multidifflist.get(1).getExpress_id()%>"   size="10"></td>
							<td><input type ="text" id="customerids2" name ="customerids2"  maxlength="300"  value="<%=multidifflist.get(1).getCustomerids()%>"   size="10"></td>
							<td><select name="warehouseid2">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(multidifflist.get(1).getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
								</select>
							</td>
							<td>
								<input type ="radio" name="isopen2" value="1"  <%if(multidifflist.get(1).getIsopen()==1){%>checked<%}%>/>开启
								<input type ="radio" name="isopen2" value="0"  <%if(multidifflist.get(1).getIsopen()==0){%>checked<%}%> />关闭
							</td>
						</tr>
						<tr >
							<td>河北</td>
							<td><input type ="text" id="username3" name ="username3"  maxlength="300"  value="<%=multidifflist.get(2).getUsername()%>"   size="10"> </td>
							<td><input type ="text" id="pwd3" name ="pwd3"  maxlength="300"  value="<%=multidifflist.get(2).getPwd()%>"  size="10"></td>
							<td><input type ="text" id="express_id3" name ="express_id3"  maxlength="300"  value="<%=multidifflist.get(2).getExpress_id()%>"   size="10"></td>
							<td><input type ="text" id="customerids3" name ="customerids3"  maxlength="300"  value="<%=multidifflist.get(2).getCustomerids()%>"   size="10"></td>
							<td><select name="warehouseid3">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(multidifflist.get(2).getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
								</select>
							</td>
							<td>
								<input type ="radio" name="isopen3" value="1"  <%if(multidifflist.get(2).getIsopen()==1){%>checked<%}%>/>开启
								<input type ="radio" name="isopen3" value="0"  <%if(multidifflist.get(2).getIsopen()==0){%>checked<%}%> />关闭
							</td>
						</tr>
						<tr >
							<td>山东</td>
							<td><input type ="text" id="username4" name ="username4"  maxlength="300"  value="<%=multidifflist.get(3).getUsername()%>"   size="10"> </td>
							<td><input type ="text" id="pwd4" name ="pwd4"  maxlength="300"  value="<%=multidifflist.get(3).getPwd()%>"   size="10"></td>
							<td><input type ="text" id="express_id4" name ="express_id4"  maxlength="300"  value="<%=multidifflist.get(3).getExpress_id()%>"   size="10"></td>
							<td><input type ="text" id="customerids4" name ="customerids4"  maxlength="300"  value="<%=multidifflist.get(3).getCustomerids()%>"   size="10"></td>
							<td><select name="warehouseid4">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(multidifflist.get(3).getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
								</select>
							</td>
							<td>
								<input type ="radio" name="isopen4" value="1"  <%if(multidifflist.get(3).getIsopen()==1){%>checked<%}%>/>开启
								<input type ="radio" name="isopen4" value="0"  <%if(multidifflist.get(3).getIsopen()==0){%>checked<%}%> />关闭
							</td>
						</tr>
						</table>
						
						
						<li><span>远程位置(下载)：</span>
	 						<input type ="text" id="download_remotePath" name ="download_remotePath"  maxlength="300"  value="<%=yangguang.getDownload_remotePath()%>"  size="30"> 
						</li>
						<li><span>远程位置(上传)：</span>
	 						<input type ="text" id="upload_remotePath" name ="upload_remotePath"  maxlength="300"  value="<%=yangguang.getUpload_remotePath()%>"  size="30"> 
						</li>
						<li><span>本地位置(下载)：</span>
	 						<input type ="text" id="downloadPath" name ="downloadPath"  maxlength="300"   value="<%=yangguang.getDownloadPath()%>"  size="30"> 
						</li>
						<li><span>本地备份(下载)：</span>
	 						<input type ="text" id="downloadPath_bak" name ="downloadPath_bak"  maxlength="300"  value="<%=yangguang.getDownloadPath_bak()%>"  size="30"> 
						</li>
						<li><span>本地位置(上传)：</span>
	 						<input type ="text" id="uploadPath" name ="uploadPath"  maxlength="300"   value="<%=yangguang.getUploadPath()%>"  size="30"> 
						</li>
						<li><span>本地备份(上传)：</span>
	 						<input type ="text" id="uploadPath_bak" name ="uploadPath_bak"  maxlength="300"   value="<%=yangguang.getUploadPath_bak()%>"  size="30"> 
						</li>
						
					
						
						<li><span>回传时间：</span>
	 						<input type ="text" id="feedbackHours" name ="feedbackHours"  maxlength="300"  value="<%=yangguang.getFeedbackHours()%>"  size="15"> 
						</li>
						<li><span>保存时间 ：</span>
	 						<input type ="text" id="keepDays" name ="keepDays"  maxlength="300"  value="<%=yangguang.getKeepDays()%>"  size="15"> 
						</li>
						<li><span>编码方式：</span>
	 						<select name="charencode">
	 							<option value="UTF-8" <%if(yangguang.getCharencode().equals("UTF-8")){%>selected<%}%>>UTF-8</option>
	 							<option value="GBK"  <%if(yangguang.getCharencode().equals("GBK")){%>selected<%}%>>GBK</option>
	 						</select> 
						</li>
						<li><span>服务器类型：</span>
	 						<select name="server_sys">
	 							<option value="WINDOWS"  <%if(yangguang.getServer_sys().equals("WINDOWS")){%>selected<%}%>>WINDOWS</option>
	 							<option value="LINUX"   <%if(yangguang.getServer_sys().equals("LINUX")){%>selected<%}%>>LINUX</option>
	 						</select> 
						</li>
						
						
						
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						
						<li><span>FTP主机名：</span>
	 						<input type ="text" id="host" name ="host"  maxlength="300" size="30" value=""  > 
						</li>
						<li><span>FTP端口号：</span>
	 						<input type ="text" id="port" name ="port"  size="15"  maxlength="300"  value=""   > 
						</li>
						
						<li>
						<span>多个配置：</span>
						</li>
						<table  width="600" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table"> 
						<tr  class="font_1">
							<td>列表</td>
							<td>FTP用户名</td>
							<td>FTP密码</td>
							<td>唯一标识</td>
							<td>系统中id</td>
							<td>导入库房</td>
							<td>是否开启</td>
						</tr>
						<tr >
							<td>天津</td>
							<td><input type ="text" id="username1" name ="username1"  maxlength="300"  value=""  size="10"> </td>
							<td><input type ="text" id="pwd1" name ="pwd1"  maxlength="300"  value=""   size="10"></td>
							<td><input type ="text" id="express_id1" name ="express_id1"  maxlength="300"  value=""   size="10"></td>
							<td><input type ="text" id="customerids1" name ="customerids1"  maxlength="300"  value=""   size="10"></td>
							<td><select name="warehouseid1">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" ><%=b.getBranchname() %></option>
								<%}%>
								</select>
							</td>
							<td><input type ="radio" name="isopen1" value="1" checked/>开启<input type ="radio" name="isopen1" value="0"/>关闭</td>
						</tr>
						<tr >
							<td>北京</td>
							<td><input type ="text" id="username2" name ="username2"  maxlength="300"  value=""   size="10"> </td>
							<td><input type ="text" id="pwd2" name ="pwd2"  maxlength="300"  value=""  size="10"></td>
							<td><input type ="text" id="express_id2" name ="express_id2"  maxlength="300"  value=""   size="10"></td>
							<td><input type ="text" id="customerids2" name ="customerids2"  maxlength="300"  value=""   size="10"></td>
							<td><select name="warehouseid2">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" ><%=b.getBranchname() %></option>
								<%}%>
								</select>
							</td>
							<td><input type ="radio" name="isopen2" value="1" checked/>开启<input type ="radio" name="isopen2" value="0"/>关闭</td>
						</tr>
						<tr >
							<td>河北</td>
							<td><input type ="text" id="username3" name ="username3"  maxlength="300"  value=""   size="10"> </td>
							<td><input type ="text" id="pwd3" name ="pwd3"  maxlength="300"  value=""  size="10"></td>
							<td><input type ="text" id="express_id3" name ="express_id3"  maxlength="300"  value=""   size="10"></td>
							<td><input type ="text" id="customerids3" name ="customerids3"  maxlength="300"  value=""   size="10"></td>
							<td><select name="warehouseid3">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" ><%=b.getBranchname() %></option>
								<%}%>
								</select>
							</td>
							<td><input type ="radio" name="isopen3" value="1" checked/>开启<input type ="radio" name="isopen3" value="0"/>关闭</td>
						</tr>
						<tr >
							<td>山东</td>
							<td><input type ="text" id="username4" name ="username4"  maxlength="300"  value=""   size="10"> </td>
							<td><input type ="text" id="pwd4" name ="pwd4"  maxlength="300"  value=""   size="10"></td>
							<td><input type ="text" id="express_id4" name ="express_id4"  maxlength="300"  value=""   size="10"></td>
							<td><input type ="text" id="customerids4" name ="customerids4"  maxlength="300"  value=""   size="10"></td>
							<td><select name="warehouseid4">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" ><%=b.getBranchname() %></option>
								<%}%>
								</select>
							</td>
							<td><input type ="radio" name="isopen4" value="1" checked/>开启<input type ="radio" name="isopen4" value="0"/>关闭</td>
						</tr>
						</table>
						
						
						
						<li><span>远程位置(下载)：</span>
	 						<input type ="text" id="download_remotePath" name ="download_remotePath"  maxlength="300"  value=""  size="30"> 
						</li>
						<li><span>远程位置(上传)：</span>
	 						<input type ="text" id="upload_remotePath" name ="upload_remotePath"  maxlength="300"  value=""  size="30"> 
						</li>
						
						<li><span>本地位置(下载)：</span>
	 						<input type ="text" id="downloadPath" name ="downloadPath"  maxlength="300"  value=""  size="30"> 
						</li>
						<li><span>本地备份(下载)：</span>
	 						<input type ="text" id="downloadPath_bak" name ="downloadPath_bak"  maxlength="300"  value=""  size="30"> 
						</li>
						<li><span>本地位置(上传)：</span>
	 						<input type ="text" id="uploadPath" name ="uploadPath"  maxlength="300"  value=""  size="30"> 
						</li>
						<li><span>本地备份(上传)：</span>
	 						<input type ="text" id="uploadPath_bak" name ="uploadPath_bak"  maxlength="300"  value=""  size="30"> 
						</li>
						
						<li><span>回传时间：</span>
	 						<input type ="text" id="feedbackHours" name ="feedbackHours"  maxlength="300"  value=""  size="15"> 
						</li>
						<li><span>保存时间 ：</span>
	 						<input type ="text" id="keepDays" name ="keepDays"  maxlength="300"  value=""  size="15"> 
						</li>
						<li><span>编码方式：</span>
	 						<select name="charencode">
	 							<option value="UTF-8">UTF-8</option>
	 							<option value="GBK">GBK</option>
	 						</select> 
						</li>
						<li><span>服务器类型：</span>
	 						<select name="server_sys">
	 							<option value="WINDOWS">WINDOWS</option>
	 							<option value="LINUX">LINUX</option>
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

