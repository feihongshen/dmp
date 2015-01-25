<%@page import="cn.explink.enumutil.PunishtimeEnum"%>
<%@page import="cn.explink.enumutil.PunishlevelEnum"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.Punish"%>
<%@page import="cn.explink.domain.PunishType"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
List<User> userList = (List<User>)request.getAttribute("userList");
List<PunishType> punishTypeList = (List<PunishType>)request.getAttribute("punishTypeList");
Punish punish = (Punish)request.getAttribute("punish");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>扣罚信息修改</h1> <!-- onSubmit="return check();" -->
		<form id="punish_cre_Form"  name="punish_cre_Form"  action="<%=request.getContextPath()%>/punish/updateState" method="post"  >
		<div id="box_form">
		<input type="hidden" readonly="readonly" name="id" id="id" value="<%=punish.getId()%>"/>
		<table>
		<tr>
		<td>
		订单号:
		</td>
		<td>
		<input type="text" name="cwb" readonly="readonly" id="cwb" value="<%=punish.getCwb() %>" style="width: 133px"/>
		</td>
		</tr>
		<tr>
		<td>扣罚类型:
		</td>
		<td>
		<%for(PunishType p:punishTypeList){
			if(punish.getPunishid()==p.getId()) {%>
		<input type="text" name="punishid" readonly="readonly" id="punishid" value="<%=p.getName() %>" style="width: 133px"/>
		<%} }%>
		</td>
		<td>扣罚站点:
		</td>
		<td>
		<%for(Branch b:branchlist){ 
			if(b.getBranchid()==punish.getBranchid()){ %>
		<input type="text" name="branchid" readonly="readonly" id="branchid" value="<%=b.getBranchname() %>" style="width: 133px"/>
		<%}} %>
		</td>
		<td>
		扣罚人员:
		</td>
		<td>
		<%for(User u:userList){
			if(u.getUserid()==punish.getUserid()){ %>
			<input type="text" name="userid" readonly="readonly" id="userid" value="<%=u.getRealname()%>" style="width: 133px"/>
				<%}} %>
		</td>
		</tr>
		<tr>
		<td>
			扣罚时效:
			</td>
		<td>
		<input type="text" name="punishtime" readonly="readonly" id="punishtime"  value="<%=PunishtimeEnum.getText(punish.getPunishtime()).getText() %>" style="width: 133px"/>
		</td>
		<td>
		优先级别:
		</td>
		<td>
			<input type="text" name="punishlevel" readonly="readonly" id="punishlevel" value="<%=PunishlevelEnum.getText(punish.getPunishlevel()).getText() %>" style="width: 133px"/>
		</td>
		<td>
		扣罚金额:
		</td>
		<td>
		<input type="text" readonly="readonly" name="punishfee"  id="punishfee" value="<%=punish.getPunishfee() %>" style="width: 133px"/>
		</td>
		</tr>
		<tr>
		<td valign="top">
		扣罚内容:
		</td>
		<td colspan="3"><textarea style="width: 100%;height: 50px;" id="punishcontent" readonly="readonly" name="punishcontent"><%=punish.getPunishcontent() %></textarea>
		</td>
		<td valign="top">
		实扣金额:
		</td>
		<td valign="top">
		<input type="text" readonly="readonly" name="realfee" id="realfee" value="<%=punish.getRealfee() %>" style="width: 133px"/>
		</td>
		</tr>
		</table>
		<input name="state" id="state" type="hidden" value="<%=punish.getState()%>"/>
		 <div align="center">
		 <input type="button" value="<%=punish.getState()==1?"取消审核":"确认审核" %>" class="button" onclick="updateState()"/>
		 <input type="button" value="取消" class="button" onclick="closeBox()" /></div>
		 </div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
<input type="hidden" id="dmpurl" value="<%=request.getContextPath()%>" />
