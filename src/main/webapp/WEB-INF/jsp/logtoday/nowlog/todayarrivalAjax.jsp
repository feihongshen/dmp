<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.logdto.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.math.BigDecimal"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>站点日志（本站）</title>
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/branchLog.js" type="text/javascript"></script>
</head>
<%
Branch nowbranch = (Branch)request.getAttribute("nowbranch");
List<Branch> branchnameList = (List<Branch>)request.getAttribute("branchnameList");
%>
<script>
function dgetViewBox(key,durl){
	window.parent.getViewBoxd(key,durl);
}
</script>
<script type="text/javascript">
$(function(){
$("#right_hideboxbtn").click(function(){
			var right_hidebox = $("#right_hidebox").css("right")
			if(
				right_hidebox == -400+'px'
			){
				$("#right_hidebox").css("right","10px");
				$("#right_hideboxbtn").css("background","url(right_hideboxbtn2.gif)");
				
			};
			
			if(right_hidebox == 10+'px'){
				$("#right_hidebox").css("right","-400px");
				$("#right_hideboxbtn").css("background","url(right_hideboxbtn.gif)");
			};
	});
});
$(function(){
	if(<%=(request.getAttribute("sitetype")==null?"-1":request.getAttribute("sitetype")).equals("2")%>&&$("#daohuo").val()==0){
		$("#daohuo_a").html("载入到货日报");
		$("#daohuo").val("1");
		getTodayArrivalDTO('<%=request.getContextPath() %>','getTodayArrivalDTO',
				'<%=request.getAttribute("branchidSession")==null?"-1":request.getAttribute("branchidSession")%>',
						'todayArrival',<%="1".equals(request.getAttribute("showWeidaohuo"))%>,'<%=request.getAttribute("startTime")%>');
	}
	$("#toudi_a").click(function(){
		if(<%=(request.getAttribute("sitetype")==null?"-1":request.getAttribute("sitetype")).equals("2")%>&&$("#toudi").val()==0){
			$("#toudi_a").html("载入投递日报");
			$("#toudi").val("1");
			getTodayDeliveryDTO('<%=request.getContextPath() %>','getTodayDeliveryDTO','<%=request.getAttribute("branchidSession")==null?"-1":request.getAttribute("branchidSession")%>','todayDelivery');
		}
	});
	$("#kuanxiang_a").click(function(){
		if(<%=(request.getAttribute("sitetype")==null?"-1":request.getAttribute("sitetype")).equals("2")%>&&$("#kuanxiang").val()==0){
			$("#kuanxiang_a").html("载入款项日报");
			$("#kuanxiang").val("1");
			getTodayFundsDTO('<%=request.getContextPath() %>','getTodayFundsDTO',
					'<%=request.getAttribute("branchidSession")==null?"-1":request.getAttribute("branchidSession")%>',
							'todayFunds','<%=request.getAttribute("startTime")%>');
		}
	});
	$("#kucun_a").click(function(){
		if(<%=(request.getAttribute("sitetype")==null?"-1":request.getAttribute("sitetype")).equals("2")%>&&$("#kucun").val()==0){
			$("#kucun_a").html("载入库存日报");
			$("#kucun").val("1");
			getTodayStockDTO('<%=request.getContextPath() %>','getTodayStockDTO','<%=request.getAttribute("branchidSession")==null?"-1":request.getAttribute("branchidSession")%>','todayStock');
		}
	});
});
</script>

<script language="javascript">
$(function(){
	var $menuli = $(".uc_midbg ul li");
	var $menulilink = $(".uc_midbg ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
	
});

</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<input type="hidden" id="daohuo" value="0" />
<input type="hidden" id="toudi" value="0" />
<input type="hidden" id="kuanxiang" value="0" />
<input type="hidden" id="kucun" value="0" />
<div class="right_box">
<div class="menucontant">
<div class="inputselect_box" style="top: 0px; ">
			&nbsp;&nbsp;当前日期：<%=new SimpleDateFormat("yyyy-MM-dd").format(new Date()) %>
			&nbsp;&nbsp;&nbsp;&nbsp;当前统计从<font color="red"><%=request.getAttribute("startTime") %> </font>到当前时间
	</div>
	<div class="jg_35"></div>
	<div class="uc_midbg">
		<ul>
			<li><a href="#" id="daohuo_a" class="light">今日到货日报</a></li>
			<li><a href="#" id="toudi_a">今日投递日报</a></li>
			<li><a href="#" id="kuanxiang_a">今日款项日报</a></li>
			<li><a href="#" id="kucun_a">今日库存日报</a></li>
		</ul>
	</div>
    <div style="padding:10px">
    <form id="searchForm" action ="<%=request.getContextPath()%>/logtoday/todayArrivalAjax" method = "post">
    站点：<select name ="branchid" id="branid" onchange="$('#searchForm').submit()">
	              <%if(branchnameList != null && branchnameList.size()>0){ %>
	               <%for( Branch b:branchnameList){ %>
	               		<option value ="<%=b.getBranchid()%>" 
	               		<%if(b.getBranchid() == new Long(request.getAttribute("branchidSession")==null?"-1":request.getAttribute("branchidSession").toString())) {%>selected="selected"<%} %>><%=b.getBranchname() %></option>
	               <%} }%>
	              </select> &nbsp;&nbsp;&nbsp;&nbsp;<font color="red">当前查看站：<%=nowbranch==null?"":nowbranch.getBranchname() %></font>
	              </form> 
	              </div>
    <div class="tabbox">
        <ul>
        <li>
        <div class="right_title">
        	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table4">
        		<tbody>
        			<tr class="font_1" height="30">
        				<!-- <td align="center" valign="middle" bgcolor="#E1F0FF">今日应到货</td> -->
        				<%if("1".equals(request.getAttribute("showWeidaohuo"))){ %>
        				<td colspan="5" align="center" valign="middle" bgcolor="#E7F4E3">今日到货</td>
        				<%}else{ %>
        				<td colspan="4" align="center" valign="middle" bgcolor="#E7F4E3">今日到货</td>
        				<%} %>
        				<td rowspan="2" align="center" valign="middle" bgcolor="#FFEFDF">到错货（单）</td>
        				</tr>
        			<tr class="font_1" height="30">
        				<!-- <td align="center" valign="middle" bgcolor="#E1F0FF">今日库房已出库（单）</td> -->
        				<%if("1".equals(request.getAttribute("showWeidaohuo"))){ %>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">未到货（单）</td>
        				<%} %>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">已到货（单）</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">漏扫已到货（单）</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">他站到货（单）</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">少货（单）</td>
        				</tr>
        			<tr height="30" id="todayArrival">
        				<%-- <td align="center" valign="middle"><strong><a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/daohuo_jinriyichuku/1"><%=todayArrival.getJinriyichuku()%></a></strong></td> --%>
        				<%if("1".equals(request.getAttribute("showWeidaohuo"))){ %>
        				<td align="center" valign="middle"><strong><a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/daohuo_weidaohuo/1">0</a></strong></td>
        				<%} %>
        				<td align="center" valign="middle"><strong><a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/daohuo_yidaohuo/1">0</a></strong></td>
        				<td align="center" valign="middle"><strong><a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/daohuo_lousaoyidaohuo/1">0</a></strong></td>
        				<td align="center" valign="middle"><strong><a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/daohuo_tazhandaohuo/1">0</a></strong></td>
        				<td align="center" valign="middle"><strong><a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/daohuo_shaohuo/1">0</a></strong></td>
        				<td align="center" valign="middle"><strong><a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/daohuo_daocuohuo/1">0</a></strong></td>
        				</tr>
        			</tbody>
        		</table>
        </div>
         </li>
         
          <li style="display:none">
		  <div class="right_title">
        	<div style="overflow-x: scroll; width:100%; padding-bottom:15px" id="scroll2">
            
                <table width="2700" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
                    <tbody>
					 <tr class="font_1" height="30">
					 	<td colspan="4" rowspan="2" align="center" valign="middle" bgcolor="#E7F4E3">今日应投合计</td>
					 	<td colspan="2"  rowspan="2" align="center" valign="middle" bgcolor="#E7F4E3">未领货</td>
					 	<td colspan="2" rowspan="2" align="center" valign="middle" bgcolor="#E1F0FF">今日投递</td>
					 	<td colspan="26" valign="middle" bgcolor="#f1f1f1">今日结果</td>
					 	<td  colspan="2" rowspan="3" align="center" valign="middle" bgcolor="#FFE900">&nbsp;&nbsp;&nbsp;&nbsp;妥投率&nbsp;&nbsp;&nbsp;</td>
				 	</tr>
                        <tr class="font_1" height="30">
                          <td colspan="2" align="center" valign="middle" bgcolor="#E1F0FF">配送成功</td>
                            <td colspan="4" align="center" valign="middle" bgcolor="#E7F4E3">拒收</td>
                          <td align="center" valign="middle" bgcolor="#E1F0FF">滞留</td>
                            <td align="center" valign="middle" bgcolor="#E7F4E3">部分拒收</td>
                            <td align="center" valign="middle" bgcolor="#E1F0FF">中转</td>
                            <td colspan="2" align="center" valign="middle" bgcolor="#E7F4E3">上门退成功</td>
                            <td align="center" valign="middle" bgcolor="#E1F0FF">上门退拒退</td>
                            <td colspan="3" align="center" valign="middle" bgcolor="#E7F4E3">上门换成功</td>
                            <td align="center" valign="middle" bgcolor="#E1F0FF">上门换拒换</td>
                            <td align="center" valign="middle" bgcolor="#E7F4E3">丢失破损</td>
                            <td colspan="3" align="center" valign="middle" bgcolor="#E1F0FF">归班合计</td>
                            <td colspan="3" align="center" valign="middle" bgcolor="#FFE6E6">小件员未反馈</td>
                            <td colspan="3" align="center" valign="middle" bgcolor="#FFE6E6">小件员反馈未归班</td>
                        </tr>
                        <tr class="font_1" height="30">
                          <td align="center" valign="middle" bgcolor="#E7F4E3">今日到货（单）</td>
                          <td align="center" valign="middle" bgcolor="#E7F4E3">昨日滞留</td>
                          <td align="center" valign="middle" bgcolor="#E7F4E3">昨日到站未领</td>
                          <td align="center" valign="middle" bgcolor="#E7F4E3">昨日未归班</td>
                          <td align="center" valign="middle" bgcolor="#E7F4E3">今日未领货（单）</td>
                          <td align="center" valign="middle" bgcolor="#E7F4E3">昨日滞留（单）</td>
                          <td align="center" valign="middle" bgcolor="#E1F0FF">今日领货（单）</td>
                          <td align="center" valign="middle" bgcolor="#E7F4E3">昨日未归班</td>
                            <td align="center" valign="middle" bgcolor="#E1F0FF">单数</td>
                            <td align="center" valign="middle" bgcolor="#E1F0FF">应收金额（元）</td>
                            <td colspan="4" align="center" valign="middle" bgcolor="#E7F4E3">单数</td>
                          <td align="center" valign="middle" bgcolor="#E1F0FF">单数</td>
                            <td align="center" valign="middle" bgcolor="#E7F4E3">单数</td>
                            <td align="center" valign="middle" bgcolor="#E1F0FF">单数</td>
                            <td align="center" valign="middle" bgcolor="#E7F4E3">单数</td>
                            <td align="center" valign="middle" bgcolor="#E7F4E3">应退金额（元）</td>
                            <td align="center" valign="middle" bgcolor="#E1F0FF">单数</td>
                            <td align="center" valign="middle" bgcolor="#E7F4E3">单数</td>
                            <td align="center" valign="middle" bgcolor="#E7F4E3">应收金额（元）</td>
                            <td align="center" valign="middle" bgcolor="#E7F4E3">应退金额（元）</td>
                            <td align="center" valign="middle" bgcolor="#E1F0FF">单数</td>
                            <td align="center" valign="middle" bgcolor="#E7F4E3">单数</td>
                            <td align="center" valign="middle" bgcolor="#E1F0FF">单数</td>
                            <td align="center" valign="middle" bgcolor="#E1F0FF">应收金额（元）</td>
                            <td align="center" valign="middle" bgcolor="#E1F0FF">应退金额（元）</td>
                            <td align="center" valign="middle" bgcolor="#FFE6E6">单数</td>
                            <td align="center" valign="middle" bgcolor="#FFE6E6">应收金额（元）</td>
                            <td align="center" valign="middle" bgcolor="#FFE6E6">应退金额（元）</td>
                            <td align="center" valign="middle" bgcolor="#FFE6E6">单数</td>
                            <td align="center" valign="middle" bgcolor="#FFE6E6">应收金额（元）</td>
                            <td align="center" valign="middle" bgcolor="#FFE6E6">应退金额（元）</td>
                        </tr>
                        <tr height="30" id="todayDelivery">
                            <td align="center" valign="middle"><strong>
                            <a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/toudi_daohuo/1">0</a></strong></td>
                            <td align="center" valign="middle"><strong>
                            0</strong></td>
                            <td align="center" valign="middle"><strong>
                            0</strong></td>
                            <td align="center" valign="middle"><strong>
                            0</strong></td>
                            <td align="center" valign="middle"><strong>
                            <a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/toudi_daozhanweiling/1">0</a></strong></td>
                            <td align="center" valign="middle"><strong>
                            <a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/zuori_fankui_leijizhiliu/1">0</a></strong></td>
                            <td align="center" valign="middle"><strong>
                            <a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/toudi_linghuo/1">0</a></strong></td>
                            <td align="center" valign="middle"><strong>
                            0</strong></td>
                            <td align="center" valign="middle"><strong>
                           <a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/toudi_peisongcheng/1">0</a></strong></td>
                            
                            <td align="right" valign="middle"><strong>
                            <a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/toudi_peisongcheng/1">0</a></strong></td>
                            <td colspan="4" align="center" valign="middle"><strong>
                            <a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/toudi_jushou/1">0</a></strong></td>
                            <td align="center" valign="middle"><strong>
                            <a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/toudi_jinrizhiliu/1">0</a></strong></td>
                            <td align="center" valign="middle"><strong>
                            <a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/toudi_bufenjushou/1">0</a></strong></td>
                            <td align="center" valign="middle"><strong>
                            <a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/toudi_zhongzhuan/1">0</a></strong></td>
                            <td align="center" valign="middle"><strong>
                            <a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/toudi_shangmentuichenggong/1">0</a></strong></td>
                            <td align="right" valign="middle"><strong>
                            0</strong></td>
                            <td align="center" valign="middle"><strong>
                            <a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/toudi_shangmentuijutui/1">0</a></strong></td>
                            <td align="center" valign="middle"><strong>
                            <a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/toudi_shangmenhuanchenggong/1">0</a></strong></td>
                            <td align="right" valign="middle"><strong>
                            0</strong></td>
                            <td align="right" valign="middle"><strong>
                            0</strong></td>
                            <td align="center" valign="middle"><strong>
                            <a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/toudi_shangmenhuanjuhuan/1">0</a></strong></td>
                            <td align="center" valign="middle"><strong>
                            <a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/toudi_diushi/1">0</a></strong></td>
                            <td align="center" valign="middle"><strong>
                            <a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/toudi_fankuiheji/1">0</a></strong></td>
                            <td align="right" valign="middle"><strong>
                            0</strong></td>
                            <td align="right" valign="middle"><strong>
                            0</strong></td>
                            <td align="center" valign="middle"><strong>
                            <a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/toudi_weifankuiheji/1">0</a></strong></td>
                            <td align="right" valign="middle"><strong>
                            0</strong></td>
                            <td align="right" valign="middle"><strong>
                            0</strong></td>
                            <td align="center" valign="middle"><strong>
                            <a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/toudi_fankuiweiguiban_heji/1">0</a></strong></td>
                            <td align="right" valign="middle"><strong>
                            0</strong></td>
                            <td align="right" valign="middle"><strong>
                            0</strong></td>
                           
                            <td colspan="2" align="center" valign="middle"><strong>
                            0%</strong></td>
                            
                        </tr>
                    </tbody>
                </table>
            </div>
			</div>
         </li>
         
          <li style="display:none">
		  	<div class="right_title">
		  		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		  			<tbody>
		  				<tr class="font_1" height="30">
		  					<td colspan="10" align="center" valign="middle" bgcolor="#E1F0FF">实收款</td>
		  					<td colspan="5" align="center" valign="middle" bgcolor="#E7F4E3">实缴款</td>
	  					</tr>
		  				<tr class="font_1" height="30">
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#E7F4E3">配送成功（单）</td>
		  					<td colspan="3" align="center" valign="middle" bgcolor="#E7F4E3">实收金额（元）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFEFDF">上门退成功（单）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFE6E6">实付金额（元）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFEFDF">上门换成功（单）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFE6E6">实收金额（元）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFE6E6">实退金额（元）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFE900">实收合计（元）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#F8F3D3">现金（元）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#F8F3D3">POS（元）</td>
		  					<td colspan="3" rowspan="2" align="center" valign="middle" bgcolor="#F8F3D3">支票（元）</td>
	  					</tr>
		  				<tr height="30" class="font_1">
		  					<td height="36" align="center" valign="middle" bgcolor="#F8F3D3">现金（元）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">POS（元）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">支票（元）</td>
	  					</tr>
		  				<tr height="30" id="todayFunds">
		  					<td align="center" valign="middle"><strong>
		  					<a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/kuanxiang_peisongchenggong/1">0</a></strong></td>
		  					<td align="right" valign="middle"><strong>0</strong></td>
		  					<td align="right" valign="middle"><strong>0</strong></td>
		  					<td align="right" valign="middle"><strong>0</strong></td>
		  					<td align="center" valign="middle"><strong>
		  					<a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/kuanxiang_shangmentui/1">0</a></strong></td>
		  					<td align="right" valign="middle"><strong>0</strong></td>
		  					<td align="center" valign="middle"><strong>
		  					<a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/kuanxiang_shangmenhuan/1">0</a></strong></td>
		  					<td align="right" valign="middle"><strong>0</strong></td>
		  					<td align="right" valign="middle"><strong>0</strong></td>
		  					<td align="right" valign="middle"><strong>0</strong></td>
		  					<td align="right" valign="middle"><strong>
		  					<a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/kuanxiang_shijiaokuan/1">0</a></strong></td>
		  					<td align="right" valign="middle"><strong><a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/kuanxiang_shijiaokuan/1">0</a></strong></td>
		  					<td align="right" valign="middle"><strong><a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/kuanxiang_shijiaokuan/1">0</a></strong></td>
	  					</tr>
	  				</tbody>
	  			</table>
		  	</div>
         </li>
         
          <li style="display:none">
		  	<div class="right_title">
		  		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table3">
		  			<tbody>
		  				<tr class="font_1" height="30">
		  					<td colspan="4" align="center" valign="middle" bgcolor="#E1F0FF">昨日库存（单）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#E7F4E3">今日到货（单）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFEFDF">今日妥投（单）</td>
		  					<td colspan="2" align="center" valign="middle" bgcolor="#F8F3D3">今日出库（单）</td>
		  					<td colspan="4" align="center" valign="middle" bgcolor="#FFE6E6">今日库存（单）</td>
		  					<td rowspan="2"  align="center" valign="middle" bgcolor="#FFE6E6">货物丢失（单）</td>
	  					</tr>
		  				<tr class="font_1" height="30">
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">到站未领（单）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">退货未出站（单）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">滞留（单）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">小件员未归班（单）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">退货出库（单）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">中转出库（单）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">到站未领（单）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">退货未出站（单）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">滞留（单）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">小件员未归班（单）</td>
	  					</tr>
		  				<tr class="font_1" height="30" id="todayStock">
		  					<td align="center" valign="middle"><strong>
		  					0</strong></td>
		  					<td align="center" valign="middle"><strong>
		  					0</strong></td>
		  					<td align="center" valign="middle"><strong>
		  					0</strong></td>
		  					<td align="center" valign="middle"><strong>
		  					0</strong></td>
		  					<td align="center" valign="middle"><strong>
		  					<a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/kucun_daohuo/1">0</a></strong></td>
		  					<td align="center" valign="middle"><strong>
		  					<a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/kucun_tuotou/1">0</a></strong></td>
		  					<td align="center" valign="middle"><strong>
		  					<a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/kucun_tuihuochuku/1">0</a></strong></td>
		  					<td align="center" valign="middle"><strong>
		  					<a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/kucun_zhongzhuanchuku/1">0</a></strong></td>
		  					<td align="center" valign="middle"><strong>
		  					<a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/toudi_daozhanweiling/1">0</a></strong></td>
		  					<td align="center" valign="middle"><strong>
		  					<a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/jushou_kuicun/1">0</a></strong></td>
		  					<td align="center" valign="middle"><strong>
		  					<a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/zhiliu_kuicun/1">0</a></strong></td>
		  					<td align="center" valign="middle"><strong>
		  					<a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/weiguiban_kuicun/1">0</a></strong></td>
		  					<td align="center" valign="middle"><strong>
                            <a href="<%=request.getContextPath()%>/logtoday/showAjax/<%=nowbranch.getBranchid() %>/toudi_diushi/1">0</a></strong></td>
	  					</tr>
	  				</tbody>
	  			</table>
		  	</div>
         </li>
		</ul>
    </div>

<!-- 	<table width="100%" border="0" cellpadding="10" cellspacing="0" class="table_5">
				<tbody>
					<tr >
						<td align="center" valign="middle"><input name="button" type="button" class="input_button1" id="add_button" value="提交本站日志"></td>
					</tr>
				</tbody>
			</table> -->
			
	<input type="hidden" id="add" value="<%=request.getContextPath()%>/logtoday/save" />		
</div>
</div>
</body>
</html>