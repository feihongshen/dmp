<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*,cn.explink.domain.Customer" pageEncoding="UTF-8"%>

<%
String responsemsg=request.getAttribute("responsemsg")==null?"":(String)request.getAttribute("responsemsg");
String rdotype=request.getAttribute("rdotype")==null?"":(String)request.getAttribute("rdotype");
List<Customer> customerlist=(List<Customer>)request.getAttribute("customerlist");

String customerid=request.getAttribute("customerid")==null?"":(String)request.getAttribute("customerid");

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script language="javascript">



	 $(document).ready(function() {
		 $("#btn").click(function (){
		
			 if($("#customerid").val()=='0'){
				 alert('请选择供货商!');
				 return;
			 }
			 if($("#handOverNo").val()==''){
				 alert('请输入交接单号!');
				 return;
			 }
			 $("#handOverNo").val()=='';
			 $("#div_loading").show();
			 $("#btn").attr("disabled","disabled");
		
			 $('#searchForm').attr('action','<%=request.getContextPath()%>/dpfoss/download');
			 $('#searchForm').submit();
			 
		 });
		});




</script>
</head>
<body style="background:#f5f5f5;overflow: hidden;" marginwidth="0" marginheight="0">
<form action="" method="post" id="searchForm">
<div class="right_box">
	<div style="background:#FFF">
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="#" class="light">德邦物流订单下载</a></li>
			</ul>
		</div>
		<div class="tabbox">
				<div style="position:relative; z-index:0; " >
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							
								
						
						</div>
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="150" align="left" valign="middle" bgcolor="#f3f3f3"><font  style="font-weight:normal">请输入当日的交接单号/运单号:</font></td>
								</tr>
								<tr class="font_1" height="30" >
									<td width="150" align="left" valign="middle" bgcolor="#f3f3f3">
									请选择供货商:
									<select name="customerid" id="customerid">
										<option value="0">请选择</option>
										<%for(Customer cust:customerlist){%>
											<option value="<%=cust.getCustomerid()%>" <%if(customerid.equals(String.valueOf(cust.getCustomerid()))){%>selected<%}%>><%=cust.getCustomername() %></option>
										<%}%>
									</select>
									</td>
								</tr>
								<tr class="font_1" height="30" >
									<td width="150" align="left" valign="middle" bgcolor="#f3f3f3">
									<input type="radio" id="rdo" name="rdotype" value="1" <%if(rdotype.equals("1")||rdotype.equals("")){%> checked<%} %>  >交接单号
									<input type="radio" id="rdo" name="rdotype" value="2"   <%if(rdotype.equals("2")){%> checked<%} %> >运单号
									<input type="text" class="saomiao_inputtxt" id="handOverNo" name="handOverNo" value="" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){submitIntoWarehouse("<%=request.getContextPath()%>",$(this).val(),$("#customerid").val(),$("#driverid").val(),$("#requestbatchno").val(),$("#rk_switch").val(),"");}'/>
									<input type="button" id="btn" name="btn" value="提交交接单" class="button" >
									<div id="div_loading" style="display:none" align="center"><IMG  src="<%=request.getContextPath()%>/images/dpfossloading.gif"> <font color="gray">正在下载订单,请稍后..</font></div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div style="height:100px"></div>
					
					<table width="100%" border="0" cellspacing="1" cellpadding="10" class="table_2">
						<tr>
							<td width="150" align="left" valign="middle" bgcolor="#f3f3f3"></td>
						</tr>
						<tr>
							<td width="150" align="left" valign="middle" bgcolor="#f3f3f3"><font color="red"><%=responsemsg %></font></td>
						</tr>
					</table>

				</div>
		</div>
	</div>
</div>
	</form>
</body>
</html>

