<%@page import="java.util.ArrayList"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%
    List<CwbOrder> List = request.getAttribute("cwbList")==null?new ArrayList<CwbOrder>():(List<CwbOrder>)request.getAttribute("cwbList");
    
    
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
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
		
	});
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

</script>
</head>
<body  style="background:#eef9ff">
<div class="saomiao_box2"> 
	<div>
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="#" class="light">订单信息修改</a></li>
				<li><a href="./toSearchCwb/1">订单信息修改查询</a></li>
			</ul>
		</div>
		<div class="tabbox">
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr >
							<td width="10%" height="26" align="left" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="5" class="table_5" >
									<tr>
										<td width="120" align="left" valign="top" bgcolor="#eef9ff">
										<form action="<%=request.getContextPath()%>/editcwb/editCwbInfo" method="post">
										订单号：
										<input name="cwb" type="text" id="cwb" onfocus="if(this.value=='输入单个订单号查询后进行修改……'){this.value=''}" onblur="if(this.value==''){this.value='输入单个订单号查询后进行修改……'}" />
										<input name="button" type="submit" class="input_button1" id="button" value="查询" />
										<input type="hidden" id="isshow" name="isshow" value="1" />
										</form>
										</td>
									</tr>
								</table>
								<%if(List!=null&&List.size()>0){
									%>
								<form action="<%=request.getContextPath()%>/editcwb/updateCwbInfo/" method="post" id="searchForm2">
								<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
										<tr class="font_1">
											<td align="center" bgcolor="#e7f4e3">订单号</td>
											<td align="center" bgcolor="#e7f4e3">收件人（修改）</td>
											<td bgcolor="#e7f4e3">手机（修改）</td>
											<td bgcolor="#e7f4e3">地址（修改）</td>
											<td bgcolor="#e7f4e3">配送站点（修改）</td>
											<td bgcolor="#e7f4e3">配送时间（修改）</td>
											<td bgcolor="#e7f4e3">电商要求</td>
											<td bgcolor="#e7f4e3">备注</td>
											<td bgcolor="#e7f4e3">操作</td>
									</tr>
									<%for(CwbOrder c:List){ %>
										<tr>
											<td width="25%"  align="center" valign="middle" height="19" ><%=c.getCwb() %>
											<input type="hidden" name="cwb" id="cwb" value="<%=c.getCwb() %>">
											</td>
											<td width="5%"  valign="middle" align="center"  ><input type="text"  value="<%=c.getConsigneenameOfkf() %>" id="editname" name="editname"/></td>
											<td width="8%"  valign="middle"  align="center"  ><input type="text"  value="<%=c.getConsigneemobileOfkf()%>" id="editmobile" name="editmobile"/></td>
											<td width="15%" valign="middle"  align="center"  ><textarea  cols="30"  name="editaddress" id="editaddress" ><%=c.getConsigneeaddress() %></textarea></td>
											<td width="15%" valign="middle"  align="center"  ><input type="text"  name="matchaddress" id="matchaddress" /></td>
											<td width="10%" valign="middle"  align="center"  ><input type ="text" name ="begindate" id="strtime"  value="<%=c.getResendtime()%>"/></td>
											<td width="10%" valign="middle"  align="left"  ><input type="text"  value="<%=c.getCustomercommand() %>" id="editcommand" name="editcommand"/></td>
											<td width="15%" valign="middle"  align="left"  ><textarea rows="3" cols="30"   id="remark" name="remark" ><%=c.getCwbremark() %></textarea></td>
											<td>
											<input name="button2" type="button" class="input_button2" id="button2" value="修改匹配站" onclick="mathaddress('<%=c.getCwb() %>');" />
											<input name="button2" type="button" class="input_button2" id="button2" value="修改" onclick="selectForm('<%=c.getCwb() %>');" />
											<input type="hidden" value="1" name="editshow" id="editshow">
											</td>
											
										</tr><%} %>
								</table></form><%} %>
								</td>
						</tr>
					</tbody>
				</table>
					<!--底部翻页 -->
	
		</div>
	</div>
</div>
<script type="text/javascript">
	function selectForm(a){
					$.ajax({
						url:"<%=request.getContextPath()%>/editcwb/updateCwbInfo/"+a,
						type:"POST",//数据发送方式 
						data:$('#searchForm2').serialize(),//参数
						dataType:'json',//接受数据格式
						success:function(data){
							if(data.errorCode == 1){
								 alert(data.error);
							}else{
								alert(data.error);
							}
						}
						   
					});
}
	function mathaddress(cwb){
		var branch=$("#matchaddress").val();
		if(branch.length>0){
					$.ajax({
						url:"<%=request.getContextPath()%>/editcwb/matchaddress",
						type:"POST",//数据发送方式 
						data:{"branchname":branch,"cwb":cwb},//参数
						dataType:'json',//接受数据格式
						success:function(data){
							if(data.errorCode == 1){
								 alert(data.error);
							}else{
								alert(data.error);
							}
						}
						   
					});
		}
		else {
			alert("请输入要匹配的站点！");
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
</body>
</html>