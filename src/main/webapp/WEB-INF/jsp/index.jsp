<%@page import="cn.explink.util.ManifestUtil"%>
<%@page import="cn.explink.domain.Menu"%>
<%@page import="cn.explink.util.ServiceUtil"%>
<%@ page language="java" import="java.util.List,java.util.ArrayList,java.util.Map" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	Map usermap = (Map) session.getAttribute("usermap");
	String username = usermap.get("username").toString();
	List<Menu> menutopList = (List<Menu>) request.getAttribute("MENUPARENTLIST");
	
	List<Menu> menus2 = (List<Menu>)request.getAttribute("menus2");
	List<Menu> menus3 = (List<Menu>)request.getAttribute("menus3");
	String omsUrl = request.getAttribute("omsUrl")==null?"oms":request.getAttribute("omsUrl").toString();
	String isOpenFlag = request.getAttribute("isOpenFlag")==null?"0":request.getAttribute("isOpenFlag").toString();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script> 
<script language="javascript" src="<%=request.getContextPath()%>/js/punish.js"></script> 
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript">
	$(function(){
		$("cwb").mouseover(function(){});
	});

	function querycwb(form) {
		var cwb = trim(form.cwb.value);
		form.action = "common?action=opscwbtoview&checkinbranchflag=1&cwb="
				+ cwb;
		form.submit();

	}

	function queryshipcwb(form) {
		var shipcwb = trim(form.shipcwb.value);
		form.action = "common?action=opsshipcwbtoview&checkinbranchflag=1&shipcwb="
				+ shipcwb;
		form.submit();

	}
	
//底部信息位置
$(function(){
	/* $(window).keydown(function(event){
		switch(event.keyCode) {
			case (event.keyCode=27):closeBox();break;
		}
	}); */
	$(window).resize(function(){
		var $scroll_hei=document.documentElement.clientHeight;
		$("#WORK_AREA").height($scroll_hei-160);
		$(".bottom_box").css("top",$scroll_hei-50);
	});
	var $scroll_hei=document.documentElement.clientHeight;
	$(".bottom_box").css("top",$scroll_hei-50);
	$("#WORK_AREA").height($scroll_hei-160);
	$("#playSearch").keydown(function(event){
		if(event.keyCode==13) {
			if(this.value.length==0){
				alert("请输入订单号");
				return ;
			}
			window.open ("<%=request.getContextPath() %>/order/queckSelectOrder/"+this.value);
			$("#playSearch").val("输入订单号后按回车查询");
		}
	});
	
});


$(document).ready(function () {
	<%if("1".equals(isOpenFlag)){ %>
	showZitiCount();
	<%}%>
	setInterval("refreshState()",1000*60*1);
    setInterval("startRequest()",60000);
//setInterval这个函数会根据后面定义的1000既每隔1秒执行一次前面那个函数
   //如果你用局部刷新，要用AJAX技术
  
 });
function startRequest()
{
	$.ajax({
		type: "POST",
		url:"<%=omsUrl %>/download/getDownCount?dmpid=<%=session.getId() %>",
		dataType:"json",
		success : function(data) {
				if(data.errorCode==0){
					$("#downCount").html(data.downCount);
					$("#beingDownCount").html(data.beingDownCount);
					$("#finishCount").html(data.finishCount);
				}
		}
	});
}
function showZitiCount()
{
	$.ajax({
		type: "POST",
		url:"<%=omsUrl %>/download/getZitiCount?dmpid=<%=session.getId() %>",
		dataType:"json",
		success : function(data) {
				if(data.errorCode==0){
					$("#zitiyanwu").html(data.zitiCount);
				}
		}
	});
	
}
$(function(){//关掉XX
	$("#xiaoxi_close").click(function(){
	var temp= $(".xiaoxi_list").is(":hidden");//是否隐藏
	if(temp){
		$(".xiaoxi_list").show("200");
		$("#xiaoxi_close").attr("src","<%=request.getContextPath()%>/images/he.png");
	}else{
		$(".xiaoxi_list").hide("200");
		$("#xiaoxi_close").attr("src","<%=request.getContextPath()%>/images/open.png");
		
	}
});

	$("#xiaoxi_over").click(function(){
		if($("#xiaoxitype").val()!='3' && $("#xiaoxitype").val()!='4'){
			$.ajax({
				type: "POST",
				url:"<%=request.getContextPath()%>/showWindow/changeState/-1",
				dataType:"json",
				success : function(data1) {
					$(".xiaoxi_center").hide("200");
				}
		 	});
		}else{
			$(".xiaoxi_center").hide("200");
		}
})

});


function refreshState(){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/showWindow/showInfo",
		dataType:"json",
		success : function(data) {
			if(data==null){
			}else{
				$("#xiaoxiulinfo").html("");
				var i = 0;
				var flag = 0;
				var n=0;
				var m=0;
				$.each(data, function(key, value) {					
					if(value.type==3){
						i=i+1;				 		
				 	}
					if(value.type==4){
						i=i+1;	
				 	}
					if(value.type==5){
						n=n+1;	
				 	}
					if(value.type==6){
						m=m+1;	
				 	}
			 	});
				
				$.each(data, function(key, value) {
					if(value.type==1){
				 		$("#xiaoxiulinfo").append("<li>您有超期异常单<strong><a style='cursor:pointer' onclick=\"changeback("+value.type+")\">"+(value.jsoninfo).replace("Sum:","")+"条</a></strong>(订单数以超期异常监控处为准)</li>");
				 		$("#xiaoxitype").attr("value",value.type);
				 	}
					if(value.type==2){
				 		$("#xiaoxiulinfo").append("<li>您有订单修改，单号：<strong><a  href='#' onclick=\"changeback("+value.type+")\">"+(value.jsoninfo)+"</a></strong></li>");
				 		$("#xiaoxitype").attr("value",value.type);
				 	}
					if(value.type==3){
						if(flag == 0){
				 		    $("#xiaoxiulinfo").append("<li><strong><a  href='#' onclick=\"changeback("+value.type+")\">您有"+i+"单新问题件待处理</a></strong></li>");
						}
				 		$("#xiaoxitype").attr("value",value.type);
				 		flag = flag+1;
				 	}
					if(value.type==4){
						if(flag == 0){
				 		    $("#xiaoxiulinfo").append("<li><strong><a  href='#' onclick=\"changeback("+value.type+")\">您有"+i+"单订单修改受理待处理</a></strong></li>");
						}
				 		$("#xiaoxitype").attr("value",value.type);
				 		flag = flag+1;
				 	}
					if(value.type==5){
						if(flag == 0){
				 		    $("#xiaoxiulinfo").append("<li><strong><a  href='#' onclick=\"changeback("+value.type+")\">您有"+n+"单订单申请修改-待处理-</a></strong></li>");
						}
				 		$("#xiaoxitype").attr("value",value.type);
				 		flag = flag+1;
				 	}
					if(value.type==6){
						if(flag == 0){
				 		    $("#xiaoxiulinfo").append("<li><strong><a  href='#' onclick=\"changeback("+value.type+")\">您有"+m+"单订单申请修改-已处理-</a></strong></li>");
						}
				 		$("#xiaoxitype").attr("value",value.type);
				 		flag = flag+1;
				 	}
			 	});
			$(".xiaoxi_center").show();
		}
	}});
	
}
function changeback(type){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/showWindow/changeState/"+type,
		dataType:"json",
		success : function(data1) {
			$(".xiaoxi_center").hide("200");
			if(type==1){
				window.open("<%=request.getContextPath()%>/ExceptionMonitor/operationTimeOut");
				}
		    if(type==3){
		    	$("#now_path").html("客服管理   &gt; 投诉处理   &gt; 问题件处理");
		    	window.open("<%=request.getContextPath()%>/abnormalOrder/toHandleabnormal/1?isshow=1&ishandle=2","WORK_AREA");		    	
		    }
		    if(type==4){
		    	$("#now_path").html("站点管理   &gt; 扫描   &gt; 问题件回复");
		    	window.open("<%=request.getContextPath()%>/abnormalOrder/toReplyabnormal/1","WORK_AREA");
		    }
		    if(type==5){
		    	$("#now_path").html("客服管理   &gt;   订单查询; 订单修改受理");
		    	window.open("<%=request.getContextPath()%>/applyeditdeliverystate/tohandleApplyEditDeliverystateList/1","WORK_AREA");		    	
		    }
		    if(type==6){
		    	$("#now_path").html("站点管理 &gt;   归班管理; 订单修改申请");
		    	window.open("<%=request.getContextPath()%>/applyeditdeliverystate/toCreateApplyEditDeliverystate/1","WORK_AREA");		    	
		    }
		}
 	});
	 
}
</script>
</head>
<body style="background:#eef9ff" onload="startRequest();">
<input type="hidden" id="xiaoxitype" value=""/>
<!--弹出公告begin -->
<div class="xiaoxi_center" id="axiaoxi" style="desplay:none;" >
	<h1><span><img style="cursor:default" src="<%=request.getContextPath()%>/images/he.png"  id="xiaoxi_close" /><img style="cursor:default" src="<%=request.getContextPath()%>/images/xiaoxi_close.png" width="20" height="20" id="xiaoxi_over" /></span>消息中心</h1>
	<div class="xiaoxi_list" id="bxiaoxi" >
		<ul id="xiaoxiulinfo">
			
		</ul>
	</div>


</div>







<!--弹出公告over -->


<!--添加下级帐户 -->
<div id="alert_box"></div>
<div class="tishi_box"></div>
<!--添加下级帐户-->





<div class="top_line"></div>

	

<div class="head_box">
	<ul class="index_dh fl">
	<% for(Menu menu:menutopList){ %>
	<li>
	<div class="menu_box">
		<ul>
		<% for(Menu menu2:menus2){ %>
			<% if(menu2.getParentid()==menu.getId()){ %>
				<h3><%=menu2.getName()%></h3>
				<% for(Menu menu3:menus3){ %>
					<%if(menu3.getParentid()==menu2.getId()){ %>
				<p>
					<a href="<%=menu3.getUrl()+"?dmpid="+session.getId() %>" onclick='$("#now_path").html("<%=menu.getName() %> &gt; <%=menu2.getName()%> &gt; <%=menu3.getName()%>");return true;' target="WORK_AREA">
					<img src="<%=menu3.getImage() %>" width="52" height="52" /><%=menu3.getName()%>
					</a>
				</p>
				<%}	} %>
			<%}}%>
		</ul>
	</div>
	<a href="#"><img src="<%=menu.getImage()%>" width="27" height="30" /><%=menu.getName() %></a></li>
	<%}%>
	<%-- <li><a href="http://58.83.193.9:9080/addressmatch/siteEditor.html"  target="WORK_AREA"><img src="<%=request.getContextPath() %>/images/dhbtn16.png" width="27" height="30" />地址库</a></li> --%>
	</ul>
	
	
	
	
	<ul class="index_dh2 fr">
		<li><a target="WORK_AREA" href="<%=request.getContextPath() %>/contact/list/1"  onclick='$("#now_path").html("系统首页  &gt; 通讯录");'><img src="<%=request.getContextPath()%>/images/contact.png"   width="27" height="30" />通讯录</a> </li>
		<%if("1".equals(isOpenFlag)){ %>
		<li>
			<div class="ziti_num" id="zitiyanwu">0</div>
			<a target="_blank" href="<%=omsUrl %>download/zitilist/1"><img src="<%=request.getContextPath()%>/images/down.png" width="27" height="30">自提延迟</a></li>
			<%} %>
		<li class="down_li">
			<div class="down_num" id="downCount"></div>
			<div class="down_xiangqing" id="DIVSHOWID">
			正在导出：<font id="beingDownCount" color='blue'></font><br />
			完成：&nbsp;&nbsp;&nbsp;&nbsp;<font id="finishCount" color='green'></font></div>
			<a target="_blank" href="<%=omsUrl %>download/list/1"><img src="<%=request.getContextPath()%>/images/down.png" width="27" height="30">下载管理</a></li>
		<li><a target="WORK_AREA" href="<%=request.getContextPath() %>/welcome"  onclick='$("#now_path").html("系统首页 ");'><img src="<%=request.getContextPath()%>/images/dhbtn7.png"   width="27" height="30" />系统首页</a></li>
		<li><a target="WORK_AREA" href="<%=request.getContextPath() %>/passwordupdate"   onclick='$("#now_path").html("系统首页  &gt; 修改密码");' ><img src="<%=request.getContextPath()%>/images/dhbtn9.png"   width="27" height="30" />修改密码</a></li>
		<%-- <li><a href="#"><img src="<%=request.getContextPath()%>/images/dhbtn10.png" width="27" height="30" />帮助中心</a></li> --%>
		<li><a href="<%=request.getContextPath() %>/resources/j_spring_security_logout" target="_top"  onClick="return confirm('确定要重新登录吗？');"><img src="<%=request.getContextPath()%>/images/dhbtn8.png" width="27" height="30" />退出</a></li>
	</ul>
	<div class="clear"></div>
</div>

<div class="main_box">
	<div class="address_nav"><span class="fr">
	快速查询：
	<input id="playSearch" type="text" value="输入订单号后按回车查询" class="input_text1"  
		onfocus="if(this.value=='输入订单号后按回车查询'){this.value=''}" onblur="if(this.value==''){this.value='输入订单号后按回车查询'}"/>
	用户：<%=usermap.get("realname") %>&nbsp;&nbsp;部门：<font color="red"><%=usermap.get("branchname")==null?"-":usermap.get("branchname") %></font></span>
	当前位置：<label id="now_path">系统首页 </label></div>

	<div class="right_box">
		<iframe id="WORK_AREA" name="WORK_AREA" src="<%=request.getContextPath() %>/welcome" width="100%" height="500px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe>
	</div>
			
	<div class="jg_10"></div>

	
	
	
	<div class="clear"></div>
</div>


<div class="bottom_box">
	当前版本:<%=ManifestUtil.getSvnRevision(request) %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<img src="images/wangzhan.gif" /><a href="http://www.explink.cn" target="_blank">www.explink.cn</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<img src="images/dianhua.gif"  />010-85896659转8005&nbsp;&nbsp;非工作时间电话：18515029892&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	在线客服：<a href="http://wpa.qq.com/msgrd?v=3&amp;uin=2801062997&amp;site=qq&amp;menu=yes"><img src="images/qqlx.gif" /></a></div>
	<iframe id="wavPlay" name="wavPlay" src="<%=request.getContextPath() %>/wavPlay" width="0" height="0" style="display: none"  frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe>
	<iframe id="printOut" name="printOut" src="" width="500" height="500" style="display: none"  frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe>
	<iframe id="printcwb" name="printcwb" src="<%=request.getContextPath() %>/printcwb" target="printcwb" width="0" height="0" style="display: none"  frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe>
</body>

</html>
