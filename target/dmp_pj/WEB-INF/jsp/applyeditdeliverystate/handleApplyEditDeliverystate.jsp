<%@page import="cn.explink.controller.DeliveryStateView"%>
<%@page import=" cn.explink.domain.Reason"%>
<%@page import="cn.explink.domain.ApplyEditDeliverystate"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.domain.AbnormalType"%>
<%@page import="cn.explink.domain.AbnormalOrder"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="cn.explink.dao.CwbDAO"%>

<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
ApplyEditDeliverystate applyEditDeliverystate = (ApplyEditDeliverystate)request.getAttribute("applyEditDeliverystate");
List<User> userList = (List<User>)request.getAttribute("userList");

DeliveryStateView deliverystate = (DeliveryStateView)request.getAttribute("deliverystate");
List<Reason> backlist = (List<Reason>)request.getAttribute("backreasonlist");

List<Reason> leavedlist = (List<Reason>)request.getAttribute("leavedreasonlist");

List<Reason> podremarkreasonlist = (List<Reason>)request.getAttribute("podremarkreasonlist");

CwbOrder cwborder = (CwbOrder)request.getAttribute("cwborder");

long backreasonid = request.getAttribute("backreasonid")==null?-1:(Long)request.getAttribute("backreasonid"); 
long leavedreasonid = request.getAttribute("leavedreasonid")==null?-1:(Long)request.getAttribute("leavedreasonid");
  
%>
<script type="text/javascript">

var initEditArray = new Array();
initEditArray[0]="<%=deliverystate.getDeliverystate()%>,podresultid";
initEditArray[1]="<%=backreasonid%>,backreasonid";
initEditArray[2]="<%=leavedreasonid%>,leavedreasonid";
initEditArray[3]="<%=deliverystate.getPodremarkid()%>,podremarkid";
initEditArray[4]="<%=deliverystate.getReturnedfee()%>,returnedfee";
initEditArray[5]="<%=deliverystate.getCash() %>,receivedfeecash";
initEditArray[6]="<%=deliverystate.getPos()%>,receivedfeepos";
initEditArray[7]="<%=deliverystate.getPosremark()%>,posremark";
initEditArray[8]="<%=deliverystate.getCheckfee()%>,receivedfeecheque";
initEditArray[9]="<%=deliverystate.getOtherfee()%>,receivedfeeother";
initEditArray[10]="<%=deliverystate.getCheckremark()%>,checkremark";
initEditArray[11]="<%=deliverystate.getDeliverstateremark()%>,deliverstateremark";
initEditArray[12]="<%=deliverystate.getReceivedfee()%>,infactfee";

</script>

<div id="box_bg" ></div>
<div id="box_contant" >
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1>
			<div id="close_box" onclick="closeBox()"></div>
			修改订单配送结果</h1>
			<form id="deliverystate_save_Form" name="deliverystate_save_Form" 
			 onSubmit="if(check_deliveystate_edit(<%=DeliveryStateEnum.PeiSongChengGong.getValue()%>,<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue()%>,
   			<%=DeliveryStateEnum.ShangMenHuanChengGong.getValue()%>,<%=DeliveryStateEnum.JuShou.getValue()%>,
   			<%=DeliveryStateEnum.BuFenTuiHuo.getValue() %>,<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>,
   			<%=DeliveryStateEnum.ShangMenJuTui.getValue() %>,<%=DeliveryStateEnum.HuoWuDiuShi.getValue() %>)){submitSaveFormAndCloseBox(this);}return false;" 
			 action="<%=request.getContextPath()%>/applyeditdeliverystate/agreeEditDeliveryState/<%=applyEditDeliverystate.getId()%>/<%=applyEditDeliverystate.getDeliverid()%>" method="post"  >
				<table width="600" border="0" cellspacing="1" cellpadding="5" class="table_2">
					<tr class="font_1">
						<td colspan="2" align="left" valign="top">
						订单号：<strong><%=applyEditDeliverystate.getCwb() %></strong> &nbsp;&nbsp;
						订单类型：<strong><%for(CwbOrderTypeIdEnum coti : CwbOrderTypeIdEnum.values()){if(applyEditDeliverystate.getCwbordertypeid()==coti.getValue()){ %><%=coti.getText() %><%}} %></strong>&nbsp;&nbsp;
						小件员姓名：<strong><%for(User u : userList){if(applyEditDeliverystate.getDeliverid()==u.getUserid()){ %><%=u.getRealname() %><%}} %></strong>&nbsp;&nbsp;
						申请人：<strong><%for(User u : userList){if(applyEditDeliverystate.getApplyuserid()==u.getUserid()){ %><%=u.getRealname() %><%}} %></strong>
					</td>
					</tr>
					<tr class="font_1">
						<td width="120" align="right" valign="top">配送结果：						</td>
						<td align="left" valign="top"><select id ="podresultid" name ="podresultid" >
							<option value="<%=applyEditDeliverystate.getEditnowdeliverystate()%>"><%for(DeliveryStateEnum dse : DeliveryStateEnum.values()){if(applyEditDeliverystate.getEditnowdeliverystate()==dse.getValue()){ %><%=dse.getText() %><%}} %></option>
						</select></td>
					</tr>
					<input type="hidden" id="isReceivedfee" value="<%=(cwborder.getPaybackfee().compareTo(cwborder.getReceivablefee())==1?"no":"yes") %>"/>
					<input type="hidden" id="newpaywayid" value="<%=cwborder.getNewpaywayid()%>"/>
		   			
                   		<%-- <option value ="-1">==请选择==</option>
	                   <%if(deliverystate.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()){%>
	                   		<option value ="<%=DeliveryStateEnum.PeiSongChengGong.getValue() %>"><%=DeliveryStateEnum.PeiSongChengGong.getText() %></option>
							<option value ="<%=DeliveryStateEnum.JuShou.getValue() %>"><%=DeliveryStateEnum.JuShou.getText() %></option>
							<option value ="<%=DeliveryStateEnum.BuFenTuiHuo.getValue() %>"><%=DeliveryStateEnum.BuFenTuiHuo.getText() %></option>
							<option value ="<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>"><%=DeliveryStateEnum.FenZhanZhiLiu.getText() %></option>
							<option value ="<%=DeliveryStateEnum.HuoWuDiuShi.getValue() %>"><%=DeliveryStateEnum.HuoWuDiuShi.getText() %></option>
	                   <%}else if(deliverystate.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()){ %>
	                   		<option value ="<%=DeliveryStateEnum.ShangMenHuanChengGong.getValue() %>"><%=DeliveryStateEnum.ShangMenHuanChengGong.getText() %></option>
	                   		<option value ="<%=DeliveryStateEnum.JuShou.getValue() %>"><%=DeliveryStateEnum.JuShou.getText() %></option>
							<option value ="<%=DeliveryStateEnum.BuFenTuiHuo.getValue() %>"><%=DeliveryStateEnum.BuFenTuiHuo.getText() %></option>
							<option value ="<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>"><%=DeliveryStateEnum.FenZhanZhiLiu.getText() %></option>
	                   <%}else if(deliverystate.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()){ %>
	                   		<option value ="<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue() %>"><%=DeliveryStateEnum.ShangMenTuiChengGong.getText() %></option>
	                   		<option value ="<%=DeliveryStateEnum.ShangMenJuTui.getValue() %>"><%=DeliveryStateEnum.ShangMenJuTui.getText() %></option>
	                   		<option value ="<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>"><%=DeliveryStateEnum.FenZhanZhiLiu.getText() %></option>
	                   <%} %> --%>
                   
           			<tr>
					<td align="right" valign="top"><span class="font_1">退货原因：</span></td>
					<td align="left" valign="top">
		           		<select name="backreasonid" id="backreasonid">
		           			<option value ="0">==请选择==</option>
		           			<%for(Reason r : backlist){ %>
		           				<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent() %></option>
		           			<%} %>
		           		</select>
	           		</td>
					</tr>
			        <tr>
						<td align="right" valign="top"><span class="font_1">滞留原因：</span></td>
						<td align="left" valign="top">
					        <select name="leavedreasonid" id="leavedreasonid">
					        	<option value ="0">==请选择==</option>
					        	<%for(Reason r : leavedlist){ %>
			           				<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent() %></option>
			           			<%} %>
					        </select>
			        	</td>
					</tr>
			        <tr>
						<td align="right" valign="top"><span class="font_1">配送结果备注：</span></td>
						<td align="left" valign="top">
					        <select name="podremarkid" id="podremarkid">
					        	<option value ="0">==请选择==</option>
					        	<%for(Reason r : podremarkreasonlist){ %>
			           				<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent() %></option>
			           			<%} %>
					        </select>
			        	</td>
					</tr>
			        <tr class="font_1">
						<td align="right">应<%=(cwborder.getPaybackfee().compareTo(cwborder.getReceivablefee())==1?"退":"收") %>款：</td>
						<td align="left"><%=deliverystate.getBusinessfee() %><input type="hidden" id="shouldfee" value="<%=deliverystate.getBusinessfee()%>"></td>
					</tr>
			        <tr class="font_1">
						<td align="right">实收款：</td>
						<td align="left"><input type="text" id="infactfee" value="<%=deliverystate.getReceivedfee()%>"/></td>
					</tr>
			        <tr class="font_1">
						<td align="right">退还现金：</td>
						<td align="left"><input type="text" name="returnedfee" id="returnedfee" value ="<%=deliverystate.getReturnedfee()%>" maxlength="50"/></td>
					</tr>
				    <tr class="font_1">
						<td align="right">实收现金：</td>
						<td align="left"><input type="text" name="receivedfeecash" id="receivedfeecash" value ="<%=deliverystate.getCash()%>"/></td>
					</tr>
					<tr class="font_1">
						<td align="right">POS刷卡实收：</td>
						<td align="left"><input type="text" name="receivedfeepos" id="receivedfeepos" value ="<%=deliverystate.getPos()%>" maxlength="50"/></td>
					</tr>
					<tr class="font_1">
						<td align="right">POS备注：</td>
						<td align="left"><input type="text" name="posremark" id="posremark" value ="<%=deliverystate.getPosremark()%>" maxlength="50"/></td>
					</tr>
			        <tr class="font_1">
						<td align="right">支票实收：</td>
						<td align="left"><input type="text" name="receivedfeecheque" id="receivedfeecheque" value="<%=deliverystate.getCheckfee() %>" maxlength="50"/></td>
					</tr>
					<tr class="font_1">
						<td align="right">其他实收：</td>
						<td align="left"><input type="text" name="receivedfeeother" id="receivedfeeother" value ="<%=deliverystate.getOtherfee()%>" maxlength="50"></td>
					</tr>
					<tr class="font_1">
						<td align="right">支票号备注：</td>
						<td align="left"><input type="text" name="checkremark" id="checkremark" value ="<%=deliverystate.getCheckremark()%>" maxlength="50"></td>
					</tr>
			        <tr class="font_1">
						<td align="right">反馈备注输入内容：</td>
						<td align="left"><input type="text" name="deliverstateremark" id="deliverstateremark" value ="<%=deliverystate.getDeliverstateremark()%>" maxlength="50"></td>
					</tr>
				</table>
			<div align="center">
				<input type="submit" value="保存" class="button">
			</div>
		</form>
	</div>
</div>

