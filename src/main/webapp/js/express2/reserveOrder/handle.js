$(function () {
    $("#city4edit").change(function () {
        changeCounty($(this).val(), "#county4edit");
    })

    //修改预定单panel
    var editPreOrderPanel = null;
    //关闭预定单panel
    var closePreOrderPanel = null;
    //返回总部panel
    var returnToCentralPanel = null;
    //分配站点panel
    var distributeBranchPanel = null;

    $("#editPreOrderPanelBtn").click(function () {
        //打开条件生成账单面板
        editPreOrderPanel = $.layer({
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
    $("#deletePreOrderBtn").click(function () {
        //打开关闭预约单面板
        closePreOrderPanel = $.layer({
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
    $("#returnToCentrelBtn").click(function () {
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
    $("#distributeBranchBtn").click(function () {
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

    $("#confirmEditPreOrderBtn").click(function () {
        confirmEditPreOrder();
    })
    $("#closeEditPreOrderPanel").click(function () {
        closePanel(editPreOrderPanel);
    })

    $("#closeClosePreOrderPanel").click(function () {
        closePanel(closePreOrderPanel);
    })

    $("#closeDistributeBranchPanel").click(function () {
        closePanel(distributeBranchPanel);
    })

    function confirmEditPreOrder() {

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

    function closePanel(panel) {
        if (panel)
            layer.close(panel)
    }
})

