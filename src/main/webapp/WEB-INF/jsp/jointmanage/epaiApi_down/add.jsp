<%@page import="cn.explink.b2c.tools.*"%>
<%@page import="cn.explink.pos.tools.*"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.b2c.explink.core_down.*" %>

<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
List<Customer> customerlist=(List<Customer>)request.getAttribute("customerlist");
List<Branch> warehouselist=(List<Branch>)request.getAttribute("warehouselist");
%>


<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>e派系统对接设置</h1>
		<form id="expt_save_Form" name="expt_save_Form"  onSubmit="if(check_epaiApi(<%=PosEnum.AliPay.getKey()%>)){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/epaiApi/create/" method="post">
		<div id="box_form">
				<ul>
					<li><span>用户编码(标识)：</span>
						<input type ="text" id="userCode" name ="userCode" value ="" size="15"  > <font color="red">*上游电商提供</font>
					</li>
					<li><span>上游电商：</span>
					<select id="customerid" name="customerid">
					<option value="-1">请选择</option>
						<%if(customerlist!=null&&customerlist.size()>0){
							for(Customer cu:customerlist){%>
								<option value="<%=cu.getCustomerid()%>"><%=cu.getCustomername() %></option>
							<%}
						} %>
 						</select> 
					</li>
					
					<li><span>获取订单URL：</span>
 						<input type ="text" id="getOrder_url" name ="getOrder_url" value ="" size="45" > 
					</li>
					<li><span>每次获取数量：</span>
 						<input type ="text" id="pageSize" name ="pageSize" value ="" size="15"  > 
					</li>
					<li><span>下载成功回传URL：</span>
 						<input type ="text" id="callBack_url" name ="callBack_url" value ="" size="45" > 
					</li>
					<li><span>状态回传URL：</span>
 						<input type ="text" id="feedback_url" name ="feedback_url" value =""  size="45" > 
					</li>
					<li><span>密钥信息：</span>
 						<input type ="text" id="private_key" name ="private_key" value =""  > 
					</li>
					<li><span>订单导入库房：</span>
					<select id="warehouseid" name="warehouseid">
					<option value="-1">请选择</option>
						<%
							for(Branch branch:warehouselist){%>
								<option value="<%=branch.getBranchid()%>"><%=branch.getBranchname() %></option>
							<%} %>
 						</select> 
					</li>
					<li><span>订单获取模式：</span>
	 						<input type ="radio" id="isPassiveReception1" name ="isPassiveReception" checked value="1"> 主动获取
	 						<input type ="radio" id="isPassiveReception2" name ="isPassiveReception"  value="2"> 被动接收
					</li>
					<li><span>是否开启（下载）：</span>
	 						<input type ="radio" id="isopenflag1" name ="isopenflag" checked value="1"> 开启
	 						<input type ="radio" id="isopenflag2" name ="isopenflag"  value="0"> 关闭
					</li>
					
					<li><span>是否开启（反馈）：</span>
	 						<input type ="radio" id="isfeedbackflag1" name ="isfeedbackflag" checked value="1"> 开启
	 						<input type ="radio" id="isfeedbackflag2" name ="isfeedbackflag"  value="0"> 关闭
					</li>
					<li><span>传输方式：</span>
	 						<input type ="radio" id="ispostflag1" name ="ispostflag" checked value="0"> 数据流
	 						<input type ="radio" id="ispostflag2" name ="ispostflag"  value="1"> POST参数
					</li>
					
					<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
					</li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button"  /></div>
		
	</form>
	</div>
</div>
<div id="box_yy"></div>

