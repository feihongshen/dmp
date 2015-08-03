<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> --%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.VO.PreOrderVO;"%>
<%@page import="cn.explink.util.*"%>
<%
  List<PreOrderVO> preOrderList = (List<PreOrderVO>)request.getAttribute("preOrderList");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/commonUtil.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/WEB-INF/jsp/express/preOrderAudit/preOrderDeal.js"></script>
<title>预订单处理</title>
<script>
function addInit(){
	//无处理
}

function closePreOrder(){
	
}
/**
 * 设置颜色
 */
function initColor(td_$){
	if(td_$.checked==null||td_$.checked==undefined){
		td_$.checked = false;
	}
	if(td_$.checked){
		$(td_$).parent().parent().css("background-color","#FFD700");
	}else{
		$(td_$).parent().parent().css("background-color","#f5f5f5");
	}
}

//全选
function checkAll(id){ 
	var chkAll = $("#"+ id +" input[type='checkbox'][name='checkAll']")[0].checked;
	var chkBoxes = $("#"+ id +" input[type='checkbox'][name='checkBox']");
	$(chkBoxes).each(function() {
		$(this)[0].checked = chkAll;
	});
}

function getSelet(id){
	var ids ="";
	//拿到操作的id
	var chkBoxes = $("#"+ id +" input[type='checkbox'][name='checkBox']");
	$(chkBoxes).each(function() {
		if($(this)[0].checked){
			ids +=$(this).val()+ ",";
		}
	});
	if(ids.length==0){
		$.messager.alert("提示", "请选中要操作的数据 ！", "warning");
		return ;
	}else{
		ids = ids.substring(0,ids.length-1);
	}
}
</script>
</head>
<body>
	<div style="background-color:#009DD9;height: 30px;">预订单处理</div>
	<div style="height: 30px;padding-top: 10px;">
		预订单编号<input />&nbsp;&nbsp;&nbsp;
		匹配状态<select><option>全部</option></select>
		<input type="button" value="查询" id="query" style="float:right;width:70px">
	</div>
	<div style="border:1px solid #000;height:30px;text-align:center;vertical-align:middle;">
		<input type="button" id='closeButton' value="自动匹配站点" style="width: 100px;float:right;margin-top: 5px;margin-right: 20px;"/>
		<input type="button" id='add_button' value="手工分配站点" style="width: 100px;float:right;margin-top: 5px;margin-right: 20px;"/>
		<input type="button" id='handMatchButton' value="退回总部" style="width: 100px;float:right;margin-top: 5px;margin-right: 20px;"/>
		<input type="button" id='autoMatchButton' value="关闭预订单" style="width: 100px;float:right;margin-top: 5px;margin-right: 20px;" onclick="getSelet('preOrderTable');"/>
	</div>
	<div>
		<table border="1px" cellspacing="0" cellpadding="0" style="width:99%;margin-left: 5px;margin-top: 5px;border:aliceblue;background-color:#f5f5f5" id="preOrderTable">
			<tr>
				<th><input type="checkbox" name="checkAll" onclick="checkAll('preOrderTable');"></th>
				<th>预订单号</th>
				<th>生成时间</th>
				<th>寄件人</th>
				<th>手机号</th>
				<th>固话</th>
				<th style="width:200px">取件地址</th>
				<th>原因</th>
			</tr>
			<%for(PreOrderVO preOrderVO:preOrderList){ %>
			<tr>
				<td align="center" valign="middle"><input type="checkbox" name="checkBox" value="<%=preOrderVO.getId()%>" onclick="initColor(this);"/></td>
				<td align="center" valign="middle"><%=preOrderVO.getPreOrderNum()%></td>
				<td align="center" valign="middle"><%=preOrderVO.getCreatTime()%></td>
				<td align="center" valign="middle"><%=preOrderVO.getSendPerson()%></td>
				<td align="center" valign="middle"><%=preOrderVO.getCellPhone()%></td>
				<td align="center" valign="middle"><%=preOrderVO.getTelePhone()%></td>
				<td align="center" valign="middle"><%=preOrderVO.getTakeAddress()%></td>
				<td align="center" valign="middle"><%=preOrderVO.getReason()%></td>
			</tr>
			<%} %>
		</table>
		
	</div>
	<input type="hidden" id="add" value="<%=request.getContextPath()%>/preOrderAudit/match" />
</body>
</html>