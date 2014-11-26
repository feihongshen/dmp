<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.util.*"%>
<%
List<CwbDetailView> cOrders =(List<CwbDetailView>) request.getAttribute("cOrders");
List<Exportmould> exportmouldlist =(List<Exportmould>) request.getAttribute("exportmouldlist");
Page page_obj =(Page)request.getAttribute("page_obj");
Object cwbs =request.getAttribute("cwbs");
Object branchids =request.getAttribute("branchids");
Object startime =request.getAttribute("startime");
Object endtime =request.getAttribute("endtime");
Object flag =request.getAttribute("flag");

/* long page =Long.parseLong(request.getAttribute("page").toString()); */
long p=1;
Object o=request.getAttribute("p");
if(o!=null)
p=Long.parseLong(o.toString());
System.out.println("sssssssssssssssss"+p+" "+o);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>明细</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
		<form action="<%=request.getContextPath()%>/exportwarhousesummary/exportExcle" method="post" id="searchForm2">
<div>
		<input type="hidden" name="cwbs" id="cwbs" value="<%=cwbs%>"/>
		<input type="hidden" name="p" id="p" value="<%=p%>"/>
		<input type="hidden" id="detail_branchids" name="detail_branchids" value='<%=branchids%>'/>
			<input type="hidden" id="detail_startime" name="detail_startime"  value='<%=startime%>'/>
			<input type="hidden" id="detail_endtime" name="detail_endtime"  value='<%=endtime%>'/>
			<input type="hidden" id="flag" name="flag"  value="<%=flag%>"/> 
					<%if(cOrders != null && cOrders.size()>0){  %>
			<select name ="exportmould" id ="exportmould">
	          <option value ="0">默认导出模板</option>
	          <%for(Exportmould e:exportmouldlist){%>
	           <option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
	          <%} %>
			</select><%} %>
			
					<input type ="submit" id="btnval0" value="导出Excel"  />
						<!-- <a href="javascript:history.go(-1)"><input  type="button" value="返回"/></a> -->
	</form>
	</div>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2"
										class="table_5">
										<tr>
											<td width="60" align="center" bgcolor="#f1f1f1">序号</td>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">包号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
											<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
												<!-- <td width="100" align="center" bgcolor="#f1f1f1">订单备注</td> -->
											<td align="center" bgcolor="#f1f1f1">地址</td>
										</tr>
									</table>
									
									<div style="height: 500px; overflow-y: scroll">
										<table id="weirukuTable" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%int index=1;
											for(CwbDetailView co : cOrders){ %>
											<tr id="TR<%=co.getCwb() %>" cwb="<%=co.getCwb() %>" customerid="<%=co.getCustomerid() %>" class="cwbids">
												<td width="60" align="center"><%=index++ %></td>
												<td width="120" align="center"><%=co.getCwb() %></td>
												<td width="100" align="center"><%=co.getPackagecode() %></td>
												<td width="100" align="center"><%=co.getCustomername() %></td>
												<td width="140"><%=co.getEmaildate() %></td>
												<td width="100"><%=co.getConsigneename() %></td>
												<td width="100"><%=co.getReceivablefee().doubleValue() %></td>
												<%-- <td width="100"><%=co.getRemarkView() %></td> --%>
												<td align="left"><%=co.getConsigneeaddress() %></td>
												</tr>
											<%} %>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
		<form action="<%=request.getContextPath()%>/exportwarhousesummary/exportExcle" method="post" id="searchForm2">
		<input type="hidden" name="cwbs" id="cwbs" value="<%=cwbs%>"/>
	</form>

	<%if(page_obj.getMaxpage()>1){ %>
			<div class="iframe_bottom">
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
					<a href="javascript:$('#searchFormFlash').attr('action','1');$('#searchFormFlash').submit();" >第一页</a>　
					<a href="javascript:$('#searchFormFlash').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchFormFlash').submit();">上一页</a>　
					<a href="javascript:$('#searchFormFlash').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchFormFlash').submit();" >下一页</a>　
					<a href="javascript:$('#searchFormFlash').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchFormFlash').submit();" >最后一页</a>
					　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
							id="selectPg"
							onchange="$('#searchFormFlash').attr('action',$(this).val());$('#searchFormFlash').submit()">
							<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
							<option value="<%=i %>"><%=i %></option>
							<% } %>
						</select>页
				</td>
			</tr> 
			</table>
			</div>
		<%} %>
		<form action="1" method="post" id="searchFormFlash">
			<input type="hidden" id="detail_branchids" name="detail_branchids" value='<%=branchids%>'/>
			<input type="hidden" id="detail_startime" name="detail_startime"  value='<%=startime%>'/>
			<input type="hidden" id="detail_endtime" name="detail_endtime"  value='<%=endtime%>'/>
			<input type="hidden" id="flag" name="flag"  value="<%=flag%>"/> 
		
		</form>
		
			<form id="detailForm"
				action="<%=request.getContextPath()%>/exportwarhousesummary/detail/1"
				method="post">
				</form>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
		</body>
</html>
