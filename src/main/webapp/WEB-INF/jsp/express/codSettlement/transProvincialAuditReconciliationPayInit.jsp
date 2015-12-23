<%@page import="cn.explink.domain.express.ExpressCodBill"%>
<%@page  import="cn.explink.enumutil.express.ExpressBillStateEnum"%>
<%@page  import="java.util.*"%>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%	
	List<ExpressCodBill> infoList = (List<ExpressCodBill>)request.getAttribute("infoList");
	List<Map<String, Object>> provincelist = (List<Map<String, Object>>)request.getAttribute("provincelist");
	List<Map<String, Object>> billStatelist = (List<Map<String, Object>>)request.getAttribute("billStatelist");
%>
<!DOCTYPE html PUBLIC "-//W3C//Dth HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dth">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>跨省代收货款审核(应付)页面</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/express/extracedOrderInput.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/easyui-extend/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/commonUtil.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/workorder/csPushSmsList.js" type="text/javascript"></script>

<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
</head>
<body>
	<div id="Flag" style="display:none;">${Flag }</div>
	<table style="margin:10px;">
		<tr>
			<td><button class="input_button2" onclick="edit();" >查看/修改</button></td>
			<td><button class="input_button2" onclick="findClick()" >查询</button></td>
		</tr>
	</table>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="infoTable">
			<colgroup>
				<col width="3%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="17%">
			</colgroup>
			<tr class="font_1">
				<th style="display:none;">id</th>
				<th bgcolor="#eef6ff"></th>
				<th bgcolor="#eef6ff">账单编号</th>
				<th bgcolor="#eef6ff">账单状态</th>
				<th bgcolor="#eef6ff">应收省份</th>
				<th bgcolor="#eef6ff">应付省份</th>
				<th bgcolor="#eef6ff">代收货款合计</th>
				<th bgcolor="#eef6ff">创建人</th>
				<th bgcolor="#eef6ff">创建日期</th>
				<th bgcolor="#eef6ff">审核人</th>
				<th bgcolor="#eef6ff">审核日期</th>
				<th bgcolor="#eef6ff">备注</th>
			</tr>
			<%for(ExpressCodBill temp : infoList){ %>
				<tr>
					<td><input type="radio" name="selectRadio" value="<%=temp.getId()%>"/></td>
					<td><%=temp.getBillNo()%></td>
					<td value="<%=temp.getBillState() %>"><%=ExpressBillStateEnum.getTextByValue(temp.getBillState())%></td>
					<td><%=temp.getReceivableProvinceName() %></td>
					<td><%=temp.getPayableProvinceName() %></td>
					<td><%=temp.getCod() %>${list.cod}</td>
					<td><%=temp.getCreatorName() %></td>
					<td><%=temp.getCreateTime()==null?"":temp.getCreateTime()%></td>
					<td><%=temp.getAuditorName() %></td>
					<td><%=temp.getAuditTime()==null?"":temp.getAuditTime() %></td>
					<td><%=temp.getRemark() %></td>
					<td style="display:none;"><%=temp.getClosingDate()==null?"":temp.getClosingDate() %></td>
					<td style="display:none;"><%=temp.getCavTime()==null?"":temp.getCavTime()%></td>
					<td style="display:none;"><%=temp.getCavName()%></td>
				</tr>
			<%} %>
	</table>
	<div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1" id="pageid">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
					<a href="javascript:searchClick(1);">第一页</a> 
					<a href="javascript:searchClick(${page_obj.previous < 1 ? 1 : page_obj.previous});">上一页</a>
					<a href="javascript:searchClick(${page_obj.next < 1 ? 1 : page_obj.next});">下一页</a>
					<a href="javascript:searchClick(${page_obj.maxpage < 1 ? 1 : page_obj.maxpage});">最后一页</a>共${page_obj.maxpage}页 共${page_obj.total}条记录 当前第
					 <select id="selectPg" onchange="searchClick($(this).val());">
						<c:forEach begin="1" end="${page_obj.maxpage}" var="i">
							<c:if test="${page == i}">
								<option value="${i}" selected="true">${i}</option>
							</c:if>
							<c:if test="${page != i}">
								<option value="${i}">${i}</option>
							</c:if>
						</c:forEach>
					</select>页
				</td>
			</tr>
		</table>
	</div>
	
	<div>
		<div id="searchBox" class="easyui-dialog" title="查询条件"  style="width: 1000px; height: 250px; top:100px;left:100px; padding: 10px 20px;z-index:400px;" closed="true">
			<form id="searchFrom" action="" method="post" style="margin: 10px;">	
				<table width="100%" class="table1" style="padding:10px;line-height: 35px;;">
						<tr>
							<td class="tdcenter">账单编号:</td>
							<td colspan="3"><input type="text" name="billNo" id="billNo_id" class="input_text2" /></td>							
							<td class="tdcenter">账单状态:</td>
							<td colspan="3">
								<select name="billState" id="billState_id" class="select2" readonly="readonly">
									<%for(Map<String,Object> temp : billStatelist){ %>
										<option value="<%=temp.get("key")  %>"><%=temp.get("value")  %></option>
									<%} %>
								</select>
							</td>							
						</tr>
						<tr>
							<td class="tdcenter">账单创建日期:</td>
							<td><input type="text" name="creatStart" id="creatStart_id"   class="easyui-datebox"/></td>							
							<td class="tdcenter">到</td>
							<td><input type="text" name="creatEnd" id="creatEnd_id"  class="easyui-datebox"/></td>
							<td class="tdcenter">账单核销日期:</td>
							<td><input type="text" name="cavStart" id="cavStart_id"   class="easyui-datebox"/></td>							
							<td class="tdcenter">到</td>
							<td><input type="text" name="cavEnd" id="cavEnd_id"  class="easyui-datebox"/></td>
						</tr>
						<tr>
							<td class="tdcenter">应收省份:</td>
							<td colspan="3">
								<select name="provinceId" id="provinceId_id" class="select2" readonly="readonly">
									<%for(Map<String,Object> temp : provincelist){ %>
										<option value="<%=temp.get("key")  %>"><%=temp.get("value")  %></option>
									<%} %>
								</select>	
							</td>						
							<td class="tdcenter">排序:</td>
							<td colspan="2">
								<select name="sequenceField" id="sequenceField_id" class="select2" readonly="readonly">
									<option value="bill_no">账单批次</option>
									<option value="bill_state">账单状态</option>
									<option value="create_time">账单创建日期</option>
									<option value="cav_time">账单核销日期</option>
								</select>
							</td>
							<td>
								<select name="ascOrDesc" id="ascOrDesc_id" class="select2" readonly="readonly">
										<option value="ASC">升序</option>
										<option value="DESC">降序</option>
								</select>
							</td>							
						</tr>
						<tr>						
							<td colspan="4" class="tdrigth"><button  class="input_button2" onclick="searchClick(1)">查询</button></td>
							<td colspan="4" class="tdleft"><button id="searchBox_close" class="input_button2" onclick="closeClick(this)">关闭</button></td>
						</tr>
					</table>
				</form>
		</div>
	</div>
	<div id="updateBox" class="easyui-dialog" title="查看/修改"  style="width: 1050px; height: 450px; top:100px;left:100px; padding: 10px 20px;" closed="true">		
		<table style="width: 100%">
		    <tr>
			    <td>
				    <input class="input_button2" type="button"  id="updateBox_close" onclick="closeClick(this)" value="返回"/>
				    <input class="input_button2" type="button"  id="auditButton" onclick="auditOrCancalAuditOrVerificatedClick('audit')" value="审核"/>
				    <input class="input_button2" type="button"  id="cancalAuditButton" onclick="auditOrCancalAuditOrVerificatedClick('cancalAudit')" value="取消审核"/>
				    <input class="input_button2" type="button"  id="checkAuditButton" onclick="auditOrCancalAuditOrVerificatedClick('verificated')" value="核销"/>
		    	</td>
		    </tr>
		</table>
		<table width="95%" class="table1" style="padding:10px;line-height: 25px;;">
			<colgroup>
				<col width="13%">
				<col width="19%">
				<col width="13%">
				<col width="19%">
				<col width="17%">
				<col width="19%">
			</colgroup>
			<tr>
				<td class="tdcenter">账单编号:</td>
				<td><input type="text" id="updateBox_billNo_id" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>							
				<td class="tdcenter">账单状态:</td>
				<td><input type="text" id="updateBox_billState_id" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>							
				<td class="tdcenter">妥投审核截止日期:</td>
				<td><input type="text" id="updateBox_closingDate_id" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>							
			</tr>
			<tr>
				<td class="tdcenter">应收省份:</td>
				<td><input type="text" id="updateBox_provinceId_id" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>
				<td class="tdcenter">应付省份:</td>
				<td><input type="text" id="updateBox_payableProvinceId_id" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>
				<td class="tdcenter">代收货款合计：</td>
				<td><input type="text" id="updateBox_cod_id" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>
			</tr>
			<tr>
				<td class="tdcenter">创建日期:</td>
				<td><input type="text" id="updateBox_createTime_id" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>
				<td class="tdcenter">审核日期:</td>
				<td><input type="text" id="updateBox_updateBox_auditTime_id" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>
				<td class="tdcenter">核销日期：</td>
				<td><input type="text" id="updateBox_cavTime_id" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>
			</tr>
			<tr>
				<td class="tdcenter">创建人:</td>
				<td><input type="text" id="updateBox_createName_id" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>
				<td class="tdcenter">审核人:</td>
				<td><input type="text" id="updateBox_auditName_auditTime_id" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>
				<td class="tdcenter">核销人：</td>
				<td><input type="text" id="updateBox_cavName_id" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>
			</tr>
			<tr>
				<td style="vertical-align:middle; text-align:center;">备注：</td>
				<td colspan="5" ><textarea name="remark" id="updateBox_remark_id" style="width:100%;height:100px;margin-top:10px;" style="background:#EBEBE4;" disabled="true"></textarea></td>
				<td style="display:none;"><input type="text" name="id" id="updateBox_id_id" class="input_text1"/></td>
			</tr>
		</table>
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="alertinfoTable">
			<colgroup>
				<col width="20%">
				<col width="16%">
				<col width="16%">
				<col width="16%">
				<col width="16%">
				<col width="16%">
			</colgroup>
			<tr class="font_1">
				<th bgcolor="#eef6ff">运单号</th>
				<th bgcolor="#eef6ff">件数</th>
				<th bgcolor="#eef6ff">揽货员</th>
				<th bgcolor="#eef6ff">派件员</th>
				<th bgcolor="#eef6ff">代收货款</th>
				<th bgcolor="#eef6ff">揽件站点</th>
			</tr>						
		</table>
		<div>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1" id="alertpageid">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
					
					</td>
				</tr>
			</table>
		</div>
	</div>
	<form id="auditOrCavForm" action="" method="post" style="margin: 10px;display:none;">	
	</form>
	<form id="exportForm" method="post" action="">
		<input type="hidden" name="provincereceivablecodbillid" id="provincereceivablecodbillid"/>
		<input type="hidden" name="page" id="page" />
		<input type="hidden" name="receivable_province_name" id="receivable_province_name"/>
		<input type="hidden" name="payable_province_name" id="payable_province_name" />
	</form>
<input type="hidden" id="fatherURL" value="<%=request.getContextPath()%>/transProvincialPayableAuditReconciliationController/" /> 
<input type="hidden" id="UncleURL" value="<%=request.getContextPath()%>/transProvincialPayableReconciliationController/" /> 
<script type="text/javascript">
var searchflag = false;
$(function(){
	if($.trim($("#Flag").html()) == "true"){
		alert("操作成功");
	}else if($.trim($("#Flag").html()) == "false"){
		alert("操作失败");
	}
	$("#searchFrom").submit(function(){  		  
	      return searchflag;  		    
	});	
});

/*
 * 查询按钮逻辑
 */
function searchClick(page){
	if(!checkdate('creatStart') || !checkdate('creatEnd') || !checkdate('cavStart') || !checkdate('cavEnd')){
		return
	}
	var url = $("#fatherURL").val()+"search/"+page;
	searchflag = true;
	$("#searchFrom").attr('action',url);
	$('#searchFrom').submit();
}
/**
 * 关闭弹出框
 */
function closeClick(obj){
	if(obj.id.indexOf("search") >= 0){
		searchflag = false;
		$("#searchBox").dialog("close");
	}else if(obj.id.indexOf("update") >= 0){
		$("#updateBox").dialog("close");
	}
}
/*
 *点击查询按钮时，弹出条件输入界面
 */
function findClick(){
	$("#searchBox").dialog("open");
}

 //编辑按钮的点击触发函数
function edit(){
 	var radio_id = $("input[name='selectRadio']:checked");
	var id = radio_id.val();
	if(id == undefined){
		$.messager.alert("提示", "请选中要操作的数据 ！", "warning");
		return;
	}
	$("#updateBox_id_id").val(id);
	$("#updateBox_billNo_id").val(radio_id.parent().parent().find("td").eq(1).text());
	$("#updateBox_billState_id").val(radio_id.parent().parent().find("td").eq(2).text());
	$("#updateBox_closingDate_id").val(radio_id.parent().parent().find("td").eq(11).text());
	$("#updateBox_provinceId_id").val(radio_id.parent().parent().find("td").eq(3).text());
	$("#updateBox_payableProvinceId_id").val(radio_id.parent().parent().find("td").eq(4).text());
	$("#updateBox_cod_id").val(radio_id.parent().parent().find("td").eq(5).text());
	$("#updateBox_createTime_id").val(radio_id.parent().parent().find("td").eq(7).text());
	$("#updateBox_updateBox_auditTime_id").val(radio_id.parent().parent().find("td").eq(9).text());
	$("#updateBox_cavTime_id").val(radio_id.parent().parent().find("td").eq(12).text());
	$("#updateBox_createName_id").val(radio_id.parent().parent().find("td").eq(6).text());
	$("#updateBox_auditName_auditTime_id").val(radio_id.parent().parent().find("td").eq(8).text());
	$("#updateBox_cavName_id").val(radio_id.parent().parent().find("td").eq(13).text());
	$("#updateBox_remark_id").val(radio_id.parent().parent().find("td").eq(10).text());
	
	//加载表格数据
	alertPageContro(id,1);
	
	$("#updateBox").dialog("open");
 		
	//根据返回的账单状态显示弹出框的按钮
	//未审核
	var billState = radio_id.parent().parent().find("td").eq(2).attr("value")
	if(billState==0){
		$("#returnButton").show();
		$("#auditButton").show();
		$("#cancalAuditButton").hide();
		$("#checkAuditButton").hide();
	}
	//已审核
	if(billState==1){
		$("#returnButton").show();
		$("#auditButton").hide();
		$("#cancalAuditButton").show();
		$("#checkAuditButton").show();
	}
	//已核销
	if(billState==2){
		$("#returnButton").show();
		$("#auditButton").hide();
		$("#cancalAuditButton").hide();
		$("#checkAuditButton").hide();
	}	
} 
function alertPageContro(id,page){
	var alerturl = $("#UncleURL").val()+"getOrderByProvincereceivablecodbillid/"+page;
	$.ajax({
		type : "POST",
		url : alerturl,
		dataType : "json",
		data:{
			"provincereceivablecodbillid":id
		},
		success : function(data) {
			var alert_infoMap = data.alert_infoMap;
			var alert_page_obj = data.alert_page_obj;
			var alert_page = data.alert_page;
			var table = $('#alertinfoTable');
			for(var i = $("#alertinfoTable tr").length; i > 1; i--){
				$("#alertinfoTable tr").eq(i-1).remove(); 
			}
			//构建信息表
			buildTable(table,alert_infoMap);
			//构建分页表
			var pagetable = $('#alertpageid');
			var pagetd=$("#alertpageid tr:eq(0) td:eq(0)");
			var previousPage;
			var nextPage;
			var maxPage;
			if(alert_page_obj.previous < 1){
				previousPage = 1;
			}else{
				previousPage = alert_page_obj.previous;
			}
			if(alert_page_obj.next < 1){
				nextPage = 1;
			}else{
				nextPage = alert_page_obj.next;
			}
			if(alert_page_obj.maxpage < 1){
				maxPage = 1;
			}else{
				maxPage = alert_page_obj.maxpage;
			}
			var htmlText = "<a href='javascript:alertPageContro(" + id + ",1);'>第一页</a>";
			htmlText = htmlText + "<a href='javascript:alertPageContro(" + id +","+ previousPage +")'>上一页</a>";
			htmlText = htmlText + "<a href='javascript:alertPageContro(" + id +","+ nextPage +")'>下一页</a>";
			htmlText = htmlText + "<a href='javascript:alertPageContro(" + id +","+ maxPage + ")'>最后一页</a>共"; 
			htmlText = htmlText + maxPage;
			htmlText = htmlText + "页 共"; 
			htmlText = htmlText + alert_page_obj.total + "条记录 当前第";
			htmlText = htmlText + "<select id='selectPg' onchange='alertPageContro(" + id +", $(this).val())'>";
			var optioinText = "";
			for(var i = 1; i <= maxPage; i++){
				if(i == alert_page){
					optioinText = optioinText + "<option value='"+ i +"' selected='true'>" + i + "</option>";
				}else{
					optioinText = optioinText + "<option value='"+ i +"'>" + i + "</option>";
				}
			}
			htmlText = htmlText + optioinText;
			htmlText = htmlText + "</select>页 ";
			pagetd.html(htmlText);
		}
	});
}
function buildTable(table,alert_infoMap){
	for(var i = 0; i < alert_infoMap.length; i ++){  	 
		var row = $("<tr></tr>");
		var td1 = $("<td></td>");
		td1.html(alert_infoMap[i].orderNo);
		row.append(td1);
		table.append(row);
						
		var td2 = $("<td></td>");
		td2.html(alert_infoMap[i].number);
		row.append(td2);
		table.append(row);
		
		var td3 = $("<td></td>");
		td3.html(alert_infoMap[i].delivermanName);
		row.append(td3);
		table.append(row);
		
		var td4 = $("<td></td>");
		td4.html(alert_infoMap[i].delivername);
		row.append(td4);
		table.append(row);
		
		var td5 = $("<td></td>");
		td5.html(alert_infoMap[i].collection_amount);
		row.append(td5);
		table.append(row);
		
		var td6 = $("<td></td>");
		td6.html(alert_infoMap[i].instationname);
		row.append(td6);
		table.append(row);				
	}
}
function auditOrCancalAuditOrVerificatedClick(auditOrCancalAuditOrVerificated){
	var radio_id = $("input[name='selectRadio']:checked");
	var id = radio_id.val();
	if(id == undefined){
		$.messager.alert("提示", "请选中要操作的数据 ！", "warning");
		return;
	}
	
    $.messager.confirm("确认", "确认操作？", function (r) {  
        if (r) {  
        	var url = $("#fatherURL").val()+"auditOrCancalAuditOrVerificated?id=" + id + "&auditOrCancalAuditOrVerificated=" + auditOrCancalAuditOrVerificated;
        	$("#auditOrCavForm").attr('action',url);
        	$('#auditOrCavForm').submit();
        }  
    });    	
}
function checkdate(name){
	var reg = /^(\d{4})-(\d{2})-(\d{2})$/;  
	var str = $("input[name="+name+"]").val();  
	var arr = reg.exec(str);  
	if (str == "") {
		return true;  
	}
	if (!reg.test(str) || !RegExp.$2 <= 12 || !RegExp.$3 <= 31){  
		$.messager.alert("提示", "请保证输入的日期格式为yyyy-mm-dd或正确的日期!", "warning"); 
		return false;  
	}else{
		return true;
	} 	
} 
</script>	
</body>
</html>