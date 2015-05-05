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
		<h1><div id="close_box" onclick="closeBox()"></div>供货商编码设置映射</h1>
		<form id="expt_save_Form" name="expt_save_Form" onSubmit="if(check_exptreasonn()){submitSaveForm(this);}return false;"   action="<%=request.getContextPath()%>/encodingsetting/create/" method="post">
		<div id="box_form">
				<ul>
					<li><span>供货商：</span>
					<select id="customerid" name="customerid">
					<option value="-1" >请选择</option>
						<%if(customerlist!=null&&customerlist.size()>0){
							for(Customer cu:customerlist){%>
								<option id="asdf" value="<%=cu.getCustomerid()%>"><%=cu.getCustomername() %></option>
							<%}
						} %>
 						</select> 
					</li>
					
					<li><span>供方编码：</span>
 						<input type ="text" id="customercode" name ="customercode" value ="" maxlength="25"  > 
					</li>
					
					<li><span>备注信息：</span>
					<input  name="remark" id="remark" size="35"/>
					</li>
				</ul>
		</div>
		<div align="center" id="add_feikong_yanzheng"><input type="submit" value="保存" class="button"  /></div>
		
		<input type="hidden" name="joint_num" value="${joint_num}"/>
		
		
	</form>
	</div>
</div>
<div id="box_yy"></div>

