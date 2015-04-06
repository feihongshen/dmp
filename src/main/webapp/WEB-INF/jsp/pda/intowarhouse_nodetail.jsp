<%@page import="cn.explink.domain.JsonContext"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Customer> cList = (List<Customer>)request.getAttribute("customerlist");
List<User> uList = (List<User>)request.getAttribute("userList");
Switch ck_switch = (Switch) request.getAttribute("ck_switch");
String RUKUPCandPDAaboutYJDPWAV = request.getAttribute("RUKUPCandPDAaboutYJDPWAV").toString();
String isprintnew = request.getAttribute("isprintnew").toString();
String wavPath=request.getContextPath()+"/images/wavnums/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
	<param name="CompanyName" value="北京易普联科信息技术有限公司" />
	<param name="License" value="653717070728688778794958093190" />
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 CompanyName="北京易普联科信息技术有限公司"
		License="653717070728688778794958093190"></embed>
</object>
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js" type="text/javascript"></script>
<title>入库扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
var LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
var data;
var startIndex=0;
var step=4;
var emaildate=0;
var preStep;

function initEmailDateUI(ed){
	var firstOpt=$("<option value='0' id='option2'>请选择(供货商_供货商仓库_结算区域)</option> ");
	$("#emaildate").html("");
	$("#emaildate").append(firstOpt);
	 $.ajax({
		 type: "POST",
			url:"<%=request.getContextPath()%>/emaildate/getEmailDateList",
			data:{customerids:$("#customerid").val(),state:"-1"},
			success:function(optionData){
				data=optionData;
				var optionstring="";
				var high ="";
				for(var j=0;j<data.length;j++){
					if(data[j].emaildateid==ed){
						preStep=j;
					}
				}
				
				moreOpt();
				if(ed){
					$("#emaildate").val(ed);
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

	$(function(){
		$("#more").click(moreOpt);
		emaildate=GetQueryString("emaildate")
		initEmailDateUI(emaildate);
		getcwbsdataForCustomer('-1','');
		getcwbsquejiandataForCustomer('-1');
		$("#scancwb").focus();
		
		$("#updateswitch").click(function(){
			var switchstate = "rkbq_02";
			if($("#updateswitch").attr("checked")=="checked"){
				switchstate = $("#updateswitch").val();
				$("#rk_switch").val(switchstate);
			}
			else{
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
		var $menuli = $(".kfsh_tabbtn ul li");
		$menuli.click(function(){
			$(this).children().addClass("light");
			$(this).siblings().children().removeClass("light");
			var index = $menuli.index(this);
			$(".tabbox li").eq(index).show().siblings().hide();
		});
	});
	function focusCwb(){
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
	
	//得到当前入库的供应商的库存量
	function getcwbsdataForCustomer(customerid, cwb) {
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath() %>/PDA/getInSum",
			data : {
				"customerid" : customerid,
				"cwb" : cwb,
				"emaildate":$("#emaildate").val()
			},
			dataType : "json",
			success : function(data) {
				$("#rukukucundanshu").html(data.weirukucount);
				$("#rukukucunjianshu").html(data.weirukusum);
				$("#successcwbnum").html(data.yirukunum);
			}
		});
		 
	}
	function initUI(){
		startIndex=0;
		 step=4;
		 emaildate=0;
		 preStep;
		initEmailDateUI(emaildate);
	}

	
	//得到缺货件数的统计
	function getcwbsquejiandataForCustomer(customerid) {
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/PDA/getInQueSum",
			data : {
				"customerid" : customerid,
				"emaildate":$("#emaildate").val()
			},
			dataType : "json",
			success : function(data) {
				$("#lesscwbnum").html(data.lesscwbnum);
			}
		});
		
	}
	

	/**
	 * 入库扫描
	 */
	function submitIntoWarehouse(pname, scancwb, customerid, driverid,
			requestbatchno, rk_switch, comment) {
		if (scancwb.length > 0) {
			$("#close_box").hide();

			//for ( var i = 0; i < scancwb.split(",").length; i++) {
				//var cwb = scancwb.split(",")[i];
				$.ajax({
							type : "POST",
							url : pname + "/PDA/cwbintowarhouse/" + scancwb
									+ "?customerid=" + customerid
									+ "&driverid=" + driverid
									+ "&requestbatchno=" + requestbatchno,
							data : {
								"comment" : comment,
								"emaildate":$("#emaildate").val()
							},
							dataType : "json",
							success : function(data) {
								$("#scancwb").val("");
								if(data.statuscode == "2"){
									alert(data.body+data.errorinfo);
								}
								if (data.statuscode == "000000") {
									$("#cwbgaojia").hide();

									$("#excelbranch").show();
									$("#customername").show();
									$("#damage").show();
									$("#multicwbnum").show();

									$("#customerid").val(data.body.cwbOrder.customerid);

									if (data.body.cwbOrder.deliverybranchid != 0) {
										$("#excelbranch").html("目的站："+ data.body.cwbdeliverybranchname
																+ "<br/>下一站："+ data.body.cwbbranchname);
									} else {
										$("#excelbranch").html("尚未匹配站点");
									}
									if(data.body.showRemark!=null){
									$("#cwbDetailshow").html("备注："+data.body.showRemark);
									}
									$("#customername").html(
											data.body.cwbcustomername);
									$("#multicwbnum").val(
											data.body.cwbOrder.sendcarnum);
									$("#msg").html(scancwb+ data.errorinfo+ "（共"+ data.body.cwbOrder.sendcarnum
													+ "件，已扫"+ data.body.cwbOrder.scannum+ "件）");
									
									/* if (data.body.cwbOrder.scannum == 1) {
										$("#successcwbnum").html(parseInt($("#successcwbnum").html()) + 1);
									} */
									if($("#updateswitch").attr('checked')=='checked')
									{
									if (rk_switch == "rkbq_01") {
										
										$("#printcwb",parent.document).attr("src",pname + "/printcwb?scancwb="+ scancwb + "&a="+ new Date());
											}
										
									else if (rk_switch == "rkbq_03") {
										$("#printcwb",parent.document).attr("src",pname + "/printcwb/printCwbnew?scancwb="+ scancwb + "&a="+ new Date());
									}
									}
									<%-- if (data.body.cwbbranchnamewav != ""&&data.body.cwbbranchnamewav != pname+ "/wav/") {
										$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ data.body.cwbbranchnamewav
															+ "&a="+ Math.random());
									}else{
										$("#wavPlay",parent.document).attr("src",pname+ "/wavPlay?wavPath="+ pname+ "/images/waverror/success.wav" + "&a="+ Math.random());
									}

									if (data.body.cwbgaojia != undefined && data.body.cwbgaojia != '') {
										$("#cwbgaojia").parent().show();
										try {
											document.gaojia.play();
										} catch (e) {
										}
									}
									if (<%=RUKUPCandPDAaboutYJDPWAV.equals("yes")%>&&data.body.cwbOrder.sendcarnum > 1) {
										try {
											document.ypdj.play();
										} catch (e) {
										}
									} --%>
									if(data.body.showRemark!=null){
										$("#cwbDetailshow").html("订单备注："+data.body.showRemark);
									}
									$("#scansuccesscwb").val(scancwb);
									$("#showcwb").html("订 单 号：" + scancwb);
									
									$("#consigneeaddress").html("地 址："+ data.body.cwbOrder.consigneeaddress);
									if(data.body.cwbOrder.emaildateid==0){
										$("#morecwbnum").html(parseInt($("#morecwbnum").html()) + 1);
									}
									//getcwbsdataForCustomer($("#customerid").val(),scancwb);
									//getcwbsquejiandataForCustomer($("#customerid").val());

								} else {
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
									//errorvedioplay(pname, data);
								}
								$("#responsebatchno").val(data.responsebatchno);
								batchPlayWav(data.wavList);
							}
						});
			//}
		}
	}
	/**
	 * 入库扫描（包）
	 */
	function submitIntoWarehouseforbale(pname, driverid, baleno) {
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
					$("#msg").html(data.body.packageCode + "　（"+ data.errorinfo + "）");
					playWav("'"+data.body.successCount+"'");
				}
			});
		}
	}
	/**
	 * 入库备注提交
	 */
	function intoWarehouseforremark(pname, scancwb, csremarkid, multicwbnum) {
		if (csremarkid == 4 && multicwbnum == "1") {
			$("#msg").html("抱歉，一票多件至少件数");
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
	
	function falshdata(){
		 $("#refresh").attr("disabled","disabled");
		 $("#refresh").val("请稍后……");
		getcwbsdataForCustomer($("#customerid").val(),"");
		getcwbsquejiandataForCustomer($("#customerid").val());
		$("#refresh").val("刷新");
		 $("#refresh").removeAttr("disabled");
	}
	///播放声音文件开始//////
	//调用方法  playWav('12345')
	function playWav(str){
		var strSplit=str.split("");
		for(var i=0;i<strSplit.length;i++) {
			  (function(i){
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
			    	playWavcommit("bofang()",200);
			  },400*i);
			 })(i);
		}
		}
		
		function playWavcommit(){
			document.getElementById('wav').play();
		}
		///播放声音文件结束//////
</script>
</head>
<body style="background: #f5f5f5" marginwidth="0" marginheight="0">
	<div id="emb"></div>
	<div class="saomiao_box2">
		<div class="saomiao_topnum2">
			<dl class="blue">
				<dt>未入库合计</dt>
				<dd id="rukukucundanshu"></dd>
			</dl>
			<dl class="yellow">
				<dt>未入库件数合计</dt>
				<dd id="rukukucunjianshu"></dd>
			</dl>
			<dl class="green">
				<dt>已入库</dt>
				<dd id="successcwbnum" name="successcwbnum">0</dd>
			</dl>
			<dl class="yellow">
				<dt>一票多件缺货件数</dt>
				<dd style="cursor: pointer" onclick="tabView('table_quejian');" id="lesscwbnum"
					name="lesscwbnum">0</dd>
			</dl>
			<dl class="red">
				<dt>有货无单</dt>
				<dd id="morecwbnum" name="morecwbnum">0</dd>
			</dl>
			<input type="button" id="refresh" value="刷新"
				onclick="location.href='<%=request.getContextPath()%>/PDA/intowarhousenodetail'"
				style="float: left; width: 100px; height: 65px; cursor: pointer; border: none; background: url(../images/buttonbgimg1.gif) no-repeat; font-size: 18px; font-family: '微软雅黑', '黑体'" />
			<br clear="all" />
		</div>

		<div class="saomiao_info2">
			<div class="saomiao_inbox2">
				<!-- <div class="saomiao_lefttitle2">扫描订单</div> -->
				<div class="saomiao_righttitle2" id="pagemsg"></div>
				<!-- <form action="" method="get"> -->
				<div class="saomiao_selet2">
					供应商： <select id="customerid" name="customerid"
						onchange="initUI();getcwbsdataForCustomer($('#customerid').val(),$('#scancwb').val());;getcwbsquejiandataForCustomer($('#customerid').val());">
						<option value="-1" selected>全部供应商</option>
						<%
							for (Customer c : cList) {
						%>
						<option value="<%=c.getCustomerid()%>"><%=c.getCustomername()%></option>
						<%
							}
						%>
					</select> 发货批次： <select id="emaildate" name="emaildate"
						onchange="getcwbsdataForCustomer($('#customerid').val(),$('#scancwb').val());;getcwbsquejiandataForCustomer($('#customerid').val());"
						style="height: 20px; width: 280px">
						<option value='0' id="option2">请选择(供货商_供货商仓库_结算区域)</option>
					</select> <a href="#" id="more" style="color: #222222">更多</a> 驾驶员： <select id="driverid" name="driverid">
						<option value="-1" selected>请选择</option>
						<%
							for (User u : uList) {
						%>
						<option value="<%=u.getUserid()%>"><%=u.getRealname()%></option>
						<%
							}
						%>
					</select>
				</div>
				<div class="saomiao_inwrith2">
					<div class="saomiao_left2">
						<p>
							<%
								if (isprintnew.equals("yes")) {
							%>
							<input type="checkbox" id="updateswitch" value="rkbq_03" name="updateswitch" <%-- <%if(ck_switch.getState().equals("rkbq_03")){ %>checked="checked"<%} %> --%>/>打印新标签
							<%
								} else {
							%>
							<input type="checkbox" id="updateswitch" value="rkbq_01" name="updateswitch" <%--  <%if(ck_switch.getState().equals("rkbq_01")){ %>checked="checked"<%} %> --%>/>打印标签
							<%
								}
							%>
						</p>
						<p style="display: none;">
							<span>包号扫描：</span> <input type="text" class="saomiao_inputtxt" value="" id="bale" name="bale"
								onKeyDown='if(event.keyCode==13&&$(this).val().length>0){submitIntoWarehouseforbale("<%=request.getContextPath()%>",$("#driverid").val(),$(this).val());}' />
						</p>
						<p>
							<span>订单号：</span> <input type="text" class="saomiao_inputtxt" id="scancwb" name="scancwb"
								value=""
								onKeyDown='if(event.keyCode==13&&$(this).val().length>0){submitIntoWarehouse("<%=request.getContextPath()%>",$(this).val(),$("#customerid").val(),$("#driverid").val(),$("#requestbatchno").val(),$("#rk_switch").val(),"");}' />
						</p>
						<!-- <p>
						<span>&nbsp;</span><input type="submit" name="button" id="button" value="完成扫描" class="button">
					</p> -->
					</div>
					<div class="saomiao_right2">
						<p id="msg" name="msg"></p>
						<p id="showcwb" name="showcwb"></p>
						<p id="cwbgaojia" name="cwbgaojia" style="display: none">高价</p>
						<p id="consigneeaddress" name="consigneeaddress"></p>
						<p id="excelbranch" name="excelbranch"></p>
						<p id="customername" name="customername"></p>
						<p id="cwbDetailshow" name="cwbDetailshow"></p>
						<div style="display: none" id="EMBED"></div>
						<div style="display: none">
							<EMBED id='ypdj' name='ypdj'
								SRC='<%=request.getContextPath()%><%=ServiceUtil.waverrorPath%><%=CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl()%>'
								LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
						</div>
						<div style="display: none">
							<EMBED id='gaojia' name='gaojia'
								SRC='<%=request.getContextPath()%><%=ServiceUtil.waverrorPath%><%=CwbOrderPDAEnum.GAO_JIA.getVediourl()%>'
								LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
						</div>
						<div style="display: none" id="errorvedio"></div>
					</div>
					<p style="display: none;">
						<input type="button" class="input_btn1" id="moregoods" name="moregoods" value="一票多物"
							onclick='intoWarehouseforremark("<%=request.getContextPath()%>",$("#scansuccesscwb").val(),4,$("#multicwbnum").val());' />
						<span>一票多物件数：</span><input type="text" id="multicwbnum" name="multicwbnum" value="1"
							class="input_txt1" />
					</p>
					<p style="display: none;">
						<input type="button" class="input_btn1" id="damage" name="damage" value="破损"
							onclick='intoWarehouseforremark("<%=request.getContextPath()%>",$("#scansuccesscwb").val(),1,$("#multicwbnum").val());' />
						<input type="button" class="input_btn1" id="superbig" name="superbig" value="超大"
							onclick='intoWarehouseforremark("<%=request.getContextPath()%>",$("#scansuccesscwb").val(),2,$("#multicwbnum").val());' />
						<input type="button" class="input_btn1" id="superweight" name="superweight" value="超重"
							onclick='intoWarehouseforremark("<%=request.getContextPath()%>",$("#scansuccesscwb").val(),3,$("#multicwbnum").val());' />
					</p>
					<input type="hidden" id="requestbatchno" name="requestbatchno" value="0" /> <input
						type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" /> <input type="hidden"
						id="rk_switch" name="rk_switch" value="<%=ck_switch.getState()%>" />
				</div>
				<!-- </form> -->
			</div>
		</div>

	</div>
</body>
</html>
