$(function() {
	$("#cnorProv").change(function () {
        changeCounty($(this).val(), "#cnorCity");
    });
	
	$("#acceptOrg").change(function () {
        changeCourier($(this).val(), "#courier");
    });

    $("#distributeBranchSelect").change(function () {
        changeCourier($(this).val(), "#distributeCourierSelect");
    });

    $("#province4edit").change(function () {
        changeCity($(this).val(), "#city4edit");
    });

    $("#city4edit").change(function () {
        changeCounty($(this).val(), "#county4edit");
    })
});

/**
 * 预约单号格式化
 * @param {Object} value the field value.
 * @param {Object} row the row record data.
 * @param {Object} index the row index.
 */
function reserveOrderNoFormatter(value, row, index) {
	return '<a href="javascript:void(0);" onclick="reserveOrderNoOnClick(event,\'' + value + '\')" >'+ value + '</a>';
}

/**
 * 预约单号点击事件
 * @param {String} reserveOrderNo
 */
function reserveOrderNoOnClick(event, reserveOrderNo) {
	var htmlContent = 
		'<div style="width:800px; height:300px; padding:20px; border:1px solid #ccc; background-color:#eee;">' +
			'<table id="reserveOrderLogGrid"></table>' +
		'</div>';
	
	$.layer({
	    type: 1,
	    title: '<span style="font-size:16px;font-weight:bold;">预约单轨迹(' + reserveOrderNo + ')</span>',
	    area: ['auto', 'auto'],
	    border: [0], //去掉默认边框
	    shade: [0.5, '#000'], //去掉遮罩
	    shadeClose: true,
	    closeBtn: [0, true], //去掉默认关闭按钮
	    page: {
	        html: htmlContent
	    }
	});
	
	$('#reserveOrderLogGrid').datagrid({
		url: contextPath + '/express2/reserveOrder/queryReserveOrderLog?reserveOrderNo=' + reserveOrderNo,
		fit: true,
		fitColumns: false,
		singleSelect: true,
		rownumbers: true,
	    columns:[[
	        {field:'id', title:'预约单id', hidden:true},
	        {field:'reserveOrderNo', title:'预约单号', hidden:true},
	        {field:'operateTimeStr', title:'操作时间', width:130, align:'center'},
	        {field:'operator', title:'操作人', width:130, align:'center'},
	        {field:'operateType', title:'操作类型', width:100, align:'center'},
	        {field:'trackDetail', title:'物流状态信息', align:'center'}
	    ]]
	});
	event.stopPropagation();
	return false;
}


function doSearch() {
	//查询list
	$('#dg_rsList').datagrid('load',{
		reserveOrderNo:$("#reserveOrderNo").val(),
		appointTimeStart:$("#appointTimeStart").val(),
		appointTimeEnd:$("#appointTimeEnd").val(),
		cnorProv:$("#cnorProv").val(),
		cnorCity:$("#cnorCity").val(),
		cnorMobile:$("#cnorMobile").val(),
		acceptOrg:$("#acceptOrg").val(),
		courier:$("#courier").val(),
		reserveOrderStatusList:$("#reserveOrderStatusList").val()
	});
}

function changeCounty(cityId, changedItem) {
    var countySelect = $(changedItem);
    if (cityId && cityId.length > 0) {
        //var citySelect;
        //citySelect = $("#sender_provinceid_id");

        //var provinceCode = provinceSelect.find("option:selected").attr("code");

        $.ajax({
            type: "POST",
            url: contextPath + "/express2/reserveOrder/getCountyByCity",
            dataType: "json",
            data: {
                "cityId": cityId
            },
            success: function (data) {
                var countyList = data.countyList;

                countySelect.empty();
                countySelect.get(0).add(new Option("区/县", ""));
                for (var i = 1; i < countyList.length; i++) {
                    countySelect.get(0).add(new Option(countyList[i].name, countyList[i].id));
                }
            }
        });
    } else {
        countySelect.empty();
        countySelect.get(0).add(new Option("区/县", ""));
    }
}
    
function changeCourier(acceptOrg,changedItem) {
	var courierSelect = $(changedItem);
    if (acceptOrg && acceptOrg.length > 0) {
        //var citySelect;
        //citySelect = $('#sender_provinceid_id");

        //var provinceCode = provinceSelect.find("option:selected").attr("code");

        $.ajax({
            type: "POST",
            url: contextPath + "/express2/reserveOrder/getCourierByBranch",
            dataType: "json",
            data: {
                "branchId": acceptOrg
            },
            success: function (data) {
                var courierList = data.courierList;

                courierSelect.empty();
                courierSelect.get(0).add(new Option("请选择", ""));
                for (var i = 0; i < courierList.length; i++) {
                    courierSelect.get(0).add(new Option(courierList[i].realname, courierList[i].userid));
                }
            }
        });
    } else {
        courierSelect.empty();
        courierSelect.get(0).add(new Option("请选择", ""));
    }
}
