<%@page import="cn.explink.domain.ExceedFee"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
ExceedFee ef = request.getAttribute("exceedFee")==null?null:(ExceedFee)request.getAttribute("exceedFee");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<body style="background:#f5f5f5">

<div class="right_box">
				<div class="right_title">
				
				<form action ="<%=request.getContextPath() %>/exceedfee/save/${exceedFee.id }" onSubmit="return check_exceedfee()" method="post" id="saveForm"> 
				<table width="100%" border="0" cellspacing="1" cellpadding="0"  id="gd_table">
					<tr>  
						<td width="30%" align="right" valign="middle">订单金额超过：</td>
						<td width="40%" align="center" valign="middle">
						<input type="text" value="<%=ef==null?0:ef.getExceedfee() %>" name="exceedfee" id ="exceedfee" maxlength="11" />元  *“入库”与“到站”扫描时提示高价
						</td>
						<td width="30%" align="left" valign="middle" >${editsave }</td>
					</tr>
					<tr>
						<td align="center">　</td>
						<td align="center"><input type="submit" value="保存" class="button"  id="save"/></td>
						<td align="center">　</td>
					</tr>
				</table>
				</form>
				</div>
				
			</div>
			
	<div class="jg_10"></div>
	<div class="clear"></div>
</body>
</HTML>

