var LODOP_wuhan; //声明为全局变量 

/**
 * 根据后台获取打印订单相关信息
 * @param scancwb
 */
var cwbDetail = {};
function printIntowarehousewuhan(scancwb){
	if(scancwb.length>0){
		var url = App.ctx + "/PDA/querycwbbranchcode";
		$.ajax({
			type: "POST",
			url: url,
			data: {"cwb":scancwb},
			dataType:"json",
			success : function(data) {
				if(data.statuscode=="000000"){
					
					var branchname = data.body.branch.branchname;
					var branchcode = data.body.branch.branchcode;
					
					var deliveryuser = data.body.deliveryuser;
					
					var cwbfontSize = 4;
					if (scancwb.length > 14) {
						cwbfontSize = 3;
					}
					var branchfontSize = 6;
					if (branchname.length > 4) {
						branchfontSize = 5
					}
					var printHtml = "<table border='0' style=\"width: 35mm; height: 30mm; display: table;\" cellspacing=\"0\" valign=\"TOP\" cellpadding=\"0\">"+
		                              "<tr><td style='word-break: keep-all;white-space:nowrap;'><div style=\"font-family:黑体; font:bolder;font-size:14mm;font-weight:14mm;\" align=\"center\" id=\"branchcode\">"+ branchcode + "</div></td></tr>"+
	                                  "<tr><td style='word-break: keep-all;white-space:nowrap;'><div style=\"font-family:黑体; font:bolder;font-size:"+branchfontSize+"mm;font-weight:"+branchfontSize+"mm;\" align=\"center\">" + branchname+ "</div></td></tr>"+
		                              "<tr><td style='word-break: keep-all;white-space:nowrap;'><div style=\"font-family:黑体; font:bolder;font-size:"+cwbfontSize+"mm;font-weight:"+cwbfontSize+"mm;\" align=\"center\">"+ scancwb +"</div></td></tr>"+
		                              "<tr><td style='word-break: keep-all;white-space:nowrap;'><div style=\"font-family:黑体; font:bolder;font-size:4mm;font-weight:4mm;\" align=\"center\">"+ deliveryuser +"</div></td></tr></table>";
					prn1_printForwuhan(scancwb,printHtml);
				}
			}
		});
	}
}
/**
 * 创建打印页面方法
 * @param scancwb
 */
function CreateOneFormPageForwuhan(scancwb,printHtml){
	LODOP_wuhan=getLodop(App.ctx,document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP_wuhan.NEWPAGE();
	LODOP_wuhan.PRINT_INIT("订单打印");
	LODOP_wuhan.SET_PRINT_STYLE("Bold",1);
	LODOP_wuhan.ADD_PRINT_TABLE(0,0,200,150,printHtml);
};
/**
 * 直接打印方法
 * @param scancwb
 */
function prn1_printForwuhan(scancwb,printHtml) {		
	CreateOneFormPageForwuhan(scancwb,printHtml);
	LODOP_wuhan.PRINT();  
	//LODOP_wuhan.PREVIEW(); //打印预览
};