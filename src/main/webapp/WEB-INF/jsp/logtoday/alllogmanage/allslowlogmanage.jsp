<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.logdto.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.util.Page"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>今日站点日志</title>
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
</head>
<%
List<BranchTodayLog> todayLogList  = (List<BranchTodayLog>)request.getAttribute("todayLogList");
Map branchMap = (Map)request.getAttribute("branchMap");
Page page_obj = (Page)request.getAttribute("page_obj");
List<Branch> branchnameList =(List<Branch>) request.getAttribute("branchnameList");
Branch nowBranch = request.getAttribute("nowBranch")==null?null:(Branch)request.getAttribute("nowBranch");

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
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="right_box">
<div class="menucontant">
<div class="inputselect_box" style="top: 0px; ">
		<form action="1" method="post" id="searchForm">
		&nbsp;&nbsp;选择站点：
			<select name ="branchid" id="branid"  onchange="$('#searchForm').submit()">
            <%String nowzhandian="全部"; %>
            <%if(nowBranch !=null){ %>
            <option value ="<%=nowBranch.getBranchid()%>"><%=nowBranch.getBranchname() %><%nowzhandian=nowBranch.getBranchname();%></option>
            <%}else{ %>
		            <option value ="-1">全部</option>
	              <%if(branchnameList != null && branchnameList.size()>0){ %>
	               <%for( Branch b:branchnameList){ %>
	               		<option value ="<%=b.getBranchid()%>" <%if(b.getBranchid() == new Long(request.getAttribute("branchidSession")==null?"-1":request.getAttribute("branchidSession").toString())) {%>selected="selected"<%} %>><%=b.getBranchname() %></option>
	               <%} }}%>
	              </select>  &nbsp;&nbsp;当前日期：<%=new SimpleDateFormat("yyyy-MM-dd").format(new Date()) %>
		</form>
	</div>
	<div class="jg_35"></div>
	<div class="uc_midbg">
		<ul>
			<li><a href="<%=request.getContextPath()%>/logtoday/todayArrivalLog/1">今日到货日报</a></li>
			<li><a href="<%=request.getContextPath()%>/logtoday/alldeliverlogmanage/1">今日投递日报</a></li>
			<li><a href="#" class="light">今日款项日报</a></li>
			<li><a href="<%=request.getContextPath()%>/logtoday/allstocklogmanage/1">今日库存日报</a></li>
		</ul>
	</div>
    <div class="tabbox">
    
      <li>
		  	<div class="right_title">
		  		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		  			<tbody>
		  				<tr class="font_1" height="30">
		  					<td rowspan="3" align="center" valign="middle" bgcolor="#F3F3F3">站点</td>
		  					<td colspan="10" align="center" valign="middle" bgcolor="#E1F0FF">实收款</td>
		  					<td colspan="5" align="center" valign="middle" bgcolor="#E7F4E3">实缴款</td>
	  					</tr>
		  				<tr class="font_1" height="30">
		  				  <td rowspan="2" align="center" valign="middle" bgcolor="#E7F4E3">配送成功（单）</td>
		  					<td colspan="3" align="center" valign="middle" bgcolor="#E7F4E3">实收金额（元）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFEFDF">上门退成功</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFE6E6">实付金额（元）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFEFDF">上门换成功（单）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFE6E6">实收金额（元）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFE6E6">实退金额（元）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#FFE6E6">实收合计（元）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#F8F3D3">现金（元）</td>
		  					<td rowspan="2" align="center" valign="middle" bgcolor="#F8F3D3">POS（元）</td>
		  					<td colspan="3" rowspan="2" align="center" valign="middle" bgcolor="#F8F3D3">支票（元）</td>
	  					</tr>
		  				<tr height="30" class="font_1">
		  					<td height="36" align="center" valign="middle" bgcolor="#F8F3D3">现金（元）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">POS（元）</td>
		  					<td align="center" valign="middle" bgcolor="#F8F3D3">支票（元）</td>
	  					</tr>
	  					<%if(todayLogList!=null){ %>  
                         <%for(BranchTodayLog branchTodayLog : todayLogList) {%> 
		  				<tr height="30">
		  				  <td align="center" valign="middle"><strong><%=branchMap.get(branchTodayLog.getBranchid()) %></strong></td>
		  					<td align="center" valign="middle"><strong><%=branchTodayLog.getToday_funds_peisongchenggong_count() %></strong></td>
		  					<td align="right" valign="middle"><strong><%=branchTodayLog.getToday_funds_peisongchenggong_cash() %></strong></td>
		  					<td align="right" valign="middle"><strong><%=branchTodayLog.getToday_funds_peisongchenggong_pos() %></strong></td>
		  					<td align="right" valign="middle"><strong><%=branchTodayLog.getToday_funds_peisongchenggong_checkfee() %></strong></td>
		  					<td align="center" valign="middle"><strong><%=branchTodayLog.getToday_funds_shangmentuichenggong_count() %></strong></td>
		  					<td align="right" valign="middle"><strong><%=branchTodayLog.getToday_funds_shangmentuichenggong_money() %></strong></td>
		  					<td align="center" valign="middle"><strong><%=branchTodayLog.getShangmenhuanchenggong() %></strong></td>
		  					<td align="right" valign="middle"><strong><%=branchTodayLog.getShangmenhuanchenggong_yingshou_amount() %></strong></td>
		  					<td align="right" valign="middle"><strong><%=branchTodayLog.getShangmenhuanchenggong_yingtui_amount() %></strong></td>
		  					<td align="right" valign="middle"><strong><%=branchTodayLog.getShishou_sum_amount() %></strong></td>
		  					<td align="right" valign="middle"><strong><%=branchTodayLog.getToday_funds_shangmentuichenggong_cash() %></strong></td>
		  					<td align="right" valign="middle"><strong><%=branchTodayLog.getToday_funds_shangmentuichenggong_pos() %></strong></td>
		  					<td align="right" valign="middle"><strong><%=branchTodayLog.getToday_funds_shangmentuichenggong_checkfee() %></strong></td>
	  					</tr>
	  					<%} }%>
	  				</tbody>
	  			</table>
	  	</div>
        </li>
    </div>
			
</div>
<form action="1" method="post" id="searchForm"></form>
<div class="jg_35"></div>
	<%if(page_obj.getMaxpage()>1){ %>
	<div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
					<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
					　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
							id="selectPg"
							onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
							<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
							<option value="<%=i %>"><%=i %></option>
							<% } %>
						</select>页
				</td>
			</tr>
		</table>
	</div>
    <%} %>
    <script type="text/javascript">
	$("#selectPg").val(<%=request.getAttribute("page") %>);
	</script>
</div>
</body>
</html>