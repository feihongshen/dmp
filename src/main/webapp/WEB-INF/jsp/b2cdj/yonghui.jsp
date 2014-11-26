<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.yonghuics.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Yonghui yihaodian = (Yonghui)request.getAttribute("yonghuiObject");
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
		<h1><div id="close_box" onclick="closeBox()"></div>永辉超市对接设置</h1>
		<form id="tmall_save_Form" name="tmall_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/yonghuics/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(yihaodian != null){ %>
						<li><span>物流公司id标识：</span>
							<input type ="text" id="userCode" name ="userCode" value="<%=yihaodian.getUserCode() %>"  maxlength="300">
						</li>
						<li><span>导出订单数据URL：</span>
	 						<input type ="text" id="download_URL" name ="download_URL" maxlength="300"   value="<%=yihaodian.getDownload_URL()%>"   > 
						</li>
						<li><span>每次获取数量：</span>
	 						<input type ="text" id="exportCwb_pageSize" name ="exportCwb_pageSize" maxlength="300"  size="15"  value="<%=yihaodian.getExportCwb_pageSize()%>"  > 
						</li>
						<li><span>每次下载次数：</span>
	 						<input type ="text" id="loopcount" name ="loopcount" maxlength="300"  size="15"  value="<%=yihaodian.getLoopcount()%>"  > 
						</li>
						<li><span>导出数据回调URL：</span>
	 						<input type ="text" id="callback_URL" name ="callback_URL"  maxlength="300"  value="<%=yihaodian.getCallback_URL() %>"  > 
						</li>
						<li><span>每次回调订单数量：</span>
	 						<input type ="text" id="callBackCount" name ="callBackCount" maxlength="300"  size="15"  value="<%=yihaodian.getCallBackCount()%>"  > 
						</li>
						
						<li><span>状态反馈URL：</span>
	 						<input type ="text" id="trackLog_URL" name ="trackLog_URL"  maxlength="300"  value="<%=yihaodian.getTrackLog_URL()%>"    > 
						</li>
						<li><span>私钥信息：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"  value="<%=yihaodian.getPrivate_key()%>"  > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value="<%=yihaodian.getCustomerids()%>"  size="15" > 
						</li>
						<li><span>是否开启订单下载：</span>
	 						<input type="radio" name="isopenDataDownload" value="1" <%if(yihaodian.getIsopenDataDownload()==1){%>checked<%}%>/> 开启
	 						<input type="radio" name="isopenDataDownload" value="0"  <%if(yihaodian.getIsopenDataDownload()==0){%>checked<%}%> /> 关闭
						</li>
						
						
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(yihaodian.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li><span>物流公司id标识：</span>
							<input type ="text" id="userCode" name ="userCode" value=""  maxlength="300">
						</li>
						<li><span>导出订单数据URL：</span>
	 						<input type ="text" id="download_URL" name ="download_URL" maxlength="300"   value=""   > 
						</li>
						<li><span>每次获取数量：</span>
	 						<input type ="text" id="exportCwb_pageSize" name ="exportCwb_pageSize" maxlength="300"  size="15"  value=""  > 
						</li>
						<li><span>每次下载次数：</span>
	 						<input type ="text" id="loopcount" name ="loopcount" maxlength="300"  size="15"  value=""  > 
						</li>
						<li><span>导出数据回调URL：</span>
	 						<input type ="text" id="callback_URL" name ="callback_URL"  maxlength="300"  value=""  > 
						</li>
						<li><span>每次回调订单数量：</span>
	 						<input type ="text" id="callBackCount" name ="callBackCount" maxlength="300"  size="15"  value=""  > 
						</li>
						
						<li><span>状态反馈URL：</span>
	 						<input type ="text" id="trackLog_URL" name ="trackLog_URL"  maxlength="300"  value=""    > 
						</li>
						<li><span>私钥信息：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"  value=""  > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>是否开启订单下载：</span>
	 						<input type="radio" name="isopenDataDownload" value="1" /> 开启
	 						<input type="radio" name="isopenDataDownload" value="0"   /> 关闭
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

