<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.b2c.tps.ThirdPartyOrder2DOCfg"%>

<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<%
ThirdPartyOrder2DOCfg thirdPartyOrder2DO = (ThirdPartyOrder2DOCfg)request.getAttribute("thirdPartyOrder2DO");
List<Long> customeridList = (List<Long>)request.getAttribute("customeridList");
List<Customer> customerList = request.getAttribute("customerList") == null ? new ArrayList<Customer>() : (List<Customer>)request.getAttribute("customerList");
%>
<script type="text/javascript">
	/* $(function() {
		$("#customerids").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择外单客户' });
	}); */
</script>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>外单推送DO服务对接设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/thirdPartyOrder2DO/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(thirdPartyOrder2DO != null){ %>
						<li><span>承运商编码：</span>
							<input type ="text" id="carrierCode" name ="carrierCode" value="<%=thirdPartyOrder2DO.getCarrierCode()%>" maxlength="32">*
						</li>
						<li><span>是否开启对接：</span>
							<input type ="radio" id="openFlag1" name ="openFlag" value="1"  <%if(thirdPartyOrder2DO.getOpenFlag()==1){%>checked<%}%> >开启
							<input type ="radio" id="openFlag2" name ="openFlag" value="0"  <%if(thirdPartyOrder2DO.getOpenFlag()==0){%>checked<%}%> >关闭
						</li>
						<li><span>最大尝试推送次数：</span>
							<input type ="text" id="maxTryTime" name ="maxTryTime" value="<%=thirdPartyOrder2DO.getMaxTryTime()%>" maxlength="2" onblur="validate('maxTryTime')">*
						</li>
						<li><span>外单客户id：</span>
							<input type ="text" id="customerids" name ="customerids" value="<%=thirdPartyOrder2DO.getCustomerids()%>" maxlength="500">*多个客户id之间以逗号隔开
							<%-- <select name ="customerids" id ="customerids" multiple="multiple" style="width: 300px;">
					          <%for(Customer c : customerList){ %>
					           <option value ="<%=c.getCustomerid() %>" 
					           <%if(!customeridList.isEmpty()){ 
					        	   if(customeridList.contains(c.getCustomerid())){
					        			%>selected="selected"<%
					        	   }
							     }%> ><%=c.getCustomername()%></option>
					          <%} %>
						    </select>
						    [<a href="javascript:multiSelectAll('customerids',1,'请选择');">全选</a>]
							[<a href="javascript:multiSelectAll('customerids',0,'请选择');">取消全选</a>] --%>
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" >* 
						</li>
					
					<%}else{ %>
						<li><span>承运商编码：</span>
							<input type ="text" id="carrierCode" name ="carrierCode" maxlength="32">*
						</li>
						<li><span>是否开启对接：</span>
							<input type ="radio" id="openFlag1" name ="openFlag" value="1">开启
							<input type ="radio" id="openFlag2" name ="openFlag" value="0" checked>关闭
						</li>
						<li><span>最大尝试推送次数：</span>
							<input type ="text" id="maxTryTime" name ="maxTryTime"  maxlength="2" onblur="validate('maxTryTime')">*
						</li>
						<li><span>外单客户id：</span>
							<input type ="text" id="customerids" name ="customerids"  maxlength="500">*多个客户id之间以逗号隔开
						
							<%-- <select name ="customerids" id ="customerids" multiple="multiple" style="width: 300px;">
					          <%for(Customer c : customerList){ %>
					           <option value ="<%=c.getCustomerid() %>" 
					           <%if(!customeridList.isEmpty()){ 
					        	   if(customeridList.contains(c.getCustomerid())){
					        			%>selected="selected"<%
					        	   }
							     }%> ><%=c.getCustomername()%></option>
					          <%} %>
						    </select>
						    [<a href="javascript:multiSelectAll('customerids',1,'请选择');">全选</a>]
							[<a href="javascript:multiSelectAll('customerids',0,'请选择');">取消全选</a>] --%>
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" >* 
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

