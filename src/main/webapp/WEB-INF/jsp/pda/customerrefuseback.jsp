<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Truck,cn.explink.domain.Bale,cn.explink.domain.Switch"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
List<Customer> cList = (List<Customer>)request.getAttribute("customerlist");
List<CwbOrder> weichukuList = (List<CwbOrder>)request.getAttribute("weitghsckList");
List<CwbOrder> yichukuList = (List<CwbOrder>)request.getAttribute("yichukulist");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>供货商拒收返库</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
	$(function(){
		getnumber();
		var $menuli2 = $("#smallTag li");
		$menuli2.click(function(){
			$(this).children().addClass("light");
			$(this).siblings().children().removeClass("light");
			var index = $menuli2.index(this);
			$(".tabbox li").eq(index).show().siblings().hide();
		});
	});
	
	function tabView(tab){
		$("#"+tab).click();
	}
	
	function addAndRemoval(cwb,tab,isRemoval){
		var trObj = $("#ViewList tr[id='TR"+cwb+"']");
		if(isRemoval){
			$("#"+tab).append(trObj);
		}else{
			$("#ViewList #errorTable tr[id='TR"+cwb+"error']").remove();
			trObj.clone(true).appendTo("#"+tab);
			$("#ViewList #errorTable tr[id='TR"+cwb+"']").attr("id",trObj.attr("id")+"error");
		}
	}
	
	
	
	function cwbcustomerrefuseback(scancwb,remarkcontent){
		if(scancwb.length>0){
			$.ajax({
				type: "POST",
				url:"<%=request.getContextPath()%>/PDA/cwbcustomerrefuseback/"+scancwb,
				data : {remarkcontent:remarkcontent},
				dataType:"json",
				success : function(data) {
					$("#scancwb").val("");
					if(data.statuscode=="000000"){
						$("#msg").html(scancwb+"         （成功扫描）");
						$("#customer").html("供货商："+data.body.customername);
						//getnumber();
						//addAndRemoval(scancwb,"successTable",true);
					}else{
						$("#msg").html(scancwb+"         （异常扫描）"+data.errorinfo);
						addAndRemoval(scancwb,"errorTable",false);
					}
					//errorvedioplay("<%=request.getContextPath()%>",data);
					batchPlayWav(data.wavList);
				}
			});
		}else{
			$("#msg").html("请扫描订单号");
		}	
	}
	function getnumber(){
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/PDA/getRefusedSum",
			dataType:"json",
			success : function(data) {
				$("#chukukucundanshu").html(data.count);
				$("#singleoutnum").html(data.success);
				$("#remarkcontent").val("");
			}
		});
	}
	
	
	//导出
	
	function exportField(flag){
		var cwbs = "";
		if(flag==1){
			
				$("#weichukuTable tr").each(function(){
					var cwb = $(this).attr("cwb");
					cwbs += "'" + cwb + "',";
				});
			
		}else if(flag==2){
				$("#successTable tr").each(function(){
					var cwb = $(this).attr("cwb");
					cwbs += "'" + cwb + "',";
				});
		}else if(flag==3){
				$("#errorTable tr").each(function(){
					var cwb = $(this).attr("cwb");
					cwbs += "'" + cwb + "',";
				});
			
		}
		if(cwbs.length>0){
			cwbs = cwbs.substring(0, cwbs.length-1);
		}
		if(cwbs!=""){
			$("#exportmould2").val($("#exportmould").val());
			$("#cwbs").val(cwbs);
			$("#btnval").attr("disabled","disabled");
		 	$("#btnval").val("请稍后……");
		 	$("#searchForm2").submit();
		}else{
			alert("没有订单信息，不能导出！");
		};
	}
	
	
	 function  falshdata(){
		 $("#refresh").attr("disabled","disabled");
		 $("#flash").attr("disabled","disabled");
		 $("#refresh").val("请稍后……");
		 $("#flash").val("请稍后……");
		 getnumber();
		 getdaifanku();
		 getyifanku();
		 $("#refresh").val("刷新");
		 $("#flash").val("刷新");
		 $("#refresh").removeAttr("disabled");
		 $("#flash").removeAttr("disabled");
	 }
	 function getyifanku(){
			
			$.ajax({
				type:"post",
				url:"<%=request.getContextPath()%>/PDA/getcustomerrefusedbackyifankulist",
				success:function(data){
					var optionstring = "";
					for ( var i = 0; i < data.length; i++) {
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"'customerid='"+data[i].customerid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					}
					$("#successTable").html(optionstring);
				}
			});
		};
		
	function getdaifanku(){
			
			$.ajax({
				type:"post",
				url:"<%=request.getContextPath()%>/PDA/getcustomerrefusedbackdaifankulist",
				success:function(data){
					var optionstring = "";
					for ( var i = 0; i < data.length; i++) {
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"'customerid='"+data[i].customerid+"'  nextbranchid='"+data[i].nextbranchid +"'>"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					}
					$("#weichukuTable").html(optionstring);
				}
			});
		};
</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="saomiao_box2">
 
	<div class="saomiao_topnum2">
		<dl class="blue">
			<dt>待返库</dt>
			<dd id="chukukucundanshu"  onclick="tabView('table_weichuku')" >0</dd>
		</dl>
		<dl class="green">
			<dt>本次已返库</dt>
			<dd id="singleoutnum"  onclick="tabView('table_yichuku')">0</dd>
		</dl>
		<input type="button"  id="refresh" value="刷新" onclick="location.href='<%=request.getContextPath() %>/PDA/customerrefuseback'" style="float:left; width:100px; height:65px; cursor:pointer; border:none; background:url(../images/buttonbgimg1.gif) no-repeat; font-size:18px; font-family:'微软雅黑', '黑体'"/>
		
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="saomiao_inwrith2">
				<div class="saomiao_left2">
					<p><span>备注：</span><input type="text" class="inputtext_2" id="remarkcontent" name="remarkcontent" value="" maxlength="50" /></p>
					<p><span>扫描：</span><input type="text" class="inputtext_2" id="scancwb" name="scancwb" value="" maxlength="50" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){cwbcustomerrefuseback($(this).val(),$("#remarkcontent").val());}'/></p>
				</div>
				<div class="saomiao_right2">
				<h4 id="msg" name="msg" ></h4>
				<h4 id="customer" name="customer" ></h4>
					  <div style="display: none" id="errorvedio"></div>
				</div>
			</div>
		</div>
	</div>

		<div>
			<div class="saomiao_tab2">
				<span style="float: right; padding: 10px">
				<input  class="input_button2" type="button" name="littlefalshbutton" id="flash" value="刷新" onclick="location.href='<%=request.getContextPath() %>/PDA/customerrefuseback'" />
				</span>
				<ul id="smallTag">
					<li><a id="table_weichuku" href="#" class="light">待返库明细</a></li>
					<li><a id="table_yichuku" href="#">已返库明细</a></li>
					<li><a href="#">异常单明细</a></li>
				</ul>
			</div>
			<div id="ViewList" class="tabbox">
				<li>
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(1);'/>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="weichukuTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%for(CwbOrder co : weichukuList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" nextbranchid="<%=co.getNextbranchid() %>" >
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
				<li style="display: none">
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(2);'/>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="successTable" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
											<%for(CwbOrder co : yichukuList){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>">
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%} %>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
				<li style="display: none">
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(3);'/>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="errorTable" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
			</div>
		</div>
</div>
		<form action="<%=request.getContextPath()%>/PDA/exportExcle" method="post" id="searchForm2">
			<input type="hidden" name="cwbs" id="cwbs" value=""/>
			<input type="hidden" name="exportmould2" id="exportmould2" />
		</form>

		
</body>
</html>
