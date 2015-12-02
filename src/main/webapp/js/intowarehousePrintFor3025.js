var LODOP_3025; //声明为全局变量 

/**
 * 根据后台获取打印订单相关信息
 * @param scancwb
 */
var cwbDetail = {};
function printIntowarehouse3025(scancwb){
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
					
					
					var width = "25mm";
					var height = "10mm";
					var font_family = "黑体";
					var font = "bolder";
					var font_size = "10mm";
					var line_height = "10mm";
					var font_weight = "bolder";
					var display = "block";
					var word_wrap = "break-word";
					if(len==3){
						width = "25mm";
						height = "10mm";
						font_family = "黑体";
						font = "bolder";
						font_size = "10mm";
						line_height = "10mm";
						font_weight = "bolder";
						display = "block";
						word_wrap = "break-word";
					}else
					if(len==4){
						width = "25mm";
						height = "8mm";
						font_family = "黑体";
						font = "bolder";
						font_size = "8mm";
						line_height = "8mm";
						font_weight = "bolder";
						display = "block";
						word_wrap = "break-word";
					}else if(len==5){
						width = "25mm";
						height = "6mm";
						font_family = "黑体";
						font = "bolder";
						font_size = "6mm";
						line_height = "6mm";
						font_weight = "bolder";
						display = "block";
					}else if(len==6){
						width = "25mm";
						height = "6mm";
						font_family = "黑体";
						font = "bolder";
						font_size = "6mm";
						line_height = "6mm";
						font_weight = "bolder";
						display = "block";
					}else if(len==7||len==8||len==9){
						width = "25mm";
						height = "10mm";
						font_family = "黑体";
						font = "bolder";
						font_size = "8mm";
						line_height = "8mm";
						font_weight = "bolder";
						display = "block";
						word_wrap = "break-word";
					}else if(len==10||len==11||len==12){
						width = "25mm";
						height = "10mm";
						font_family = "黑体";
						font = "bolder";
						font_size = "8mm";
						line_height = "8mm";
						font_weight = "bolder";
						display = "block";
						word_wrap = "break-word";
					}else if(len==13||len==14||len==15){
						width = "25mm";
						height = "10mm";
						font_family = "黑体";
						font = "bolder";
						font_size = "6mm";
						line_height = "6mm";
						font_weight = "bolder";
						display = "block";
						word_wrap = "break-word";
					}else if(len==16||len==17||len==18){
						width = "25mm";
						height = "10mm";
						font_family = "黑体";
						font = "bolder";
						font_size = "6mm";
						line_height = "6mm";
						font_weight = "bolder";
						display = "block";
						word_wrap = "break-word";
					}else if(len==19||len==20||len==21){
						width = "25mm";
						height = "10mm";
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
					"<table border='0' style=\"width:25mm;height:20mm; display: table; \"  cellspacing=\"0\" valign=\"TOP\" cellpadding=\"0\" >"+
					"<tr><td style=\"margin: -15,0,0,-20;padding: -20,0,0,-20;display: table-cell;\">"+
					"<div style=\"font-size:5mm ;font-family:黑体;height: 3mm;line-height: 6mm;font-weight:bolder;\" align=\"center\" id=\"branchname\">"+ branchname  + "</div>" +
					"</td></tr><tr style=\"width:30mm;height: 10mm;\"><td style=\"padding:-30,0,0,-20;margin:-25,0,0,-20;width:30mm;height: 10mm;\">"+
					"<div style=\"width:"+ width +";height: " + height + ";font-family:" + font_family +"; font: " + font + ";font-size: " + font_size + ";font-weight: " + font_weight + ";" +
					"display: " + display + ";word-wrap: " + word_wrap + "; line-height: " + line_height + ";\" align=\"center\" id=\"branchcode\">" + data.body.branchcode + "</div>"+
					"</td></tr><tr><td style=\"padding: -30,0,0,-20;margin: -25,0,0,-20;display: table-cell;\">"+
					"<div style=\"font: bolder;font-size: 3mm;height: 4mm;line-height: 2mm;\" align=\"center\" id=\"transcwb\">" + scancwb + "</div>" + 
					"</td></tr></table>";
					
					prn1_printFor3025(scancwb,printHtml);
				}
			}
		});
	}
}
/**
 * 创建打印页面方法
 * @param scancwb
 */
function CreateOneFormPageFor3025(scancwb,printHtml){

	LODOP_3025=getLodop(App.ctx,document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP_3025.NEWPAGE();
	LODOP_3025.PRINT_INIT("订单打印");
	LODOP_3025.SET_PRINT_STYLE("Bold",1);
	LODOP_3025.ADD_PRINT_TABLE(0,0,200,150,printHtml);
	
};
/**
 * 直接打印方法
 * @param scancwb
 */
function prn1_printFor3025(scancwb,printHtml) {		
	CreateOneFormPageFor3025(scancwb,printHtml);
	LODOP_3025.PRINT();
};