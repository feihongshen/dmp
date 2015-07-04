<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>工资查询</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css" type="text/css"  />
<script src="${pageContext.request.contextPath}/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="${pageContext.request.contextPath}/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="${pageContext.request.contextPath}/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/js.js" type="text/javascript"></script>
<script src="${ctx}/js/commonUtil.js" type="text/javascript"></script>
<script src="${ctx}/js/workorder/csPushSmsList.js" type="text/javascript"></script>
<script type="text/javascript">
function showButton()
{ 	if($("#filename").val().indexOf(".xlsx")==-1&&$("#filename").val().indexOf(".xls")==-1)
	{
	alert("文件类型必须为xls或者xlsx");
	$("#filename").val('');
	$("#subfile").attr('disabled','disabled');
	return false;
	}
	if($("#filename").val().length>0)
	{
	$("#subfile").removeAttr('disabled');
	}
}
function addInit(){
	//无处理
}
function showError()
{
	$("#add").val('${pageContext.request.contextPath}/salaryFixed/importFlagError/'+$("#importFlag").val());
	getAddBox();
	}
function showUp()
{
	
	$("#fileup").removeAttr('style');
	$("#imp").attr('disabled','disabled');

	}
	

function checkall()
{ var checked=$("#all").attr('checked');
	$("[id=id]").each(
			function()
			{if(checked=='true'||checked=='checked')
				{$(this).attr('checked',checked);}
			else {
				$(this).removeAttr('checked');
			}
			});
}
function exportByCondation(sign){
	$("#exportform").attr("action","<%=request.getContextPath()%>/salaryQuery/exportExceldata/"+sign);
	$("#exportform").submit();
}

</script>
</head>

<body style="background:#eef9ff">

<div class="right_box">
	<div class="inputselect_box">
<table>
	<form action="1" method="post" id="searchForm">
	    <tr>
	    <td colspan="4" height="25px">
	    <input class="input_button2" type="submit"  value="查询"/>
	    </td>
	    <td colspan="4" height="25px" style="margin-right: 3%;">
	    <input class="input_button2" type="button"  value="全部导出" onclick="exportByCondation(0);"/>
	    </td>
	     <td colspan="4" width="400" height="25px" style="margin-right: 2%;">
	    <input class="input_button1" type="button"  value="按工资条导出" onclick="exportByCondation(1);"/>
	    </td>
	    </tr>
	    <tr>
	    <td  align="right" height="25px">批次编号：</td>
	    <td  align="left"><input type="text" name="batchnum" value="${realname}"/></td>
	    <td  align="right">站点：</td>
	    <td  align="left">
	   <%--  <input type="text" name="branch" value="${idcard}" /> --%>
	    <select id="branch" name="branch" >
	    	<option value="0">请选择站点</option>
	    	<c:forEach  items="${branchList}" var="branch">
	    	<option value="${branch.branchid}">${branch.branchname}</option>
	    	</c:forEach>
	    
	    </select>
	    </td>
	    <td  align="right">配送员：</td>
	    <td  align="left"><input type="text" name="distributionmember" value="${idcard}" /></td>
	    </tr>
	    <tr>
	      <td  align="right">身份证号：</td>
	    <td  align="left"><input type="text" name="idcard" value="${idcard}"/></td>
	    </tr>
	    </form>
		<tr>
		</tr>
 </table>

	</div>

	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="right_title">
	<div style="overflow: auto;">
	<table width="250%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	<tr>
	<td height="30px"  valign="middle"><input type="checkbox" id="all" onclick="checkall()"/> </td>
	<td align="center" valign="middle"style="font-weight: bold;"> 站点</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 姓名</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 身份证号</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 结算单量</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 基本工资 </td>
	<td align="center" valign="middle"style="font-weight: bold;"> 岗位工资</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 提成</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 工龄 </td>
	<td align="center" valign="middle"style="font-weight: bold;"> 邮费补贴</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 固定补贴</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 话费补助</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 高温寒冷补贴</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 扣款撤销</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它补贴</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它补贴2</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它补贴3</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它补贴4</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它补贴5</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它补贴6</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 加班费</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 考勤扣款 </td>
	<td align="center" valign="middle"style="font-weight: bold;"> 个人社保扣款</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 个人公积金扣款</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 违纪违规扣罚</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 货损赔偿</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 宿舍费用</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它扣罚</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它扣罚2</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它扣罚3</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它扣罚4</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它扣罚5</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它扣罚6</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 货物预付款</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它预付款</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它预付款2</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它预付款3</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它预付款4</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它预付款5</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它预付款6</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 应发工资</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 个税</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 实发工资</td>
	</tr>
	<c:forEach items="${salaryList}" var="salary">
	<tr> 
		<td align="center" valign="middle"><input type="checkbox" id="id" value="${salary.id}"/></td>
		<td align="center" valign="middle">${salary.realname}</td>
		<td align="center" valign="middle">${salary.idcard}</td>
		<td align="center" valign="middle">${salary.salarybasic}</td>
		<td align="center" valign="middle">${salary.salaryjob}</td>
		<td align="center" valign="middle">${salary.pushcash}</td>
		<td align="center" valign="middle">${salary.jobpush}</td>
		<td align="center" valign="middle">${salary.agejob}</td>
		<td align="center" valign="middle">${salary.bonusroom}</td>
		<td align="center" valign="middle">${salary.bonusallday}</td>
		<td align="center" valign="middle">${salary.bonusfood}</td>
		<td align="center" valign="middle">${salary.bonustraffic}</td>
		<td align="center" valign="middle">${salary.bonusphone}</td>
		<td align="center" valign="middle">${salary.bonusweather}</td>
		<td align="center" valign="middle">${salary.penalizecancel_import}</td>
		<td align="center" valign="middle">${salary.bonusother1}</td>
		<td align="center" valign="middle">${salary.bonusother2}</td>
		<td align="center" valign="middle">${salary.bonusother3}</td>
		<td align="center" valign="middle">${salary.bonusother4}</td>
		<td align="center" valign="middle">${salary.bonusother5}</td>
		<td align="center" valign="middle">${salary.bonusother6}</td>
		<td align="center" valign="middle">${salary.overtimework}</td>
		<td align="center" valign="middle">${salary.attendance}</td>
		<td align="center" valign="middle">${salary.security}</td>
		<td align="center" valign="middle">${salary.gongjijin}</td>
		<td align="center" valign="middle">${salary.foul_import}</td>
		<td align="center" valign="middle">${salary.dorm}</td>
		<td align="center" valign="middle">${salary.penalizeother1}</td>
		<td align="center" valign="middle">${salary.penalizeother2}</td>
		<td align="center" valign="middle">${salary.penalizeother3}</td>
		<td align="center" valign="middle">${salary.penalizeother4}</td>
		<td align="center" valign="middle">${salary.penalizeother5}</td>
		<td align="center" valign="middle">${salary.penalizeother6}</td>
		<td align="center" valign="middle">${salary.imprestother1}</td>
		<td align="center" valign="middle">${salary.imprestother2}</td>
		<td align="center" valign="middle">${salary.imprestother3}</td>
		<td align="center" valign="middle">${salary.imprestother4}</td>
		<td align="center" valign="middle">${salary.imprestother5}</td>
		<td align="center" valign="middle">${salary.imprestother6}</td>
		<td align="center" valign="middle">${salary.carrent}</td>
		<td align="center" valign="middle">${salary.carmaintain}</td>
		<td align="center" valign="middle">${salary.carfuel}</td>
		<td align="center" valign="middle">${salary.carfuel}</td>
	</tr>
	</c:forEach>
	</table>
	</div>
	</div>
	<input type="hidden" id="dmpurl" value="${pageContext.request.contextPath}" />
	<c:if test='${page_obj.maxpage>1}'>
	<div class="iframe_bottom">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
	<tr>
		<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
			<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
			<a href="javascript:$('#searchForm').attr('action','${page_obj.previous < 1 ? 1 : page_obj.previous}');$('#searchForm').submit();">上一页</a>　
			<a href="javascript:$('#searchForm').attr('action','${page_obj.next < 1 ? 1 : page_obj.next }');$('#searchForm').submit();" >下一页</a>　
			<a href="javascript:$('#searchForm').attr('action','${page_obj.maxpage < 1 ? 1 : page_obj.maxpage}');$('#searchForm').submit();" >最后一页</a>
			　共${page_obj.maxpage}页　共${page_obj.total}条记录 　当前第<select
					id="selectPg"
					onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
					<c:forEach var="i" begin="1" end="${page_obj.maxpage}">
					<option value='${i}' ${page==i?'selected=seleted':''}>${i}</option>
					</c:forEach>
				</select>页
		</td>
	</tr>
	</table>
	</div>
	</c:if>
	<form action="<%=request.getContextPath()%>/salaryQuery/exportExceldata/0" method="post" id="exportform">
		<input type="hidden" name="batchnum" value="${realname}"/>
		<input type="hidden" name="branch" value="${idcard}"/>
		<input type="hidden" name="distributionmember" value="${idcard}"/>
		<input type="hidden" name="idcard" value="${idcard}"/>
	</form>
</body>
</html>


