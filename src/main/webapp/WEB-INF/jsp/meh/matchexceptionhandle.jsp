<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page
	import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page
	import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.MatchExceptionOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
Integer tWaitTraOrdCnt = (Integer)request.getAttribute("tWaitTraOrdCnt");
Integer hWaitTraOrdCnt = (Integer)request.getAttribute("hWaitTraOrdCnt");
Integer tWaitMatOrdCnt = (Integer)request.getAttribute("tWaitMatOrdCnt");
Integer hWaitMatOrdCnt = (Integer)request.getAttribute("hWaitMatOrdCnt");
List<MatchExceptionOrder> tWaitHanOrdList = (List<MatchExceptionOrder>)request.getAttribute("tWaitHanOrdList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js"
	type="text/javascript"></script>
<title>异常匹配处理</title>
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

	

	function showTab(index) {
		var $tab = $(".saomiao_tab2 li").eq(index);
		$tab.children().addClass("light");
		$tab.siblings().children().removeClass("light");
		$(".tabbox li").eq(index).show().siblings().hide();
	}
	
	function loadWaitHandleOrder(today){
		var url='<%=request.getContextPath()+"/meh/"%>';
		var tableId,tabIndex;
		if(today){
			url += "querytodaywaithandleorder"
			tableId = "t_wait_handle_table";
			tabIndex = 0;
		}
		else{
			url += "queryhistorywaithandleorder"
			tableId = "h_wait_handle_table";
			tabIndex = 1;
		}
		$.ajax({
			type:"post",
			dataType:"json",
			async:false,
			url:url,
			success:function(data){
				refreshTableData(tableId , data , tabIndex);
			}
		});
	}
	
	function refreshTableData(tableId , data , tabIndex){
	 	showTab(tabIndex);
	 	addRowData(tableId , data);
	}
	
	
	function addRowData(tableId , data){
		var $table = $("#" + tableId);
		$table.empty();
		for(var i = 0; i < data.length;i++){
			$table.append(createTR(data[i]));
		}
	}

	
	function createTR(data) {
		var tr = "<tr cwb="+ data.cwb +">";
		tr += createTD("center", "100px", data.cwb);
		tr += createTD("center", "100px", data.reportOutAreaTime);
		tr += createTD("center", "100px", data.reportOutAreaBranchName);
		tr += createTD("center", "100px", data.reportOutAreaUserName);
		tr += createTD("center", "100px", data.matchBranchName);
		tr += createTD("center", "100px", data.customerName);
		tr += createTD("center", "150px", data.customerPhone);
		tr += createTD("right", "100px", data.receivedFee);
		tr += createTD("center", null, data.customerAddress);
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
					<span>今日待分转单</span><span>今日待匹配</span>
				</dt>
				<dd style="cursor: pointer">
					<span><a href="#"><%=tWaitTraOrdCnt%></a></span>
					<span><a href="#"><%=tWaitMatOrdCnt%></a></span>
				</dd>
			</dl>


			<dl class="yellow">
				<dt>
					<span>历史待转单</span><span>历史待匹配</span>
				</dt>
				<dd>
					<span><a href="#"><%=hWaitTraOrdCnt%></a></span>
					<span><a href="#"><%=hWaitMatOrdCnt%></a></span>
				</dd>
			</dl>

			<dl class="green">
				<dt>
					<span>今日已转单</span><span>今日已匹配</span>
				</dt>
				<dd>
					<span><a href="#">0</a></span> <span><a href="#">0</a></span>
				</dd>
			</dl>

			<input type="button" id="refresh" value="刷新"
				onclick="location.href='<%=request.getContextPath()%>/meh/matchexceptionhandle'"
				style="float: left; width: 100px; height: 65px; cursor: pointer; border: none; background: url(../images/buttonbgimg1.gif) no-repeat; font-size: 18px; font-family: '微软雅黑', '黑体'" />
			<br clear="all" />
		</div>

		<div class="saomiao_info2">
			<div class="saomiao_inbox2">
				<div class="saomiao_selet2">
					站&#12288;点：<select id="customerid" name="customerid"
						onchange="tohome();">
						<option value="-1" selected>请选择</option>
						<%
							for (Branch b : branchList) {
						%>
						<option value="<%=b.getBranchid()%>"><%=b.getBranchname()%>
						</option>
						<%
							}
						%>
					</select>
				</div>
				<div class="saomiao_inwrith2">
					<div class="saomiao_left2">
						<p>
							订单号：<input type="text" class="saomiao_inputtxt" id="scancwb"
								name="scancwb" value=""
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
						<input type="button" class="input_btn1" id="moregoods"
							name="moregoods" value="一票多物"
							onclick='intoWarehouseforremark("<%=request.getContextPath()%>",$("#scansuccesscwb").val(),4,$("#multicwbnum").val());' />
						<span>一票多物件数：</span><input type="text" id="multicwbnum"
							name="multicwbnum" value="1" class="input_txt1" />
					</p>
				</div>
			</div>
		</div>

		<div>
			<div class="saomiao_tab2">
				<ul>
					<li><a href="#" onclick="loadWaitHandleOrder(true)"
						class="light">今日待处理</a></li>
					<li><a href="#" onclick="loadWaitHandleOrder(false)">历史待处理</a></li>
					<li><a href="#" onclick="showTab(2)">今日已处理</a></li>
					<li><a href="#" onclick="showTab(3)">异常单明细</a></li>
				</ul>
			</div>
			<div id="ViewList" class="tabbox">
				<li><input type="button" id="btnval0" value="导出Excel"
					class="input_button1"
					onclick='exportField(1,$("#customerid").val());' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="100" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">上报超区时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">上报超区站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">上报超区人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">分配站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">退件人姓名</td>
											<td width="150" align="center" bgcolor="#f1f1f1">联系方式</td>
											<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
											<td align="center" bgcolor="#f1f1f1">取件地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="t_wait_handle_table" width="100%" border="0"
											cellspacing="1" cellpadding="2" class="table_2">
											<%
												for (MatchExceptionOrder meo : tWaitHanOrdList) {
											%>
											<tr cwb="<%=meo.getCwb()%>" class="cwbids">
												<td width="100" align="center"><%=meo.getCwb()%></td>
												<td width="100" align="center"><%=meo.getReportOutAreaTime()%></td>
												<td width="100" align="center"><%=meo.getReportOutAreaBranchName()%></td>
												<td width="100" align="center"><%=meo.getReportOutAreaUserName()%></td>
												<td width="100" align="center"><%=meo.getReportOutAreaBranchName()%></td>
												<td width="100" align="center"><%=meo.getCustomerName()%></td>
												<td width="150" align="center"><%=meo.getCustomerPhone()%></td>
												<td width="100" align="right"><%=meo.getReceivedFee()%></td>
												<td align="center"><%=meo.getCustomerAddress()%></td>
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
					onclick='exportField(2,$("#customerid").val());' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="100" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">上报超区时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">上报超区站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">上报超区人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">分配站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">退件人姓名</td>
											<td width="150" align="center" bgcolor="#f1f1f1">联系方式</td>
											<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
											<td align="center" bgcolor="#f1f1f1">取件地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="h_wait_handle_table" width="100%" border="0"
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
											<td width="100" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">上报超区时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">上报超区站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">上报超区人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">分配站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">退件人姓名</td>
											<td width="150" align="center" bgcolor="#f1f1f1">联系方式</td>
											<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
											<td align="center" bgcolor="#f1f1f1">取件地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="t_handle_table" width="100%" border="0"
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
											<td width="100" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">上报超区时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">上报超区站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">上报超区人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">分配站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">退件人姓名</td>
											<td width="150" align="center" bgcolor="#f1f1f1">联系方式</td>
											<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
											<td align="center" bgcolor="#f1f1f1">取件地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="exception_table" width="100%" border="0"
											cellspacing="1" cellpadding="2" class="table_2">
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table></li>
			</div>
		</div>
	</div>
</body>
</html>
