<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.OrderBackCheck"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%
	List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
	List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
	List<CwbOrderView> covList = (List<CwbOrderView>)request.getAttribute("cwbApplyZhongZhuanlist");
	Page page_obj = (Page)request.getAttribute("page_obj");
	String cwbs=request.getParameter("cwbs")==null?"":request.getParameter("cwbs");
	int branchtype = Integer.parseInt(request.getAttribute("branchType") == null ?"0":request.getAttribute("branchType").toString());
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
<script src="<%=request.getContextPath()%>/js/easyui-extend/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.inputer.js"></script>
<script src="${pageContext.request.contextPath}/js/inputer.js"></script>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<script type="text/javascript">
//提交审核按钮
function subPass(){
	var ids="";//选中id
	var num=0;
	$('input[type="checkbox"][name="checkbox"]').each(
	       	function() {
	          	if($(this).attr("checked")=="checked"){
	          		if($("#ishandle"+$(this).val()).val()==3||$("#ishandle"+$(this).val()).val()==2){
	          			num++;
	          		}
	          		ids+=$(this).val()+",";
	          	}
	          }
	       );
	if(ids.length==0){
		alert("请勾选订单");
		return false;
	}
	if(num>0){
		alert("已经审核过的订单不能再次审核！！");
		return false;
	}
	if(ids.length>0){
		ids = ids.substring(0, ids.length-1);
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
    				$("#searchForm").submit();
    				<%-- location.href="<%=request.getContextPath()%>/cwbapply/zhongZhuantrueAuditByCwb"; --%>
    			}else{
    				alert(data.error);
    				$("#searchForm").submit();
    				<%-- location.href="<%=request.getContextPath()%>/cwbapply/zhongZhuantrueAuditByCwb"; --%>
    			}
    		}
    	});
	}
}

function subNopass(){
	var ids="";//选中id
	var num=0;
	$('input[type="checkbox"][name="checkbox"]').each(
	       	function() {
	          	if($(this).attr("checked")=="checked"){
	          		if($("#ishandle"+$(this).val()).val()==3||$("#ishandle"+$(this).val()).val()==2){
	          			num++;
	          		}
	          		ids+=$(this).val()+",";
	          	}
	          }
	       );
	if(ids.length==0){
		alert("请勾选订单");
		return false;
	}
	if(num>0){
		alert("已经审核过的订单不能再次审核！！");
		return false;
	}
	if(ids.length>0){
		ids = ids.substring(0, ids.length-1);
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
    				$("#searchForm").submit();
    				<%-- location.href="<%=request.getContextPath()%>/cwbapply/zhongZhuanrefuseAuditByCwb"; --%>
    			}else{
    				alert(data.error);
    				$("#searchForm").submit();
    		        <%-- location.href="<%=request.getContextPath()%>/cwbapply/zhongZhuanrefuseAuditByCwb"; --%>
    			}
    		}
    	});
	}
}

function check(){
	var len=$.trim($("#cwbs").val()).length;
 	if(len>0)
		{
 		 $("#searchForm").submit();
		return true;
		} 

 	if($("input[name='begindate']").val()==""){
		alert("请选择开始时间");
		return false;
	}
	if($("input[name='enddate']").val()==""){
		alert("请选择结束时间");
		return false;
	}
	if($("input[name='begindate']").val()>$("input[name='enddate']").val()){
		alert("开始时间不能大于结束时间");
		return false;
	}
	if(!Days()||($("input[name='begindate']").val()=='' &&$("input[name='enddate']").val()!='')||($("input[name='begindate']").val()!='' &&$("input[name='enddate']").val()=='')){
		alert("时间跨度不能大于30天！");
		return false;
	}

   $("#searchForm").submit();
	return true;
}
function Days(){     
	var day1 = $("input[name='begindate']").val();   
	var day2 = $("input[name='enddate']").val(); 
	var y1, y2, m1, m2, d1, d2;//year, month, day;   
	day1=new Date(Date.parse(day1.replace(/-/g,"/"))); 
	day2=new Date(Date.parse(day2.replace(/-/g,"/")));
	y1=day1.getFullYear();
	y2=day2.getFullYear();
	m1=parseInt(day1.getMonth())+1 ;
	m2=parseInt(day2.getMonth())+1;
	d1=day1.getDate();
	d2=day2.getDate();
	var date1 = new Date(y1, m1, d1);            
	var date2 = new Date(y2, m2, d2);   
	var minsec = Date.parse(date2) - Date.parse(date1);          
	var days = minsec / 1000 / 60 / 60 / 24;  
	if(days>30){
		return false;
	}        
	return true;
}

function reset(){
	 $("#cwbs").val('');
	 $("#cwbtypeid").val('0');
	 $("#customerid").val('0');
	 $("#branchid").val('0');
	 $("#ishandle").val('0');
	 $("#strtime").val('');
	 $("#endtime").val('');
	};	

function exportField(){
 	if(<%=covList!=null&&!covList.isEmpty()%>){
 		$("#btnval").attr("disabled","disabled"); 
	 	$("#btnval").val("请稍后……");
		$("#searchForm").attr("action",'<%=request.getContextPath()%>/cwbapply/zhongzhuanchuzhanshenhexport');	
		$("#searchForm").submit();
		$("#searchForm").attr("action","1");
	}else{
		alert("没有做查询操作，不能导出！");
	} 
}

function btnClick(){
	if($("[name='checkbox']").attr("checked")=="checked"||$("[name='checkbox']").attr("checked")=="true"){
		$("[name='checkbox']").removeAttr("checked");
		$("#selectbtn").text("全选");
	}else{
		$("[name='checkbox']").attr("checked","checked");
		$("#selectbtn").text("取消");
	}
}


function resetData(){
	$("#cwbs").val("");
	$("#strtime").val("");
	$("#endtime").val("");
	$("#cwbtypeid").val(0);
	$("#customerid").val(0);
	$("#cwbtypeid").val(0);
	$("#branchid").val(0);
	$("#ishandle").val(0);
}
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
											<textarea name="cwbs" rows="3" class="kfsh_text" id="cwbs"  ><%=cwbs %></textarea>
											
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
											<select name ="customerid" id ="customerid" style="width:150px">
												<option  value ="0">全部</option>
												<%if(customerList!=null){
													for(Customer cus:customerList){ %>
													<option value ="<%=cus.getCustomerid()%>"><%=cus.getCustomername()%></option>
												<%} }%>
											</select>
											&nbsp;&nbsp;
											配送站点:
												<input type="text" id="branchid" name="branchid" class="easyui-validatebox" style="width: 110px;"initDataType="TABLE"
												initDataKey="Branch" 
												filterField="sitetype" 
												filterVal="2"
												viewField="branchname" saveField="branchid"/>
											<%-- <select name ="branchid" id ="branchid">
												<option  value ="0">全部</option>
												<%if(branchList!=null){ 
													for(Branch br:branchList){ %>
													<option value ="<%=br.getBranchid()%>"><%=br.getBranchname()%></option>
													<%} }%>
											</select>
 --%>										</td>
									</tr>
									<br>
									<tr align="left">
										<td align="right">
											&nbsp;&nbsp;
											审核状态:
											<select name ="ishandle" id ="ishandle">
												<option  value ="0">待审核</option>
												<option value ="<%=ShenHeStateEnum.shenhebutongguo.getValue() %>"><%=ShenHeStateEnum.shenhebutongguo.getText()%></option>
												<option value ="<%=ShenHeStateEnum.shenhetongguo.getValue() %>"><%=ShenHeStateEnum.shenhetongguo.getText()%></option>
											</select>
										</td>
										<td align="left">
											&nbsp;&nbsp;
											归班反馈时间:
												<input  type ="text" name="enddate"  class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:160,prompt: '结束时间'"/>
												<input  type ="text" name="begindate"  class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:160,prompt: '起始时间'"/>
										</td>
									</tr>
								</table>
								<table>
									<tr>
										<td width="20%">
											<input type="button" onclick="check();" id="serch" value="查询" class="input_button2">&nbsp;&nbsp;
											<input type="button" onclick="resetData();" value="重置" class="input_button2">&nbsp;&nbsp;
										</td>
										<td width="20%">
										<%if(branchtype != 2){ %>
											<input type="button" onclick="subPass()" value="审核通过" class="input_button2">&nbsp;&nbsp;
											<input type="button" onclick="subNopass()" value="审核不通过" class="input_button2">&nbsp;&nbsp;
											<%} %>
											<input name="btnval" type="button" id="btnval" value="导出" class="input_button2" onclick="exportField();"/>
										</td>
									</tr>
								</table>
							</form>
						</div>

						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="40" align="center" valign="middle" bgcolor="#E7F4E3"><a href="#" onclick="btnClick();" id="selectbtn">取消</a></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单类型</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">客户名称</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">当前站点</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">配送站点</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">到站时间</td>
									<td width="80" align="center" valign="middle" bgcolor="#E7F4E3">审核人</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">审核时间</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">审核状态</td>
								</tr>
							</tbody>
						</table>
					</div>
					<br>
					<br>
					<br>
					<div style="height:104px"></div> 
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
							<% if(covList!=null){
							for(CwbOrderView cwb :covList){ %>
								<tr>
									<td  width="40" align="center" valign="middle">
										<input type="checkbox" name="checkbox" id="checkbox" checked="checked" value="<%=cwb.getOpscwbid()%>"/>
									</td>
									<td width="100" align="center" valign="middle"><%=cwb.getCwb()%></td>
									<td width="100" align="center" valign="middle"><%=cwb.getCwbordertypename()%></td>
									<td width="100" align="center" valign="middle"><%=cwb.getCustomername()%></td>
									<td width="100" align="center" valign="middle"><%=cwb.getBranchname() %></td>
									<td width="100" align="center" valign="middle"><%=cwb.getMatchbranchname() %></td>
									<td width="100" align="center" valign="middle"><%=cwb.getInSitetime() %></td>
									<td width="80" align="center" valign="middle"><%=cwb.getAuditor() %></td>
									<td width="100" align="center" valign="middle"><%=cwb.getAudittime() %></td>
									<td width="100" align="center" valign="middle"><%=cwb.getAuditstatename() %></td>
								</tr>
								<input type="hidden" id="ishandle<%=cwb.getOpscwbid()%>" value="<%=cwb.getAuditstate()%>"/>
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
<script type="text/javascript">
	$("#selectPg").val(<%=request.getAttribute("page")%>);
	$("#cwbtypeid").val(<%=request.getParameter("cwbtypeid")==null?0:Integer.parseInt(request.getParameter("cwbtypeid"))%>);
	$("#customerid").val(<%=request.getParameter("customerid")==null?0:Long.parseLong(request.getParameter("customerid"))%>);
	$("#branchid").val(<%=request.getParameter("branchid")==null?0:Long.parseLong(request.getParameter("branchid"))%>);
	$("#ishandle").val(<%=request.getParameter("ishandle")==null?0:Long.parseLong(request.getParameter("ishandle"))%>);
	$("#strtime").val("<%=request.getParameter("begindate")==null?"":request.getParameter("begindate")%>");
	$("#endtime").val("<%=request.getParameter("enddate")==null?"":request.getParameter("enddate")%>");
</script>

</BODY>
</HTML>
