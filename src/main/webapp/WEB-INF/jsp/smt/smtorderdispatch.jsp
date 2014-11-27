<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page
	import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page
	import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<User> deliverList = (List<User>)request.getAttribute("deliverList");
Integer todayNotPickingCwbCount = (Integer)request.getAttribute("todayNotPickingCwbCount");
Integer historyNotPickingCwbCount = (Integer)request.getAttribute("historyNotPickingCwbCount");
List<CwbOrder> todayNotPickingCwbList= (List<CwbOrder> )request.getAttribute("todayNotPickingCwbList");
List<CwbOrder> historyNotPickingCwbList= (List<CwbOrder> )request.getAttribute("historyNotPickingCwbList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js"
	type="text/javascript"></script>
<title>中转入库扫描（明细）</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css"
	type="text/css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"
	type="text/javascript"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
var data;
var startIndex=0;
var step=4;
var preStep;
function initEmailDateUI(emaildate){
	 $.ajax({
		 type: "POST",
			url:"<%=request.getContextPath()%>/emaildate/getEmailDateList",
			data:{customerids:$("#customerid").val(),state:"-1"},
			success:function(optionData){
				data=optionData;
				var optionstring="";
				var high ="";
				var preStep;
				for(var j=0;j<data.length;j++){
					if(data[j].emaildateid==emaildate){
						preStep=j;
					}
				}
				moreOpt();
				if(emaildate){
					$("#emaildate").val(emaildate);
				}
			
			}
		});
}
	function moreOpt(){
		step=startIndex+4;
		if(preStep>step){
			step=preStep;
		}
		for(var i=startIndex;i<data.length;i++){
			if(i>step){
				continue;
			}
			optionstring="<option value='"+data[i].emaildateid+"'>"+
			data[i].emaildatetime+(data[i].state==0?"（未到货）":"")+" "+
			data[i].customername+"_"+data[i].warehousename+"_"+data[i].areaname
			+"</option>";
			var opt=$(optionstring);
			$("#emaildate").append(opt);
			startIndex=i+1;
		}
	}
var emaildate=0;
	$(function(){
		$("#more").click(moreOpt);
		emaildate=GetQueryString("emaildate");
		initEmailDateUI(emaildate);
		getcwbsdataForCustomer($("#customerid").val(),'',emaildate);
		getcwbsquejiandataForCustomer($("#customerid").val());
		$("#scancwb").focus();
		$("#updateswitch").click(function(){
			var switchstate = "rkbq_02";
			if($("#updateswitch").attr("checked")=="checked"){
				switchstate = $("#updateswitch").val();
				$("#rk_switch").val(switchstate);
			}else{
				switchstate = "rkbq_02";
				$("#rk_switch").val("rkbq_02");
			}
			$.ajax({
				type: "POST",
				url:"<%=request.getContextPath()%>/switchcontroller/updateswitch?switchstate="+switchstate,
				dataType:"json",
				success : function(data) {
					if(data.errorCode==1){
						alert(data.error);
					}
				}                 
			});
		});
	});
	 $(function(){
			var $menuli = $(".saomiao_tab ul li");
			$menuli.click(function(){
				$(this).children().addClass("light");
				$(this).siblings().children().removeClass("light");
				var index = $menuli.index(this);
				/* $(".tabbox li").eq(index).show().siblings().hide(); */
			});
			
		}) 

	 $(function() {
		var $menuli = $(".saomiao_tab2 ul li");
		$menuli.click(function() {
			$(this).children().addClass("light");
			$(this).siblings().children().removeClass("light");
			var index = $menuli.index(this);
			$(".tabbox li").eq(index).show().siblings().hide();
		});
	});
	
	function focusCwb(){
		$("#scancwb").focus();
	}
	function tabView(tab){
		$("#"+tab).click();
	}
	
	//得到当前入库的供应商的库存量
	function getcwbsdataForCustomer(customerid, cwb,emaildate) {
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/PDA/getInSum",
			data : {
				"customerid" : customerid,
				"cwb" : cwb,
				"emaildate": emaildate
			},
			dataType : "json",
			success : function(data) {
				$("#rukukucundanshu").html(data.weirukucount);
				$("#rukukucunjianshu").html(data.weirukusum);
				$("#successcwbnum").html(data.yirukunum);
			}
		});
	}
	
	
	//得到入库缺货件数的统计
	function getcwbsquejiandataForCustomer(customerid) {
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/PDA/getInQueSum",
			data : {
				"customerid" : customerid,
				"emaildate":emaildate
			},
			dataType : "json",
			success : function(data) {
				$("#lesscwbnum").html(data.lesscwbnum);
			}
		});
	}
	
	//得到入库缺货件数的list列表
	function getrukucwbquejiandataList(customerid){
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/PDA/getInQueList",
			data : {
				"customerid" : customerid,
				"emaildate":emaildate
			},
			dataType : "html",
			success : function(data) {
				$("#lesscwbTable").html(data);
			}
		});
		
	}
function callfunction(cwb){//getEmailDateByIds
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/PDA/getEmaildateid/"+cwb,
		data:{customerids:$("#customerid").val(),state:'0'},
		success:function(data){
			alert(data.cwb+"订单号不在本批次中，请选择"+data.emaildatename+"的批次");
		}
	});
}


function addAndRemoval(cwb,tab,isRemoval){
	var trObj = $("#ViewList tr[cwb='"+cwb+"']");
	if(isRemoval){
		$("#"+tab).append(trObj);
	}else{
		$("#ViewList #errorTable tr[id='TR"+cwb+"error']").remove();
		trObj.clone(true).appendTo("#"+tab);
		$("#ViewList #errorTable tr[id='TR"+cwb+"']").attr("id",trObj.attr("id")+"error");
	}
}

	
function branchDeliver(pname,scancwb,deliverid,requestbatchno){
	if(deliverid==-1){
		$("#msg").html("扫描前请选择小件员");
		return ;
	}else if(scancwb.length>0){
		var allnum = 0;
		
		$.ajax({
			type: "POST",
			url:pname+"/PDA/cwbbranchdeliver/"+scancwb+"?deliverid="+deliverid+"&requestbatchno="+requestbatchno,
			dataType:"json",
			success : function(data) {
				$("#scancwb").val("");
				$("#pagemsg").html("");
				//var linghuoSuccessCount = deliverStr[deliverid].split(",").length-1;
				if(data.statuscode=="000000"){
					$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
					
					$("#scansuccesscwb").val(scancwb);
					$("#showcwb").html("订 单 号："+scancwb);
					$("#consigneeaddress").html("地 址："+data.body.cwbOrder.consigneeaddress);
					if(data.body.cwbOrder.customercommand.indexOf('预约')>=0&&data.yuyuedaService=='yes')
					{	
						$("#customercommand").html("预约派送");
					}
					else{$("#customercommand").html("");}
					if(data.body.cwbOrder.cwbordertypeid==<%=CwbOrderTypeIdEnum.Peisong.getValue()%>){
						$("#cwbordertype").html("订单类型：配送订单");
						$("#fee").html("应收金额："+data.body.cwbOrder.receivablefee);
					}else if(data.body.cwbOrder.cwbordertypeid==<%=CwbOrderTypeIdEnum.Shangmenhuan.getValue()%>){
						$("#cwbordertype").html("订单类型：上门换订单");
						$("#fee").html("应收金额："+data.body.cwbOrder.receivablefee);
					}else if(data.body.cwbOrder.cwbordertypeid==<%=CwbOrderTypeIdEnum.Shangmentui.getValue()%>){
						$("#cwbordertype").html("订单类型：上门退订单");
						$("#fee").html("应退金额："+data.body.cwbOrder.paybackfee);
					}
					if(data.body.showRemark!=null){
					$("#cwbDetailshow").html("订单备注："+data.body.showRemark);
					}
					$("#exceldeliverid").html(data.body.cwbdelivername);
					$("#deliver").html("已领货（"+data.body.cwbdelivername+"）");
				}else{
					$("#exceldeliverid").html("");
					$("#showcwb").html("");
					$("#consigneeaddress").html("");
					$("#cwbordertype").html("");
					$("#cwbDetailshow").html("");
					$("#deliver").html("已领货");
					$("#fee").html("");
					$("#customercommand").html("");
					$("#msg").html(scancwb+"         （异常扫描）"+data.errorinfo);
					addAndRemoval(scancwb,"errorTable",false);
				}
				$("#responsebatchno").val(data.responsebatchno);
				batchPlayWav(data.wavList);
			}
		});
	}
}


 $(function(){
	 $("#today_checkbox").click(function(){
		var checked = $(this).attr("checked");
		checkedTableAllRow("today_table" ,checked);
	 });
	 
	 $("#history_checkbox").click(function(){
		var checked =  $(this).attr("checked");
		checkedTableAllRow("history_table" ,checked);
	});
 });
 
 function checkedTableAllRow(tableId,checked){
	 if(checked){
		 $("#" +tableId ).find("input[type='checkbox']").attr("checked" ,"checked");	 
	 }else{
		 $("#" +tableId ).find("input[type='checkbox']").removeAttr("checked");
	 }	 
 }
 
 function loadHistoryNotPickingOrder(scope)
 {
	 $.ajax({
		 type:"post",
		 url:"<%=request.getContextPath()%>/smt/loadsmthistorynotpickingorder",
		 dataType:"json",
		 async:false,
		 data:{scope:scope,page:1},
		 success:function(data){
	 		var smtOrderList = data.smtOrderList;
	 		refreshTable("history_table" , smtOrderList ,1);
		 },
		 error:function(data)
		 {
			 alert(data);
		 }
	 });
 }
 
 
 function loadTodayNotPickingOrder(scope){
	 $.ajax({
		 type:"post",
		 url:"<%=request.getContextPath()%>/smt/loadsmttodaynotpickingorder",
		 dataType:"json",
		 async:false,
		 data:{scope:scope,page:1},
		 success:function(data){
	 		var smtOrderList = data.smtOrderList;
	 		refreshTable("today_table" , smtOrderList,0);
		 },
		 error:function(data){
			 alert(data);
		 }
	 });
 }
 
 
 function loadTodayPickingOrder(scope){
	 $.ajax({
		 type:"post",
		 url:"<%=request.getContextPath()%>/smt/loadsmttodaypickingorder",
		 dataType:"json",
		 async:false,
		 data:{scope:scope,page:1},
		 success:function(data){
	 		var smtOrderList = data.smtOrderList;
	 		refreshTable("today_picking_table" , smtOrderList,2);
		 },
		 error:function(data){
			 alert(data);
		 }
	 });
 }
 
 function loadTodayOutAreaOrder()
 {
	 $.ajax({
		 type:"post",
		 url:"<%=request.getContextPath()%>/smt/loadsmttodayoutareaorder",
			dataType : "json",
			async : false,
			success : function(data) {
				var smtOrderList = data.smtOrderList;
			},
			error : function(data) {
				alert(data);
			}
		});
	}

 
 	function loadSmtOrder(dataType , timeType , dispatched , page ,tableId , dtabIndex){
 		 $.ajax({
 			 type:"post",
 			 url:"<%=request.getContextPath()%>/smt/querysmtorder",
 				dataType : "json",
 				async : false,
 				data:{
 					dataType:dataType,
 					timeType:timeType,
 					dispatched:dispatched,
 					page:page
 				},
 				success : function(data) {
 					var smtOrderList = data.smtOrderList;
 					refreshTable(tableId,dataList,tabIndex);
 				},
 				error : function(data) {
 					alert(data);
 				}
 			});
 	}

 	function refreshTable(tableId, dataList, tableIndex) {
		var $table = $("#" + tableId);
		$table.empty();
		if (dataList) {
			var dataHtml = createTableRowData(dataList);
			$table.append(dataHtml);
		}
		showTab(tableIndex);
	}

	function createTableRowData(dataList) {
		var allRow = "";
		var length = dataList.length;
		for (var i = 0; i < length; i++) {
			var tr = createTR(dataList[i]);
			allRow += tr;
		}
		return allRow;
	}

	function showTab(index) {
		var $tab = $(".saomiao_tab2 li").eq(index);
		$tab.children().addClass("light");
		$tab.siblings().children().removeClass("light");
		$(".tabbox li").eq(index).show().siblings().hide();
	}

	function createTR(data) {
		var tr = "<tr cwb="+ data.cwb +">";
		tr += createTD("center", "40px", "<input type='checkbox'></input>");
		tr += createTD("center", "100px", data.cwb);
		tr += createTD("center", "100px", data.matchBranch);
		tr += createTD("right", "100px", data.receivedFee);
		tr += createTD("center", "100px", data.customerName);
		tr += createTD("center", "150px", data.phone);
		tr += createTD("center", null, data.address);
		tr += "</tr>";
		return tr;
	}

	function createTD(align, width, content) {
		var td = "<td  bgcolor='#f1f1f1' align='" + align + "'";
		if (width) {
			td += " width='" + width + "'";
		}
		td += ">" + content + "</td>"
		return td;
	}

	function outArea(tableId) {
		var $table = $("#" + tableId);
		var cwbs = [];
		$table.find("input[type='checkbox']").filter(":checked").each(function(){
			var cwb = $(this).closest("tr").attr("cwb");
			cwbs.push(cwb);
		});
		if(cwbs.length ==0)
			{
			
			}
		 $.ajax({
			 type:"post",
			 url:"<%=request.getContextPath()%>/PDA/smtorderoutarea",
			dataType : "json",
			async : false,
			data : {
				cwbs : cwbs
			},
			success : function(data) {
				handleOutAreaSuccess(tableId, cwbs);
			},
			error : function(data) {

			}
		});
	}

	function handleOutAreaSuccess(tableId, cwbs) {
		var $table = $("#" + tableId);
		for (var i = 0; i < cwbs.length; i++) {
			$table.find("tr[cwb='" + cwbs[i] + "']").remove();
		}
		var length = cwbs.length;
		var $tOutArea = $("#t_out_area");
		var oriCnt = $tOutArea.html();
		$tOutArea.html(parseInt(oriCnt) + length);
	}
</script>
<style>
dl dt span {
	width: 50%;
	display: inline-block;
}

dl dd span {
	width: 46%;
	display: inline-block;
}

.blue a {
	color: #336699;
}

.yellow a {
	color: #ff6600;
}

.green a {
	color: #339900
}

.red a {
	color: #666633;
}

.input_button1 {
	margin: 10px 0px 0px 10px;
}

.saomiao_tab {
	height: 17px;
}
</style>
</head>
<body style="background: #eef9ff" marginwidth="0" marginheight="0">
	<div class="saomiao_box2">

		<div class="saomiao_topnum2">
			<dl class="blue">
				<dt>
					<span>今日新单待分派</span><span>今日转单待分派</span>
				</dt>
				<dd style="cursor: pointer">
					<span onclick="loadSmtOrder('normal','today',false, 1 ,'today_table',0)"><a
						id="t_normal_not_picking" href="#"><%=todayNotPickingCwbCount%></a></span>
					<span onclick="loadSmtOrder('transfer','today',false, 1 ,'today_table',0)"><a
						id="t_transfer_not_picking" href="#"><%=todayNotPickingCwbCount%></a></span>
				</dd>
			</dl>


			<dl class="yellow">
				<dt>
					<span>历史新单待分派</span><span>历史转单待分派</span>
				</dt>
				<dd style="cursor: pointer">
					<span onclick="loadSmtOrder('normal','history',false,1,'history_table',1)"><a
						id="h_normal_not_picking" href="#"><%=historyNotPickingCwbCount%></a></span>
					<span onclick="loadSmtOrder('transfer','history',false,1,'history_table',1)"><a
						id="h_transfer_not_picking" href="#"><%=historyNotPickingCwbCount%></a></span>
				</dd>
			</dl>

			<dl class="green">
				<dt>
					<span>今日分派新单</span><span>今日分派转单</span>
				</dt>
				<dd>
					<span onclick="loadSmtOrder('normal','today',true,1,'today_dispatch_table',2)"><a
						id="t_normal_picking" href="#">0</a></span> <span
						onclick="loadSmtOrder('normal','today',true,1,'today_dispatch_table',2)"><a
						id="t_transfer_picking" href="#">0</a></span>
				</dd>
			</dl>


			<dl class="red">
				<dt>今日超区</dt>
				<dd style="cursor: pointer">
					<span onclick="loadTodayOutAreaOrder()"><a id="t_out_area"
						href="#">0</a></span>
				</dd>
			</dl>
			<input type="button" id="refresh" value="刷新"
				onclick="location.href='<%=request.getContextPath()%>/PDA/smtorderdispatch'"
				style="float: left; width: 100px; height: 65px; cursor: pointer; border: none; background: url(../images/buttonbgimg1.gif) no-repeat; font-size: 18px; font-family: '微软雅黑', '黑体'" />
			<br clear="all" />
		</div>

		<div class="saomiao_info2">
			<div class="saomiao_inbox2">
				<div class="saomiao_tab">
					<ul id="bigTag">
						<li><a href="#" id="scancwbTag"
							onclick="clearMsg();$(function(){$('#baleno').parent().hide();$('#finish').parent().hide();$('#baleno').val('');$('#scancwb').val('');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();})"
							class="light">扫描订单</a></li>
					</ul>
				</div>
				<div class="saomiao_selet2">
					小件员：<select id="deliverid" name="deliverid">
						<option value="-1" selected>请选择</option>
						<%
							for (User c : deliverList) {
						%>
						<option value="<%=c.getUserid()%>"><%=c.getRealname()%></option>
						<%
							}
						%>
					</select>
				</div>
				<div class="saomiao_inwrith2">
					<div class="saomiao_left2">
						<p>
							订单号：<input type="text" class="saomiao_inputtxt2" id="scancwb"
								name="scancwb" value=""
								onKeyDown='if(event.keyCode==13&&$(this).val().length>0){branchDeliver("<%=request.getContextPath()%>",$(this).val(),$("#deliverid").val(),$("#requestbatchno").val());}' />
						</p>
					</div>
					<div class="saomiao_right2">
						<p id="msg" name="msg"></p>
						<p id="cwbordertype" name="cwbordertype"></p>
						<p id="showcwb" name="showcwb"></p>
						<p id="cwbgaojia" name="cwbgaojia" style="display: none">高价</p>
						<p id="consigneeaddress" name="consigneeaddress"></p>
						<p id="fee" name="fee"></p>
						<p id="exceldeliverid" name="exceldeliverid"></p>
						<p id="cwbDetailshow" name="cwbDetailshow"></p>
						<p id="customercommand" name="customercommand"></p>
					</div>
					<input type="hidden" id="requestbatchno" name="requestbatchno"
						value="0" /> <input type="hidden" id="scansuccesscwb"
						name="scansuccesscwb" value="" />
				</div>
			</div>
		</div>

		<div>
			<div class="saomiao_tab2">
				<ul>
					<li><a id="today_not_picking_a" href="#" class="light">今日未领货</a></li>
					<li><a id="history_not_picking_a" href="#">历史待分派</a></li>
					<li><a id="today_dispatch_a" href="#">今日已分派</a></li>
					<li><a href="#">异常单明细</a></li>
					<li><a id="today_out_area" href="#">今日超区</a></li>
				</ul>
			</div>
			<div id="ViewList" class="tabbox">
				<li><input type="button" id="btnval0" value="导出Excel"
					class="input_button1"
					onclick='exportField(1,$("#customerid").val());' /> <input
					type="button" id="btnval0" value="超区" class="input_button1"
					onclick="outArea('today_table')" />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="40" align="center" bgcolor="#f1f1f1"><input
												id="today_checkbox" type="checkbox"></input></td>
											<td width="100" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">匹配站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
											<td width="100" align="center" bgcolor="#f1f1f1">退件人姓名</td>
											<td width="150" align="center" bgcolor="#f1f1f1">联系方式</td>
											<td align="center" bgcolor="#f1f1f1">取件地址</td>
										</tr>
									</table>
									<div style="height: 170px; overflow-y: scroll">
										<table id="today_table" width="100%" border="0"
											cellspacing="1" cellpadding="2" class="table_2">
											<%
												for (CwbOrder co : todayNotPickingCwbList) {
											%>
											<tr cwb="<%=co.getCwb()%>" class="cwbids">
												<td width="40" align="center" bgcolor="#f1f1f1"><input
													type="checkbox"></input></td>
												<td width="120" align="center" bgcolor="#f1f1f1"><%=co.getCwb()%></td>
												<td width="100" align="center" bgcolor="#f1f1f1">匹配站点</td>
												<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
												<td width="100" align="center" bgcolor="#f1f1f1"><%=co.getConsigneename()%></td>
												<td width="100" align="center" bgcolor="#f1f1f1"><%=co.getConsigneephone()%></td>
												<td align="center" bgcolor="#f1f1f1"><%=co.getConsigneeaddress()%></td>

											</tr>
											<%
												}
											%>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table></li>
				<li style="display: none"><input type="button" id="btnval0"
					value="导出Excel" class="input_button1"
					onclick='exportField(2,$("#customerid").val());' /> <input
					type="button" id="btnval0" value="超区" class="input_button1"
					onclick="outArea('history_table')" />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="40px" align="center" bgcolor="#f1f1f1"><input
												id="history_checkbox" type="checkbox"></input></td>
											<td width="100px" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100px" align="center" bgcolor="#f1f1f1">匹配站点</td>
											<td width="100px" align="center" bgcolor="#f1f1f1">应收运费</td>
											<td width="100px" align="center" bgcolor="#f1f1f1">退件人姓名</td>
											<td width="150px" align="center" bgcolor="#f1f1f1">联系方式</td>
											<td align="center" bgcolor="#f1f1f1">取件地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="history_table" width="100%" border="0"
											cellspacing="1" cellpadding="2" class="table_2">
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table></li>

				<li style="display: none"><input type="button" id="btnval0"
					value="导出Excel" class="input_button1"
					onclick='exportField(4,$("#customerid").val());' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">匹配站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
											<td width="100" align="center" bgcolor="#f1f1f1">小件员</td>
											<td width="100" align="center" bgcolor="#f1f1f1">客户名称</td>
											<td width="100" align="center" bgcolor="#f1f1f1">退件人姓名</td>
											<td width="100" align="center" bgcolor="#f1f1f1">联系方式</td>
											<td align="center" bgcolor="#f1f1f1">取件地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="today_dispatch_table" width="100%" border="0"
											cellspacing="1" cellpadding="2" class="table_2">
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table></li>

				<li style="display: none"><input type="button" id="btnval0"
					value="导出Excel" class="input_button1"
					onclick='exportField(3,$("#customerid").val());' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">客户名称</td>
											<td width="100" align="center" bgcolor="#f1f1f1">退件人姓名</td>
											<td width="100" align="center" bgcolor="#f1f1f1">联系方式</td>
											<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
											<td align="center" bgcolor="#f1f1f1">取件地址</td>
											<td width="100" align="center" bgcolor="#f1f1f1">匹配站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">小件员</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="errorTable" width="100%" border="0" cellspacing="1"
											cellpadding="2" class="table_2">
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table></li>
				<li style="display: none"><input type="button" id="btnval0"
					value="导出Excel" class="input_button1"
					onclick='exportField(3,$("#customerid").val());' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">匹配站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
											<td width="100" align="center" bgcolor="#f1f1f1">退件人姓名</td>
											<td width="100" align="center" bgcolor="#f1f1f1">联系方式</td>
											<td align="center" bgcolor="#f1f1f1">取件地址</td>

										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="out_area_table" width="100%" border="0"
											cellspacing="1" cellpadding="2" class="table_2">
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table></li>
			</div>
		</div>

		<form action="<%=request.getContextPath()%>/PDA/exportExcle"
			method="post" id="searchForm2">
			<input type="hidden" name="cwbs" id="cwbs" value="" /> <input
				type="hidden" name="exportmould2" id="exportmould2" />
		</form>
		<form action="<%=request.getContextPath()%>/PDA/exportByCustomerid"
			method="post" id="searchForm3">
			<input type="hidden" name="customerid" value="0" id="expcustomerid" />
			<input type="hidden" name="emaildate" value="0" id="expemailid" /> <input
				type="hidden" name="type" value="" id="type" />
		</form>
	</div>
</body>
</html>
