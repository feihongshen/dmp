<%@page import="cn.explink.controller.AbnormalView"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.domain.AbnormalType"%>
<%@page import="cn.explink.domain.AbnormalOrder"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="cn.explink.dao.CwbDAO"%>

<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<AbnormalType> abnormalTypeList = (List<AbnormalType>)request.getAttribute("abnormalTypeList");
List<AbnormalView> views=(List<AbnormalView>)request.getAttribute("views");
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
Page page_obj = (Page)request.getAttribute("page_obj");
Object cwb = request.getAttribute("cwb")==null?"":request.getAttribute("cwb");
  ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext()); 
  String starttime=request.getParameter("begindate")==null?DateTimeUtil.getDateBefore(1):request.getParameter("begindate");
  String endtime=request.getParameter("enddate")==null?DateTimeUtil.getNowTime():request.getParameter("enddate");

  String showabnomal = request.getAttribute("showabnomal")==null?"":request.getAttribute("showabnomal").toString();
  String sitetype = request.getAttribute("sitetype")==null?"":request.getAttribute("sitetype").toString();
  String ishandle = request.getAttribute("ishandle").toString();
  String branchid = request.getAttribute("branchid").toString();
  long customerid = Long.parseLong(request.getAttribute("customerid").toString());
  long roleid = Long.parseLong(request.getAttribute("roleid").toString());
  long userid = Long.parseLong(request.getAttribute("userid").toString());
  List<Customer> customerlist=(List<Customer>)request.getAttribute("customerlist");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.inputer.js"></script>
<script src="${pageContext.request.contextPath}/js/inputer.js"></script>
<script language="javascript">
$(function(){
	var $menuli = $(".kfsh_tabbtn ul li");
	var $menulilink = $(".kfsh_tabbtn ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
	
	$("#strtime").datetimepicker({
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
	$("#chuangjianstrtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#chuangjianendtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
	
	
	checkstate();
	
});

function getThisBoxList(id,flag){
	var URL="";
	if(flag==0){
		URL=$("#handle"+id).val()+"&isfind=0";
		console.info($("#dutyuseridhhh"+id).val());
		console.info($("#userid").val());
		console.info($("#dutyuseridhhh"+id).val()!=$("#userid").val());
		if($("#sitetype").val()!=5){
			if(($("#useridhhh"+id).val()!=$("#userid").val())&&($("#dutyuseridhhh"+id).val()!=$("#userid").val())){
				//是否是本站的站长
				if($("#iszhanzhang"+id).val()!=1){
					alert("对不起，您不是该问题件的创建方与责任方(或双方的站长)不允许操作！！");
					return;
				}
			
			}
		}
		if($("#handlehhh"+id).val()==5){
			alert("已经结案的问题件不能进行处理！！");
			return;
		}
	}else if(flag==1){
		
		URL=$("#handle"+id).val()+"&isfind=1";
	}
	$.ajax({
		type: "POST",
		url:URL,
		dataType:"html",
		success : function(data) {
			$("#alert_box",parent.document).html(data);
		},
		complete:function(){
			viewBox();
		}
	});
}
function check(){
	if($("#ishandle").val()==<%=AbnormalOrderHandleEnum.daichuli.getValue()%>||$("#ishandle").val()==<%=AbnormalOrderHandleEnum.yichuli.getValue()%>||$("#ishandle").val()==<%=AbnormalOrderHandleEnum.jieanchuli.getValue()%>){

	if($("#strtime").val()!=""||$("#endtime").val()!=""){
		if($("#strtime").val()==""){
			alert("请选择开始时间！！");
			return;
		}
		if($("#endtime").val()==""){
			alert("请选择结束时间！！");
			return;
		}
		if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() !=''){
			alert("开始时间不能大于结束时间");
			return;
		}
	}
if($("#ishandle").val()==<%=AbnormalOrderHandleEnum.jieanchuli.getValue()%>){
	 if($("#strtime").val()==""){
			alert("请选择开始时间");
			return false;
			}
			if($("#endtime").val()==""){
			alert("请选择结束时间");
			return false;
		} 
				if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() !=''){
					alert("开始时间不能大于结束时间");
					return;
				}
			 if(!Days($("#strtime").val(),$("#endtime").val())||($("#strtime").val()=='' &&$("#endtime").val()!='')||($("#strtime").val()!='' &&$("#endtime").val()=='')){
				alert("时间跨度不能大于31天！");
				return false;
			} 
}
}else{
	if($("#ishandle").val()==0){
		if($("#chuangjianstrtime").val()==""){
			alert("请选择开始时间！！");
			return;
		}
		if($("#chuangjianendtime").val()==""){
			alert("请选择结束时间！！");
			return;
		}
		 if($("#chuangjianstrtime").val()>$("#chuangjianendtime").val() && $("#chuangjianendtime").val() !=''){
				alert("开始时间不能大于结束时间");
				return;
			}  

	}
	if(!Days($("#chuangjianstrtime").val(),$("#chuangjianendtime").val())||($("#chuangjianstrtime").val()=='' &&$("#chuangjianendtime").val()!='')||($("#chuangjianstrtime").val()!='' &&$("#chuangjianendtime").val()=='')){
		alert("时间跨度不能大于31天！");
		return false;
	} 
}
$("#searchForm").submit();

}

function Days(day1,day2){     
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
	if(days>31){
		return false;
	}        
	return true;
}	
function editSuccess(data){
	window.parent.closeBox();
	$("#searchForm").submit();
}		
function checkstate(){
	if($("#ishandle").val()==0){
		$("#chuangjianstrtime").show();
		$("#chuangjianendtime").show();
		$("#strtime").hide();
		$("#endtime").hide();
		$("#chuli").html("创建时间：");
	}else if($("#ishandle").val()==<%=AbnormalOrderHandleEnum.weichuli.getValue()%>){
		$("#chuangjianstrtime").show();
		$("#chuangjianendtime").show();
		$("#strtime").hide();
		$("#endtime").hide();
		$("#chuli").html("创建时间：");
	}else if($("#ishandle").val()==<%=AbnormalOrderHandleEnum.daichuli.getValue()%>){
		$("#chuangjianstrtime").hide();
		$("#chuangjianendtime").hide();
		$("#strtime").show();
		$("#endtime").show();
		$("#chuli").html("处理时间：");
	}else if($("#ishandle").val()==<%=AbnormalOrderHandleEnum.jieanchuli.getValue()%>){
		$("#chuangjianstrtime").hide();
		$("#chuangjianendtime").hide();
		$("#strtime").show();
		$("#endtime").show();
		$("#chuli").html("处理时间：");
	}else{
		$("#chuangjianstrtime").hide();
		$("#chuangjianendtime").hide();
		$("#strtime").show();
		$("#endtime").show();
		$("#chuli").html("处理时间：");
	}
	if($("#ishandle").val()!=<%=AbnormalOrderHandleEnum.jieanchuli.getValue()%>){
		$("#dealresult").val(0);
	}
	$("#chuangjianstrtime").val("");
	$("#chuangjianendtime").val("");
	$("#strtime").val("");
	$("#endtime").val("");
}

function isgetallcheck(){
	if($('input[name="id"]:checked').size()>0){
		$('input[name="id"]').each(function(){
			$(this).attr("checked",false);
		});
	}else{
		$('input[name="id"]').attr("checked",true);
	}
}
//dutyuseridhhh
function stateBatch()
{	var num=0;
	var ids="";
	var nodutynum=0;
	var noduty="";
	var efectiveids="";
	$('input[name="id"]:checked').each(function(){ //由于复选框一般选中的是多个,所以可以循环输出
		id=$(this).val();
		if($("#sitetype").val()!=5){
			if(($("#useridhhh"+id).val()!=$("#userid").val())&&($("#dutyuseridhhh"+id).val()!=$("#userid").val())){
				num=num+1;
			}
		}
		if($("#dutyuseridhhh"+id).val()==0){
			nodutynum=nodutynum+1;
			if($.trim(id).length!=0){
				noduty+=id+",";
			}
		}
		if($.trim(id).length!=0){
		ids+=id+",";
		}
		});
	/* alert(ids);
	alert(noduty); */
	if(ids.length==0){
		if(noduty.length==0||yijieanids.length==0){
			alert("请选择要操作的记录！");
			return false;
		}
		
	}
	//一般没有这种情况
	if(num>0){
		if($("#roleid").val()!=4){
			alert("您所选择的问题件包含您不是当事责任机构与责任人的单号，不允许操作！！");
			return;
		}
	}
	if(nodutynum>0){
		efectiveids=ids;
		for(var j=0;j<noduty.substring(0, noduty.length-1).split(",").length;j++){
		if(ids.indexOf(noduty.substring(0, noduty.length-1).split(",")[j]+",")>=0){
	 		efectiveids=efectiveids.replace(noduty.substring(0, noduty.length-1).split(",")[j]+",", "");	
			}
		}
	}
	if(nodutynum>0){
		if($("#roleid").val()==1){
			if(confirm("您所选择的问题件包含没有责任机构与责任人的单号"+noduty.substring(0, noduty.length-1).split(",").length+"单（以上情况的订单将不做操作），确认继续执行吗？")){
				if(ids.indexOf(",")>0&&efectiveids.indexOf(",")>0){
					$.ajax({
						type : "POST",
						url:"<%=request.getContextPath()%>/abnormalOrder/gotoBatch",
						data:{"ids":efectiveids.substring(0, efectiveids.length-1)},
						dataType : "html",
						success : function(data) {$("#alert_box",parent.document).html(data);
						},
						complete:function(){
							viewBox();
						}
					});
					}else{
						alert("没有能够操作的问题件，请重新选择！");
						return;
					}
			}
			return;
		}
	}
	if(ids.indexOf(",")>0){
		$.ajax({
			type : "POST",
			url:"<%=request.getContextPath()%>/abnormalOrder/gotoBatch",
			data:{"ids":ids.substring(0, ids.length-1)},
			dataType : "html",
			success : function(data) {$("#alert_box",parent.document).html(data);
			},
			complete:function(){
				viewBox();
			}
		});
		}else{
			alert("没有能够操作的问题件，请重新选择！");
			return;
		}
	<%-- if(yijieanids.length>0){
		if($("#roleid").val()!=1){
			
				
			
		}else{
			if(ids.indexOf(",")>0){
				$.ajax({
					type : "POST",
					url:"<%=request.getContextPath()%>/abnormalOrder/gotoBatch",
					data:{"ids":ids.substring(0, ids.length-1)},
					dataType : "html",
					success : function(data) {$("#alert_box",parent.document).html(data);
					},
					complete:function(){
						viewBox();
					}
				});
				}else{
					alert("没有能够操作的问题件，请重新选择！");
				}
		}
		
	
	} --%>
	
	}
function reviseQuestionError(state)
{
	var num=0;
	var ids="";
	var statenotpass="";
	var effectids="";
	$('input[name="id"]:checked').each(function(){ //由于复选框一般选中的是多个,所以可以循环输出
		id=$(this).val();
		if($("#sitetype").val()!=5){
			if(($("#useridhhh"+id).val()!=$("#userid").val())&&($("#dutyuseridhhh"+id).val()!=$("#userid").val())){
				if($("#iszhanzhang"+id).val()!=1){
					num=num+1;
				}
			}
		
		}
		if($("#handlehhh"+id).val()!=1&&$("#handlehhh"+id).val()!=8){
			if($.trim(id).length!=0){
				statenotpass+=id+",";
				}
			
		}
		if($.trim(id).length!=0){
		ids+=id+",";
		}
		});
	/* if(num>=1){
		alert("您所选择的问题件里面包含您不是责任方与创建的订单！！");
		return;
	} */
	if(ids.length==0){
		alert("请选择要操作的记录！");
		return false;
	}
	
/* 	if($("#ishandlehhh").val()!=1&&$("#ishandlehhh").val()!=8){
		alert("当前所选订单的订单状态不是创建状态！！不允许修改！");
		return;
	} */
	if(statenotpass.indexOf(",")>=0){
		effectids=ids;
		for(var i=0;i<statenotpass.substring(0, statenotpass.length-1).split(",").length;i++){
			if(effectids.indexOf(statenotpass.substring(0, statenotpass.length-1).split(",")[i]+"")>=0){
				effectids=effectids.replace(statenotpass.substring(0, statenotpass.length-1).split(",")[i]+",","");
			}
		}
		if(confirm("您所选的订单里面包含不是创建状态的订单"+statenotpass.substring(0, statenotpass.length-1).split(",").length+"单，确定要继续操作吗？？")){
			if(ids.indexOf(",")>0&&effectids.length>0){
				$.ajax({
					type : "POST",
					url:"<%=request.getContextPath()%>/abnormalOrder/revisequestionstate",
					data:{"ids":effectids.substring(0, effectids.length-1)},
					dataType : "html",
					success : function(data) {$("#alert_box",parent.document).html(data);
						
					},
					complete:function(){
						viewBox();
					}
				});
				}else{
					alert("没有能够操作的订单！！");
					return;
				}
		}
	}
	if(ids.indexOf(",")>0){
	$.ajax({
		type : "POST",
		url:"<%=request.getContextPath()%>/abnormalOrder/revisequestionstate",
		data:{"ids":ids.substring(0, ids.length-1)},
		dataType : "html",
		success : function(data) {$("#alert_box",parent.document).html(data);
			
		},
		complete:function(){
			viewBox();
		}
	});
	}else{
		alert("没有能够操作的订单！！");
	}
	}
function resultdatadeal(id)
{
/* 	var ids="";
	$('input[name="id"]:checked').each(function(){ //由于复选框一般选中的是多个,所以可以循环输出
		id=$(this).val();
		if($.trim(id).length!=0){
		ids+=id+",";
		}
		});
	if(ids.length==0){
		alert("请选择！");
		return false;
	}
	if(ids.split("\n").length>1){
		alert("只支持选择一单数据进行处理");
	} */
	
	if($("#sitetype").val()!=5){
		
			alert("对不起，只有客服有权限对问题件结案，请联系客服！！");
			return;
		
	}
	console.info($("#ishandlehhh").val());
	if($("#handlehhh"+id).val()!=2){
		alert("当前所选订单的订单状态不是客服处理完成状态！！不允许结案！");
		return;
	}
	$.ajax({
		type : "POST",
		url:"<%=request.getContextPath()%>/abnormalOrder/resultdatadeallast",
		data:{"id":id},
		dataType : "html",
		success : function(data) {$("#alert_box",parent.document).html(data);
		},
		complete:function(){
			viewBox();
		}
	});
	}
	function resetData(){
		$("#cwb").val("");
		$("#createbranchid").val(0);
		$("#customerid").val(-1);
		$("#abnormaltypeid").val(0);
		$("#dutybranchid").val(0);
		$("#ishandle").val(0);
		$("#losebackisornot").val(-1);
		$("#dealresult").val(0);
		$("#chuangjianstrtime").show();
		$("#chuangjianendtime").show();
		$("#strtime").hide();
		$("#endtime").hide();
		$("#chuangjianstrtime").val("");
		$("#chuangjianendtime").val("");
		$("#strtime").val("");
		$("#endtime").val("");
		$("#chuli").html("创建时间：");
	}
function checkdealresult(){
	if($("#ishandle").val()!=5){
		$("#dealresult").val(0);
		alert("请先选择处理状态为结案处理！！");
		return;
	}
}
</script>
</head>
<body style="background:#f5f5f5;overflow: hidden;" marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<div style="position:relative; z-index:0;" >
			<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
				<div class="kfsh_search">
							<form action="1" method="post" id="searchForm">
								<table width="100%"  style="font-size: 12px;">
								<tr>
								<td align="left" >
								订单号：
									<textarea id="cwb" class="kfsh_text" rows="2" name="cwb" ><%=cwb%></textarea>
								</td>
								
								<td align="left">
								&nbsp;创建机构：
									<label for="select2"></label>
									<select name="createbranchid" id="createbranchid" class="select1">
										<option value="0">请选择</option>
										<%if(branchList!=null||branchList.size()!=0){for(Branch b : branchList){ %>
											<option value="<%=b.getBranchid()%>"><%=b.getBranchname() %></option>
										<%}} %>
									</select>
								<br>
								<br>
									客户名称 ： <select name ="customerid" id ="customerid"  style="width: 120px;"  class="select1">
									<option value="-1">请选择</option>
		          						<%for(Customer c : customerlist){ %>
		      					     <option value ="<%=c.getCustomerid() %>"  <%if(c.getCustomerid()==customerid){%>  selected="selected"<%}%>><%=c.getCustomername() %></option>
		         						 <%} %>
		      						  </select>
		      						</td>
		      						<td>
									<label for="select3"></label>
									问题件类型：
									<select name="abnormaltypeid" id="abnormaltypeid" class="select1">
										<option value="0">请选择问题件类型</option>
										<%if(abnormalTypeList!=null||abnormalTypeList.size()>0)for(AbnormalType at : abnormalTypeList){ %>
										<option title="<%=at.getName() %>" value="<%=at.getId()%>"><%if(at.getName().length()>25){%><%=at.getName().substring(0,25)%><%}else{%><%=at.getName() %><%} %></option>
										<%} %>
									</select>
									<br><br>
										&nbsp;责 任 机构：
									<label for="select2"></label>
									<select name="dutybranchid" id="dutybranchid" class="select1">
										<option value="0">请选择责任机构</option>
										<%if(branchList!=null||branchList.size()!=0){for(Branch b : branchList){ %>
											<option value="<%=b.getBranchid()%>"><%=b.getBranchname() %></option>
										<%}} %>
									</select>
									</td>
										<td>
									<label for="select3"></label>
									处理状态：
									<select name="ishandle" id="ishandle" onchange="checkstate()" class="select1">
										<option value="0">全部状态</option>
										<option value="<%=AbnormalOrderHandleEnum.weichuli.getValue()%>"><%=AbnormalOrderHandleEnum.weichuli.getText() %></option>
										<option value="<%=AbnormalOrderHandleEnum.daichuli.getValue()%>"><%=AbnormalOrderHandleEnum.daichuli.getText() %></option>
										<option value="<%=AbnormalOrderHandleEnum.yichuli.getValue()%>"><%=AbnormalOrderHandleEnum.yichuli.getText() %></option>
										<option value="<%=AbnormalOrderHandleEnum.jieanchuli.getValue()%>"><%=AbnormalOrderHandleEnum.jieanchuli.getText() %></option>
									</select>
									<br><br>
										丢失返回 ：
									<label for="select2"></label>
									<select name="losebackisornot" id="losebackisornot" class="select1">
										<option value="-1">请选择是否丢失</option>
										<option value="0">丢失未找回</option>
										<option value="1">丢失找回</option>
										</select>
									</td>
									<td>
								处理结果：
									<label for="select4"></label>
									<select name="dealresult" id="dealresult" onchange="checkdealresult()" class="select1">
										<option value="0">请选择处理结果</option> 
										<option value="1">问题成立</option>
										<option value="2">问题不成立</option>
									</select>
									
									<br><br>
									<strong id="chuli"></strong>
									<input type ="text" name ="begindate" id="strtime"  value="<%=request.getParameter("begindate")==null?"":request.getParameter("begindate") %>" class="input_text1" style="height:20px;"/>
									<input type ="text" name ="chuangjianbegindate" id="chuangjianstrtime"  value="<%=request.getAttribute("chuangjianbegindate")==null?"":request.getAttribute("chuangjianbegindate") %>" class="input_text1" style="height:20px;"/>
<%-- 									<strong id="chuli" >处理时间：</strong>
									<input type ="text"  style="width: 120px;" name ="begindate" id="strtime"  value="<%=request.getParameter("begindate")==null?"":request.getParameter("begindate") %>"/>
									<input type ="text"  style="width: 120px;" name ="chuangjianbegindate" id="chuangjianstrtime"  value="<%=request.getAttribute("chuangjianbegindate")==null?"":request.getAttribute("chuangjianbegindate") %>"/>  --%>
									<strong id="chulidown">到</strong>
									<input type ="text" name ="enddate" id="endtime"  value="<%=request.getParameter("enddate")==null?"":request.getParameter("enddate") %>" class="input_text1" style="height:20px;"/>
									<input type ="text" name ="chuangjianenddate" id="chuangjianendtime"  value="<%=request.getAttribute("chuangjianenddate")==null?"":request.getAttribute("chuangjianenddate") %>" class="input_text1" style="height:20px;"/>
									<input type="hidden" name="isshow" value="1"/>
									<input type="hidden" name="branchid"  id="branchid" value="<%=branchid %>"/>
									<input type="hidden" name="sitetype"  id="sitetype" value="<%=sitetype %>"/>
									<input type="hidden" name="userid"  id="userid" value="<%=userid %>"/>
									<input type="hidden" name="roleid"  id="roleid" value="<%=roleid %>"/>
									</td>
									</tr>
									<tr>
									<td align="left">
									<input type="button"  onclick="check()" value="查询" class="input_button2"/>
									<input type="button"    value="重置" class="input_button2" onclick="resetData();">
									</td>
									<td>
<!-- 									<input type="button" onclick="resultdatadeal();" value="结案处理"  class="input_button2"/>
 -->									<input type ="button" value="批量处理" class="input_button2" <%if(views.size()==0){ %> disabled="disabled" <%} %> onclick="stateBatch();"/>
									</td>
									<td>
									<input type="hidden" id="ishandlehhh" name="ishandlehhh" value="<%=ishandle%>"/>
									<input type="button" onclick="reviseQuestionError();" value="修改"  class="input_button2"/>
									</td>
									<td></td>
									<td>
									<input type ="button" id="btnval" value="导出" class="input_button2" <%if(views.size()==0){ %> disabled="disabled" <%} %> onclick="exportField();"/>
									</td>
								<%-- 	<%if(views != null && views.size()>0){ %>
										<input type ="button" value="批量处理" class="input_button2"  onclick="stateBatch();"/>
										<input type ="button" id="btnval" value="导出" class="input_button1" onclick="exportField();"/>
									<%} %> --%>
									
								</tr>
								
								</table>
							</form>
				</div>
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
					<tbody>
						<tr class="font_1" height="30" >
							<td width="30"  align="center" valign="middle" bgcolor="#f3f3f3"><a style="cursor: pointer;" onclick="isgetallcheck();">全选</a></td>
							<td width="100" align="center" valign="middle" bgcolor="#f3f3f3">问题件单号</td>
							<td width="100" align="center" valign="middle" bgcolor="#f3f3f3">订单号</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">供货商</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单类型</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">发货时间</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单操作状态</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">创建机构</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">创建人</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">问题件类型</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">创建时间</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">问题件说明</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">问题件状态</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">处理结果</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">责任机构</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">责任人</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">是否关联扣罚单</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">丢失找回</td>
							<td width="160" align="center" valign="middle" bgcolor="#E7F4E3">操作</td>
						</tr>
					</tbody>
				</table>
				</div>
				<br>
				<br>
				<br>
			<div style="height: 500px;overflow:auto; ">
		    <div style="height: 140px;"></div>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
				<tbody>
					<%if(views!=null||views.size()>0)for(AbnormalView view : views){ %>
					<tr height="30" >
						<td width="30" align="center" valign="middle" bgcolor="#eef6ff">
						<%if(view.getIshandle()!=5) {%>
						<input id="id" type="checkbox" value="<%=view.getId()%>"  name="id"/>
						<%} %>
						</td>
						<td width="100" align="center" valign="middle"><%=view.getQuestionno() %></td>
						<td width="100" align="center" valign="middle"><%=view.getCwb() %></td>
						<td width="100" align="center" valign="middle"><%=view.getCustomerName() %></td>
						<td width="100" align="center" valign="middle"><%=view.getCwborderType() %></td>
						<td width="100" align="center" valign="middle"><%=view.getEmaildate() %></td>
						<td width="100" align="center" valign="middle"><%=view.getFlowordertype() %></td>
						<td width="100" align="center" valign="middle"><%=view.getBranchName()  %></td>
						<td width="100" align="center" valign="middle"><%=view.getCreuserName() %></td>
						<td width="100" align="center" valign="middle"><%=view.getAbnormalType() %></td>
						<td width="100" align="center" valign="middle"><%=view.getCredatetime() %></td>
						<td width="100" align="center" valign="middle"><%=view.getDescribe() %></td>
						<td width="100" align="center" valign="middle"><%=view.getIshandleName() %></td>
						<td width="100" align="center" valign="middle"><%=view.getDealResultContent() %></td>
						<td width="100" align="center" valign="middle"><%=view.getDutybranchname() %></td>
						<td width="100" align="center" valign="middle"><%=view.getDutyperson() %></td>
						<td width="100" align="center" valign="middle"><%=view.getIsfinecontent() %></td>
						<td width="100" align="center" valign="middle"><%=view.getIsfindInfo() %></td>
						<td width="160" align="center" valign="middle">
						<%if(!ishandle.equals(AbnormalOrderHandleEnum.jieanchuli.getValue()+"")){ %>
						<a id="" href="javascript:void(0);" style="color: blue;" onclick="getThisBoxList('<%=view.getId() %>','0');">处理</a>
						<a href="javascript:void(0);" style="color: blue;" onclick="resultdatadeal('<%=view.getId() %>');">结案处理</a></td>
						<%}else{
							%>
							<a href="javascript:void(0);" style="color: blue;"  onclick="getThisBoxList('<%=view.getId() %>','1');">查看</a>
<%-- 						<input type="button" name="" id="" value="查看" class="input_button2" onclick="getThisBoxList('<%=view.getId() %>','1');"/></td>
						<input type="button" name="" id="" value="结案处理" class="input_button2" onclick="resultdatadeal(<%=view.getId() %>);"/></td> --%>
						
						<%} %>
						<input type="hidden" id="handle<%=view.getId() %>" value="<%=request.getContextPath()%>/abnormalOrder/getabnormalOrder/<%=view.getId() %>?type=1" />
						<input type="hidden" id="useridhhh<%=view.getId() %>"  value="<%=view.getCreuserid() %>"/>
						<input type="hidden" id="dutyuseridhhh<%=view.getId() %>"  value="<%=view.getDutypersonid() %>"/>
						<input type="hidden" id="handlehhh<%=view.getId() %>"  value="<%=view.getIshandle() %>"/>
						<input type="hidden" id="iszhanzhang<%=view.getId() %>"  value="<%=view.getIszhanzhang() %>"/>
						
					</tr>
					<%} %>
				</tbody>
			</table>
			<div style="height: 120px;"></div>
			</div>
			</div>
			<%if(page_obj.getMaxpage()>1){ %>
				<div class="iframe_bottom">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
						<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
						　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
								id="selectPg"
								onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
								<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
								<option value="<%=i %>"><%=i %></option>
								<% } %>
							</select>页
					</td>
				</tr>
				</table>
				</div>
			<%} %>
	</div>
</div>
<form action="<%=request.getContextPath()%>/abnormalOrder/exportExcle" method="post" id="searchForm2">
	<input type="hidden" name="cwbStrs" id="cwbStrs" value="<%=request.getParameter("cwb")==null?"":request.getParameter("cwb") %>"/>
	<input type="hidden" name="createbranchid1" id="createbranchid1" value="<%=request.getParameter("createbranchid")==null?"0":request.getParameter("createbranchid")%>"/>
	<input type="hidden" name="dutybranchid1" id="dutybranchid1" value="<%=request.getParameter("dutybranchid")==null?"0":request.getParameter("dutybranchid")%>"/>
	<input type="hidden" name="abnormaltypeid1" id="abnormaltypeid1" value="<%=request.getParameter("abnormaltypeid")==null?"0":request.getParameter("abnormaltypeid")%>"/>
	<input type="hidden" name="ishandle1" id="ishandle1" value="<%=request.getParameter("ishandle")==null?"0":request.getParameter("ishandle")%>"/>
	<input type="hidden" name="losebackisornot1" id="losebackisornot1" value="<%=request.getParameter("losebackisornot")==null?"0":request.getParameter("losebackisornot")%>"/>
	<input type="hidden" name="dealresult1" id="dealresult1" value="<%=request.getParameter("dealresult")==null?"0":request.getParameter("dealresult")%>"/>
	<input type="hidden" name="begindate1" id="bedindate1" value="<%=request.getParameter("begindate")==null?"":request.getParameter("begindate")%>"/>
	<input type="hidden" name="enddate1" id="enddate1" value="<%=request.getParameter("enddate")==null?"":request.getParameter("enddate")%>"/>
	<input type="hidden" name="chuangjianbegindate1" id="chuangjianbedindate1" value="<%=request.getParameter("chuangjianbegindate")==null?"":request.getParameter("chuangjianbegindate")%>"/>
	<input type="hidden" name="chuangjianenddate1" id="chuangjianenddate1" value="<%=request.getParameter("chuangjianenddate")==null?"":request.getParameter("chuangjianenddate")%>"/>
	<input type="hidden" name="customerid1" id="customerid1" value="<%=request.getParameter("customerid")==null?"":request.getParameter("customerid")%>"/>
</form>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#abnormaltypeid").val(<%=request.getParameter("abnormaltypeid")==null?0:Long.parseLong(request.getParameter("abnormaltypeid"))%>);
$("#ishandle").val(<%=request.getParameter("ishandle")==null?0:Long.parseLong(request.getParameter("ishandle"))%>);
$("#dutybranchid").val(<%=request.getParameter("dutybranchid")==null?0:Long.parseLong(request.getParameter("dutybranchid"))%>);
$("#createbranchid").val(<%=request.getParameter("createbranchid")==null?0:Long.parseLong(request.getParameter("createbranchid"))%>);
$("#customerid").val(<%=request.getParameter("customerid")==null?0:Long.parseLong(request.getParameter("customerid"))%>);
$("#losebackisornot").val(<%=request.getParameter("losebackisornot")==null?-1:Long.parseLong(request.getParameter("losebackisornot"))%>);
$("#dealresult").val(<%=request.getParameter("dealresult")==null?0:Long.parseLong(request.getParameter("dealresult"))%>);
$("#strtime").val('<%=request.getParameter("begindate")==null?"":request.getParameter("begindate")%>');
$("#endtime").val('<%=request.getParameter("enddate")==null?"":request.getParameter("enddate")%>');
$("#chuangjianstrtime").val('<%=request.getParameter("chuangjianbegindate")==null?"":request.getParameter("chuangjianbegindate")%>');
$("#chuangjianendtime").val('<%=request.getParameter("chuangjianenddate")==null?"":request.getParameter("chuangjianenddate")%>');
function exportField(){
	if(<%=views != null && views.size()>0 %>){
		$("#btnval").attr("disabled","disabled");
	 	$("#btnval").val("请稍后……");
	 	$("#searchForm2").submit();
	}else{
		alert("没有做查询操作，不能导出！");
	};
}

</script>
</body>
</html>

