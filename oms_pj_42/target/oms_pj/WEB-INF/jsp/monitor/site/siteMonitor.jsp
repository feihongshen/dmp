
<%@page import="java.math.BigDecimal"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.MonitorDTO"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Branch"%>
 <%
 
 MonitorDTO daoruDate =(MonitorDTO) request.getAttribute("daoruDate"); 
 MonitorDTO youdanwuhuoDate =(MonitorDTO) request.getAttribute("youdanwuhuoDate"); 
 MonitorDTO youhuowudanDate =(MonitorDTO) request.getAttribute("youhuowudanDate");
 MonitorDTO weidaohuoDate =(MonitorDTO) request.getAttribute("weidaohuoDate"); 
 MonitorDTO rukuweilingDate =(MonitorDTO) request.getAttribute("rukuweilingDate"); 
	MonitorDTO yichangdanDate =(MonitorDTO) request.getAttribute("yichangdanDate"); 
	MonitorDTO yilinghuoDate =(MonitorDTO) request.getAttribute("yilinghuoDate"); 
	MonitorDTO yiliudanDate =(MonitorDTO) request.getAttribute("yiliudanDate"); 
	MonitorDTO kucuntuihuoDate =(MonitorDTO) request.getAttribute("kucuntuihuoDate"); 
	MonitorDTO kucunzhiliuDate =(MonitorDTO) request.getAttribute("kucunzhiliuDate"); 
	MonitorDTO weijiaokuanDate =(MonitorDTO) request.getAttribute("weijiaokuanDate"); 
	MonitorDTO qiankuanDate =(MonitorDTO) request.getAttribute("qiankuanDate"); 
	MonitorDTO tuotouDate =(MonitorDTO) request.getAttribute("tuotouDate"); 
	MonitorDTO weituotouDate =(MonitorDTO) request.getAttribute("weituotouDate"); 
	MonitorDTO qitaDate =(MonitorDTO) request.getAttribute("qitaDate"); 
 
 List<Branch> branchnameList =(List<Branch>) request.getAttribute("branchnameList");
 Branch nowBranch = request.getAttribute("nowBranch")==null?null:(Branch)request.getAttribute("nowBranch");
 List<Customer> cumstrListAll =(List<Customer>) request.getAttribute("cumstrListAll"); 
 List cumstrList1 =(List) request.getAttribute("cumstrList1");
 %>
    <html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<title>站点信息监控</title>
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
	if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() != ''){
		alert("开始时间不能大于结束时间");
		return false;
	}
	if($("#startinSitetime").val()>$("#endinSitetime").val() && $("#endinSitetime").val() != ''){
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
	$("#startinSitetime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endinSitetime").datetimepicker({
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
//根据滚动条滚动时间刷新漂浮框的xy轴
function reIframeTopAndBottomXY(){
	var st = document.documentElement.scrollTop;
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
	}
	
}
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

</script>
</head>

<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<form id="searchForm" action ="<%=request.getContextPath()%>/monitorsite/date" method = "post">
		
	 &nbsp;&nbsp;&nbsp;   站点：<select name ="branchid" id="branid">
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
			            <%} }%>
			        </select>
     <br/> &nbsp;&nbsp;&nbsp;  发货时间：<input type ="text" name ="crateStartdate" id="strtime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("crateStartdate")) %>">  
                     到      <input type ="text" name ="crateEnddate" id="endtime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("crateEnddate")) %>">
      到站时间：<input type ="text" name ="startinSitetime" id="startinSitetime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("startinSitetime")) %>">  
                     到      <input type ="text" name ="endinSitetime" id="endinSitetime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("endinSitetime")) %>">

        <input type ="button" id="btnval" value="搜索" class="input_button2" />
		 <input type="hidden" id="controlStr" name="controlStrCustomer" value=""/>
	<input type="hidden" id="isshow" name="isshow" value="0" /> 
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div><div class="jg_10"></div>
<div style="overflow-x: scroll; width:100%" id="scroll2">
	<table width="2800" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
			<td  colspan="3" align="center" valign="middle" bgcolor="#eef6ff">系统信息匹配量</td>
			<td  colspan="3" align="center" valign="middle" bgcolor="#eef6ff">实际应到货量</td>
			<td   colspan="12" align="center" valign="middle" bgcolor="#eef6ff">已到货</td>
			<td  colspan="3" align="center" valign="middle" bgcolor="#eef6ff">已领货</td>
			<td   colspan="6" align="center" valign="middle" bgcolor="#eef6ff">反馈</td>
			<td   colspan="8" align="center" valign="middle" bgcolor="#eef6ff">归班</td>
			<td   colspan="6" align="center" valign="middle" bgcolor="#eef6ff">库存</td>
			<td  colspan="3" align="center" valign="middle" bgcolor="#eef6ff">其他</td>
	   </tr>     
		<tr>     
			<td  colspan="3" align="center" valign="middle" bgcolor="#eef6ff">系统信息匹配量</td>
			<td  colspan="3" align="center" valign="middle" bgcolor="#eef6ff">实际应到货量</td>
			<td  colspan="3" align="center" valign="middle" bgcolor="#eef6ff">到站未领</td>
			<td  colspan="3" align="center" valign="middle" bgcolor="#eef6ff">到错货</td>
			<td  colspan="3" align="center" valign="middle" bgcolor="#eef6ff">异常单</td>
			<td  colspan="3" align="center" valign="middle" bgcolor="#eef6ff">分站少货</td>
			<td  colspan="3" align="center" valign="middle" bgcolor="#eef6ff">已领货</td>
			<td  colspan="3" align="center" valign="middle" bgcolor="#eef6ff">妥投</td>
			<td  colspan="3" align="center" valign="middle" bgcolor="#eef6ff">未妥投</td>
			<td  colspan="4" align="center" valign="middle" bgcolor="#eef6ff">未交款</td>
			<td  colspan="4" align="center" valign="middle" bgcolor="#eef6ff">已交款</td>
			<td  colspan="3" align="center" valign="middle" bgcolor="#eef6ff">退货</td>
			<td  colspan="3" align="center" valign="middle" bgcolor="#eef6ff">滞留</td>
			<td  colspan="3" align="center" valign="middle" bgcolor="#eef6ff">其他</td>
		</tr>
		<tr>
			<td >单数</td>
			<td >应收金额</td>
			<td >应退金额</td>
			<td >单数</td>
			<td >应收金额</td>
			<td >应退金额</td>
			<td >单数</td>
			<td >应收金额</td>
			<td >应退金额</td>
			<td >单数</td>
			<td >应收金额</td>
			<td >应退金额</td>	
			<td >单数</td>
			<td >应收金额</td>
			<td >应退金额</td>
			<td >单数</td>
			<td >应收金额</td>
			<td >应退金额</td>
			<td >单数</td>
			<td >应收金额</td>
			<td >应退金额</td>
			<td >单数</td>
			<td >应收金额</td>
			<td >应退金额</td>
			<td >单数</td>
			<td >应收金额</td>
			<td >应退金额</td>
			
			<td >单数</td>
			<td >现金</td>
			<td >POS</td>
			<td >应退金额</td>
			<td >单数</td>
			<td >现金</td>
			<td >POS</td>
			<td >应退金额</td>
			
			<td >单数</td>
			<td >应收金额</td>
			<td >应退金额</td>
			<td >单数</td>
			<td >应收金额</td>
			<td >应退金额</td>
	        
			<td >单数</td>
	        <td >应收金额</td>
	        <td >应退金额</td>
		</tr>
		<tr>
			<td>
			<%if(daoruDate.getCountsum()>0){ %>
			<a href="<%=request.getContextPath()%>/monitorsite/dateshow/<%=request.getAttribute("daoruDateType")%>/1"><%=daoruDate.getCountsum()%></a>
			<%}else{ %>0<%} %>
			</td>             
			<td><%=daoruDate.getCaramountsum()%></td>         
			<td><%=daoruDate.getPaybackfee()%></td>         
			<td>
			<%if(weidaohuoDate.getCountsum()>0){ %>
			<a href="<%=request.getContextPath()%>/monitorsite/dateshow/<%=request.getAttribute("weidaohuoDateType")%>/1"><%=weidaohuoDate.getCountsum()%></a>
			<%}else{ %>0<%} %>
			</td>             
			<td><%=weidaohuoDate.getCaramountsum()%></td>         
			<td><%=weidaohuoDate.getPaybackfee()%></td>         
			<td>
			<%if(rukuweilingDate.getCountsum()>0){ %>
			<a href="<%=request.getContextPath()%>/monitorsite/dateshow/<%=request.getAttribute("rukuweilingDateType")%>/1"><%=rukuweilingDate.getCountsum()%></a>
			<%}else{ %>0<%} %>
			</td>              
			<td><%=rukuweilingDate.getCaramountsum()%></td>          
			<td><%=rukuweilingDate.getPaybackfee()%></td>          
			<td>
			<%if(youhuowudanDate.getCountsum()>0){ %>
			<a href="<%=request.getContextPath()%>/monitorsite/dateshow/<%=request.getAttribute("youhuowudanDateType")%>/1"><%=youhuowudanDate.getCountsum()%></a>
			<%}else{ %>0<%} %>
			</td>               
			<td><%=youhuowudanDate.getCountsum()>0?youhuowudanDate.getCaramountsum():"0.00"%></td>           
			<td><%=youhuowudanDate.getCountsum()>0?youhuowudanDate.getPaybackfee():"0.00"%></td>           
			<td>
			<%if(yichangdanDate.getCountsum()>0){ %>
			<a href="<%=request.getContextPath()%>/monitorsite/dateshow/<%=request.getAttribute("yichangdanDateType")%>/1"><%=yichangdanDate.getCountsum()%></a>
			<%}else{ %>0<%} %>
			</td>               
			<td><%=yichangdanDate.getCaramountsum()%></td>           
			<td><%=yichangdanDate.getPaybackfee()%></td>           
			<td>
			<%if(youdanwuhuoDate.getCountsum()>0){ %>
			<a href="<%=request.getContextPath()%>/monitorsite/dateshow/<%=request.getAttribute("youdanwuhuoDateType")%>/1"><%=youdanwuhuoDate.getCountsum()%></a>
			<%}else{ %>0<%} %>
			</td>               
			<td><%=youdanwuhuoDate.getCountsum()>0?youdanwuhuoDate.getCaramountsum():"0.00"%></td>           
			<td><%=youdanwuhuoDate.getCountsum()>0?youdanwuhuoDate.getPaybackfee():"0.00"%></td>           
			<td>
			<%if(yilinghuoDate.getCountsum()>0){ %>
			<a href="<%=request.getContextPath()%>/monitorsite/dateshow/<%=request.getAttribute("yilinghuoDateType")%>/1"><%=yilinghuoDate.getCountsum()%></a>
			<%}else{ %>0<%} %>
			</td>             
			<td><%=yilinghuoDate.getCaramountsum()%></td>         
			<td><%=yilinghuoDate.getPaybackfee()%></td>         
			<td>
			<%if(tuotouDate.getCountsum()>0){ %>
			<a href="<%=request.getContextPath()%>/monitorsite/dateshow/<%=request.getAttribute("tuotouDateType")%>/1"><%=tuotouDate.getCountsum()%></a>
			<%}else{ %>0<%} %>
			</td>             
			<td><%=tuotouDate.getCaramountsum()%></td>         
			<td><%=tuotouDate.getPaybackfee()%></td>         
			<td>
			<%if(weituotouDate.getCountsum()>0){ %>
			<a href="<%=request.getContextPath()%>/monitorsite/dateshow/<%=request.getAttribute("weituotouDateType")%>/1"><%=weituotouDate.getCountsum()%></a>
			<%}else{ %>0<%} %>
			</td>             
			<td><%=weituotouDate.getCaramountsum()%></td>         
			<td><%=weituotouDate.getPaybackfee()%></td>         
			<td>
			<%if(weijiaokuanDate.getCountsum()>0){ %>
			<a href="<%=request.getContextPath()%>/monitorsite/dateshow/<%=request.getAttribute("weijiaokuanDateType")%>/1"><%=weijiaokuanDate.getCountsum()%></a>
			<%}else{ %>0<%} %>
			</td> 
			<td><%=weijiaokuanDate.getCaramountsum()%></td>  
			<td><%=weijiaokuanDate.getCaramountpos()%></td>               	       
			<td><%=weijiaokuanDate.getPaybackfee()%></td>  
			<td>
			<%if(qiankuanDate.getCountsum()>0){ %>
			<a href="<%=request.getContextPath()%>/monitorsite/dateshow/<%=request.getAttribute("qiankuanDateType")%>/1"><%=qiankuanDate.getCountsum()%></a>
			<%}else{ %>0<%} %>
			</td>             
			<td><%=qiankuanDate.getCaramountsum()%></td>  
			<td><%=qiankuanDate.getCaramountpos()%></td>        
			<td><%=qiankuanDate.getPaybackfee()%></td>  
			<td>
			<%if(kucuntuihuoDate.getCountsum()>0){ %>
			<a href="<%=request.getContextPath()%>/monitorsite/dateshow/<%=request.getAttribute("kucuntuihuoDateType")%>/1"><%=kucuntuihuoDate.getCountsum()%></a>
			<%}else{ %>0<%} %>
			</td>             
			<td><%=kucuntuihuoDate.getCaramountsum()%></td>         
			<td><%=kucuntuihuoDate.getPaybackfee()%></td>         
			<td>
			<%if(kucunzhiliuDate.getCountsum()>0){ %>
			<a href="<%=request.getContextPath()%>/monitorsite/dateshow/<%=request.getAttribute("kucunzhiliuDateType")%>/1"><%=kucunzhiliuDate.getCountsum()%></a>
			<%}else{ %>0<%} %>
			</td>             
			<td><%=kucunzhiliuDate.getCaramountsum()%></td>         
			<td><%=kucunzhiliuDate.getPaybackfee()%></td>         
			<td>
			<%if(qitaDate.getCountsum()>0){ %>
			<a href="<%=request.getContextPath()%>/monitorsite/dateshow/<%=request.getAttribute("qitaDateType")%>/1"><%=qitaDate.getCountsum()%></a>
			<%}else{ %>0<%} %>
			</td>             
			<td><%=qitaDate.getCaramountsum()%></td>         
			<td><%=qitaDate.getPaybackfee()%></td>         
	    </tr>
	</table>
	</div>
	
	<div class="jg_10"></div><div class="jg_10"></div>
</div>		
	<div class="jg_10"></div>
	<div class="clear"></div>
</body>
</html>


