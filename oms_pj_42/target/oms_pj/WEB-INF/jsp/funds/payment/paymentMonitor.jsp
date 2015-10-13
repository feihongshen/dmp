
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.MonitorDTO"%>
<%@page import="cn.explink.util.*"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.dto.*"%>
 <%
 
 //MonitorDTO daoruDate =(MonitorDTO) request.getAttribute("daoruDate"); 
 //MonitorDTO chenggongDate =(MonitorDTO) request.getAttribute("chenggongDate"); 
 //MonitorDTO tuihuoDate =(MonitorDTO) request.getAttribute("tuihuoDate");
 //MonitorDTO zhiliuDate =(MonitorDTO) request.getAttribute("zhiliuDate"); 
  List<CarAmountStatiticsDTO> datalist =(List<CarAmountStatiticsDTO>) request.getAttribute("datalist");
 
 
 
 List<Branch> branchnameList =(List<Branch>) request.getAttribute("branchnameList");
 List<Customer> cumstrListAll =(List<Customer>) request.getAttribute("cumstrListAll"); 
 Customer customer =(Customer) request.getAttribute("customer");
 long customerid = Long.parseLong(request.getAttribute("customerid")==null?"0":request.getAttribute("customerid").toString());
 %>
    <html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<title>货款管理</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<%-- <link rel="stylesheet" href="<%=request.getContextPath()%>/css/omsTable.css" type="text/css"> --%>
<%-- <script type="text/javascript"	src="<%=request.getContextPath()%>/js/omsTable.js"></script> --%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/highcharts/highcharts.js"></script>
<script src="<%=request.getContextPath()%>/js/highcharts/exporting.js"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript">
function check(){
	if($("#strtime").val()>$("#endtime").val()){
		alert("开始时间不能大于结束时间");
		return false;
	}
	else{
		return true;
	}
}
</script>
<script type="text/javascript">

      $(document).ready(function() {
   //获取下拉框的值
   $("#btnval").click(function(){
       if(check()){
    	   $("#isshow").val(1);
        $("#searchForm").submit();
       }
   });
      });
   
</script>
<script>
$(function() {
	$("#strtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
});
</script>   

</head>

<body style="background:#eef9ff">

<div class="right_box">
	<div class="inputselect_box">
	<span>
	</span>
	<form  id="searchForm" action ="<%=request.getContextPath()%>/funds/payment" method = "post">
		供货商：<select id="customerid" name="customerid" >
		           <option value="-1" >全部</option>
		          <%if(cumstrListAll != null && cumstrListAll.size()>0){ %>
		           <%for( Customer c:cumstrListAll){ %>
		            <option value="<%= c.getCustomerid()%>" <%if(c.getCustomerid() == Integer.parseInt(request.getParameter("customerid")==null?"-1":request.getParameter("customerid").toString()) ) {%>selected="selected"<%}%>><%=c.getCustomername()%></option>
		           <%} }%>
        	   </select>
                 发货时间：<input type ="text" name ="emailStartTime" id="strtime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("emailStartTime")) %>">
                                到     <input type ="text" name ="eamilEndTime" id="endtime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("eamilEndTime")) %>">
                  导出状态：<select name ="exportType" id="exportType">
	               <option value="-1">不限 </option>
	               <option value ="0" <%if(0 == new Long(request.getParameter("exportType")==null?"0":request.getParameter("exportType"))) {%>selected="selected"<%} %>>未导出</option>
	               <option value ="1" <%if(1 == new Long(request.getParameter("exportType")==null?"0":request.getParameter("exportType"))) {%>selected="selected"<%} %>>导出未审核</option>
	               <option value ="2" <%if(2 == new Long(request.getParameter("exportType")==null?"0":request.getParameter("exportType"))) {%>selected="selected"<%} %>>导出已审核</option>
                </select> 
                 <input type="hidden" id="isshow" name="isshow" value="0" />  
		<input type="button" id="btnval" value="搜索" class="input_button2" />
	</form>
	 <input type="hidden" id="controlStr" name="controlStrCustomer" value=""/>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
    <% if(datalist!=null&&datalist.size()>0){ %>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">序号</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
			<td width="13%" align="center" valign="middle" bgcolor="#eef6ff">发货总数</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">应收金额</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">配送成功数量</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">配送成功金额</td>
			<!-- <td width="10%" align="center" valign="middle" bgcolor="#eef6ff">退货数量</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">退货金额</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">库存遗留数量</td>
			<td width="18%" align="center" valign="middle" bgcolor="#eef6ff">库存遗留金额</td> -->
		</tr>
		<%
		long totalcount=0;
		double totalamount=0;
		long successcount=0;
		double successamount=0;
		long kucuncount=0;
		double kucunamount=0;
		long backcount=0;
		double backamount=0;
		long i=0;
		for(CarAmountStatiticsDTO dt:datalist){
			i++;
			totalcount+=dt.getTotalcount();
			totalamount+=dt.getTotalamount();
			successcount+=dt.getSuccesscount();
			successamount+=dt.getSuccessamount();
			kucuncount+=dt.getKucuncount();
			kucunamount+=dt.getKucunamount();
			backcount+=dt.getBackcount();
			backamount+=dt.getBackamount();
		%>
		<tr>
			<td width="5%" align="center" valign="middle" ><%=i %></td>
			<td width="10%" align="center" valign="middle"><%=dt.getCustomername()%></td>              
			<td width="8%"  align="center" valign="middle">
				<a target="_self" href="<%=request.getContextPath()%>/funds/searchdetail/<%=FlowOrderTypeEnum.DaoRuShuJu.getValue()%>/<%=dt.getCustomerid() %>/1" ><%=dt.getTotalcount()%></a></td>              
			<td width="13%"  align="right" valign="middle">
				<a target="_self"  href="<%=request.getContextPath()%>/funds/searchdetail/<%=FlowOrderTypeEnum.DaoRuShuJu.getValue()%>/<%=dt.getCustomerid() %>/1"><%=dt.getTotalamount()%></a></td>
			<td width="10%"align="center" valign="middle" >
				<a target="_self"  href="<%=request.getContextPath()%>/funds/searchdetail/<%=FlowOrderTypeEnum.PeiSongChengGong.getValue()%>/<%=dt.getCustomerid() %>/1"><%=dt.getSuccesscount()%></a></td>             
			<td width="8%"  align="right" valign="middle">
				<a target="_self"  href="<%=request.getContextPath()%>/funds/searchdetail/<%=FlowOrderTypeEnum.PeiSongChengGong.getValue()%>/<%=dt.getCustomerid() %>/1"><%=dt.getSuccessamount()%></a></td>         
			<!-- <td width="10%"align="center" valign="middle" >
				<a target="_self"  href="<%=request.getContextPath()%>/funds/searchdetail/<%=FlowOrderTypeEnum.JuShou.getValue()%>/<%=dt.getCustomerid() %>/1"><%=dt.getBackcount()%></a></td>              
			<td width="8%"  align="center" valign="middle">
				<a target="_self"  href="<%=request.getContextPath()%>/funds/searchdetail/<%=FlowOrderTypeEnum.JuShou.getValue()%>/<%=dt.getCustomerid() %>/1"><%=dt.getBackamount()%></a></td>          
			<td width="10%"align="center" valign="middle" >
				<a target="_self"  href="<%=request.getContextPath()%>/funds/searchdetail/-1/<%=dt.getCustomerid() %>/1"><%=dt.getKucuncount()%></a></td>               
			<td width="18%"  align="center" valign="middle">
				<a target="_self"  href="<%=request.getContextPath()%>/funds/searchdetail/-1/<%=dt.getCustomerid() %>/1"><%=dt.getKucunamount()%></a></td>
				
				 -->           
		</tr>
		<%
		} %>
		<tr>
		    <td>合计：</td>
		    <td></td>
			<td><%=totalcount%>[票]</td>              
			<td align="right"><%=JMath.numstring(totalamount)%>[元]</td>
			<td><%=successcount%>[票]</td>             
			<td align="right"><%=JMath.numstring(successamount)%>[元]</td>         
			<!-- <td><%=backcount%>[票]</td>              
			<td><%=JMath.numstring(backamount)%>[元]</td>          
			<td><%=kucuncount%>[票]</td>               
			<td><%=JMath.numstring(kucunamount)%>[元]</td>                 
			 -->
	   </tr>
	</table>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
	<div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
					<input type ="button"  value="导出excel" class="input_button2" onclick="excelExp();"/>&nbsp;&nbsp;<input type ="button"  value="导出excel并审核" class="input_button1" onclick="excelExpAndCheck();"/>
				</td>
			</tr>
		</table>
	</div>
	<%} else{%>
	   <center>
	        <font color ="red">当前查询暂无数据！</font>
	   </center>
	<%} %>
</div>			
	<div class="jg_10"></div>
	<div class="clear"></div>
<script type="text/javascript">
function excelExp(){
		window.location.href="<%=request.getContextPath()%>/funds/paymentExp/1";
		return true;
}
function excelExpAndCheck(){
		window.location.href="<%=request.getContextPath()%>/funds/paymentExp/2";
		return true;
}

</script>
</body>
</html>






