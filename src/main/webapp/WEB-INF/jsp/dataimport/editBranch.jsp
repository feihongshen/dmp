<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
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
	List<Customer> customerlist = (List<Customer>) request.getAttribute("customers");
	List<Branch> branchlist = (List<Branch>) request.getAttribute("branchs");
	long emaildateidParam = request.getParameter("emaildate")==null?0:Long.parseLong(request.getParameter("emaildate").toString());
	List<EmailDate> emaildatelist = (List<EmailDate>) request.getAttribute("emaildateList");
	List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
	long SuccessAddress=(Long )request.getAttribute("SuccessAddress");
	long NotSuccess=(Long )request.getAttribute("NotSuccess");
	long SuccessEdit=(Long )request.getAttribute("SuccessEdit");
	long SuccessNew=(Long )request.getAttribute("SuccessNew");
	Page page_obj = request.getAttribute("page_obj")==null?new Page():(Page)request.getAttribute("page_obj");
	long branchidParam = request.getParameter("branchid")==null?0:Long.parseLong(request.getParameter("branchid").toString());
    String sessionid = session.getId();
    String addrDeliveryStationUrl = request.getAttribute("addrDeliveryStationUrl")==null?"":request.getAttribute("addrDeliveryStationUrl").toString();
    String addrUser = request.getAttribute("addrUser")==null?"":request.getAttribute("addrUser").toString();
    String addrPsw = request.getAttribute("addrPsw")==null?"":request.getAttribute("addrPsw").toString();
%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css"
	type="text/css" media="all" />
<%-- <script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"
	type="text/javascript"></script> --%>
<script
	src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/expdopmap.js"></script>
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=szTBW9236HO8EDCYuk4xQlP4"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
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
			var branchcode;
			if(data.errorCode==0){
				branchcode = data.branchcode;
				form.excelbranch.value=data.excelbranch;
				//form.exceldeliver.value=data.exceldeliver;
				var $inp = $("input:text[name='excelbranch']");
				var nxtIdx = $inp.index(form.excelbranch) + 1;
	            $("input:text[name='excelbranch']:eq(" + nxtIdx + ")").focus();
	            getCourier(branchcode, form.index.value);
			}else{
				form.excelbranch.value="";
			}
		}
	});
}

function selectPage(page){
		$("#page").val(page);
		$("#isshow").val("1");
		$("#editBranchForm").submit();
}
function resendaddressMsg(cwbs,emaildate,customerid,addressCodeEditType,onePageNumber,isshow,branchid){
	if(emaildate==0){
		alert("请选择批次!");
		return false;
	}else{
		$("#btnval1").attr("disabled","disabled");
		$("#btnval1").val("请稍后……");
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/dataimport/resendAddressmatch",
			data:{
				cwbs:cwbs,
				emaildate:emaildate,
				customerid:customerid,
				addressCodeEditType:addressCodeEditType,
				onePageNumber:onePageNumber,
				branchid:branchid,
				isshow:isshow
				},
			dataType : "json",
			success:function(data){
				alert(data.error);
				$("#btnval1").removeAttr("disabled","disabled");
				$("#btnval1").val("重新匹配");
				
			}
		});
	}
}

function searchForm(){
	if($("#cwbs").val().length==0&&$("#emaildate").val()==0){
		alert("请选择批次!");
		return false;
	}else{
		$("#editBranchForm").submit();
	}
}
function selectAll(){
	if($('input[name="machbranch"]:checked').size()>0){
		$('input[name="machbranch"]').each(function(){
			$(this).attr("checked",false);
		});
	}else{
		$('input[name="machbranch"]').attr("checked",true);
	}
}
function bdbranchmatch(){
	var matchcwbs="";
	var matchbranchcode=$("#matchbranchcode").val();
	var matchbranchname=$("#matchbranchname").val();
	$('input[name="machbranch"]:checked').each(function(){ //由于复选框一般选中的是多个,所以可以循环输出
		matchcwbs += $(this).val()+",";
		});
	if(matchcwbs==""){
		alert("请选择要通过地图匹配的订单号！");
	}else if(matchbranchname==""){
		alert("请在地图上选择所在站点区域！！");
	}else{
		matchcwbs.substring(0, matchcwbs.length-1);
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/dataimport/bdmatchbranch",
			data:{
				matchcwbs:matchcwbs,
				matchbranchcode:matchbranchcode,
				matchbranchname:matchbranchname
				},
			dataType : "json",
			success:function(data){
				if(data.errorCode==0){
					var successcwbs=data.successcwb.split(",");
					 for(var i=0;i<successcwbs.length;i++){
						 $("#ladd"+successcwbs[i]).val(data.excelbranch);
					 }
					alert(data.error);
				}else if(data.errorCode==1){
					alert(data.error);
				}else if(data.errorCode==111){
					
						var successcwbs=data.successcwb.split(",");
						for(var i=0;i<successcwbs.length;i++){
							 $("#ladd"+successcwbs[i]).val(data.excelbranch);
						 }
					alert(data.error);
				}else{
					alert(data.error);
				}
			}
		});
	}
}
/* $(function(){
	$("#customerid").combobox();
	}) */
</script>
</head>
<body style="background: #f5f5f5">

	<div class="right_box">
		<div class="menucontant">
			<div class="uc_midbg" id="uc_midbg">
				<ul>
					<li><a href="#" class="light">修改匹配信息</a></li>
					<li><a href="editBatchBranch">批量匹配</a></li>
					<li><a href="editBranchonBranch">匹配站按站</a></li>
					<li><a href="addresslibrarymatching">手动匹配</a></li>
					<!-- <li><a href="batchedit" >批量修改</a></li> -->
				</ul>
			</div>
			<form name="editBranchForm" id="editBranchForm" method="POST"
				action="editBranch">
				<table width="100%" height="23" border="0" cellpadding="0"
					cellspacing="5" class="right_set1">
					<tr id="customertr" class=VwCtr style="display:">
						<td>
						订单编号：<textarea cols="24" rows="4"  name ="cwbs" id="cwbs" ></textarea>
									<textarea cols="24" rows="4"  style="display:none" name ="cwbstr1"  >${cwbs}</textarea>
							 客户：<select
							name="customerid" id="customerid" class="select1"
							onchange="changeCustomerid()">
								<option value="0">请选择</option>
								<%
									for(Customer c : customerlist){
								%>
								<option value="<%=c.getCustomerid()%>"
									<%if((c.getCustomerid()+"").equals(request.getParameter("customerid"))){out.print("selected");}%>><%=c.getCustomername()%></option>
								<%
									}
								%>
						</select>发货批次： <select id="emaildate" name="emaildate" class="select1"
							style="height: 20px; width: 280px">
								<option value='0' id="option2">请选择(供货商_供货商仓库_结算区域)</option>
								<%
									if(!emaildatelist.isEmpty())for(EmailDate e : emaildatelist){
								%>
								<option value=<%=e.getEmaildateid()%>><%=e.getEmaildatetime()%><%=e.getState()==1?"（已到货）":""%>
									<%=e.getCustomername()+"_"+e.getWarehousename()+"_"+e.getAreaname()%></option>
								<%
									}
								%>
						</select> 每页<select name="onePageNumber" id="onePageNumber" class="select1"
							style="height: 20px; width: 50px">
								<option value="10">10</option>
								<option value="30">30</option>
								<option value="50">50</option>
								<option value="100">100</option>
						</select>行 <input type="button" class="input_button2" value="查询"
							onclick="searchForm();">
					</tr>
					<tr id="customertr" class=VwCtr style="display:">
						<td>导出模板：<select name="exportmould" id="exportmould"
							class="select1">
								<option value="0">默认导出模板</option>
								<%
									for(Exportmould e:exportmouldlist){
								%>
								<option value="<%=e.getMouldfieldids()%>"><%=e.getMouldname()%></option>
								<%
									}
								%>
						</select> <input type="button" id="btnval0" class="input_button2"
							value="导出" onclick="exportField();" /> (共<a
							href="javascript:$('#addressCodeEditType').val(-2);selectPage(1);"
							style="font-size: 18; font-weight: bold; color: red;">${AllAddress}</a>单
							地址库匹配<a <%if(SuccessAddress==0){%> href="#" <%} else{%>
							href="javascript:$('#addressCodeEditType').val(<%=CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue()%>);selectPage(1);"
							<%}%> style="font-size: 18; font-weight: bold; color: red;">${SuccessAddress}</a>
							修改匹配<a <%if(SuccessEdit==0){%> href="#" <%} else{%>
							href="javascript:$('#addressCodeEditType').val(<%=CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue()%>);selectPage(1);"
							<%}%> style="font-size: 18; font-weight: bold; color: red;">${SuccessEdit}</a>
							人工匹配<a <%if(SuccessNew==0){%> href="#" <%} else{%>
							href="javascript:$('#addressCodeEditType').val(<%=CwbOrderAddressCodeEditTypeEnum.RenGong.getValue()%>);selectPage(1);"
							<%}%> style="font-size: 18; font-weight: bold; color: red;">${SuccessNew}</a>
							未匹配<a <%if(NotSuccess==0){%> href="#" <%} else{%>
							href="javascript:$('#addressCodeEditType').val(<%=CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue()%>);selectPage(1);"
							<%}%> style="font-size: 18; font-weight: bold; color: red;">${NotSuccess}</a>)
							<input type="button" id="btnval1" class="input_button2"
							value="重新匹配"
							onclick='resendaddressMsg("<%=request.getParameter("cwbs")==null?"":request.getParameter("cwbs")%>",<%=emaildateidParam%>,<%=request.getParameter("customerid")==null?0:request.getParameter("customerid")%>,<%=request.getParameter("addressCodeEditType")==null?-1:request.getParameter("addressCodeEditType")%>,<%=request.getParameter("onePageNumber")==null?10:request.getParameter("onePageNumber")%>,1,<%=branchidParam%>);'>
							<input type="button" id="baidupipei" class="input_button2" value="站点匹配"  onclick='bdbranchmatch();'/>
							<input type="hidden" id="addressCodeEditType"
							name="addressCodeEditType" value="-1" /> <!-- 0为全部 1 为成功 2 为匹配 -->
							<input type="hidden" id="branchid" name="branchid" value="0" />
							<input type="hidden" id="page" name="page" value="1" /> <input
							type="hidden" id="isshow" name="isshow" value="1" /></td>
					</tr>
				</table>
			</form>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="50%" style="vertical-align:top;">
						<table id="Order" width="100%" border="0" cellspacing="1"
							cellpadding="0" class="table_2">
							<tr class="font_1">
								
								<td width="10%" align="center" height="19" align="center"
									valign="middle" bgcolor="#eef6ff">操作[<a href="javascript:void(0)" style="color:blue;" onclick="selectAll();">全选/反选</a>]</td>
								<td width="10%" align="center" height="19" align="center"
									valign="middle" bgcolor="#eef6ff">订单号</td>
								<!-- <td width="10%" align="center" align="center" valign="middle"
									bgcolor="#eef6ff">收件人</td>
								<td width="10%" align="center" align="center" valign="middle"
									bgcolor="#eef6ff">手机</td>
								<td width="8%" align="center" align="center" valign="middle"
									bgcolor="#eef6ff">订单类型</td> -->
								<td width="10%" align="center" align="center" valign="middle"
									bgcolor="#eef6ff">订单状态</td>
								<!-- <td width="5%" align="center" align="center" valign="middle"
									bgcolor="#eef6ff">邮编</td> -->
							 	<td width="25%" align="center" align="center" valign="middle"
									bgcolor="#eef6ff">收件地址</td> 
								<td width="15%" align="center" align="center" valign="middle"
									bgcolor="#eef6ff">匹配到站（回车保存）</td>
								<td width="20%" align="center" align="center" valign="middle"
									bgcolor="#eef6ff">匹配到小件员</td>
								<td width="10%" align="center" align="center" valign="middle"
									bgcolor="#eef6ff">操作</td>
							</tr>
							<c:forEach var="vo" varStatus="voStatus" items="${cwbOrderBranchMatchVoList}">
								<tr>
									<form id="f${vo.cwbOrder.cwb }" method="POST" onSubmit="subEdit(this);return false;" action="editexcel/${vo.cwbOrder.cwb }">
										<input type="hidden" name="index" value="${voStatus.index }">
										<td width="10%" align="center" ><input id="machbranch" name="machbranch" type="checkbox" value="${vo.cwbOrder.cwb }" /></td>
										<td width="10%" align="center" height="19">${vo.cwbOrder.cwb }</td>
										<td width="10%" align="center">${vo.flowordertypeVal }</td>
										<td width="25%" align="left" id="add${vo.cwbOrder.cwb }">${vo.cwbOrder.consigneeaddress}</td>
										<td width="15%" align="center">
											<input type="text" style="width:100px;" id="ladd${vo.cwbOrder.cwb }" name="excelbranch" value="${vo.cwbOrder.excelbranch }"
												onfocus="$('#add${vo.cwbOrder.cwb }').css('background','#bbffaa');"
												onblur="$('#add${vo.cwbOrder.cwb }').css('background','#ffffff');" />
										</td>
										<td width="20%" align="left">
											<select id="courier_${voStatus.index }" name="courier" class="easyui-combobox" style="width:120px;">
												<option value="" selected="selected">请选择</option>
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
						</table> <%
 	if(page_obj.getMaxpage()>1){
 %>
						<div class="iframe_bottom" id="view_page">
							<table width="50%" border="0" cellspacing="1" cellpadding="0"
								class="table_1">
								<tr>
									<td height="38" align="center" valign="middle"
										bgcolor="#eef6ff"><a
										href="javascript:$('#addressCodeEditType').val(<%=request.getParameter("addressCodeEditType")==null?-1:request.getParameter("addressCodeEditType")%>);$('#branchid').val(<%=branchidParam%>);selectPage(1);">第一页</a>
										<a
										href="javascript:$('#addressCodeEditType').val(<%=request.getParameter("addressCodeEditType")==null?-1:request.getParameter("addressCodeEditType")%>);$('#branchid').val(<%=branchidParam%>);selectPage(<%=page_obj.getPrevious()<1?1:page_obj.getPrevious()%>);">上一页</a>
										<a
										href="javascript:$('#addressCodeEditType').val(<%=request.getParameter("addressCodeEditType")==null?-1:request.getParameter("addressCodeEditType")%>);$('#branchid').val(<%=branchidParam%>);selectPage(<%=page_obj.getNext()<1?1:page_obj.getNext()%>);">下一页</a>
										<a
										href="javascript:$('#addressCodeEditType').val(<%=request.getParameter("addressCodeEditType")==null?-1:request.getParameter("addressCodeEditType")%>);$('#branchid').val(<%=branchidParam%>);selectPage(<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage()%>);">最后一页</a>
										共<%=page_obj.getMaxpage()%>页 共<%=page_obj.getTotal()%>条记录 当前第<select
										id="selectPg"
										onchange="$('#addressCodeEditType').val(<%=request.getParameter("addressCodeEditType")==null?-1:request.getParameter("addressCodeEditType")%>);$('#branchid').val(<%=branchidParam%>);selectPage($(this).val());">
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
						</div> <%
 	}
 %>
					</td>
					<td width="50%">
						<div id="banchMap" style="height:380px;width:100%;"></div>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<form action="<%=request.getContextPath()%>/dataimport/exportExcle"
		method="post" id="searchForm2">
	<%-- 	<input type="text" name="cwb1" id="cwb1"
			value="<%=request.getParameter("cwbs")==null?"":request.getParameter("cwbs")%>" /> --%>
		<textarea hidden="hidden" name="cwb1" id="cwb1"><%=request.getParameter("cwbs")==null?"":request.getParameter("cwbs")%></textarea>
		<input type="hidden" name="emaildate1" id="emaildate1"
			value="<%=emaildateidParam%>" /> <input type="hidden"
			name="customerid1" id="customerid1"
			value="<%=request.getParameter("customerid")==null?"0":request.getParameter("customerid")%>" />
		<input type="hidden" name="addressCodeEditType1"
			id="addressCodeEditType1"
			value="<%=request.getParameter("addressCodeEditType")==null?-1:request.getParameter("addressCodeEditType")%>" />
		<input type="hidden" id="branchid1" name="branchid1"
			value="<%=branchidParam%>" /> <input type="hidden"
			name="exportmould2" id="exportmould2" />
	</form>
	<form action="<%=request.getContextPath()%>/dataimport/bdmatchbranch" id="bdmatchcwbs" method="post">
		<!-- <input type="hidden" id="bdmatchcwbs" name="bdmatchcwbs" /> -->
		<input type="hidden" id="matchbranchcode" name="matchbranchcode" value=""/>
		<input type="hidden" id="matchbranchname" name="matchbranchname" value=""/>
	</form>
	<script type="text/javascript">
$(function(){
	$("#selectPg").val(<%=request.getAttribute("page")%>); 
	$("#onePageNumber").val(<%=request.getParameter("onePageNumber")==null?"10":request.getParameter("onePageNumber")%>);
	$("#emaildate").val(<%=emaildateidParam%>);
	$("#branchid").val(<%=branchidParam%>);
});

function getCourier(branchcode, index) {
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

function initCourier(index, courierList) {
	if(courierList == null) {
		courierList = new Array();
	}
	var $courier = $("#courier_" + index);
	var data = new Array();
	$courier.empty();
	$courier[0].add(new Option("请选择", "", true, true));
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
	var courierName = $("#courier_" + index).combobox("getValue");
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

function exportField(){
	var listSize = ${fn:length(cwbOrderBranchMatchVoList)};
	if($("#isshow").val()=="1"&& listSize > 0){
		$("#exportmould2").val($("#exportmould").val());
		$("#btnval0").attr("disabled","disabled");
	 	$("#btnval0").val("请稍后……");
	 	$("#searchForm2").submit();
	}else{
		alert("没有做查询操作，不能导出！");
	};
}

function changeCustomerid(){
	 $.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/emaildate/getEmailDateList",
		data:{customerids:$("#customerid").val(),
			  state:1
		},
		success:function(data){
			var optionstring="";
			var high ="";
			optionstring+="<option value='0'>请选择(供货商_供货商仓库_结算区域)</option>";
			for(var i=0;i<data.length;i++){
				optionstring+="<option value='"+data[i].emaildateid+"'>"+
				data[i].emaildatetime+(data[i].state==1?"（已到货）":"")+" "+
				data[i].customername+"_"+data[i].warehousename+"_"+data[i].areaname
				+"</option>";
			}
			$("#emaildate").html(optionstring);
		}
	});
}



/**
 *  地图加载
 */
 //"uc_midbg"  editBranchForm
 
 // 
 

 (function loadMap(){
	 
	 // 计算高度
	 
	 $("#banchMap").height($(document).height()-$("#uc_midbg").height()-$("#editBranchForm").height()-5);
	 
	var mapManager=new ExpLink.ExpdopMap();
	mapManager.initializeMap({
		map:"banchMap"
	});
	mapManager.initializeDeliveryStation();
    var deliveryStation = mapManager.getDeliveryStation();
    
    // url 需要获取
    var url = "<%=addrDeliveryStationUrl%>";
    
    $.ajax({
        type: "get",
        url: url,
        dataType: "jsonp",
        jsonp: "callback",
        data:"<%=addrUser%><%=addrPsw%>dmpid=<%=sessionid%>&callback=?",
        error: function (e)
        {
        },
        success: function (data)
        {
            deliveryStation.setDeliveryStationItems(data);
         // 设置视域
            setTimeout(function(){
            	deliveryStation.setViewportToAllStationRegion();
            },300);
        }
    });
    
    // 点击区域
    //ExpLink.DeliveryStationEventType.STATIONREGIONCLICK
    mapManager.add(ExpLink.DeliveryStationEventType.STATIONREGIONCLICK,function(e){
       var info=e;
       $("#matchbranchcode").val(e.target.id);
       $("#matchbranchname").val(e.target.name);
       //alert(e.target.id);
       //alert(e.target.name);
    });
    
    // 设置视域
    setTimeout(function(){
    	deliveryStation.setViewportToAllStationRegion();
    },2000);
   
    // 获取当前选中区域的id
    //deliveryStation.getSelectedDeliveryStationID();
    
    
    
    
})();



</script>
</body>
</html>
