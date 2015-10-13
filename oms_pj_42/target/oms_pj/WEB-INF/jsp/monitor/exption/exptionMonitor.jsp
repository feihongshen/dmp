
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
 
 Map exptionMap =(Map) request.getAttribute("exptionMap");
 
 
 %>
    <html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<title>异常信息监控</title>
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

</head>
<form  id="searchForm" action ="<%=request.getContextPath()%>/monitor/date/7" method = "post">
		    站点：<select name ="branchid" id="branid">
	             <option value ="-1" >全部</option>
	              <%if(branchnameList != null && branchnameList.size()>0){ %>
	                <%for( Branch b:branchnameList){ %>
	               		<option value ="<%=b.getBranchid()%>" <%if(b.getBranchid() == new Long(request.getParameter("branchid")==null?"-1":request.getParameter("branchid"))) {%>selected="selected"<%} %>><%=b.getBranchname() %></option>
	                <%}} %>
	             </select> 
               发货时间：<input type ="text" name ="emailStartTime" id="strtime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("emailStartTime")) %>">
                              到   <input type ="text" name ="eamilEndTime" id="endtime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("eamilEndTime")) %>">
  
  		<input type ="button" id="btnval" value="搜索" class="input_button2" />
		<input type="hidden" id="controlStr" name="controlStrCustomer" value=""/>
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
	    <td width="25%" align="center" valign="middle" bgcolor="#eef6ff">站点</td>
		<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">有单无货</td>
		<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">有货无单</td>
		<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">异常单</td>
		</tr>
		<%if(branchidNow< 0){ %>
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
			&& ((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("youdanwuhuo_"+branchnameList.get(i).getBranchid()) != null){ %>
			<a href="<%=request.getContextPath()%>/monitor/dateshow/7/1?flowType=<%=request.getAttribute("youdanwuhuoDateType")%>&branchPramid=<%=branchnameList.get(i).getBranchid()%>">
			<%=((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("youdanwuhuo_"+branchnameList.get(i).getBranchid())%>
			</a>
			<%} else{%>
			0
			<%} %>
			</td>             
			<td>
			<%if(exptionMap != null && 
			exptionMap.get(branchnameList.get(i).getBranchid())!=null
			&& ((Map)exptionMap.get(branchnameList.get(i).getBranchid())) != null
			&& ((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("youhuowudan_"+branchnameList.get(i).getBranchid()) != null){ %>
			<a href="<%=request.getContextPath()%>/monitor/dateshow/7/1?flowType=<%=request.getAttribute("youhuowudanDateType")%>&branchPramid=<%=branchnameList.get(i).getBranchid()%>">
			<%=((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("youhuowudan_"+branchnameList.get(i).getBranchid())%>
			</a>
			<%} else{%>
			0
			<%} %>
			</td> 
			<td>
			<%if(exptionMap != null && 
			exptionMap.get(branchnameList.get(i).getBranchid())!=null
			&& ((Map)exptionMap.get(branchnameList.get(i).getBranchid())) != null
			&& ((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("yichangdan_"+branchnameList.get(i).getBranchid()) != null){ %>
			<a href="<%=request.getContextPath()%>/monitor/dateshow/7/1?flowType=<%=request.getAttribute("yichangdanDateType")%>&branchPramid=<%=branchnameList.get(i).getBranchid()%>">
			<%=((Map)exptionMap.get(branchnameList.get(i).getBranchid())).get("yichangdan_"+branchnameList.get(i).getBranchid())%>
			</a>
			<%} else{%>
			0
			<%} %>
			</td>          
		</tr>
		<%} 
		}
		}else{%>
		<tr>
			<td>
			<%=branchObjectNow==null?"":branchObjectNow.getBranchname()%>
			</td>
			<td>
			<%if(exptionMap != null 
			&& ((Map)exptionMap.get(branchidNow)) != null
			&& ((Map)exptionMap.get(branchidNow)).get("youdanwuhuo_"+branchidNow) != null){ %>
			<a href="<%=request.getContextPath()%>/monitor/dateshow/7/1?flowType=<%=request.getAttribute("youdanwuhuoDateType")%>&branchPramid=<%=branchidNow%>">
			<%=((Map)exptionMap.get(branchidNow)).get("youdanwuhuo_"+branchidNow)%>
			</a>
			<%} else{%>
			0
			<%} %>
			</td>             
			<td>
			<%if(exptionMap != null 
			&& ((Map)exptionMap.get(branchidNow)) != null
			&& ((Map)exptionMap.get(branchidNow)).get("youhuowudan_"+branchidNow) != null){ %>
			<a href="<%=request.getContextPath()%>/monitor/dateshow/7/1?flowType=<%=request.getAttribute("youhuowudanDateType")%>&branchPramid=<%=branchidNow%>">
			<%=((Map)exptionMap.get(branchidNow)).get("youhuowudan_"+branchidNow)%>
			</a>
			<%} else{%>
			0
			<%} %>
			</td> 
			<td>
			<%if(exptionMap != null 
			&& ((Map)exptionMap.get(branchidNow)) != null
			&& ((Map)exptionMap.get(branchidNow)).get("yichangdan_"+branchidNow) != null){ %>
			<a href="<%=request.getContextPath()%>/monitor/dateshow/7/1?flowType=<%=request.getAttribute("yichangdanDateType")%>&branchPramid=<%=branchidNow%>">
			<%=((Map)exptionMap.get(branchidNow)).get("yichangdan_"+branchidNow)%>
			</a>
			<%} else{%>
			0
			<%} %>
			</td>          
		</tr>
	<%} %>
	</table>
	<div class="jg_10"></div><div class="jg_10"></div>
</div>
			
	<div class="jg_10"></div>

	<div class="clear"></div>
</body>
</html>



