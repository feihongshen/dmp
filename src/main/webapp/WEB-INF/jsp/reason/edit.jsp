<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.domain.Reason"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
Reason reason = (Reason)request.getAttribute("reason");
%>

<script type="text/javascript">

	 var initReasontype = "<%=reason.getReasontype() %>,reasontype";

</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()" ></div>修改常用语</h1>
		<form id="reason_save_Form" name="reason_save_Form"  onSubmit="if(check_reason()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/reason/save/${reason.reasonid}" method="post">
		<div id="box_form">
				<ul>
					<li><span>类型：</span> 
					<%-- <select name ="reasontype" id="reasontype" class="select1" onclick="editoffenword()">
				       <option value ="0">请选择</option> --%>
		             <%for(ReasonTypeEnum ry : ReasonTypeEnum.values()){ %>
		               		<%if( ry.getValue()==reason.getReasontype()){%>
		               		<label><%=ry.getText() %></label>
		               <%}
		               		}%> </li>
		          
			     	     <%if(reason.getReasontype()==1&&reason.getWhichreason()==1){ %>        	
				       <p id="selectapply" style="margin-left: 70px;" align="left"> 			      
				       <input type="checkbox" id="checkbox1" name="changealowflag"  value="1" />中转是否要申请
				       </p>	           	   
			   			<%} %>
		           
					<li><span>内容：</span><input type ="text" id="reasoncontent" name ="reasoncontent" value ="<%=reason.getReasoncontent() %><%-- ${reason.reasoncontent} --%>" maxlength="30" class="input_text1"></li>
		
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button"/></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>

