<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@page import="cn.explink.enumutil.OutwarehousegroupOperateEnum"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>武汉领货交接单打印</title>
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
<param name="CompanyName" value="北京易普联科信息技术有限公司" />
<param name="License" value="653717070728688778794958093190" />
<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 companyname="北京易普联科信息技术有限公司" license="653717070728688778794958093190"></embed>
</object>
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
var LODOP; //声明为全局变量 
function prn1_preview() {	
	createOneFormPage();           
	LODOP.PREVIEW();	
};
function prn1_print() {		
	createOneFormPage();
	LODOP.PRINT();	
};

function createOneFormPage(){
	LODOP = getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));                      
	var strBodyStyle = document.getElementById("print_style").outerHTML;
	LODOP.PRINT_INIT("武汉领货交接单打印");
	var printSize = $("#printSize").val();
	for (var i = 0; i < printSize;i++) {
		LODOP.NewPage();
		var strBodyHtml = document.getElementById("printTable").outerHTML;
		strBodyHtml = strBodyHtml.replace("preview_box", "");
		LODOP.ADD_PRINT_HTM("0mm","0mm","RightMargin:0mm","BottomMargin:0mm", '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">' + strBodyStyle + "<body>" + strBodyHtml + "</body>");
	}
};

function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print();
	}
}

$(function() {
	$("#printSize").change(function() {
		var $this = $(this);
		var printSize = $this.val();
		if (!(/(^[1-9]\d*$)/.test(printSize))) {
			alert("请输入正整数！");
			$this.val("1");
			return;
		}
	});
});
</script>
<style type="text/css" id="print_style">
	.preview_box {
		border: 1px solid #585656;
	}
	.out_box {
		width: 75mm;
	}
	.inner_box {
		left-margin : 3mm;
		right-margin : 9 mm;
	}
	.title {
		font-size: 14px;
		width:84%;
		text-align:center;
		word-wrap: break-word;
		word-break: normal;
	}
	.content_1 {
		font-size: 12px;
	}
	.dashed {
		border-top:1px dashed #585656;
		height: 1px;
		overflow:hidden;
		margin-top:1mm;
		margin-bottom:1mm;
	}
	.float_left {
		float:left;
	}
	.float_right {
		float:right;
	}
	.cwb th {
		font-size: 12px;
		font-weight: normal;
		border-bottom:1px dashed #585656;
	}
	.cwb td {
		font-size: 12px;
		font-weight: normal;
		word-break:break-all;
	}
</style>
</head>
<body>
	<a href="javascript:nowprint()">直接打印</a>
	<a href="javascript:prn1_preview()">预览</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<a href="javascript:history.go(-1)">返回</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	打印<input id="printSize" type="text" style="width:25px;" value="1">份
	<br><br>
	<div id="printTable">
		<div class="out_box preview_box">
			<div class="inner_box">
				<div class="title">湖北品骏（${branchname }）站交款单</div>
				<div class="content_1">
					<span>打印：${printUsername}</span>
					<span style="word-spacing:1mm;">&nbsp;</span>
					<span><fmt:formatDate value="${printTime }" pattern="yyyy-MM-dd HH:mm:dd"/></span>
				</div>
				<div class="content_1">
					交款：${auditingtimeStart } - ${auditingtimeEnd }
				</div>
				<div class="content_1">
					${deliveryBranchName }：${deliveryName }
				</div>
				<div class="dashed"></div>
				<c:forEach var="printVo" items="${deliverPaymentPrintVoList }">
					<div class="content_1">
						<table class="cwb" width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<th width="30%" align="left">业务	</th>
								<th width="20%" align="left">订单类型</th>
								<th width="10%" align="right">数量</th>
								<th width="20%" align="right">应收合计</th>
								<th width="20%" align="right">实际合计</th>
							</tr>
							<c:forEach var="reportVo" items="${printVo.deliverPaymentReportVoList }">
								<tr>
									<td align="left">${reportVo.customerName }</th>
									<td align="left">${reportVo.cwbOrderType }</th>
									<td align="right">${reportVo.orderCount }</th>
									<td align="right">${reportVo.shouldTotal }</th>
									<td align="right">${reportVo.realTotal }</th>
								</tr>
							</c:forEach>
						</table>				
					</div>
					<div class="dashed"></div>
					<div class="content_1">
						<span>${printVo.deliveryPayment.payname }</span>
						<span style="word-spacing:1mm;">&nbsp;</span>
						<span>共${printVo.orderCount }单</span>
						<span style="word-spacing:1mm;">&nbsp;</span>
						<span>应收：${printVo.shouldTotal }</span>
						<span style="word-spacing:1mm;">&nbsp;</span>
						<span>实收：${printVo.realTotal }</span>
					</div>
					<div class="dashed"></div>
				</c:forEach>
				<div class="content_1">
					投递员：
				</div>
				<div class="content_1">
					<div class="float_right">
						<div class="float_left">时间：</div>
						<div class="float_left" style="width:25px;">&nbsp;</div>
						<div class="float_left">年</div>
						<div class="float_left" style="width:20px;">&nbsp;</div>
						<div class="float_left">月</div>
						<div class="float_left" style="width:20px;">&nbsp;</div>
						<div class="float_left">日</div>
						<div style="clear:both;"></div>
					</div>
					<div style="clear:both;"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>