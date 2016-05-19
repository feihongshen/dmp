$(function () {

    //修改预定单panel
    var editReserveOrderPanel = null;
    //关闭预定单panel
    var closeReserveOrderPanel = null;
    //返回总部panel
    var returnToCentralPanel = null;
    //分配站点panel
    var distributeBranchPanel = null;


    //站点快递预约单处理的反馈panel
    var feedBackPanel = null;


    $('#editReserveOrderPanelBtn').click(function () {
        if (checkAtLeastSelectOneRow()) {
            return false;
        }
        var rows = $('#dg_rsList').datagrid('getChecked');
        if(rows.length != 1){
            allertMsg.alertError("请只选择一个预约单");
            return false;
        }

        var selectedRow = rows[0];

        $("#reserveOrderNo4edit").val(selectedRow.reserveOrderNo);
        $('#cnorName4edit').val(selectedRow.cnorName);
        $('#province4edit').val("");
        
        if ($('#province4edit option:contains("' + selectedRow.cnorProvName + '")').length > 0) {
        	
        	//如果匹配到城市，则设置城市的值
        	var $cityOption = $('#city4edit option:contains("' + selectedRow.cnorCityName + '")');
        	if ($cityOption.length > 0) {
        		setSelectValue('city4edit', $cityOption.val()); 
        		$('#city4edit').change();
        		
        		//监听区的数据加载
        		createDetector('county4editDetector',
        			function() {
        				return $('#county4edit option').length > 1;
        			},
        			function() {
        				
        				//如果匹配到区，则设置区的值
        				var $countryOption = $('#county4edit option:contains("' + selectedRow.cnorRegionName + '")');
        				if ($countryOption.length > 0) {
        					setSelectValue('county4edit', $countryOption.val());
        					
        				} else {
        					$('#county4edit').val("");
        				}
        				
        				
        			}, 310);
        		
        	} else {
        		$('#city4edit').val("");
        		$('#county4edit').val("");
        	}
        	
        } else {
        	$('#city4edit').val("");
        	$('#county4edit').val("");
        }
        
        $('#cnorAddr4edit').val(selectedRow.cnorAddr);
        $('#requireTimeStr4edit').val(selectedRow.requireTimeStr);

        //打开条件生成账单面板
        editReserveOrderPanel = $.layer({
            type: 1,
            title: '修改预约单',
            shadeClose: true,
            maxmin: false,
            fix: false,
            area: [500, 300],
            page: {
                dom: '#dialog1'
            }
        });
    });
    
    /**
	 * 设置select框的值
	 * @param {String} id select框的id
	 * @param {String} value 要设置的value
	 */
	function setSelectValue(id, value) {
		var sel = document.getElementById(id);
		if(!sel) return;
		for (var i = 0, len = sel.options.length; i < len; i++) {        
	        if (sel.options[i].value == value) {
				sel.selectedIndex = i;        
	            break;        
	        }        
	    }  
	}
	
	/**
	 * 创建侦察器
	 * @param {String} detectorId 侦察器id，存放到window对象中
	 * @param {Function} conditionFun 执行条件方法，返回false就继续侦查
	 * @param {Function} targetFun 目标方法
	 * @param {Number} interval 侦查时间间隔
	 */
	function createDetector(detectorId ,conditionFun, targetFun, interval) {
		var timmer = window.setInterval(function() {
			//console.log(detectorId + " execute");
			if(conditionFun()) {
				deleteDetector(detectorId);
				targetFun();
			}
		}, interval);
		window[detectorId] = timmer;
	}
	
	/**
	 * 删除侦察器
	 * @param {String} detectorId 侦察器id
	 */
	function deleteDetector(detectorId) {
		if(window[detectorId]) {
			window.clearInterval(window[detectorId]);
		}
	}
    
    $('#deleteReserveOrderBtn').click(function () {
        if (checkAtLeastSelectOneRow()) {
            return false;
        }

        //打开关闭预约单面板
        closeReserveOrderPanel = $.layer({
            type: 1,
            title: '关闭预约单',
            shadeClose: true,
            maxmin: false,
            fix: false,
            area: [250, 300],
            page: {
                dom: '#dialog2'
            }
        });
    });
    $('#returnToCentralBtn').click(function () {
        if (checkAtLeastSelectOneRow()) {
            return false;
        }
        var rows = $('#dg_rsList').datagrid('getChecked');
        
        var flag = true;
        $.each(rows, function (index, value) {
        	var reserveOrderStatus = value.reserveOrderStatus;
        	if(reserveOrderStatus != hadAllocationPro && reserveOrderStatus != haveStationOutZone) {
        		flag = false;
        		return false;
        	}
        });
        if(!flag) {
        	allertMsg.alertError("选中的预约单无法做退回总部操作！");
        	return false;
        }
        //打开退回总部面板
        returnToCentralPanel = $.layer({
            type: 1,
            title: '退回总部',
            shadeClose: true,
            maxmin: false,
            fix: false,
            area: [250, 300],
            page: {
                dom: '#dialog3'
            }
        });
    });
    $('#distributeBranchBtn').click(function () {
        if (checkAtLeastSelectOneRow()) {
            return false;
        }
        var rows = $('#dg_rsList').datagrid('getChecked');
        
        var flag = true;
        $.each(rows, function (index, value) {
        	var reserveOrderStatus = value.reserveOrderStatus;
        	if(reserveOrderStatus != hadAllocationPro  
        			&& reserveOrderStatus !=hadAllocationStation 
        			&& reserveOrderStatus != haveStationOutZone) {
        		flag = false;
        		return false;
        	}
        });
        if(!flag) {
        	allertMsg.alertError("选中的预约单无法做分配站点操作！");
        	return false;
        }
        if (rows.length == 1) {
            $('#distributeBranchSelect option:selected').removeAttr('selected');
            $('#distributeBranchSelect option').each(function () {
                if ($(this).text() == rows[0].acceptOrgName) {
                    $(this).attr('selected', 'selected');
                    return false;
                }
            });

            $('#distributeCourierSelect option:selected').removeAttr('selected');
            $('#distributeCourierSelect option').each(function () {
                if ($(this).text() == rows[0].courierName) {
                    $(this).attr('selected', 'selected');
                    return false;
                }
            });
        } else {
            $('#distributeBranchSelect option:selected').removeAttr('selected');
            $('#distributeCourierSelect option:selected').removeAttr('selected');
        }

        //打开退回总部面板
        distributeBranchPanel = $.layer({
            type: 1,
            title: '分配站点',
            shadeClose: true,
            maxmin: false,
            fix: false,
            area: [300, 300],
            page: {
                dom: '#dialog4'
            }
        });
    });
    $('#feedbackBtn').click(function () {
        if (checkAtLeastSelectOneRow()) {
            return false;
        }
        var rows = $('#dg_rsList').datagrid('getChecked');
        if (rows.length == 1) {
            var selectedRow = rows[0];
            $("#reserveOrderNo4Feedback").val(selectedRow.reserveOrderNo);
            $('#courierName4Feedback').val(selectedRow.cnorName);
            $('#requireTimeStr4Feedback').val(selectedRow.requireTimeStr);
        } else {
            $("#reserveOrderNo4Feedback").val("");
            $('#courierName4Feedback').val("");
            $('#requireTimeStr4Feedback').val("");
        }

        $('#type4Feedback').val("");
        $('#reason4Feedback').val("");
        $('#cnorRemark4Feedback').val("");

        //打开条件生成账单面板
        feedBackPanel = $.layer({
            type: 1,
            title: '反馈',
            shadeClose: true,
            maxmin: false,
            fix: false,
            area: [400, 500],
            page: {
                dom: '#dialog2'
            }
        });
    });


    $('#confirmEditReserveOrderBtn').click(function () {
        confirmEditReserveOrder();
    });
    $('#confirmCloseReserveOrderBtn').click(function () {
        confirmCloseReserveOrder();
    });
    $('#confirmReturnToCentralBtn').click(function () {
        confirmReturnToCentral();
    });
    $('#confirmDistributeBranchBtn').click(function () {
        confirmDistributeBranch();
    });
    $('#confirmFeedbackBtn').click(function () {
        confirmFeedback();
    });

    $('#closeCloseReserveOrderPanel').click(function () {
        closePanel(closeReserveOrderPanel);
    });
    $('#closeEditReserveOrderPanel').click(function () {
        closePanel(editReserveOrderPanel);
    });
    $('#closeDistributeBranchPanel').click(function () {
        closePanel(distributeBranchPanel);
    });
    $('#closeReturnToCentralPanel').click(function () {
        closePanel(returnToCentralPanel);
    });
    $('#closeFeedbackPanel').click(function () {
        closePanel(feedBackPanel);
    });

    function confirmEditReserveOrder() {
        var rows = $('#dg_rsList').datagrid('getChecked');

        var param = {
            reserveOrderNo : rows[0].reserveOrderNo,
            cnorName4edit : $('#cnorName4edit').val(),
            province4edit : $('#province4edit').val(),
            city4edit : $('#city4edit').val(),
            cityName4edit : $('#city4edit option:selected').text(),
            county4edit : $('#county4edit').val(),
            countyName4edit : $('#county4edit  option:selected').text(),
            cnorAddr4edit : $('#cnorAddr4edit').val(),
            requireTimeStr4edit : $('#requireTimeStr4edit').val()
        };

        $.ajax({
            type: "POST",
            url: contextPath + "/express2/reserveOrder/editReserveOrder",
            dataType: "json",
            data: param,
            success: function (data) {
                if (data.errorinfo){
                    allertMsg.alertError(data.errorinfo);
                    return;
                }
                $('#dg_rsList').datagrid('reload');
                $("#reserveOrderNo4edit").val("");
                $('#cnorName4eidt').val("");
                //$('#province4edit').val("");
                $('#city4edit').val("");
                $('#county4edit').val("");
                $('#cnorAddr4edit').val("");
                $('#requireTimeStr4edit').val("");
                closePanel(editReserveOrderPanel);
            }
        });
    }
    function confirmCloseReserveOrder() {
        var rows = $('#dg_rsList').datagrid('getChecked');

        var param = [];

        $.each(rows, function (index, value) {
            var reserveOrder = {};
            reserveOrder.reserveOrderNo = value.reserveOrderNo;
            reserveOrder.recordVersion = value.recordVersion;
            reserveOrder.reason = $('#closeReason').val()
            param.push(reserveOrder);
        });


        $.ajax({
            type: "POST",
            url: contextPath + "/express2/reserveOrder/closeReserveOrder",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(param)  ,
            success: function (data) {
                if (data.errorMsg){
                    allertMsg.alertError(data.errorMsg);
                }
                $('#dg_rsList').datagrid('reload');
                $('#closeReason').val("");
                closePanel(closeReserveOrderPanel);
            }
        });
    }
    function confirmReturnToCentral() {
        var rows = $('#dg_rsList').datagrid('getChecked');

        var param = [];

        $.each(rows, function (index, value) {
            var reserveOrder = {};
            reserveOrder.reserveOrderNo = value.reserveOrderNo;
            reserveOrder.recordVersion = value.recordVersion;
            reserveOrder.operateType = returnType;
            reserveOrder.reason = $('#returnReason').val();
            param.push(reserveOrder);
        });

        $.ajax({
            type: "POST",
            url: contextPath + "/express2/reserveOrder/returnToCentral",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(param)  ,
            success: function (data) {
                if (data.errorMsg){
                    allertMsg.alertError(data.errorMsg);
                }
                $('#dg_rsList').datagrid('reload');
                $('#returnReason').val("");
                closePanel(returnToCentralPanel);
            }
        });
    }

    function confirmDistributeBranch() {
        var rows = $('#dg_rsList').datagrid('getChecked');

        var param = [];

        var distributeBranch ;
        if($('#distributeBranchSelect').val()){
            distributeBranch = $('#distributeBranchSelect').val();
        }else {
            distributeBranch = "";
        }

        var distributeCourier = $('#distributeCourierSelect').val();

        $.each(rows, function (index, value) {
            var reserveOrder = {};
            reserveOrder.reserveOrderNo = value.reserveOrderNo;
            reserveOrder.recordVersion = value.recordVersion;
            reserveOrder.acceptOrg = distributeBranch;
            reserveOrder.courier = distributeCourier;
            param.push(reserveOrder);
        });

        $.ajax({
            type: "POST",
            url: contextPath + "/express2/reserveOrder/distributeBranch",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(param)  ,
            success: function (data) {
                if (data.errorMsg){
                    allertMsg.alertError(data.errorMsg);
                }
                $('#dg_rsList').datagrid('reload');
                $('#distributeBranch').val("");
                $('#distributeCourier').val("");
                closePanel(distributeBranchPanel);
            }
        });
    }
    function confirmFeedback() {
        var rows = $('#dg_rsList').datagrid('getChecked');

        //var reserveOrderNos = [] ;
        //$.each(rows, function(index, value){
        //    reserveOrderNos.push(value.reserveOrderNo);
        //});

        //var param = {
        //    reserveOrderNos : reserveOrderNos.join(","),
        //    optCode4Feedback : $('#optCode4Feedback').val(),
        //    reason4Feedback : $('#reason4Feedback').val(),
        //    cnorRemark4Feedback : $('#cnorRemark4Feedback').val(),
        //    requireTimeStr4Feedback : $('#requireTimeStr4Feedback').val()
        //};

        var param = [];

        $.each(rows, function (index, value) {
            var reserveOrder = {};
            reserveOrder.reserveOrderNo = value.reserveOrderNo;
            reserveOrder.recordVersion = value.recordVersion;
            reserveOrder.operateType =  $('#optCode4Feedback').val();
            reserveOrder.reason = $('#reason4Feedback').val();
            reserveOrder.cnorRemark = $('#cnorRemark4Feedback').val();
            reserveOrder.requireTimeStr = $('#requireTimeStr4Feedback').val();
            param.push(reserveOrder);
        });


        $.ajax({
            type: "POST",
            url: contextPath + "/express2/reserveOrder/feedback",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(param),
            success: function (data) {
                if (data.errorMsg){
                    allertMsg.alertError(data.errorMsg);
                }
                $('#dg_rsList').datagrid('reload');
                $("#reserveOrderNo4Feedback").val("");
                $('#courierName4Feedback').val("");
                $('#optCode4Feedback').val("");
                $('#reason4Feedback').val("");
                $('#requireTimeStr4Feedback').val("");
                $('#cnorRemark4Feedback').val("");
                closePanel(feedBackPanel);
            }
        });
    }

});

