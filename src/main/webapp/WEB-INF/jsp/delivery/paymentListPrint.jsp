<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>交款单打印</title>
	
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.8.0.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/layer.min.js"></script>
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/eap/sys/js/eapTools.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40//plug-in/tools/curdtools.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40//plug-in/tools/easyuiextend.js"></script>
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiple.select.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js"></script>
	
	<link type="text/css" href="<%=request.getContextPath()%>/css/2.css" rel="stylesheet"/>
    <link type="text/css" href="<%=request.getContextPath()%>/css/reset.css" rel="stylesheet"></link>
	<link type="text/css" href="<%=request.getContextPath()%>/css/index.css" rel="stylesheet"/>
	<link type="text/css" href="<%=request.getContextPath()%>/js/multiSelcet/multiple-select.css" rel="stylesheet"/>
    <link type="text/css" href="<%=request.getContextPath()%>/css/easyui/themes/default/easyui.css" rel="stylesheet" media="all"/>
	<link type="text/css" href="<%=request.getContextPath()%>/dmp40/plug-in/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
	<link id="skinlayercss" href="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/skin/layer.css" rel="stylesheet" type="text/css">
</head>
<body class="easyui-layout" leftmargin="0" topmargin="0">
    <div data-options="region:'center'" style="height:100%;overflow-x: auto; overflow-y: auto;">
		<table id="dg_rsList" class="easyui-datagrid" toolbar="#cwb_toolbar" nowrap="false"  fit="true" showFooter="true" fitColumns="false" rownumbers="true" singleSelect="false" width="100%">
				<thead>
					<tr>
						<th checkbox="true"></th>
						<th field="customerName" align="center" width="200">客户名称</th>
						<th field="cwbOrderType" align="center" width="100">订单类型</th>
						<th field="orderCount" align="right" width="100">订单数量</th>
						<th field="deliveryPaymentPattern" align="center" width="100">支付方式</th>
						<th field="shouldReceivedfeeStr" align="right" width="100">应收金额</th>
						<th field="shouldPaybackfeeStr" align="right" width="100">应退金额</th>
						<th field="shouldfareStr" align="right" width="100">应收运费</th>
						<th field="shouldTotalStr" align="right" width="100">应收合计</th>
						<th field="realTotalStr" align="right" width="100">实收合计</th>
						<th field="deliveryName" align="center" width="100">小件员</th>
					</tr>
				</thead>
		</table>
		<div id="cwb_toolbar">
			<div class="form-inline" style="padding:10px 0 0 10px">
			    <label style="width:250px;">
			    	<label style="width:70px;">小件员：</label>
			    	<select id="deliver" style="width:150px;">
			    		<option value="">请选择</option>
			    		<c:forEach var="deliver" items="${deliverList }">
			    			<option value="${deliver.userid }">${deliver.realname }</option>
			    		</c:forEach>
			    	</select>
			    </label>
			    <label style="width:500px;">
			    	<label style="width:100px;">归班审核时间：</label>
			    	<input type ="text" name ="auditingtimeStart" id="auditingtimeStart"  value="" readonly="readonly" style="background-color:#fff;width:150px;cursor:pointer" class="Wdate" 
			    		onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'#F{$dp.$DV(\'<fmt:formatDate value="${now }" pattern="yyyy-M-d"/> %H:%m:%s\', {d:-30})}', maxDate:'#F{$dp.$D(\'auditingtimeEnd\')||\'<fmt:formatDate value="${now }" pattern="yyyy-M-d"/> %H:%m:%s}\'}'})"/>
			    	<label>至</label>
			    	<input type ="text" name ="auditingtimeEnd" id="auditingtimeEnd"  value=""  readonly="readonly" style="background-color:#fff;width:150px;cursor:pointer" class="Wdate" 
			    		onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'#F{$dp.$D(\'auditingtimeStart\')||$dp.$DV(\'<fmt:formatDate value="${now }" pattern="yyyy-M-d"/> %H:%m:%s\', {d:-30})}', maxDate:'<fmt:formatDate value="${now }" pattern="yyyy-M-d"/> %H:%m:%s'})"/>
			    </label>
			    <label style="width:250px;">
			    	<label style="width:70px;">支付方式：</label>
			    	<select id="paymentType" style="width:150px;">
			    		<option value="0">全部</option>
			    		<c:forEach var="pay" items="${payArray }">
			    			<option value="${pay.payno }">${pay.payname }</option>
			    		</c:forEach>
			    	</select>
			    </label>
			</div>
			<div class="form-inline" style="padding:10px 0 0 10px">
			    <label>
			    	<div class="btn btn-default" style="margin-right:5px;" id= "search"><i class="icon-search"></i>查询</div>
			    	<div class="btn btn-default" style="margin-right:5px;" id= "print"><i class="icon-print"></i>打印</div>
			    </label>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$("#search").click(function() {
		var deliveryId = $("#deliver").val();
		if (deliveryId == null || deliveryId == "") {
			$.messager.alert("错误", "请选择小件员！", "error");
			return;
		}
		var auditingtimeStart = $("#auditingtimeStart").val();
		var auditingtimeEnd = $("#auditingtimeEnd").val();
		if(auditingtimeStart == null || auditingtimeStart == "") {
			if (auditingtimeEnd != null && auditingtimeEnd != "") {
				$.messager.alert("错误", "请选择归班审核开始时间！", "error");
				return;
			} else {
				auditingtimeStart = '<fmt:formatDate value="${now }" pattern="yyyy-MM-dd 00:00:00"/>';
				$("#auditingtimeStart").val(auditingtimeStart);
			}
		}
		//数据加载动画
		var layEle = layer.load("加载中...", 0);
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/delivery/getPaymentPrintList",
			data:{
				deliveryId:deliveryId, 
				auditingtimeStart:auditingtimeStart, 
				auditingtimeEnd:auditingtimeEnd,
				paymentType: $("#paymentType").val()
			},
			dataType : "json",
			success : function(data) {
				layer.close(layEle);
				if (data.status == 0) {
					var result = data.result;
					$('#dg_rsList').datagrid('loadData', result);//加载数据
				} else {
					$.messager.alert("错误", data.msg, "error");
				}
			},
			error: function() {
				layer.close(layEle);
				$.messager.alert("错误", "加载出现异常！", "error");
			}
		});
	});
</script>
</html>