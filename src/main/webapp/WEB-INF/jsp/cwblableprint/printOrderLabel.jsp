<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
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
		LODOP.ADD_PRINT_HTM(1,1,"RightMargin:0mm","BottomMargin:0mm", strBodyStyle + "<body>" + document.getElementById("printTable_" + i).outerHTML + "</body>");
		var transcwb = $("#cwb_" + i).val();
		if(transcwb != null && transcwb != "") {
			LODOP.ADD_PRINT_BARCODE(100,30,145,55,"128B",transcwb);
			LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
			LODOP.ADD_PRINT_BARCODE(373,158,145,55,"128B",transcwb);
			LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
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
	border-top-width: 1px;
	border-right-width: 1px;
	border-bottom-width: 1px;
	border-left-width: 1px;
	border-top-style: solid;
	border-right-style: solid;
	border-bottom-style: solid;
	border-left-style: solid;
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

.inner_table td {
	padding-left: 5px;
	padding-right: 5px;
}
</style>
</head>
<body>
	<a href="javascript:nowprint()">直接打印</a>
	<a href="javascript:prn1_preview()">预览</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:history.go(-1)">返回</a>
	<c:forEach var="vo" items="${printOrderLabelVoList }" varStatus="status">
	<c:set var="cwb" value="${vo.cwbOrder }"/>
	<c:set var="branch" value="${vo.branch }"/>
	<input type="hidden" id="cwb_${status.index }" value="${cwb.transcwb }">
	<div id="printTable_${status.index}">
		<div class="box">
			<table style="margin:2mm 2mm 0 2mm;width: 76mm" cellpadding="0" cellspacing="0">
				<tr>
			      <td class="td_1" style="height: 20mm;" align="center" valign="middle">
			      </td>
			    </tr>
			    <tr>
      				<td class="td_1" style="height: 20mm;">
      					<table class="inner_table" width="100%" border="0" cellspacing="0" cellpadding="0">
          					<tr>
            					<td align="center" valign="middle" style="border-right:1px solid #585656;width:45mm;" rowspan="2">
            						
            					</td>
           						<td valign="middle" style="border-bottom:1px solid #585656;padding-left: 0px;padding-right: 0px;height: 14mm;" colspan="2">
           							<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
           								<tr>
           									<td style="width:10px;">
           										<div style="font-family: 黑体;font-size: 10px;">目<br>的<br>地</div>
           									</td>
           									<td style="padding-left: 0px;padding-right: 0px;">
           										<c:choose>
				           							<c:when test="${fn:length(branch.branchname) gt 12 }">
				           								<div style="font-family: 黑体;font-size: 10px;font-weight: bold;">${branch.branchname }</div>
				           							</c:when>
				           							<c:otherwise>
				           								<div style="font-family: 黑体;font-size: 12px;font-weight: bold;">${branch.branchname }</div>
				           							</c:otherwise>
				           							</c:choose>
           									</td>
           								</tr>
           							</table>
           							
            					</td>
            				</tr>
            				<tr>
            					<td style="width:30px;height: 6mm;padding-left: 5px;padding-right: 0px;">
            						<span style="font-family: 黑体;font-size: 10px;">原寄地</span>
            					</td>
            					<td style="padding-left: 5px;padding-right: 0px;">
            						<c:choose>
            						<c:when test="${fn:length(cwb.instationname) gt 5 }">
           								<span style="font-family: 黑体;font-size: 8px;font-weight: bold;">${cwb.instationname }</span>
           							</c:when>
           							<c:otherwise>
           								<span style="font-family: 黑体;font-size: 10px;font-weight: bold;">${cwb.instationname }</span>
           							</c:otherwise>
           							</c:choose>
            					</td>
          					</tr>
        				</table>
	  				</td>
    			</tr>
    			<tr>
    				<td class="td_1" style="height: 15mm;">
    					<table class="inner_table" width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
			        		<tr>
			          			<td valign="middle" style="width:58mm; border-right:1px solid #585656;">
			          				<div style="font:20px;font-family: 黑体;font-weight: bold;margin-left:5px;margin-right:5px;float:left;">收<br>件</div>
			            			<div>
			            				<span style="font-family: 黑体;font-size: 12px;">${cwb.cwbprovince } ${cwb.cwbcity } ${cwb.cwbcounty }</span><br>
			            				<span style="font-family: 黑体;font-size: 9px;">${cwb.consigneeaddress }</span><br>
			            				<span style="font-family: 黑体;font-size: 9px;">${cwb.consigneename }</span>
			            				<span style="margin-left:10px;font-family: Arial;font-size: 9px;">${cwb.consigneemobile }</span>
			            			</div>
			          			</td>
			          			<td valign="middle" align="center">
			            			<span style="font-family: Arial;font-size: 16px;font-weight: bold;">${branch.branchcode }</span>
			          			</td>
			         		 </tr>
			       		</table>
			      	</td>
			    </tr>
			    <tr>
			    	<td class="td_1" style="height: 16mm;">
			      		<table class="inner_table" width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
			        		<tr>
			          			<td valign="middle" style="width:23mm;padding: 0 0 0 5px;">
					          		<div style="line-height:12px;font-family: 黑体;font-size: 8px;">
					          			业务类型：
					          		</div>
					           		<div style="line-height:12px;font-family: 黑体;font-size: 8px;">价保声明价值：${cwb.announcedvalue }元</div>
					            	<div style="line-height:12px;font-family: 黑体;font-size: 8px;">重量：${cwb.carrealweight }kg</div>
					            	<div style="line-height:12px;font-family: 黑体;font-size: 8px;">
					            		付款方式：
					            		<c:choose>
					            			<c:when test="${cwb.paymethod eq 0}">月结</c:when>
					            			<c:when test="${cwb.paymethod eq 1}">现付</c:when>
					            			<c:when test="${cwb.paymethod eq 2}">到付</c:when>
					            		</c:choose>
					            	</span>
			          			</td>
				          		<td valign="middle" style="width:22mm; padding: 0 0 0 0px;border-right:1px solid #585656;">
				          			<div style="line-height:12px;font-family: 黑体;font-size: 8px;">运费：${cwb.shouldfare }元</div>
				            		<div style="line-height:12px;font-family: 黑体;font-size: 8px;">保费：${cwb.insuredfee }元</div>
				            		<div style="line-height:12px;font-family: 黑体;font-size: 8px;">包装费：${cwb.packagefee }元</div>
				            		<div style="margin-left:-16px;line-height:12px;font-family: 黑体;font-size: 8px;">月结账户：${cwb.monthsettleno }</div>
				          		</td>
				          		<td valign="middle">
				          			<div style="font:20px;font-weight:bold;font-family: 黑体;margin-right:5px;margin-top:5px;float:left;">寄<br>件</div>
				            		<div style="margin-top:5px;">
				              			<span style="line-height:10px;font-family: 黑体;font-size: 8px;">${cwb.senderprovince }${cwb.sendercity }${cwb.sendercounty }${cwb.senderaddress }</span><br>
				            		</div>
				            		<div style="clear:both;font-size: 8px;font-family: 黑体;margin-left:5px;margin-bottom:5px;">
				            			${cwb.sendername }  ${cwb.sendercellphone }
				            		</div>
				          		</td>
			          		</tr>
			        	</table>
			      	</td>
			    </tr>
			    <tr>
			       <td class="td_1" valign="top"  style="height: 9mm;padding: 0 5px 0 5px;">
			       		<table class="inner_table" width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
			       			<tr>
			       				<td valign="middle" width="40%">
			       					<div style="line-height:8px;font-family: 黑体;font-size: 8px;">寄<br>托<br>物</div>
			       				</td>
			       				<td>
			       					<div style="font-family: 黑体;font-size: 8px;">${cwb.entrustname }</div>
			       				</td>
			       			<tr>
			       		</table>
			       </td>
    			</tr>
    			<tr>
       				<td class="td_1" style="height: 12.5mm;border-bottom-width: 0px;">
      					<table class="inner_table" width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
          					<tr>
          						<td valign="middle" style="width:22mm;border-right:1px solid #585656;">
              						<span style="line-height:14px;font-family: 黑体;font-size: 8px;">收件员：${cwb.collectorname }</span><br>
              						<span style="line-height:14px;font-family: 黑体;font-size: 8px;">寄件日期：${cwb.emaildate }</span><br>
              						<span style="line-height:14px;font-family: 黑体;font-size: 8px;">派件员：${cwb.exceldeliver }</span>
            					</td>
            					<td valign="middle" style="width:33mm;border-right:1px solid #585656;">
            						<span style="line-height:14px;font-family:黑体;font-size: 8px;">代收货款:${cwb.receivablefee }元</span><br>
                					<span style="line-height:14px;font-family:黑体;font-size: 8px;">运费合计:${cwb.totalfee }元</span><br>
                					<span style="line-height:14px;font-family:黑体;font-size: 10px;font-weight: bold;">应收合计：${vo.shouldReceiveTotal }元</span>
            					</td>
           						<td valign="middle">
            	 					<span style="line-height:14px;font-family: 黑体;font-size: 8px;">收方签署：</span><br>
              						<span style="line-height:14px;font-family: 黑体;font-size: 8px;">&nbsp;</span><br>
              						<span style="line-height:14px;font-family: 黑体;font-size: 8px;">日期：</span>
            					</td>
          					</tr>
        				</table>
      				</td>
    			</tr>
			</table>
		</div>
		<div class="box" style="border-top-width: 0px;">
			<table style="margin:0 2mm 2mm 2mm;width: 76mm" cellpadding="0" cellspacing="0">
				<tr>
      				<td style="height: 18mm;border-top-width: 0px;" class="td_1">
	          			<table class="inner_table" width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
			          			<td align="center" valign="middle" style="height:18mm;border-right:1px solid #585656;border-bottom:1px solid #585656;">
			            		</td>
			            		<td style="border-bottom:1px solid #585656;">
			            			
			            		</td>
			            	</tr>
			            	<tr>
          						<td valign="middle" style="width:34mm;border-right:1px solid #585656;" rowspan="2">
	          						<div style="font:20px;font-weight: bold;font-family: 黑体;margin-left:5px;margin-right:5px;float:left;">寄<br>件</div>
	            					<div>
	              						<span style="font-family: 黑体;font-size: 8px;">${cwb.senderprovince }${cwb.sendercity }${cwb.sendercounty }${cwb.senderstreet }${cwb.senderaddress }</span><br>
	            					</div>
	            					<div style="margin-left:27px;lear:both;">
	             						<span style="font-size: 8px;font-family: 黑体;">${cwb.sendername }  ${cwb.sendercellphone }</span>
	            					</div>
            					</td>
            					<td valign="middle" style="height:4mm;border-bottom:1px solid #585656;">
            						<span style="font-family: 黑体;font-size: 8px;">订单号 ${cwb.cwb }</span>
            					</td>
            				</tr>
            				<tr>
	            				<td valign="middle" style="height:12.5mm;">
	            					<div style="font:16px;font-weight: bold;font-family: 黑体;margin-left:5px;margin-right:5px;float:left;">收<br>件</div>
	            					<div>
	            						<div style="line-height:8px;font-family: 黑体;font-size: 8px;">${cwb.cwbprovince }${cwb.cwbcity }${cwb.cwbcounty }</div>
	            						<div style="line-height:8px;font-family: 黑体;font-size: 8px;">${cwb.consigneeaddress }</div>
	            						<div style="line-height:8px;font-size: 8px;font-family: 黑体;">${cwb.consigneename } ${cwb.consigneemobile }</div>
	            					</div>
	            				</td>
            				</tr>
			            </table>
		         	</td>
		         </tr>
		         <tr>
			      	<td style="height: 9mm;" class="td_1">
			        	<table class="inner_table" width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
			            	<tr>
			                  <td valign="top" style="width:55mm;padding: 5px;border-right:1px solid #585656;">
			                  	<span style="line-height:14px;font-family: 黑体;font-size: 9px;">备注</span>
			                  </td>
			                  <td align="center" valign="middle">
			                  	 <span style="line-height:14px;font-family: 黑体;font-size:9px;">应收合计</span><br>
			            		<span style="line-height:14px;font-family: Arial;font-size: 9px;">${vo.shouldReceiveTotal }元</span>
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