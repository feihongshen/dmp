<%@page import="cn.explink.b2c.tools.*"%>
<%@page import="cn.explink.pos.tools.*"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.b2c.tools.encodingSetting.*" %>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
List<Customer> customerlist=(List<Customer>)request.getAttribute("customerlist");
EncodingSetting expt=(EncodingSetting)request.getAttribute("encodingsetting");


%>

<body >

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>供货商编码设置修改</h1>
		<form id="expt_save_Form" name="expt_save_Form"  onSubmit="submitSaveForm(this);return false;" action="<%=request.getContextPath()%>/encodingsetting/save/<%=expt.getPoscodeid()%>" method="post">
		<div id="box_form">
				<ul>
					
					
					<li><span>供货商：</span>
					<select id="customerid" name="customerid" disabled>
					<option value="-1">请选择</option>
						<%if(customerlist!=null&&customerlist.size()>0){
							for(Customer cu:customerlist){%>
								<option value="<%=cu.getCustomerid()%>"    <%if(cu.getCustomerid()==expt.getCustomerid()){%>selected<%}%>><%=cu.getCustomername() %></option>
							<%}
						} %>
 						</select> 
					</li>
					<li><span>供方编码：</span>
 						<input type ="text" id="customercode" name ="customercode" value ="<%=expt.getCustomercode() %>" maxlength="25"  > 
					</li>
					
					<li><span>备注信息：</span>
					<input  name="remark" id="remark" size="35" value="<%=expt.getRemark()%>"/>
					</li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button"  /></div>
		<input type="hidden" name="joint_num" value="${joint_num}"/>
	</form>
	</div>
</div>
<div id="box_yy"></div>
</body>

