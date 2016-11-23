<%@page import="cn.explink.controller.DeliveryStateDTO"%>
<%@page import=" cn.explink.domain.Branch"%>
<%@page import=" cn.explink.domain.User"%>
<%@page import=" cn.explink.domain.OrderGoods"%>
<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat, java.math.*" pageEncoding="UTF-8"%>
<%
	DeliveryStateDTO dsDTO = request.getAttribute("deliveryStateDTO")==null?null:(DeliveryStateDTO)request.getAttribute("deliveryStateDTO");
	Branch b = 	request.getAttribute("branch")==null?null:(Branch)request.getAttribute("branch");
	User deliver = 	request.getAttribute("deliver")==null?null:(User)request.getAttribute("deliver");

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	SimpleDateFormat sdf_save = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String okTime =sdf.format(new Date());
	
	String printUrl = request.getContextPath()+"/delivery/printSub?branchid="+b.getBranchid()
			+"&userid="+deliver.getUserid()+"&auditingtime="+okTime+"&nownumber="+dsDTO.getNowNumber()
			+"&yiliu="+(dsDTO.getYiliu())+"&lishiweishenhe="+dsDTO.getLishi_weishenhe()
			+"&zanbuchuli="+dsDTO.getZanbuchuli()+"&SumCount="+dsDTO.getAllNumber()
			+"&pscg="+(dsDTO.getFankui_peisong_chenggong()-dsDTO.getFankui_peisong_chenggong_zanbuchuli())
			+"&pscgamount="+dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_peisong_chenggongList())
			+"&pscgposamount="+dsDTO.getPosAmountNotZanbuchuli(dsDTO.getFankui_peisong_chenggongList())
			+"&tuihuo="+(dsDTO.getFankui_tuihuo()-dsDTO.getFankui_tuihuo_zanbuchuli())
			+"&tuihuoamount="+(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_tuihuoList()))
			+"&bufentuihuo="+(dsDTO.getFankui_bufentuihuo()-dsDTO.getFankui_bufentuihuo_zanbuchuli())
			+"&bufentuihuoamount="+(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_bufentuihuoList()))
			+"&bufentuihuoposamount="+(dsDTO.getPosAmountNotZanbuchuli(dsDTO.getFankui_bufentuihuoList()))
			+"&zhiliu="+(dsDTO.getFankui_zhiliu()-dsDTO.getFankui_zhiliu_zanbuchuli())
			+"&zhiliuamount="+(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_zhiliuList()))
			+"&zhongzhuan="+(dsDTO.getFankui_zhongzhuan()-dsDTO.getFankui_zhongzhuan_zanbuchuli())
			+"&zhongzhuanamount="+(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_zhongzhuanList()))
			+"&smtcg="+(dsDTO.getFankui_shangmentui_chenggong()-dsDTO.getFankui_shangmentui_chenggong_zanbuchuli())
			+"&smtcgamount="+(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_chenggongList()))
			+"&smtcgfare="+(dsDTO.getSmtcgFareAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_chenggongList())) 
			+"&smtjutui="+(dsDTO.getFankui_shangmentui_jutui()-dsDTO.getFankui_shangmentui_jutui_zanbuchuli())
			+"&smtjutuiamount="+(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_jutuiList()))
			+"&smtjutuifare="+(dsDTO.getSmtjtFareAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_jutuiList()))
			+"&smhcg="+(dsDTO.getFankui_shangmenhuan_chenggong()-dsDTO.getFankui_shangmenhuan_chenggong_zanbuchuli())
			+"&smhcgamount="+(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_shangmenhuan_chenggongList()))
			+"&smhcgposamount="+(dsDTO.getPosAmountNotZanbuchuli(dsDTO.getFankui_shangmenhuan_chenggongList()))
			+"&diushi="+(dsDTO.getFankui_diushi()-dsDTO.getFankui_diushi_zanbuchuli())
			+"&diushiamount="+(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_diushiList()))
			+"&SumReturnCount="+dsDTO.getSubNumber()
			+"&SumReturnCountAmount="+dsDTO.getTotal().subtract(dsDTO.getPos_amount()).subtract(dsDTO.getCodpos_amount())
			+"&SumReturnCountPosAmount="+dsDTO.getPos_amount()
			+"&SumReturnCountCodPosAmount="+dsDTO.getCodpos_amount()
			+"&SumSmtFare="+dsDTO.getSmtcgfare_amount().add(dsDTO.getSmtjtfare_amount())
			+"&pscgcodposamount="+dsDTO.getCodPosAmountNotZanbuchuli(dsDTO.getFankui_peisong_chenggongList())
			+"&smhcgposamount="+(dsDTO.getCodPosAmountNotZanbuchuli(dsDTO.getFankui_shangmenhuan_chenggongList()));
	
String usedeliverpayup = request.getAttribute("usedeliverpayup")==null?"no":(String)request.getAttribute("usedeliverpayup");
List<OrderGoods> vipSmhGoodsList=(List<OrderGoods>)request.getAttribute("vipSmhGoodsList");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_in_bg">
		<h1>
			<div id="close_box" onclick="closeBox()"></div>
			归班确认</h1>
	<%if("yes".equals(usedeliverpayup)){ //当开启小件员交款功能时，使用不同的验证提交ajax %>
		<form method="post" action="<%=request.getContextPath()%>/delivery/ok" onsubmit="submitAuditAndCloseBox(this);return false;" name="cwbordertype_cre_Form" id="cwbordertype_cre_Form">
	<%}else{ %>
		<form method="post" action="<%=request.getContextPath()%>/delivery/ok" onsubmit="$('#send_addcityuser').attr('disabled','disabled');submitSaveFormAndCloseBox(this);return false;" name="cwbordertype_cre_Form" id="cwbordertype_cre_Form">
	<%} %>
		<div id="box_form">
			
			<input type="hidden" name="zanbuchuliTrStr" value="<%=request.getAttribute("zanbuchuliTrStrFinal") %>"/>
			<input type="hidden" name="subTrStr" value="<%=request.getAttribute("subTrStrFinal") %>"/>
			<input type="hidden" name="nocwbs" value="<%=request.getParameter("nocwbs") %>"/>
			<input type="hidden" name="subAmount" value="<%=dsDTO.getUpPayAmount() %>"/>
			<input type="hidden" name="subAmountPos" value="<%=dsDTO.getPos_amount() %>"/>
			<input type="hidden" name="subAmountCodPos" value="<%=dsDTO.getCodpos_amount() %>"/>
			<input type="hidden" name="subSmtFare" value="<%=dsDTO.getSmtcgfare_amount().add(dsDTO.getSmtjtfare_amount()) %>"/>
			<input type="hidden" name="okTime" value="<%=sdf_save.format(new Date()) %>"/>
			<input type="hidden" name="deliverealuser" value="<%=deliver.getUserid() %>"/>
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
						<tr id="customertr" class=VwCtr style="display:">
							<td width="300" colspan="2">站点：<%=b.getBranchname() %>　　姓名：<%=deliver.getRealname() %></td>
							<td width="250">审核日期：<%=okTime %></td>
						</tr>
						<tr>
							<td colspan="2"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table3">
								<tr >
									<td width="50%" align="center" valign="middle" >今日领货</td>
									<td align="center" valign="middle" ><strong><%=dsDTO.getNowNumber() %></strong>
									<input type="hidden" name="nownumber" value="<%=dsDTO.getNowNumber() %>" />
									</td>
									
								</tr>
								<tr>
									<td width="10%" align="center" valign="middle" >历史遗留货物</td>
									<td align="center" valign="middle" ><strong><%=dsDTO.getYiliu() %></strong>
									<input type="hidden" name="yiliu" value="<%=dsDTO.getYiliu() %>" />
									</td>
									
								</tr>
									<tr>
									<td width="10%" align="center" valign="middle" >历史未归班</td>
									<td align="center" valign="middle" ><strong><%=dsDTO.getLishi_weishenhe() %></strong>
									<input type="hidden" name="lishi_weishenhe" value="<%=dsDTO.getLishi_weishenhe() %>" />
									</td>
									
								</tr>
								<tr>
									<td width="10%" align="center" valign="middle" >暂不处理</td>
									<td align="center" valign="middle" ><strong><%=dsDTO.getZanbuchuli() %></strong>
									<input type="hidden" name="zanbuchuli" value="<%=dsDTO.getZanbuchuli() %>" />
									</td>
									
								</tr>
								
								<tr>
									<td align="center" valign="middle" >总货数</td>
									<td align="center" valign="middle" ><strong><%=dsDTO.getAllNumber() %></strong></td>
								</tr>
							</table>
							<td ><%if("yes".equals(usedeliverpayup)){ //当开启小件员交款功能时，提交小件员交款对应的参数  %>
								小件员当前现金余额：<%=deliver.getDeliverAccount() %>
								<br/>小件员当前POS余额：<%=deliver.getDeliverPosAccount() %>
								<input type="hidden" name="deliverAccount" value="<%=deliver.getDeliverAccount() %>"/>
								<input type="hidden" name="deliverPosAccount"  value="<%=deliver.getDeliverPosAccount() %>"/>
								<br/><input type="radio" name="deliverpayuptype" value="1" />网银　
								<input type="radio" name="deliverpayuptype" value="2"  />POS　
								<input type="radio" name="deliverpayuptype" value="3"  />现金 　
								<br/>应交金额：<%=dsDTO.getTotal().subtract(dsDTO.getPos_amount()).subtract(dsDTO.getCodpos_amount()).subtract(deliver.getDeliverAccount()) %>元
                                <br/>实际交款：<input type="text" id="deliverpayupamount" name="deliverpayupamount" value="" size="10"/>(两位小数)
								<br/><label id="deliverpayupbanknumView" >小票号：<input type="text" id="deliverpayupbanknum" name="deliverpayupbanknum" value="" size="20"/></label>
								<br/><label id="deliverpayupaddressView" >地     址：<input type="text" id="deliverpayupaddress" name="deliverpayupaddress" value="" size="40"/></label>
								<br/>用户POS刷卡金额：<input type="text" id="deliverpayupamount_pos" name="deliverpayupamount_pos" value="<%=dsDTO.getPos_amount() %>" size="10"/>(两位小数)
								<br/>支付宝COD扫码金额：<input type="text" id="deliverpayupamount_codpos" name="deliverpayupamount_codpos" value="<%=dsDTO.getCodpos_amount() %>" size="10"/>(两位小数)
								<%} %>
							　</td>
						</tr>
						<tr>
							<td colspan="3">归班结果：共计<%=dsDTO.getAllNumber() %>单，审核<%=dsDTO.getSubNumber() %>单</td>
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
									<td align="center" valign="middle" ><%=dsDTO.getFankui_peisong_chenggong()-dsDTO.getFankui_peisong_chenggong_zanbuchuli() %>
									<input type="hidden" name="peisong_chenggong" value="<%=dsDTO.getFankui_peisong_chenggong()-dsDTO.getFankui_peisong_chenggong_zanbuchuli() %>" />
									</td>
									<td width="10%" align="center" >
									POS收款:<%=dsDTO.getPosAmountNotZanbuchuli(dsDTO.getFankui_peisong_chenggongList()) %>元　
									支付宝COD扫码收款:<%=dsDTO.getCodPosAmountNotZanbuchuli(dsDTO.getFankui_peisong_chenggongList()) %>元　
									其他收款:<%=dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_peisong_chenggongList()) %>元
									<input type="hidden" name="peisong_chenggong_amount" value="<%=dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_peisong_chenggongList()) %>" />
									<input type="hidden" name="peisong_chenggong_pos_amount" value="<%=dsDTO.getPosAmountNotZanbuchuli(dsDTO.getFankui_peisong_chenggongList()) %>" />
									<input type="hidden" name="peisong_chenggong_codpos_amount" value="<%=dsDTO.getPosAmountNotZanbuchuli(dsDTO.getFankui_peisong_chenggongList()) %>" />
									</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >拒收</td>
									<td align="center" valign="middle" ><%=dsDTO.getFankui_tuihuo()-dsDTO.getFankui_tuihuo_zanbuchuli() %>
									<input type="hidden" name="tuihuo" value="<%=dsDTO.getFankui_tuihuo()-dsDTO.getFankui_tuihuo_zanbuchuli() %>" />
									</td>
									<td align="center" ><%=dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_tuihuoList()) %>元
									<input type="hidden" name="tuihuo_amount" value="<%=dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_tuihuoList()) %>" />
									</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >部分拒收</td>
									<td align="center" valign="middle" ><%=dsDTO.getFankui_bufentuihuo()-dsDTO.getFankui_bufentuihuo_zanbuchuli() %>
									<input type="hidden" name="bufentuihuo" value="<%=dsDTO.getFankui_bufentuihuo()-dsDTO.getFankui_bufentuihuo_zanbuchuli() %>" />
									</td>
									<td align="center" >
									POS收款:<%=dsDTO.getPosAmountNotZanbuchuli(dsDTO.getFankui_bufentuihuoList()) %>元　
									其他收款:<%=dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_bufentuihuoList()) %>元
									<input type="hidden" name="bufentuihuo_amount" value="<%=dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_bufentuihuoList()) %>" />
									<input type="hidden" name="bufentuihuo_pos_amount" value="<%=dsDTO.getPosAmountNotZanbuchuli(dsDTO.getFankui_bufentuihuoList()) %>" />
									</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >滞留</td>
									<td align="center" valign="middle" ><%=dsDTO.getFankui_zhiliu()-dsDTO.getFankui_zhiliu_zanbuchuli() %>
									<input type="hidden" name="zhiliu" value="<%=dsDTO.getFankui_zhiliu()-dsDTO.getFankui_zhiliu_zanbuchuli() %>" />
									</td>
									<td align="center" ><%=dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_zhiliuList()) %>元
									<input type="hidden" name="zhiliu_amount" value="<%=dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_zhiliuList()) %>" />
									</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >上门退成功</td>
									<td align="center" valign="middle" ><%=dsDTO.getFankui_shangmentui_chenggong()-dsDTO.getFankui_shangmentui_chenggong_zanbuchuli() %>
									<input type="hidden" name="shangmentui_chenggong" value="<%=dsDTO.getFankui_shangmentui_chenggong()-dsDTO.getFankui_shangmentui_chenggong_zanbuchuli() %>" />
									</td>
									<td align="center" ><%=dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_chenggongList()) %>元　
									<input type="hidden" name="shangmentui_chenggong_amount" value="<%=dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_chenggongList()) %>" />
									实收运费：<%=dsDTO.getSmtcgFareAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_chenggongList()) %>元
									<input type="hidden" name="shangmentui_chenggong_fare" value="<%=dsDTO.getSmtcgFareAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_chenggongList()) %>" />
									</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >上门退拒退</td>
									<td align="center" valign="middle" ><%=dsDTO.getFankui_shangmentui_jutui()-dsDTO.getFankui_shangmentui_jutui_zanbuchuli() %>
									<input type="hidden" name="shangmentui_jutui" value="<%=dsDTO.getFankui_shangmentui_jutui()-dsDTO.getFankui_shangmentui_jutui_zanbuchuli() %>" />
									</td>
									<td align="center" ><%=dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_jutuiList()) %>元　
									<input type="hidden" name="shangmentui_jutui_amount" value="<%=dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_jutuiList()) %>" />
									实收运费：<%=dsDTO.getSmtjtFareAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_jutuiList()) %>元
									<input type="hidden" name="shangmentui_jutui_fare" value="<%=dsDTO.getSmtjtFareAmountNotZanbuchuli(dsDTO.getFankui_shangmentui_jutuiList()) %>" />
									</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >上门换成功</td>
									<td align="center" valign="middle" ><%=dsDTO.getFankui_shangmenhuan_chenggong()-dsDTO.getFankui_shangmenhuan_chenggong_zanbuchuli() %>
									<input type="hidden" name="shangmenhuan_chenggong" value="<%=dsDTO.getFankui_shangmenhuan_chenggong()-dsDTO.getFankui_shangmenhuan_chenggong_zanbuchuli() %>" />
									</td>
									<td align="center" >
									POS收款:<%=dsDTO.getPosAmountNotZanbuchuli(dsDTO.getFankui_shangmenhuan_chenggongList()) %>元　
									支付宝COD扫码收款:<%=dsDTO.getCodPosAmountNotZanbuchuli(dsDTO.getFankui_shangmenhuan_chenggongList()) %>元
									其他收款:<%=dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_shangmenhuan_chenggongList()) %>元
									<input type="hidden" name="shangmenhuan_chenggong_amount" value="<%=dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_shangmenhuan_chenggongList()) %>" />
									<input type="hidden" name="shangmenhuan_chenggong_pos_amount" value="<%=dsDTO.getPosAmountNotZanbuchuli(dsDTO.getFankui_shangmenhuan_chenggongList()) %>" />
									<input type="hidden" name="shangmenhuan_chenggong_codpos_amount" value="<%=dsDTO.getPosAmountNotZanbuchuli(dsDTO.getFankui_shangmenhuan_chenggongList()) %>" />
									</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >丢失破损</td>
									<td align="center" valign="middle" ><%=dsDTO.getFankui_diushi()-dsDTO.getFankui_diushi_zanbuchuli() %>
									<input type="hidden" name="diushi" value="<%=dsDTO.getFankui_diushi()-dsDTO.getFankui_diushi_zanbuchuli() %>" />
									</td>
									<td align="center" ><%=dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_diushiList()) %>元
									<input type="hidden" name="diushi_amount" value="<%=dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_diushiList()) %>" />
									</td>
								</tr>
								
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >待中转</td>
									<td align="center" valign="middle" ><%=dsDTO.getFankui_zhongzhuan()-dsDTO.getFankui_zhongzhuan_zanbuchuli() %>
									<input type="hidden" name="zhongzhuan" value="<%=dsDTO.getFankui_zhongzhuan()-dsDTO.getFankui_zhongzhuan_zanbuchuli() %>" />
									</td>
									<td align="center" ><%=dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_zhongzhuanList()) %>元
									<input type="hidden" name="zhongzhuan_amount" value="<%=dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_zhongzhuanList()) %>" />
									</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#f4f4f4" >合计</td>
									<td align="center" valign="middle" bgcolor="#f4f4f4" ><strong><%=dsDTO.getSubNumber() %></strong></td>
									<td align="center" bgcolor="#f4f4f4" >
										<strong>
										pos收款:<%=dsDTO.getPos_amount() %>元　
										支付宝COD扫码收款:<%=dsDTO.getCodpos_amount() %>元　
										其他收款:<%=dsDTO.getTotal().subtract(dsDTO.getPos_amount()).subtract(dsDTO.getCodpos_amount()).subtract(new BigDecimal(dsDTO.getAmountNotZanbuchuli(dsDTO.getFankui_tuihuoList()))) %>元　
										实收运费:<%=dsDTO.getSmtcgfare_amount().add(dsDTO.getSmtjtfare_amount()) %>元
										</strong>
									</td>
								</tr>
							</table></td>
						</tr>
						
						<%if(vipSmhGoodsList!=null&&vipSmhGoodsList.size()>0){%>
						<tr id="vipSmhGoodsList">
							<td colspan="3">
									<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
										<tr class="font_1">
											<td width="10%" height="38" align="center" valign="middle" bgcolor="#eef6ff">揽退订单号</td>
											<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">序号</td>
											<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">商品编码</td>
											<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">商品名称</td>
											<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">商品品牌</td>
											<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">件数</td>
											<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">商品规格</td>
											<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">备注</td>
										</tr>
										<%
										String goodsCwb="";
										int goodsSeq=0;
										for(OrderGoods goods:vipSmhGoodsList){
											if(!goods.getCwb().equals(goodsCwb)){
												goodsSeq=1;
												goodsCwb=goods.getCwb();
											}else{
												goodsSeq=goodsSeq+1;
											}
										%>
										<tr>
											<td><%=goods.getCwb()%></td>
											<td><%=goodsSeq%></td>
											<td><%=goods.getGoods_code()%></td>
											<td><%=goods.getGoods_name()%></td>
											<td><%=goods.getGoods_brand()%></td>
											<td><%=goods.getGoods_num()%></td>
											<td><%=goods.getGoods_spec()%></td>
											<td>全揽</td>
										</tr>
										<%}%>
										</table>
								</td></tr>
								<%}%>

					</table>
		</div>
		<div align="center"><input type="submit" value="确认" id="send_addcityuser"   class="button"/>
		<input type="button" value="确认并打印"  onclick="window.open('<%=printUrl %>');return true;" class="button"/>
		<%if(vipSmhGoodsList!=null&&vipSmhGoodsList.size()>0){%>
		<a href="#vipSmhGoodsList">上门退成功明细</a>
		<%}%>
		</div>

	</form>
	</div>

</div>
