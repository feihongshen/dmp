<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<Branch> branchList = (List<Branch>) request.getAttribute("branchList");
	CwbOrder cwbOrder = (CwbOrder) request.getAttribute("cwbOrder");
	String cwb = "";
	String transCwb = "";
	String deliveryBranchName = "";
	String goodsCount = "";
	String category = "";
	boolean isOXOJIT = false;
	if (cwbOrder != null) {
		cwb = cwbOrder.getCwb();
		category = " : " + cwbOrder.getCartype();
		goodsCount = " : " + cwbOrder.getSendcarnum();
		
		String temp = cwbOrder.getTranscwb();
		if (temp != null && temp.indexOf(",") > -1) {
			transCwb = temp.substring(0,temp.indexOf(","));
		} else {
			transCwb = temp;
		}
		if (branchList != null && !branchList.isEmpty()) {
			boolean isContain = false;
			for (Branch b : branchList) {
				if(b.getBranchid() == cwbOrder.getDeliverybranchid()){
					deliveryBranchName = b.getBranchname();
					isContain = true;
					break;
				}
			}
			if (!isContain) {
				deliveryBranchName = "";
			}
		} else {
			deliveryBranchName = "";
		}

		if (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.OXO_JIT.getValue()) {
			category = " : ";
			goodsCount = " : ";
			deliveryBranchName = ""; 
		}
	}
	Map usermap = (Map) session.getAttribute("usermap");
	
%>

<html xmlns:o="urn:schemas-microsoft-com:office:office"
	xmlns:w="urn:schemas-microsoft-com:office:word"
	xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8">
<title>OXO订单运单打印</title>
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA"
	width=0 height=0>
	<param name="CompanyName" value="北京易普联科信息技术有限公司" />
	<param name="License" value="653717070728688778794958093190" />
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0
		CompanyName="北京易普联科信息技术有限公司" License="653717070728688778794958093190"></embed>
</object>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js" type="text/javascript"></script>

<script type="text/javascript">
var LODOP; //声明为全局变量 
function prn1_preview(cwb) {
	CreateOneFormPage(cwb);
	LODOP.PREVIEW();	
};
function prn1_print(cwb) {	
	CreateOneFormPage(cwb);
	LODOP.PRINT();	
};

function CreateOneFormPage(cwb){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP.PRINT_INIT("OXO订单运单打印");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	LODOP.NEWPAGE();
	LODOP.ADD_PRINT_HTM(0,30,840,1100,document.getElementById("form1").innerHTML);
			
	var transcwb = $("#transcwb").val();		
	var cwb = $("#cwb").val();	
	LODOP.ADD_PRINT_BARCODE(240,280,200,50,"128Auto",transcwb);
	LODOP.SET_PRINT_STYLEA(0, "FontSize", 12);
	LODOP.ADD_PRINT_BARCODE(770,280,200,50,"128Auto",cwb);
	LODOP.SET_PRINT_STYLEA(0, "FontSize", 12);
};
function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print('<%=cwb%>');
	}
}
</script>
</head>
<body marginwidth="0" marginheight="0" style="font-size: 14px" >
	<div id="noImage">
	<a href="javascript:prn1_preview('<%=cwb%>')">打印预览</a>
	<a href="javascript:nowprint()">直接打印</a>
	<a href="javascript:history.go(-1);">返回</a>
	<div id="form1" align="center">
		<input type="hidden" id="cwb" value="<%=cwb %>">
        <input type="hidden" id="transcwb" value="<%=transCwb %>">
		<%if (cwbOrder != null) { %>
			<table border="0" cellspacing="0" cellpadding="0">
			  <tr>
			    <td width="100"></td>
			    <td>
						<table border="1" cellspacing="0" cellpadding="0">
							<tr>
								<td>
									<table width="500px" border="0" cellspacing="0" cellpadding="0"
										style="font-size: 16px">
										<tr>
											<td colspan="4" align="center" height="50"><font
												size="5px"><strong>唯品会</strong></font></td>
										</tr>
										<tr>
											<td colspan="4" align="center" height="50"><font
												size="3px"><strong>VIP.COM</strong></font></td>
										</tr>

										<tr height="50">
											<td width="100" valign="middle" style="font-weight: bold;">签收人:</td>
											<td width="100" valign="middle"></td>
											<td ></td>
											<td ></td>
										</tr>

										<tr height="50">
											<td width="100" valign="middle" style="font-weight: bold;">应收金额:</td>
											<td width="100" valign="middle" style="font-weight: bold;"><%=cwbOrder.getReceivablefee()%></td>
											<td ></td>
											<td ></td>
										</tr>
										<tr>
											<td style="font-size: 18px" colspan="4" height="30"
												align="middle" valign="bottom">运单号： <%=transCwb%></td>
										</tr>
										<tr>
											<td height="100" colspan="4" align="middle">
												<div id="transCwbBarcodeTarget"></div>
											</td>
										</tr>

										<tr>
											<td colspan="2" valign="middle" align="middle">
												<table border="1" width="80" height="80" cellpadding="0"
													cellspacing="0">
													<tr>
														<td></td>
													</tr>
												</table>
											</td>
											<td colspan="2" valign="middle" align="middle">
												<table border="1" width="180" height="90" cellpadding="0"
													cellspacing="0">
													<tr>
														<td style="font-size: 40px; text-align: center;"><%=deliveryBranchName%></td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
			  </tr>
			</table>
			
			<br />
			<div style="width:700px;font-weight: bold; border-bottom: 2px dashed #000000;"></div>
			<br />
			<br />
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="100"></td>
					<td>
						<table border="1" cellspacing="0" cellpadding="0">
							<tr>
								<td>
									<table width="500px" border="0" cellspacing="0" cellpadding="0"
										style="font-size: 16px">
										<tr>
											<td width="100" height="30" valign="middle"
												style="font-weight: bold;">*会员留存联</td>
											<td width="150"></td>
											<td></td>
											<td width="236"><img src="../images/billwarn.png" /></td>
										</tr>
										<tr>
											<td width="100" height="30" valign="middle">收货人:</td>
											<td width="150" height="30" valign="middle"><%=cwbOrder.getConsigneename()%></td>
											<td ></td>
											<td ></td>
										</tr>
										<tr>
											<td width="100" height="30" valign="middle">电话:</td>
											<td width="150" height="30" valign="middle"><%=cwbOrder.getConsigneemobile()%></td>
											<td ></td>
											<td ></td>
										</tr>
										<tr>
											<td width="100" height="30" valign="middle">收货地址:</td>
											<td colspan="3" height="30" valign="middle"><%=cwbOrder.getConsigneeaddress()%></td>
										</tr>
										<tr>
											<td style="font-size: 18px" colspan="4" height="50" align="middle" valign="bottom">订单号：
												<%=cwb%></td>
										</tr>
										<tr>
											<td height="50" colspan="4" align="middle">
												<div id="cwbBarcodeTarget"></div>
											</td>
										</tr>
									</table> 
									<br />
									<div style="width: 500px; font-weight: bold; border-bottom: 1px dashed #000000;"></div>
									<br />

									<table width="500px" border="0" cellspacing="0" cellpadding="0"
										style="font-size: 16px">

										<tr>
											<td  align="right" width="250" style="font-weight: bold;">品名 <%=category%></td>
											<td  align="middle"width="250" style="font-weight: bold;">数量<%=goodsCount%></td>
										</tr>

										<tr valign="bottom">
											<td width="100" height="30">退货寄件地址</td>
											<td></td>
										</tr>
										<tr valign="bottom">
											<td colspan="2" height="30">广东省佛山市顺德区陈村镇石洲村委国通大道唯品会物流中心43-48号门，顾客退货服务部（收）
												邮编：528313 联系电话：4006789888</td>
										</tr>

										<tr valign="bottom">
											<td height="50">温馨提示：</td>
											<td></td>
										</tr>
										<tr valign="bottom">
											<td colspan="2" height="30">如需退货，请先登录销售平台申请，再将此单放入包裹中一并寄到如下地址，否则无法为您办理退货，敬请理解。</td>
										</tr>

									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
         <%} %>
		</div>
	</div>
	<%if (cwb == "") {%>
	<div>无未打印过的订单</div>
	<%}%>
</body>
<script type="text/javascript">
/* function generateBarcode(id,barcodeTarget){ //订单号barcode
	var value = $("#"+id).val();
	if (value != null && value != '') {
		var btype = "code128";
	    var settings = {
	      output: "css",
	      bgColor: "#FFFFFF",
	      color: "#000000",
	      barWidth: 2,
	      barHeight: 30
	    };
	    $("#"+barcodeTarget).hide();
	    $("#"+barcodeTarget).html("").show().barcode(value, btype, settings);
	}
}
$(function() {
	generateBarcode("transcwb","transCwbBarcodeTarget");
	generateBarcode("cwb","cwbBarcodeTarget");
});	 */
</script>
</html>