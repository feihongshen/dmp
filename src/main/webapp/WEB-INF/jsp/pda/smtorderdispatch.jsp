<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch"%>
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
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js" type="text/javascript"></script>
<title>中转入库扫描（明细）</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
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
	
	function addAndRemoval(cwb,tab,isRemoval,customerid){
		var trObj = $("#ViewList tr[id='TR"+cwb+"']");
		if(isRemoval){
			$("#"+tab).append(trObj);
		}else{
			$("#ViewList #errorTable tr[id='TR"+cwb+"error']").remove();
			trObj.clone(true).appendTo("#"+tab);
			$("#ViewList #errorTable tr[id='TR"+cwb+"']").attr("id",trObj.attr("id")+"error");
		}
		//已入库明细只显示该供货商明细、异常单明细只显示该供货商明细
		if(customerid>0){
			$("#successTable tr").hide();
			$("#successTable tr[customerid='"+customerid+"']").show();
			
			$("#errorTable tr").hide();
			$("#errorTable tr[customerid='"+customerid+"']").show();
		}else{
			$("#successTable tr").show();
			$("#errorTable tr").show();
		}
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
	
	/**
	 * 入库扫描
	 */
	function submitIntoWarehouse(pname, scancwb, customerid, driverid,
			requestbatchno, rk_switch, comment) {
		
		
		var flag=false;
		
		if (scancwb.length > 0) {
			$("#close_box").hide();

			if($("#scanbaleTag").attr("class")=="light"){//入库根据包号扫描订单
				baledaohuo(scancwb,driverid,comment);
			}else{//入库
				//是否按批次过滤？
				if($("#emaildate").val()>0){
					var recheck=false;
					$(".yirukucwbids").each(function(i,val){
						if(scancwb==val.id.substr(2)){
							alert(scancwb+"订单重复入库");
							recheck=true;
							return;
						}
					} )
					if(recheck){
						return;
					}
					var flag=false;
					$(".cwbids").each(function(i,val){
						if(scancwb==val.id.substr(2)){
							flag=true;
						}
					} )
					if(!flag){
						callfunction(scancwb);
						return;
					}
				}
				$.ajax({
							type : "POST",
							url : pname + "/PDA/cwbintowarhouse/" + scancwb
									+ "?customerid=" + customerid
									+ "&driverid=" + driverid
									+ "&requestbatchno=" + requestbatchno,
							data : {
								"comment" : comment
							},
							dataType : "json",
							success : function(data) {
								$("#scancwb").val("");
								
								if (data.statuscode == "000000") {
									
									$("#cwbgaojia").hide();

									$("#excelbranch").show();
									$("#customername").show();
									$("#damage").show();
									$("#multicwbnum").show();

									$("#customerid").val(data.body.cwbOrder.customerid);

									if(data.body.showRemark!=null){
										$("#cwbDetailshow").html("订单备注："+data.body.showRemark);
										}
									if (data.body.cwbOrder.deliverybranchid != 0) {
										$("#excelbranch").html("目的站："+ data.body.cwbdeliverybranchname
																+ "<br/>下一站："+ data.body.cwbbranchname);
									} else {
										$("#excelbranch").html("尚未匹配站点");
									}
									$("#customername").html(
											data.body.cwbcustomername);
									$("#multicwbnum").val(
											data.body.cwbOrder.sendcarnum);
									$("#msg").html(scancwb+ data.errorinfo+ "（共"+ data.body.cwbOrder.sendcarnum
													+ "件，已扫"+ data.body.cwbOrder.scannum+ "件）");
									
									//将成功扫描的订单放到已入库明细中
									//addAndRemoval(data.body.cwbOrder.cwb,"successTable",true,$("#customerid").val());
									
									
									
									/* if (data.body.cwbOrder.scannum == 1) {
										$("#successcwbnum").html(parseInt($("#successcwbnum").html()) + 1);
									} */

									if (rk_switch == "rkbq_01") {
										$("#printcwb",parent.document).attr("src",pname + "/printcwb?scancwb="+ scancwb + "&a="+ new Date());
									}
									else if (rk_switch == "rkbq_03") {
										$("#printcwb",parent.document).attr("src",pname + "/printcwb/printCwbnew?scancwb="+ scancwb + "&a="+ new Date());
									}

									<%--if (data.body.cwbbranchnamewav != ""&&data.body.cwbbranchnamewav != pname+ "/wav/") {
										$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ data.body.cwbbranchnamewav
															+ "&a="+ Math.random());
									}else{
										$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
									}

									if (data.body.cwbgaojia != undefined && data.body.cwbgaojia != '') {
										$("#cwbgaojia").parent().show();
										try {
											document.gaojia.Play();
										} catch (e) {
										}
									}
									if (<%=RUKUPCandPDAaboutYJDPWAV.equals("yes")%>&&data.body.cwbOrder.sendcarnum > 1) {
										try {
											document.ypdj.Play();
										} catch (e) {
										}
									} --%>

									$("#scansuccesscwb").val(scancwb);
									$("#showcwb").html("订 单 号：" + scancwb);
									$("#consigneeaddress").html("地 址："+ data.body.cwbOrder.consigneeaddress);
									if(data.body.showRemark!=null){
									$("#cwbDetailshow").html("订单备注："+data.body.showRemark);
									}
									if(data.body.cwbOrder.emaildateid==0){
										$("#morecwbnum").html(parseInt($("#morecwbnum").html()) + 1);
									}
									//getcwbsdataForCustomer($("#customerid").val(),scancwb);
									//getcwbsquejiandataForCustomer($("#customerid").val());
									//getrukucwbquejiandataList($("#customerid").val());

								} /* else if (data.statuscode == "13") {
									$("#morecwbnum").html(parseInt($("#morecwbnum").html()) + 1);
									errorvedioplay(pname, data);
									//将有货无单扫描的订单放到有货无单明细中
									//addAndRemoval(cwb,"youhuowudanTable");
								} */ else {
									$("#excelbranch").hide();
									$("#customername").hide();
									$("#cwbgaojia").hide();
									$("#damage").hide();
									$("#multicwbnum").hide();
									$("#multicwbnum").val("1");
									$("#showcwb").html("");
									$("#cwbDetailshow").html("");
									$("#consigneeaddress").html("");
									$("#msg").html("（异常扫描）" + data.errorinfo);
									addAndRemoval(scancwb,"errorTable",false,$("#customerid").val());
									//errorvedioplay(pname, data);
								}
								$("#responsebatchno").val(data.responsebatchno);
								batchPlayWav(data.wavList);
							}
						});
			}
		}
	}
	/**
	 * 入库扫描（包）
	 */
	/* function submitIntoWarehouseforbale(pname, driverid, baleno) {
		if (scancwb.length == 0 && baleno.length == 0) {
			$("#pagemsg").html("请先扫描");
			return;
		}
		if (baleno.length > 0) {
			$.ajax({
				type : "POST",
				url : pname + "/PDA/cwbintowarhouseByPackageCode/" + baleno
						+ "?driverid=" + driverid,
				dataType : "json",
				success : function(data) {
					$("#bale").val("");
					$("#msg")
							.html(
									data.body.packageCode + "　（"
											+ data.errorinfo + "）");
				}
			});
		}
	} */
	/**
	 * 入库备注提交
	 */
	function intoWarehouseforremark(pname, scancwb, csremarkid, multicwbnum) {
		if (csremarkid == 4 && multicwbnum == "1") {
			$("#msg").html("抱歉，一票多件至少件数为2");
			return;
		} else {
			$.ajax({
				type : "POST",
				url : pname + "/PDA/forremark/" + scancwb + "?csremarkid="
						+ csremarkid + "&multicwbnum=" + multicwbnum
						+ "&requestbatchno=0",
				dataType : "json",
				success : function(data) {
					if (data.statuscode == "000000") {
						$("#msg").html("订单备注操作成功");
					} else {
						$("#msg").html(data.errorinfo);
						errorvedioplay(pname, data);
					}
				}
			});
		}
	}
	
	
function exportField(flag,customerid){
	var cwbs = "";
	$("#expemailid").val($("#emaildate").val());
	if(flag==1){
		$("#expcustomerid").val($("#customerid").val());
		$("#type").val("weiruku");
		$("#searchForm3").submit();
	}else if(flag==2){
		$("#expcustomerid").val($("#customerid").val());
		$("#type").val("yiruku");
		$("#searchForm3").submit();
	}else if(flag==3){//修改导出问题
		/* if(customerid>0){
			$("#errorTable tr[customerid='"+customerid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}else{ */
			$("#errorTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		/* } */
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
	}else if(flag==4){
		$("#expcustomerid").val($("#customerid").val());
		$("#type").val("ypdj");
		$("#searchForm3").submit();
	}
	
}

var weipage=1;
var yipage=1;
function weiruku(){
	
}
function yiruku(){
	
	
}
function ranCreate(){
	var timestamp = new Date().getTime();
	$("#baleno").val(timestamp);
	$("#baleno").attr('readonly','readonly');
	$('#scancwb').parent().show();
	$('#scancwb').show();
	$('#scancwb').focus();
}
function hanCreate(){
	$('#baleno').removeAttr('readonly');
	$('#baleno').val('');
	$('#baleno').focus();
}
function clearMsg(){
	$("#msg").html("");
	$("#showcwb").html("");
	$("#excelbranch").html("");
	$("#customername").html("");
	$("#consigneeaddress").html("");
	$("#cwbDetailshow").html("");
	$("#cwbgaojia").hide();
}

function tohome(){
	var isscanbaleTag = 1;
	if($("#scanbaleTag").hasClass("light")){
		isscanbaleTag=1;
	}else{
		isscanbaleTag=0;
	}
	var temp=$("#customerid").val();
	if("-1"==temp||temp<0){
		$("#emaildate").val("0")
	}
	window.location.href="<%=request.getContextPath()%>/PDA/intowarhouse?customerid="+$("#customerid").val()+"&isscanbaleTag="+isscanbaleTag+"&emaildate="+$("#emaildate").val();
}
$(function(){
	var $menuli = $(".saomiao_tab ul li");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		/* $(".tabbox li").eq(index).show().siblings().hide(); */
	});
	
}) 


//=========合包到货=============
 function baledaohuo(scancwb,driverid,comment){
 	if($("#baleno").val()==""){
 		alert("包号不能为空！");
 		$("#scancwb").val("");
 		return;
 	}
 	$.ajax({
 		type: "POST",
 		url:"<%=request.getContextPath()%>/bale/balezhongzhuandaohuo/"+$("#baleno").val()+"/"+scancwb+"?driverid="+driverid,
 		data:{
			"comment":comment
		},
 		dataType : "json",
 		success : function(data) {
 			clearMsg();
 			$("#msg").html(data.body.errorinfo);
 			$("#scancwb").val("");
 			errorvedioplay("<%=request.getContextPath()%>",data);
 		}
 	});
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
	 
	 $("#today_not_picking_a").click(function(){
		 reloadTodayTable();
	 });
	 
	 $("#history_not_picking_a").click(function(){
		 reloadHistoryTable();
	 });
	 
 });
 
 function reloadTodayTable(dataScope)
 {
	 $("#today_table").empty();
 }
 
 function reloadHistoryTable()
 {
	 $("#history_table").empty();
 }
 
 
 
 function checkedTableAllRow(tableId,checked)
 {
	 if(checked){
		 $("#" +tableId ).find("input[type='checkbox']").attr("checked" ,"checked");	 
	 }else{
		 $("#" +tableId ).find("input[type='checkbox']").removeAttr("checked");
	 }	 
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
					<span><a href="#" onclick="tabView('table_weiruku')" id="rukukucundanshu">0</a></span> <span><a
						href="#">0</a></span>
				</dd>
			</dl>


			<dl class="yellow">
				<dt>
					<span>历史新单待分派</span><span>历史转单待分派</span>
				</dt>
				<dd>
					<span><a href="#" onclick="tabView('table_weiruku')" id="rukukucundanshu"><%=historyNotPickingCwbCount%></a></span>
					<span><a href="#">0</a></span>
				</dd>
			</dl>

			<dl class="green">
				<dt>
					<span>今日分派新单</span><span>今日分派转单</span>
				</dt>
				<dd>
					<span><a href="#" onclick="tabView('table_weiruku')" id="rukukucundanshu">0</a></span> <span><a
						href="#">0</a></span>
				</dd>
			</dl>


			<dl class="red">
				<dt>今日超区</dt>
				<dd>
					<a href="#" onclick="tabView('table_weiruku')" id="rukukucundanshu">0</a>
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
							订单号：<input type="text" class="saomiao_inputtxt" id="scancwb" name="scancwb" value=""
								onKeyDown='if(event.keyCode==13&&$(this).val().length>0){submitIntoWarehouse("<%=request.getContextPath()%>",$(this).val(),$("#customerid").val(),$("#driverid").val(),$("#requestbatchno").val(),$("#rk_switch").val(),"");}' />
						</p>
					</div>
					<div class="saomiao_right2">
						<p id="msg" name="msg"></p>
						<p id="showcwb" name="showcwb"></p>
						<p id="cwbgaojia" name="cwbgaojia" style="display: none">高价</p>
						<p id="consigneeaddress" name="consigneeaddress"></p>
						<p id="excelbranch" name="excelbranch"></p>
						<p id="customername" name="customername"></p>
						<p id="cwbDetailshow" name="cwbDetailshow"></p>
						<div style="display: none" id="errorvedio"></div>
					</div>
					<p style="display: none;">
						<input type="button" class="input_btn1" id="moregoods" name="moregoods" value="一票多物"
							onclick='intoWarehouseforremark("<%=request.getContextPath()%>",$("#scansuccesscwb").val(),4,$("#multicwbnum").val());' />
						<span>一票多物件数：</span><input type="text" id="multicwbnum" name="multicwbnum" value="1"
							class="input_txt1" />
					</p>
					<input type="hidden" id="requestbatchno" name="requestbatchno" value="0" /> <input
						type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" />
				</div>
			</div>
		</div>

		<div>
			<div class="saomiao_tab2">
				<ul>
					<li><a id="today_not_picking_a" href="#" class="light">今日未领货</a></li>
					<li><a id="history_not_picking_a" href="#">历史待分派</a></li>
					<li><a id="table_quejian" href="#">今日已分派</a></li>
					<li><a href="#">异常单明细</a></li>
					<li><a href="#">今日超区</a></li>
				</ul>
			</div>
			<div id="ViewList" class="tabbox">
				<li><input type="button" id="btnval0" value="导出Excel" class="input_button1"
					onclick='exportField(1,$("#customerid").val());' /> <input type="button" id="btnval0"
					value="超区" class="input_button1" onclick='exportField(1,$("#customerid").val());' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
										<tr>
											<td width="40" align="center" bgcolor="#f1f1f1"><input id="today_checkbox"
												type="checkbox"></input></td>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">系统接收时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">本站接收时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">退件人姓名</td>
											<td width="100" align="center" bgcolor="#f1f1f1">联系方式</td>
											<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
											<td align="center" bgcolor="#f1f1f1">取件地址</td>
											<td width="100" align="center" bgcolor="#f1f1f1">匹配站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">小件员</td>
										</tr>
									</table>
									<div style="height: 170px; overflow-y: scroll">
										<table id="today_table" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%
												for (CwbOrder co : todayNotPickingCwbList) {
											%>
											<tr id="TR<%=co.getCwb()%>" cwb="<%=co.getCwb()%>" customerid="<%=co.getCustomerid()%>"
												class="cwbids">
												<td width="40" align="center" bgcolor="#f1f1f1"><input type="checkbox"></input></td>
												<td width="120" align="center" bgcolor="#f1f1f1"><%=co.getCwb()%></td>
												<td width="100" align="center" bgcolor="#f1f1f1">系统接收时间</td>
												<td width="100" align="center" bgcolor="#f1f1f1">本站接收时间</td>
												<td width="100" align="center" bgcolor="#f1f1f1"><%=co.getConsigneename()%></td>
												<td width="100" align="center" bgcolor="#f1f1f1"><%=co.getConsigneephone()%></td>
												<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
												<td align="center" bgcolor="#f1f1f1"><%=co.getConsigneeaddress()%></td>
												<td width="100" align="center" bgcolor="#f1f1f1">匹配站点</td>
												<td width="100" align="center" bgcolor="#f1f1f1">小件员</td>
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
				<li style="display: none"><input type="button" id="btnval0" value="导出Excel"
					class="input_button1" onclick='exportField(2,$("#customerid").val());' /> <input type="button"
					id="btnval0" value="超区" class="input_button1" onclick='exportField(1,$("#customerid").val());' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
										<tr>
											<td width="40" align="center" bgcolor="#f1f1f1"><input id="history_checkbox"
												type="checkbox"></input></td>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">系统接收时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">本站接收时间</td>
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
										<table id="history_table" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%
												for (CwbOrder co : todayNotPickingCwbList) {
											%>
											<tr id="TR<%=co.getCwb()%>" cwb="<%=co.getCwb()%>" customerid="<%=co.getCustomerid()%>"
												class="cwbids">
												<td width="40" align="center" bgcolor="#f1f1f1"><input type="checkbox"></input></td>
												<td width="120" align="center" bgcolor="#f1f1f1"><%=co.getCwb()%></td>
												<td width="100" align="center" bgcolor="#f1f1f1">系统接收时间</td>
												<td width="100" align="center" bgcolor="#f1f1f1">本站接收时间</td>
												<td width="100" align="center" bgcolor="#f1f1f1"><%=co.getConsigneename()%></td>
												<td width="100" align="center" bgcolor="#f1f1f1"><%=co.getConsigneephone()%></td>
												<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
												<td align="center" bgcolor="#f1f1f1"><%=co.getConsigneeaddress()%></td>
												<td width="100" align="center" bgcolor="#f1f1f1">匹配站点</td>
												<td width="100" align="center" bgcolor="#f1f1f1">小件员</td>
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

				<li style="display: none"><input type="button" id="btnval0" value="导出Excel"
					class="input_button1" onclick='exportField(4,$("#customerid").val());' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">系统接收时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">本站接收时间</td>
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
										<table id="lesscwbTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table></li>

				<li style="display: none"><input type="button" id="btnval0" value="导出Excel"
					class="input_button1" onclick='exportField(3,$("#customerid").val());' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">系统接收时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">本站接收时间</td>
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
										<table id="errorTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table></li>
				<li style="display: none"><input type="button" id="btnval0" value="导出Excel"
					class="input_button1" onclick='exportField(3,$("#customerid").val());' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">系统接收时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">本站接收时间</td>
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
										<table id="errorTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table></li>
			</div>
		</div>

		<form action="<%=request.getContextPath()%>/PDA/exportExcle" method="post" id="searchForm2">
			<input type="hidden" name="cwbs" id="cwbs" value="" /> <input type="hidden" name="exportmould2"
				id="exportmould2" />
		</form>
		<form action="<%=request.getContextPath()%>/PDA/exportByCustomerid" method="post" id="searchForm3">
			<input type="hidden" name="customerid" value="0" id="expcustomerid" /> <input type="hidden"
				name="emaildate" value="0" id="expemailid" /> <input type="hidden" name="type" value=""
				id="type" />
		</form>
	</div>
</body>
</html>
