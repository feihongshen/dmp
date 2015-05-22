<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.amazon.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Amazon amazon = (Amazon)request.getAttribute("amazonObject");
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
		<h1><div id="close_box" onclick="closeBox()"></div>亚马逊接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/amazon/saveamazon/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(amazon != null){ %>
						
						<li><span>上传文件地址：</span>
	 						<input type ="text" id="ws_url" name ="tnt_url"  maxlength="300" size="50"  value="<%=amazon.getTnt_url()%>"    > 
						</li>
						<li><span>上传文件备份地址：</span>
	 						<input type ="text" id="ws_url_bak" name ="tnt_url_bak"  maxlength="300" size="50"  value="<%=amazon.getTnt_url_bak()%>"    > 
						</li>
						<li><span>下载文件地址：</span>
	 						<input type ="text" id="full_url" name ="full_url"  maxlength="300" size="50"  value="<%=amazon.getFull_url()%>"    > 
						</li>
						
						<li><span>发送方id：</span>
	 						<input type ="text" id="senderIdentifier" name ="senderIdentifier"  maxlength="300"  value="<%=amazon.getSenderIdentifier()%>"  size="15" > 
						</li>
						<li><span>接收方id：</span>
	 						<input type ="text" id="recipientIdentifier" name ="recipientIdentifier"  maxlength="300"  value="<%=amazon.getRecipientIdentifier()%>"  size="15" > 
						</li>
						<li><span>CarrierSCAC：</span>
	 						<input type ="text" id="carrierSCAC" name ="carrierSCAC"  maxlength="300"  value="<%=amazon.getCarrierSCAC()%>"  size="15" > 
						</li>
						<li><span>Dss文件名：</span>
	 						<input type ="text" id="dssFile" name ="dssFile"  maxlength="300"  value="<%=amazon.getDssFile()%>"  size="15" > 
						</li>
						<li><span>系统customerid：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value="<%=amazon.getCustomerid()%>"  size="15" > 
						</li>
						<li><span>推送最大数量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="<%=amazon.getMaxCount()%>"  size="15" > 
						</li>
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(amazon.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						<li><span>显示配送员信息：</span>
							<select name="isShow">
									<option value="1" <%if(amazon.getIsShow()==1){%>selected<%}%>>是</option>
									<option value="0" <%if(amazon.getIsShow()==0){%>selected<%}%>>否</option>
							</select>
						</li>
						<li><span>是否合并妥投：</span>
							<select name="isHebingTuotou">
									<option value="0" <%if(amazon.getIsHebingTuotou()==0){%>selected<%}%>>是</option>
									<option value="1" <%if(amazon.getIsHebingTuotou()==1){%>selected<%}%>>否</option>
							</select>
						</li>
						<li><span>超期天数：</span>
							<input type ="text" id="delay" name ="delay"  maxlength="300"  size="15" value="<%=amazon.getDelay()%>" /> 
						</li>
						<li><span>是否自动推送：</span>
							<select name="isSystemCommit">
									<option value="1" <%if(amazon.getIsSystemCommit()==1){%>selected<%}%>>是</option>
									<option value="0" <%if(amazon.getIsSystemCommit()==0){%>selected<%}%>>否</option>
							</select>
						</li>
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						
						<li><span>上传文件地址：</span>
	 						<input type ="text" id="tnt_url" name ="tnt_url"  maxlength="300"  value=""   size="50"  > 
						</li>
						<li><span>上传文件备份地址：</span>
	 						<input type ="text" id="tnt_url_bak" name ="tnt_url_bak"  maxlength="300"  value=""   size="50"  > 
						</li>
						<li><span>下载文件地址：</span>
	 						<input type ="text" id="full_url" name ="full_url"  maxlength="300"  value=""   size="50"  > 
						</li>
						<li><span>发送方id：</span>
	 						<input type ="text" id="senderIdentifier" name ="senderIdentifier"  maxlength="300"   size="15" > 
						</li>
						<li><span>接收方id：</span>
	 						<input type ="text" id="recipientIdentifier" name ="recipientIdentifier"  maxlength="300"   size="15" > 
						</li>
						<li><span>CarrierSCAC：</span>
	 						<input type ="text" id="carrierSCAC" name ="carrierSCAC"  maxlength="300"    size="15" > 
						</li>
						<li><span>Dss文件名：</span>
	 						<input type ="text" id="dssFile" name ="dssFile"  maxlength="300"    size="15" > 
						</li>
						<li><span>系统customerid：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>推送最大数量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value=""  size="15" > 
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
						<li><span>显示配送员信息：</span>
							<select name="isShow">
									<option value="1" >是</option>
									<option value="0" >否</option>
							</select>
						</li>
						<li><span>是否合并妥投：</span>
							<select name="isHebingTuotou">
									<option value="0" >是</option>
									<option value="1" >否</option>
							</select>
						</li>
						<li><span>超期天数：</span>
							<input type ="text" id="delay" name ="delay"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>是否自动推送：</span>
							<select name="isSystemCommit">
									<option value="1" >是</option>
									<option value="0" >否</option>
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

