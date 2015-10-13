
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.enumutil.ComplaintTypeEnum"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.BranchPayamount"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
  List<BranchPayamount> nowdayNochackList = (List<BranchPayamount>)request.getAttribute("nowdayNochackList");
  List<BranchPayamount> nowdayChackList = (List<BranchPayamount>)request.getAttribute("nowdayChackList");
  Page page_obj = (Page)request.getAttribute("page_obj");
  
  //新增审核状态条件
  int upstate=request.getAttribute("upstate")!=null&&!"".equals(request.getAttribute("upstate").toString())?new Integer(request.getAttribute("upstate").toString()):0;
	if(upstate==0){
		nowdayChackList=null;
	}else{
		nowdayNochackList=null;
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/explink.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/util.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>

<script type="text/javascript">
function check(){
	if($("#sBranchpaydatetime").val()>$("#eBranchpaydatetime").val()){
		alert("站点交款开始时间不能大于结束时间");
		return false;
	}
	
	if($("#sDeliverpaydate").val()>$("#eDeliverpaydate").val()){
		alert("员工交款开始时间不能大于结束时间");
		return false;
	}
		return true;
}
</script>
<script type="text/javascript">  
$("document").ready(function(){  
	$("#btn1").click(function(){  
		if($("#btn1").attr("checked")){
			$("[name='checkbox']").attr("checked",'true');//全选  
		}else{
		   $("[name='checkbox']").removeAttr("checked");//取消全选  
		}	
	
	});
	$("#btnval").click(function(){
		if(check()){
	    $("#searchForm").submit();
		}
	});

	$("#updateF").click(function(){//
		$("#controlStr").val("");
		$("#mackStr").val("");
		var str=""; 
		var mackStr = "";
		$("input[name='checkbox']:checkbox:checked").each(function(){  
			str+=$(this).val()+":"
			+$("#a"+$(this).val()).val()
			+","+$("#b"+$(this).val()).val()
			+","+$("#c"+$(this).val()).val()
			+","+$("#d"+$(this).val()).val()
			+";"; 
			mackStr+=$(this).val()+"P:P"
			+$("#e"+$(this).val()).val()+"P_P"; 
		}); 
		$("#controlStr").val(str);
		$("#mackStr").val(mackStr);
		if($("#mackStr").val()==""){
			alert("没有勾选任何项,不能做此操作!");
			return false;
		}
	    $("#updateForm").submit();
	});
}); 

</script>
<script>
$(function() {
	$("#sBranchpaydatetime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#eBranchpaydatetime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#sDeliverpaydate").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#eDeliverpaydate").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
});
</script>   
</HEAD>
<body style="background:#eef9ff">
   <div class="menucontant">
		<form id="searchForm" action ="<%=request.getContextPath()%>/funds/paymentCheack?fromType=1" method = "post">
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1" border="1">
			<tr id="customertr" class=VwCtr style="display:">
					<td width="100%">
					站点上交款审核：<a href="<%=request.getContextPath()%>/funds/paymentCheack?flag=0">待审核<font style="FONT-SIZE: 20pt; FONT-FAMILY: 华文行楷," color=#ff0000><%=request.getAttribute("allNochackSum") %></font>条</a>
					</td>
			  </tr>
			  <tr>
					<td width="100%">
					审核状态：<select name="upstate">
								<option value="0" <%if(upstate==0){%>selected<%} %> >未审核</option>
								<option value="1" <%if(upstate==1){%>selected<%} %>>已审核</option>
							</select>
					    站点：<select name ="branchid" id="branid">
				               <option value="-1">全部</option>
				               <%if(branchList != null && branchList.size()>0){ %>
				                <%for( Branch b:branchList){ %>
				               <option value ="<%=b.getBranchid()%>" <%if(b.getBranchid() == new Long(request.getParameter("branchid")==null?"-1":request.getParameter("branchid"))) {%>selected="selected"<%} %>><%=b.getBranchname() %></option>
				               <%} }%>
			              </select> 
                                                       站点上交款时间：<input type ="text" name ="strateBranchpaydatetime" id="sBranchpaydatetime" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime")) %>">
                                                                                                到<input type ="text" name ="endBranchpaydatetime" id="eBranchpaydatetime" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime")) %>">
					员工上交款时间：<input type ="text" name ="strateDeliverpaydate" id="sDeliverpaydate" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateDeliverpaydate")) %>">
                                                                                                到<input type ="text" name ="endDeliverpaydate" id="eDeliverpaydate" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("endDeliverpaydate")) %>">
					<input type ="button"  value ="查看" id="btnval" class="input_button2"/>
					</td>
				</tr>
				</table>
		 </form>
<div style="overflow-x: scroll; width:100%" id="scroll2">		
           <table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">		 
				<tr>
				<%if(nowdayNochackList != null && nowdayNochackList.size()>0){ %>
				   
					<td width="100%">
					   <table width="2000" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tr class="font_1"> 
							<td colspan ="20" align="left">
						    <font style="FONT-SIZE: 20pt;" >未审核记录：</font>
					        </td>
					        </tr>
							<tr class="font_1">
								<td  height="38" align="center" valign="middle" bgcolor="#eef6ff"><input type="checkbox" name="checkboxAll" id="btn1"></td>
								<td   align="center" valign="middle" bgcolor="#eef6ff">站点  </td>
								<td  align="center" valign="middle" bgcolor="#eef6ff">站点上缴时间</td>
								<td   valign="middle" bgcolor="#eef6ff">员工交款日期</td>
								<td   align="center" valign="middle" bgcolor="#eef6ff">票数</td>
								<td   align="center" valign="middle" bgcolor="#eef6ff">当日应上缴</td>
								<td  align="center" valign="middle" bgcolor="#eef6ff">当日实收（不含POS）</td>
								<td  align="center" valign="middle" bgcolor="#eef6ff">现金实收[元]</td>
								<td  align="center" valign="middle" bgcolor="#eef6ff">其他款项 </td>
								<td  align="center" valign="middle" bgcolor="#eef6ff">累计欠款</td>
								<td  align="center" valign="middle" bgcolor="#eef6ff">POS实收</td>
								<td  align="center" valign="middle" bgcolor="#eef6ff">支票实收</td>
								<td  align="center" valign="middle" bgcolor="#eef6ff">当日实收（含POS）</td>
								<td  align="center" valign="middle" bgcolor="#eef6ff">上交款审核备注</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">上交款备注 </td>
								<td align="center" valign="middle" bgcolor="#eef6ff">上交款方式</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">交款类型 </td>
								<td align="center" valign="middle" bgcolor="#eef6ff">上交款人</td>
							</tr>
							<% 
							  BigDecimal receivablefee=BigDecimal.ZERO; //应上缴金额
							  BigDecimal receivedfee=BigDecimal.ZERO; //实际上缴金额
							  BigDecimal receivedfeecash=BigDecimal.ZERO; //现金金额
							  BigDecimal totaldebtfee=BigDecimal.ZERO; //累计欠款
							  BigDecimal receivedfeepos=BigDecimal.ZERO; //pos实收
							  BigDecimal receivedfeecheque=BigDecimal.ZERO; //支票实收
							  BigDecimal otherbranchfee=BigDecimal.ZERO; //其他款项
							  BigDecimal receivedfeeAndPos=BigDecimal.ZERO; //其他款项
							  
							  int cwbnum = 0;
							%>
						    <% for(BranchPayamount b : nowdayNochackList){ %>
								<tr valign="middle">
											<td align="center" valign="middle" ><input type="checkbox" name="checkbox" value="<%=b.getBranchid()+"_"+b.getBranchpaydatetime().substring(0, 10)%>"></td>
											<td align="center" valign="middle" ><%=b.getBranchname()==null?"":b.getBranchname() %></td>
											<td align="center" valign="middle" ><%=b.getBranchpaydatetime() %> </td>
											<td align="center" valign="middle" ><%=b.getDeliverpaydate() %></td>
											<td align="center" valign="middle">
											 <%if(b.getCwbnum() > 0 ){ %>
											 <a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=b.getBranchid()+"_"+b.getBranchpaydatetime().substring(0, 10) %>/-1/1"><%=b.getCwbnum() %></a>
											 <%}else{ %>
											 0
											 <%} %>
											 </td>
											<td align="right" valign="middle" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=b.getBranchid()+"_"+b.getBranchpaydatetime().substring(0, 10) %>/-1/1">
											<%=b.getReceivablefee()==null?BigDecimal.ZERO:b.getReceivablefee() %></a></strong></td>
											<td align="right" valign="middle" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=b.getBranchid()+"_"+b.getBranchpaydatetime().substring(0, 10) %>/-1/1">
											<%=b.getReceivedfee()==null?BigDecimal.ZERO:b.getReceivedfee() %></a></strong></td>
											<td align="right" valign="middle" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=b.getBranchid()+"_"+b.getBranchpaydatetime().substring(0, 10) %>/1/1">
											<%=b.getReceivedfeecash()==null?BigDecimal.ZERO:b.getReceivedfeecash() %></a></strong></td>
											<td align="right" valign="middle"><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=b.getBranchid()+"_"+b.getBranchpaydatetime().substring(0, 10) %>/3/1">
											<%=b.getOtherbranchfee()==null?BigDecimal.ZERO:b.getOtherbranchfee() %></a></strong></td>
											<td align="right" valign="middle"><strong><%=b.getTotaldebtfee()==null?BigDecimal.ZERO:b.getTotaldebtfee() %></strong></td>
											<td align="right" valign="middle"><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=b.getBranchid()+"_"+b.getBranchpaydatetime().substring(0, 10) %>/2/1">
											<%=b.getReceivedfeepos()==null?BigDecimal.ZERO:b.getReceivedfeepos() %></a></strong></td>
											<td align="right" valign="middle"><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=b.getBranchid()+"_"+b.getBranchpaydatetime().substring(0, 10) %>/4/1">
											<%=b.getReceivedfeecheque()==null?BigDecimal.ZERO:b.getReceivedfeecheque() %></a></strong></td>
											<td align="right" valign="middle"><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=b.getBranchid()+"_"+b.getBranchpaydatetime().substring(0, 10) %>/-2/1">
											<%=b.getReceivedfee()==null?BigDecimal.ZERO.add(b.getReceivedfeepos()==null?BigDecimal.ZERO:b.getReceivedfeepos()):
												b.getReceivedfee().add(b.getReceivedfeepos()==null?BigDecimal.ZERO:b.getReceivedfeepos()) %></a></strong></td>
											<td align="center" valign="middle"><input  type="text"  id="e<%=b.getBranchid()+"_"+b.getBranchpaydatetime().substring(0, 10)%>"></td>
											<td align="center" valign="middle"><%=b.getPayremark() %></td>
											<td align="center" valign="middle">
											<%if(b.getWay()!=0){ %>
											  <%=b.getWay()==1?"银行转账":"现金"%>
											<%}else{ %>
											  &nbsp;
											<%} %></td>
											<td align="center" valign="middle">
											<%if(b.getPayup_type()!=0){ %>
											<%=b.getPayup_type()==1?"货款":"罚款"%>
											<%} %>
											</td>
											 <td align="center" valign="middle"><%=b.getPayuprealname() %></td>
								 </tr>
								 <%
									receivablefee = receivablefee.add(b.getReceivablefee()==null?BigDecimal.ZERO:b.getReceivablefee());
									receivedfee = receivedfee.add(b.getReceivedfee()==null?BigDecimal.ZERO:b.getReceivedfee());
									receivedfeecash = receivedfeecash.add(b.getReceivedfeecash()==null?BigDecimal.ZERO:b.getReceivedfeecash());
									otherbranchfee = otherbranchfee.add(b.getOtherbranchfee()==null?BigDecimal.ZERO:b.getOtherbranchfee());
									totaldebtfee = totaldebtfee.add(b.getTotaldebtfee()==null?BigDecimal.ZERO:b.getTotaldebtfee());
									receivedfeepos = receivedfeepos.add(b.getReceivedfeepos()==null?BigDecimal.ZERO:b.getReceivedfeepos());
									receivedfeecheque = receivedfeecheque.add(b.getReceivedfeecheque()==null?BigDecimal.ZERO:b.getReceivedfeecheque());
									receivedfeeAndPos = receivedfeeAndPos.add(b.getReceivedfee()==null?BigDecimal.ZERO.add(b.getReceivedfeepos()==null?BigDecimal.ZERO:b.getReceivedfeepos()):
										b.getReceivedfee().add(b.getReceivedfeepos()==null?BigDecimal.ZERO:b.getReceivedfeepos()));
									cwbnum = cwbnum + b.getCwbnum();
								  %>
							<%} %> 
								<tr valign="middle">
									<td>合计</td>
									<td></td>
									<td></td>
									<td></td>
									<td><%=cwbnum%>[票]</td>
									<td align="right" ><strong><%= receivablefee%></strong>[元]</td>
									<td align="right" ><strong><%= receivedfee%></strong>[元]</td>
									<td align="right" ><strong><%= receivedfeecash%></strong>[元]</td>
									<td align="right" ><strong><%= otherbranchfee%></strong>[元]</td>
									<td align="right" ><strong><%= totaldebtfee%></strong>[元]</td>
									<td align="right" ><strong><%= receivedfeepos%></strong>[元]</td>
									<td align="right" ><strong><%= receivedfeecheque%></strong>[元]</td>
									<td align="right" ><strong><%= receivedfeeAndPos%></strong>[元]</td>
									<td></td>
									<td></td> 
									<td></td>
									<td></td>
									<td></td>
								</tr>
									
							    <tr  valign="middle">
							       <td colspan ="19" align="center" valign="middle">
								      <form id="updateForm" action ="<%=request.getContextPath()%>/funds/update"  method = "post">
					                      <input type="hidden" id="controlStr" name="controlStr" value=""/>
					                      <input type="hidden" id="mackStr" name="mackStr" value=""/>
					                      <input type="button" id="updateF"  value="审核交款"/>
					                  </form>
				                  </td>
					            </tr>
					   </table>
					</td>
					<%}%>
				</tr>
				 <tr>
				  <%if(nowdayChackList != null && nowdayChackList.size()>0){ %>
	               <td width="100%">
	                <table width="2000" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
	                <tr class="font_1"> 
							<td colspan ="21" align="left">
						    <font style="FONT-SIZE: 20pt;" >已审核记录：</font>
					        </td>
					        </tr>
	                 <tr class="font_1">
                        <td   align="center" valign="middle" bgcolor="#eef6ff">站点  </td>
						<td   align="center" valign="middle" bgcolor="#eef6ff">站点上缴时间</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff">员工交款日期</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff">票数</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff">当日应上缴</td>
						<td   align="center" valign="middle" bgcolor="#eef6ff">当日实收(不含pos)</td>
						<td   align="center" valign="middle" bgcolor="#eef6ff">现金实收[元]</td>
						<td   align="center" valign="middle" bgcolor="#eef6ff">其他款项 </td>
						<td   align="center" valign="middle" bgcolor="#eef6ff">累计欠款</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff">POS实收</td>
						<td   align="center" valign="middle" bgcolor="#eef6ff">支票实收</td>
						<td   align="center" valign="middle" bgcolor="#eef6ff">当日实收(含pos)</td>
						<td   align="center" valign="middle" bgcolor="#eef6ff">上交款审核备注</td>
						<td   align="center" valign="middle" bgcolor="#eef6ff">上交款备注 </td>
						<td  align="center" valign="middle" bgcolor="#eef6ff">上交款方式</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff">交款类型 </td>
						<td   align="center" valign="middle" bgcolor="#eef6ff">上交款人</td>
						<td   align="center" valign="middle" bgcolor="#eef6ff">审核人</td>
						<td   align="center" valign="middle" bgcolor="#eef6ff">审核时间</td>
					  </tr>
					   <% for(BranchPayamount b : nowdayChackList){ %>
						  <tr valign="middle">
							<td align="center" valign="middle"><%=b.getBranchname() %></td>
							<td align="center" valign="middle"><%=b.getBranchpaydatetime() %></td>
							<td align="center" valign="middle"><%=b.getDeliverpaydate() %></td>
							<td align="center" valign="middle">
							<%if(b.getCwbnum() > 0 ){ %>
									<a href="<%=request.getContextPath()%>/funds/deliverybackStateTypeShow/<%=b.getBranchid()+"_"+b.getBranchpaydatetime().substring(0, 10) %>/-1/1"><%=b.getCwbnum() %></a>
							<%}else{ %>
								0
							<%} %>
                           </td>
							<td align="right" valign="middle"><a href="<%=request.getContextPath()%>/funds/deliverybackStateTypeShow/<%=b.getBranchid()+"_"+b.getBranchpaydatetime().substring(0, 10) %>/-1/1">
							<%=b.getReceivablefee()==null?BigDecimal.ZERO:b.getReceivablefee() %></a></td>
							<td align="right" valign="middle"><a href="<%=request.getContextPath()%>/funds/deliverybackStateTypeShow/<%=b.getBranchid()+"_"+b.getBranchpaydatetime().substring(0, 10) %>/-1/1">
							<%=b.getReceivedfee()==null?BigDecimal.ZERO:b.getReceivedfee() %></a></td>
							<td align="right" valign="middle"><a href="<%=request.getContextPath()%>/funds/deliverybackStateTypeShow/<%=b.getBranchid()+"_"+b.getBranchpaydatetime().substring(0, 10) %>/1/1">
							<%=b.getReceivedfeecash()==null?BigDecimal.ZERO:b.getReceivedfeecash() %></a></td>
							<td align="right" valign="middle"><a href="<%=request.getContextPath()%>/funds/deliverybackStateTypeShow/<%=b.getBranchid()+"_"+b.getBranchpaydatetime().substring(0, 10) %>/3/1">
							<%=b.getOtherbranchfee()==null?BigDecimal.ZERO:b.getOtherbranchfee() %></a></td>
							<td align="right" valign="middle"><%=b.getTotaldebtfee()==null?BigDecimal.ZERO:b.getTotaldebtfee() %></td>
							<td align="right" valign="middle"><a href="<%=request.getContextPath()%>/funds/deliverybackStateTypeShow/<%=b.getBranchid()+"_"+b.getBranchpaydatetime().substring(0, 10) %>/2/1">
							<%=b.getReceivedfeepos()==null?BigDecimal.ZERO:b.getReceivedfeepos() %></a></td>
							<td align="right" valign="middle"><a href="<%=request.getContextPath()%>/funds/deliverybackStateTypeShow/<%=b.getBranchid()+"_"+b.getBranchpaydatetime().substring(0, 10) %>/4/1">
							<%=b.getReceivedfeecheque()==null?BigDecimal.ZERO:b.getReceivedfeecheque() %></a></td>
							<td align="right" valign="middle"><a href="<%=request.getContextPath()%>/funds/deliverybackStateTypeShow/<%=b.getBranchid()+"_"+b.getBranchpaydatetime().substring(0, 10) %>/-2/1">
							<%=b.getReceivedfee()==null?BigDecimal.ZERO.add(b.getReceivedfeepos()==null?BigDecimal.ZERO:b.getReceivedfeepos()):
												b.getReceivedfee().add(b.getReceivedfeepos()==null?BigDecimal.ZERO:b.getReceivedfeepos()) %></a></td>
							<td align="center" valign="middle"><%=b.getCheckremark() %></td>
							<td align="center" valign="middle"><%=b.getPayremark() %></td>
							<td align="center" valign="middle">
							<%if(b.getWay()!=0){ %>
											  <%=b.getWay()==1?"银行转账":"现金"%>
											<%}else{ %>
											  &nbsp;
											<%} %>
							</td>
							<td align="center" valign="middle">
							<%if(b.getPayup_type()!=0){ %>
								<%=b.getPayup_type()==1?"货款":"罚款"%>
								<%} %>
							</td>
							<td align="center" valign="middle"><%=b.getPayuprealname() %></td>
							<td align="center" valign="middle"><%=b.getUsername() %></td>
							<td align="center" valign="middle"><%=b.getCheckdate() %></td>
						  </tr>
						<%} %> 
						
				     </table>
	               </td>
	               
				<%} %>
				</tr> 
			</table>
	  		</div>
   </div> 
   </body>
</HTML>
   
