$(function () {
    $("#city4edit").change(function () {
        changeCounty($(this).val(), "#county4edit");
    })



    //修改预定单panel
    var editReserveOrderPanel = null;
    //关闭预定单panel
    var closeReserveOrderPanel = null;
    //返回总部panel
    var returnToCentralPanel = null;
    //分配站点panel
    var distributeBranchPanel = null;

    $('#editReserveOrderPanelBtn').click(function () {
        if (checkAtLeastSelectOneRow()) {
            return false;
        }

        //打开条件生成账单面板
        editReserveOrderPanel = $.layer({
            type: 1,
            title: '修改预约单',
            shadeClose: true,
            maxmin: false,
            fix: false,
            area: [400, 300],
            page: {
                dom: '#dialog1'
            }
        });
    })
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
    })
    $('#returnToCentralBtn').click(function () {
        if (checkAtLeastSelectOneRow()) {
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
    })


    $('#distributeBranchBtn').click(function () {
        if (checkAtLeastSelectOneRow()) {
            return false;
        }
        var rows = $('#dg').datagrid('getChecked');

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
    })

    $('#confirmEditReserveOrderBtn').click(function () {
        confirmEditReserveOrder();
    })
    $('#closeEditReserveOrderPanel').click(function () {
        closePanel(editReserveOrderPanel);
    })
    $('#confirmCloseReserveOrderBtn').click(function () {
        confirmCloseReserveOrder();
    })
    $('#closeCloseReserveOrderPanel').click(function () {
        closePanel(closeReserveOrderPanel);
    })

    $('#closeDistributeBranchPanel').click(function () {
        closePanel(distributeBranchPanel);
    })

    function confirmEditReserveOrder() {

        var param = {}

        $.ajax({
            type: "POST",
            url: "",
            dataType: "json",
            data: param,
            success: function (data) {

            }
        });
    }

    function confirmCloseReserveOrder() {
        var rows = $('#dg').datagrid('getChecked');

        var reserveOrderNos = [] ;
        $.each(rows, function(index, value){
            reserveOrderNos.push(value.reserveOrderNo);
        })

        var param = {
            reserveOrderNos : reserveOrderNos.join(",") ,
            closeReason : $('#closeReason').val()
        }

        $.ajax({
            type: "POST",
            url: contextPath + "/express2/reserveOrder/closeReserveOrder",
            dataType: "json",
            data: param,
            success: function (data) {
                $('#dg').datagrid('reload');
                $('#closeReason').val("");
                closePanel(closeReserveOrderPanel);
            }
        });
    }

    function checkAtLeastSelectOneRow() {
        var rows = $('#dg').datagrid('getChecked');
        if (!rows || rows.length < 1) {
            allertMsg.alertError("请选择预约单");
            return true;
        }
    }

    function closePanel(panel) {
        if (panel)
            layer.close(panel)
    }

    var allertMsg = {
        /**
         * 成功提示
         * @param msg  提示信息
         */
        "msgOk": function (msg) {
            layer.msg(msg || "Error", 1, 1);
        }
        /**
         * 弹出错误信息
         * @param msg  提示信息
         */
        , "msgError": function (msg) {
            layer.msg(msg || "Error", 1, 3);
        }
        /**
         * 弹出成功提示
         * @param msg  提示信息
         */
        , "alertOk": function (msg) {
            layer.alert(msg || "OK", 1);
        }
        /**
         * 弹出错误提示
         * @param msg  提示信息
         */
        , "alertError": function (msg) {
            layer.alert(msg || "Error", 3);
        }
    }
})

