
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.EmaildateTDO"%>
<%@page import="cn.explink.controller.MonitorDTO"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Branch"%>
 <%MonitorDTO daoruDate =(MonitorDTO) request.getAttribute("daoruDate"); %> 
 <%MonitorDTO youhuowudanDate =(MonitorDTO) request.getAttribute("youhuowudanDate"); %> 
 <%MonitorDTO youdanwuhuoDate =(MonitorDTO) request.getAttribute("youdanwuhuoDate"); %> 
 
 <% 
 List<Branch> kufangList =(List<Branch>) request.getAttribute("kufangList");
 List<Branch> branchnameList =(List<Branch>) request.getAttribute("branchnameList");
 List<Customer> cumstrListAll =(List<Customer>) request.getAttribute("cumstrListAll"); 
 List cumstrList1 =(List) request.getAttribute("cumstrList1");
 %>
    <html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<title>数据监控（数据组）</title>
<%-- <link rel="stylesheet" href="<%=request.getContextPath()%>/css/omsTable.css" type="text/css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/omsTable.js"></script> --%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
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

<script type="text/javascript">

        $(document).ready(function() {
            // Options displayed in comma-separated list
            $("#control_7").multiSelect({ oneOrMoreSelected: '*' });
   
   //获取下拉框的值
   $("#btnval").click(function(){
        var checkval="";
        $("label").each(function(index){
     //找到被选中的项
           if($(this).attr("class")=="checked"){
        checkval+=$(this).children().attr("value")+",";
     }
        });
        $("#controlStrCustomer").val(checkval);
        if(check()){
            $("#searchForm").submit();
            }
   });
        });
   
    </script>
   
<script type="text/javascript">
$(function () {
    var chart;
    $(document).ready(function() {
        chart = new Highcharts.Chart({
            chart: {
                renderTo: 'container',
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: '数据监控订单数量分布图'
            },
            tooltip: {
                formatter: function() {
                    return '<b>'+ this.point.name +'</b>: '+ this.percentage.toFixed(2) +' %';
                }
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        color: '#000000',
                        connectorColor: '#000000',
                        formatter: function() {
                            return '<b>'+ this.point.name +'</b>: '+ this.percentage.toFixed(2) +' %';
                        }
                    }
                }
            },
            series: [{
                type: 'pie',
                name: 'Browser share',
                data: [
                    ['正常单',       <%=daoruDate.getCountsum()-youhuowudanDate.getCountsum()-youdanwuhuoDate.getCountsum()%>],
                    ['有货无单',       <%=youhuowudanDate.getCountsum()%>],
                    {
                        name: '有单无货',
                        y: <%=youdanwuhuoDate.getCountsum()%>,
                        sliced: true,
                        selected: true
                    }
                ]
            }]
        });
    });
    
});
		</script>
</head>
<form id="searchForm" action ="<%=request.getContextPath()%>/monitor/date/2" method = "post">
		库房/站点：<select name ="branchid" id="branid">
		            <option value ="-1">全部</option>
	              <%if(kufangList != null){ %>
	               <%for( Branch b:kufangList){ %>
	               		<option value ="<%=b.getBranchid()%>" 
	               		<%if(b.getBranchid() == 
	               		new Long(request.getParameter("branchid")==null?"-1":request.getParameter("branchid")) ||
	               		b.getBranchid() == 
	               		new Long(request.getAttribute("branchid")==null?"-1":request.getAttribute("branchid").toString())
	               				) 
	               		
	               		{%>selected="selected"<%} %>><%=b.getBranchname() %>[库房]</option>
	               <%} }%>
	               <%if(branchnameList != null){ %>
	               <%for( Branch b:branchnameList){ %>
	               		<option value ="<%=b.getBranchid()%>" 
	               		<%if(b.getBranchid() == new Long(request.getParameter("branchid")==null?"-1":request.getParameter("branchid"))||
	               		b.getBranchid() ==  new Long(request.getAttribute("branchid")==null?"-1":request.getAttribute("branchid").toString())
	               				) {%>selected="selected"<%} %>><%=b.getBranchname() %>[站点]</option>
	               <%}} %>
	              </select>
		  供货商（可多选）：<select id="control_7" name="control_7[]" multiple="multiple" style="width: 320px;">
					          <%if(cumstrListAll != null && cumstrListAll.size()>0){ %>
					           <%for( Customer c:cumstrListAll){ %>
					            <option value="<%= c.getCustomerid()%>" <%if(!cumstrList1.isEmpty()) 
						            {for(int i=0;i<cumstrList1.size();i++){
						            	if(c.getCustomerid() == new Long(cumstrList1.get(i).toString())){
						            		%>selected="selected"<%
						            	 break;
						            	}
						            }
					             }%>><%=c.getCustomername()%></option>
					            <%}} %>
					        </select>
                                           发货时间：<input type ="text" name ="emailStartTime" id="strtime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("emailStartTime")) %>">
                                                            到   <input type ="text" name ="eamilEndTime" id="endtime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("eamilEndTime")) %>">
        <input type ="button" id="btnval" value="搜索" class="input_button2" />
		<input type="hidden" id="controlStrCustomer" name="controlStrCustomer" value=""/>
		<input type ="button" id="btnval" value="导出excel" class="input_button2" onclick="javascript:location.href='<%=request.getContextPath()%>/monitor/dateExport/2'"/>
	</form>
<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<span>
	</span>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
			<td colspan="2" width="33%" align="center" valign="middle" bgcolor="#eef6ff">导入数据</td>
			<td colspan="2" width="33%" align="center" valign="middle" bgcolor="#eef6ff">有货无单</td>
			<td colspan="2" width="34%" align="center" valign="middle" bgcolor="#eef6ff">有单无货</td>
	   </tr>
		<tr>
			<td  width="10%" align="center" valign="middle">单数</td>
			<td  width="23%" align="center" valign="middle">金额</td>
			<td  width="10%" align="center" valign="middle">单数</td>
			<td  width="23%" align="center" valign="middle">金额</td>
			<td  width="10%" align="center" valign="middle">单数</td>
			<td  width="24%" align="center" valign="middle">金额</td>
		</tr>
		<tr>
			<td><%=daoruDate.getCountsum()%></td>              
			<td><%=daoruDate.getCaramountsum()%></td>
			<td><a href="<%=request.getContextPath()%>/monitor/dateshow/2/1?flowType=<%=request.getAttribute("youhuowudanDateType")%>"><%=youhuowudanDate.getCountsum()%></a></td>             
			<td><%=youhuowudanDate.getCaramountsum()%></td>         
			<td><a href="<%=request.getContextPath()%>/monitor/dateshow/2/1?flowType=<%=request.getAttribute("youdanwuhuoDateType")%>"><%=youdanwuhuoDate.getCountsum()%></a></td>              
			<td><%=youdanwuhuoDate.getCaramountsum()%></td>          
	    </tr>
	</table>
		<div id="container" style="min-width: 600px; height: 450px; margin: 0 auto"></div>
	<div class="jg_10"></div><div class="jg_10"></div>
</div>		
	<div class="jg_10"></div>
	<div class="clear"></div>
</body>
</html>
