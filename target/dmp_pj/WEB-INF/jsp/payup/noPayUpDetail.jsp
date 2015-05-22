<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.controller.GotoClassAndDeliveryDTO"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<GotoClassAndDeliveryDTO> nopayupdetial = (List<GotoClassAndDeliveryDTO>)request.getAttribute("nopayupdetial");

List<User> userList = request.getAttribute("userList")==null?null:(List<User>)request.getAttribute("userList");
BigDecimal sumbus =BigDecimal.ZERO;
BigDecimal sumreturn=BigDecimal.ZERO;
BigDecimal sumcash=BigDecimal.ZERO;
BigDecimal sumpos=BigDecimal.ZERO;
BigDecimal sumcodpos=BigDecimal.ZERO;
BigDecimal sumcheck=BigDecimal.ZERO;
BigDecimal sumother=BigDecimal.ZERO;

%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>未缴款明细</h1>
		<div id="box_form">
		<table width="800px" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		   <tr class="font_1">
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td> 
				<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
				<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">归班人</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">审核时间</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">应处理金额</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">应退金额</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">现金实收</td>  
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">POS</td> 
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">支付宝COD扫码</td> 
				<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">支票</td>  
				<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">其它</td>  
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">订单状态</td>
				
			</tr>
			<%for(GotoClassAndDeliveryDTO gcd :nopayupdetial){ %>
			<tr>
				<td align="center" valign="middle"><%=gcd.getCwb() %></td>                
				<td align="center" valign="middle"><%=gcd.getCwbordertypeName() %></td>   
				<td align="center" valign="middle"><%if(userList!=null)for(User u : userList){if(gcd.getDeliverealuser()==u.getUserid()){%><%=u.getRealname() %><%}} %></td>
				<td align="center" valign="middle"><%=gcd.getAuditingtime() %></td> 
				<td align="right" valign="middle"><%=gcd.getBusinessfee() %></td>    
				<td align="right" valign="middle"><%=gcd.getReturnedfee() %></td>                     
				<td align="right" valign="middle"><%=gcd.getCash() %></td>                
				<td align="right" valign="middle"><%=gcd.getPos() %></td>                 
				<td align="right" valign="middle"><%=gcd.getCodpos() %></td>                 
				<td align="right" valign="middle"><%=gcd.getCheckfee() %></td>            
				<td align="right" valign="middle"><%=gcd.getOtherfee() %></td>      
				<td align="center" valign="middle"><%
						if(DeliveryStateEnum.PeiSongChengGong.getValue()==gcd.getDeliverystate()){
							out.print(DeliveryStateEnum.PeiSongChengGong.getText());
						}else if(DeliveryStateEnum.ShangMenTuiChengGong.getValue()==gcd.getDeliverystate()){
							out.print(DeliveryStateEnum.ShangMenTuiChengGong.getText());
						}else if(DeliveryStateEnum.ShangMenHuanChengGong.getValue()==gcd.getDeliverystate()){
							out.print(DeliveryStateEnum.ShangMenHuanChengGong.getText());
						}else if(DeliveryStateEnum.BuFenTuiHuo.getValue()==gcd.getDeliverystate()){
							out.print(DeliveryStateEnum.BuFenTuiHuo.getText());
						}else if(DeliveryStateEnum.HuoWuDiuShi.getValue()==gcd.getDeliverystate()){
							out.print(DeliveryStateEnum.HuoWuDiuShi.getText());
						}
				 %></td>       
				<%-- <td align="center" valign="middle"><%=gcd.getConsigneename() %></td>  
				<td align="center" valign="middle"><%=gcd.getConsigneephone() %></td>    --%>
			</tr>
			<%sumbus=  sumbus.add(gcd.getBusinessfee()==null?BigDecimal.ZERO:gcd.getBusinessfee());
                        sumreturn=  sumreturn.add(gcd.getReturnedfee()==null?BigDecimal.ZERO:gcd.getReturnedfee() );
                        sumcash=        sumcash.add(gcd.getCash()==null?BigDecimal.ZERO:gcd.getCash());
                        sumpos=  sumpos.add(gcd.getPos()==null?BigDecimal.ZERO:gcd.getPos());
                        sumcodpos=  sumcodpos.add(gcd.getCodpos()==null?BigDecimal.ZERO:gcd.getCodpos());
                        sumcheck=       sumcheck.add(gcd.getCheckfee()==null?BigDecimal.ZERO:gcd.getCheckfee());
                        sumother=       sumother.add(gcd.getOtherfee()==null?BigDecimal.ZERO:gcd.getOtherfee());%>
                        <%} %>  
			<tr>
			  <td>总计</td>
			  <td>&nbsp;</td>
			  <td>&nbsp;</td>
			  <td>&nbsp;</td>
			  <td><%=sumbus %></td>
			  <td><%=sumreturn %></td>
			  <td><%=sumcash %></td>
			  <td><%=sumpos %></td>
			  <td><%=sumcodpos %></td>
			  <td><%=sumcheck %></td>
			  <td><%=sumother %></td>
			  <td>&nbsp;</td>
			</tr>     	
		</table> 
				
		</div>
	</div>
</div>
<div id="box_yy"></div>
