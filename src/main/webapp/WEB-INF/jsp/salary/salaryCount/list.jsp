<%@page import="cn.explink.domain.Branch"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<jsp:useBean id="now" class="java.util.Date" scope="page"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>工资计算</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/2.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css" type="text/css"  />
<script src="${ctx}/js/easyui-extend/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctx}/js/commonUtil.js" type="text/javascript"></script>
<script src="${ctx}/js/workorder/csPushSmsList.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$('#find').dialog('close');
	$('#add').dialog('close');
	$('#edit').dialog('close');
	$('#save').dialog('close');
});
function addInit(){
	//无处理
}
function allchecked(){ 
	var ids="";
	$("[id=id]").each(function(){
		if($(this)[0].checked==true){
			ids+=","+$(this).val();
		}
	});
		if(ids.indexOf(',')!=-1){
			ids=ids.substr(1);
		}
	if(ids.length==0){
		alert("请选择需要删除的批次编号!");
		return false;
	}
	var dmpurl=$("#dmpurl").val();
	if(window.confirm("确定要删除吗！")&&ids.length>0){
	$.ajax({
		type:"post",
		url:dmpurl+"/salaryCount/delete",
		data:{"ids":ids},
		dataType:"json",
		success:function(data){
			if(data.counts>0){
				alert("成功移除"+data.counts+"记录");
				}
			$("#find form").submit();
			}
		});
	}
}
function checkall(){ 
	var checked=$("#all")[0].checked;
	$("[id=id]").each(function(){
		var e = $(this)[0];
		if(checked=='true'||checked=='checked'){
			e['checked'] = checked;
			//$(e).attr('checked',checked);
		}else {
			//$(e).removeAttr('checked');
			e['checked'] = checked;
		}
	});
}
function showUp(){
	$("#fileup").removeAttr('style');
	$("#top").removeAttr('style');
	$("#br").attr('style','display: none;');
	$("#imp").attr('disabled','disabled');
//	$("#box_form").removeAttr('style');
}
function showButton(){ 	
	if($("#filename").val().indexOf(".xlsx")==-1&&$("#filename").val().indexOf(".xls")==-1){
		alert("文件类型必须为xls或者xlsx");
		$("#filename").val('');
		$("#subfile").attr('disabled','disabled');
		return false;
	}
	if($("#filename").val().length>0){
		$("#subfile").removeAttr('disabled');
	}
}
function check(flag){ 
	var startime=$("#"+flag+" [name=starttime]").val();
	var endtime=$("#"+flag+" [name=endtime]").val();
	if(flag=='add'){
	 	if(startime==""){
			alert("请选择开始时间");
			return ;
		}
		if(startime==""){
			alert("请选择结束时间");
			return ;
		}
		if($("#"+flag+" [name=branchid]").val()==''){
			alert("请选择站点！");
			return ;
		}
		//$("#"+flag).attr("display","none");
		<%-- $("#"+flag+" form").submit();
		if('1'=='<%=request.getParameter("edit")%>'){
			$("#save").css('display','block'); 
		} --%>
	}	
	if(startime>endtime){
		alert("开始时间不能大于结束时间");
		return ;
	} 
	$("#"+flag+" form").submit();
}
$(function(){
	if(<%=request.getAttribute("edit")%>==1){
		$('#save').dialog('open');
		$('#save').removeAttr('hidden');
		//$("#save").css('display','block');
	}
});

function seeoralter(){
	var indexVar = -1;
	var batchVar = '';
	$('input[name="isselect"]:checked').each(function(index){
		indexVar = index;
		batchVar = $(this).val();
	});
	if(indexVar<0){
		alert("请选择需要 查看/修改 的批次编号!");
		return false;
	}
	if(indexVar>0){
		alert("选择一个进行 查看/修改 即可!");
		return false;
	}
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/salaryCount/seeOralter",
		data:{batchid:batchVar},
		dataType:"json",
		success : function(data) {
			if(data!=null){
				$("#batchid").val(data.batchid);//批次编号
				$("#batchstate").val(data.batchstate);//核销状态
				$("#branchname").val(data.branchname);//站点	
				$("#starttime").val(data.starttime);//开始时间
				$("#endtime").val(data.endtime);//结束时间
				$("#remark").val(data.remark);//备注
				$('#save').dialog('open');
				$('#save').removeAttr('hidden');
			}else{
				alert("系统异常！");
			}
		}
	});
}

function checkit(){ 
	var checked=$("#all").attr('checked');
	$("[id=id]").each(
		function(){
			if(checked=='true'||checked=='checked'){
				$(this).attr('checked',checked);}
			else {
				$(this).removeAttr('checked');
			}
		});
}

function saveform(){
	$("#saveform").attr('action','<%=request.getContextPath()%>/salaryCount/credata');
	$("#saveform").submit();
	$('#save').dialog('close');
}


</script>
</head>

<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
		<table style="width: 60%">
			    <tr>
				    <td>
					    <input class="input_button2" type="button" onclick="$('#add').dialog('open');" value="新增"/>
					    <input class="input_button2" type="button" onclick="seeoralter();" value="查看/修改"/>
					    <input class="input_button2" type="button" onclick="allchecked()"  value="删除"/>
					    <input class="input_button2" type="button" onclick="$('#find').dialog('open');" value="查询"/>
				    </td>
			    </tr>
		 </table>
	</div>


	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div><div class="jg_10"></div>
	<div class="right_title">
	<div style="overflow: auto;">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		<tr>
			<td height="30px"  valign="middle">
				<input type="checkbox" id="all" onclick="checkall()"/> 
			</td>
			<td align="center" valign="middle"style="font-weight: lighter;"> 批次编号</td>
			<td align="center" valign="middle"style="font-weight: lighter;"> 批次状态</td>
			<td align="center" valign="middle"style="font-weight: lighter;"> 站点 </td>
			<td align="center" valign="middle"style="font-weight: lighter;"> 期间 </td>
			<td align="center" valign="middle"style="font-weight: lighter;"> 配送人员数 </td>
			<td align="center" valign="middle"style="font-weight: lighter;"> 核销人 </td>
			<td align="center" valign="middle"style="font-weight: lighter;"> 核销日期 </td>
		</tr>
		<c:forEach items="${salaryCountList}" var="salary">
		<tr>
			<td height="30px" align="center"  valign="middle">
				<input type="checkbox" id="id" name="isselect" value="${salary.batchid}" /> 
			</td>
			<td align="center" valign="middle" >${salary.batchid}</td>
			<td align="center" valign="middle" >
				<c:forEach items="${batchStateEnum}" var="state">
					<c:if test="${salary.batchstate==state.value}">${state.text }</c:if>
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
	</div>
	<input type="hidden" id="dmpurl" value="${pageContext.request.contextPath}" />
	<c:if test='${page_obj.maxpage>1}'>
	<div class="iframe_bottom"> 
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
	<tr>
		<td height="38" align="center" valign="middle" bgcolor="#eef6ff" style="font-size: 10px;">
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
<!-- 新增层显示 -->
<div  id="add" class="easyui-dialog" title="新增" data-options="iconCls:'icon-save',modal:true" style="width:700px;height:220px;">
<%-- <form action="${ctx}/salaryCount/credata" method="post" id="credatafrom"> --%>
<form action="${ctx}/salaryCount/credata" method="post" id="credatafrom">
<table width="100%" border="0" cellspacing="1" cellpadding="0" style="margin-top: 10px;font-size: 10px;">
       	<tr>
        	<td colspan="2"  align="center" valign="bottom">
	        	<input type="button" class="input_button2" value="返回" onclick="$('#add').dialog('close');"/>
	        	<input type="button" class="input_button2" value="保存" onclick="check('add')"/>
        	</td>
        </tr>
       	<tr>
       		<td align="right" nowrap="nowrap" style="width: 10%;">批次编号：</td>
       		<td nowrap="nowrap" style="width: 20%;">
       			<input name="batchid" type="text" style="width: 100%;" disabled="disabled" readonly="readonly" value="自动生成"/> 
       		</td>
       		<td nowrap="nowrap" align="right" style="width: 10%;">批次状态：</td>
       		<td nowrap="nowrap" style="width: 20%;">
        		<select name="batchstate" style="width: 100%;" disabled="disabled">
        			<option value="0">未核销</option>
        			<option value="1">已核销</option>
        		</select>
       		</td>
       		<td nowrap="nowrap" align="right" style="width: 10%;" >站点：</td>
       		<td nowrap="nowrap" style="width: 20%;">
       		<%-- <select name="branchid" style="width: 100%">
       		 <option value="-1"></option>
       		 <c:forEach items="${ branchList}" var="branch">
       		 <option value="${branch.branchid }">${branch.branchname }</option>
       		 </c:forEach>
       		</select> --%>
       		<input type="text" name="branchid" class="easyui-validatebox" 
				initDataType="TABLE" 
				initDataKey="Branch" 
				viewField="branchname" 
	  	        saveField="branchid"
	  	        filterField="sitetype" 
				filterVal="2"  
				data-options="width:150,prompt: '站点'"
			/>
       		</td>
       	</tr>
       	<tr>
       		<td nowrap="nowrap" align="right">期间：</td>
       		<td nowrap="nowrap">
	        	<input type="text" name="starttime" id="starttime_add" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '起始时间'"/> 到 
	 	        <input type="text" name="endtime" id="endtime_add" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '终止时间'"/>
       		</td>
       	</tr>
       	<tr>
       		<td nowrap="nowrap" align="right" rowspan="2">备注：</td>
       		<td nowrap="nowrap" colspan="6" rowspan="3">
	   	 	<textarea rows="3"  name="remark" style="width: 100%;resize: none;"></textarea>
        </td>
       	</tr>
       	<tr>
	       	<td colspan="6" >
	       	&nbsp;
	       	</td>
       	</tr>
     </table>
   </form>
</div>
<!-- 查看/修改层显示 -->
<%-- <c:if test="${edit==1 }"> --%>
	<div  id="save" hidden="hidden" class="easyui-dialog" title="编辑" data-options="iconCls:'icon-save',modal:true" style="width:800px;height:220px;">
	<form action="${ctx}/salaryCount/save" method="post" id="saveform">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" style="margin-top: 10px;font-size: 10px;">
        	<tr>
	         	<td colspan="5"  align="left" valign="bottom">
		         	<input type="button" class="input_button2" value="返回" onclick="$('#save').dialog('close');"/>
		         	<input type="button" class="input_button2" value="保存" onclick="saveform();"/>
		         	<input type="hidden" name="isnow" value="1" />
		         	<input type="submit" class="input_button2" value="核销完成"/>
	         	</td>
	         	<td>
	         		<input type="button"  style="width:110px;" class="input_button2" id="imp"  onclick="showUp()" value="人事数据导入"/>
	         	</td>
         	</tr>
         	<tr>
		    	<td colspan="6">
					<div id="fileup"  style="display: none;">
					<table>
						<form id="penalizeOut_cre_Form" name="penalizeOut_import_Form"  action="${pageContext.request.contextPath}/salaryCount/importData" method="post" enctype="multipart/form-data" >
							<tr>
								<td>
									<input type="file"   name="Filedata" id="filename" onchange="showButton()" accept=".xls,.xlsx"/> <!--  -->
								</td>
								<td>
									<input type="submit" class="input_button2" value="确认" disabled="disabled" id="subfile"/>
								</td>
								<c:if test="${importflag>0 }" >	
								 	<td>
										<span style="font-weight: lighter;font-size: 10px"> 成功:</span> 
									</td>
									<td>
										<span style="font-weight: bold;color: red">${record.successCounts}</span>
									</td> 
									<td>
										<span style="font-weight: lighter;font-size: 10px"> 失败:</span>
									</td>
									<td>
										<input type="hidden" id="importFlag" value="${importflag}"/>
										<span ${record.failCounts > 0 ? 'onclick="showError()"' : ''} style="font-weight: bold;color: red;cursor:pointer ;">${record.failCounts}</span>
									</td>
								</c:if>	
							</tr>
						</form>
						</table>
					</div>
				</td>					
			</tr>
         	<tr>
         		<td align="right" nowrap="nowrap" style="width: 10%;">
         			批次编号：
         		</td>
         		<td nowrap="nowrap" style="width: 20%;">
         			<input id="batchid" name="batchid" type="text" style="width: 100%;" <!-- readonly="readonly" --> <!-- disabled="disabled" --> value="${salary.batchid}"/> 
         		</td>
         		<td nowrap="nowrap" align="right" style="width: 10%;">
         			批次状态：
         		</td>
         		<td nowrap="nowrap" style="width: 20%;">
		         	<select id="batchstate" name="batchstate" style="width: 100%" disabled="disabled">
	         		 <option value="-1"></option>
		         		 <c:forEach items="${batchStateEnum}" var="batch" >
		         		 	<option value="${batch.value}"${salary.batchstate==batch.value?'selected=selected':'' } >${batch.text }</option>
		         		 </c:forEach>
	         		</select>
         		</td>
         		<td nowrap="nowrap" align="right" style="width: 10%;" >站点：</td>
         		<td nowrap="nowrap" style="width: 20%;">
         	 	<select id="branchname" name="branchid" style="width: 100%" <!-- disabled="disabled" -->>
	         		 <option value="-1"></option>
	         		 <c:forEach items="${branchList}" var="branch">
	         		 	<option value="${branch.branchid}" ${salary.branchid==branch.branchid?'selected=selected':'' }>${branch.branchname }</option>
	         		 </c:forEach>
         		</select>
         		<%-- <input type="text" id="branchname" name="branchid" class="easyui-validatebox" 
					initDataType="TABLE" 
					initDataKey="Branch" 
					viewField="branchname" 
      	        	saveField="branchid"
      	        	filterField="sitetype" 
					filterVal="2"  
					data-options="width:150,prompt: '站点'"
					value="${salary.branchid}"
				/> --%>
         		 </td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right">期间：</td>
         		<td nowrap="nowrap">
		         	<input type="text" id="starttime" name="starttime"  value="${salary.starttime}"/> 到 
		   	       	<input type="text" id="endtime" name="endtime" value="${salary.endtime}"/>
         		</td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right" rowspan="2">备注：</td>
         		<td nowrap="nowrap" colspan="6" rowspan="3">
			    	<textarea rows="3"  id="remark" name="remark" style="width: 100%;resize: none;">${salary.remark}</textarea>
		        </td>
         	</tr>
         	<tr>
         		<div class="right_title">
				<div style="overflow: auto;">
				<table width="700%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
				<tr>
					<td align="center" valign="middle" style="font-weight: lighter;width: 20px;"><input type="checkbox" id="all" onclick="checkit()"/> </td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 50px;"> 姓名</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 120px;"> 身份证号</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 结算单量</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 基本工资</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 岗位工资</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 绩效奖金</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 岗位津贴</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 提成</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 工龄</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 住房补贴</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 全勤补贴</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 餐费补贴</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 交通补贴</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 通讯补贴</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 高温寒冷补贴</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 扣款撤销</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 120px;"> 扣款撤销(导入)</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 其它补贴</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 100px;"> 其它补贴2</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 100px;"> 其它补贴3</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 100px;"> 其它补贴4</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 100px;"> 其它补贴5</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 100px;"> 其它补贴6</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 加班费</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 考勤扣款</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 120px;"> 个人社保扣款</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 120px;"> 个人公积金扣款</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 120px;"> 违纪违规扣罚</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 160px;"> 违纪扣款扣罚(导入)</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 货损赔偿</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 宿舍费用</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 其它扣罚</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 100px;"> 其它扣罚2</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 100px;"> 其它扣罚3</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 100px;"> 其它扣罚4</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 100px;"> 其它扣罚5</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 100px;"> 其它扣罚6</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 100px;"> 货物预付款</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 100px;"> 其它预付款</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 100px;"> 其它预付款2</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 100px;"> 其它预付款3</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 100px;"> 其它预付款4</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 100px;"> 其它预付款5</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 100px;"> 其它预付款6</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 120px;"> 租用车辆费用</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 120px;"> 车子维修费用</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 油/电费用</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 应发工资</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 个税</td>
					<td align="center" valign="middle"style="font-weight: lighter;width: 80px;"> 实发工资</td>
				</tr>
				<%-- <c:forEach items="${salaryList}" var="salary">
					<tr> 
						<td align="center" valign="middle"><input type="checkbox" id="id" value="${salary.id}"/></td>
						<td align="center" valign="middle">${salary.realname}</td>
						<td align="center" valign="middle">${salary.idcard}</td>
						<td align="center" valign="middle">${salary.accountSingle}</td>
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
						<td align="center" valign="middle">${salary.goods}</td>
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
					</tr>
				</c:forEach> --%>
				</table>
					<input type="button" onclick="allchecked()" value="移除"/>
				</div>
				</div>
         	</tr>
         	<tr>
	         	<td colspan="6" >
	         	&nbsp;
	         	</td>
         	</tr>
         </table>
        </form>
	</div>
<%-- </c:if> --%>
<!-- 查询层显示 -->
	<div  id="find" class="easyui-dialog" title="查寻条件" data-options="iconCls:'icon-save',modal:true" style="width:700px;height:220px;">
	<form action="${ctx}/salaryCount/list/1" method="post" id="searchForm" >
         	<table width="100%" border="0" cellspacing="1" cellpadding="0" style="margin-top: 10px;font-size: 10px;">
         	<tr>
         		<td align="right" nowrap="nowrap" style="width: 15%;">批次编号：</td>
         		<td nowrap="nowrap" style="width: 30%;">
         			<input  name="batchid" type="text" style="width: 100%;" value="${batchid }" /> 
         		</td>
         		<td nowrap="nowrap" align="right" style="width: 15%;" value="${batchstate }" >批次状态：</td>
         		<td nowrap="nowrap" style="width: 30%;">
	         		<select  name="batchstate" style="width: 100%;" >
	         		<%-- <c:forEach  items="${batchStateEnum}" var="batch">
	         			<option vaule="${batch.value}">${batch.text}</option>
	         			</c:forEach> --%>
	         			<option value="-1"></option>
	         			<option value="1" ${batchstate==1?'selected=selected':'' } >已核销</option>
	         			<option value="0" ${batchstate==0?'selected=selected':'' }>未核销</option>
	         		</select>
         		</td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right" >站点：</td> 
         		<td nowrap="nowrap">
         		<%-- <select name="branchid" style="width: 100%">
         		 <option value="-1"></option>
         		 <c:forEach items="${ branchList}" var="branch">
         		 <option value="${branch.branchid }">${branch.branchname }</option>
         		 </c:forEach>
         		</select> --%>
         			<input type="text" name="branchname" value="${branchname}"/>
         		</td>
         		<td nowrap="nowrap" align="right">期间：</td>
         		<td nowrap="nowrap">
		         	<input type="text" name="starttime" id="starttime_find" value="${starttime}" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '起始时间'"/> 到 
	   	       		<input type="text" name="endtime"   id="endtime_find" value="${endtime}" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '终止时间'"/>
         		</td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right" >核销人：</td>
         		<td nowrap="nowrap">
         			<input type="text" style="width: 100%;" name="realname" value="${realname}"/> 
         		</td>
         		<td nowrap="nowrap" align="right" >核销日期：</td>
         		<td nowrap="nowrap">
         			<input type="text" name="operationTime" value="${operationTime}" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:150,prompt: '核销日期'"/>
         		</td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right">排序：</td>
         		<td nowrap="nowrap">
			    	<select style="width:70%;" name="orderbyname">
				    	<option value="batchid" ${orderbyname=='batchid'?'selected=selected':'' }>批次编号</option>
				    	<option value="operationTime" ${orderbyname=='operationTime'?'selected=selected':'' }>核销日期</option>
			    	</select>
			    	<select style="width:30%;" name="orderbyway">
				    	<option value="asc" ${orderbyway=='asc'?'selected=selected':'' }>升序</option>
				    	<option value="desc" ${orderbyway=='desc'?'selected=selected':'' } >降序</option>
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
		         	<input type="button" onclick="check('find')" class="input_button2" value="查询" />
		         	<input type="button" class="input_button2" value="关闭" onclick="$('#find').dialog('close');"/>
	         	</td>
         	</tr>
         	</table>
         	</form>
	</div>
</body>
</html>


