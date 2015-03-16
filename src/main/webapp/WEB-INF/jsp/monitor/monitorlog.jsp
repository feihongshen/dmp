<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.controller.MonitorLogDTO"%>
<%@page import="cn.explink.controller.MonitorLogSim"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.util.Page"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单生命周期监控</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>

</head>
<%
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
Map<Long,String> customerMap = (Map<Long,String>)request.getAttribute("customerMap");
Map<Long ,MonitorLogSim> weidaohuoMap = (Map<Long,MonitorLogSim>)request.getAttribute("weidaohuoMap");
Map<Long ,MonitorLogSim> tihuoMap = (Map<Long,MonitorLogSim>)request.getAttribute("tihuoMap");
Map<Long ,MonitorLogSim> rukuMap = (Map<Long,MonitorLogSim>)request.getAttribute("rukuMap");
Map<Long ,MonitorLogSim> chukuMap = (Map<Long,MonitorLogSim>)request.getAttribute("chukuMap");
Map<Long ,MonitorLogSim> daozhanMap = (Map<Long,MonitorLogSim>)request.getAttribute("daozhanMap");
Map<Long ,MonitorLogSim> yichuzhanMap = (Map<Long,MonitorLogSim>)request.getAttribute("yichuzhanMap");
Map<Long ,MonitorLogSim> zhongzhanrukuMap = (Map<Long,MonitorLogSim>)request.getAttribute("zhongzhanrukuMap");
Map<Long ,MonitorLogSim> tuihuorukuMap = (Map<Long,MonitorLogSim>)request.getAttribute("tuihuorukuMap");
Map<Long ,MonitorLogSim> tuigonghuoshangMap = (Map<Long,MonitorLogSim>)request.getAttribute("tuigonghuoshangMap");
List<MonitorLogDTO>  monitorList = (List<MonitorLogDTO> )request.getAttribute("monitorList");
List customeridList =(List) request.getAttribute("customeridStr");
%>
<script>
function dgetViewBox(key,durl){
	window.parent.getViewBoxd(key,durl);
}
</script>
<script type="text/javascript">

$(document).ready(function() {
	   //获取下拉框的值
	   $("#chakan").click(function(){
		    	$("#searchForm").submit();
		    	$("#chakan").attr("disabled","disabled");
				$("#chakan").val("请稍等..");
	   });
	});
	
$(function() {
	$("#customerid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择供货商' });
});
$(function() {
	$(".operantis").each(function(){
		pre=$(this).prev();
		opre=pre.prev();
		var k=opre.text()/pre.text();
		if(!k||k=="Infinity"){
			$(this).text("0");
			return true;
		}
		$(this).text(k.toFixed(0));
	} );
	
});
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
 <body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="right_box">
<div class="inputselect_box" style="top: 0px; ">
		<form action="<%=request.getContextPath()%>/monitorlog/monitorloglist" method="post" id="searchForm">
		  &nbsp;&nbsp;选择供货商：
			<select name ="customerid" id ="customerid" multiple="multiple" style="width: 300px;">
		          <%for(Customer c : customerlist){ %>
		           <option value ="<%=c.getCustomerid() %>" 
		           <%if(!customeridList.isEmpty()) 
			            {for(int i=0;i<customeridList.size();i++){
			            	if(c.getCustomerid()== new Long(customeridList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%> ><%=c.getCustomername()%></option>
		          <%} %>
		    </select>
		    [<a href="javascript:multiSelectAll('customerid',1,'请选择');">全选</a>]
			[<a href="javascript:multiSelectAll('customerid',0,'请选择');">取消全选</a>]
			<input type="hidden" name="isnow" value="1">
			<input type="button" class="input_button2" id="chakan" value="查看">
			<!-- <input type ="button" id="exportExcel" value="导出Excel" class="input_button2" /> -->
	  </form>
	</div>
  <div style="background:#FFF">
	<div style="height:25px"></div>
	<div class="jg_10"></div>
	<div class="tabbox">
	  <div style="position:relative; z-index:0 " >
 		<div style="overflow-x:scroll; width:100% " id="scroll">
	       <table width="1500" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
			   <tbody>
			   	<tr class="font_1" height="30" >
			   		<td  align="center" valign="middle"  rowspan="2" >供货商</td> 
			   		<td  align="center" valign="middle"  colspan="2" >导入未到货</td>
			   		<td  align="center" valign="middle"  colspan="2" >提货未入库</td>
			   		<td  align="center" valign="middle"  colspan="2" >已入分拣库未出库</td>
			   		<td  align="center" valign="middle"  colspan="2" >已出库未到站</td>
			   		<td  align="center" valign="middle"  colspan="2" >站点在站货物</td>
			   		<td  align="center" valign="middle"  colspan="2" >已出站在途</td>
			   		<td  align="center" valign="middle"  colspan="2" >已入中转库未出库</td>
			   		<td  align="center" valign="middle"  colspan="2" >已入退货库未出库</td>
			   		<td  align="center" valign="middle"  colspan="2" >退客户在途</td>
			   		<td  align="center" valign="middle"  colspan="2" >汇总</td>
				</tr>
			   	<tr class="font_1" height="30" >
			   		<td  align="center" valign="middle" >票数</td>
			   		<td  align="center" valign="middle" >金额</td>
			   		<td  align="center" valign="middle" >票数</td>
			   		<td  align="center" valign="middle" >金额</td>
			   		<td  align="center" valign="middle" >票数</td>
			   		<td  align="center" valign="middle" >金额</td>
			   		<td  align="center" valign="middle" >票数</td>
			   		<td  align="center" valign="middle" >金额</td>
			   		<td  align="center" valign="middle" >票数</td>
			   		<td  align="center" valign="middle" >金额</td>
			   		<td  align="center" valign="middle" >票数</td>
			   		<td  align="center" valign="middle" >金额</td>
			   		<td  align="center" valign="middle" >票数</td>
			   		<td  align="center" valign="middle" >金额</td>
			   		<td  align="center" valign="middle" >票数</td>
			   		<td  align="center" valign="middle" >金额</td>
			   		<td  align="center" valign="middle" >票数</td>
			   		<td  align="center" valign="middle" >金额</td>
			   		<td  align="center" valign="middle" >票数</td>
			   		<td  align="center" valign="middle" >金额</td>
				</tr>
				<%if(customerMap != null && customerMap.size()>0){ %>
				<%
				 long	weidaohuoCountsum =0;
				 BigDecimal	weidaohuoCaramountsum = BigDecimal.ZERO;
				 long	tihuoCountsum =0;
				 BigDecimal	tihuoCaramountsum = BigDecimal.ZERO;
				 long	rukuCountsum =0;
				 BigDecimal	rukuCaramountsum = BigDecimal.ZERO;
				 long	chukuCountsum =0;
				 BigDecimal	chukuCaramountsum = BigDecimal.ZERO;
				 long	daozhanCountsum =0;
				 BigDecimal	daozhanCaramountsum = BigDecimal.ZERO;
				 long	yichuzhanCountsum =0;
				 BigDecimal	yichuzhanCaramountsum = BigDecimal.ZERO;
				 long	zhongzhanrukuCountsum =0;
				 BigDecimal	zhongzhuanrukuCaramountsum = BigDecimal.ZERO;
				 long	tuihuorukuCountsum =0;
				 BigDecimal	tuihuorukuCaramountsum = BigDecimal.ZERO;
				 long	tuigonghuoshangCountsum =0;
				 BigDecimal	tuigonghuoshangCaramountsum = BigDecimal.ZERO;
				%> 
				<%for(Map.Entry<Long ,String> mo : customerMap.entrySet()){ %>
				<%
				long	 weidaohuoCountsum1 = weidaohuoMap.get(mo.getKey()) == null?0:weidaohuoMap.get(mo.getKey()).getDcount();
				BigDecimal weidaohuoCaramountsum1 = weidaohuoMap.get(mo.getKey()) == null? BigDecimal.ZERO:weidaohuoMap.get(mo.getKey()).getDsum();
				long	 tihuoCountsum1 = tihuoMap.get(mo.getKey()) == null?0:tihuoMap.get(mo.getKey()).getDcount();
				BigDecimal tihuoCaramountsum1 = tihuoMap.get(mo.getKey()) == null? BigDecimal.ZERO:tihuoMap.get(mo.getKey()).getDsum();
				long	 rukuCountsum1 = rukuMap.get(mo.getKey()) == null?0:rukuMap.get(mo.getKey()).getDcount();
				BigDecimal rukuCaramountsum1 = rukuMap.get(mo.getKey()) == null? BigDecimal.ZERO:rukuMap.get(mo.getKey()).getDsum();
				long	 chukuCountsum1 = daozhanMap.get(mo.getKey()) == null?0:daozhanMap.get(mo.getKey()).getDcount();
				BigDecimal chukuCaramountsum1 = daozhanMap.get(mo.getKey()) == null? BigDecimal.ZERO:daozhanMap.get(mo.getKey()).getDsum();
				long	 daozhanCountsum1 = yichuzhanMap.get(mo.getKey()) == null?0:yichuzhanMap.get(mo.getKey()).getDcount();
				BigDecimal daozhanCaramountsum1 = yichuzhanMap.get(mo.getKey()) == null? BigDecimal.ZERO:yichuzhanMap.get(mo.getKey()).getDsum();
				long	 yichuzhanCountsum1 = yichuzhanMap.get(mo.getKey()) == null?0:yichuzhanMap.get(mo.getKey()).getDcount();
				BigDecimal yichuzhanCaramountsum1 = yichuzhanMap.get(mo.getKey()) == null? BigDecimal.ZERO:yichuzhanMap.get(mo.getKey()).getDsum();
				long	 zhongzhanrukuCountsum1 = zhongzhanrukuMap.get(mo.getKey()) == null?0:zhongzhanrukuMap.get(mo.getKey()).getDcount();
				BigDecimal zhongzhuanrukuCaramountsum1 = zhongzhanrukuMap.get(mo.getKey()) == null? BigDecimal.ZERO:zhongzhanrukuMap.get(mo.getKey()).getDsum();
				long	 tuihuorukuCountsum1 = tuihuorukuMap.get(mo.getKey()) == null?0:tuihuorukuMap.get(mo.getKey()).getDcount();
				BigDecimal tuihuorukuCaramountsum1 = tuihuorukuMap.get(mo.getKey()) == null? BigDecimal.ZERO:tuihuorukuMap.get(mo.getKey()).getDsum();
				long	 tuigonghuoshangCountsum1 = tuigonghuoshangMap.get(mo.getKey()) == null?0:tuigonghuoshangMap.get(mo.getKey()).getDcount();
				BigDecimal tuigonghuoshangCaramountsum1 = tuigonghuoshangMap.get(mo.getKey()) == null? BigDecimal.ZERO:tuigonghuoshangMap.get(mo.getKey()).getDsum();
				
				%>
			   	<tr height="30">
			   		<td  align="center" valign="middle" ><%=mo.getValue() %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getKey()%>/weidaohuo/1"><%=weidaohuoCountsum1 %> </a></td>
			   		<td  align="right" valign="middle" ><%=weidaohuoCaramountsum1 %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getKey()%>/tihuo/1"><%=tihuoCountsum1 %></a></td>
			   		<td  align="right" valign="middle" ><%=tihuoCaramountsum1 %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getKey()%>/ruku/1"> <%=rukuCountsum1 %></a></td>
			   		<td  align="right" valign="middle" ><%=rukuCaramountsum1 %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getKey()%>/chuku/1"> <%=chukuCountsum1 %></a></td>
			   		<td  align="right" valign="middle" ><%=chukuCaramountsum1%></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getKey()%>/daozhan/1"> <%=daozhanCountsum1%></a></td>
			   		<td  align="right" valign="middle" ><%=daozhanCaramountsum1 %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getKey()%>/yichuzhan/1"> <%=yichuzhanCountsum1 %></a></td>
			   		<td  align="right" valign="middle" ><%=yichuzhanCaramountsum1  %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getKey()%>/Zhongzhanruku/1"> <%=zhongzhanrukuCountsum1%></a></td>
			   		<td  align="right" valign="middle" ><%=zhongzhuanrukuCaramountsum1 %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getKey()%>/tuihuoruku/1"> <%=tuihuorukuCountsum1%></a></td>
			   		<td  align="right" valign="middle" ><%=tuihuorukuCaramountsum1 %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getKey()%>/tuigonghuoshang/1"> <%=tuigonghuoshangCountsum1%></a></td>
			   		<td  align="right" valign="middle" ><%=tuigonghuoshangCaramountsum1  %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getKey()%>/all/1"> <%= weidaohuoCountsum1 +tihuoCountsum1 +rukuCountsum1
					   		+chukuCountsum1 +daozhanCountsum1+ 
					   		yichuzhanCountsum1+zhongzhanrukuCountsum1
					   		+tuihuorukuCountsum1+tuigonghuoshangCountsum1  %></a></td>
			   		<td  align="right" valign="middle" >
			   		<%= weidaohuoCaramountsum1.add(tihuoCaramountsum1).
					   		add(tihuoCaramountsum1).add(rukuCaramountsum1).add(chukuCaramountsum1).
					   		add(daozhanCaramountsum1).add(yichuzhanCaramountsum1)
					   		.add(zhongzhuanrukuCaramountsum1).add(tuihuorukuCaramountsum1).
					   		add(tuigonghuoshangCaramountsum1)%>
			   		</td>
				</tr>
				<%
				 weidaohuoCountsum += weidaohuoMap.get(mo.getKey()) == null?0:weidaohuoMap.get(mo.getKey()).getDcount();
				 weidaohuoCaramountsum = weidaohuoCaramountsum.add(weidaohuoMap.get(mo.getKey()) == null? BigDecimal.ZERO:weidaohuoMap.get(mo.getKey()).getDsum());
				 tihuoCountsum += tihuoMap.get(mo.getKey()) == null?0:tihuoMap.get(mo.getKey()).getDcount();
				 tihuoCaramountsum = tihuoCaramountsum.add(tihuoMap.get(mo.getKey()) == null? BigDecimal.ZERO:tihuoMap.get(mo.getKey()).getDsum());
				 rukuCountsum += rukuMap.get(mo.getKey()) == null?0:rukuMap.get(mo.getKey()).getDcount();
				 rukuCaramountsum = rukuCaramountsum.add(rukuMap.get(mo.getKey()) == null? BigDecimal.ZERO:rukuMap.get(mo.getKey()).getDsum());
				 chukuCountsum += daozhanMap.get(mo.getKey()) == null?0:daozhanMap.get(mo.getKey()).getDcount();
				 chukuCaramountsum = chukuCaramountsum.add(daozhanMap.get(mo.getKey()) == null? BigDecimal.ZERO:daozhanMap.get(mo.getKey()).getDsum());
				 daozhanCountsum += yichuzhanMap.get(mo.getKey()) == null?0:yichuzhanMap.get(mo.getKey()).getDcount();
				 daozhanCaramountsum = daozhanCaramountsum.add(yichuzhanMap.get(mo.getKey()) == null? BigDecimal.ZERO:yichuzhanMap.get(mo.getKey()).getDsum());
				 yichuzhanCountsum += yichuzhanMap.get(mo.getKey()) == null?0:yichuzhanMap.get(mo.getKey()).getDcount();
				 yichuzhanCaramountsum = yichuzhanCaramountsum.add(yichuzhanMap.get(mo.getKey()) == null? BigDecimal.ZERO:yichuzhanMap.get(mo.getKey()).getDsum());
				 zhongzhanrukuCountsum += zhongzhanrukuMap.get(mo.getKey()) == null?0:zhongzhanrukuMap.get(mo.getKey()).getDcount();
				 zhongzhuanrukuCaramountsum = zhongzhuanrukuCaramountsum.add(zhongzhanrukuMap.get(mo.getKey()) == null? BigDecimal.ZERO:zhongzhanrukuMap.get(mo.getKey()).getDsum());
				 tuihuorukuCountsum += tuihuorukuMap.get(mo.getKey()) == null?0:tuihuorukuMap.get(mo.getKey()).getDcount();
				 tuihuorukuCaramountsum = tuihuorukuCaramountsum.add(tuihuorukuMap.get(mo.getKey()) == null? BigDecimal.ZERO:tuihuorukuMap.get(mo.getKey()).getDsum());
				 tuigonghuoshangCountsum += tuigonghuoshangMap.get(mo.getKey()) == null?0:tuigonghuoshangMap.get(mo.getKey()).getDcount();
				 tuigonghuoshangCaramountsum = tuigonghuoshangCaramountsum.add(tuigonghuoshangMap.get(mo.getKey()) == null? BigDecimal.ZERO:tuigonghuoshangMap.get(mo.getKey()).getDsum());
				
				%>
				<%} %>
				<tr height="30">
			   		<td  align="center" valign="middle" ><font color ="red">合计</font> </td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/-1/weidaohuo/1"><%=weidaohuoCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=weidaohuoCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/-1/tihuo/1"><%=tihuoCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=tihuoCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/-1/ruku/1"><%=rukuCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=rukuCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/-1/chuku/1"><%=chukuCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=chukuCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/-1/daozhan/1"><%=daozhanCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=daozhanCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/-1/yichuzhan/1"><%=yichuzhanCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=yichuzhanCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/-1/Zhongzhanruku/1"><%=zhongzhanrukuCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=zhongzhuanrukuCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/-1/tuihuoruku/1"><%=tuihuorukuCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=tuihuorukuCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/-1/tuigonghuoshang/1"><%=tuigonghuoshangCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=tuigonghuoshangCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/-1/all/1"><%=weidaohuoCountsum +tihuoCountsum +rukuCountsum
			   		+chukuCountsum +daozhanCountsum+ 
			   		yichuzhanCountsum+zhongzhanrukuCountsum
			   		+tuihuorukuCountsum+tuigonghuoshangCountsum %></a></td>
			   		<td  align="right" valign="middle" >
			   		<%=weidaohuoCaramountsum.add(tihuoCaramountsum).
			   		add(tihuoCaramountsum).add(rukuCaramountsum).add(chukuCaramountsum).
			   		add(daozhanCaramountsum).add(yichuzhanCaramountsum)
			   		.add(zhongzhuanrukuCaramountsum).add(tuihuorukuCaramountsum).
			   		add(tuigonghuoshangCaramountsum) %>
			   		</td>
				</tr>
				<%} %>
			   	</tbody>
			</table>
		    </div>
		    <div class="jg_10"></div><div class="jg_10"></div>
	</div>
		</div>
	</div>
</div>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body> 
</html>