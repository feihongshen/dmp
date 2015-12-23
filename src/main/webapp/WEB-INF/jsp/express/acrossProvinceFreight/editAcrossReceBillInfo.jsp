<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/express/billOpePage.css" type="text/css"  />
</head>
<body>
<div>
	<input type="button" class="input_button1" id="backBtn" value="返回" onclick="closeEditBox_common();" />
	<input type="button" class="input_button1" id="createBtn" value="保存" onclick="saveEditInfoEdit();" />
</div>
<div>
	<input type="hidden" id="eidtBillId" value="${expressBill.id }" />
	<input type="hidden" id="billState_page" value="${expressBill.billState }" />
	<table>
		<tr>
			<td class="maxtd" align="right">账单编号</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.billNo }" /></td>
			<td class="maxtd" align="right">账单状态</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.billStateStr }" /></td>
			<td class="maxtd" align="right">应收省份</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.receProvinceName }" /></td>
			
		</tr>
		<tr>
			<td class="maxtd" align="right">应付省份</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.payProvinceName }" /></td>
			<td class="maxtd" align="right">运费金额合计</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.freight }" /></td>
			<td class="maxtd" align="right">代收货款合计</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.cod }" /></td>
			
		</tr>
		<tr>
			<td class="maxtd" align="right">创建日期</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.createTime }" /></td>
			<td class="maxtd" align="right">审核日期</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.auditTime }" /></td>
			<td class="maxtd" align="right">核销日期</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.cavTime }" /></td>
		</tr>
		<tr>
			<td class="maxtd" align="right">创建人</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.creatorName }" /></td>
			<td class="maxtd" align="right">审核人</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.auditorName }" /></td>
			<td class="maxtd" align="right">核销人</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.cavName }" /></td>
		</tr>
		<tr>
			<td class="maxtd" align="right">妥投审核截止日期</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.dealLineTimeName }" /></td>
			<td class="maxtd" align="right">签收截止日期</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.dealLineTime }" /></td>
			<td colspan="2"></td>
		</tr>
		
		<tr>
			<tr>
			<td class="maxtd" align="right" valign="top">备注</td>
			<td colspan="8">
				<textarea rows="3" cols="70" id="editrRemark" name="editrRemark" value="">${expressBill.remark }</textarea>
			</td>
		</tr>
		</tr>
	</table>
</div>
<div>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
		<tr align="center">
			<th height="30px" width="120px;" align="center" valign="middle" style="font-weight: bold;" >运单号</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">件数</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">揽件员</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">派件员</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">付款方式</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">运费合计</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">运费</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">包装费用</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">保价费用</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">代收货款</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">站点</th>
		</tr>
			<c:forEach items="${cwbListItem }" var="listItem">
		  <tr align="center">
		    <td height="30px" align="center" valign="middle">${listItem.orderNo }</td>
		    <td align="center" valign="middle">${listItem.orderCount }</td>
		    <td align="center" valign="middle">${listItem.deliveryMan }</td>
		    <td align="center" valign="middle">${listItem.sendMan }</td>
		    <td align="center" valign="middle">${listItem.payMethod }</td>
		    <td align="center" valign="middle">${listItem.transportFeeTotal }</td>
		    <td align="center" valign="middle">${listItem.transportFee }</td>
		    <td align="center" valign="middle">${listItem.packFee }</td>
		    <td align="center" valign="middle">${listItem.saveFee }</td>
		    <td align="center" valign="middle">${listItem.receivablefee }</td>
		    <td align="center" valign="middle">${listItem.branchName }</td>
		  </tr>
	  		</c:forEach>
	</table>
	<table width="100%">
	  <tr>
	    <td align="center">
	    	共${total}条${pageTotal}页　第${pageno}页
	    	<c:if test="${pageno > 1}">　　
	    	<a href="javascript:void(0);" onclick="cwbDetailInfo4EditPage(1,${expressBill.id });">首页</a>　<a href="javascript:void(0);" onclick="cwbDetailInfo4EditPage(${pageno-1 },${expressBill.id });">上页</a>
	    	</c:if>
	    	<c:if test="${pageno < pageTotal}">　<!-- cwbDetailInfo4EditPage(${pageno+1 }) -->
	    	<a href="javascript:void(0);" onclick="cwbDetailInfo4EditPage(${pageno+1 },${expressBill.id });">下页</a>　<a href="javascript:void(0);" onclick="cwbDetailInfo4EditPage(${pageTotal },${expressBill.id });">尾页</a>
	    	</c:if>
	    </td>
	  </tr>
	</table>
</div>

<script type="text/javascript">
$(function(){
	var billState = $("#billState_page").val();
	if(billState>0){
		$("#editrRemark").attr("disabled","disabled");
	}
});
function saveEditInfoEdit(){
	var bill_temp_id = $("#eidtBillId").val();
	var remark_temp = $("#editrRemark").val();
	if(bill_temp_id){
		if(undefined == remark_temp){
			remark_temp = "";
		}
		saveEditInfo(bill_temp_id,remark_temp);
	}
}
</script> 
</body>
</html>