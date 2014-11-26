<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.logdto.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>
<%
List<String> dateList = (List<String>)request.getAttribute("dateList"); 
List<Branch>  branchnameList = (List<Branch> )request.getAttribute("branchnameList"); 
Map<Long, Map<String, BranchTodayLog>> branchMap = (Map<Long, Map<String, BranchTodayLog>>)request.getAttribute("branchMap"); 
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>历史投递率</title>
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
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
	})
});

$(function() {
	$("#startdate").datepicker();
	$("#enddate").datepicker();
});
</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="right_box">
	<div class="inputselect_box" style="top: 0px; ">
			<form action="1" method="post" id="searchForm">
    选择日期： <input type ="text" name ="startdate" id="startdate" 
   value ="<%=request.getAttribute("startdate")==null ?
	 new SimpleDateFormat("yyyy-MM-dd").format(new Date()):request.getAttribute("startdate") %>">到
	 <input type ="text" name ="enddate" id="enddate" 
   value ="<%=request.getAttribute("enddate")==null ?
	 new SimpleDateFormat("yyyy-MM-dd").format(new Date()):request.getAttribute("enddate") %>">
			<input type="submit" class="input_button2" value="查看">
	  </form>
	</div>
	<div class="right_title">
	<div class="jg_35"></div>
	<div style="width:100%; overflow-x:scroll">
	<%if(dateList != null && dateList.size()>0){ %>
	<table width="<%=request.getAttribute("width")%>" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		<tbody>
	   	<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
	   		<td  rowspan="2" align="center" valign="middle" bgcolor="#eef6ff">站点</td>
	   		<%for(String date:dateList){ %>
	   		<td colspan="4" align="center" valign="middle" bgcolor="#eef6ff" ><%=date %></td>
	   		<%} %>
	   		<td  rowspan="2" align="center" valign="middle" bgcolor="#eef6ff">合计</td>
	   		</tr>
	   	<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
	   	<%for(String date:dateList){ %>
			<td  align="center" valign="middle" bgcolor="#eef6ff">应投</td>
			<td  align="center" valign="middle" bgcolor="#eef6ff">实投</td>
			<td  align="center" valign="middle" bgcolor="#eef6ff">妥投</td>
			<td  align="center" valign="middle" bgcolor="#eef6ff">妥投率</td>
		<%} %>	
			</tr>
		<%if(branchnameList != null && branchnameList.size()>0){ %>
		<%
				long jinriyingtou_countAllC=0;
				long today_fankui_linghuoAllC=0;
				long today_kucun_sucessAllC=0;
				Map<Long ,Map<String,Long>> yingtouMap = new  HashMap<Long ,Map<String,Long>>();
				Map<Long ,Map<String,Long>> shitoutouMap = new  HashMap<Long ,Map<String,Long>>();
				Map<Long ,Map<String,Long>> tuotouMap = new  HashMap<Long ,Map<String,Long>>();
				
				%>
		<%for(Branch b:branchnameList){ %>
		<tr style="background-color: rgb(249, 252, 253); ">
			<td align="center" valign="middle"><%=b.getBranchname() %></td>
			<%
			long  jinriyingtou_countAll =0;
			long  today_fankui_linghuoAll =0;
			long  today_kucun_sucessAll =0;
			Map<String ,Long> yingtou = new HashMap<String ,Long>();
	        Map<String ,Long> shitou = new HashMap<String ,Long>();
	        Map<String ,Long> tuotou = new HashMap<String ,Long>();
			%>
			<%for(String date:dateList){ %>
			<%if(branchMap != null && branchMap.size()>0){ %>
			<%
			long  jinriyingtou_count =(branchMap.get(b.getBranchid())==null?0:
			    branchMap.get(b.getBranchid()).get(date)==null?0:branchMap.get(b.getBranchid()).get(date).getJinriyingtou_count()
			    )+(branchMap.get(b.getBranchid())==null?0:
				    branchMap.get(b.getBranchid()).get(date)==null?0:branchMap.get(b.getBranchid()).get(date).getToday_wrong_arrive()
					    );
			long  today_fankui_linghuo =(branchMap.get(b.getBranchid())==null?0:
			    branchMap.get(b.getBranchid()).get(date)==null?0:branchMap.get(b.getBranchid()).get(date).getToday_fankui_linghuo())+
				    (branchMap.get(b.getBranchid())==null?0:
					    branchMap.get(b.getBranchid()).get(date)==null?0:branchMap.get(b.getBranchid()).get(date).getToday_fankui_zuoriweiguiban())+(branchMap.get(b.getBranchid())==null?0:
						    branchMap.get(b.getBranchid()).get(date)==null?0:branchMap.get(b.getBranchid()).get(date).getToday_wrong_arrive()
							    )   ;
			today_fankui_linghuo = today_fankui_linghuo>jinriyingtou_count?jinriyingtou_count:today_fankui_linghuo;
			long  today_kucun_sucess =branchMap.get(b.getBranchid())==null?0:
			    branchMap.get(b.getBranchid()).get(date)==null?0:branchMap.get(b.getBranchid()).get(date).getToday_kucun_sucess();
			%>
				<td align="center" valign="middle"><%=jinriyingtou_count %></td>
				<td align="center" valign="middle"><%=today_fankui_linghuo %></td>
				<td align="center" valign="middle"><%=today_kucun_sucess %></td>
				<td bgcolor="#FFE900" align="center" valign="middle">
				           <% 
				           
				           jinriyingtou_countAll += jinriyingtou_count  ;      
				           today_fankui_linghuoAll += today_fankui_linghuo  ;      
				           today_kucun_sucessAll += today_kucun_sucess  ; 
				           
				           jinriyingtou_countAllC += jinriyingtou_count  ;      
				           today_fankui_linghuoAllC += today_fankui_linghuo  ;      
				           today_kucun_sucessAllC += today_kucun_sucess  ; 
				           yingtou.put(date, jinriyingtou_count);
				           shitou.put(date, today_fankui_linghuo);
				           tuotou.put(date, today_kucun_sucess);
				           
                            BigDecimal b1 = new BigDecimal(today_kucun_sucess*100);
                            BigDecimal b2=  new BigDecimal(jinriyingtou_count);
                            out.print(jinriyingtou_count==0?"0" : 
                            	b1.divide(b2,2,BigDecimal.ROUND_HALF_UP).doubleValue());
                            %>%</td>
			<%}else{%>
				<td align="center" valign="middle">0</td>
				<td align="center" valign="middle">0</td>
				<td align="center" valign="middle">0</td>
				<td bgcolor="#FFE900" align="center" valign="middle">0%</td>
			<%} %>
			<% } %>
			<td bgcolor="#FFE900" align="center" valign="middle"><% 
                            BigDecimal b1 = new BigDecimal(today_kucun_sucessAll*100);
                            BigDecimal b2=  new BigDecimal(jinriyingtou_countAll);
                            out.print(jinriyingtou_countAll==0?"0" : 
                            	b1.divide(b2,2,BigDecimal.ROUND_HALF_UP).doubleValue());
                            %>%</td>
			</tr>
			<%
			yingtouMap.put(b.getBranchid(), yingtou);
			shitoutouMap.put(b.getBranchid(), shitou);
			tuotouMap.put(b.getBranchid(), tuotou);
			%>
		<%}%>
		<tr style="background-color: rgb(249, 252, 253); ">
				<td  align="center" valign="middle" bgcolor="#f1f1f1">合计</td>
				
				<%for(String date:dateList){
					long yingtouAll = 0;
					long shitouAll = 0;
					long tuotouAll = 0;
				for(Branch b:branchnameList){ 
				  yingtouAll += yingtouMap.get(b.getBranchid()).get(date);
				  shitouAll += shitoutouMap.get(b.getBranchid()).get(date);
				  tuotouAll += tuotouMap.get(b.getBranchid()).get(date);
				} %>
				<td align="center" valign="middle"><%=yingtouAll %></td>
				<td align="center" valign="middle"><%=shitouAll %></td>
				<td align="center" valign="middle"><%=tuotouAll %></td>
				<td bgcolor="#FFE900" align="center" valign="middle"><% 
					   
                            BigDecimal b1 = new BigDecimal(tuotouAll*100);
                            BigDecimal b2=  new BigDecimal(yingtouAll);
                            out.print(yingtouAll==0?"0" : 
                            	b1.divide(b2,2,BigDecimal.ROUND_HALF_UP).doubleValue());
                            %>%</td>
				<%
				}
				%>
				<td bgcolor="#FFE900" align="center" valign="middle" bgcolor="#f1f1f1"><% 
				                
                            BigDecimal b1 = new BigDecimal(today_kucun_sucessAllC*100);
                            BigDecimal b2=  new BigDecimal(jinriyingtou_countAllC);
                            out.print(jinriyingtou_countAllC==0?"0" : 
                            	b1.divide(b2,2,BigDecimal.ROUND_HALF_UP).doubleValue());
                            %>%</td>
			</tr>
		<% } %>
		  
		
		</tbody>
</table>
<%} %>
<div class="jg_35"></div>
</div>	
	</div>

</div>
</body>
</html>