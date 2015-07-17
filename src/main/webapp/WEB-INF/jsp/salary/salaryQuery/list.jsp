<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>工资查询</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/2.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css" type="text/css"  />
<script src="${ctx}/js/easyui-extend/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctx}/js/commonUtil.js" type="text/javascript"></script>
<script src="${ctx}/js/workorder/csPushSmsList.js" type="text/javascript"></script>
<script type="text/javascript">
$(function (){
	onBlurOnclick($("#distributionmember"));
	onBlurOnclick($("#idcard"));
	onBlurOnclick($("#batchnum"));
});
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
	if($("#gd_table").children().find("tr").length==1){
		alert("当前没有要导出的数据！！请先查询出需要导出的数据！！");
		return;
	}
	$("#exportform").attr("action","<%=request.getContextPath()%>/salaryQuery/exportExceldata/"+sign);
	$("#exportform").submit();
}
function clearData(){
	$("#batchnum").val("");
	$("#branch").val(0);
	$("#distributionmember").val("");
	$("#idcard").val("");
}
function onfocusOnclick(obj){
	if($(obj).val()=='模糊匹配'){
		$(obj).val("");
		$(obj).css('color','#000000');
	}
}
function onBlurOnclick(obj){
	if($(obj).val()==''){
		$(obj).val("模糊匹配");
		$(obj).css('color','#CCCCCC');
	}else{
		if($(obj).val()!='模糊匹配'){
			$(obj).css('color','#000000');
		}
	}
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
	    <input class="input_button2" type="button" onclick="clearData();"  value="重置"/>
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
	    <td  align="left"><input type="text" name="batchnum" id="batchnum"  value="${batchnum}"  style="color: #CCCCCC" onfocus="onfocusOnclick(this);" onblur="onBlurOnclick(this);"/></td>
	    <td  align="right" nowrap="nowrap" style="width: 20%;">站点：</td>
	    <td  align="left" nowrap="nowrap" style="width: 20%;">
	   <%--  <input type="text" name="branch" value="${idcard}" /> --%>
	    <select id="branch" name="branch" >
	    	<option value="0">请选择站点</option>
	    	<c:forEach  items="${branchList}" var="branch">
	    	<option value="${branch.branchid}" <c:if test="${branchid == branch.branchid}"> selected="selected" </c:if> >${branch.branchname}</option>
	    	</c:forEach>
	    </select>
	     <!--  <input type="text" name="branchid" class="easyui-validatebox" 
					initDataType="TABLE" 
					initDataKey="Branch" 
					viewField="branchname" 
      	        	saveField="branchid"
					data-options="width:150,prompt: '站点'"
			/> -->
	    </td>
	    <td  align="right">配送员：</td>
	    <td  align="left"><input type="text" name="distributionmember" id="distributionmember" value="${distributionmember}" style="color: #CCCCCC" onfocus="onfocusOnclick(this);" onblur="onBlurOnclick(this);" /></td>
	    </tr>
	    <tr>
	      <td  align="right">身份证号：</td>
	    <td  align="left"><input type="text" name="idcard" id="idcard" value="${idcard}"   style="color: #CCCCCC" onfocus="onfocusOnclick(this);" onblur="onBlurOnclick(this);"/></td>
	    <input type="hidden" name="isshow" value="1"></input>
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
	<div style="overflow:scroll;">
	<table width="250%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table" >
	<tr>
<!-- 	<td height="30px"  valign="middle"><input type="checkbox" id="all" onclick="checkall()"/> </td>
 -->	<td align="center" valign="middle"style="font-weight: bold;"> 站点</td>
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
	<c:forEach items="${salaryGathers}" var="salary">
	<tr> 
<%-- 		<td align="center" valign="middle"><input type="checkbox" id="id" value="${salary.id}"/></td>
 --%>		<td align="center" valign="middle">${salary.branchname}</td>
		<td align="center" valign="middle">${salary.realname}</td>
		<td align="center" valign="middle">${salary.idcard}</td>
		<td align="center" valign="middle">${salary.accountSingle}</td>
		<td align="center" valign="middle">${salary.salarybasic}</td>
		<td align="center" valign="middle">${salary.salaryjob}</td>
		<td align="center" valign="middle">${salary.salaradd}</td>
		<td align="center" valign="middle">${salary.agejob}</td>
		<td align="center" valign="middle">${salary.carfuel}</td>
		<td align="center" valign="middle">${salary.bonusfixed}</td>
		<td align="center" valign="middle">${salary.bonusphone}</td>
		<td align="center" valign="middle">${salary.bonusweather}</td>
		<td align="center" valign="middle">${salary.penalizecancel}</td>
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
		<td align="center" valign="middle">${salary.foul}</td>
		<td align="center" valign="middle">${salary.goods}</td>
		<td align="center" valign="middle">${salary.dorm}</td>
		<td align="center" valign="middle">${salary.penalizeother1}</td>
		<td align="center" valign="middle">${salary.penalizeother2}</td>
		<td align="center" valign="middle">${salary.penalizeother3}</td>
		<td align="center" valign="middle">${salary.penalizeother4}</td>
		<td align="center" valign="middle">${salary.penalizeother5}</td>
		<td align="center" valign="middle">${salary.penalizeother6}</td>
		<td align="center" valign="middle">${salary.imprestgoods}</td>
		<td align="center" valign="middle">${salary.imprestother1}</td>
		<td align="center" valign="middle">${salary.imprestother2}</td>
		<td align="center" valign="middle">${salary.imprestother3}</td>
		<td align="center" valign="middle">${salary.imprestother4}</td>
		<td align="center" valign="middle">${salary.imprestother5}</td>
		<td align="center" valign="middle">${salary.imprestother6}</td>
		<td align="center" valign="middle">${salary.salaryaccrual}</td>
		<td align="center" valign="middle">${salary.tax}</td>
		<td align="center" valign="middle">${salary.salary}</td>
	</tr>
	</c:forEach>
	</table>
	</div>
	</div>
	<input type="hidden" id="dmpurl" value="${pageContext.request.contextPath}" />
	<div style="height: 10px;">
	<c:if test='${page_obj.maxpage>1}'>
	<div class="iframe_bottom">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
	<tr>
		<td height="20" align="center" valign="middle" bgcolor="#eef6ff">
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
	</div>
	<form action="<%=request.getContextPath()%>/salaryQuery/exportExceldata/0" method="post" id="exportform">
		<input type="hidden" name="batchnum1" value="${batchnum}"/>
		<input type="hidden" name="branch1" value="${branchid}"/>
		<input type="hidden" name="distributionmember1" value="${distributionmember}"/>
		<input type="hidden" name="idcard1" value="${idcard}"/>
	</form>
</body>
</html>


