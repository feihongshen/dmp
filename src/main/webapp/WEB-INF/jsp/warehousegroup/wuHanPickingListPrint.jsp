<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	var strBodyHtml = document.getElementById("printTable").outerHTML;
	strBodyHtml = strBodyHtml.replace("preview_box", "");
	LODOP.ADD_PRINT_HTM("0mm","0mm","RightMargin:0mm","BottomMargin:0mm", '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">' + strBodyStyle + "<body>" + strBodyHtml + "</body>");
};

function setcreowg(){
	var operatetype = <%=OutwarehousegroupOperateEnum.FenZhanLingHuo.getValue()%>;
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/warehousegroup/creowg/${nextbranchid}",
		data : {cwbs:"${huizongcwbs }", driverid: "${deliverid}", customerid: "${customerid}", operatetype:operatetype, "strtime":"${strtime}", "endtime": "${endtime}"},
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
		font-size: 11px;
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
	.cwb th {
		font-size: 11px;
		font-weight: normal;
		text-align:left;
		border-bottom:1px dashed #585656;
	}
	.cwb td {
		font-size: 10px;
		font-weight: normal;
		text-align:left;
		vertical-align: top;
		word-break:break-all;
	}
</style>
</head>
<body>
	<a href="javascript:nowprint()">直接打印</a>
	<a href="javascript:prn1_preview()">预览</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:history.go(-1)">返回</a>
	<div id="printTable">
		<div class="out_box preview_box">
			<div class="inner_box">
				<div class="title">湖北品骏领货签收单</div>
				<div class="content_1">
					<span>站点：${branchname }</span>
					<span style="word-spacing:1mm;">&nbsp;</span>
					<span>快递员：${delivername }</span>
					<span style="word-spacing:1mm;">&nbsp;</span>
					<span>第1/1页</span>
				</div>
				<div class="content_1">
					<span>打印：${username }</span>
					<span style="word-spacing:1mm;">&nbsp;</span>
					<span>时间：<fmt:formatDate value="${printTime }" pattern="yyyy-MM-dd HH:mm:dd"/></span>
				</div>
				<div class="dashed"></div>
				<div>
					<table class="cwb" width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<th width="10%">序号</th>
							<th width="45%">条码</th>
							<th width="45%">条码</th>
						</tr>
						<c:forEach var="cwbArray" items="${cwbArrayList }" varStatus="status">
							<tr>
								<td>${status.index + 1 }</td>
								<c:forEach var="cwb" items="${cwbArray }">
									<td>${cwb }</td>
								</c:forEach>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div class="dashed"></div>
				<div class="content_1">本次领货时间：<br>${strtime } 至${endtime }</div>
				<div class="content_1">
					<span>共${ordernum }单</span>
					<span style="word-spacing:1mm;">&nbsp;</span>
					<span>代收货款合计${receivablefeeTotal }元</span>
					<span style="word-spacing:1mm;">&nbsp;</span>
					<span>第1/1页</span>
				</div>
				<div class="dashed"></div>
				<div class="content_1">
					<div class="float_left" style="width:35mm">
						站长助理签字：
						<br>
						<br>
						<fmt:formatDate value="${printTime }" pattern="yyyy"/>年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日
					</div>
					<div class="float_left">
						投递员签字：
						<br><br>
						<fmt:formatDate value="${printTime }" pattern="yyyy"/>年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日
					</div>
					<div style="clear:both;"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>