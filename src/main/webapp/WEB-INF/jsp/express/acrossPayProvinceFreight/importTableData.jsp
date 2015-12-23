<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/express/billOpePage.css" type="text/css"  />
</head>
<body>
<div id="table_div">
	<!-- 自动核查，若出现核对不上的运费，明细列表中的运单整行标红 -->
	<table width="120%" border="0" cellspacing="1" cellpadding="0" class="table_2" >
		<tr align="center">
			<c:if test="${reasonNeed > 0}">　　
				<th  width="100px;" align="center" valign="middle" style="font-weight: bold;" >未匹配原因</th>
	    	</c:if>
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
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">配送站点</th>
		</tr>
			<c:forEach items="${cwbListItem }" var="listItem">
		  <tr align="center" style="color:${listItem.trColor } ">
		  	<c:if test="${reasonNeed > 0}">　　
		   	 	<td width="100px;" align="center" valign="middle">${listItem.notMatchReason }</td>
	    	</c:if>
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
	<c:if test="${reasonNeed == 0}">　　
		<table width="100%">
		  <tr>
		    <td align="center">
		    	共${total}条${pageTotal}页　第${pageno}页
		    	<c:if test="${pageno > 1}">　　
		    	<a href="javascript:void(0);" onclick="refreshTableDataPage(1,${expressBill.id },${reasonNeed });">首页</a>　<a href="javascript:void(0);" onclick="refreshTableDataPage(${pageno-1 },${expressBill.id },${reasonNeed });">上页</a>
		    	</c:if>
		    	<c:if test="${pageno < pageTotal}">　<!-- cwbDetailInfo4EditPage(${pageno+1 }) -->
		    	<a href="javascript:void(0);" onclick="refreshTableDataPage(${pageno+1 },${expressBill.id },${reasonNeed });">下页</a>　<a href="javascript:void(0);" onclick="refreshTableDataPage(${pageTotal },${expressBill.id },${reasonNeed });">尾页</a>
		    	</c:if>
		    </td>
		  </tr>
		</table>
	</c:if>
	
</div>
<script type="text/javascript">
/**
 * 局部刷新表格中的数据[还没生效]
 */
function refreshTableDataPage(pageNo4Edit,billId,reasonNeed){
	if(billId==null||undefined==billId){
		billId = $("#eidtBillId").val();
	}
	var url =  App.ctx+"/acrossPayFreightCheck/switch2EditView4AcrossPayPart";
	if(reasonNeed>0){//核对之后的分页查询数据
		url =  App.ctx+"/acrossPayFreightCheck/verifyImportRecordsScan";
	}
	if(billId){
		$.ajax({
			type : "post",
			async : false, // 设为false就是同步请求
			url : url,
			data : {
				page:pageNo4Edit,
				billId:billId
			},
			datatype : "json",
			success : function(result) {
				if(result){
					$('#table_div').html("");
					$('#table_div').html(result);
				}
			}
		});
	}
}
</script>
</body>
</html>