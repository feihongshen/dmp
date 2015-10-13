<%@page import="cn.explink.domain.ExcelColumnSet"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    ExcelColumnSet columnList = (ExcelColumnSet)request.getAttribute("customerid");
String columnKeyAndValue[] = new String[53];
columnKeyAndValue[0]="未设置";
columnKeyAndValue[1]="A";
columnKeyAndValue[2]="B";
columnKeyAndValue[3]="C";
columnKeyAndValue[4]="D";
columnKeyAndValue[5]="E";
columnKeyAndValue[6]="F";
columnKeyAndValue[7]="G";
columnKeyAndValue[8]="H";
columnKeyAndValue[9]="I";
columnKeyAndValue[10]="J";
columnKeyAndValue[11]="K";
columnKeyAndValue[12]="L";
columnKeyAndValue[13]="M";
columnKeyAndValue[14]="N";
columnKeyAndValue[15]="O";
columnKeyAndValue[16]="P";
columnKeyAndValue[17]="Q";
columnKeyAndValue[18]="R";
columnKeyAndValue[19]="S";
columnKeyAndValue[20]="T";
columnKeyAndValue[21]="U";
columnKeyAndValue[22]="V";
columnKeyAndValue[23]="W";
columnKeyAndValue[24]="X";
columnKeyAndValue[25]="Y";
columnKeyAndValue[26]="Z";
for (int y=1 ; y<27 ;y++){
	columnKeyAndValue[26+y] = "A" + columnKeyAndValue[y];
}
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1>
			<div id="close_box" onclick="closeBox()"></div>
			修改设置
		</h1>
		<div id="box_form">
			<table width="100%" border="0" cellspacing="10" cellpadding="0">
				<tr>
					<td>
						<p><span><font color ="red">供货商：</font></span><font color ="red">${name.customername}</font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getCwbindex()] != "未设置") {%> color="red" <%} %>>条码号/订单号：</font> </span><font <%if(columnKeyAndValue[columnList.getCwbindex()] != "未设置"){%> color ="red" <%} %>><%=columnKeyAndValue[columnList.getCwbindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getCommonnumberindex()] != "未设置") {%> color="red" <%} %>>承运商编号：</font></span><font <%if(columnKeyAndValue[columnList.getCommonnumberindex()] != "未设置") {%> color ="red" <%} %>><%=columnKeyAndValue[columnList.getCommonnumberindex()] %></font> </p>
						<p><span><font <%if(columnKeyAndValue[columnList.getTranscwbindex()] != "未设置") {%> color="red" <%} %>>发货单号：</font></span>  <font <%if(columnKeyAndValue[columnList.getTranscwbindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getTranscwbindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getConsigneenameindex()] != "未设置") {%> color="red" <%} %>>收件人姓名：</font></span> <font <%if(columnKeyAndValue[columnList.getConsigneenameindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getConsigneenameindex()] %></font> </p>
						<p><span><font <%if(columnKeyAndValue[columnList.getConsigneeaddressindex()] != "未设置") {%> color="red" <%} %>>收件人地址：</font></span><font <%if(columnKeyAndValue[columnList.getConsigneeaddressindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getConsigneeaddressindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getConsigneepostcodeindex()] != "未设置") {%> color="red" <%} %>> 邮编：</font></span><font <%if(columnKeyAndValue[columnList.getConsigneepostcodeindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getConsigneepostcodeindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getConsigneephoneindex()] != "未设置") {%> color="red" <%} %>>电话：</font></span><font <%if(columnKeyAndValue[columnList.getConsigneephoneindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getConsigneephoneindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getConsigneemobileindex()] != "未设置") {%> color="red" <%} %>>手机：</font></span><font <%if(columnKeyAndValue[columnList.getConsigneemobileindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getConsigneemobileindex()] %></font></p>
						<p><span><font color ="red">获取手机：</font></span><font color ="red"><%=columnList.getGetmobileflag()==0?"否":"是" %></font> </p>
						<p><span><font <%if(columnKeyAndValue[columnList.getSendcargonameindex()] != "未设置") {%> color="red" <%} %>>发出商品：</font></span><font <%if(columnKeyAndValue[columnList.getSendcargonameindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getSendcargonameindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getBackcargonameindex()] != "未设置") {%> color="red" <%} %>>取回商品：</font></span><font <%if(columnKeyAndValue[columnList.getBackcargonameindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getBackcargonameindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getCargorealweightindex()] != "未设置") {%> color="red" <%} %>>实际重量kg：</font></span><font <%if(columnKeyAndValue[columnList.getCargorealweightindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getCargorealweightindex()] %></font></p>
					</td>
					<td>
						<p><span><font <%if(columnKeyAndValue[columnList.getReceivablefeeindex()] != "未设置") {%> color="red" <%} %>>应收金额：</font></span><font <%if(columnKeyAndValue[columnList.getReceivablefeeindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getReceivablefeeindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getPaybackfeeindex()] != "未设置") {%> color="red" <%} %>>应退金额：</font></span><font <%if(columnKeyAndValue[columnList.getPaybackfeeindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getPaybackfeeindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getCwbremarkindex()] != "未设置") {%> color="red" <%} %>>备注信息：</font></span><font <%if(columnKeyAndValue[columnList.getCwbremarkindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getCwbremarkindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getExceldeliverindex()] != "未设置") {%> color="red" <%} %>>指定小件员：</font></span><font <%if(columnKeyAndValue[columnList.getExceldeliverindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getExceldeliverindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getExcelbranchindex()] != "未设置") {%> color="red" <%} %>>指定派送分站：</font></span><font <%if(columnKeyAndValue[columnList.getExcelbranchindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getExcelbranchindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getShipcwbindex()] != "未设置") {%> color="red" <%} %>>配送单号：</font></span><font <%if(columnKeyAndValue[columnList.getShipcwbindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getShipcwbindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getConsigneenoindex()] != "未设置") {%> color="red" <%} %>>收件人编号：</font></span><font <%if(columnKeyAndValue[columnList.getConsigneenoindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getConsigneenoindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getCargoamountindex()] != "未设置") {%> color="red" <%} %>>货物金额：</font></span><font <%if(columnKeyAndValue[columnList.getCargoamountindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getCargoamountindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getCustomercommandindex()] != "未设置") {%> color="red" <%} %>>客户要求：</font></span><font <%if(columnKeyAndValue[columnList.getCustomercommandindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getCustomercommandindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getCargotypeindex()] != "未设置") {%> color="red" <%} %>>货物类型：</font></span><font <%if(columnKeyAndValue[columnList.getCargotypeindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getCargotypeindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getCargosizeindex()] != "未设置") {%> color="red" <%} %>>货物尺寸：</font></span><font <%if(columnKeyAndValue[columnList.getCargosizeindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getCargosizeindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getBackcargoamountindex()] != "未设置") {%> color="red" <%} %>>取回商品金额：</font></span><font <%if(columnKeyAndValue[columnList.getBackcargoamountindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getBackcargoamountindex()] %></font></p>
						
						

					</td>
					<td>
						<p><span><font <%if(columnKeyAndValue[columnList.getDestinationindex()] != "未设置") {%> color="red" <%} %>>目的地：</font></span><font <%if(columnKeyAndValue[columnList.getDestinationindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getDestinationindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getTranswayindex()] != "未设置") {%> color="red" <%} %>>运输方式：</font></span><font <%if(columnKeyAndValue[columnList.getTranswayindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getTranswayindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getSendcargonumindex()] != "未设置") {%> color="red" <%} %>>发货数量：</font></span><font <%if(columnKeyAndValue[columnList.getSendcargonumindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getSendcargonumindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getBackcargonumindex()] != "未设置") {%> color="red" <%} %>>取货数量：</font></span> <font <%if(columnKeyAndValue[columnList.getBackcargonumindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getBackcargonumindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getCwbprovinceindex()] != "未设置") {%> color="red" <%} %>>省：</font></span><font <%if(columnKeyAndValue[columnList.getCwbprovinceindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getCwbprovinceindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getCwbcityindex()] != "未设置") {%> color="red" <%} %>>城市：</font></span><font <%if(columnKeyAndValue[columnList.getCwbcityindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getCwbcityindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getCwbcountyindex()] != "未设置") {%> color="red" <%} %>>区县：</font></span><font <%if(columnKeyAndValue[columnList.getCwbcountyindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getCwbcountyindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getWarehousenameindex()] != "未设置") {%> color="red" <%} %>>发货仓库：</font></span><font <%if(columnKeyAndValue[columnList.getWarehousenameindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getWarehousenameindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getCwbordertypeindex()] != "未设置") {%> color="red" <%} %>>订单类型：</font></span><font <%if(columnKeyAndValue[columnList.getCwbordertypeindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getCwbordertypeindex()] %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getCwbdelivertypeindex()] != "未设置") {%> color="red" <%} %>>派送类型：</font></span><font <%if(columnKeyAndValue[columnList.getCwbdelivertypeindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getCwbdelivertypeindex()] %></font></p>
						
						<%-- <p><span>发货时间：</span><%=columnKeyAndValue[columnList.getEmaildateindex()] %></p> --%>
						<p><span><font color="red" >修改时间：</font></span><font color ="red"><%=columnList.getUpdatetime() %></font></p>
						<p><span><font <%if(columnKeyAndValue[columnList.getAccountareaindex()] != "未设置") {%> color="red" <%} %>>结算区域：</font></span><font <%if(columnKeyAndValue[columnList.getAccountareaindex()] != "未设置") {%> color="red" <%} %>><%=columnKeyAndValue[columnList.getAccountareaindex()] %></font></p>

					</td>
				</tr>
			</table>
			<div align="center">
				<input type="submit" value="关闭" onclick="closeBox()" />
			</div>
		</div>
	</div>
</div>
<div id="box_yy"></div>
