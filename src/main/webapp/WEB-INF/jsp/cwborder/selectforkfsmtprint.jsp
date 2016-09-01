<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上门退打印模板</title>
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
	LODOP.PRINT_INIT("上门退打印模板");
	LODOP.SET_PRINT_PAGESIZE(0,0,0,"A4");
	for(var i = 0; i < ${fn:length(printVoList)}; i++) {
		LODOP.NewPage();
		var strBodyHtml = document.getElementById("printTable_" + i).outerHTML;
		strBodyHtml = strBodyHtml.replace("preview_box", "");
		LODOP.ADD_PRINT_HTM("10mm","10mm","RightMargin:10mm","BottomMargin:10mm", '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">' + strBodyStyle + "<body>" + strBodyHtml + "</body>");
		var cwb = $("#cwb_" + i).val();
		var addressNo = $("#addressNo_" + i).val();
		var boxTop = $("#box_" + i).offset().top;
		var cwbBarcodeTop = parseInt($("#cwbBarcode_" + i).offset().top);
		var cwbBarcodePositionTop = cwbBarcodeTop - boxTop + 5; // 微调5
		var backBarcodeTop = parseInt($("#backBarcode_" + i).offset().top);
		var backBarcodePositionTop = backBarcodeTop - boxTop + 30; // 微调
		LODOP.SET_PRINT_STYLEA(0,"FontSize",10);
		addBarcode(cwbBarcodePositionTop,"140mm","50mm","15mm","128Auto", cwb);
		addBarcode(backBarcodePositionTop,"140mm","50mm","15mm","128Auto",addressNo);
		var maxHeight = 520; // A4纸半页的高度为561,中间保留一定的宽度80，约20mm
		var boxHeight = $("#box_" + i).outerHeight();
		// A4纸高度为297mm，折半之后为148mm，再增加5mm预留高度，防止换页
		var box2Top = "153mm";
		if (maxHeight < boxHeight) { // 如果一页不能显示两联的话，就分页显示
			LODOP.NewPage();
			var box2Top = "10mm";
			addBarcode(cwbBarcodePositionTop,"140mm","50mm","15mm","128Auto",cwb);
			addBarcode(backBarcodePositionTop,"140mm","50mm","15mm","128Auto",addressNo);
		} else {
			addBarcode(cwbBarcodePositionTop + 540,"140mm","50mm","15mm","128Auto",cwb);
			addBarcode(backBarcodePositionTop + 540,"140mm","50mm","15mm","128Auto",addressNo);
		}
		LODOP.ADD_PRINT_HTM(box2Top,"10mm","RightMargin:10mm","BottomMargin:10mm", '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">' + strBodyStyle + "<body>" + strBodyHtml + "</body>");
	}
};

function addBarcode(top, left, width, height, type, barcode) {
	if (barcode != null && barcode != "") { // 防止出现空条形码
		LODOP.ADD_PRINT_BARCODE(top, left, width, height, type, barcode);
	}
}

function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print();
	}
}
</script>
<style type="text/css" id="print_style">
	.preview_box {
		border: 1px solid #585656;
		margin-bottom:5mm;
		padding:10mm;
		width:190mm;
	}
	.box {
		width:190mm;
	}
	.out_box {
		width:100%;
		word-break:break-all;
	}
	.box_fee {
		border: 1px solid #585656;
		padding:1mm;
	}
	.inner_box {
		width:100%;
		font-size: 12px;
	}
	.inner_box_2 {
		border-collapse: collapse;
        border: none;
		width:100%;
		font-size: 12px;
	}
	.inner_box_2 td {
		border: 1px solid #585656;
		padding:1mm;
	}
	.title {
		font-size: 16px;
		width: 100%;
		font-weight: bold;
	}
	.tip {
		font-size: 14px;
		font-weight: bold;
	}
</style>
</head>
<body>
	<a href="javascript:nowprint()">直接打印</a>
	<a href="javascript:prn1_preview()">预览</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:history.go(-1)">返回</a>
	<c:forEach var="vo" items="${printVoList }" varStatus="status">
		<c:set var="cwb" value="${vo.cwbOrder }"></c:set>
		<div id="printTable_${status.index}" class="preview_box">
			<input type="hidden" id="cwb_${status.index }" value="${cwb.cwb }">
			<input type="hidden" id="addressNo_${status.index }" value="${vo.smtCwb.returnNo }">
			<div id="box_${status.index}" class="box">
				<table class="out_box" cellpadding="0" cellspacing="0">
					<tr>
						<td align="center">
							<span class="title">${vo.customer.customername }上门揽退单</span>
						</td>
					</tr>
					<tr>
						<td style="height:20mm;">
							<div id="cwbBarcode_${status.index}"></div>
						</td>
					</tr>
					<tr>
						<td>
							<table class="inner_box" cellpadding="0" cellspacing="0">
								<tr>
									<td width="60%" valign="bottom">
										<div>联系人：${cwb.consigneename }</div>
										<div>联系电话：${vo.consigneemobile }</div>
										<div>取件地址：${cwb.consigneeaddress }</div>
									</td>
									<td class="box_fee" valign="bottom">
										<div>合计费用：${vo.totalfee }元  ${vo.cnTotalfee }</div>
										<div>运费：${cwb.shouldfare }元</div>
										<div>应收费用：${cwb.receivablefee }元</div>
										<div>应付费用：${cwb.paybackfee }元</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td style="height:1mm;"></td>
					</tr>
					<tr>
						<td>
							<table class="inner_box_2" cellpadding="0" cellspacing="0">
								<tr>
									<td width="10%" align="center">
										商品明细
									</td>
									<td>
										${cwb.backcarname }
									</td>
								<tr>
								<tr>
									<td align="center">
										订单备注
									</td>
									<td>
										${cwb.cwbremark }
									</td>
								<tr>
								<tr>
									<td align="center">
										客户要求
									</td>
									<td>
										${cwb.customercommand }
									</td>
								<tr>
							</table>
						</td>
					</tr>
					<tr>
						<td style="height:1mm;"></td>
					</tr>
					<tr>
						<td>
							<table class="inner_box" cellpadding="0" cellspacing="0">
								<tr>
									<td width="60%">
										退货地址：${vo.smtCwb.returnAddress }
									</td>
									<td>
										<div id="backBarcode_${status.index}"></div>
									</td>
								</tr>
								<tr>
									<td width="60%">
										商家售后热线：${vo.customer.customerphone }
									</td>
									<td>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td style="height:12mm;"></td>
					</tr>
					<tr>
						<td>
							<table class="inner_box" cellpadding="0" cellspacing="0">
								<tr>
									<td width="50%" style="padding-top:1mm;padding-bottom:1mm;">
										客户签字：
									</td>
									<td>
										快递员签字：
									</td>
								</tr>
								<tr>
									<td width="50%" style="padding-top:1mm;padding-bottom:1mm;">
										身份证号码：
									</td>
									<td>
										日期：
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td style="height:4mm;"></td>
					</tr>
					<tr>
						<td>
							<font class="tip">上门取件请务必核对清楚取回商品明细，如有退款请让客户签字确认收款！！！</font>
						</td>
					</tr>
					<tr>
						<td style="height:4mm;"></td>
					</tr>
					<tr>
						<td>
							<table class="inner_box" cellpadding="0" cellspacing="0">
								<tr>
									<td width="50%">
									</td>
									<td>
										打印日期：<fmt:formatDate value="${printTime }" pattern="yyyy年MM月dd日HH时mm分dd秒"/>
									</td>
							</table>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</c:forEach>
</body>
</html>