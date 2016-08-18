<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@page import="cn.explink.enumutil.OutwarehousegroupOperateEnum"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>武汉运输交接单打印</title>
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
	LODOP.PRINT_INIT("武汉运输交接单打印");
	for(var i = 0; i < ${fn:length(printList)}; i++) {
		LODOP.NewPage();
		var strBodyHtml = document.getElementById("printTable_" + i).outerHTML;
		strBodyHtml = strBodyHtml.replace("preview_box", "");
		LODOP.ADD_PRINT_HTM("0mm","0mm","RightMargin:0mm","BottomMargin:0mm", '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">' + strBodyStyle + "<body>" + strBodyHtml + "</body>");
	}
};

function setcreowg(){
	var operatetype = <%=OutwarehousegroupOperateEnum.ChuKu.getValue()%>;
	var baleno="";
	var baleid="${baleid }";
	if($("#baleno").length>0){
		baleno=$("#baleno").text();
	}
	 $.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/warehousegroup/creowgnew",
			data : {"cwbs":"${cwbs }","operatetype":operatetype,"driverid": ${deliverid },"baleno":baleno,"baleid":baleid, "strtime":"${strtime}", "endtime": "${endtime}"},
			dataType:"json",
			success : function(data) {
				if(data.errorCode==0){
					prn1_print();
				}
			},error: function(XMLHttpRequest, textStatus, errorThrown) {
	            alert(XMLHttpRequest.status+"---"+XMLHttpRequest.responseText);
	        }
		});
 
}

function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		setcreowg();
	}
}
</script>
<style type="text/css" id="print_style">
	.preview_box {
		border: 1px solid #585656;
	}
	.out_box {
		width: 75mm;
	}
	.inner_box {
		margin: 5mm;
	}
	.title {
		font-size: 14px;
		width:100%;
		text-align:center;
	}
	.content_1 {
		font-size: 12px;
	}
	.content_2 {
		font-size: 10px;
		font-weight: bold;
		padding-left: 3mm;
		margin-top:1mm;
	}
	.content_3 {
		font-size: 10px;
		margin-left: 3mm;
		margin-top:1mm;
	}
	.dashed {
		border-top:1px dashed #585656;
		height: 1px;
		overflow:hidden;
		margin-top:1mm;
		margin-bottom:1mm;
	}
	.underline {
		border-bottom:1px solid #585656;
	}
	.square {
		width: 10px;
		height: 10px; 
		border:1px solid #585656;
		margin-left:1mm;
		margin-right:1mm;
	}
	.float_left {
		float:left;
	}
	.float_right {
		float:right;
	}
</style>
</head>
<body>
	<a href="javascript:nowprint()">直接打印</a>
	<a href="javascript:prn1_preview()">预览</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:history.go(-1)">返回</a>
	<c:forEach var="vo" items="${printList }" varStatus="status">
	<div id="printTable_${status.index }">
		<div class="out_box preview_box">
			<div class="inner_box">
				<div class="title">（${vo.branchname }）至（${vo.nextBranchname }）运输交接单</div>
				<div class="dashed"></div>
				<div class="content_1">出库时间：${vo.outstockStartTime } 至 ${vo.outstockEndTime }</div>
				<div class="content_1">出库：共${vo.outstockOrderNum }单${vo.outstockSendNum }件（其中单独交接0单0件）</div>
				<div class="dashed"></div>
				<div class="content_2">分拣中心填写区：</div>
				<div class="content_3">车辆封签码：<span class="underline" style="word-spacing:40.5mm;">&nbsp;</span></div>
				<div class="content_3">异常说明：<span class="underline" style="word-spacing:43mm;">&nbsp;</span></div>
				<div class="dashed"></div>
				<div class="content_2">运输部填写区：</div>
				<div class="content_3">
					<span class="float_left">封签码与车辆是否一致：</span>
					<div class="float_right" style="width:8mm;">&nbsp;</div>
					<span class="float_right">否</span>
					<div class="square float_right"></div>
					<div class="float_right" style="width:2mm;">&nbsp;</div>
					<span class="float_right">是</span>
					<div class="square float_right"></div>
					<div style="clear:both;"></div>
				</div>
				<div class="content_3">
					<span class="float_left">封签码是否完好：</span>
					<div class="float_right" style="width:8mm;">&nbsp;</div>
					<span class="float_right">否</span>
					<div class="square float_right"></div>
					<div class="float_right" style="width:2mm;">&nbsp;</div>
					<span class="float_right">是</span>
					<div class="square float_right"></div>
					<div style="clear:both;"></div>
				</div>
				<div class="content_3">
					<span>车辆到站时间：</span>
					<span style="word-spacing:7mm;">&nbsp;</span>
					<span>时</span>
					<span style="word-spacing:7mm;">&nbsp;</span>
					<span>分</span>
				</div>
				<div class="content_3">
					<span>开始卸货时间：</span>
					<span style="word-spacing:7mm;">&nbsp;</span>
					<span>时</span>
					<span style="word-spacing:7mm;">&nbsp;</span>
					<span>分</span>
				</div>
				<div class="content_3">
					<span>车辆离站时间：</span>
					<span style="word-spacing:7mm;">&nbsp;</span>
					<span>时</span>
					<span style="word-spacing:7mm;">&nbsp;</span>
					<span>分</span>
				</div>
				<div class="content_3">
					<span class="float_left">差异处理：现场寻找差异</span>
					<div class="float_right" style="width:8mm;">&nbsp;</div>
					<span class="float_right">否</span>
					<div class="square float_right"></div>
					<div class="float_right" style="width:2mm;">&nbsp;</div>
					<span class="float_right">是</span>
					<div class="square float_right"></div>
					<div style="clear:both;"></div>
				</div>
				<div class="content_3">
					<div class="float_left" style="width:13mm;">&nbsp;</div>
					<span class="float_left">是否逐单二次扫描</span>
					<div class="float_right" style="width:8mm;">&nbsp;</div>
					<span class="float_right">否</span>
					<div class="square float_right"></div>
					<div class="float_right" style="width:2mm;">&nbsp;</div>
					<span class="float_right">是</span>
					<div class="square float_right"></div>
					<div style="clear:both;"></div>
				</div>
				<div class="content_3">
					<span>实收《缴款单》</span>
					<span class="underline" style="word-spacing:10mm;">&nbsp;</span>
					<span>袋</span>
				</div>
				<div class="content_3">
					异常备注：<span class="underline" style="word-spacing:43mm;">&nbsp;</span>
				</div>
				<div class="content_3">
					<span class="underline" style="word-spacing:56.5mm;">&nbsp;</span>
				</div>
				<div class="content_3">
					<span>交接人签字：</span>
					<span class="underline" style="word-spacing:10mm;">&nbsp;</span>
					<span>时间：&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日&nbsp;&nbsp;&nbsp;&nbsp;时&nbsp;&nbsp;&nbsp;&nbsp;分</span>
				</div>
				<div class="dashed"></div>
				<div class="content_2">配送站填写区：</div>
				<div class="content_3">车辆封签码：<span class="underline" style="word-spacing:40.5mm;">&nbsp;</span></div>
				<div class="content_3">
					<span class="float_left">封签码与车辆是否一致：</span>
					<div class="float_right" style="width:8mm;">&nbsp;</div>
					<span class="float_right">否</span>
					<div class="square float_right"></div>
					<div class="float_right" style="width:2mm;">&nbsp;</div>
					<span class="float_right">是</span>
					<div class="square float_right"></div>
					<div style="clear:both;"></div>
				</div>
				<div class="content_3">
					<span class="float_left">封签码是否完好：</span>
					<div class="float_right" style="width:8mm;">&nbsp;</div>
					<span class="float_right">否</span>
					<div class="square float_right"></div>
					<div class="float_right" style="width:2mm;">&nbsp;</div>
					<span class="float_right">是</span>
					<div class="square float_right"></div>
					<div style="clear:both;"></div>
				</div>
				<div class="content_3">
					<span class="float_left">司机全程监督卸货扫描：</span>
					<div class="float_right" style="width:8mm;">&nbsp;</div>
					<span class="float_right">否</span>
					<div class="square float_right"></div>
					<div class="float_right" style="width:2mm;">&nbsp;</div>
					<span class="float_right">是</span>
					<div class="square float_right"></div>
					<div style="clear:both;"></div>
				</div>
				<div class="content_3">
					<span class="float_left">差异（不勾选视为无差异）：</span>
					<div class="float_right" style="width:8mm;">&nbsp;</div>
					<span class="float_right">否</span>
					<div class="square float_right"></div>
					<div class="float_right" style="width:2mm;">&nbsp;</div>
					<span class="float_right">是</span>
					<div class="square float_right"></div>
					<div style="clear:both;"></div>
				</div>
				<div class="content_3">
					<span>返回《缴款单》</span>
					<span class="underline" style="word-spacing:10mm;">&nbsp;</span>
					<span>袋</span>
				</div>
				<div class="content_3">
					异常备注：<span class="underline" style="word-spacing:43mm;">&nbsp;</span>
				</div>
				<div class="content_3">
					<span class="underline" style="word-spacing:56.5mm;">&nbsp;</span>
				</div>
				<div class="content_3">
					<span>交接人签字：</span>
					<span class="underline" style="word-spacing:10mm;">&nbsp;</span>
					<span>时间：&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日&nbsp;&nbsp;&nbsp;&nbsp;时&nbsp;&nbsp;&nbsp;&nbsp;分</span>
				</div>
			</div>
		</div>
	</div>
	</c:forEach>
</body>
</html>