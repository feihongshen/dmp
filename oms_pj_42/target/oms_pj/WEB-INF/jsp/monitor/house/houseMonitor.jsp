<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.controller.MonitorView"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.MonitorDTO"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Branch"%>
 <% List<MonitorView> monitorView=(List<MonitorView>) request.getAttribute("houseviewlist"); %> 

<%--  <% List<MonitorDTO> daoruDate=(List<MonitorDTO>) request.getAttribute("rukuzongliangDate"); %>  --%>
<%--  <%MonitorDTO weirukuDate =(MonitorDTO) request.getAttribute("weirukuDate"); %> 
 <%MonitorDTO yirukuDate =(MonitorDTO) request.getAttribute("yirukuDate"); %> 
 <%MonitorDTO youdanwuhuoDate =(MonitorDTO) request.getAttribute("youdanwuhuoDate"); %> 
 <%MonitorDTO youhuowudanDate =(MonitorDTO) request.getAttribute("youhuowudanDate"); %> 
 <%MonitorDTO chukuzaituDate =(MonitorDTO) request.getAttribute("chukuzaituDate"); %> 
 <%MonitorDTO kucunDate =(MonitorDTO) request.getAttribute("kucunDate"); %>  --%>
 <% 
    List<Branch> kufangList =(List<Branch>) request.getAttribute("kufangList");
/*  Branch nowBranch = request.getAttribute("nowBranch")==null?null:(Branch)request.getAttribute("nowBranch"); */
/*  List<Customer> cumstrListAll =(List<Customer>) request.getAttribute("cumstrListAll"); 
 List cumstrList1 =(List) request.getAttribute("cumstrList1"); */
 %>
    <html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<title>表格样式</title>
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
/* function check(){
	if($("#strtime").val()>$("#endtime").val()){
		alert("开始时间不能大于结束时间");
		return false;
	}
	else{
		return true;
	}
} */
</script>
<script type="text/javascript">

     /*    $(document).ready(function() {
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
    */
    </script>
<script>
$(function() {
	/* $("#strtime").datetimepicker({
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
	}); */
	
});
</script>   
<script type="text/javascript">
<%-- $(function () {
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
                text: '库房入库/未入库分布'
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
                    ['未入库',   <%=weirukuDate.getCountsum()%>],
                    {
                        name: '已入库',
                        y: <%=yirukuDate.getCountsum()%>,
                        sliced: true,
                        selected: true
                    }
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
                text: '库房订单状态分布'
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
                    ['库房少货',   <%=youdanwuhuoDate.getCountsum()%>],
                    ['到错货',   <%=youhuowudanDate.getCountsum()%>],
                    ['出库在途',   <%=chukuzaituDate.getCountsum()%>],
                    {
                        name: '库存',
                        y: <%=kucunDate.getCountsum()%>,
                        sliced: true,
                        selected: true
                    }
                ]
            }]
        });
    });
    
}); --%>
		</script>
<script type="text/javascript">
//根据滚动条滚动时间刷新漂浮框的xy轴
function reIframeTopAndBottomXY(){
	/* var st = document.documentElement.scrollTop;
	if(st==0){st=document.body.scrollTop;}
	var ch =  document.documentElement.clientHeight;
	if(ch==0){ch=document.body.clientHeight;}
	if($(".iframe_bottom")){
		$(".iframe_bottom").css("top", st+ch-40);
	}
	if($("#scroll")){
		$("#scroll").css("height", st+ch-130);
	}
	if($(".form_btnbox")){
		$(".form_btnbox").css("top", st+ch-75);
	}
	if($(".inputselect_box")){
		$(".inputselect_box").css("top", st);
	}
	if($(".list_topbar")){
		$(".list_topbar").css("top", st);
	} */
	
/* }
$(window).resize(function() {
	centerBox();
	reIframeTopAndBottomXY();
	});
$(window).scroll(function(event){
	reIframeTopAndBottomXY();
});
$(window).load(function(event){
	reIframeTopAndBottomXY();
});
 */
</script>
</head>

<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<form id="searchForm" action ="<%=request.getContextPath()%>/monitorhouse/date" method = "post">
           <%-- &nbsp;&nbsp; 库房：<select name ="branchid" id="branid">
            <%String nowzhandian="全部"; %>
            <%if(nowBranch !=null){ %>
            <option value ="<%=nowBranch.getBranchid()%>"><%=nowBranch.getBranchname() %><%nowzhandian=nowBranch.getBranchname();%></option>
            <%}else{ %>
		            <option value ="-1">全部</option>
	              <%if(kufangList != null && kufangList.size()>0){ %>
	               <%for( Branch b:kufangList){ %>
	               		<option value ="<%=b.getBranchid()%>" <%if(b.getBranchid() == new Long(request.getAttribute("branchidSession")==null?"-1":request.getAttribute("branchidSession").toString())) { nowzhandian=b.getBranchname();%>selected="selected"<%} %>><%=b.getBranchname() %></option>
	               <%} }}%>
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
                                             入库时间：<input type ="text" name ="crateStartdate" id="strtime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("crateStartdate")) %>">
                                                            到    <input type ="text" name ="crateEnddate" id="endtime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("crateEnddate")) %>">
  		<input type="hidden" id="controlStr" name="controlStrCustomer" value=""/>
		<input type ="button" id="btnval" value="搜索" class="input_button2" /> --%>
		<%-- <input type ="button" id="btnval" value="导出excel" class="input_button2" onclick="javascript:location.href='<%=request.getContextPath()%>/monitorhouse/dateExport'"/> --%>
	</form>
	</div>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
	    <td colspan="1" width="20%" align="center" valign="middle" bgcolor="#eef6ff">库房</td>
		<td colspan="2" width="20%" align="center" valign="middle" bgcolor="#eef6ff">应入库总量</td>
		<td colspan="2" width="20%" align="center" valign="middle" bgcolor="#eef6ff">整批未入库</td>		
		<td colspan="2" width="20%" align="center" valign="middle" bgcolor="#eef6ff">出库在途</td>
		<td colspan="2" width="20%" align="center" valign="middle" bgcolor="#eef6ff">库存</td>
		</tr>
		<tr>
			<td width="20%" align="center" valign="middle">-</td>
			<td width="10%" align="center" valign="middle">单数</td>
			<td width="10%" align="center" valign="middle">金额</td>
			<td width="10%" align="center" valign="middle">单数</td>
			<td width="10%" align="center" valign="middle">金额</td>
			<td width="10%" align="center" valign="middle">单数</td>
			<td width="10%" align="center" valign="middle">金额</td>
			<td width="10%" align="center" valign="middle">单数</td>
			<td width="10%" align="center" valign="middle">金额</td>	
		</tr>
		<%for(MonitorView mv:monitorView){ %>
		<tr>
			<td><%for(Branch branch:kufangList){ if(mv.getCarwarehouse()==branch.getBranchid()){%><%=branch.getBranchname()%><%break;}}%></td>
			<td>
				<%if(mv.getYingrukucount()>0){ %>
				<a href="<%=request.getContextPath()%>/monitorhouse/dateshow/1?datetype=yingruku&carwarehouse=<%=mv.getCarwarehouse()%>"><%=mv.getYingrukucount()%></a>
				<%}else{ %>
				0
				<%} %>
			</td>             
			<td><%=mv.getYingrukusum()==null || mv.getYingrukusum().compareTo(BigDecimal.ZERO)==0?"0.00":mv.getYingrukusum()%></td> 
			<td>
				<%if(mv.getWeirukucount()>0){ %>
				<a href="<%=request.getContextPath()%>/monitorhouse/dateshow/1?datetype=weiruku&carwarehouse=<%=mv.getCarwarehouse()%>"><%=mv.getWeirukucount()%></a>
				<%}else{ %>
				0
				<%} %>
			</td>             
			<td><%=mv.getWeirukusum()==null|| mv.getYingrukusum().compareTo(BigDecimal.ZERO)==0?"0.00":mv.getWeirukusum()%></td> 
			<td>
				<%if(mv.getChukuzaitucount()>0){ %>
				<a href="<%=request.getContextPath()%>/monitorhouse/dateshow/1?datetype=chukuzaitu&carwarehouse=<%=mv.getCarwarehouse()%>"><%=mv.getChukuzaitucount()%></a>
				<%}else{ %>
				0
				<%} %>
			</td>             
			<td><%=mv.getChukuzaitusum()==null|| mv.getYingrukusum().compareTo(BigDecimal.ZERO)==0?"0.00":mv.getChukuzaitusum()%></td> 
			<td>
				<%if(mv.getKucuncount()>0){ %>
				<a href="<%=request.getContextPath()%>/monitorhouse/dateshow/1?datetype=kucun&carwarehouse=<%=mv.getCarwarehouse()%>"><%=mv.getKucuncount()%></a>
				<%}else{ %>
				0
				<%} %>
			</td>             
			<td><%=mv.getKucunsum()==null|| mv.getYingrukusum().compareTo(BigDecimal.ZERO)==0?"0.00":mv.getKucunsum()%></td>         
			<%-- <td>
				<%if(weirukuDate.getCountsum()>0){ %>
				<a href="<%=request.getContextPath()%>/monitorhouse/dateshow/<%=request.getAttribute("weirukuDateType")%>/1"><%=weirukuDate.getCountsum()%></a>
				<%}else{ %>
				0
				<%} %>
			</td>             
			<td><%=weirukuDate.getCaramountsum()%></td>         
			<td>
				<%if(yirukuDate.getCountsum()>0){ %>
				<a href="<%=request.getContextPath()%>/monitorhouse/dateshow/<%=request.getAttribute("yirukuDateType")%>/1"><%=yirukuDate.getCountsum()%></a>
				<%}else{ %>
				0
				<%} %>
			</td>              
			<td><%=yirukuDate.getCaramountsum()%></td>          
			<td>
				<%if(youdanwuhuoDate.getCountsum()>0){ %>
				<a href="<%=request.getContextPath()%>/monitorhouse/dateshow/<%=request.getAttribute("youdanwuhuoDateType")%>/1"><%=youdanwuhuoDate.getCountsum()%></a>
				<%}else{ %>
				0
				<%} %>
			</td>               
			<td><%=youdanwuhuoDate.getCaramountsum()%></td>           
			<td>
				<%if(youhuowudanDate.getCountsum()>0){ %>
				<a href="<%=request.getContextPath()%>/monitorhouse/dateshow/<%=request.getAttribute("youhuowudanDateType")%>/1"><%=youhuowudanDate.getCountsum()%></a>
				<%}else{ %>
				0
				<%} %>
			</td>               
			<td><%=youhuowudanDate.getCaramountsum()%></td>           
			<td>
				<%if(chukuzaituDate.getCountsum()>0){ %>
				<a href="<%=request.getContextPath()%>/monitorhouse/dateshow/<%=request.getAttribute("chukuzaituDateType")%>/1"><%=chukuzaituDate.getCountsum()%></a>
				<%}else{ %>
				0
				<%} %>
			</td>             
			<td><%=chukuzaituDate.getCaramountsum()%></td>         
			<td>
				<%if(kucunDate.getCountsum()>0){ %>
				<a href="<%=request.getContextPath()%>/monitorhouse/dateshow/<%=request.getAttribute("kucunDateType")%>/1"><%=kucunDate.getCountsum()%></a>
				<%}else{ %>
				0
				<%} %>
			</td>             
			<td><%=kucunDate.getCaramountsum()%></td>      --%>    
	    </tr>
	    <%} %>
	</table>
	<div style="width:1000px; margin:0 auto">
		<div id="container" style="width:500px; overflow:hidden; height: 450px; float:left"></div>
        <div id="container2" style="width:500px; overflow:hidden; height: 450px; float:right"></div>
	</div>

	<div class="jg_10"></div><div class="jg_10"></div>
</div>		
	<div class="jg_10"></div>
	<div class="clear"></div>
</body>
</html>


