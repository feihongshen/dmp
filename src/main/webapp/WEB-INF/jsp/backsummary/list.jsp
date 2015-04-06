<%@page import="cn.explink.domain.BackSummary"%>
<%@page import="cn.explink.enumutil.BackTypeEnum"%>
<%@page import="java.text.SimpleDateFormat,java.text.DecimalFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<BackSummary> list = request.getAttribute("backSummary")==null?null:(List<BackSummary>)request.getAttribute("backSummary");
	String time=request.getParameter("time")==null?"":request.getParameter("time");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>退货中心出入库跟踪表</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$("#time").datepicker();
	
	$("#find").click(function(){
		if($("#time").val()==""){
			alert("请选择日期");
			return;
		}
		$("#searchForm").submit();
	});
});

function exportExcel(){
	$("#export").attr("disabled","disabled"); 
	$("#export").val("请稍后……");
	$("#searchForm2").submit();	
}
</script>
</head>
<body style="background:#f5f5f5">

<div class="right_box">
	<div class="inputselect_box">
		<form action="" method="post" id="searchForm">
			日期：
			<input type="text" name="time" id="time" value ="<%=time%>"/>
			<input type="button" id="find" value="查询"  class="input_button2" />
			<%if(list!=null&&!list.isEmpty()){%>
			<input type ="button" id="export" value="导出" class="input_button2" onclick="exportExcel()"/>
			<%}%>
		</form>
	</div>
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		<tr class="font_1">
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff" rowspan="2">日期</td>
			<td width="40%" valign="middle" bgcolor="#eef6ff" colspan="2">超期未到退货中心数量</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff" rowspan="2">退供应商出仓数量</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff" rowspan="2">当日入库订单量</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff" rowspan="2">退货占比</td>
		</tr>
		<tr class="font_1">
			<td align="center" valign="middle" bgcolor="#eef6ff">站点超24H未到退货中心数量</td>
			<td align="center" valign="middle" bgcolor="#eef6ff">站点超72H未到退货中心数量</td>
		</tr>
		<% if(list!=null&&!list.isEmpty()){
			for(BackSummary o:list){
		%>
		<tr>
			<td><%=time%></td>
			<td><a <%if(o.getNums24()>0){out.print("href="+request.getContextPath()+"/backsummary/detail/"+o.getSummaryid()+"/"+BackTypeEnum.TuiHuoChuZhan24.getValue()+"/"+time+"/1");}%>>
					<%=o.getNums24()%>
				</a>
			</td>
			<td><a <%if(o.getNums72()>0){out.print("href="+request.getContextPath()+"/backsummary/detail/"+o.getSummaryid()+"/"+BackTypeEnum.TuiHuoChuZhan72.getValue()+"/"+time+"/1");}%>>
					<%=o.getNums72()%>
				</a>
			</td>
			<td><a <%if(o.getNumsout()>0){out.print("href="+request.getContextPath()+"/backsummary/detail/"+o.getSummaryid()+"/"+BackTypeEnum.TuiGongHuoShangChuKu.getValue()+"/"+time+"/1");}%>>
					<%=o.getNumsout()%>
				</a>
			</td>
			<td><a <%if(o.getNumsinto()>0){out.print("href="+request.getContextPath()+"/backsummary/detail/"+o.getSummaryid()+"/"+BackTypeEnum.KuFangRuKu.getValue()+"/"+time+"/1");}%>>
					<%=o.getNumsinto()%>
				</a>
			</td>
			<td>
				<%
					if(o.getNumsinto()==0){
						out.print("--");
					}else if(o.getNumsout()==0){
						out.print("0.00%");
					}else{
						DecimalFormat df = new DecimalFormat("0.00");//格式化小数    
						double z=(double)o.getNumsout()/o.getNumsinto();
						out.print(df.format(z*100)+"%");
					}
				%>
			</td>
		</tr>
		<%}}%>
	</table>
<form id="searchForm2" action ="<%=request.getContextPath()%>/backsummary/exportExcleList" method = "post" > 
	<input type="hidden" name="exceltime" id="exceltime" value ="<%=time%>"/>
</form> 				
</div>
</body>
</html>
