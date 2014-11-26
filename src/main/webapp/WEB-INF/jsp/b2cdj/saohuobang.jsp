<%@page import="cn.explink.b2c.tools.B2cEnum"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="java.util.List"%>
<%@page import="cn.explink.b2c.saohuobang.Saohuobang"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%
    Saohuobang shb = (Saohuobang)request.getAttribute("SaohuobangObject");
    List<Branch> warehouselist=(List<Branch>)request.getAttribute("warehouselist");
    int key=(Integer)request.getAttribute("joint_num");
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>
		<%if(key==B2cEnum.saohuobang.getKey()){ %>
		扫货帮接口设置
		<%} else{%>美甲接口设置
		<%} %>
		</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;"  action="<%=request.getContextPath()%>/S_huobang/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(shb != null){ %>
						<li><span>客户编号：</span>
							<input type ="text" id="clientID" name ="clientID" value="<%=shb.getClientID()%>"  maxlength="300">
						</li>
						<li><span>ProviderID：</span>
	 						<input type ="text" id="logisticProviderID" name ="logisticProviderID" maxlength="300"   value="<%=shb.getProviderID()%>"   > 
						</li>
						<li><span>key_ID：</span>
	 						<input type ="text" id="key" name ="key" maxlength="300"   value="<%=shb.getKey()%>"   > 
						</li>
						<li><span>物流公司标识：</span>
	 						<input type ="text" id="customerid" name ="customerid" maxlength="300"   value="<%=shb.getCustomerId()%>"   > 
						</li>
						
						<li><span>订单下载接口URL：</span>
	 						<input type ="text" id="trackLog_URL" name ="trackLog_URL"  maxlength="300"  value="<%=shb.getTrackLog_URL()%>"    > 
						</li>
						<li><span>每次回调订单数量：</span>
	 						<input type ="text" id="callbackcount" name ="callbackcount" maxlength="300"  size="15"  value="<%=shb.getCallBackCount()%>"  > 
						</li>
						
						<li><span>是否开启订单下载：</span>
	 						<input type="radio" name="isopenDataDownload" value="1" checked/> 开启
	 						<input type="radio" name="isopenDataDownload" value="0" /> 关闭
						</li>
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(shb.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li><span>客户编号：</span>
							<input type ="text" id="clientID" name ="clientID" value=""  maxlength="300">
						</li>
						<li><span>ProviderID：</span>
	 						<input type ="text" id="logisticProviderID" name ="logisticProviderID" maxlength="300"   value=""   > 
						</li>
						<li><span>key_ID：</span>
	 						<input type ="text" id="key" name ="key" maxlength="300"   value=""   > 
						</li>
						<li><span>物流公司标识：</span>
	 						<input type ="text" id="customerid" name ="customerid" maxlength="300"   value=""   > 
						</li>
						
						<li><span>订单下载接口URL：</span>
	 						<input type ="text" id="trackLog_URL" name ="trackLog_URL"  maxlength="300"  value=""    > 
						</li>
						<li><span>每次回调订单数量：</span>
	 						<input type ="text" id="callbackcount" name ="callbackcount" maxlength="300"  size="15"  value=""  > 
						</li>
						
						<li><span>是否开启订单下载：</span>
	 						<input type="radio" name="isopenDataDownload" value="1" checked/> 开启
	 						<input type="radio" name="isopenDataDownload" value="0" /> 关闭
						</li>
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>"><%=b.getBranchname() %></option>
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
</html>