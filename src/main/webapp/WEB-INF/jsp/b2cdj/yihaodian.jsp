<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.yihaodian.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Yihaodian yihaodian = (Yihaodian)request.getAttribute("yihaodianObject");
List<Branch> warehouselist=(List<Branch>)request.getAttribute("warehouselist");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg" style="width: 500px;">
		<h1><div id="close_box" onclick="closeBox()"></div>一号店对接设置</h1>
		<form id="tmall_save_Form" name="tmall_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/yihaodian/saveYihaodian/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(yihaodian != null){ %>
						<li><span>物流公司id标识：</span>
							<input type ="text" id="userCode" name ="userCode" value="<%=yihaodian.getUserCode() %>"  maxlength="300">
						</li>
						<li><span>导出订单数据URL：</span>
	 						<input type ="text" id="exportCwb_URL" name ="exportCwb_URL" maxlength="300"   value="<%=yihaodian.getExportCwb_URL()%>"   > 
						</li>
						<li><span>每次获取数量：</span>
	 						<input type ="text" id="exportCwb_pageSize" name ="exportCwb_pageSize" maxlength="300"  size="15"  value="<%=yihaodian.getExportCwb_pageSize()%>"  > 
						</li>
						<li><span>每次下载次数：</span>
	 						<input type ="text" id="loopcount" name ="loopcount" maxlength="300"  size="15"  value="<%=yihaodian.getLoopcount()%>"  > 
						</li>
						<li><span>导出数据回调URL：</span>
	 						<input type ="text" id="exportSuccess_URL" name ="exportSuccess_URL"  maxlength="300"  value="<%=yihaodian.getExportSuccess_URL() %>"  > 
						</li>
						<li><span>每次回调订单数量：</span>
	 						<input type ="text" id="callBackCount" name ="callBackCount" maxlength="300"  size="15"  value="<%=yihaodian.getCallBackCount()%>"  > 
						</li>
						<li><span>配送结果反馈URL：</span>
	 						<input type ="text" id="deliveryResult_URL" name ="deliveryResult_URL"  maxlength="300"  value="<%=yihaodian.getDeliveryResult_URL() %>"  > 
						</li>
						<li><span>支付信息修改URL：</span>
	 						<input type ="text" id="updatePayResult_URL" name ="updatePayResult_URL"  maxlength="300"  value="<%=yihaodian.getUpdatePayResult_URL() %>"  > 
						</li>
						<li><span>跟踪日志反馈URL：</span>
	 						<input type ="text" id="trackLog_URL" name ="trackLog_URL"  maxlength="300"  value="<%=yihaodian.getTrackLog_URL()%>"    > 
						</li>
						<li><span>私钥信息：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"  value="<%=yihaodian.getPrivate_key()%>"  > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value="<%=yihaodian.getCustomerids()%>"  size="15" > 
						</li>
						<li><span>药网customerid：</span>
	 						<input type ="text" id="ywcustomerid" name ="ywcustomerid"  maxlength="300"  value="<%=yihaodian.getYwcustomerid()%>"  size="15" > 
						</li>
						<li><span>是否开启订单下载：</span>
	 						<input type="radio" name="isopenDataDownload" value="1" <%if(yihaodian.getIsopenDataDownload()==1){%>checked<%}%>/> 开启
	 						<input type="radio" name="isopenDataDownload" value="0"  <%if(yihaodian.getIsopenDataDownload()==0){%>checked<%}%> /> 关闭
						</li>
						<li><span>开启药网URL：</span>
	 						<input type="radio" name="isopenywaddressflag" value="1" <%if(yihaodian.getIsopenywaddressflag()==1){%>checked<%}%>/> 开启
	 						<input type="radio" name="isopenywaddressflag" value="0"  <%if(yihaodian.getIsopenywaddressflag()==0){%>checked<%}%> /> 关闭
						</li>
						<li><span>药网导出订单URL：</span>
	 						<input type ="text" id="ywexportCwb_URL" name ="ywexportCwb_URL" maxlength="300"   value="<%=yihaodian.getYwexportCwb_URL()%>"   > 
						</li>
							<li><span>药网导出回调URL：</span>
	 						<input type ="text" id="ywexportSuccess_URL" name ="ywexportSuccess_URL"  maxlength="300"  value="<%=yihaodian.getYwexportSuccess_URL() %>"  > 
						</li>
						<li><span>药网配送反馈URL：</span>
	 						<input type ="text" id="ywdeliveryResult_URL" name ="ywdeliveryResult_URL"  maxlength="300"  value="<%=yihaodian.getYwdeliveryResult_URL() %>"  > 
						</li>
						<li><span>药网跟踪反馈URL：</span>
	 						<input type ="text" id="ywtrackLog_URL" name ="ywtrackLog_URL"  maxlength="300"  value="<%=yihaodian.getYwtrackLog_URL()%>"    > 
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
	 						<input type ="text" id="exportCwb_URL" name ="exportCwb_URL" maxlength="300"  value=""   > 
						</li>
						<li><span>每次获取数量：</span>
	 						<input type ="text" id="exportCwb_pageSize" name ="exportCwb_pageSize" size="15"  maxlength="300"   value=""  > 
						</li>
						<li><span>每次下载次数：</span>
	 						<input type ="text" id="loopcount" name ="loopcount" maxlength="300"  size="15"  value=""  > 
						</li>
						<li><span>导出数据回调URL：</span>
	 						<input type ="text" id="exportSuccess_URL" name ="exportSuccess_URL"  maxlength="300"  value=""  > 
						</li>
						<li><span>每次回调订单数量：</span>
	 						<input type ="text" id="callBackCount" name ="callBackCount" maxlength="300"  size="15"  value=""  > 
						</li>
						<li><span>配送结果反馈URL：</span>
	 						<input type ="text" id="deliveryResult_URL" name ="deliveryResult_URL"  maxlength="300"  value=""  > 
						</li>
						<li><span>支付信息修改URL：</span>
	 						<input type ="text" id="updatePayResult_URL" name ="updatePayResult_URL"  maxlength="300"  value=""  > 
						</li>
						<li><span>跟踪日志反馈URL：</span>
	 						<input type ="text" id="trackLog_URL" name ="trackLog_URL"  maxlength="300"  value=""    > 
						</li>
						<li><span>私钥信息：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"  value=""  > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>药网customerid：</span>
	 						<input type ="text" id="ywcustomerid" name ="ywcustomerid"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>是否开启订单下载：</span>
	 						<input type="radio" name="isopenDataDownload" value="1" checked/> 开启
	 						<input type="radio" name="isopenDataDownload" value="0" /> 关闭
						</li>
						
						
						
						
						<li><span>开启药网URL：</span>
	 						<input type="radio" name="isopenywaddressflag" value="1" /> 开启
	 						<input type="radio" name="isopenywaddressflag" value="0" checked /> 关闭
						</li>
						<li><span>药网导出订单URL：</span>
	 						<input type ="text" id="ywexportCwb_URL" name ="ywexportCwb_URL" maxlength="300"   value=""   > 
						</li>
							<li><span>药网导出回调URL：</span>
	 						<input type ="text" id="ywexportSuccess_URL" name ="ywexportSuccess_URL"  maxlength="300"  value=""  > 
						</li>
						<li><span>药网配送反馈URL：</span>
	 						<input type ="text" id="ywdeliveryResult_URL" name ="ywdeliveryResult_URL"  maxlength="300"  value=""  > 
						</li>
						<li><span>药网跟踪反馈URL：</span>
	 						<input type ="text" id="ywtrackLog_URL" name ="ywtrackLog_URL"  maxlength="300"  value=""    > 
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
	 						<input type ="password" id="password" name ="password"  maxlength="30"    size="20"> 
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

