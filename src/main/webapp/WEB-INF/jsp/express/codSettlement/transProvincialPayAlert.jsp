<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//Dth HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dth">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>跨省代收货款对账（应付）弹出页面</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<style type="text/css">
.tdleft {
	text-align: left;
}

.tdcenter {
	text-align: center;
}
</style>
</head>
<body>
<div>					
	<table width="100%" style="margin-top: 10px;">
		<colgroup>
			<col width="10%">
			<col width="10%">
			<col width="10%">
			<col width="10%">
			<col width="60%">
		</colgroup>
		<tr>
			<td class="tdcenter"><button  id="insertBox_close" class="input_button2" onclick="closeClick(this)">返回</button></td>
			<td class="tdcenter"><button  id="insertBox_save" class="input_button2" onclick="alertSaveClick()">保存</button></td>
			<td class="tdcenter"><button  id="insertBox_import" class="input_button2" onclick="importClick()">导入应收账单</button></td>
			<td class="tdcenter"><button  id="insertBox_check" class="input_button2" onclick="checkClick()">自动核对数据</button></td>
			<td></td>
		</tr>
	</table>
	<table width="95%" class="table1" style="padding:10px;line-height: 25px;;">
		<colgroup>
			<col width="13%">
			<col width="19%">
			<col width="13%">
			<col width="19%">
			<col width="17%">
			<col width="19%">
		</colgroup>
		<tr>
			<td class="tdcenter">账单编号:</td>
			<td><input type="text" name="billNo" id="insertBox_billNo" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>							
			<td class="tdcenter">账单状态:</td>
			<td><input type="text" class="input_text1" style="background:#EBEBE4;" disabled="true" value="未审核"/></td>							
			<td class="tdcenter"></td>
			<td></td>
			<input type="hidden"  name="billState" id="insertBox_billState" value='0'/>	
			<input type="hidden"  name="cod" id="insertBox_cod" value="${money }"/>
			<input type="hidden"  name="orderCount" id="insertBox_orderCount" value="${count }"/>					
		</tr>
		<tr>
			<td class="tdcenter">应收省份:</td>
			<td><input type="text" name="receivableProvinceName" id="insertBox_receivableProvinceName" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>
			<input type="hidden" name="receivableProvinceId" id="insertBox_receivableProvinceId"/>
			<td class="tdcenter">应付省份:</td>
			<td><input type="text" name="payableProvinceName" id="insertBox_payableProvinceName" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>
			<input type="hidden"  name="payableProvinceId" id="insertBox_payableProvinceId"/>
			<td class="tdcenter">代收货款合计：</td>
			<td><input type="text" name="cod" id="insertBox_cod_id" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>
		</tr>
		<tr>
			<td class="tdcenter">创建日期:</td>
			<td><input type="text" id="insertBox_createTime_id" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>
			<td class="tdcenter">审核日期:</td>
			<td><input type="text" id="insertBox_updateBox_auditTime_id" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>
			<td class="tdcenter">核销日期：</td>
			<td><input type="text" id="insertBox_cavTime_id" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>
		</tr>
		<tr>
			<td class="tdcenter">创建人:</td>
			<td><input type="text" id="insertBox_createName_id" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>
			<td class="tdcenter">审核人:</td>
			<td><input type="text" id="insertBox_auditName_auditTime_id" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>
			<td class="tdcenter">核销人：</td>
			<td><input type="text" id="insertBox_cavName_id" class="input_text1" style="background:#EBEBE4;" disabled="true"/></td>
		</tr>
		<tr>
			<td style="vertical-align:middle; text-align:center;">备注：</td>
			<td colspan="5" ><textarea name="remark" id="insertBox_remark" style="width:100%;height:100px;margin-top:10px;"></textarea></td>
			<td style="display:none;"><input type="text" name="id" id="updateBox_id_id" class="input_text1"/></td>
		</tr>
	</table>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="alertinfoTable">
		<colgroup>
			<col width="20%">
			<col width="16%">
			<col width="16%">
			<col width="16%">
			<col width="16%">
			<col width="16%">
		</colgroup>
		<tr class="font_1">
			<th bgcolor="#eef6ff">运单号</th>
			<th bgcolor="#eef6ff">件数</th>
			<th bgcolor="#eef6ff">揽货员</th>
			<th bgcolor="#eef6ff">派件员</th>
			<th bgcolor="#eef6ff">代收货款</th>
			<th bgcolor="#eef6ff">揽件站点</th>
		</tr>
		<%-- 
		<c:forEach items="${infolist}" var="list">
			<tr>
				<td>${list.BillNo }</td>
				......
				 后台传回来的导入数据
			</tr>	
		</c:forEach>	 
		--%>			
	</table>
	<div>
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1" id="pageid">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff"><a
					href="javascript:creatClick(1);">第一页</a> <a
					href="javascript:creatClick('${page_obj.previous < 1 ? 1 : page_obj.previous}');">上一页</a>
					<a
					href="javascript:creatClick('${page_obj.next < 1 ? 1 : page_obj.next}');">下一页</a>
					<a
					href="javascript:creatClick('${page_obj.maxpage < 1 ? 1 : page_obj.maxpage}');">最后一页</a>
					共${page_obj.maxpage}页 共${page_obj.total}条记录 当前第 <select id="selectPg"
					onchange="creatClick($(this).val())">
						<c:forEach begin="1" end="${page_obj.maxpage}" var="i">
							<c:if test="${page == i}">
								<option value="${i}" selected="true">${i}</option>
							</c:if>
							<c:if test="${page != i}">
								<option value="${i}">${i}</option>
							</c:if>
						</c:forEach>
				</select>页</td>
			</tr>
		</table>
	</div>
</div>
</body>
</html>