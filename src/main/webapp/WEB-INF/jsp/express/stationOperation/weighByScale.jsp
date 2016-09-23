<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page
	import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.VO.express.DeliverSummaryItem,cn.explink.domain.VO.express.CombineQueryView"%>
<%@page import="cn.explink.domain.express.CwbOrderForCombineQuery"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	String waybillNo = (String) request.getAttribute("waybillNo");
	Double weight = (Double) request.getAttribute("weight");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>电子称称重</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css"
	media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>

<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
var weightTime = <%=request.getAttribute("weightTime") == null ?10:Integer.parseInt(request.getAttribute("weightTime").toString())%> ;
var requestContext = "<%=request.getContextPath()%>" ;
	$(function() {
		var $menuli = $(".kfsh_tabbtn ul li");
		var $menulilink = $(".kfsh_tabbtn ul li a");
		$menuli.click(function() {
			$(this).children().addClass("light");
			$(this).siblings().children().removeClass("light");
			var index = $menuli.index(this);
			$(".tabbox li").eq(index).show().siblings().hide();
		});
	})

	$(function() {
		var $menuli1 = $("#bigTag li");
		$menuli1.click(function() {
			$(this).children().addClass("light");
			$(this).siblings().children().removeClass("light");
		});

		var $menuli2 = $("#smallTag li");
		$menuli2.click(function() {
			$(this).children().addClass("light");
			$(this).siblings().children().removeClass("light");
			var index = $menuli2.index(this);
			$(".tabbox li").eq(index).show().siblings().hide();
		});
	})

	function tabView(tab) {
		$("#" + tab).click();
	}

	function addAndRemoval(cwb, tab, isRemoval) {
		var trObj = $("#ViewList tr[id='TR" + cwb + "']");
		if (isRemoval) {
			$("#" + tab).append(trObj);
		} else {
			$("#ViewList #errorTable tr[id='TR" + cwb + "error']").remove();
			trObj.clone(true).appendTo("#" + tab);
			$("#ViewList #errorTable tr[id='TR" + cwb + "']").attr("id",
					trObj.attr("id") + "error");
		}
	}
	
	function submitWeight(keyCode) {
		var waybillNo = jQuery("#waybillNo").val().trim()  ;
		if(keyCode!=13 || waybillNo.length <= 0){
			return ;
		}
		// modify by bruce shangguan 20160922 获取稳定的重量数据
		console.log("weightTime:"+weightTime) ;
		jQuery("#weightNotice").text("正在称重中,请稍等......") ;
		window.setTimeout(function waitForWeight(){
			var weight = jQuery('#weightSpan').text();
			var weightExp = /^[0-9]+(\.[0-9]+[1-9])?$/ ;
			if (!weightExp.test(weight)) {
				jQuery("#weightNotice").text("请检查系统是否已连接电子秤") ;
				return;
			}
			saveOrUpdateWeight(waybillNo,weight) ;
		} , (weightTime + 0.01) * 1000) ;
		// end 20160922
	};

	function saveOrUpdateWeight(waybillNo,weight){
		var params = {
				waybillNo:waybillNo,
				weight:weight	
		};
		$.ajax({
			type: "POST",
			url:requestContext + "/stationOperation/submitWeight",
			data:params,
			dataType:"json",
			success : function(rs) {
				jQuery("#waybillNo").val("") ;
				jQuery("#weightNotice").text("");
				jQuery("#weightSpan").text("0.00") ;
				jQuery("#waybillNoSpan").html(waybillNo) ;
				jQuery("#lastWeightSpan").html(weight) ;
			}}) ;
	}
	
	// add by bruce shangguan 20160922 实时读取电子秤数据
	function getWeightRepeatable() {
		window.setInterval("setWeight()", 1);
	}

	function setWeight() {
        try{
        	var scaleApplet  =  window.parent.document.getElementById("scaleApplet") ;
        	var weight = scaleApplet.getWeight();
        	if (weight != null && weight != '' &&　weight != undefined ) {
    			document.getElementById("weightSpan").innerHTML = weight;
    		}
        }catch(e){
        	document.getElementById("weightSpan").innerHTML = "0.00" ;
        }
	}
	
</script>
</head>
<body style="background: #f5f5f5" marginwidth="0" marginheight="0" onload="getWeightRepeatable();" >
	<div class="inputselect_box">
			<table width="80%">
				<tr>
					<td>运单号<input type="text" id="waybillNo" name="waybillNo"
						style="height: 30px; font-size: x-large; font-weight: bold;"
						onKeyDown='submitWeight(event.keyCode)' />
					</td>
					<td style="color: red; font-size: x-large; font-weight: bold;">实际重量（kg）：<label
						id="weightSpan">0.00</label> <input type="hidden" id="weight" name="weight" />
					</td>
					<td style="color: red; font-size: x-large; font-weight: bold;">
					   <label id="weightNotice" > </label> 
					</td>
				</tr>
			</table>
		<hr />
		<h1 style="font-weight: bold; font-size: xx-large;">上一次扫描结果：</h1>
		<table width="50%">
			<tr>
				<td style="font-size: x-large;">运单号：<span id="waybillNoSpan"><%=waybillNo == null ? "" : waybillNo%></span>
				</td>
				<td style="font-size: x-large;">实际重量（kg）：<span id="lastWeightSpan"><%=weight == null ? "" : weight%></span>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>