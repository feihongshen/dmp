<%@page import="cn.explink.domain.ExcelColumnSet"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    ExcelColumnSet columnList = (ExcelColumnSet)request.getAttribute("e");
%>
<script>
var initEditArray = new Array();
initEditArray[0]=("<%=columnList.getCwbindex() %>,cwbindex");
initEditArray[1]=("<%=columnList.getConsigneenameindex() %>,consigneenameindex");
initEditArray[2]=("<%=columnList.getConsigneeaddressindex() %>,consigneeaddressindex");
initEditArray[3]=("<%=columnList.getConsigneepostcodeindex() %>,consigneepostcodeindex");
initEditArray[4]=("<%=columnList.getConsigneephoneindex() %>,consigneephoneindex");
initEditArray[5]=("<%=columnList.getConsigneemobileindex() %>,consigneemobileindex");
initEditArray[6]=("<%=columnList.getCwbremarkindex() %>,cwbremarkindex");
initEditArray[7]=("<%=columnList.getSendcargonameindex() %>,sendcargonameindex");
initEditArray[8]=("<%=columnList.getCargorealweightindex() %>,cargorealweightindex");
initEditArray[9]=("<%=columnList.getReceivablefeeindex() %>,receivablefeeindex");
initEditArray[10]=("<%=columnList.getPaybackfeeindex() %>,paybackfeeindex");
initEditArray[11]=("<%=columnList.getAccountareaindex() %>,accountareaindex");
initEditArray[12]=("<%=columnList.getExceldeliverindex() %>,exceldeliverindex");
initEditArray[13]=("<%=columnList.getExcelbranchindex() %>,excelbranchindex");
initEditArray[14]=("<%=columnList.getShipcwbindex() %>,shipcwbindex");
initEditArray[15]=("<%=columnList.getConsigneenoindex() %>,consigneenoindex");
initEditArray[16]=("<%=columnList.getCargoamountindex() %>,cargoamountindex");
initEditArray[17]=("<%=columnList.getCustomercommandindex() %>,customercommandindex");
initEditArray[18]=("<%=columnList.getCargotypeindex() %>,cargotypeindex");
initEditArray[19]=("<%=columnList.getTranscwbindex() %>,transcwbindex");
initEditArray[20]=("<%=columnList.getCargosizeindex() %>,cargosizeindex");
initEditArray[21]=("<%=columnList.getBackcargoamountindex() %>,backcargoamountindex");
initEditArray[22]=("<%=columnList.getDestinationindex() %>,destinationindex");
initEditArray[23]=("<%=columnList.getTranswayindex() %>,transwayindex");
initEditArray[24]=("<%=columnList.getCommonnumberindex() %>,commonnumberindex");
initEditArray[25]=("<%=columnList.getSendcargonumindex() %>,sendcargonumindex");
initEditArray[26]=("<%=columnList.getBackcargonumindex() %>,backcargonumindex");
initEditArray[27]=("<%=columnList.getCwbprovinceindex() %>,cwbprovinceindex");
initEditArray[28]=("<%=columnList.getCwbcityindex() %>,cwbcityindex");
initEditArray[29]=("<%=columnList.getCwbcountyindex() %>,cwbcountyindex");
initEditArray[30]=("<%=columnList.getWarehousenameindex() %>,warehousenameindex");
initEditArray[31]=("<%=columnList.getCwbordertypeindex() %>,cwbordertypeindex");
initEditArray[32]=("<%=columnList.getCwbdelivertypeindex() %>,cwbdelivertypeindex");

initEditArray[33]=("<%=columnList.getRemark1index() %>,remark1index");
initEditArray[34]=("<%=columnList.getRemark2index() %>,remark2index");
initEditArray[35]=("<%=columnList.getRemark3index() %>,remark3index");
initEditArray[36]=("<%=columnList.getRemark4index() %>,remark4index");
initEditArray[37]=("<%=columnList.getRemark5index() %>,remark5index");

initEditArray[38]=("<%=columnList.getPaywayindex() %>,paywayindex");
initEditArray[39]=("<%=columnList.getBackcargonameindex() %>,backcargonameindex");
initEditArray[40]=("<%=columnList.getModelnameindex() %>,modelnameindex");
initEditArray[41]=("<%=columnList.getEmaildateindex() %>,emaildateindex");
initEditArray[42]=("<%=columnList.getShouldfareindex() %>,shouldfareindex");
</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改设置</h1>
		<form id="excelcolumn_save_Form" name="excelcolumn_save_Form" method="POST" action="<%=request.getContextPath()%>/excelcolumn/save/${e.columnid }" onSubmit="if(check_excelcolumn()){submitSaveForm(this);}return false;" >
		<div id="box_form" style="width: 800px" >
			<div class="gysselect"><span>供货商选择：</span>
					${name.customername}</div>
			<table width="100%" border="0" cellspacing="10" cellpadding="0">
				<tr>
					<td>	
					<input type="hidden" name="columnid" value="${e.columnid }">
					<input type="hidden" name="customerid" id="customerid" value="${name.customerid }-${name.isAutoProductcwb }">
					
					<p><span>条码号/订单号：</span>
					<select id="cwbindex" name="cwbindex">
						 <option value="0" >未设置</option>
					     </select>*</p>
					<p><span>承运商编号：</span>
					<select id="commonnumberindex" name="commonnumberindex">
						<option value="0" selected>未设置</option>
			        </select></p>
			        <p><span>运单号：</span> 
					<select id="transcwbindex" name="transcwbindex">
						<option value="0" selected>未设置</option>
			        </select></p>
			         <p><span>订单类型：</span>
					<select id="cwbordertypeindex" name="cwbordertypeindex">
						<option value="0" selected>未设置</option>
			        </select> *</p>
					<p><span>收件人姓名：</span>
					<select id="consigneenameindex" name="consigneenameindex">
						<option value="0" selected>未设置</option>
			        </select> </p>
					<p><span>收件人地址：</span>
					<select id="consigneeaddressindex" name="consigneeaddressindex">
						<option value="0" selected>未设置</option>
			        </select> *</p>
			        <p><span>邮编：</span>
					<select id="consigneepostcodeindex" name="consigneepostcodeindex">
						<option value="0" selected>未设置</option>
			        </select></p>
			         <p><span>电话：</span>
					<select id="consigneephoneindex" name="consigneephoneindex">
						<option value="0" selected>未设置</option>
			        </select></p>
			        <p><span>手机：</span>
					<select id="consigneemobileindex" name="consigneemobileindex">
						<option value="0" selected>未设置</option>
			        </select></p>
		        	<p><span>获取手机：</span>
			         	<input type="radio" name="getmobileflag" value="0" <%=columnList.getGetmobileflag()==0?"checked=\"checked\"":"" %> />否　
						<input type="radio"  name="getmobileflag" value="1" <%=columnList.getGetmobileflag()==1?"checked=\"checked\"":"" %> />是
					</p>
					<p><span>货物金额：</span>
					<select id="cargoamountindex" name="cargoamountindex">
						<option value="0" selected>未设置</option>
			        </select> *</p>
			        <p><span>应收金额：</span>
					<select id=receivablefeeindex name="receivablefeeindex">
						<option value="0" selected>未设置</option>
			        </select></p>
			       <p><span>应退金额：</span>
					<select id="paybackfeeindex" name="paybackfeeindex">
						<option value="0" selected>未设置</option>
			        </select></p>
			         <p><span>支付方式：</span> 
					<select id="paywayindex" name="paywayindex">
						<option value="0" selected>未设置</option>
			        </select></p>
			        <div style="height: 60px;"></div>
			        
			        
			        
			        </td>
			        <td>
			        <p><span>实际重量kg：</span> 
					<select id="cargorealweightindex" name="cargorealweightindex">
						<option value="0" selected>未设置</option>
			        </select></p>
			        <p><span>货物尺寸：</span>
					<select id="cargosizeindex" name="cargosizeindex">
						<option value="0" selected>未设置</option>
			        </select></p>
			        <p><span>发出商品：</span>
					<select id="sendcargonameindex" name="sendcargonameindex">
						<option value="0" selected>未设置</option>
			        </select></p>
			        <p><span>取回商品：</span>
					<select id="backcargonameindex" name="backcargonameindex">
						<option value="0" selected>未设置</option>
			        </select></p>
			        <p><span>备注信息：</span>
					<select id="cwbremarkindex" name="cwbremarkindex">
						<option value="0" selected>未设置</option>
			        </select></p>
		          	<p><span>指定小件员：</span>
					<select id="exceldeliverindex" name="exceldeliverindex">
						<option value="0" selected>未设置</option>
			        </select></p>
		          	<p><span>指定派送分站：</span>
					<select id="excelbranchindex" name="excelbranchindex">
						<option value="0" selected>未设置</option>
			        </select></p>
		         	
			       
		          	<p><span>收件人编号：</span>
					<select id="consigneenoindex" name="consigneenoindex">
						<option value="0" selected>未设置</option>
			        </select></p>
		          	
		         	<p><span>客户要求：</span> 
					<select id="customercommandindex" name="customercommandindex">
						<option value="0" selected>未设置</option>
			        </select></p>
			         
		          	<p><span>取回商品金额：</span>
					<select id="backcargoamountindex" name="backcargoamountindex">
						<option value="0" selected>未设置</option>
			        </select></p>
		          	<p><span>目的地：</span>
					<select id="destinationindex" name="destinationindex">
						<option value="0" selected>未设置</option>
			        </select></p>
		          	<p><span>发货数量：</span>
					<select id="sendcargonumindex" name="sendcargonumindex">
						<option value="0" selected>未设置</option>
			        </select></p>
		         	<p><span>取货数量：</span>
					<select id="backcargonumindex" name="backcargonumindex">
						<option value="0" selected>未设置</option>
			        </select></p>
			        <p><span>运输方式：</span>
					<select id="transwayindex" name="transwayindex">
						<option value="0" selected>未设置</option>
			        </select></p>
			        <div style="height: 65px;"></div>
			        
			       	</td>
					<td>
					
					
		         	
		         	<p><span>省：</span> 
					<select id="cwbprovinceindex" name="cwbprovinceindex">
						<option value="0" selected>未设置</option>
			        </select></p>
		          	<p><span>城市：</span>
					<select id="cwbcityindex" name="cwbcityindex">
						<option value="0" selected>未设置</option>
			        </select></p>
		          	<p><span>区县：</span>
					<select id="cwbcountyindex" name="cwbcountyindex">
						<option value="0" selected>未设置</option>
			        </select></p>
		          	<p><span>发货仓库：</span>
					<select id="warehousenameindex" name="warehousenameindex">
						<option value="0" selected>未设置</option>
			        </select></p>
		          	<p><span>供货商运单号：</span> 
					<select id="shipcwbindex" name="shipcwbindex">
						<option value="0" selected>未设置</option>
			        </select></p>
			        <p><span>货物类型：</span> 
					<select id="cargotypeindex" name="cargotypeindex">
						<option value="0" selected>未设置</option>
			        </select></p>
		          	<p><span>派送类型：</span>
					<select id="cwbdelivertypeindex" name="cwbdelivertypeindex">
						<option value="0" selected>未设置</option>
			        </select></p>
		          	<p><span>发货时间：</span>
					<select id="emaildateindex" name="emaildateindex">
						<option value="0" selected>未设置</option>
			        </select></p>
		         	<p style ="display:none;"><span>修改时间：</span>
					<select id="updatetime" name="updatetime">
						<option value="0" selected>未设置</option>
			        </select></p>
		         	<p><span>结算区域：</span>
					<select id="accountareaindex" name="accountareaindex">
						<option value="0" selected>未设置</option>
			        </select></p>
			         <p><span>应收运费：</span>
						<select id="shouldfareindex" name="shouldfareindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			        
			       <p><span>面单模版名称：</span>
						<select id="modelnameindex" name="modelnameindex">
							<option value="0" selected>未设置</option>
				        </select></p>
				    
				    <p><span>自定义1：</span>
					<select id="remark1index" name="remark1index">
						<option value="0" selected>未设置</option>
			        </select></p>
			        <p><span>自定义2：</span>
					<select id="remark2index" name="remark2index">
						<option value="0" selected>未设置</option>
			        </select></p>
			        <p><span>自定义3：</span>
					<select id="remark3index" name="remark3index">
						<option value="0" selected>未设置</option>
			        </select></p>
			        <p><span>自定义4：</span>
					<select id="remark4index" name="remark4index">
						<option value="0" selected>未设置</option>
			        </select></p>
			        <p><span>自定义5：</span>
					<select id="remark5index" name="remark5index">
						<option value="0" selected>未设置</option>
			        </select></p>
			       </td>
				</tr>
			   </table>
		</div>
		<div align="center"><input type="submit" value="保存" class="button" /><br/><br/><font id="errorState" color="red"></font></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>

