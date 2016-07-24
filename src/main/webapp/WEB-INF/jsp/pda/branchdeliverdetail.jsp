<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@page import="cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import="cn.explink.enumutil.CwbFlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch,cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
List<CwbDetailView> todayweilinghuolist = (List<CwbDetailView>)request.getAttribute("todayweilinghuoViewlist");
List<CwbDetailView> historyweilinghuolist = (List<CwbDetailView>)request.getAttribute("historyweilinghuolist");
List<CwbDetailView> yilinghuolist = (List<CwbDetailView>)request.getAttribute("yilinghuolist");
List<Customer> cList = (List<Customer>)request.getAttribute("customerlist");
List<User> uList = (List<User>)request.getAttribute("userList");
String did=request.getParameter("deliverid")==null?"0":request.getParameter("deliverid");
long deliverid=Long.parseLong(did);
boolean showCustomerSign= request.getAttribute("showCustomerSign")==null?false:(Boolean)request.getAttribute("showCustomerSign");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>领货扫描（明细）</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$(function(){
	var $menuli = $(".kfsh_tabbtn ul li");
	var $menulilink = $(".kfsh_tabbtn ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
})

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

$(function(){
	getweilingdata($("#deliverid").val());
	 $("#scancwb").focus();
});

//得到当前待领货的库存量
function getweilingdata(deliverid){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath() %>/PDA/getWeiLingHuoSum",
		data:{"deliverid":deliverid},
		dataType:"json",
		success : function(data) {
			$("#todayweilingdanshu").html(data.todayweilinghuocount);
			$("#historyweilingdanshu").html(data.historyweilinghuocount);
			$("#linghuoSuccessCount").html(data.yilinghuo);
			$("#yuyuedayilinghuocount").html(data.yuyuedayilinghuocount);
			$("#yuyuedaweilinghuocount").html(data.yuyuedaweilinghuocount);
			
		}
	});
}

/**
 * 小件员领货扫描
 */
 var deliverStr=[];
 
<%for(User u : uList){ %>
	deliverStr[<%=u.getUserid()%>]="";
<%}%>
function branchDeliver(pname,scancwb,deliverid,requestbatchno){
	if(deliverid==-1){
		$("#msg").html("扫描前请选择小件员");
		return ;
	}else if(scancwb.length>0){
		var allnum = 0;
		var isChaoqu = $("#isChaoqu").is(":checked");
		$.ajax({
			type: "POST",
			url:pname+"/PDA/cwbbranchdeliver/"+scancwb+"?deliverid="+deliverid+"&requestbatchno="+requestbatchno + "&isChaoqu=" + isChaoqu,
			dataType:"json",
			success : function(data) {
				
				$("#scancwb").val("");
				$("#pagemsg").html("");
				//var linghuoSuccessCount = deliverStr[deliverid].split(",").length-1;
				if(data.statuscode=="000000"){
					$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
					
					$("#scansuccesscwb").val(scancwb);
					$("#showcwb").html("订 单 号："+data.body.cwbOrder.cwb);
					$("#consigneeaddress").html("地 址："+data.body.cwbOrder.consigneeaddress);
					if(data.body.isChaoqu == true) {
						if(data.body.matchDeliver == "") {
							$("#matchDeliver").html("尚未匹配小件员");
						} else {
							$("#matchDeliver").html("订单匹配小件员：" + data.body.matchDeliver);
						}
						$("#receiveDeliver").html("领货小件员：" + data.body.receiveDeliver);
					}
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
					if(data.body.isChaoqu != true) {
						$("#exceldeliverid").html(data.body.cwbdelivername);
					}
					$("#deliver").html("已领货（"+data.body.cwbdelivername+"）");
					
					if (data.body.editCwb != null && data.body.editCwb != "") {
						alert(data.body.editCwb);
					}
					
					$("#guanlianlantuidan").hide();
					if(data.body.guanlianlantuidan){
						$("#guanlianlantuidan").show();
					}
					
					
					//var checkRecord = data.body.checkRecord;
					//alert(checkRecord);
					//getweilingdata(data.body.cwbOrder.deliverid);
					//将成功扫描的订单放到已入库明细中
					//addAndRemoval(data.body.cwbOrder.cwb,"successTable",true);
					/*if(data.body.cwbOrder.sendcarnum>1){
						document.ypdj.Play();
					}
					
					if(data.body.cwbdelivernamewav!=pname+"/wav/"){
						$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbdelivernamewav+"&a="+Math.random());
					}else{
						$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
					}*/
				}else{
					$("#exceldeliverid").html("");
					$("#showcwb").html("");
					$("#consigneeaddress").html("");
					$("#matchDeliver").html("");
					$("#receiveDeliver").html("");
					$("#cwbordertype").html("");
					$("#cwbDetailshow").html("");
					$("#deliver").html("已领货");
					$("#fee").html("");
					$("#customercommand").html("");
					$("#msg").html(scancwb+"         （异常扫描）"+data.errorinfo);
					addAndRemoval(scancwb,"errorTable",false);
					//errorvedioplay(pname,data);
					if (data.statuscode=='149') { //订单存在未确认的支付信息修改申请，终止领货，并且弹窗提示
						alert(data.errorinfo);
					}
				}
				$("#responsebatchno").val(data.responsebatchno);
				batchPlayWav(data.wavList);
			}
		});
	}
}
function exportField(flag,deliverid){
	var cwbs = "";
	if(flag==1){
		/* $("#todayweilinghuoTable tr").each(function(){
			var cwb = $(this).attr("cwb");
			cwbs += "'" + cwb + "',";
		}); */
		$("#type").val(flag);
		$("#searchForm3").submit();
	}else if(flag==2){
		/* if(deliverid>0){
			$("#successTable tr[deliverid='"+deliverid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}else{
			$("#successTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		} */
		$("#type").val(flag);
		$("#searchForm3").submit();
	}else if(flag==3){//修改导出问题
		/* if(deliverid>0){
			$("#errorTable tr[deliverid='"+deliverid+"']").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
		}else{ */
			$("#errorTable tr").each(function(){
				var cwb = $(this).attr("cwb");
				cwbs += "'" + cwb + "',";
			});
			if(cwbs.length>0){
				cwbs = cwbs.substring(0, cwbs.length-1);
			}
			if(cwbs!=""){
				$("#cwbs").val(cwbs);
				$("#btnval").attr("disabled","disabled");
			 	$("#btnval").val("请稍后……");
			 	$("#searchForm2").submit();
			}else{
				alert("没有订单信息，不能导出！");
			};
	}else if(flag==4){
		/* $("#historyweilinghuoTable tr").each(function(){
			var cwb = $(this).attr("cwb");
			cwbs += "'" + cwb + "',";
		}); */
		$("#type").val(flag);
		$("#searchForm3").submit();
	}
}

var toweilingpage=1;var historyweilingpage=1;var yipage=1;
function todayweilinghuo(){
	toweilingpage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbranchideliverweilinglist",
		data:{"page":toweilingpage,"deliverid":$("#deliverid").val()},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					<%if(showCustomerSign){ %>
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' nextbranchid='"+data[i].nextbranchid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
					+"<td width='230' align='center'> "+data[i].consigneeaddress+"</td>"
					+"<td width='60' align='center'>"+data[i].cwbstatetext+"</td>"
					+"<td width='60' align='center'>"+data[i].flowordertypetext+"</td>"
					+"<td align='center'>"+data[i].checkstateresultname+"</td>"
					+ "</tr>";
				<%}else{ %>
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' nextbranchid='"+data[i].nextbranchid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td width='230' align='center'> "+data[i].consigneeaddress+"</td>"
					+"<td width='60' align='center'>"+data[i].cwbstatetext+"</td>"
					+"<td width='60' align='center'>"+data[i].flowordertypetext+"</td>"
					+"<td align='center'>"+data[i].checkstateresultname+"</td>"
					+ "</tr>";
				<%} %>
				}
				$("#todayweilinghuo").remove();
				$("#todayweilinghuoTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
					var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="todayweilinghuo();" id="todayweilinghuo">查看更多</td></tr>';
				$("#todayweilinghuoTable").append(more);
				}
			}
		}
	});
	
	
}

function  historyweilinghuo(){
	historyweilingpage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbranchideliverweilinghistorylist",
		data:{"page":historyweilingpage,"deliverid":$("#deliverid").val()},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					<%if(showCustomerSign){ %>
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' deliverid='"+data[i].deliverid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
					+"<td width='230' align='center'> "+data[i].consigneeaddress+"</td>"
					+"<td width='60' align='center'>"+data[i].cwbstatetext+"</td>"
					+"<td width='60' align='center'>"+data[i].flowordertypetext+"</td>"
					+"<td align='center'>"+data[i].checkstateresultname+"</td>"
					+ "</tr>";
				<%}else{ %>
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' deliverid='"+data[i].deliverid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td width='230' align='center'> "+data[i].consigneeaddress+"</td>"
					+"<td width='60' align='center'>"+data[i].cwbstatetext+"</td>"
					+"<td width='60' align='center'>"+data[i].flowordertypetext+"</td>"
					+"<td align='center'>"+data[i].checkstateresultname+"</td>"
					+ "</tr>";
				<%} %>
				}
				$("#historyweilinghuo").remove();
				$("#historyweilinghuoTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
					var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="historyweilinghuo();" id="historyweilinghuo">查看更多</td></tr>';
				$("#historyweilinghuoTable").append(more);
				}
			}
		}
	});
	
	
}  

function yiling(){
	yipage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getbranchideliveryilinglist",
		data:{"page":yipage,"deliverid":$("#deliverid").val()},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					<%if(showCustomerSign){ %>
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' deliverid='"+data[i].deliverid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
					+"<td width='230' align='center'> "+data[i].consigneeaddress+"</td>"
					+"<td width='60' align='center'>"+data[i].cwbstatetext+"</td>"
					+"<td width='60' align='center'>"+data[i].flowordertypetext+"</td>"
					+"<td align='center'>"+data[i].checkstateresultname+"</td>"
					+ "</tr>";
				<%}else{ %>
					optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' deliverid='"+data[i].deliverid+"' >"
					+"<td width='120' align='center'>"+data[i].cwb+"</td>"
					+"<td width='100' align='center'> "+data[i].customername+"</td>"
					+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
					+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
					+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
					+"<td width='230' align='center'> "+data[i].consigneeaddress+"</td>"
					+"<td width='60' align='center'>"+data[i].cwbstatetext+"</td>"
					+"<td width='60' align='center'>"+data[i].flowordertypetext+"</td>"
					+"<td align='center'>"+data[i].checkstateresultname+"</td>"
					+ "</tr>";
				<%} %>
					
				}
				$("#yiling").remove();
				$("#successTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
					var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="yiling();" id="yiling">查看更多</td></tr>';
				$("#successTable").append(more);
				}
			}
		}
	});
	
}
function tohome(){
	window.location.href="<%=request.getContextPath() %>/PDA/branchdeliverdetail?deliverid="+$("#deliverid").val();
}

function scancwbKeyDownAction(event) {
    if (event.keyCode == 13) {
        if ($("#scancwb").val().length > 0) {
            $.ajax({
                type: "POST",
                url: "<%=request.getContextPath()%>" + "/PDA/isExtractInput/" + $("#scancwb").val(),
                dataType: "json",
                success: function (data) {

                    if (data.body && data.body.result == true) {
                        $("#msg").html("请补录全快递单信息再领货");
                        return false;
                    } else {
                        branchDeliver("<%=request.getContextPath()%>", $("#scancwb").val(), $("#deliverid").val(), $("#requestbatchno").val());

                    }
                }
            });
        }
    }
}


</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="saomiao_box2">

<div class="saomiao_tab2">
		<ul>
			<li><a href="#"  class="light">逐单操作</a></li>		
			<li><a href="<%=request.getContextPath()%>/PDA/branchdeliverBatch">批量操作</a></li>
		</ul>
	</div>
 
 
	<div class="saomiao_topnum2">
		<dl class="blue">
			<dt>今日待领货合计</dt>
			<dd style="cursor:pointer" onclick="tabView('table_todayweilinghuo')" id="todayweilingdanshu">0</dd>
		</dl>
		<dl class="yellow">
			<dt>历史待领货合计</dt>
			<dd style="cursor:pointer" onclick="tabView('table_historyweilinghuo')" id="historyweilingdanshu">0</dd>
		</dl>
		<c:if test="${yuyuedaService=='yes'}">
			<dl class="green">
			<dt>预约达待领货</dt>
			<dd style="cursor:pointer"  id="yuyuedaweilinghuocount"></dd>
		</dl>
		<dl class="blue">
			<dt>预约达已领货</dt>
			<dd style="cursor:pointer"  id="yuyuedayilinghuocount"></dd>
		</dl>
		</c:if>
		<dl class="green">
			<dt id="deliver">已领货</dt>
			<dd style="cursor:pointer" onclick="tabView('table_yilinghuo')" id="linghuoSuccessCount">0</dd>
		</dl>
		<input type="button"  id="refresh" value="刷新" onclick="location.href='<%=request.getContextPath() %>/PDA/branchdeliverdetail'" style="float:left; width:100px; height:65px; cursor:pointer; border:none; background:url(../images/buttonbgimg1.gif) no-repeat; font-size:18px; font-family:'微软雅黑', '黑体'"/>
		<br clear="all">
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<!-- <div class="saomiao_lefttitle">扫描订单</div> -->
			<div class="saomiao_righttitle" id="pagemsg"></div>
			<div class="saomiao_selet2">
				小件员：
					<select id="deliverid" name="deliverid" onchange="tohome();" class="select1">
						<option value="-1" selected>请选择</option>
						<%for(User u : uList){ %>
							<option value="<%=u.getUserid() %>"  <%if(deliverid==u.getUserid()) {%>selected=selected<%} %>    ><%=u.getRealname() %></option>
						<%} %>
			        </select>*
			        超区领货：<input type="checkbox" id="isChaoqu" name="isChaoqu"/>
			</div>
			<div class="saomiao_inwrith2">
				<div class="saomiao_left2">
					<p><span>订单号：</span>
						<input type="text" class="saomiao_inputtxt2" id="scancwb" name="scancwb" value=""  onKeyDown='return scancwbKeyDownAction(event);'/>
					</p>
				</div>
				<div class="saomiao_right2">
					<p id="msg" name="msg" ></p>
					<p id="cwbordertype" name="cwbordertype"></p>
					<p id="showcwb" name="showcwb"></p>
					<p id="cwbgaojia" name="cwbgaojia" style="display: none" >高价</p>
					<p id="guanlianlantuidan" name="guanlianlantuidan" style="display: none" ><font color="red" size="12">关联揽退单</font></p>
					<p id="consigneeaddress" name="consigneeaddress"></p>
					<p id="matchDeliver" name="matchDeliver"></p>
					<p id="receiveDeliver" name="receiveDeliver"></p>
					<p id="fee" name="fee"></p>
					<p id="exceldeliverid" name="exceldeliverid"></p>
					<p id="cwbDetailshow" name="cwbDetailshow"></p>
					<p id="customercommand" name="customercommand"></p>
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
			</div>
		</div>
	</div>
	<div>
		<div class="saomiao_tab2">
			<span style="float: right; padding: 10px">
			<input  class="input_button1" type="button" name="littlefalshbutton" id="flash" value="刷新" onclick="location.href='<%=request.getContextPath() %>/PDA/branchdeliverdetail'" />
			
			</span>
			<ul id="smallTag">
				<li><a id="table_todayweilinghuo" href="#" class="light">今日未领货</a></li>
				<li><a id="table_historyweilinghuo" href="#">历史未领货</a></li>
				<li><a id="table_yilinghuo" href="#">已领货明细</a></li>
				<!-- <li><a id="table_youhuowudan" href="#">有货无单明细</a></li> -->
				<li><a href="#">异常单明细</a></li>
			</ul>
		</div>
		<div id="ViewList" class="tabbox">
			<li>
				<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(1,$("#deliverid").val());'/>
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr>
							<td width="10%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="2"
									class="table_5">
									<tr>
										<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
										<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
										<td width="140" align="center" bgcolor="#f1f1f1">到货时间</td>
										<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
										<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
										<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
										
										<td width="230" align="center" bgcolor="#f1f1f1">地址</td>
											<!-- hps_Concerto create 2016年5月25日11:57:40 -->
										<td width="60" align="center" bgcolor="#f1f1f1">订单状态</td>
										<td width="60" align="center" bgcolor="#f1f1f1">操作状态</td>
										<td align="center" bgcolor="#f1f1f1">退货出站审核结果</td>
										<!-- ******************************************** -->
									</tr>
								</table>
								<div style="height: 160px; overflow-y: scroll">
									<table id="todayweilinghuoTable" width="100%" border="0" cellspacing="1" cellpadding="2"
										class="table_2">
										<%if(todayweilinghuolist!=null&&todayweilinghuolist.size()>0)for(CwbDetailView co : todayweilinghuolist){ %>
										<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" deliverid="<%=co.getDeliverid()%>">
											<td width="120" align="center"><%=co.getCwb() %></td>
											<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
											<td width="140"><%=co.getInSitetime() %></td>
											<td width="100"><%=co.getConsigneename() %></td>
											<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
											<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView() %></td>
												<%} %>
											<td width="230" align="left"><%=co.getConsigneeaddress() %></td>
											<!-- hps_Concerto create 2016年5月25日11:57:40 -->
											<td width="60" align="center">
											<% for (CwbStateEnum  cwb   : CwbStateEnum.values()) {if (cwb.getValue()==co.getCwbstate()) {%>
											<%=cwb.getText()%>
											<% }}%>
											</td>
											<td width="60" align="center"><%
											if(CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText()=="已审核"){%>
											审核为：<%=DeliveryStateEnum.getByValue(co.getDeliverystate()).getText() %>
											<%}else if(CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText()=="已反馈") {%>
											反馈为：<%=DeliveryStateEnum.getByValue(co.getDeliverystate()).getText() %>
											<%}else{ %>
											<%=CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText()%><%} %></td>
											<td align="center"><%=co.getCheckstateresultname() %></td>
											<!-- ****************************** -->
											
										</tr>
										<%} %>
										 <%if(todayweilinghuolist!=null&&todayweilinghuolist.size()==Page.DETAIL_PAGE_NUMBER){ %>
											<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="todayweilinghuo();" id="todayweilinghuo">查看更多</td></tr>
										<%} %>
									</table>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</li>
			<li style="display: none">
				<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(4,$("#deliverid").val());'/>
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr>
							<td width="10%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="2"
									class="table_5">
									<tr>
										<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
										<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
										<td width="140" align="center" bgcolor="#f1f1f1">到货时间</td>
										<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
										<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
										<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
										<td width="230" align="center" bgcolor="#f1f1f1">地址</td>
										<!-- hps_Concerto create 2016年5月25日11:57:40 -->
										<td width="60" align="center" bgcolor="#f1f1f1">订单状态</td>
										<td width="60" align="center" bgcolor="#f1f1f1">操作状态</td>
										<td align="center" bgcolor="#f1f1f1">退货出站审核结果</td>
										<!-- ******************************************** -->
									</tr>
								</table>
								<div style="height: 160px; overflow-y: scroll">
									<table id="historyweilinghuoTable" width="100%" border="0" cellspacing="1" cellpadding="2"
										class="table_2">
										<%if(historyweilinghuolist!=null&&historyweilinghuolist.size()>0)for(CwbDetailView co : historyweilinghuolist){ %>
										<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" deliverid="<%=co.getDeliverid()%>">
											<td width="120" align="center"><%=co.getCwb() %></td>
											<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
											<td width="140"><%=co.getInSitetime() %></td>
											<td width="100"><%=co.getConsigneename() %></td>
											<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
											<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView() %></td>
												<%} %>
											<td width="230" align="left"><%=co.getConsigneeaddress() %></td>
											<!-- hps_Concerto create 2016年5月25日11:57:40 -->
											<td width="60" align="center">
											<% for (CwbStateEnum  cwb   : CwbStateEnum.values()) {if (cwb.getValue()==co.getCwbstate()) {%>
											<%=cwb.getText()%>
											<% }}%>
											</td>
											<td width="60" align="center"><%
											if(CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText()=="已审核"){%>
											审核为：<%=DeliveryStateEnum.getByValue(co.getDeliverystate()).getText() %>
											<%}else if(CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText()=="已反馈") {%>
											反馈为：<%=DeliveryStateEnum.getByValue(co.getDeliverystate()).getText() %>
											<%}else{ %>
											<%=CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText()%><%} %></td>
											<td align="center"><%=co.getCheckstateresultname() %></td>
											<!-- ****************************** -->
										</tr>
										<%} %>
										<%if(historyweilinghuolist!=null&&historyweilinghuolist.size()==Page.DETAIL_PAGE_NUMBER){ %>
											<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="historyweilinghuo();" id="historyweilinghuo">查看更多</td></tr>
										<%} %>
									</table>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</li>
			<li style="display: none">
				<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(2,$("#deliverid").val());'/>
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr>
							<td width="10%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="2"
									class="table_5">
									<tr>
										<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
										<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
										<td width="140" align="center" bgcolor="#f1f1f1">领货时间</td>
										<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
										<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
										<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
										<td width="230" align="center" bgcolor="#f1f1f1">地址</td>
										<!-- hps_Concerto create 2016年5月25日11:57:40 -->
										<td width="60" align="center" bgcolor="#f1f1f1">订单状态</td>
										<td width="60" align="center" bgcolor="#f1f1f1">操作状态</td>
										<td align="center" bgcolor="#f1f1f1">退货出站审核结果</td>
										<!-- ******************************************** -->
									</tr>
								</table>
								<div style="height: 160px; overflow-y: scroll">
									<table id="successTable" width="100%" border="0" cellspacing="1" cellpadding="2"	class="table_2">
									<%if(yilinghuolist!=null&&yilinghuolist.size()>0)for(CwbDetailView co : yilinghuolist){ %>
										<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" deliverid="<%=co.getDeliverid() %>">
											<td width="120" align="center"><%=co.getCwb() %></td>
											<td width="100" align="center"><%for(Customer c:cList){if(c.getCustomerid()==co.getCustomerid()){out.print(c.getCustomername());break;}} %></td>
											<td width="140"><%=co.getPickGoodstime() %></td>
											<td width="100"><%=co.getConsigneename() %></td>
											<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
											<%if(showCustomerSign){ %>
													<td width="100"><%=co.getRemarkView() %></td>
												<%} %>
										
											<td width="230" align="left"><%=co.getConsigneeaddress() %></td>
											<!-- hps_Concerto create 2016年5月25日11:57:40 -->
											<td width="60" align="center">
											<% for (CwbStateEnum  cwb   : CwbStateEnum.values()) {if (cwb.getValue()==co.getCwbstate()) {%>
											<%=cwb.getText()%>
											<% }}%>
											</td>
											<td width="60" align="center"><%
											if(CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText()=="已审核"){%>
											审核为：<%=DeliveryStateEnum.getByValue(co.getDeliverystate()).getText() %>
											<%}else if(CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText()=="已反馈") {%>
											反馈为：<%=DeliveryStateEnum.getByValue(co.getDeliverystate()).getText() %>
											<%}else{ %>
											<%=CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText()%><%} %></td>
											<td align="center"><%=co.getCheckstateresultname() %></td>
											<!-- ****************************** -->
										</tr>
										<%} %>
										<%if(yilinghuolist!=null&&yilinghuolist.size()==Page.DETAIL_PAGE_NUMBER){ %>
											<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="yiling();" id="yiling">查看更多</td></tr>
										<%} %>
									</table>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</li>
			
			<li style="display: none">
				<input type ="button" id="btnval0" value="导出Excel" class="input_button1" onclick='exportField(3,$("#deliverid").val());'/>
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr>
							<td width="10%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="2"
									class="table_5">
									<tr>
										<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
										<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
										<td width="140" align="center" bgcolor="#f1f1f1">到货时间</td>
										<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
										<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
										<%if(showCustomerSign){ %>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%} %>
										<td width="230" align="center" bgcolor="#f1f1f1">地址</td>
										<!-- hps_Concerto create 2016年5月25日11:57:40 -->
										<td width="60" align="center" bgcolor="#f1f1f1">订单状态</td>
										<td width="60" align="center" bgcolor="#f1f1f1">操作状态</td>
										<td align="center" bgcolor="#f1f1f1">退货出站审核结果</td>
										<!-- ******************************************** -->
										
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
	<form action="<%=request.getContextPath()%>/PDA/exportExcle" method="post" id="searchForm2">
		<input type="hidden" name="cwbs" id="cwbs" value=""/>
		<input type="hidden" name="exportmould2" id="exportmould2" />
	</form>
	<form action="<%=request.getContextPath() %>/PDA/exportByDeliverid" method="post" id="searchForm3">
		<input  type="hidden"  name="deliverid" value="<%=request.getParameter("deliverid")==null?"0":request.getParameter("deliverid") %>" id="deliverid" />
		<input type="hidden" name="type" value="" id="type"/>
	</form>
</div>
</body>
</html>