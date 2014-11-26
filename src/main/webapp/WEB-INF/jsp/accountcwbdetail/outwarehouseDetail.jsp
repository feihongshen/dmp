<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.Branch,cn.explink.domain.AccountCwbDetail,cn.explink.domain.AccountFeeDetail"%>
<%@page import="cn.explink.enumutil.AccountTongjiEnum,cn.explink.enumutil.CwbOrderTypeIdEnum,cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import="cn.explink.util.Page"%>
<%
	List<AccountCwbDetail> detailList=request.getAttribute("detailList")==null?null:(List<AccountCwbDetail>)request.getAttribute("detailList");
	Page page_obj = (Page)request.getAttribute("page_obj");
	String ids=request.getParameter("ids")==null?"":request.getParameter("ids");
	AccountCwbDetail accountCwbDetail=(AccountCwbDetail)request.getAttribute("accountCwbDetail");
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>买单结算订单明细</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script language="javascript">
function exportField(){
	$("#clickExport").attr("disabled","disabled");
	$("#clickExport").val("请稍后");
 	$("#exportForm").submit(); 
}
</script>
</head>
<body style="background:#fff" marginwidth="0" marginheight="0">
<div class="inputselect_box" style="top: 0px ">
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page")%>" method="post" id="searchForm">
		<input type="hidden" id="ids" name="ids" value="<%=ids%>"/>
		<input type="button" class="input_button1" id="clickExport"  onclick="exportField();" value="导出Excel" />
	</form>
</div>
<div style="height:35px"></div>
<form action="" method="post">
	<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
		<tr class="font_1">
			<td bgcolor="#f4f4f4">订单号</td>
			<td bgcolor="#f4f4f4">订单类型</td>
			<!-- <td bgcolor="#f4f4f4">配送结果</td> -->
			<td bgcolor="#f4f4f4">发货件数</td>
			<td bgcolor="#f4f4f4">出库件数</td>
			<td bgcolor="#f4f4f4">货物价值[元]</td>
			<td bgcolor="#f4f4f4">代收货款[元]</td>
			<td bgcolor="#f4f4f4">应退款[元]</td>
		</tr>
		<%if(detailList!=null&&!detailList.isEmpty()){
			for(int i=0;i<detailList.size();i++){
				AccountCwbDetail list=detailList.get(i);
		%>
		<tr>
			<td><%=list.getCwb()%></td>
			<td><%if(list.getCwbordertypeid()==CwbOrderTypeIdEnum.Peisong.getValue()){out.print("配送");}else if(list.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmenhuan.getValue()){
					out.print("上门换");}else if(list.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()){out.print("上门退");}else{out.print("未确定");}
				%>
			</td>
			<%-- <td><%for(DeliveryStateEnum ft : DeliveryStateEnum.values()){if(list.getDeliverystate()==ft.getValue()){ %><%=ft.getText() %><%}} %> --%>
			</td>
			<td><%=list.getSendcarnum()%></td>
			<td><%=list.getScannum()%></td>
			<td align="right"><strong><%=list.getCaramount()%></strong></td>
			<td align="right"><strong><%=list.getReceivablefee()%></strong></td>
			<td align="right"><strong><%=list.getPaybackfee()%></strong></td>
		</tr>
		<%}} %>
		<tr>
			<td class="high">合计：<font color="red"><%=request.getAttribute("total")%></font>&nbsp;单  </td>
			<td></td>
			<td></td>
			<td></td>
			<td class="high"><font color="red"><%=accountCwbDetail.getCaramount()%></font></td>
			<td class="high"><font color="red"><%=accountCwbDetail.getReceivablefee()%></font></td>
			<td class="high"><font color="red"><%=accountCwbDetail.getPaybackfee()%></font></td>
		</tr>
	</table>
	<!--底部翻页 -->
	<div class="jg_35"></div>
	<%if(page_obj.getMaxpage()>1){%>
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
</form>

<form action="<%=request.getContextPath() %>/accountcwbdetail/exportOutwarehouseDetail" method="post" id="exportForm">
	<input type="hidden" id="ids" name="ids" value="<%=ids%>"/>
</form>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>
</html>