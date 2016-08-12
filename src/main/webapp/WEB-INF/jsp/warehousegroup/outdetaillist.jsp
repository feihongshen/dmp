<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.print.template.PrintTemplate"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.domain.orderflow.*"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%
    List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
    List<PrintView> printList = (List<PrintView>)request.getAttribute("printList");
    List<PrintTemplate> pList = (List<PrintTemplate>)request.getAttribute("printtemplateList");
    List<User> uList = (List<User>)request.getAttribute("uList");
    List<Truck> tList = (List<Truck>)request.getAttribute("tList");
    
    String strtime=request.getParameter("strtime")==null?"":(String)request.getParameter("strtime");
    String endtime=request.getParameter("endtime")==null?"":(String)request.getParameter("endtime");
    String baleno=request.getParameter("baleno")==null?"":(String)request.getParameter("baleno");
    String[] branchidList=request.getParameterValues("branchid");
    long truckid=Long.parseLong(request.getParameter("truckid")==null?"-1":request.getParameter("truckid").toString());
    long driverid=Long.parseLong(request.getParameter("driverid")==null?"-1":request.getParameter("driverid").toString());
    int m=-1;
%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>出库交接单打印</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/multiple-select.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<%-- <script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script> --%>
<%-- <script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>  --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/jquery-1.8.0.min.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/multiple-select.js"></script>
<%-- <script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script> --%>
<script type="text/javascript">
function cwbfind(){
	/* var branchid = $('input[name="branchid"]:checked').length; */
	var truckid = $('input[name="truckid"]:checked').length;
	 
	if($("#endtime").val() !=''&& $("#strtime").val() !=''&&$("#strtime").val()>$("#endtime").val()){
		alert("开始时间不能大于结束时间");
		return false;
	}
	if(truckid<0){
		alert("请选择车牌号!");
	}
// 	if($("#branchid").combobox('getValue')!=""&&$("#branchid").combobox('getValue')!= null){
	if($("#branchid").val()!=""&&$("#branchid").val()!= null){
		$("#searchForm").submit();
	}else{
// 		alert($("#branchid").combobox('getValue'));
		alert("抱歉，请选择下一站点！");
	}
}
function bdprint(){
	var isprint = "";
	$('input[name="isprint"]:checked').each(function(){ //由于复选框一般选中的是多个,所以可以循环输出
		isprint = $(this).val();
		});
	var printtemplateid = $("#printtemplateid").val();
	if(isprint==""){
		alert("请选择要打印的订单！");
	}else if(printtemplateid==0){
		alert("请选择打印模版！");
	} else{
		$("#selectforsmtbdprintForm").attr("action","<%=request.getContextPath() %>/warehousegroupdetail/outbillprinting_defaultnew");
		$("#selectforsmtbdprintForm").submit();
	}
}
function isgetallcheck(){
	if($('input[name="isprint"]:checked').size()>0){
		$('input[name="isprint"]').each(function(){
			$(this).attr("checked",false);
		});
	}else{
		$('input[name="isprint"]').attr("checked",true);
	}
}

$(function(){
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
/* 	$("#branchid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择下一站' });  */
	
	<%if(pList.size()==0){%>
		alert("您还没有设置模版，请先设置模版！");
	<%}%>
/* 	$("#branchid").change(function(){
		$("#nextbranchid").val($(this).val());	
	}); */
	/* $("#branchid").combobox({
		 multiple:true,
		onChange: function (n,o) {
			$("#nextbranchid").val($("#branchid").combobox('getValue'));	
		}
	}); */
	
	$("#templateid").change(function(){
		$("#printtemplateid").val($(this).val());	
	});
	
});

function cwbexport(){
	var isprint="";
	$('input[name="isprint"]:checked').each(function(){
		isprint=$(this).val();
	});
	if(isprint==""){
		alert("请选择要导出的订单！");
	}else{
		$("#selectforsmtbdprintForm").attr("action","<%=request.getContextPath() %>/warehousegroupdetail/exportforoutwarehouse");
		$("#selectforsmtbdprintForm").submit();
		
	}
	
}

$(function(){
	
$("#branchid").multipleSelect({
    filter: true
});
})
</script>
</head>
<body style="background:#f5f5f5">
<div class="right_box">
	<div class="inputselect_box">
				<span>
				</span>
				<form action="1" method="post" id="searchForm">
					<input type="hidden"  name="isshow" value="1"  />
					 <div>  
	<table>	<tr>
		<%-- <td>
                        站点名称
			<select name="ismohu" id="ismohu" class="select1">
					<option value ="1"<%if(1==(request.getParameter("ismohu")==null?1:Long.parseLong(request.getParameter("ismohu")))){%>selected="selected"<%}%>>模糊匹配</option>
					<option value ="2"<%if(2==(request.getParameter("ismohu")==null?1:Long.parseLong(request.getParameter("ismohu")))){%>selected="selected"<%}%>>精确匹配</option>
			 </select>
			 </td> --%>
			<%--  <input name="branchname" id="branchname" class="input_text1" onKeydown="if(event.keyCode==13&&$(this).val().length>0){moHuOrJingQueSlect($('#ismohu').val(),'<%=request.getContextPath()%>','branchid',$(this).val());}"/> --%>
					<td>
					 下一站
					<%--  <select name="branchid" id="branchid"  style="width:120px;" class="easyui-combobox">

				        <%for(Branch b :branchlist){ %>
				           <option value="<%=b.getBranchid()%>" 
				           <%if(branchidList!=null&&branchidList.length>0){
				        	for(int i=0;i<branchidList.length;i++){
			            	if(b.getBranchid()==Long.parseLong(branchidList[i])){
			            		%>selected="selected"<%
			            	     break;
			            	}}}%>><%=b.getBranchname()%></option>
				        <%} %>
			        </select> --%>
			        <select name="branchid" id="branchid" multiple="multiple"  style="width:180px;">

				        <%for(Branch b :branchlist){ 
// 				        	m++;
				        %>
				           <option value="<%=b.getBranchid()%>" sid=<%=m %>
				           <%if(branchidList!=null&&branchidList.length>0){
				        	for(int i=0;i<branchidList.length;i++){
			            	if(b.getBranchid()==Long.parseLong(branchidList[i])){
			            		%>selected="selected"<%
			            	    break;
			            	}}}%>><%=b.getBranchname()%></option>
				        <%} %>
			        </select>
			        </td>
<!-- 			        [<a href="javascript:multiSelectAll('branchid',1,'请选择');">全选</a>]
					[<a href="javascript:multiSelectAll('branchid',0,'请选择');">取消全选</a>] -->
					<td style="margin-right: 0px"><%=request.getAttribute("time") %></td>
				<td style="border-right : 3px">
					   
						<input type ="text" name ="strtime" id="strtime"  value="<%=strtime %>" class="easyui-datetimebox" style="width: 141px;margin-top: auto;";/>
				</td>
				<td>到</td>
				<td style="border-left: 3px">
						<input type ="text" name ="endtime" id="endtime"  value="<%=endtime %>" class="easyui-datetimebox" style="width: 141px;margin-top: auto;"/>
						
				</td>
				<td>订单类型：<select name="cwbOrderTypeId">
				<option value="0">--请选择--</option>
				  <%
				  for(int i :CwbOrderTypeIdEnum.getMap().keySet()){
				  %>
				 	 <option value="<%=i%>" <%=String.valueOf(i).equals(request.getParameter("cwbOrderTypeId"))?"selected='selected'":""%>><%=CwbOrderTypeIdEnum.getTextByValue(i)%></option>
				 	 <%
				  } 
				  %>
					
				</select></td>
				<td style="border-left: 0px">（未打印订单只保留15天）</td>
				</tr>		
			</table>	
			</div>
				<br/>
				<br/>
				<!-- 添加包号查询 -->
				包&nbsp;&nbsp;号:<input style="width:207px;height: auto;" type ="text" name ="baleno" id="baleno"  value="<%=baleno%>" class="input_text1"/>&nbsp;&nbsp;
				<!-- 添加驾驶员-->
				
				驾驶员：
				<select id="driverid" name="driverid" style="width: 160px" class="select1">
					<option value="-1" selected>请选择</option>
					<%for(User u : uList){ %>
						<option value="<%=u.getUserid() %>" <%if(driverid==u.getUserid()){%> selected="selected" <%}%> ><%=u.getRealname() %></option>
					<%} %>
		        </select>
		        <!--添加车辆  -->
				车辆：
				<select id="truckid" name="truckid" style="width: 160px" class="select1">
					<option value="-1" selected>请选择</option>
					<%for(Truck t : tList){ %>
						<option value="<%=t.getTruckid() %>" <%if(truckid==t.getTruckid()){%> selected="selected" <%}%> ><%=t.getTruckno() %></option>
					<%} %>
		        </select>
		        &nbsp;&nbsp;
		      　　<input type="button" id="find" onclick="cwbfind();" value="查询" class="input_button2" />
		      <%if(printList!=null&&printList.size()>0){ %>
		      　　<input type="button" id="forexport" onclick="cwbexport();" value="导出" class="input_button2" />
		      <%} %>
		      <div style="float:right;margin-top: -20px;">   
				     	 打印模版：<select name="templateid" id="templateid" class="select1">
					  			<%for(PrintTemplate pt : pList){ %>
					  				<option value="<%=pt.getId()%>"><%=pt.getName() %>（<%if(pt.getTemplatetype()==1){ %>按单<%}else if(pt.getTemplatetype()==2){ %>汇总<%} else if(pt.getTemplatetype()==4){ %>武汉飞远<%} %>）</option>
					  			<%} %>
							</select>
				      <input type="button" onclick="bdprint();" value="打印" class="input_button2" />
				      <a href="<%=request.getContextPath() %>/warehousegroupdetail/historyoutlist/1/${type}">历史打印列表>></a>
					</div>
				</form>
				</div>
				<br/><br/><br/><br/><br/>
				<div class="right_title">
				<div style="height:30px;"></div>
				<div style="height:380px;width:98%;overflow: auto;">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
					<tr class="font_1">
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作<a style="cursor: pointer;" onclick="isgetallcheck();">（全选）</a></td>
						<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">订单号/包号</td>
						<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">出库站点</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">当前状态</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">出库时间</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
					</tr>
				<!-- </table> -->
				<form action="" method="post" id="selectforsmtbdprintForm" >
				<input type="hidden"  name="flowordertype" value="<%=request.getAttribute("flowordertype") %>" />
					<!-- <table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table"> -->
					<%for(PrintView pv:printList){ %>
						 <tr>
						 	<td width="10%" align="center" valign="middle"><input id="isprint" name="isprint" type="checkbox" value="<%=pv.getCwb() %>" checked="checked"/></td>
							<%if(!"".equals(pv.getBaleno()) && pv.getBaleno() != null ){ %>
							<td width="25%" align="center" valign="middle"><%=pv.getCwb() %>--<%=pv.getBaleno() %></td>
							<%}else{ %>
							<td width="25%" align="center" valign="middle"><%=pv.getCwb() %></td>
							<%} %>
							<td width="25%" align="center" valign="middle"><%=pv.getNextbranchname()%></td>
						    <td width="10%" align="center" valign="middle"><%=pv.getFlowordertypeMethod()%></td>
						    <td width="20%" align="center" valign="middle"><%=pv.getOutstoreroomtime()%></td>
						    <td width="10%" align="center" valign="middle"><%=CwbOrderTypeIdEnum.getTextByValue(pv.getCwbordertypeid())%></td>
						</tr>
					<%} %>
					</table>
					
					<input id="nextbranchid" name="nextbranchid" value="<%=request.getAttribute("branchids")%>" type="hidden"/>
					<input id="printtemplateid" name="printtemplateid" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("templateid"))%>" type="hidden"/>
					<input name="type" value="<%=request.getAttribute("type") %>" type="hidden"/>
					<input name="baleno" value="<%=request.getAttribute("baleno") %>" type="hidden"/>
					<input name="truckid" value="<%=request.getAttribute("truckid") %>" type="hidden"/>
					<input name="driverid" value="<%=request.getAttribute("driverid") %>" type="hidden"/>
					<input name="starttime" value="${realStrTime }" type="hidden"/>
					<input name="endtime" value="${realEndTime }" type="hidden"/>
				</form>
				</div>
				<div class="jg_10"></div><div class="jg_10"></div>
				</div>
			</div>			
	<div class="jg_10"></div>
	<div class="clear"></div>


<script type="text/javascript">
$(function() {
	$("#selectPg").val(<%=request.getAttribute("page") %>);
	$("#printtemplateid").val($("#templateid").val());
});

</script>
</body>
</html>

