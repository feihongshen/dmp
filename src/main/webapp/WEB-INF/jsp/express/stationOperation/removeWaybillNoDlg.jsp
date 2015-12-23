<%@page import="cn.explink.util.StringUtil"%>
<%@ page language="java" import="java.util.*,cn.explink.domain.express.CwbOrderForCombine"
	pageEncoding="UTF-8"%>
<%
	String packageNo = (String) request.getAttribute("packageNo");
	Long nextBranch = (Long) request.getAttribute("nextBranch");
	Integer provinceId = (Integer) request.getAttribute("provinceId");
	String selectedCities = (String) request.getAttribute("selectedCities");
	String waybillNos = (String) request.getAttribute("waybillNos");
	Integer waybillTotalCount = (Integer) request.getAttribute("waybillTotalCount");
	Long itemTotalCount = (Long) request.getAttribute("itemTotalCount");
	List<CwbOrderForCombine> selectedCwbOrderList = (List<CwbOrderForCombine>) request.getAttribute("selectedCwbOrderList");
%>
<!-- 弹出对话框-->
<div id="alert_box"></div>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1>
			<div id="close_box" onclick="closeDlgAndRefresh()"></div>
			移除
		</h1>
		<div id="box_form">
			<table>
				<tr>
					<td>包号</td>
					<td><span style="height: 30px; width: 200px; font-size: x-large; font-weight: bold;"><%=packageNo%></span>
					</td>
					<td>运单号</td>
					<td><input type="text" id="waybillNo"
						style="height: 30px; font-size: x-large; font-weight: bold;"
						onKeyDown="removeWaybillNo(event);" /></td>
				</tr>
			</table>
		</div>
		<div class="saomiao_box2" style="width: 1000px">
			<div>
				<table width="100%" border="0" cellspacing="10" cellpadding="0" style="margin-top: 10px">
					<tbody>
						<tr>
							<td width="10%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
									<tr>
										<td width="100" align="center" bgcolor="#f1f1f1">运单号</td>
										<td width="50" align="center" bgcolor="#f1f1f1">件数</td>
										<td width="50" align="center" bgcolor="#f1f1f1">小件员</td>
										<td width="70" align="center" bgcolor="#f1f1f1">揽件时间</td>
										<td width="50" align="center" bgcolor="#f1f1f1">付款方式</td>
										<td width="50" align="center" bgcolor="#f1f1f1">费用合计</td>
										<td width="50" align="center" bgcolor="#f1f1f1">运费</td>
										<td width="50" align="center" bgcolor="#f1f1f1">包装费用</td>
										<td width="50" align="center" bgcolor="#f1f1f1">保价费用</td>
										<td width="50" align="center" bgcolor="#f1f1f1">收件省份</td>
										<td width="50" align="center" bgcolor="#f1f1f1">收件城市</td>
									</tr>

								</table>
								<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
									<%
										if (selectedCwbOrderList != null && !selectedCwbOrderList.isEmpty())
											for (CwbOrderForCombine selectedCwbOrder : selectedCwbOrderList) {
									%>
									<tr>
										<td width="100" align="center"><%=selectedCwbOrder.getCwb()%></td>
										<td width="50"><%=selectedCwbOrder.getSendcarnum()%></td>
										<td width="50"><%=selectedCwbOrder.getCollectorname() == null ? "" : selectedCwbOrder.getCollectorname()%></td>
										<td width="70"><%=selectedCwbOrder.getInstationdatetime() == null ? "" : selectedCwbOrder.getInstationdatetime()%></td>
										<td width="50"><%=selectedCwbOrder.getPayMethodName()%></td>
										<td width="50"><%=selectedCwbOrder.getTotalfee() == null ? "" : selectedCwbOrder.getTotalfee()%></td>
										<td width="50"><%=selectedCwbOrder.getShouldfare() == null ? "" : selectedCwbOrder.getShouldfare()%></td>
										<td width="50"><%=selectedCwbOrder.getPackagefee() == null ? "" : selectedCwbOrder.getPackagefee()%></td>
										<td width="50"><%=selectedCwbOrder.getInsuredfee() == null ? "" : selectedCwbOrder.getInsuredfee()%></td>
										<td width="50"><%=selectedCwbOrder.getProvinceName() == null ? "" : selectedCwbOrder.getProvinceName()%></td>
										<td width="50"><%=selectedCwbOrder.getCityName() == null ? "" : selectedCwbOrder.getCityName()%></td>
									</tr>
									<%
										}
									%>
								</table>
						</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<div id="box_yy"></div>
<!-- 分配的ajax地址 -->
<input type="hidden" id="doRemove" value="<%=request.getContextPath()%>/stationOperation/doRemove" />
<!-- 根据运单号获取运单的ajax地址 -->
<input type="hidden" id="getExpressOrderByWaybillNo"
	value="<%=request.getContextPath()%>/stationOperation/getExpressOrderByWaybillNo" />
<!-- 移除的ajax地址 -->
<input type="hidden" id="remove"
	value="<%=request.getContextPath()%>/stationOperation/openRemoveDlg" />


<input type="hidden" id="packageNo" value="<%=packageNo%>" />
<input type="hidden" id="nextBranch" value="<%=nextBranch%>" />
<input type="hidden" id="provinceId" value="<%=provinceId%>" />
<input type="hidden" id="selectedCities" value="<%=selectedCities%>" />

<input type="hidden" id="waybillNos" value="<%=waybillNos%>" />
<input type="hidden" id="waybillTotalCount" value="<%=waybillTotalCount%>" />
<input type="hidden" id="itemTotalCount" value="<%=itemTotalCount%>" />
