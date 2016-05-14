$(function () {
    //汇总选中账单信息
    $('#dg').datagrid({});

    //定义条件生成账单和导入生成账单 panel
    var editPreOderPanel = null;

    $("#city").change(function () {
        changeCounty($(this).val(), "#county");
    })

    $("#city4edit").change(function () {
        changeCounty($(this).val(), "#county4edit");
    })

    $("#branch").change(function () {
        changeKDY($(this).val());
    })

    $("#openEditPreOderPanelBtn").click(function () {
        openEditPreOderPanel();
    })

    $("#confirmEditPreOrderBtn").click(function () {
        confirmEditPreOrder();
    })
    $("#closeEditPreOrderPanel").click(function () {
        closeEditPreOderPanel();
    })

    function changeCounty(cityId, changedItem) {
        var countySelect = $(changedItem);
        if (cityId && cityId.length > 0) {
            //var citySelect;
            //citySelect = $("#sender_provinceid_id");

            //var provinceCode = provinceSelect.find("option:selected").attr("code");

            $.ajax({
                type: "POST",
                url: "getCountyByCity",
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


    function changeKDY(branchId) {
        var kdySelect = $("#kdy");
        if (branchId && branchId.length > 0) {
            //var citySelect;
            //citySelect = $("#sender_provinceid_id");

            //var provinceCode = provinceSelect.find("option:selected").attr("code");

            $.ajax({
                type: "POST",
                url: "getCourierByBranch",
                dataType: "json",
                data: {
                    "branchId": branchId
                },
                success: function (data) {
                    var kdyList = data.kdyList;

                    kdySelect.empty();
                    for (var i = 0; i < kdyList.length; i++) {
                        kdySelect.get(0).add(new Option(kdyList[i].username, kdyList[i].userid));
                    }
                }
            });
        } else {
            kdySelect.empty();
        }
    }


    //打开条件生成账单面板
    function openEditPreOderPanel() {
        editPreOderPanel = $.layer({
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
    }

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

    function closeEditPreOderPanel() {
        if (editPreOderPanel)
            layer.close(editPreOderPanel)
    }
})

