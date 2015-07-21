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


<script type="text/javascript">
 

	$(function() {
		var dmpurl = $("#dmpurl").val();
		$('#find').dialog('close');
		$('#add').dialog('close');
		$('#edit').dialog('close');
		//$("[id*=_yes]").attr('style','display:none');
		$("[id*=_no]").attr('style', 'display:none');
		$('#edit_rule').panel({ 
			onClose: function(event, ui) {
			window.location=dmpurl+"/paifeirule/list/1";
			}
		});
		showflag('ps_basic',$("#ps_showflag_basic").val());
		showflag('ps_collection',$("#ps_showflag_collection").val());
	});
	function addInit() {
		//无处理
	}
	function allchecked() {
		var ids = "";
		$("[id=id]").each(function() {
			if ($(this)[0].checked == true) {
				ids += "," + $(this).val();
			}
		});
		if (ids.indexOf(',') != -1) {
			ids = ids.substr(1);
		}
		var dmpurl = $("#dmpurl").val();
		if (window.confirm("确定要删除吗！") && ids.length > 0) {
			$.ajax({
				type : "post",
				url : dmpurl + "/paifeirule/delete",
				data : {
					"ids" : ids
				},
				dataType : "json",
				success : function(data) {
					if (data.counts > 0) {
						alert("成功移除" + data.counts + "记录");
					}
					$("#find form").submit();
				}
			});
		}
	}
	function checkall() {
		var checked = $("#all")[0].checked;
		$("[id=id]").each(function() {
			var e = $(this)[0];
			if (checked == 'true' || checked == 'checked') {
				e['checked'] = checked;
				//$(e).attr('checked',checked);
			} else {
				//$(e).removeAttr('checked');
				e['checked'] = checked;
			}
		});
	}
	function AllTR(pf, type) {
		var flag = $("#" + pf + "_" + type + "_table tr[id=thead] [type='checkbox']")[0].checked;
		$("#" + pf + "_" + type + "_table tr [type='checkbox']").each(function() {
			$(this)[0].checked = flag;
		});
	}
	function removeTR(pf, type) {
		$("#" + pf + "_" + type + "_table tr[id!=thead] [checked]").each(function() {
			$(this).parent().parent().remove();
		});
	}
	var i = 0;
	function addTR(pf, type) {
		i++;
		var customerid = "customerid" + i;
		//var customername="customername";
		var customer = "<input type='text' id='" + customerid + "' name='customerid' class='easyui-validatebox' style='width: 100%;'"
				+ "initDataType='TABLE'" + "initDataKey='Customer' " + "viewField='customername'" + "saveField='customerid'" + "/>";
		var PFfee = "<input style='width: 100%;' type='text'  id='" + type + "PFfee' name='" + type + "PFfee'/>";
		var remark = "<input style='width: 100%;' type='text'  id='remark' name='remark'/>";
		var tr = "<tr><input type='hidden' name='showflag' value='1'/>" + "<td  align='center'><input type='checkbox'/></td>" + "<td  align='center'>" + customer + "</td>" + "<td  align='center'>"
				+ PFfee + "</td>" + "<td  align='center'>" + remark + "</td>" + "</tr>";
		$("#" + pf + "_" + type + "_table").append(tr);
		//  tr.appendTo(basictr);   
		initDynamicSelect(customerid, 'TABLE');
	}
	function addTROfinsertion(pf, type) {
		var mincount = "<input style='width: 100%;' type='text'  id='mincount' name='mincount'/>";
		var maxcount = "<input style='width: 100%;' type='text'  id='maxcount' name='maxcount'/>";
		var insertionfee = "<input style='width: 100%;' type='text'  id='insertionfee' name='insertionfee'/>";
		var remark = "<input style='width: 100%;' type='text'  id='remark' name='remark'/>";
		var tr = "<tr>" + "<td  align='center'><input type='checkbox'/></td>" + "<td  align='center'>" + mincount + "</td>" + "<td  align='center'>"
				+ maxcount + "</td>" + "<td  align='center'>" + insertionfee + "</td>" + "<td  align='center'>" + remark + "</td>" + "</tr>";
		$("#" + pf + "_" + type + "_table").append(tr);
	}
	function addTROfOverArea(pf, type) {
		var mincount = "<input style='width: 100%;' type='text'  id='mincount' name='mincount'/>";
		var maxcount = "<input style='width: 100%;' type='text'  id='maxcount' name='maxcount'/>";
		var subsidyfee = "<input style='width: 100%;' type='text'  id='subsidyfee' name='subsidyfee'/>";
		var remark = "<input style='width: 100%;' type='text'  id='remark' name='remark'/>";
		var tr = "<tr>" + "<td  align='center'><input type='checkbox'/></td>" + "<td  align='center'>" + mincount + "</td>" + "<td  align='center'>"
				+ maxcount + "</td>" + "<td  align='center'>" + subsidyfee + "</td>" + "<td  align='center'>" + remark + "</td>" + "</tr>";
		$("#" + pf + "_" + type + "_table").append(tr);
	}

	function sub() {
		var dmpurl = $("#dmpurl").val();
		if ($("#s_ruletypeid").val() == 1) {
			var tabs = new Array("ps", "th");
		}
		if ($("#s_ruletypeid").val() == 2) {
			var tabs = new Array("ps", "th", "zz");
		}
		if ($("#s_ruletypeid").val() == 3) {
			var tabs = new Array("ps");
		}
		var jsontab = {};
		for (var i = 0; i < tabs.length; i++) {
			var basic = getJson(tabs[i], 'basic');
			var collection = getJson(tabs[i], 'collection');
			var insertion = getArrayinsertion(tabs[i], 'insertion');
			var subsidyfee = getJsonOfsubsidyfee(tabs[i], 'business');
			var area = getJsonOfArea(tabs[i]);
			var overarea = getOverarea(tabs[i], 'overarea');
			var json = {};
			if (!jQuery.isEmptyObject(collection)) {
				json.collection = collection;
			}
			if (!jQuery.isEmptyObject(basic)) {
				json.basic = basic;
			}
			if (insertion.length > 0) {
				json.insertion = insertion;
			}
			if ("" != subsidyfee) {
				json.subsidyfee = subsidyfee;
			}
			if (!jQuery.isEmptyObject(area)) {
				json.area = area;
			}
			if ("" != overarea) {
				json.overarea = overarea;
			}
			if (!jQuery.isEmptyObject(json)) {
				jsontab[tabs[i]] = json;
			}
		}
		//alert(JSON.stringify(jsontab));
		 $.ajax({
			type : "post",
			url : dmpurl + "/paifeirule/save",
			data : {
				"json" : JSON.stringify(jsontab),
				"pfruletypeid" : $("#s_ruletypeid").val(),
				"pfruleid" : $("#s_ruleid").val()
			},
			dataType : "json",
			success : function(obj) {
				if (obj.errorCode == 1) {
					$('#save').dialog('close');
				}
				alert(obj.error);
			}
		}); 
	}
	function getOverarea(tab, type) {
		if ($("#" + tab + "_" + type + "_tr input[type='checkbox']")[0].checked == true) {
			return "1";
		} else {
			return "";
		}
	}
	function getJson(tab, type) {
		var json = {};
		if ($("#" + tab + "_" + type + "_flag")[0].checked) {
			if ($("#" + tab + "_" + type + "_tr select").val() == 'yes') {
				var customers = new Array();
				$("#" + tab + "_" + type + "_table tr[id!=thead]").each(function() {
					customers.push($(this).serializeObject());
				});
				json.PFfees = customers;
				json.showflag = 'yes';
			} else {
				json.showflag = 'no';
				json.PFfee = $("#" + tab + "_" + type + "PFfee").val();
			}
		}
		return json;
	}
	function getArrayinsertion(tab, type) {
		var json = {};
		var insertion = new Array();
		if ($("#" + tab + "_" + type + "_flag")[0].checked) {

			$("#" + tab + "_" + type + "_table tr[id!=thead]").each(function() {
				insertion.push($(this).serializeObject());
			});
			json.insertion = insertion;

		}
		return insertion;
	}
	function getJsonOfsubsidyfee(tab, type) {
		//var json = {};
		if ($("#" + tab + "_" + type + "_flag")[0].checked) {

			return $("#" + tab + "_" + type + "_subsidyfee").val();
		}
		return "";
	}
	function getJsonOfArea(tab) {
		var jsonArea={};
			var areas=new Array();
		if ($("#" + tab + "_area_flag")[0].checked) {
			$("#" + tab + "_area_div>table").each(function(){
			var json = {};
			 var areafee=$(this).find("#areafee").val();
			 var overbig=new Array();
			 $(this).find("[id*=" + tab + "_overbig] tr[id!=thead]").each(function() {
				 overbig.push($(this).serializeObject());
				});
			 var overweight=new Array();
			 $(this).find("[id*=" + tab + "_overweight] tr[id!=thead]").each(function() {
				 overweight.push($(this).serializeObject());
				});
			 json.areafee=areafee;
			 json.overbig=overbig;
			 json.overweight=overweight;
			
			areas.push(json);
		});
			jsonArea=areas;
		}
		return jsonArea;
	}
	function showflag(flag, val) {
		$("#" + flag + "_yes").attr('style', 'display:none');
		$("#" + flag + "_no").attr('style', 'display:none');
		$("#" + flag + "_" + val).removeAttr('style');
	}
	function addArea(tab,areaname,areaid)
	{	if($("tr [id="+tab+"_"+areaid+"]")[0].style.background==undefined||$("tr [id="+tab+"_"+areaid+"]")[0].style.background==''){
		$("tr [id="+tab+"_"+areaid+"]")[0].style.background='yellow';
		var $area_table=$("#area_table").clone();
		$area_table[0].id=tab+"_area_table_"+areaid;
		$area_table.find("#areaid").val(areaid);
		$area_table.find("#areaname").text(areaname);
		var overbig="overbig"+areaid;
		var overweight="overweight"+areaid;
		$area_table.find("#overbig_table")[0].id=tab+"_"+overbig+"_table";
		$area_table.find("#overweight_table")[0].id=tab+"_"+overweight+"_table";
		
		$area_table.find("#overbig_checbox").attr('onclick','AllTR("'+tab+'","'+overbig+'")');
		$area_table.find("#overweight_checbox").attr('onclick','AllTR("'+tab+'","'+overweight+'")');
		
		$area_table.find("#overbig_remove").attr('onclick','removeTR("'+tab+'","'+overbig+'")');
		$area_table.find("#overweight__remove").attr('onclick','removeTR("'+tab+'","'+overweight+'")');
		
		$area_table.find("#overweight_add").attr('onclick','addTROfOverArea("'+tab+'","'+overweight+'")');
		$area_table.find("#overbig_add").attr('onclick','addTROfOverArea("'+tab+'","'+overbig+'")');
		$("#"+tab+"_area_div").append($area_table);
	}
	else if($("tr [id="+tab+"_"+areaid+"]")[0].style.background=='yellow')
		{
		$("tr [id="+tab+"_"+areaid+"]")[0].style.background='';
		$("#"+tab+"_area_div table[id="+tab+"_area_table_"+areaid+"]").remove();
		}
	}
	function  editRule(ruleid)
	{	$("#rule_table tr[id!=thead]").each(function(){
		$(this)[0].style.background='';
	});
		if($("#"+ruleid)[0].style.background==undefined||$("#"+ruleid)[0].style.background=='')
		{
			$("#"+ruleid)[0].style.background='yellow';
			$("#edit_ruleid").val(ruleid);
			$("#edit_form").submit();
		}

/* 		 $.ajax({
				type : "post",
				url : dmpurl + "/paifeirule/edit",
				data : {
					"pfruleid" : ruleid
				},
				dataType : "json",
				success : function(obj) {
					if (obj.errorCode == 1) {
						$('#save').dialog('close');
					}
					alert(obj.error);
				}
			});  */
	}
	function subEidt(formId,tab,edittype)
	{    var dmpurl = $("#dmpurl").val();
		 var json={};
		 var ruleid=$("#edit_rule_id").val();
		 var typeid=$("#edit_ruletype").val();
		 json=$("#"+formId).serializeObject();
		if(edittype=="basic"){
			var objs=new Array();
			$("#"+formId+" tr[id!=thead]").each(function() {
				 objs.push($(this).serializeObject());
				// objs.
			});
			json=objs;
		}
	//alert(JSON.stringify(json));
		 $.ajax({
				type : "post",
				url : dmpurl + "/paifeirule/edittype",
				data : {
					"json":JSON.stringify(json),
					"type":tab+"_"+edittype,
					"rulejson":JSON.stringify($("#edit_rule_from").serializeObject())
				},
				dataType : "json",
				success : function(obj) {
		
				}
			}); 
	}
	function subBasicOrCollection(formId,edittype,tab)
	{    var dmpurl = $("#dmpurl").val();
		var json={};
		
		var objs=new Array();
			$("#"+formId+" tr[id!=thead]").each(function() {
				 objs.push($(this).serializeObject());
				// objs.
			});
			json=objs;
			
	//alert(JSON.stringify(json));
		 $.ajax({
				type : "post",
				url : dmpurl + "/paifeirule/edittype",
				data : {
					"json":JSON.stringify(json),
					"type":tab+"_"+edittype
				},
				dataType : "json",
				success : function(obj) {
		
				}
			}); 
	}
</script>
</head>

<body style="background: #eef9ff">

	<div class="right_box">
		<div class="inputselect_box">
			<table style="width: 60%">
				<tr>
					<td><input class="input_button2" type="button" onclick="$('#add').dialog('open')"
						value="新增" /> <input class="input_button2" type="button" value="查看/修改" /> <input
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
			<div style="overflow: auto;">
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
							<td align="center" valign="middle"><input type="checkbox" id="id" value="${pf.pfruleNO}" /></td>
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
					<td><input type="text" name="jushouPFfee" /></td>
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
							type="button" class="input_button2" value="保存" onclick="sub()" /></td>
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
						<td><input type="text" id="s_jushouPFfee" value="${rule.jushouPFfee }" disabled="disabled" /></td>
					</tr>
					<tr>
						<td style="line-height: 30px" align="right">结算规则名称：</td>
						<td colspan="5"><input style="width: 100%" type="text" id="s_name" value="${rule.name}" disabled="disabled"/></td>
					</tr>
					<tr>
						<td style="line-height: 30px" align="right" valign="middle">备注：</td>
						<td id="s_remark" colspan="5"><textarea rows="3" style="width: 100%; resize: none;" disabled="disabled">${rule.remark }</textarea></td>
					</tr>
				</table>
				<div id="tt" class="easyui-tabs" style="height:450px; overflow: scroll;">
					<div title=" 配送费规则" >
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
											<tr id="ps_111" onclick="addArea('ps','广东','111')" >
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
										<div id="ps_area_div">
										
											</div>
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
									<td><input type="checkbox" id="th_area_flag"  />区域属性补助费</td>
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
											<tr id="th_111" onclick="addArea('th','广东','111')" >
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
										<div id="th_area_div">
										
											</div>
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
									<td><input type="checkbox" id="zz_area_flag"  />区域属性补助费</td>
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
											<tr id="zz_111" onclick="addArea('zz','广东','111')" >
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
										<div id="zz_area_div">
										
											</div>
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
		<div id="edit_rule" class="easyui-dialog" title="编辑/修改" data-options="iconCls:'icon-save',modal:true" style="width: 800px;">
			<form action="" method="post" id="edit_rule_from">
				<input type="hidden" id="edit_rule_id" name="id" value="${rule.id}" />
				
				<table width="85%" border="0" cellspacing="1" cellpadding="0"
					style="margin-top: 10px; margin-left: 25px; font-size: 10px;">
					<tr>
					<td></td>
						<td ><input
							type="button" class="input_button2" value="返回" onclick="$('#edit_rule').dialog('close');" /> </td>
					</tr>
					<tr>
						<td style="line-height: 30px" align="right">规则类型：</td>
						<td><%-- <select style="width: 100%;" name="type" id="edit_type" disabled="disabled">
								<c:forEach items="${PaiFeiRuleTypeEnum}" var="type">
									<option value="${type.value}" ${type.value==rule.type ? 'selected=selected' : '' }>${type.text}</option>
								</c:forEach>
						</select> --%>
						 <input type="text" name="type" id="edit_ruletype" readonly="true"  class="easyui-validatebox" 
						 	data-options="width:150,prompt: '规则类型'"
						 	initDataType="ENUM" 
						 	initDataKey="cn.explink.enumutil.PaiFeiRuleTypeEnum"
						 	viewField="text" 
						 	saveField="value"
						 	readonly="true"
						 	value="${rule.type}"
							/>
						</td>
						<td align="right">状态：</td>
						<td><%-- <select style="width: 100%;" id="s_state" name="state" >
								<c:forEach items="${PaiFeiRuleStateEnum}" var="state">
									<option value="${state.value}" ${state.value==rule.state ? 'selected=selected' : '' }>${state.text}</option>
								</c:forEach>
						</select> --%>
						 <input type="text" name="state" id="edit_state" readonly="true"  class="easyui-validatebox" 
						 	data-options="width:150,prompt: '状态'"
						 	initDataType="ENUM" 
						 	initDataKey="cn.explink.enumutil.PaiFeiRuleStateEnum"
						 	viewField="text" 
						 	saveField="value"
						 	value="${rule.state}"
						 	readonly="true"
							/>
						</td>
						<td align="right">拒收派费(元)：</td>
						<td><input type="text" id="edit_jushouPFfee" name="jushouPFfee" value="${rule.jushouPFfee }"  /></td>
					</tr>
					<tr>
						<td style="line-height: 30px" align="right">结算规则名称：</td>
						<td colspan="5"><input style="width: 100%" type="text" id="edit_name" name="name" value="${rule.name}" /></td>
					</tr>
					<tr>
						<td style="line-height: 30px" align="right" valign="middle">备注：</td>
						<td id="edit_remark" colspan="5"><textarea rows="3" name="remark" style="width: 100%; resize: none;" >${rule.remark }</textarea></td>
					</tr>
					<tr><td><td><input
							type="button" class="input_button2" value="保存" onclick="subEidt('edit_rule_from','','rule')" /></td></tr>
				</table>
				</form>
				<div id="tt" class="easyui-tabs" style="height:450px; overflow: scroll;">
					<div title=" 配送费规则" >
						<table style="width: 100%; margin-top: 10px; font-size: 10px;" border="0" cellspacing="1"
							cellpadding="0">
							<tr id="ps_basic_tr">
								<td style="width: 14%;"><input type="checkbox" id="ps_basic_flag"  ${basicPS!=null||basicListPS!=null?'checked=checked':'' } />基本派费</td>
								<td><select onchange="showflag('ps_basic',$(this).val())" id="ps_showflag_basic">
										<option  ${ps_showflag_basic=="yes" ? 'selected=selected' : '' } value="yes">按供货商区分</option>
										<option  ${ps_showflag_basic=="no" ? 'selected=selected' : '' } value="no">不按供货商区分</option>
								</select><span id="ps_basic_no"><input type="text" style="margin-top: -5px" name="basicPFfee"
										id="ps_basicPFfee" value="${basicPS.basicPFfee }"/>元</span>
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
											<c:forEach items="${basicListPS}" var="basic" > 
											<tr>
											<input type='hidden' name='showflag' value='1'/>
											<td  align='center'><input type='checkbox'/></td>
											<td>
											<input type="text" id="${basic.customerid }" name="customerid" class="easyui-validatebox" style="width: 100%;"		
											initDataType="TABLE" 
											initDataKey="Customer" 
											viewField="customername"
											saveField="customerid"
											value="${basic.customerid}"
											/>
											</td>
											<td><input style='width: 100%;' type='text'  id='basicPFfee' name='basicPFfee' value="${basic.basicPFfee }"/></td>
											<td><input style='width: 100%;' type='text'  id='remark' name='remark' value="${basic.remark }"/></td>
											</tr>
											</c:forEach>
										</table>
										<input type="button" value="添加" onclick="addTR('ps','basic')" /> 
										<input type="button" value="移除" onclick="removeTR('ps','basic')" />
										<input type="button" value="保存" onclick="subEidt('edit_ps_basic_from','ps','basic')"/>
									</form></div></td>
							</tr>
							<tr id="ps_collection_tr">
								<td><input type="checkbox" id="ps_collection_flag" ${collectionPS!=null||collectionListPS!=null?'checked=checked':'' }/>代收补助费</td>
								<td><select onchange="showflag('ps_collection',$(this).val())" id="ps_showflag_collection">
										<option ${ps_showflag_collection=="yes" ? 'selected=selected' : '' } value="yes">按供货商区分</option>
										<option ${ps_showflag_collection=="no" ? 'selected=selected' : '' } value="no">不按供货商区分</option>
								</select><span id="ps_collection_no"><input type="text" id="ps_collectionPFfee"
										name="ps_collectionPFfee" value="${collectionPS.collectionPFfee }" style="margin-top: -5px" />元</span>
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
											<c:forEach items="${collectionListPS}" var="collection" > 
											<tr>
											<td  align='center'><input type='checkbox'/></td>
											<td>
											<input type="text" id="${collection.customerid }" name="customerid" class="easyui-validatebox" style="width: 100%;"		
											initDataType="TABLE" 
											initDataKey="Customer" 
											viewField="customername"
											saveField="customerid"
											value="${collection.customerid}"
											/>
											</td>
											<td><input style='width: 100%;' type='text'  id='collectionPFfee' name='collectionPFfee' value="${collection.collectionPFfee}"/></td>
											<td><input style='width: 100%;' type='text'  id='remark' name='remark' value="${collection.remark}"/></td>
											</tr>
											</c:forEach>
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
											<tr id="ps_111" onclick="addArea('ps','广东','111')" >
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
										<div id="ps_area_div">
										
											</div>
									</div> 
								</td>
							</tr>
							<tr id="ps_overarea_tr">
								<td><input type="checkbox" value="1" ${overareaPS!=null?'checked=checked':'' }/>超区补助</td>
							</tr>
							<tr id="ps_business_tr">
								<td><input type="checkbox" id="ps_business_flag" ${buFbusinessPS!=null?'checked=checked':''  } />业务补助</td>
								<td><input value="${buFbusinessPS.subsidyfee}" type="text" id="ps_business_subsidyfee" /></td>
							</tr>
							<tr id="ps_insertion_tr">
								<td><input type="checkbox" id="ps_insertion_flag"  ${insertionListPS!=null?'checked=checked':''  }/>托单补助</td>
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
											<c:forEach items="${insertionListPS}" var="insertion">
											<tr>
											<td  align='center'><input type='checkbox'/></td>
											<td><input style='width: 100%;' type='text' value="${insertion.mincount}"  id='mincount' name='mincount'/></td>
											<td><input style='width: 100%;' type='text' value="${insertion.maxcount}" id='maxcount' name='maxcount'/></td>
											<td><input style='width: 100%;' type='text' value="${insertion.insertionfee}" id='insertionfee' name='insertionfee'/></td>
											<td><input style='width: 100%;' type='text' value="${insertion.remark}" id='remark' name='remark'/></td>
											</tr>
											</c:forEach>
										</table>
									</div> <input type="button" value="添加" onclick="addTROfinsertion('ps','insertion')" /> <input
									type="button" onclick="removeTR('ps','insertion')" value="移除" />
								</td>
							</tr>
						</table>
					</div>
					<c:if test="${rule.type==1||rule.type==2}">
						<div title="提货费规则" id="edit_th">
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
									<td><input type="checkbox" id="th_area_flag"  />区域属性补助费</td>
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
											<tr id="th_111" onclick="addArea('th','广东','111')" >
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
										<div id="th_area_div">
										
											</div>
										</div> 
									</td>
								</tr>
								<tr id="th_overarea_tr">
									<td><input type="checkbox" value="1" />超区补助</td>
								</tr>
								<tr id="th_business_tr">
									<td><input type="checkbox" id="th_business_flag" />业务补助</td>
									<td><input type="text" id="th_business_subsidyfee" value="${}"/></td>
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
						<div title="中转费规则" id="edit_zz">
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
									<td><input type="checkbox" id="zz_area_flag"  />区域属性补助费</td>
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
											<tr id="zz_111" onclick="addArea('zz','广东','111')" >
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
										<div id="zz_area_div">
										
											</div>
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
							<option value="name" ${name=='name'?'selected=selected':''}>结算规则名称</option>
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
	<table  width="95%" border="0" style="margin-left: 5%" cellspacing="1" cellpadding="0" class="table_2"
											id="area_table">
											<tr>
												<td align="center" >区域名称：</td>
												<td align="left"><span id="areaname"></span><input type="hidden" id="areaid" value="1"/> </td>
											</tr>
											<tr>
												<td align="left" nowrap="nowrap" valign="bottom"><input type="checkbox" />区域补助金额</td>
												<td align="left" ><span><input type="text" id="areafee"  style="margin-top: -5px" />元</span></td>
											</tr>
											<tr>
												<td align="left"  valign="bottom"><input type="checkbox" />超大补助</td>
												<td align="left" ><table align="left" width="100%" border="0"  cellspacing="1" cellpadding="0" class="table_2"
													id="overbig_table">
														<tr id="thead">
															<td style="width: 10%" align="center"><input type="checkbox" id="overbig_checbox" onclick="AllTR('ps','overbig')"/></td>
															<td style="width: 20%" align="center">开始体积数(cm3)</td>
															<td style="width: 15%" align="center">截至体积数(cm3)</td>
															<td style="width: 15%" align="center">补助金额(元)</td>
															<td style="width: 25%" align="center">备注</td>
														</tr>
													</table> 
													<input type="button" id="overbig_add" value="添加"onclick="addTROfOverArea('ps','overbig')" /> <input id="overbig_remove" onclick="removeTR('ps','overbig')"  type="button" value="移除" />
												</td>
											</tr>
											<tr>
												<td align="left" ><input type="checkbox" />超重补助</td>
												<td align="left" ><table align="left"  width="100%" border="0"  cellspacing="1" cellpadding="0" class="table_2"
													id="overweight_table">
														<tr id="thead">
															<td style="width: 10%" align="center"><input type="checkbox" id="overweight_checbox" onclick="AllTR('ps','overweight')"  /></td>
															<td style="width: 20%" align="center">开始超重数(kg)</td>
															<td style="width: 15%" align="center">截至超重数(kg)</td>
															<td style="width: 15%" align="center">补助金额(元)</td>
															<td style="width: 25%" align="center">备注</td>
														</tr>
													</table> 
													<input type="button" id="overweight_add" value="添加" onclick="addTROfOverArea('ps','overweight')" /> <input id="overweight_add" onclick="removeTR('ps','overweight')" type="button" value="移除" />
												</td>
											</tr>
											</table>
	</div>
	<form action="1" id="edit_form">
	<input type="hidden" id="edit_ruleid" name="edit_ruleid"/>
	</form>
</body>
</html>


