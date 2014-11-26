<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.dao.CustomerDAO"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.OutWarehouseGroup"%>
<%@page import="cn.explink.enumutil.OutwarehousegroupOperateEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Customer> customerList = request.getAttribute("customerList")==null?null:(List<Customer>)request.getAttribute("customerList");
List<CwbOrder> clist = request.getAttribute("clist")==null?null:(List<CwbOrder>)request.getAttribute("clist");
%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>小标签打印功能</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<script type="text/javascript">
function customeridchange(customerid){
	$.ajax({
		type:"POST",//数据发送方式 
		url:"<%=request.getContextPath()%>/cwbLablePrint/updateEmaildateid",//后台处理程序
		data:{customerid  :customerid},//参数
		success:function(data){
			$("#emaildateid").empty();//清空下拉框//$("#select").html('');
			if(data.length>0){
				for(var i=0;i<data.length;i++){
					$("<option value='"+data[i].emaildateid+"'>"+data[i].emaildatetime+"</option>").appendTo("#emaildateid");
				}
			}
		}		
	});
}

$(function(){
	$("#find").click(function(){
		if($("#cwbs").val()==""&&$("#customerid").val()==0){
			alert("订单号和供货商请任选其一");
			return false;
		}else{
			$("#searchForm").submit();
		}
	});
})
function bdprint(){
	var isprint = "";
	$('input[name="isprint"]:checked').each(function(){ //由于复选框一般选中的是多个,所以可以循环输出
		isprint = $(this).val();
		});
	if(isprint==""){
		alert("请选择要打印的订单！");
	}else{
		$("#selectforprintForm").submit();
	}
}

function isgetallcheck(){
	if($('input[name="isprint"]:checked').size()>0){
		$('input[name="isprint"]').each(function(){
			$(this).attr("checked",false);
		});
	}else{
		$('input[name="isprint"]').attr("checked",true);
	}
}
</script>
</head>
<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
		<form action="<%=request.getContextPath() %>/cwbLablePrint/cwbbarcodeprintlist" method="post" id="searchForm">
			订单号： <label for="textfield"></label> 
				<textarea name="cwbs" cols="25" rows="4" id="cwbs" style="vertical-align: middle;height:60px"  ></textarea>
			供货商：<select id ="customerid" name ="customerid" onchange="customeridchange($(this).val());"> 
			              <option value ="0">请选择</option>
			              <%for(Customer c:customerList){ %>
			                <option value ="<%=c.getCustomerid()%>"><%=c.getCustomername() %></option>
			              <%} %>
			            </select>
				<select id="emaildateid" name="emaildateid" >
					<option value="0">请选择发货时间</option>
				</select>
		      　　<input type="button" id="find" value="查询" class="input_button2" />
			<input type="button" onclick="bdprint();" value="打印" class="input_button2"/>
		</form>
		</div>
		<div class="right_title">
		<div class="jg_35"></div>
		<div class="jg_35"></div>
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
		   <tbody>
		   		<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
				   	<td width="60" align="center" valign="middle" bgcolor="#f3f3f3"><a style="cursor: pointer;" onclick="isgetallcheck();">全选</a></td>
				   	<td width="150" align="center" valign="middle" bgcolor="#EEF6FF">订单号</td>
				   <!-- 	<td width="120" align="center" valign="middle" bgcolor="#FFEFDF">配送站点</td>
				   	<td width="120" align="center" valign="middle" bgcolor="#EEF6FF">订单类型</td>
				   	<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">代收款</td> -->
				   	<td width="120" align="center" valign="middle" bgcolor="#EEF6FF">运单号</td>
				   	<td width="120" align="center" valign="middle" bgcolor="#EEF6FF">收件人</td>
				   	<td width="120" align="center" valign="middle" bgcolor="#EEF6FF">发货数量</td>
				   	<!-- <td width="120" align="center" valign="middle" bgcolor="#FFEFDF">电话</td>
				   	<td align="center" valign="middle" bgcolor="#FFEFDF">地址</td> -->
			   	</tr>
			   	<form action="<%=request.getContextPath() %>/cwbLablePrint/cwbbarcodeprint_xhm" method="post" id="selectforprintForm" >
			   	<%if(clist!=null&&clist.size()>0){for(CwbOrder c : clist){ %>
				   	<tr style="background-color: rgb(249, 252, 253); ">
						<td align="center" valign="middle" bgcolor="#f3f3f3">
							<input id="isprint" type="checkbox" value="<%=c.getCwb()%>" name="isprint"/>
						</td>
						<td align="center" valign="middle"><%=c.getCwb() %></td>
						<td align="center" valign="middle"><%=c.getTranscwb() %></td>
						<td align="center" valign="middle"><%=c.getConsigneename() %></td>
						<td align="center" valign="middle"><%=c.getSendcarnum() %></td>
					</tr>
			   	<%}} %>
			   	</form>
			</tbody>
		</table>
	</div>
</div>			

<script type="text/javascript">
</script>
</body>
</html>

