<%@page import="cn.explink.domain.CsConsigneeInfo"%>
<%@page import="cn.explink.domain.CwbOrderAndCustomname"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<CwbOrderAndCustomname> lco=(List<CwbOrderAndCustomname>)request.getAttribute("lco")==null?null:(List<CwbOrderAndCustomname>)request.getAttribute("lco");	
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<%-- <script language="javascript" src="<%=request.getContextPath()%>/js/temp.js"></script> --%>
<script type="text/javascript">
function getGoonacceptWO(GV,GS){

	$('#GV').val(GV);
	$('#GS').val(GS);
}

function gettrValue(cwbId){
	//$('#GV').val("");
	$('#trid').val(cwbId);
	var cwbStr = 'cwb'+cwbId;
	WOcwb=$('#'+cwbStr).text();

	$.ajax({
		type:'POST',
		data:'cwb='+WOcwb,	
		url:'<%=request.getContextPath()%>/workorder/findGoOnAcceptWOByCWB',
		datatype:'json',
		success:function(data){
			var thStr1 = "<tr class='font_1'>" +
			"<th bgcolor='#eef6ff'>工单号</th>" +
			"<th bgcolor='#eef6ff'>订单号</th>" +
			"<th bgcolor='#eef6ff'>来电号码</th>" +
			"<th bgcolor='#eef6ff'>归属地</th>"	 +			
			"<th bgcolor='#eef6ff'>受理时间</th>" +
			"<th bgcolor='#eef6ff'>工单类型</th>" +
			"<th bgcolor='#eef6ff'>受理内容</th>" +
			"<th bgcolor='#eef6ff'>工单状态</th></tr>";
$.each(data,function(ind,ele){
	var dataTrStr1 = "";
	dataTrStr1 = "<tr onclick='getGoonacceptWO(\""+ele.acceptNo+"\",\""+ele.complaintState+"\")'>" +
			"<td>"+ele.acceptNo+"</td>" +
			"<td><a href='<%=request.getContextPath()%>/order/queckSelectOrder/"+ele.orderNo+"' target='_Blank'>"+ele.orderNo+"</a></td>" +
			"<td>"+ele.phoneOne+"</td>" +
			"<td>"+ele.provence+"</td>" +
			"<td>"+ele.acceptTime+"</td>" +
			"<td>"+ele.showcomplaintTypeName+"</td>" +
			"<td>"+ele.content+"</td>" +
			"<td>"+ele.showComplaintStateName+"</td></tr>";
	thStr1 += dataTrStr1;
	
});
$("#showWO").html(thStr1);
selectcolorofwo();
		}
	});	
	var cwbStr="";	
}


$(function(){
	$("#contactLastTime").datetimepicker({
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
		if($('#GS').val()==4){
			alert("此工单已结束，不能继续操作");
			return false;
		}
		if($('#GV').val()==""){
			alert('请选择一条数据');
			return false;
		}
		getAddBoxx2($('#GV').val());
	});
	
	selectcolor();
	selectcolorofwo();
	

});

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
		data:"opscwbid="+$('#trid').val(),
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
		data:"opscwbid="+$('#trid').val(),
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
	}else if($('#sex1').val()==""&&$('#sex').val()==""){
		alert('请选择性别');
		return false;
	}else if($('#province').val()==""){
		alert('请输入省份');
		return false;
	}else if($('#city').val()==""){
		alert('请输入城市');
		return false;
	}else if($('#name').val()==""){
		alert('请输入姓名');
		return false;
	}else if($('#contactLastTime').val()==""){
		alert('请输入来电时间');
		return false;
	}
	$.ajax({
		type:'POST',
		data:$('#cciForm').serialize(),
		url:'<%=request.getContextPath()%>/workorder/addCsConsigneeInfo',
		success:function(data){
			alert("保存成功");
		}
		
		
	});
	
}

function SelectPhone(){
		 $.ajax({			 
			 type:'POST',
			 data:'phoneonOne='+$(phoneonOne).val(),
			 dataType:'json',
			 url:'<%=request.getContextPath()%>/workorder/selectByPhoneNum',
			 success:function(data){
				 if(data!=null)
				 
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
					
				  }
			 });

	
}

function SelectdetailForm(){
	$.ajax({
		type:'POST',
		data:'phoneonOne='+$(phoneonOne).val(),
		datatype:'json',
		url:'<%=request.getContextPath()%>/workorder/SelectdetalForm',
		success:function(data){					
				var thStr = "<tr>" +
							"<th bgcolor='#eef6ff'>订单号</th>" +
							"<th bgcolor='#eef6ff'>运单号</th>" +
							"<th bgcolor='#eef6ff'>供货商</th>" +
							"<th bgcolor='#eef6ff'>发货时间</th>" +
							"<th bgcolor='#eef6ff'>收件人名称</th>" +
							"<th bgcolor='#eef6ff'>收货地址</th>" +
							"<th bgcolor='#eef6ff'>手机</th>" +
							"<th bgcolor='#eef6ff'>当前状态</th></tr>";
				$.each(data,function(ind,ele){
				
				
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
				selectcolor();			
		}
		
	});
	
}

	
function submitselect(){
	
	if($('#cwb123').val()==""&&$('#emaildate123').val()==""&&$('#consigneename123').val()==""&&$('#consigneemobile123').val()==""){
		alert('请输入查询条件~O(∩_∩)O~');
		return false;
	}else
	$.ajax({
		type:'POST',
		data:$('#selectF').serialize(),
		url:$('#selectF').attr('action'),
		dataType:'json',
		success:function(data){
			var thStr = "<tr>" +
			"<th bgcolor='#eef6ff'>订单号</th>" +
			"<th bgcolor='#eef6ff'>运单号</th>" +
			"<th bgcolor='#eef6ff'>供货商</th>" +
			"<th bgcolor='#eef6ff'>发货时间</th>" +
			"<th bgcolor='#eef6ff'>收件人名称</th>" +
			"<th bgcolor='#eef6ff'>收货地址</th>" +
			"<th bgcolor='#eef6ff'>手机</th>" +
			"<th bgcolor='#eef6ff'>当前状态</th></tr>";
$.each(data,function(ind,ele){
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
selectcolor();
			
			
		}
	
	});


}

function submitselect2(){    //通过手机号查询工单
	$.ajax({
		type:'POST',
		data:'phoneonOne='+$(phoneonOne).val(),
		url:'<%=request.getContextPath()%>/workorder/findGoOnAcceptWO',
		dataType:'json',
		success:function(data){
			var thStr1 = "<tr class='font_1'>" +
			"<th bgcolor='#eef6ff'>工单号</th>" +
			"<th bgcolor='#eef6ff'>订单号</th>" +
			"<th bgcolor='#eef6ff'>来电号码</th>" +
			"<th bgcolor='#eef6ff'>归属地</th>"	 +			
			"<th bgcolor='#eef6ff'>受理时间</th>" +
			"<th bgcolor='#eef6ff'>工单类型</th>" +
			"<th bgcolor='#eef6ff'>受理内容</th>" +
			"<th bgcolor='#eef6ff'>工单状态</th></tr>";
$.each(data,function(ind,ele){
	var dataTrStr1 = "";
	dataTrStr1 = "<tr onclick='getGoonacceptWO(\""+ele.acceptNo+"\",\""+ele.complaintState+"\")'>" +
			"<td>"+ele.acceptNo+"</td>" +  /* order/queckSelectOrder/123 */
			"<td>"+ele.orderNo+"</td>" +
			"<td>"+ele.phoneOne+"</td>" +
			"<td>"+ele.provence+"</td>" +
			"<td>"+ele.acceptTime+"</td>" +
			"<td>"+ele.showcomplaintTypeName+"</td>" +
			"<td>"+ele.content+"</td>" +
			"<td>"+ele.showComplaintStateName+"</td></tr>";
	thStr1 += dataTrStr1;
	
});
/* "<td>"+ele.showcomplaintTypeName+"</td>" +
"<td>"+ele.content+"</td>" +
"<td>"+ele.showComplaintStateName+"</td></tr>"; */
$("#showWO").html(thStr1);
selectcolorofwo();

			}
		});


}
 
function verifyphoneonOne(){                   //电话号码验证是否为空和是否为数字
	var reg = new RegExp("^[0-9]*$");  
    var obj = document.getElementById("phoneonOne");  
 if(!reg.test(obj.value)){  
     alert("请输入正确格式的手机号码!");
     return false;
 }else if($('#phoneonOne').val()!=""){
		SelectPhone();
		SelectdetailForm();
		submitselect2();
	}else{
		alert('手机号码不能为空O(∩_∩)O');
		return false;
	}
	
}



</script>


</head>
<body>
<div>

		<table width="100%">
				<form id="cciForm" action="<%=request.getContextPath()%>/workorder/addCsConsigneeInfo">
				<tr>
					<td>来电号码:<input type="text" name="phoneonOne" 
					id="phoneonOne" 
					onkeypress="if (event.keyCode == 13){verifyphoneonOne();}"  
					class="input_text1" />
					</td>
					<td>客户分类:
					<select name="consigneeType" id="consigneeType" class="select1">
						<option value="0" id="op0">普通客户</option>
						<option value="1" id="op1">VIP客户</option>						
					</select>
					</td>				
					<td>性别:
					男<input type="radio" name="sex" value="1" id="sex1">
					女<input type="radio" name="sex" value="0" id="sex">
					</td>
					<td>归属省份:<input type="text" name="province" class="input_text1" id="province"></td>
					<td>归属城市:<input type="text" name="city" id="city" class="input_text1"></td>
				</tr>
				<tr>									
					<td>来电姓名:<input type="text" name="name" class="input_text1" id="dname"></td>
					<td>最后联系时间:<input type="text" name="contactLastTime" id="contactLastTime" class="input_text1"></td>
					<td>联系次数:<input type="text" name="contactNum" class="input_text1" id="contactNum"></td>
					
					</td>
				</tr>	
				</form>
				<tr><td><button onclick="addCci()" class="input_button2">保存</button></td></tr>
				</table>
		</div>
<hr>		

		<div>

			<hr>
		<div>	

			<table>
				<form action="<%=request.getContextPath()%>/workorder/selectDetalFormByCondition" id="selectF">
					<tr>
						<td>发货时间:<input type="text" name="emaildate"  id="emaildate123" class="input_text1" onkeydown="if(event.keyCode == 13){submitselect(); }"/></td>
						<td>订/运单号:<input type="text" name="cwb"  id="cwb123" class="input_text1" onkeydown="if(event.keyCode == 13){submitselect(); }"/></td>
						<td>收件人姓名:<input type="text" name="consigneename"  id="consigneename123" class="input_text1" onkeydown="if(event.keyCode == 13){submitselect(); }"/></td>
						<td>收件人手机:<input type="text" name="consigneemobile"  id="consigneemobile123" class="input_text1" onkeydown="if(event.keyCode == 13){submitselect(); }"/></td>
									
				</form>	
						<td><button class="input_button2" id="submitselect" onclick="submitselect()">查询</td> 
						<td><button id="createquerywo_button"  class="input_button1">创建查询工单</button></td>							
						<td><button id="createcomplain_buttonn"   class="input_button1">创建投诉工单</button></td>						
					</tr>
			</table>
			<hr>

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
		</div>	
	<hr></br>
			<button id="GoonacceptWO" class="input_button2">继续受理</button>
	<hr>	
	
		<div>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="showWO"></table>
		</div>
			
				
	</div>

	<input type="hidden" id=trid>
	<input type="hidden" id="createquerywo" value="<%=request.getContextPath()%>/workorder/CreateQueryWorkOrder"/>
	<input type="hidden" id="createcomplain" value="<%=request.getContextPath()%>/workorder/CreateComplainWorkOrder"/>					
	<input type="hidden" id="GoOnacceptWo" value="<%=request.getContextPath()%>/workorder/GoOnacceptWo"/>
	<input type="hidden" id="GV" />
	<input type="hidden" id="GS"/>
</body>

</html>