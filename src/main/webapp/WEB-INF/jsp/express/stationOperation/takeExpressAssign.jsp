<%@page import="cn.explink.domain.CwbDetailView"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page
	import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch,cn.explink.domain.CwbOrder"%>
<%@page
	import="cn.explink.enumutil.express.DistributeConditionEnum,cn.explink.domain.express.ExpressPreOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	Page page_obj = (Page)request.getAttribute("page_obj");
	//预订单列表
	List<ExpressPreOrder> preOrderList = (List<ExpressPreOrder>)request.getAttribute("preOrderList");
    //分配情况
	List<DistributeConditionEnum> distributeConditionList = (List<DistributeConditionEnum>)request.getAttribute("distributeConditionList");
    
//     List
    
	List<User> deliverList = (List<User>)request.getAttribute("deliverList");

	
	Integer selectedDistributeCondition = (Integer)request.getAttribute("selectedDistributeCondition");
	
	Long selectedDeliverid = (Long)request.getAttribute("selectedDeliverid");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>揽件分配/调整</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">


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

	/**
	 * 小件员领货扫描
	 */
	var deliverStr = [];
<%for(User deliver : deliverList){%>
	deliverStr[
<%=deliver.getUserid()%>
	] = "";
<%}%>
	function exportField(flag, deliverid) {
		var cwbs = "";
		if (flag == 1) {
			$("#type").val(flag);
			$("#searchForm3").submit();
		} else if (flag == 2) {
			$("#type").val(flag);
			$("#searchForm3").submit();
		} else if (flag == 3) {//修改导出问题
			$("#errorTable tr").each(function() {
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
			if (cwbs.length > 0) {
				cwbs = cwbs.substring(0, cwbs.length - 1);
			}
			if (cwbs != "") {
				$("#cwbs").val(cwbs);
				$("#btnval").attr("disabled", "disabled");
				$("#btnval").val("请稍后……");
				$("#searchForm2").submit();
			} else {
				alert("没有订单信息，不能导出！");
			};
		} else if (flag == 4) {
			$("#type").val(flag);
			$("#searchForm3").submit();
		}
	}

	$(function() {
		$("#assign_button").click(function() {
			var selectedPreOrders = getSelectedPreOrders();
			if (selectedPreOrders == null || selectedPreOrders == "") {
				showHintInfo("请先选中预订单再进行分配！");
				return;
			}
			$.ajax({
				type : "POST",
				url : $("#assign").val(),
				dataType : "html",
				data : {
					"selectedPreOrders" : selectedPreOrders
				},
				success : function(data) {
					$("#alert_box", parent.document).html(data);
				},
				complete : function() {
					// 				addInit();// 初始化某些ajax弹出页面
					viewBox();
				}
			});

		});

	});
	function showHintInfo(msg) {
		$(".tishi_box").html(msg);
		$(".tishi_box").show();
		setTimeout("$(\".tishi_box\").hide(1000)", 2000);
	};

	$(function() {
		$("#superzone_button").click(function() {
			var selectedPreOrders = getSelectedPreOrders();
			if (selectedPreOrders == null || selectedPreOrders == "") {
				showHintInfo("请先选中预订单再进行站点超区操作！");
				return;
			}
			$.ajax({
				type : "POST",
				url : $("#superzone").val(),
				dataType : "html",
				data : {
					"selectedPreOrders" : selectedPreOrders
				},
				success : function(data) {
					$("#alert_box", parent.document).html(data);
				},
				complete : function() {
					// 				addInit();// 初始化某些ajax弹出页面
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
	function getSelectedPreOrders() {
		var selectedPreOrders = "";
		$('input[name="selectedPreOrder"]:checked').each(function() { //由于复选框一般选中的是多个,所以可以循环输出
			selectedPreOrders += $(this).val() + ",";
		});
		return selectedPreOrders;
	}

	$(function() {
		$("#search_button").click(function() {
			debugger
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

	function selectAll(_obj) {
		 var $Box = $(_obj);
		 $('input[name="selectedPreOrder"]').attr("checked", $Box[0].checked);
	
	}
	$(function() {
		$("#distributeCondition").change(function() {
			if ($("#distributeCondition").val() == 1) {
				$("#deliverid").attr("disabled", true);
			} else {
				$("#deliverid").attr("disabled", false);
			}
			//liuwuqiang 9.29
			if($("#distributeCondition").val() == <%= DistributeConditionEnum.AleryDistributed.getValue()%>){
				document.getElementById("returnResult_div").style.display="";
			}else{
				document.getElementById("returnResult_div").style.display = "none";
			}
		});
	});
	
	$(function(){
		//单选模糊查询下拉框
		 $("#deliverid").combobox();
		//实现模糊查询的下拉框的全部选项三角号的显示
		 $("span.combo-arrow").css({"margin-right":"-18px","margin-top":"-20px"});
		//liuwuqiang 9.29
		/* $("#returnResult").val($("#returnResultValue").val()); */
	 })

</script>
</head>
<body style="background: #f5f5f5" marginwidth="0" marginheight="0">
<div id="returnResult_flag" style="display:none;">${returnResult }</div>
	<div class="tishi_box"></div>
	<div class="inputselect_box">
		<span><input name="" type="button" value="站点超区" class="input_button1" id="superzone_button" />
		</span> <span><input name="" type="button" value="分配" class="input_button1" id="assign_button" />
		</span>
		<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page")%>"
			method="post" id="searchForm">
			<div>
			<div style="float: left;">
			分配情况：<select id="distributeCondition" name="distributeCondition" class="select1">
				<%
					for (DistributeConditionEnum distributeCondition : distributeConditionList) {
								if(selectedDistributeCondition!=null&&selectedDistributeCondition.equals(Integer.valueOf(distributeCondition.getValue()))){
				%>
				<option value=<%=distributeCondition.getValue()%> selected="selected"><%=distributeCondition.getText()%></option>
				<%
					}else{
				%>
				<option value=<%=distributeCondition.getValue()%>><%=distributeCondition.getText()%></option>
				<%
					}
				}
				%>
			</select>
			</div>
			<%if(DistributeConditionEnum.AleryDistributed.getValue() == selectedDistributeCondition){%>
				<div id="returnResult_div" style="float: left;;margin-left:30px;">
			<% }else{%> 
				<div id="returnResult_div" style="float: left;display:none;margin-left:30px;">	
			<% }%>
				反馈结果:
					<select id="returnResult" name="returnResult"  style="width:200px;">
						<option value></option>  
						<option value="3">延迟揽件</option>  
						<option value="6">揽件超区</option>
					</select>
			</div>
			<div  style="float: left;margin-left: 3%;"> 
				<div style="float: left;">小件员：</div>
				<div style="float: left;"><select id="deliverid" name="deliverid" class="select1">
				<option value=-1>请选择</option>
				<%
					for (User deliver : deliverList) {
								if(selectedDeliverid!=null&&selectedDeliverid.equals(Long.valueOf(deliver.getUserid()))){
				%>
				<option value=<%=deliver.getUserid()%> selected="selected"><%=deliver.getRealname()%></option>
				<%
					}else{
				%>
				<option value=<%=deliver.getUserid()%>><%=deliver.getRealname()%></option>
				<%
					}
				}
				%>
			</select></div> 
			</div>
			<div>
			<input type="submit" id="find" onclick="$('#searchForm').attr('action',1);return true;"
				value="查询" class="input_button2" />
			</div>
			</div>
		</form>



	</div>
	<div class="saomiao_box2">
		<div>
			<table width="100%" border="0" cellspacing="10" cellpadding="0" style="margin-top: 20px">
				<tbody>
					<tr>
						<td width="10%" height="26" align="left" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
								<tr>
									<td width="2" align="center" bgcolor="#f1f1f1"><input  type="checkbox"
										onclick="selectAll(this);" /></td>
									<td width="50" align="center" bgcolor="#f1f1f1">预订单编号</td>
									<td width="50" align="center" bgcolor="#f1f1f1">寄件人</td>
									<td width="50" align="center" bgcolor="#f1f1f1">手机号</td>
									<td width="50" align="center" bgcolor="#f1f1f1">固话</td>
									<td width="50" align="center" bgcolor="#f1f1f1">小件员</td>
									<td width="50" align="center" bgcolor="#f1f1f1">预约时间</td>
									<!-- 当分配情况为已分配时显示反馈结果，未分配时不显示反馈结果   -->
									 <%
										if(selectedDistributeCondition==3){
									%> 
									<td width="50" align="center" bgcolor="#f1f1f1">反馈结果</td>
									<%
										}
									%> 										
									<td width="100" align="center" bgcolor="#f1f1f1">取件地址</td>
								</tr>
							</table>

							<table id="todayToTakeTable" width="100%" border="0" cellspacing="1" cellpadding="2"
								class="table_2">
								<%
									if(preOrderList!=null&&!preOrderList.isEmpty())
																for (ExpressPreOrder preOrder : preOrderList) {
								%>
								<tr>
									<td width="2" align="center"><input id="selectedPreOrder" name="selectedPreOrder"
										type="checkbox" value="<%=preOrder.getId()%>" /></td>
									<td width="50" align="center"><%=preOrder.getPreOrderNo()%></td>
									<td width="50"><%=preOrder.getSendPerson()==null?"":preOrder.getSendPerson()%></td>
									<td width="50"><%=preOrder.getCellphone()==null?"":preOrder.getCellphone()%></td>
									<td width="50"><%=preOrder.getTelephone()==null?"":preOrder.getTelephone()%></td>
									<td width="50"><%=preOrder.getTelephone()==null?"":preOrder.getDelivermanName()%></td>
									<td width="50"><%=preOrder.getArrangeTime()==null?"":preOrder.getArrangeTime()%></td>
									<!-- 当分配情况为已分配时显示反馈结果，未分配时不显示反馈结果   -->
									<%
										if(selectedDistributeCondition==3){
									%> 
									<td width="50"><%=preOrder.getArrangeTime()==null?"":preOrder.getExcuteStateStr()%></td>
									<%
										}
									%>  											
									
									<td width="100" align="center"><%=preOrder.getCollectAddress()==null?"":preOrder.getCollectAddress()%></td>
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
			if(page_obj.getMaxpage()>1){
		%>
		<div class="iframe_bottom">
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff"><a
						href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();">第一页</a> <a
						href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious()%>');$('#searchForm').submit();">上一页</a>
						<a
						href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext()%>');$('#searchForm').submit();">下一页</a>
						<a
						href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage()%>');$('#searchForm').submit();">最后一页</a>
						共<%=page_obj.getMaxpage()%>页 共<%=page_obj.getTotal()%>条记录 当前第<select id="selectPg"
						onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
							<%
								for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {
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
		$("#selectPg").val(
			<%=request.getAttribute("page")%>
		);
		$("#returnResult option").each(function (){  
	        if($(this).val() == $("#returnResult_flag").html()){
	        	$(this).attr("selected","selected");
	        }
	     });  
		
	</script>
	<!-- 查询的ajax地址 -->
	<input type="hidden" id="search" value="<%=request.getContextPath()%>/stationOperation/search" />
	<!-- 分配的ajax地址 -->
	<input type="hidden" id="assign"
		value="<%=request.getContextPath()%>/stationOperation/openAssignDlg" />
	<!-- 站点超区的ajax地址 -->
	<input type="hidden" id="superzone"
		value="<%=request.getContextPath()%>/stationOperation/openSuperzoneDlg" />
	<!-- 导出的ajax地址 -->
	<input type="hidden" id="exportExcel"
		value="<%=request.getContextPath()%>/stationOperation/exportExcel" />
		<!-- liuwuqiang 9.29 -->
	<%-- <input value="${returnResult}" id="returnResultValue" type="hidden"/> --%>
	
	

	
</body>
</html>