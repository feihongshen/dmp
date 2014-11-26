<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.domain.GotoClassAuditing"%>
<%@page import="cn.explink.domain.GotoClassOld"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.User"%>
<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
<%
	GotoClassAuditing gca = (GotoClassAuditing)request.getAttribute("gotoClassAuditing");
	GotoClassOld gco = (GotoClassOld)request.getAttribute("gotoClassOld");
	Branch b = (Branch)request.getAttribute("branch");
	User u = (User)request.getAttribute("deliverealuser");
	
	String printUrl = request.getContextPath()+"/delivery/printSub?branchid="+b.getBranchid()
			+"&userid="+u.getUserid()+"&auditingtime="+gca.getAuditingtime()+"&nownumber="+gco.getNownumber()
			+"&yiliu="+gco.getYiliu()+"&lishiweishenhe="+gco.getLishi_weishenhe()+"&zanbuchuli="+gco.getZanbuchuli()
			+"&SumCount="+gco.getSumCount()+"&pscg="+gco.getPeisong_chenggong()+"&pscgamount="+gco.getPeisong_chenggong_amount()
			+"&pscgposamount="+gco.getPeisong_chenggong_pos_amount()
			+"&tuihuo="+gco.getTuihuo()+"&tuihuoamount="+gco.getTuihuo_amount()+"&bufentuihuo="+gco.getBufentuihuo()
			+"&bufentuihuoamount="+gco.getBufentuihuo_amount()+"&bufentuihuoposamount="+gco.getBufentuihuo_pos_amount()
			+"&zhiliu="+gco.getZhiliu()+"&zhiliuamount="+gco.getZhiliu_amount()
			+"&smtcg="+gco.getShangmentui_chenggong()+"&smtcgamount="+gco.getShangmentui_chenggong_amount()
			+"&smtcgfare="+gco.getShangmentui_chenggong_fare()
			+"&smtjutui="+gco.getShangmentui_jutui()
			+"&smtjutuiamount="+gco.getShangmentui_jutui_amount()
			+"&smtjutuifare="+gco.getShangmentui_jutui_fare()
			+"&smhcg="+gco.getShangmenhuan_chenggong()+"&smhcgamount="+gco.getShangmenhuan_chenggong_amount()
			+"&smhcgposamount="+gco.getShangmenhuan_chenggong_pos_amount()
			+"&diushi="+gco.getDiushi()+"&diushiamount="+gco.getDiushi_amount()+"&SumReturnCount="+gco.getSumReturnCount()
			+"&SumReturnCountAmount="+gco.getSumReturnCountAmount()+"&SumReturnCountPosAmount="+gco.getSumReturnCountPosAmount()
			+"&SumSmtFare="+gco.getShangmentui_chenggong_fare().add(gco.getShangmentui_jutui_fare());
			
	String usedeliverpayup = request.getAttribute("usedeliverpayup")==null?"no":(String)request.getAttribute("usedeliverpayup");
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_in_bg">
		<h1>
			<div id="close_box" onclick="closeBox()"></div>
			历史归班详情</h1>
	<form method="post" action="<%=request.getContextPath()%>/delivery/deliverPayUpArrearage/<%=gca.getId() %>" onsubmit="submitAuditArrearageAndCloseBox();return false;" name="cwbordertype_cre_Form" id="cwbordertype_cre_Form">
		<div id="box_form">
			
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
						<tr id="customertr" class=VwCtr style="display:">
							<td width="300" colspan="2">站点：<%=b.getBranchname() %>　姓名：<%=u.getRealname() %></td>
							<td width="250">审核日期：<%=gca.getAuditingtime() %></td>
						</tr>
						<tr>
							<td colspan="2"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table3">
								<tr >
									<td width="50%" align="center" valign="middle" >当日领货</td>
									<td align="center" valign="middle" ><strong><%=gco.getNownumber() %></strong></td>
									
								</tr>
								<tr>
									<td width="10%" align="center" valign="middle" >遗留货物</td>
									<td align="center" valign="middle" ><strong><%=gco.getYiliu() %></strong></td>
									
								</tr>
									<tr>
									<td width="10%" align="center" valign="middle" >历史未归班</td>
									<td align="center" valign="middle" ><strong><%=gco.getLishi_weishenhe() %></strong></td>
									
								</tr>
								<tr>
									<td width="10%" align="center" valign="middle" >暂不处理</td>
									<td align="center" valign="middle" ><strong><%=gco.getZanbuchuli() %></strong></td>
									
								</tr>
								
								<tr>
									<td align="center" valign="middle" >总货数</td>
									<td align="center" valign="middle" ><strong><%=gco.getSumCount() %></strong></td>
								</tr>
							</table>
							<td colspan="2">
							<%if("yes".equals(usedeliverpayup)){ //当开启小件员交款功能时，提交小件员交款对应的参数 %>
								小件员当时现金余额：<%=gca.getDeliverAccount() %>元
								<br/>小件员当时POS余额：<%=gca.getDeliverPosAccount() %>元
								<br/>现金交款：<%=gca.getDeliverpayupamount().doubleValue() %>元
								<br/>当前余额：<%=gca.getDeliverpayuparrearage().doubleValue() %>元
								<br/>用户POS刷卡交款：<%=gca.getDeliverpayupamount_pos().doubleValue() %>元
								<br/>用户POS刷卡欠款：<%=gca.getDeliverpayuparrearage_pos().doubleValue() %>元
								<br/>小票号：<%=gca.getDeliverpayupbanknum() %>
								<br/>地     址：<%=gca.getPayupaddress() %>
								
							<%} %>
							　</td>
						</tr>
						<tr>
							<td colspan="3">归班结果：共计<%=gco.getSumCount() %>单，审核<%=gco.getSumReturnCount() %>单</td>
						</tr>
						<tr>
							<td colspan="3"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
								<tr class="font_1">
									<td width="20%" height="38" align="center" valign="middle" bgcolor="#eef6ff">归班状态</td>
									<td align="center" valign="middle" bgcolor="#eef6ff">单数</td>
									<td width="60%" align="center" valign="middle" bgcolor="#eef6ff">收款金额</td>
								</tr>
								<tr>
									<td width="10%" align="center" valign="middle" bgcolor="#eef6ff" >配送成功</td>
									<td align="center" valign="middle" ><%=gco.getPeisong_chenggong() %></td>
									<td width="10%" align="center" >POS收款:<%=gco.getPeisong_chenggong_pos_amount() %>元　其他收款:<%=gco.getPeisong_chenggong_amount() %>元</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >拒收</td>
									<td align="center" valign="middle" ><%=gco.getTuihuo() %></td>
									<td align="center" ><%=gco.getTuihuo_amount() %>元</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >部分拒收</td>
									<td align="center" valign="middle" ><%=gco.getBufentuihuo() %></td>
									<td align="center" >POS收款:<%=gco.getBufentuihuo_pos_amount() %>元　其他收款:<%=gco.getBufentuihuo_amount() %>元</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >滞留</td>
									<td align="center" valign="middle" ><%=gco.getZhiliu() %></td>
									<td align="center" ><%=gco.getZhiliu_amount() %>元</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >上门退成功</td>
									<td align="center" valign="middle" ><%=gco.getShangmentui_chenggong() %></td>
									<td align="center" ><%=gco.getShangmentui_chenggong_amount() %>元　实收运费：<%=gco.getShangmentui_chenggong_fare()==null?BigDecimal.ZERO:gco.getShangmentui_chenggong_fare() %>元</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >上门退拒退</td>
									<td align="center" valign="middle" ><%=gco.getShangmentui_jutui() %></td>
									<td align="center" ><%=gco.getShangmentui_jutui_amount() %>元　实收运费：<%=gco.getShangmentui_jutui_fare()==null?BigDecimal.ZERO:gco.getShangmentui_jutui_fare() %>元</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >上门换成功</td>
									<td align="center" valign="middle" ><%=gco.getShangmenhuan_chenggong() %></td>
									<td align="center" >POS收款:<%=gco.getShangmenhuan_chenggong_pos_amount() %>元　其他收款:<%=gco.getShangmenhuan_chenggong_amount() %>元</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >丢失破损</td>
									<td align="center" valign="middle" ><%=gco.getDiushi() %></td>
									<td align="center" ><%=gco.getDiushi_amount() %>元</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#f4f4f4" >合计</td>
									<td align="center" valign="middle" bgcolor="#f4f4f4" ><strong><%=gco.getSumReturnCount() %></strong></td>
									<td align="center" bgcolor="#f4f4f4" ><strong>pos收款:<%=gco.getSumReturnCountPosAmount() %>元　其他收款:<%=gco.getSumReturnCountAmount() %>元　实收运费:<%=(gco.getShangmentui_chenggong_fare()==null?BigDecimal.ZERO:gco.getShangmentui_chenggong_fare()).add(gco.getShangmentui_jutui_fare()==null?BigDecimal.ZERO:gco.getShangmentui_jutui_fare()) %>元</strong></td>
								</tr>
							</table></td>
						</tr>
					</table>
		</div>
		<div align="center">
			<input type="button" value="打 印" id="print_GotoClassOld" onclick="window.open('<%=printUrl %>')"  class="button"/>
		</div>
		 
	 </form>
	</div>
</div>
