<%@page import="cn.explink.b2c.tools.*"%>
<%@page import="cn.explink.pos.tools.*"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.*"%>

<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
List<Customer> customerlist=(List<Customer>)request.getAttribute("customerlist");
%>


<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>异常码设置</h1>
		<form id="expt_save_Form" name="expt_save_Form"  onSubmit="if(check_exptreason(<%=PosEnum.AliPay.getKey()%>)){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/exptreason/create/" method="post">
		<div id="box_form">
				<ul>
					<li><span>异常码提供方：</span>
						<select name ="support_key" id="support_key" onchange="changethisB2cFlag(this.value,<%=PosEnum.AliPay.getKey()%>)">
			               <option value ="-1">请选择</option>
			                 <option value ="1_-2">通用</option>
			               <%for(Customer en : customerlist){ %>
		              		 <option value ="1_<%=en.getCustomerid()%>" ><%=en.getCustomername() %></option>
		              		 <%} %>
			               <%for(PosEnum p : PosEnum.values()){ %>
			               	<option value ="<%=p.getKey()%>"><%=p.getText() %></option>
			               <%} %>
			            </select>
					</li>
					<li><span>供方编码：</span>
 						<input type ="text" id="customercode" name ="customercode" value ="" maxlength="25"  > 
					</li>
					<li style="display:none" id="b2cflag"><span>设置针对B2C编码：</span>
					<select id="customerid" name="customerid">
					<option value="-1">请选择</option>
						<%if(customerlist!=null&&customerlist.size()>0){
							for(Customer cu:customerlist){%>
								<option value="<%=cu.getCustomerid()%>"><%=cu.getCustomername() %></option>
							<%}
						} %>
 						</select> <font color="red">*目前只支持支付宝</font>
					</li>
					<li ><span>异常类型：</span>
 						<select name="expt_type" id="expt_type">
 							<option value="<%=ReasonTypeEnum.BeHelpUp.getValue()%>"><%=ReasonTypeEnum.BeHelpUp.getText() %></option>
 							<option value="<%=ReasonTypeEnum.ReturnGoods.getValue()%>"><%=ReasonTypeEnum.ReturnGoods.getText() %></option>
 							<option value="<%=ReasonTypeEnum.WeiShuaKa.getValue()%>"><%=ReasonTypeEnum.WeiShuaKa.getText() %></option>
 							<option value="<%=ReasonTypeEnum.DiuShi.getValue()%>"><%=ReasonTypeEnum.DiuShi.getText() %></option>
 						</select>
					</li>
					<li><span>异常码：</span>
 						<input type ="text" id="expt_code" name ="expt_code" value ="" maxlength="25"  > 
					</li>
					
					<li><span>异常码说明：</span>
 						<input type ="text" id="expt_msg" name ="expt_msg" value ="" maxlength="30"  > 
					</li>
					<li><span>备注信息：</span>
					<input  name="expt_remark" id="expt_remark" size="35"/>
					</li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button"  /></div>
		<input type="hidden" name="joint_num" value="${joint_num}"/>
	</form>
	</div>
</div>
<div id="box_yy"></div>

