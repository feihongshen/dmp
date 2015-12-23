<%@page import="cn.explink.domain.CwbDetailView"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page
	import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch,cn.explink.domain.CwbOrder"%>
<%@page
	import="cn.explink.domain.express.ExpressPreOrder,cn.explink.domain.Branch,cn.explink.domain.VO.express.AdressVO,cn.explink.domain.express.CwbOrderForCombine"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	Page page_obj = (Page)request.getAttribute("page_obj");

	Long currentPage=(Long)request.getAttribute("page");
	//包号
	String packageNo = (String)request.getAttribute("packageNo");
	//下一站
	Long selectedNextBranch = (Long)request.getAttribute("nextBranch");
	//选中的省
	Integer selectedProvince = (Integer)request.getAttribute("provinceId");
	//选中的市
	String selectedCities = (String) request.getAttribute("selectedCities");
	//快递订单列表
	List<CwbOrderForCombine> expressOrderList = (List<CwbOrderForCombine>)request.getAttribute("expressOrderList");
    //下一站
	List<Branch> nextBranchList = (List<Branch>)request.getAttribute("nextBranchList");
    //省
	List<AdressVO> provinceList = (List<AdressVO>)request.getAttribute("provinceList");
	//市
	List<AdressVO> cityList = (List<AdressVO>)request.getAttribute("cityList");
	//运单总数
	Integer waybillTotalCount = (Integer)request.getAttribute("waybillTotalCount");
	//总件数
	Long itemTotalCount = (Long)request.getAttribute("itemTotalCount");
	//选中的运单号
	String waybillNos = (String)request.getAttribute("waybillNos");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>揽件合包</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet"
	type="text/css" />
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect_exp.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>

<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">

	$(function() {
		if ($("#packageNo").val() == null || $("#packageNo").val().length == 0) {
			$("#packageNo").focus();
		} else {
			$("#waybillNo").focus();
		}
	});

	//added by jiangyu  失去焦点的时候校验包号的唯一性
	$(function() {
		$("#packageNo").bind("blur", checkPackageCodeIsUsed);
	});
	
	//校验包号是否已经被使用
	function checkPackageCodeIsUsed() {
		var packageNo = $("#packageNo").val().trim();
		//如果为空则不校验
		if(!packageNo){
			return;
		}
		$.ajax({
			type : "POST",
			url : $("#validatePackageCodeUsed").val(),
			dataType : "json",
			data : {
				"packageNo" : packageNo
			},
			success : function(data) {
				if (data.status) {
					var validateResult = data.attributes.checkResult;
					if (validateResult) {//返回结果ture
						showHintInfo("包号已经被使用！！");
					}
				} else {//异常
					showHintInfo("" + data.msg);
				}
			}
		});
	}
	//end

	$(function() {
		$("#city").expMultiSelect({
			oneOrMoreSelected : '*',
			noneSelected : '----请选择市----'
		}, function() {

		}, function(val) {
			var cities = ",";
			$(".multiSelectOptions label.checked input").each(function() {
				// alert($(this).val());
				// alert($(this).parent().text());
				cities = cities + $(this).val() + ",";
			});
			$("#cities").val(cities);
			$('#searchForm').attr('action', $("#takeExpressCombine").val());
			$("#searchForm").submit();
		});
	});

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
	};

	$(function() {
		$("#remove_button").click(function() {
			var waybillNos = $("#waybillNos").val();
			/* */if (waybillNos.length <= 0) {
				showHintInfo("没有可以移除的运单！");
				return;
			}
			$.ajax({
				type : "POST",
				url : $("#remove").val(),
				dataType : "html",
				data : {
					"packageNo" : $("#packageNo").val(),
					"nextBranch" : $("#nextBranch").val(),
					"provinceId" : $("#province").val(),
					"waybillNos" : waybillNos,
					"waybillTotalCount" : $("#waybillTotalCount").val(),
					"itemTotalCount" : $("#itemTotalCount").val()
				},
				success : function(data) {
					$("#alert_box", parent.document).html(data);
				},
				complete : function() {
					//addInit();// 初始化某些ajax弹出页面
					viewBox();
				}
			});
		});
	});

	function viewBox() {
		$("#alert_box", parent.document).show();
		$("#dress_box", parent.document).css("visibility", "hidden");
		window.parent.centerBox();
	}
	function getSelectedExpressOrders() {
		var selectedExpressOrders = "";
		$('input[name="selectedExpressOrder"]:checked').each(function() { //由于复选框一般选中的是多个,所以可以循环输出
			selectedExpressOrders += $(this).val() + ",";
		});
		return selectedExpressOrders;
	};
	function getSelectedCityArr() {
		var selectedCityArr = [];
		$('input[name="city"]:checked').each(function() { //由于复选框一般选中的是多个,所以可以循环输出
			selectedCityArr.push($(this).val());
		});
		return selectedCityArr;
	};

	$(function() {
		$("#search_button").click(function() {
			$.ajax({
				type : "POST",
				url : $("#search").val(),
				data : {
					"distributeCondition" : $("#distributeCondition").val(),
					"deliverid" : $("#deliverid").val()
				},
				success : function(data) {
				}
			});
		});
	});

	$(function() {
		$("#province").change(function() {
			$('#searchForm').attr('action', $("#takeExpressCombine").val());
			$("#searchForm").submit();
		});
	});

	function loadCities() {
		$.ajax({
			type : "POST",
			url : $("#getCitiesByProvince").val(),
			dataType : "html",
			data : {
				"provinceId" : $("#province").val()
			},
			success : function(data) {
				var cityArr = $.parseJSON(data);

				for (var i = 0; i < cityArr.length; i++) {
					$(".multiSelectOptions").append(
							"<label><input type='checkbox' name='city' value='"+cityArr[i].id+"'>"
									+ cityArr[i].name + "</input></label>");
				}
			}
		});
	};

	function selectAll() {
		if ($("input[id='selectAll']").is(':checked')) {
			$('input[name="selectedExpressOrder"]').each(function() {
				$(this).attr("checked", true);
			});
		} else {
			$('input[name="selectedExpressOrder"]').each(function() {
				$(this).attr("checked", false);
			});
		}
	};

	function showHintInfo(info) {
		$(".tishi_box").html(info);
		$(".tishi_box").show();
		setTimeout("$(\".tishi_box\").hide(1000)", 2000);
	};
	
	//校验包号是否唯一
	function checkPackageNoUnique(packageNo) {
		var isUnique=false;
		$.ajax({
			type : "POST",
			url : $("#checkPackageNoUnique").val(),
			dataType : "json",
			async: false,
			data : {
				"packageNo" : packageNo
			},
			success : function(data) {
				isUnique=data;
				if (!data) {
					$("#waybillNo").val("");
					$("#packageNo").val("");
					showHintInfo("包号已经被使用，请重新输入包号！");
				} 
			}
		});
		return isUnique;
	}

	function validateRequiredInfo() {
		var packageNo = $("#packageNo").val();
		if (packageNo == undefined || packageNo == "" || packageNo == null) {
			showHintInfo("包号未指定！");
			$("#waybillNo").val("");
			return false;
		}
		if(!checkPackageNoUnique(packageNo)){
			return;
		}
		if ($("#nextBranch").val() == -1) {
			showHintInfo("下一站未选择！");
			$("#waybillNo").val("");
			return false;
		}
		return true;
	}
	function playSuccessSound(fn) {
		var wavArr = [{
			"time" : 1000,
			url : $("#successSoundPath").val()
		}];
		batchPlayWav(wavArr);
		setTimeout(fn, 600);
	}

	function playErrorSound() {
		var wavArr = [{
			"time" : 1000,
			url : $("#failSoundPath").val()
		}];
		batchPlayWav(wavArr);
	}

	function addWaybillNo(path, waybillNo) {
		if (!validateRequiredInfo()) {
			playErrorSound();
			return;
		}
		if (inSelectedWaybillNos(waybillNo)) {
			showHintInfo("该运单已扫描！");
			$("#waybillNo").val("");
			playErrorSound();
			return;
		}
		if (waybillNo.length > 0) {
			$.ajax({
				type : "POST",
				url : $("#getExpressOrderByWaybillNo").val(),
				data : {
					"waybillNo" : waybillNo
				},
				success : function(data) {
					if (!data.expressOrderExist) {
						$(".tishi_box").html("该运单不存在或不属于本机构！");
						$(".tishi_box").show();
						setTimeout("$(\".tishi_box\").hide(1000)", 2000);
						$("#waybillNo").val("");
						playErrorSound();
						return;
					}
					if (data.inPackage) {
						$(".tishi_box").html("该运单不是入站（入库）状态或已经合包！");
						$(".tishi_box").show();
						setTimeout("$(\".tishi_box\").hide(1000)", 2000);
						$("#waybillNo").val("");
						playErrorSound();
						return;
					}
					playSuccessSound(function() {
						var waybillNos = $("#waybillNos").val() + ","
								+ waybillNo;
						$("#addWaybill").attr("value", "true");
						$("#waybillNo").attr("value", waybillNo);
						$("#waybillNos").attr("value", waybillNos);

						$('#searchForm').attr("action",
								$("#takeExpressCombine").val());
						$('#searchForm').submit();
					});
					$("#waybillNo").val("");
				}
			});
		};
	};
	function getNotEmptyWaybillNoArr(allWaybillNos) {
		var waybillNoArr = allWaybillNos.split(",");
		var notEmptyWaybillNoArr = [];
		for (var i = 0; i < waybillNoArr.length; i++) {
			if (waybillNoArr[i] != "") {
				notEmptyWaybillNoArr.push(waybillNoArr[i]);
			}
		}
		return notEmptyWaybillNoArr;
	}

	function inCities(cityId) {
		var inCities = false;
		var selectedCityArr = getSelectedCityArr();
		if (selectedCityArr.length > 0) {
			for (var i = 0; i < selectedCityArr.length; i++) {
				if (cityId == selectedCityArr[i]) {
					inCities = true;
					break;
				}
			}
		} else {
			inCities = true;
		}
		return inCities;
	}
	function inSelectedWaybillNos(waybillNo) {
		var inSelectedWaybillNos = false;
		var waybillNoArr = getNotEmptyWaybillNoArr($("#waybillNos").val());
		if (waybillNoArr.length > 0) {
			for (var i = 0; i < waybillNoArr.length; i++) {
				if (waybillNo == waybillNoArr[i]) {
					inSelectedWaybillNos = true;
					break;
				}
			}
		} else {
			inSelectedWaybillNos = false;
		}
		return inSelectedWaybillNos;
	}

	$(function() {
		$("#combine_button").click(function() {
			var waybillNos = $("#waybillNos").val();
			var selectedWaybillNos = getSelectedExpressOrders();

			var allWaybillNos = waybillNos + "," + selectedWaybillNos;
			if (getNotEmptyWaybillNoArr(allWaybillNos).length < 1) {
				showHintInfo("没有可以合包的运单！");
				return;
			}
			if (!validateRequiredInfo()) {
				return;
			}
			$("#hiddenWaybillNos").val(allWaybillNos);

			$('#searchForm').attr('action', $("#combine").val());
			$("#searchForm").submit();
			showHintInfo("合包成功！");
		});
	});

	$(function() {
		//单选模糊查询下拉框
		$("#nextBranch").combobox();
		$("#province").combobox();
		$("span.combo-arrow").css({"margin-right":"-18px","margin-top":"-20px"});
	})
</script>
</head>
<body style="background: #f5f5f5" marginwidth="0" marginheight="0">
	<div class="tishi_box"></div>
	<div class="inputselect_box">
		<form action="<%=request.getAttribute("page") == null ? "1" : request.getAttribute("page")%>"
			method="post" id="searchForm">
			<table>
				<tr>
					<td>包号：<input type="text" id="packageNo" name="packageNo"
						style="height: 30px; font-size: x-large; font-weight: bold;"
						value="<%=packageNo == null ? "" : packageNo%>" />
					</td>
					<td>下一站：<select id="nextBranch" name="nextBranch" class="select1">
							<option value=-1>全部</option>
							<%
								for (Branch nextBranch : nextBranchList) {
																																		if (selectedNextBranch != null && selectedNextBranch.equals(Long.valueOf(nextBranch.getBranchid()))) {
							%>
							<option value=<%=nextBranch.getBranchid()%> selected="selected"><%=nextBranch.getBranchname()%></option>
							<%
								} else {
							%>
							<option value=<%=nextBranch.getBranchid()%>><%=nextBranch.getBranchname()%></option>
							<%
								}
																																	}
							%>
					</select>
					</td>
					<td>省： <select id="province" name="province" class="select1">
							<option value=-1>全部</option>
							<%
								for (AdressVO province : provinceList) {
																									if (selectedProvince != null && selectedProvince.equals(Integer.valueOf(province.getId()))) {
							%>
							<option value=<%=province.getId()%> selected="selected"><%=province.getName()%></option>
							<%
								} else {
							%>
							<option value=<%=province.getId()%>><%=province.getName()%></option>
							<%
								}
																																	}
							%>
					</select></td>
					<td>市：<select id="city" name="city" multiple="multiple" class="select1">
							<%
								for (AdressVO city : cityList) {
																								if (selectedCities != null && selectedCities.contains(","+city.getId()+",")) {
							%>
							<option value=<%=city.getId()%> selected="selected"><%=city.getName()%></option>
							<%
								} else {
							%>
							<option value=<%=city.getId()%>><%=city.getName()%></option>
							<%
								}
																							}
							%>
					</select> <input type="hidden" id="cities" name="cities"
						value="<%=selectedCities==null?"":selectedCities%>" />
					</td>
					<input type="hidden" id="waybillNos" name="waybillNos"
						value="<%=waybillNos == null ? "" : waybillNos%>" />
					<input type="hidden" id="page" name="page"
						value="<%=currentPage == null ? 1 : currentPage%>" />
				</tr>
				<tr>
					<td><span>运单号:<input type="text" id="waybillNo" name="waybillNo"
							style="height: 30px; font-size: x-large; font-weight: bold;"
							onKeyDown='if(event.keyCode==13&&$(this).val().length>0){addWaybillNo("<%=request.getContextPath()%>",$(this).val());}' /></span>
						<input type="hidden" id="addWaybill" name="addWaybill" value="false" /></td>
					<td>运单总数：<span style="font-size: x-large; font-weight: bold;"><%=waybillTotalCount == null ? 0 : waybillTotalCount%></span>
						<input type="hidden" id="waybillTotalCount" name="waybillTotalCount"
						value="<%=waybillTotalCount == null ? 0 : waybillTotalCount%>" />
					</td>
					<td>总件数：<span style="font-size: x-large; font-weight: bold;"><%=itemTotalCount == null ? 0 : itemTotalCount%></span>
						<input type="hidden" id="itemTotalCount" name="itemTotalCount"
						value="<%=itemTotalCount == null ? 0 : itemTotalCount%>" />
					</td>
					<td><span><input name="" type="button" value="移除" class="input_button1"
							id="remove_button" /> </span> <input type="hidden" id="hiddenWaybillNos" name="hiddenWaybillNos" />
						<span><input name="" type="button" value="合包" class="input_button1" id="combine_button" />
					</span></td>
				</tr>
			</table>
		</form>

	</div>
	<div class="saomiao_box2">
		<div>
			<table width="100%" border="0" cellspacing="10" cellpadding="0" style="margin-top: 100px">
				<tbody>
					<tr>
						<td width="10%" height="26" align="left" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
								<tr>
									<td width="2" align="center" bgcolor="#f1f1f1"><input id="selectAll" type="checkbox"
										onclick="selectAll();" /></td>
									<td width="100" align="center" bgcolor="#f1f1f1">运单号</td>
									<td width="50" align="center" bgcolor="#f1f1f1">件数</td>
									<td width="50" align="center" bgcolor="#f1f1f1">小件员</td>
									<td width="70" align="center" bgcolor="#f1f1f1">揽件时间</td>
									<td width="50" align="center" bgcolor="#f1f1f1">付款方式</td>
									<td width="50" align="center" bgcolor="#f1f1f1">费用合计</td>
									<td width="50" align="center" bgcolor="#f1f1f1">运费</td>
									<td width="50" align="center" bgcolor="#f1f1f1">包装费用</td>
									<td width="50" align="center" bgcolor="#f1f1f1">保价费用</td>
									<td width="50" align="center" bgcolor="#f1f1f1">收件省份</td>
									<td width="50" align="center" bgcolor="#f1f1f1">收件城市</td>
								</tr>
							</table>

							<table width="100%" border="0" cellspacing="1" cellpadding="2"
								class="table_2">
								<%
									if (expressOrderList != null && !expressOrderList.isEmpty())
																																								for (CwbOrderForCombine expressOrder : expressOrderList) {
								%>
								<tr>
									<td width="2" align="center"><input id="selectedExpressOrder"
										name="selectedExpressOrder" type="checkbox" value="<%=expressOrder.getCwb()%>" /></td>
									<td width="100" align="center"><%=expressOrder.getCwb()%></td>
									<td width="50"><%=expressOrder.getSendcarnum()%></td>
									<td width="50"><%=expressOrder.getCollectorname()==null?"":expressOrder.getCollectorname()%></td>
									<td width="70"><%=expressOrder.getInstationdatetime()==null?"":expressOrder.getInstationdatetime()%></td>
									<td width="50"><%=expressOrder.getPayMethodName()%></td>
									<td width="50"><%=expressOrder.getTotalfee()==null?"":expressOrder.getTotalfee()%></td>
									<td width="50"><%=expressOrder.getShouldfare()==null?"":expressOrder.getShouldfare()%></td>
									<td width="50"><%=expressOrder.getPackagefee()==null?"":expressOrder.getPackagefee()%></td>
									<td width="50"><%=expressOrder.getInsuredfee()==null?"":expressOrder.getInsuredfee()%></td>
									<td width="50"><%=expressOrder.getProvinceName()==null?"":expressOrder.getProvinceName()%></td>
									<td width="50"><%=expressOrder.getCityName()==null?"":expressOrder.getCityName()%></td>
								</tr>
								<%
									}
								%>
							</table>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<%
			if (page_obj.getMaxpage() > 1) {
		%>
		<div class="iframe_bottom">
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff"><a
						href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/stationOperation/takeExpressCombine/1');$('#searchForm').submit();">第一页</a> <a
						href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/stationOperation/takeExpressCombine/<%=page_obj.getPrevious() < 1 ? 1 : page_obj.getPrevious()%>');$('#searchForm').submit();">上一页</a>
						<a
						href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/stationOperation/takeExpressCombine/<%=page_obj.getNext() < 1 ? 1 : page_obj.getNext()%>');$('#searchForm').submit();">下一页</a>
						<a
						href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/stationOperation/takeExpressCombine/<%=page_obj.getMaxpage() < 1 ? 1 : page_obj.getMaxpage()%>');$('#searchForm').submit();">最后一页</a>
						共<%=page_obj.getMaxpage()%>页 共<%=page_obj.getTotal()%>条记录 当前第<select id="selectPg"
						onchange="$('#searchForm').attr('action','<%=request.getContextPath()%>/stationOperation/takeExpressCombine/'+$(this).val());$('#searchForm').submit()">
							<%
								for (int i = 1; i <= page_obj.getMaxpage(); i++) {
							%>
								<option value="<%=i%>"><%=i%></option>
							<%
								}
							%>
					</select>页</td>
				</tr>
			</table>
		</div>
		<%
			}
		%>
	</div>
	<script type="text/javascript">
		$("#selectPg").val(<%=request.getAttribute("page")%>);
	</script>
	<!-- 查询的ajax地址 -->
	<input type="hidden" id="getCitiesByProvince"
		value="<%=request.getContextPath()%>/stationOperation/getCitiesByProvince" />
	<!-- 分配的ajax地址 -->
	<input type="hidden" id="combine" value="<%=request.getContextPath()%>/stationOperation/combine" />
	<!-- 移除的ajax地址 -->
	<input type="hidden" id="remove"
		value="<%=request.getContextPath()%>/stationOperation/openRemoveDlg" />
	<!-- 查询运单号查询快递订单的ajax地址 -->
	<input type="hidden" id="getExpressOrderByWaybillNo"
		value="<%=request.getContextPath()%>/stationOperation/getExpressOrderByWaybillNo" />

	<!-- 校验包号的唯一性的ajax地址 -->
	<input type="hidden" id="validatePackageCodeUsed"
		value="<%=request.getContextPath()%>/stationOperation/validatePackageCodeUsed" />
		
	<!-- 校验包号是否被使用的ajax地址 -->
	<input type="hidden" id="checkPackageNoUnique"
		value="<%=request.getContextPath()%>/stationOperation/checkPackageNoUnique" />

	<!-- 根据省、市、排除的运单号加载运单的ajax地址 -->
	<input type="hidden" id="takeExpressCombine"
		value="<%=request.getContextPath()%>/stationOperation/takeExpressCombine/<%=currentPage == null ? 1 : currentPage%>?" />

	<input type="hidden" id="failSoundPath"
		value="<%=request.getContextPath()%>/images/waverror/fail.wav" />

	<input type="hidden" id="successSoundPath"
		value="<%=request.getContextPath()%>/images/waverror/success.wav" />
</body>
</html>