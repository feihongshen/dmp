<%@page import="cn.explink.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="net.sf.json.JSONObject"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<JSONObject> list = (List<JSONObject>)request.getAttribute("list");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>

<script type="text/javascript">
function check(){
	var begin=$("#beginday").val();
	var end =$("#endday").val();
	
	if(begin.length>0){
		if(end.length>0){
			if(begin>=end){
				alert("开始时间应小于结束时间");
				return false;	
			}	
		}
	}else{
		alert("请选择开始时间");
		return false;
	}
	return true;
	
}
</script>
<script>
$(function() {
	$("#beginday").datepicker();	
	$("#endday").datepicker();	
});
</script>   
</HEAD>
<body style="background:#f5f5f5">
   <div class="menucontant">
		<form id="searchForm" action ="<%=request.getContextPath()%>/funds/viewall" onsubmit="return check();" method = "post">
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1" border="1">
			  <tr>
					<td width="100%">
                                                       日期：<input type ="text" name ="beginday" id="beginday" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("beginday")) %>">
                     	到  <input type ="text" name ="endday" id="endday" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("endday")) %>">
                     自然日分割时间为<select name ="hours" id="hours" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("hours")) %>">
                     	<option value="0" <%="0".equals(request.getParameter("hours"))?"selected":"" %>>0</option>
                     	<option value="1" <%="1".equals(request.getParameter("hours"))?"selected":"" %>>1</option>
                     	<option value="2" <%="2".equals(request.getParameter("hours"))?"selected":"" %>>2</option>
                     	<option value="3" <%="3".equals(request.getParameter("hours"))?"selected":"" %>>3</option>
                     	<option value="4" <%="4".equals(request.getParameter("hours"))?"selected":"" %>>4</option>
                     	<option value="5" <%="5".equals(request.getParameter("hours"))?"selected":"" %>>5</option>
                     	<option value="6" <%="6".equals(request.getParameter("hours"))?"selected":"" %>>6</option>
                     	<option value="7" <%="7".equals(request.getParameter("hours"))?"selected":"" %>>7</option>
                     	<option value="8" <%="8".equals(request.getParameter("hours"))?"selected":"" %>>8</option>
                     	<option value="9" <%="9".equals(request.getParameter("hours"))?"selected":"" %>>9</option>
                     	<option value="10" <%="10".equals(request.getParameter("hours"))?"selected":"" %>>10</option>
                     	<option value="11" <%="11".equals(request.getParameter("hours"))?"selected":"" %>>11</option>
                     	<option value="12" <%="12".equals(request.getParameter("hours"))?"selected":"" %>>12</option>
                     	<option value="13" <%="13".equals(request.getParameter("hours"))?"selected":"" %>>13</option>
                     	<option value="14" <%="14".equals(request.getParameter("hours"))?"selected":"" %>>14</option>
                     	<option value="15" <%="15".equals(request.getParameter("hours"))?"selected":"" %>>15</option>
                     	<option value="16" <%="16".equals(request.getParameter("hours"))?"selected":"" %>>16</option>
                     	<option value="17" <%="17".equals(request.getParameter("hours"))?"selected":"" %>>17</option>
                     	<option value="18" <%="18".equals(request.getParameter("hours"))?"selected":"" %>>18</option>
                     	<option value="19" <%="19".equals(request.getParameter("hours"))?"selected":"" %>>19</option>
                     	<option value="20" <%="20".equals(request.getParameter("hours"))?"selected":"" %>>20</option>
                     	<option value="21" <%="21".equals(request.getParameter("hours"))?"selected":"" %>>21</option>
                     	<option value="22" <%="22".equals(request.getParameter("hours"))?"selected":"" %>>22</option>
                     	<option value="23" <%="23".equals(request.getParameter("hours"))?"selected":"" %>>23</option>
                     </select>点
					<input type ="submit"  value ="查看" id="btnval" class="input_button2"/>
					</td>
				</tr>
				</table>
		 </form>
	<div style="overflow-x: scroll; width:100%" id="scroll2">		
           <table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">		 
				<tr>
					<td width="100%">
					   <table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tr class="font_1"> 
							<td colspan ="7" align="left">
						    <font style="FONT-SIZE: 20pt;" ></font>
					        </td>
					        </tr>
							<tr class="font_1">
								<td align="center" rowspan ="2" valign="middle" bgcolor="#eef6ff">站点  </td>
								<td align="center" colspan ="2" valign="middle" bgcolor="#eef6ff">反馈未归班审核</td>
								<td align="center" colspan ="2" valign="middle" bgcolor="#eef6ff">已归班未上交款</td>
								<td align="center" colspan ="2" valign="middle" bgcolor="#eef6ff">已上交财务未审核</td>
							</tr>
							<tr class="font_1">
								<td align="center" valign="middle" bgcolor="#eef6ff">POS</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">其他</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">POS</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">其他</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">POS</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">其他</td>
							</tr>
							<%
							BigDecimal Tdeliveryamount = BigDecimal.ZERO;
							BigDecimal Tdeliverypos =  BigDecimal.ZERO;
							BigDecimal Tauditingamount =  BigDecimal.ZERO;
							BigDecimal Tauditingpos =  BigDecimal.ZERO;
							BigDecimal Tpayupamount =  BigDecimal.ZERO;
							BigDecimal Tpayuppos =  BigDecimal.ZERO;
							for(JSONObject viewAll:list){ 
							BigDecimal deliveryamount = viewAll.get("deliveryamount")==null?BigDecimal.ZERO:BigDecimal.valueOf(viewAll.getDouble("deliveryamount"));
							BigDecimal deliverypos = viewAll.get("deliverypos")==null?BigDecimal.ZERO:BigDecimal.valueOf(viewAll.getDouble("deliverypos"));
							BigDecimal auditingamount = viewAll.get("auditingamount")==null?BigDecimal.ZERO:BigDecimal.valueOf(viewAll.getDouble("auditingamount"));
							BigDecimal auditingpos = viewAll.get("auditingpos")==null?BigDecimal.ZERO:BigDecimal.valueOf(viewAll.getDouble("auditingpos"));
							BigDecimal payupamount = viewAll.get("payupamount")==null?BigDecimal.ZERO:BigDecimal.valueOf(viewAll.getDouble("payupamount"));
							BigDecimal payuppos = viewAll.get("payuppos")==null?BigDecimal.ZERO:BigDecimal.valueOf(viewAll.getDouble("payuppos"));
							
							Tdeliveryamount = Tdeliveryamount.add(deliveryamount);
							Tdeliverypos =  Tdeliverypos.add(deliverypos);
							Tauditingamount = Tauditingamount.add(auditingamount);
							Tauditingpos = Tauditingpos.add(auditingpos);
							Tpayupamount = Tpayupamount.add(payupamount);
							Tpayuppos = Tpayuppos.add(payuppos);
							
							%>
							<tr valign="middle">
								<td align="center" valign="middle" ><%=viewAll.getString("branchname") %></td>
								<td align="center" valign="middle" ><%=deliverypos.doubleValue() %></td>
								<td align="center" valign="middle" ><%=deliveryamount.doubleValue() %></td>
								<td align="center" valign="middle" ><%=auditingpos.doubleValue() %></td>
								<td align="center" valign="middle" ><%=auditingamount.doubleValue() %></td>
								<td align="center" valign="middle" ><%=payuppos.doubleValue() %></td>
								<td align="center" valign="middle"><%=payupamount.doubleValue() %></td>
							</tr>
							<%} %>
							<tr valign="middle">
								<td>合计</td>
								<td><%=Tdeliverypos.doubleValue() %></td>
								<td><%=Tdeliveryamount.doubleValue() %></td>
								<td><%=Tauditingpos.doubleValue() %></td>
								<td><%=Tauditingamount.doubleValue() %></td>
								<td><%=Tpayuppos.doubleValue() %></td>
								<td><%=Tpayupamount.doubleValue() %></td> 
							</tr>
					   </table>
					</td>
				</tr>
			</table>
			<div class="jg_10"></div>
			<div class="jg_10"></div>
			</div>
  		</div>
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<div class="clear"></div>

<script type="text/javascript">
</script>
   </body>
</HTML>
   
