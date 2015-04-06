<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<CwbOrder> cwbOrderList = (List<CwbOrder>)request.getAttribute("cwborderList");
  List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
  List<Branch> branchAllList = (List<Branch>)request.getAttribute("branchAllList");
  
  Page page_obj =(Page)request.getAttribute("page_obj");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<script type="text/javascript">
function editInit(){
}

function editSuccess(data){
	$("#searchForm").submit();
}

</script>
</HEAD>
<BODY style="background:#f5f5f5">
<div class="right_box">
<div class="inputselect_box">
<form id="searchForm" action ="1" method = "post">
出库站点： <select id="branchid" name="branchid">
               <option value =-1>请选择</option>
              <%for(Branch b : branchList){ %>
                <option value ="<%=b.getBranchid()%>"><%=b.getBranchname() %></option>
              <%} %>
              </select>
<input type ="submit"  value ="查询">
<input type="button"  onclick="location.href='1'" value="返回" class="input_button2" />
</form>
</div>
<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	<tr class="font_1">
			<td width="10%" height="38" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">下一站</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">到错货站点</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">收件地址</td>
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">备注信息</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		 <% for(CwbOrder co : cwbOrderList){ %>
		<tr>
			<td width="10%" align="center" valign="middle"><%=co.getCwb() %></td>
			<td width="5%" align="center" valign="middle" >
			<%for(CwbOrderTypeIdEnum ce : CwbOrderTypeIdEnum.values()){if(co.getCwbordertypeid()==ce.getValue()){ %>
				<%=ce.getText() %>
			<%}} %>
			</td>
			<td width="8%" align="center" valign="middle" ><%=co.getEmaildate() %></td>
			<td width="10%" align="center" valign="middle"><%if(branchAllList!=null&&branchAllList.size()>0){for(Branch b : branchAllList){if(co.getNextbranchid()==b.getBranchid()){ %><%=b.getBranchname() %><%break;}}} %></td>
			<td width="10%" align="center" valign="middle"><%if(branchAllList!=null&&branchAllList.size()>0){for(Branch b : branchAllList){if(co.getCurrentbranchid()==b.getBranchid()){ %><%=b.getBranchname() %><%break;}}} %></td>
			<td width="10%" align="left" valign="middle" ><%=co.getConsigneeaddress() %></td>
			<td width="15%" align="center"><%=co.getCwbremark() %></td>
			<td width="10%" align="center">
				[<a id="sub_<%=co.getCwb() %>" href="javascript:edit_button('<%=co.getCwb()%>');">改为正常入库</a>]
			</td>
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
$("#branchid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("branchid"))%>);
</script>
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/cwborder/daocuohuobeizhupage/" />
</BODY>
</HTML>
