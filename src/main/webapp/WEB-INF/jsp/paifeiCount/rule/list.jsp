<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<jsp:useBean id="now" class="java.util.Date" scope="page" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>派费结算规则</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/2.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css" type="text/css" />
<script src="${pageContext.request.contextPath}/js/paifeirule.js"></script>

<script type="text/javascript">
	
</script>
</head>

<body style="background: #eef9ff">

	<div class="right_box">
		<div class="inputselect_box">
			<table style="width: 60%">
				<tr>
					<td><input class="input_button2" type="button" onclick="$('#add').dialog('open')"
						value="新增" /> <input class="input_button2" type="button" value="查看/修改" onclick="joineditRule()" /> <input
						class="input_button2" type="button" onclick="allchecked()" value="删除" /> <input
						class="input_button2" type="button" onclick="$('#find').dialog('open')" value="查询" /></td>
				</tr>
			</table>

		</div>


		<div class="jg_10"></div>
		<div class="jg_10"></div>
		<div class="jg_10"></div>
		<div class="jg_10"></div>
		<div class="jg_10"></div>
		<div class="right_title">
			<div style="height: 380px;overflow: auto;" >
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="rule_table">
					<tr id="thead">
						<td height="30px" valign="middle"><input type="checkbox" id="all" onclick="checkall()" />
						</td>
						<td align="center" valign="middle" style="font-weight: bold;">结算规则名称</td>
						<td align="center" valign="middle" style="font-weight: bold;">规则类型</td>
						<td align="center" valign="middle" style="font-weight: bold;">状态</td>
						<td align="center" valign="middle" style="font-weight: bold;">备注</td>
					</tr>
					<c:forEach items="${paiFeiRules}" var="pf">
						<tr id="${pf.id }" onclick="editRule('${pf.id }')">
							<td align="center" valign="middle"><input type="checkbox" id="id" value="${pf.id}" /></td>
							<td align="center" valign="middle">${pf.name}</td>
							<c:forEach items="${PaiFeiRuleTypeEnum}" var="t">
								<c:if test="${t.value==pf.type }">
									<td align="center" valign="middle">${t.text}</td>
								</c:if>
							</c:forEach>
							<c:forEach items="${PaiFeiRuleStateEnum}" var="s">
								<c:if test="${s.value==pf.state }">
									<td align="center" valign="middle">${s.text}</td>
								</c:if>
							</c:forEach>
							<td align="center" valign="middle">${pf.remark}</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</div>
	<input type="hidden" id="dmpurl" value="${pageContext.request.contextPath}" />
	<c:if test='${page_obj.maxpage>1}'>
		<div class="iframe_bottom">
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff" style="font-size: 10px;">
						<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();">第一页</a> <a
						href="javascript:$('#searchForm').attr('action','${page_obj.previous<1?1:page_obj.previous}');$('#searchForm').submit();">上一页</a>
						<a
						href="javascript:$('#searchForm').attr('action','${page_obj.next<1?1:page_obj.next }');$('#searchForm').submit();">下一页</a>
						<a
						href="javascript:$('#searchForm').attr('action','${page_obj.maxpage<1?1:page_obj.maxpage}');$('#searchForm').submit();">最后一页</a>
						共${page_obj.maxpage}页 共${page_obj.total}条记录 当前第<select id="selectPg"
						onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
							<c:forEach var="i" begin="1" end="${page_obj.maxpage}">
								<option value='${i}' ${page==i?'selected=seleted':''}>${i}</option>
							</c:forEach>
					</select>页
					</td>
				</tr>
			</table>
		</div>
	</c:if>
	<!-- 新增层显示 -->
	<div id="add" class="easyui-dialog" title="新增" data-options="iconCls:'icon-save',modal:true"
		style="width: 700px; height: 220px;">
		<form action="${ctx}/paifeirule/credata" method="post" id="credatafrom">
			<table width="85%" border="0" cellspacing="1" cellpadding="0"
				style="margin-top: 10px; margin-left: 25px; font-size: 10px;">
				<tr>
					<td style="line-height: 30px" colspan="2" align="center" valign="bottom"><input
						type="button" class="input_button2" value="返回" onclick="$('#add').dialog('close');" /> <input
						type="submit" class="input_button2" value="保存" /></td>
				</tr>
				<tr>
					<td style="line-height: 30px" align="right">规则类型：</td>
					<td><select style="width: 100%;" name="type">
							<!-- <option value="0">-全部-</option> -->
							<c:forEach items="${PaiFeiRuleTypeEnum}" var="type">
								<option value="${type.value}">${type.text}</option>
							</c:forEach>
					</select></td>
					<td align="right">状态：</td>
					<td><select style="width: 100%;" name="state">
							<c:forEach items="${PaiFeiRuleStateEnum}" var="state">
								<option value="${state.value}">${state.text}</option>
							</c:forEach>
					</select></td>
					<td align="right">拒收派费(元)：</td>
					<td><input type="text" name="jushouPFfee" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"/></td>
				</tr>
				<tr>
					<td style="line-height: 30px" align="right">结算规则名称：</td>
					<td colspan="5"><input style="width: 100%" type="text" name="name" /></td>
				</tr>
				<tr>
					<td style="line-height: 30px" align="right" valign="middle">备注：</td>
					<td colspan="5"><textarea rows="3" style="width: 100%; resize: none;" name="remark"> </textarea></td>
				</tr>
			</table>
		</form>
	</div>
	<!-- 查看/修改层显示 -->
	<c:if test="${save==1 }">
		<div id="save" class="easyui-dialog" title="保存" data-options="iconCls:'icon-save',modal:true"
			style="width: 800px;">
			<form action="" method="post" id="save_from">
				<input type="hidden" id="s_ruleid" value="${rule.id}" />
				<table width="85%" border="0" cellspacing="1" cellpadding="0"
					style="margin-top: 10px; margin-left: 25px; font-size: 10px;">
					<tr>
						<td style="line-height: 30px" colspan="2" align="center" valign="bottom"><input
							type="button" class="input_button2" value="返回" onclick="$('#add').dialog('close');" /> <input
							type="button" class="input_button2" value="保存" onclick="sub(${rule.type})" /></td>
					</tr>
					<tr>
						<td style="line-height: 30px" align="right">规则类型：</td>
						<td><select style="width: 100%;" id="s_ruletypeid" disabled="disabled">
								<c:forEach items="${PaiFeiRuleTypeEnum}" var="type">
									<option value="${type.value}" ${type.value==rule.type ? 'selected=selected' : '' }>${type.text}</option>
								</c:forEach>
						</select></td>
						<td align="right">状态：</td>
						<td><select style="width: 100%;" id="s_state" disabled="disabled">
								<c:forEach items="${PaiFeiRuleStateEnum}" var="state">
									<option value="${state.value}" ${state.value==rule.state ? 'selected=selected' : '' }>${state.text}</option>
								</c:forEach>
						</select></td>
						<td align="right">拒收派费(元)：</td>
						<td><input type="text" id="s_jushouPFfee" value="${rule.jushouPFfee }"
							disabled="disabled" /></td>
					</tr>
					<tr>
						<td style="line-height: 30px" align="right">结算规则名称：</td>
						<td colspan="5"><input style="width: 100%" type="text" id="s_name" value="${rule.name}"
							disabled="disabled" /></td>
					</tr>
					<tr>
						<td style="line-height: 30px" align="right" valign="middle">备注：</td>
						<td id="s_remark" colspan="5"><textarea rows="3" style="width: 100%; resize: none;"
								disabled="disabled">${rule.remark }</textarea></td>
					</tr>
				</table>
				<div id="tt" class="easyui-tabs" style="height: 450px; overflow: scroll;">
					<div title=" 配送费规则">
						<table style="width: 100%; margin-top: 10px; font-size: 10px;" border="0" cellspacing="1"
							cellpadding="0">
							<tr id="ps_basic_tr">
								<td style="width: 14%;"><input type="checkbox" id="ps_basic_flag" />基本派费</td>
								<td><select onchange="showflag('ps_basic',$(this).val())">
										<option value="yes">按供货商区分</option>
										<option value="no">不按供货商区分</option>
								</select><span id="ps_basic_no"><input type="text" style="margin-top: -5px" name="basicPFfee"
										id="ps_basicPFfee" />元</span>
									<div id="ps_basic_yes">
										<table id="ps_basic_table" width="100%" border="0" cellspacing="1" cellpadding="0"
											class="table_2" id="gd_table">
											<tr id="thead">
												<th style="width: 10%" align="center"><input type="checkbox"
													onclick="AllTR('ps','basic')" /></th>
												<th style="width: 25%" align="center">供货商</th>
												<th style="width: 25%" align="center">基本派费金额</th>
												<th style="width: 40%" align="center">备注</th>
											</tr>

										</table>
										<input type="button" value="添加" onclick="addTR('ps','basic')" /> <input type="button"
											value="移除" onclick="removeTR('ps','basic')" />
									</div></td>
							</tr>
							<tr id="ps_collection_tr">
								<td><input type="checkbox" id="ps_collection_flag" />代收补助费</td>
								<td><select onchange="showflag('ps_collection',$(this).val())">
										<option value="yes">按供货商区分</option>
										<option value="no">不按供货商区分</option>
								</select><span id="ps_collection_no"><input type="text" id="ps_collectionPFfee"
										name="ps_collectionPFfee" style="margin-top: -5px" />元</span>
									<div id="ps_collection_yes">
										<table id="ps_collection_table" width="100%" border="0" cellspacing="1" cellpadding="0"
											class="table_2" id="gd_table">
											<tr id="thead">
												<td style="width: 10%" align="center"><input type="checkbox"
													onclick="AllTR('ps','collection')" /></td>
												<td style="width: 25%" align="center">供货商</td>
												<td style="width: 25%" align="center">基本派费金额</td>
												<td style="width: 40%" align="center">备注</td>
											</tr>
										</table>
										<input type="button" value="添加" onclick="addTR('ps','collection')" /> <input
											type="button" value="移除" onclick="removeTR('ps','collection')" />
									</div></td>
							</tr>
							<tr id="ps_area_tr">
								<td><input type="checkbox" id="ps_area_flag" />区域属性补助费</td>
								<td>
									<div>
										<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2"
											id="gd_table">
											<tr>
												<td style="width: 10%" align="center">城市</td>
												<td style="width: 20%" align="center">区/县</td>
												<td style="width: 15%" align="center">启用区域补助</td>
												<td style="width: 15%" align="center">启用超大补助</td>
												<td style="width: 15%" align="center">启用超重补助</td>
												<td style="width: 25%" align="center">备注</td>
											</tr>
											<tr id="ps_111" onclick="addArea('ps','广东','111')">
												<td>广东</td>
												<td>广州</td>
												<td>是</td>
												<td>是</td>
												<td>否</td>
												<td>广东省是个好地方</td>
											</tr>
											<tr id="ps_222" onclick="addArea('ps','北京','222')">
												<td>朝阳</td>
												<td>北京</td>
												<td>否</td>
												<td>是</td>
												<td>否</td>
												<td>我爱北京天安门</td>
											</tr>
										</table>
										<div id="ps_area_div"></div>
									</div>
								</td>
							</tr>
							<tr id="ps_overarea_tr">
								<td><input type="checkbox" value="1" />超区补助</td>
							</tr>
							<tr id="ps_business_tr">
								<td><input type="checkbox" id="ps_business_flag" />业务补助</td>
								<td><input type="text" id="ps_business_subsidyfee" /></td>
							</tr>
							<tr id="ps_insertion_tr">
								<td><input type="checkbox" id="ps_insertion_flag" />托单补助</td>
								<td>
									<div>
										<table id="ps_insertion_table" width="100%" border="0" cellspacing="1" cellpadding="0"
											class="table_2" id="gd_table">
											<tr id="thead">
												<td style="width: 10%" align="center"><input type="checkbox"
													onclick="AllTR('ps','insertion')" /></td>
												<td style="width: 20%" align="center">托单数下限</td>
												<td style="width: 20%" align="center">托单数上限</td>
												<td style="width: 20%" align="center">补助金额(元)</td>
												<td style="width: 30%" align="center">备注</td>
											</tr>
										</table>
									</div> <input type="button" value="添加" onclick="addTROfinsertion('ps','insertion')" /> <input
									type="button" onclick="removeTR('ps','insertion')" value="移除" />
								</td>
							</tr>
						</table>
					</div>
					<c:if test="${rule.type==1||rule.type==2}">
						<div title="提货费规则">
							<table style="width: 100%; margin-top: 10px; font-size: 10px;" border="0" cellspacing="1"
								cellpadding="0">
								<tr id="th_basic_tr">
									<td style="width: 14%;"><input type="checkbox" id="th_basic_flag" />基本派费</td>
									<td><select onchange="showflag('th_basic',$(this).val())">
											<option value="yes">按供货商区分</option>
											<option value="no">不按供货商区分</option>
									</select><span id="th_basic_no"><input type="text" style="margin-top: -5px"
											name="th_basicPFfee" id="th_basicPFfee" />元</span>
										<div id="th_basic_yes">
											<table id="th_basic_table" width="100%" border="0" cellspacing="1" cellpadding="0"
												class="table_2" id="gd_table">
												<tr id="thead">
													<th style="width: 10%" align="center"><input type="checkbox"
														onclick="AllTR('th','basic')" /></th>
													<th style="width: 25%" align="center">供货商</th>
													<th style="width: 25%" align="center">基本派费金额</th>
													<th style="width: 40%" align="center">备注</th>
												</tr>
											</table>
											<input type="button" value="添加" onclick="addTR('th','basic')" /> <input type="button"
												onclick="removeTR('th','basic')" value="移除" />
										</div></td>
								</tr>
								<tr id="th_collection_tr">
									<td><input type="checkbox" id="th_collection_flag" />代收补助费</td>
									<td><select onchange="showflag('th_collection',$(this).val())">
											<option value="yes">按供货商区分</option>
											<option value="no">不按供货商区分</option>
									</select><span id="th_collection_no"><input type="text" id="th_collectionPFfee"
											name="th_collectionPFfee" style="margin-top: -5px" />元</span>
										<div id="th_collection_yes">
											<table id="th_collection_table" width="100%" border="0" cellspacing="1" cellpadding="0"
												class="table_2" id="gd_table">
												<tr id="thead">
													<td style="width: 10%" align="center"><input type="checkbox"
														onclick="AllTR('th','collection')" /></td>
													<td style="width: 25%" align="center">供货商</td>
													<td style="width: 25%" align="center">基本派费金额</td>
													<td style="width: 40%" align="center">备注</td>
												</tr>
											</table>
											<input type="button" value="添加" onclick="addTR('th','collection')" /> <input
												type="button" onclick="removeTR('th','collection')" value="移除" />
										</div></td>
								</tr>
								<tr id="th_area_tr">
									<td><input type="checkbox" id="th_area_flag" />区域属性补助费</td>
									<td>
										<div>
											<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2"
												id="gd_table">
												<tr>
													<td style="width: 10%" align="center">城市</td>
													<td style="width: 20%" align="center">区/县</td>
													<td style="width: 15%" align="center">启用区域补助</td>
													<td style="width: 15%" align="center">启用超大补助</td>
													<td style="width: 15%" align="center">启用超重补助</td>
													<td style="width: 25%" align="center">备注</td>
												</tr>
												<tr id="th_111" onclick="addArea('th','广东','111')">
													<td>广东</td>
													<td>广州</td>
													<td>是</td>
													<td>是</td>
													<td>否</td>
													<td>广东省是个好地方</td>
												</tr>
												<tr id="th_222" onclick="addArea('th','北京','222')">
													<td>朝阳</td>
													<td>北京</td>
													<td>否</td>
													<td>是</td>
													<td>否</td>
													<td>我爱北京天安门</td>
												</tr>
											</table>
											<div id="th_area_div"></div>
										</div>
									</td>
								</tr>
								<tr id="th_overarea_tr">
									<td><input type="checkbox" value="1" />超区补助</td>
								</tr>
								<tr id="th_business_tr">
									<td><input type="checkbox" id="th_business_flag" />业务补助</td>
									<td><input type="text" id="th_business_subsidyfee" /></td>
								</tr>
								<tr id="th_insertion_tr">
									<td><input type="checkbox" id="th_insertion_flag" />托单补助</td>
									<td>
										<div>
											<table id="th_insertion_table" width="100%" border="0" cellspacing="1" cellpadding="0"
												class="table_2" id="gd_table">
												<tr id="thead">
													<td style="width: 10%" align="center"><input type="checkbox"
														onclick="AllTR('th','insertion')" /></td>
													<td style="width: 20%" align="center">托单数下限</td>
													<td style="width: 20%" align="center">托单数上限</td>
													<td style="width: 20%" align="center">补助金额(元)</td>
													<td style="width: 30%" align="center">备注</td>
												</tr>
											</table>
										</div> <input type="button" value="添加" onclick="addTROfinsertion('th','insertion')" /> <input
										onclick="removeTR('th','insertion')" type="button" value="移除" />
									</td>
								</tr>
							</table>
						</div>
					</c:if>
					<c:if test="${rule.type==2}">
						<div title="中转费规则">
							<table style="width: 100%; margin-top: 10px; font-size: 10px;" border="0" cellspacing="1"
								cellpadding="0">
								<tr id="zz_basic_tr">
									<td style="width: 14%;"><input type="checkbox" id="zz_basic_flag" />基本派费</td>
									<td><select onchange="showflag('zz_basic',$(this).val())">
											<option value="yes">按供货商区分</option>
											<option value="no">不按供货商区分</option>
									</select><span id="zz_basic_no"><input type="text" style="margin-top: -5px"
											name="zz_basicPFfee" id="zz_basicPFfee" />元</span>
										<div id="zz_basic_yes">
											<table id="zz_basic_table" width="100%" border="0" cellspacing="1" cellpadding="0"
												class="table_2" id="gd_table">
												<tr id="thead">
													<th style="width: 10%" align="center"><input type="checkbox"
														onclick="AllTR('zz','basic')" /></th>
													<th style="width: 25%" align="center">供货商</th>
													<th style="width: 25%" align="center">基本派费金额</th>
													<th style="width: 40%" align="center">备注</th>
												</tr>
											</table>
											<input type="button" value="添加" onclick="addTR('zz','basic')" /> <input type="button"
												onclick="removeTR('zz','basic')" value="移除" />
										</div></td>
								</tr>
								<tr id="zz_collection_tr">
									<td><input type="checkbox" id="zz_collection_flag" />代收补助费</td>
									<td><select onchange="showflag('zz_collection',$(this).val())">
											<option value="yes">按供货商区分</option>
											<option value="no">不按供货商区分</option>
									</select><span id="zz_collection_no"><input type="text" id="zz_collectionPFfee"
											name="zz_collectionPFfee" style="margin-top: -5px" />元</span>
										<div id="zz_collection_yes">
											<table id="zz_collection_table" width="100%" border="0" cellspacing="1" cellpadding="0"
												class="table_2" id="gd_table">
												<tr id="thead">
													<td style="width: 10%" align="center"><input type="checkbox"
														onclick="AllTR('zz','collection')" /></td>
													<td style="width: 25%" align="center">供货商</td>
													<td style="width: 25%" align="center">基本派费金额</td>
													<td style="width: 40%" align="center">备注</td>
												</tr>
											</table>
											<input type="button" value="添加" onclick="addTR('zz','collection')" /> <input
												type="button" onclick="removeTR('zz','collection')" value="移除" />
										</div></td>
								</tr>
								<tr id="zz_area_tr">
									<td><input type="checkbox" id="zz_area_flag" />区域属性补助费</td>
									<td>
										<div>
											<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2"
												id="gd_table">
												<tr>
													<td style="width: 10%" align="center">城市</td>
													<td style="width: 20%" align="center">区/县</td>
													<td style="width: 15%" align="center">启用区域补助</td>
													<td style="width: 15%" align="center">启用超大补助</td>
													<td style="width: 15%" align="center">启用超重补助</td>
													<td style="width: 25%" align="center">备注</td>
												</tr>
												<tr id="zz_111" onclick="addArea('zz','广东','111')">
													<td>广东</td>
													<td>广州</td>
													<td>是</td>
													<td>是</td>
													<td>否</td>
													<td>广东省是个好地方</td>
												</tr>
												<tr id="zz_222" onclick="addArea('zz','北京','222')">
													<td>朝阳</td>
													<td>北京</td>
													<td>否</td>
													<td>是</td>
													<td>否</td>
													<td>我爱北京天安门</td>
												</tr>
											</table>
											<div id="zz_area_div"></div>
										</div>
									</td>
								</tr>
								<tr id="zz_overarea_tr">
									<td><input type="checkbox" value="1" />超区补助</td>

								</tr>
								<tr id="zz_business_tr">
									<td><input type="checkbox" id="zz_business_flag" />业务补助</td>
									<td><input type="text" id="zz_business_subsidyfee" /></td>
								</tr>
								<tr id="zz_insertion_tr">
									<td><input type="checkbox" id="zz_insertion_flag" />托单补助</td>
									<td>
										<div>
											<table id="zz_insertion_table" width="100%" border="0" cellspacing="1" cellpadding="0"
												class="table_2" id="gd_table">
												<tr id="thead">
													<td style="width: 10%" align="center"><input type="checkbox"
														onclick="AllTR('zz','insertion')" /></td>
													<td style="width: 20%" align="center">托单数下限</td>
													<td style="width: 20%" align="center">托单数上限</td>
													<td style="width: 20%" align="center">补助金额(元)</td>
													<td style="width: 30%" align="center">备注</td>
												</tr>
											</table>
										</div> <input type="button" value="添加" onclick="addTROfinsertion('zz','insertion')" /> <input
										onclick="removeTR('zz','insertion')" type="button" value="移除" />
									</td>
								</tr>
							</table>
						</div>
					</c:if>
				</div>
			</form>
		</div>

	</c:if>

	<!-- 	编辑页面 -->
	<c:if test="${edit==1 }">
		<div id="edit_rule" class="easyui-dialog" title="编辑/修改"
			data-options="iconCls:'icon-save',modal:true" style="width: 800px;">
			<form action="" method="post" id="edit_rule_from">
				<input type="hidden" id="edit_rule_id" name="id" value="${rule.id}" />

				<table width="85%" border="0" cellspacing="1" cellpadding="0"
					style="margin-top: 10px; margin-left: 25px; font-size: 10px;">
					<tr>
						<td></td>
						<td><input type="button" class="input_button2" value="返回"
							onclick="$('#edit_rule').dialog('close');" /></td>
					</tr>
					<tr>
						<td style="line-height: 30px" align="right">规则类型：</td>
						<td>
							<%-- <select style="width: 100%;" name="type" id="edit_type" disabled="disabled">
								<c:forEach items="${PaiFeiRuleTypeEnum}" var="type">
									<option value="${type.value}" ${type.value==rule.type ? 'selected=selected' : '' }>${type.text}</option>
								</c:forEach>
						</select> --%> <input type="text" name="type" id="edit_ruletype" readonly="true"
							class="easyui-validatebox" data-options="width:150,prompt: '规则类型'" initDataType="ENUM"
							initDataKey="cn.explink.enumutil.PaiFeiRuleTypeEnum" viewField="text" saveField="value"
							readonly="true" value="${rule.type}" />
						</td>
						<td align="right">状态：</td>
						<td>
							<%-- <select style="width: 100%;" id="s_state" name="state" >
								<c:forEach items="${PaiFeiRuleStateEnum}" var="state">
									<option value="${state.value}" ${state.value==rule.state ? 'selected=selected' : '' }>${state.text}</option>
								</c:forEach>
						</select> --%> <input type="text" name="state" id="edit_state" readonly="true"
							class="easyui-validatebox" data-options="width:150,prompt: '状态'" initDataType="ENUM"
							initDataKey="cn.explink.enumutil.PaiFeiRuleStateEnum" viewField="text" saveField="value"
							value="${rule.state}" readonly="true" />
						</td>
						<td align="right">拒收派费(元)：</td>
						<td><input type="text" id="edit_jushouPFfee" name="jushouPFfee" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"
							value="${rule.jushouPFfee }" /></td>
					</tr>
					<tr>
						<td style="line-height: 30px" align="right">结算规则名称：</td>
						<td colspan="5"><input style="width: 100%" type="text" id="edit_name" name="name"
							value="${rule.name}" /></td>
					</tr>
					<tr>
						<td style="line-height: 30px" align="right" valign="middle">备注：</td>
						<td id="edit_remark" colspan="5"><textarea rows="3" name="remark"
								style="width: 100%; resize: none;">${rule.remark }</textarea></td>
					</tr>
					<tr>
						<td><td><input type="button" class="input_button2" value="保存"
								onclick="subEidt('edit_rule_from','','rule')" /></td>
					</tr>
				</table>
			</form>
			<div id="tt" class="easyui-tabs" style="height: 450px; overflow: scroll;">
				<div title=" 配送费规则">
					<table style="width: 100%; margin-top: 10px; font-size: 10px;" border="0" cellspacing="1"
						cellpadding="0">
						<tr id="ps_basic_tr">
							<td style="width: 14%;"><input type="checkbox" id="ps_basic_flag"
								${basicPS!=null||fn:length(basicListPS)>0?'checked=checked':'' } />基本派费</td>
							<td><span id="edit_ps_basicno_from"> <input type='hidden' name='showflag'
									value='0' /> <select onchange="showflag('ps_basic',$(this).val())" id="ps_showflag_basic">
										<option ${ps_showflag_basic=="yes" ? 'selected=selected' : '' } value="yes">按供货商区分</option>
										<option ${ps_showflag_basic=="no" ? 'selected=selected' : '' } value="no">不按供货商区分</option>
								</select><span id="ps_basic_no"> <input type="text" style="margin-top: -5px" name="basicPFfee" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"
										id="ps_basicPFfee" value="${basicPS.basicPFfee }" />元 <input type="button" value="保存"
										onclick="subEidt('edit_ps_basicno_from','ps','basicno')" />
								</span>
								
							</span>
								<div id="ps_basic_yes">
									<form action="" id="edit_ps_basic_from">

										<table id="ps_basic_table" width="100%" border="0" cellspacing="1" cellpadding="0"
											class="table_2" id="gd_table">
											<tr id="thead">
												<th style="width: 10%" align="center"><input type="checkbox"
													onclick="AllTR('ps','basic')" /></th>
												<th style="width: 25%" align="center">供货商</th>
												<th style="width: 25%" align="center">基本派费金额</th>
												<th style="width: 40%" align="center">备注</th>
											</tr>
											<c:forEach items="${basicListPS}" var="basic">
												<tr>
													<input type='hidden' name='showflag' value='1' />
													<td align='center'><input type='checkbox' /></td>
													<td><input type="text" id="${basic.customerid }" name="customerid"
														class="easyui-validatebox" style="width: 100%;" initDataType="TABLE"
														initDataKey="Customer" viewField="customername" saveField="customerid"
														value="${basic.customerid}" /></td>
													<td><input style='width: 100%;' type='text' id='basicPFfee' name='basicPFfee' onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"
														value="${basic.basicPFfee }" /></td>
													<td><input style='width: 100%;' type='text' id='remark' name='remark'
														value="${basic.remark }" /></td>
												</tr>
											</c:forEach>
										</table>
										<input type="button" value="添加" onclick="addTR('ps','basic')" /> <input type="button"
											value="移除" onclick="removeTR('ps','basic')" /> <input type="button" value="保存"
											onclick="subEidt('edit_ps_basic_from','ps','basic')" />
									</form>
								</div></td>
						</tr>
						<tr id="ps_collection_tr">
							<td><input type="checkbox" id="ps_collection_flag"
								${collectionPS!=null||fn:length(collectionPS)>0?'checked=checked':'' } />代收补助费</td>
							<td><span id="edit_ps_collectionno_from"> <input type='hidden' name='showflag'
									value='0' /> <select onchange="showflag('ps_collection',$(this).val())"
									id="ps_showflag_collection">
										<option ${ps_showflag_collection=="yes" ? 'selected=selected' : '' } value="yes">按供货商区分</option>
										<option ${ps_showflag_collection=="no" ? 'selected=selected' : '' } value="no">不按供货商区分</option>
								</select><span id="ps_collection_no"> <input type="text" id="ps_collectionPFfee" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"
										name="collectionPFfee" value="${collectionPS.collectionPFfee }" style="margin-top: -5px" />元
										<input type="button" value="保存"
										onclick="subEidt('edit_ps_collectionno_from','ps','collectionno')" />
								</span>
							</span>

								<div id="ps_collection_yes">
									<form action="" id="edit_ps_collection_from">
										<table id="ps_collection_table" width="100%" border="0" cellspacing="1" cellpadding="0"
											class="table_2" id="gd_table">
											<tr id="thead">
												<th style="width: 10%" align="center"><input type="checkbox"
													onclick="AllTR('ps','collection')" /></th>
												<th style="width: 25%" align="center">供货商</th>
												<th style="width: 25%" align="center">基本派费金额</th>
												<th style="width: 40%" align="center">备注</th>
											</tr>
											<c:forEach items="${collectionListPS}" var="collection">
												<tr>
													<input type='hidden' name='showflag' value='1' />
													<td align='center'><input type='checkbox' /></td>
													<td><input type="text" id="${collection.customerid }" name="customerid"
														class="easyui-validatebox" style="width: 100%;" initDataType="TABLE"
														initDataKey="Customer" viewField="customername" saveField="customerid"
														value="${collection.customerid}" /></td>
													<td><input style='width: 100%;' type='text' id='collectionPFfee' onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"
														name='collectionPFfee' value="${collection.collectionPFfee}" /></td>
													<td><input style='width: 100%;' type='text' id='remark' name='remark'
														value="${collection.remark}" /></td>
												</tr>
											</c:forEach>
										</table>
										<input type="button" value="添加" onclick="addTR('ps','collection')" /> <input
											type="button" value="移除" onclick="removeTR('ps','collection')" /> <input type="button"
											value="保存" onclick="subEidt('edit_ps_collection_from','ps','collection')" />
								</div>
								</form></td>
						</tr>
						<tr id="ps_area_tr">
							<td><input type="checkbox" id="ps_area_flag" />区域属性补助费</td>
							<td>
								<div>
									<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2"
										id="gd_table">
										<tr>
											<td style="width: 10%" align="center">城市</td>
											<td style="width: 20%" align="center">区/县</td>
											<td style="width: 15%" align="center">启用区域补助</td>
											<td style="width: 15%" align="center">启用超大补助</td>
											<td style="width: 15%" align="center">启用超重补助</td>
											<td style="width: 25%" align="center">备注</td>
										</tr>
										<tr id="ps_111" onclick="showArea($(this),'edit_area_ps_1','广东')">
											<td>广东</td>
											<td>广州</td>
											<td>是</td>
											<td>是</td>
											<td>否</td>
											<td>广东省是个好地方</td>
										</tr>
										<tr id="ps_222" onclick="showArea($(this),'edit_area_ps_2','北京')">
											<td>北京</td>
											<td>朝阳</td>
											<td>否</td>
											<td>是</td>
											<td>否</td>
											<td>我爱北京天安门</td>
										</tr>
									</table>
									<div id="ps_area_div">
		<c:forEach items="${pfareaListPS}" var="area">
		<div id="edit_area_ps_${area.id}">
		<table width="95%" border="0" style="margin-left: 5%" cellspacing="1" cellpadding="0"
			class="table_2" id="area_table">
			<tr>
				<td align="center">区域名称：</td>
				<td align="left"><span id="areaname">${area.areaname }</span><input type="hidden" id="areaid" value="${areafee.areaid}" /></td>
			</tr>
			<tr>
				<td align="left" nowrap="nowrap" valign="bottom"><input type="checkbox" />区域补助金额</td>
				<td align="left"><span><input value="${area.areafee }" type="text" id="areafee" style="margin-top: -5px" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"/>元</span></td>
			</tr>
			<tr>
				<td align="left" valign="bottom"><input type="checkbox" />超大补助</td>
				<td align="left"><table align="left" width="100%" border="0" cellspacing="1"
						cellpadding="0" class="table_2" id="ps_overbig_${area.id }_table">
						<tr id="thead">
							<td style="width: 10%" align="center"><input type="checkbox" id="overbig_checbox"
								onclick="AllTR('ps','overbig_${area.id }')" /></td>
							<td style="width: 20%" align="center">开始体积数(cm3)</td>
							<td style="width: 15%" align="center">截至体积数(cm3)</td>
							<td style="width: 15%" align="center">补助金额(元)</td>
							<td style="width: 25%" align="center">备注</td>
						</tr>
						<c:forEach items="${overbigMapPS[area.id]}" var="big">
						
						<tr>
						<td><input type="checkbox"/><input type="hidden" name="areaid" value="${area.id }" /><input type="hidden" name="id" value="${big.id }" /></td>
						<td><input style="width: 100%;" type="text"  id="mincount" name="mincount" value="${big.mincount}" onblur="comparaTo($(this),'min')"/></td>
						<td><input style="width: 100%;" type="text"  id="maxcount" name="maxcount" value="${big.maxcount}" onblur="comparaTo($(this),'max')"/></td>
						<td><input style="width: 100%;" type="text"  id="subsidyfee" name="subsidyfee" value="${big.subsidyfee}" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"/></td>
						<td><input style="width: 100%;" type="text"  id="remark" name="remark"  value="${big.remark}"/></td>
						</tr>
						</c:forEach>
					</table> <input type="button" id="overbig_add" value="添加" onclick="addTROfOverAreaEdit('ps','overbig_${area.id}')" />
					<input id="overbig_remove" onclick="removeTR('ps','overbig_${area.id}')" type="button" value="移除" />
					<input type="button" value="保存"
										onclick="subEidt('ps_overbig_${area.id }_table','ps','overbig')" />
					</td>
			</tr>
			<tr>
				<td align="left"><input type="checkbox" />超重补助</td>
				<td align="left"><table align="left" width="100%" border="0" cellspacing="1"
						cellpadding="0" class="table_2" id="ps_overweight_${area.id}_table">
						<tr id="thead">
							<td style="width: 10%" align="center"><input type="checkbox" id="overweight_checbox"
								onclick="AllTR('ps','overweight_${area.id}')" /></td>
							<td style="width: 20%" align="center">开始超重数(kg)</td>
							<td style="width: 15%" align="center">截至超重数(kg)</td>
							<td style="width: 15%" align="center">补助金额(元)</td>
							<td style="width: 25%" align="center">备注</td>
						</tr>
							<c:forEach items="${overweightMapPS[area.id]}" var="weight">
							
						<tr>
						<td><input type="checkbox"/><input type="hidden" name="areaid" value="${area.id }" /><input type="hidden" name="id" value="${weight.id }" /></td>
						<td><input style="width: 100%;" type="text"  id="mincount" name="mincount" value="${weight.mincount}" onblur="comparaTo($(this),'min')"/></td>
						<td><input style="width: 100%;" type="text"  id="maxcount" name="maxcount" value="${weight.maxcount}" onblur="comparaTo($(this),'max')"/></td>
						<td><input style="width: 100%;" type="text"  id="subsidyfee" name="subsidyfee" value="${weight.subsidyfee}" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"/></td>
						<td><input style="width: 100%;" type="text"  id="remark" name="remark"  value="${weight.remark}"/></td>
						</tr>
						</c:forEach>
					</table> <input type="button" id="overweight_add" value="添加"
					onclick="addTROfOverAreaEdit('ps','overweight_${area.id}')" /> <input id="overweight_add"
					onclick="removeTR('ps','overweight_${area.id}')" type="button" value="移除" />
							<input type="button" value="保存"
										onclick="subEidt('ps_overweight_${area.id }_table','ps','overweight')" />
					</td>
			</tr>
		</table>
	</div>
	</c:forEach>
									</div>
								</div>
							</td>
						</tr>
						<tr id="ps_overarea_tr">
							<td><input type="checkbox" id="edit_ps_state_checkbox" value="1"
								${overareaPS!=null&&overareaPS.state==1?'checked=checked':'' } />超区补助</td>
							<td><form id="edit_ps_overarea_from">
									<input name="state" type="hidden" id="edit_ps_state" /> <input type="button" value="保存"
										onclick="subEidt('edit_ps_overarea_from','ps','overarea')" />
								</form></td>
						</tr>
						<tr id="ps_business_tr">
							<td><input type="checkbox" id="ps_business_flag"
								${buFbusinessPS!=null?'checked=checked':''  } />业务补助</td>
							<td><form id="edit_ps_business_from">
									<input value="${buFbusinessPS.subsidyfee}" type="text" id="edit_ps_business_subsidyfee" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"
										name="subsidyfee" /> <input type="button" value="保存"
										onclick="subEidt('edit_ps_business_from','ps','business')" />
								</form></td>
						</tr>
						<tr id="ps_insertion_tr">
							<td><input type="checkbox" id="ps_insertion_flag"
								${fn:length(insertionListPS)>0&&(insertionListPS!=null)?'checked=checked':''  } />托单补助</td>
							<td>
								<div>
									<form id="edit_ps_insertion_from">
										<table id="ps_insertion_table" width="100%" border="0" cellspacing="1" cellpadding="0"
											class="table_2" id="gd_table">
											<tr id="thead">
												<td style="width: 10%" align="center"><input type="checkbox"
													onclick="AllTR('ps','insertion')" /></td>
												<td style="width: 20%" align="center">托单数下限</td>
												<td style="width: 20%" align="center">托单数上限</td>
												<td style="width: 20%" align="center">补助金额(元)</td>
												<td style="width: 30%" align="center">备注</td>
											</tr>
											<c:forEach items="${insertionListPS}" var="insertion">
												<tr>
													<td align='center'><input type='checkbox' /></td>
													<td><input style='width: 100%;' type='text' value="${insertion.mincount}" onblur="comparaTo($(this),'min')"
														id='mincount' name='mincount' /></td>
													<td><input style='width: 100%;' type='text' value="${insertion.maxcount}" onblur="comparaTo($(this),'max')"
														id='maxcount' name='maxcount' /></td>
													<td><input style='width: 100%;' type='text' value="${insertion.insertionfee}" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"
														id='insertionfee' name='insertionfee' /></td>
													<td><input style='width: 100%;' type='text' value="${insertion.remark}"
														id='remark' name='remark' /></td>
												</tr>
											</c:forEach>
										</table>
								</div> <input type="button" value="添加" onclick="addTROfinsertion('ps','insertion')" /> <input
								type="button" onclick="removeTR('ps','insertion')" value="移除" /> <input type="button"
								value="保存" onclick="subEidt('edit_ps_insertion_from','ps','insertion')" />
								</form>
							</td>
						</tr>
					</table>
				</div>
				<c:if test="${rule.type==1||rule.type==2}">
					<div title="提货费规则" id="edit_th">
						<table style="width: 100%; margin-top: 10px; font-size: 10px;" border="0" cellspacing="1"
							cellpadding="0">
							<tr id="th_basic_tr">
								<td style="width: 14%;"><input type="checkbox" id="th_basic_flag"
									${basicTH!=null||fn:length(basicListTH)>0?'checked=checked':'' } />基本派费</td>
								<td><span id="edit_th_basicno_from"> <input type='hidden' name='showflag'
										value='0' /> <select onchange="showflag('th_basic',$(this).val())" id="th_showflag_basic"> 
											<option ${th_showflag_basic=="yes" ? 'selected=selected' : '' } value="yes">按供货商区分</option>
											<option ${th_showflag_basic=="no" ? 'selected=selected' : '' } value="no">不按供货商区分</option>
									</select><span id="th_basic_no"> <input type="text" style="margin-top: -5px" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"
											name="basicPFfee" id="th_basicPFfee" value="${basicTH.basicPFfee }" />元 <input 
											type="button" value="保存" onclick="subEidt('edit_th_basicno_from','th','basicno')" />
									</span>
								</span>
									<div id="th_basic_yes">
										<form action="" id="edit_th_basic_from">

											<table id="th_basic_table" width="100%" border="0" cellspacing="1" cellpadding="0"
												class="table_2" id="gd_table">
												<tr id="thead">
													<th style="width: 10%" align="center"><input type="checkbox"
														onclick="AllTR('th','basic')" /></th>
													<th style="width: 25%" align="center">供货商</th>
													<th style="width: 25%" align="center">基本派费金额</th>
													<th style="width: 40%" align="center">备注</th>
												</tr>
												<c:forEach items="${basicListTH}" var="basic">
													<tr>
														<input type='hidden' name='showflag' value='1' />
														<td align='center'><input type='checkbox' /></td>
														<td><input type="text" id="${basic.customerid }" name="customerid"
															class="easyui-validatebox" style="width: 100%;" initDataType="TABLE"
															initDataKey="Customer" viewField="customername" saveField="customerid"
															value="${basic.customerid}" /></td>
														<td><input style='width: 100%;' type='text' id='basicPFfee' name='basicPFfee' onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"
															value="${basic.basicPFfee }" /></td>
														<td><input style='width: 100%;' type='text' id='remark' name='remark'
															value="${basic.remark }" /></td>
													</tr>
												</c:forEach>
											</table>
											<input type="button" value="添加" onclick="addTR('th','basic')" /> <input type="button"
												value="移除" onclick="removeTR('th','basic')" /> <input type="button" value="保存"
												onclick="subEidt('edit_th_basic_from','th','basic')" />
										</form>
									</div></td>
							</tr>
							<tr id="th_collection_tr">
								<td style="width: 14%;"><input type="checkbox" id="th_basic_flag"
									${collectionTH!=null||fn:length(collectionListTH)>0?'checked=checked':'' } />代收补助费</td>
								<td><span id="edit_th_collectionno_from"> <input type='hidden' name='showflag'
										value='0' /> <select onchange="showflag('th_collection',$(this).val())" id="th_showflag_collection">
											<option ${th_showflag_collection=="yes" ? 'selected=selected' : '' } value="yes">按供货商区分</option>
											<option ${th_showflag_collection=="no" ? 'selected=selected' : '' } value="no">不按供货商区分</option>
									</select><span id="th_collectionno_no"> <input type="text" style="margin-top: -5px" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"
											name="collectionnoPFfee" id="th_collectionnoPFfee" value="${collectionTH.collectionPFfee }" />元 <input
											type="button" value="保存" onclick="subEidt('edit_th_collectionno_from','th','collectionno')" />
									</span>
								</span>
									<div id="th_collection_yes">
										<form action="" id="edit_th_collection_from">
											<table id="th_collection_table" width="100%" border="0" cellspacing="1" cellpadding="0"
												class="table_2" id="gd_table">
												<tr id="thead">
													<th style="width: 10%" align="center"><input type="checkbox"
														onclick="AllTR('th','collection')" /></th>
													<th style="width: 25%" align="center">供货商</th>
													<th style="width: 25%" align="center">基本派费金额</th>
													<th style="width: 40%" align="center">备注</th>
												</tr>
												<c:forEach items="${collectionListTH}" var="collection">
													<tr>
														<input type='hidden' name='showflag' value='1' />
														<td align='center'><input type='checkbox' /></td>
														<td><input type="text" id="${collection.customerid }" name="customerid"
															class="easyui-validatebox" style="width: 100%;" initDataType="TABLE"
															initDataKey="Customer" viewField="customername" saveField="customerid"
															value="${collection.customerid}" /></td>
														<td><input style='width: 100%;' type='text' id='collectionPFfee' onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"
															name='collectionPFfee' value="${collection.collectionPFfee }" /></td>
														<td><input style='width: 100%;' type='text' id='remark' name='remark'
															value="${collection.remark }" /></td>
													</tr>
												</c:forEach>
											</table>
											<input type="button" value="添加" onclick="addTR('th','collection')" /> <input
												type="button" value="移除" onclick="removeTR('th','collection')" /> <input type="button"
												value="保存" onclick="subEidt('edit_th_collection_from','th','collection')" />
										</form>
									</div></td>
							</tr>
							<tr id="th_area_tr">
								<td><input type="checkbox" id="th_area_flag" />区域属性补助费</td>
								<td>
									<div>
										<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2"
											id="gd_table">
											<tr>
												<td style="width: 10%" align="center">城市</td>
												<td style="width: 20%" align="center">区/县</td>
												<td style="width: 15%" align="center">启用区域补助</td>
												<td style="width: 15%" align="center">启用超大补助</td>
												<td style="width: 15%" align="center">启用超重补助</td>
												<td style="width: 25%" align="center">备注</td>
											</tr>
											<tr id="th_111" onclick="showArea($(this),'edit_area_th_1','广东')">
												<td>广东</td>
												<td>广州</td>
												<td>是</td>
												<td>是</td>
												<td>否</td>
												<td>广东省是个好地方</td>
											</tr>
											<tr id="th_222" onclick="showArea($(this),'edit_area_th_2','北京')">
												<td>朝阳</td>
												<td>北京</td>
												<td>否</td>
												<td>是</td>
												<td>否</td>
												<td>我爱北京天安门</td>
											</tr>
										</table>
										<div id="th_area_div">
										<c:forEach items="${pfareaListTH}" var="area">
		<div id="edit_area_th_${area.id}">
		<table width="95%" border="0" style="margin-left: 5%" cellspacing="1" cellpadding="0"
			class="table_2" id="area_table">
			<tr>
				<td align="center">区域名称：</td>
				<td align="left"><span id="areaname">${area.areaname }</span><input type="hidden" id="areaid" value="${areafee.areaid}" /></td>
			</tr>
			<tr>
				<td align="left" nowrap="nowrap" valign="bottom"><input type="checkbox" />区域补助金额</td>
				<td align="left"><span><input value="${area.areafee }" type="text" id="areafee" style="margin-top: -5px" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"/>元</span></td>
			</tr>
			<tr>
				<td align="left" valign="bottom"><input type="checkbox" />超大补助</td>
				<td align="left"><table align="left" width="100%" border="0" cellspacing="1"
						cellpadding="0" class="table_2" id="th_overbig_${area.id }_table">
						<tr id="thead">
							<td style="width: 10%" align="center"><input type="checkbox" id="overbig_checbox"
								onclick="AllTR('th','overbig_${area.id }')" /></td>
							<td style="width: 20%" align="center">开始体积数(cm3)</td>
							<td style="width: 15%" align="center">截至体积数(cm3)</td>
							<td style="width: 15%" align="center">补助金额(元)</td>
							<td style="width: 25%" align="center">备注</td>
						</tr>
						<c:forEach items="${overbigMapTH[area.id]}" var="big">
						
						<tr>
						<td><input type="checkbox"/><input type="hidden" name="areaid" value="${area.id }" /><input type="hidden" name="id" value="${big.id }" /></td>
						<td><input style="width: 100%;" type="text"  id="mincount" name="mincount" value="${big.mincount}" onblur="comparaTo($(this),'max')"/></td>
						<td><input style="width: 100%;" type="text"  id="maxcount" name="maxcount" value="${big.maxcount}" onblur="comparaTo($(this),'min')"/></td>
						<td><input style="width: 100%;" type="text"  id="subsidyfee" name="subsidyfee" value="${big.subsidyfee}" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"/></td>
						<td><input style="width: 100%;" type="text"  id="remark" name="remark"  value="${big.remark}"/></td>
						</tr>
						</c:forEach>
					</table> <input type="button" id="overbig_add" value="添加" onclick="addTROfOverAreaEdit('th','overbig_${area.id}')" />
					<input id="overbig_remove" onclick="removeTR('th','overbig_${area.id}')" type="button" value="移除" />
					<input type="button" value="保存"
										onclick="subEidt('th_overbig_${area.id }_table','th','overbig')" />
					</td>
			</tr>
			<tr>
				<td align="left"><input type="checkbox" />超重补助</td>
				<td align="left"><table align="left" width="100%" border="0" cellspacing="1"
						cellpadding="0" class="table_2" id="th_overweight_${area.id}_table">
						<tr id="thead">
							<td style="width: 10%" align="center"><input type="checkbox" id="overweight_checbox"
								onclick="AllTR('th','overweight_${area.id}')" /></td>
							<td style="width: 20%" align="center">开始超重数(kg)</td>
							<td style="width: 15%" align="center">截至超重数(kg)</td>
							<td style="width: 15%" align="center">补助金额(元)</td>
							<td style="width: 25%" align="center">备注</td>
						</tr>
							<c:forEach items="${overweightMapPS[area.id]}" var="weight">
							
						<tr>
						<td><input type="checkbox"/><input type="hidden" name="areaid" value="${area.id }" /><input type="hidden" name="id" value="${weight.id }" /></td>
						<td><input style="width: 100%;" type="text"  id="mincount" name="mincount" value="${weight.mincount}" onblur="comparaTo($(this),'min')"/></td>
						<td><input style="width: 100%;" type="text"  id="maxcount" name="maxcount" value="${weight.maxcount}" onblur="comparaTo($(this),'max')"/></td>
						<td><input style="width: 100%;" type="text"  id="subsidyfee" name="subsidyfee" value="${weight.subsidyfee}" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"/></td>
						<td><input style="width: 100%;" type="text"  id="remark" name="remark"  value="${weight.remark}"/></td>
						</tr>
						</c:forEach>
					</table> <input type="button" id="overweight_add" value="添加"
					onclick="addTROfOverAreaEdit('th','overweight_${area.id}')" /> <input id="overweight_add"
					onclick="removeTR('th','overweight_${area.id}')" type="button" value="移除" />
							<input type="button" value="保存"
										onclick="subEidt('th_overweight_${area.id }_table','th','overweight')" />
					</td>
			</tr>
		</table>
	</div>
	</c:forEach>
										</div>
									</div>
								</td>
							</tr>
							<tr id="th_overarea_tr">
								<td><input type="checkbox" id="edit_th_state_checkbox" value="1"
									${overareaTH!=null&&overareaTH.state==1?'checked=checked':'' } />超区补助</td>
								<td><form id="edit_th_overarea_from">
										<input name="state" type="hidden" id="edit_th_state" /> <input type="button" value="保存"
											onclick="subEidt('edit_th_overarea_from','th','overarea')" />
									</form></td>
							</tr>
							<tr id="th_business_tr">
								<td><input type="checkbox" id="th_business_flag"
									${buFbusinessTH!=null?'checked=checked':''  } />业务补助</td>
								<td><form id="edit_th_business_from">
										<input value="${buFbusinessTH.subsidyfee}" type="text" id="edit_th_business_subsidyfee" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"
											name="subsidyfee" /> <input type="button" value="保存" 
											onclick="subEidt('edit_th_business_from','th','business')" />
									</form></td>
							</tr>
							<tr id="th_insertion_tr">
								<td><input type="checkbox" id="ps_insertion_flag"
									${fn:length(insertionListTH)>0&&(insertionListTH!=null)?'checked=checked':''  } />托单补助</td>
								<td>
									<div>
										<form id="edit_th_insertion_from">
											<table id="th_insertion_table" width="100%" border="0" cellspacing="1" cellpadding="0"
												class="table_2" id="gd_table">
												<tr id="thead">
													<td style="width: 10%" align="center"><input type="checkbox"
														onclick="AllTR('th','insertion')" /></td>
													<td style="width: 20%" align="center">托单数下限</td>
													<td style="width: 20%" align="center">托单数上限</td>
													<td style="width: 20%" align="center">补助金额(元)</td>
													<td style="width: 30%" align="center">备注</td>
												</tr>
												<c:forEach items="${insertionListTH}" var="insertion">
													<tr>
														<td align='center'><input type='checkbox' /></td>
														<td><input style='width: 100%;' type='text' value="${insertion.mincount}" onblur="comparaTo($(this),'min')"
															id='mincount' name='mincount' /></td>
														<td><input style='width: 100%;' type='text' value="${insertion.maxcount}" onblur="comparaTo($(this),'max')"
															id='maxcount' name='maxcount' /></td>
														<td><input style='width: 100%;' type='text' value="${insertion.insertionfee}" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"
															id='insertionfee' name='insertionfee' /></td>
														<td><input style='width: 100%;' type='text' value="${insertion.remark}"
															id='remark' name='remark' /></td>
													</tr>
												</c:forEach>
											</table>
									</div> <input type="button" value="添加" onclick="addTROfinsertion('th','insertion')" /> <input
									type="button" onclick="removeTR('th','insertion')" value="移除" /> <input type="button"
									value="保存" onclick="subEidt('edit_th_insertion_from','th','insertion')" />
									</form>
								</td>
							</tr>
						</table>
					</div>
				</c:if>
				<c:if test="${rule.type==2}">
					<div title="中转费费规则" id="edit_zz">
						<table style="width: 100%; margin-top: 10px; font-size: 10px;" border="0" cellspacing="1"
							cellpadding="0">
							<tr id="zz_basic_tr">
								<td style="width: 14%;"><input type="checkbox" id="zz_basic_flag"
									${basicZZ!=null||fn:length(basicListZZ)>0?'checked=checked':'' } />基本派费</td>
								<td><span id="edit_zz_basicno_from"> <input type='hidden' name='showflag'
										value='0' /> <select onchange="showflag('zz_basic',$(this).val())" id="zz_showflag_basic">
											<option ${zz_showflag_basic=="yes" ? 'selected=selected' : '' } value="yes">按供货商区分</option>
											<option ${zz_showflag_basic=="no" ? 'selected=selected' : '' } value="no">不按供货商区分</option>
									</select><span id="zz_basic_no"> <input type="text" style="margin-top: -5px" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"
											name="basicPFfee" id="zz_basicPFfee" value="${basicZZ.basicPFfee }" />元 <input
											type="button" value="保存" onclick="subEidt('edit_zz_basicno_from','zz','basicno')" />
									</span>
								</span>
									<div id="zz_basic_yes">
										<form action="" id="edit_zz_basic_from">

											<table id="zz_basic_table" width="100%" border="0" cellspacing="1" cellpadding="0"
												class="table_2" id="gd_table">
												<tr id="thead">
													<th style="width: 10%" align="center"><input type="checkbox"
														onclick="AllTR('zz','basic')" /></th>
													<th style="width: 25%" align="center">供货商</th>
													<th style="width: 25%" align="center">基本派费金额</th>
													<th style="width: 40%" align="center">备注</th>
												</tr>
												<c:forEach items="${basicListZZ}" var="basic">
													<tr>
														<input type='hidden' name='showflag' value='1' />
														<td align='center'><input type='checkbox' /></td>
														<td><input type="text" id="${basic.customerid }" name="customerid"
															class="easyui-validatebox" style="width: 100%;" initDataType="TABLE"
															initDataKey="Customer" viewField="customername" saveField="customerid"
															value="${basic.customerid}" /></td>
														<td><input style='width: 100%;' type='text' id='basicPFfee' name='basicPFfee' onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"
															value="${basic.basicPFfee }" /></td>
														<td><input style='width: 100%;' type='text' id='remark' name='remark'
															value="${basic.remark }" /></td>
													</tr>
												</c:forEach>
											</table>
											<input type="button" value="添加" onclick="addTR('zz','basic')" /> <input type="button"
												value="移除" onclick="removeTR('zz','basic')" /> <input type="button" value="保存"
												onclick="subEidt('edit_zz_basic_from','zz','basic')" />
										</form>
									</div></td>
							</tr>
							<tr id="zz_collection_tr">
								<td style="width: 14%;"><input type="checkbox" id="zz_basic_flag"
									${collectionZZ!=null||fn:length(collectionListZZ)>0?'checked=checked':'' } />代收补助费</td>
								<td><span id="edit_zz_collectionno_from"> <input type='hidden' name='showflag'
										value='0' /> <select onchange="showflag('zz_collection',$(this).val())" id="zz_showflag_collection">
											<option ${zz_showflag_collection=="yes" ? 'selected=selected' : '' } value="yes">按供货商区分</option>
											<option ${zz_showflag_collection=="no" ? 'selected=selected' : '' } value="no">不按供货商区分</option>
									</select><span id="zz_collection_no"> <input type="text" style="margin-top: -5px" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"
											name="collectionPFfee" id="zz_collectionPFfee" value="${collectionZZ.collectionPFfee }" />元 <input
											type="button" value="保存" onclick="subEidt('edit_zz_collectionno_from','zz','collectionno')" />
									</span>
								</span>
									<div id="zz_collection_yes">
										<form action="" id="edit_zz_collection_from">

											<table id="zz_collection_table" width="100%" border="0" cellspacing="1" cellpadding="0"
												class="table_2" id="gd_table">
												<tr id="thead">
													<th style="width: 10%" align="center"><input type="checkbox"
														onclick="AllTR('zz','collection')" /></th>
													<th style="width: 25%" align="center">供货商</th>
													<th style="width: 25%" align="center">基本派费金额</th>
													<th style="width: 40%" align="center">备注</th>
												</tr>
												<c:forEach items="${collectionListZZ}" var="collection">
													<tr>
														<input type='hidden' name='showflag' value='1' />
														<td align='center'><input type='checkbox' /></td>
														<td><input type="text" id="${collection.customerid }" name="customerid"
															class="easyui-validatebox" style="width: 100%;" initDataType="TABLE"
															initDataKey="Customer" viewField="customername" saveField="customerid"
															value="${collection.customerid}" /></td>
														<td><input style='width: 100%;' type='text' id='collectionPFfee' onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"
															name='collectionPFfee' value="${collection.collectionPFfee }" /></td>
														<td><input style='width: 100%;' type='text' id='remark' name='remark'
															value="${collection.remark }" /></td>
													</tr>
												</c:forEach>
											</table>
											<input type="button" value="添加" onclick="addTR('zz','collection')" /> <input
												type="button" value="移除" onclick="removeTR('zz','collection')" /> <input type="button"
												value="保存" onclick="subEidt('edit_zz_collection_from','zz','collection')" />
										</form>
									</div></td>
							</tr>
							<tr id="zz_area_tr">
								<td><input type="checkbox" id="zz_area_flag" />区域属性补助费</td>
								<td>
									<div>
										<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2"
											id="gd_table">
											<tr>
												<td style="width: 10%" align="center">城市</td>
												<td style="width: 20%" align="center">区/县</td>
												<td style="width: 15%" align="center">启用区域补助</td>
												<td style="width: 15%" align="center">启用超大补助</td>
												<td style="width: 15%" align="center">启用超重补助</td>
												<td style="width: 25%" align="center">备注</td>
											</tr>
											<tr id="zz_111" onclick="showArea($(this),'edit_area_zz_1','广东')">
												<td>广东</td>
												<td>广州</td>
												<td>是</td>
												<td>是</td>
												<td>否</td>
												<td>广东省是个好地方</td>
											</tr>
											<tr id="zz_222" onclick="showArea($(this),'edit_area_zz_1','北京')">
												<td>朝阳</td>
												<td>北京</td>
												<td>否</td>
												<td>是</td>
												<td>否</td>
												<td>我爱北京天安门</td>
											</tr>
										</table>
										<div id="zz_area_div">
										<c:forEach items="${pfareaListZZ}" var="area">
		<div id="edit_area_zz_${area.id}">
		<table width="95%" border="0" style="margin-left: 5%" cellspacing="1" cellpadding="0"
			class="table_2" id="area_table">
			<tr>
				<td align="center">区域名称：</td>
				<td align="left"><span id="areaname">${area.areaname }</span><input type="hidden" id="areaid" value="${areafee.areaid}" /></td>
			</tr>
			<tr>
				<td align="left" nowrap="nowrap" valign="bottom"><input type="checkbox" />区域补助金额</td>
				<td align="left"><span><input value="${area.areafee }" type="text" id="areafee" style="margin-top: -5px" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"/>元</span></td>
			</tr>
			<tr>
				<td align="left" valign="bottom"><input type="checkbox" />超大补助</td>
				<td align="left"><table align="left" width="100%" border="0" cellspacing="1"
						cellpadding="0" class="table_2" id="zz_overbig_${area.id }_table">
						<tr id="thead">
							<td style="width: 10%" align="center"><input type="checkbox" id="overbig_checbox"
								onclick="AllTR('zz','overbig_${area.id }')" /></td>
							<td style="width: 20%" align="center">开始体积数(cm3)</td>
							<td style="width: 15%" align="center">截至体积数(cm3)</td>
							<td style="width: 15%" align="center">补助金额(元)</td>
							<td style="width: 25%" align="center">备注</td>
						</tr>
						<c:forEach items="${overbigMapZZ[area.id]}" var="big">
						
						<tr>
						<td><input type="checkbox"/><input type="hidden" name="areaid" value="${area.id }" /><input type="hidden" name="id" value="${big.id }" /></td>
						<td><input style="width: 100%;" type="text"  id="mincount" name="mincount" value="${big.mincount}" onblur="comparaTo($(this),'min')"/></td>
						<td><input style="width: 100%;" type="text"  id="maxcount" name="maxcount" value="${big.maxcount}" onblur="comparaTo($(this),'max')"/></td>
						<td><input style="width: 100%;" type="text"  id="subsidyfee" name="subsidyfee" value="${big.subsidyfee}" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"/></td>
						<td><input style="width: 100%;" type="text"  id="remark" name="remark"  value="${big.remark}"/></td>
						</tr>
						</c:forEach>
					</table> <input type="button" id="overbig_add" value="添加" onclick="addTROfOverAreaEdit('zz','overbig_${area.id}')" />
					<input id="overbig_remove" onclick="removeTR('zz','overbig_${area.id}')" type="button" value="移除" />
					<input type="button" value="保存"
										onclick="subEidt('zz_overbig_${area.id }_table','zz','overbig')" />
					</td>
			</tr>
			<tr>
				<td align="left"><input type="checkbox" />超重补助</td>
				<td align="left"><table align="left" width="100%" border="0" cellspacing="1"
						cellpadding="0" class="table_2" id="zz_overweight_${area.id}_table">
						<tr id="thead">
							<td style="width: 10%" align="center"><input type="checkbox" id="overweight_checbox"
								onclick="AllTR('zz','overweight_${area.id}')" /></td>
							<td style="width: 20%" align="center">开始超重数(kg)</td>
							<td style="width: 15%" align="center">截至超重数(kg)</td>
							<td style="width: 15%" align="center">补助金额(元)</td>
							<td style="width: 25%" align="center">备注</td>
						</tr>
							<c:forEach items="${overweightMapZZ[area.id]}" var="weight">
							
						<tr>
						<td><input type="checkbox"/><input type="hidden" name="areaid" value="${area.id }" /><input type="hidden" name="id" value="${weight.id }" /></td>
						<td><input style="width: 100%;" type="text"  id="mincount" name="mincount" value="${weight.mincount}" onblur="comparaTo($(this),'min')"/></td>
						<td><input style="width: 100%;" type="text"  id="maxcount" name="maxcount" value="${weight.maxcount}" onblur="comparaTo($(this),'max')"/></td>
						<td><input style="width: 100%;" type="text"  id="subsidyfee" name="subsidyfee" value="${weight.subsidyfee}" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"/></td>
						<td><input style="width: 100%;" type="text"  id="remark" name="remark"  value="${weight.remark}"/></td>
						</tr>
						</c:forEach>
					</table> <input type="button" id="overweight_add" value="添加"
					onclick="addTROfOverAreaEdit('zz','overweight_${area.id}')" /> <input id="overweight_add"
					onclick="removeTR('zz','overweight_${area.id}')" type="button" value="移除" />
							<input type="button" value="保存"
									onclick="subEidt('zz_overweight_${area.id }_table','zz','overweight')" />
					</td>
			</tr>
		</table>
	</div>
	</c:forEach>
										</div>
									</div>
								</td>
							</tr>
							<tr id="zz_overarea_tr">
								<td><input type="checkbox" id="edit_zz_state_checkbox" value="1"
									${overareaZZ!=null&&overareaZZ.state==1?'checked=checked':'' } />超区补助</td>
								<td><form id="edit_zz_overarea_from">
										<input name="state" type="hidden" id="edit_zz_state" /> <input type="button" value="保存"
											onclick="subEidt('edit_th_overarea_from','zz','overarea')" />
									</form></td>
							</tr>
							<tr id="zz_business_tr">
								<td><input type="checkbox" id="zz_business_flag"
									${buFbusinessZZ!=null?'checked=checked':''  } />业务补助</td>
								<td><form id="edit_zz_business_from">
										<input value="${buFbusinessZZ.subsidyfee}" type="text" id="edit_zz_business_subsidyfee" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"
											name="subsidyfee" /> <input type="button" value="保存"
											onclick="subEidt('edit_zz_business_from','zz','business')" />
									</form></td>
							</tr>
							<tr id="zz_insertion_tr">
								<td><input type="checkbox" id="zz_insertion_flag"
									${fn:length(insertionListTH)>0&&(insertionListTH!=null)?'checked=checked':''  } />托单补助</td>
								<td>
									<div>
										<form id="edit_zz_insertion_from">
											<table id="zz_insertion_table" width="100%" border="0" cellspacing="1" cellpadding="0"
												class="table_2" id="gd_table">
												<tr id="thead">
													<td style="width: 10%" align="center"><input type="checkbox"
														onclick="AllTR('zz','insertion')" /></td>
													<td style="width: 20%" align="center">托单数下限</td>
													<td style="width: 20%" align="center">托单数上限</td>
													<td style="width: 20%" align="center">补助金额(元)</td>
													<td style="width: 30%" align="center">备注</td>
												</tr>
												<c:forEach items="${insertionListZZ}" var="insertion">
													<tr>
														<td align='center'><input type='checkbox' /></td>
														<td><input style='width: 100%;' type='text' value="${insertion.mincount}" onblur="comparaTo($(this),'min')"
															id='mincount' name='mincount' /></td>
														<td><input style='width: 100%;' type='text' value="${insertion.maxcount}" onblur="comparaTo($(this),'max')"
															id='maxcount' name='maxcount' /></td>
														<td><input style='width: 100%;' type='text' value="${insertion.insertionfee}" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"
															id='insertionfee' name='insertionfee' /></td>
														<td><input style='width: 100%;' type='text' value="${insertion.remark}"
															id='remark' name='remark' /></td>
													</tr>
												</c:forEach>
											</table>
									</div> <input type="button" value="添加" onclick="addTROfinsertion('zz','insertion')" /> <input
									type="button" onclick="removeTR('zz','insertion')" value="移除" /> <input type="button"
									value="保存" onclick="subEidt('edit_zz_insertion_from','zz','insertion')" />
									</form>
								</td>
							</tr>
						</table>
					</div>
				</c:if>
			</div>
			<!-- 	</form> -->
		</div>

	</c:if>



	<!-- 查询层显示 -->
	<div id="find" class="easyui-dialog" title="查寻条件" data-options="iconCls:'icon-save',modal:true"
		style="width: 700px; height: 220px;">
		<form action="${ctx}/paifeirule/list/1" method="post" id="searchForm">
			<table width="85%" border="0" cellspacing="1" cellpadding="0"
				style="margin-top: 20px; margin-right: 200px; font-size: 10px;">
				<tr>
					<td align="right" style="line-height: 30px">结算规则名称：</td>
					<td><input type="text" style="width: 100%;" name="name" value="${name}" /></td>
					<td align="right">状态：</td>
					<td><select style="width: 100%;" name="type">
							<option value="0">-全部-</option>
							<c:forEach items="${PaiFeiRuleTypeEnum}" var="t">
								<option value="${t.value }" ${t.value==type?'selected=selected':''}>${t.text }</option>
							</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td align="right" style="line-height: 30px">规则类型：</td>
					<td><select style="width: 100%;" name="state">
							<option value="0">-全部-</option>
							<c:forEach items="${PaiFeiRuleStateEnum}" var="s">
								<option value="${s.value }" ${s.value==state?'selected=selected':''}>${s.text }</option>
							</c:forEach>
					</select></td>
					<td align="right">备注：</td>
					<td><input type="text" style="width: 100%;" name="remark" value="${remark }" /></td>
				</tr>
				<tr>
					<td align="right" style="line-height: 30px">排序：</td>
					<td><select style="width: 60%;" name="orderby">
							<option value="name" ${orderby=='name'?'selected=selected':''}>结算规则名称</option>
							<option value="type" ${orderby=='type'?'selected=selected':''}>规则类型</option>
							<option value="state" ${orderby=='state'?'selected=selected':''}>状态</option>
					</select> <select style="width: 38%;" name="orderbyType">
							<option value="asc" ${orderbytype=='asc'?'selected=selected':'' }>升序</option>
							<option value="desc" ${orderbytype=='desc'?'selected=selected':'' }>降序</option>
					</select></td>
				</tr>
				<tr>
					<td style="line-height: 30px" colspan="4" align="center"><input class="input_button2"
						type="submit" value="查询" /> <input class="input_button2" type="button" value="关闭"
						onclick="$('#find').dialog('close');" /></td>
				</tr>
			</table>
		</form>
	</div>
	<div style="display: none;">
		<table width="95%" border="0" style="margin-left: 5%" cellspacing="1" cellpadding="0"
			class="table_2" id="area_table">
			<tr>
				<td align="center">区域名称：</td>
				<td align="left"><span id="areaname"></span><input type="hidden" id="areaid" value="1" /></td>
			</tr>
			<tr>
				<td align="left" nowrap="nowrap" valign="bottom"><input type="checkbox" />区域补助金额</td>
				<td align="left"><span><input type="text" id="areafee" style="margin-top: -5px" onblur="javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}"/>元</span></td>
			</tr>
			<tr id="overbigflagtrno">
				<td align="left" valign="bottom"><input type="checkbox" />超大补助</td>
				<td align="left"><table align="left" width="100%" border="0" cellspacing="1"
						cellpadding="0" class="table_2" id="overbig_table">
						<tr id="thead">
							<td style="width: 10%" align="center"><input type="checkbox" id="overbig_checbox"
								onclick="AllTR('ps','overbig')" /></td>
							<td style="width: 20%" align="center">开始体积数(cm3)</td>
							<td style="width: 15%" align="center">截至体积数(cm3)</td>
							<td style="width: 15%" align="center">补助金额(元)</td>
							<td style="width: 25%" align="center">备注</td>
						</tr>
					</table> <input type="button" id="overbig_add" value="添加" onclick="addTROfOverArea('ps','overbig')" />
					<input id="overbig_remove" onclick="removeTR('ps','overbig')" type="button" value="移除" /></td>
			</tr>
			<tr id="overbigflagtr">
				<td align="left" valign="bottom"><input type="checkbox"  id="overbigflag"/>超大补助</td>
			</tr>
			<tr >
				<td align="left"><input type="checkbox" />超重补助</td>
				<td align="left"><table align="left" width="100%" border="0" cellspacing="1"
						cellpadding="0" class="table_2" id="overweight_table">
						<tr id="thead">
							<td style="width: 10%" align="center"><input type="checkbox" id="overweight_checbox"
								onclick="AllTR('ps','overweight')" /></td>
							<td style="width: 20%" align="center">开始超重数(kg)</td>
							<td style="width: 15%" align="center">截至超重数(kg)</td>
							<td style="width: 15%" align="center">补助金额(元)</td>
							<td style="width: 25%" align="center">备注</td>
						</tr>
					</table> <input type="button" id="overweight_add" value="添加"
					onclick="addTROfOverArea('ps','overweight')" /> <input id="overweight_add"
					onclick="removeTR('ps','overweight')" type="button" value="移除" /></td>
			</tr>
		</table>
	</div>
	<form action="${pageContext.request.contextPath}/paifeirule/list/1" id="edit_form" method="post">
		<input type="hidden" id="edit_ruleid" name="edit_ruleid" />
	</form>
		<input type="hidden" id="edit_ruletype" name="edit_ruletype" value="${rule.type }" />
</body>
</html>


