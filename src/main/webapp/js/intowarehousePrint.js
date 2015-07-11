var LODOP; //声明为全局变量 

/**
 * 根据后台获取打印订单相关信息
 * @param scancwb
 */
var cwbDetail = {};
function printIntowarehouse(scancwb){
	if(scancwb.length>0){
		$.ajax({
			type: "POST",
			url: App.ctx + "/PDA/cwbscancwbbranchnew1",
			data: {"cwb":scancwb},
			dataType:"json",
			success : function(data) {
				if(data.statuscode=="000000"){
					
					var branchname= data.body.cwbOrder.excelbranch;
					if(data.body.cwbOrder.excelbranch.length>4){
						branchname= data.body.cwbOrder.excelbranch.substring(0,4);
					}
					$("#branchcode").html(data.body.branchcode);//站点分拣编码
					var len=data.body.branchcode.length;
					var length=100-Math.floor(len/2)+3+"mm";
					var length2=45-len;
					
					var width = "50mm";
					var height = "16mm";
					var font_family = "黑体";
					var font = "bolder";
					var font_size = "16mm";
					var line_height = "12mm";
					var font_weight = "bolder";
					var display = "block";
					var word_wrap = "break-word";
					if(len==4){
						width = "40mm";
						height = "16mm";
						font_family = "黑体";
						font = "bolder";
						font_size = "14mm";
						line_height = "14mm";
						font_weight = "bolder";
						display = "block";
						word_wrap = "break-word";
					}else if(len==5){
						width = "40mm";
						height = "16mm";
						font_family = "黑体";
						font = "bolder";
						font_size = "14mm";
						line_height = "14mm";
						font_weight = "bolder";
						display = "block";
					}else if(len==6){
						width = "40mm";
						height = "16mm";
						font_family = "黑体";
						font = "bolder";
						font_size = "10mm";
						line_height = "10mm";
						font_weight = "bolder";
						display = "block";
					}else if(len==7||len==8||len==9){
						width = "40mm";
						height = "16mm";
						font_family = "黑体";
						font = "bolder";
						font_size = "9mm";
						line_height = "9mm";
						font_weight = "bolder";
						display = "block";
						word_wrap = "break-word";
					}else if(len==10||len==11||len==12){
						width = "40mm";
						height = "16mm";
						font_family = "黑体";
						font = "bolder";
						font_size = "8mm";
						line_height = "9mm";
						font_weight = "bolder";
						display = "block";
						word_wrap = "break-word";
					}else if(len==13||len==14||len==15){
						width = "40mm";
						height = "16mm";
						font_family = "黑体";
						font = "bolder";
						font_size = "6mm";
						line_height = "9mm";
						font_weight = "bolder";
						display = "block";
						word_wrap = "break-word";
					}else if(len==16||len==17||len==18){
						width = "40mm";
						height = "16mm";
						font_family = "黑体";
						font = "bolder";
						font_size = "8mm";
						line_height = "6mm";
						font_weight = "bolder";
						display = "block";
						word_wrap = "break-word";
					}else if(len==19||len==20||len==21){
						width = "40mm";
						height = "16mm";
						font_family = "黑体";
						font = "bolder";
						font_size = "6mm";
						line_height = "6mm";
						font_weight = "bolder";
						display = "block";
						word_wrap = "break-word";
					}else{
						line_height = "20mm";
					}
					
					var printHtml = 
					"<table border='0' style=\"width:50mm;height:30mm; display: table; \"  cellspacing=\"0\" cellpadding=\"0\" >"+
					"<tr><td style=\"margin: -20;padding: -20;display: table-cell;\">"+
					"<div style=\"font-size:10mm ;font-family:黑体;height: 3mm;line-height: 12mm;font-weight:bolder;\" align=\"center\" id=\"branchname\">"+ branchname  + "</div>" +
					"</td></tr><tr style=\"width:50mm;height: 20mm;\"><td style=\"padding:-20;margin:-20 ;width:50mm;height: 16mm;\">"+
					"<div style=\"width:"+ width +";height: " + height + ";font-family:" + font_family +"; font: " + font + ";font-size: " + font_size + ";font-weight: " + font_weight + ";" +
					"display: " + display + ";word-wrap: " + word_wrap + "; line-height: " + line_height + ";\" align=\"center\" id=\"branchcode\">" + data.body.branchcode + "</div>"+
					"</td></tr><tr><td style=\"padding: -20;margin: -20;display: table-cell;\">"+
					"<div style=\"font: bolder;font-size: 3mm;height: 4mm;line-height: 2mm;\" align=\"center\" id=\"transcwb\">" + scancwb + "</div>" + 
					"</td></tr></table>";
					
					prn1_print(scancwb,printHtml);
				}
			}
		});
	}
}
/**
 * 创建打印页面方法
 * @param scancwb
 */
function CreateOneFormPage(scancwb,printHtml){

	LODOP=getLodop(App.ctx,document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP.NEWPAGE();
	LODOP.PRINT_INIT("订单打印");
	LODOP.SET_PRINT_STYLE("Bold",1);
	LODOP.ADD_PRINT_TABLE(10,10,500,300,printHtml);
	
};
/**
 * 直接打印方法
 * @param scancwb
 */
function prn1_print(scancwb,printHtml) {		
	CreateOneFormPage(scancwb,printHtml);
	LODOP.PRINT();
};