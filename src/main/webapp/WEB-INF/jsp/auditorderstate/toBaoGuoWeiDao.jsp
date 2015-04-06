<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
List<CwbOrderView> cwbList = (List<CwbOrderView>)request.getAttribute("cwbList");
List<Reason> reasonList = (List<Reason>)request.getAttribute("reasonList");
List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#btnval1").click(function(){
		if($("#cwb").val().length==0){
			alert("请输入订单号！");
			return false;
			}
		
		send1();
	});
	$("#btnval2").click(function(){
		if($("#cwb").val().length==0){
			alert("请输入订单号！");
			return false;
			}
		
		send2();
	});
	$("#btnval3").click(function(){
		if($("#cwb").val().length==0){
			alert("请输入订单号！");
			return false;
			}
		
		send3();
	});
});
function send1(){
	loading();
	var params=$('#searchForm').serialize();
$.ajax({
	url:"<%=request.getContextPath()%>/cwborder/toBaoGuoWeiDaoCommit",//后台处理程序
	type:"POST",//数据发送方式 
	data:params,//参数
	dataType:'json',//接受数据格式
	success:function(json){
		loadend();
		$("#cwb").val('');
		str="";
		str +="<p>成功数：<font color='red'>"+json.sussesCount+"</font></p>";
		str +="<p>失败数：<font color='red'>"+json.errorCount+"</font></p>";
		str +="<p>失败原因：</p>";
		str +=json.errorMsg;
		$("#msg").html(str);
	}
	   
});
}
function send2(){
	loading();
	var params=$('#searchForm').serialize();
$.ajax({
	url:"<%=request.getContextPath()%>/cwborder/toZhongzhuanYanwuCommit",//后台处理程序
	type:"POST",//数据发送方式 
	data:params,//参数
	dataType:'json',//接受数据格式
	success:function(json){
		loadend();
		$("#cwb").val('');
		str="";
		str +="<p>成功数：<font color='red'>"+json.sussesCount+"</font></p>";
		str +="<p>失败数：<font color='red'>"+json.errorCount+"</font></p>";
		str +="<p>失败原因：</p>";
		str +=json.errorMsg;
		$("#msg").html(str);
	}
	   
});
}
function send3(){
	loading();
	var params=$('#searchForm').serialize();
$.ajax({
	url:"<%=request.getContextPath()%>/cwborder/toHuowuDiushiCommit",//后台处理程序
	type:"POST",//数据发送方式 
	data:params,//参数
	dataType:'json',//接受数据格式
	success:function(json){
		loadend();
		$("#cwb").val('');
		str="";
		str +="<p>成功数：<font color='red'>"+json.sussesCount+"</font></p>";
		str +="<p>失败数：<font color='red'>"+json.errorCount+"</font></p>";
		str +="<p>失败原因：</p>";
		str +=json.errorMsg;
		$("#msg").html(str);
	}
	   
});
}
function loading(){
	document.getElementById('container').style.display='block';
}
function loadend(){
	document.getElementById('container').style.display='none';
}

</script>
</HEAD>
<BODY style="background:#f5f5f5"  marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="<%=request.getContextPath() %>/cwborder/toTuiHuo" >订单拦截</a></li>
				<li><a href="<%=request.getContextPath() %>/cwborder/toTuiHuoZaiTou">审核为退货再投</a></li>
				<li><a href="<%=request.getContextPath() %>/cwborder/toTuiGongHuoShang">审核为退供货商</a></li>
				<li><a href="<%=request.getContextPath() %>/cwborder/toTuiGongHuoShangSuccess">审核为供货商确认退货</a></li>
				<li><a href="<%=request.getContextPath() %>/cwborder/toChongZhiStatus" >重置审核状态</a></li>
				<li><a href="<%=request.getContextPath() %>/cwborder/toBaoGuoWeiDao" class="light">亚马逊订单处理</a></li>
				<!-- <li><a href="./toZhongZhuan">审核为中转</a></li> -->
				<%-- <%if(request.getAttribute("isUseAuditTuiHuo") != null && "yes".equals(request.getAttribute("isUseAuditTuiHuo").toString())){ %>
					<li><a href="<%=request.getContextPath() %>/cwbapply/kefuuserapplytoTuiHuolist/1">审核为退货</a></li>
				<%} %> --%>
				<%if(request.getAttribute("isUseAuditZhongZhuan") != null && "yes".equals(request.getAttribute("isUseAuditZhongZhuan").toString())){ %>
					<li><a href="<%=request.getContextPath() %>/cwbapply/kefuuserapplytoZhongZhuanlist/1">审核为中转</a></li>
				<%} %>
				<li><a href="<%=request.getContextPath() %>/orderBackCheck/toTuiHuoCheck">审核为允许退货出站</a></li>
				<li><a href="<%=request.getContextPath() %>/cwborder/toChangeZhongZhuan">审核为中转件</a></li>
				<li><a href="<%=request.getContextPath() %>/cwborder/toChangeZhongZhuan">审核为中转件</a></li>
				
			</ul>
		</div>
		<div>
				<div>
					<div>
						<div>
							<form action="" method="POST" id="searchForm">
									订单号：
								<textarea name="cwb" rows="20" cols="40" class="kfsh_text" id="cwb"  ></textarea>
								
								<input id="btnval1"  type="button" value="标记为包裹未到" >
								<input id="btnval2"  type="button" value="标记为中转延误" >
								<input id="btnval3"  type="button" value="标记为货物丢失" >
							</form>
						</div>
						<div id="container" style="display:none">
  					             正在处理，请耐心等候。。<img src="<%=request.getContextPath()%>/images/aloading.gif" alt="" />
                        </div>
						<div id="msg">
	  	
	  					</div>
		</div>
	</div>
</div>
</BODY>
</HTML>
