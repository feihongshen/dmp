<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Truck,cn.explink.domain.Bale,cn.explink.domain.Switch"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%

List<Branch> bList = request.getAttribute("branchlist")==null?new ArrayList<Branch>():(List<Branch>)request.getAttribute("branchlist");
List<User> uList = request.getAttribute("userList")==null?new ArrayList<User>():(List<User>)request.getAttribute("userList");
List<Truck> tList = request.getAttribute("truckList")==null?new ArrayList<Truck>():(List<Truck>)request.getAttribute("truckList");
List<Bale> balelist = request.getAttribute("balelist")==null?new ArrayList<Bale>():(List<Bale>)request.getAttribute("balelist");
Switch ckfb_switch = (Switch) request.getAttribute("ckfb_switch");
Map usermap = (Map) session.getAttribute("usermap");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>出库扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$(function(){
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
	
})

	$(function(){
		getOutSum('<%=request.getContextPath()%>',0);
		$("#scancwb").focus();
	});
	
	
	
$(function() {
	var $menuli = $(".uc_midbg ul li");
	$menuli.click(function() {
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
});

	//得到当前出库的站点的库存量
	function getOutSum(pname,branchid){
		$.ajax({
			type: "POST",
			url:pname+"/PDA/getOutSum?nextbranchid="+branchid,
			dataType:"json",
			success : function(data) {
				$("#chukukucundanshu").html(data.weichukucount);
				$("#singleoutnum").html(data.yichukucount);
			}
		});
	}
	

/**
 * 出库扫描
 */
var branchStr=[];
var Cwbs="";
function exportWarehouse(pname,scancwb,branchid,driverid,truckid,requestbatchno,baleno,ck_switch,confirmflag){
	if(scancwb.length>0){
		
		$.ajax({
			type: "POST",
			url:pname+"/PDA/cwbexportwarhouse/"+scancwb+"?branchid="+branchid+"&driverid="+driverid+"&truckid="+truckid+"&confirmflag="+confirmflag+"&requestbatchno="+requestbatchno+"&baleno="+baleno,
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
							//getOutSum(pname,data.body.cwbOrder.nextbranchid);
						}
					}/* else if(data.body.cwbOrder.nextbranchid!=0&&branchid!=-1&&branchid!=0&&data.body.cwbOrder.nextbranchid!=branchid){
						if(ck_switch=="ck_01"){//允许出库
							var con = confirm("出库站点与分拣站点不一致，是否强制出库？");
							if(con == false){
								$("#excelbranch").html("");
								$("#showcwb").html("");
								$("#msg").html(scancwb+"         （异常扫描）出库站点与分拣站点不一致");
								$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+pname+"/images/waverror/fail.wav&a="+Math.random());
							}else{
								$.ajax({
									type: "POST",
									url:pname+"/PDA/cwbexportwarhouse/"+scancwb+"?branchid="+branchid+"&driverid="+driverid+"&truckid="+truckid+"&confirmflag=1&requestbatchno="+requestbatchno+"&baleno="+baleno,
									dataType:"json",
									success : function(data) {
										if(data.statuscode=="000000"){
											$("#branchid").val(data.body.cwbOrder.nextbranchid);
											$("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
											$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
											
											if(data.body.cwbOrder.scannum==1){
												if(Cwbs.indexOf("|"+scancwb+"|")>-1){
													Cwbs += "|"+scancwb+"|";
													
												}
												getOutSum(pname,data.body.cwbOrder.nextbranchid);
											}
											
											$("#scansuccesscwb").val(scancwb);
											$("#showcwb").html("订 单 号："+scancwb);
											
											if(data.body.cwbOrder.sendcarnum>1){
												document.ypdj.Play();
											}
											if(data.body.cwbbranchnamewav!=""&&data.body.cwbbranchnamewav!=pname+"/wav/"){
												$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random());
											}else{
												$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
											}
										}else{
											$("#excelbranch").html("");
											$("#showcwb").html("");
											$("#msg").html(scancwb+"         （异常扫描）"+data.errorinfo);
											errorvedioplay(pname,data);
										}
									}
								});
							}
						}else{
							$("#excelbranch").html("");
							$("#showcwb").html("");
							$("#msg").html(scancwb+"         （异常扫描）出库站点与分拣站点不一致");
							$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+pname+"/images/waverror/fail.wav&a="+Math.random());
						}
					} */else{
						$("#branchid").val(data.body.cwbOrder.nextbranchid);
						if(data.body.cwbOrder.scannum==1){
							if(Cwbs.indexOf("|"+scancwb+"|")==-1){
								Cwbs += "|"+scancwb+"|";
								
							}
							//getOutSum(pname,data.body.cwbOrder.nextbranchid);
						}
						
						$("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
						$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
						
						$("#scansuccesscwb").val(scancwb);
						$("#showcwb").html("订 单 号："+scancwb);
						//getOutSum(pname,data.body.cwbOrder.nextbranchid);
						
						
						/*if(data.body.cwbOrder.sendcarnum>1){
							document.ypdj.Play();
						}
						if(data.body.cwbbranchnamewav!=""&&data.body.cwbbranchnamewav!=pname+"/wav/"){
							$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random());
						}else{
							$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
						} */
					}
				}else{
					$("#excelbranch").html("");
					$("#showcwb").html("");
					$("#msg").html(scancwb+"         （异常扫描）"+data.errorinfo);
					//errorvedioplay(pname,data);
				}
				$("#responsebatchno").val(data.responsebatchno);
				batchPlayWav(data.wavList);
			}
		});
	}
}


function clearMsg(){
	$("#msg").html("");
	$("#showcwb").html("");
	$("#excelbranch").html("");
}

function  falshdata(){
	$("#refresh").attr("disabled","disabled");
	 $("#refresh").val("请稍后……");
	getOutSum('<%=request.getContextPath() %>',$("#branchid").val());
	$("#refresh").val("刷新");
	 $("#refresh").removeAttr("disabled");
}
</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="saomiao_box2">
 
	<div class="saomiao_topnum2">
		<dl class="blue">
			<dt>待出库</dt>
			<dd id="chukukucundanshu">0</dd>
		</dl>
		
		<dl class="green">
			<dt>本次已出库</dt>
			<dd id="singleoutnum">0</dd>
		</dl>
		<input type="button"  id="refresh" value="刷新" onclick="location.href='<%=request.getContextPath() %>/PDA/jiajijianchukulist'" style="float:left; width:100px; height:65px; cursor:pointer; border:none; background:url(../images/buttonbgimg1.gif) no-repeat; font-size:18px; font-family:'微软雅黑', '黑体'"/>
		
		
		<br clear="all"/>
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="kfsh_tabbtn">
				<ul id="bigTag">
					<li><a href="#" onclick="clearMsg();$(function(){$('#baleno').parent().hide();$('#finish').parent().hide();$('#baleno').val('');$('#scancwb').val('');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();})" class="light">扫描订单</a></li>
					
				</ul>
			</div>
			<div class="saomiao_righttitle2" id="pagemsg"></div>
			<!-- <form action="" method="get"> -->
			<div class="saomiao_selet2">
				下一站：
				<select id="branchid" name="branchid" onchange="getOutSum('<%=request.getContextPath()%>',$(this).val());">
					<option value="-1" selected>请选择</option>
					<%for(Branch b : bList){ %>
						<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
					<%} %>
				</select>
				驾驶员：
				<select id="driverid" name="driverid">
					<option value="-1" selected>请选择</option>
					<%for(User u : uList){ %>
						<option value="<%=u.getUserid() %>" ><%=u.getRealname() %></option>
					<%} %>
		        </select>
				车辆：
				<select id="truckid" name="truckid">
					<option value="-1" selected>请选择</option>
					<%for(Truck t : tList){ %>
						<option value="<%=t.getTruckid() %>" ><%=t.getTruckno() %></option>
					<%} %>
		        </select>
			</div>
			<div class="saomiao_inwrith2">
				<div class="saomiao_left2">
					<%if(Long.parseLong(usermap.get("isImposedOutWarehouse").toString())==1){ %>
					<p style="display:none">
						强制出库:</span><input type="checkbox" id="confirmflag" name="confirmflag" value="1"/>
					</p>
					<%} %>
					<p style="display: none;"><span>包号：</span><input type="text" class="saomiao_inputtxt2" name="baleno" id="baleno" onKeyDown="if(event.keyCode==13&&$(this).val().length>0){$(this).attr('readonly','readonly');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();}"/></p>
					<p><span>订单号：</span>
						<input type="text" class="saomiao_inputtxt2" value="" id="scancwb" name="scancwb"  onKeyDown='if(event.keyCode==13&&$(this).val().length>0){exportWarehouse("<%=request.getContextPath()%>",$(this).val(),$("#branchid").val(),$("#driverid").val(),$("#truckid").val(),$("#requestbatchno").val(),$("#baleno").val(),$("#ck_switch").val(),$("#confirmflag").attr("checked")=="checked"?1:0);}'/>
					</p>
					<p style="display: none;">
						<span>&nbsp;</span><input type="submit" name="finish" id="finish" value="完成扫描" class="button" onclick="$('#baleno').removeAttr('readonly');$('#baleno').val('');">
					</p>
				</div>
				<div class="saomiao_right2">
					<p id="msg" name="msg" ></p>
					<p id="showcwb" name="showcwb"></p>
					<p id="excelbranch" name="excelbranch" ></p>
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
		</div>
	</div>
</div>
</body>
</html>
