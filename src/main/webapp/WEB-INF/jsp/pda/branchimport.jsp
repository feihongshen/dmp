<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<User> uList = (List<User>)request.getAttribute("userList");
Switch ck_switch = (Switch) request.getAttribute("ck_switch");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>到货扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
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

//得到到货缺件数
function getDaoHuoQueSum() {
	$.ajax({
		type : "POST",
		url : "<%=request.getContextPath()%>/PDA/getDaoHuoQueSum",
		dataType : "json",
		success : function(data) {
			$("#lesscwbnum").html(data.lesscwbnum);
		}
	});
}

//得到当前到货的库存量
function getcwbsdataForDaoHuo(pname,cwb){
	$.ajax({
		type: "POST",
		url:pname+"/PDA/getZhanDianInSum",
		data:{
			"cwb":cwb
		},
		dataType:"json",
		success : function(data) {
			$("#jinriweidaohuodanshu").html(data.jinriweidaohuocount);
			$("#historyweidaohuodanshu").html(data.historyweidaohuocount);
			$("#successcwbnum").html(data.yidaohuonum);
		}
	});
}
$(function(){
	getcwbsdataForDaoHuo('<%=request.getContextPath() %>','');
	getDaoHuoQueSum();
	 $("#scancwb").focus();
});

var branchimportcwbStr="";
var successnum = 0;
function submitIntoWarehouse(pname,scancwb,customerid,driverid,requestbatchno,rk_switch,comment){
	if(scancwb.length>0){
		if($("#balenoTab").attr("class")=="light"){//入库根据包号扫描订单
			baledaohuo(scancwb,driverid,requestbatchno);
		}else{//入库
			$.ajax({
				type: "POST",
				url:pname+"/PDA/cwbsubstationGoods/"+scancwb+"?driverid="+driverid+"&requestbatchno="+requestbatchno,
				data:{
					"comment":""
				},
				dataType:"json",
				success : function(data) {
					$("#scancwb").val("");
					if(data.statuscode=="000000"){
						$("#cwbgaojia").hide();
						if(data.body.showRemark!=null){
						$("#cwbDetailshow").html("订单备注："+data.body.showRemark);
						}
						$("#excelbranch").show();
						$("#customername").show();
						$("#damage").show();
						$("#multicwbnum").show();
						
						$("#customerid").val(data.body.cwbOrder.customerid);
						
						if(data.body.cwbOrder.deliverybranchid!=0){
							$("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
						}else{
							$("#excelbranch").html("尚未匹配站点");
						}
						
						$("#customername").html(data.body.cwbcustomername);
						$("#address").html(data.body.cwbOrder.consigneeaddress);
						
						$("#multicwbnum").val(data.body.cwbOrder.sendcarnum);
						$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
						//getcwbsdataForDaoHuo(pname,scancwb);
						
						if(rk_switch=="rkbq_01"){
							$("#printcwb").attr("src",pname+"/printcwb?scancwb="+scancwb+"&a="+new Date());
						}
						
						/*if(data.body.cwbbranchnamewav!=""&&data.body.cwbbranchnamewav!=pname+"/wav/"){
							$("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random());
						}else{
							$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
						}
						
						if(data.body.cwbgaojia!=undefined&&data.body.cwbgaojia!=''){
							$("#cwbgaojia").parent().show();
							try{
							document.gaojia.Play();
							}catch (e) {}
						}
						if(data.body.cwbOrder.sendcarnum>1){
							try{
								document.ypdj.Play();
							}catch (e) {}
						}*/
							
						$("#scansuccesscwb").val(scancwb);
						$("#showcwb").html("订 单 号："+scancwb);
					}else{
						$("#cwbDetailshow").html("");
						$("#excelbranch").hide();
						$("#customername").hide();
						$("#address").html("");
						$("#cwbgaojia").hide();
						$("#damage").hide();
						$("#multicwbnum").hide();
						$("#multicwbnum").val("1");
						$("#showcwb").html("");
						$("#msg").html("         （异常扫描）"+data.errorinfo);
						//errorvedioplay(pname,data);
					}
					$("#responsebatchno").val(data.responsebatchno);
					/* if(successnum!=0){
						$("#successcwbnum").html(successnum);
					} */
					batchPlayWav(data.wavList);
				}
			});
		}
	}
}
/**
 * 入库扫描（包）
 */
/* function submitIntoWarehouseforbale(pname,driverid,baleno){
	if(scancwb.length==0&&baleno.length==0){
		$("#pagemsg").html("请先扫描");
		return ;
	}
	if(baleno.length>0){
		$.ajax({
			type: "POST",
			url:pname+"/PDA/cwbintowarhouseByPackageCode/"+baleno+"?driverid="+driverid,
			dataType:"json",
			success : function(data) {
				$("#baleno").val("");
				$("#msg").html(data.body.packageCode+"　（"+data.errorinfo+"）");
			}
		});
	}
} */
/**
 * 入库备注提交
 */
function intoWarehouseforremark(pname,scancwb,csremarkid,multicwbnum){
	if(csremarkid==4&&multicwbnum=="1"){
		$("#msg").html("抱歉，一票多件至少件数");
		return ;
	}else{
		$.ajax({
			type: "POST",
			url:pname+"/PDA/forremark/"+scancwb+"?csremarkid="+csremarkid+"&multicwbnum="+multicwbnum+"&requestbatchno=0",
			dataType:"json",
			success : function(data) {
				if(data.statuscode=="000000"){
					$("#msg").html("订单备注操作成功");
				}else{
					$("#msg").html(data.errorinfo);
					errorvedioplay(pname,data);
				}
			}
		});
	}
}
function clearMsg(){
	$("#msg").html("");
	$("#showcwb").html("");
	$("#excelbranch").html("");
	$("#customername").html("");
	$("#address").html("");
	
	
	$("#cwbgaojia").hide();
	$("#damage").hide();
	$("#multicwbnum").hide();
	$("#multicwbnum").val("1");
	
}

function  falshdata(){
	 $("#refresh").attr("disabled","disabled");
	 $("#refresh").val("请稍后……");
	getcwbsdataForDaoHuo('<%=request.getContextPath() %>','');
	 $("#refresh").val("刷新");
	 $("#refresh").removeAttr("disabled");
}


//=========合包到货=============
function baledaohuo(scancwb,driverid,requestbatchno){
	if($("#baleno").val()==""){
		alert("包号不能为空！");
		$("#scancwb").val("");
		return;
	}
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/bale/baledaohuo/"+$("#baleno").val()+"/"+scancwb+"?driverid="+driverid+"&requestbatchno="+requestbatchno,
		dataType : "json",
		success : function(data) {
			$("#msg").html("");
			$("#msg").html(data.body.errorinfo);
			errorvedioplay("<%=request.getContextPath()%>",data);
			$("#scancwb").val("");
		}
	});
}
</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="saomiao_box">

	<div class="saomiao_topnum">
		<dl class="blue">
			<dt>今日未到货</dt>
			<dd id="jinriweidaohuodanshu"></dd>
		</dl>
		<dl class="yellow">
			<dt>历史未到货</dt>
			<dd id="historyweidaohuodanshu"></dd>
		</dl>
		<dl class="green">
			<dt>已扫描</dt>
			<dd id="successcwbnum" name="successcwbnum">0</dd>
		</dl>
		<dl class="yellow">
			<dt>一票多件缺货件数</dt>
			<dd id="lesscwbnum" name="lesscwbnum" >0</dd>
		</dl>
		<input type="button"  id="refresh" value="刷新" onclick="location.href='<%=request.getContextPath() %>/PDA/branchimort'" style="float:left; width:100px; height:65px; cursor:pointer; border:none; background:url(../images/buttonbgimg1.gif) no-repeat; font-size:18px; font-family:'微软雅黑', '黑体'"/>
		
		<br clear="all"/>
	</div>
	
	<div class="saomiao_info">
		<div class="saomiao_inbox">
			<div class="kfsh_tabbtn">
				<ul>
					<li><a href="#" id="cwbTab" onclick="clearMsg();$(function(){$('#baleno').parent().hide();$('#baleno').val('');$('#scancwb').val('');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();})" class="light">扫描订单</a></li>
					<li><a href="#" id="balenoTab" onclick="clearMsg();$(function(){$('#baleno').parent().show();$('#baleno').show();$('#baleno').val('');$('#baleno').focus();$('#scancwb').val('');$('#scancwb').hide();$('#scancwb').parent().hide();})">合包到货</a></li>
				</ul>
			</div>
			<div class="saomiao_righttitle" id="pagemsg"></div>
			<!-- <form action="" method="get"> -->
			<div class="saomiao_selet">
				驾驶员：
				<select id="driverid" name="driverid">
					<option value="-1" selected>请选择</option>
					<%for(User u : uList){ %>
						<option value="<%=u.getUserid() %>" ><%=u.getRealname() %></option>
					<%} %>
				</select>
			</div>
			<div class="saomiao_inwrith">
				<div class="saomiao_left">
					<p style="display: none;"><span>包号：</span>
						<input type="text" class="saomiao_inputtxt" value=""  id="baleno" name="bale" onKeyDown="if(event.keyCode==13&&$(this).val().length>0){$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();}"/>
					</p>
					<p><span>订单号：</span>
						<input type="text" class="saomiao_inputtxt" id="scancwb" name="scancwb" value="" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){submitIntoWarehouse("<%=request.getContextPath()%>",$(this).val(),-1,$("#driverid").val(),$("#requestbatchno").val(),$("#rk_switch").val(),"");}'/>
					</p>
				</div>
				<div class="saomiao_right">
					<p id="msg" name="msg" ></p>
					<p id="showcwb" name="showcwb"></p>
					<p id="cwbgaojia" name="cwbgaojia" style="display: none" >高价</p>
					<p id="excelbranch" name="excelbranch" ></p>
					<p id="customername" name="customername" ></p>
					<p id="address" name="address" ></p>
					<p id="cwbDetailshow" name="cwbDetailshow" ></p>
					
					<div style="display: none" id="EMBED">
					</div>
					<div style="display: none">
						<EMBED id='ypdj' name='ypdj' SRC='<%=request.getContextPath() %><%=ServiceUtil.waverrorPath %><%=CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl() %>' LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
					</div>
					<div style="display: none">
						<EMBED id='gaojia' name='gaojia' SRC='<%=request.getContextPath() %><%=ServiceUtil.waverrorPath %><%=CwbOrderPDAEnum.GAO_JIA.getVediourl() %>' LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
					</div>
					<div style="display: none" id="errorvedio"></div>
				</div>
				<p style="display: none;">
						<input type="button" class="input_btn1" id="moregoods" name="moregoods" value="一票多物" onclick='intoWarehouseforremark("<%=request.getContextPath()%>",$("#scansuccesscwb").val(),4,$("#multicwbnum").val());'/>
						<span>一票多物件数：</span><input type="text" id="multicwbnum" name="multicwbnum" value="1" class="input_txt1"/>
					</p>
					<p style="display: none;">
						<input type="button" class="input_btn1" id="damage" name="damage" value="破损" onclick='intoWarehouseforremark("<%=request.getContextPath()%>",$("#scansuccesscwb").val(),1,$("#multicwbnum").val());'/>
						<input type="button" class="input_btn1" id="superbig" name="superbig" value="超大" onclick='intoWarehouseforremark("<%=request.getContextPath()%>",$("#scansuccesscwb").val(),2,$("#multicwbnum").val());'/>
						<input type="button" class="input_btn1" id="superweight" name="superweight" value="超重" onclick='intoWarehouseforremark("<%=request.getContextPath()%>",$("#scansuccesscwb").val(),3,$("#multicwbnum").val());'/>
					</p>
					<input type="hidden" id="requestbatchno" name="requestbatchno" value="0" />
			        <input type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" />
			        <input type="hidden" id="rk_switch" name="rk_switch" value="<%=ck_switch.getState() %>" />
			</div><!-- </form> -->
		</div>
	</div>
</div>
</body>
</html>
