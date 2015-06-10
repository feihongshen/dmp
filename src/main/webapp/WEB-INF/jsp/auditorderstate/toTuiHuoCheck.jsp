<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.util.StringUtil"%>
<%
	List<OrderBackCheck> orderbackList = (List<OrderBackCheck>)request.getAttribute("orderbackList");
	List<CwbOrderView> covlist = (List<CwbOrderView>)request.getAttribute("covlist");
	List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
	List<Customer> customerlist = (List<Customer>)request.getAttribute("customerList");
	//List<CwbOrderView> cwbList = (List<CwbOrderView>)request.getAttribute("cwbList");
	List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
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
//提交审核按钮
function sub(){
	var ids="";//选中id
	$('input[type="checkbox"][name="checkbox"]').each(
	       	function() {
	          	if($(this).attr("checked")=="checked"){
	          		ids+=$(this).val()+",";
	          	}
	          }
	       );
	if(ids.length==0){
		alert("无提交数据");
		return false;
	}
	
	if(confirm("确认审核吗？")){
		$("#submitF").attr("disabled","disabled");
    	$("#submitF").val("请稍候");
    	
    	$.ajax({
    		type: "POST",
    		url:'<%=request.getContextPath()%>/orderBackCheck/save',
    		data:"ids="+ids.substring(0,ids.lastIndexOf(",")),
    		dataType : "json",
    		success : function(data) {
    			if(data.errorCode==0){
    				alert(data.error);
    				location.href="<%=request.getContextPath()%>/orderBackCheck/toTuiHuoCheck?searchType="+$("#searchType").val();
    			}else{
    				alert(data.error);
    				location.href="<%=request.getContextPath()%>/orderBackCheck/toTuiHuoCheck?searchType="+$("#searchType").val();
    			}
    		}
    	});
	}
}

function exportField(){
	if(<%=orderbackList!=null&&!orderbackList.isEmpty()%>){
		$("#btnval").attr("disabled","disabled"); 
	 	$("#btnval").val("请稍后……");
		$("#exportForm").submit();	
	}else{
		alert("没有做查询操作，不能导出！");
	}
}

//全选按钮
function btnClick(){
	$("[name='checkbox']").attr("checked",'true');//全选  
}

//查询全部
function submitAll(){
	$("#searchType").val("1");
	$("#searchForm").submit();
}

//根据订单号查询
function submitCwb(){
	$("#searchType").val("0");
	$("#searchForm").submit();
}

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
</HEAD>
<BODY style="background:#f5f5f5"  marginwidth="0" marginheight="0">
<div class="right_box">
	<div>
		<div class="kfsh_tabbtn">
		</div>
		<div class="tabbox">
				<div style="position:relative; z-index:0 " >
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							<form action="./toTuiHuoCheck" method="POST" id="searchForm">

							<table>
								<tr>
									<td rowspan="2">
										订单号：
										<textarea name="cwb" rows="3" class="kfsh_text" id="cwb" onFocus="if(this.value=='查询多个订单用回车隔开'){this.value=''}" onBlur="if(this.value==''){this.value='查询多个订单用回车隔开'}" >查询多个订单用回车隔开</textarea>
									</td>
									<td>
										&nbsp;&nbsp;
										订单类型:
										<select name ="cwbtypeid" id ="cwbtypeid">
											<option  value ="0">全部</option>
												<option value ="<%=CwbOrderTypeIdEnum.Peisong.getValue()%>"><%=CwbOrderTypeIdEnum.Peisong.getText()%></option>
												<option value ="<%=CwbOrderTypeIdEnum.Shangmentui.getValue()%>"><%=CwbOrderTypeIdEnum.Shangmentui.getText()%></option>
												<option value ="<%=CwbOrderTypeIdEnum.Shangmenhuan.getValue()%>"><%=CwbOrderTypeIdEnum.Shangmenhuan.getText()%></option>
										</select>
									</td>
									<td>	
										&nbsp;&nbsp;
										客户名称:
										<select name ="customerid" id ="customerid">
											<option  value ="0">全部</option>
											<%if(customerList!=null){ %>
												<%for(Customer cus:customerList){ %>
												<option value ="<%=cus.getCustomerid()%>"><%=cus.getCustomername()%></option>
												<%} }%>
										</select>
										&nbsp;&nbsp;&nbsp;
										配送站点:
										<select name ="branchid" id ="branchid">
											<option  value ="0">全部</option>
											<%if(branchList!=null){ %>
												<%for(Branch br:branchList){ %>
												<option value ="<%=br.getBranchid()%>"><%=br.getBranchname()%></option>
												<%} }%>
										</select>
									</td>
								</tr>
								<tr>
									<td>
										&nbsp;&nbsp;
										审核状态:
										<select name ="auditstate" id ="auditstate">
											<option  value ="0">全部</option>
												<option value = "<%=ApplyStateEnum.daishenhe.getValue()%>"><%=ApplyStateEnum.daishenhe.getText() %></option>
												<option value ="<%=ApplyStateEnum.yishenhe.getValue() %>"><%=ApplyStateEnum.yishenhe.getText() %></option>
										</select>
									</td>
									<td>
										&nbsp;&nbsp;
										审核结果:
										<select name ="shenheresult" id ="shenheresult">
											<option  value ="0">全部</option>
											<option value ="<%=TuihuoResultEnum.querentuihuo.getValue()%>"><%=TuihuoResultEnum.querentuihuo.getText() %></option>
											<option value ="<%=TuihuoResultEnum.zhandianzhiliu.getValue() %>"><%=TuihuoResultEnum.zhandianzhiliu.getText() %></option>
											<option value ="<%=TuihuoResultEnum.zhandianmaidan.getValue() %>" ><%=TuihuoResultEnum.zhandianmaidan.getText() %></option>
										</select>
										&nbsp;&nbsp;
										归班反馈时间:
											<input type ="text" name ="begindate" id="strtime"  value="" class="input_text1" style="height:20px;"/>
										到
											<input type ="text" name ="enddate" id="endtime"  value="" class="input_text1" style="height:20px;"/>
									</td>
									<td>
									</td>
								</tr>
							</table>
							<table>
								<tr>
									<td width="20%">
										<input type="hidden" value="<%=request.getParameter("searchType")==null?"":request.getParameter("searchType")%>" id="searchType" name="searchType">
										<input type="button" onclick="submitCwb()" value="查询" class="input_button2">&nbsp;&nbsp;
										<input type="button" onclick="submitAll()" value="重置" class="input_button2">&nbsp;&nbsp;
									</td>
									<td width="20%">
										<input type="button" onclick="getdisplay()" value="确认退货" class="input_button2">&nbsp;&nbsp;
										<input type="button" onclick="getdisplay()" value="站点滞留" class="input_button2">&nbsp;&nbsp;
										<input type="button" onclick="getdisplay()" value="站点买单" class="input_button2">&nbsp;&nbsp;
									</td>
									<td>
										<%if(orderbackList!=null&&!orderbackList.isEmpty()){%><span>
											<input name="" type="button" id="btnval" value="导出" class="input_button2" onclick="exportField();"/>
										</span> <%} %>
									</td>
								</tr>
							</table>
						</div>
							
								<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="40" align="center" valign="middle" bgcolor="#E7F4E3"><a href="#" onclick="btnClick();">全选</a></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单类型</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">客户名称</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">配送站点</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">归班反馈时间</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div style="height:100px"></div>
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
							<%if(covlist!=null&&!covlist.isEmpty()){%> 
								<%for(CwbOrderView cwb :covlist){ %>
									<tr>
										<td  width="40" align="center" valign="middle">
											<input type="checkbox" checked="true" name="checkbox" id="checkbox" value="<%=cwb.getScancwb()%>"/>
										</td>
										<td width="100" align="center" valign="middle"><%=cwb.getCwb() %></td>
										<td width="100" align="center" valign="middle"><%=StringUtil.nullConvertToEmptyString(cwb.getCwbordertypename())%></td>
										<td width="100" align="center" valign="middle"><%=StringUtil.nullConvertToEmptyString(cwb.getCustomername()) %></td>
										<td width="100" align="center" valign="middle"><%=StringUtil.nullConvertToEmptyString(cwb.getBranchname())%></td>
										<td width="100" align="center" valign="middle"><%=StringUtil.nullConvertToEmptyString(cwb.getCreatetime())%></td>
									</tr>
							<%} }%>
							</tbody>
						</table>
				</div>
				<div style="height:40px"></div>
		</div>
	</div>
</div>
<form action="<%=request.getContextPath() %>/orderBackCheck/export" method="post" id="exportForm">
	<textarea style="display:none" name="cwb" id="cwb"><%=request.getParameter("cwb")==null?"":request.getParameter("cwb")%></textarea>
	<input type="hidden" value="<%=request.getParameter("searchType")==null?"":request.getParameter("searchType")%>" id="searchType" name="searchType"/>
</form>
</BODY>
</HTML>
