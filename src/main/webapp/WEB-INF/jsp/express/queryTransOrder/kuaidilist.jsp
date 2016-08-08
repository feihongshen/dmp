<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Common"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbKuaiDiView> cwbViewList = (List<CwbKuaiDiView>)request.getAttribute("cwbViewList");
List<User> lanshouuserlist = (List<User>)request.getAttribute("lanshouuserlist");
List<User> paisonguserlist = (List<User>)request.getAttribute("paisonguserlist");
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
List lanshoubranchidList =(List) request.getAttribute("lanshoubranchidList");
List paisongbranchidList =(List) request.getAttribute("paisongbranchidList");
  
String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");
Page page_obj = (Page)request.getAttribute("page_obj");
  
long lanshouuserid=request.getParameter("lanshouuserid")==null?0:Long.parseLong(request.getParameter("lanshouuserid"));
long paisonguserid=request.getParameter("paisonguserid")==null?0:Long.parseLong(request.getParameter("paisonguserid"));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>揽收订单查询</title>
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

<link href="<%=request.getContextPath()%>/css/multiple-select.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiple.select.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
	   //获取下拉框的值
	   $("#find").click(function(){
	         var checkval="";
	         $("label").each(function(index){
			     //找到被选中的项
			     if($(this).attr("class")=="checked"){
			        checkval+=$(this).children().attr("value")+",";
			     }
	         });
	         $("#controlStr").val(checkval);
	         if(check()){
	            $("#isshow").val(1);
		    	$("#searchForm").submit();
		    	$("#find").attr("disabled","disabled");
				$("#find").val("请稍等..");
	         }
	   });
	});

function check(){
	if($("#strtime").val()==""){
		alert("请选择开始时间");
		return false;
	}
	if($("#endtime").val()==""){
		alert("请选择结束时间");
		return false;
	}
	if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() !=''){
		alert("开始时间不能大于结束时间");
		return false;
	}
	else{
		return true;
	}
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
//	$("#lanshoubranchid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择' });
//	$("#paisongbranchid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择' });
    $("#lanshoubranchid").multipleSelect({
        placeholder: "请选择",
        filter: true
    });
    $("#paisongbranchid").multipleSelect({
        placeholder: "请选择",
        filter: true
    });
});
// function searchFrom(){
// 	$('#searchForm').attr('action',1);return true;
// }

var m=n=<%=request.getAttribute("check")==null?0:Integer.parseInt(request.getAttribute("check").toString())%>;
function lanshouchange(){
	m=0;
}
function paisongchange(){
	n=0;
}
function changeBranchDeliver(branchname,delivername,deliverid){
	if((branchname=='lanshoubranchid'&&m==0)||(branchname=='paisongbranchid'&&n==0)){
		var checkval="";
		$("label[class=checked]>input[name="+branchname+"]").each(function(index){
		     //找到被选中的项
		        checkval+=$(this).val()+",";
	       });
			$.ajax({
				url:"<%=request.getContextPath()%>/datastatistics/updateDeliverByBranchids",//后台处理程序
				type:"POST",//数据发送方式 
				data:"branchid="+checkval,//参数
				dataType:'json',//接受数据格式
				success:function(json){
					$("#"+delivername).empty();//清空下拉框//$("#select").html('');
					$("<option value='-1'>请选择</option>").appendTo("#"+delivername);//添加下拉框的option
					for(var j = 0 ; j < json.length ; j++){
						if(json[j].userid == deliverid){
							if(json[j].employeestatus==<%=UserEmployeestatusEnum.LiZhi.getValue()%>){
								$("<option value='"+json[j].userid+"' selected='selected'  >"+json[j].realname+"(离职)</option>").appendTo("#"+delivername);
							}else{
								$("<option value='"+json[j].userid+"' selected='selected'  >"+json[j].realname+"</option>").appendTo("#"+delivername);
							}
						}else{
							if(json[j].employeestatus==<%=UserEmployeestatusEnum.LiZhi.getValue()%>){
								$("<option value='"+json[j].userid+"'>"+json[j].realname+"(离职)</option>").appendTo("#"+delivername);
							}else{
								$("<option value='"+json[j].userid+"'>"+json[j].realname+"</option>").appendTo("#"+delivername);
							}
						}
					}
				}		
			});
			
			if(branchname=='lanshoubranchid'){
				m++;
			}
			if(branchname=='paisongbranchid'&&n==0){
				n++;
			}
	}
}
</script>
</head>

<body style="background:#f5f5f5" >
<div class="right_box">
	<div class="inputselect_box">
	<form action="<%=request.getContextPath()%>/expressQueryController/kuaidilist/1" method="post" id="searchForm">
	<input type="hidden" id="orderByNameId" name="orderbyName" value="emaildate"/>
	<input type="hidden" id="orderByTypeId" name="orderbyType" value="<%=request.getParameter("orderbyType")==null?"DESC":request.getParameter("orderbyType") %>"/>
	<input type="hidden" id="isshow" name="isshow" value="<%=request.getParameter("isshow")==null?"0":request.getParameter("isshow") %>" />
	<input type="hidden" name="page" value="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:40px">
	<tr>
		<td align="left">
			<select id="timeType" name="timeType">
			<option value="1" <%=request.getParameter("timeType")!=null&&request.getParameter("timeType").equals("1")?"selected":"" %>>揽收时间</option>
			<option value="2" <%=request.getParameter("timeType")!=null&&request.getParameter("timeType").equals("2")?"selected":"" %>>配送时间</option>
			</select>
				<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
			到
				<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
			揽收站点：
				<label onclick="lanshouchange();">
				<select name ="lanshoubranchid" id ="lanshoubranchid" multiple="multiple" style="width: 300px;">
		          <%for(Branch b : branchList){ %>
		           <option value ="<%=b.getBranchid() %>" 
		            <%if(!lanshoubranchidList.isEmpty()) 
			            {for(int i=0;i<lanshoubranchidList.size();i++){
			            	if(b.getBranchid()== new Long(lanshoubranchidList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>><%=b.getBranchname() %></option>
		          <%} %>
		        </select>
		        </label>
		        <%--[<a href="javascript:multiSelectAll('lanshoubranchid',1,'请选择');">全选</a>]--%>
				<%--[<a href="javascript:multiSelectAll('lanshoubranchid',0,'请选择');">取消全选</a>]--%>
			揽收人:
			<select name="lanshouuserid" id ="lanshouuserid" onclick="changeBranchDeliver('lanshoubranchid','lanshouuserid',<%=lanshouuserid%>);">
		          <option value ="-1">请选择</option>
		          <%if(lanshouuserlist!=null&&lanshouuserlist.size()>0)for(User u : lanshouuserlist){ %>
		          	<option value="<%=u.getUserid() %>" <%if(u.getUserid()==(request.getParameter("lanshouuserid")==null?-1:Long.parseLong(request.getParameter("lanshouuserid").toString()))){ %>selected="selected"<%} %>><%=u.getRealname() %></option>
		          <%} %>
		          
			 </select>
		        <br/>
		       <%--  站点名称：
		        <select name="ismohu" id="ismohu" >
					<option value ="1"<%if(1==(request.getParameter("ismohu")==null?1:Long.parseLong(request.getParameter("ismohu")))){%>selected="selected"<%}%>>模糊匹配</option>
					<option value ="2"<%if(2==(request.getParameter("ismohu")==null?1:Long.parseLong(request.getParameter("ismohu")))){%>selected="selected"<%}%>>精确匹配</option>
			 	</select>
		        <input name="branchname" id="branchname" onKeyDown="if(event.keyCode==13&&$(this).val().length>0){moHuOrJingQueSlect($('#ismohu').val(),'<%=request.getContextPath()%>','dispatchbranchid',$(this).val());change();}"/> --%>
			配送站点：
			<label onclick="paisongchange();">
			<select name ="paisongbranchid" id ="paisongbranchid"  multiple="multiple" style="width: 320px;">
		          <%for(Branch b : branchList){ %>
		          <option value ="<%=b.getBranchid() %>"<%if(!paisongbranchidList.isEmpty()) 
			            {for(int i=0;i<paisongbranchidList.size();i++){
			            	if(b.getBranchid()== new Long(paisongbranchidList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>><%=b.getBranchname()%></option>
		          <%}%>
			 </select>
			 </label>
			<%--[<a href="javascript:multiSelectAll('paisongbranchid',1,'请选择');">全选</a>]--%>
			<%--[<a href="javascript:multiSelectAll('paisongbranchid',0,'请选择');">取消全选</a>]--%>
			 派件人:
			<select name ="paisonguserid" id ="paisonguserid" onclick="changeBranchDeliver('paisongbranchid','paisonguserid',<%=paisonguserid %>);">
		          <option value ="-1">请选择</option>
		          <%if(paisonguserlist!=null&&paisonguserlist.size()>0)for(User u : paisonguserlist){ %>
		          	<option value="<%=u.getUserid() %>" <%if(u.getUserid()==(request.getParameter("paisonguserid")==null?-1:Long.parseLong(request.getParameter("paisonguserid").toString()))){ %>selected="selected"<%} %>><%=u.getRealname() %></option>
		          <%} %>
		          
			 </select>
			<input type="button" id="find" onclick="" value="查询" class="input_button2" />
			&nbsp;&nbsp;<input type ="button" id="btnval0" value="导出" class="input_button1" onclick="exportField('0','0');"/>
		</td>
	</tr>
</table>
	</form>
	<form action="<%=request.getContextPath()%>/expressQueryController/exportExcle" method="post" id="searchForm2">
		<input type="hidden" name="begindate1" id="begindate1" value="<%=starttime%>"/>
		<input type="hidden" name="enddate1" id="enddate1" value="<%=endtime%>"/>
		<input type="hidden" name="timeType1" id="timeType1" value="<%=request.getParameter("timeType")==null?"1":request.getParameter("timeType")%>"/>
		<input type="hidden" name="lanshouuserid1" id="lanshouuserid1" value="<%=request.getParameter("lanshouuserid")==null?"-1":request.getParameter("lanshouuserid")%>"/>
		<input type="hidden" name="paisonguserid1" id="paisonguserid1" value="<%=request.getParameter("paisonguserid")==null?"-1":request.getParameter("paisonguserid")%>"/>
		<div style="display: none;">
			<select name ="lanshoubranchid1" id ="lanshoubranchid1" multiple="multiple" style="width: 300px;">
		          <%for(Branch b : branchList){ %>
		           <option value ="<%=b.getBranchid() %>" 
		            <%if(!lanshoubranchidList.isEmpty()) 
			            {for(int i=0;i<lanshoubranchidList.size();i++){
			            	if(b.getBranchid()== new Long(lanshoubranchidList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>><%=b.getBranchname() %></option>
		          <%} %>
		        </select>
			<select name ="paisongbranchid1" id ="paisongbranchid1"  multiple="multiple" style="width: 320px;">
		          <%for(Branch b : branchList){ %>
		          <option value ="<%=b.getBranchid() %>"<%if(!paisongbranchidList.isEmpty()) 
			            {for(int i=0;i<paisongbranchidList.size();i++){
			            	if(b.getBranchid()== new Long(paisongbranchidList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>><%=b.getBranchname()%></option>
		          <%}%>
			 </select>
		</div>
	</form>
	</div>
	<div class="right_title">
	<div style="height:60px"></div><%if(cwbViewList != null && cwbViewList.size()>0){  %>
	<div style="overflow-x:scroll; width:100% " id="scroll">
		<table width="1500" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		   <tr class="font_1">
				<td align="center" bgcolor="#F4F4F4">快递单号</td>
				<td align="center" bgcolor="#F4F4F4">揽收人</td>
				<td bgcolor="#F4F4F4">揽收站点</td>
				<td bgcolor="#F4F4F4">配送站点</td>
				<td bgcolor="#F4F4F4">揽收时间</td>
				<td bgcolor="#F4F4F4">收件人</td>
				<td bgcolor="#F4F4F4">收件人手机号</td>
				<td bgcolor="#F4F4F4">收件人地址</td>
				<td bgcolor="#F4F4F4">费用总计</td>
				<td bgcolor="#F4F4F4">当前状态</td>
				<td bgcolor="#F4F4F4">备注</td>
				<td bgcolor="#F4F4F4">应收运费</td>
				<td bgcolor="#F4F4F4">包装费</td>
				<td bgcolor="#F4F4F4">保价费</td>
				<td bgcolor="#F4F4F4">重量</td>
				<td bgcolor="#F4F4F4">付款方式</td>
				<td bgcolor="#F4F4F4">客户</td>
				<td bgcolor="#F4F4F4">客户编号</td>
			</tr>
		<% for(CwbKuaiDiView  c : cwbViewList){ %>
				<tr bgcolor="#FF3300">
					<td align="center"><a  target="_blank" href="<%=request.getContextPath()%>/order/queckSelectOrder/<%=c.getCwb() %>"><%=c.getCwb() %></a></td>
					<td align="center"><%=c.getLanshouusername() %></td>
					<td><%=c.getLanshoubranchname() %></td>
					<td><%=c.getPaisongbranchname() %></td>
					<td><%=c.getLanshoutime() %></td>
					<td><%=c.getConsigneename() %></td>
					<td><%=c.getConsigneemobile() %></td>
					<td><%=c.getConsigneeaddress() %></td>
					<td><strong><%=c.getAllfee() %></strong></td>
					<td><label for="textfield2"></label><%=c.getFlowordertypeMethod() %></td>
					<td><%=c.getRemark() %></td>
					<td><%=c.getShouldfare() %></td>
					<td><%=c.getPackagefee() %></td>
					<td><%=c.getInsuredrate() %></td>
					<td><%=c.getRealweight() %></td>
					<td><%=c.getPaymethod() %></td>
					<td><%=c.getCustomername() %></td>
					<td><%=c.getCustomercode() %></td>
				 </tr>
		 <% }%>
	</table>
	</div><%} %>
	<div class="jg_10"></div><div class="jg_10"></div>
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
<div class="jg_10"></div>
<div class="clear"></div>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);

function exportField(page,j){
	if($("#isshow").val()=="1"&&<%=cwbViewList != null && cwbViewList.size()>0  %>){
		$("#exportmould2").val($("#exportmould").val());
		$("#btnval"+j).attr("disabled","disabled");
	 	$("#btnval"+j).val("请稍后……");
	 	$("#begin").val(page);
	 	$("#searchForm2").submit();
	}else{
		alert("没有做查询操作，不能导出！");
	};
}
</script>
</body>
</html>

