<%@page import="cn.explink.b2c.tools.*"%>
<%@page import="cn.explink.pos.tools.*"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
List<Common> customerlist=(List<Common>)request.getAttribute("customerlist");
CommonReason expt=(CommonReason)request.getAttribute("exptreason");


%>

<body >

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>承运商异常码修改</h1>
		<form id="expt_save_Form" name="expt_save_Form" onSubmit="if(check_exptreason(<%=PosEnum.AliPay.getKey()%>)){submitSaveForm(this);}return false;"  action="<%=request.getContextPath()%>/Commreason/save/<%=expt.getId() %>" method="post">
		<div id="box_form">
				<ul>
					<li><span>异常码提供方：</span>
						<select name ="support_key" id="support_key"  disabled="disabled">
			               <option value ="-1">请选择</option>
			               <%for(Common en : customerlist){ %>
		              		 <option value ="<%=en.getId()%>"  <%if(en.getId()==expt.getCommonid()){%>selected<%} %>><%=en.getCommonname() %></option>
		              		 <%} %>
			               
			            </select>
			            
					</li>
					<li><span>供方编码：</span>
 						<input type ="text" id="customercode" name ="customercode" value ="<%=expt.getCustomercode() %>" maxlength="25"  > 
					</li>
					
					<li ><span>异常类型：</span>
 						<select name="expt_type" id="expt_type">
 							<option value="<%=ReasonTypeEnum.BeHelpUp.getValue()%>" <%if(ReasonTypeEnum.BeHelpUp.getValue()==expt.getExpt_type()){%>selected<%}%>><%=ReasonTypeEnum.BeHelpUp.getText() %></option>
 							<option value="<%=ReasonTypeEnum.ReturnGoods.getValue()%>"  <%if(ReasonTypeEnum.ReturnGoods.getValue()==expt.getExpt_type()){%>selected<%}%>><%=ReasonTypeEnum.ReturnGoods.getText() %></option>
 							<option value="<%=ReasonTypeEnum.WeiShuaKa.getValue()%>"  <%if(ReasonTypeEnum.WeiShuaKa.getValue()==expt.getExpt_type()){%>selected<%}%>><%=ReasonTypeEnum.WeiShuaKa.getText() %></option>
 							<option value="<%=ReasonTypeEnum.DiuShi.getValue()%>"  <%if(ReasonTypeEnum.DiuShi.getValue()==expt.getExpt_type()){%>selected<%}%>><%=ReasonTypeEnum.DiuShi.getText() %></option>
 						</select>
					</li>
					<li><span>异常码：</span>
 						<input type ="text" id="expt_code" name ="expt_code" value ="<%=expt.getExpt_code() %>" maxlength="25"  > 
					</li>
					<li><span>异常码说明：</span>
 						<input type ="text" id="expt_msg" name ="expt_msg" value ="<%=expt.getExpt_msg() %>" maxlength="30"  > 
					</li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button"  /></div>
		<input type="hidden" name="support_key" value="<%=expt.getId()%>"/>
	</form>
	</div>
</div>
<div id="box_yy"></div>
</body>

