<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css" type="text/css"  />
<script src="${ctx}/js/easyui-extend/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctx}/js/commonUtil.js" type="text/javascript"></script>
<script src="${ctx}/js/workorder/csPushSmsList.js" type="text/javascript"></script>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>工资计算</title>
<script type="text/javascript">
$(function(){
	$('#find').dialog('close');
	$('#add').dialog('close');
	$('#edit').dialog('close');
});
function addInit(){
	//无处理
}
function allchecked()
{ var ids="";
	$("[id=id]").each(
			function()
			{
				if($(this).attr('checked')=='true'||$(this).attr('checked')=='checked')
					{
					ids+=","+$(this).val();
					}
			});
	if(ids.indexOf(',')!=-1)
		{
		ids=ids.substr(1);
		}
	
	var dmpurl=$("#dmpurl").val();
	if(window.confirm("确定要移除吗！")&&ids.length>0){
	$.ajax({
		type:"post",
		url:dmpurl+"/salaryFixed/delete",
		data:{"ids":ids},
		dataType:"json",
		success:function(data){
			if(data.counts>0){
				alert("成功移除"+data.counts+"记录");
				}
			$("#searchForm").submit();
			}
		});
	}
}
function checkall()
{ var checked=$("#all")[0].checked;
	$("[id=id]").each(function(){
		var e = $(this)[0];
		if(checked=='true'||checked=='checked')
		{
			e['checked'] = checked;
			//$(e).attr('checked',checked);
			}
		else {
			//$(e).removeAttr('checked');
			e['checked'] = checked;
		}
	});
}
</script>
</head>

<body style="background:#eef9ff">

<div class="right_box">
	<div class="inputselect_box">
	<form action="1" method="post" id="searchForm">
		<table style="width: 60%">
			    <tr>
			    <td>
			    <input class="input_button2" type="button" onclick="$('#add').dialog('open')" value="新增"/>
			    <input class="input_button2" type="button"  value="查看/修改"/>
			    <input class="input_button2" type="button"  value="删除"/>
			    <input class="input_button2" type="button" onclick="$('#find').dialog('open')" value="查询"/>
			    </td>
			    </tr>
		 </table>
	    </form>

	</div>


	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div><div class="jg_10"></div>
	<div class="right_title">
	<div style="overflow: auto;">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	<tr>
	<td height="30px"  valign="middle"><input type="checkbox" id="all" onclick="checkall()"/> </td>
	<td align="center" valign="middle"style="font-weight: bold;"> 批次编号</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 批次状态</td>
	<td align="center" valign="middle"style="font-weight: bold;"> 站点 </td>
	<td align="center" valign="middle"style="font-weight: bold;"> 期间 </td>
	<td align="center" valign="middle"style="font-weight: bold;"> 配送人员数 </td>
	<td align="center" valign="middle"style="font-weight: bold;"> 核销人 </td>
	<td align="center" valign="middle"style="font-weight: bold;"> 核销日期 </td>
	</tr>
	<c:forEach items="${salaryCountList}" var="salary">
	<tr>
	<td height="30px" align="center"  valign="middle"><input type="checkbox" id="id" value="${salary.batchid}" /> </td>
	<td align="center" valign="middle" >${salary.batchid}</td>
	<td align="center" valign="middle" >
	<c:forEach items="${batchStateEnum}" var="state">
	<c:if test="${salary.bacthstate==state.value}">${state.text }</c:if>
	</c:forEach>
	</td>
	<td align="center" valign="middle" >
	<c:forEach items="${ branchList}" var="branch">
	<c:if test="${salary.branchid==branch.branchid}">${branch.branchname }</c:if>
	</c:forEach>
	</td>
	<td align="center" valign="middle" >
	${salary.starttime }至${salary.endtime }
	</td>
	<td align="center" valign="middle" >
	${salary.usercount}
	</td>
	<td align="center" valign="middle" >
	<c:forEach items="${userList}" var="user">
	<c:if test="${salary.userid==user.userid}">${user.realname }</c:if>
	</c:forEach>
	</td>
	<td align="center" valign="middle" >
	${salary.operationTime}
	</td>
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
			<a href="javascript:$('#searchForm').attr('action','${page_obj.previous<1?1:page_obj.previous}');$('#searchForm').submit();">上一页</a>　
			<a href="javascript:$('#searchForm').attr('action','${page_obj.next<1?1:page_obj.next }');$('#searchForm').submit();" >下一页</a>　
			<a href="javascript:$('#searchForm').attr('action','${page_obj.maxpage<1?1:page_obj.maxpage}');$('#searchForm').submit();" >最后一页</a>
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
<!-- 新增层显示 -->
<div  id="add" class="easyui-dialog" title="新增" data-options="iconCls:'icon-save'" style="width:700px;height:220px;">
<table width="100%" border="0" cellspacing="1" cellpadding="0" style="margin-top: 10px;font-size: 10px;">
         		<tr>
	         	<td colspan="2"  align="center" valign="bottom">
	         	<input type="button" class="input_button2" value="返回" onclick="$('#add').dialog('close');"/>
	         	<input type="submit" class="input_button2" value="保存"/>
	         	</td>
	         	</tr>
         		<tr>
         		<td align="right" nowrap="nowrap" style="width: 10%;">批次编号：</td>
         		<td nowrap="nowrap" style="width: 20%;"><input type="text" style="width: 100%;"/> </td>
         		<td nowrap="nowrap" align="right" style="width: 10%;">批次状态：</td>
         		<td nowrap="nowrap" style="width: 20%;">
	         		<select style="width: 100%;">
	         			<option>全部</option>
	         		</select>
         		</td>
         		<td nowrap="nowrap" align="right" style="width: 10%;" >站点：</td>
         		<td nowrap="nowrap" style="width: 20%;"><input type="text" style="width: 100%;"/> </td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right">期间：</td>
         		<td nowrap="nowrap">
	         	 <input type="text"  class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:95,prompt: '起始时间'"/> 到 
   	       		 <input type="text"  class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:95,prompt: '终止时间'"/>
         		</td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right" rowspan="2">备注：</td>
         		<td nowrap="nowrap" colspan="6" rowspan="3">
			    <textarea rows="3"  style="width: 100%;resize: none;"></textarea>
		        </td>
         	</tr>
         	<tr>
         	<td colspan="6" >
         	&nbsp;
         	</td>
         	</tr>
         	</table>
</div>
<!-- 查看/修改层显示 -->
<!-- 查询层显示 -->
	<div  id="find" class="easyui-dialog" title="查寻条件" data-options="iconCls:'icon-save'" style="width:700px;height:220px;">

         	<table width="100%" border="0" cellspacing="1" cellpadding="0" style="margin-top: 10px;font-size: 10px;">
         	<tr>
         		<td align="right" nowrap="nowrap" style="width: 15%;">批次编号：</td>
         		<td nowrap="nowrap" style="width: 30%;"><input type="text" style="width: 100%;" name="batchid"/> </td>
         		<td nowrap="nowrap" align="right" style="width: 15%;" >批次状态：</td>
         		<td nowrap="nowrap" style="width: 30%;">
	         		<select style="width: 100%;"name="bacthstate">
	         		   <c:forEach  items="${batchStateEnum}" var="batch">
	         			<option ${batch.text}>${batch.text}</option>
	         			</c:forEach>
	         		</select>
         		</td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right" >站点：</td> 
         		<td nowrap="nowrap">
         		<select name="branchid" style="width: 100%">
         		 <c:forEach items="${ branchlList}" var="branch">
         		 <option value="${branch.branchid }">${branch.branchname }</option>
         		 </c:forEach>
         		</select>
         		</td>
         		<td nowrap="nowrap" align="right">期间：</td>
         		<td nowrap="nowrap">
	         	 <input type="text" name="starttime" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '起始时间'"/> 到 
   	       		 <input type="text" name="endtime" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '终止时间'"/>
         		</td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right" >核销人：</td>
         		<td nowrap="nowrap"><input type="text" style="width: 100%;" name="realname"/> </td>
         		<td nowrap="nowrap" align="right" name="operationTime">核销日期：</td>
         		<td nowrap="nowrap"><input type="text"  class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:150,prompt: '核销日起'"/></td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right">排序：</td>
         		<td nowrap="nowrap">
			    	<select style="width:70%;" name="orderbyname">
			    	<option value="batchid">批次编号</option>
			    	<option value="opreationTime">核销日期</option>
			    	</select>
			    	<select style="width:30%;" name="orderbyway">
			    	<option value="asc">升序</option>
			    	<option value="desc">降序</option>
			    	</select>
		        </td>
         	</tr>
         	<tr>
         	<td colspan="4" >
         	&nbsp;
         	</td>
         	</tr>
         	<tr>
         	<td colspan="4" rowspan="2" align="center" valign="bottom">
         	<input type="submit" class="input_button2" value="查询"/>
         	<input type="button" class="input_button2" value="关闭" onclick="$('#find').dialog('close');"/>
         	</td>
         	</tr>
         	</table>
	</div>

</body>
</html>


