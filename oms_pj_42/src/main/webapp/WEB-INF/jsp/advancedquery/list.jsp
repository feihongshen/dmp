<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.Common"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<CwbOrder> orderlist = (List<CwbOrder>)request.getAttribute("orderlist");
  List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
  List<Common> commonlist = (List<Common>)request.getAttribute("commonlist");
  List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
  Page page_obj = (Page)request.getAttribute("page_obj");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>高级查询</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script>
function delSuccess(data){
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
</head>

<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
	<input type="hidden" id="orderByNameId" name="orderbyName" value="shiptime"/>
		<input type="hidden" id="orderByTypeId" name="orderbyType" value="<%=request.getParameter("orderbyType")==null?"DESC":request.getParameter("orderbyType") %>"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="300" rowspan="3" align="left" valign="top">订单号/运单号：
			<textarea cols="26" rows="4"  name ="cwbandtranscwb"><%=request.getParameter("cwbandtranscwb")==null?"":request.getParameter("cwbandtranscwb") %></textarea>
			</td>
		<td width="500" align="left">
			<select name ="customerid" id ="customerid" >
		          <option value ="0">请选择供货商</option>
		          <%for(Customer c : customerlist){ %>
		           <option value =<%=c.getCustomerid() %> 
		           <%if(c.getCustomerid() == Integer.parseInt(request.getParameter("customerid")==null?"0":request.getParameter("customerid"))  ){ %>selected="selected" <%} %> ><%=c.getCustomername() %></option>
		          <%} %>
		        </select>
			
			<select name ="commonnumber" id ="commonnumber">
		          <option value ="">请选择承运商</option>
		          <%for(Common c : commonlist){ %>
		          <option value =<%=c.getCommonnumber() %> 
		           <%if(c.getCommonnumber().equals(request.getParameter("commonnumber")==null?"":request.getParameter("commonnumber"))){ %>selected="selected" <%} %> ><%=c.getCommonname()%></option>
		          <%} %>
		        </select>
		</td>
		<td align="left">&nbsp;</td>
	</tr>
	<tr>
		<td align="left"><select id="datetype" name ="datetype">
            <option value="1">发货日期</option>
            <option value="2">签收日期</option>
            <option value="3">入库时间</option>
		</select>
			<input type ="text" name ="begindate" id="emaildate" class="Wdate"  onclick="WdatePicker()" value="<%=request.getParameter("begindate")==null?"":request.getParameter("begindate") %>"/>
			到
			<input type ="text" name ="enddate" id="emaildate" class="Wdate"  onclick="WdatePicker()" value="<%=request.getParameter("enddate")==null?"":request.getParameter("enddate") %>"/></td>
		<td align="left">&nbsp;</td>
	</tr>
	<tr>
		<td align="left"><select name =flowordertype id ="flowordertype">
          <option value ="0">请选择订单状态</option>
          <option <%if(FlowOrderTypeEnum.DaoRuShuJu.getValue() == Integer.parseInt(request.getParameter("flowordertype")==null?"0":request.getParameter("flowordertype"))  ){ %>selected="selected" <%} %> value="<%=FlowOrderTypeEnum.DaoRuShuJu.getValue()%>"><%=FlowOrderTypeEnum.DaoRuShuJu.getText()%></option>
          <option <%if(FlowOrderTypeEnum.RuKu.getValue() == Integer.parseInt(request.getParameter("flowordertype")==null?"0":request.getParameter("flowordertype"))  ){ %>selected="selected" <%} %> value="<%=FlowOrderTypeEnum.RuKu.getValue()%>"><%=FlowOrderTypeEnum.RuKu.getText()%></option>
          <option <%if(FlowOrderTypeEnum.PeiSongChengGong.getValue() == Integer.parseInt(request.getParameter("flowordertype")==null?"0":request.getParameter("flowordertype"))  ){ %>selected="selected" <%} %> value="<%=FlowOrderTypeEnum.PeiSongChengGong.getValue()%>"><%=FlowOrderTypeEnum.PeiSongChengGong.getText()%></option>
          <option <%if(FlowOrderTypeEnum.JuShou.getValue() == Integer.parseInt(request.getParameter("flowordertype")==null?"0":request.getParameter("flowordertype"))  ){ %>selected="selected" <%} %> value="<%=FlowOrderTypeEnum.JuShou.getValue()%>"><%=FlowOrderTypeEnum.JuShou.getText()%></option>
		</select>
			<input type="hidden" id="isshow" name="isshow" value="isshow" />
			<input type="submit" id="find" onclick="$('#searchForm').attr('action',1);return true;" value="查询" class="input_button2" /></td>
		<td align="left">
			<input type ="button" id="btnval" value="导出excle" class="input_button2" onclick="exportField();"/></td>
	</tr>
</table>
	</form>
	<form action="<%=request.getContextPath()%>/advancedquery/exportExcle" method="post" id="searchForm2">
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
			    <td width="5%" align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('emaildate');" >天数</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('flowordertype');" >订单号</td>
				<td width="9%" align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('customerid');" >供货商</td>
				<td width="9%" align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('commonnumber');" >承运商</td>
				<td width="18%" align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwbremark');" >备注</td>
				<td width="9%" align="center" valign="middle" bgcolor="#eef6ff"  onclick="orderForm('cwb');" >快件状态</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('transcwb');" >运单号</td>
				<td width="5%" align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('consigneename');" >姓名</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('receivablefee');" >代收款（元）</td>
				<td width="12%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		<% for(CwbOrder c : orderlist){ %>
				<tr bgcolor="#FF3300">
					<td align="center" valign="middle"><%=c.getBeforDay()%></td>
					<td align="center" valign="middle"><%=c.getCwb()%></td>
					<td align="center" valign="middle"><%=c.getCustomername()%></td>
					<td align="center" valign="middle"><%=(c.getCommonname()==null || c.getCommonname().toString().equals(""))?c.getCommonnumber():c.getCommonname() %></td>
					<td align="center" valign="middle"><%if(c.getCwbremark().length()>50){%>……<%=c.getCwbremark().substring(c.getCwbremark().length()-50, c.getCwbremark().length())%><%} else{%><%=c.getCwbremark()%><%} %></td>
					<td align="center" valign="middle"><%=c.getFlowordertypeMethod()%></td>
					<td align="center" valign="middle"><%=c.getTranscwb()%></td>
					<td align="center" valign="middle"><%=c.getConsigneename()%></td>
					<td align="center" valign="middle"><%=c.getReceivablefee()%></td>
					<td align="center" valign="middle" >[<a id="quxiaoMackButton_<%=c.getCwb()%>" href ="javascript:updateMack('<%=c.getCwb()%>')"><%=c.getMarksflag()==0?"标记":"取消标记" %></a>]
					                                    [<a href ="javascript:getViewBox('<%=c.getCwb()%>')">操作日志</a>]</td>
				 </tr>
		 <%} %>
	</table>
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
$("#datetype").val(<%=request.getParameter("datetype")==null?"1":request.getParameter("datetype")%>);
</script>

<script type="text/javascript">

function exportField(){
		$("#searchForm2").submit();	
}

</script>
<!-- 标记ajax地址 -->
<input type="hidden" id="updateMack_url" value="<%=request.getContextPath()%>/order/updateMack" />
<!-- 操作日志的ajax地址 -->
<input type="hidden" id="view" value="<%=request.getContextPath()%>/order/makelog/" />
</body>
</html>

