<%@page import="java.util.ArrayList"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<link href="<%=request.getContextPath()%>/dmp40/plug-in/chosen_v1.6.1/docsupport/prism.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/dmp40/plug-in/chosen_v1.6.1/chosen.css" rel="stylesheet">
<script src="<%=request.getContextPath()%>/dmp40/plug-in/chosen_v1.6.1/docsupport/prism.js" type="text/javascript" charset="utf-8"></script>
<script src="<%=request.getContextPath()%>/dmp40/plug-in/chosen_v1.6.1/chosen.jquery.js" type="text/javascript"></script>
</head>
<body>

<script type="text/javascript">
	$(function(){
		$("cwb").mouseover(function(){});
	});
	$(function() {
		$("#strtime").datetimepicker({
		    changeMonth: true,
		    changeYear: true,
		    hourGrid: 4,
			minuteGrid: 10,
		    timeFormat: 'hh:mm:ss',
		    dateFormat: 'yy-mm-dd'
		});
		$(".chosen-select-deselect").chosen({allow_single_deselect:true});
	});
	
	function getCourier(cwb, matchExceldeliverid) {
		var branchname = $("#branchlist" + cwb).val();
		if(branchname == null || branchname == "") {
			initCourier(cwb);
			return;
		}
		$.ajax({
			type: "POST",
	        url: "<%=request.getContextPath() %>/dataimport/getCourierByBranch",
	        data: {branchname:branchname},
	        dataType: "json",
	        success: function(data) {
	        	initCourier(cwb, data, matchExceldeliverid);
	        },
	        error: function() {
	        	initCourier(cwb);
	        	alert("获取小件员时发生错误！"); 
	        }    
	    });
	}
	function initCourier(cwb, courierList, matchExceldeliverid) {
		if(courierList == null) {
			courierList = new Array();
		}
		if(matchExceldeliverid == null) {
			matchExceldeliverid = "";
		}
		var $courier = $("#courier" + cwb);
		$courier.empty();
		$courier[0].add(new Option("", "", true, true));
		$.each(courierList, function(i, courier) { 
			if(courier.userid == matchExceldeliverid) { // 选中
				alert(1);
				$courier[0].add(new Option(courier.realname, courier.username, true, true));
			} else {
				$courier[0].add(new Option(courier.realname, courier.username));
			}
		});
		$courier.chosen("destroy");
		$courier.chosen({allow_single_deselect:true});
	}
	
	function setMatchAddress(obj,cwb){
		$("#matchaddress"+cwb).val($(obj).val());
	}
	function querycwb(form) {
		var cwb = trim(form.cwb.value);
		form.action = "common?action=opscwbtoview&checkinbranchflag=1&cwb="
				+ cwb;
		form.submit();

	}

	function queryshipcwb(form) {
		var shipcwb = trim(form.shipcwb.value);
		form.action = "common?action=opsshipcwbtoview&checkinbranchflag=1&shipcwb="
				+ shipcwb;
		form.submit();

	}
	
//底部信息位置
$(function(){
	$(window).resize(function(){
		var $scroll_hei=document.documentElement.clientHeight;
		$("#WORK_AREA").height($scroll_hei-160);
		$(".bottom_box").css("top",$scroll_hei-50);
	});
	var $scroll_hei=document.documentElement.clientHeight;
	$(".bottom_box").css("top",$scroll_hei-50);
	$("#WORK_AREA").height($scroll_hei-160);
	$("#playSearch").keydown(function(event){
		if(event.keyCode==13) {
			window.open ("/oms/order/queckSelectOrder/1?cwb="+this.value+"&dmpid=85C94DDF6073E6BE87A8C1577448EE08");
			$("#playSearch").val("输入订单号后按回车查询");
		}
	});
	
});


function editInit(){
	
}
</script>
</head>
<body  style="background:#f5f5f5">
<div class="saomiao_box2"> 
	<div>
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="#" class="light">订单信息修改</a></li>
				<li><a href="<%=request.getContextPath()%>/editcwb/toSearchCwb/1">订单修改查询</a></li>
			</ul>
		</div>
		<div class="tabbox">
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr >
							<td width="10%" height="26" align="left" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="5" class="table_5" >
									<tr>
										<td width="120" align="left" valign="top" bgcolor="#f5f5f5">
										<form action="<%=request.getContextPath()%>/editcwb/editCwbInfo" method="post">
										订单号：
										<textarea rows="3" name="cwb" id="cwb" onfocus="if(this.value=='输入订单号查询后进行修改……'){this.value=''}" onblur="if(this.value==''){this.value='输入订单号查询后进行修改……'}">输入订单号查询后进行修改……</textarea>
<!-- 										<input name="cwb" type="text" id="cwb" onfocus="if(this.value=='输入单个订单号查询后进行修改……'){this.value=''}" onblur="if(this.value==''){this.value='输入单个订单号查询后进行修改……'}" class="input_text1" style="height:20px;"/>
 -->										<input name="button" type="submit" class="input_button2" id="button" value="查询" />
										<input type="hidden" id="isshow" name="isshow" value="1" />
										</form>
										</td>
									</tr>
								</table>
								<%-- <%if(List!=null&&List.size()>0){
									%> --%>
								<form action="<%=request.getContextPath()%>/editcwb/updateCwbInfo/" method="post" id="searchForm2">
								<table id="order" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
									<tr class="font_1">
											<td align="center" bgcolor="#e7f4e3">订单号</td>
											<td align="center" bgcolor="#e7f4e3">收件人（修改）</td>
											<td bgcolor="#e7f4e3">手机（修改）</td>
											<td bgcolor="#e7f4e3">地址（修改）</td>
											<td bgcolor="#e7f4e3">配送站点（修改）</td>
											<td bgcolor="#e7f4e3">小件员（修改）</td>
											<td bgcolor="#e7f4e3">电商要求</td>
											<td bgcolor="#e7f4e3">备注</td>
											<td bgcolor="#e7f4e3">操作</td>
										</tr>
										<c:forEach var="vo" varStatus="voStatus" items="${cwbOrderBranchMatchVoList}">
											<tr>
												<td width="15%"  align="center" valign="middle" height="19" >${vo.cwbOrder.cwb }
													<input type="hidden" name="cwb" id="cwb${vo.cwbOrder.cwb }" value="${vo.cwbOrder.cwb }">
												</td>
												<td width="5%"  valign="middle" align="center"  >
													<input type="text" size="8px"  value="${vo.cwbOrder.consigneenameOfkf }" id="editname${vo.cwbOrder.cwb }" name="editname"/>
												</td>
												<td width="10%"  valign="middle"  align="center"  >
													<input type="text" size="12px"   value="${vo.cwbOrder.consigneemobileOfkf }" id="editmobile${vo.cwbOrder.cwb }" name="editmobile"/>
												</td>
												<td width="15%" valign="middle"  align="center"  >
													<textarea  cols="20"  name="editaddress" id="editaddress${vo.cwbOrder.cwb }" >${vo.cwbOrder.consigneeaddress }</textarea>
												</td>
												<td width="15%" valign="middle"  align="center"  >
													<input type="text" onkeyup="findbranch('${vo.cwbOrder.cwb }')" value="${vo.cwbOrder.excelbranch }"  name="matchaddress" id="matchaddress${vo.cwbOrder.cwb }" />
													<select id="branchlist${vo.cwbOrder.cwb }" onchange="setMatchAddress(this,'${vo.cwbOrder.cwb }');getCourier('${vo.cwbOrder.cwb }');">
														<option value="">请选择</option>
														<c:forEach var="branch" items="${branchs }">
															<option value="${branch.branchname }" <c:if test="${vo.cwbOrder.deliverybranchid eq branch.branchid }">selected="selected"</c:if>>${branch.branchname }</option>
														</c:forEach>
													</select>
												</td>
												<td width="10%"valign="middle"  align="left">
													<select id="courier${vo.cwbOrder.cwb }" name="courier" style="width:120px;" data-placeholder="请选择" class="chosen-select-deselect" tabindex="12">
														<option value="" selected="selected"></option>
														<c:forEach var="courier" items="${vo.courierList }">
															<option value="${courier.username }" <c:if test="${vo.cwbOrder.exceldeliverid eq courier.userid }">selected="selected"</c:if>>${courier.realname }</option>
														</c:forEach>
													</select>
												</td>
												<td width="10%" valign="middle"  align="left"  >
													<input type="text" size="12px"  value="${vo.cwbOrder.customercommand }" id="editcommand${vo.cwbOrder.cwb }" name="editcommand"/>
												</td>
												<td width="15%" valign="middle"  align="left"  >
													<textarea rows="3" cols="20"   id="remark${vo.cwbOrder.cwb }" name="remark" >${vo.cwbOrder.cwbremark }</textarea>
												</td>
												<td>
													<input name="button2" type="button" class="input_button2" id="buttonMatch${vo.cwbOrder.cwb }" value="修改匹配站" onclick="mathaddress('${vo.cwbOrder.cwb }');" />
													<input name="button2" type="button" class="input_button2" id="button2${vo.cwbOrder.cwb }" value="修改" onclick="selectForm('${vo.cwbOrder.cwb }');" />
													<input type="hidden" value="1" name="editshow" id="editshow${vo.cwbOrder.cwb }">
													<textarea cols="20"  name="checkeditaddress" id="checkeditaddress${vo.cwbOrder.cwb }" style="display:none" >${vo.cwbOrder.consigneeaddress }</textarea>
													<input type="hidden"   value="${vo.cwbOrder.consigneenameOfkf }" id="checkeditname${vo.cwbOrder.cwb }" name="checkeditname"/>
													<input type="hidden"   value="${vo.cwbOrder.consigneemobileOfkf }" id="checkeditmobile${vo.cwbOrder.cwb }" name="checkeditmobile"/>
													<input type="hidden"   value="${destinationName }" id="checkbranchname${vo.cwbOrder.cwb }" name="checkbranchname"/>
													<input type="hidden"  value="${vo.cwbOrder.customercommand }" id="checkeditcommand${vo.cwbOrder.cwb }" name="checkeditcommand"/>
												</td>
											</tr>
										</c:forEach>
									</table>
								</form>
							</td>
						</tr>
					</tbody>
				</table>
				<!--底部翻页 -->
				<div style="display: none;" >
					<select id="branchAll" onchange="setMatchAddress(this)">
						<option value="">请选择</option>
							<c:forEach var="branch" items="${branchs }">
								<option value="${branch.branchname }" <c:if test="${vo.cwbOrder.deliverybranchid eq branch.branchid }">selected="selected"</c:if>>${branch.branchname }</option>
							</c:forEach>
						</select>
				</div>
		</div>
	</div>
</div>
<script type="text/javascript">

	function selectForm(a){
		if($("#branchlist"+a).val() == "") {
			alert("请选择站点！");
			return false;
		}
		$("#button2"+a).attr('disabled','disabled');
		$("#button2"+a).val('修改中');
		$("#checkbranchid"+a).val($("#branchlist"+a).val());
			//$("#checkeditaddress")
					$.ajax({
						url:"<%=request.getContextPath()%>/editcwb/updateCwbInfo/"+a,
						type:"POST",//数据发送方式 
						data:{
							editname:$("#editname"+a).val(),	
							editmobile:$("#editmobile"+a).val(),	
							editcommand:$("#editcommand"+a).val(),	
							editshow:$("#editshow"+a).val(),	
							remark:$("#remark"+a).val(),	
							matchaddress:$("#branchlist"+a).val(),	
							courierName:$("#courier" + a).val(),
							editaddress:$("#editaddress"+a).val(),	
							checkeditaddress:$("#checkeditaddress"+a).val(),	
							checkeditname:$("#checkeditname"+a).val(),	
							checkeditmobile:$("#checkeditmobile"+a).val(),	
							checkbranchname:$("#checkbranchname"+a).val(),	
							checkeditcommand:$("#checkeditcommand"+a).val(),	
						},
						dataType:'json',//接受数据格式
						success:function(data){
							if(data.errorCode == 1){
								 alert(data.error);
								 $("#button2"+a).removeAttr('disabled');
									$("#button2"+a).val('修改');
							}else{
								$("#checkeditaddress"+a).val($("#editaddress"+a).val());
								$("#checkeditname"+a).val($("#editname"+a).val());
								$("#checkeditmobile"+a).val($("#editmobile"+a).val());
								if(($("#matchaddress"+a).val()!="请选择")){
									if($("#matchaddress"+a).val()==""){
									}else{
										$("#checkbranchname"+a).val($("#matchaddress"+a).val());
									}
								}
								$("#checkeditcommand"+a).val($("#editcommand"+a).val());
								$("#checkbegindate").val($("#strtime").val());
								$("#button2"+a).removeAttr('disabled');
								$("#button2"+a).val('修改');
								alert(data.error);
							}
						}
						   
					});
}
	function mathaddress(cwb){
		$("#buttonMatch"+cwb).attr('disabled','disabled');
		$("#buttonMatch"+cwb).val('匹配中');
		var editaddress=$("#editaddress"+cwb).val();
		if(editaddress.length>0){
					$.ajax({
						url:"<%=request.getContextPath()%>/editcwb/matchaddress",
						type:"POST",//数据发送方式 
						data:{"address":editaddress,"cwb":cwb},//参数
						dataType:'json',//接受数据格式
						success:function(data){
							if(data.netpoint.length==0){
								$("#buttonMatch"+cwb).removeAttr('disabled');
								$("#buttonMatch"+cwb).val('修改匹配站');
								alert("未匹配到站点");}
							$("#matchaddress"+cwb).val((data.netpoint));
							if($("#matchaddress"+cwb).val().length>0){
								$("#buttonMatch"+cwb).removeAttr('disabled');
								$("#buttonMatch"+cwb).val('修改匹配站');
								findbranch(cwb, data.exceldeliverid);
							}
							
						}
						   
					});
		}
		else {
			alert("请检查收件人地址！");
		}
}
	function findbranch(cwb, matchExceldeliverid){
		var branchname=$("#matchaddress"+cwb).val();
		if(branchname.length>0){
					$.ajax({
						url:"<%=request.getContextPath()%>/editcwb/findbranch",
						type:"POST",//数据发送方式 
						data:{"branchname":branchname},//参数
						dataType:'json',//接受数据格式
						success:function(data){
							if(data.length>0)
								{
								var options="";
								for(var i=0;i<data.length;i++)
									{
									
									options+="<option value='"+data[i].branchname+"'>"+data[i].branchname+"</option>";
									}
								$("#branchlist"+cwb).empty();
								$("#branchlist"+cwb).append(options);
								}else {
									$("#branchlist"+cwb).empty();
									$('#branchAll option').each(function(){
										  $("<option value='"+$(this).val()+"'>"+$(this).text()+"</option>").appendTo("#branchlist"+cwb);
										  }); 
								}
							
							getCourier(cwb, matchExceldeliverid);
						}
						   
					});
		}
		else{
			$("#branchlist"+cwb).empty();
			$('#branchAll option').each(function(){
				  $("<option value='"+$(this).val()+"'>"+$(this).text()+"</option>").appendTo("#branchlist"+cwb);
				  });
		}
		
}
	
	function showinfo(a){
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/editcwb/searchCwbInfo/"+a,
			dataType:"json",
			success : function(data) {  
				if(data.errorCode == 1){
					 alert(data.error);
				}
			}
		});
	}
</script>
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/editcwb/findCwbDetail/" />
</body>
</html>