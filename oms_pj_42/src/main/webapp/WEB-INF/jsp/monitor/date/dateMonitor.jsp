
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.EmaildateTDO"%>
<%@page import="cn.explink.controller.MonitorDTO"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
 <%List<EmaildateTDO> emailList =(List<EmaildateTDO>) request.getAttribute("emailListByEmailTime"); %> 
 <%MonitorDTO daoruDate =(MonitorDTO) request.getAttribute("daoruDate"); %> 
 <%MonitorDTO kufangDate =(MonitorDTO) request.getAttribute("kufangDate"); %> 
 <%MonitorDTO zaituDate =(MonitorDTO) request.getAttribute("zaituDate"); %> 
 <%MonitorDTO zhanDate =(MonitorDTO) request.getAttribute("zhanDate"); %> 
 <%MonitorDTO renDate =(MonitorDTO) request.getAttribute("renDate"); %> 
 <%MonitorDTO tuihuoDate =(MonitorDTO) request.getAttribute("tuihuoDate"); %> 
 <%MonitorDTO zhongzhuanDate =(MonitorDTO) request.getAttribute("zhongzhuanDate"); %> 
 <%MonitorDTO chenggongDate =(MonitorDTO) request.getAttribute("chenggongDate"); %> 
 <%MonitorDTO diushiDate =(MonitorDTO) request.getAttribute("diushiDate"); %> 
 <%MonitorDTO yichangDate =(MonitorDTO) request.getAttribute("yichangDate"); %> 
 <%MonitorDTO chaDate =(MonitorDTO) request.getAttribute("chaDate"); %> 
 
 <%
 
 List<Branch> kufangList =(List<Branch>) request.getAttribute("kufangList");
 List<Branch> branchnameList =(List<Branch>) request.getAttribute("branchnameList");
 %> 
    
    
    <html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<title>数据监控</title>
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
        $("#controlStr").val(checkval);
        if(check()){
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
                    ['库房',   <%=kufangDate.getCountsum()%>],
                    ['在途',       <%=zaituDate.getCountsum()%>],
                    ['站点',       <%=zhanDate.getCountsum()%>],
                    ['小件员',      <%=renDate.getCountsum()%>],
                    ['退货站',       <%=tuihuoDate.getCountsum()%>],
                    ['中转站',      <%=zhongzhuanDate.getCountsum()%>],
                    ['丢失',       <%=diushiDate.getCountsum()%>],
                    ['异常',       <%=yichangDate.getCountsum()%>],
                    {
                        name: '成功',
                        y: <%=chenggongDate.getCountsum()%>,
                        sliced: true,
                        selected: true
                    },
                    ['差',   <%=chaDate.getCountsum()%>]
                ]
            }]
        });
    });
    
});
$(function () {
    var chart;
    $(document).ready(function() {
        chart = new Highcharts.Chart({
            chart: {
                renderTo: 'container2',
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: '数据监控订单金额分布图'
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
                    ['库房',   <%=kufangDate.getCaramountsum()%>],
                    ['在途',       <%=zaituDate.getCaramountsum()%>],
                    ['站点',       <%=zhanDate.getCaramountsum()%>],
                    ['小件员',      <%=renDate.getCaramountsum()%>],
                    ['退货站',       <%=tuihuoDate.getCaramountsum()%>],
                    ['中转站',      <%=zhongzhuanDate.getCaramountsum()%>],
                    ['丢失',       <%=diushiDate.getCaramountsum()%>],
                    ['异常',       <%=yichangDate.getCaramountsum()%>],
                    {
                        name: '成功',
                        y: <%=chenggongDate.getCaramountsum()%>,
                        sliced: true,
                        selected: true
                    },
                    ['差',   <%=chaDate.getCaramountsum()%>]
                ]
            }]
        });
    });
    
});
		</script>
</head>
<form id="searchForm" action ="<%=request.getContextPath()%>/monitor/date/1" method = "post">
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
		  发货时间：<input type ="text" name ="emailStartTime" id="strtime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("emailStartTime")) %>">  
                                                            到   <input type ="text" name ="eamilEndTime" id="endtime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("eamilEndTime")) %>">
		  批次号： <select id="control_7" name="control_7[]" multiple="multiple" style="width: 320px;">
					           <%if(emailList != null && emailList.size()>0){ %>
					            <%for( EmaildateTDO e:emailList){ %>
					            <option value="<%= e.getEmaildateid()%>"
					            <%if((request.getParameter("controlStrEmail")==null?"":request.getParameter("controlStrEmail")).indexOf(e.getEmaildate()==null?"F":e.getEmaildate()) > -1 ){ %> selected="selected"<%} %>
					            ><%=e.getCustomername()%>-<%=e.getEmaildate()%></option>
					            <%}} %>
				           </select>
                                          
		<input type ="button" id="btnval" value="搜索" class="input_button2" />
		<input type="hidden" id="controlStr" name="controlStrEmail" value=""/>
		<input type ="button" id="btnval" value="导出excel" class="input_button2" onclick="javascript:location.href='<%=request.getContextPath()%>/monitor/dateExport/1'"/>
	</form>
<body style="background:#eef9ff">
<div class="right_box">
	<div class="right_title">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
			<td colspan="2" width="10%" align="center" valign="middle" bgcolor="#eef6ff">导入数据</td>
			<td colspan="2" width="9%" align="center" valign="middle" bgcolor="#eef6ff">库房</td>
			<td colspan="2" width="9%" align="center" valign="middle" bgcolor="#eef6ff">在途</td>
			<td colspan="2" width="9%" align="center" valign="middle" bgcolor="#eef6ff">站点</td>
			<td colspan="2" width="9%" align="center" valign="middle" bgcolor="#eef6ff">小件员</td>
			<td colspan="2" width="9%" align="center" valign="middle" bgcolor="#eef6ff">退货站</td>
			<td colspan="2" width="9%" align="center" valign="middle" bgcolor="#eef6ff">中转站</td>
			<td colspan="2" width="9%" align="center" valign="middle" bgcolor="#eef6ff">成功</td>
			<td colspan="2" width="9%" align="center" valign="middle" bgcolor="#eef6ff">丢失</td>
			<td colspan="2" width="9%" align="center" valign="middle" bgcolor="#eef6ff">异常</td>
			<td colspan="2" width="9%" align="center" valign="middle" bgcolor="#eef6ff">差</td>
		</tr>
		<tr>
			<td width="5%"align="center" valign="middle">单数</td>
			<td width="5%" align="center" valign="middle">金额</td>
			<td width="4%" align="center" valign="middle">单数</td>
			<td width="5%" align="center" valign="middle">金额</td>
			<td width="4%" align="center" valign="middle">单数</td>
			<td width="5%" align="center" valign="middle">金额</td>
			<td width="4%" align="center" valign="middle">单数</td>
			<td width="5%" align="center" valign="middle">金额</td>
			<td width="4%" align="center" valign="middle">单数</td>
			<td width="5%" align="center" valign="middle">金额</td>
			<td width="4%" align="center" valign="middle">单数</td>
			<td width="5%" align="center" valign="middle">金额</td>
			<td width="4%"  align="center" valign="middle">单数</td>
			<td width="5%"  align="center" valign="middle">金额</td>
			<td width="4%"  align="center" valign="middle">单数</td>
			<td width="5%"  align="center" valign="middle">金额</td>
			<td width="4%"  align="center" valign="middle">单数</td>
			<td width="5%"  align="center" valign="middle">金额</td>
			<td width="4%"  align="center" valign="middle">单数</td>
			<td width="5%"  align="center" valign="middle">金额</td>
			<td width="4%"  align="center" valign="middle">单数</td>
			<td width="5%"  align="center" valign="middle">金额</td>
		</tr>
		<tr>
			<td><%=daoruDate.getCountsum()%></td>              
			<td><%=daoruDate.getCaramountsum()%></td>
			<td><a href="<%=request.getContextPath()%>/monitor/dateshow/1/1?flowType=<%=request.getAttribute("kufangDateType")%>"><%=kufangDate.getCountsum()%></a></td>             
			<td><%=kufangDate.getCaramountsum()%></td>         
			<td><a href="<%=request.getContextPath()%>/monitor/dateshow/1/1?flowType=<%=request.getAttribute("zaituDateType")%>"><%=zaituDate.getCountsum()%></a></td>              
			<td><%=zaituDate.getCaramountsum()%></td>          
			<td><a href="<%=request.getContextPath()%>/monitor/dateshow/1/1?flowType=<%=request.getAttribute("zhanDateType")%>"><%=zhanDate.getCountsum()%></a></td>               
			<td><%=zhanDate.getCaramountsum()%></td>           
			<td><a href="<%=request.getContextPath()%>/monitor/dateshow/1/1?flowType=<%=request.getAttribute("renDateType")%>"><%=renDate.getCountsum()%></a></td>                
			<td><%=renDate.getCaramountsum()%></td>            
			<td><a href="<%=request.getContextPath()%>/monitor/dateshow/1/1?flowType=<%=request.getAttribute("tuihuoDateType")%>"><%=tuihuoDate.getCountsum()%></a></td>             
			<td><%=tuihuoDate.getCaramountsum()%></td>         
			<td><a href="<%=request.getContextPath()%>/monitor/dateshow/1/1?flowType=<%=request.getAttribute("zhongzhuanDateType")%>"><%=zhongzhuanDate.getCountsum()%></a></td>         
			<td><%=zhongzhuanDate.getCaramountsum()%></td>     
			<td><a href="<%=request.getContextPath()%>/monitor/dateshow/1/1?flowType=<%=request.getAttribute("chenggongDateType")%>"><%=chenggongDate.getCountsum()%></a></td>          
			<td><%=chenggongDate.getCaramountsum()%></td>      
			<td><a href="<%=request.getContextPath()%>/monitor/dateshow/1/1?flowType=<%=request.getAttribute("diushiDateType")%>"><%=diushiDate.getCountsum()%></a></td>             
			<td><%=diushiDate.getCaramountsum()%></td>         
			<td><a href="<%=request.getContextPath()%>/monitor/dateshow/1/1?flowType=<%=request.getAttribute("yichangDateType")%>"><%=yichangDate.getCountsum()%></a></td>            
			<td><%=yichangDate.getCaramountsum()%></td>
			<td><a href="<%=request.getContextPath()%>/monitor/dateshow/1/1?flowType=<%=request.getAttribute("chaDateType")%>"><%=chaDate.getCountsum()%></a></td>
			<td><%=chaDate.getCaramountsum()%></td>
	</tr>
	</table>
		<div id="container" style="min-width: 600px; height: 450px; margin: 0 auto"></div>
	    <div id="container2" style="min-width: 600px; height: 450px; margin: 0 auto"></div>
	<div class="jg_10"></div><div class="jg_10"></div>
</div>
			
	<div class="jg_10"></div>

	<div class="clear"></div>
	</div>
</body>
</html>






