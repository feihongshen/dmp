<%@page import="cn.explink.domain.MissPieceView"%>
<%@page import="cn.explink.domain.MissPiece"%>
<%@page import="cn.explink.controller.AbnormalView"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.domain.AbnormalType"%>
<%@page import="cn.explink.domain.AbnormalOrder"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="cn.explink.dao.CwbDAO"%>

<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<MissPieceView> misspieces=(List<MissPieceView>)request.getAttribute("missPieces");
Object cwb=request.getAttribute("cwb")==null?"":request.getAttribute("cwb");
List<Branch> branchList = (List<Branch>)request.getAttribute("branchlist");
Page page_obj = (Page)request.getAttribute("page_obj");
 String starttime=request.getParameter("begindate")==null?DateTimeUtil.getDateBefore(1):request.getParameter("begindate");
 String endtime=request.getParameter("enddate")==null?DateTimeUtil.getNowTime():request.getParameter("enddate");
  long customerid = Long.parseLong(request.getAttribute("customerid").toString());
  List<Customer> customerlist=(List<Customer>)request.getAttribute("customers");
  long cwbordertype=request.getAttribute("cwbordertype")==null?0:Long.parseLong(request.getAttribute("cwbordertype")+"");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-form.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.inputer.js"></script>
<script src="${pageContext.request.contextPath}/js/inputer.js"></script>
<script language="javascript">
$(function(){
	var $menuli = $(".kfsh_tabbtn ul li");
	var $menulilink = $(".kfsh_tabbtn ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
	$("#cwbordertype").val();
	
})


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


function getThisBoxList(id,flag){
	var URL="";
	if(flag==0){
		URL=$("#handle"+id).val()+"&isfind=0";
	}else if(flag==1){
		URL=$("#handle"+id).val()+"&isfind=1";
	}
	$.ajax({
		type: "POST",
		url:URL,
		dataType:"html",
		success : function(data) {
			$("#alert_box",parent.document).html(data);
			
		},
		complete:function(){
			viewBox();
		}
	});
}

function Days(day1,day2){     
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
	if(days>31){
		return false;
	}        
	return true;
}	
		
function editSuccess(data){
	window.parent.closeBox();
	$("#searchForm").submit();
}		
function checkstate(){
	if($("#ishandle").val()==<%=AbnormalOrderHandleEnum.weichuli.getValue()%>){
		$("#chuangjianstrtime").show();
		$("#chuangjianendtime").show();
		$("#strtime").hide();
		$("#endtime").hide();
		$("#chuli").html("创建时间：");
	}else if($("#ishandle").val()==<%=AbnormalOrderHandleEnum.daichuli.getValue()%>){
		$("#chuangjianstrtime").hide();
		$("#chuangjianendtime").hide();
		$("#strtime").show();
		$("#endtime").show();
		$("#chuli").html("处理时间：");
	}else{
		$("#chuangjianstrtime").hide();
		$("#chuangjianendtime").hide();
		$("#strtime").show();
		$("#endtime").show();
		$("#chuli").html("处理时间：");
	}
}

function isgetallcheck(){
	if($('input[name="id"]:checked').size()>0){
		$('input[name="id"]').each(function(){
			$(this).attr("checked",false);
		});
	}else{
		$('input[name="id"]').attr("checked",true);
	}
}

function check(){
	if($("#customerid").val()==0&&$("#cwb").val()==""&&$("#losebackbranchid").val()==0&&$("#cwbordertype").val()==0&&$("#losebackbranchid").val()==0&&$("#strtime").val()==""&&$("#endtime").val()==""){
		alert("请至少选择一个条件进行查询！");
		return;
	}else if($("#endtime").val()!=""&&$("#strtime").val()==""){
		alert("请选择开始时间");
	}else if($("#endtime").val()==""&&$("#strtime").val()!=""){
		alert("请选择结束时间");
	}
	$("#searchForm").submit();
}
function cancelmisspiece(){
	var ids="";
	$('input[name="id"]:checked').each(function(){ //由于复选框一般选中的是多个,所以可以循环输出
		id=$(this).val();
		if($.trim(id).length!=0){
		ids+=id+",";
		}
		});
	if(ids.length==0){
		alert("请选择！");
		return false;
	}
	if(confirm("您确定要作废这些丢失订单吗？？")==true){
		if(ids.indexOf(",")>0){
			$.ajax({
				type : "POST",
				url:"<%=request.getContextPath()%>/abnormalOrder/cancelMisspiece",
				data:{"ids":ids.substring(0, ids.length-1)},
				dataType : "json",
				success : function(data) {/* $("#alert_box",parent.document).html(data); */
				alert(data.error);
				$("#searchForm").submit();
				}
			});
			}
	}
	
}
	
function getThisBoxListAdd(cwbinfo){
	$.ajax({
		type:'post',
		url:"<%=request.getContextPath()%>/abnormalOrder/losebackcwbout/"+cwbinfo,
		dataType:"html",
		success : function(data) {
			$("#alert_box",parent.document).html(data);
		},
		complete:function(){
			viewBox();
		}
	});
}
function resethence(){
	$("#customerid").val(0);
	$("#cwbordertype").val(0);
	$("#losebackbranchid").val(0);
	$("#strtime").val("");
	$("#endtime").val("");
	$("#cwb").val("");
}
</script>
</head>
<body style="background:#f5f5f5;overflow: hidden;" marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<div style="position:relative; z-index:0;" >
			<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
				<div class="kfsh_search">
							<form action="1" method="post" id="searchForm">
								<table width="100%"  style="font-size: 12px;">
								<tr>
								<td align="left" >
								订单号：
									<textarea id="cwb" class="kfsh_text" rows="2" name="cwb" ><%=cwb %></textarea>
								</td>
								
								<td align="left">
								
									<label for="select2"></label>
										客户名称 ： <select name ="customerid" id ="customerid"    class="select1">
									<option value="0">请选择</option>
		          						<%for(Customer c : customerlist){ %>
		      					     <option value ="<%=c.getCustomerid() %>"  <%if(c.getCustomerid()==customerid){%>  selected="selected"<%}%>><%=c.getCustomername() %></option>
		         						 <%} %>
		      						  </select>
									
								<br>
								<br>
									订单类 型 ：<select id="cwbordertype" name="cwbordertype" class="select1">
										<option value="0">请选择订单类型</option>
										<%for(CwbOrderTypeIdEnum cwbordertypehh:CwbOrderTypeIdEnum.values()){ %>
										<%if(cwbordertypehh.getValue()!=-1) {%>
										<option value="<%=cwbordertypehh.getValue() %>"><%=cwbordertypehh.getText() %></option>
										
										<%}else{ %>
										
										<%} %>
										<%} %>
									</select>
		      						</td>
									<td>
								找回机构：
									<label for="select2"></label>
									<select name="losebackbranchid" id="losebackbranchid" class="select1">
										<option value="0">请选择责任机构</option>
										<%if(branchList!=null||branchList.size()!=0){for(Branch b : branchList){ %>
											<option value="<%=b.getBranchid()%>"><%=b.getBranchname() %></option>
										<%}} %>
									</select>
									<br><br>
									<strong id="chuli">创建时间：</strong>
									<input type ="text" name ="begindate" id="strtime"  value="<%=request.getParameter("begindate")==null?"":request.getParameter("begindate") %>" class="input_text1" style="height:20px;"/>
									<strong id="chulidown">到</strong>
									<input type ="text" name ="enddate" id="endtime"  value="<%=request.getParameter("enddate")==null?"":request.getParameter("enddate") %>" class="input_text1" style="height:20px;"/>
									<input type="hidden" name="isshow" value="1"/>
								</td>
									</tr>
									<tr>
									<td align="left">
									<input type="button"  onclick="check()" value="查询" class="input_button2"/>
									<input type="button" onclick="resethence();"   value="重置" class="input_button2">
									<input type="button"  onclick="cancelmisspiece();"  value="作废" class="input_button2">
									</td>
									<td>
									<input type ="button" id="btnval" value="导出" class="input_button2" <%if(misspieces.size()==0){ %> disabled="disabled" <%} %> onclick="exportField();"/>
								</td>
								</tr>
								</table>
							</form>
				</div>
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
					<tbody>
						<tr class="font_1" height="30" >
							<td width="30"  align="center" valign="middle" bgcolor="#f3f3f3"><a style="cursor: pointer;" onclick="isgetallcheck();">全选</a></td>
							<td width="100" align="center" valign="middle" bgcolor="#f3f3f3">订单号</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">客户名称</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单类型</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单操作状态</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">找回机构</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">创建人</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">创建时间</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">问题件单号</td>
							<td width="160" align="center" valign="middle" bgcolor="#E7F4E3">操作</td>
						</tr>
					</tbody>
				</table>
				</div>
			<div style="height: 500px;overflow:auto; ">
		    <div style="height: 140px;"></div>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
				<tbody>
					<%if(misspieces!=null||misspieces.size()>0)for(MissPieceView misspiece: misspieces){ %>
					<tr height="30" >
						<td width="30" align="center" valign="middle" bgcolor="#eef6ff">
						<input id="id" type="checkbox" value="<%=misspiece.getId()%>"  name="id"/>
						</td>
						<td width="100" align="center" valign="middle"><a style="color: blue;" href="javascript:getThisBoxListAdd('<%=misspiece.getCwb() %>');"><%=misspiece.getCwb() %></a></td>
						<td width="100" align="center" valign="middle"><%=misspiece.getCustomername() %></td>
						<td width="100" align="center" valign="middle"><%=misspiece.getOrdertype() %></td>
						<td width="100" align="center" valign="middle"><%=misspiece.getFlowordertypeName() %></td>
						<td width="100" align="center" valign="middle"><%=misspiece.getCallbackbranchname() %></td>
						<td width="100" align="center" valign="middle"><%=misspiece.getCreusername() %></td>
						<td width="100" align="center" valign="middle"><%=misspiece.getCreatetime() %></td>
						<td width="100" align="center" valign="middle" title="<%=misspiece.getQuestionno()==null?"":misspiece.getQuestionno() %>"><%=misspiece.getQuestionno()==null?"":misspiece.getQuestionno().split(",").length>2?misspiece.getQuestionno().split(",")[0]+","+misspiece.getQuestionno().split(",")[1]+"...":misspiece.getQuestionno() %></td>
						<td width="160" align="center" valign="middle"><a href="javascript:void(0)">处理<a/></td>
					</tr>
					<%} %>
				</tbody>
			</table>
			<div style="height: 120px;"></div>
			</div>
			</div>
			<%if(page_obj.getMaxpage()>1){ %>
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
<form action="<%=request.getContextPath()%>/abnormalOrder/losebackExportExcle" method="post" id="searchForm2">
	<input type="hidden" name="cwbStrs" id="cwbStrs" value="<%=request.getParameter("cwb")==null?"":request.getParameter("cwb") %>"/>
	<input type="hidden" name="cwbordertype" id="cwbordertype" value="<%=request.getParameter("cwbordertype")==null?"":request.getParameter("cwbordertype") %>"/>
	<input type="hidden" name="losebackbranchid" id="losebackbranchid" value="<%=request.getParameter("losebackbranchid")==null?"0":request.getParameter("losebackbranchid")%>"/>
	<input type="hidden" name="begindate1" id="bedindate1" value="<%=request.getParameter("begindate")==null?"":request.getParameter("begindate")%>"/>
	<input type="hidden" name="enddate1" id="enddate1" value="<%=request.getParameter("enddate")==null?"":request.getParameter("enddate")%>"/>
	<input type="hidden" name="customerid" id="customerid" value="<%=request.getParameter("customerid")==null?"":request.getParameter("customerid")%>"/>
</form>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#cwbordertype").val(<%=request.getParameter("cwbordertype")==null?0:Long.parseLong(request.getParameter("cwbordertype"))%>);
$("#losebackbranchid").val(<%=request.getParameter("losebackbranchid")==null?0:Long.parseLong(request.getParameter("losebackbranchid"))%>);
$("#strtime").val('<%=request.getParameter("begindate")==null?"":request.getParameter("begindate")%>');
$("#endtime").val('<%=request.getParameter("enddate")==null?"":request.getParameter("enddate")%>');
$("#customerid").val('<%=request.getParameter("customerid")==null?"":request.getParameter("customerid")%>');
function exportField(){
	if(<%=misspieces != null && misspieces.size()>0 %>){
		$("#btnval").attr("disabled","disabled");
	 	$("#btnval").val("请稍后……");
	 	$("#searchForm2").submit();
	}else{
		alert("没有做查询操作，不能导出！");
	};
}

</script>
</body>
</html>

