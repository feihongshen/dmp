<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>面单打印</title>
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
	CreateOneFormPage();           
	LODOP.PREVIEW();	
};
function prn1_print() {		
	CreateOneFormPage();
	LODOP.PRINT();	
};

function CreateOneFormPage(){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));                      
	var strBodyStyle=document.getElementById("print_style").outerHTML;
	LODOP.PRINT_INIT("面单打印");
	//LODOP.SET_PRINT_PAGESIZE(0,815,1500,"A4");
	for(var i = 0; i < ${fn:length(printOrderLabelVoList)}; i++) {
		LODOP.NewPage();
		LODOP.ADD_PRINT_HTM("2.5mm","10mm","RightMargin:0mm","BottomMargin:0mm", strBodyStyle + "<body>" + document.getElementById("printTable_" + i).outerHTML + "</body>");
		var transcwb = $("#transcwb_" + i).val();
		if(transcwb != null && transcwb != "") {
			LODOP.ADD_PRINT_BARCODE(108,53,150,40,"128Auto",transcwb);
			LODOP.SET_PRINT_STYLEA(0,"FontSize",10);
			LODOP.ADD_PRINT_BARCODE(224,67,150,40,"128Auto",transcwb);
			LODOP.SET_PRINT_STYLEA(0,"FontSize",10);
		}
	}
};

function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print();
	}
}
</script>
<style type="text/css" id="print_style">
.box {
	width: 80mm;
}
.box table {
	border-collapse: collapse; 
	border: none; 
}
.box table tr .td_1 {
	border: 1px solid #585656;
}

.box table tr .td_1 table tr td .barcode {
	height: 10mm;
	width: 38mm;
	border-top-width: 1px;
	border-right-width: 1px;
	border-bottom-width: 1px;
	border-left-width: 1px;
	border-top-style: solid;
	border-right-style: solid;
	border-bottom-style: solid;
	border-left-style: solid;
}

.inner_table {
	word-break:break-all;
}

.inner_table td div{
	padding-left: 5px;
	padding-right: 5px;
}
.label-default {
	background-color: #262537;
	color : #fff;
}
</style>
</head>
<body>
	<a href="javascript:nowprint()">直接打印</a>
	<a href="javascript:prn1_preview()">预览</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:history.go(-1)">返回</a>
	<c:forEach var="vo" items="${printOrderLabelVoList }" varStatus="status">
	<c:set var="cwb" value="${vo.cwbOrder }"/>
	<c:set var="branch" value="${vo.branch }"/>
	<input type="hidden" id="transcwb_${status.index }" value="${cwb.transcwb }">
	<div id="printTable_${status.index}">
		<div class="box">
			<table style="margin:2mm 2mm 0 2mm;width: 76mm" cellpadding="0" cellspacing="0">
				<tr>
			      <td class="td_1" style="height: 21.7mm;" align="center" valign="middle">
			      </td>
			    </tr>
			    <tr>
			    	<td class="td_1" style="height: 15.3mm;">
			    		<table class="inner_table" width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
			    			<tr>
			    				<td style="border-right:1px solid #585656;width:42mm;">
            					</td>
			    				<td style="width:4mm;" valign="middle">
			    					<div style="font-family: 黑体;font-weight: bold;font-size: 14px;">寄<br>件</div>
			    				</td>
			    				<td valign="middle">
			    					<div style="line-height:10px;font-family: 黑体;font-size: 8px;padding-left: 0px;padding-right: 2px;">
				    					<font style="font-size: 10px;">${cwb.senderprovince }${cwb.sendercity }${cwb.sendercounty }</font>${cwb.senderaddress }
				    					<br>
				    					${cwb.sendername }  <c:choose><c:when test="${empty cwb.sendercellphone }">${cwb.sendertelephone }</c:when><c:otherwise>${cwb.sendercellphone }</c:otherwise></c:choose>
			    					</div>
			    				</td>
			    			</tr>
			    		</table>
			    	</td>
			    </tr>
			    <tr>
			    	<td class="td_1" style="height: 4mm;">
			    		<table class="inner_table" width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
			    			<tr>
			    				<td style="width:42mm;">
			    					<div style="font-family: 黑体;font-size: 8px;">收件人：${cwb.consigneename }  <c:choose><c:when test="${empty cwb.consigneemobile }">${cwb.consigneephone }</c:when><c:otherwise>${cwb.consigneemobile }</c:otherwise></c:choose></div>
			    				</td>
			    				<td>
			    					<div style="font-family: 黑体;font-size: 8px;">订单号：${cwb.cwb }</div>
			    				</td>
			    			</tr>
			    		</table>
			    	</td>
			    </tr>
			    <tr>
			    	<td class="td_1" style="height: 11.3mm;">
			    		<table class="inner_table" width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
			    			<tr>
			    				<td style="width:4mm;" valign="middle">
			    					<div style="font-family: 黑体;font-weight: bold;font-size: 14px;">收<br>件</div>
			    				</td>
			    				<td>
			    					<div><span style="line-height:10px;font-family: 黑体;font-weight: bold;font-size: 10px;">${cwb.cwbprovince }${cwb.cwbcity }${cwb.cwbcounty }</span><span style="line-height:10px;font-family: 黑体;font-size: 8px;">${cwb.consigneeaddress }</span></div>
			    				</td>
			    			</tr>
			    		</table>
			    	</td>
			    </tr>
			    <tr>
			    	<td class="td_1" style="height: 25.9mm;">
			    		<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
			    			<tr>
			    				<td style="border-right:1px solid #585656; width:50mm;">
			    					<table class="inner_table" width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
			    						<tr>
			    							<td style="border-bottom:1px solid #585656;height:14.6mm;" colspan="2">&nbsp;
			    							</td>
			    						</tr>
			    						<tr>
			    							<td style="width:4mm;" valign="middle">
						    					<div style="font-family: 黑体;font-weight: bold;font-size: 14px;">收<br>件</div>
						    				</td>
			    							<td>
			    								<div style="line-height:10px;font-family: 黑体;font-size: 8px;">
				    								<font style="line-height:10px;font-family: 黑体;font-weight: bold;font-size: 10px;">${cwb.cwbprovince }${cwb.cwbcity }${cwb.cwbcounty }</font>${cwb.consigneeaddress }
				    								<br>
				    								${cwb.consigneename }  <c:choose><c:when test="${empty cwb.consigneemobile }">${cwb.consigneephone }</c:when><c:otherwise>${cwb.consigneemobile }</c:otherwise></c:choose>
			    								</div>
			    							</td>
			    						</tr>
			    					</table>
			    				</td>
			    				<td>
			    					<table class="inner_table" width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
			    						<tr>
			    							<td style="border-bottom:1px solid #585656;height:11.8mm;width:2mm;">
			    								<div style="font-family: 黑体;font-size: 10px;">目<br>的<br>地</div>
			    							</td>
			    							<td style="border-bottom:1px solid #585656;">
			    								<c:choose>
				           							<c:when test="${fn:length(vo.destination) gt 4 }">
				           								<div style="font-family: 黑体;font-size: 12px;padding-left: 0px;padding-right: 2px;">${vo.destination }</div>
				           							</c:when>
				           							<c:otherwise>
				           								<div style="font-family: 黑体;font-size: 18px;padding-left: 0px;padding-right: 2px;">${vo.destination }</div>
				           							</c:otherwise>
				           						</c:choose>
			    							</td>
			    						</tr>
			    						<tr>
			    							<td colspan="2" align="center" valign="middle">
			    								<c:choose>
				           							<c:when test="${fn:length(branch.branchcode) le 4 }">
				           								<div style="font-family: 黑体;font-weight: bold;font-size: 40px;">${branch.branchcode }</div>
				           							</c:when>
				           							<c:when test="${fn:length(branch.branchcode) le 5 }">
				           								<div style="font-family: 黑体;font-weight: bold;font-size: 30px;">${branch.branchcode }</div>
				           							</c:when>
				           							<c:when test="${fn:length(branch.branchcode) le 6 }">
				           								<div style="font-family: 黑体;font-weight: bold;font-size: 25px;">${branch.branchcode }</div>
				           							</c:when>
				           							<c:otherwise>
				           								<div style="font-family: 黑体;font-weight: bold;font-size: 20px;">${branch.branchcode }</div>
				           							</c:otherwise>
				           						</c:choose>
			    							</td>
			    						</tr>
			    					</table>
			    				</td>
			    			</tr>
			    		</table>
			    	</td>
			    </tr>
			    <tr>
			    	<td class="td_1" style="height: 16.2mm;">
			      		<table class="inner_table" width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
			        		<tr>
			          			<td valign="middle" style="width:25mm;">
					          		<div style="line-height:12px;font-family: 黑体;font-size: 8px;padding: 0 0 0 5px;">
					          			服务类型：
					          			<c:choose>
					          				<c:when test="${cwb.expressProductType eq 1 }">标准</c:when>
					          				<c:when test="${cwb.expressProductType eq 2 }">次日达</c:when>
					          				<c:when test="${cwb.expressProductType eq 3 }">当日达</c:when>
					          			</c:choose>
					          		</div>
					           		<div style="line-height:12px;font-family: 黑体;font-size: 8px;padding: 0 0 0 5px;">保价声明价值：<c:if test="${cwb.paymethod ne 0 }">${cwb.announcedvalue }元</c:if></div>
					            	<div style="line-height:12px;font-family: 黑体;font-size: 8px;padding: 0 0 0 5px;">重量：${cwb.carrealweight }kg</div>
					            	<div style="line-height:12px;font-family: 黑体;font-size: 8px;padding: 0 0 0 5px;">
					            		付款方式：
					            		<strong>
						            		<c:choose>
						            			<c:when test="${cwb.paymethod eq 0}">月结</c:when>
						            			<c:when test="${cwb.paymethod eq 1}">现付</c:when>
						            			<c:when test="${cwb.paymethod eq 2}">到付</c:when>
						            		</c:choose>
					            		</strong>
					            	</div>
			          			</td>
				          		<td valign="middle" style="width:20mm;border-right:1px solid #585656;">
				          			<div style="line-height:12px;font-family: 黑体;font-size: 8px;padding: 0 0 0 0px;">
				          				运费：<c:if test="${cwb.paymethod ne 0 }">${cwb.shouldfare }元</c:if>
				          			</div>
				            		<div style="line-height:12px;font-family: 黑体;font-size: 8px;padding: 0 0 0 0px;">
				            			保费：<c:if test="${cwb.paymethod ne 0 }">${cwb.insuredfee }元</c:if>
				            		</div>
				            		<div style="line-height:12px;font-family: 黑体;font-size: 8px;padding: 0 0 0 0px;">
				            			包装费：<c:if test="${cwb.paymethod ne 0 }">${cwb.packagefee }元</c:if>
				            		</div>
				            		<div style="margin-left:-16px;line-height:12px;font-family: 黑体;font-size: 8px;padding: 0 0 0 0px;">月结账户：${cwb.monthsettleno }</div>
				          		</td>
				          		<td style="width:4mm;" valign="middle">
						    		<div style="font-family: 黑体;font-weight: bold;font-size: 14px;">寄<br>件</div>
						    	</td>
			    				<td valign="middle">
			    					<div style="line-height:10px;font-family: 黑体;font-size: 8px;padding-left: 0px;padding-right: 2px;">
				    					<font style="font-size: 10px;">${cwb.senderprovince }${cwb.sendercity }${cwb.sendercounty }</font>${cwb.senderaddress }
				    					<br>
				    					${cwb.sendername }  <c:choose><c:when test="${empty cwb.sendercellphone }">${cwb.sendertelephone }</c:when><c:otherwise>${cwb.sendercellphone }</c:otherwise></c:choose>
			    					</div>
			    				</td>
			          		</tr>
			        	</table>
			      	</td>
			    </tr>
			    <tr>
			    	<td class="td_1" style="height: 9mm;">
			      		<table class="inner_table" width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
			        		<tr>
			        			<td valign="middle" width="45%">
			       					<div style="line-height:8px;font-family: 黑体;font-weight: bold;font-size: 8px;">寄<br>托<br>物</div>
			       				</td>
			       				<td>
			       					<div style="font-family: 黑体;font-size: 8px;">${cwb.entrustname }</div>
			       				</td>
			        		</tr>
			        	</table>
			        </td>
			    </tr>
			    <tr>
			    	<td class="td_1" style="height: 9mm;">
			      		<table class="inner_table" width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
			        		<tr>
			        			<td style="border-right:1px solid #585656;width:24.3mm">
			       					<div style="line-height:14px;font-family: 黑体;font-size: 8px;">收件员：${cwb.collectorname }</div>
              						<div style="line-height:14px;font-family: 黑体;font-size: 8px;">寄件日期：<fmt:formatDate value="${vo.senderDate }" pattern="yyyy-M-d"/></div>
              						<div style="line-height:14px;font-family: 黑体;font-size: 8px;">派件员：${cwb.exceldeliver }</div>
			       				</td>
			       				<td style="border-right:1px solid #585656;width:30.5mm">
			       					<div style="line-height:14px;font-family:黑体;font-size: 8px;">代收货款:${cwb.receivablefee }元</div>
			       					<div style="line-height:8px;font-family: 黑体;font-size: 8px;">&nbsp;</div>
                					<div style="line-height:14px;font-family:黑体;font-size: 8px;">
                						运费合计:<c:if test="${cwb.paymethod ne 0 }">${cwb.totalfee }元</c:if>
                						<label style="margin-left:10px;" class="label-default">
	                						<c:choose>
							            		<c:when test="${cwb.paymethod eq 0}">月结</c:when>
							            		<c:when test="${cwb.paymethod eq 1}">现付</c:when>
							            		<c:when test="${cwb.paymethod eq 2}">到付</c:when>
							            	</c:choose>
						            	</label>
                					</div>
			       				</td>
			       				<td>
			       					<div style="line-height:14px;font-family: 黑体;font-size: 8px;">收方签署：</div>
              						<div style="line-height:8px;font-family: 黑体;font-size: 8px;">&nbsp;</div>
              						<div style="line-height:14px;font-family: 黑体;font-size: 8px;">日期：&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;日</div>
			       				</td>
			        		</tr>
			        	</table>
			        </td>
			    </tr>
			</table>
		</div>
	</div>
	</c:forEach>
</body>
</html>