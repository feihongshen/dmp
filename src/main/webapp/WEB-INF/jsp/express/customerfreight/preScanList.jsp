<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style type="text/css">
.div_level_pre{
	float:left;
	width: 250px;
	border: 1px solid #303030;
	height: 60px;
}
</style>
</head>

<body>
	<div>
		<div class="div_level_pre">
			<div class="div_20">
				<span class="span_red_no">&nbsp;&nbsp;&nbsp;&nbsp;${summary.countName } 单量</span><br/> 
				<span>&nbsp;</span><br/>
				<span id="monthPayOrderCount" class="span_red">${summary.count }</span>
			</div>
			<div class="div_30">
				<span class="span_red_no">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${summary.feeName } 总金额</span>
				<br/> <span>&nbsp;</span><br/>
				<span id="monthPayTransFee" class="span_red">${summary.fee }</span>
			</div>
		</div>
		<br/>
	</div>
	
	<div style="margin-top: 10px;">
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
			  </tr>
		  		</c:forEach>
		</table>
		<table width="100%">
		  <tr>
		    <td align="center">
		    	共${total}条${pageTotal}页　第${pageno}页
		    	<c:if test="${pageno > 1}">　　
		    	<a href="javascript:void(0);" onclick="preScanPageingFun(1);">首页</a>　<a href="javascript:void(0);" onclick="preScanPageingFun(${pageno-1 } );">上页</a>
		    	</c:if>
		    	<c:if test="${pageno < pageTotal}">　
		    	<a href="javascript:void(0);" onclick="preScanPageingFun(${pageno+1 });">下页</a>　<a href="javascript:void(0);" onclick="preScanPageingFun(${pageTotal });">尾页</a>
		    	</c:if>
		    </td>
		  </tr>
		</table>
	</div>
	<br/>
	<div align="center" style="margin-bottom: 10px;">
		<input type="button" class="input_button1" id="generateBill" value="生成账单"  onclick="generateBillOpe4Customer();"/> 
		<input type="button" class="input_button1" id="cancleCreate" value="取消" onclick="cancleCreateBill_common();"/>
	</div>
	
	<input type="hidden" id="billNo4PreScan" value="${createParams.billNo4Create}"></input>
	<input type="hidden" id="customerId4PreScan" value="${createParams.customerId4Create}"></input>
	<input type="hidden" id="payMethod4PreScan" value="${createParams.payMethod4Create}"></input>
	<input type="hidden" id="deadLineTime4PreScan" value="${createParams.deadLineTime4Create}"></input>
	<input type="hidden" id="remark4PreScan" value="${createParams.remark4Create}"></input>
	<input type="hidden" id="branchId4PreScan" value="${createParams.branchId4Create }"></input>
	<script src="<%=request.getContextPath()%>/js/express/customerfreight/preScanList.js" type="text/javascript"></script>
	<script>
		function preScanPageingFun(pageNo_temp){
			var tempParams = {
				page:pageNo_temp,	
				billNo4Create:$("#billNo4PreScan").val(),
				customerId4Create:$("#customerId4PreScan").val(),
				payMethod4Create:$("#payMethod4PreScan").val(),
				deadLineTime4Create:$("#deadLineTime4PreScan").val(),
				remark4Create:$("#remark4PreScan").val(),
				branchId4Create:$("#branchId4PreScan").val()
			}
			cwbDetailInfoPreScanPage(tempParams);
		}
	</script>
</body>
</html>
