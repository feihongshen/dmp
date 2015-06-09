<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.enumutil.PunishtimeEnum"%>
<%@page import="cn.explink.enumutil.PunishlevelEnum"%>
<%@page import="cn.explink.domain.Punish"%>
<%@page import="cn.explink.domain.PunishType"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.util.Page"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>扣罚类型登记</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	$("#starttime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
});

</script>
</head>

<body style="background:#eef9ff" onload="fun()">

<div class="right_box">
	<div class="inputselect_box">
<table width="100%"  >
<tr>
<td width="5%" align="right" rowspan="2" >订单号：</td>
<td width="15%" rowspan="2" valign="bottom" ><textarea  cols="13" style="width: 100%;resize: none;"></textarea> </td>
<td width="7%" align="right">客户名称： </td>
<td width="13%" >
<select style="width: 100%" name="customerid" id="customerid">
<option>全部</option>
		  <c:forEach var="customer" items="${customerList}" varStatus="i">
             <option <c:if  test='${ customer.customerid==1}'>selected="selected"</c:if> value="${customer.customerid}">${customer.customername}</option>
       </c:forEach>
</select> 
</td>
<td width="7%" align="right" > 赔付大类：</td>
<td width="13%" >
<select style="width: 100%">
<option>工单</option>
</select>
</td>
<td width="7%" align="right"> 赔付小类：</td>
<td width="13%" >
<select style="width: 100%">
<option>服务投诉</option>
</select>
</td>
<td width="20%"> </td>
</tr>

<tr>

<td  align="right">订单状态： </td>
<td  >
<select style="width: 100%">
<option>配送</option>
</select> 
</td>
<td  align="right" > 赔付单状态：</td>
<td  >
<select style="width: 100%">
<option>全部</option>
</select>
</td>
<td  align="right"> 创建日期：</td>
<td colspan="2">
<input type ="text" name ="starttime" id="starttime"  value="" style="width: 30%"/>
			到
<input type ="text" name ="endtime" id="endtime"  value="" style="width: 30%"/>
</td>
</tr>
<tr>
<td><input type="button" class="input_button2" value="创建"/> </td>
<td><input type="button" class="input_button2" value="导入"/> <input class="input_button2" type="button" value="生成扣罚单"/> </td>
<td><input type="button" class="input_button2" value="撤销"/> </td>
<td> </td>
<td> </td>
<td> </td>
<td> </td>
<td colspan="2"> <input class="input_button2" type="button" value="查询"/><input class="input_button2" type="button" value="重置"/><input class="input_button2" type="button" value="导出"/>  </td>
</tr>
 </table>
	<form action="<%=request.getContextPath()%>/punish/list/1" method="post" id="searchForm">
		
	</form>
	<div style="height: 20px"></div>
	<div id="box_form" >
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	<tr>
	<td height="30px"  valign="middle"><input type="checkbox"/> </td>
	<td align="center" valign="middle"style="font-weight: bold;"> 订单号</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 客户名称 </td>
	<td align="center" valign="middle"style="font-weight: bold;"> 订单状态</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 订单金额</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 对外赔付金额</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 赔付大类</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 赔付小类</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 创建人</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 创建日期</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 撤销人</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 撤销时间</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 赔付单状态</td>
	
		</tr>
	<tr>
	<td><input type="checkbox"/></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	</tr>
	
	</tr>
	<tr>
	<td></td>
	<td>赔付总计</td>
	<td></td>
	<td></td>
	<td></td>
	<td>1000</td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	</tr>
	</table>
	</div>
</body>
</html>


