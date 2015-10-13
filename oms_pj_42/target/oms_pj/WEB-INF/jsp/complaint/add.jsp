<%@page import="cn.explink.enumutil.ComplaintTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<div id="close_box" onclick="closeBox()"></div><h1>创建投诉</h1>
		<div id="box_form">
			<form id="complaint_cre_Form" name="complaint_cre_Form" onSubmit="if(check_Complaint()){submitCreateForm(this);}return false;" action="<%=request.getContextPath()%>/complaint/create" method="post"  >
				<ul>
					<li><span>投诉类型  ：</span><select name ="complainttypeid" id="ctypeid">
									               <option value ="<%=ComplaintTypeEnum.Order.getValue()%>"><%=ComplaintTypeEnum.Order.getText() %></option>
									               <option value ="<%=ComplaintTypeEnum.Courier.getValue()%>"><%=ComplaintTypeEnum.Courier.getText()%></option>
									               <option value ="<%=ComplaintTypeEnum.Site.getValue()%>"><%=ComplaintTypeEnum.Site.getText()%></option>
									            </select></li>
					<li><span>订单号：</span><input type ="text" id ="ccwb" name ="complaintcwb" />*</li>
					<li><span>被投诉人编号：</span><input type ="text" id ="cuserid" name ="complaintuserid" onkeyup="checkNum('cuserid');"/></li>
					<li><span>投诉备注  ：</span><input type ="text" id ="cuserdesc" name ="complaintuserdesc" />*</li>
					<li><span>投诉人编号：</span><input type ="text" id ="ccustomerid" name ="complaintcustomerid" onkeyup="checkNum('ccustomerid');" /></li>
					<li><span>投诉人：</span><input type ="text" id ="ccontactman" name ="complaintcontactman" />*</li>
					<li><span>投诉人联系电话：</span><input type ="text" id ="cphone" name ="complaintphone" />*</li>
					<li><span>投诉内容：</span><input type ="text" id ="ccontent" style= "width:80;height:200 " name ="complaintcontent" />*</li>
				</ul>
				 <div align="center"><input type="submit" value="确认" class="button" /><br/><br/><font id="errorState" color="red"></font></div>
			</form>
		</div>
	</div>
</div>






