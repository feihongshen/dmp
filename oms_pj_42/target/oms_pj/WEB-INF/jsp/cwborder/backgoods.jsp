<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.Common"%>
<%@page import="cn.explink.enumutil.GoodsBacktypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<Common> commonList = (List<Common>)request.getAttribute("commonlist");
  List<Customer> customerList = (List<Customer>)request.getAttribute("customerlist");
  List<CwbOrder> backgoodslist = (List<CwbOrder>)request.getAttribute("backgoodslist");
  List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
  Page page_obj = (Page)request.getAttribute("page_obj");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>退货管理</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
function editInit(){
	//无处理
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

function audit(cwb,type){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/order/audit/"+cwb,
		data:{auditstate:type},
		dataType:"json",
		success : function(data) {
			if(data.errorCode==0){
				alert(data.error);
					$("#auditstate1_"+cwb).toggle();
					$("#auditstate2_"+cwb).toggle();
					
			}else{
				alert(data.error);
			}
		}
	});
	if(type ==0){
		$("#auditstate3_"+cwb).html("未审核");
	}else if(type ==-1){
		$("#auditstate3_"+cwb).html("审核未通过");
	}else if(type ==1){
		$("#auditstate3_"+cwb).html("审为再投");
	}else if(type ==2){
		$("#auditstate3_"+cwb).html("审为退供货商");
	}
}
function auditEgan(cwb){
	var type = "0";
	if($("#auditEganstate_"+cwb).html()=="再投审核"){
		type ="1";
	}
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/order/auditEgan/"+cwb,
		data:{auditstate:type},
		dataType:"json",
		success : function(data) {
			if(data.errorCode==0){
				alert(data.error);
				if($("#auditEganstate_"+cwb).html()=="再投审核"){
					$("#auditEganstate_"+cwb).html("撤销再投审核");
				}else{
					$("#auditEganstate_"+cwb).html("再投审核");
				}
			}else{
				alert(data.error);
			}
		}
	});
}
</script>
</head>

<body style="background:#eef9ff">

<div class="right_box">
	<div class="inputselect_box">
	<span>
	</span>
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
		<input type="hidden" id="orderByNameId" name="orderbyName" value="shiptime"/>
		<input type="hidden" id="orderByTypeId" name="orderbyType" value="<%=request.getParameter("orderByType")==null?"DESC":request.getParameter("orderByType") %>"/>
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
		<select name ="auditstate" id ="auditstate" >
			<option value ="-2">请选择审核条件</option>
			 <%for(GoodsBacktypeEnum g : GoodsBacktypeEnum.values()){ %>
			<option value ="<%=g.getValue()%>"><%=g.getText()%></option>
			<%} %>
		</select>
		发货时间：<input type ="text" name ="beginshiptime" id="emaildate" class="Wdate"  onclick="WdatePicker()" value="<%=request.getParameter("beginshiptime")==null?"":request.getParameter("beginshiptime") %>"/>&nbsp;&nbsp;到
		<input type ="text" name ="endshiptime" id="emaildate" class="Wdate"  onclick="WdatePicker()" value="<%=request.getParameter("endshiptime")==null?"":request.getParameter("endshiptime") %>"/>
		<input type="submit" id="find" onclick="$('#searchForm').attr('action',1);return true;" value="查询" class="input_button2" />
		<select name ="exportmould" id ="exportmould">
	          <option value ="0">默认模版</option>
	          <%for(Exportmould e:exportmouldlist){%>
	           <option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
	          <%} %>
		</select>
	    <input type ="button" id="btnval" value="导出excle" class="input_button2" onclick="exportField();"/>
	</form>
	<form action="<%=request.getContextPath()%>/order/backGoodsexportExcle" method="post" id="searchForm2">
	 <input type="hidden" name="exportmould2" id="exportmould2" />
	</form>
	</div>
	<div class="right_title">
	<div style="height: 35px"></div>
<form method ="post" action ="<%=request.getContextPath()%>/order/batch"  id ="form1">
	<div style="overflow-x:scroll; width:100%" id="scroll">
	<table width="1420" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
			<td width="100"  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('auditstate');" >状态</td>
			<td width="150"   align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('flowordertype');">快件状态</td>
			<td width="100"  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('customerid');">供货商</td>
			<td width="100"  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('commonnumber');">承运商</td>
			<td width="100"  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwbremark');">备注</td>
			<td width="100" align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');">订单号</td>
			<td width="100" align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('transcwb');">运单号</td>
			<td width="150" align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('shiptime');">发货时间</td>
			<td width="100" align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('receivablefee');">代收款</td>
			<td width="400" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		<% for(CwbOrder c : backgoodslist){ %>
		<tr>
			<td align="center" valign="middle" id="auditstate3_<%=c.getCwb()%>"><%=c.getGoodsbackType()%></td>		
			<td align="center" valign="middle"><%=c.getFlowordertypeMethod()%></td>
			<td align="center" valign="middle"><%=c.getCustomername()%></td>
			<td align="center" valign="middle"><%=(c.getCommonname()==null||"".equals(c.getCommonname()))?c.getCommonnumber():c.getCommonname()%></td>
			<td align="center" valign="middle"><%if(c.getCwbremark().length()>50){%>……<%=c.getCwbremark().substring(c.getCwbremark().length()-50, c.getCwbremark().length())%><%} else{%><%=c.getCwbremark()%><%} %></td>
			<td align="center" valign="middle"><%=c.getCwb()%></td>
			<td align="center" valign="middle"><%=c.getTranscwb()%></td>
			<td align="center" valign="middle"><%=c.getEmaildate() %></td>
			<td align="center" valign="middle"><%=c.getReceivablefee()%></td>
			<td align="center" valign="middle" >
			<span>
			[<a href ="javascript:edit_button('<%=c.getCwb()%>')">退货备注</a>]
			[<a href ="javascript:getViewBox('<%=c.getCwb()%>')">操作日志</a>]
			</span>
			<span id="auditstate1_<%=c.getCwb()%>" <%if(c.getAuditstate() !=0 && c.getAuditstate() !=3){ %> style='display:none;'<%} %>>
			[<a  href ="javascript:audit('<%=c.getCwb()%>',<%= GoodsBacktypeEnum.Shenweituigongyingshang.getValue()%>)">
			审为退供货商
			</a>]
			[<a  href ="javascript:audit('<%=c.getCwb()%>',<%= GoodsBacktypeEnum.Shenweizaitou.getValue()%>)">
			审为再投
			</a>]
			[<a  href ="javascript:audit('<%=c.getCwb()%>',<%= GoodsBacktypeEnum.Shenhebutongguo.getValue()%>)">
			审合不通过
			</a>]
			</span>
			<span id="auditstate2_<%=c.getCwb()%>" <%if(c.getAuditstate() ==0 || c.getAuditstate() ==3){ %> style='display:none;'<%} %>>
			[<a  href ="javascript:audit('<%=c.getCwb()%>',<%= GoodsBacktypeEnum.Weishenhe.getValue()%>)">
			撤销审核
			</a>]
			</span>
			
			</td>
		</tr>
		<%} %>
	</table>
	</div>
	</form>
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
$("#surpassdate").val(<%=request.getParameter("surpassdate")==null?"0":request.getParameter("surpassdate") %>);
</script>

<script type="text/javascript">

function exportField(){
		$("#exportmould2").val($("#exportmould").val());
		$("#searchForm2").submit();	
}
$("#auditstate").val(<%=request.getParameter("auditstate")%>);
</script>
<!-- 退货备注的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/order/returngoodsremark/" />
<!-- 操作日志的ajax地址 -->
<input type="hidden" id="view" value="<%=request.getContextPath()%>/order/makelog/" />
</body>
</html>

