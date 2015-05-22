<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<User> uList = (List<User>)request.getAttribute("userList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>领货扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
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
	getweilingdata(0);
	 $("#scancwb").focus();
});

//得到当前待领货的库存量
function getweilingdata(deliverid){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/PDA/getWeiLingHuoSum",
		data:{deliverid:deliverid},
		dataType:"json",
		success : function(data) {
			$("#todayweilingdanshu").html(data.todayweilinghuocount);
			$("#historyweilingdanshu").html(data.historyweilinghuocount);
			$("#linghuoSuccessCount").html(data.yilinghuo);
		}
	});
}

/**
 * 小件员领货扫描
 */
 var deliverStr=[];
 
<%for(User u : uList){%>
	deliverStr[<%=u.getUserid()%>]="";
<%}%>
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
					$("#scansuccesscwb").val(scancwb);
					
					<%-- <%for(User u : uList){ %>
						deliverStr[<%=u.getUserid()%>]=deliverStr[<%=u.getUserid()%>].replace(scancwb+",","");
					<%}%>
					deliverStr[deliverid] = scancwb + "," + deliverStr[deliverid];
					
					linghuoSuccessCount = deliverStr[deliverid].split(",").length-1; --%>
					
					$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
					
					
					$("#showcwb").html("订 单 号："+scancwb);
					$("#consigneeaddress").html("地 址："+data.body.cwbOrder.consigneeaddress);
					
					
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
					
					//getweilingdata(data.body.cwbOrder.deliverid);
					/*if(data.body.cwbOrder.sendcarnum>1){
						document.ypdj.Play();
					}
					
					if(data.body.cwbdelivernamewav!=pname+"/wav/"){
						$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbdelivernamewav+"&a="+Math.random());
					}else{
						$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
					}*/
				}else{
					$("#cwbDetailshow").html("");
					$("#exceldeliverid").html("");
					$("#showcwb").html("");
					$("#consigneeaddress").html("");
					$("#cwbordertype").html("");
					$("#deliver").html("已领货");
					$("#fee").html("");
					$("#msg").html(scancwb+"         （异常扫描）"+data.errorinfo);
					//errorvedioplay(pname,data);
				}
				
				
				<%-- <%for(User u : uList){ %>
					allnum += deliverStr[<%=u.getUserid()%>].split(",").length-1;
				<%}%>
				
				if(linghuoSuccessCount!=0){
					$("#linghuoSuccessCount").html(linghuoSuccessCount);
				}
				$("#allnum").html(allnum); --%>
				$("#responsebatchno").val(data.responsebatchno);
				batchPlayWav(data.wavList);
			}
		});
	}
}


function  falshdata(){
	 $("#refresh").attr("disabled","disabled");
	 $("#refresh").val("请稍后……");
	getweilingdata($("#deliverid").val());
	$("#refresh").val("刷新");
	 $("#refresh").removeAttr("disabled");
}

/* function getsum(deliverid){
	//alert(deliverStr[deliverid].split(",")+"----"+deliverStr[deliverid].split(",").length-1);
	if(deliverid!=-1){
		$("#linghuoSuccessCount").html(deliverStr[deliverid].split(",").length-1);
	}else{
		$("#linghuoSuccessCount").html(0);
	}
} */
</script>
</head>
<body style="background: #f5f5f5" marginwidth="0" marginheight="0">
	<div class="saomiao_box">

		<div class="saomiao_topnum">
			<dl class="blue">
				<dt>今日待领货合计</dt>
				<dd id="todayweilingdanshu">0</dd>
			</dl>
			<dl class="yellow">
				<dt>历史待领货合计</dt>
				<dd id="historyweilingdanshu">0</dd>
			</dl>
			<dl class="green">
				<dt id="deliver">已领货</dt>
				<dd id="linghuoSuccessCount">0</dd>
			</dl>
			<input type="button" id="refresh" value="刷新"
				onclick="location.href='<%=request.getContextPath()%>/PDA/branchdeliver'"
				style="float: left; width: 100px; height: 65px; cursor: pointer; border: none; background: url(../images/buttonbgimg1.gif) no-repeat; font-size: 18px; font-family: '微软雅黑', '黑体'" />

			<br clear="all">
		</div>

		<div class="saomiao_info">
			<div class="saomiao_inbox">
				<div class="saomiao_lefttitle">扫描订单</div>
				<div class="saomiao_righttitle" id="pagemsg"></div>
				<div class="saomiao_selet">
					选择小件员： <select id="deliverid" name="deliverid" onchange="getweilingdata($(this).val());">
						<option value="-1" selected>请选择</option>
						<%
							for (User u : uList) {
						%>
						<option value="<%=u.getUserid()%>"><%=u.getRealname()%></option>
						<%
							}
						%>
					</select>*
				</div>
				<div class="saomiao_inwrith">
					<div class="saomiao_left">
						<p>
							<span>订单号：</span> <input type="text" class="saomiao_inputtxt" id="scancwb" name="scancwb"
								value=""
								onKeyDown='if(event.keyCode==13&&$(this).val().length>0){branchDeliver("<%=request.getContextPath()%>",$(this).val(),$("#deliverid").val(),$("#requestbatchno").val());}' />
						</p>
					</div>
					<div class="saomiao_right">
						<p id="msg" name="msg"></p>
						<p id="cwbordertype" name="cwbordertype"></p>
						<p id="showcwb" name="showcwb"></p>
						<p id="cwbgaojia" name="cwbgaojia" style="display: none">高价</p>
						<p id="consigneeaddress" name="consigneeaddress"></p>
						<p id="fee" name="fee"></p>
						<p id="exceldeliverid" name="exceldeliverid"></p>
						<p id="cwbDetailshow" name="cwbDetailshow"></p>
						<div style="display: none" id="EMBED"></div>
						<div style="display: none">
							<EMBED id='ypdj' name='ypdj'
								SRC='<%=request.getContextPath()%><%=ServiceUtil.waverrorPath%><%=CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl()%>'
								LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
						</div>
						<div style="display: none" id="errorvedio"></div>
					</div>
					<input type="hidden" id="requestbatchno" name="requestbatchno" value="0" /> <input
						type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" />
				</div>
			</div>
		</div>

	</div>

</body>
</html>

