<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.OrderBackCheck"%>
<%@page import="cn.explink.util.StringUtil"%>
<%
	List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
	List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
	List<CwbOrder> cwblist = (List<CwbOrder>)request.getAttribute("cwbList");
	Map<Long,Customer> mapcustomerlist = (Map<Long,Customer>)request.getAttribute("mapcustomerlist");
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
	
	if(confirm("确认要审核为中转件吗？")){
		$("#submitF").attr("disabled","disabled");
    	$("#submitF").val("请稍候");
    	
    	$.ajax({
    		type: "POST",
    		url:'<%=request.getContextPath()%>/cwborder/saveChangeZhongZhuan',
    		data:"ids="+ids.substring(0,ids.lastIndexOf(",")),
    		dataType : "json",
    		success : function(data) {
    			if(data.errorCode==0){
    				alert(data.error);
    				location.href="<%=request.getContextPath()%>/cwborder/toChangeZhongZhuan?searchType="+$("#searchType").val();
    			}else{
    				alert(data.error);
    				location.href="<%=request.getContextPath()%>/cwborder/toChangeZhongZhuan?searchType="+$("#searchType").val();
    			}
    		}
    	});
	}
}

function exportField(){
	if(<%=cwblist!=null&&!cwblist.isEmpty()%>){
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
							<form action="./toChangeZhongZhuan" method="POST" id="searchForm">
									<%
							if(cwblist!=null&&!cwblist.isEmpty()){%><span>
								</span> <%} %>订单号：
								<textarea name="cwb" rows="3" class="kfsh_text" id="cwb" onFocus="if(this.value=='查询多个订单用回车隔开'){this.value=''}" onBlur="if(this.value==''){this.value='查询多个订单用回车隔开'}" >查询多个订单用回车隔开</textarea>
								订单类型:
								<select name ="cwbtypeid" id ="cwbtypeid">
									<option  value ="0">全部</option>
										<option value ="<%=CwbOrderTypeIdEnum.Peisong.getValue()%>"><%=CwbOrderTypeIdEnum.Peisong.getText()%></option>
										<option value ="<%=CwbOrderTypeIdEnum.Shangmentui.getValue()%>"><%=CwbOrderTypeIdEnum.Shangmentui.getText()%></option>
										<option value ="<%=CwbOrderTypeIdEnum.Shangmenhuan.getValue()%>"><%=CwbOrderTypeIdEnum.Shangmenhuan.getText()%></option>
								</select>
								客户名称:
								<select name ="customerid" id ="customerid">
									<option  value ="0">全部</option>
									<%if(customerList!=null){ %>
										<%for(Customer cus:customerList){ %>
										<option value ="<%=cus.getCustomerid()%>"><%=cus.getCustomername()%></option>
										<%} %>
									<%} %>
								</select>
								配送站点:
								<select name ="branchid" id ="branchid">
									<option  value ="0">全部</option>
									<%if(branchList!=null){ %>
										<%for(Branch br:branchList){ %>
										<option value ="<%=br.getBranchid()%>"><%=br.getBranchname()%></option>
										<%} }%>
								</select>
								审核状态:
								<select name ="auditstate" id ="auditstate">
									<option  value ="0">全部</option>
										<option value ="1">待审核</option>
										<option value ="<%=FlowOrderTypeEnum.YiShenHe.getValue() %>">已<%=FlowOrderTypeEnum.YiShenHe.getText()%></option>
								</select>
								归班反馈时间:
									<input type ="text" name ="begindate" id="strtime"  value="" class="input_text1" style="height:20px;"/>
								到
									<input type ="text" name ="enddate" id="endtime"  value="" class="input_text1" style="height:20px;"/>
								<input type="hidden" value="<%=request.getParameter("searchType")==null?"":request.getParameter("searchType")%>" id="searchType" name="searchType">
								<input type="button" onclick="submitCwb()" value="查询" class="input_button2">&nbsp;&nbsp;
								<input type="button" onclick="" value="重置" class="input_button2">&nbsp;&nbsp;
								<input type="button" onclick="sub()" value="审核通过" class="input_button2">&nbsp;&nbsp;
								<input type="button" onclick="submitCwb()" value="审核不通过" class="input_button2">&nbsp;&nbsp;
								<%if(cwblist!=null&&!cwblist.isEmpty()){%><span>
									<input name="btnval" type="button" id="btnval" value="导出" class="input_button2" onclick="exportField();"/>
								</span> <%} %>
								
							</form>
						</div>
								<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="40" align="center" valign="middle" bgcolor="#E7F4E3"><a href="#" onclick="btnClick();">全选</a></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单类型</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">客户名称</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">当前站点</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">匹配站点</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">到站时间</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div style="height:100px"></div>
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
							<%-- <%for(CwbOrder cwb :cwblist){%> --%>
								<tr>
									<td  width="40" align="center" valign="middle">
										<input type="checkbox" checked="checked" name="checkbox" id="checkbox" value="<%-- <%=cwb.getOpscwbid()%> --%>"/>
									</td>
									<td width="100" align="center" valign="middle"><%-- <%=cwb.getCwb() %> --%></td>
									<td width="100" align="center" valign="middle"><%-- <%=mapcustomerlist.get(cwb.getCustomerid()).getCustomername() %> --%></td>
									<td width="100" align="center" valign="middle"><%-- <%=CwbOrderTypeIdEnum.getByValue(Integer.valueOf(cwb.getCwbordertypeid())).getText()%> --%></td>
									<td width="100" align="center" valign="middle"><%-- <%=CwbStateEnum.getByValue((int)cwb.getCwbstate()).getText()%> --%></td>
									<td width="100" align="center" valign="middle"><%-- <%=cwb.getConsigneename() %> --%></td>
									<td width="100" align="center" valign="middle"><%-- <%=cwb.getConsigneemobile() %> --%></td>
								</tr>
							<%-- <%} %> --%>
							</tbody>
						</table>
				</div>
				<div style="height:40px"></div>
				<%-- <div class="iframe_bottom" >
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
						<tbody>
							<tr height="38" >
								<td align="center" valign="middle" bgcolor="#FFFFFF"><input type="button" id="submitF" value="提交审核" onclick="sub()" class="input_button1"></td>
							</tr>
						</tbody>
					</table>
				</div><%} %> --%>
		</div>
	</div>
</div>
<form action="<%=request.getContextPath() %>/orderBackCheck/export" method="post" id="exportForm">
	<textarea style="display:none" name="cwb" id="cwb"><%=request.getParameter("cwb")==null?"":request.getParameter("cwb")%></textarea>
	<input type="hidden" value="<%=request.getParameter("searchType")==null?"":request.getParameter("searchType")%>" id="searchType" name="searchType"/>
</form>
</BODY>
</HTML>
