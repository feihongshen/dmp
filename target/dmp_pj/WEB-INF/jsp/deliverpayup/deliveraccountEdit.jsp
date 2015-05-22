<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	User u = request.getAttribute("user")==null?null:(User)request.getAttribute("user");
	List<Branch> bList = request.getAttribute("bList")==null?null:(List<Branch>)request.getAttribute("bList");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>小件员调账</h1>
		<form id="deliveraccountEdit_Form" name="deliveraccountEdit_Form" 
			 onSubmit="if(check_deliveraccountEdit()){submitDeliveraccountEdit(this);}return false;" 
			 action="<%=request.getContextPath()%>/deliverpayup/saveDeliveraccountEdit/<%=u.getUserid() %>;jsessionid=<%=session.getId()%>" method="post"  >
		<div id="box_form">
			<ul>
           		<li><span>站点：</span><%for(Branch b : bList){if(b.getBranchid()==u.getBranchid()){out.print(b.getBranchname());}} %></li>
           		<li><span>姓名：</span><%=u.getRealname() %></li>
           		<li><span>现金余额：</span><%=u.getDeliverAccount() %>元</li>
		        <li><span>POS余额：</span><%=u.getDeliverPosAccount() %>元</li>
		        <li><span>说明：</span>输入负数为扣减，输入正数为加款！</li>
			    <li><span>调整现金：</span><input type="text" name="deliverpayupamount"  id="deliverpayupamount" size="12"/>元</li>
		        <li><span>调整POS：</span><input type="text" name="deliverpayupamount_pos" id="deliverpayupamount_pos" size="12"/>元</li>
			    <li><span>备注：</span><input type="text" name="remark" id="remark" maxlength="499"/></li>
	         </ul>
	         
			
		</div>
		<div align="center"><input type="submit" value="确认" class="button" id="sub" /></div>
		</form>	
	</div>
</div>

<div id="box_yy"></div>

