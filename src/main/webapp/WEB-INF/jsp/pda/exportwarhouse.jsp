<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum,cn.explink.enumutil.ExceptionCwbErrorTypeEnum"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page
	import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Truck,cn.explink.domain.Bale,cn.explink.domain.Switch"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer,cn.explink.domain.Reason"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbDetailView> weichukuList = (List<CwbDetailView>)request.getAttribute("weichukulist");
List<CwbDetailView> yichukuList = (List<CwbDetailView>)request.getAttribute("yichukulist");
List<Customer> cList = (List<Customer>)request.getAttribute("customerlist");

List<Branch> bList = request.getAttribute("branchlist")==null?new ArrayList<Branch>():(List<Branch>)request.getAttribute("branchlist");
List<User> uList = request.getAttribute("userList")==null?new ArrayList<User>():(List<User>)request.getAttribute("userList");
List<Truck> tList = request.getAttribute("truckList")==null?new ArrayList<Truck>():(List<Truck>)request.getAttribute("truckList");
List<Bale> balelist = request.getAttribute("balelist")==null?new ArrayList<Bale>():(List<Bale>)request.getAttribute("balelist");
//Switch ck_switch = (Switch) request.getAttribute("ck_switch");
Switch ckfb_switch = (Switch) request.getAttribute("ckfb_switch");
Map usermap = (Map) session.getAttribute("usermap");
long branchid=request.getParameter("branchid")==null?0:Long.parseLong(request.getParameter("branchid"));
boolean showCustomerSign= request.getAttribute("showCustomerSign")==null?false:(Boolean)request.getAttribute("showCustomerSign");
long isscanbaleTag= request.getAttribute("isscanbaleTag")==null?1:Long.parseLong(request.getAttribute("isscanbaleTag").toString());
int isshowzhongzhuan= request.getAttribute("isshowzhongzhuan")==null?0:Integer.parseInt(request.getAttribute("isshowzhongzhuan").toString());
String chorsezhongzhuanreason= request.getAttribute("chorsezhongzhuanreason")==null?"no":request.getAttribute("chorsezhongzhuanreason").toString();
String wavPath=request.getContextPath()+"/images/wavnums/";


List<Reason> reasonlist = request.getAttribute("reasonlist")==null?null:(List<Reason>)request.getAttribute("reasonlist");

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>分拣库出库扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/express/feedback/comm.js"></script>
<script src="<%=request.getContextPath()%>/js/datePlugin/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script type="text/javascript">
var requestContext = "<%=request.getContextPath()%>" ;
var weightTime = <%=request.getAttribute("weightTime") == null ?10:Integer.parseInt(request.getAttribute("weightTime").toString())%> ;
	function changBlur(){
		var isscanbaleTag = 1;
		if($("#scanbaleTag").hasClass("light")){
			isscanbaleTag=1;
		}else{
			isscanbaleTag=0;
		}
		window.location.href="<%=request.getContextPath() %>/PDA/exportwarhouse?branchid="+$('#branchid').combobox('getValue')+"&isscanbaleTag="+isscanbaleTag;
	}
$(function(){
	$("#branchid").combobox();
	
	//combobox控件的显示输入框绑定失去焦点事件（浏览器兼容问题，谷歌可以，火狐不行，腾出时间再看一下这个问题）
	/* $("input.combo-text.validatebox-text.validatebox-f.textbox").blur(function(){
		if($('#branchid').combobox('getValue')==undefined||$('#branchid').combobox('getValue')==0){
			if($('#branchid').combobox('getData').length>0){
				var data = $('#branchid').combobox('getData');
				$("#branchid").val(data[0].value);
				$("#branchid").combobox('select',data[0].value);
				$("input.combo-text.validatebox-text.validatebox-f.textbox").val(data[0].text);
				changBlur();
			}

		}else{
			changBlur();
		}
	}) */

	if('${isOpenDialog}'=='open'){
		$('#find').dialog('close');
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
	
})

	$(function(){
		getOutSum('<%=request.getContextPath()%>',$("#branchid").combobox("getValue"));
		getcwbsquejiandataForBranchid($("#branchid").combobox("getValue"));
		$("#scancwb").focus();
	});
function closeDialog(){
	$('#find').dialog('close');
	$("#scancwb").focus();
}

	
	
$(function() {
	var $menuli = $(".uc_midbg ul li");
	$menuli.click(function() {
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
});

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
	function getOutSum(pname,branchid){
		$.ajax({
			type: "POST",
			url:pname+"/PDA/getOutSum?nextbranchid="+branchid,
			dataType:"json",
			success : function(data) {
				$("#chukukucundanshu").html(data.weichukucount);
				$("#chukukucunjianshu").html(data.weichukusum);
				$("#singleoutnum").html(data.yichukucount);
			}
		});
		//未出库明细、已出库明细、异常单明细只显示该下一站明细
		/* if(branchid>0){
			$("#weichukuTable tr").hide();
			$("#weichukuTable tr[nextbranchid='"+branchid+"']").show();
			
			$("#successTable tr").hide();
			$("#successTable tr[nextbranchid='"+branchid+"']").show();
			
			$("#errorTable tr").hide();
			$("#errorTable tr[nextbranchid='"+branchid+"']").show();
		}else{
			$("#weichukuTable tr").show();
			$("#successTable tr").show();
			$("#errorTable tr").show();
		} */
	}
	
	//得到出库缺货件数的统计
	function getcwbsquejiandataForBranchid(nextbranchid) {
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/PDA/getOutQueSum",
			data : {
				"nextbranchid" : nextbranchid
			},
			dataType : "json",
			success : function(data) {
				$("#lesscwbnum").html(data.lesscwbnum);
			}
		});
	}
	
	//得到出库缺货件数的list列表
	function getchukucwbquejiandataList(nextbranchid){
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/PDA/getOutQueList",
			data : {
				"nextbranchid" : nextbranchid
			},
			dataType : "html",
			success : function(data) {
				$("#lesscwbTable").html(data);
			}
		});
		
	}
/**
 * 出库扫描
 */
var branchStr=[];
var Cwbs="";

/**
 * 输入单号后，
 */
 function exportWarehouse(pname,scancwb,branchid,driverid,truckid,requestbatchno,baleno,ck_switch,confirmflag){
		var needWeightFlag = jQuery("#needWeightFlag").attr("checked") ;
	    var carrealweight = 0 ;
	    if(needWeightFlag == undefined){
	    	exportWarehouseForWeight(pname,scancwb,branchid,driverid,truckid,requestbatchno,baleno,ck_switch,confirmflag,carrealweight);
	    	return ;
	    }
	    $("#msg").html("") ;
	    $("#showcwb").html("") ;
	    $("#excelbranch").html("") ;
	    $("#carweightDesc").html("") ;
	    $("#cwbDetailshow").html("") ;
	    jQuery("#weightNotice").text("正在称重中,请稍等......") ;
	    var weightIntervalId = window.setInterval("setWeight()", 1);
	    window.setTimeout(function waitForWeight(){
	    	carrealweight = jQuery("#weightSpan").text(); // 获取电子秤重量
	    	window.clearInterval(weightIntervalId) ;
	    	if(carrealweight == undefined || parseFloat(carrealweight) <= 0){
	    		jQuery("#weightNotice").text("") ;
	    		jQuery("#msg").html(scancwb + "(获取不到重量)，请手动输入重量！") ;
	    		jQuery("#orderWeight").attr("disabled" , false) ;
	    		jQuery("#orderWeight").focus() ;
	        	return false ;
	    	}
	    exportWarehouseForWeight(pname,scancwb,branchid,driverid,truckid,requestbatchno,baleno,ck_switch,confirmflag,carrealweight);
	},(weightTime + 0.1) * 1000) ;
}

function exportWarehouseForWeight(pname,scancwb,branchid,driverid,truckid,requestbatchno,baleno,ck_switch,confirmflag,carrealweight){
	var needWeightFlag = jQuery("#needWeightFlag").attr("checked") ;
	if(scancwb.length>0){
		if("${isOpenDialog}" != "open"){
			if(scancwb.indexOf("@zd_")>-1){
			$("#branchid").combobox('setValue',scancwb.split('_')[1]);
			if($("#branchid").combobox("getValue")!=scancwb.split('_')[1]){
				$("#msg").html("         （异常扫描）扫描站点失败");
				$("#branchid").combobox('setValue',0);
			}else{
				$("#msg").html("");
			}
			$("#scancwb").val("");
			$("#scancwb").focus() ;
			return false;
		}
		if($("#scanbaleTag").attr("class")=="light"){//出库根据包号扫描订单
			baleaddcwbCheck();
		}else{//出库
			
				if('<%=isshowzhongzhuan%>'=='1'&&'<%=chorsezhongzhuanreason%>'=='yes'){
					if($("#reasonid").val()==0){
						alert('请选择中转原因!');
						return;
					}
					
				}
				$.ajax({
					type: "POST",
					url:pname+"/PDA/cwbexportwarhouse/"+scancwb+"?branchid="+branchid+"&driverid="+driverid+"&truckid="+truckid+"&confirmflag="+confirmflag+"&requestbatchno="+requestbatchno+"&baleno="+baleno,
					dataType:"json",
					success : function(data) {
						jQuery("#weightSpan").text("0.00") ;
						jQuery("#weightNotice").text("") ;
						$("#scancwb").val("");
						$("#scancwb").focus() ;
						if(data.statuscode=="000000"){
							if(data.body.packageCode!=""){
								$("#msg").html(data.body.packageCode+"　（"+data.errorinfo+"）");
								if(data.body.cwbOrder.scannum==1){
									if(Cwbs.indexOf("|"+scancwb+"|")==-1){
										Cwbs += "|"+scancwb+"|";
										/*$("#singleoutnum").html(parseInt($("#singleoutnum").html())+1);
										$("#alloutnum").html(parseInt($("#alloutnum").html())+1);
										branchStr[data.body.cwbOrder.nextbranchid] = $("#singleoutnum").html(); */
									}
									//getOutSum(pname,data.body.cwbOrder.nextbranchid);
									//getcwbsquejiandataForBranchid(data.body.cwbOrder.nextbranchid);
									//getchukucwbquejiandataList(data.body.cwbOrder.nextbranchid);
									//将成功扫描的订单放到已入库明细中
									//addAndRemoval(data.body.cwbOrder.cwb,"successTable",true,$("#branchid").combobox("getValue"));
								}
								if(data.body.cwbOrder.sendcarnum>1){
									numbervedioplay("<%=request.getContextPath()%>",data.body.successCount);
									//document.ypdj.play();
								}
								if(data.body.cwbbranchnamewav!=""&&data.body.cwbbranchnamewav!=pname+"/wav/"){
									numbervedioplay("<%=request.getContextPath()%>",data.body.successCount);
									//$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random());
								}else{
									$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
									numbervedioplay("<%=request.getContextPath()%>",data.body.successCount);
								}
							}else{
								$("#branchid").combobox('setValue',data.body.cwbOrder.nextbranchid);
								if(data.body.cwbOrder.scannum==1){
									if(Cwbs.indexOf("|"+scancwb+"|")==-1){
										Cwbs += "|"+scancwb+"|";
										/* $("#singleoutnum").html(parseInt($("#singleoutnum").html())+1);
										$("#alloutnum").html(parseInt($("#alloutnum").html())+1);
										branchStr[data.body.cwbOrder.nextbranchid] = $("#singleoutnum").html(); */
									}
									//getOutSum(pname,data.body.cwbOrder.nextbranchid);
									//getcwbsquejiandataForBranchid(data.body.cwbOrder.nextbranchid);
									//getchukucwbquejiandataList(data.body.cwbOrder.nextbranchid);
									
									//将成功扫描的订单放到已入库明细中
									//addAndRemoval(data.body.cwbOrder.cwb,"successTable",true,$("#branchid").combobox("getValue"));
								}
								
								$("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
								$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
								$("#scansuccesscwb").val(scancwb);
								$("#showcwb").html("订 单 号："+data.body.cwbOrder.cwb);
								if(data.body.showRemark!=null){
								$("#cwbDetailshow").html("订单备注："+data.body.showRemark);
								}
								//getOutSum(pname,data.body.cwbOrder.nextbranchid);
								//getcwbsquejiandataForBranchid(data.body.cwbOrder.nextbranchid);
								//getchukucwbquejiandataList(data.body.cwbOrder.nextbranchid);
								
								//将成功扫描的订单放到已入库明细中
								//addAndRemoval(data.body.cwbOrder.cwb,"successTable",true,$("#branchid").combobox("getValue"));
								
								//if(data.body.cwbOrder.sendcarnum>1){
								//	document.ypdj.play();
								//}
								//if(data.body.cwbbranchnamewav!=""&&data.body.cwbbranchnamewav!=pname+"/wav/"){
								//	$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random());
								//}else{
								//	$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
								//} 
								}
						}else if(data.statuscode=="0001"){
							$("#excelbranch").html("");
							$("#showcwb").html("");
							$("#msg").html(data.errorinfo);
							addAndRemoval(scancwb,"errorTable",false,$("#branchid").val());
						}else{
							$("#excelbranch").html("");
							$("#showcwb").html("");
							$("#msg").html(scancwb+"         （异常扫描）"+data.errorinfo);
							addAndRemoval(scancwb,"errorTable",false,$("#branchid").combobox("getValue"));
							//errorvedioplay(pname,data);
						}
						if(data.hasOwnProperty('newCarrealWeight') && data.body.newCarrealWeight != undefined){
							$("#carweightDesc").html("重量(Kg):" + data.body.newCarrealWeight+"<br/>") ;
						}else{
							$("#carweightDesc").html("") ;
						}
						$("#responsebatchno").val(data.responsebatchno);
						batchPlayWav(data.wavList);
					}
				});
			}
		}else{
				if(scancwb.indexOf("@zd_")>-1){
					$("#branchid").combobox('setValue',scancwb.split('_')[1]);
					if($("#branchid").combobox("getValue")!=scancwb.split('_')[1]){
						$("#msg1").html("         （异常扫描）扫描站点失败");
						$("#branchid").combobox('setValue',0);
						$('#find').dialog('open');
						$("#scancwb").blur();
					}else{
						$("#msg1").html("");
					}
					$("#scancwb").val("");
					return false;
				}
				if($("#scanbaleTag").attr("class")=="light"){//出库根据包号扫描订单
					baleaddcwbCheck();
				}else{//出库
						if('<%=isshowzhongzhuan%>'=='1'&&'<%=chorsezhongzhuanreason%>'=='yes'){
							if($("#reasonid").val()==0){
								alert('请选择中转原因!');
								return;
							}
							
						}
				        var openDialonPDAUrl = pname+"/PDA/cwbexportwarhouse/"+scancwb+"?branchid="+branchid+"&driverid="+driverid+"&truckid="+truckid+"&confirmflag="+confirmflag+"&requestbatchno="+requestbatchno+"&baleno="+baleno ;
				        if(needWeightFlag == "checked"){
				        	openDialonPDAUrl = pname+"/PDA/cwbExportWarhouseWeight/"+scancwb+"?branchid="+branchid+"&driverid="+driverid+"&truckid="+truckid+"&confirmflag="+confirmflag+"&requestbatchno="+requestbatchno+"&baleno="+baleno+"&carrealweight="+carrealweight ; 
				        }
						$.ajax({
							type: "POST",
							url:openDialonPDAUrl,
							dataType:"json",
							success : function(data) {
								jQuery("#weightSpan").text("0.00") ;
								jQuery("#weightNotice").text("") ;
								$("#scancwb").val("");
								$("#scancwb").focus() ;
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
										$("#branchid").combobox('setValue',data.body.cwbOrder.nextbranchid);
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
									if(data.body.newCarrealWeight != undefined){
										$("#carweightDesc").html("重量(Kg):" + data.body.newCarrealWeight+"<br/>") ;
									}else{
										$("#carweightDesc").html("") ;
									}
								}
								else if(data.statuscode=="0001"){
									$("#excelbranch").html("");
									$("#carweightDesc").html("") ;
									$("#showcwb").html("");
									$("#msg1").html(data.errorinfo);
									$('#find').dialog('open');
									$("#scancwb").blur();
									addAndRemoval(scancwb,"errorTable",false,$("#branchid").val());
								}else if(data.statuscode=="0002"){
									$("#excelbranch").html("");
									$("#carweightDesc").html("") ;
									$("#showcwb").html("");
									$("#msg1").html(data.errorinfo);
									$('#find').dialog('open');
									$("#scancwb").blur();
									addAndRemoval(scancwb,"errorTable",false,$("#branchid").val());
									//errorvedioplay(pname,data);
								}else{
									$("#excelbranch").html("");
									$("#carweightDesc").html("") ;
									$("#showcwb").html("");
									$("#msg1").html(scancwb+"         （异常扫描）"+data.errorinfo);
									$('#find').dialog('open');
									$("#scancwb").blur();
									addAndRemoval(scancwb,"errorTable",false,$("#branchid").combobox("getValue"));
									//errorvedioplay(pname,data);
								}
								jQuery("#orderWeight").val("") ;
								jQuery("#orderWeight").attr("disabled" , true) ;
								$("#responsebatchno").val(data.responsebatchno);
								batchPlayWav(data.wavList);
							}
						});
				}
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
	$("#carweightDesc").html("") ;
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
		/* if(branchid>0){
			$("#errorTable tr[nextbranchid='"+branchid+"']").each(function(){
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
		$("#type").val("ypdj");
		$("#searchForm3").submit();
	}
	
}

var weipage=1;var yipage=1;var asc=0;
function weichuku(){
	weipage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getexportweichukulist",
		data:{"page":weipage,"branchid":$("#branchid").combobox("getValue")},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					<%if(showCustomerSign){ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' nextbranchid='"+data[i].nextbranchid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'> "+data[i].packagecode+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%}else{ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' nextbranchid='"+data[i].nextbranchid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'> "+data[i].packagecode+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%} %>
				}
				$("#weichuku").remove();
				$("#weichukuTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
				var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="weichuku();" id="weichuku">查看更多</td></tr>';
				$("#weichukuTable").append(more);
				}
			}
		}
	});
	
	
}
function orderbyweichuku(type){
	asc+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/orderbyweichuku",
		data:{"asc":asc,"branchid":$("#branchid").combobox("getValue"),"orderby":type},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					<%if(showCustomerSign){ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' nextbranchid='"+data[i].nextbranchid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'> "+data[i].packagecode+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%}else{ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' nextbranchid='"+data[i].nextbranchid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'> "+data[i].packagecode+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%} %>
				}
				$("#weichuku").remove();
				$("#weichukuTable").empty();
				$("#weichukuTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
				var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="weichuku();" id="weichuku">查看更多</td></tr>';
				$("#weichukuTable").append(more);
				}
			}
		}
	});
	
	
}

function yichuku(){
	yipage+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/getexportyichukulist",
		data:{"page":yipage,
			"branchid":$("#branchid").combobox("getValue"),
			"flowordertype":<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue()%>},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					<%if(showCustomerSign){ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' nextbranchid='"+data[i].nextbranchid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'> "+data[i].packagecode+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%}else{ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' nextbranchid='"+data[i].nextbranchid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'> "+data[i].packagecode+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%} %>
				}
				$("#yichuku").remove();
				$("#successTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
				var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="yichuku();" id="yichuku">查看更多</td></tr>'
				$("#successTable").append(more);
				}
			}
		}
	});
}
function orderbyyichuku(type){
	asc+=1;
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/PDA/orderbyyichuku",
		data:{"orderby":type,
			"asc":asc,
			"branchid":$("#branchid").combobox("getValue"),
			"flowordertype":<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue()%>},
		success:function(data){
			if(data.length>0){
				var optionstring = "";
				for ( var i = 0; i < data.length; i++) {
					<%if(showCustomerSign){ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' nextbranchid='"+data[i].nextbranchid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'> "+data[i].packagecode+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td width='100' align='center'> "+data[i].remarkView+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%}else{ %>
						optionstring += "<tr id='TR"+data[i].cwb+"'  cwb='"+data[i].cwb+"' customerid='"+data[i].customerid+"' nextbranchid='"+data[i].nextbranchid+"' >"
						+"<td width='120' align='center'>"+data[i].cwb+"</td>"
						+"<td width='100' align='center'> "+data[i].packagecode+"</td>"
						+"<td width='100' align='center'> "+data[i].customername+"</td>"
						+"<td width='140' align='center'> "+data[i].emaildate+"</td>"
						+"<td width='100' align='center'> "+data[i].consigneename+"</td>"
						+"<td width='100' align='center'> "+data[i].receivablefee+"</td>"
						+"<td  align='left'> "+data[i].consigneeaddress+"</td>"
						+ "</tr>";
					<%} %>
				}
				$("#yichuku").remove();
				$("#successTable").empty();
				$("#successTable").append(optionstring);
				if(data.length==<%=Page.DETAIL_PAGE_NUMBER%>){
				var more='<tr align="center"  ><td  colspan="<%if(showCustomerSign){ %>7<%}else{ %>6<%} %>" style="cursor:pointer" onclick="yichuku();" id="yichuku">查看更多</td></tr>'
				$("#successTable").append(more);
				}
			}
		}
	});
}
<%-- function tohome(){
	var isscanbaleTag = 1;
	if($("#scanbaleTag").hasClass("light")){
		isscanbaleTag=1;
	}else{
		isscanbaleTag=0;
	}
	window.location.href="<%=request.getContextPath() %>/PDA/exportwarhouse?branchid="+$("#branchid").combobox("getValue")+"&isscanbaleTag="+isscanbaleTag;
	
} --%>

function ranCreate(){
	if($("#branchid").combobox("getValue")==0){
		alert('请选择下一站');
		return;
	}
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

$(function(){
	if(<%=isscanbaleTag==1%> ){
		$("#scanbaleTag").click();
		//$("#scancwbTag").removeClass("light");
	}else{
		//$("#scanbaleTag").removeClass("light");
		$("#scancwbTag").click();
	}
});
///播放声音文件开始//////
//调用方法  playWav('12345')
function playWav(str){
	var strSplit=str.split("");
	for(var i=0;i<strSplit.length;i++) {
		(function(i){
			setTimeout(function(){
				numbervedioplay("<%=request.getContextPath()%>",strSplit[i]);
			},400*i);
		})(i);
		  <%-- (function(i){
		    setTimeout(function(){
		    	url="<%=wavPath%>"+strSplit[i]+".wav";
		    	var src="<EMBED id='wav' name='wav' src='"+url+"' LOOP=false AUTOSTART=true MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>"+
		    	"<div id='FlashMusicBox' class='FlashMusicBox' style='position:absolute; overflow:hidden; left:-10000px; top:-10000px; width:10px; height:10px; border:solid 1px #F00;'>"+
		    	  "<object class='FlashMusic' classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' codebase='http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0' width='990' height='222'>"+
		    		"<param id='movie' name='movie' value='"+url+"'/><param name='quality' value='high' /><param name='wmode' value='transparent' />  <param name='LOOP' value='0' />"+ 
		    		"<embed class='FlashMusic' src='"+url+"' quality='high' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash' width='990' height='222' wmode='transparent' loop='-1' ></embed>"+
		    	"</object></div>"; 
		    	$("#emb").html(src);
		    	
		    	/* document.getElementById('wav').play(); */
		    	setTimeout("bofang()",200);
		  },400*i);
		 })(i); --%>
	}
}

function bofang(){
	document.getElementById('wav').play();
} 
///播放声音文件结束//////
	
	
//=============出库根据包号扫描订单检查===============
function baleaddcwbCheck(){
	var branchid = $("#branchid").combobox("getValue") ;
	if($("#branchid").combobox("getValue")==0){
		alert("请选择下一站！");
		return;
	}
	if($("#baleno").val()==""){
		alert("包号不能为空！");
		$("#baleno").focus() ;
		return;
	}
	if($("#scancwb").val()==""){
		alert("订单号不能为空！");
		$("#scancwb").focus() ;
		return;
	}
	var confirmflag=$("#confirmflag").attr("checked")=="checked"?1:0;
	var scancwb = $("#scancwb").val().trim() ;
	var baleno = $("#baleno").val().trim() ;
	var url = requestContext + "/bale/baleaddcwbCheck/" + scancwb + "/" + baleno + "?flag=1&branchid=" + branchid + "&confirmflag=" + confirmflag ;
	$.ajax({
   		type: "POST",
   		url : url ,
   		dataType : "json",
   		success : function(data) {
   			$("#msg").html("");
   			$("#scancwb").focus() ;
   			if(data.body.errorcode=="111111"){
   				jQuery("#weightSpan").text("0.00") ;
   				jQuery("#weightNotice").text("") ;
   				$("#carweightDesc").html("") ;
   				$("#orderWeight").val("") ;
   				jQuery("#orderWeight").attr("disabled" , true) ;
   				if(data.body.errorenum=="Bale_ChongXinFengBao"){//此订单已在包号：XXX中封包，确认要重新封包吗?
   					/* if(confirm(data.body.errorinfo)){
   						baleaddcwb();//出库根据包号扫描订单
   					} */
   				}else{
   					$("#scancwb").val("");
   					if("${isOpenDialog}" != "open"){
		   				$("#msg").html("（异常扫描）"+data.body.errorinfo);
   					}else{
   						$("#msg1").html("（异常扫描）"+data.body.errorinfo);
   						$('#find').dialog('open');
   						$("#scancwb").blur();
   					}
	   				errorvedioplay("<%=request.getContextPath()%>",data);
   				}
   				return;
   			}
   			baleaddcwb(data.body.scancwb,data.body.baleno);//出库根据包号扫描订单
   		}
   	});
}
//=============出库根据包号扫描订单===============	
function baleaddcwb(scancwb,baleno){
	if($("#baleno").val()==""){
		alert("包号不能为空！");
		$("#scancwb").val("");
		$("#scancwb").focus() ;
		return;
	}
/* 	if($("#baleno").val()!=baleno||$("#scancwb").val()!=scancwb){
		
		return;
	} */
	var branchid = $("#branchid").combobox("getValue") ;
	var url = requestContext +"/bale/baleaddcwb/" + scancwb + "/" + baleno + "?branchid=" + branchid ;
	var needWeightFlag = jQuery("#needWeightFlag").attr("checked") ;
	if(needWeightFlag == "checked"){
		var carrealweight = jQuery("#weightSpan").text() ; // 获取电子秤重量
    	if(carrealweight == undefined || parseFloat(carrealweight) <= 0){
    		carrealweight = jQuery("#orderWeight").val().trim() ;
    	}
    	url = requestContext +"/bale/baleaddcwb/" + scancwb + "/" + baleno + "?branchid=" + branchid  + "&carrealweight=" + carrealweight;
	}
	$.ajax({
		type: "POST",
		url : url ,
		dataType : "json",
		success : function(data) {
			$("#msg").html("");
			$("#scancwb").val("");
			$("#scancwb").focus() ;
			$("#orderWeight").val("") ;
			jQuery("#orderWeight").attr("disabled" , true) ;
			jQuery("#weightSpan").text("0.00") ;
			jQuery("#weightNotice").text("") ;
			if(data.body.errorcode=="000000"){
				$("#msg").html("（扫描成功）"+$("#baleno").val()+"包号共"+data.body.successCount+"单,共"+data.body.scannum+"件");
				<%-- numbervedioplay("<%=request.getContextPath()%>",data.body.successCount); --%>
				playWav(""+data.body.successCount);
			}else{
				if("${isOpenDialog}" != "open"){
	   				$("#msg").html("（异常扫描）"+data.body.errorinfo);
				}else{
					$("#msg1").html("（异常扫描）"+data.body.errorinfo);
					$('#find').dialog('open');
					$("#scancwb").blur();
				}
				errorvedioplay("<%=request.getContextPath()%>",data);
			}
			if(data.body.newCarrealWeight != undefined &&  needWeightFlag == "checked"){
				$("#carweightDesc").html("重量(Kg):" + data.body.newCarrealWeight+"<br/>") ;
			}else{
				$("#carweightDesc").html("") ;
			}
		}
	});
}

//=============封包按钮===============	
function fengbao(){
	if($("#baleno").val()==""){
		alert("包号不能为空！");
		return;
	}
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/bale/fengbao/"+$("#baleno").val()+"?branchid="+$("#branchid").combobox("getValue"),
		dataType : "json",
		success : function(data) {
			$("#msg").html("");
			if(data.body.errorcode=="000000"){
				$("#msg").html($("#baleno").val()+"包号封包成功！");
				successvedioplay("<%=request.getContextPath()%>",data);
			}else{
				if("${isOpenDialog}" != "open"){
					$("#msg").html("（封包异常）"+data.body.errorinfo);
				}else{
					$("#msg1").html("（异常扫描）"+data.body.errorinfo);
					$('#find').dialog('open');
					$("#scancwb").blur();
				}
				errorvedioplay("<%=request.getContextPath()%>",data);
			}
			$("#scancwb").val("");
			$("#scancwb").focus() ;
			
		}
	});
}

//=============按包出库按钮===============	
function chuku(){
	if($("#baleno").val()==""){
		alert("包号不能为空！");
		return;
	}
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/bale/balechuku/"+$("#baleno").val()+"?branchid="+$("#branchid").combobox("getValue")+"&driverid="+$("#driverid").val()+"&truckid="+$("#truckid").val(),
		dataType : "json",
		success : function(data) {
			$("#msg").html("");
			$("#msg").html(data.body.errorinfo);
			$("#errorTable").html("");
			$("#carweightDesc").html("") ;
			if(data.body.errorListView!=null){
	 			$.each(data.body.errorListView, function(key, value) {
	 				var tr = document.getElementById("errorTable").insertRow();
					tr.id="error_"+key;
					var td = tr.insertCell(0);
					td.setAttribute("width",120);
					td.innerHTML = value.cwb;
					var td = tr.insertCell(1);
					td.setAttribute("width",100);
					td.innerHTML = value.customername;
					var td = tr.insertCell(2);
					td.setAttribute("width",140);
					td.innerHTML = value.emaildate;
					var td = tr.insertCell(3);
					td.setAttribute("width",100);
					td.innerHTML = value.consigneename;
					var td = tr.insertCell(4);
					td.setAttribute("width",100);
					td.innerHTML = value.receivablefee;
					var td = tr.insertCell(5);
					td.setAttribute("width",100);
					td.innerHTML = value.cwbremark;
					var td = tr.insertCell(6);
					td.setAttribute("width",200);
					td.innerHTML = value.consigneeaddress;
					
					var td = tr.insertCell(7);
					td.innerHTML = value.errorreasion;
			 	});
	 			
	 			
			}
			
			$("#scancwb").val("");
			$("#scancwb").focus() ;
			$("#baleno").val("");
			
			if(data.body.errorListView!=null){
				errorvedioplay("<%=request.getContextPath()%>",data);
			}else{
				successvedioplay("<%=request.getContextPath()%>",data);
			}
			
		}
	});
}

function setWeight() {
	try{
		var weight = window.parent.document.getElementById("scaleApplet").getWeight();
        if (weight != null && weight != '') {
	       document.getElementById("weightSpan").innerHTML = weight;
         }
	}catch(e){
		jQuery("#weightSpan").text("0.00") ;
		jQuery("#weightNotice").text("实际重量为空，检查电子称！"); 
	}
}

function setNeedWeight(){
	var needWeightFlag = jQuery("#needWeightFlag").attr("checked") ;
	jQuery("#orderWeight").val("") ;
	jQuery("#orderWeight").attr("disabled" , true) ;
	jQuery("#weightSpan").text("0.00") ;
	jQuery("#weightNotice").text("") ;
	
}
/**
 * 校验手动输入货物重量
 */
function saveOrderWeight(keyCode){
	if(keyCode != 13){
		return ;
	}
	var orderWeight = jQuery("#orderWeight").val().trim() ;
	var weightExp = /^[1-9]\d*(\.\d*)?|0\.\d*[1-9]\d*$/ ;
	if(!weightExp.test(orderWeight)){
		alert("请输入重量");
		jQuery("#orderWeight").focus() ;
		return ;
	}
	var scancwb = jQuery("#scancwb").val().trim() ;
	if(scancwb.length <= 0){
		alert("请输入订单号/运单号") ;
		jQuery("#scancwb").focus() ;
		return ;
	}
	jQuery("#msg").html("") ;
	var branchid = $("#branchid").combobox("getValue") ;
	var driverid = $("#driverid").val() ;
	var truckid = $("#truckid").val() ;
	var requestbatchno = $("#requestbatchno").val() ;
	var baleno = $("#baleno").val() ;
	var ck_switch = $("#ck_switch").val() ;
	var confirmflag = $("#confirmflag").attr("checked")=="checked"?1:0 ;
	exportWarehouseForWeight(requestContext,scancwb,branchid,driverid,truckid,requestbatchno,baleno,ck_switch,confirmflag,orderWeight) ;
	
}
</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div id="emb"></div>
<div class="saomiao_box2">
	<div class="saomiao_tab2">
		<ul>
			<li><a href="#"  class="light">逐单操作</a></li>		
			<li><a href="<%=request.getContextPath()%>/PDA/exportwarhouseBatch">批量操作</a></li>
		</ul>
	</div>
	
	<div class="saomiao_topnum2">
		<dl class="blue">
			<dt>待出库</dt>
			<dd style="cursor:pointer" onclick="tabView('table_weichuku')"  id="chukukucundanshu">0</dd>
		</dl>
		<dl class="yellow">
			<dt>待出库件数合计</dt>
			<dd id="chukukucunjianshu">0</dd>
		</dl>
		<dl class="green">
			<dt>本次已出库</dt>
			<dd style="cursor:pointer" onclick="tabView('table_yichuku')" id="singleoutnum">0</dd>
		</dl>
		<dl class="yellow">
			<dt>一票多件缺货件数</dt>
				<dd style="cursor: pointer"
					onclick="tabView('table_quejian');getchukucwbquejiandataList($('#branchid').combobox('getValue'));"
					id="lesscwbnum" name="lesscwbnum">0</dd>
		</dl>
			<input type="button" id="refresh" value="刷新"
				onclick="location.href='<%=request.getContextPath()%>/PDA/exportwarhouse'"
				style="float: left; width: 100px; height: 65px; cursor: pointer; border: none; background: url(../images/buttonbgimg1.gif) no-repeat; font-size: 18px; font-family: '微软雅黑', '黑体'" />
		<br clear="all"/>
	</div>
	
	<div class="saomiao_info2">
		<div class="saomiao_inbox2">
			<div class="saomiao_tab">
				<ul id="bigTag">
						<li><a href="#" id="scancwbTag"
							onclick="clearMsg();$(function(){$('#baleno').parent().hide();$('#finish').parent().hide();$('#baleno').val('');$('#scancwb').val('');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();$('#baleBtn').hide();})"
							class="light">扫描订单</a></li>
						<%
							if (ckfb_switch.getId() != 0 && ckfb_switch.getState().equals(SwitchEnum.ChuKuFengBao.getInfo())) {
						%>
						<li><a href="#" id="scanbaleTag"
							onclick="clearMsg();$(function(){$('#baleno').parent().show();$('#baleno').show();$('#finish').parent().show();$('#finish').show();$('#baleno').val('');$('#baleno').focus();$('#scancwb').val('');$('#scancwb').parent().hide();$('#baleBtn').show();})">合包出库</a></li>
						<%
							}
						%>
				</ul>
			</div>
			<div class="saomiao_righttitle2" id="pagemsg"></div>
			<!-- <form action="" method="get"> -->
			<div class="saomiao_selet2">
					下一站：
					 <select id="branchid" name="branchid" style="width: 150px" onChange="changBlur();">
					<option value="0" selected>请选择</option>
						<%
							for (Branch b : bList) {
						%>
						<option value="<%=b.getBranchid()%>" <%if (branchid == b.getBranchid()) {%> selected=selected
							<%}%>><%=b.getBranchname()%></option>
						<%
							}
						%>
					</select> 
					驾驶员： 
					<select id="driverid" name="driverid" class="select1">
					<option value="-1" selected>请选择</option>
						<%
							for (User u : uList) {
						%>
						<option value="<%=u.getUserid() %>" ><%=u.getRealname() %></option>
						<%
							}
						%>
					</select> 
					车辆： 
					<select id="truckid" name="truckid" class="select1">
					<option value="-1" selected>请选择</option>
						<%
							for (Truck t : tList) {
						%>
						<option value="<%=t.getTruckid() %>" ><%=t.getTruckno() %></option>
						<%
							}
						%>
		        </select>
					<%
						if (isshowzhongzhuan == 1) {
					%>
					中转原因： <select name="reasonid" id="reasonid" class="select1">
			        	<option value ="0">==请选择==</option>
						<%
							if (reasonlist != null && reasonlist.size() > 0)
									for (Reason r : reasonlist) {
						%>
	           				<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent() %></option>
						<%
							}
						%>
			        </select>
					<%
						}
					%>
			</div>
			<div class="saomiao_inwrith2">
				<div class="saomiao_left2">
						<%
							if (Long.parseLong(usermap.get("isImposedOutWarehouse").toString()) == 1) {
						%>
					<p>
						<span>强制出库:</span><input type="checkbox" id="confirmflag" name="confirmflag" value="1"/>
					</p>
						<%
							}
						%>
					<p>
						<span>称重:</span><input type="checkbox" id="needWeightFlag" name="needWeightFlag" onclick = "setNeedWeight()" />
					</p>
					<p style="display: none;">
							<span>包号：</span><input type="text" class="saomiao_inputtxt2" name="baleno" id="baleno"
								onKeyDown="if(event.keyCode==13&&$(this).val().length>0){if($(this).val().indexOf('@zd_')>-1){$('#branchid').combobox('setValue',$(this).val().split('_')[1]);if($('#branchid').combobox('getValue')!=$(this).val().split('_')[1]){$('#msg').html('         （异常扫描）扫描站点失败');$('#branchid').combobox('setValue',0);}else{$('#msg').html('');}$(this).val('');return false;}if($('#branchid').val()==0){alert('请选择下一站');return;}$(this).attr('readonly','readonly');$('#scancwb').parent().show();$('#scancwb').show();$('#cwbPackageNoSpan').text('订单号：');$('#scancwb').focus();}" />
							<span>&nbsp;</span> <input type="button" id="randomCreate" value="随机生成" class="button"
								onclick="ranCreate();" /> <input type="button" id="handCreate" value="手工输入" class="button"
								onclick="hanCreate();" />
					
					</p>
						<p>
							<span id="cwbPackageNoSpan">单/包号：</span> <input type="text" class="saomiao_inputtxt2"
								value="" id="scancwb" name="scancwb"
								onKeyDown='if(event.keyCode==13&&$(this).val().length>0){exportWarehouse("<%=request.getContextPath()%>",$(this).val(),$("#branchid").combobox("getValue"),$("#driverid").val(),$("#truckid").val(),$("#requestbatchno").val(),$("#baleno").val(),$("#ck_switch").val(),$("#confirmflag").attr("checked")=="checked"?1:0);}' />
						</p>
						<p id="baleBtn">
							<span>&nbsp;</span>
							<!-- <input type="submit" name="finish" id="finish" value="完成扫描" class="button" onclick="$('#baleno').removeAttr('readonly');$('#baleno').val('');"/> -->
							<input type="button" name="fengbao" id="fengbao" value="封包" class="button"
								onclick="fengbao()" /> <input type="button" name="chuku" id="chuku" value="出库"
								class="button" onclick="chuku()" />
						</p>
					<p>
					     <span style = "width:90px;">实际重量(Kg)：</span>
					     <lable id="weightSpan">0.00</lable>
					     <label id="weightNotice" > </label> 
					</p>
					<p>
						<span>重量(Kg):</span><input type="text" class="saomiao_inputtxt1" id="orderWeight" name="orderWeight" disabled = "true"  onKeyDown = "saveOrderWeight(event.keyCode)" maxlength = "7" />
					</p>
					</div>
					<c:if test="${isOpenDialog=='open'}">
						<div id="find" class="easyui-dialog" data-options="modal:true" title="提示信息"
							style="width: 400px; height: 200px;">
							<div class="saomiao_right2">
								<p id="msg1" name="msg1"></p>
								<p id="showcwb" name="showcwb"></p>
								<p id="excelbranch" name="excelbranch" ></p>
								<p id="carweightDesc" name="carweightDesc" ></p>
								<p id="cwbDetailshow" name="cwbDetailshow" ></p>
									<div style="display: none" id="EMBED"></div>
								<div style="display: none">
										<EMBED id='ypdj' name='ypdj'
											SRC='<%=request.getContextPath()%><%=ServiceUtil.waverrorPath%><%=CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl()%>'
											LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
								</div>
									<div style="display: none" id="errorvedio"></div>
								</div>
							<div  align="center" valign="bottom" style="margin-top:100px;">
					         	<input type="button" class="input_button1" value="关闭" onclick="closeDialog();"/>
				         	</div>
				 	</div>
			 	</c:if>
			 
				
				
				<div class="saomiao_right2">
					<p id="msg" name="msg" ></p>
					<p id="showcwb" name="showcwb"></p>
					<p id="excelbranch" name="excelbranch" ></p>
					<p id="carweightDesc" name = "carweightDesc"/>
					<p id="cwbDetailshow" name="cwbDetailshow" ></p>
						<div style="display: none" id="EMBED"></div>
					<div style="display: none">
							<EMBED id='ypdj' name='ypdj'
								SRC='<%=request.getContextPath()%><%=ServiceUtil.waverrorPath%><%=CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl()%>'
								LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
					</div>
						<div style="display: none" id="errorvedio"></div>
					</div>
					<input type="hidden" id="requestbatchno" name="requestbatchno" value="0" /> <input
						type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" /> <input type="hidden"
						id="ck_switch" name="ck_switch" value="ck_02" />
				</div>
				<!-- </form> -->
		</div>
	</div>
	
	
		<div>
			<div class="saomiao_tab2">
				<span style="float: right; padding: 10px">
				<input class="input_button1" type="button"
					name="littlefalshbutton" id="flash" value="刷新"
					onclick="location.href='<%=request.getContextPath()%>/PDA/exportwarhouse'" />
					</span>
				<ul id="smallTag">
					<li><a id="table_weichuku" href="#" class="light">待出库明细</a></li>
					<li><a id="table_yichuku" href="#">已出库明细</a></li>
					<li><a id="table_quejian" href="#"
						onclick='getchukucwbquejiandataList($("#branchid").combobox("getValue"));getcwbsquejiandataForBranchid($("#branchid").combobox("getValue"))'>一票多件缺件</a></li>
					<li><a href="#">异常单明细</a></li>
				</ul>
			</div>
			<div id="ViewList" class="tabbox">
				<li>
				<input type="button" id="btnval0" value="导出Excel" class="input_button1"
					onclick='exportField(1,$("#branchid").combobox("getValue"));' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">包号</td>
											<td width="100" align="center" bgcolor="#f1f1f1"><span style="cursor: pointer;" onclick="orderbyweichuku('customerid')">供货商</span></td>
											<td width="140" align="center" bgcolor="#f1f1f1"><span style="cursor: pointer;" onclick="orderbyweichuku('emaildate')">发货时间</span></td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<%
												if (showCustomerSign) {
											%>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%
												}
											%>
											<td align="center" bgcolor="#f1f1f1">地址</td>
											
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="weichukuTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%
												for (CwbDetailView co : weichukuList) {
											%>
											<tr id="TR<%=co.getCwb()%>" cwb="<%=co.getCwb()%>"
												customerid="<%=co.getCustomerid()%>" nextbranchid="<%=co.getNextbranchid()%>">
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%=co.getCustomername() %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<%
													if (showCustomerSign) {
												%>
													<td width="100"><%=co.getRemarkView() %></td>
												<%
													}
												%>
												<td align="left"><%=co.getConsigneeaddress() %></td>
												
											</tr>
											<%
												}
											%>
											<%
												if (weichukuList != null && weichukuList.size() == Page.DETAIL_PAGE_NUMBER) {
											%>
											<tr align="center">
												<td colspan="<%if (showCustomerSign) {%>7<%} else {%>6<%}%>" style="cursor: pointer"
													onclick="weichuku();" id="weichuku">查看更多</td>
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
					class="input_button1" onclick='exportField(2,$("#branchid").combobox("getValue"));' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">包号</td>
											<td width="100" align="center" bgcolor="#f1f1f1"><span style="cursor: pointer;" onclick="orderbyyichuku('customerid')">供货商</span></td>
											<td width="140" align="center" bgcolor="#f1f1f1"><span style="cursor: pointer;" onclick="orderbyyichuku('emaildate')">发货时间</span></td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<%
												if (showCustomerSign) {
											%>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%
												}
											%>
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="successTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%
												for (CwbDetailView co : yichukuList) {
											%>
											<tr id="TR<%=co.getCwb()%>" cwb="<%=co.getCwb()%>"
												customerid="<%=co.getCustomerid()%>">
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%=co.getCustomername() %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<%
													if (showCustomerSign) {
												%>
													<td width="100"><%=co.getRemarkView()==null?"":co.getRemarkView()%></td>
												<%
													}
												%>
												<td align="left"><%=co.getConsigneeaddress() %></td>
											</tr>
											<%
												}
											%>
											<%
												if (yichukuList != null && yichukuList.size() == Page.DETAIL_PAGE_NUMBER) {
											%>
											<tr aglin="center">
												<td colspan="<%if (showCustomerSign) {%>7<%} else {%>6<%}%>" style="cursor: pointer"
													onclick="yichuku();" id="yichuku">查看更多</td>
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
											<td width="120" align="center" bgcolor="#f1f1f1">运单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<td align="center" bgcolor="#f1f1f1">地址</td>
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
					class="input_button1" onclick='exportField(3,$("#branchid").combobox("getValue"));' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">包号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
											<%
												if (showCustomerSign) {
											%>
												<td width="100" align="center" bgcolor="#f1f1f1">订单备注</td>
											<%
												}
											%>
											<td align="center" bgcolor="#f1f1f1">地址</td>
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
 		<form action="<%=request.getContextPath() %>/PDA/exportBybranchid" method="post" id="searchForm3">
			<input type="hidden" name="branchid"
				value="<%=request.getParameter("branchid") == null ? "0" : request.getParameter("branchid")%>"
				id="expbranchid" /> <input type="hidden" name="type" value="" id="type" />
	</form>
</div>
</body>
</html>