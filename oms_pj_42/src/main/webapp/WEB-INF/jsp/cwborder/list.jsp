<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.Common"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<Common> commonList = (List<Common>)request.getAttribute("commonlist");
  List<Customer> customerList = (List<Customer>)request.getAttribute("customerlist");
  List<CwbOrder> orderlist = (List<CwbOrder>)request.getAttribute("orderlist");
  List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
  Page page_obj = (Page)request.getAttribute("page_obj");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预警</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
function delSuccess(data){
	$("#searchForm").submit();
}

function excelImport(){
	if($("#txtFileID").val()==""){
		alert("还没有选择任何上传文件");
		return false;
	}
	$('#searchForm').attr('action','<%=request.getContextPath()%>/order/importExcle');
	$("#searchForm").submit();
}

function orderForm(ordername){
    
	if($("#orderByTypeId").val()=="ASC"){
    	$("#orderByNameId").val(ordername);
    	$("#orderByTypeId").val("DESC");
    }else {
    	$("#orderByNameId").val(ordername);
    	$("#orderByTypeId").val("ASC");
    }
	$("#searchForm").submit();
	
}
</script>
<script type="text/javascript">
$("document").ready(function(){  
	$("#checkall").click(function(){  
	  if($("#checkall").attr("checked")){
			$("[name='batchtag']").attr("checked",'true');//全选  
		}else{
		   $("[name='batchtag']").removeAttr("checked");//取消全选  
		}	
	});
 });
function updateBatchMack(){
	var batchtag = new Array();
	$("input[name='batchtag']:checked").each(function() {batchtag.push($(this).val());});
	if (batchtag.length > 0) {
			$.ajax({
				type : "POST",
				url : "<%=request.getContextPath()%>/order/updateBatchMack",
				data : {cwbs: batchtag.join(','),mackType:1},
				dataType : "json",
				success : function(data) {
					if(data.errorCode==0){
						$("input[name='batchtag']:checked").each(function() {
							batchtag.push($("#quxiaoMackButton_"+$(this).val()).html("取消标记"));
						});
					}
					alert(data.error);
				}
			});
	}else{
		alert("您没有选择要标记的记录");
	}
}
</script>
<script type="text/javascript">
function showMsg(){
	if(<%=request.getAttribute("importMsg")!=null%> ){
		$('#searchForm').attr('action','<%=request.getContextPath()%>/order/earlywarning/1');
		alert("<%=request.getAttribute("importMsg")%>");
	}
}
showMsg();

</script>
</head>
<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box" >
		<form action="<%=request.getContextPath()%>/order/earlywarning/1" method="post" enctype="multipart/form-data" name="form1" id="searchForm">
		<input type="hidden" id="orderByNameId" name="orderbyName" value="shiptime"/>
		<input type="hidden" id="orderByTypeId" name="orderbyType" value="<%=request.getParameter("orderbyType")==null?"DESC":request.getParameter("orderbyType") %>"/>
	
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="50%">
			<input type="button"  value="批量标记" onclick="updateBatchMack()" class="input_button2" />	
			<select name ="customerid" id ="customerid" >
	          <option value ="0">请选择供货商</option>
	          <%for(Customer c : customerList){ %>
	           <option value =<%=c.getCustomerid() %> 
		           <%if(c.getCustomerid() == Integer.parseInt(request.getParameter("customerid")==null?"0":request.getParameter("customerid"))  ){ %>selected="selected" <%} %> >
		           <%=c.getCustomername() %>
	           </option>
	          <%} %>
			</select>
			<select name ="commonnumber" id ="commonnumber" >
	          <option value ="">请选择承运商</option>
	          <%for(Common c : commonList){ %>
	          <option value =<%=c.getCommonnumber() %> 
		           <%if(c.getCommonnumber().equals(request.getParameter("commonnumber")==null?"":request.getParameter("commonnumber"))  ){ %>selected="selected" <%} %> >
		           <%=c.getCommonname()%>
	          </option>
	          <%} %>
			</select>	
			<select name ="surpassdate" id ="surpassdate">
		          <option value ="-2">超过2天</option>
		          <option value ="-3">超过3天</option>
		          <option value ="-4">超过4天</option>
		          <option value ="-5">超过5天</option>
		          <option value ="-6">超过6天</option>
		          <option value ="-7">超过7天</option>
			</select>
			<input type= "checkbox" value ="1" name ="marksflag" id ="marksflag" <%if(Integer.parseInt(request.getParameter("marksflag")==null?"0":request.getParameter("marksflag"))>0){ %>checked="checked" <%} %> />已标记
			<input type="hidden" id="isshow" name="isshow" value="isshow" />
			<input type="submit" id="find" onclick="$('#searchForm').attr('action','<%=request.getContextPath()%>/order/earlywarning/1');return true;" value="查询" class="input_button2" />
	    </td>
		<td width="20%">
			<select name ="exportmould" id ="exportmould">
		          <option value ="0">默认模板</option>
		          <%for(Exportmould e:exportmouldlist){%>
		           <option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
		          <%} %>
			</select>
		    <input type ="button" id="btnval" value="导出excle" class="input_button2" onclick="exportField();"/>
		</td>
		<td><label for="fileField"></label>
			<input type="file" name="txtFileName" id="txtFileID" class="selectfile"/>
			<input type="button"  value="导入结果" class="input_button2" onclick="excelImport();" />
		</td>
		</tr>
	</table>
		</form>
	<form action="<%=request.getContextPath()%>/order/exportExcle" method="post" id="searchForm2">
	 <input type="hidden" name="exportmould2" id="exportmould2" />
	</form>
	</div>
	<div class="jg_35"></div>
	<div class="right_title">
		<div class="list_topbar">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		  <tr class="font_1">
				<td width="3%" align="center" valign="middle" bgcolor="#eef6ff"><input type ="checkbox" id ="checkall" value ="" name ="checkall"/></td>
				<td width="5%" align="center" valign="middle" bgcolor="#eef6ff"  onclick="orderForm('emaildate');" >天数</td>                 
				<td width="9%" align="center" valign="middle" bgcolor="#eef6ff"  onclick="orderForm('flowordertype');" >快件状态</td>           
				<td width="9%" align="center" valign="middle" bgcolor="#eef6ff"  onclick="orderForm('customerid');" >供货商</td>               
				<td width="9%" align="center" valign="middle" bgcolor="#eef6ff"  onclick="orderForm('commonnumber');" >承运商</td>             
				<td width="18%" align="center" valign="middle" bgcolor="#eef6ff"  onclick="orderForm('cwbremark');" >备注</td>                
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff"  onclick="orderForm('cwb');" >订单号 </td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff"  onclick="orderForm('transcwb');" >运单号</td>                
				<td width="5%" align="center" valign="middle" bgcolor="#eef6ff"   onclick="orderForm('consigneename');" >姓名</td>             
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff"  onclick="orderForm('receivablefee');" >代收款（元）</td>        
				<td width="12%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
			</tr>
			</table>
		</div>
		<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_4" >
		<% for(CwbOrder c : orderlist){ %>
				<tr <%if(c.getBeforDay()>=7) {%> bgcolor="#c90200"<%}else if(c.getBeforDay()==6){%> bgcolor="#ff3300" <%} else if(c.getBeforDay()==5){%> bgcolor="#6665fe" <%}else if(c.getBeforDay()==4){ %>  bgcolor="#026698"  <%}else if(c.getBeforDay()==3){ %>  bgcolor="#006600" <%}else if(c.getBeforDay()==2){ %> bgcolor="#009900" <%}%>>
				    <td width="3%"  align="center" valign="middle"><input type ="checkbox" value ="<%=c.getCwb() %>" id ="batchtag" name ="batchtag"/></td>
					<td width="5%"  align="center" valign="middle"><em><%=c.getBeforDay()%></em></td>
					<td width="9%"  align="center" valign="middle"><%=c.getFlowordertypeMethod()%></td>
					<td width="9%"  align="center" valign="middle"><%=c.getCustomername()%></td>
					<td width="9%"  align="center" valign="middle"><%=c.getCommonname()%></td>
					<td width="18%" align="center" valign="middle"><%if(c.getCwbremark().length()>50){%>……<%=c.getCwbremark().substring(c.getCwbremark().length()-50, c.getCwbremark().length())%><%} else{%><%=c.getCwbremark()%><%} %></td>
					<td width="10%" align="center" valign="middle"><%=c.getCwb()%></td>
					<td width="10%" align="center" valign="middle"><%=c.getTranscwb()%></td>
					<td width="5%"  align="center" valign="middle"><%=c.getConsigneename()%></td>
					<td width="10%" align="center" valign="middle"><%=c.getReceivablefee()%></td>
					<td width="12%" align="center" valign="middle" >[<a id="quxiaoMackButton_<%=c.getCwb()%>" href ="javascript:updateMack('<%=c.getCwb()%>')"><%=c.getMarksflag()==0?"标记":"取消标记" %></a>]
					                                    [<a href ="javascript:getViewBox('<%=c.getCwb()%>')">操作日志</a>]</td>
				</tr>
		<%} %>
		</table>
	</div>
	<div class="jg_30"></div>
	<div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr >
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff"><a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/order/earlywarning/1');$('#searchForm').submit();" >第一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/order/earlywarning/<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/order/earlywarning/<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/order/earlywarning/<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
					　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
							id="selectPg"
							onchange="$('#searchForm').attr('action',<%=request.getContextPath()%>/order/earlywarning/$(this).val());$('#searchForm').submit()">
							<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
							<option value="<%=i %>"><%=i %></option>
							<% } %>
						</select>页</td>
			</tr>
		</table>
	</div>
</div>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#surpassdate").val(<%=request.getParameter("surpassdate")==null?"0":request.getParameter("surpassdate") %>);
</script>

<script type="text/javascript">

function exportField(){
		$("#exportmould2").val($("#exportmould").val());
		$("#searchForm2").submit();	
	
}

</script>
<!-- 标记ajax地址 -->
<input type="hidden" id="updateMack_url" value="<%=request.getContextPath()%>/order/updateMack" />
<!-- 操作日志的ajax地址 -->
<input type="hidden" id="view" value="<%=request.getContextPath()%>/order/makelog/" />
</body>


</html>

