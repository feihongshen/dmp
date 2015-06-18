<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>扣罚类型登记</title>
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
<script type="text/javascript">
$(function() {
	$("#starttime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
});
function check(){
	var len=$.trim($("#cwbs").val()).length;
 	if(len>0)
		{
 		 $("#searchForm").submit();
		return true;
		} 

	if($("#starttime").val()==""){
		alert("请选择开始时间");
		return false;
	}
	if($("#endtime").val()==""){
		alert("请选择结束时间");
		return false;
	}
	if($("#starttime").val()>$("#endtime").val()){
		alert("开始时间不能大于结束时间");
		return false;
	}
	if(!Days()||($("#starttime").val()=='' &&$("#endtime").val()!='')||($("#starttime").val()!='' &&$("#endtime").val()=='')){
		alert("时间跨度不能大于30天！");
		return false;
	}

   $("#searchForm").submit();
	return true;
}
function Days(){     
	var day1 = $("#starttime").val();   
	var day2 = $("#endtime").val(); 
	var y1, y2, m1, m2, d1, d2;//year, month, day;   
	day1=new Date(Date.parse(day1.replace(/-/g,"/"))); 
	day2=new Date(Date.parse(day2.replace(/-/g,"/")));
	y1=day1.getFullYear();
	y2=day2.getFullYear();
	m1=parseInt(day1.getMonth())+1 ;
	m2=parseInt(day2.getMonth())+1;
	d1=day1.getDate();
	d2=day2.getDate();
	var date1 = new Date(y1, m1, d1);            
	var date2 = new Date(y2, m2, d2);   
	var minsec = Date.parse(date2) - Date.parse(date1);          
	var days = minsec / 1000 / 60 / 60 / 24;  
	if(days>30){
		return false;
	}        
	return true;
}
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
function showError()
{
	$("#add").val('${pageContext.request.contextPath}/penalizeOut/importFlagError/'+$("#importFlag").val());
	getAddBox();
	}
function showUp()
{
	
	$("#fileup").removeAttr('style');
	$("#imp").attr('disabled','disabled');

	}
</script>
</head>

<body style="background:#eef9ff">

<div class="right_box">
	<div class="inputselect_box">
<form action="${pageContext.request.contextPath}/penalizeOut/list/1" method="post" id="searchForm">
<table  >
    <tr>
    <td colspan="4" height="25px">
    <input class="input_button2" type="button" onclick="check()" value="查询"/>
    <input type="button" <%-- ${importFlag>0?'disabled="disabled"':''} --%>  class="input_button2" value="固定值导入" id="imp"  onclick="showUp()"/> 
    </td>
    </tr>
    <tr>
    <td  align="right" height="25px">姓名：</td>
    <td  align="left"><input type="text" name="realname"/></td>
    <td  align="right">身份证号：</td>
    <td  align="left"><input type="text" name="idcard"/></td>
    </tr>
	<tr>
		<td colspan="4" height="25px">
		<div id="fileup"  style="display: none;" <%-- ${importFlag>0?'':'style="display: none;"' } --%>>
			<form id="penalizeOut_cre_Form" name="penalizeOut_import_Form"  action="${pageContext.request.contextPath}/penalizeOut/importData" method="post" enctype="multipart/form-data" >
			<table>
				<tr>
					<td>
						<input type="file"   name="Filedata" id="filename" onchange="showButton()" accept=".xls,.xlsx"/> <!--  -->
				   </td>
				   <td>
					   <input type="submit" class="input_button2" value="确认" disabled="disabled" id="subfile"/>
				  </td>
		 
		     <c:if test="${importFlag>0 }" >
					<td>
						<span style="font-weight: bold;"> 成功:</span> 
				   </td>
				   <td>
						<span style="font-weight: bold;color: red">${record.successCounts}</span>
				  </td> 
				  <td>
						<span style="font-weight: bold;"> 失败:</span>
				 </td>
				 <td>
						<input type="hidden" id="importFlag" value="${importFlag}"/>
						<span ${record.failCounts>0?'onclick="showError()"':''} style="font-weight: bold;color: red;cursor:pointer ;">${record.failCounts}</span>
				 </td>
		 </c:if>
		 </tr>
	</form>
	</table>
	</div>
	</td>
		</tr>
 </table>

	</div>

	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="right_title">
	<div style="overflow: auto;">
	<table width="200%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	<tr>
	<td height="30px"  valign="middle"> </td>
	<td align="center" valign="middle"style="font-weight: bold;"> 姓名</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 身份证号</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 基本工资 </td>
	<td align="center" valign="middle"style="font-weight: bold;"> 岗位工资</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 工龄</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 油费补贴</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 固定补贴</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 话费补助</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 高温寒冷补贴</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它补贴</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它补贴2</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它补贴3</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它补贴4</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它补贴5</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 其它补贴6</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 加班费</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 考勤扣款</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 个人社保扣款</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 个人公积金扣款</td>
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
	</tr>
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
			<a href="javascript:$('#searchForm').attr('action','${page_obj.previous<1?1:page_obj.previous}');$('#searchForm').submit();">上一页</a>　
			<a href="javascript:$('#searchForm').attr('action','${page_obj.next<1?1:page_obj.next }');$('#searchForm').submit();" >下一页</a>　
			<a href="javascript:$('#searchForm').attr('action','${page_obj.maxpage<1?1:page_obj.maxpage}');$('#searchForm').submit();" >最后一页</a>
			　共${page_obj.maxpage}页　共${page_obj.total}条记录 　当前第<select
					id="selectPg"
					onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
					<c:forEach var="i" begin="1" end="${page_obj.maxpage}">
					<option value='${i}'>${i}</option>
					</c:forEach>
				</select>页
		</td>
	</tr>
	</table>
	</div>
	</c:if>
	
</body>
</html>


