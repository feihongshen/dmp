
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.util.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link href="<%=request.getContextPath()%>/css/multiple-select.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiple.select.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/easyui/datagrid.pagnation.js" type="text/javascript"></script>
 
<script type="text/javascript">

$(function(){
	
	 
	
	$("#applyUserid").multipleSelect({
		placeholder: "全部",
        filter: true,
        single: false
    });
	
	//机构级联用户下拉菜单
	$("#branchid").change(function(){
		
		var branchid = $(this).val();
		$.ajax({
			type: "POST",
			url:"${pageContext.request.contextPath}/user/getUsersByBranchids?branchid="+branchid,
			dataType:"json",
			success : function(userList) {
				//清空用户下拉菜单
				$("#applyUserid").empty();
				if(userList!=null && userList!=''){
					for(var i=0;i<userList.length;i++){
						var user = userList[i];
						var option = $("<option>").val(user.userid).text(user.realname);
						$("#applyUserid").append(option);
					}
				}
				$("#applyUserid").multipleSelect();	
			},
			complete:function(){
			}
		});
	});
	//全选branchid
	selectAll("branchid"); 
	//放到这里是为了selectAll后再进行初始化
	$("#branchid").multipleSelect({
        placeholder: "请选择",
        filter: true,
        single: false
    });
	//搜索按钮单击事件
	$("#btnSearch").click(function(){
		var startApplyTime = $("#startApplyTime").datetimebox('getValue');
		var endApplyTime = $("#endApplyTime").datetimebox('getValue');
		if(startApplyTime>endApplyTime && endApplyTime !=''){
			alert("开始时间不能大于结束时间");
			return;
		}
		$.DgPage.searchPage();
	});
	//审核通过按钮单击事件
	$("#btnReviewPass").click(function(){
		submitForReview(true);
	});
	//审核不通过按钮单击事件
	$("#btnReviewDenied").click(function(){
		submitForReview(false);
	});
	//导出excel单击事件
	$("#btnExport").click(function(){
		var startApplyTime = $("#startApplyTime").datetimebox('getValue');
		var endApplyTime = $("#endApplyTime").datetimebox('getValue');
		if(startApplyTime>endApplyTime && endApplyTime !=''){
			alert("开始时间不能大于结束时间");
			return;
		}
		var action="${pageContext.request.contextPath}/applyediteditcartype/exportExcel";
		$("#searchForm").attr('action',action);
		$("#searchForm").submit();
	});

	$.DgPage.init({
		url: "${pageContext.request.contextPath}/applyediteditcartype/apiReviewList",
		formId: "searchForm",
		dgId: "dg",
		pageParamName: "page",
		pageSizeParamName: "pageSize",
		loadNow:true
	});
	/* 如果需重写数据处理方法，可参照下面的定义
	$.DgPage.formatDgPageData = function(data){
		return {
			total:data.total ,
    		rows: data.list
		};
	}
	*/

});

//提交审核申请（通过/不通过）
function submitForReview(isReviewPass){
	var rows = $('#dg').datagrid('getChecked');//返回所有被选中的行
	if(rows==''){
		alert("请勾选需要审核的记录");
		return ;
	}
	var ids = "";
	for(var i=0;i<rows.length;i++){
		var vo = rows[i];
		if(vo.reviewStatus>0){
			alert("存在已审核的记录，请剔除后重新提交");
			return;
		}else{
			ids = ids + vo.id;
			if(i<rows.length-1){
				ids = ids + ",";
			}
		}
	}
	$("#reviewTips").html("正在审核，请稍后...");
	$.ajax({
		type: "POST",
		url:"${pageContext.request.contextPath}/applyediteditcartype/apiReview",
		data: {
			applyIds:ids,
			isReviewPass:isReviewPass
		}, 
		dataType:"json",
		success : function(data) {
			$("#reviewTips").empty();
           if(data!=null){
        	   $.DgPage.searchPage();
        	   outputSubmitResult(data);
        	   $('#result').dialog('open');
           }
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			$("#result").empty();
			$('#result').html("错误信息如下，请重试，如仍然出现，请联系技术人员："+errorThrown);
		},
		complete : function(){
			$("#reviewTips").empty();
		}
	});
	
}


//输出审核操作提示信息
function outputSubmitResult(data) {
	var succList = null;
	var failList = null;
	if(data!=null && data.succList!=null){
		succList = data.succList;
	}
	if(data!=null && data.failList!=null){
		failList = data.failList;
	}
	var succCount = 0;
	var failCount = 0;
	if(succList!=null){
		succCount = succList.length;
	}
	if(failList!=null){
		failCount = failList.length;
	}
	var $result = $("#result");
	$result.empty();
	var LINE_HTML_CONST = "<hr style='height:1px;border:none;border-top:1px solid #86AFD5;' />"
	
	appendResult($result, "提交成功: " + succCount + " 条," + "失败: "+ failCount + " 条");
	$result.append(LINE_HTML_CONST);
	appendResult($result, "成功: " + succCount + " 条:");
	appendDataList($result,succList);
	$result.append(LINE_HTML_CONST);
	appendResult($result, "失败: " + failCount + " 条:");
	appendDataList($result,failList);
}

function appendDataList($result,dataList){
	if(dataList == null || dataList.length<1){
		return ;
	}
	var content = "<table width='98%' >";
	for(var i=0; i<dataList.length ; i++){
		var opsResult = dataList[i];
		content = content +"<tr>"
						  +  "<td width='45%'>"+opsResult.cwb+   "</td>"
						  +  "<td width='50%'>"+opsResult.remark+"</td>"
						  +"</tr>";
		
	}
	content = content + "</table>";
	$result.append(content);
}
function appendResult($result, message) {
	$result.append("<font size=4>" + message + "</font>");
}

function selectAll(selectorId)
{
	
 	$("#"+selectorId).children().each(function(){$(this).attr("selected","selected")});
 	$("#"+selectorId).change();
}
</script>
</head>

<body style="background:#f5f5f5">
<div class="right_box">
	<div class="inputselect_box">
<form action="${pageContext.request.contextPath}/applyediteditcartype/reviewlist" method="post" id="searchForm">

<table width="100%"  >
<tr>
<td width="6%" align="right" rowspan="2" valign="middle">订单号：</td>
<td width="15%" rowspan="2" valign="bottom" ><textarea  name="cwbs" id="cwbs" style="width: 100%;height:100%;resize: none;">${param.cwbs}</textarea> </td>
<td width="7%" align="right">申请机构： </td>
<td width="13%" >
<select style="width: 100%" name="branchid" id="branchid" multiple="multiple">
		 <c:forEach var="branch" items="${branchList}">
			<option value="${branch.branchid}" <c:if test="${param.branchid==branch.branchid}">selected="selected"</c:if>>${branch.branchname}</option>
		</c:forEach>
</select>
</td>
<td width="7%" align="right" > 申请人：</td>
<td width="13%" >
<select style="width: 100%" id="applyUserid" name="applyUserid" multiple="multiple" >
	<c:forEach var="user" items="${userList}">
			<option value="${user.userid}" <c:if test="${param.applyUserid==user.userid}">selected="selected"</c:if>>${user.realname}</option>
		</c:forEach>
</select>
</td>
<td width="9%" align="right">申请时间(起)：</td>
<td width="13%" >
<input type ="text" name ="startApplyTime" id="startApplyTime"  value="${param.startApplyTime}" maxlength="19" style="width:150px" class="easyui-datetimebox"/>
</td>
<td width="6%"> </td>
</tr>

<tr>

<td  align="right">审核结果： </td>
<td  >
<select style="width:100%" id="isReview" name="isReview" >
	<option value =""> 全部</option>
	<option value ="false" <c:if test="${not empty param.isReview && param.isReview==false}">selected="selected"</c:if>>未审核</option>
	<option value ="true" <c:if test="${not empty param.isReview && param.isReview==true}">selected="selected"</c:if>>已审核</option>
</select>
</td>
<td  align="right" > 审核状态： </td>
<td  >
<select style="width:100%" id="reviewStatus" name="reviewStatus" >
	<option value =""> 全部</option>
	<c:forEach var="reviewStatus" items="${reviewStatusList}">
		<option value="${reviewStatus.value}" <c:if test="${not empty param.reviewStatus &&  param.reviewStatus==reviewStatus.value}">selected="selected"</c:if>>${reviewStatus.text}</option>
	</c:forEach>
</select>
</td>
<td  align="right"> 申请时间(止)：</td>
<td>
<input type ="text" name ="endApplyTime" id="endApplyTime"  value="${param.endApplyTime}" maxlength="19" style="width:150px" class="easyui-datetimebox"/>
</td>
</tr>
<tr>
<td><input type="button" class="input_button2" id="btnSearch" value="查询"/> </td>
<td>
<c:if test="${isCS}">
	<input type="button" class="input_button2" id="btnReviewPass" value="审核通过"/>
	<input type="button" class="input_button2" id="btnReviewDenied" value="审核不通过"/>
</c:if>
</td>
<td><input type="button" class="input_button2" id="btnExport" value="导出"/> </td>
<td><div id="reviewTips"></div></td>
<td></td>
<td> </td>
<td> </td>
<td colspan="2"> 
</td>
</tr>
<tr><td colspan="6">

	</div></td>
</tr>
 </table>

	</div>		
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	
    <div  data-options="region:'center'" style="overflow-x:auto;overflow-y:auto;width:100%;height:78%;">
      <table id="dg" class="easyui-datagrid" 
            showFooter="true" fit="true" fitColumns="false"  singleSelect="true" checkOnSelect = "false" selectOnCheck="false"
			width="100%" pageSize="10" rownumbers="true" pagination="true"  pageList="[10,30,50]" >
         <thead>
         	<th field="id" align="center" width="50px" checkbox="true">全选/取消</th>
         	<th field="cwb" align="center" width="120px" >订单号</th>
			<th field="transcwb" align="center" width="120px" >运单号 </th>
			<th field="customername" align="center" width="100px" >客户</th>
			<th field="doTypeName" align="center" width="100px" >订/运单类型</th>
			<th field="originalCartype" align="center" width="100px" >原货物类型</th>
			<th field="applyCartype" align="center" width="120px" >申请修改货物类型</th>
			<th field="carsize" align="center" width="100px" >长宽高（mm）</th>
			<th field="carrealweight" align="center" width="100px" >重量（kg）</th>
			<th field="applyBranchname" align="center" width="80px" >申请机构</th>
			<th field="applyUsername" align="center" width="50px" >申请人</th>
			<th field="applyTime" align="center" width="130px" >申请时间</th>
			<th field="reviewUsername" align="center" width="50px" >审核人</th>
			<th field="reviewTime" align="center" width="130px" >审核时间</th>
			<th field="remark" align="center" width="130px" >备注</th>
         </thead>
   	  </table>
   	
   	</div>
</div>

</form>	
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<div class="clear"></div>
</div>

<div id="result" class="easyui-dialog" closed="true" title="操作成功" data-options="iconCls:'icon-save'" style="width:500px;height:450px;padding:10px;">
</div>

</body>
</html>

