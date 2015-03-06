<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.enumutil.PunishlevelEnum"%>
<%@page import="cn.explink.enumutil.PunishtimeEnum"%>
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
List<Customer> customers = (List<Customer>)request.getAttribute("customers");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>扣罚信息修改</h1> <!-- onSubmit="return check();" -->
		<form id="punish_cre_Form" name="punish_cre_Form"  action="<%=request.getContextPath()%>/punish/update" method="post"  >
		<div id="box_form">
		<input type="hidden" name="id" id="id" value="<%=punish.getId()%>"/>
		<table>
		<tr>
		<td>
		订单号:
		</td>
		<td>
		<input type="text" name="cwb" readonly="readonly" id="cwb" value="<%=punish.getCwb() %>" style="width: 133px"/>
		</td>
		<td>
		供货商:
		</td>
		<td>
		<select  name="customerid" id="customerid" style="width: 140px">
		<option value="0">请选择</option>
		<%for(Customer cu:customers){ %>
		<option value="<%=cu.getCustomerid() %>" <%if(punish.getCustomerid()==cu.getCustomerid()) {%>selected="selected" <%} %> ><%=cu.getCustomername() %></option>
		<%} %>
		</select>
		</td>
		</tr>
		<tr>
		<td>扣罚类型:
		</td>
		<td>
		<select id="punishid" name="punishid" style="width: 140px">
		<option value="0">请选择</option>
		<%for(PunishType p:punishTypeList){ %>
		<option value="<%=p.getId() %>"<%if(punish.getPunishid()==p.getId()) {%>selected="selected" <%} %>><%=p.getName() %></option>
		<%} %>
		</select>
		</td>
		<td>责任部门:
		</td>
		<td>
		<select id="branchid" name="branchid" style="width: 140px" onchange="selectBranch($(this).val())">
		<option value="0">请选择</option>
		<%for(Branch b:branchlist){ %>
		<option value="<%=b.getBranchid()%>" <%if(b.getBranchid()==punish.getBranchid()){ %> selected="selected"<%} %>><%=b.getBranchname() %></option>
		<%} %>
		</select>
		</td>
		<td>
		责任人:
		</td>
		<td>
		<select id="userid" name="userid" style="width: 140px" onchange="selectUser($(this).val())">
		<option value="0">请选择</option>
		<%for(User u:userList){ %>
		<option value="<%=u.getUserid()%>" <%if(u.getUserid()==punish.getUserid()){ %> selected="selected"<%} %>><%=u.getRealname() %></option>
		<%} %>
		</select>
		</td>
		</tr>
		<tr>
		<td>
			扣罚时效:
			</td>
		<td>
			<select id="punishtime" style="width: 140px" name="punishtime" >
		<option value="0">请选择</option>
		<%for (PunishtimeEnum pEnum : PunishtimeEnum.values()) {%>
			<option value="<%=pEnum.getValue()%>" <%if(pEnum.getValue()==punish.getPunishtime()){ %> selected="selected"<%} %>><%=pEnum.getText() %></option>
		<%}%>
		</select>
		</td>
		<td>
		优先级别:
		</td>
		<td>
		<select id="punishlevel" style="width: 140px" name="punishlevel">
		<option value="0">请选择</option>
		<%for (PunishlevelEnum pEnum : PunishlevelEnum.values()) {%>
			<option value="<%=pEnum.getValue()%>" <%if(pEnum.getValue()==punish.getPunishlevel()){ %> selected="selected"<%} %>><%=pEnum.getText() %></option>
		<%}%>
		</select>
		</td>
		<td>
		扣罚金额:
		</td>
		<td>
		<input type="text" name="punishfee"  id="punishfee" value="<%=punish.getPunishfee() %>" style="width: 133px"/>
		</td>
		</tr>
		<tr>
		<td valign="top">
		扣罚内容:
		</td>
		<td colspan="3"><textarea style="width: 100%;height: 50px;" id="punishcontent" name="punishcontent"><%=punish.getPunishcontent() %></textarea>
		</td>
		<td valign="top">
		实扣金额:
		</td>
		<td valign="top">
		<input type="text" name="realfee" id="realfee" value="<%=punish.getRealfee() %>" style="width: 133px"/>
		</td>
		</tr>
		</table>
		 <div align="center">
		 <input type="button" value="确认" class="button" onclick="update()"/>
		 <input type="button" value="取消" class="button" onclick="closeBox()" /></div>
		 </div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
<input type="hidden" id="dmpurl" value="<%=request.getContextPath()%>" />
