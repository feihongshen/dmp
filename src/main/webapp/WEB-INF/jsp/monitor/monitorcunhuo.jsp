<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.util.Page"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>存货报表</title>
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
List<Branch> branchlist = request.getAttribute("branchList") == null ? new ArrayList<Branch>():(List<Branch>)request.getAttribute("branchList") ;
List dispatchbranchidList =(List) request.getAttribute("dispatchbranchidStr");
Map<Long,String> branchmap = (Map<Long,String>)request.getAttribute("branchnameMap");
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
$(function() {
	$("#dispatchbranchid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择' });
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
		<form action="<%=request.getContextPath()%>/kpigather/list/1" method="post" id="searchForm">
		  &nbsp;&nbsp;选择机构：
			<select name ="dispatchbranchid" id ="dispatchbranchid"  multiple="multiple" style="width: 320px;">
		          <%for(Branch b : branchlist){ %>
		          <option value ="<%=b.getBranchid() %>" 
		           <%if(!dispatchbranchidList.isEmpty()) 
			            {for(int i=0;i<dispatchbranchidList.size();i++){
			            	if(b.getBranchid()== new Long(dispatchbranchidList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>  ><%=b.getBranchname()%></option>
		          <%}%>
			 </select>[<a href="javascript:multiSelectAll('dispatchbranchid',1,'请选择');">全选</a>][<a href="javascript:multiSelectAll('dispatchbranchid',0,'请选择');">取消全选</a>]
			 <input type="button" id="find" onclick="" value="查询" class="input_button2" />
			<!-- <input type ="button" id="exportExcel" value="导出Excel" class="input_button2" /> -->
	  </form>
	</div>
  <div style="background:#FFF">
	<div style="height:25px"></div>
	<div class="jg_10"></div>
	<div class="tabbox">
	  <div style="position:relative; z-index:0 " >
	       <table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
			   <tbody>
			   	<tr class="font_1" height="30" >
			   		<td  align="center" valign="middle" rowspan="2" >机构名称</td>
			   		<td  align="center" valign="middle"  colspan="2" >库存（库房/站点）</td>
			   		<td  align="center" valign="middle"  colspan="2" >已出在途(库房/站点)</td>
			   		<td  align="center" valign="middle"  colspan="2" >未入库（库存）</td>
			   		<td  align="center" valign="middle"  colspan="2" >已退甲方未返款</td>
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
				</tr>
				
			   	<tr height="30">
			   		<td  align="center" valign="middle" >wwwwwwwwwww</td>
			   		<td  align="center" valign="middle" ></td>
			   		<td  align="center" valign="middle" ></td>
			   		<td  align="center" valign="middle" ></td>
			   		<td  align="center" valign="middle" ></td>
			   		<td  align="center" valign="middle" ></td>
			   		<td  align="center" valign="middle" ></td>
			   		<td  align="center" valign="middle" ></td>
			   		<td  align="center" valign="middle" ></td>
			   		<td  align="center" valign="middle" ></td>
			   		<td  align="center" valign="middle" ></td>
				</tr>
				
			   	</tbody>
			</table>
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