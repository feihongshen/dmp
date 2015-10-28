
<%@ include file="/WEB-INF/jsp/commonLib/header.jsp"%>
<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Truck,cn.explink.domain.Bale,cn.explink.domain.Switch"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbDetailView> weichukuList = (List<CwbDetailView>)request.getAttribute("weiChuKuList");
List<CwbDetailView> yichukuList = (List<CwbDetailView>)request.getAttribute("yiChuKuList");
List<Customer> cList = (List<Customer>)request.getAttribute("customerList");
List<JSONObject> objList = request.getAttribute("objList")==null?null:(List<JSONObject>)request.getAttribute("objList");
List<Branch> bList = request.getAttribute("branchList")==null?new ArrayList<Branch>():(List<Branch>)request.getAttribute("branchList");
List<User> uList = request.getAttribute("userList")==null?new ArrayList<User>():(List<User>)request.getAttribute("userList");
List<Truck> tList = request.getAttribute("truckList")==null?new ArrayList<Truck>():(List<Truck>)request.getAttribute("truckList");
Map usermap = (Map) session.getAttribute("usermap");
long  branchid=request.getParameter("branchid")==null?0:Long.parseLong(request.getParameter("branchid"));
boolean showCustomerSign= request.getAttribute("showCustomerSign")==null?false:(Boolean)request.getAttribute("showCustomerSign");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>分拣中转出库扫描</title>
<link rel="stylesheet" href="${ctx}/css/2.css" type="text/css" />
<link rel="stylesheet" href="${ctx}/css/reset.css" type="text/css" />
<link rel="stylesheet" href="${ctx}/css/index.css" type="text/css"  />
<link rel="stylesheet" href="${ctx}/js/easyui-extend/plugins/easyui/jquery-easyui-theme/default/easyui.css" type="text/css" />

<style>
dl dt span {	width: 50%;	display: inline-block;}
dl dd span {	width: 46%;	display: inline-block;}
.blue a {	color: #336699;}
.yellow a {	color: #ff6600;}
.green a {	color: #339900}
.red a {	color: #666633;}
.input_button1 {	margin: 10px 0px 0px 10px;}
.saomiao_tab {	height: 17px;}
</style>

<script type="text/javascript"	src="${ctx}/dmp40/plug-in/jquery/jquery-1.8.3.min.js"></script>
<script src="${ctx}/js/easyui-extend/plugins/easyui/jquery-easyui-1.3.6/jquery.easyui.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/commonWidget.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/underscore/underscore-1.8.2.min.js"></script>


<script type="text/javascript">

_.templateSettings = {
  evaluate: /\{\%([\s\S]+?)\%\}/g,
  interpolate: /\{\%=([\s\S]+?)\%\}/g
};

var g_data = {
	showCustomerSign : ${showCustomerSign},
	pageSize : <%=Page.DETAIL_PAGE_NUMBER%>
};

$(function(){

	if('${isConfigZhongZhuan}'=='false'){
		$('#dialog_isconfig_zz').dialog('open');
	}


	var $menuli1 = $("#bigTag li");
	$menuli1.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
	});
	
	var $menuli2 = $("#smallTag li");
	$menuli2.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli2.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});

	var $menuli = $(".uc_midbg ul li");
	$menuli.click(function() {
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});

	
	
	getSortAndChangeOutSum();
	getcwbsquejiandataForBranchid();
	loadWeiChuList(1);
	loadYiChuList(1);
	
	$("#cwbs").focus();
})




function getNextBranchId(){
	return $('#branchid').val();
}

function loadWeiChuList(nextPage){
	var config = {
		ajax:{
			url :App.ctx + "/PDA/getSortAndChangeExportWeiChuKuList"
			,params : {
				page : nextPage
				,branchid: getNextBranchId()
			}
		}
		,prefixKey : 'weichuku' // DOM的唯一表示
		,renderId : 'tb_weichuku' //表格渲染到DOM的ID
		,tpl_list : 'tpl_weichuku_list' //模板: 表格-表头数据
		,tpl_rows : 'tpl_weichuku_list_rows' //模板: 表格行数据
		,funcLoadMore : 'loadWeiChuList' //function name when '查看更多' 点击时候触发
	}
	Widget.Datagrid.ajaxLoadDataGrid(config)
}

function loadYiChuList(nextPage){
	var config = {
		ajax:{
			url : App.ctx + "/PDA/getSortAndChangeExportYiChuKuList"
			,params : {
				page : nextPage
				,branchid:getNextBranchId()
			}
		}
		,prefixKey : 'yichuku' // DOM的唯一表示
		,renderId : 'tb_yichuku' //表格渲染到DOM的ID
		,tpl_list : 'tpl_yichuku_list' //模板: 表格-表头数据
		,tpl_rows : 'tpl_yichuku_list_rows' //模板: 表格行数据
		,funcLoadMore : 'loadYiChuList' //function name when '查看更多' 点击时候触发
	}
	Widget.Datagrid.ajaxLoadDataGrid(config)
}


//得到出库缺货件数的list列表
function getchukucwbquejiandataList(nextPage){

	var config = {
		ajax:{
			url : App.ctx + "/PDA/getSortAndChangeOutQueListPage"
			,params : {
				page : nextPage
				,nextbranchid: getNextBranchId()
			}
		}
		,prefixKey : 'ypdj_lesscwb' // DOM的唯一表示
		,renderId : 'tb_ypdj_lesscwb' //表格渲染到DOM的ID
		,tpl_list : 'tpl_ypdj_lesscwb_list' //模板: 表格-表头数据
		,tpl_rows : 'tpl_ypdj_lesscwb_list_rows' //模板: 表格行数据
		,funcLoadMore : 'getchukucwbquejiandataList' //function name when '查看更多' 点击时候触发
	}
	Widget.Datagrid.ajaxLoadDataGrid(config)
 }



function tabView(tab){
	$("#"+tab).click();
}

function addAndRemoval(cwb,tab,isRemoval,branchid){
	var trObj = $("#ViewList tr[id='TR"+cwb+"']");
	if(isRemoval){
		$("#"+tab).append(trObj);
	}else{
		$("#ViewList #errorTable tr[id='TR"+cwb+"error']").remove();
		trObj.clone(true).appendTo("#"+tab);
		$("#ViewList #errorTable tr[id='TR"+cwb+"']").attr("id",trObj.attr("id")+"error");
	}
	//已出库明细只显示该下一站明细、异常单明细只显示该下一站明细
	if(branchid>0){
		$("#successTable tr").hide();
		$("#successTable tr[nextbranchid='"+branchid+"']").show();
		
		$("#errorTable tr").hide();
		$("#errorTable tr[nextbranchid='"+branchid+"']").show();
	}else{
		$("#successTable tr").show();
		$("#errorTable tr").show();
	}
}
	
//得到当前出库的站点的库存量
function getSortAndChangeOutSum(){
	var nextbranchid = getNextBranchId();
	$.ajax({
		type: "GET",
		url: App.ctx +"/PDA/getSortAndChangeOutSum?nextbranchid="+ nextbranchid ,
		dataType:"json",
		success : function(data) {
			$("#fenjian_not_export_count").html(data.weichukucount_fj);
			$("#fenjian_not_export_sum").html(data.weichukusum_fj);
			$("#zhongzhuan_not_export_count").html(data.weichukucount_zz);
			$("#zhongzhuan_not_export_sum").html(data.weichukusum_zz);
			$("#singleoutnum").html(data.yichukucount);
			$("#lesscwbnum").html(data.lesscwbnum);
		}
	});
}

	//得到出库缺货件数的统计
function getcwbsquejiandataForBranchid() {
	var nextbranchid = getNextBranchId();
	$.ajax({
		type : "GET",
		url : App.ctx + "/PDA/getSortAndChangeOutQueSum?nextbranchid="+ nextbranchid ,
		dataType : "json",
		success : function(data) {
			$("#lesscwbnum").html(data.lesscwbnum);
		}
	});
}
	
	

/**
 * 出库扫描
 */
var branchStr=[];
var Cwbs="";

function _exportWarehouse(pname,scancwb,branchid,driverid,truckid,requestbatchno,baleno,ck_switch,confirmflag,isOpenDialog){
	
	if(scancwb.indexOf("@zd_")>-1){
		$("#branchid").val(scancwb.split('_')[1]);
		if($("#branchid").val()!=scancwb.split('_')[1]){
			
			if(isOpenDialog){
				$("#msg1").html("         （异常扫描）扫描站点失败");
				$('#find').dialog('open');
				$("#scancwb").blur();
			}else{
				$("#msg").html("         （异常扫描）扫描站点失败");
			}
			
			$("#branchid").val(0);
		}else{
			if(isOpenDialog){
				$("#msg1").html("");
			}else{
				$("#msg").html(" ");
			}
		}
		$("#scancwb").val("");
		return false;
	}
	if($("#scanbaleTag").attr("class")=="light"){//出库根据包号扫描订单
		baleaddcwbCheck();
	}else{//出库
		
			
			$.ajax({
				type: "POST",
				url:pname+"/PDA/cwbSortingAndChangeExportWarehouse/"+scancwb+"?branchid="+branchid+"&driverid="+driverid+"&truckid="+truckid+"&confirmflag="+confirmflag+"&requestbatchno="+requestbatchno+"&baleno="+baleno,
				dataType:"json",
				success : function(data) {
					$("#scancwb").val("");
					if(data.statuscode=="000000"){
						if(data.body.packageCode!=""){
							$("#msg").html(data.body.packageCode+"　（"+data.errorinfo+"）");
							if(data.body.cwbOrder.scannum==1){
								if(Cwbs.indexOf("|"+scancwb+"|")==-1){
									Cwbs += "|"+scancwb+"|";
								}
							}
							if(data.body.cwbOrder.sendcarnum>1){
								numbervedioplay("<%=request.getContextPath()%>",data.body.successCount);
							}
							if(data.body.cwbbranchnamewav!=""&&data.body.cwbbranchnamewav!=pname+"/wav/"){
								numbervedioplay("<%=request.getContextPath()%>",data.body.successCount);
							}else{
								$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
								numbervedioplay("<%=request.getContextPath()%>",data.body.successCount);
							}
						}else{
							$("#branchid").val(data.body.cwbOrder.nextbranchid);
							if(data.body.cwbOrder.scannum==1){
								if(Cwbs.indexOf("|"+scancwb+"|")==-1){
									Cwbs += "|"+scancwb+"|";
								}
							}
							
							$("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
							$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
							
							$("#scansuccesscwb").val(scancwb);
							$("#showcwb").html("订 单 号："+scancwb);
							if(data.body.showRemark!=null){
								$("#cwbDetailshow").html("订单备注："+data.body.showRemark);
							}
						}
					}else{
						$("#excelbranch").html("");
						$("#showcwb").html("");

						if(isOpenDialog){
							$("#msg1").html(scancwb+"         （异常扫描）"+data.errorinfo);
							$('#find').dialog('open');
							$("#scancwb").blur();
						}else{
							$("#msg").html(scancwb+"         （异常扫描）"+data.errorinfo);
						}	

						addAndRemoval(scancwb,"errorTable",false,$("#branchid").val());
					}
					$("#responsebatchno").val(data.responsebatchno);
					batchPlayWav(data.wavList);
				}
			});
		}
	
}


function exportWarehouse(pname,scancwb,branchid,driverid,truckid,requestbatchno,baleno,ck_switch,confirmflag){
	if(scancwb.length>0){
		if("${isOpenDialog}" != "open"){
			_exportWarehouse(pname,scancwb,branchid,driverid,truckid,requestbatchno,baleno,ck_switch,confirmflag,false);
		}else{
			_exportWarehouse(pname,scancwb,branchid,driverid,truckid,requestbatchno,baleno,ck_switch,confirmflag,true);
		}
	}
}
/* function getsum(branchid){
	if(branchStr[branchid]>0){
		$("#singleoutnum").html(branchStr[branchid]);
	}else{
		$("#singleoutnum").html(0);
	}
} */

function clearMsg(){
	$("#msg").html("");
	$("#showcwb").html("");
	$("#excelbranch").html("");
	$("#cwbDetailshow").html("");
}


function exportField(flag,branchid){
	var cwbs = "";
	if(flag==1){
		$("#type").val("weichuku");
		$("#searchForm3").submit();
	}else if(flag==2){
		$("#type").val("yichuku");
		$("#searchForm3").submit();
	}else if(flag==3){//修改导出问题
		
			$("#errorTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		
		if(cwbs.length>0){
			cwbs = cwbs.substring(0, cwbs.length-1);
		}
		if(cwbs!=""){
			$("#exportmould2").val($("#exportmould").val());
			$("#exportcwbs").val(cwbs);
			$("#btnval").attr("disabled","disabled");
		 	$("#btnval").val("请稍后……");
		 	$("#searchForm2").submit();
		}else{
			alert("没有订单信息，不能导出！");
		};
	}else if(flag==4){
		$("#type").val("ypdj");
		$("#searchForm3").submit();
		
	}
	
}

function changevalue(){
	var value = $("#confirmflag").attr("checked")=="checked"?1:0;
	$("#confirmflag").val(value);
}






function tohome(){
	window.location.href="<%=request.getContextPath() %>/PDA/cwbSortingAndChangeExportWarehouseBatch?branchid="+$("#branchid").val();	
}


</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="saomiao_box2">
	<div class="saomiao_tab2">
		<ul>
			<li><a href="<%=request.getContextPath()%>/PDA/sortingAndChangeExportWarehouse" >逐单操作</a></li>		
			<li><a href="#" class="light" >批量操作</a></li>
		</ul>
	</div>

 
	<div class="saomiao_topnum2">
		
		<dl class="blue">
			<dt>
				<span>分拣库待出库</span><span>分拣库待出库合计</span>
			</dt>
			<dd style="cursor: pointer">
				<span onclick="tabView('table_weichuku')">${weichukucount_fj}</span> 
						
				<span>${weichukusum_fj}</span>
			</dd>
		</dl>
	
		<dl class="green">
			<dt>
				<span>中转待出库</span><span>中转待出库合计</span>
			</dt>
			<dd style="cursor: pointer">
				<span onclick="tabView('table_weichuku')">${weichukucount_zz}</span> 
						
				<span>${weichukusum_zz}</span>
			</dd>
		</dl>
		
		<dl class="yellow">
			<dt>
				<span>已出库未到站</span><span>一票多件缺货件数</span>
			</dt>
			<dd style="cursor: pointer">
				<span onclick="tabView('table_yichuku')">${yichukucount }</span> 
						
				<span onclick="tabView('table_quejian');getchukucwbquejiandataList(1);">
				<a href="#"
					id="lesscwbnum"><img
						src="<%=request.getContextPath()%>/images/loading_small.gif" /></a></span>
			</dd>
		</dl>
		
		
		
		<br clear="all"/>
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="saomiao_righttitle2" id="pagemsg"></div>
			<form action="${ctx}/PDA/cwbSortingAndChangeExportWarehouseBatch" id="submitform" method="post">
			<div class="saomiao_selet2">
				下一站：
				<select id="branchid" name="branchid" onchange="tohome();" class="select1">
					<option value="-1" selected>请选择</option>
					<%
					for(Branch b : bList){ %>
						<option value="<%=b.getBranchid() %>" <%if(branchid==b.getBranchid()) {%> selected=selected<% }%> ><%=b.getBranchname() %></option>
					<%} %>
				</select>
				驾驶员：
				<select id="driverid" name="driverid" class="select1">
					<option value="-1" selected>请选择</option>
					<%for(User u : uList){ %>
						<option value="<%=u.getUserid() %>" ><%=u.getRealname() %></option>
					<%} %>
		        </select>
				车辆：
				<select id="truckid" name="truckid" class="select1">
					<option value="-1" selected>请选择</option>
					<%for(Truck t : tList){ %>
						<option value="<%=t.getTruckid() %>" ><%=t.getTruckno() %></option>
					<%} %>
		        </select>
			</div>
			<div class="saomiao_inwrith2">
				<div class="saomiao_left2">
					<%if(Long.parseLong(usermap.get("isImposedOutWarehouse").toString())==1){ %>
					<p>
						<span>强制出库:</span><input type="checkbox" id="confirmflag" name="confirmflag" value=''onclick="changevalue();"/>
					</p>
					<%} %>
					<p><span>订单号：</span>
						<%-- <input type="text" class="saomiao_inputtxt2" value="" id="scancwb" name="scancwb"  onKeyDown='if(event.keyCode==13&&$(this).val().length>0){exportWarehouse("<%=request.getContextPath()%>",$(this).val(),$("#branchid").val(),$("#driverid").val(),$("#truckid").val(),$("#requestbatchno").val(),$("#baleno").val(),$("#ck_switch").val(),$("#confirmflag").attr("checked")=="checked"?1:0);}'/> --%>
						<textarea cols="24" rows="4"  name ="cwbs" id="cwbs" ></textarea>
					</p>
					<span>&nbsp;</span><input type="submit" name="finish" id="finish" value="确认批量出库" class="button" >
				</div>
				<div class="saomiao_right2">
					<p id="msg" name="msg" >${msg }</p>
					<div style="display: none" id="EMBED">
					</div>
					<div style="display: none">
						<EMBED id='ypdj' name='ypdj' SRC='<%=request.getContextPath() %><%=ServiceUtil.waverrorPath %><%=CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl() %>' LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
					</div>
					<div style="display: none" id="errorvedio">
					</div>
				</div>
				<input type="hidden" id="requestbatchno" name="requestbatchno" value="0" />
		        <input type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" />
		        <input type="hidden" id="ck_switch" name="ck_switch" value="ck_02" />
			</div>
			</form>
		</div>
	</div>
	
	
		<div>
			<div class="saomiao_tab2">
				<span style="float: right; padding: 10px"></span>
				<ul id="smallTag">
					<li><a id="table_weichuku" href="#" class="light">待出库明细</a></li>
					<li><a id="table_yichuku" href="#">已出库明细</a></li>
					<li><a id="table_quejian" href="#" onclick="getchukucwbquejiandataList(1);getcwbsquejiandataForBranchid();">一票多件缺件</a></li>
					<li><a href="#">异常单明细</a></li>
				</ul>
			</div>
			<div id="ViewList" class="tabbox">
				

				<!-- 待出库列表 -->
				<li>
					<input type="button" id="btnval0" value="导出Excel" class="input_button1"
						onclick='exportField(1,$("#branchid").val());' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0" id="tb_weichuku">
					</table>
				</li>
				<li style="display: none">
					<input type="button" id="btnval0" value="导出Excel"
					class="input_button1" onclick='exportField(2,$("#branchid").val());' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0" id="tb_yichuku">				
					</table>
				</li>
				<li style="display: none"><input type="button" id="btnval0" value="导出Excel"
					class="input_button1" onclick='exportField(4,$("#customerid").val());' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0" id="tb_ypdj_lesscwb">
						
					</table>
				</li>


				<li style="display: none">
					<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(3,$("#branchid").val());'/>
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
											<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
											<td width="100" align="center" bgcolor="#f1f1f1">异常原因</td>
										</tr>
									</table>
									<div>
										<table id="errorTable" width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
										<%if(objList!=null)for(JSONObject obj : objList){if(!obj.get("errorcode").equals("000000")){ %>
										<%JSONObject cwbOrder =  obj.get("cwbOrder")==null?null:JSONObject.fromObject(obj.get("cwbOrder"));%>
											<tr id="TR<%=obj.get("cwb") %>" cwb="<%=obj.get("cwb") %>" >
												<td width="120" align="center"><%=obj.get("cwb") %></td>
												<td width="100" align="center"><%=obj.get("customername") %></td>
												<td width="140" align="center"><%=cwbOrder==null?"":cwbOrder.getString("emaildate") %></td>
												<td width="100" align="center"><%=cwbOrder==null?"":cwbOrder.getString("consigneename") %></td>
												<td width="100" align="center"><%=cwbOrder==null?"":cwbOrder.getDouble("receivablefee") %></td>
												<%if(showCustomerSign){ %>
													<td width="100"><%=obj.get("showRemark")==null?"":obj.get("showRemark") %></td>
												<%} %>
												<td width="100" align="center"><%=obj.get("errorinfo") %></td>
											</tr>
											<%}} %>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
			</div>
		</div>
		
		<form action="<%=request.getContextPath()%>/PDA/exportExcle" method="post" id="searchForm2">
			<input type="hidden" name="cwbs" id="exportcwbs" value=""/>
			<input type="hidden" name="exportmould2" id="exportmould2" />
		</form>
 		<form action="<%=request.getContextPath() %>/PDA/exportBybranchid" method="post" id="searchForm3">
			<input  type="hidden"  name="branchid" value="<%=request.getParameter("branchid")==null?"0":request.getParameter("branchid")%>" id="expbranchid" />
			<input type="hidden" name="type" value="" id="type"/>
		</form>


		<c:if test="${isConfigZhongZhuan=='false'}">
			<div  id="dialog_isconfig_zz" class="easyui-dialog" data-options="modal:true" title="提示信息"  style="width:400px;height:200px;">
		 		<div class="saomiao_right2">
						<p id="msg1" name="msg1" >当前用户的【区域权限设置】中没有分配相关的【中转库】，为了不影响您的操作，请联系管理员。</p>
					<div  align="center" valign="bottom" style="margin-top:10px;">
			         	<input type="button" class="input_button1" value="关闭" onclick="$('#dialog_isconfig_zz').dialog('close')"/>
		         	</div>
		 	</div>
	 	</c:if>
</div>
</body>


<script type="text/template" id="tpl_weichuku_list">
    	
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
							{%if(showCustomerSign){ %}
								<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
							{%} %}
							<td align="center" bgcolor="#f1f1f1">地址</td>
						</tr>
					</table>
					<div style="height: 160px; overflow-y: scroll">
						<table  width="100%" border="0" cellspacing="1" cellpadding="2"
							class="table_2">
							<tbody id="{%=domID.contentTable%}"></tbody>
						</table>
					</div>
				</td>
			</tr>
		</tbody>
	
</script>
<script type="text/template" id="tpl_weichuku_list_rows">

{%_.each(rows,function(co,i){%}
	<tr id="TR{%=co.cwb%}" cwb="{%=co.cwb %}" customerid="{%=co.customerid%}" nextbranchid="{%=co.nextbranchid %}" >
		<td width="120" align="center">{%=co.cwb %}</td>
		<td width="100" align="center">{%=co.customername%}</td>
		<td width="140">{%=co.emaildate %}</td>
		<td width="100">{%=co.consigneename %}</td>
		<td width="100">{%=co.receivablefee %}</td>
		{%if(showCustomerSign){ %}
			<td width="100">{%=co.remarkView %}</td>
		{%} %}
		<td align="left">{%=co.consigneeaddress %}</td>
	</tr>
{%}) %}

{%if(rows.length>=pageSize){ %}
	<tr id="{%=domID.loadMore%}" align="center"  >
		<td  colspan="{%=showCustomerSign? 7:6 %}" style="cursor:pointer" 
			onclick="{%=funcName.loadMore%}({%=nextPage%});" >查看更多</td>
	</tr>
{%} %}
</script>



<script type="text/template" id="tpl_yichuku_list">
    	
		<tbody>
			<tr>
				<td width="10%" height="26" align="left" valign="top">
					<table width="100%" border="0" cellspacing="0" cellpadding="2"
						class="table_5">
						<tr>
							<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
							<td width="100" align="center" bgcolor="#f1f1f1">包号</td>
							<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
							<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
							<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
							<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
							{%if(showCustomerSign){ %}
								<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
							{%} %}
							<td align="center" bgcolor="#f1f1f1">地址</td>
						</tr>
					</table>
					<div style="height: 160px; overflow-y: scroll">
						<table  width="100%" border="0" cellspacing="1" cellpadding="2"
							class="table_2">
							<tbody id="{%=domID.contentTable%}"></tbody>
						</table>
					</div>
				</td>
			</tr>
		</tbody>
	
</script>
<script type="text/template" id="tpl_yichuku_list_rows">

{%_.each(rows,function(co,i){%}
	<tr id="TR{%=co.cwb%}" cwb="{%=co.cwb %}" customerid="{%=co.customerid%}" nextbranchid="{%=co.nextbranchid %}" >
		<td width="120" align="center">{%=co.cwb %}</td>
		<td width="100" align="center">{%=co.packagecode %}</td>
		<td width="100" align="center">{%=co.customername%}</td>
		<td width="140">{%=co.emaildate %}</td>
		<td width="100">{%=co.consigneename %}</td>
		<td width="100">{%=co.receivablefee %}</td>
		{%if(showCustomerSign){ %}
			<td width="100">{%=co.remarkView %}</td>
		{%} %}
		<td align="left">{%=co.consigneeaddress %}</td>
	</tr>
{%}) %}

{%if(rows.length>=pageSize){ %}
	<tr id="{%=domID.loadMore%}" align="center"  >
		<td  colspan="{%=showCustomerSign? 8:7 %}" style="cursor:pointer" 
			onclick="{%=funcName.loadMore%}({%=nextPage%});" >查看更多</td>
	</tr>
{%} %}
</script>

<script type="text/template" id="tpl_ypdj_lesscwb_list">
    	
		<tbody>
			<tr>
				<td width="10%" height="26" align="left" valign="top">
					<table width="100%" border="0" cellspacing="0" cellpadding="2"	class="table_5">
						<tr>
							<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
							<td width="120" align="center" bgcolor="#f1f1f1">运单号</td>
							<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
							<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
							<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
							<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
							<td align="center" bgcolor="#f1f1f1">地址</td>
						</tr>
					</table>
					<div style="height: 160px; overflow-y: scroll">
						<table  width="100%" border="0" cellspacing="1" cellpadding="2"
							class="table_2">
							<tbody id="{%=domID.contentTable%}"></tbody>
						</table>
					</div>
				</td>
			</tr>
		</tbody>
	
</script>
<script type="text/template" id="tpl_ypdj_lesscwb_list_rows">

{%_.each(rows,function(co,i){%}
	<tr id="TR{%=co.cwb%}" cwb="{%=co.cwb %}" customerid="{%=co.customerid%}" nextbranchid="{%=co.nextbranchid %}" >
		<td width="120" align="center">{%=co.cwb %}</td>
		<td width="120" align="center">{%=co.transcwb %}</td>
		<td width="100" align="center">{%=co.customername%}</td>
		<td width="140">{%=co.emaildate %}</td>
		<td width="100">{%=co.consigneename %}</td>
		<td width="100">{%=co.receivablefee %}</td>
		<td align="left">{%=co.consigneeaddress %}</td>
	</tr>
{%}) %}

{%if(rows.length>=pageSize){ %}
	<tr id="{%=domID.loadMore%}" align="center"  >
		<td  colspan="{%=showCustomerSign? 8:7 %}" style="cursor:pointer" 
			onclick="{%=funcName.loadMore%}({%=nextPage%});" >查看更多</td>
	</tr>
{%} %}
</script>


</html>
