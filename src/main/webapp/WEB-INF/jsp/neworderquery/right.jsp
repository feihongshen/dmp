<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.controller.OrderFlowView"%>
<%@page import="cn.explink.controller.QuickSelectView"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.orderflow.*"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
QuickSelectView view = (QuickSelectView)request.getAttribute("view");
Customer customer = (Customer)request.getAttribute("customer");
AccountArea accountArea = (AccountArea)request.getAttribute("accountArea");
CustomWareHouse customWareHouse = (CustomWareHouse)request.getAttribute("customWareHouse");
Branch deliverybranch=(Branch)request.getAttribute("deliverybranch");
CwbOrder cwborder = (CwbOrder)request.getAttribute("cwborder");
List<Remark> remarkList = (ArrayList<Remark>)request.getAttribute("remarkList");
List<AbnormalWriteBackView> backViewList = (List<AbnormalWriteBackView>)request.getAttribute("abnormalWriteBackViewList");
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
List<User> userList = (List<User>)request.getAttribute("userList");
List<PunishType> punishTypeList = (List<PunishType>)request.getAttribute("punishTypeList");
ArrayList<PenalizeInside> punishList = (ArrayList<PenalizeInside>)request.getAttribute("punishList");
String punishinsideShixiaoTime=request.getAttribute("punishinsideShixiaoTime")==null?"":request.getAttribute("punishinsideShixiaoTime").toString();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单查询</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<style type="text/css">
.fornew { width:100%; background:#00AEF0;}
</style>
<script language="javascript">
$(function(){
	var $menuli = $(".uc_midbg ul li");
	var $menulilink = $(".uc_midbg ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
	
});

function gotoForm(cwb){
	window.location.href="<%=request.getContextPath()%>/order/right/"+cwb;
}
$(function(){
	$("#remark").keydown(function(event){
		if(event.keyCode==13) {
			return false;
		}
	});
	
});
function remarkPost(){
		if($("#remark").val()=='输入备注信息' || $("#remark").val()==''){
			alert('请输入备注信息');
			return false;
		}
		$.ajax({
			type: "POST",
			url:$("#remarkForm").attr("action"),
			data:$("#remarkForm").serialize(),
			dataType:"json",
			success : function(data) {
				if(data.errorCode==0){
					alert("保存成功");
					window.location.href="<%=request.getContextPath()%>/order/right/<%=view==null?"--":view.getCwb()%>";
				}else{
					alert(data.remark);
				}
			}
		});	
		return false;
	}
</script>
</head>

<body onLoad="$(&#39;#orderSearch&#39;).focus();" marginwidth="0" marginheight="0">
<%if(view !=null ){ %>
<table width="100%" border="0" cellspacing="5" cellpadding="0" class="table_5">
	<tr>
		<td><table width="100%" border="0" cellspacing="1" cellpadding="0">
			<tbody>
				<tr >
				<td height="26" align="left" valign="middle" >
					<div class="uc_midbg fornew">
						<ul >
							<h1>&nbsp;订单详情</h1>
						</ul>
					</div>
				</td>
			</tr>
			<tr>
				<td>
				
				<div class="uc_midbg">
						<ul>
							<li><a href="#" class="light">基本信息</a></li>
							<li><a href="#">款项信息</a></li>
							<li><a href="#">备注信息</a></li>
							<li><a href="#">扣罚信息</a></li>
						</ul>
					</div>
					<div class="tabbox">
						<li>
							<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" >
					<tbody>
					<tr>
						<td bgcolor="#F1F1F1">订&nbsp;单&nbsp;号</td>
						<td><%=view.getCwb()%></td>
						<td bgcolor="#F1F1F1">供货商</td>
						<td><%=customer.getCustomername()%></td>
						<td bgcolor="#F1F1F1">收件人姓名</td>
						<td><%=view.getConsigneenameOfkf()%></td>
						<td bgcolor="#F1F1F1">手机</td>
						<td><%=view.getConsigneemobileOfkf()%></td>
					</tr>
					<tr>
						<td bgcolor="#F1F1F1">收件人地址</td>
						<td colspan="7"><%=view.getConsigneeaddress()%></td>
						</tr>
					<tr>
						<td bgcolor="#F1F1F1">发出商品</td>
						<td ><%=view.getSendcarname()%></td>
						<td bgcolor="#F1F1F1">订单类型</td>
						<td><%=view.getOrderType()%></td>
						<td bgcolor="#F1F1F1">货物类型</td>
						<td><%=view.getCartype()%></td>
						<td bgcolor="#F1F1F1">省</td>
						<td><%=view.getCwbprovince()%></td>
						</tr>
					<tr>
						<td bgcolor="#F1F1F1">客户要求</td>
						<td colspan="5"><%=view.getCustomercommand()%></td>
						<td bgcolor="#F1F1F1">城市</td>
						<td><%=view.getCwbcity()%></td>
						</tr>
					<tr>
						<td bgcolor="#F1F1F1">指定派送分站</td>
						<td><%=deliverybranch.getBranchname()==null?"":deliverybranch.getBranchname()%></td>
						<td bgcolor="#F1F1F1">取回商品</td>
						<td><%=view.getBackcarname()%></td>
						<td bgcolor="#F1F1F1">收件人编号</td>
						<td><%=view.getConsigneeno()%></td>
						<td bgcolor="#F1F1F1">区县</td>
						<td><%=view.getCwbcounty()%></td>
						</tr>
					<tr>
						<td bgcolor="#F1F1F1">面单模板名称</td>
						<td><%=view.getModelname()%></td>
						<td bgcolor="#F1F1F1">发货仓库</td>
						<td><%=customWareHouse.getCustomerwarehouse()==null?"":customWareHouse.getCustomerwarehouse()%></td>
						<td bgcolor="#F1F1F1">货物尺寸</td>
						<td><%=view.getCarsize()%></td>
<!-- 						<td bgcolor="#F1F1F1">自定义1</td> -->
<%-- 						<td><%=cwborder.getRemark1()==null?"":cwborder.getRemark1()%></td> --%>
						
						<td bgcolor="#F1F1F1">关联单号</td>
						<td><%=cwborder.getExchangecwb()==null?"":cwborder.getExchangecwb()%></td>
						</tr>
					<tr>
						<td bgcolor="#F1F1F1">供货商运单号</td>
						<td><%=view.getShipcwb() %></td>
						<td bgcolor="#F1F1F1">发货数量</td>
						<td><%=view.getSendcarnum()%></td>
						<td bgcolor="#F1F1F1">目的地</td>
						<td><%=view.getDestination()%></td>
						<td bgcolor="#F1F1F1">自定义2</td>
						<td><%=cwborder.getRemark2()==null?"":cwborder.getRemark2() %></td>
						
					</tr>
					<tr>
						<td bgcolor="#F1F1F1">实际重量KG</td>
						<td><%=view.getCarrealweight()%></td>
						<td bgcolor="#F1F1F1">取货数量</td>
						<td><%=view.getBackcarnum()%></td>
						<td bgcolor="#F1F1F1">运单号</td>
						<td><%=view.getShipcwb()%></td>
						<td bgcolor="#F1F1F1">自定义3</td>
						<td><%=cwborder.getRemark3()==null?"":cwborder.getRemark3() %></td>
						
					</tr>
					<tr>
						<td bgcolor="#F1F1F1">指定小件员</td>
						<td><%=view.getExceldeliver()%></td>
						<td bgcolor="#F1F1F1">运输方式</td>
						<td><%=view.getTransway()%></td>
						<td bgcolor="#F1F1F1">邮编</td>
						<td><%=view.getConsigneepostcode()%></td>
						<td bgcolor="#F1F1F1">自定义4</td>
						<td><%=cwborder.getRemark4()==null?"":cwborder.getRemark4() %></td>
					</tr>
					<tr>
						<td bgcolor="#F1F1F1">结算区域</td>
						<td><%=accountArea.getAreaname()==null?"":accountArea.getAreaname()%></td>
						<td bgcolor="#F1F1F1">派送类型</td>
						<td><%=view.getCwbdelivertypeStr()%></td>
						<td bgcolor="#F1F1F1">电话</td>
						<td><%=view.getConsigneephoneOfkf()%></td>
						<td bgcolor="#F1F1F1">自定义5</td>
						<td><%=cwborder.getRemark5()==null?"":cwborder.getRemark5()%></td>
					</tr>
					</tbody>
							</table>
						</li>
						<li style="display:none">
							<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" id="gd_table" >
	<tbody>
		<tr>
			<td bgcolor="#F1F1F1">代收金额(元)</td>
			<td><%=view.getReceivablefee()%> </td>
			<td bgcolor="#F1F1F1">货物金额(元)</td>
			<td><%=view.getCaramount()%></td>
			<td bgcolor="#F1F1F1">应退金额(元)</td>
			<td><%=view.getPaybackfee()%></td>
			<td bgcolor="#F1F1F1">支票(元)</td>
			<td><%=view.getCheckfee()%></td>
		</tr>
		<tr>
			<td bgcolor="#F1F1F1">原支付方式</td>
			<td><%=view.getPaytypeNameOld()%></td>
			<td bgcolor="#F1F1F1">现金(元)</td>
			<td><%=view.getCash()%></td>
			<td bgcolor="#F1F1F1">POS(元)</td>
			<td><%=view.getPos()%></td>
			<td bgcolor="#F1F1F1">其它(元)</td>
			<td><%=view.getOtherfee()%></td>
		</tr>
		<tr>
			<td bgcolor="#F1F1F1">现支付方式</td>
			<td><%=view.getPaytypeName() %></td>
			<td bgcolor="#F1F1F1"></td>
			<td></td>
			<td bgcolor="#F1F1F1"></td>
			<td></td>
			<td bgcolor="#F1F1F1"></td>
			<td></td>
		</tr>
	</tbody>
</table>
						</li>
						<li style="display:none">
						<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" id="gd_table" >
					<tbody>
					<%if(remarkList != null && remarkList.size()>0){ %>
						<%if(view.getCwbremark() !=null && !view.getCwbremark().equals("")){ %>
								<%for(String r:view.getCwbremark().split("\n")) {%>
									<tr>
									<td colspan="8" bgcolor="#f1f1f1"><p><%=r%></p></td>
								   </tr>
								<%} %>
							<%}%>
						<%for(Remark r : remarkList){ %>
						<tr>
							<td bgcolor="#f1f1f1"><p>备注时间</p></td>
							
							<td><%=r.getCreatetime() %></td>
							<td bgcolor="#F1F1F1">备注人</td>
							<td><%=r.getUsername() %></td>
							<td bgcolor="#F1F1F1">备注类型</td>
							<td><%=r.getRemarktype() %></td>
							<td bgcolor="#F1F1F1">备注内容</td>
							<td><%=r.getRemark() %></td>
						</tr>
							<%}
					}else{ %>
						<%if(view.getCwbremark() !=null && !view.getCwbremark().equals("")){ %>
							<%for(String r:view.getCwbremark().split("\n")) {%>
								<tr>
								<td colspan="8" bgcolor="#f1f1f1"><p><%=r%></p></td>
							   </tr>
							<%} %>
						<%}else{ %>
						<tr>
							<td colspan="8" bgcolor="#f1f1f1"><p>暂无备注</p></td>
						</tr>
						<%} %>
					<%} %>
					<tr>
							<td colspan="8" bgcolor="#f1f1f1">
							<form method="post" id="remarkForm" action="<%=request.getContextPath()%>/order/saveCwbRemark/<%=view.getCwb()%>">
							<input type="text" id="remark" name="remark" size="60" 
							onblur="if(this.value==''){this.value='输入备注信息'}" onfocus="if(this.value=='输入备注信息'){this.value=''}" value="输入备注信息"  />
							<input type="button"  class='input_button2' value="保存备注" onclick="remarkPost();"/>
							</form>
							</td>
					</tr>
					</tbody>
</table>
						</li>
						<li style="display:none">
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	  <tr class="font_1">
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">扣罚类型</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">扣罚站点</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">扣罚人员</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">扣罚时效</td>
<!-- 			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">优先级别</td>
 -->			<td width="7%" align="center" valign="middle" bgcolor="#eef6ff">扣罚金额</td>
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">扣罚内容</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">创建人</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">创建时间</td>
		</tr>
		<%if(punishList!=null&&punishList.size()>0){
			for(PenalizeInside punish:punishList)
			{
			%>
		<tr>
			<td width="8%" align="center" valign="middle"><%=punish.getCwb()%></td>
			<td width="8%" align="center" valign="middle">
			<%=punish.getPunishbigsortname()%>
			</td>
			<td width="8%" align="center" valign="middle">
			<%=punish.getDutybranchname()%>
			</td>
			<td width="8%" align="center" valign="middle">
			<%=punish.getDutypersonname()%>
			</td>
			<td width="8%" align="center" valign="middle">
			<%=punishinsideShixiaoTime %>小时
			</td>
		<%-- 	<td width="8%" align="center" valign="middle">
			<%=punish.getPunishlevel() %>
			</td> --%>
			<td width="7%" align="center" valign="middle">
			<%=punish.getShenhepunishprice() %>
			</td>
			<td width="15%" align="center" valign="middle">
			<%=punish.getShenhedescribe() %>
			</td>
			<td width="8%" align="center" valign="middle">
			<%=punish.getCreUserName()%>
			</td>
			<td width="13%" align="center" valign="middle">
			<%=punish.getCreDate() %>
			</td>
		</tr>
		<%}} %>
	</table>
</li>
						
					</div>
				
				</td>
			</tr>
		</tbody></table>
		<table width="100%" border="0" cellspacing="1" cellpadding="2">
			<tbody>
				<tr class="font_1">
				<td width="10%" height="26" align="left" valign="middle" bgcolor="#00AEF0">&nbsp;
					<h1>订单过程</h1></td>
			</tr>
			</tbody></table>
		<table width="99%" border="0" cellspacing="0" cellpadding="0">
			<tbody>
				<tr>
					<td width="10%" height="26" align="left" valign="middle">
					<div style="overflow-y:auto; overflow-x:hidden">订单号：<strong><%=view.getCwb() %></strong>&nbsp;&nbsp;
					当前状态：<strong><%if(view.getFlowordertypeMethod()=="已审核"){%>审核为：<%=view.getDeliveryStateText() %><%}else if(view.getFlowordertypeMethod()=="已反馈") {%>反馈为：<%=view.getDeliveryStateText() %><%}else{ %><%=view.getFlowordertypeMethod() %><%} %></strong>&nbsp;&nbsp;
					配送状态：<strong><%=view.getCwbdelivertypeStr() %></strong>					
						<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" >
						<tr>
							<td width="120" align="center" bgcolor="#f1f1f1">操作时间</td>
							<td width="60" align="center" bgcolor="#f1f1f1">操作员</td>
							<td align="center" bgcolor="#f1f1f1">操作详情</td>
						</tr>
						<%if(view.getOrderFlowList() != null && view.getOrderFlowList().size()>0){ %> 
											   <% for(int i=0;i< view.getOrderFlowList().size();i++){%>
											   <% OrderFlowView flow = view.getOrderFlowList().get(i);%> 
												<tr>
													<td align="center"><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(flow.getCreateDate())%></td>
													<td align="center"><%=flow.getOperator().getRealname() %></td>
												    <td><%=flow.getDetail()%>
												    </td>
                                                 </tr>	
                                                 <%if(backViewList != null && backViewList.size()>0){ %>
                                                 <%SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); %>
                                                 <%for(int j=0,k=0;j<backViewList.size();j++,k++){ %>
                                                 	   <% if(flow.getCreateDate().getTime() - f.parse(backViewList.get(j).getCredatetime()).getTime()<=0){%>
                                                 	        <%if(i+1 < view.getOrderFlowList().size() && f.parse(backViewList.get(j).getCredatetime()).getTime() - view.getOrderFlowList().get(i+1).getCreateDate().getTime()<=0){ %>
	                                                 	        <tr>
																	<td align="center"><%=backViewList.get(j).getCredatetime()%></td>
																	<td align="center"><%=backViewList.get(j).getUsername() %></td>
																    <td><font color="red">[问题件]类型：[<%=backViewList.get(j).getTypename() %>]</font>在[<font color="red"><%=backViewList.get(j).getBranchname()%></font>]
																    <%=k==0?"创建":(backViewList.get(j).getDescribe()==null||"".equals(backViewList.get(j).getDescribe())?"备注：无":"备注："+backViewList.get(j).getDescribe()) %>
																    </td>
	                                                 			</tr>
                                                 	        <% backViewList.remove(backViewList.get(j));
                                                 	        j=-1;
                                     			                } %>
                                                 	   <%} %>
                                                 	   <%if(i == view.getOrderFlowList().size()-1 && backViewList.size()>0){ %>
                                                 	   			<tr>
																	<td align="center"><%=backViewList.get(j).getCredatetime()%></td>
																	<td align="center"><%=backViewList.get(j).getUsername() %></td>
																    <td><font color="red">[问题件]类型：[<%=backViewList.get(j).getTypename() %>]</font>在[<font color="red"><%=backViewList.get(j).getBranchname()%></font>]
																    <%=k==0?"创建":(backViewList.get(j).getDescribe()==null||"".equals(backViewList.get(j).getDescribe())?"备注：无":"备注："+backViewList.get(j).getDescribe()) %>
																    </td>
	                                                 			</tr>
                                                 	   <%} %>
                                                 <%}
                                                 } %>
											<%}}%> 
											
						
						</table>
					</div></td>
				</tr>
			</tbody>
		</table></td>
	</tr>
</table>
<%} else{%>
<table width="100%" border="0" cellspacing="5" cellpadding="0" class="table_5">
	<tr>
		<td><table width="100%" border="0" cellspacing="1" cellpadding="0">
			<tbody>
				<tr class="font_1">
				<td height="26" align="left" valign="middle" bgcolor="#00AEF0"><h1>&nbsp;订单详情</h1></td>
			</tr>
			<tr>
				<td>
				
				<div class="uc_midbg">
						<ul>
							<li><a href="#" class="light">基本信息</a></li>
							<li><a href="#">款项信息</a></li>
							<li><a href="#">备注信息</a></li>
							<li><a href="#">扣罚信息</a></li>
						</ul>
					</div>
					<div class="tabbox">
						<li>
							<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" >
					<tbody>
					<tr>
						<td bgcolor="#F1F1F1">订&nbsp;单&nbsp;号</td>
						<td>&nbsp;&nbsp;&nbsp;</td>
						<td bgcolor="#F1F1F1">供货商</td>
						<td>&nbsp;&nbsp;&nbsp;</td>
						<td bgcolor="#F1F1F1">收件人姓名</td>
						<td>&nbsp;&nbsp;&nbsp;</td>
						<td bgcolor="#F1F1F1">手机</td>
						<td>&nbsp;&nbsp;&nbsp;</td>
					</tr>
					<tr>
						<td bgcolor="#F1F1F1">收件人地址</td>
						<td colspan="7"></td>
						</tr>
					<tr>
						<td bgcolor="#F1F1F1">发出商品</td>
						<td ></td>
						<td bgcolor="#F1F1F1">订单类型</td>
						<td></td>
						<td bgcolor="#F1F1F1">货物类型</td>
						<td></td>
						<td bgcolor="#F1F1F1">省</td>
						<td></td>
						</tr>
					<tr>
						<td bgcolor="#F1F1F1">客户要求</td>
						<td colspan="5"></td>
						<td bgcolor="#F1F1F1">城市</td>
						<td></td>
						</tr>
					<tr>
						<td bgcolor="#F1F1F1">指定派送分站</td>
						<td></td>
						<td bgcolor="#F1F1F1">取回商品</td>
						<td></td>
						<td bgcolor="#F1F1F1">收件人编号</td>
						<td></td>
						<td bgcolor="#F1F1F1">区县</td>
						<td></td>
						</tr>
					<tr>
						<td bgcolor="#F1F1F1">面单模板名称</td>
						<td></td>
						<td bgcolor="#F1F1F1">发货仓库</td>
						<td></td>
						<td bgcolor="#F1F1F1">货物尺寸</td>
						<td></td>
						<td bgcolor="#F1F1F1">关联单号</td>
						<td></td>
						
						</tr>
					<tr>
						<td bgcolor="#F1F1F1">供货商运单号</td>
						<td></td>
						<td bgcolor="#F1F1F1">发货数量</td>
						<td></td>
						<td bgcolor="#F1F1F1">目的地</td>
						<td></td>
						<td bgcolor="#F1F1F1">自定义2</td>
						<td></td>
						
					</tr>
					<tr>
						<td bgcolor="#F1F1F1">实际重量KG</td>
						<td></td>
						<td bgcolor="#F1F1F1">取货数量</td>
						<td></td>
						<td bgcolor="#F1F1F1">运单号</td>
						<td></td>
						<td bgcolor="#F1F1F1">自定义3</td>
						<td></td>
						
					</tr>
					<tr>
						<td bgcolor="#F1F1F1">指定小件员</td>
						<td></td>
						<td bgcolor="#F1F1F1">运输方式</td>
						<td></td>
						<td bgcolor="#F1F1F1">邮编</td>
						<td></td>
						<td bgcolor="#F1F1F1">自定义4</td>
						<td></td>
					</tr>
					<tr>
						<td bgcolor="#F1F1F1">结算区域</td>
						<td></td>
						<td bgcolor="#F1F1F1">派送类型</td>
						<td></td>
						<td bgcolor="#F1F1F1">电话</td>
						<td></td>
						<td bgcolor="#F1F1F1">自定义5</td>
						<td></td>
					</tr>
					</tbody>
							</table>
						</li>
						<li style="display:none">
							<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" id="gd_table" >
	<tbody>
		<tr>
			<td bgcolor="#F1F1F1">代收金额(元)</td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td bgcolor="#F1F1F1">货物金额(元)</td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td bgcolor="#F1F1F1">应退金额(元)</td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td bgcolor="#F1F1F1">支票(元)</td>
			<td>&nbsp;&nbsp;&nbsp;</td>
		</tr>
		<tr>
			<td bgcolor="#F1F1F1">原支付方式</td>
			<td></td>
			<td bgcolor="#F1F1F1">现金(元)</td>
			<td></td>
			<td bgcolor="#F1F1F1">POS(元)</td>
			<td></td>
			<td bgcolor="#F1F1F1">其它(元)</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td bgcolor="#F1F1F1">交款方式</td>
			<td></td>
			<td bgcolor="#F1F1F1"></td>
			<td></td>
			<td bgcolor="#F1F1F1"></td>
			<td></td>
			<td bgcolor="#F1F1F1"></td>
			<td>&nbsp;</td>
		</tr>
	</tbody>
</table></li>

						<li style="display:none">
						<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" id="gd_table" >
					<tbody><tr>
						<td bgcolor="#f1f1f1"><p>备注时间</p></td>
						
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td bgcolor="#F1F1F1">备注人</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td bgcolor="#F1F1F1">备注类型</td>
						<td>&nbsp;&nbsp;&nbsp;</td>
						<td bgcolor="#F1F1F1">备注内容</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						</tr>
					</tbody>
</table>
						</li>
						<li style="display:none">
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	  <tr class="font_1">
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">扣罚类型</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">扣罚站点</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">扣罚人员</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">扣罚时效</td>
<!-- 			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">优先级别</td>
 -->			<td width="7%" align="center" valign="middle" bgcolor="#eef6ff">扣罚金额</td>
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">扣罚内容</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">创建人</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">创建时间</td>
		</tr>
		<%if(punishList!=null&&punishList.size()>0){
			for(PenalizeInside punish:punishList)
			{%>
		<tr>
			<td width="8%" align="center" valign="middle"><%=punish.getCwb()%></td>
			<td width="8%" align="center" valign="middle">
			<%=punish.getCwb()%>
			</td>
			<td width="8%" align="center" valign="middle">
			<%=punish.getPunishbigsortname()%>
			</td>
			<td width="8%" align="center" valign="middle">
			<%=punish.getDutybranchname()%>
			</td>
			<td width="8%" align="center" valign="middle">
			<%=punish.getDutypersonname() %>
			</td>
			<td width="8%" align="center" valign="middle">
			<%=punishinsideShixiaoTime %>小时
			</td>
			<%-- <td width="8%" align="center" valign="middle">
			<%=punish.getPunishlevel() %>
			</td> --%>
			<td width="7%" align="center" valign="middle">
			<%=punish.getShenhepunishprice() %>
			</td>
			<td width="15%" align="center" valign="middle">
			<%=punish.getShenhedescribe() %>
			</td>
			<td width="8%" align="center" valign="middle">
			<%=punish.getCreUserName()%>
			</td>
			<td width="13%" align="center" valign="middle">
			<%=punish.getCreDate() %>
			</td>
		</tr>
		<%} }%>
	</table>
</li>
					
					</div>
				
				</td>
			</tr>
		</tbody></table>
		<table width="100%" border="0" cellspacing="1" cellpadding="2">
			<tbody>
				<tr class="font_1">
				<td width="10%" height="26" align="left" valign="middle" bgcolor="#00AEF0">&nbsp;
					<h1>订单过程</h1></td>
			</tr>
			</tbody></table>
		<table width="99%" border="0" cellspacing="0" cellpadding="0">
			<tbody>
				<tr>
					<td width="10%" height="26" align="left" valign="middle">
					<div style="overflow-y:auto; overflow-x:hidden">订单号：<strong></strong>&nbsp;&nbsp;当前状态：<strong></strong>&nbsp;&nbsp;配送状态：<strong></strong>					
						<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" >
						<tr>
							<td width="120" align="center" bgcolor="#f1f1f1">操作时间</td>
							<td width="60" align="center" bgcolor="#f1f1f1">操作员</td>
							<td align="center" bgcolor="#f1f1f1">操作详情</td>
						</tr>
						
						</table>
					</div></td>
				</tr>
			</tbody>
		</table></td>
	</tr>
</table>
<%} %>
</body></html>

