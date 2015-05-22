
<%@page import="cn.explink.b2c.tools.CommonJoint"%>
<%@page import="cn.explink.b2c.tools.commonExptService"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.pos.tools.*"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
List<Common> customerlist =(List<Common>)request.getAttribute("customerlist");

%>


<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>新增（承运商）异常码原因配对</h1>
		<form id="exptcode_Form" name="exptcode_Form" onSubmit="if(check_exptreason(<%=PosEnum.AliPay.getKey()%>)){submitSaveForm(this);}return false;"  action="<%=request.getContextPath()%>/Commreason/createJoint" method="post">
		<div id="box_form">
				<ul>
					<li><span>请选择异常类型：</span>
						<select name ="expt_type" id="expt_type" onchange="updateExptReasonByType('<%=request.getContextPath()%>/exptcodejoint/reasonByType',this.value),updateExptReasonSupportByCode('<%=request.getContextPath()%>/Commreason/searchExptReasonById',support_key.value,this.value)">
				               <option value ="-1">全部</option>
				               <option value="<%=ReasonTypeEnum.BeHelpUp.getValue()%>"><%=ReasonTypeEnum.BeHelpUp.getText()%></option>
				           	   <option value="<%=ReasonTypeEnum.ReturnGoods.getValue()%>"><%=ReasonTypeEnum.ReturnGoods.getText()%></option>
				           	   <option value="<%=ReasonTypeEnum.WeiShuaKa.getValue()%>"><%=ReasonTypeEnum.WeiShuaKa.getText()%></option>
				           	   <option value="<%=ReasonTypeEnum.DiuShi.getValue()%>"><%=ReasonTypeEnum.DiuShi.getText()%></option>
		          		 </select>
					</li>
					
					<li><span>承运商异常原因：</span>
					<select id="reasonid" name="reasonid">
						<option value="-1">请选择异常原因</option>
 					</select> 
					</li>
					<li ><span>选择承运商：</span>
 						<select name ="support_key" id="support_key" onchange="updateExptReasonSupportByCode('<%=request.getContextPath()%>/Commreason/searchExptReasonById',this.value,expt_type.value)">
			               <%for(Common en : customerlist){ %>
			               <option value ="<%=en.getId()%>" ><%=en.getCommonname() %></option>
			               <%} %>
		          		 </select>
					</li>
					<li><span>异常码/描述：</span>
 						<select name="exptsupportreason" id="exptsupportreason">
 							<option>请选择提供方异常码/描述</option>
 						</select>
					</li>
				</ul>
		</div>
		<div id="box_top_bg"></div>
		<div align="center"><input type="submit" value="保存" class="button"  /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>

