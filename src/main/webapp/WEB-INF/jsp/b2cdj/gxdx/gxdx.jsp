<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.b2c.gxdx.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
GxDx gxdx = (GxDx)request.getAttribute("gxdx");
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
		<h1><div id="close_box" onclick="closeBox()"></div>广信电信匹配设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="submitSaveForm(this);return false;" action="<%=request.getContextPath()%>/gxdxAddress/save/${joint_num}" method="post">
		<div id="box_form">
			<ul>
				<c:choose>
					<c:when test="${gxdx != null}">  
							<li><span>物流公司编号：</span>
		 						<input type ="text" id="logisticProviderID" name ="logisticProviderID"  maxlength="300"  value="${gxdx.logisticProviderID}"> 
							</li>
							<li><span>密钥信息：</span>
		 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"  value="${gxdx.private_key}"  > 
							</li>
							<li><span>请求URL：</span>
		 						<input type ="text" id="requestUrl" name ="requestUrl"  maxlength="300"  value="${gxdx.requestUrl}"  > 
							</li>
							<li><span>最大查询数：</span>
		 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="${gxdx.maxCount}"  > 
							</li>
							<li><span>客户id：</span>
		 						<input type ="text" id="customerid" name ="customerid"  maxlength="30"  value="${gxdx.customerid}" size="20" > 
							</li>
							<li><span>订单导入库房：</span>
								<select name="exportbranchid">
									<option value="0">请选择库房</option>
									<%for(Branch b:warehouselist){
									%>
										<option value="<%=b.getBranchid()%>" <%if(gxdx.getExportbranchid()==b.getBranchid()){%>selected<%}%> ><%=b.getBranchname() %></option>
									<%}%>
								</select>
							</li>
							<li><span>密码：</span>
		 						<input type ="password" id="password" name ="password"  maxlength="30"  value="${password}" size="20" > 
							</li>
						</c:when>
						<c:otherwise>
							<li><span>物流公司编号：</span>
		 						<input type ="text" id="logisticProviderID" name ="logisticProviderID"  maxlength="300"  value=""  > 
							</li>
							<li><span>密钥信息：</span>
		 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"  value=""  > 
							</li>
							<li><span>请求URL：</span>
		 						<input type ="text" id="requestUrl" name ="requestUrl"  maxlength="300"  value=""  > 
							</li>
							<li><span>最大查询数：</span>
		 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value=""  > 
							</li>
							<li><span>客户id：</span>
		 						<input type ="text" id="customerid" name ="customerid"  maxlength="30"  value=""  > 
							</li>
							<li><span>订单导入库房：</span>
								<select name="exportbranchid">
									<option value="0">请选择库房</option>
									<%for(Branch b:warehouselist){%>
										<option value="<%=b.getBranchid()%>" ><%=b.getBranchname() %></option>
									<%}%>
								</select>
							</li>
							<li><span>密码：</span>
		 						<input type ="password" id="password" name ="password"  maxlength="30"  value=""  > 
							</li>
						</c:otherwise>
				</c:choose>
			</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button"  /></div>
		<input type="hidden" name="joint_num" value="${joint_num}"/>
	</form>
	</div>
</div>
<div id="box_yy"></div>

