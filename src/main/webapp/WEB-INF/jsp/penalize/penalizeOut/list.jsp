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
	

	$("#addpenalizeOut").click(function() {
		$("#add").val('${pageContext.request.contextPath}/penalizeOut/addpenalizeOut');
		getAddBox();
	});
	$("#addpenalizeIn").click(function() {
		$("#add").val('${pageContext.request.contextPath}/penalizeOut/addpenalizeIn/'+$('input:radio:checked').val());
		getAddBox();
	});
	$("#cancelpenalizeOut").click(function() {
		$("#add").val('${pageContext.request.contextPath}/penalizeOut/cancelpenalizeOut/'+$('input:radio:checked').val());
		getAddBox();
	});

	
});
function checkRdio()
{
	var list= $('input:radio:checked').val();
    if(list!=null){
    	$("#cancelpenalizeOut").removeAttr('disabled');
        $("#addpenalizeIn").removeAttr('disabled');
    }
}
function addInit(){
	//无处理
}
function findbig()
{ var parent=$("#penalizesmall").find("option:selected").attr("id");
	$("#penalizebig option[value='"+parent+"']").attr("selected","selected");
} 
function findsmall(id)
{
	var dmpurl=$("#dmpurl").val();
$("#penalizesmall").empty();
$.ajax({
	type:"post",
	url:dmpurl+"/penalizeType/findsmall",
	data:{"id":id},
	dataType:"json",
	success:function(data){
		if(data.length>0){
			var optstr="<option value='0'>请选择</option>";

			for(var i=0;i<data.length;i++)
			{
				optstr+="<option value='"+data[i].id+"' id='"+data[i].parent+"'>"+data[i].text+"</option>";
			}
			
			$("#penalizesmall").append(optstr);
		}
	}});
}

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
function exportExcel(){
	$("#exportExcel").submit();
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
	$("#top").removeAttr('style');
	$("#br").attr('style','display: none;');
	$("#imp").attr('disabled','disabled');
//	$("#box_form").removeAttr('style');
	}
</script>
</head>

<body style="background:#eef9ff">

<div class="right_box">
	<div class="inputselect_box">
<form action="${pageContext.request.contextPath}/penalizeOut/list/1" method="post" id="searchForm">
<table width="100%"  >
<tr>
<td width="5%" align="right" rowspan="2" valign="middle">订单号：</td>
<td width="15%" rowspan="2" valign="bottom" ><textarea  name="cwbs" id="cwbs" style="width: 100%;height:100%;resize: none;">${cwbs}</textarea> </td>
<td width="7%" align="right">客户名称： </td>
<td width="13%" >
<select style="width: 100%" name="customerid" id="customerid">
<option  value="0">请选择</option>
		  <c:forEach items="${customerList}" var="cus">
             <option value="${cus.customerid}" ${cus.customerid==customerid?'selected=selected':'' }>${cus.customername}</option>
       </c:forEach>
</select> 
</td>
<td width="7%" align="right" > 赔付大类：</td>
<td width="13%" >
<select style="width: 100%" id="penalizebig" name="penalizeOutbig" onchange="findsmall($(this).val())">
<option value ="0">请选择</option>
<c:forEach items="${penalizebigList}" var="big" >
<option value="${big.id}" ${big.id==penalizeOutbig?'selected=selected':''}>${big.text}</option>
</c:forEach>
</select>
</td>
<td width="7%" align="right"> 赔付小类：</td>
<td width="13%" >
<select style="width: 100%" id="penalizesmall" name="penalizeOutsmall" onchange="findbig()">
<option value ="0">请选择</option>
<c:forEach items="${penalizesmallList}" var="small">
<option value="${small.id}"  id="${small.parent}" ${small.id==penalizeOutsmall?'selected=selected':''}>${small.text}</option>
</c:forEach>
</select>
</td>
<td width="20%"> </td>
</tr>

<tr>

<td  align="right">订单状态： </td>
<td  >
<select style="width: 100%" name="flowordertype">
<option value="0">请选择</option>
<c:forEach items="${flowordertypes}" var="flow">
<option value="${flow.value }" ${flow.value==flowordertype?'selected=selected':'' }>${flow.text }</option>
</c:forEach>
</select> 
</td>
<td  align="right" > 赔付单状态：</td>
<td  >
<select style="width: 100%" name="penalizeState">
<option  value="0">请选择</option>
<c:forEach items="${penalizeSates}" var="state">
<option value="${state.value}" ${state.value==penalizeState?'selected=selected':''}>${state.text}</option>
</c:forEach>
</select>
</td>
<td  align="right"> 创建日期：</td>
<td colspan="2">
<input type ="text" name ="starttime" id="starttime"  value="${starttime }" style="width: 30%"/>
			到
<input type ="text" name ="endtime" id="endtime"  value="${endtime }" style="width: 30%"/>
</td>
</tr>
<tr>
<td><input type="button" class="input_button2" id="addpenalizeOut" value="创建"/> </td>
<td><input type="button" ${importFlag > 0 ? 'disabled="disabled"' : ''}  class="input_button2" value="导入" id="imp"  onclick="showUp()"/> <input class="input_button2" type="button" disabled="disabled" value="生成扣罚单" id="addpenalizeIn"/> </td>
<td><input type="button" class="input_button2" disabled="disabled" id="cancelpenalizeOut" value="撤销"/></td>
<td> </td>
<td> </td>
<td> </td>
<td> </td>
<td colspan="2"> 
<input class="input_button2" type="button" onclick="check()" value="查询"/>
<input class="input_button2" type="button" onclick="javascript:window.location.href='${pageContext.request.contextPath}/penalizeOut/list/1'" value="重置" />
<input class="input_button2" type="button" onclick="exportExcel()"  ${page_obj.total> 0 ? '' : 'disabled="disabled"' } value="导出"/>  
</td>
</tr>
	<input name="isnow" value="1" type="hidden"/>
	</form>
<tr><td colspan="6">
<div id="fileup"  ${importFlag > 0 ? '' : 'style="display: none;"' }>
<table>
	<form id="penalizeOut_cre_Form" name="penalizeOut_import_Form"  action="${pageContext.request.contextPath}/penalizeOut/importData" method="post" enctype="multipart/form-data" >
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
	</div></td>
</tr>
 </table>

	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<table width="100%" style="height: 400px;overflow: auto;" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	<tr>
	<td height="30px"  valign="middle"> </td>
	<td align="center" valign="middle"style="font-weight: bold;"> 赔付单号</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 订单号</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 客户名称 </td>
	<td align="center" valign="middle"style="font-weight: bold;"> 订单状态</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 订单金额</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 对外赔付金额</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 赔付大类</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 赔付小类</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 创建人</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 创建日期</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 撤销人</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 撤销时间</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 赔付单状态</td>
	</tr>
	<c:forEach items="${penalizeOutList}" var="out">
	<tr>
	<td height="30px"  valign="middle"><input type="radio" name="penalizeOutId" onclick="checkRdio()" id="penalizeOutId" value="${out.penalizeOutId}"/> </td>
	<td align="center" valign="middle"> ${out.penalizeOutNO}</td>
	<td align="center" valign="middle"> ${out.cwb}</td>
	<td align="center" valign="middle"> 
	<c:forEach items="${customerList}" var="customer">
	<c:if test="${customer.customerid==out.customerid}">${customer.customername}</c:if>
	</c:forEach>
	</td>
	<td align="center" valign="middle"> 
	<c:forEach items="${flowordertypes}" var="flow">
	<c:if test="${flow.value==out.flowordertype}">${flow.text}</c:if>
	</c:forEach>
	</td>
	<td align="center" valign="middle"> ${out.caramount}</td>
	<td align="center" valign="middle"> ${out.penalizeOutfee}</td>
	<td align="center" valign="middle"> 
	<c:forEach items="${penalizebigList}" var="type">
	<c:if test="${type.id==out.penalizeOutbig}">${type.text}</c:if>
	</c:forEach>
	</td>
	<td align="center" valign="middle"> 
	<c:forEach items="${penalizesmallList}" var="type">
	<c:if test="${type.id==out.penalizeOutsmall}">${type.text}</c:if>
	</c:forEach>
	</td>
	<td align="center" valign="middle">
	<c:forEach items="${userList}" var="user">
	<c:if test="${user.userid==out.createruser}">${user.realname}</c:if>
	</c:forEach>
	</td>
	<td align="center" valign="middle"> ${out.createrdate}</td>
	<td align="center" valign="middle"> 
	<c:forEach items="${userList}" var="user">
	<c:if test="${user.userid==out.canceluser}">${user.realname}</c:if>
	</c:forEach>
	</td>
	<td align="center" valign="middle"> ${out.canceldate}</td>
	<td align="center" valign="middle"> 
	<c:forEach items="${penalizeSateEnums}" var="state">
	<c:if test="${state.value==out.penalizeOutstate}">${state.text}</c:if>
	</c:forEach>
	</td>
	</tr>
	</c:forEach>
	<tr>
	<td></td>
	<td></td>
	<td align="center" valign="middle" style="color: red;font-weight: bold;">赔付总计</td>
	<td></td>
	<td></td>
	<td></td>
	<td align="center" valign="middle" style="color: red;font-weight: bold;">${penalizeOutfeeSum}</td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	</tr>
	</table>
	</div>
	<input type="hidden" id="add" value="${pageContext.request.contextPath}/penalizeOut/addpenalizeOut" />
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
					<option value='${i}' ${page == i ? 'selected=seleted' : ''}>${i}</option>
					</c:forEach>
				</select>页
		</td>
	</tr>
	</table>
	</div>
	</c:if>
	<form action="${pageContext.request.contextPath}/penalizeOut/exportExcel" method="post" id="exportExcel">
	<input type="hidden" name="cwbs" value="${cwbstr}"/>
	<input type="hidden" name="customeird" value="${customerid }"/>
	<input type="hidden" name="flowordertype" value="${flowordertype }"/>
	<input type="hidden" name="penalizeOutbig" value="${penalizeOutbig }"/>
	<input type="hidden" name="penalizeOutsmall" value="${penalizeOutsmall }"/>
	<input type="hidden" name="penalizeState" value="${penalizeState }"/>
	<input type="hidden" name="starttime" value="${starttime}"/>
	<input type="hidden" name="endtime" value="${endtime}"/>
	 </form>
</body>
</html>


