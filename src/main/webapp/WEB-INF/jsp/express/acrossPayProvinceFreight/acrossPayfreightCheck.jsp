<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%@ include file="/WEB-INF/jsp/express/includeFile/include.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>跨省应付运费对账</title>
</head>

<body style="background:#eef9ff">

<div class="right_box">
	<div class="inputselect_box">
		<table style="width: 100%">
			    <tr>
				    <td>
					    <input class="input_button2" type="button" id="add_btn" value="新增"/>
					    <input class="input_button2" type="button" id="edit_btn"  value="查看/修改"/>
					    <input class="input_button2" type="button" id="delete_btn"  value="删除"/>
					    <input class="input_button2" type="button" id="query_btn"  value="查询"/>
			    	</td>
			    </tr>
		 </table>
	</div>
	
	
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<div class="right_title">
		<div style="overflow: auto;">
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="listTable">
				<tr>
					<td height="30px"  valign="middle"><!-- <input type="checkbox" name="checkAll" onclick="checkAll('listTable')"/>  --></td>
					<td align="center" valign="middle" style="font-weight: bold;"> 账单编号</td>
					<td align="center" valign="middle" style="font-weight: bold;"> 账单状态</td>
					<td align="center" valign="middle" style="font-weight: bold;"> 应收省份</td>
					<td align="center" valign="middle" style="font-weight: bold;"> 应付省份</td>
					<td align="center" valign="middle" style="font-weight: bold;"> 运费金额合计 </td>
					<td align="center" valign="middle" style="font-weight: bold;"> 代收货款合计 </td>
					<td align="center" valign="middle" style="font-weight: bold;"> 创建人 </td>
					<td align="center" valign="middle" style="font-weight: bold;"> 创建时间</td>
					<td align="center" valign="middle" style="font-weight: bold;"> 审核人</td>
					<td align="center" valign="middle" style="font-weight: bold;"> 审核日期</td>
					<td align="center" valign="middle" style="font-weight: bold;"> 备注</td>
				</tr>
				
				<c:forEach items="${billList}" var="list">
				<tr>
					<td height="30px" align="center"  valign="middle">
						<input type="checkbox" name="checkBox" value="${list.id}" extralval="${list.billState}" onclick="initColor(this,'listTable');"/>
					</td>
					<td align="center" valign="middle" > ${list.billNo}</td>
					<td align="center" valign="middle" > ${list.billStateStr}</td>
					<td align="center" valign="middle" > ${list.receProvinceName}</td><!--应收省份  -->
					<td align="center" valign="middle" > ${list.payProvinceName}</td><!--应付省份  -->
					<td align="center" valign="middle" > ${list.freight}</td>
					<td align="center" valign="middle" > ${list.cod}</td>
					<td align="center" valign="middle" > ${list.createTime}</td>
					<td align="center" valign="middle" > ${list.creatorName}</td>
					<td align="center" valign="middle" > ${list.auditorName}</td>
					<td align="center" valign="middle" > ${list.auditTime}</td>
					<td align="center" valign="middle" > ${list.remark}</td>
				</tr>
				</c:forEach>
			</table>
		</div>
	</div>
</div>
<!--查询  -->
<div>
	<div id="dlgQueryBox" class="easyui-dialog" style="width: 790px; height: 200px; top:100px;left:100px; padding: 10px 20px;z-index:400px;" closed="true" buttons="#btnsOfExpressQuery">
		<form id="queryBoxItemForm" method="post">
			<table>
				<tr>
					<td>账单编号</td>
					<td><input id="billNo" name="billNo" class="inp03"></input></td>
					<td width="50px;"></td>
					<td>账单状态</td>
					<td>
						<select id="billState" name="billState" class="sel_02">
							<option value="">全部</option>
							<option value="0">未审核</option>
							<option value="1">已审核</option>
							<option value="2">已核销</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>账单创建日期</td>
					<td>
						<input id="createBeginTime" name="createBeginTime" class="easyui-datebox" style="width: 100px;height: 25px;"></input>
						至
						<input id="createEndTime" name="createEndTime" class="easyui-datebox" style="width: 100px;height: 25px;"></input>
					</td>
					<td width="50px;"></td>
					<td>账单核销日期</td>
					<td>
						<input id="verifyBeginTime" name="verifyBeginTime" class="easyui-datebox" style="width: 100px;height: 25px;"></input>
						至
						<input id="verifyEndTime" name="verifyEndTime" class="easyui-datebox" style="width: 100px;height: 25px;"></input>
					</td>
				</tr>
				<tr>
					<td>应收省份</td>
					<td>
						<select id="receProvinceId" name="receProvinceId" class="sel_02">
						</select>
					</td>
					<td width="50px;"></td>
					<td>排序</td>
					<td>
						<select id="orderField" name="orderField" class="sel_03">
							<option value="1">账单编号</option>
							<option value="2">账单状态</option>
							<option value="3">账单创建日期</option>
							<option value="4">账单核销日期</option>
						</select>
						<select id="orderRule" name="orderRule" class="sel_04">
							<option value="asc">升序</option>
							<option value="desc">降序</option>
						</select>
					</td>
				</tr>
			</table>
		</form>
		<br/>
		<div align="center">
			<input type="button" class="input_button1" id="queryBtn" value="查询" />
			&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" class="input_button1" id="closeBtn" value="关闭" />
		</div>
	</div>
</div>

<!--编辑  -->
<div>
	<div id="dlgEditBox" class="easyui-dialog" style="width: 880px; height: 630px; top:100px;left:100px; padding: 10px 20px;z-index:400px;" closed="true" buttons="#btnsOfExpressEdit">
		<div id="editScanContainer"></div>
	</div>
</div>

<!--新增  -->
<div>
	<div id="dlgAddBox" class="easyui-dialog" style="width: 1024px; height: 260px; top:100px;left:100px; padding: 10px 20px;z-index:400px;" closed="true" buttons="#btnsOfExpressEdit">
		<div style="margin-left: 35px;">
			<input type="button" class="input_button1" id="backBtn" value="返回" />
			<input type="button" class="input_button1" id="createBtn" value="创建" />
		</div>
		<form id="addForm" action="" method="post" >
			<input type="hidden" id="payMethod4Create" value="2" /><!-- 到付 -->
			<!-- 应付省份id -->
			<input type="hidden" id="payProvince4Create" name="payProvince4Create" value="${userInfo.provinceId }"/>
			<div style="margin-top: 10px;">
				<table border="0px;">
					<tr>
						<td class="maxtd1" align="right">账单编号</td>
						<td class="maxtd"><input value="jjyy1234" class="inp01" disabled="disabled" id="billNo4Create" name="billNo4Create" /></td>
						
						<!-- <td class="maxtd"></td> -->
						
						<td class="maxtd" align="right">应收省份</td>
						<td class="maxtd">
							<input value="${userInfo.provinceName }" class="inp01" disabled="disabled" id="payProvince4CreateName" name="payProvince4CreateName" />
						</td>
						
						<td class="maxtd1" align="right">应付省份</td>
						<td class="maxtd">
							<select id="receProvince4Create" value="receProvince4Create" class="sel_01">
								<option value="1">湖北</option>
								<option value="3">广东</option>
							</select>
						</td>
						
					</tr>
					
					<!-- <tr style="height: 10px;"></tr>
					<tr>
						<td class="maxtd1" align="right">应付省份</td>
						<td class="maxtd">
							<select id="receProvince4Create" value="receProvince4Create" class="sel_01">
								<option value="1">湖北</option>
								<option value="3">广东</option>
							</select>
						</td>
						
						<td class="maxtd" colspan="3"></td>
						
						<td class="maxtd" align="right">妥投审核截止日期</td>
						<td class="maxtd" >
							<input id="deadLineTime4Create" name="deadLineTime4Create" class="easyui-datebox" style="width: 200px;height: 25px;"></input>
						</td>
					</tr>
					 -->
					<tr style="height: 10px;"></tr>
					<tr>
						<td width="120px;" valign="top" align="right">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注</td>
						<td colspan="6">
							<textarea rows="3" cols="80" id="remark4Create" name="remark4Create" ></textarea>
						</td>
					</tr>
				</table>
			</div>
		</form>
	</div>
</div>	


<div class="iframe_bottom"> 
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
		<tr>
			<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
			<a href="javascript:$('#queryCondition').attr('action','${pageContext.request.contextPath}/acrossPayFreightCheck/billList4AcrossPay/1');$('#queryCondition').submit();" >第一页</a>　
			<a href="javascript:$('#queryCondition').attr('action','${pageContext.request.contextPath}/acrossPayFreightCheck/billList4AcrossPay/${page_obj.previous<1 ? 1 : page_obj.previous }');$('#queryCondition').submit();">上一页</a>　
			<a href="javascript:$('#queryCondition').attr('action','${pageContext.request.contextPath}/acrossPayFreightCheck/billList4AcrossPay/${page_obj.next<1 ? 1 : page_obj.next }');$('#queryCondition').submit();" >下一页</a>　
			<a href="javascript:$('#queryCondition').attr('action','${pageContext.request.contextPath}/acrossPayFreightCheck/billList4AcrossPay/${page_obj.maxpage<1 ? 1 : page_obj.maxpage }');$('#queryCondition').submit();" >最后一页</a>
			　共${page_obj.maxpage}页　共${page_obj.total}条记录 　当前第
			<select id="selectPg" onchange="$('#queryCondition').attr('action',$(this).val());$('#queryCondition').submit()">
				<c:forEach var="i" begin='1' end='${page_obj.maxpage}'>
					<option value='${i}' ${page==i ? 'selected=seleted' : ''}>${i}</option>
				</c:forEach>
			</select>页
			</td>
		</tr>
	</table>
</div>
<!--查询条件隐藏  -->
<form id="queryCondition" method="post">
	<input type="hidden" value="${params.billNo} " id="billNo" name="billNo" />
	<input type="hidden" value="${params.billState} " id="billState" name="billState" />
	<input type="hidden" value="${params.createBeginTime} " id="createBeginTime" name="createBeginTime" />
	<input type="hidden" value="${params.createEndTime} " id="createEndTime" name="createEndTime" />
	<input type="hidden" value="${params.verifyBeginTime} " id="verifyBeginTime" name="verifyBeginTime" />
	<input type="hidden" value="${params.verifyEndTime} " id="verifyEndTime" name="verifyEndTime" />
	<input type="hidden" value="${params.customerId} " id="customerId" name="customerId" />
	<input type="hidden" value="${params.branchId} " id="branchId" name="branchId" />
	<input type="hidden" value="${params.orderField} " id="orderField" name="orderField" />
	<input type="hidden" value="${params.orderRule} " id="orderRule" name="orderRule" />
	<input type="hidden" value="${params.payableProvinceId} " id="payableProvinceId" name="payableProvinceId" /><!--应付  -->
	<input type="hidden" value="${params.receProvinceId} " id="receProvinceId" name="receProvinceId"/><!-- 应收 -->
</form>


<script src="<%=request.getContextPath()%>/js/express/acrossPayFreight/acrossPayfreightCheck.js" type="text/javascript"></script>
</body>

</html>