<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 

<!DOCTYPE html PUBLIC "-//W3C//Dth HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dth">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>预订单查询页面</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<style type="text/css">
	.tdright{
		text-align:right;
	}
	.table1 tr{
		line-height:35px;
	}
	.tdleft{
		text-align:left;		
	}
	.table1 table{
	border: 1px solid #ccc;
	border-collapse: collapse;
	}
</style>
<script type="text/javascript">
$(function(){
	 $('#start_id').datepicker();
	 $('#end_id').datepicker();  
});

</script>

</head>
<body>
<div>
	<form action="${ requestScope.page == null ? '1' : requestScope.page }" id=PageFromW method="post">
			<table width="100%" class="table1">	
				<colgroup>
					<col width="10%">
					<col width="15%">
					<col width="10%">
					<col width="15%">
					<col width="10%">
					<col width="15%">
					<col width="10%">
					<col width="15%">
				</colgroup>			
				<tr>
					<td class="tdright">生成时间:</td>
					<td class="tdleft"><input type="text" name="start_name"  id="start_id" readonly="readonly" class="input_text1"/></td>
					<td colspan="2" ><div style="display:inline;margin-right:50px;">到 </div> <input type="text" name="end_name"  id="end_id" readonly="readonly" class="input_text1"/></td>
					<td class="tdright">预订单编号:</td>
					<td class="tdleft"><input type="text" name="advanceordercode_name"  id="advanceordercode_id" class="input_text1" /></td>
					<td class="tdright">执行状态:</td>
					<td class="tdleft"><select name="excuteType_name" id="excuteType_id" class="select1" >
						<c:forEach items="${excuteTypelist}" var="list">
							<option value="${list.key}">${list.value}</option>	
						</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td class="tdright">寄件人:</td>
					<td class="tdleft"><input type="text" name="sender_name"  id="sender_id" class="input_text1" /></td>
					<td class="tdright">手机号:</td>
					<td class="tdleft"><input type="text" name="mobile_name"  id="mobile_id" class="input_text1" /></td>		
					<c:if test="${userType == 1}">	
						<td class="tdright">站点：</td>
						<td class="tdleft">					
							<select name="station_name" id="station_id" class="select1" >
								<c:forEach items="${stationlist}" var="list">
									<option value="${list.key}">${list.value}</option>	
								</c:forEach>
							</select>
						</td>
					</c:if>
					<c:if test="${userType != 1}">		
						<td class="tdright" style="visibility:hidden;">站点：</td>
						<td class="tdleft" style="visibility:hidden;">					
							<select name="station_name" id="station_id" class="select1" >
								<c:forEach items="${stationlist}" var="list">
									<option value="${list.key}">${list.value}</option>	
								</c:forEach>
							</select>
						</td>
					</c:if>					
					<td></td>
					<td class="tdleft">
						<input type="submit" value="查询" class="input_button2" onclick="$('#PageFromW').attr('action',1);return true;"/>
					</td>					
				</tr>	
			</table>				
	</form>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="callertb">
				<tr class="font_1">
					<th bgcolor="#eef6ff">预订单编号</th>
					<th bgcolor="#eef6ff">执行状态</th>
					<th bgcolor="#eef6ff">站点</th>
					<th bgcolor="#eef6ff">寄件人</th>
					<th bgcolor="#eef6ff">手机号</th>
					<th bgcolor="#eef6ff">固话</th>
					<th bgcolor="#eef6ff">取件地址</th>					
				</tr>
				<%-- <c:forEach  var="cv">
				</c:forEach> --%>
	</table>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1" id="pageid">
		<tr>
			<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
				<a href="javascript:$('#PageFromW').attr('action','1');$('#PageFromW').submit();" >第一页</a>　
				<a href="javascript:$('#PageFromW').attr('action','${page_obj.previous < 1 ? 1 : page_obj.previous}');$('#PageFromW').submit();">上一页</a>　
				<a href="javascript:$('#PageFromW').attr('action','${page_obj.next < 1 ? 1 : page_obj.next}');$('#PageFromW').submit();" >下一页</a>　
				<a href="javascript:$('#PageFromW').attr('action','${page_obj.maxpage < 1 ? 1 : page_obj.maxpage}');$('#PageFromW').submit();" >最后一页</a>
				　共${page_obj.maxpage}页　共${page_obj.total}条记录 　当前第
					<select
						id="selectPg"
						onchange="$('#PageFromW').attr('action',$(this).val());$('#PageFromW').submit()">
						<c:forEach begin="1" end="${page_obj.maxpage}" var="i">
							<option value="${i}">${i}</option>
						</c:forEach>
					</select>页
			</td>
		</tr>
	</table>
</div>
</body>
</html>