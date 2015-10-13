
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.MonitorDTO"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Branch"%>
 <%
 
  
 
 List<Branch> branchnameList =(List<Branch>) request.getAttribute("branchnameList");
 Branch branchObjectNow =(Branch) request.getAttribute("branchObjectNow");
 List<Customer> cumstrListAll =(List<Customer>) request.getAttribute("cumstrListAll"); 
 List cumstrList1 =(List) request.getAttribute("cumstrList1");
 long branchidNow =Long.parseLong(request.getAttribute("branchidNow")==null?"-1":request.getAttribute("branchidNow").toString()) ;
 
 Map exptionMap =(Map) request.getAttribute("moneyMap");
 
 
 %>
    <html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<title>财务信息监控</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/omsTable.css" type="text/css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/omsTable.js"></script>
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
        $("#searchForm").submit();
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

<body>
 <form id="searchForm" action ="<%=request.getContextPath()%>/monitor/date/7" method = "post">
   <p> 
      入库时间：<input type ="text" name ="crateStartdate" id="strtime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("crateStartdate")) %>">
    
    到<input type ="text" name ="crateEnddate" id="endtime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("crateEnddate")) %>">
  
     <input type ="button"  value ="查看" id="btnval">
        </p>
        <input type="hidden" id="controlStr" name="controlStrCustomer" value=""/>
        </form>
<div style="text-align:center;">
	<table class="datagrid">
	
	<tr>
		<th >站点</th>
		<th >已入库</th>
		<th >已交款</th>
		<th >欠款</th>
		<th >库存</th>
	</tr>
	
	<%if(branchnameList!= null && branchnameList.size()>0){ %>
	<%for(int i =0;i<branchnameList.size();i++){ %>
	<tr>
		<td>
		<%=branchnameList.get(i).getBranchname()%>
		</td>
		<td>
		<%if(exptionMap != null && 
		exptionMap.get(branchnameList.get(i).getBranchid())!=null
		&& ((Map)exptionMap.get(branchnameList.get(i).getBranchid())) != null
		&& ((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("yiruku_"+branchnameList.get(i).getBranchid()) != null){ %>
		<a href="<%=request.getContextPath()%>/monitor/dateshow/8?flowType=<%=request.getAttribute("yirukuDateType")%>&branchPramid=<%=branchnameList.get(i).getBranchid()%>">
		<%=((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("yiruku_"+branchnameList.get(i).getBranchid())%>
		</a>
		<%} else{%>
		0
		<%} %>
		</td>             
		<td>
		<%if(exptionMap != null && 
		exptionMap.get(branchnameList.get(i).getBranchid())!=null
		&& ((Map)exptionMap.get(branchnameList.get(i).getBranchid())) != null
		&& ((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("yijiaokuan_"+branchnameList.get(i).getBranchid()) != null){ %>
		<a href="<%=request.getContextPath()%>/monitor/dateshow/8?flowType=<%=request.getAttribute("yijiaokuanDateType")%>&branchPramid=<%=branchnameList.get(i).getBranchid()%>">
		<%=((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("yijiaokuan_"+branchnameList.get(i).getBranchid())%>
		</a>
		<%} else{%>
		0
		<%} %>
		</td> 
		<td>
		<%if(exptionMap != null && 
		exptionMap.get(branchnameList.get(i).getBranchid())!=null
		&& ((Map)exptionMap.get(branchnameList.get(i).getBranchid())) != null
		&& ((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("qiankuan_"+branchnameList.get(i).getBranchid()) != null){ %>
		<a href="<%=request.getContextPath()%>/monitor/dateshow/7?flowType=<%=request.getAttribute("qiankuanDateType")%>&branchPramid=<%=branchnameList.get(i).getBranchid()%>">
		<%=((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("qiankuan_"+branchnameList.get(i).getBranchid())%>
		</a>
		<%} else{%>
		0
		<%} %>
		</td> 
		         
		<td>
		<%if(exptionMap != null && 
		exptionMap.get(branchnameList.get(i).getBranchid())!=null
		&& ((Map)exptionMap.get(branchnameList.get(i).getBranchid())) != null
		&& ((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("kucun_"+branchnameList.get(i).getBranchid()) != null){ %>
		<a href="<%=request.getContextPath()%>/monitor/dateshow/7?flowType=<%=request.getAttribute("kucunDateType")%>&branchPramid=<%=branchnameList.get(i).getBranchid()%>">
		<%=((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("kucun_"+branchnameList.get(i).getBranchid())%>
		</a>
		<%} else{%>
		0
		<%} %>
		</td>          
	</tr>
	<%} 
	}%>
	
	</table>
</div>
</body>

</html>  