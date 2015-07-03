<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 

<!DOCTYPE html PUBLIC "-//W3C//Dth HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dth">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css" type="text/css"  />
<script src="${pageContext.request.contextPath}/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="${pageContext.request.contextPath}/js/js.js"></script>

<script type="text/javascript">


function getPhoneonOne(phone,id){
	$('#callerphone').val(phone);
	$('#callerphoneid').val(id);
}
function addInit(){
	
}

function editInit(){
	
}
$(function(){
	
	
	$('#chongzhi').click(function(){
		$('#phoneonOne').val('');
		$('#name').val('');
		
		
		
	});
	
$('#svc').val($('#consigneeTypeVL').val());
	
	
	$('#edit_but').click(function(){
		if($('#callerphoneid').val()==""){
			alert('请选择一行数据' );
		}	else{	
			edit_button($('#callerphoneid').val());
		}
	});
	
	
	$("table#callertb tr").click(function(){
		$(this).css("backgroundColor","yellow");
		$(this).siblings().css("backgroundColor","#ffffff");
	});
	$('#remove_button').click(function(){
			$.ajax({
				type:'POST',
				data:'phoneone='+$('#callerphone').val(),
				url:'${pageContext.request.contextPath}/workorder/removeCallerArchival',
				dataType:'json',
				success:function(data){
					   $(".tishi_box").html(data.error);
					   $(".tishi_box").show();
					   setTimeout("$(\".tishi_box\").hide(1000)", 2000);
					   if (data.errorCode == 0){
						   $('#PageFromW').submit();
					   } 					
				}
			});
			$('#callerphoneid').val('');
	});
});

function deleteidValue(){
	$('#callerphone').val('');
	$('#callerphoneid').val('');
}

</script>
</head>
<body>
	<div>
		<form action="${ requestScope.page == null ? '1' : requestScope.page }" id=PageFromW method="post">
			<table>
				<tr>
					<td>姓名:<input type="text" name="name" id="name" value="${param.name == null  ? '' : param.name}"/></td>
					<td>电话:<input type="text" name="phoneonOne" id="phoneonOne" value="${param.phoneonOne == null  ? '' : param.phoneonOne}"/></td>				
					<td>
						客户:<select name="consigneeType" id="svc" class="select1" >
								<option value="-1">选择客户分类</option>
								<c:forEach items="${KeHuLeiXingAllReason}" var="k">
									<option value="${k.reasonid}">${k.reasoncontent}</option>	
								</c:forEach>
							
											
							</select>
					</td>
				</tr>
				<tr>
					<td>
						<input type="submit" value="查询" class="input_button2" onclick="$('#PageFromW').attr('action',1);return true;"/>
						<input type="button" value="重置" class="input_button2" id="chongzhi"/>
					</td>
				</tr>	
				</table>				
		</form>
		
			<table>
				<tr>
					<td><button id="add_button" class="input_button2" onclick="deleteidValue()">新增</button></td>
					<td><button id="edit_but" class="input_button2">修改</button></td>
					<td><button id="remove_button" class="input_button2">删除</button></td>
					<td><button class="input_button2" onclick="exportInFoExcle()" id="exinfo">导出</button></td>
				</tr>
			</table>
			<hr>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="callertb">
				<tr class="font_1">
					<th bgcolor="#eef6ff">姓名</th>
					<th bgcolor="#eef6ff">性别</th>
					<th bgcolor="#eef6ff">电话1</th>
					<th bgcolor="#eef6ff">电话2</th>
					<th bgcolor="#eef6ff">邮箱</th>
					<th bgcolor="#eef6ff">省份</th>
					<th bgcolor="#eef6ff">城市</th>
					<th bgcolor="#eef6ff">客户分类</th>
					<th bgcolor="#eef6ff">最后联系时间</th>
					<th bgcolor="#eef6ff">联系次数</th>
				</tr>
				<!--  -->
				<c:forEach items="${ccilist}" var="cv">
				<tr onclick="getPhoneonOne('${cv.phoneonOne}','${cv.id}')">
					
							<td>${cv.name}</td>
							<c:choose> 
							  <c:when test="${cv.sex==1}">  
							   		<td>男</td>				
							  </c:when> 							
							  <c:when test="${cv.sex==0}"> 
							    	<td>女</td>					
							  </c:when> 							
							  <c:otherwise>   
							  		<td></td>							
							  </c:otherwise> 							
							</c:choose> 
							<td>${cv.phoneonOne == null ? "" : cv.phoneonOne}</td>	
							<td>${cv.phoneonTwo == null ? "" : cv.phoneonTwo}</td>
							<td>${cv.mailBox == null ? "" : cv.mailBox}</td>
							<td>${cv.province == null ? "" : cv.province}</td>
							<td>${cv.city == null ? "" :  cv.city}</td>
							<td>
							<c:forEach items="${KeHuLeiXingAllReason}" var="k">
									<c:if test="${cv.consigneeType == k.reasonid}">
										 ${k.reasoncontent}
									</c:if>																																	
							</c:forEach>
							</td>
							<td>${cv.contactLastTime == null ? "" : cv.contactLastTime}</td>
							<td>${cv.contactNum == null ? "" : cv.contactNum}</td>					
				</tr>
				</c:forEach>
			</table>
		</div>
<input type="hidden" id="add" value="${pageContext.request.contextPath}/workorder/NewAddMaintain"/>
<input type="hidden" id="edit" value="${pageContext.request.contextPath}/workorder/EditEditMaintain/"/>
<input type="hidden" id="callerphone"/>
<input type="hidden" id="callerphoneid"/>

	<form action="${pageContext.request.contextPath}/workorder/exportExcle" id="CallerInfoForm">
		<input type="hidden" name="name" value="${param.name == null  ? '' : param.name}">
		<input type="hidden" name="phoneonOne" value="${param.phoneonOne == null  ? '' : param.phoneonOne}">
		<input type="hidden" name="consigneeType" value="${param.consigneeType == null  ? '' : param.consigneeType}">
	</form>
	
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1" id="pageid">
		<tr>
			<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
				<a href="javascript:$('#PageFromW').attr('action','1');$('#PageFromW').submit();" >第一页</a>　
				<a href="javascript:$('#PageFromW').attr('action','${page_obj.previous < 1 ? 1 : page_obj.previous}');$('#PageFromW').submit();">上一页</a>　
				<a href="javascript:$('#PageFromW').attr('action','${page_obj.next < 1 ? 1 : page_obj.next}');$('#PageFromW').submit();" >下一页</a>　
				<a href="javascript:$('#PageFromW').attr('action','${page_obj.maxpage < 1 ? 1 : page_obj.maxpage}');$('#PageFromW').submit();" >最后一页</a>
				　共${page_obj.maxpage}页　共${page_obj.total}条记录 　当前第
					<select
						id="selectPg"
						onchange="$('#PageFromW').attr('action',$(this).val());$('#PageFromW').submit()">
						<c:forEach begin="1" end="${page_obj.maxpage}" var="i">
							<option value="${i}">${i}</option>
						</c:forEach>
					</select>页
			</td>
		</tr>
	</table>
	<script type="text/javascript">
		
		function exportInFoExcle(){
			var listsize='${fn:length(ccilist)}';
			if(listsize==0){		 
			 	alert("没有做查询操作，不能导出！");
			 	return false;
			}else{
				$("#exinfo").val("请稍后……");
			 	$("#CallerInfoForm").submit();
			};
			
			
		}	
		var sv='${requestScope.page}';
		$("#selectPg").val(sv);
	</script>
	<div class="tishi_box"></div>
	
	<input type="hidden" id="consigneeTypeVL" value="${param.consigneeType == null  ? '' : param.consigneeType}"/>
</body>
</html>