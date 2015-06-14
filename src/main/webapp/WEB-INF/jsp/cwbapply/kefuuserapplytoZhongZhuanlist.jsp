<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.OrderBackCheck"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%
	String hiddenCwb = request.getAttribute("cwb")==null?"":request.getAttribute("cwb").toString();
	List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
	List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
	List<CwbApplyZhongZhuan> cwbApplyZhongZhuanlist = (List<CwbApplyZhongZhuan>)request.getAttribute("cwbApplyZhongZhuanlist");
	Map<Long,String> customerMap = (Map<Long,String>)request.getAttribute("customerMap");
	Page page_obj = (Page)request.getAttribute("page_obj");
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
function subPass(){
	var ids="";//选中id
	$('input[type="checkbox"][name="checkbox"]').each(
	       	function() {
	          	if($(this).attr("checked")=="checked"){
	          		ids+=$(this).val()+",";
	          	}
	          }
	       );
	if(ids.length==0){
		alert("请勾选订单");
		return false;
	}
	
	if(confirm("确认选择审核的结果吗？")){
    	$.ajax({
    		type: "POST",
    		url:'<%=request.getContextPath()%>/cwbapply/zhongZhuantrueAuditByCwb',
    		data:{ids:ids},
    		dataType : "json",
    		success : function(data) {
    			if(data.errorCode==0){
    				alert(data.error);
    				location.href="<%=request.getContextPath()%>/cwbapply/zhongZhuantrueAuditByCwb";
    			}else{
    				alert(data.error);
    				location.href="<%=request.getContextPath()%>/cwbapply/zhongZhuantrueAuditByCwb";
    			}
    		}
    	});
	}
}

function subNopass(){
	var ids="";//选中id
	$('input[type="checkbox"][name="checkbox"]').each(
	       	function() {
	          	if($(this).attr("checked")=="checked"){
	          		ids+=$(this).val()+",";
	          	}
	          }
	       );
	if(ids.length==0){
		alert("请勾选订单");
		return false;
	}
	
	if(confirm("确认选择审核的结果吗？")){
    	$.ajax({
    		type: "POST",
    		url:'<%=request.getContextPath()%>/cwbapply/zhongZhuanrefuseAuditByCwb',
    		data:{ids:ids},
    		dataType : "json",
    		success : function(data) {
    			if(data.errorCode==0){
    				alert(data.error);
    				location.href="<%=request.getContextPath()%>/cwbapply/zhongZhuanrefuseAuditByCwb";
    			}else{
    				alert(data.error);
    				location.href="<%=request.getContextPath()%>/cwbapply/zhongZhuanrefuseAuditByCwb";
    			}
    		}
    	});
	}
}

function exportField(){
	if(<%=cwbApplyZhongZhuanlist!=null&&!cwbApplyZhongZhuanlist.isEmpty()%>){
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
		<div class="kfsh_tabbtn"></div>
		<div class="tabbox">
				<div style="position:relative; z-index:0 " >
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							<form action="1" method="POST" id="searchForm">
								<table >
									<tr>
										<td rowspan="2">
											订单号：
											<textarea name="cwbs" rows="3" class="kfsh_text" id="cwbs" onFocus="if(this.value=='查询多个订单用回车隔开'){this.value=''}" onBlur="if(this.value==''){this.value='查询多个订单用回车隔开'}" >查询多个订单用回车隔开</textarea>
											<input name="hiddencwb" type="hidden" id="hiddencwb" value="<%=hiddenCwb %>" />
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
												<%if(customerList!=null){
													for(Customer cus:customerList){ %>
													<option value ="<%=cus.getCustomerid()%>"><%=cus.getCustomername()%></option>
												<%} }%>
											</select>
											&nbsp;&nbsp;
											配送站点:
											<select name ="branchid" id ="branchid">
												<option  value ="0">全部</option>
												<%if(branchList!=null){ 
													for(Branch br:branchList){ %>
													<option value ="<%=br.getBranchid()%>"><%=br.getBranchname()%></option>
													<%} }%>
											</select>
										</td>
									</tr>
									<br>
									<tr>
										<td>
											&nbsp;&nbsp;
											审核状态:
											<select name ="shenhestate" id ="shenhestate">
												<option  value ="0">全部</option>
												<option value ="<%=ApplyStateEnum.daishenhe.getValue()%>"><%=ApplyStateEnum.daishenhe.getText() %></option>
												<option value ="<%=ApplyStateEnum.yishenhe.getValue() %>"><%=ApplyStateEnum.yishenhe.getText()%></option>
											</select>
										</td>
										<td>
											&nbsp;&nbsp;
											归班反馈时间:
												<input type ="text" name ="begindate" id="strtime"  value="<%=request.getParameter("begindate")==null?"":request.getParameter("begindate") %>" class="input_text1" style="height:20px;"/>
											到
												<input type ="text" name ="enddate" id="endtime"  value="<%=request.getParameter("enddate")==null?"":request.getParameter("enddate") %>" class="input_text1" style="height:20px;"/>
										</td>
									</tr>
								</table>
								<table>
									<tr>
										<td>
											<input type="submit" value="查询" class="input_button2">&nbsp;&nbsp;
											<input type="button" onclick="" value="重置" class="input_button2">&nbsp;&nbsp;
										</td>
										<td>
											<input type="hidden" value="<%=request.getParameter("searchType")==null?"":request.getParameter("searchType")%>" id="searchType" name="searchType"> 
											<input type="button" onclick="subPass()" value="审核通过" class="input_button2">&nbsp;&nbsp;
											<input type="button" onclick="subNopass()" value="审核不通过" class="input_button2">&nbsp;&nbsp;
										</td>
									</tr>
								</table>
								<%if(cwbApplyZhongZhuanlist!=null&&!cwbApplyZhongZhuanlist.isEmpty()){%>
								<span>
									<input name="btnval" type="button" id="btnval" value="导出" class="input_button2" onclick="exportField();"/>
								</span> 
								<%} %>
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
					<br>
					<br>
					<br>
					<br>
					<div style="height:108px"></div> 
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
							<% if(cwbApplyZhongZhuanlist!=null){
							for(CwbApplyZhongZhuan cwb :cwbApplyZhongZhuanlist){ %>
								<tr>
									<td  width="40" align="center" valign="middle">
										<input type="checkbox" name="checkbox" id="checkbox" value="<%=cwb.getId()%>"/>
									</td>
									<td width="100" align="center" valign="middle"><%=cwb.getCwb()%></td>
									<td width="100" align="center" valign="middle"><%=CwbOrderTypeIdEnum.getTextByValue(Integer.parseInt(String.valueOf(cwb.getCwbordertypeid())))%></td>
									<td width="100" align="center" valign="middle"><%=customerMap.get(cwb.getCustomerid())%></td>
									<td width="100" align="center" valign="middle"><%=cwb.getApplyzhongzhuanbranchid() %></td>
									<td width="100" align="center" valign="middle"></td>
									<td width="100" align="center" valign="middle"></td>
								</tr>
							<%} }%> 
							</tbody>
						</table>
				</div>
				<%if(page_obj!=null&&page_obj.getMaxpage()>1){ %>
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
<form action="<%=request.getContextPath() %>/orderBackCheck/export" method="post" id="exportForm">
	<textarea style="display:none" name="cwb" id="cwb"><%=request.getParameter("cwb")==null?"":request.getParameter("cwb")%></textarea>
	<input type="hidden" value="<%=request.getParameter("searchType")==null?"":request.getParameter("searchType")%>" id="searchType" name="searchType"/>
</form>
<script type="text/javascript">
	$("#selectPg").val(<%=request.getAttribute("page")%>);
	$("#cwbtypeid").val(<%=request.getParameter("cwbtypeid")==null?0:Integer.parseInt(request.getParameter("cwbtypeid"))%>);
	$("#customerid").val(<%=request.getParameter("customerid")==null?0:Long.parseLong(request.getParameter("customerid"))%>);
	$("#branchid").val(<%=request.getParameter("branchid")==null?0:Long.parseLong(request.getParameter("branchid"))%>);
	$("#shenhestate").val(<%=request.getParameter("shenhestate")==null?0:Long.parseLong(request.getParameter("shenhestate"))%>);
	$("#strtime").val("<%=request.getParameter("begindate")==null?"":request.getParameter("begindate")%>");
	$("#endtime").val("<%=request.getParameter("enddate")==null?"":request.getParameter("enddate")%>");
</script>

</BODY>
</HTML>
