/**
 * 预约单号格式化
 * @param {Object} value the field value.
 * @param {Object} row the row record data.
 * @param {Object} index the row index.
 */
function reserveOrderNoFormatter(value, row, index) {
	return '<a href="javascript:reserveOrderNoOnClick(\'' + value + '\')" >'+ value + '</a>';
}

/**
 * 预约单号点击事件
 * @param {String} reserveOrderNo
 */
function reserveOrderNoOnClick(reserveOrderNo) {
	alert(reserveOrderNo);
}

$(function() {
	$("#cnorProv").change(function () {
        changeCounty($(this).val(), "#cnorCity");
    })
});

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