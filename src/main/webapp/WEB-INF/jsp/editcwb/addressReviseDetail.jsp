<%@page import="cn.explink.domain.OrderAddressRevise"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
 <%
    List<OrderAddressRevise> orderAddressRevises=request.getAttribute("orderAddressRevises")==null?new ArrayList<OrderAddressRevise>():(List<OrderAddressRevise>)request.getAttribute("orderAddressRevises");
    %>

<head>
</head>

<div id="box_bg" ></div>
<div id="box_contant" >
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>
		订单相关信息修改详情</h1>
		<div style="padding:10px; background:#FFF">		 
	
    <div class="tabbox" style="width:900px;overflow-y:scroll;padding-right:15px;height:400px" >
    
        <li>
        <div class="right_title">
        	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" >
        		<tbody>
        			<tr class="font_1" height="30">
        				<td valign="middle"  align="center" width="10%" align="center" bgcolor="#e7f4e3">订单号</td>
											<td valign="middle"  align="center" width="10%" align="center" bgcolor="#e7f4e3">收件人姓名修改信息</td>
											<td valign="middle"  align="center" width="10%" align="center" bgcolor="#e7f4e3">收件人电话修改信息</td>
											<td valign="middle" align="center"  width="10%"   align="center" bgcolor="#e7f4e3">收件人地址修改信息</td>
											<td valign="middle" align="center"  width="10%"   align="center" bgcolor="#e7f4e3">配送站点修改信息</td>
											<td valign="middle" align="center"  width="10%"   align="center" bgcolor="#e7f4e3">小件员修改信息</td>
											<td valign="middle" align="center"  width="10%"   align="center" bgcolor="#e7f4e3">电商要求修改信息</td>
											<td valign="middle"  align="center" width="10%" bgcolor="#e7f4e3">修改人</td>
											<td valign="middle"  align="center" width="10%" bgcolor="#e7f4e3">修改时间</td>
        				</tr>
                         <tr height="30">
                         	<%for(OrderAddressRevise data:orderAddressRevises){
									%>
                      <td   valign="middle" align="center"  ><%=data.getCwb() %></td>
									<td   valign="middle" align="center"  ><strong><%=data.getReceivemen()==null?"":data.getReceivemen() %></strong></td>
									<td   valign="middle" align="center"  ><strong><%=data.getPhone()==null?"":data.getPhone() %></strong></td>
									<td   valign="middle"  align="center" ><textarea readonly="readonly"><%=data.getAddress()%></textarea></td>
									<td   valign="middle"  align="center" ><strong><%=data.getDestination()==null?"":data.getDestination() %></strong></td>
									<td   valign="middle"  align="center" ><strong><%=data.getExceldeliver()==null?"":data.getExceldeliver() %></strong></td>
									<td   valign="middle"  align="center" ><strong><%=data.getCustomerrequest()==null?"":data.getCustomerrequest() %></strong></td>
									<td   valign="middle"  align="center" ><strong><%=data.getModifiername() %></strong></td>
									<td   valign="middle"  align="center" ><strong><%=data.getRevisetime() %></strong></td>
                            </tr>
                     		<%} %>
                        </tbody>
                    </table>
            </div>
       </li>
          
         
         
	</div>
		</div>
	</div>
</div>


