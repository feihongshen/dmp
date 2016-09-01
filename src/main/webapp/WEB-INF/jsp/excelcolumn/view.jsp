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
columnKeyAndValue[10]="J	";
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
			查看设置
		</h1>
		<div id="box_form"  style="width: 800px">
			<table width="750px" border="0" cellspacing="10" cellpadding="0">
				<tr class="table_6">
					<td>
						<p><span><font color ="red">供货商：</font></span><font color ="red">${name.customername}</font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getCwbindex()].equals("未设置")?"":"color = 'red'" %>>条码号/订单号：</font></span><font <%=columnKeyAndValue[columnList.getCwbindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getCwbindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getCommonnumberindex()].equals("未设置")?"":"color = 'red'" %>>承运商编号：</font></span><font <%=columnKeyAndValue[columnList.getCommonnumberindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getCommonnumberindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getTranscwbindex()].equals("未设置")?"":"color = 'red'" %>>运单号：</font></span><font <%=columnKeyAndValue[columnList.getTranscwbindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getTranscwbindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getCwbordertypeindex()].equals("未设置")?"":"color = 'red'" %>>订单类型：</font></span><font <%=columnKeyAndValue[columnList.getCwbordertypeindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getCwbordertypeindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getConsigneenameindex()].equals("未设置")?"":"color = 'red'" %>>收件人姓名：</font></span><font <%=columnKeyAndValue[columnList.getConsigneenameindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getConsigneenameindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getConsigneeaddressindex()].equals("未设置")?"":"color = 'red'" %>>收件人地址：</font></span><font <%=columnKeyAndValue[columnList.getConsigneeaddressindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getConsigneeaddressindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getConsigneepostcodeindex()].equals("未设置")?"":"color = 'red'" %>>邮编：</font></span><font <%=columnKeyAndValue[columnList.getConsigneepostcodeindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getConsigneepostcodeindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getConsigneephoneindex()].equals("未设置")?"":"color = 'red'" %>>电话：</font></span><font <%=columnKeyAndValue[columnList.getConsigneephoneindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getConsigneephoneindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getConsigneemobileindex()].equals("未设置")?"":"color = 'red'" %>>手机：</font></span><font <%=columnKeyAndValue[columnList.getConsigneemobileindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getConsigneemobileindex()] %></font></p>
						<p><span><font color = "red">获取手机：</font></span><font color = "red"><%=columnList.getGetmobileflag()==0?"否":"是" %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getCargoamountindex()].equals("未设置")?"":"color = 'red'" %>>货物金额：</font></span><font <%=columnKeyAndValue[columnList.getCargoamountindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getCargoamountindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getReceivablefeeindex()].equals("未设置")?"":"color = 'red'" %>>应收金额：</font></span><font <%=columnKeyAndValue[columnList.getReceivablefeeindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getReceivablefeeindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getPaybackfeeindex()].equals("未设置")?"":"color = 'red'" %>>应退金额：</font></span><font <%=columnKeyAndValue[columnList.getPaybackfeeindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getPaybackfeeindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getReturnnoindex()].equals("未设置")?"":"color = 'red'" %>>退货号：</font></span><font <%=columnKeyAndValue[columnList.getReturnnoindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getReturnnoindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getReturnaddressindex()].equals("未设置")?"":"color = 'red'" %>>退货地址：</font></span><font <%=columnKeyAndValue[columnList.getReturnaddressindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getReturnaddressindex()] %></font></p>
						<div style="height: 60px;"></div>
					
					</td>
					<td  >
						<p><span><font <%=columnKeyAndValue[columnList.getCargorealweightindex()].equals("未设置")?"":"color = 'red'" %>>实际重量kg：</font></span><font <%=columnKeyAndValue[columnList.getCargorealweightindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getCargorealweightindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getCargosizeindex()].equals("未设置")?"":"color = 'red'" %>>货物尺寸：</font></span><font <%=columnKeyAndValue[columnList.getCargosizeindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getCargosizeindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getSendcargonameindex()].equals("未设置")?"":"color = 'red'" %>>发出商品：</font></span><font <%=columnKeyAndValue[columnList.getSendcargonameindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getSendcargonameindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getBackcargonameindex()].equals("未设置")?"":"color = 'red'" %>>取回商品：</font></span><font <%=columnKeyAndValue[columnList.getBackcargonameindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getBackcargonameindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getCwbremarkindex()].equals("未设置")?"":"color = 'red'" %>>备注信息：</font></span><font <%=columnKeyAndValue[columnList.getCwbremarkindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getCwbremarkindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getExceldeliverindex()].equals("未设置")?"":"color = 'red'" %>>指定小件员：</font></span><font <%=columnKeyAndValue[columnList.getExceldeliverindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getExceldeliverindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getExcelbranchindex()].equals("未设置")?"":"color = 'red'" %>>指定派送分站：</font></span><font <%=columnKeyAndValue[columnList.getExcelbranchindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getExcelbranchindex()] %></font></p>
						
						<p><span><font <%=columnKeyAndValue[columnList.getConsigneenoindex()].equals("未设置")?"":"color = 'red'" %>>收件人编号：</font></span><font <%=columnKeyAndValue[columnList.getConsigneenoindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getConsigneenoindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getCustomercommandindex()].equals("未设置")?"":"color = 'red'" %>>客户要求：</font></span><font <%=columnKeyAndValue[columnList.getCustomercommandindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getCustomercommandindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getBackcargoamountindex()].equals("未设置")?"":"color = 'red'" %>>取回商品金额：</font></span><font <%=columnKeyAndValue[columnList.getBackcargoamountindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getBackcargoamountindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getDestinationindex()].equals("未设置")?"":"color = 'red'" %>>目的地：</font></span><font <%=columnKeyAndValue[columnList.getDestinationindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getDestinationindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getSendcargonumindex()].equals("未设置")?"":"color = 'red'" %>>发货数量：</font></span><font <%=columnKeyAndValue[columnList.getSendcargonumindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getSendcargonumindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getBackcargonumindex()].equals("未设置")?"":"color = 'red'" %>>取货数量：</font></span><font <%=columnKeyAndValue[columnList.getBackcargonumindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getBackcargonumindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getTranswayindex()].equals("未设置")?"":"color = 'red'" %>>运输方式：</font></span><font <%=columnKeyAndValue[columnList.getTranswayindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getTranswayindex()] %></font></p>
							<div style="height: 100px;"></div>
					</td>
					<td>
						<p><span><font <%=columnKeyAndValue[columnList.getCwbprovinceindex()].equals("未设置")?"":"color = 'red'" %>>省：</font></span><font <%=columnKeyAndValue[columnList.getCwbprovinceindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getCwbprovinceindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getCwbcityindex()].equals("未设置")?"":"color = 'red'" %>>城市：</font></span><font <%=columnKeyAndValue[columnList.getCwbcityindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getCwbcityindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getCwbcountyindex()].equals("未设置")?"":"color = 'red'" %>>区县：</font></span><font <%=columnKeyAndValue[columnList.getCwbcountyindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getCwbcountyindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getWarehousenameindex()].equals("未设置")?"":"color = 'red'" %>>发货仓库：</font></span><font <%=columnKeyAndValue[columnList.getWarehousenameindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getWarehousenameindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getShipcwbindex()].equals("未设置")?"":"color = 'red'" %>>供货商运单号：</font></span><font <%=columnKeyAndValue[columnList.getShipcwbindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getShipcwbindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getCargotypeindex()].equals("未设置")?"":"color = 'red'" %>>货物类型：</font></span><font <%=columnKeyAndValue[columnList.getCargotypeindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getCargotypeindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getCwbdelivertypeindex()].equals("未设置")?"":"color = 'red'" %>>派送类型：</font></span><font <%=columnKeyAndValue[columnList.getCwbdelivertypeindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getCwbdelivertypeindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getEmaildateindex()].equals("未设置")?"":"color = 'red'" %>>发货时间：</font></span><font <%=columnKeyAndValue[columnList.getEmaildateindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getEmaildateindex()] %></font></p>
						<p><span><font color = "red" >修改时间：</font></span><font color = "red" ><%=columnList.getUpdatetime() %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getAccountareaindex()].equals("未设置")?"":"color = 'red'" %>>结算区域：</font></span><font <%=columnKeyAndValue[columnList.getAccountareaindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getAccountareaindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getShouldfareindex()].equals("未设置")?"":"color = 'red'" %>>应收运费：</font></span><font <%=columnKeyAndValue[columnList.getShouldfareindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getShouldfareindex()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getModelnameindex()].equals("未设置")?"":"color = 'red'" %>>面单模版名称：</font></span><font <%=columnKeyAndValue[columnList.getModelnameindex()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getModelnameindex()] %></font></p>

						<p><span><font <%=columnKeyAndValue[columnList.getRemark1index()].equals("未设置")?"":"color = 'red'" %>>自定义1：</font></span><font <%=columnKeyAndValue[columnList.getRemark1index()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getRemark1index()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getRemark2index()].equals("未设置")?"":"color = 'red'" %>>自定义2：</font></span><font <%=columnKeyAndValue[columnList.getRemark2index()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getRemark2index()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getRemark3index()].equals("未设置")?"":"color = 'red'" %>>自定义3：</font></span><font <%=columnKeyAndValue[columnList.getRemark3index()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getRemark3index()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getRemark4index()].equals("未设置")?"":"color = 'red'" %>>自定义4：</font></span><font <%=columnKeyAndValue[columnList.getRemark4index()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getRemark4index()] %></font></p>
						<p><span><font <%=columnKeyAndValue[columnList.getRemark5index()].equals("未设置")?"":"color = 'red'" %>>自定义5：</font></span><font <%=columnKeyAndValue[columnList.getRemark5index()].equals("未设置")?"":"color = 'red'" %>><%=columnKeyAndValue[columnList.getRemark5index()] %></font></p>
					</td>
				</tr>
			</table>
			<div align="center">
				<input type="submit" value="关闭" onclick="closeBox()" class="input_button2" />
			</div>
		</div>
	</div>
</div>
<div id="box_yy"></div>
