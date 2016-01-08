<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.dao.ReasonDao"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>

<%

List<Reason>  rslist = (List<Reason>)request.getAttribute("reasonList");

%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建常用语</h1>
		<form id="reason_cre_Form" name="reason_cre_Form" onSubmit="if(check_reason()){submitCreateForm(this);}return false;" action="<%=request.getContextPath()%>/reason/create" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>类型：</span> 
					<select name ="reasontype" id="reasontype" class="select1" onchange="whenhidden(),getReasonByFirstReason('<%=request.getContextPath()%>/reason/getfirstreason','parentid',0)">
				       <option value ="0">请选择</option>
		               <%for(ReasonTypeEnum ry : ReasonTypeEnum.values()){ %>
		               <option value ="<%=ry.getValue()%>"><%=ry.getText() %></option>
		               <%} %>
		           </select>*</li>
		           <div hidden="true" id="divs">
			           <li><span id=which style="margin-left: 50px" align="left">
				           <input type="radio" name="whichreason" id="radio1" checked="checked" onclick="to_change(1)" value='1'>一级
				           <input type="radio" id="radio2" name="whichreason"  onclick="to_change(2),getReasonByFirstReason('<%=request.getContextPath()%>/reason/getfirstreason','parentid',0)" value='2'>二级 
			           </span></li>
		           </div>
		            <div hidden="true" id="div_changealowflag">
			            <li><span id="changealowflag" style="margin-left: 50px;" align="left">
				           <input type="checkbox" id="checkbox1" name="changealowflag"  value='1'>中转是否要审核 
			           	   </span>
			           </li>
		           </div>
		           <div hidden="true" id="div_2" >
			           <li><span>一级原因：</span> 
						<select name ="parentid" id="parentid" >
					       <option value ="0">请选择</option>
			           </select>*</li>
		           </div>		
		           <div hidden="true" id="div_intercept">
			           <li><span id="interceptType" style="margin-left: 50px;width:180px;" align="left">
				           <input type="radio" id="intercept_3" name="interceptType"  checked="checked" value='3'>退货
				           <input type="radio" id="intercept_1" name="interceptType"   value='1'>丢失
				           <input type="radio" id="intercept_2" name="interceptType"   value='2'>破损 
			           </span></li>
		           </div>   
					<li><span>内容：</span><input type ="text" id="reasoncontent" name ="reasoncontent" maxlength="30" class="input_text1"></li>				
				 	
				</ul>
		</div>
		<div align="center"><input type="submit" value="确认" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>

