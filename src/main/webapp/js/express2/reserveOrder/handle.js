$(function () {
    //汇总选中账单信息
    $('#dg').datagrid({});


    $('#city').change(function () {
        changeCounty($(this).val(), "#county");
    })

    $('#city4edit').change(function () {
        changeCounty($(this).val(), "#county4edit");
    })

    $('#branch').change(function () {
        changeCourier($(this).val(), "#courier");
    })

    $('#distributeBranchSelect').change(function () {
        changeCourier($(this).val(), "#distributeCourierSelect");
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

    function changeCounty(cityId, changedItem) {
        var countySelect = $(changedItem);
        if (cityId && cityId.length > 0) {
            //var citySelect;
            //citySelect = $('#sender_provinceid_id");

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


    function changeCourier(branchId, changedItem) {
        var courierSelect = $(changedItem);
        if (branchId && branchId.length > 0) {
            //var citySelect;
            //citySelect = $('#sender_provinceid_id");

            //var provinceCode = provinceSelect.find("option:selected").attr("code");

            $.ajax({
                type: "POST",
                url: contextPath + "/express2/reserveOrder/getCourierByBranch",
                dataType: "json",
                data: {
                    "branchId": branchId
                },
                success: function (data) {
                    var courierList = data.courierList;

                    courierSelect.empty();
                    courierSelect.get(0).add(new Option("请选择", ""));
                    for (var i = 0; i < courierList.length; i++) {
                        courierSelect.get(0).add(new Option(courierList[i].username, courierList[i].userid));
                    }
                }
            });
        } else {
            courierSelect.empty();
        }
    }

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

