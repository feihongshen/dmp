<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.controller.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
Page page_obj = (Page)request.getAttribute("page_obj");
List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
List<CwbOrderView> cwbList = (List<CwbOrderView>)request.getAttribute("cwbList");
List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
String cwbs = request.getParameter("cwb")==null?"":request.getParameter("cwb");
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
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript">

$(function(){
	
	$("table#gd_table tr:odd").css("backgroundColor","#f9fcfd");
	$("table#gd_table tr:odd").hover(function(){
		$(this).css("backgroundColor","#fff9ed");									  
	},function(){
		$(this).css("backgroundColor","#f9fcfd");	
	});
	$("table#gd_table tr:even").hover(function(){
		$(this).css("backgroundColor","#fff9ed");									  
	},function(){
		$(this).css("backgroundColor","#fff");	
	});//表单颜色交替和鼠标滑过高亮
	
	$("table#gd_table2 tr").click(function(){
			$(this).css("backgroundColor","#FFA500");
			$(this).siblings().css("backgroundColor","#ffffff");
			$("tr[cwbstate='no']").css("backgroundColor","#aaaaaa");
		});
	
	$(".index_dh li").mouseover(function(){
		//$(this).show(0)
		var $child = $(this).children(0);
		$child.show();
	});
	$(".index_dh li").mouseout(function(){
 
		$(".menu_box").hide();
	});
	
	$("tr[cwbstate='no']").css("backgroundColor","#aaaaaa");
});

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

function check(){
	var len=$.trim($("#cwb").val()).length;
 	if(len>0)
		{
 		 $("#searchForm").submit();
		return true;
		} 

	if($("#strtime").val()==""){
		alert("请选择开始时间");
		return false;
	}
	if($("#endtime").val()==""){
		alert("请选择结束时间");
		return false;
	}
	if($("#strtime").val()>$("#endtime").val()){
		alert("开始时间不能大于结束时间");
		return false;
	}
	if(!Days()||($("#strtime").val()=='' &&$("#endtime").val()!='')||($("#strtime").val()!='' &&$("#endtime").val()=='')){
		alert("时间跨度不能大于30天！");
		return false;
	}

   $("#searchForm").submit();
	return true;
}
function Days(){     
	var day1 = $("#strtime").val();   
	var day2 = $("#endtime").val(); 
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
//退客户成功
function sub(){
	var datavalue = "[";
	
	if($('input[name="ischeck"]:checked').size()>0){
		$('input[name="ischeck"]:checked').each(function(index){
			$(this).attr("checked",false);
			datavalue = datavalue +"\""+$(this).val()+"\",";
		});
	}
	if(datavalue.length>1){
		datavalue= datavalue.substring(0, datavalue.length-1);
	}
	datavalue= datavalue + "]";
	if(datavalue.length>2){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/cwborder/auditTuiGongHuoShangSuccess",
		data:{cwbs:datavalue},
		dataType:"html",
		success : function(data) {
			alert("成功修改状态："+data.split("_s_")[0]+"单\n订单状态无变动："+data.split("_s_")[1]+"单");
			searchForm.submit();
		}
	});
	}
	
}

//退客户成功
function sub2(){
	var datavalue = "[";
	
	if($('input[name="ischeck"]:checked').size()>0){
		$('input[name="ischeck"]:checked').each(function(index){
			$(this).attr("checked",false);
			datavalue = datavalue +"\""+$(this).val()+"\",";
		});
	}
	if(datavalue.length>1){
		datavalue= datavalue.substring(0, datavalue.length-1);
	}
	datavalue= datavalue + "]";
	if(datavalue.length>2){
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/cwborder/auditTuiGongHuoShangFailure",
			data:{cwbs:datavalue},
			dataType:"html",
			success : function(data) {
				alert("成功修改状态："+data.split("_s_")[0]+"单\n订单状态无变动："+data.split("_s_")[1]+"单");
				searchForm.submit();
			}
		});
	}
	
}

function btnClick(){
	if($("[name='ischeck']").attr("checked")=="checked"||$("[name='ischeck']").attr("checked")=="true"){
		$("[name='ischeck']").removeAttr("checked");
		$("#selectbtn").text("全选");
	}else{
		$("[name='ischeck']").attr("checked","checked");
		$("#selectbtn").text("反选");
	}
}


function exportField(){
 	if(<%=cwbList!=null&&cwbList.size()!=0%>){
 		$("#btnval").attr("disabled","disabled"); 
	 	$("#btnval").val("请稍后……");
		$("#searchForm").attr("action",'<%=request.getContextPath()%>/cwborder/toGonghuoshangExport');	
		$("#searchForm").submit();
		$("#searchForm").attr("action","1");
 	}else{
		alert("没有做查询操作，不能导出！");
	} 
}
function resetData(){
	$("#cwb").val("");
	$("#cwbtypeid").val(0);
	$("#customerid").val(0);
	$("#shenhestate").val(0);
	$("#strtime").val("");
	$("#endtime").val("");
}
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
							<form action="1" method="post" id="searchForm">
								
								<table>
									<tr>
										<td rowspan="2">
											订单号：
											<textarea name="cwb" rows="3" class="kfsh_text" id="cwb"><%=cwbs %></textarea>
											<input type="hidden" id="isnow" name="isnow" value="1" />
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
													<%} %>
												<%} %>
											</select>
										</td>
									</tr>
									<tr>
										<td>
											&nbsp;&nbsp;
											确认状态:
											<select name ="shenhestate" id ="shenhestate">
												<option value ="0">待确认</option>
												<option value = "1">退客户成功</option>
												<option value ="2">拒收退货</option>
											</select>
										</td>
										<td>
											&nbsp;&nbsp;
											退客户出库时间:
											<input type ="text" name ="begindate" id="strtime"  value=""/>
											到
											<input type ="text" name ="enddate" id="endtime"  value=""/>
										</td>
										<td>
										</td>
									</tr>
								</table>
								<table>
									<tr>
										<td width="20%">
											<input type="button" onclick="check();"  value="查询" class="input_button2">&nbsp;&nbsp;
											<input type="button"  onclick="resetData();" value="重置" class="input_button2">&nbsp;&nbsp;
										</td>
										<td width="20%">
											<input type="button" onclick="sub()" value="退客户成功" class="input_button2">&nbsp;&nbsp;
											<input type="button" onclick="sub2()" value="拒收退货" class="input_button2">&nbsp;&nbsp;
											<%-- <%if(cwbList!=null&&!cwbList.isEmpty()){ %> --%>
												<input name="" type="button" id="btnval" value="导出" class="input_button2" onclick="exportField();"/>
											<%-- <%} %> --%>
										</td>
									</tr>
									
								</table>
							</form>
							<form action="<%=request.getContextPath()%>/cwborder/exportExcle" method="post" id="searchForm2">
								<input type="hidden" name="exportmould2" id="exportmould2" />
							</form>
						</div>
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="40" align="center" valign="middle" bgcolor="#E7F4E3"><a href="#" onclick="btnClick();" id="selectbtn">反选</a></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单类型</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">客户名称</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单金额</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">发货时间</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">退客户出库时间</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">确认状态</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">确认人</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">确认时间</td>
								</tr>
								
						<%if(cwbList!=null){ %>
							<%for(CwbOrderView cwb :cwbList){ %>
							<tr class="font_1" height="30" >
								<td width="40" align="center" valign="middle" >
									<input type="checkbox" name="ischeck" id="ischeck" checked="checked" value="<%=cwb.getCwb()%>"/>
								</td>
								<td width="100" align="center" valign="middle"><%=cwb.getCwb() %></td>
								<td width="100" align="center" valign="middle"><%=cwb.getCwbordertypename() %></td>
								<td width="100" align="center" valign="middle"><%=cwb.getCustomername()%></td>
								<td width="100" align="right" valign="middle"><strong><%=cwb.getReceivablefee() %></strong></td>
								<td width="100" align="center" valign="middle"><%=cwb.getEmaildate() %></td>
								<td width="100" align="center" valign="middle"><%=cwb.getCreatetime() %></td>
								<td width="100" align="center" valign="middle"><%=cwb.getAuditstatename() %></td>
								<td width="100" align="center" valign="middle" ><%=cwb.getAuditor() %></td>
								<td width="100" align="center" valign="middle"><%=cwb.getAudittime() %></td>
							</tr>
						<%} }%>
						
							</tbody>
						</table> 
					</div>
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
	$("#shenhestate").val(<%=request.getParameter("shenhestate")==null?0:Long.parseLong(request.getParameter("shenhestate"))%>);
	$("#strtime").val("<%=request.getParameter("begindate")==null?"":request.getParameter("begindate")%>");
	$("#endtime").val("<%=request.getParameter("enddate")==null?"":request.getParameter("enddate")%>");
</script>
</BODY>
</HTML>
