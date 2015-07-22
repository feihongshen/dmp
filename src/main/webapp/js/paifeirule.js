function isFee(nums) {
		var exp = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;
		return exp.test(nums);
	}
var  fee_check="onblur=javascript:if(!isFee($(this).val())){alert('输入有误');$(this).val('0.00');}";
	$(function() {
		var dmpurl = $("#dmpurl").val();
		$('#find').dialog('close');
		$('#add').dialog('close');
		$('#edit').dialog('close');
		//$("[id*=_yes]").attr('style','display:none');
		$("[id*=_no]").attr('style', 'display:none');
		$('#edit_rule').panel({
			onClose : function(event, ui) {
				window.location = dmpurl + "/paifeirule/list/1";
			}
		});
		showflag('ps_basic', $("#ps_showflag_basic").val());
		showflag('ps_collection', $("#ps_showflag_collection").val());
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
		var PFfee = "<input "+fee_check+" style='width: 100%;' type='text'  id='" + type + "PFfee' name='" + type + "PFfee'/>";
		var remark = "<input style='width: 100%;' type='text'  id='remark' name='remark'/>";
		var tr = "<tr><input type='hidden' name='showflag' value='1'/>" + "<td  align='center'><input type='checkbox'/></td>"
				+ "<td  align='center'>" + customer + "</td>" + "<td  align='center'>" + PFfee + "</td>" + "<td  align='center'>" + remark + "</td>"
				+ "</tr>";
		$("#" + pf + "_" + type + "_table").append(tr);
		//  tr.appendTo(basictr);   
		initDynamicSelect(customerid, 'TABLE');
	}
	function addTROfinsertion(pf, type) {
		var mincount = "<input style='width: 100%;' type='text'  id='mincount' name='mincount'/>";
		var maxcount = "<input style='width: 100%;' type='text'  id='maxcount' name='maxcount'/>";
		var insertionfee = "<input "+fee_check+" style='width: 100%;' type='text'  id='insertionfee' name='insertionfee'/>";
		var remark = "<input style='width: 100%;' type='text'  id='remark' name='remark'/>";
		var tr = "<tr>" + "<td  align='center'><input type='checkbox'/></td>" + "<td  align='center'>" + mincount + "</td>" + "<td  align='center'>"
				+ maxcount + "</td>" + "<td  align='center'>" + insertionfee + "</td>" + "<td  align='center'>" + remark + "</td>" + "</tr>";
		$("#" + pf + "_" + type + "_table").append(tr);
	}
	function addTROfOverArea(pf, type) {
		var mincount = "<input style='width: 100%;' type='text'  id='mincount' name='mincount'/>";
		var maxcount = "<input style='width: 100%;' type='text'  id='maxcount' name='maxcount'/>";
		var subsidyfee = "<input "+fee_check+" style='width: 100%;' type='text'  id='subsidyfee' name='subsidyfee'/>";
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
		var jsonArea = {};
		var areas = new Array();
		if ($("#" + tab + "_area_flag")[0].checked) {
			$("#" + tab + "_area_div>table").each(function() {
				var json = {};
				var areafee = $(this).find("#areafee").val();
				var overbig = new Array();
				$(this).find("[id*=" + tab + "_overbig] tr[id!=thead]").each(function() {
					overbig.push($(this).serializeObject());
				});
				var overweight = new Array();
				$(this).find("[id*=" + tab + "_overweight] tr[id!=thead]").each(function() {
					overweight.push($(this).serializeObject());
				});
				json.areafee = areafee;
				json.overbig = overbig;
				json.overweight = overweight;

				areas.push(json);
			});
			jsonArea = areas;
		}
		return jsonArea;
	}
	function showflag(flag, val) {
		$("#" + flag + "_yes").attr('style', 'display:none');
		$("#" + flag + "_no").attr('style', 'display:none');
		$("#" + flag + "_" + val).removeAttr('style');
	}
	function addArea(tab, areaname, areaid) {
		if ($("tr [id=" + tab + "_" + areaid + "]")[0].style.background == undefined
				|| $("tr [id=" + tab + "_" + areaid + "]")[0].style.background == '') {
			$("tr [id=" + tab + "_" + areaid + "]")[0].style.background = 'yellow';
			var $area_table = $("#area_table").clone();
			$area_table[0].id = tab + "_area_table_" + areaid;
			$area_table.find("#areaid").val(areaid);
			$area_table.find("#areaname").text(areaname);
			var overbig = "overbig" + areaid;
			var overweight = "overweight" + areaid;
			$area_table.find("#overbig_table")[0].id = tab + "_" + overbig + "_table";
			$area_table.find("#overweight_table")[0].id = tab + "_" + overweight + "_table";

			$area_table.find("#overbig_checbox").attr('onclick', 'AllTR("' + tab + '","' + overbig + '")');
			$area_table.find("#overweight_checbox").attr('onclick', 'AllTR("' + tab + '","' + overweight + '")');

			$area_table.find("#overbig_remove").attr('onclick', 'removeTR("' + tab + '","' + overbig + '")');
			$area_table.find("#overweight__remove").attr('onclick', 'removeTR("' + tab + '","' + overweight + '")');

			$area_table.find("#overweight_add").attr('onclick', 'addTROfOverArea("' + tab + '","' + overweight + '")');
			$area_table.find("#overbig_add").attr('onclick', 'addTROfOverArea("' + tab + '","' + overbig + '")');
			$("#" + tab + "_area_div").append($area_table);
		} else if ($("tr [id=" + tab + "_" + areaid + "]")[0].style.background == 'yellow') {
			$("tr [id=" + tab + "_" + areaid + "]")[0].style.background = '';
			$("#" + tab + "_area_div table[id=" + tab + "_area_table_" + areaid + "]").remove();
		}
	}
	function editRule(ruleid) {
		$("#rule_table tr[id!=thead]").each(function() {
			$(this)[0].style.background = '';
		});
		if ($("#" + ruleid)[0].style.background == undefined || $("#" + ruleid)[0].style.background == '') {
			$("#" + ruleid)[0].style.background = 'yellow';
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
	function subEidt(formId, tab, edittype) {
		var dmpurl = $("#dmpurl").val();
		var json = {};
		var ruleid = $("#edit_rule_id").val();
		var typeid = $("#edit_ruletype").val();
		if (edittype == "basic" || edittype == "collection" || edittype == "insertion") {
			var objs = new Array();
			$("#" + formId + " tr[id!=thead]").each(function() {
				objs.push($(this).serializeObject());
				// objs.
			});
			json = objs;
		} else if (edittype == "overarea") {
			if ($("#edit_" + tab + "_state_checkbox")[0].checked) {
				$("#edit_" + tab + "_state").val(1);
			} else {
				$("#edit_" + tab + "_state").val(0);
			}
			json = $("#" + formId).serializeObject();
		} else {
			json = $("#" + formId).serializeObject();
		}
		//alert(JSON.stringify(json));
		$.ajax({
			type : "post",
			url : dmpurl + "/paifeirule/edittype",
			data : {
				"json" : JSON.stringify(json),
				"type" : tab + "_" + edittype,
				"rulejson" : JSON.stringify($("#edit_rule_from").serializeObject())
			},
			dataType : "json",
			success : function(obj) {
				alert(obj.error);
			}
		});
	}