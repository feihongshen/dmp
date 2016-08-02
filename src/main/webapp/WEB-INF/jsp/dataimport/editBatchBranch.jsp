<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbFlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@page import="cn.explink.domain.EmailDate"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="java.util.List,java.util.ArrayList"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.util.Page"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
	List<Branch> branchlist = (List<Branch>) request.getAttribute("branchs");
	
	Page page_obj = request.getAttribute("page_obj")==null?new Page():(Page)request.getAttribute("page_obj");
	//String showphoneflag = session.getAttribute("showphoneflag")==null?"0":(String)session.getAttribute("showphoneflag");
	String error = request.getAttribute("error")==null?"":request.getAttribute("error").toString();
%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<script>
function subEdit(form){
	if(form.excelbranch.value.length==0){
		alert("请输入站点");
		return false;
	}
	$(".tishi_box",parent.document).hide();
	$.ajax({
		type: "POST",
		url:$(form).attr("action"),
		data:$(form).serialize(),
		dataType:"json",
		success : function(data) {
			$(".tishi_box",parent.document).html(data.error);
			$(".tishi_box",parent.document).show();
			setTimeout("$(\".tishi_box\",parent.document).hide(1000)", 2000);
			if(data.errorCode==0){
				form.excelbranch.value=data.excelbranch;
				//form.exceldeliver.value=data.exceldeliver;
				var $inp = $("input:text[name='excelbranch']");
				var nxtIdx = $inp.index(form.excelbranch) + 1;
	            $("input:text[name='excelbranch']:eq(" + nxtIdx + ")").focus();
	             getCourierByCode(data.branchcode, form.index.value);
			}else{
				form.excelbranch.value="";
			}
		}
	});
}
function getCourierByCode(branchcode, index) {
	if(branchcode == null || branchcode == "") {
		initCourier(index);
		return;
	}
	$.ajax({
		type: "POST",
        url: "<%=request.getContextPath() %>/dataimport/getCourierByBranch",
        data: {branchcode:branchcode},
        success: function(data) {
        	initCourier(index, data);
        },
        error: function() {
        	initCourier(index);
        	alert("获取小件员时发生错误！"); 
        }    
    });
}

function getCourier(index) {
	var branchid = $("#branchcode").combobox("getValue");
	var branch = $("#excelbranch").val().replace(/(^\s*)|(\s*$)/g,"");
	if (branch == "输入站点名称或者站点编号") {
		branch = "";
	}
	var branchcode;
	if(branchid.length>0&&branch.length>0){
		alert("下拉框与输入框只能选一个");
		return false;
	}
	branchcode = branchid.length > 0 ? branchid : branch;
	if(branchcode == null || branchcode == "") {
		initCourier(index);
		return;
	}
	$.ajax({
		type: "POST",
        url: "<%=request.getContextPath() %>/dataimport/getCourierByBranch",
        data: {branchcode:branchcode},
        dataType: "json",
        success: function(data) {
        	initCourier(index, data);
        },
        error: function() {
        	initCourier(index);
        	alert("获取小件员时发生错误！"); 
        }    
    });
}

function initCourier(index, courierList) {
	if(courierList == null) {
		courierList = new Array();
	}
	var $courier;
	if(index == null) {
		$courier = $("#courierName");
	} else {
		$courier = $("#courier_" + index);
	}
	var data = new Array();
	$courier.empty();
	$courier[0].add(new Option("", "", true, true));
	data.push({"id":"", "text":"请选择"});
	$.each(courierList, function(i, courier) { 
		$courier[0].add(new Option(courier.realname, courier.username));
		data.push({"id":courier.username, "text":courier.realname});
	});
	$courier.combobox({
        url: null,
        valueField: 'id',
        textField: 'text',
        data: data,
        onLoadSuccess: function () {
        	$(this).combobox("select", "");
        }
    });
}

function saveBranchAndCourier(cwb, index) {
	var courierName = $("#courier_" + index).val();
	$.ajax({
		type: "POST",
        url: "<%=request.getContextPath() %>/dataimport/saveCourier",
        data: {cwb:cwb, courierName:courierName},
        dataType: "json",
        success: function(data) {
        	$(".tishi_box",parent.document).html(data.error);
			$(".tishi_box",parent.document).show();
			setTimeout("$(\".tishi_box\",parent.document).hide(1000)", 2000);
        }    
    });
}

function selectPage(page){
		$("#page").val(page);
		$("#isshow").val("1");
		$("#editBranchForm").submit();
}
function showMsg(){
		if(<%="1".equals(request.getAttribute("msg")) %>){
			alert("无此站点");
		}
}
 function selectForm(){
	 if($("#cwbs").val() == ''){
		 alert("请输入订单号");
		 return false;
	 }
	 var branchid=$("#branchcode").combobox("getValue");
	 var branch=$("#excelbranch").val().replace(/(^\s*)|(\s*$)/g,"");
	 if(branchid.length>0&&branch.length>0){
		 if(branch!='输入站点名称或者站点编号'){
			 alert("下拉框与输入框只能选一个");
			 return false;
		 }
	 } 
	 if(branch == '输入站点名称或者站点编号' || $("#excelbranch").val() == '' ){
		 if(!branchid.length>0){
			 alert("请输入站点或者站点编号");
			 return false;
		 }
	 }
	 $("#selectButton").attr("disabled","disabled");
	 $("#selectButton").val("请稍候");
	 $("#editBranchForm").submit();
 }
</script>
</head>
<body  style="background:#f5f5f5" onload="showMsg()">

<div class="right_box">
	<div class="menucontant">
	<div class="uc_midbg">
		<ul>	
			<li><a href="editBranch">修改匹配信息</a></li>
			<li><a href="editBatchBranch" class="light">批量匹配</a></li>
			<li><a href="editBranchonBranch">匹配站按站</a></li>
			<li><a href="addresslibrarymatching">手动匹配</a></li>
			<!-- <li><a href="batchedit" >批量修改</a></li> -->
		</ul>
	</div>
	<form name="editBranchForm" id="editBranchForm" method="POST" action="editBatchBranch"  >
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
				<tr id="customertr" class=VwCtr style="display:">
					<td width="100%" colspan="2">
					订单号：<textarea cols="24" rows="4"  name ="cwbs" id="cwbs" ><%if("1".equals(request.getAttribute("msg"))){ %><%=request.getParameter("cwbs")%><%} %></textarea>
					匹配站点：<%if(branchlist!=null&&branchlist.size()>0){%>
						<select id="branchcode" name="branchcode" onchange="getCourier()" style="width:150px;" class="easyui-combobox">
								<option value="" selected="selected">--请选择站点--</option>
								<%for( Branch b:branchlist){ %>
									<option value="<%=b.getBranchcode() %>"><%=b.getBranchname() %></option>
								<%}%>
								</select>
															
							<%}%>
						<input type="text" id="excelbranch" name="excelbranch" size="30" 
							onblur="if(this.value==''){this.value='输入站点名称或者站点编号'}"
							onfocus="if(this.value=='输入站点名称或者站点编号'){this.value=''}" onchange="getCourier()" value="输入站点名称或者站点编号"  />
						<select id="courierName" name="courierName" style="width: 150px;" class="easyui-combobox">
							<option value="" selected="selected">请选择</option>
						</select>
						<input type="button"  class='input_button2' value="确认匹配"  onclick="selectForm();" id="selectButton"/>
							<!-- <input style="width: 130px; height:20px; border:none; background-color: #ADEAEA ; cursor:pointer; padding:0; text-align:left;" type="button" id="btnval0" value="导出所有未匹配订单" onclick="exportField();"/><br/> -->
					<%if(error.length()>0 ){ %><br/><font color="red"><%=error %> </font><%} %>
					<%if("1".equals(request.getParameter("isshow")) ){ %>已匹配 <font color="red"><%=request.getAttribute("count") %> </font>单<%} %>	
					</td>
				</tr>
				<tr class=VwCtr style="display:">
					<td width="100%" colspan="2">
					站点表：
					<%for(Branch b : branchlist){ out.print("<span  style='background-color:#eef6ff;' >"+b.getBranchname()+"("+b.getBranchcode()+")</span>　"); }%>
					</td>
					
				</tr>
			</table>
			<input type="hidden" id="isshow" name="isshow" value="1" />
		</form>
				<table id="Order" width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" >
					<tr class="font_1">
						<td width="10%" align="center" height="19" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
						<td width="10%" align="center" align="center" valign="middle" bgcolor="#eef6ff">收件人</td>
						<td width="10%" align="center" align="center" valign="middle" bgcolor="#eef6ff">手机</td>
						<td width="8%" align="center" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
						<td width="8%" align="center" align="center" valign="middle" bgcolor="#eef6ff">订单状态</td>
						<td width="5%" align="center" align="center" valign="middle" bgcolor="#eef6ff">邮编</td>
						<td width="29%" align="center" align="center" valign="middle" bgcolor="#eef6ff">收件地址</td>
						<td width="10%" align="center" align="center" valign="middle" bgcolor="#eef6ff">匹配到站（回车保存）</td>
						<td width="10%" align="center" height="38" align="center" valign="middle" bgcolor="#eef6ff">匹配到小件员</td>
 						<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
 					</tr>
					<c:forEach var="vo" varStatus="voStatus" items="${cwbOrderBranchMatchVoList}">
						<tr>
							<form id="f${vo.cwbOrder.cwb }" method="POST" onSubmit="subEdit(this);return false;" action="editexcel/${vo.cwbOrder.cwb }">
								<input type="hidden" name="index" value="${voStatus.index }">
								<td align="center" height="19">${vo.cwbOrder.cwb }</td>
								<td align="center">${vo.cwbOrder.consigneename}</td>
								<td align="center">${vo.cwbOrder.consigneemobile}</td>
								<td align="center">${vo.flowordertypeVal }</td>
								<td align="center">${vo.orderTypeVal }</td>
								<td align="center">${vo.cwbOrder.consigneepostcode}</td>
								<td align="left" id="add${vo.cwbOrder.cwb }">${vo.cwbOrder.consigneeaddress}</td>
								<td align="left">
									<input type="text" style="width:100px;" id="ladd${vo.cwbOrder.cwb }" name="excelbranch" value="${vo.cwbOrder.excelbranch }"
												onfocus="$('#add${vo.cwbOrder.cwb }').css('background','#bbffaa');"
												onblur="$('#add${vo.cwbOrder.cwb }').css('background','#ffffff');" />
								</td>
								<td align="left">
									<select id="courier_${voStatus.index }" name="courier" class="easyui-combobox" style="width:120px;">
										<option value="" selected="selected"></option>
										<c:forEach var="courier" items="${vo.courierList }">
											<option value="${courier.username }" <c:if test="${vo.cwbOrder.exceldeliverid eq courier.userid }">selected="selected"</c:if>>${courier.realname }</option>
										</c:forEach>
									</select>
								</td>
								<td width="10%" align="center">
									<input type="button" class="input_button2" onclick="saveBranchAndCourier('${vo.cwbOrder.cwb }', '${voStatus.index }')" value="保存小件员"/>
								</td>
							</form>
						</tr>
					</c:forEach>
				</table>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				</div>
				<%-- <%if(page_obj.getMaxpage()>1){ %>
				<div class="iframe_bottom" id="view_page" >
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
						<a href="javascript:selectPage(1);" >第一页</a>　
						<a href="javascript:selectPage(<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>);">上一页</a>　
						<a href="javascript:selectPage(<%=page_obj.getNext()<1?1:page_obj.getNext() %>);" >下一页</a>　
						<a href="javascript:selectPage(<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>);" >最后一页</a>
						　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
								id="selectPg"
								onchange="selectPage($(this).val());">
								<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
								<option value="<%=i %>" ><%=i %></option>
								<% } %>
							</select>页
					</td>
				</tr>
				</table>
				</div>
				<script type="text/javascript">
				$(function(){
					$("#selectPg").val(<%=request.getAttribute("page") %>);
					$("#onePageNumber").val(<%=request.getParameter("onePageNumber")==null?"10":request.getParameter("onePageNumber") %>);
				});
				</script>
				<%} %> --%>
		
</div>
<form action="<%=request.getContextPath()%>/dataimport/exportExcleNoPIPei" method="post" id="searchForm2">
</form>
<script type="text/javascript">
function exportField(){
	$("#exportmould2").val($("#exportmould").val());
	$("#btnval0").attr("disabled","disabled");
 	$("#btnval0").val("请稍后……");
 	$("#searchForm2").submit();
}

</script>
</body>
</html>
