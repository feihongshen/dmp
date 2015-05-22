
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.pos.tools.*"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%

List<Reason> reasonlist =(List<Reason>)request.getAttribute("reasonList");
List<TransferReason> transreasonlist =(List<TransferReason>)request.getAttribute("transreasonList");

TransferResMatch transferResMatch =(TransferResMatch)request.getAttribute("transferResMatch");


%>


<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改中转原因匹配</h1>
		<form id="exptcode_Form" name="exptcode_Form"  onSubmit="if(check_transferResasonMatch()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/transferReason/save/" method="post">
		<div id="box_form">
				<ul>
					<li><span>固定中转原因：</span>
						<select name ="transferReasonid" id="transferReasonid" >
				               <option value ="-1">请选择</option>
				              <%for(TransferReason ts:transreasonlist){
				             %>
				             <option value="<%=ts.getId()%>" <%if(transferResMatch.getTransferReasonid()==ts.getId()){%>selected<%}%>><%=ts.getReasonname()%></option>
				              <%}%>
				              
				           	   
		          		 </select>
					</li>
					
					<li><span>常用语中转原因：</span>
					<select id="reasonid" name="reasonid">
						<option value="-1">请选择</option>
						 <%for(Reason rs:reasonlist){
				             %>
				             <option value="<%=rs.getReasonid()%>"  <%if(transferResMatch.getReasonid()==rs.getReasonid()){%>selected<%}%>><%=rs.getReasoncontent()%></option>
				              <%}%>
 					</select> 
					</li>
					
					<li><span>备注信息：</span>
					<input  name="remark" id="remark" size="35"/>
					</li>
				</ul>
		</div>
		<div id="box_top_bg"></div>
		<div align="center"><input type="submit" value="保存" class="button"  /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>

