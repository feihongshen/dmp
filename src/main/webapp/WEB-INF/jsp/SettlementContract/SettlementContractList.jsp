<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css" type="text/css"  />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="${pageContext.request.contextPath}/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="${pageContext.request.contextPath}/js/js.js"></script>
</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
		$('#CriteriaQueryButton').click(function(){
			getAddCriteriaQueryBox();
		});
	});
	
	
	
	function getAddCriteriaQueryBox(){
		$.ajax({
			type : "POST",
			data:'',
			url : $("#CriteriaQueryUrl").val(),
			dataType : "html",
			success : function(data) {
				$("#alert_box", parent.document).html(data);

			},
			complete : function() {
				addInit();
				viewBox();
			}
		});
	}	
	
	function addInit(){}
	
	
	
	
	</script>

	<div style="margin-top: 0.3cm;margin-left: 0.5cm">
		<button class="input_button2" id="add_button">新增</button>
		<button class="input_button2">查看/修改</button>
		<button class="input_button2">删除</button>
		<button class="input_button2" id="CriteriaQueryButton">查询</button>
	</div>
	<div style="margin-top: 0.5cm">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
			<tr class="font_1">
				<th><input type="checkbox"></th>
				<th>结算规则名称</th>
				<th>规则类型</th>
				<th>状态</th>
				<th>备注</th>
			</tr>
			
		</table>
	</div>
	
<input type="hidden" id="CriteriaQueryUrl" value="${pageContext.request.contextPath}/SettlementContract/CriteriaQuery">	
<input type="hidden" id="add" value="${pageContext.request.contextPath}/SettlementContract/AddSettlementContract">	

</body>
</html>