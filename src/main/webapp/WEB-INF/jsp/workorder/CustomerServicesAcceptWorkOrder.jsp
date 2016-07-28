<%@page import="cn.explink.domain.CsConsigneeInfo"%>
<%@page import="cn.explink.domain.CwbOrderAndCustomname"%>
<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@page import="cn.explink.enumutil.ComplaintStateEnum"%>
<%@page import="cn.explink.enumutil.ComplaintTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<CwbOrderAndCustomname> lco=request.getAttribute("lco")==null?null:(List<CwbOrderAndCustomname>)request.getAttribute("lco");	
	List<Reason> reasonList=request.getAttribute("KeHuLeiXingAllReason")==null?null:(List<Reason>)request.getAttribute("KeHuLeiXingAllReason");
	String username=(String)request.getParameter("Username");
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<%-- <script language="javascript" src="<%=request.getContextPath()%>/js/temp.js"></script> --%>
<script type="text/javascript">
/* Userflag */
function getGoonacceptWO(GV,GS){
	/* $('#Userflag').val(uf); */
	$('#GoonacceptWO').show();
	$('#GV').val(GV);
	$('#GS').val(GS);
	
}

function showWorkOrderDetailBox(V) {
	$.ajax({
		type : "POST",
		data:{"acceptNo":V},	
		url :"<%=request.getContextPath()%>/workorder/OrgAppeal",
		dataType : "html",
		success : function(data) {
			// alert(data);
			$("#alert_box", parent.document).html(data);

		},
		complete : function() {
			addInit();// 初始化某些ajax弹出页面
			viewBox();
		}
	});
}

function gettrValue(cwbId){
	//$('#GV').val("");
	$('#GoonacceptWO').hide();
	$('#createquerywo_button').show();
	$('#createcomplain_buttonn').removeAttr('disabled');
	$('#trid').val(cwbId);
	var cwbStr = 'cwb'+cwbId;
	   
	var cwbnum=$('#'+cwbStr).text();
	$('#WorkorderidValue').val(cwbnum);
	showWorkorder(1);
	var cwbStr="";
}
function showWorkorder(page){
	$.ajax({
		type:'POST',
		data:'cwb='+$('#WorkorderidValue').val(),	
		url:'<%=request.getContextPath()%>/workorder/findGoOnAcceptWOByCWB/'+page,
		datatype:'json',
		success:function(data){
			var pageFlag = false;
			var thStr1 = "<tr class='font_1'>" +
			"<th bgcolor='#eef6ff'>工单号</th>" +
			"<th bgcolor='#eef6ff'>订单号</th>" +
			"<th bgcolor='#eef6ff'>来电号码</th>" +
			"<th bgcolor='#eef6ff'>归属地</th>" +			
			"<th bgcolor='#eef6ff'>受理时间</th>" +
			"<th bgcolor='#eef6ff'>受理内容</th>" +
			"<th bgcolor='#eef6ff'>工单状态</th></tr>";
			$.each(data.list,function(ind,ele){
				pageFlag = true;
				var dataTrStr1 = "";
					var contentTemp = ele.content;								
					contentTemp = contentTemp.substring(0,10);					
					dataTrStr1 = "<tr onclick='getGoonacceptWO(\""+ele.acceptNo+"\",\""+ele.complaintState+"\")'>" +
							"<td><a href='javascript:showWorkOrderDetailBox(\""+ele.acceptNo+"\")'>"+ele.acceptNo+"</a></td>" +  /* order/queckSelectOrder/123 */
							"<td>"+ele.orderNo+"</td>" +
							"<td>"+ele.phoneOne+"</td>" +
							"<td>"+ele.provence+"</td>" +
							"<td>"+ele.acceptTime+"</td>" +
							"<td>"+contentTemp+"</td>" +
							"<td>"+ele.showComplaintStateName+"</td></tr>";					
				thStr1 += dataTrStr1;				
			});
			$("#showWO").html(thStr1);			
			var apage="<tr><td height='38' align='center' valign='middle' bgcolor='#eef6ff'><a href='javascript:showWorkorder(1)'>"+'第一页'+"</a>" +
			"<a href='javascript:showWorkorder("+data.page_obj.previous+")'>"+'上一页'+"</a>"+
			"<a href='javascript:showWorkorder("+data.page_obj.next+")'>"+'下一页'+"</a>" +
			"<a href='javascript:showWorkorder("+data.page_obj.maxpage+")'>"+'最后一页'+"</a><td>"+
			"</tr>";
			if(pageFlag){
				$("#pageWo").html(apage);
			}
			selectcolorofwo();
			}
		});	
	}

	
	
	
$(function(){
	
	if($('#GV').val()==""){
		$('#GoonacceptWO').hide();
	}
	
	if($('#trid').val()==""){
		$('#createquerywo_button').hide();
		$('#createcomplain_buttonn').attr('disabled','disabled');
	}
	
	
	
	$("#emaildate123").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
	$("#emaildate456").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#createcomplain_buttonn").click(function(){
		getAddBoxx();
	});
	$("#createquerywo_button").click(function(){
		getAddBoxx1();
	});
	$("#GoonacceptWO").click(function(){
		if($('#GS').val()==$('#ComplaintStateEnumValue').val()){
			alert("此工单已结束，不能继续操作");
			return false;
		}
/* 		if($('#GV').val()==""){
			alert('请选择一条数据');
			return false;
		} */
		
/* 		 if($('#Userflag').val()==$('#ComplaintTypeEnumChaXun').val()){
			 getAddBoxGoOnAcceptQueryWo($('#GV').val());
		}else if($('#Userflag').val()==$('#ComplaintTypeEnumTouSu').val()){	 */					
			getAddBoxx2($('#GV').val());
	/* 	}  */
		/* getAddBoxGoOnAcceptQueryWo($('#GV').val()); */
		
		
		
		
	});
	
	selectcolor();
	selectcolorofwo();
	

});

function getAddBoxGoOnAcceptQueryWo(valWo) {
	 
	$.ajax({
		type : "POST",
		data:"workorder="+valWo,
		url : $("#GoOnacceptWoQuery").val(),
		dataType : "html",
		success : function(data) {
			// alert(data);
			$("#alert_box", parent.document).html(data);
			
		},
		complete : function() {
			addInit();// 初始化某些ajax弹出页面			
			viewBox();
		}
	});
	}

function selectcolorofwo(){
	$("table#showWO tr").click(function(){
		$(this).css("backgroundColor","yellow");
		$(this).siblings().css("backgroundColor","#ffffff");
	});
}

function selectcolor(){
	$("table#CsAcceptDg tr").click(function(){
		$(this).css("backgroundColor","yellow");
		$(this).siblings().css("backgroundColor","#ffffff");
	});

	
	
	}

function getAddBoxx() { 
	$.ajax({
		type : "POST",
	/* 	data:"opscwbid="+$('#trid').val()&"CallerPhoneValue="+$('#CallerPhoneValue').val(), */
		data:{"opscwbid":$('#trid').val(),"CallerPhoneValue":$('#CallerPhoneValue').val()},
		url : $("#createcomplain").val(),
		dataType : "html",
		success : function(data) {
			// alert(data);
			$("#alert_box", parent.document).html(data);

		},
		complete : function() {
			addInit();// 初始化某些ajax弹出页面
			viewBox();
		}
	});
	//$('#trid').val("");
}
function getAddBoxx1() {
	$.ajax({
		type : "POST",
		/* data:"opscwbid="+$('#trid').val(), */
		data:{"opscwbid":$('#trid').val(),"CallerPhoneValue":$('#CallerPhoneValue').val()},
		url : $("#createquerywo").val(),
		dataType : "html",
		success : function(data) {
			$("#alert_box", parent.document).html(data);
			
		},
		complete : function() {
			addInit();// 初始化某些ajax弹出页面			
			viewBox();
		}
	});
	//$('#trid').val("");
}


function addInit(){
}

function addCci(){
	if($('#phoneonOne').val()==""){
		alert('请输入电话号码');
		return false;
	}else if($('#dname').val()==""){
		alert('请输入姓名');
		return false;
	}
	
	/****begin****/
	//vic.liang@pjbest.com 2016-07-13 工单导入修改必填项
	/* else if($('#consigneeType').val()=="-1"){
		alert('请选择客户类型');
		return false;
 	}else if($('#sex1').val()==""&&$('#sex').val()==""){
	}else if($("input[name='sex']:checked").val() == undefined ){
		alert('请选择性别');
		return false;
	}else if($('#province').val()==""){
		alert('请输入省份');
		return false;
	}else if($('#city').val()==""){
		alert('请输入城市');
		return false;
	} */
	/****end*****/

	/* else if($('#contactLastTime').val()==""){
		alert('请输入来电时间');
		return false;
	}

	else if($('#contactNum').val()==""){
		alert('请填写联系次数');
		return false;
	} */
	$('#CallerPhoneValue').val($('#phoneonOne').val());
	$.ajax({
		type:'POST',
		data:$('#cciForm').serialize(),
		url:'<%=request.getContextPath()%>/workorder/addCsConsigneeInfo',
		success:function(data){
			alert("保存成功");
		}
		
		
	});
	
		$('#savecallerinfo').attr("disabled","disabled");

	
	
}
function SelectPhone(){

		 $.ajax({			 
			 type:'POST',
			 data:'phoneonOne='+$(phoneonOne).val(),
			 dataType:'json',
			 url:'<%=request.getContextPath()%>/workorder/selectByPhoneNum',
			 success:function(data){
				 if(data!=null){	
						 $('#contactLastTime').show();
						 $('#contactNum').show();
					$('#dname').val(data.name);
					 $('#consigneeType').val(data.consigneeType); 
					$('#city').val(data.city);
					$('#province').val(data.province);
					$('#contactNum').val(data.contactNum);
					$('#contactLastTime').val(data.contactLastTime);
					if(data.sex==1){
						$('#sex1').attr("checked",'true');
					}else{
						$('#sex').attr("checked","true");
					}
					$('#savecallerinfo').attr("disabled","disabled");
					 }else if(data==null){
						 var tm=CurentTime();
						 $('#contactLastTime').show();
						 $('#contactNum').show();
						 $('#contactLastTime').val(tm);
						 $('#contactNum').val('1');
					 $('#savecallerinfo').removeAttr("disabled");
					 $('#dname').val("");
					 $('#consigneeType').val(""); 
					$('#city').val("");
					$('#province').val("");
					$('#sex').removeAttr("checked");
					$('#sex1').removeAttr("checked");
					 
				 	}
				  }
		 });	
}
function SelectdetailForm(page){
	$.ajax({
		type:'POST',
		data:'phoneonOne='+$(phoneonOne).val(),
		datatype:'json',
		url:'<%=request.getContextPath()%>/workorder/SelectdetalForm/'+page,
		success:function(data){					
				var pageFlag = false;
				var thStr = "<tr>" +
							"<th bgcolor='#eef6ff'>订单号</th>" +
							"<th bgcolor='#eef6ff'>运单号</th>" +
							"<th bgcolor='#eef6ff'>供货商</th>" +
							"<th bgcolor='#eef6ff'>发货时间</th>" +
							"<th bgcolor='#eef6ff'>收件人名称</th>" +
							"<th bgcolor='#eef6ff'>收货地址</th>" +
							"<th bgcolor='#eef6ff'>手机</th>" +
							"<th bgcolor='#eef6ff'>当前状态</th></tr>";
				$.each(data.list,function(ind,ele){
				
					pageFlag = true;
					var dataTrStr = "";
					dataTrStr = "<tr onclick='gettrValue("+ele.id+")' id='eleid'>" +
							"<td id='cwb"+ele.id+"'><a href='<%=request.getContextPath()%>/order/queckSelectOrder/"+ele.cwb+"' target='_Blank'>"+ele.cwb+"</a></td>" +
							"<td>"+ele.transcwb+"</td>" +
							"<td>"+ele.customername+"</td>" +
							"<td>"+ele.emaildate+"</td>" +
							"<td>"+ele.consigneename+"</td>" +
							"<td>"+ele.consigneeaddress+"</td>" +
							"<td>"+ele.consigneemobile+"</td>" +
							"<td>"+ele.cwbstate+"</td>" +
							"</tr>";
					thStr += dataTrStr;
					
				});
				$("#CsAcceptDg").html(thStr);
				var apage="<tr><td height='38' align='center' valign='middle' bgcolor='#eef6ff'><a href='javascript:SelectdetailForm(1)'>"+'第一页'+"</a>&nbsp;&nbsp;&nbsp;" +
				"<a href='javascript:SelectdetailForm("+data.page_obj.previous+")'>"+'上一页'+"</a>&nbsp;&nbsp;&nbsp;"+
				"<a href='javascript:SelectdetailForm("+data.page_obj.next+")'>"+'下一页'+"</a>&nbsp;&nbsp;&nbsp;" +
				"<a href='javascript:SelectdetailForm("+data.page_obj.maxpage+")'>"+'最后一页'+"</a>&nbsp;&nbsp;<td>"+
				"</tr>";
				
				if(pageFlag){
					$("#pageid").html(apage);
				}
			
				selectcolor();			
		}
		
	});
	
}

	/* 
						"<a href='javascript:SelectdetailForm("+data.page_obj.previous < 1 ? 1 : data.page_obj.previous+")'>"+'上一页'+"</a>"+
				"<a href='javascript:SelectdetailForm("+data.page_obj.next < 1 ? 1 : data.page_obj.next+")'>"+'下一页'+"</a>" +
				"<a href='javascript:SelectdetailForm("+data.page_obj.maxpage < 1 ? 1 : data.page_obj.maxpage+")'>"+'最后一页'+"</a><td>"+
	*/
function submitselect(page){
	/* $('#createquerywo_button').hide(); */
	$('#createcomplain_buttonn').attr('disabled','disabled');
	if($('#cwb123').val()==""&&$('#emaildate123').val()==""&&$('#consigneename123').val()==""&&$('#consigneemobile123').val()==""){
		alert('请输入查询条件');
		return false;
	}else
	$.ajax({
		type:'POST',
		data:$('#selectF').serialize(),
		url:$('#selectF').attr('action')+"/"+page,
		dataType:'json',
		success:function(data){
			var pageFlag = false;
			var thStr = "<tr>" +
			"<th bgcolor='#eef6ff'>订单号</th>" +
			"<th bgcolor='#eef6ff'>运单号</th>" +
			"<th bgcolor='#eef6ff'>供货商</th>" +
			"<th bgcolor='#eef6ff'>发货时间</th>" +
			"<th bgcolor='#eef6ff'>收件人名称</th>" +
			"<th bgcolor='#eef6ff'>收货地址</th>" +
			"<th bgcolor='#eef6ff'>手机</th>" +
			"<th bgcolor='#eef6ff'>当前状态</th></tr>";
			
			
			$.each(data.list,function(ind,ele){
				pageFlag = true;
				var dataTrStr = "";
				dataTrStr = "<tr onclick='gettrValue("+ ele.id +")' id='eleid'>" +
						"<td id='cwb"+ele.id+"'><a href='<%=request.getContextPath()%>/order/queckSelectOrder/"+ele.cwb+"' target='_Blank'>"+ele.cwb+"</a></td>" +
						"<td>"+ele.transcwb+"</td>" +
						"<td>"+ele.customername+"</td>" +
						"<td>"+ele.emaildate+"</td>" +
						"<td>"+ele.consigneename+"</td>" +
						"<td>"+ele.consigneeaddress+"</td>" +
						"<td>"+ele.consigneemobile+"</td>" +
						"<td>"+ele.cwbstate+"</td></tr>";
				thStr += dataTrStr;
	
});
	$("#CsAcceptDg").html(thStr);
	var apage="<tr><td height='38' align='center' valign='middle' bgcolor='#eef6ff'><a href='javascript:submitselect(1)'>"+'第一页'+"</a>&nbsp;&nbsp;&nbsp;" +
	"<a href='javascript:submitselect("+data.page_obj.previous+")'>"+'上一页'+"</a>&nbsp;&nbsp;&nbsp;"+
	"<a href='javascript:submitselect("+data.page_obj.next+")'>"+'下一页'+"</a>&nbsp;&nbsp;&nbsp;" +
	"<a href='javascript:submitselect("+data.page_obj.maxpage+")'>"+'最后一页'+"</a><td>&nbsp;&nbsp;&nbsp;"+
	"</tr>";
	if(pageFlag){
		$("#pageid").html(apage);
	}
	


selectcolor();
			
			
		}
	
	});


}

function submitselect2(page){    //通过手机号查询工单
	$.ajax({
		type:'POST',
		data:'phoneonOne='+$(phoneonOne).val(),
		url:'<%=request.getContextPath()%>/workorder/findGoOnAcceptWO/'+page,
			dataType : 'json',
			success : function(data) {
				var pageFlag = false;
				var thStr1 = "<tr class='font_1'>"
						+ "<th bgcolor='#eef6ff'>工单号</th>"
						+ "<th bgcolor='#eef6ff'>订单号</th>"
						+ "<th bgcolor='#eef6ff'>来电号码</th>"
						+ "<th bgcolor='#eef6ff'>归属地</th>"
						+ "<th bgcolor='#eef6ff'>受理时间</th>"
						+ "<th bgcolor='#eef6ff'>受理内容</th>"
						+ "<th bgcolor='#eef6ff'>工单状态</th></tr>";
				$.each(data.list,function(ind, ele) {
							pageFlag = true;
							var dataTrStr1 = "";
								var contentTemp = ele.content;								
								contentTemp = contentTemp.substring(0,10);
								dataTrStr1 = "<tr><td><a href='javascript:showWorkOrderDetailBox(\""+ele.acceptNo+"\")'>"
										+ ele.acceptNo + "</a></td>" + 
										"<td>" + ele.orderNo + "</td>" + "<td>"
										+ ele.phoneOne + "</td>" + "<td>"
										+ ele.provence + "</td>" 
										+ "<td>"+ ele.acceptTime + "</td>" 
										+ "<td>" + contentTemp + "</td>"
										+ "<td>" + ele.showComplaintStateName
										+ "</td></tr>";
						
							thStr1 += dataTrStr1;

						});

				$("#showWO").html(thStr1);
				var apage="<tr><td height='38' align='center' valign='middle' bgcolor='#eef6ff'><a href='javascript:submitselect2(1)'>"+'第一页'+"</a>&nbsp;&nbsp;&nbsp;" +
				"<a href='javascript:submitselect2("+data.page_obj.previous+")'>"+'上一页'+"</a>&nbsp;&nbsp;&nbsp;"+
				"<a href='javascript:submitselect2("+data.page_obj.next+")'>"+'下一页'+"</a>&nbsp;&nbsp;&nbsp;" +
				"<a href='javascript:submitselect2("+data.page_obj.maxpage+")'>"+'最后一页'+"</a><td>&nbsp;&nbsp;&nbsp;"+
				"</tr>";			
				if(pageFlag){
					$("#pageWo").html(apage);
				}
				selectcolorofwo();

			}
		});

	}

	function verifyphoneonOne() {
		$('#CallerPhoneValue').val($('#phoneonOne').val());
	
/* 		$('#createquerywo_button').hide(); */
		$('#createcomplain_buttonn').attr('disabled','disabled');
		var reg = /^1\d{10}$/  //电话号码验证是否为空和是否为数字
		var obj = document.getElementById("phoneonOne");
		if (!reg.test(obj.value)) {
			alert("请输入正确格式的手机号码!");
			return false;
		} else if ($('#phoneonOne').val() != "") {
			SelectPhone();
			SelectdetailForm(1);
			submitselect2(1);
		} else {
			alert('手机号码不能为空');
			return false;
		}
	}
	
	
    function CurentTime()   
    { 
        var now = new Date();
        
        var year = now.getFullYear();       //年
        var month = now.getMonth() + 1;     //月
        var day = now.getDate();            //日
        
        var hh = now.getHours();            //时
        var mm = now.getMinutes();          //分
        var ss = now.getSeconds();           //秒
        
        var clock = year + "-";
        
        if(month < 10)
            clock += "0";
        
        clock += month + "-";
        
        if(day < 10)
            clock += "0";
            
        clock += day + " ";
        
        if(hh < 10)
            clock += "0";
            
        clock += hh + ":";
        if (mm < 10) clock += '0'; 
        clock += mm + ":"; 
         
        if (ss < 10) clock += '0'; 
        clock += ss; 
        return(clock); 
    }



</script>


</head>
<body> <!--  onload="startclock()" -->
<div>

		<table width="100%">
				<form id="cciForm" action="<%=request.getContextPath()%>/workorder/addCsConsigneeInfo">
				<tr>
					<td><font color="red">*</font>来电号码:<input type="text" name="phoneonOne" 
					id="phoneonOne" 
					onkeypress="if (event.keyCode == 13){verifyphoneonOne();}"  
					class="input_text1" maxlength="11"/>
					</td>
					<td>客户分类:
					<select name="consigneeType" id="consigneeType" class="select1">
						<option value="-1">选择客户分类</option>
						<%for(Reason r:reasonList) {%>
						<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent()%></option>	
						<%} %>				
					</select>
					</td>				
					<td noWrap="noWrap">性别:
					男<input type="radio" name="sex" value="1" id="sex1">
					女<input type="radio" name="sex" value="0" id="sex">
					</td>
					<td noWrap="noWrap">归属省份:<input type="text" name="province" class="input_text1" id="province" onblur="isChineseValue(this.value)"></td>
					<td noWrap="noWrap">归属城市:<input type="text" name="city" id="city" class="input_text1" onblur="isChineseValue1(this.value)"></td>
				</tr>
				<tr>									
					<td noWrap="noWrap"><font color="red">*</font>来电姓名:<input type="text" name="name" class="input_text1" id="dname" maxlength="15"></td>
					<td noWrap="noWrap">最后联系时间:<input type="text" id="contactLastTime" disabled="disabled" style="display: none"/></td>
					<td noWrap="noWrap">联系次数:<input type="text" id="contactNum" disabled="disabled" style="display: none"></td>
				</tr>	
				</form>
				<tr>
					<td>
						<button onclick="addCci()" class="input_button2" id=savecallerinfo>保存</button>
					</td>
				</tr>
				</table>
		</div>
<hr>		

		<div>

			<hr>
		<div>	

			<table>
				<form action="<%=request.getContextPath()%>/workorder/selectDetalFormByCondition" id="selectF">
					<tr>
						<td noWrap="noWrap">发货时间:<input type="text" name="staremaildate"  id="emaildate123" class="input_text1"/>到<input type="text" name="endemaildate"  id="emaildate456" class="input_text1"/></td>
						<td noWrap="noWrap">订/运单号:<input type="text" name="cwb"  id="cwb123" class="input_text1" onkeydown="if(event.keyCode == 13){submitselect(1); }"/></td>
						<td noWrap="noWrap">收件人姓名:<input type="text" name="consigneename"  id="consigneename123" class="input_text1" onkeydown="if(event.keyCode == 13){submitselect(1); }"/></td>
						<td noWrap="noWrap">收件人手机:<input type="text" name="consigneemobile"  id="consigneemobile123" class="input_text1" onkeydown="if(event.keyCode == 13){submitselect(1); }"/></td>
									
				</form>	
						<td><button class="input_button2" id="submitselect" onclick="submitselect(1)">查询</td> 
						<!-- <td><button id="createquerywo_button"  class="input_button1">创建查询工单</button></td>	 -->						
						<td><button id="createcomplain_buttonn"  class="input_button1">创建工单</button></td>						
					</tr>
			</table>
			<hr>
				<div>
					<table id="CsAcceptDg" width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
						<tbody>
								<tr class="font_1">
									<th bgcolor="#eef6ff">订单号</th>
									<th bgcolor="#eef6ff">运单号</th>
									<th bgcolor="#eef6ff">供货商</th>
									<th bgcolor="#eef6ff">发货时间</th>
									<th bgcolor="#eef6ff">收件人名称</th>
									<th bgcolor="#eef6ff">收货地址</th>
									<th bgcolor="#eef6ff">手机</th>
									<th bgcolor="#eef6ff">当前状态</th>
								</tr>	
						</tbody>			
					</table>	
					
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1" id="pageid">
		
	</table>
				</div>			
		</div>	
	<hr></br>
			<!-- <button id="GoonacceptWO" class="input_button2" style="display: none;">继续受理</button> -->
	<hr>	
	
		<div>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="showWO"></table>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1" id="pageWo"></table>
		</div>
			
				
	</div>

	<input type="hidden" id="trid"/>
	<input type="hidden" id="Userflag"/>
	<input type="hidden" id="createquerywo" value="<%=request.getContextPath()%>/workorder/CreateQueryWorkOrder"/>
	<input type="hidden" id="createcomplain" value="<%=request.getContextPath()%>/workorder/CreateComplainWorkOrder"/>					
	<input type="hidden" id="GoOnacceptWo" value="<%=request.getContextPath()%>/workorder/GoOnacceptWo"/>
	<input type="hidden" id="GoOnacceptWoQuery" value="<%=request.getContextPath()%>/workorder/GoOnacceptWoQuery"/>
	<input type="hidden" id="GV" />
	<input type="hidden" id="GS"/>
	<input type="hidden" value="<%=username%>" id="currentusername"> 
	<input type="hidden" id="WorkorderidValue"/>
	<%-- <input type="hidden" id="ComplaintTypeEnumChaXun" value="<%=ComplaintTypeEnum.DingDanChaXun.getValue()%>"/>
	<input type="hidden" id="ComplaintTypeEnumTouSu" value="<%=ComplaintTypeEnum.CuijianTousu.getValue()%>"/> --%>
	<input type="hidden" id="CallerPhoneValue">
	
</body>

</html>