<%@ page language="java" import="java.util.*,cn.explink.domain.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title></title>
<c:set var="ctx_path" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx_path}/css/2.css" type="text/css">
<link rel="stylesheet" href="${ctx_path}/css/index.css" type="text/css">
<script src="${ctx_path}/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="${ctx_path}/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="${ctx_path}/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="${ctx_path}/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${ctx_path}/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css"
	media="all" />
<script src="${ctx_path}/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="${ctx_path}/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="${ctx_path}/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="${ctx_path}/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="${ctx_path}/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script src="${ctx_path}/js/json2.min.js" type="text/javascript"></script>
<%
int exportFlag=0;
OverdueResultVO overdueResultVO=request.getAttribute("request")==null?null:(OverdueResultVO)request.getAttribute("request");
if(overdueResultVO!=null&&overdueResultVO.getBranchMap()!=null&&overdueResultVO.getBranchMap().size()>0){
	exportFlag=1;
}

%>
<script>
	function check() {
		if ($("#startTime").val() == "") {
			alert("请选择开始时间");
			return false;
		}
		if ($("#endTime").val() == "") {
			alert("请选择结束时间");
			return false;
		}
		if ($("#starTime").val() > $("#endTime").val()) {
			alert("开始时间不能大于结束时间");
			return false;
		}
		var orgs = getMultiSelectValues("orgs");
		if (orgs.length == 0) {
			alert("请选择机构");
			return false;
		}
		if ($("#venderId").val() == 0) {
			alert("请选供应商");
			return false;
		}
		return true;
	}
	function Days() {
		var day1 = $("#strtime").val();
		var day2 = $("#endtime").val();
		var y1, y2, m1, m2, d1, d2;//year, month, day;   
		day1 = new Date(Date.parse(day1.replace(/-/g, "/")));
		day2 = new Date(Date.parse(day2.replace(/-/g, "/")));
		y1 = day1.getFullYear();
		y2 = day2.getFullYear();
		m1 = parseInt(day1.getMonth()) + 1;
		m2 = parseInt(day2.getMonth()) + 1;
		d1 = day1.getDate();
		d2 = day2.getDate();
		var date1 = new Date(y1, m1, d1);
		var date2 = new Date(y2, m2, d2);
		var minsec = Date.parse(date2) - Date.parse(date1);
		var days = minsec / 1000 / 60 / 60 / 24;
		if (days > 7) {
			return false;
		}
		return true;
	}
</script>

<script>
	$(function() {
		$("#startTime").datetimepicker({
			changeMonth : true,
			changeYear : true,
			hourGrid : 4,
			minuteGrid : 10,
			timeFormat : 'hh:mm:ss',
			dateFormat : 'yy-mm-dd'
		});
		$("#endTime").datetimepicker({
			changeMonth : true,
			changeYear : true,
			hourGrid : 4,
			minuteGrid : 10,
			timeFormat : 'hh:mm:ss',
			dateFormat : 'yy-mm-dd'
		});
		$("#orgs").multiSelect({
			oneOrMoreSelected : '*',
			noneSelected : '请选择'
		});

		$("#timeType").multiSelect({
			oneOrMoreSelected : '*',
			noneSelected : '请选择'
		});
		$("#showCols").multiSelect({
			oneOrMoreSelected : '*',
			noneSelected : '请选择'
		});

	});
	$(function() {
		$("#find").click(function() {
			if (check()) {
				var $searchForm = $("#searchForm");
				$searchForm.attr("action", "${ctx_path}/overdueexmo/1");
				$("#searchForm").submit();
			}
		});
	});

	$(function() {
		var branchIds = getBranchIds();
		var venderIds = getVenderIds();
		var condVO = getSearchFormValueObject();
		for (var i = 0; i < branchIds.length; i++) {
			sendRequest(branchIds[i], venderIds[i], condVO);
		}
	});

	function getBranchIds() {
		var branchs = $("td[id='branch_id']");
		var branchIds = [];
		for (var i = 0; i < branchs.length; i++) {
			branchIds.push($(branchs[i]).html());
		}
		return branchIds;
	}

	function getVenderIds() {
		var venders = $("td[id='vender_id']");
		var venderIds = [];
		for (var i = 0; i < venders.length; i++) {
			venderIds.push($(venders[i]).html());
		}
		return venderIds;
	}

	function sendRequest(branchId, venderId, condVO) {
		$.ajax({
			type : "post",
			url : "${ctx_path}/overdueexmo/getbranchdata/" + branchId + "/"
					+ venderId + "?" + Math.random(),
			dataType : "json",
			async : true,
			data : {
				cond : JSON.stringify(condVO)
			},
			success : function(result) {
				showRowData(result);
			}
		});
	}

	function showRowData(result) {
		var branchId = result.branchId;
		var trs = $('#static_table tr');
		var index = -1;
		for (var i = 1; i < trs.length; i++) {
			if ($(trs[i]).find("td").eq(0).html() == branchId) {
				index = i;
				break;
			}
		}
		var $tr = $("#dynamic_table").find("tr").eq(index);
		$tr.empty();
		var resultList = result.resultList;

		for (var i = 0; i < resultList.length; i++) {
			$tr.append(makeCell(resultList[i], index));
		}
	}

	function makeCell(cell, rowIndex) {
		var showColIndex = cell.showColIndex;
		var html = '<td><a href="javascript:showDetail(' + rowIndex + ','
				+ showColIndex + ')">' + cell.count + '</a></td>';
		if (cell.percent) {
			html += '<td>' + cell.percent + '</td>'
		}
		return html;
	}

	function getSearchFormValueObject() {
		var $searchForm = $("#searchForm");
		var vo = {};
		vo.optTimeType = $("#optTimeType").val();
		vo.startTime = $("#startTime").val();
		vo.endTime = $("#endTime").val();
		vo.orgs = getMultiSelectValues("orgs");
		vo.venderId = $("#venderId").val();
		vo.enableTEQuery = $("#enableTEQuery").attr("checked") == undefined ? false
				: true;
		vo.showCols = getMultiSelectValues("showCols");

		return vo;
	}

	function getMultiSelectValues(name) {
		var $fields = $("input[name='" + name + "']");
		var fieldIds = [];
		for (var i = 0; i < $fields.length; i++) {
			if ($($fields[i]).attr("checked") != undefined) {
				fieldIds.push($($fields[i]).val());
			}
		}
		return fieldIds;
	}

	function exportData() {
		<%if(exportFlag==0){
			%>
			alert('当前没有数据，不能导出');
			return;
		<%}
		%>
		
		var $exportBtn = $("#btn_export");
		var $searchForm = $("#searchForm");
		$searchForm.attr("action", "${ctx_path}/overdueexmo/exportdata");
		$searchForm.submit();
	}

	function showDetail(rowIdex, showColIndex) {
		var $detailForm = $("#detailForm");
		var tr = $('#static_table tr').eq(rowIdex);
		var $tr = $(tr);
		var orgId = $("#branch_id", $tr).html();
		var venderId = $("#vender_id", $tr).html();
		$("#orgId", $detailForm).val(orgId);
		$("#venderId", $detailForm).val(venderId);
		$("#showColIndex", $detailForm).val(showColIndex);

		$detailForm.submit();
	}
</script>

<style>
.search_div {
	padding: 5px;
	z-index: 9;
	width: 100%;
	background: url(../images/repeatx.png) repeat-x 0 -485px;
	border: 1px solid #86AFD5;
}

.table_div {
	margin-top: 5px;
}

.sys_query_checkbox {
	vertical-align: middle
}

.search_div div {
	padding: 3px;
}

.hide_td {
	display: none;
}

.red {
	color:red
}
</style>

</head>

<body style="background: #f5f5f5">
	<div class="inputselect_box">
		<form id="searchForm" name="searchForm" action="${ctx_path}/overdueexmo/1" method="post">
			<div>
				<input id="enableTEQuery" name="enableTEQuery" type="checkbox" class="sys_query_checkbox"
					<c:if test="${cond.enableTEQuery}">checked</c:if>>启用时效设置
			</div>
			<div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size:12px;height:90px;">
			    <tr>
    <td>
    操作时间
				<select id="optTimeType" name="optTimeType" class="select1">
					<c:forEach items="${constant.timeTypeMap}" var="entry">
						<option value="${entry.key}" <c:if test="${entry.key == cond.optTimeType}">selected</c:if>>${entry.value}</option>
					</c:forEach>
				</select>
				<input type="text" name="startTime" id="startTime" class="input_text1" style="height:20px;" value="${cond.startTime}" />
				到 
				<input
					type="text" name="endTime" id="endTime" value="${cond.endTime}" class="input_text1" />
    </td>
    <td>
    机构名称
    <label class="red">*</label><select id="orgs"
					name="orgs" multiple="multiple" class="select1">
					<c:forEach items="${constant.orgMap}" var="entry">
						<option value="${entry.key}" <c:if test="${fn:contains(cond.orgs,entry.key)}">selected</c:if>>${entry.value}</option>
					</c:forEach>
				</select>
				[<a href="javascript:multiSelectAll('orgs',1,'请选择');">全选</a>] 
				[<a href="javascript:multiSelectAll('orgs',0,'请选择');">取消全选</a>]
    </td>
    </tr>
    <td>
    客户
    <label class="red">*</label><select id="venderId"
					name="venderId" class="select1">
					<option value="0" <c:if test="${cond.venderId == 0}">selected</c:if>>请选择</option>
					<c:forEach items="${constant.venderMap}" var="entry">
						<option value="${entry.key}" <c:if test="${cond.venderId == entry.key}">selected</c:if>>${entry.value}</option>
					</c:forEach>
				</select>
    </td>
    <td>
    列表展示
    <select id="showCols" name="showCols" multiple="multiple" class="select1">
					<c:forEach items="${constant.showColMap}" var="entry">
						<option value="${entry.key}"
							<c:if test="${fn:contains(cond.showCols,entry.key)}">selected</c:if>>${entry.value}</option>
					</c:forEach>
				</select>
				[<a href="javascript:multiSelectAll('showCols',1,'请选择');">全选</a>] 
				[<a href="javascript:multiSelectAll('showCols',0,'请选择');">取消全选</a>]
    </td>
    </tr>
    <tr>
    <td>
    <input type="button" id="find"value="查询" class="input_button2" /> 
    <input type="button" id="btn_export" value="导出" class="input_button2" onclick="exportData()" />
    </td>
    <td>
    </td>
    </tr>
			</table>	      
			</div>
		</form>
	</div>
	<div style="height:130px"></div>
	<div class="table_div">
		<div style="float: left; width: 20%">
			<table style="width: 100%" border="0" cellspacing="1" cellpadding="0" class="table_2"
				id="static_table">
				<tr class="font_1">
					<td width="30%" align="center" valign="middle" bgcolor="#eef6ff">机构名称</td>
					<td width="30%" align="center" valign="middle" bgcolor="#eef6ff">客户</td>
				</tr>
				<c:forEach items="${result.branchMap}" var="entry">
					<tr>
						<td id="branch_id" class="hide_td">${entry.key}</td>
						<td>${entry.value}</td>
						<td id="vender_id" class="hide_td">${result.venderId}</td>
						<td>${result.venderName}</td>
					</tr>
				</c:forEach>
			</table>
		</div>

		<div style="width: 80%; overflow: auto;">
			<table style="width: 100%" border="0" cellspacing="1" cellpadding="0" id="dynamic_table"
				class="table_2">
				<tr>
					<c:forEach items="${result.showColList}" var="colName">
						<td>${colName}</td>
					</c:forEach>
				</tr>
				<c:forEach items="${result.branchMap}" var="entry">
					<tr>
						<c:forEach items="${result.showColList}" var="colName">
							<td>&nbsp;</td>
						</c:forEach>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>

	<div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff"><a
					href="javascript:$('#searchForm').attr('action','${ctx_path}/overdueexmo/1');$('#searchForm').submit();">第一页</a>
					<a
					<c:choose>  
                <c:when test="${result.page != 1}">href="javascript:$('#searchForm').attr('action','${ctx_path}/overdueexmo/${result.page - 1}');$('#searchForm').submit();"</c:when>  
                <c:otherwise>href="#"</c:otherwise>  
        		</c:choose>>上一页</a>
					<a
					<c:choose>  
                <c:when test="${(result.page != result.pageCount)}">href="javascript:$('#searchForm').attr('action','${ctx_path}/overdueexmo/${result.page + 1}');$('#searchForm').submit();"</c:when>  
                <c:otherwise>href="#"</c:otherwise>  
        		</c:choose>>下一页</a>
					<a
					href="javascript:$('#searchForm').attr('action','${ctx_path}/overdueexmo/${result.pageCount}');$('#searchForm').submit();">最后一页</a>
					共${result.pageCount}页 共${result.count}条记录 当前第<select id="select"
					onchange="$('#searchForm').attr('action','${ctx_path}/overdueexmo/'+$(this).val());$('#searchForm').submit()">
						<c:forEach var="i" begin="1" end="${result.pageCount}" step="1">
							<option value="${i}" <c:if test="${result.page == i}">selected</c:if>>${i}</option>
						</c:forEach>
				</select>页</td>
			</tr>
		</table>
	</div>

	<form id="detailForm" action="${ctx_path}/overdueexmo/showdetail/1">
		<input name="enableTEQuery" type="hidden" value="${cond.enableTEQuery}" /> <input type="hidden"
			name="startTime" type="hidden" value="${cond.startTime}" /> <input type="hidden" name="endTime"
			type="hidden" value="${cond.endTime}" /> <input type="hidden" id="orgId" name="orgId"
			type="hidden" /> <input type="hidden" id="venderId" name="venderId" type="hidden" /> <input
			type="hidden" id="showColIndex" name="showColIndex" type="hidden" />


	</form>
</body>
</html>






