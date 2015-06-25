$(function() {
	var content;
	$("#content td").click(function() {
		var clickObj = $(this);
		content = clickObj.html();
		changeToEdit(clickObj);
	});
	function changeToEdit(node) {
		node.html("");
		var inputObj = $("<input type='text'/>");
		inputObj.css("border", "0").css("background-color",
				node.css("background-color")).css("font-size",
				node.css("font-size")).css("height", "20px").css("width",
				node.css("width")).val(content).appendTo(node).get(0).select();
		inputObj.click(function() {
			return false;
		}).keyup(function(event) {
			var keyvalue = event.which;
			if (keyvalue == 13) {
				node.html(node.children("input").val());
			}
			if (keyvalue == 27) {
				node.html(content);
			}
		}).blur(function() {
			node.html(node.children("input").val());
//			if (node.children("input").val() != content) {
//				if (confirm("是否保存修改的内容？", "Yes", "No")) {
//					node.html(node.children("input").val());
//				} else {
//					node.html(content);
//				}
//			} else {
//				node.html(content);
//			}
		});
	}
});