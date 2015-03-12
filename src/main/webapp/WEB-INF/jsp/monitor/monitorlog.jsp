<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.controller.MonitorLogDTO"%>
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
List<MonitorLogDTO>  monitorList = (List<MonitorLogDTO> )request.getAttribute("monitorList");
List customeridList =(List) request.getAttribute("customeridStr");
Map<String,Object> workermap = (Map<String,Object>)request.getAttribute("branckWorkNum");
String startTime = request.getAttribute("startTime")==null?"":request.getAttribute("startTime").toString();
Page page_obj = (Page)request.getAttribute("page_obj");
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
				     }%> ><%=c.getCustomername() %></option>
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
			   		<td  align="center" valign="middle"  colspan="2" >站点在站资金</td>
			   		<td  align="center" valign="middle"  colspan="2" >已出站在途</td>
			   		<td  align="center" valign="middle"  colspan="2" >已入中转库未出库</td>
			   		<td  align="center" valign="middle"  colspan="2" >已入退货库未出库</td>
			   		<td  align="center" valign="middle"  colspan="2" >退客户在途</td>
			   		<td  align="center" valign="middle"  colspan="2" >退客户未收款</td>
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
			   		<td  align="center" valign="middle" >票数</td>
			   		<td  align="center" valign="middle" >金额</td>
			   		<td  align="center" valign="middle" >票数</td>
			   		<td  align="center" valign="middle" >金额</td>
				</tr>
				<%if(monitorList != null && monitorList.size()>0){ %>
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
				 long	zaizhanzijiCountsum =0;
				 BigDecimal	zaizhanzijiCaramountsum = BigDecimal.ZERO;
				 long	yichuzhanCountsum =0;
				 BigDecimal	yichuzhanCaramountsum = BigDecimal.ZERO;
				 long	zhongzhanrukuCountsum =0;
				 BigDecimal	zhongzhuanrukuCaramountsum = BigDecimal.ZERO;
				 long	tuihuorukuCountsum =0;
				 BigDecimal	tuihuorukuCaramountsum = BigDecimal.ZERO;
				 long	tuigonghuoshangCountsum =0;
				 BigDecimal	tuigonghuoshangCaramountsum = BigDecimal.ZERO;
				 long	tuikehuweishoukuanCountsum =0;
				 BigDecimal	tuikehuweishoukuanCaramountsum = BigDecimal.ZERO;
				%> 
				<%for(MonitorLogDTO mo : monitorList){ %>
			   	<tr height="30">
			   		<td  align="center" valign="middle" ><%=customerMap.get( mo.getCustomerid()) %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getCustomerid()%>/weidaohuo/1"> <%=mo.getWeidaohuoCountsum() %> </a></td>
			   		<td  align="right" valign="middle" ><%=mo.getWeidaohuoCaramountsum() %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getCustomerid()%>/tihuo/1"> <%=mo.getTihuoCountsum() %></a></td>
			   		<td  align="right" valign="middle" ><%=mo.getTihuoCaramountsum() %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getCustomerid()%>/ruku/1"> <%=mo.getRukuCountsum() %></a></td>
			   		<td  align="right" valign="middle" ><%=mo.getRukuCaramountsum() %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getCustomerid()%>/chuku/1"> <%=mo.getChukuCountsum() %></a></td>
			   		<td  align="right" valign="middle" ><%=mo.getChukuCaramountsum() %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getCustomerid()%>/daozhan/1"> <%=mo.getDaozhanCountsum() %></a></td>
			   		<td  align="right" valign="middle" ><%=mo.getDaozhanCaramountsum() %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getCustomerid()%>/zaizhanziji/1"> <%=mo.getZaizhanzijiCountsum() %></a></td>
			   		<td  align="right" valign="middle" ><%=mo.getZaizhanzijiCaramountsum() %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getCustomerid()%>/yichuzhan/1"> <%=mo.getYichuzhanCountsum() %></a></td>
			   		<td  align="right" valign="middle" ><%=mo.getYichuzhanCaramountsum() %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getCustomerid()%>/Zhongzhanruku/1"> <%=mo.getZhongzhanrukuCountsum() %></a></td>
			   		<td  align="right" valign="middle" ><%=mo.getZhongzhuanrukuCaramountsum() %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getCustomerid()%>/tuihuoruku/1"> <%=mo.getTuihuorukuCountsum() %></a></td>
			   		<td  align="right" valign="middle" ><%=mo.getTuihuorukuCaramountsum() %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getCustomerid()%>/tuigonghuoshang/1"> <%=mo.getTuigonghuoshangCountsum() %></a></td>
			   		<td  align="right" valign="middle" ><%=mo.getTuigonghuoshangCaramountsum() %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getCustomerid()%>/tuikehuweishoukuan/1"> <%=mo.getTuikehuweishoukuanCountsum() %></a></td>
			   		<td  align="right" valign="middle" ><%=mo.getTuikehuweishoukuanCaramountsum() %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/show/<%=mo.getCustomerid()%>/all/1"> <%= mo.getWeidaohuoCountsum() +mo.getTihuoCountsum()+mo.getRukuCountsum()
			   		+mo.getChukuCountsum()+mo.getDaozhanCountsum()+ mo.getZaizhanzijiCountsum()+
			   		mo.getYichuzhanCountsum()+mo.getZhongzhanrukuCountsum()
			   		+mo.getTuihuorukuCountsum()+mo.getTuigonghuoshangCountsum()+mo.getTuikehuweishoukuanCountsum()  %></a></td>
			   		<td  align="right" valign="middle" >
			   		<%=mo.getWeidaohuoCaramountsum().add(mo.getTihuoCaramountsum()).
			   		add(mo.getTihuoCaramountsum()).add(mo.getRukuCaramountsum()).add(mo.getChukuCaramountsum()).
			   		add(mo.getDaozhanCaramountsum()).add(mo.getZaizhanzijiCaramountsum()).add(mo.getYichuzhanCaramountsum())
			   		.add(mo.getZhongzhuanrukuCaramountsum()).add(mo.getTuihuorukuCaramountsum()).
			   		add(mo.getTuigonghuoshangCaramountsum()).add(mo.getTuikehuweishoukuanCaramountsum()) %>
			   		</td>
				</tr>
				<%
				 weidaohuoCountsum += mo.getWeidaohuoCountsum();
				 weidaohuoCaramountsum = weidaohuoCaramountsum.add(mo.getWeidaohuoCaramountsum());
				 tihuoCountsum += mo.getTihuoCountsum();
				 tihuoCaramountsum = tihuoCaramountsum.add(mo.getTihuoCaramountsum());
				 rukuCountsum += mo.getRukuCountsum();
				 rukuCaramountsum = rukuCaramountsum.add(mo.getRukuCaramountsum());
				 chukuCountsum += mo.getChukuCountsum();
				 chukuCaramountsum = chukuCaramountsum.add(mo.getChukuCaramountsum());
				 daozhanCountsum += mo.getDaozhanCountsum();
				 daozhanCaramountsum = daozhanCaramountsum.add(mo.getDaozhanCaramountsum());
				 zaizhanzijiCountsum += mo.getZaizhanzijiCountsum();
				 zaizhanzijiCaramountsum = zaizhanzijiCaramountsum.add(mo.getZaizhanzijiCaramountsum());
				 yichuzhanCountsum += mo.getYichuzhanCountsum();
				 yichuzhanCaramountsum = yichuzhanCaramountsum.add(mo.getYichuzhanCaramountsum());
				 zhongzhanrukuCountsum += mo.getZhongzhanrukuCountsum();
				 zhongzhuanrukuCaramountsum = zhongzhuanrukuCaramountsum.add(mo.getZhongzhuanrukuCaramountsum());
				 tuihuorukuCountsum += mo.getTuihuorukuCountsum();
				 tuihuorukuCaramountsum = tuihuorukuCaramountsum.add(mo.getTuihuorukuCaramountsum());
				 tuigonghuoshangCountsum += mo.getTuigonghuoshangCountsum();
				 tuigonghuoshangCaramountsum = tuigonghuoshangCaramountsum.add(mo.getTuigonghuoshangCaramountsum());
				 tuikehuweishoukuanCountsum += mo.getTuikehuweishoukuanCountsum();
				 tuikehuweishoukuanCaramountsum = tuikehuweishoukuanCaramountsum.add(mo.getTuikehuweishoukuanCaramountsum());
				
				%>
				<%} %>
				<tr height="30">
			   		<td  align="center" valign="middle" ><font color ="red">合计</font> </td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/showAll/weidaohuo/1"><%=weidaohuoCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=weidaohuoCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/showAll/tihuo/1"><%=tihuoCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=tihuoCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/showAll/ruku/1"><%=rukuCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=rukuCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/showAll/chuku/1"><%=chukuCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=chukuCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/showAll/daozhan/1"><%=daozhanCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=daozhanCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/showAll/zaizhanziji/1"><%=zaizhanzijiCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=zaizhanzijiCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/showAll/yichuzhan/1"><%=yichuzhanCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=yichuzhanCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/showAll/zhongzhanruku/1"><%=zhongzhanrukuCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=zhongzhuanrukuCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/showAll/tuihuoruku/1"><%=tuihuorukuCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=tuihuorukuCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/showAll/tuigonghuoshang/1"><%=tuigonghuoshangCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=tuigonghuoshangCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/showAll/tuikehuweishoukuan/1"><%=tuikehuweishoukuanCountsum %></a></td>
			   		<td  align="right" valign="middle" ><%=tuikehuweishoukuanCaramountsum %></td>
			   		<td  align="center" valign="middle" ><a href="<%=request.getContextPath()%>/monitorlog/showAll/all/1"><%=weidaohuoCountsum +tihuoCountsum +rukuCountsum
			   		+chukuCountsum +daozhanCountsum+ zaizhanzijiCountsum+
			   		yichuzhanCountsum+zhongzhanrukuCountsum
			   		+tuihuorukuCountsum+tuigonghuoshangCountsum+tuikehuweishoukuanCountsum  %></a></td>
			   		<td  align="right" valign="middle" >
			   		<%=weidaohuoCaramountsum.add(tihuoCaramountsum).
			   		add(tihuoCaramountsum).add(rukuCaramountsum).add(chukuCaramountsum).
			   		add(daozhanCaramountsum).add(zaizhanzijiCaramountsum).add(yichuzhanCaramountsum)
			   		.add(zhongzhuanrukuCaramountsum).add(tuihuorukuCaramountsum).
			   		add(tuigonghuoshangCaramountsum).add(tuikehuweishoukuanCaramountsum) %>
			   		</td>
				</tr>
				<%} %>
			   	</tbody>
			</table>
		    </div>
		    <div class="jg_10"></div><div class="jg_10"></div>
	</div>
	<%if(page_obj != null && page_obj.getMaxpage()>1){ %>
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
		</div>
	</div>
</div>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body> 
</html>