<%@page import="org.codehaus.jackson.map.ObjectMapper"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.controller.OrderFlowView"%>
<%@page import="cn.explink.controller.QuickSelectView"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.orderflow.*"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<%
List<JSONObject> viewList = request.getAttribute("viewList")==null?null:(List<JSONObject>)request.getAttribute("viewList");

List<Map<String, List<OrderFlowView>>> orderflowviewlist = request.getAttribute("orderflowviewlist")==null?null:(List<Map<String, List<OrderFlowView>>>)request.getAttribute("orderflowviewlist");
List<Map<String, List<ComplaintsView>>> comViewsList = request.getAttribute("comViewList")==null?null:(List<Map<String, List<ComplaintsView>>>)request.getAttribute("comViewList");
List<Map<String, List<AbnormalWriteBackView>>> abnormalWriteBackViewList = request.getAttribute("abnormalWriteBackViewList")==null?null:(List<Map<String, List<AbnormalWriteBackView>>>)request.getAttribute("abnormalWriteBackViewList");

String remand = request.getAttribute("remand")==null?"":request.getAttribute("remand").toString();
%>
<head>
<title>订单查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script>
</script>
</head>

<body onLoad="$('#cwb').focus();">
<table width="628" border="0" cellspacing="0" cellpadding="0"  align="center" class="table_2" style="height:567px">
	<tr align="center">
		<td width="100%" valign="top">
		<div style="height:567px; font-size:12px; overflow-y:auto; overflow-x:hidden">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" style="font-size:12px">
			<tr class="font_1">
				<td width="100%" height="26" align="center" valign="middle" bgcolor="#eef6ff" style ="font-size: 20px;">&nbsp;
				订单查询</td>
			</tr>
			<tr>
			  <td width="100%" height="26" align="center" valign="middle" bgcolor="#eef6ff">
			      <font color = "red"><%=remand %></font>
			  </td>
			</tr>
			<tr>
				<td width="100%" align="center" valign="top" valign="middle" >
					<%if(viewList != null){ %>
					<%for(JSONObject viewobj : viewList){ %>
					<table width="100%" border="0" cellspacing="1" cellpadding="0"   class="right_set2" >
						<tr>
							<td width="20%"  align="left" valign="middle">
								<p>订单号：<font color="red"><%=viewobj.getString("cwb") %></font></p>
								<p><font color="red"><%=viewobj.getString("remand") %></font></p>
							</td>
							<td colspan="2" >
							</td>
						</tr>
					</table>
					<table id="table_<%=viewobj.getString("cwb") %>" width="100%" border="0" cellspacing="1" cellpadding="0"   class="right_set2" >
						<tr>
							<td width="20%" align="left"  >操作时间</td>
							<td width="10%" align="center"  >操作人</td>
						    <td width="70%" align="left" >操作过程
						    </td>
                        </tr>
						<%if(viewobj.getString("remand").equals("")&&orderflowviewlist!=null&&orderflowviewlist.get(viewList.indexOf(viewobj)).get(viewobj.getString("cwb"))!=null){ %>
						<%List<OrderFlowView> OrderFlowList = orderflowviewlist.get(viewList.indexOf(viewobj)).get(viewobj.getString("cwb"));
						List<ComplaintsView> comViewList = comViewsList.get(viewList.indexOf(viewobj)).get(viewobj.getString("cwb"));
						List<AbnormalWriteBackView> backViewList = abnormalWriteBackViewList.get(viewList.indexOf(viewobj)).get(viewobj.getString("cwb"));%>
						   <% for(int i=0;i< OrderFlowList.size();i++){%>
						   <% OrderFlowView flow = OrderFlowList.get(i);%> 
							<tr>
								<td align="center"><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(flow.getCreateDate())%></td>
								<td align="center"><%=flow.getOperator().getRealname() %></td>
							    <td><%=flow.getDetail()%>
							    </td>
                                            </tr>	
                                            <%if(backViewList != null && backViewList.size()>0){ %>
                                            <%SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); %>
                                            <%for(int j=0,k=0;j<backViewList.size();j++,k++){ %>
                                            	   <% if(flow.getCreateDate().getTime() - f.parse(backViewList.get(j).getCredatetime()).getTime()<=0){%>
                                            	        <%if(i+1 < OrderFlowList.size() && f.parse(backViewList.get(j).getCredatetime()).getTime() - OrderFlowList.get(i+1).getCreateDate().getTime()<=0){ %>
                                             	        <tr>
												<td align="center"><%=backViewList.get(j).getCredatetime()%></td>
												<td align="center"><%=backViewList.get(j).getUsername() %></td>
											    <td><font color="red">[问题件]类型：[<%=backViewList.get(j).getTypename() %>]</font>在[<font color="red"><%=backViewList.get(j).getBranchname()%></font>]
											    <%=k==0?"创建":(backViewList.get(j).getDescribe()==null||"".equals(backViewList.get(j).getDescribe())?"备注：无":"备注："+backViewList.get(j).getDescribe()) %>
											    </td>
                                             			</tr>
                                            	        <% backViewList.remove(backViewList.get(j));
                                            	        j=-1;
                                			                } %>
                                            	   <%} %>
                                            	   <%if(i == OrderFlowList.size()-1 && backViewList.size()>0){ %>
                                            	   			<tr>
												<td align="center"><%=backViewList.get(j).getCredatetime()%></td>
												<td align="center"><%=backViewList.get(j).getUsername() %></td>
											    <td><font color="red">[问题件]类型：[<%=backViewList.get(j).getTypename() %>]</font>在[<font color="red"><%=backViewList.get(j).getBranchname()%></font>]
											    <%=k==0?"创建":(backViewList.get(j).getDescribe()==null||"".equals(backViewList.get(j).getDescribe())?"备注：无":"备注："+backViewList.get(j).getDescribe()) %>
											    </td>
                                             			</tr>
                                            	   <%} %>
                                            <%}
                                            } %>
						<%if(comViewList!=null&&comViewList.size()>0){%>
						<%SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); %>
						    <%for(int m=0;m<comViewList.size();m++){ %>
                                            	   <% if(flow.getCreateDate().getTime() - f2.parse(comViewList.get(m).getCreateTime()).getTime()<=0){%>
                                            	        <%if(i+1 < OrderFlowList.size() && f2.parse(comViewList.get(m).getCreateTime()).getTime() - OrderFlowList.get(i+1).getCreateDate().getTime()<=0){ %>
                                            	        <%if(comViewList.get(m).getAuditTypeName().equals("")){ %>
                                            	        	<tr>
												<td align="center"><%=comViewList.get(m).getCreateTime()%></td>
												<td align="center">
												<%if(comViewList.get(m).getAuditTypeName().equals("")){ %>
													<%=comViewList.get(m).getCreateUserName() %>
												<%}else{ %>
													<%=comViewList.get(m).getAuditUserName() %>
												<%} %>
												</td>
											    <td>在[<font color="red"><%=comViewList.get(m).getBranchname() %></font>]
											    <%if(comViewList.get(m).getAuditTypeName().equals("")){ %>
											    	受理为[<font color="red"><%=comViewList.get(m).getTypename()%></font>]备注：<font color="red"><%=comViewList.get(m).getContent() %></font>
											    <%}else{ %>
											    	受理为[<font color="red"><%=comViewList.get(m).getTypename()%></font>]备注：<font color="red"><%=comViewList.get(m).getContent() %></font>
											    	审核[<font color="red"><%=comViewList.get(m).getAuditTypeName()%></font>]备注：<font color="red"><%=comViewList.get(m).getAuditRemark() %></font>
											    <%} %>
											    </td>
                                             			</tr>
                                            	        <%}else{ %>
                                            	        	<tr>
												<td align="center"><%=comViewList.get(m).getCreateTime()%></td>
												<td align="center">
													<%=comViewList.get(m).getCreateUserName() %>
												</td>
											    <td>在[<font color="red"><%=comViewList.get(m).getBranchname() %></font>]
											    	受理为[<font color="red"><%=comViewList.get(m).getTypename()%></font>]备注：<font color="red"><%=comViewList.get(m).getContent() %></font>
											    </td>
                                             			</tr>
                                             			<tr>
												<td align="center"><%=comViewList.get(m).getAuditTime()%></td>
												<td align="center">
													<%=comViewList.get(m).getAuditUserName() %>
												</td>
											    <td>在[<font color="red"><%=comViewList.get(m).getBranchname() %></font>]
											    	审核[<font color="red"><%=comViewList.get(m).getAuditTypeName()%></font>]备注：<font color="red"><%=comViewList.get(m).getAuditRemark() %></font>
											    </td>
                                             			</tr>
                                            	        <%} %>
                                             	        
                                            	        <% comViewList.remove(comViewList.get(m));
                                            	        m=-1;
                                			                } %>
                                            	   <%} %>
                                            	   <%if(i == OrderFlowList.size()-1 && comViewList.size()>0){ %>
                                            	   			 <%if(comViewList.get(m).getAuditTypeName().equals("")){ %>
                                            	        	<tr>
												<td align="center"><%=comViewList.get(m).getCreateTime()%></td>
												<td align="center">
												<%if(comViewList.get(m).getAuditTypeName().equals("")){ %>
													<%=comViewList.get(m).getCreateUserName() %>
												<%}else{ %>
													<%=comViewList.get(m).getAuditUserName() %>
												<%} %>
												</td>
											    <td>在[<font color="red"><%=comViewList.get(m).getBranchname() %></font>]
											    <%if(comViewList.get(m).getAuditTypeName().equals("")){ %>
											    	受理为[<font color="red"><%=comViewList.get(m).getTypename()%></font>]备注：<font color="red"><%=comViewList.get(m).getContent() %></font>
											    <%}else{ %>
											    	受理为[<font color="red"><%=comViewList.get(m).getTypename()%></font>]备注：<font color="red"><%=comViewList.get(m).getContent() %></font>
											    	审核[<font color="red"><%=comViewList.get(m).getAuditTypeName()%></font>]备注：<font color="red"><%=comViewList.get(m).getAuditRemark() %></font>
											    <%} %>
											    </td>
                                             			</tr>
                                            	        <%}else{ %>
                                            	        	<tr>
												<td align="center"><%=comViewList.get(m).getCreateTime()%></td>
												<td align="center">
													<%=comViewList.get(m).getCreateUserName() %>
												</td>
											    <td>在[<font color="red"><%=comViewList.get(m).getBranchname() %></font>]
											    	受理为[<font color="red"><%=comViewList.get(m).getTypename()%></font>]备注：<font color="red"><%=comViewList.get(m).getContent() %></font>
											    </td>
                                             			</tr>
                                             			<tr>
												<td align="center"><%=comViewList.get(m).getAuditTime()%></td>
												<td align="center">
													<%=comViewList.get(m).getAuditUserName() %>
												</td>
											    <td>在[<font color="red"><%=comViewList.get(m).getBranchname() %></font>]
											    	审核[<font color="red"><%=comViewList.get(m).getAuditTypeName()%></font>]备注：<font color="red"><%=comViewList.get(m).getAuditRemark() %></font>
											    </td>
                                             			</tr>
                                            	        <%} %>
                                            	   <%} %>
							<%}} %>
						<%}} %> 
					</table>
					<p>----------------------------------------------------------------------------------------------------</p>
					<%}}%>
				</td>
			</tr>
		</table>
		</div></td>
	</tr>
</table>
</body>
</html>