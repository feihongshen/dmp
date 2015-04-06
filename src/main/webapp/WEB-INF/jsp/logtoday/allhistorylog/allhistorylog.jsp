<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.logdto.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>站点历史日志</title>
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
</head>
<%
List<BranchTodayLog> todayLogList  = (List<BranchTodayLog>)request.getAttribute("todayLogList");
Map branchMap = (Map)request.getAttribute("branchMap");
Map<Long,BranchTodayLog> branchAndTomorrow = (Map<Long,BranchTodayLog>)request.getAttribute("branchAndTomorrow");
Page page_obj = (Page)request.getAttribute("page_obj");
List<Branch> branchnameList =(List<Branch>) request.getAttribute("branchnameList");
Branch nowBranch = request.getAttribute("nowBranch")==null?null:(Branch)request.getAttribute("nowBranch");
%>
<script>
function dgetViewBox(key,durl){
	window.parent.getViewBoxd(key,durl);
}
</script>
<script>
$(function() {
	$("#createdate").datepicker();
});
</script>
<script type="text/javascript">
function addInit(){
	//无处理
}
function addSuccess(data){
	//无处理
}
function editInit(){
	//无处理
}
function editSuccess(data){
	//无处理
}
function delSuccess(data){
	//无处理
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
</head>
<body style="background:#f5f5f5;overflow-y:hidden" marginwidth="0" marginheight="0" >
<div class="right_box">
	<div class="inputselect_box" style="top: 0px; ">
		<form action="" method="post" id="searchForm">
		  &nbsp;&nbsp;选择站点：
			<select name ="branchid" id="branid">
            <%String nowzhandian="全部"; %>
            <%if(nowBranch !=null){ %>
            <option value ="<%=nowBranch.getBranchid()%>"><%=nowBranch.getBranchname() %><%nowzhandian=nowBranch.getBranchname();%></option>
            <%}else{ %>
		            <option value ="-1">全部</option>
	              <%if(branchnameList != null && branchnameList.size()>0){ %>
	               <%for( Branch b:branchnameList){ %>
	               		<option value ="<%=b.getBranchid()%>" <%if(b.getBranchid() == new Long(request.getAttribute("branchidSession")==null?"-1":request.getAttribute("branchidSession").toString())) {%>selected="selected"<%} %>><%=b.getBranchname() %></option>
	               <%} }}%>
	              </select>
选择日期： <input type ="text" name ="createdate" id="createdate" 
 value ="<%=request.getAttribute("createdate")==null || "".equals(request.getAttribute("createdate"))?
	 new SimpleDateFormat("yyyy-MM-dd").format(new Date()):request.getAttribute("createdate") %>">
			<input type="submit" class="input_button2" id="chakan" value="查看">
			<%if(todayLogList!=null&&todayLogList.size()>0){ %>  
				<input type="button" class="input_button2" value="导出" id="daochu">
			<%} %>
	  </form>
	</div>
	<div class="right_title">
	<div class="jg_35"></div>
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table4">
				<tr class="font_1">
					<td bgcolor="#F3F3F3">站点</td>
					<td bgcolor="#E7F4E3">统计</td>
					<td bgcolor="#E1F0FF">昨日库存</td>
					<td bgcolor="#FFEFDF">今日到货</td>
					<td bgcolor="#FFE6E6">今日应投</td>
					<td bgcolor="#E7F4E3">今日妥投</td>
					<td bgcolor="#E1F0FF">今日出库</td>
					<td bgcolor="#FFEFDF">今日库存</td>
					<td bgcolor="#FFEFDF">今日拒收</td>
					<td bgcolor="#FFEFDF">分拣错误及转址</td>
					<td bgcolor="#FFEFDF">次日应投量</td>
					<td bgcolor="#FFE6E6">今日投递率</td>
					<td bgcolor="#F3F3F3">操作</td>
				</tr>
				 <%if(todayLogList!=null){ %>  
                  <%for(BranchTodayLog branchTodayLog : todayLogList) {%> 
                  <%long yingtoucount = (branchTodayLog.getJinriyingtou_count()<branchTodayLog.getToday_fankui_linghuo()?branchTodayLog.getToday_fankui_linghuo():branchTodayLog.getJinriyingtou_count() )+branchTodayLog.getToday_wrong_arrive(); %>
				<tr>
					<td rowspan="2" align="center" valign="middle" bgcolor="#F3F3F3"><strong><%=branchMap.get(branchTodayLog.getBranchid()) %></strong></td>
					<td align="center" valign="middle">数量（单）</td>
					<td align="center" valign="middle"><%=branchTodayLog.getZuorikucun_count() %></td>
					<td align="center" valign="middle"><%=branchTodayLog.getJinridaohuo_count()+branchTodayLog.getToday_wrong_arrive() %></td>
					<td align="center" valign="middle"><%=yingtoucount %></td>
					<td align="center" valign="middle"><%=branchTodayLog.getJinrishoukuan_count() %></td>
					<td align="center" valign="middle"><%=branchTodayLog.getJinrichuku_count() %></td>
					<td align="center" valign="middle"><%=branchTodayLog.getJinrikucun_count() %></td>
					<td align="center" valign="middle"><%=branchTodayLog.getToday_fankui_jushou()+branchTodayLog.getToday_fankui_bufenjushou()  %></td>
					<td align="center" valign="middle"><%=branchTodayLog.getToday_fankui_zhobgzhuan() %></td>
					<td align="center" valign="middle"><%=branchAndTomorrow.get(branchTodayLog.getBranchid())==null?"0":branchAndTomorrow.get(branchTodayLog.getBranchid()).getJinriyingtou_count() %></td>
					<td rowspan="2" align="center" valign="middle"><strong> <% 
                            BigDecimal b1 = new BigDecimal(
                    					(branchTodayLog.getJinrishoukuan_count())*100);
                            BigDecimal b2=  new BigDecimal(yingtoucount );
                            out.print((branchTodayLog.getJinriyingtou_count()) ==0?"0" : 
                            	b1.divide(b2,2,BigDecimal.ROUND_HALF_UP).doubleValue());
                            %>%</strong></td>
					<td rowspan="2" align="center" valign="middle"><a href="javascript:getSetExportViewBox(<%=branchTodayLog.getId()%>);" >查看详情</a></td>
				</tr>
				
				<tr>
					<td align="center" valign="middle">金额（元）</td>
					<td align="center" valign="middle"><strong><%=branchTodayLog.getZuorikucun_money() %></strong></td>
					<td align="center" valign="middle"><strong><%=branchTodayLog.getJinridaohuo_money() %></strong></td>
					<td align="center" valign="middle"><strong><%=branchTodayLog.getJinriyingtou_money() %></strong></td>
					<td align="center" valign="middle"><strong><%=branchTodayLog.getJinrishoukuan_money() %></strong></td>
					<td align="center" valign="middle"><strong><%=branchTodayLog.getJinrichuku_money() %></strong></td>
					<td align="center" valign="middle"><strong><%=branchTodayLog.getJinrikucun_money() %></strong></td>
					<td align="center" valign="middle"><strong>-</strong></td>
					<td align="center" valign="middle"><strong>-</strong></td>
					<td align="center" valign="middle"><strong><%=branchAndTomorrow.get(branchTodayLog.getBranchid())==null?"0":branchAndTomorrow.get(branchTodayLog.getBranchid()).getJinriyingtou_money() %></strong></td>
				</tr>
				<%} }%>
				
			</table>
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
	$("#chakan").click(function(){
		$("#searchForm").attr("action","<%=request.getContextPath() %>/logtoday/allhistorylog/1");
		$("#searchForm").submit();
		
	});
	$("#daochu").click(function(){
		$("#searchForm").attr("action","<%=request.getContextPath() %>/logtoday/exporttable");
		$("#searchForm").submit();
	});
	</script>
	<input type="hidden" id="view" value="<%=request.getContextPath()%>/logtoday/show/" />
</div>
</body>
</html>